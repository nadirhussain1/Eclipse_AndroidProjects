package com.android.slidingmenuexample;


import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.flamerapp.LoginUsingFacebook;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.pojo.LogOutData;
import com.appdupe.flamerapp.pojo.UpdatePreference;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.edmodo.rangebar.RangeBar;
import com.edmodo.rangebar.RangeBar.OnRangeBarChangeListener;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class SettingsActivity extends Activity implements OnClickListener{

	private RangeBar ageRangeBar=null;
	private SeekBar distanceSeekBar=null;
	private int minAge=18;
	private int maxAge=100;

	private TextView searchDistanceTextView=null;
	private TextView showDistanceTextView=null;
	private TextView maxAgeTextView=null;
	private TextView minAgeTextView=null;
	private TextView showAgesTextView=null;
	private TextView milesTextView=null;
	private TextView kmTextView=null;
	private Button logOutButton=null;

	private int radiusDistance=60;
	private boolean isDistanceUnitKm=false;

	private ProgressDialog mDialog=null;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private boolean isDialogDismissed=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View settingsView = inflater.inflate(R.layout.settings_new_screen, null, false);
		ScalingUtility.getInstance(this).scaleView(settingsView);
		setContentView(settingsView);

		initDataList();
		initClicks();

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		if (session == null){ 
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if (session == null){
				session = new Session(this);
			}
			Session.setActiveSession(session);	
		}

		displayData();
	}

	@Override
	public void onBackPressed() {
		updateUserPrefrence();
	}
	private void initDataList(){	
		ageRangeBar=(RangeBar)findViewById(R.id.ageRangeBar);
		distanceSeekBar=(SeekBar)findViewById(R.id.searchDistanceProgressBar);
		maxAgeTextView=(TextView)findViewById(R.id.maxAgeRange);
		minAgeTextView=(TextView)findViewById(R.id.minAgeRange);
		searchDistanceTextView=(TextView)findViewById(R.id.searchDistanceTextView);
		showDistanceTextView=(TextView)findViewById(R.id.showDistanceTextView);
		showAgesTextView=(TextView)findViewById(R.id.showAgesTextView);
		kmTextView=(TextView)findViewById(R.id.KmTextView);
		milesTextView=(TextView)findViewById(R.id.milesTextView);
		logOutButton=(Button)findViewById(R.id.logOutButton);
	}
	private void initClicks(){
		ageRangeBar.setOnRangeBarChangeListener(ageRangeBarListener);
		kmTextView.setOnClickListener(this);
		milesTextView.setOnClickListener(this);
		distanceSeekBar.setOnSeekBarChangeListener(distanceSeekBarChangeListener);
		findViewById(R.id.backButtonClickArea).setOnClickListener(this);
		logOutButton.setOnClickListener(logOutClickListener);
	}
	private void displayData(){
		SessionManager mSessionManager=new SessionManager(this);
		if(mSessionManager.getUserLowerAge() !=null){
			minAge=Integer.parseInt(mSessionManager.getUserLowerAge());
		}
		if(mSessionManager.getUserHeigherAge() !=null){
			maxAge=Integer.parseInt(mSessionManager.getUserHeigherAge());
		}

		ageRangeBar.setTickCount(maxAge - minAge + 1); 
		ageRangeBar.setTickHeight(0);
		ageRangeBar.setLeft(minAge);
		ageRangeBar.setRight(maxAge);

		String showAgesString="Show ages between :"+ "<b>" +minAge+"-"+maxAge+"</b>"; 
		showAgesTextView.setText(Html.fromHtml(showAgesString));

		String searchString="";
		if (mSessionManager.getDistaceUnit().equalsIgnoreCase("Km")){
			kmTextView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			milesTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));

			isDistanceUnitKm=true;
			radiusDistance=mSessionManager.getDistance();	
			searchString ="Search within "+ "<b>" + radiusDistance + " Km</b> of current location"; 
		}
		else{
			kmTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			milesTextView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			isDistanceUnitKm=false;
			radiusDistance=mSessionManager.getDistance();

			searchString ="Search within "+ "<b>" + radiusDistance + " miles</b> of current location"; 
		}
		searchDistanceTextView.setText(Html.fromHtml(searchString));
		distanceSeekBar.setProgress(radiusDistance);	
	}
	private OnRangeBarChangeListener ageRangeBarListener=new OnRangeBarChangeListener() {

		@Override
		public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex,int rightThumbIndex) {

			minAge =18+leftThumbIndex;
			maxAge=18+rightThumbIndex;

			String showAgesString="Show ages between :"+ "<b>" +minAge+"-"+maxAge+"</b>"; 
			showAgesTextView.setText(Html.fromHtml(showAgesString));

		}
	};
	private OnClickListener logOutClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			logoutCurrentUser();	
		}
	};
	private OnSeekBarChangeListener distanceSeekBarChangeListener=new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			radiusDistance = progress;
			String searchString="";
			if (isDistanceUnitKm){ 
				searchString ="Search within "+ "<b>" + radiusDistance + " Km</b> of current location"; 
			}
			else{
				searchString ="Search within "+ "<b>" + radiusDistance + " miles</b> of current location";
			}
			searchDistanceTextView.setText(Html.fromHtml(searchString));
		}
	};
	@Override
	public void onClick(View v){		
		if (v.getId()==R.id.milesTextView){
			kmTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			milesTextView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			isDistanceUnitKm=false;

			String searchString ="Search within"+ "<b>" + radiusDistance + " miles</b> of current location"; 
			searchDistanceTextView.setText(Html.fromHtml(searchString));
		}
		else if (v.getId()==R.id.KmTextView) {

			kmTextView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			milesTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));

			isDistanceUnitKm=true;	
			String searchString ="Search within"+ "<b>" + radiusDistance + " Km</b> of current location"; 
			searchDistanceTextView.setText(Html.fromHtml(searchString));

		}else if(v.getId()==R.id.backButtonClickArea){
			updateUserPrefrence();
		}

	}

	private void logoutCurrentUser(){
		SessionManager mSessionManager=new SessionManager(this);
		String sessionToke=mSessionManager.getUserToken();
		String  deviceid=Ultilities.getDeviceId(this);
		String [] params={sessionToke,deviceid};
		new BackGroundTaskForLogout().execute(params);

	}
	//
	//	private void delettCurrentUser()
	//	{
	//		SessionManager mSessionManager=new SessionManager(this);
	//		String sessionToke=mSessionManager.getUserToken();
	//		String  deviceid=Ultilities.getDeviceId(this);
	//		String [] params={sessionToke,deviceid};
	//		new BackGroundTaskForDeleteAccount().execute(params);		
	//	}
	//
	//	private class BackGroundTaskForDeleteAccount extends AsyncTask<String, Void, Void>
	//	{
	//		private String response;
	//		private boolean responseSuccess;
	//		private List<NameValuePair>deletuserAccountParameterList;
	//		private DeleteUserAccountData deleteUserAccountData;
	//		private Ultilities mUltilities=new Ultilities();
	//
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//			deletuserAccountParameterList=mUltilities.getDeleteUserAccountParameter(params);
	//			response=   mUltilities.makeHttpRequest(CommonConstant.deleteUserAccount_url,CommonConstant.methodeName,deletuserAccountParameterList);
	//			Gson gson = new Gson();
	//			deleteUserAccountData=   gson.fromJson(response, DeleteUserAccountData.class);
	//			if (deleteUserAccountData.getErrFlag()==0&&deleteUserAccountData.getErrNum()==61) 
	//			{
	//				Session session = Session.getActiveSession();
	//				if (!session.isClosed()){
	//					session.closeAndClearTokenInformation();
	//				}
	//			}
	//			else if (deleteUserAccountData.getErrFlag()==1&&deleteUserAccountData.getErrNum()==31)
	//			{
	//				Session session = Session.getActiveSession();
	//
	//				if (!session.isClosed()){
	//					session.closeAndClearTokenInformation();
	//				}
	//
	//			}			 
	//
	//			return null;
	//		}
	//		@Override
	//		protected void onPreExecute() 
	//		{
	//			super.onPreExecute();
	//			try {
	//				mDialog=mUltilities.GetProcessDialog(SettingsActivity.this);
	//				mDialog.setMessage("Please wait..");
	//				mDialog.setCancelable(true);
	//				mDialog.show();
	//			} catch (Exception e) 
	//			{
	//				//logError("BackGroundTaskForLogout onPreExecute Exception "+e);
	//			}
	//		}
	//		@Override
	//		protected void onPostExecute(java.lang.Void result) 
	//		{
	//			super.onPostExecute(result);
	//			try
	//			{
	//				mDialog.dismiss();
	//				if (deleteUserAccountData.getErrFlag()==0&&deleteUserAccountData.getErrNum()==61) 
	//				{					
	//					//					Intent intent = new Intent(Intent.ACTION_MAIN);
	//					//					intent.addCategory(Intent.CATEGORY_HOME);
	//					//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//					//					startActivity(intent);
	//					//					this.finish();
	//					SessionManager mSessionManager=new SessionManager(SettingsActivity.this);
	//					mSessionManager.logoutUser();
	//					Intent intent=new Intent(SettingsActivity.this, LoginUsingFacebook.class);
	//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//					startActivity(intent);
	//					finish();
	//				}
	//				else if (deleteUserAccountData.getErrFlag()==1&&deleteUserAccountData.getErrNum()==62)
	//				{
	//					ErrorMessage("Alert", deleteUserAccountData.getErrMsg());
	//				}
	//				else if (deleteUserAccountData.getErrFlag()==1&&deleteUserAccountData.getErrNum()==31)
	//				{
	//					ErrorMessageInvalidSessionTOken("Alert", deleteUserAccountData.getErrMsg());
	//				}
	//				else 
	//				{
	//					// some thing wrong is happend	
	//				}
	//
	//			} catch (Exception e) 
	//			{
	//				//logError("BackGroundTaskForLogout onPostExecute Exception "+e);
	//
	//			}
	//		}
	//	}
	//
	private class BackGroundTaskForLogout extends AsyncTask<String, Void, Void>{
		private String response;
		private boolean responseSuccess=false;
		private List<NameValuePair>logOutParameter;
		private LogOutData logOutData;
		private Ultilities mUltilities=new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			try {
				logOutParameter=mUltilities.getLogOutParameter(params);
				response=   mUltilities.makeHttpRequest(CommonConstant.logout_url,CommonConstant.methodeName,logOutParameter);
				Gson gson = new Gson();
				logOutData=   gson.fromJson(response, LogOutData.class);
				if (logOutData.getErrFlag()==0&&logOutData.getErrNum()==41) {
					Session session = Session.getActiveSession();
					if (!session.isClosed()){
						session.closeAndClearTokenInformation();
						session.close();
						Session.setActiveSession(null);
					}
				}
				else if (logOutData.getErrFlag()==1 && logOutData.getErrNum()==31){
					Session session = Session.getActiveSession();
					if (!session.isClosed()){
						session.closeAndClearTokenInformation();
						session.close();
						Session.setActiveSession(null);

					}	
				}

			} catch (Exception e) {

			}


			return null;
		}
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			try
			{
				mDialog.dismiss();
				if (logOutData.getErrFlag()==0 && logOutData.getErrNum()==41) {
					SessionManager mSessionManager=new SessionManager(SettingsActivity.this);
					mSessionManager.logoutUser();
					Intent intent=new Intent(SettingsActivity.this, LoginUsingFacebook.class);
					startActivity(intent);
					SettingsActivity.this.finish();
				}
				else if (logOutData.getErrFlag()==1&&logOutData.getErrNum()==37){
					ErrorMessage("Alert", logOutData.getErrMsg());
				}
				else if (logOutData.getErrFlag()==1&&logOutData.getErrNum()==31){
					ErrorMessageInvalidSessionTOken("Alert", logOutData.getErrMsg());
				}

			} catch (Exception e) {

			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				mDialog=mUltilities.GetProcessDialog(SettingsActivity.this);
				mDialog.setMessage("Please wait..");
				mDialog.setCancelable(true);
				mDialog.show();
			} catch (Exception e) {

			}
		}	   
	}

	private void updateUserPrefrence(){
		SessionManager mSessionManager=new SessionManager(this);
		String sessionToke=mSessionManager.getUserToken();
		String  deviceid=Ultilities.getDeviceId(this);
		String loweragePrefrence=""+minAge;
		String heigherAge=""+maxAge;
		String sexPrefrence="male";
		String selectedusersex="male";
		String userSelectedRadius=""+radiusDistance;

		String diatnceUnit="Km";
		if(!isDistanceUnitKm){
			diatnceUnit="Miles";
		}
		String [] params={sessionToke,deviceid,selectedusersex,sexPrefrence,loweragePrefrence,heigherAge,userSelectedRadius,diatnceUnit};
		new BackgroundTaskForUpdatePrefrence().execute(params);
	}

	private class BackgroundTaskForUpdatePrefrence extends AsyncTask<String, Void, Void>
	{
		private String response;
		private boolean responseSuccess;
		private List<NameValuePair>prefrenceUpdateParameter;
		private UpdatePreference mUpdatePrefrence;
		private Ultilities mUltilities=new Ultilities();
		private String loweragePrefrence,heigherAge,sexPrefrence,selectedusersex,userSelectedRadius;
		@Override
		protected Void doInBackground(String... params) 
		{
			try{
				loweragePrefrence=params[4];
				heigherAge=params[5];
				userSelectedRadius=params[6];

				prefrenceUpdateParameter=mUltilities.getUserPrefrenceParameter(params);
				response=   mUltilities.makeHttpRequest(CommonConstant.updatePrefrence_url,CommonConstant.methodeName,prefrenceUpdateParameter);
				//logDebug("BackgroundTaskForUpdatePrefrence doInBackground  response "+response);
				Gson gson = new Gson();
				mUpdatePrefrence=   gson.fromJson(response, UpdatePreference.class);
			} catch (Exception e){
				Log.d("Exception",""+e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(mDialog!=null && mDialog.isShowing()){
				mDialog.dismiss();	
			}
			//if (mUpdatePrefrence.getErrFlag()==0 && mUpdatePrefrence.getErrNum()==13){
			SessionManager mSessionManager=new SessionManager(SettingsActivity.this);
			mSessionManager.setDistance(Integer.parseInt(userSelectedRadius));
			mSessionManager.setUserHeigherAge(heigherAge);
			mSessionManager.setUserLowerAge(loweragePrefrence);

			mSessionManager.setUserSex(selectedusersex);
			mSessionManager.setUserPrefSex(sexPrefrence);
			if(isDistanceUnitKm){
				mSessionManager.setDistaceUnit("Km");
			}else{
				mSessionManager.setDistaceUnit("Miles");
			}

			Toast.makeText(SettingsActivity.this, mUpdatePrefrence.getErrMsg(), Toast.LENGTH_SHORT).show();

			SettingsActivity.this.finish();


		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog=mUltilities.GetProcessDialog(SettingsActivity.this);
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}

	}

	private void ErrorMessage(String title,String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}


	private void ErrorMessageInvalidSessionTOken(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				SessionManager mSessionManager=new SessionManager(SettingsActivity.this);
				mSessionManager.logoutUser();
				Intent intent=new Intent(SettingsActivity.this, LoginUsingFacebook.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				dialog.dismiss();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}



	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			//updateView();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(this, CommonConstant.flurryKey);
	}

	@Override
	public void onStop(){
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(this);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}


}
