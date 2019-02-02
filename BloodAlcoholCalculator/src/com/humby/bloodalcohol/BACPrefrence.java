package com.humby.bloodalcohol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class BACPrefrence 
{
	private static final String USER_PREFS = "BAC_PREFS";
	private SharedPreferences appSharedPrefs;
	private SharedPreferences.Editor prefsEditor;
	private String nameKey = "name_prefs";
	private String heightKey = "height_prefs";
	private String weightKey = "weight_prefs";
	private String genderKey = "gender_prefs";
	private String maleKey = "male_pref";
	private String femaleKey = "female_pref";

	public BACPrefrence(Context context){
		this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}


	public String getNameKey() {
		return appSharedPrefs.getString(nameKey, "");
	}


	public void setNameKey(String nameKey) {
		prefsEditor.putString(this.nameKey, nameKey).commit();

	}


	public String getHeightKey() {
		return appSharedPrefs.getString(heightKey, "");
	}


	public void setHeightKey(String heightKey) {
		prefsEditor.putString(this.heightKey, heightKey).commit();
	}


	public String getWeightKey() {
		return appSharedPrefs.getString(weightKey, "");
	}

	public void setMaleKey(boolean male)
	{
		prefsEditor.putBoolean(maleKey, male).commit();
	}

	public void setFemaleKey(boolean female)
	{
		prefsEditor.putBoolean(femaleKey, female).commit();
	}

	public boolean getMaleKey() {
		return appSharedPrefs.getBoolean(maleKey, false);
	}

	public boolean getFemaleKey() {
		return appSharedPrefs.getBoolean(femaleKey, false);
	}

	public void setWeightKey(String weightKey) {
		prefsEditor.putString(this.weightKey, weightKey).commit();
	}


	public String getGenderKey() {
		return appSharedPrefs.getString(genderKey, "");
	}


	public void setGenderKey(String genderKey) {
		prefsEditor.putString(this.genderKey, genderKey).commit();
	}
	public void saveInAppPurchaseFlag(boolean flag){
		prefsEditor.putBoolean("IN_APP_PURCHASE", flag);
		prefsEditor.commit();
	}
	public boolean getInAppPurchaseStatus(){
		return appSharedPrefs.getBoolean("IN_APP_PURCHASE", false);
	}


}
