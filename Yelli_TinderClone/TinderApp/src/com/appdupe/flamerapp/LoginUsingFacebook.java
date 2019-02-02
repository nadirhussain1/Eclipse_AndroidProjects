/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdupe.flamerapp;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.slidingmenuexample.MainActivity;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.fragments.LoginPagerOneFragment;
import com.appdupe.flamerapp.fragments.LoginPagerTwoFragment;
import com.appdupe.flamerapp.globaldata.FlamerPreferences;
import com.appdupe.flamerapp.pojo.AgeRange;
import com.appdupe.flamerapp.pojo.Hometown;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.ImageURL;
import com.appdupe.flamerapp.pojo.Location;
import com.appdupe.flamerapp.pojo.LoginData;
import com.appdupe.flamerapp.pojo.PictureType;
import com.appdupe.flamerapp.pojo.UploadImage;
import com.appdupe.flamerapp.pojo.UserFacebookLikeData;
import com.appdupe.flamerapp.pojo.UserFacebookLikeId;
import com.appdupe.flamerapp.pojo.UserProfileData;
import com.appdupe.flamerapp.pojo.UserProfileImages;
import com.appdupe.flamerapp.pojo.YelliEducationData;
import com.appdupe.flamerapp.pojo.YelliWorkData;
import com.appdupe.flamerapp.pojo.fb.UserFaceBookInfo;
import com.appdupe.flamerapp.pojo.fb.education.FacebookConcentrationType;
import com.appdupe.flamerapp.pojo.fb.education.FacebookEducationType;
import com.appdupe.flamerapp.pojo.fb.work.FacebookWorkType;
import com.appdupe.flamerapp.utility.AlertDialogManager;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ConnectionDetector;
import com.appdupe.flamerapp.utility.GlobalUtil;
import com.appdupe.flamerapp.utility.LocationFinder;
import com.appdupe.flamerapp.utility.LocationFinder.LocationResult;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerapp.utility.Utility;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LoginUsingFacebook extends FragmentActivity {	

	private static final String TAG = "LoginUsingActivityActivity";
	public     String mDebugTag = "LoginUsingFacebookActivity";
	public final static int GPS_REQUEST=100;
	public static boolean IS_TESTING_MODE=true;

	private GoogleCloudMessaging gcm=null;
	private  String regid="";
	private ConnectionDetector cd=null;
	private   String SENDER_ID = "775207507530";

	private  android.app.ProgressDialog mdialog=null;
	private LocationFinder newLocationFinder=null;
	private double  mLatitude=0;
	private double	mLongitude=0;  
	private Ultilities mUltilities=new Ultilities();
	private UserFaceBookInfo mUserFaceBookInfo;
	private boolean isSessionOpened=false;
	private boolean isLocationReady=false;
	private boolean isLoginButtonClicked=false;

	private LoginPagerAdapter logInPagerAdapter=null;
	private ViewPager mViewPager=null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View splashView = inflater.inflate(R.layout.login_fb_mainlayout, null, false);
		if(IS_TESTING_MODE){
			isLocationReady = true;
		}
		ScalingUtility.getInstance(this).scaleView(splashView);
		setContentView(splashView);

		initViewPager();
		//generateHashCode();

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null){ 
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null,new SessionStatusCallback(), savedInstanceState);
			}
			if (session == null){
				session = new Session(this);
			}
			Session.setActiveSession(session);	
		}

		cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectingToInternet()) {	
			if(!checkPlayServices()){
				return;
			}
			else{
				regid=getRegistrationId(this);
				if (regid.isEmpty()){
					registerInBackground();
				}
				else {
					GlobalUtil.logDebug(mDebugTag,"reg id saved : "+regid);
				}             
			} 
			startLocationFind();
		}
		else {
			AlertDialogManager.internetConnetionErrorAlertDialog(LoginUsingFacebook.this);
		}		
	}
	private void startLocationFind(){
		mdialog=mUltilities.GetProcessDialog(LoginUsingFacebook.this);
		mdialog.setTitle("Finding Location ");
		mdialog.setMessage("Please Wait...");
		mdialog.setCancelable(false);
		mdialog.show();
		newLocationFinder = new LocationFinder();
		newLocationFinder.getLocation(LoginUsingFacebook.this, mLocationResult); //Commented for testing 
	}
	private void initViewPager(){
		mViewPager=(ViewPager) findViewById(R.id.pager);
		logInPagerAdapter=new LoginPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(logInPagerAdapter);

		Button facebookLoginButton=(Button)findViewById(R.id.facebookLoginButton);
		facebookLoginButton.setOnClickListener(facebookLoginClickListener);
	}

	private LocationResult mLocationResult = new LocationResult(){ 
		public void gotLocation(final double latitude, final double longitude){
			if(mdialog!=null){
				mdialog.cancel();
			}
			if (latitude == 0.0 || longitude == 0.0) {
				ErrorMessageLocationNotFonr("Alert","Current Location not found please Switch on your GPS_PROVIDER or  NETWORK_PROVIDER");
				return;
			}
			else{
				mLatitude 	= latitude;
				mLongitude 	= longitude;
				FlamerPreferences.getInstance(LoginUsingFacebook.this).saveLocationGeoPoint(latitude, longitude);
				//				if(isSessionOpened){
				//					updateView();
				//				}
			}

			isLocationReady=true;
		}
	};

	private void generateHashCode(){
		String packageName = "com.appdupe.flamerapp";

		try {
			PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Exception(NameNotFoundException) : "+e);

		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Exception(NoSuchAlgorithmException) : "+e);
		}
	}
	private void registerInBackground(){
		new GCMRegistration().execute();

	}

	private class GCMRegistration extends AsyncTask<String, Void, Void>{
		private boolean flagforresponse=true;
		private String[] params;

		@Override
		protected Void doInBackground(String... params) 
		{
			String msg = "";
			try {
				if (gcm == null){
					gcm = GoogleCloudMessaging.getInstance(LoginUsingFacebook.this);
				}
				regid = gcm.register(SENDER_ID);
				String regidfoundseccessfully="getGoogleRegistrationId";
				FlurryAgent.logEvent(regidfoundseccessfully);
				msg = "GCMRegistration doInBackground Device registered, registration ID=" + regid;
				storeRegistrationId(LoginUsingFacebook.this, regid);
			} 
			catch (IOException ex){
				msg = "Error :" + ex.getMessage();
				//	logDebug("GCMRegistration doInBackground   msg "+gcm);
				//params[18]="";
				//	this.params=params;
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			//logDebug("GCMRegistration onPostExecute  ");
			//	new BackGroundTaskForLogind().execute(params);


		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	} 

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,FlamerConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} 
			else {
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) 
	{
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(LoginUsingFacebook.class.getSimpleName(),Context.MODE_PRIVATE);
	}

	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) 
	{
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(FlamerConstants.PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			//logDebug("getRegistrationId   Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(FlamerConstants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) 
		{
			// Log.i(TAG, "App version changed.");
			//	logDebug("getRegistrationId   App version changed.");
			return "";
		}
		return registrationId;
	} 

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(FlamerConstants.PROPERTY_REG_ID, regId);
		editor.putInt(FlamerConstants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, CommonConstant.flurryKey);
	}

	@Override
	public void onStop(){
		super.onStop();
		Session.getActiveSession().removeCallback(new SessionStatusCallback());
		FlurryAgent.onEndSession(this);

		if (mdialog!=null){
			mdialog.dismiss();
			mdialog=null;
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==GPS_REQUEST){
			newLocationFinder.getLocation(LoginUsingFacebook.this, mLocationResult);
		}else{
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private void updateView() {
		Session session = Session.getActiveSession();
		isSessionOpened=false;
		if (session != null && session.isOpened()) {
			isSessionOpened=true;
		}
		if(isSessionOpened && isLocationReady && isLoginButtonClicked){
			isLoginButtonClicked=false;
			isLocationReady=false;
			new BackGroundTaskForFetchingDataFromFaceBook().execute();	
		}
	}

	private class BackGroundTaskForFetchingDataFromFaceBook extends AsyncTask<String, Void, Void> {
		private File AppDirectoryPath;
		private boolean flagForResponse=true;
		private boolean profilePictureSet=true;
		private SessionManager mSessionManager=new SessionManager(LoginUsingFacebook.this);

		private Ultilities mUltilities=new Ultilities();
		private DatabaseHandler databaseHandler=new DatabaseHandler(LoginUsingFacebook.this);

		@Override
		protected Void doInBackground(String... params) {
			try {
				Session   mCurrentSession=Session.getActiveSession();
				String   URL =	"https://graph.facebook.com/me?fields=education,work,religion,bio,id,birthday,email,gender,first_name,age_range,hometown,last_name,name,relationship_status,quotes,about,location,interested_in&access_token="+mCurrentSession.getAccessToken();
				HttpClient hc = new DefaultHttpClient();
				HttpGet get = new HttpGet(URL);
				HttpResponse rp = hc.execute(get);
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String queryAlbums = EntityUtils.toString(rp.getEntity());
					GlobalUtil.logDebug(mDebugTag,"BackGroundTaskForFetchingDataFromFaceBook   doInBackground fetch all user data Step 1 : "+ queryAlbums);
					Gson gson = new Gson();
					mUserFaceBookInfo=   gson.fromJson(queryAlbums, UserFaceBookInfo.class);

				}	

				PictureType picType = checkProfilePictureSilhouette(mUserFaceBookInfo.getFaceBookId());
				if(picType.isSilhouette()){
					throw new IllegalStateException();

				}

				AppDirectoryPath = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
				mUltilities.deleteNon_EmptyDir(AppDirectoryPath);
				databaseHandler.deleteUserData();
				databaseHandler.deleteMatchedlist();
				mSessionManager.logoutUser();

			} catch(IllegalStateException ise){
				profilePictureSet= false;
			}
			catch (Exception e) {
				flagForResponse=false;
				return null;
			} catch (Throwable e) {
				e.printStackTrace();
				flagForResponse=false;
				//logDebug("BackGroundTaskForFetchingDataFromFaceBook   doInBackground Throwable "+ e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);

			if(!profilePictureSet){
				mdialog.dismiss();
				mdialog.cancel();
				ErrorMessage("No Profile Picture?", "Sorry. Yalli requires you to have a facebook profile picture. ");
				return;

			}else if (flagForResponse){
				Location mLocation=mUserFaceBookInfo.getLocation();
				AgeRange mAgeRange=mUserFaceBookInfo.getAgeRange();
				Hometown mHometown=mUserFaceBookInfo.getHomeTown();
				
				String userEducation="";
				String userWork="";



				String firstName=mUserFaceBookInfo.getFirstName();//0
				String lastName=mUserFaceBookInfo.getLastName();//1
				String facebookId=""+mUserFaceBookInfo.getFaceBookId();	//2	
				String userEmail=mUserFaceBookInfo.getEmail();//3
				String uerGender=mUserFaceBookInfo.getGender();//4
				String authenticationType=CommonConstant.facebooAuthenticationType;//5
				String deviceType=CommonConstant.deviceType;//6
				String deviceRegid="";//7
				String qbid="34344";//8
				String city="";

				if (mHometown!=null) 
				{
					city=mHometown.getHomeTownName();//9
				}
				String country="";
				if (mLocation!=null){
					country=mLocation.getName();//10
				}

				if (mLatitude == 0.0 || mLongitude == 0.0){
					mLatitude=13.0287751;
					mLongitude=77.5896221;						
				}
				String currentLantitude=""+mLatitude/*"13.0287751"*/;//11
				String currentLogitude=""+mLongitude/*"77.5896221"*/;//12
				String usertagline=mUserFaceBookInfo.getBio();//13
				String userPersional_dec=mUserFaceBookInfo.getBio();//14

				String tempdateandtime=mUserFaceBookInfo.getBirthday();

				String userBirthday=null;
				if (tempdateandtime!=null)
				{
					userBirthday=	mUltilities.getDate(tempdateandtime, 7);
				}

				int age = -1;
				try {

					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
					Date date = format.parse(tempdateandtime);
					LocalDate birthdate = new LocalDate (date);          //Birth date
					LocalDate today = new LocalDate ();  
					Period p = new Period(birthdate, today, PeriodType.yearMonthDay());
					age = p.getYears();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				int minage=mAgeRange.getMin();
				int maxage=mAgeRange.getMax();

				if (minage==0) 
				{
					if(uerGender.equalsIgnoreCase("male") && age > -1){
						minage = age - 10;
					}else if(uerGender.equalsIgnoreCase("female") && age > -1) {
						minage = age -2;
					}else{
						minage=18;
					}
				}
				if (maxage==0) 
				{
					if(uerGender.equalsIgnoreCase("male") && age > -1){
						maxage = age + 2;
					}else if(uerGender.equalsIgnoreCase("female") && age > -1) {
						maxage = age + 10;
					}else{
						maxage = 55;
					}
				}

				String interested =null;
				if (uerGender.equalsIgnoreCase("male")) 
				{
					uerGender="1";
					interested="2";
					mSessionManager.setUserPrefSex("2");
					mSessionManager.setUserSex(uerGender);
				}
				else 
				{
					uerGender="2";
					interested="1";
					mSessionManager.setUserPrefSex("1");
					mSessionManager.setUserSex(uerGender);
				}

				String userInterested=interested;//16
				String loweraageprefrence=""+minage;//17
				String maxageprefrence=""+maxage;//18
				String userPreferenceRadius="100";//19
				mSessionManager.setDistaceUnit("Km");
				mSessionManager.setDistance(300);
				String deviceId=Ultilities.getDeviceId(LoginUsingFacebook.this);//20
				mSessionManager.setFacebookId(""+mUserFaceBookInfo.getFaceBookId());


				if (userEmail!=null&&userEmail.length()>0) 
				{

				}
				else 
				{
					Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
					Account[] accounts = AccountManager.get(LoginUsingFacebook.this).getAccounts();
					for (Account account : accounts) 
					{
						if (emailPattern.matcher(account.name).matches()) 
						{
							userEmail = account.name;
							mUserFaceBookInfo.setEmail(userEmail);
							//	logDebug(" BackGroundTaskForFetchingDataFromFaceBook onPostExecute from sync email possibleEmail   "+userEmail);

						}
					}
				}

				if (userEmail==null || userEmail.trim().length()==0) {
					userEmail="";
				}
				deviceRegid=regid/*"APA91bFDFSSYqjJMs2B125_UlHVAEou7ztZWJhrQ0gi8nr2_SLR0HQ_QyM6FYBV72TES_uAXO20VexAyf09y4QYlw823MjiOnyddiuBIP1qQZ69zpcQuv5U2p6m3GRhXmP7vLe7AO5eME7bu00QS093-oywJfW-7tg"*/;
				//logDebug("BackGroundTaskForFetchingDataFromFaceBook   from device onPostExecute regid "+ regid);

				if (deviceRegid!=null&&!deviceRegid.isEmpty()) {
					//deviceRegid="googleREGIdNotFound";
					String regidisempty="googleREGIdNotFound";
					FlurryAgent.logEvent(regidisempty);						
				}
				else {
					deviceRegid="googleREGIdNotFound";
				}

				userEducation = getUserEducationParamString(mUserFaceBookInfo);
				userWork = getUserWorkParamString(mUserFaceBookInfo);
				Log.d("Education",userEducation);
				Log.d("Work",userWork);

				String [] params=
					{
						firstName, 			//0
						lastName,  			//1
						facebookId,			//2
						userEmail,			//3
						uerGender,			//4
						city,				//5
						country,			//6
						currentLantitude,	//7
						currentLogitude,	//8
						usertagline,		//9
						userPersional_dec,	//10
						userBirthday,		//11
						userInterested,		//12
						loweraageprefrence,	//13
						maxageprefrence,	//14
						userPreferenceRadius,//15
						deviceId,			//16
						deviceRegid,		//17
						qbid,				//18
						deviceType,			//19
						authenticationType,	//20
						userEducation,      //21
						userWork,            //22
						Session.getActiveSession().getAccessToken() //23
					};
				new BackGroundTaskForLogind().execute(params);
				//getUserLikes(params);

			}
			else {
				if(mdialog!=null){
					mdialog.cancel();
				}
				ErrorMessage("Alert", "Sorry. Not able fetch your profile information from facebook!! ");
			}
		}

		private String getUserWorkParamString(UserFaceBookInfo mUserFaceBookInfo) {

			StringBuffer workParamString = new StringBuffer("");
			if(mUserFaceBookInfo.getWork() == null || mUserFaceBookInfo.getWork().size() == 0)
				return workParamString.toString();

			List<FacebookWorkType> fbWorkType =  mUserFaceBookInfo.getWork();
			int employmentOrder = 1;
			for(FacebookWorkType workItem : fbWorkType){
				if(workItem != null){
					workParamString.append(workItem.getEmployer().getName() + ":");
				}else{
					workParamString.append( ":");
				}

				if(workItem.getPosition() != null){
					workParamString.append(workItem.getPosition().getName() + ":");
				}else{
					workParamString.append( ":");
				}

				if(workItem.getStartDate() != null && !Utility.isEmptyFBDate(workItem.getStartDate())){
					workParamString.append(workItem.getStartDate() + ":");
				}else{
					workParamString.append( ":");
				}

				if(workItem.getEndDate() != null && !Utility.isEmptyFBDate(workItem.getEndDate())){
					workParamString.append(workItem.getEndDate() + ":");
				}else{
					workParamString.append( ":");
				}
				workParamString.append(employmentOrder++ + ":");


				Utility.removeLastDelimiter(workParamString, ':');
				workParamString.append(",");
			}
			Utility.removeLastDelimiter(workParamString, ',');
			return workParamString.toString();		}

		private String getUserEducationParamString(UserFaceBookInfo mUserFaceBookInfo) {

			StringBuffer educationParamString = new StringBuffer("");
			if(mUserFaceBookInfo.getEducation() == null || mUserFaceBookInfo.getEducation().size() == 0)
				return educationParamString.toString();

			List<FacebookEducationType> fbEducationType =  mUserFaceBookInfo.getEducation();
			for(FacebookEducationType educationItem : fbEducationType){
				if(educationItem.getSchool() != null){
					educationParamString.append(educationItem.getSchool().getName() + ":");
				}else{
					educationParamString.append( ":");
				}

				if(educationItem.getConcentration() != null && educationItem.getConcentration().size() != 0 ){
					educationParamString.append(getUserConcentrationParamString(educationItem.getConcentration()) + ":");
				}else{
					educationParamString.append( ":");
				}

				if(educationItem.getYear() != null && !Utility.isEmptyFBDate(educationItem.getYear().getName())){
					educationParamString.append(educationItem.getYear().getName() + ":");
				}else{
					educationParamString.append( ":");
				}

				if(educationItem.getType() != null){
					educationParamString.append(educationItem.getType() + ":");
					if(educationItem.getType().equals("Graduate School")){
						educationParamString.append(1 + ":");
					}else if(educationItem.getType().equals("College")){
						educationParamString.append(2 + ":");
					}else if(educationItem.getType().equals("High School")){
						educationParamString.append(3 + ":");
					}
				}


				Utility.removeLastDelimiter(educationParamString, ':');
				educationParamString.append(",");
			}
			Utility.removeLastDelimiter(educationParamString, ',');
			return educationParamString.toString();
		}

		private String getUserConcentrationParamString(List<FacebookConcentrationType> fbConcentrations){
			StringBuffer concentrations = new StringBuffer(0);
			for (FacebookConcentrationType concentration : fbConcentrations){
				concentrations.append(concentration.getName() + '-');

			}
			Utility.removeLastDelimiter(concentrations, '-');
			return concentrations.toString();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			try {
				mdialog=mUltilities.GetProcessDialog(LoginUsingFacebook.this);
				mdialog.setTitle("Fetching data          ");
				mdialog.setMessage("Please Wait...");
				mdialog.setCancelable(false);
				mdialog.show();
			}
			catch (Exception e)
			{
				//logDebug("BackGroundTaskForFetchingDataFromFaceBook   onPreExecute exception "+ e);
			}
		}
	}

	/*private void getUserLikes(final String [] params){
		Session mSession=Session.getActiveSession();
		String fqlQuery = "SELECT page_id FROM page_fan WHERE uid="+"'"+params[2]+"'";
		Bundle param = new Bundle();
		param.putString("q", fqlQuery);

		Request request = new Request(mSession,"/fql",param,HttpMethod.GET,new Request.Callback()
		{
			public void onCompleted(Response response) 
			{	
				try 
				{
					final String Response=response.toString();
					final String finalResponse=Response.substring(Response.indexOf("state=")+6, Response.indexOf("}, error:")); 
					GlobalUtil.logDebug(mDebugTag,"getUserLikes finalResponse "+finalResponse);
					Gson gson = new Gson();
					UserFacebookLikeData mUserFaceBookLikeData=   gson.fromJson(finalResponse, UserFacebookLikeData.class);
					ArrayList<UserFacebookLikeId>likeid=mUserFaceBookLikeData.getLikeid();
					String userlikes="";
					for (int i = 0; i < likeid.size(); i++)
					{
						userlikes=userlikes+likeid.get(i).getObject_id()+",";
						//GlobalUtil.logDebug(mDebugTag,"getUserLikes userlikes "+userlikes);

					}
					params[16]=userlikes;
					Log.e(TAG, "Now next step(BackGroundTaskForLogind) after fetching user likes");
					new BackGroundTaskForLogind().execute(params);
				} 
				catch (Exception e){
					Log.e(TAG, "Going to next step(BackGroundTaskForLogind) but user likes are not fetched.");
					new BackGroundTaskForLogind().execute(params);
				}		                    
			}   
		});

		Request.executeBatchAsync(request);
	}*/

	private class BackGroundTaskForLogind extends AsyncTask<String, Void, Void> 
	{
		private String lognResponse;
		private boolean flagForResponse=true;

		private LoginData mLoginData;

		private List<NameValuePair>loginNameValuePairList;

		protected Void doInBackground(String... params)
		{
			try 
			{
				loginNameValuePairList=mUltilities.getLoginParameter(params);

				Log.e("LOGIN PARAMS", loginNameValuePairList.toString());	//CHECKING PARAMS 
				lognResponse =  mUltilities.makeHttpRequest(CommonConstant.login_url,CommonConstant.methodeName, loginNameValuePairList);
				Log.i(TAG, "BackGroundTaskForLogind lognResponse JSON RESP :  "+lognResponse);
				Gson gson = new Gson();
				mLoginData =   gson.fromJson(lognResponse, LoginData.class);
				Log.i(TAG, " Gson RESP :  "+mLoginData);
				if (mLoginData.getErrFlag()==0 && mLoginData.getErrNum()==2 && mLoginData.getProfilePic()!=null){
					
						File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
						File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedirectory));
						File imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+"0.jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(mLoginData.getProfilePic().replaceAll(" ","%20"),imageFile);
					
				}
				else if ((mLoginData.getErrFlag()==0 && mLoginData.getErrNum()==3) ||mLoginData.getProfilePic()==null) 
				{
					// signup 
				}
				else {
					// do nothing 
					flagForResponse=false; // originally not commented
					//logError("BackGroundTaskForLogind doInBackground mLoginData"+mLoginData);
				}
			}
			catch(Exception e)
			{
				flagForResponse=false; //  originally not commented
				//logError("BackGroundTaskForLogind doInBackground Exception"+e);
			}
			return null;
		}

		protected void onPostExecute(Void feed) 
		{
			super.onPostExecute(feed);

			if (flagForResponse)
			{
				try
				{
					GlobalUtil.logDebug(mDebugTag,"BackGroundTaskForLogind doInBackground message"+mLoginData.getLoginMasseage());
					if (mLoginData.getErrFlag()==0 && mLoginData.getErrNum()==2 && mLoginData.getProfilePic()!=null) {
						SessionManager mSessionManager=new SessionManager(LoginUsingFacebook.this);
						mSessionManager.setUserToken(mLoginData.getUserToken());
						mSessionManager.setLastUpdate(mLoginData.getJoined());
						getUserProfile();		
					}
					else if ((mLoginData.getErrFlag()==0 && mLoginData.getErrNum()==3) || mLoginData.getProfilePic()==null){
						SessionManager mSessionManager=new SessionManager(LoginUsingFacebook.this);
						mSessionManager.setUserToken(mLoginData.getUserToken());
						mSessionManager.setLastUpdate(mLoginData.getJoined());
						getProfildPic();
					}
					else{
						mUltilities.showDialogConfirm(LoginUsingFacebook.this, getResources().getString(R.string.loginfaildalerttilel), mLoginData.getLoginMasseage(), true).show();
						mdialog.dismiss();
					}


				} catch (Exception e) 
				{
					//logError("BackGroundTaskForLogind   onPostExecute Exception "+ e);
					mdialog.dismiss();

				}
			}
			else 
			{
				//logError("BackGroundTaskForLogind onPostExecute");	
				ErrorMessage("Alert ", "Sorry! Server not able to processs your request");
				mdialog.dismiss();
			}
		}
		@Override
		protected void onPreExecute() 
		{
			mdialog.setTitle("Creating profile            ");
			mdialog.setMessage("Please Wait...");
			super.onPreExecute();
		}
	}

	private PictureType checkProfilePictureSilhouette(long fbId) {

		final PictureType isSilhouette = new PictureType();

		String fqlQuery = "SELECT is_silhouette FROM profile_pic WHERE id ='" + fbId + "'";
		//logDebug("checkProfilePictureSet  Query: " + fqlQuery);
		Bundle param = new Bundle();
		param.putString("q", fqlQuery);
		Session session = Session.getActiveSession();

		Request request = new Request(session,"/fql",param,HttpMethod.GET,new Request.Callback()
		{
			public void onCompleted(Response response) 
			{

				try {
					JSONObject result = response.getGraphObject().getInnerJSONObject();
					JSONArray obj = result.getJSONArray("data");
					String checkSilhouetteString = ((JSONObject)obj.get(0)).getString("is_silhouette");
					if (checkSilhouetteString != null) {
						isSilhouette.setSilhouette(Boolean.valueOf(checkSilhouetteString));

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}                  
		});

		Request.executeAndWait(request); 



		return isSilhouette;
	}


	private void getProfildPic(){
		SessionManager mSessionManager=new SessionManager(this);

		String[] params={mSessionManager.getFacebookId()};
		String fqlQuery = "select src_big from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner="+"'"+params[0]+"'"+" and name='Profile Pictures') LIMIT 5";
		Bundle param = new Bundle();
		param.putString("q", fqlQuery);
		Session session = Session.getActiveSession();
		Request request = new Request(session,"/fql",param,HttpMethod.GET,new Request.Callback(){
			public void onCompleted(Response response) 
			{
				//Log.i(TAG, "Result: " + response.toString());
				//logDebug("getProfildPic    response  "+response.toString());
				String []  params={response.toString()} ;
				new BackGroundTaskForDownload().execute(params);

			}                  
		});

		Request.executeBatchAsync(request);    
	}


	private class BackGroundTaskForDownload extends AsyncTask<String, Void, Void> // TODO BackGroundTaskForDownload
	{
		Ultilities mUltilities=new Ultilities();
		UserProfileImages mUserProfilePick;

		private String deviceid;
		//	private Ultilities mUltilities=new Ultilities();
		private String  sessionToken;
		private SessionManager mSessionManager=new SessionManager(LoginUsingFacebook.this);
		private List<NameValuePair>uploadNameValuePairList;
		private String uploadResponse;
		private UploadImage mUploadImage;
		private boolean getImageFromFaceBookResponse=true;
		@Override
		protected Void doInBackground(String... params)
		{
			try 
			{
				String picurlresponse=params[0];
				Log.e(TAG, "pic url : "+picurlresponse);
				picurlresponse=picurlresponse.substring(picurlresponse.indexOf("state=")+1, picurlresponse.indexOf("}, error"));
				picurlresponse=picurlresponse.substring(5, picurlresponse.length());
				Gson gson = new Gson();
				mUserProfilePick=   gson.fromJson(picurlresponse, UserProfileImages.class);
				ArrayList<ImageURL>poickList=mUserProfilePick.getData();
				if (poickList!=null&&poickList.size()>0) 
				{
					String profilePickUrl ="";
					String otherPicurl="";

					for (int i = 0; i < poickList.size(); i++)
					{
						if (i==0) 
						{
							profilePickUrl=poickList.get(i).getSrc();

						}

						if (i==poickList.size()-1) 
						{
							otherPicurl=otherPicurl+poickList.get(i).getSrc();
						}
						else 
						{
							otherPicurl=otherPicurl+poickList.get(i).getSrc()+",";
						}

					}


					Log.e(TAG, "profilePickUrl : "+profilePickUrl + " and otherPicurl : "+otherPicurl);

					deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(LoginUsingFacebook.this);
					sessionToken=mSessionManager.getUserToken();

					String [] uploadParameter={sessionToken,deviceid,profilePickUrl,otherPicurl};

					uploadNameValuePairList=mUltilities.getUploadParameter(uploadParameter);
					//	logDebug("BackGroundTaskForDownload doInBackground uploadNameValuePairList "+uploadNameValuePairList);

					uploadResponse=   mUltilities.makeHttpRequest(CommonConstant.uploadImage_url,CommonConstant.methodeName,uploadNameValuePairList);
					//logDebug("BackGroundTaskForDownload doInBackground uploadResponse "+uploadResponse);

					mUploadImage=   gson.fromJson(uploadResponse, UploadImage.class);

					if (mUploadImage.getErrNum()==18) 
					{
						getImageFromFaceBookResponse=true;
						mSessionManager.setProFilePicture(mUploadImage.getPicURL());
						File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
						File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedirectory));
						File imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+"0.jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(mSessionManager.getUserPrifilePck().replaceAll(" ","%20"),imageFile);
					}
					else if (mUploadImage.getErrNum()==1&&mUploadImage.getErrFlag()==1)
					{
						getImageFromFaceBookResponse=false;
					}
				}
				else 
				{
					getImageFromFaceBookResponse=false;
				}

			} 
			catch(JsonSyntaxException ex)
			{
				// Inform then user that the the Json data contains invalid syntax
				//logError("BackGroundTaskForDownload   doInBackground JsonSyntaxException "+ex);	
			}
			catch (Exception e) 
			{
				//logError("BackGroundTaskForDownload   doInBackground Exception "+e);	
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);

			try {
				getUserProfile();

			} catch (Exception e2) {
				//logError("BackGroundTaskForDownload   onPostExecute  Exception"+e2);	
			}


		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}
	}

	private void getUserProfile(){
		SessionManager mSessionManager =new SessionManager(this);

		String userFaceBookid=mSessionManager.getFacebookId();
		String userSessionToken=mSessionManager.getUserToken();
		String userDeviceId=Ultilities.getDeviceId(this);

		String [] params={userSessionToken,userDeviceId,userFaceBookid};
		new BackGroundTaskForUserProfile().execute(params);
	}

	private class BackGroundTaskForUserProfile extends AsyncTask<String, Void, Void>
	{
		Ultilities mUltilities=new Ultilities();
		private String getProfileResponse;
		private List<NameValuePair>userProfileNameValuePairList;
		private UserProfileData mUserProFileData;
		private ImageDetail imageDetail;
		private File imageFile;
		private	SessionManager sessionManager=new SessionManager(LoginUsingFacebook.this);
		private ArrayList<ImageDetail>imageList=new ArrayList<ImageDetail>();

		@Override
		protected Void doInBackground(String... params) 
		{			
			try {				
				File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
				File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedire));
				mUltilities.deleteNon_EmptyDir(_picDir);
				userProfileNameValuePairList=mUltilities.getUserProfileParameter(params);

				Log.e("userProfileNameValuePairList", userProfileNameValuePairList.toString());


				getProfileResponse=   mUltilities.makeHttpRequest(CommonConstant.getProfile_url,CommonConstant.methodeName,userProfileNameValuePairList);
				Log.e(TAG,"getProfileResponse : "+ getProfileResponse);

				Gson gson = new Gson();
				mUserProFileData=   gson.fromJson(getProfileResponse, UserProfileData.class);
				String [] images=mUserProFileData.getImages();
				for (int i = 0; i < images.length; i++){
					imageDetail=new ImageDetail();
					imageDetail.setImageUrl(images[i]);
					imageDetail.setUserFacebookid(sessionManager.getFacebookId());
					imageList.add(imageDetail);
				}
				Log.e(TAG,"imageList size : "+ imageList.size());
				for (int i = 0; i < imageList.size(); i++) {
					switch (i) 
					{
					case 0:
						sessionManager.setProFilePicture(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;
					case 1:
						sessionManager.setProFilePicture1(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;
					case 2:
						sessionManager.setProFilePicture2(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;
					case 3:
						sessionManager.setProFilePicture3(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;
					case 4:
						sessionManager.setProFilePicture4(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;
					case 5:
						sessionManager.setProFilePicture5(imageList.get(i).getImageUrl());
						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
						imageList.get(i).setSdcardpath(imageFile.getAbsolutePath());
						imageList.get(i).setImageOrder(i+1);
						break;

					}
				}
				DatabaseHandler mDatabaseHandler=new DatabaseHandler(LoginUsingFacebook.this);
				mDatabaseHandler.addImagedetal(imageList);

			} catch (Exception e) {
				Log.e(TAG,"getProfileResponse : "+ getProfileResponse + " with error nessage  " + e.getMessage());
				
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			try 
			{
				mdialog.dismiss();

				SessionManager mSessionManager=new SessionManager(LoginUsingFacebook.this);
				mSessionManager.createLoginSession();
				mSessionManager.setUserProfileName(mUserProFileData.getFirstName());
				mSessionManager.setUserAge(""+mUserProFileData.getAge());


				//logDebug("BackGroundTaskForUserProfile  onPostExecute  user persional description "+mUserProFileData.getPersDesc());
				if (mUserProFileData.getPersDesc()!=null && mUserProFileData.getPersDesc().length()>0) {
					mSessionManager.setUserAbout(mUserProFileData.getPersDesc());
				}
				else {
					sessionManager.setUserAbout("");
				}
				ArrayList<YelliEducationData> education = mUserProFileData.getEducation();
				ArrayList<YelliWorkData> work = mUserProFileData.getWork();

				if(education != null && education.size() != 0 ){
					YelliEducationData educationData = education.get(0);
					mSessionManager.setUserEducation(GlobalUtil.parseEducationRow(educationData));

				}

				if(work != null && work.size() != 0 ){
					YelliWorkData workData = work.get(0);
					mSessionManager.setUserWork(GlobalUtil.parseWorkExperienceRow(workData));

				}

				Intent intent=new Intent(LoginUsingFacebook.this,MainActivity.class);
				intent.putExtra("FROM_SPLASH", false);
				startActivity(intent);
				LoginUsingFacebook.this.finish();
			} 
			catch (Exception e) 
			{
				//logError("BackGroundTaskForUserProfile   onPostExecute Exception  "+e);
			}
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}		
	}




	/**
	 * Showing an alert dialog to usre in case of any error.
	 * 
	 * @param title - title for the dialog.
	 * @param message - error message.
	 */
	private void ErrorMessage(String title,String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginUsingFacebook.this);
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

	private void ErrorMessageLocationNotFonr(String title,String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginUsingFacebook.this);
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
	/**
	 * If the user presses the facebook button, this function is being called.
	 * 
	 * 
	 */
	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if(!session.isOpened()){
			isLoginButtonClicked=true;
			List<String> permissions = Arrays.asList("user_friends","user_education_history", "user_work_history", "user_birthday","user_religion_politics", "email","user_relationships","user_photos","user_likes","user_relationship_details","read_stream");
			Session.openActiveSession(this,true, permissions, new SessionStatusCallback());
		}

	}
	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed())
		{
			session.closeAndClearTokenInformation();
		}
	}
	private OnClickListener facebookLoginClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			onClickLogin();	
		}
	};

	/**
	 * A callback which is called from facebook when facebook button is pressed.
	 * 
	 * @author spm1
	 *
	 */

	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {			
			Log.e(TAG, "Status Call Back from fb, session : "+session + " state : "+state);
			updateView();
		}
	}

	private class LoginPagerAdapter extends FragmentStatePagerAdapter{

		public LoginPagerAdapter(FragmentManager fm) {
			super(fm);

		}
		@Override
		public Fragment getItem(int position) {
			Fragment fragment=null;

			switch (position) {
			case 0:
				fragment=new LoginPagerOneFragment();
				break;
			case 1:
				fragment=new LoginPagerTwoFragment();
				break;

			default:
				break;
			}
			return fragment;
		}


		@Override
		public int getCount() {	
			return 2;
		}


	}

}
