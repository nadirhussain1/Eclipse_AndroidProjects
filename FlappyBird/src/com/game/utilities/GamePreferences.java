package com.game.utilities;

import android.content.SharedPreferences;

import com.game.model.FlappyConstants;
import com.game.model.SharedData;


public class GamePreferences {
	private static GamePreferences gamePreferences=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;

	public static GamePreferences getInstance() {
		if(gamePreferences==null){
			gamePreferences=new GamePreferences();
		}
		return gamePreferences;
	}
	private GamePreferences(){
		sharedPreferences = SharedData.getInstance().mContext.getSharedPreferences("FlappyPref", 0);
		editor = sharedPreferences.edit();
	}
	public void saveBestScore(int score){
		editor.putInt(FlappyConstants.BEST_SCORE_PREF,score);
		editor.commit();
	}
	public int getBestScore(){
		return sharedPreferences.getInt(FlappyConstants.BEST_SCORE_PREF,0);
	}
	public void saveGameMode(int mode){
		editor.putInt(FlappyConstants.GAME_MODE,mode);
		editor.commit();
	}
	public int getGameMode(){
		return sharedPreferences.getInt(FlappyConstants.GAME_MODE,FlappyConstants.CLASSIC_MODE);
	}
	public void saveGameDiffLevel(int level){
		editor.putInt(FlappyConstants.GAME_DIFF_LEVEL,level);
		editor.commit();
	}
	public int getGameDiffLevel(){
		return sharedPreferences.getInt(FlappyConstants.GAME_DIFF_LEVEL,FlappyConstants.NORMAL_MODE);
	}
	
}
