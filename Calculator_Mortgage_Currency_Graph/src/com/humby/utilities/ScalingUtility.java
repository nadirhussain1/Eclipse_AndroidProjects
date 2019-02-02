package com.humby.utilities;

import android.app.Activity;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.humby.model.SharedData;


public class ScalingUtility {
	private  double standardWidth =720;
	private  double standardHeight = 1184;
	private  double standardDensity = 2.0;

	private double widthRatio = 0;
	private double heightRatio = 0;
	private double minScalingFactor = 0;
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
		height = display.getHeight();

		int actionBarHeight=getActionBarHeight();
		standardHeight=standardHeight-actionBarHeight;
		height=height-actionBarHeight;

		widthRatio = (width / standardWidth);
		heightRatio = (height / standardHeight);
		minScalingFactor = Math.min(widthRatio, heightRatio)* (standardDensity / activity.getResources().getDisplayMetrics().density);
	}

	public int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (SharedData.getInstance().mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
					true))
				actionBarHeight = TypedValue.complexToDimensionPixelSize(
						tv.data,SharedData.getInstance().mContext. getResources().getDisplayMetrics());
		} else {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					SharedData.getInstance().mContext.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
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
					linearParams.width *= widthRatio;
				}

				if (!tag.equalsIgnoreCase("constantHeight") && linearParams.height != MarginLayoutParams.WRAP_CONTENT && linearParams.height !=-1) {
					linearParams.height *= heightRatio;
				}
				linearParams.leftMargin *= widthRatio;
				linearParams.rightMargin*=widthRatio;
				linearParams.topMargin *= heightRatio;
				linearParams.bottomMargin *= heightRatio;
				childView.setLayoutParams(linearParams);

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
			textSize *= minScalingFactor;

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
	public int getCurrentWidth(){
		return width;
	}
	public int getCurrentHeight(){
		return height;
	}
	
}