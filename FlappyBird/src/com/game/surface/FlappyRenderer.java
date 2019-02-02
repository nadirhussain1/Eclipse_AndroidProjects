package com.game.surface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.game.model.AssetLoader;
import com.game.model.FlappyBird;
import com.game.model.Grass;
import com.game.model.Pipe;
import com.game.model.SharedData;
import com.game.utilities.ScalingUtility;

public class FlappyRenderer {
	FlappyWorld gameWorld;
	FlappyBird bird;
	ScrollHandler scroller;
	Grass frontGrass;
	Grass backGrass;
	Pipe  pipe1;
	Pipe  pipe2;
	Pipe  pipe3;

	public FlappyRenderer(FlappyWorld world){
		gameWorld=world;
		initGameObjects();
	}
	private void initGameObjects(){
		bird=gameWorld.getBird();
		scroller = gameWorld.getScroller();
		frontGrass = scroller.getFrontGrass();
		backGrass = scroller.getBackGrass();
		pipe1 = scroller.getPipe1();
		pipe2 = scroller.getPipe2();
		pipe3 = scroller.getPipe3();
	}
	public void renderWorld(Canvas canvas){
		drawBackground(canvas);
		drawPipes(canvas);
		drawGround(canvas);
		drawGrass(canvas);
		drawBird(canvas);
		drawScore(canvas);
		drawReadyButton(canvas);
		
	}
	private void drawBackground(Canvas canvas){
		canvas.drawBitmap(AssetLoader.getInstance().backgroundBitmap, 0, 0,null);
	}
	private void drawGround(Canvas canvas){
		canvas.drawBitmap(AssetLoader.getInstance().groundBitmap, 0, SharedData.getInstance().groundHeadPoint,null);
	}
	private void drawBird(Canvas canvas){
		if(bird.stopFlapping()){
			bird.birdBitmap=AssetLoader.getInstance().bird_middle;	
		}else{
			if(SharedData.getInstance().birdFrame==0){
				bird.birdBitmap=AssetLoader.getInstance().bird_up;
			}else if(SharedData.getInstance().birdFrame==1){
				bird.birdBitmap=AssetLoader.getInstance().bird_middle;
			}else{
				bird.birdBitmap=AssetLoader.getInstance().bird_down;
			}
			SharedData.getInstance().birdFrame++;
			SharedData.getInstance().birdFrame=SharedData.getInstance().birdFrame%3;
		}
		
		canvas.save();
		canvas.rotate(bird.rotation,  bird.xPosition + ( bird.birdBitmap.getWidth() / 2), bird.yPosition + (bird.birdBitmap.getHeight() / 2));
		canvas.drawBitmap(bird.birdBitmap, bird.xPosition, bird.yPosition,null);
		canvas.restore();
	}
	private void drawGrass(Canvas canvas){
		canvas.drawBitmap(AssetLoader.getInstance().grass, frontGrass.getX(), frontGrass.getY(), null);
		canvas.drawBitmap(AssetLoader.getInstance().grass, backGrass.getX(), backGrass.getY(), null);
	}
	private void drawPipes(Canvas canvas){
		Rect src=new Rect(0,0,AssetLoader.getInstance().pipeUp.getWidth(),AssetLoader.getInstance().pipeUp.getHeight());
		canvas.drawBitmap(AssetLoader.getInstance().pipeDown,src,pipe1.upperPipeRect,null);
		canvas.drawBitmap(AssetLoader.getInstance().pipeUp,src,pipe1.lowerPipeRect,null);
		canvas.drawBitmap(AssetLoader.getInstance().pipeDown, src, pipe2.upperPipeRect, null);
		canvas.drawBitmap(AssetLoader.getInstance().pipeUp, src, pipe2.lowerPipeRect, null);
		canvas.drawBitmap(AssetLoader.getInstance().pipeDown, src, pipe3.upperPipeRect, null);
		canvas.drawBitmap(AssetLoader.getInstance().pipeUp, src, pipe3.lowerPipeRect, null);
	}
	private void drawScore(Canvas canvas){
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#000000"));
		paint.setTextSize(40);
		canvas.drawText("SCORE :"+gameWorld.getScore(),50, 50, paint);
	}
    private void drawReadyButton(Canvas canvas){
    	if(gameWorld.isReady()){
//    		int left=(SharedData.getInstance().screenWidth-AssetLoader.getInstance().readyButtonBitmap.getWidth())/2;
//    		int top=SharedData.getInstance().screenHeight-(AssetLoader.getInstance().readyButtonBitmap.getHeight()+50);
//			canvas.drawBitmap(AssetLoader.getInstance().readyButtonBitmap,left, top, null);
    		Paint paint = new Paint();
    		paint.setStyle(Paint.Style.FILL);
    		paint.setColor(Color.parseColor("#000000"));
    		paint.setTextSize(40);
    		canvas.drawText("GET READY",(SharedData.getInstance().screenWidth/2)-(50*SharedData.getInstance().widthRatio),50 , paint);
		}
    }
}
