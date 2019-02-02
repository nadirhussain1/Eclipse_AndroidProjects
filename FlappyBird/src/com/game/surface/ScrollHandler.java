package com.game.surface;

import com.game.flappybird.R;
import com.game.model.AssetLoader;
import com.game.model.FlappyBird;
import com.game.model.FlappyConstants;
import com.game.model.Grass;
import com.game.model.Pipe;
import com.game.model.SharedData;
import com.game.model.SoundManager;

public class ScrollHandler {

	private Grass frontGrass, backGrass;
	private Pipe pipe1, pipe2, pipe3;
	FlappyWorld gameWorld=null;

	public ScrollHandler(FlappyWorld world) {
		gameWorld=world;

		int grassYPosition=SharedData.getInstance().groundHeadPoint-AssetLoader.getInstance().grass.getHeight();
		int grassWidth=AssetLoader.getInstance().grass.getWidth();
		int grassHeight=AssetLoader.getInstance().grass.getHeight();
		frontGrass = new Grass((SharedData.getInstance().screenWidth-grassWidth),grassYPosition, grassWidth, grassHeight,FlappyConstants.SCROLL_SPEED);
		backGrass = new Grass(frontGrass.getTailX(), grassYPosition, grassWidth,grassHeight,FlappyConstants.SCROLL_SPEED);

		pipe1 = new Pipe((int)(FlappyConstants.PIPE_1_LEFT*SharedData.getInstance().widthRatio), 0,AssetLoader.getInstance().pipeDown.getWidth(),(int)(150*SharedData.getInstance().heightRatio),FlappyConstants.SCROLL_SPEED);
		pipe2 = new Pipe((int)(pipe1.getTailX() +FlappyConstants.PIPE_GAP*SharedData.getInstance().widthRatio), 0, AssetLoader.getInstance().pipeDown.getWidth(),(int)(170*SharedData.getInstance().heightRatio),FlappyConstants.SCROLL_SPEED);
		pipe3 = new Pipe((int)(pipe2.getTailX() +FlappyConstants.PIPE_GAP*SharedData.getInstance().widthRatio), 0, AssetLoader.getInstance().pipeDown.getWidth(),(int)(150*SharedData.getInstance().heightRatio), FlappyConstants.SCROLL_SPEED);
	}

	public void update(float delta) {
		// Update our objects
		frontGrass.update(delta);
		backGrass.update(delta);
		pipe1.update(delta);
		pipe2.update(delta);
		pipe3.update(delta);

		// Check if any of the pipes are scrolled left,
		// and reset accordingly
		if (pipe1.isScrolledLeft()) {
			pipe1.reset((int)(pipe3.getTailX() + FlappyConstants.PIPE_GAP*SharedData.getInstance().widthRatio));
		} else if (pipe2.isScrolledLeft()) {
			pipe2.reset((int)(pipe1.getTailX() + FlappyConstants.PIPE_GAP*SharedData.getInstance().widthRatio));

		} else if (pipe3.isScrolledLeft()) {
			pipe3.reset((int)(pipe2.getTailX() +FlappyConstants.PIPE_GAP*SharedData.getInstance().widthRatio));
		}

		// Same with grass
		if (frontGrass.isScrolledLeft()) {
			frontGrass.reset(backGrass.getTailX());

		} else if (backGrass.isScrolledLeft()) {
			backGrass.reset(frontGrass.getTailX());

		}
	}
	public boolean collides(FlappyBird bird){
		if (!pipe1.isScored() && ( (pipe1.getX() +pipe1.getWidth()) <bird.xPosition)){
			addScore(1);
			pipe1.setScored(true);
			SoundManager.getInstance().playSound(R.raw.score);
		} else if (!pipe2.isScored() && ( (pipe2.getX() +pipe2.getWidth()) <bird.xPosition)){
			addScore(1);
			pipe2.setScored(true);
			SoundManager.getInstance().playSound(R.raw.score);

		} else if (!pipe3.isScored() && ( (pipe3.getX() +pipe3.getWidth()) <bird.xPosition)){
			addScore(1);
			pipe3.setScored(true);
			SoundManager.getInstance().playSound(R.raw.score);
		}

		return (pipe1.collides(bird) || pipe2.collides(bird) || pipe3.collides(bird));
	}
	public void stop(){
		frontGrass.stop();
		backGrass.stop();
		pipe1.stop();
		pipe2.stop();
		pipe3.stop();
	}

	public Grass getFrontGrass() {
		return frontGrass;
	}

	public Grass getBackGrass() {
		return backGrass;
	}

	public Pipe getPipe1() {
		return pipe1;
	}

	public Pipe getPipe2() {
		return pipe2;
	}

	public Pipe getPipe3() {
		return pipe3;
	}
	private void addScore(int increment) {
		gameWorld.addScore(increment);
	}
	public void onRestart() {
        frontGrass.onRestart(0, FlappyConstants.SCROLL_SPEED);
        backGrass.onRestart(frontGrass.getTailX(), FlappyConstants.SCROLL_SPEED);
        pipe1.onRestart(FlappyConstants.PIPE_1_LEFT, FlappyConstants.SCROLL_SPEED);
        pipe2.onRestart(pipe1.getTailX() + FlappyConstants.PIPE_GAP, FlappyConstants.SCROLL_SPEED);
        pipe3.onRestart(pipe2.getTailX() + FlappyConstants.PIPE_GAP, FlappyConstants.SCROLL_SPEED);
    }

}
