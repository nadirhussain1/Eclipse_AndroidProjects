package com.assessment.cdnlocater;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Formatter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assessment.constants.AppConstants;
import com.assessment.interfaces.AsyncLocaterResponseCallBack;
import com.assessment.model.LocationPoint;
import com.assessment.model.Server;
import com.assessment.requests.IpLocaterRequest;
import com.assessment.screens.ExtendedWebView;
import com.assessment.utilities.GlobalUtility;

public class MainActivity extends Activity implements LocationListener{

	private Server southAfrServer=null;
	private Server internationServer=null;
	private Location userLocation=null;
	private ExtendedWebView webView=null;
	ProgressDialog progressDialog=null;

	public static int nearest_server_id=AppConstants.SA_SERVER_ID;
	public  TextView networkNameTextView=null;
	public  TextView addressTextView=null;
	private int  GPS_REQUEST_CODE=200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Internet Connection check
		if(GlobalUtility.isInternetAvailable(this)){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			showProgressDialog(getResources().getString(R.string.loading_init_message));
			initUIViews();
			//finding location of both South African and Amazon Servers
			locateServers();

		}else{
			GlobalUtility.displayMessageAlert(this,getResources().getString(R.string.connection_dialog_title),getResources().getString(R.string.connection_dialog_message),true );
		}

	}
	@Override
	public void onBackPressed() {
		if(progressDialog!=null && progressDialog.isShowing()){
			return;
		}else{	
			finish();
		}

	}
	private void initUIViews(){
		webView=new ExtendedWebView(this);
		RelativeLayout webViewContainer=(RelativeLayout)findViewById(R.id.webViewContainer);
		webViewContainer.addView(webView);

		networkNameTextView=(TextView)findViewById(R.id.networkTimeTextView);
		addressTextView=(TextView)findViewById(R.id.formattedAddressTextView);

	}
	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	public  void refreshTextLabels(){
		if(nearest_server_id==AppConstants.SA_SERVER_ID){
			networkNameTextView.setText(southAfrServer.getNetwork()+","+southAfrServer.getTimeZone());
			addressTextView.setText(southAfrServer.getAddress());
		}else{
			networkNameTextView.setText(internationServer.getNetwork()+","+internationServer.getTimeZone());
			addressTextView.setText(internationServer.getAddress());
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==GPS_REQUEST_CODE){
			findUserLocation();
		}
	}
	private void locateServers(){
		locateSouthAfricanServer();
	}
	private void locateSouthAfricanServer(){
		String ip=getIpAddressFromHttpUrl(AppConstants.SA_CDN_BASE_URL);
		IpLocaterRequest southAfricanServerLocaterReq=new IpLocaterRequest(ip,southAfricanServerLocaterCallBack);
		southAfricanServerLocaterReq.execute();
	}
	private void locateInternationServer(){
		String ip=getIpAddressFromHttpUrl(AppConstants.IN_CDN_BASE_URL);
		IpLocaterRequest internationLocater=new IpLocaterRequest(ip,internationLocaterCallBack);
		internationLocater.execute();
	}
	private void  findGeoPointsOfServers(){
		southAfrServer.setLocation(findGeoLocationFromAddress(southAfrServer.getAddress()));
		internationServer.setLocation(findGeoLocationFromAddress(internationServer.getAddress()));

		findUserLocation();
	}
	private void findUserLocation(){
		LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			getLastLocation();
			findNearestServer();
		}else{
			String title=getResources().getString(R.string.gps_disabled_title);
			String message=getResources().getString(R.string.gps_disabled_message);
			enableGPSDialog(title,message);
		}
	}
	private void getLastLocation(){
		LocationManager  mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
		List<String> providers = mLocationManager.getProviders(true);

		for (String provider : providers) {
			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location == null) {
				continue;
			}
			if (userLocation == null || location.getAccuracy() < userLocation.getAccuracy()) {
				userLocation = location;
			}
		}

	}
	private void enableGPSDialog(String title,String message){
		new AlertDialog.Builder(this).setMessage(message) 
		.setTitle(title) 
		.setCancelable(true) 
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.cancel();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, GPS_REQUEST_CODE);
			} 
		}) 
		.show(); 
	}
	private void findNearestServer(){
		Location southAfricanLocation=new Location("South Africa CDN");
		southAfricanLocation.setLatitude(southAfrServer.getLocation().getLatitude());
		southAfricanLocation.setLongitude(southAfrServer.getLocation().getLongitude());

		Location internationLocation=new Location("Internation CDN");
		internationLocation.setLatitude(internationServer.getLocation().getLatitude());
		internationLocation.setLongitude(internationServer.getLocation().getLongitude());

		double distanceOfInternationServer=userLocation.distanceTo(internationLocation);
		double distanceOfSAServer=userLocation.distanceTo(southAfricanLocation);

		if(distanceOfInternationServer<distanceOfSAServer){
			nearest_server_id=AppConstants.INT_SERVER_ID;
		}else{
			nearest_server_id=AppConstants.SA_SERVER_ID;	
		}
		progressDialog.cancel();
		webView.loadImage();

	}
	@SuppressWarnings("deprecation")
	private String getDeviceWifiIpAddress(){
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return Formatter.formatIpAddress(ipAddress);	
	}
	private LocationPoint findGeoLocationFromAddress(String address){
		List<Address> foundGeocode = null;
		LocationPoint point=new LocationPoint();
		try {
			foundGeocode = new Geocoder(this).getFromLocationName(address, 1);
			double latitude=foundGeocode.get(0).getLatitude(); //getting latitude
			double longitude=foundGeocode.get(0).getLongitude();//getting longitude

			point.setLatitude(latitude);
			point.setLongitude(longitude);
		} catch (IOException e) {
			e.printStackTrace();
			String alertTitle=getResources().getString(R.string.locater_failure_title);
			String alertMessage=getResources().getString(R.string.locater_failure_message);
			GlobalUtility.displayMessageAlert(this, alertTitle, alertMessage, false);
		}

		return point;	
	}
	private String getIpAddressFromHttpUrl(String urlString){

		String ip="";
		try {
			InetAddress address = InetAddress.getByName(new URL(urlString).getHost());
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			GlobalUtility.displayExceptionToast(this,getResources().getString(R.string.exception_toast_message));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			GlobalUtility.displayExceptionToast(this,getResources().getString(R.string.exception_toast_message));
		}

		return ip;

	}
	AsyncLocaterResponseCallBack southAfricanServerLocaterCallBack=new AsyncLocaterResponseCallBack() {

		@Override
		public void onResult(String resultCode, Server server) {
			if(resultCode.equalsIgnoreCase("true")){
				southAfrServer=server;
				locateInternationServer();
			}else{
				String alertTitle=getResources().getString(R.string.locater_failure_title);
				String alertMessage=getResources().getString(R.string.locater_failure_message);
				GlobalUtility.displayMessageAlert(MainActivity.this,alertTitle, alertMessage, true);
			}

		}
	};
	AsyncLocaterResponseCallBack internationLocaterCallBack=new AsyncLocaterResponseCallBack() {

		@Override
		public void onResult(String resultCode, Server server) {
			if(resultCode.equalsIgnoreCase("true")){
				internationServer=server;
				findGeoPointsOfServers();
			}else{
				String alertTitle=getResources().getString(R.string.locater_failure_title);
				String alertMessage=getResources().getString(R.string.locater_failure_message);
				GlobalUtility.displayMessageAlert(MainActivity.this,alertTitle, alertMessage, true);
			}	

		}
	};

	@Override
	public void onLocationChanged(Location location) {
		userLocation=location;
		findNearestServer();

	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {


	}
	@Override
	public void onProviderEnabled(String provider) {


	}
	@Override
	public void onProviderDisabled(String provider) {


	}


}
