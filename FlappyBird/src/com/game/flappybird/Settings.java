package com.game.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.game.model.FlappyConstants;
import com.game.model.SharedData;
import com.game.utilities.GamePreferences;
import com.game.utilities.ScalingUtility;

public class Settings extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.settings, null, false);
		ScalingUtility.getInstance(this).scaleView(view);
		setContentView(view);

		findViewById(R.id.ClassicModeButton).setOnClickListener(modeClickListener);
		findViewById(R.id.StoryModeButton).setOnClickListener(modeClickListener);
		findViewById(R.id.startNewGameButton).setOnClickListener(startGameListener);

		SharedData.getInstance().setContext(this);
	}
	android.view.View.OnClickListener startGameListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainIntent = new Intent(Settings.this, MainActivity.class);
			startActivity(mainIntent);
            finish();
		}
	};
	OnClickListener modeClickListener=new OnClickListener() {

		@Override
		public void onClick(View viewClicked) {
			if(viewClicked.getId()==R.id.StoryModeButton){
				GamePreferences.getInstance().saveGameMode(FlappyConstants.STORY_MODE);
			}else{
				GamePreferences.getInstance().saveGameMode(FlappyConstants.CLASSIC_MODE);
			}
			DiffSettings diffScreen=new DiffSettings(Settings.this);
			diffScreen.showDialog();

		}
	};


}
