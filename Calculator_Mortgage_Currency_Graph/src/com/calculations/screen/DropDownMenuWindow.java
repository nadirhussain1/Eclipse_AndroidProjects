package com.calculations.screen;

import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.humby.utilities.ScalingUtility;

public class DropDownMenuWindow {

	private PopupWindow popWindow=null;
	int width=480;
	int offsetY=-2;

	@SuppressWarnings("deprecation")
	public  DropDownMenuWindow(View dropDownView){

		width=ScalingUtility.getInstance().resizeProvidedWidth(width);
		offsetY=ScalingUtility.getInstance().resizeProvidedHeight(offsetY);

		popWindow=new PopupWindow(dropDownView, width, LayoutParams.WRAP_CONTENT);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);
		popWindow.setTouchInterceptor(PopUpToucIntercepter);

	}
	public void showCalculatorMenu(View anchor){	
		popWindow.showAsDropDown(anchor, 0, offsetY);	
	}
	public void showCalculatorMenu(View anchor,int offset){	
		popWindow.showAsDropDown(anchor, 0,offset);	
	}
	OnTouchListener PopUpToucIntercepter=new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
				popWindow.dismiss();
				return true;
			}
			return false;
		}
	};
	public void dismissWindow(){

		if(popWindow!=null){
			popWindow.dismiss();
		}

	}


}
