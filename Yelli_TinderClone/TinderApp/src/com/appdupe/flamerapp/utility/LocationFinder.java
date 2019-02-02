package com.appdupe.flamerapp.utility;


import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.appdupe.flamerapp.LoginUsingFacebook;
/**
 * GPS Check is a public class that allows to find out current location of user
 * via GPS and also provides a GUI for the same.The GUI has two buttons to stop
 * the location search & back button. Stopping will lead to manual PoS
 * screen.The UI is launched immediately after a new activity is started.
 */

public class LocationFinder {
	private LocationManager locationManager=null;
	private LocationResult locationResult=null;
	private Location userLocation=null;
	private boolean isNetworkEnabled=false;
	private boolean isLocationFound=false;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters 
	private static final long MIN_TIME_BW_UPDATES = 3000; // 1 minute

	public void getLocation(Context context, LocationResult result) {
		//I use LocationResult callback class to pass location value from MyLocation to user code.
		locationResult=result;
		if(locationManager==null){
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if(!isNetworkEnabled){
			displayGpsEnableAlert(context);
		}
		else{
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGps);
			getLastLocation();
		}
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location){
			if(!isLocationFound){
				getLastLocation();
			}
		}
		public void onProviderDisabled(String provider) {

		}
		public void onProviderEnabled(String provider){

		}
		public void onStatusChanged(String provider, int status, Bundle extras){

		}
	};



	private void getLastLocation(){
		userLocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(userLocation !=null && !isLocationFound){
			isLocationFound=true;
			locationManager.removeUpdates(locationListenerGps);	
			locationResult.gotLocation(userLocation.getLatitude(),userLocation.getLongitude());
		}

	}

	public interface LocationResult{
		public void gotLocation(double latitude,double longitude);
	}

	public String getLocationName(Context context,double pLatitude,double pLongitude){

		Geocoder mGeoCoder = new Geocoder(context);
		String detailedAddress="";
		try {
			List<Address> addresses = mGeoCoder.getFromLocation(pLatitude,pLongitude, 10); //<10>
			for (Address address : addresses) {
				detailedAddress+=address.getAddressLine(0)+",";
			}
		} catch (IOException e) {
			Log.e("LocateMe", "Could not get Geocoder data", e);
		}
		return detailedAddress;

	}
	private void displayGpsEnableAlert(final Context context){
		new AlertDialog.Builder(context).setMessage("GPS required to get location.").setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				((Activity)context).startActivityForResult(intent,LoginUsingFacebook.GPS_REQUEST);
			}
		}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				((Activity)context).finish();
			}
		}).create().show();
	}

}
