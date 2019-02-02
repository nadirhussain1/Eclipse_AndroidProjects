package com.game.flappybird;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.model.SharedData;
import com.game.model.SoundManager;
import com.game.surface.GameView;
import com.game.utilities.GamePreferences;

public class MainActivity extends Activity {
    RelativeLayout gameOverLayout=null;
    Button playButton=null;
    TextView currentScoreText=null;
    TextView bestScoreText=null;
    GameView gameViewScreen;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gameOverLayout=(RelativeLayout)findViewById(R.id.gameOverLayout);
		playButton=(Button)findViewById(R.id.PlayButton);
		gameViewScreen=(GameView)findViewById(R.id.gameView);
		currentScoreText=(TextView)findViewById(R.id.scoreAmountView);
		bestScoreText=(TextView)findViewById(R.id.bestTextScoreView);
		
		playButton.setOnClickListener(playClickListener);
		SharedData.getInstance().setContext(this);

	}
	@Override
	protected void onPause() {
		super.onPause();
		
		int currentScore=gameViewScreen.gameWorld.getScore();
		int saveBestScore=GamePreferences.getInstance().getBestScore();
		if(currentScore>saveBestScore){
			saveBestScore=currentScore;
			GamePreferences.getInstance().saveBestScore(saveBestScore);
		}
		
		SoundManager.getInstance().killSoundManager();
	}
	@Override
	protected void onResume() {
		super.onResume();
        loadPrefSavedValues();
		SoundManager.getInstance();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
	private void loadPrefSavedValues(){
		SharedData.getInstance().currentGameMode=GamePreferences.getInstance().getGameMode();
		SharedData.getInstance().currentGameDiffLevel=GamePreferences.getInstance().getGameDiffLevel();
	}
	public void showGameOverView(){
		int currentScore=gameViewScreen.gameWorld.getScore();
		int saveBestScore=GamePreferences.getInstance().getBestScore();
		if(currentScore>saveBestScore){
			saveBestScore=currentScore;
			GamePreferences.getInstance().saveBestScore(saveBestScore);
		}
		currentScoreText.setText(""+currentScore);
		bestScoreText.setText(""+saveBestScore);
		
		gameOverLayout.setVisibility(View.VISIBLE);
	}
	public void hideGameOverView(){
		gameOverLayout.setVisibility(View.GONE);
	}
	OnClickListener playClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			hideGameOverView();
			gameViewScreen.gameWorld.restart();
			
		}
	};
}
