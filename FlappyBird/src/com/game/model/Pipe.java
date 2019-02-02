package com.game.model;

import java.util.Random;

import android.graphics.Rect;

public class Pipe extends Scrollable {

	private Random random;
	public Rect upperPipeRect;
	public Rect lowerPipeRect;
	private boolean isScored = false;
	
	public Pipe(int x, int y, int width, int height, int scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		random = new Random();

		upperPipeRect=new Rect();
		lowerPipeRect=new Rect();
	}

	@Override
	public void reset(int newX) {
		super.reset(newX);
		height = random.nextInt((int)(200*SharedData.getInstance().widthRatio))+20;
		isScored=false;
		
		upperPipeRect.set(position.x,0,position.x+width,height);
		lowerPipeRect.set(position.x,(int)(height+FlappyConstants.VERTICAL_GAP*SharedData.getInstance().heightRatio),position.x+width,SharedData.getInstance().screenHeight);
	}
	@Override
	public void update(float delta) {
		super.update(delta);
		upperPipeRect.set(position.x,0,position.x+width,height);
		lowerPipeRect.set(position.x,(int)(height+FlappyConstants.VERTICAL_GAP*SharedData.getInstance().heightRatio),position.x+width,SharedData.getInstance().screenHeight);
	}
	public boolean collides(FlappyBird bird) {	
		return ((bird.birdRect.intersect(upperPipeRect) || bird.birdRect.intersect(lowerPipeRect)));       
    }
	public boolean isScored() {
	    return isScored;
	}
	public void setScored(boolean isScore) {
	    isScored = isScore;
	}
	public void onRestart(float x, float scrollSpeed) {
	    velocity= (int) scrollSpeed;
	    reset((int)x);
	}

}
