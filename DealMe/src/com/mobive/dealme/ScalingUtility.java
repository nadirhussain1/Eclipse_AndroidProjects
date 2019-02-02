package com.mobive.dealme;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;


public class ScalingUtility {
	private final double standardWidth =720;
	private final double standardHeight = 1184;
	private final double standardDensity = 2.0;
	private  double runningDeviceDensity=0;

	private double widthRatio = 0;
	private double heightRatio = 0;
	private double textSizeScalingFactor = 0;
	private double minScalingFactor=0;
	private static ScalingUtility scalingUtility =null ;
	Context context=null;
	int width=0;
	int height=0;

	public static ScalingUtility getInstance(Activity activity) {
		if(scalingUtility==null){
			scalingUtility=new ScalingUtility(activity);
		}
		return scalingUtility;
	}

	protected ScalingUtility(Activity activity) {
		context=activity.getApplicationContext();
		Display display = activity.getWindowManager().getDefaultDisplay();

		width = display.getWidth();
		height = display.getHeight();

		runningDeviceDensity= context.getResources().getDisplayMetrics().density;

		widthRatio = (width / standardWidth);
		heightRatio = (height / standardHeight);
		minScalingFactor=Math.min(widthRatio, heightRatio);	
		textSizeScalingFactor= minScalingFactor* (standardDensity /runningDeviceDensity);
	}


	public int resizeProvidedWidth(int givenWidth){

		return (int) (givenWidth*widthRatio);

	}
	public int resizeProvidedHeight(int givenHeight){

		return (int) (givenHeight*heightRatio);

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
		else{

			MarginLayoutParams linearParams = (MarginLayoutParams) childView.getLayoutParams();
			// double scaleValue = Math.min(widthRatio, heightRatio);
			if(linearParams!=null){
				Log.d("Density","Scale"+childView.getClass().getName());
				if (linearParams.width != MarginLayoutParams.WRAP_CONTENT && linearParams.width!=-1) {
					linearParams.width *= minScalingFactor;
				}

				if (linearParams.height != MarginLayoutParams.WRAP_CONTENT && linearParams.height !=-1) {
					linearParams.height *= minScalingFactor;
				}
				linearParams.leftMargin *= widthRatio;
				linearParams.rightMargin*=widthRatio;
				linearParams.topMargin *= heightRatio;
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
			textSize *= textSizeScalingFactor;

			((TextView) childView).setTextSize(TypedValue.COMPLEX_UNIT_PX,
					textSize);
		}

	}
	public int getCurrentWidth(){
		return width;
	}
	public int getCurrentHeight(){
		return height;
	}

	public int reSizeWidth(int width){
		return (int) (width*widthRatio);
	}
	public int reSizeHeight(int height){
		return (int) (height*heightRatio);
	}
}