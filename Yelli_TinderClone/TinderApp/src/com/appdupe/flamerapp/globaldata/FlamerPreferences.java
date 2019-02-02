package com.appdupe.flamerapp.globaldata;

import com.appdupe.flamerapp.LoginUsingFacebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class FlamerPreferences {
	
	private static FlamerPreferences flamerPreferences=null;
	private SharedPreferences preferences=null;
	private SharedPreferences.Editor editor = null;
	
	public static FlamerPreferences getInstance(Context context){
		if(flamerPreferences==null){
			flamerPreferences=new FlamerPreferences(context);
		}
		return flamerPreferences;
	}
	private FlamerPreferences(Context context){
		preferences=context.getSharedPreferences("FlamerPreferences",Context.MODE_PRIVATE);
		editor=preferences.edit();
	}
	public void saveLocationGeoPoint(double latitude,double longitude){
		editor.putFloat("User_Latitude", (float) latitude);
		editor.putFloat("User_Longitude", (float) longitude);
		
		editor.commit();
	}
	public Location getUserGeoLocation(){
		Location location=new Location("User_location");
		location.setLatitude(preferences.getFloat("User_Latitude", 0.0f));
		location.setLatitude(preferences.getFloat("User_Longitude", 0.0f));
		
		return location;
		
	}

}
