package com.mobive.dealme;

import java.util.ArrayList;

import android.content.SharedPreferences;


public class DealMePreferences {
	private static DealMePreferences dealMePreferences=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;

	public static DealMePreferences getInstance() {
		if(dealMePreferences==null){
			dealMePreferences=new DealMePreferences();
		}
		return dealMePreferences;
	}
	private DealMePreferences(){

		sharedPreferences = DataUtility.getContext().getSharedPreferences("DealMePreferences", 0);
		editor = sharedPreferences.edit();
	}
	public void saveSignUpStatus(boolean isAlreadySignedUp){
		editor.putBoolean("isAlreadySignedUp",isAlreadySignedUp);
		editor.commit();
	}
	public boolean getSignedUpStatus(){
		return sharedPreferences.getBoolean("isAlreadySignedUp", false);
	}
	public void saveSelectedCity(String cityName){
		editor.putString("CityNAME",cityName);
		editor.commit();
	}
	public String getSelectedCity(){
		return sharedPreferences.getString("CityNAME", "");
	}
	public void saveUserEmailAndPassword(String email,String password){
		editor.putString("useremail", email);
    	editor.putString("password", password);
    	editor.commit();
	}
	public void saveUserEmail(String email){
		editor.putString("useremail", email);
		editor.commit();
	}
	public void saveUserPassword(String password){
		editor.putString("password", password);
    	editor.commit();
	}
	public String getUserEmail(){
		return sharedPreferences.getString("useremail", "");
	}
	public String getUserPassword(){
		return sharedPreferences.getString("password", "");
	}
	public void saveFirstName(String firstName){
		editor.putString("FirstName",firstName);
		editor.commit();
	}
	public void saveLastName(String secondName){
		editor.putString("LastName",secondName);
		editor.commit();
	}
	public String getFirstName(){
		return sharedPreferences.getString("FirstName","");
	}
	public String getLastName(){
		return sharedPreferences.getString("LastName", "");
	}
	public void saveUserSelectedDealsCatgories(String selectedDeals){
		editor.putString("SelectedDeals",selectedDeals);
		editor.commit();
	}
	public String getSelectedDeals(){
		return sharedPreferences.getString("SelectedDeals", "");
	}
	public void saveUserSelectedDealsTitles(String dealsTitles){
		editor.putString("SelectedDealsTitles",dealsTitles);
		editor.commit();
	}
	public ArrayList<String> getUserSelectedDealsTitles(){
		String selectedTitles=sharedPreferences.getString("SelectedDealsTitles", "");
		ArrayList<String>dealsListTitles=new ArrayList<String>();
		int commaIndex=selectedTitles.indexOf(',');
		
		while(commaIndex!=-1){
			dealsListTitles.add(selectedTitles.substring(0,commaIndex));
			selectedTitles=selectedTitles.substring(commaIndex+1);
			commaIndex=selectedTitles.indexOf(',');
		}
		dealsListTitles.add(selectedTitles);
		return dealsListTitles;
	}

}
