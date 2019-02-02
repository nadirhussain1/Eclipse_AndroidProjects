package br.com.utilities;

import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;
import br.com.global.SharedData;


public class ScalingUtility {
	private  double standardWidth =720;
	private  double standardHeight = 1134;
	private  double standardDensity = 2.0;

	private double widthRatio = 0;
	private double heightRatio = 0;
	private double aspectRatioFactor=0;
	private double textScalingFactor = 0;
	private int  width=0;
	private int height=0;


	private static ScalingUtility scalingUtility =null ;

	public static ScalingUtility getInstance() {
		if(scalingUtility==null){
			scalingUtility=new ScalingUtility();
		}
		return scalingUtility;
	}

	protected ScalingUtility() {
		Activity activity=(Activity)SharedData.getInstance().mContext;
		Display display = activity.getWindowManager().getDefaultDisplay();

		width = display.getWidth();
		height = display.getHeight()-getStatusBarHeight();
		Log.d("StHeight",""+height);
		widthRatio = (width / standardWidth);
		heightRatio = (height / standardHeight);
		aspectRatioFactor= Math.min(widthRatio, heightRatio);
		textScalingFactor = Math.min(widthRatio, heightRatio)* (standardDensity / activity.getResources().getDisplayMetrics().density);	
	}

	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = SharedData.getInstance().mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = SharedData.getInstance().mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public void scaleView(View view) {
		recursiveScaleView(view);
	}

	private void recursiveScaleView(View childView) {

		if(childView.getLayoutParams() instanceof AbsListView.LayoutParams ){

			AbsListView.LayoutParams linearParams=(AbsListView.LayoutParams) childView
					.getLayoutParams();

			linearParams.width *= widthRatio;
			linearParams.height *= heightRatio;

			childView.setLayoutParams(linearParams);

		}
		else if(childView.getLayoutParams() instanceof MarginLayoutParams){

			MarginLayoutParams linearParams = (MarginLayoutParams) childView.getLayoutParams();
			String tag="";
			if(childView.getTag()!=null){
				tag=childView.getTag().toString();
			}
			if(linearParams!=null && !tag.equalsIgnoreCase("constant"))
			{

				if (!tag.equalsIgnoreCase("constantWidth") && linearParams.width != MarginLayoutParams.WRAP_CONTENT && linearParams.width!=-1) {
					linearParams.width *= aspectRatioFactor;
				}

				if (!tag.equalsIgnoreCase("constantHeight") && linearParams.height != MarginLayoutParams.WRAP_CONTENT && linearParams.height !=-1) {
					linearParams.height *= aspectRatioFactor;
				}
				linearParams.leftMargin   *= widthRatio;
				linearParams.rightMargin  *=widthRatio;
				linearParams.topMargin    *= heightRatio;

				childView.setLayoutParams(linearParams);

				int paddingTop=(int) (childView.getPaddingTop()*heightRatio);
				int paddingLeft=(int) (childView.getPaddingLeft()*widthRatio);
				int paddingRight=(int) (childView.getPaddingRight()*widthRatio);
				int paddingBottom=(int) (childView.getPaddingBottom()*heightRatio);
				childView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

			}
		}

		if (childView instanceof ViewGroup) {
			int childCount = ((ViewGroup) childView).getChildCount();

			for (int i = 0; i < childCount; i++) {
				recursiveScaleView(((ViewGroup) childView).getChildAt(i));
			}
			return;
		}

		else if (childView instanceof TextView) {
			float textSize = ((TextView) childView).getTextSize();
			textSize *= textScalingFactor;
			if(width<=240 || height<=320){
				textSize=textSize+1.2f;
			}
			((TextView) childView).setTextSize(TypedValue.COMPLEX_UNIT_PX,
					textSize);
		}

	}
	public int resizeProvidedWidth(int givenWidth){
		return (int) (givenWidth*widthRatio);
	}
	public int resizeProvidedHeight(int givenHeight){
		return (int) (givenHeight*heightRatio);
	}
	public int scaleTextSize(int size){
		return (int) (size*textScalingFactor);
	}
	public int resizeAspectFactor(int size){
		return (int)(size*aspectRatioFactor);
	}
	public int getCurrentWidth(){
		return width;
	}
	public int getCurrentHeight(){
		return height;
	}

}