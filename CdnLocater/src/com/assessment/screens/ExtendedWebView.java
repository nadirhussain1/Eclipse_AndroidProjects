package com.assessment.screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.assessment.cdnlocater.MainActivity;
import com.assessment.cdnlocater.R;
import com.assessment.constants.AppConstants;

public class ExtendedWebView  extends WebView{
	Context mContext;
	GestureDetector gestureDetector=null;
	int currentImageIndex=0;
	private String [] images_id_list=null;
	private String loadingMessage="";

	private int screenWidth;
	private int screenHeight;
	private ProgressDialog progressDialog=null;
	private boolean isLoading=false;

	public ExtendedWebView(Context context) {
		super(context);

		mContext=context;
		gestureDetector=new GestureDetector(mContext, gestureListener);
		//load images_ids strings
		images_id_list=mContext.getResources().getStringArray(R.array.images_id_list);
		loadingMessage=mContext.getResources().getString(R.string.loading_message);
		loadScreenDimensions();

		setWebViewClient(new CustomWebViewClient());

	}
	private void loadScreenDimensions(){
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		screenWidth = (int) (metrics.widthPixels/metrics.density);
		screenHeight = (int) (metrics.heightPixels/metrics.density);
		
		Log.d("Size","Pixels=Width="+screenWidth+" Height="+screenHeight);
	}
	public void loadImage(){
		showProgressDialog(loadingMessage);

		String url="";
		if(MainActivity.nearest_server_id==AppConstants.SA_SERVER_ID){
			url=AppConstants.SA_CDN_BASE_URL+images_id_list[currentImageIndex]+".jpg?rmode=scale&w="+screenWidth+"&h="+screenHeight;
		}else{
			url=AppConstants.IN_CDN_BASE_URL+images_id_list[currentImageIndex]+".jpg?rmode=scale&w="+screenWidth+"&h="+screenHeight;
		}

		((MainActivity)mContext).refreshTextLabels();
		loadUrl(url);
	}
	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		if(event.getAction()==MotionEvent.ACTION_UP){
			isLoading=false;
		}	
		return super.onTouchEvent(event);
	};
	private class CustomWebViewClient extends WebViewClient{
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressDialog.cancel();
		}

	}


	OnGestureListener gestureListener=new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {


		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {

			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {


		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {

			float deltaX = Math.abs(e1.getRawX()-e2.getRawX());

			if (deltaX > 200 && !isLoading) {
				isLoading=true;
				if (e1.getRawX() > e2.getRawX()) {
					currentImageIndex++;
					if(currentImageIndex>=images_id_list.length){
						currentImageIndex=0; 
					}
				} else if(e1.getRawX() < e2.getRawX()){
					currentImageIndex--;
					if(currentImageIndex<0){
						currentImageIndex=images_id_list.length-1;
					}
				}
				loadImage();
			}

			return false;

		}

		@Override
		public boolean onDown(MotionEvent e) {

			return false;
		}
	};

}
