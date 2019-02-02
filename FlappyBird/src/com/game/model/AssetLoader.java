package com.game.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.game.flappybird.R;

public class AssetLoader {
	private static AssetLoader assetLoaderInstance=null;
	public Bitmap backgroundBitmap=null;
	public Bitmap groundBitmap=null;
	public Bitmap bird_middle=null;
	public Bitmap bird_up=null;
	public Bitmap bird_down=null;
	public Bitmap movingBar=null;
	public Bitmap pipeUp=null;
	public Bitmap pipeDown=null;
	public Bitmap grass=null;
    public Bitmap readyButtonBitmap=null;
    
	public static AssetLoader getInstance() {
		if(assetLoaderInstance==null){
			assetLoaderInstance=new AssetLoader();
		}
		return assetLoaderInstance;

	}
	private AssetLoader() {
		Context mContext=SharedData.getInstance().mContext;
		Bitmap bitmap=SharedData.getInstance().decodeResource(mContext, R.drawable.forest_background);
		backgroundBitmap = Bitmap.createScaledBitmap(bitmap,SharedData.getInstance().screenWidth, SharedData.getInstance().screenHeight, false);
		bitmap=SharedData.getInstance().decodeResource(mContext, R.drawable.forest_ground);
		int height=(int) (FlappyConstants.GROUND_HEIGHT*SharedData.getInstance().heightRatio);
		groundBitmap = Bitmap.createScaledBitmap(bitmap,SharedData.getInstance().screenWidth,height, false);
        
		bird_middle=SharedData.getInstance().decodeResource(mContext, R.drawable.bird_middle);
		bird_down=SharedData.getInstance().decodeResource(mContext, R.drawable.bird_down);
		bird_up=SharedData.getInstance().decodeResource(mContext, R.drawable.bird_up);
		movingBar=SharedData.getInstance().decodeResource(mContext, R.drawable.forest_grass);
		
		pipeUp=SharedData.getInstance().decodeResource(mContext, R.drawable.stump_up);
		pipeDown=SharedData.getInstance().decodeResource(mContext, R.drawable.stump_down);
		bitmap=SharedData.getInstance().decodeResource(mContext, R.drawable.forest_grass);
		readyButtonBitmap=SharedData.getInstance().decodeResource(mContext, R.drawable.buttons_background);
		height=(int) (30*SharedData.getInstance().heightRatio);
		grass=Bitmap.createScaledBitmap(bitmap,SharedData.getInstance().screenWidth,height, false);
		
		SharedData.getInstance().groundHeadPoint=SharedData.getInstance().screenHeight-groundBitmap.getHeight();
		SharedData.getInstance().middleScreenPoint=SharedData.getInstance().screenHeight/2-bird_middle.getHeight();
	}
	

}
