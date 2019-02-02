package com.game.surface;



import com.game.flappybird.R;
import com.game.model.FlappyBird;
import com.game.model.FlappyConstants;
import com.game.model.SharedData;
import com.game.model.SoundManager;


public class FlappyWorld {
	private FlappyBird bird;
	private ScrollHandler scroller;
	private int score = 0;
	private GameState currentState;


	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public FlappyWorld() {
		currentState = GameState.READY;
		bird = new FlappyBird();
		scroller = new ScrollHandler(this);

	}
	public void updateWorld(float delta) {

		switch (currentState) {
		case READY:
			updateReady(delta);
			break;

		case RUNNING:
		default:
			updateRunning(delta);
			break;
		}

	}
	private void updateReady(float delta) {
		//currentState=GameState.RUNNING;
		//bird.update(delta);
		//scroller.update(delta);
	}
	public void updateRunning(float delta){

		if(scroller.collides(bird) && bird.isAlive()){	
			gameOverHandle();
		}else if(bird.birdRect.bottom>SharedData.getInstance().groundHeadPoint){
			gameOverHandle();
			bird.decelerate();
		}else if(SharedData.getInstance().currentGameMode==FlappyConstants.CLASSIC_MODE){
			if(SharedData.getInstance().currentGameDiffLevel==FlappyConstants.NORMAL_MODE){
				bird.update(delta);
				scroller.update(delta);
			}else{
				bird.update(delta);
				scroller.update(delta);
				bird.jumpClick();
			}
		}
	}
	private void gameOverHandle(){
		currentState = GameState.GAMEOVER;
		scroller.stop();
		bird.die();
		SoundManager.getInstance().playSound(R.raw.dead);
	}
	public void gameClick(){
		bird.jumpClick();
	}
	public FlappyBird getBird(){
		return bird;
	}
	public ScrollHandler getScroller() {
		return scroller;
	}
	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}
	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void restart() {
		currentState = GameState.READY;
		score = 0;
		bird.onRestart();
		scroller.onRestart();
		SharedData.getInstance().isTouchedDown=true;
		SharedData.getInstance().touchDownTime=0;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}


}
