package com.game.surface;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.flappybird.MainActivity;
import com.game.flappybird.R;
import com.game.model.AssetLoader;
import com.game.model.FlappyConstants;
import com.game.model.SharedData;
import com.game.model.SoundManager;
import com.game.utilities.ScalingUtility;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{

	public GameThread thread=null;
	public Context mContext;
	public FlappyWorld gameWorld;
	private FlappyRenderer gameRenderer;
	private boolean isShowingGameOver=false;


	public GameView(Context context, AttributeSet attrs) {
		super(context,attrs);

		getHolder().addCallback(this);
		mContext=context;

		thread = new GameThread(getHolder(), this);
		setFocusable(true);

		SharedData.getInstance().screenWidth=mContext.getResources().getDisplayMetrics().widthPixels;
		SharedData.getInstance().screenHeight=(mContext.getResources().getDisplayMetrics().heightPixels);
		SharedData.getInstance().widthRatio=(float) ScalingUtility.getInstance((Activity)mContext).getScaleWidthFactor();
		SharedData.getInstance().heightRatio=(float) ScalingUtility.getInstance((Activity)mContext).getScaleHeightFactor();
		SharedData.getInstance().mContext=context;
		AssetLoader.getInstance();

		gameWorld = new FlappyWorld();
		gameRenderer = new FlappyRenderer(gameWorld);

	}
	private void showGameOverScreen(){
		((MainActivity)mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {

				((MainActivity)mContext).showGameOverView();
			}
		});
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setDrawing(true);
		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		killThread();

	}
	public void killThread(){

		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction())
		{
		case MotionEvent.ACTION_UP:
			if(SharedData.getInstance().currentGameMode==FlappyConstants.CLASSIC_MODE){
				if(SharedData.getInstance().currentGameDiffLevel==FlappyConstants.NORMAL_MODE){
					if (gameWorld.isReady()) {
						gameWorld.start();
					}else{
						SoundManager.getInstance().playSound(R.raw.bird_flap);
						gameClick();
					}
				}else{

					SharedData.getInstance().isTouchedDown=false;
				}

			}
			break;
		case MotionEvent.ACTION_DOWN:
			if(SharedData.getInstance().currentGameMode==FlappyConstants.CLASSIC_MODE){
				if(SharedData.getInstance().currentGameDiffLevel==FlappyConstants.HARD_MODE){
					if (gameWorld.isReady()) {
						gameWorld.start();
					}else{
						SharedData.getInstance().isTouchedDown=true;
						SharedData.getInstance().touchDownTime=System.currentTimeMillis();
						gameClick();
					}
				}

			}
			break;


		}
		return true;
	}

	public void render(long delta,Canvas canvas) {
		if(canvas!=null){
			gameRenderer.renderWorld(canvas);
		}
	}

	public void update(long delta) {
		if(gameWorld.isGameOver()){
			if(!isShowingGameOver){
				isShowingGameOver=true;
				showGameOverScreen();
			}
		}else{
			isShowingGameOver=false;
			gameWorld.updateWorld(delta);
		}
	}
	private void gameClick(){
		gameWorld.gameClick();
	}

}

