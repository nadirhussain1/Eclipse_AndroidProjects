package com.app.utilities;

import android.content.Context;
import android.content.SharedPreferences;


public class BluePref {
	private static BluePref prefInstance=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;

	public static BluePref getInstance(Context context) {
		if(prefInstance==null){
			prefInstance=new BluePref(context);
		}
		return prefInstance;
	}
	private BluePref(Context context){
		sharedPreferences = context.getSharedPreferences("BluetoothApp", 0);
		editor = sharedPreferences.edit();
	}
	public void savePresetMotorAValue(){
		editor.putInt(MotorConstants.MOTOR_A_SPEED, SharedData.getInstance().speedMotorA);
		editor.commit();
	}
	public void savePresetMotorBValue(){
		editor.putInt(MotorConstants.MOTOR_B_SPEED, SharedData.getInstance().speedMotorB);
		editor.commit();
	}
	public void loadAppResumeData(){
		SharedData.getInstance().speedMotorA=sharedPreferences.getInt(MotorConstants.MOTOR_A_SPEED,255);
		SharedData.getInstance().speedMotorB=sharedPreferences.getInt(MotorConstants.MOTOR_B_SPEED,255);	
	}
}
