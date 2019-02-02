package com.game.model;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.game.flappybird.R;

public class FlappyBird {
	public Bitmap birdBitmap;
	public float xPosition;
	public float yPosition;
	public float rotation;
	private float velocity=4;
	public  Rect birdRect;
	private boolean isAlive;

	public FlappyBird(){
		birdBitmap=AssetLoader.getInstance().bird_middle;
		xPosition=(int) (FlappyConstants.BIRD_X_POSITION*SharedData.getInstance().widthRatio);
		yPosition=SharedData.getInstance().middleScreenPoint;	
		birdRect=new Rect((int)(xPosition+5),(int)(yPosition+5),(int)((xPosition+birdBitmap.getWidth()-5)),(int)((yPosition+birdBitmap.getHeight()-5)));
		isAlive=true;
	}
	public void update(float delta){

		yPosition+=velocity;
		velocity+=0.2;

		if(velocity>=10){
			velocity=10;
		}

		if(yPosition<=10){
			yPosition=10;
		}
		rotation=(yPosition-SharedData.getInstance().middleScreenPoint)*20/100;
		if(rotation>20){
			rotation=20;
		}
		else if(rotation<-20){
			rotation=-20;
		}

		birdRect.set((int)(xPosition+5),(int)(yPosition+5),(int)((xPosition+birdBitmap.getWidth()-5)),(int)((yPosition+birdBitmap.getHeight()-5)));
		Log.d("BirdX","Left="+birdRect.left+" Right="+birdRect.right);
	}
	public void jumpClick(){
		if(isAlive){
			if(SharedData.getInstance().currentGameMode==FlappyConstants.CLASSIC_MODE){
				if(SharedData.getInstance().currentGameDiffLevel==FlappyConstants.NORMAL_MODE){
					yPosition-=130;
					//velocity=0.2f;
				}else if(SharedData.getInstance().isTouchedDown){
					long diff=System.currentTimeMillis()-SharedData.getInstance().touchDownTime;
					if(diff>FlappyConstants.TOUCH_HOLD_TIME_THRESHOLD){
						SoundManager.getInstance().playSound(R.raw.bird_flap);
						yPosition-=20;
					}
				}
			}
		}

	}
	public boolean isFalling(){
		return (velocity>5);

	}
	public boolean stopFlapping(){
		return (velocity>6 || !isAlive);
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void die() {
		isAlive = false;
		velocity = 0;
	}

	public void decelerate() {
		velocity = 0;
	}
	public void onRestart() {
		rotation = 0;
		yPosition=SharedData.getInstance().middleScreenPoint;	
		birdRect=new Rect((int)(xPosition+5),(int)(yPosition+5),(int)((xPosition+birdBitmap.getWidth()-5)),(int)((yPosition+birdBitmap.getHeight()-5)));
		velocity=4;
		isAlive = true;
	}

}
