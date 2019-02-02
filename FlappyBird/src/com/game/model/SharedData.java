
package com.game.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SharedData {

	private static  SharedData instance = null;
	public  int currentGameMode=FlappyConstants.CLASSIC_MODE;
	public  int currentGameDiffLevel=FlappyConstants.NORMAL_MODE;
	public  int middleScreenPoint=0;
	public  int groundHeadPoint=0;
	public  int screenWidth;
	public  int screenHeight;
	public  float widthRatio;
	public  float heightRatio;
    public Context mContext=null;
    public int birdFrame=0;
    public boolean isTouchedDown=true;
    public long touchDownTime=0;
    
	public static SharedData getInstance() {
		if(instance==null){
			instance=new SharedData();
		}
		return instance;

	}
	public void killInstance(){
		instance=null;
	}


	public void resetSharedDate(){
		currentGameMode=FlappyConstants.CLASSIC_MODE;
		currentGameDiffLevel=FlappyConstants.NORMAL_MODE;
	}

	public Bitmap decodeResource(Context context,int id){
		return BitmapFactory.decodeResource(context.getResources(),id);
	}
	public Bitmap decodeResource(Context context,String name){
		int id=context.getResources().getIdentifier(name,"drawable",context.getPackageName());
		return BitmapFactory.decodeResource(context.getResources(),id);
	}
	public void setContext(Context context){
		mContext=context;
	}
	
}
