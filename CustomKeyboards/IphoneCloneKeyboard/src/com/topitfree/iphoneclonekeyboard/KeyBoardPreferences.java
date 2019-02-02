package com.topitfree.iphoneclonekeyboard;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyBoardPreferences {
	
	private static KeyBoardPreferences keyboardPreferences=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;
   
	public static KeyBoardPreferences getInstance(Context context) {
		if(keyboardPreferences==null){
			keyboardPreferences=new KeyBoardPreferences(context);
		}
		return keyboardPreferences;
	}
	private KeyBoardPreferences(Context context ){

		sharedPreferences =context.getSharedPreferences("KeyBoardPreferences", 0);
		editor = sharedPreferences.edit();
	}
	public void saveSoundSettingsFlag(boolean isSound){
		editor.putBoolean("SoundSettings",isSound);
		editor.commit();
	}
	public boolean getSoundSettings(){
		return sharedPreferences.getBoolean("SoundSettings", true);
	}
	public void saveVibrationSettingsFlag(boolean isVib){
		editor.putBoolean("VibrationSettings",isVib);
		editor.commit();
	}
	public boolean getVibrationSettings(){
		return sharedPreferences.getBoolean("VibrationSettings", true);
	}
	public void saveArrowKeysSettingsFlag(boolean isArrows){
		editor.putBoolean("ArrowsSettings",isArrows);
		editor.commit();
	}
	public boolean getArrowsSettings(){
		return sharedPreferences.getBoolean("ArrowsSettings", true);
	}
	public void saveTraceKeySettingsFlag(boolean isTrace){
		editor.putBoolean("TraceKeySettings",isTrace);
		editor.commit();
	}
	public boolean getTraceKeySettings(){
		return sharedPreferences.getBoolean("TraceKeySettings", true);
	}
	public void saveVolumelevel(int progress){
		editor.putInt("VolumeLevel",progress);
		editor.commit();
	}
	public int getVolumeLevel(){
		return sharedPreferences.getInt("VolumeLevel", -1);
	}

}
