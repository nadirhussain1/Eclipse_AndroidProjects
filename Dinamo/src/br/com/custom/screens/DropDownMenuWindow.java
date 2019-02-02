package br.com.custom.screens;

import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import br.com.utilities.ScalingUtility;

public class DropDownMenuWindow {

	private PopupWindow popWindow=null;
	int mWidth=210;
	int offsetY=0;
	int xOffset;

	@SuppressWarnings("deprecation")
	public  DropDownMenuWindow(View dropDownView,int width,int x,int y){

		mWidth=ScalingUtility.getInstance().resizeProvidedWidth(width);
		offsetY=ScalingUtility.getInstance().resizeProvidedHeight(y);
		xOffset=ScalingUtility.getInstance().resizeProvidedWidth(x);

		popWindow=new PopupWindow(dropDownView, mWidth, LayoutParams.WRAP_CONTENT);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);
		popWindow.setTouchInterceptor(PopUpToucIntercepter);

	}
	public void showCalculatorMenu(View anchor){
		popWindow.showAsDropDown(anchor, xOffset, offsetY);	
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
