package com.android.slidingmenuexample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.appdupe.flamerapp.LoginUsingFacebook;
import com.appdupe.flamerapp.MessagesActivity;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.adapters.DrawerListAdapter;
import com.appdupe.flamerapp.globaldata.FlamerPreferences;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.pojo.LikedMatcheData;
import com.appdupe.flamerapp.pojo.Likes;
import com.appdupe.flamerapp.screens.AllMatchesSlideWindow;
import com.appdupe.flamerapp.utility.AlertDialogManager;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.LocationFinder;
import com.appdupe.flamerapp.utility.LocationFinder.LocationResult;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.facebook.Session;
import com.facebook.SessionState;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity {

	private Button   rightMatchesButton=null;
	private Button   rightEditButton=null;
	private CharSequence mTitle=null;
	private DrawerLayout mDrawerLayout=null;
	private ListView mDrawerList=null;
	private DrawerListAdapter drawerAdapter=null;


	private double mLatitude=0;
	private double mLongitude=0;

	private  Session.StatusCallback statusCallback = new SessionStatusCallback();
	private  Dialog mdialog=null;
	private  View    rightMenuRootView=null;
	private boolean usersignup=false;
	private boolean isProfileclicked=false;
	private boolean isFromSplash=true;
	public static ArrayList<LikeMatcheddataForListview>arryList;
	public static AllMatchesSlideWindow allMatchesWindow=null;
	public Bitmap userProfileImage=null;
	private LocationFinder newLocationFinder=null;
	public static Context context=null;
	public Fragment currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		context=this;
		isFromSplash=getIntent().getBooleanExtra("FROM_SPLASH",true);

		setContentView(R.layout.slidmenuxamplemainactivity);
		initDrawerLayout();
		rightMenuRootView=findViewById(R.id.rightMenuLayout);

		arryList=new ArrayList<LikeMatcheddataForListview>();

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
		new SessionManager(this);
		setProfilePic();

		if(LoginUsingFacebook.IS_TESTING_MODE || !isFromSplash){
			proceedToUI();
		}else{
			startLocationFinder();
		}
	}
	private void startLocationFinder(){
		newLocationFinder = new LocationFinder();
		newLocationFinder.getLocation(MainActivity.this, mLocationResult);	
	}
	private LocationResult mLocationResult = new LocationResult(){ 
		public void gotLocation(final double latitude, final double longitude){
			if (!(latitude == 0.0 || longitude == 0.0)) {
				mLatitude 	= latitude;
				mLongitude 	= longitude;
				updateLocationToBackendServer();
			}else{
				proceedToUI();
			}
		}
	};
	private void updateLocationToBackendServer(){
		String deviceid=Ultilities.getDeviceId(this);
		SessionManager mSessionManager=new SessionManager(this);
		String sessionToken=mSessionManager.getUserToken();

		String params []={sessionToken,deviceid,String.valueOf(mLatitude),String.valueOf(mLongitude)};
		new LocationUpdaterTask().execute(params);
	}
	private void proceedToUI(){
		displayView(0);
		findLikedMatched();
	}
	private void initDrawerLayout(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		drawerAdapter=new DrawerListAdapter(this);
		mDrawerList.setAdapter(drawerAdapter);
		mDrawerList.setOnItemClickListener(drawerItemClickListener);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customActionBarView = inflater.inflate(R.layout.custom_action_bar, null, false);
		ScalingUtility.getInstance(this).scaleView(customActionBarView);

		getSupportActionBar().setCustomView(customActionBarView);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().getCustomView().findViewById(R.id.action_home).setOnClickListener(drawerToggleListener);
		rightMatchesButton=(Button)getSupportActionBar().getCustomView().findViewById(R.id.action_rightmenu);
		rightEditButton=(Button)getSupportActionBar().getCustomView().findViewById(R.id.action_editButton);
		rightMatchesButton.setOnClickListener(rightMatchesListener);
		rightEditButton.setOnClickListener(rightEditActionListener);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);


	}

	@Override
	protected void onResume() {
		super.onResume();
		SessionManager sessionManager=new SessionManager(this);
		if (sessionManager.isIsProfileImageChanged()){
			sessionManager.setIsProfileImageChanged(false);	
			setProfilePic();
		}
		//	TODO: Need to update GPS location data in DB here.
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
		if (mdialog!=null) 
		{
			mdialog.dismiss();
			mdialog=null;
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==LoginUsingFacebook.GPS_REQUEST){
			newLocationFinder.getLocation(MainActivity.this, mLocationResult);
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
	@Override
	public void onBackPressed() {
		if (allMatchesWindow !=null){
			allMatchesWindow.hideScreen();
			allMatchesWindow=null;
		}
		else {
			super.onBackPressed();
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (usersignup) {
				usersignup=false;
			}
		}
	}
	private void toggleDrawerLayout(){
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	private void rightMatchesAction(){
		switchAllMatchesScreen();
	}
	private void rightEditAction(){
		Intent mIntent =new Intent(MainActivity.this, EditProfileActivity.class);
		startActivity(mIntent);
	}

	private void switchAllMatchesScreen(){
		if(allMatchesWindow==null){
			allMatchesWindow=new AllMatchesSlideWindow(MainActivity.this, arryList,rightMenuRootView);
			allMatchesWindow.showScreen();
		}else{
			allMatchesWindow.hideScreen();
			allMatchesWindow=null;
		}
	}
	private void launchInviteOptions(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	private void launchSettingsActivity(){
		Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
		startActivity(settingsIntent);
	}
	private void launchMessagesActivity(){
		Intent msgIntent=new Intent(MainActivity.this,MessagesActivity.class);
		startActivity(msgIntent);
	}
	private OnItemClickListener drawerItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			isProfileclicked=false;
			rightEditButton.setVisibility(View.GONE);
			rightMatchesButton.setVisibility(View.VISIBLE);

			if(allMatchesWindow !=null){
				allMatchesWindow.hideScreen();
				allMatchesWindow=null;
			}
			if(position==2){
				launchMessagesActivity();
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				mDrawerLayout.closeDrawer(mDrawerList);
			}else if(position==4){
				launchInviteOptions();
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				mDrawerLayout.closeDrawer(mDrawerList);
			}else if(position==3){
				launchSettingsActivity();
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			else{
				displayView(position);
			}
		}
	};
	private OnClickListener drawerToggleListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			toggleDrawerLayout();

		}
	};
	private OnClickListener rightMatchesListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			rightMatchesAction();

		}
	};
	OnClickListener rightEditActionListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
           rightEditAction();

		}
	};
	private void displayView(int position){
		switch (position) {
		case 1:
			isProfileclicked=true;
			rightMatchesButton.setVisibility(View.GONE);
			rightEditButton.setVisibility(View.VISIBLE);
			currentFragment = new ProfileFragment();
			break;
		case 0:
			currentFragment = new HomeFragment();
			break;

		default:
			break;
		}

		if (currentFragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, currentFragment).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

	}
	private void setProfilePic(){
		Ultilities mUltilities=new Ultilities();
		try {
			DatabaseHandler mdaDatabaseHandler=new DatabaseHandler(this);
			String imageOrderArray[]={"1"};
			ArrayList<ImageDetail>imagelist=	mdaDatabaseHandler.getImageDetailByImageOrder(imageOrderArray);
			if (imagelist!=null && imagelist.size()>0) {

				Bitmap	bitmapimage = mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(imagelist.get(0).getSdcardpath());
				Bitmap cropedBitmap=null;
				ScalingUtilities mScalingUtilities =new ScalingUtilities();
				if (bitmapimage!=null) {
					cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage, 80, 80, ScalingLogic.CROP);
					bitmapimage.recycle();
					userProfileImage=	 mUltilities.getCircleBitmap(cropedBitmap, 1);
					cropedBitmap.recycle();

				}			
			}

		} catch (Exception e) {		
			new BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory().execute();
		}

	}

	private class BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory extends AsyncTask<ImageView, Void, Void>{
		private Ultilities mUltilities=new Ultilities();
		private SessionManager mSessionManager= new SessionManager(MainActivity.this);

		@Override
		protected Void doInBackground(ImageView... params){
			File imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+"0.jpg");
			com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(mSessionManager.getUserPrifilePck().replaceAll(" ","%20"),imageFile);
			return null;
		}
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			File _sdCard;
			File _picDir;
			File myimgFile;
			try {
				_sdCard = mUltilities.getSdCardPath();
				_picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));
				myimgFile= new File(_picDir, "profilepic"+0+".jpg"); 
				Bitmap	bitmapimage = mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(myimgFile.getAbsolutePath());

				ScalingUtilities mScalingUtilities =new ScalingUtilities();
				Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage, 80, 80, ScalingLogic.CROP);
				bitmapimage.recycle();
				userProfileImage=	 mUltilities.getCircleBitmap(cropedBitmap, 1);
				cropedBitmap.recycle();

			} catch (Exception e) {
				//logError("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory onPostExecute  Exception"+e);
			}
		}

	}

	private void findLikedMatched(){
		String deviceid=Ultilities.getDeviceId(this);
		SessionManager mSessionManager=new SessionManager(this);

		String sessionToken=mSessionManager.getUserToken();
		Ultilities mUltilitie=new Ultilities();
		String	currentdeviceTime=null;
		String curenttime=mUltilitie.getCurrentGmtTime();
		mSessionManager.setLastUpdate(curenttime);
		String params []={sessionToken,deviceid,currentdeviceTime};
		new BackgroundTaskForFindLikeMatched().execute(params);
	}
	private class BackgroundTaskForFindLikeMatched extends AsyncTask<String, Void, Void>
	{
		private Ultilities mUltilities=new Ultilities();
		private List<NameValuePair>getuserparameter;
		private String likedmatchedata;
		private LikedMatcheData matcheData;
		private ArrayList<Likes>likesList;
		private LikeMatcheddataForListview matcheddataForListview;
		DatabaseHandler mDatabaseHandler=new DatabaseHandler(MainActivity.this);
		private boolean isResponseSuccess=true;
		@Override
		protected Void doInBackground(String... params) {
			try {

				File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
				File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedirematchuserdirectory));

				getuserparameter=mUltilities.getUserLikedParameter(params);
				likedmatchedata=   mUltilities.makeHttpRequest(CommonConstant.getliked_url,CommonConstant.methodeName,getuserparameter);
				Gson gson = new Gson();
				matcheData=   gson.fromJson(likedmatchedata, LikedMatcheData.class);

				if (matcheData.getErrFlag()==0) {
					likesList= matcheData.getLikes();

					if (arryList!=null) {
						arryList.clear();
					}


					for (int i = 0; i < likesList.size(); i++) {
						matcheddataForListview=new LikeMatcheddataForListview();
						String userName=likesList.get(i).getfName();
						String facebookid=likesList.get(i).getFbId();

						String picturl =likesList.get(i).getpPic();
						int falg=likesList.get(i).getFlag();
						String latd=likesList.get(i).getLadt();

						matcheddataForListview.setFacebookid(facebookid);
						matcheddataForListview.setUserName(userName);
						matcheddataForListview.setImageUrl(picturl);
						matcheddataForListview.setFlag(""+falg);
						matcheddataForListview.setladt(latd);

						File imageFile=	mUltilities.createFileInSideDirectory(_picDir, userName+facebookid+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(likesList.get(i).getpPic().replaceAll(" ","%20"),imageFile);
						matcheddataForListview.setFilePath(imageFile.getAbsolutePath());

						arryList.add(matcheddataForListview);

					}
					DatabaseHandler mDatabaseHandler =new DatabaseHandler(MainActivity.this);
					SessionManager mSessionManager =new SessionManager(MainActivity.this);
					String userFacebookid=mSessionManager.getFacebookId();


					mDatabaseHandler.deleteMatchedlist();
					mDatabaseHandler.insertMatchList(arryList,userFacebookid);
					//////////////////////////////////////////////////////////////////////////////////

					ArrayList<LikeMatcheddataForListview>arryListtem=mDatabaseHandler.getUserFindMatch();

					if (arryListtem!=null && arryListtem.size()>0) {	
						arryList.clear();
						arryList.addAll(arryListtem);	
					}


				}
				//	"errNum": "50",
				//	 errFlag": "1",
				//	 errMsg": "Sorry, no matches found!"
				else if (matcheData.getErrFlag()==1) {
					ArrayList<LikeMatcheddataForListview>arryListtem=mDatabaseHandler.getUserFindMatch();
					if (arryListtem!=null && arryListtem.size()>0) {
						arryList.clear();
						arryList.addAll(arryListtem);
					}

				}

			} 
			catch (Exception e) {
				isResponseSuccess=false;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mdialog !=null){
				mdialog.dismiss();
			}
			if (!isResponseSuccess){
				AlertDialogManager.errorMessage(MainActivity.this, "Alert", "Request timeout");
			}else if(allMatchesWindow !=null){
				allMatchesWindow.refreshMatchesList();
			}
		}


	}
	private class LocationUpdaterTask extends AsyncTask<String,Void, Void>{
		private Ultilities mUltilities=null;
		private List<NameValuePair> locationParams=null;

		public LocationUpdaterTask(){
			mUltilities=new Ultilities();

		}
		@Override
		protected Void doInBackground(String... params) {
			locationParams=mUltilities.getUserLocationParams(params);
			String response=mUltilities.makeHttpRequest(CommonConstant.Update_Location_Url,CommonConstant.methodeName,locationParams);
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mdialog=mUltilities.GetProcessDialog(MainActivity.this);
			mdialog.setCancelable(false);
			mdialog.show();
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			FlamerPreferences.getInstance(MainActivity.this).saveLocationGeoPoint(mLatitude, mLongitude);
			proceedToUI();
		}

	}





}
