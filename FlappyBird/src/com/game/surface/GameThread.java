package com.game.surface;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private static final String TAG = GameThread.class.getSimpleName();
	private SurfaceHolder surfaceHolder;
	private GameView gameView;
	private boolean keepDrawing;
	private boolean isRunning=true;
	private long delta=0;
	private long lastTime=0;

	public GameThread(SurfaceHolder surfaceHolder, GameView gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameView = gamePanel;
	}
	public void setDrawing(boolean keepdrawing) {
		this.keepDrawing = keepdrawing;
	}

	@Override
	public void run() 
	{
		Canvas canvas;

		while (isRunning) 
		{
			if(keepDrawing){
				
				if(lastTime !=0){
					delta=System.currentTimeMillis()-lastTime;
					lastTime=System.currentTimeMillis();
				}

				canvas = null;
				try {
					canvas = this.surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) 
					{	
						this.gameView.update(delta);	
						this.gameView.render(delta,canvas);				
					}
				} finally {

					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}	
			}
		}

	}

}
