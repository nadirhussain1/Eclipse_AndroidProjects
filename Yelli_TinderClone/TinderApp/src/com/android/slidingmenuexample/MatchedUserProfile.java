package com.android.slidingmenuexample;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.adapters.MutualFriendsAdapter;
import com.appdupe.flamerapp.adapters.MutualLikesAdapter;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.globaldata.FlamerPreferences;
import com.appdupe.flamerapp.pojo.GellaryData;
import com.appdupe.flamerapp.pojo.InviteActionData;
import com.appdupe.flamerapp.pojo.UserProfileData;
import com.appdupe.flamerapp.pojo.YelliEducationData;
import com.appdupe.flamerapp.pojo.YelliWorkData;
import com.appdupe.flamerapp.utility.AlertDialogManager;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ConnectionDetector;
import com.appdupe.flamerapp.utility.ExtendedGallery;
import com.appdupe.flamerapp.utility.FontsUtil;
import com.appdupe.flamerapp.utility.GlobalUtil;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MatchedUserProfile extends Activity implements OnClickListener{

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private ExtendedGallery imageExtendedGallery=null;
	private ExtendedGallery mFriendsGallery=null;
	private ExtendedGallery mLikesGallery=null;
	private LinearLayout image_count=null;

	private TextView userNameTextView=null;
	private TextView userAgeTextView=null;
	private TextView distanceTextView=null;
	private TextView activeLastTimeTextView=null;
	private TextView aboutNameTextView=null;
	private TextView aboutDescrTextView=null;
	private TextView totalMutualFriendsTextView=null;
	private TextView educationTextView=null;
	private TextView workTextView=null;

	private Button likeButton,dislikebutton=null;
	private ArrayList<GellaryData>imageList=null;
	private ImageAdapterForGellary mAdapterForGellary=null;
	private MutualFriendsAdapter mFriendsAdapter=null;
	private MutualLikesAdapter mLikesAdapter=null;
	
	private ProgressDialog mDialog=null;
	private int [] imageHeightandWIdth=new int[2];
	private int [] mutualHeightandWIdth=new int[2];
	private ConnectionDetector cd=null;
	private  int totalGalleryImages=0;
	private  View [] dotViews=null ;

	private double matchLatitude=0;
	private double matchLongitude=0;

	//private RelativeLayout Aboutuseragelayout=null;
	//	private RelativeLayout likedislikebuttonlayout=null;
	//	private RelativeLayout userFriendPhotogallery=null;
	//	private RelativeLayout userInterestedGallery=null;
	//	private RelativeLayout gallery_paging=null;

	//private HorizontalListView userfriendgallery,userIntestedgallery;
	//	private ArrayList<GellaryData>userintrestdata;
	//	private ArrayList<GellaryData>userFriendlist;
	//	private ImageAdapterForGellaryInterested mAdapterForGellaryInterested;
	//	private ImageAdapterForGellaryfriends mAdapterForGellaryfriends;
	//private RelativeLayout.LayoutParams  layoutParams;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View matchedUserView = inflater.inflate(R.layout.match_user_new_profile, null, false);
		ScalingUtility.getInstance(this).scaleView(matchedUserView);
		setContentView(matchedUserView);

		initLayoutResource();

		Bundle bundle=getIntent().getExtras();
		if (bundle!=null){
			if (bundle.getBoolean(CommonConstant.isFromChatScreen)){
				likeButton.setVisibility(View.GONE);
				dislikebutton.setVisibility(View.GONE);
			}else{
				matchLatitude=Double.valueOf(bundle.getString("Latitude"));
				matchLongitude=Double.valueOf(bundle.getString("Longitude"));
			}
		}

		imageHeightandWIdth[0]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.USER_GALLERY_PIC_WIDTH);
		imageHeightandWIdth[1]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.USER_GALLERY_PIC_HEIGHT);
		
		mutualHeightandWIdth[0]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.FRIENDS_GALLERY_WIDTH);
		mutualHeightandWIdth[1]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.FRIENDS_GALLERY_HEIGHT);

		imageList=new ArrayList<GellaryData>();
		mAdapterForGellary=new ImageAdapterForGellary(this, imageList);
		imageExtendedGallery.setAdapter(mAdapterForGellary);
		
		

		//getUserProfile();		

		//		userintrestdata=new ArrayList<GellaryData>();
		//		mAdapterForGellaryInterested=new ImageAdapterForGellaryInterested(this, userintrestdata);
		//		userIntestedgallery.setAdapter(mAdapterForGellaryInterested);
		//
		//		userFriendlist=new ArrayList<GellaryData>();
		//		mAdapterForGellaryfriends=new ImageAdapterForGellaryfriends(this, userFriendlist);
		//		userfriendgallery.setAdapter(mAdapterForGellaryfriends);

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




		//		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(imageHeightandWIdth[1], imageHeightandWIdth[0]);
		//		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);


		//		layoutParams=ultilities.getRelativelayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//		layoutParams.addRule(RelativeLayout.BELOW,R.id.userinterestedlayout);
		//		layoutParams.setMargins(0, 5, 0, 0);
		//		userInterestedGallery.setLayoutParams(layoutParams);
		//
		//		layoutParams=ultilities.getRelativelayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//		layoutParams.addRule(RelativeLayout.BELOW,R.id.myfriendsnamelayout);
		//		layoutParams.setMargins(0, 5, 0, 0);
		//
		//		userFriendPhotogallery.setLayoutParams(layoutParams);	        

		cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectingToInternet()) {
			getUserProfile();
			//getUserShareeInterest();
		}
		else {
			AlertDialogManager.internetConnetionErrorAlertDialog(MatchedUserProfile.this);

		}


		imageExtendedGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
				for (int i = 0; i < totalGalleryImages; i++){
					dotViews[i].setBackgroundResource(R.drawable.empty_dot);
				}
				dotViews[pos].setBackgroundResource(R.drawable.filled_dot);

			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void initLayoutResource(){
		likeButton=(Button)findViewById(R.id.likeButton);
		dislikebutton=(Button)findViewById(R.id.dislikebutton);
		imageExtendedGallery=(ExtendedGallery)findViewById(R.id.imageExtendedGallery);
		mFriendsGallery=(ExtendedGallery)findViewById(R.id.friendsGallery);
		mLikesGallery=(ExtendedGallery)findViewById(R.id.likesGallery);
		
		image_count=(LinearLayout)findViewById(R.id.image_count);
		userNameTextView=(TextView)findViewById(R.id.nameTextView);
		userAgeTextView=(TextView)findViewById(R.id.ageTextView);
		distanceTextView=(TextView)findViewById(R.id.distanceTextView);
		activeLastTimeTextView=(TextView)findViewById(R.id.activityTimeTextView);

		aboutNameTextView=(TextView)findViewById(R.id.aboutNameTextView);
		aboutDescrTextView=(TextView)findViewById(R.id.perDescriptionTextView);
		totalMutualFriendsTextView=(TextView)findViewById(R.id.totalMutualFriendsTextView);
		educationTextView=(TextView)findViewById(R.id.educationTextView);
		workTextView=(TextView)findViewById(R.id.workTextView);
		TextView doneButtonView=(TextView)findViewById(R.id.doneTextview);



		//	userFriendPhotogallery=(RelativeLayout)findViewById(R.id.userFriendPhotogallery);
		//	userInterestedGallery=(RelativeLayout)findViewById(R.id.userInterestedGallery);
		//	userfriendgallery=(HorizontalListView)findViewById(R.id.userfriendgallery);
		//	userIntestedgallery=(HorizontalListView)findViewById(R.id.userIntestedgallery);

		applyFontToText();	
		likeButton.setOnClickListener(this);
		dislikebutton.setOnClickListener(this);
		doneButtonView.setOnClickListener(this);
	}
	private void applyFontToText(){
		FontsUtil.applyFontToText(this, userNameTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(this, userAgeTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(this, totalMutualFriendsTextView, FlamerConstants.HELVETICA_LIGHT);
		FontsUtil.applyFontToText(this, aboutNameTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(this, aboutDescrTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(this, distanceTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(this, activeLastTimeTextView, FlamerConstants.HELVETICA_ROMAN);
	}

	private void getUserProfile(){
		SessionManager mSessionManager =new SessionManager(this);

		String macheduserFacebookid=mSessionManager.getMatchedUserFacebookId();
		String userSessionToken=mSessionManager.getUserToken();
		String userDeviceId=Ultilities.getDeviceId(this);
		if (macheduserFacebookid!=null && macheduserFacebookid.length()>0&&userSessionToken!=null&&userSessionToken.length()>0&&userDeviceId!=null&&userDeviceId.length()>0)
		{
			String [] params={userSessionToken,userDeviceId,macheduserFacebookid};
			new BackGroundTaskForUserProfile().execute(params);
		}
		else {
			ErrorMessageMandetoryFiledMissing(getResources().getString(R.string.alert),getResources().getString(R.string.retriedmessage));
		}


	}

	private class BackGroundTaskForUserProfile extends AsyncTask<String, Void, Void>{
		Ultilities mUltilities=new Ultilities();
		private String getProfileResponse;
		private List<NameValuePair>userProfileNameValuePairList;
		private UserProfileData mUserProFileData;
		private GellaryData mGellaryData=null;

		@Override
		protected Void doInBackground(String... params) {
			try {

				userProfileNameValuePairList=mUltilities.getUserProfileParameter(params);
				getProfileResponse=   mUltilities.makeHttpRequest(CommonConstant.getProfile_url,CommonConstant.methodeName,userProfileNameValuePairList);
				Gson gson = new Gson();
				mUserProFileData=   gson.fromJson(getProfileResponse, UserProfileData.class);
				String [] images=mUserProFileData.getImages();

				for (int i = 0; i < images.length; i++){
					mGellaryData=new GellaryData();
					mGellaryData.setImageUrl(images[i]);
					imageList.add(mGellaryData);

				}
			} catch (Exception e){
				//logError("BackGroundTaskForUserProfile   doInBackground Exception"+e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			displayDataOnScreen();
			if (mDialog!=null){
				mDialog.dismiss();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog=mUltilities.GetProcessDialog(MatchedUserProfile.this);	
			mDialog.setMessage("Please Wait..");
			mDialog.setCancelable(false);
			mDialog.show();
		}
		private void displayDataOnScreen(){
			if(imageList !=null && imageList.size()>0){
				dotViews = new View[imageList.size()];

				image_count.removeAllViews();
				for (int i = 0; i <imageList.size(); i++) {
					totalGalleryImages=imageList.size();

					dotViews[i] = new View(MatchedUserProfile.this);

					int width=ScalingUtility.getInstance(MatchedUserProfile.this).resizeAspectFactor(45);
					int height=ScalingUtility.getInstance(MatchedUserProfile.this).resizeAspectFactor(44);
					int left=ScalingUtility.getInstance(MatchedUserProfile.this).resizeAspectFactor(15);

					LinearLayout.LayoutParams linearParams=new LinearLayout.LayoutParams(width, height);
					linearParams.setMargins(left, 0, 0, 0);
					dotViews[i].setLayoutParams(linearParams);
					dotViews[i].setBackgroundResource(R.drawable.empty_dot);

					image_count.addView(dotViews[i]);

				}
				dotViews[0].setBackgroundResource(R.drawable.filled_dot);
				mAdapterForGellary.notifyDataSetChanged();
			}

			mFriendsAdapter=new MutualFriendsAdapter(MatchedUserProfile.this, mUserProFileData.getMutualFriends());
			mLikesAdapter=new MutualLikesAdapter(MatchedUserProfile.this, mUserProFileData.getMutualLikes());
			mFriendsGallery.setAdapter(mFriendsAdapter);
			mLikesGallery.setAdapter(mLikesAdapter);
			
			userAgeTextView.setText(""+mUserProFileData.getAge());
			userNameTextView.setText(mUserProFileData.getFirstName()+", ");	
			//			if (sessionManager.getDistaceUnit().equals("Km")){
			//				DistanceUinit="Km.";   
			//			}
			//			else{
			//				DistanceUinit="Mi.";   
			//			}
			//
			//			distanceTextView.setText("Less then "+mUserProFileData.getDistance()+" "+DistanceUinit+" away");
			displayDistance();	
			displayLastActiveTime();

			if (mUserProFileData.getPersDesc()!=null && mUserProFileData.getPersDesc().length()>0){	
				aboutNameTextView.setText("About "+""+mUserProFileData.getFirstName());
				aboutDescrTextView.setText(""+mUserProFileData.getPersDesc());
			}else{
				aboutNameTextView.setVisibility(View.GONE);
				aboutDescrTextView.setVisibility(View.GONE);
			}
			ArrayList<YelliEducationData> education = mUserProFileData.getEducation();
			ArrayList<YelliWorkData> work = mUserProFileData.getWork();
			
			if(education != null && education.size() != 0 ){
				YelliEducationData educationData = education.get(0);
				educationTextView.setText(GlobalUtil.parseEducationRow(educationData));
				
			}
			
			if(work != null && work.size() != 0 ){
				YelliWorkData workData = work.get(0);
				workTextView.setText(GlobalUtil.parseWorkExperienceRow(workData));
				
			}
		}
		
		private void displayLastActiveTime(){
			String gmtTime=mUserProFileData.getLastActive();
			Date lastActiveDate=GlobalUtil.convertToDate(gmtTime);
			Date currentDate=new Date();

			long timeDiff=currentDate.getTime()-lastActiveDate.getTime();
			int days=(int) (timeDiff/(1000*60*60*24));
			timeDiff=timeDiff%(1000*60*60*24);
			int hours=(int) (timeDiff/(1000*60*60));
			timeDiff=timeDiff%(1000*60*60);
			int minutes=(int) (timeDiff/(1000*60));

			String timeText="active ";
			if(days>0){
				timeText=timeText+""+days+" days";
			}
			if(hours>0){
				timeText=timeText+" "+hours+" hours";
			}
			if(minutes>0){
				timeText=timeText+" "+minutes+" minutes";
			}
			timeText=timeText+" ago";

			activeLastTimeTextView.setText(timeText);
		}
		private void displayDistance(){
			Location matchLocation=new Location("Match_Location");
			matchLocation.setLatitude(matchLatitude);
			matchLocation.setLongitude(matchLongitude);
			distanceTextView.setText(""+calculateDistance(matchLocation)+" away");
		}
		private String calculateDistance(Location matchLocation){
			SessionManager sessionManager=new SessionManager(MatchedUserProfile.this);
			Location myLocation=FlamerPreferences.getInstance(MatchedUserProfile.this).getUserGeoLocation();
			float distance= myLocation.distanceTo(matchLocation);
			if(sessionManager.getDistaceUnit().equalsIgnoreCase("Km")){
				distance/=1000;
				return ""+distance+" Km";
			}else{
				distance/=1609.34;
				return ""+distance+" Miles";
			}

		}

	}


	//	private void getUserShareeInterest()
	//	{
	//		Session mSession=Session.getActiveSession();
	//		SessionManager mSessionManager =new SessionManager(this);
	//
	//		String [] params={mSessionManager.getFacebookId(),mSessionManager.getMatchedUserFacebookId()};
	//		try 
	//		{
	//			if (mSession.isOpened())
	//			{
	//				getMysharedIntreste(params);
	//				//new BackGroundTaskForGetUserInterest().equals(params);
	//			}
	//			else 
	//			{
	//				getOpenedSession();
	//			}
	//		} 
	//		catch (Exception e) {
	//			//logDebug("getUserShareeInterest Exception "+e);
	//			getOpenedSession();
	//		}
	//	}


	//	private void getMysharedIntreste(String [] params)
	//	{
	//		Session mSession=Session.getActiveSession();
	//
	//
	//		String fqlQuery= "{\"query1\":\"SELECT pic_square,name from page where page_id IN (SELECT page_id  FROM page_fan WHERE uid="+"'"+params[0]+"'"+"AND page_id IN (SELECT page_id FROM page_fan WHERE uid ="+"'"+params[1]+"'"+"))\",\"query2\":\" SELECT uid, name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend where uid1="+"'"+params[1]+"'"+" and uid2 in (SELECT uid2 FROM friend where uid1="+"'"+params[0]+"'"+"))\"}";
	//
	//		Bundle param = new Bundle();
	//		param.putString("q", fqlQuery);
	//		//    Session session = Session.getActiveSession();
	//		//logDebug("getMysharedIntreste  fqlQuery "+fqlQuery);
	//		Request request = new Request(mSession,"/fql",param,HttpMethod.GET,new Request.Callback()
	//		{         
	//			public void onCompleted(Response response) 
	//			{
	//				//og.i(TAG, "Result: " + response.toString());
	//
	//				String []  params={response.toString()} ;
	//				new BackGroundTaskForGetUserInterest().execute(params);
	//
	//
	//
	//			}                  
	//		});
	//
	//		Request.executeBatchAsync(request);    
	//	}





	//	private class BackGroundTaskForGetUserInterest extends AsyncTask<String, Void, Void>
	//	{
	//		private String intrestrespons;
	//		private UserInterestAndFriendData userInterestAndFriendData;
	//		private ArrayList<UserInterestAndFriendQueryData> userInterestAnsFriendlist;
	//		///	private ArrayList<GellaryData>useliksdata;
	//		private Ultilities mUltilities=new Ultilities();
	//		private UserInterestAndFriendQueryData andFriendQuaryDataIntrest;
	//		private UserInterestAndFriendQueryData andFriendQuaryDatafriend;
	//		private ArrayList<QueryOneResult> interestList;
	//		private ArrayList<QuerySecondResult> FriendList;
	//		private RelativeLayout.LayoutParams  layoutParams;
	//		private RelativeLayout.LayoutParams  layoutParamsfriend;
	//		int interestImageHeigthandwidth[]=mUltilities.getImageHeightAndWidthForInrestAndFriendsLyout(MatchedUserProfile.this);
	//
	//
	//
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//			try 
	//			{
	//				intrestrespons=params[0];				
	//				Gson gson = new Gson();
	//				intrestrespons=intrestrespons.substring(intrestrespons.indexOf("state=")+6, intrestrespons.indexOf("}, error:"));
	//				intrestrespons=intrestrespons.replaceFirst("fql_result_set", "fql_result_set1");
	//				userInterestAndFriendData=   gson.fromJson(intrestrespons, UserInterestAndFriendData.class);
	//				userInterestAnsFriendlist=userInterestAndFriendData.getDatalist();
	//				andFriendQuaryDataIntrest=userInterestAnsFriendlist.get(0);
	//				andFriendQuaryDatafriend=userInterestAnsFriendlist.get(1);
	//				interestList=andFriendQuaryDataIntrest.getInterestList();
	//				FriendList= andFriendQuaryDatafriend.getFriendList();
	//
	//				if (interestList!=null&&interestList.size()>0) 
	//				{
	//					for (int i = 0; i < interestList.size(); i++) 
	//					{
	//						GellaryData mGellaryData=new GellaryData();
	//						mGellaryData.setImageUrl(interestList.get(i).getInterestPicUlt());
	//						mGellaryData.setInterestedName(interestList.get(i).getInterestName());
	//						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
	//						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());
	//						userintrestdata.add(mGellaryData);
	//					}
	//
	//					runOnUiThread(new Runnable()
	//					{
	//
	//						@Override
	//						public void run() 
	//						{
	//							if (interestList.size()>35) 
	//							{
	//								userInterestedcount.setText("("+35+"+)");
	//							}
	//							else 
	//							{
	//								userInterestedcount.setText("("+interestList.size()+")");
	//							}
	//							mAdapterForGellaryInterested.notifyDataSetChanged();
	//
	//							layoutParams=mUltilities.getRelativelayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, interestImageHeigthandwidth[0]);
	//							layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	//							layoutParams.addRule(RelativeLayout.BELOW,R.id.userinterestedlayout);
	//							layoutParams.setMargins(0, 5, 0, 0);
	//							userInterestedGallery.setLayoutParams(layoutParams);
	//						}
	//					});
	//
	//				}
	//				else 
	//				{
	//					// no itereste adde yet
	//				}
	//
	//				if (FriendList!=null&&FriendList.size()>0) 
	//				{
	//					for (int i = 0; i < FriendList.size(); i++) 
	//					{
	//						GellaryData mGellaryData=new GellaryData();
	//						mGellaryData.setImageUrl(FriendList.get(i).getFriendPicUlt());
	//						mGellaryData.setInterestedName(FriendList.get(i).getFriendName());
	//
	//						//logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
	//						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());
	//
	//						userFriendlist.add(mGellaryData);
	//
	//					}
	//					runOnUiThread(new Runnable()
	//					{
	//
	//						@Override
	//						public void run() 
	//						{
	//							if (FriendList.size()>35) 
	//							{
	//								myfriendssharecont.setText("("+35+"+)");
	//							}
	//
	//
	//
	//							else {
	//								myfriendssharecont.setText("("+FriendList.size()+")");
	//							}
	//
	//							mAdapterForGellaryfriends.notifyDataSetChanged();
	//							layoutParamsfriend=mUltilities.getRelativelayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, interestImageHeigthandwidth[0]);
	//							layoutParamsfriend.addRule(RelativeLayout.CENTER_HORIZONTAL);
	//							layoutParamsfriend.addRule(RelativeLayout.BELOW,R.id.myfriendsnamelayout);
	//							layoutParamsfriend.setMargins(0, 5, 0, 0);
	//							userFriendPhotogallery.setLayoutParams(layoutParamsfriend);
	//
	//
	//						}
	//					});
	//				}
	//				else 
	//				{
	//					// no any friends
	//
	//				}
	//
	//			} 
	//			catch (Exception e)
	//			{
	//				//logError("BackGroundTaskForGetUserInterest  doInBackground exception"+e);
	//
	//			}
	//
	//
	//
	//
	//			return null;
	//		}
	//
	//		@Override
	//		protected void onPostExecute(Void result) 
	//		{
	//			super.onPostExecute(result);
	//
	//		}
	//
	//		@Override
	//		protected void onPreExecute() 
	//		{
	//			super.onPreExecute();
	//		}
	//	}



	private void getOpenedSession(){

		Session.openActiveSession(this, true, statusCallback);
	}

	private class ImageAdapterForGellary extends ArrayAdapter<GellaryData>
	{
		Activity mActivity;
		private LayoutInflater mInflater;
		private Ultilities mUltilities=new Ultilities();
		private int [] imageheightandWidth=mUltilities.getImageHeightAndWidthForGellary(MatchedUserProfile.this);
		public ImageAdapterForGellary(Activity context, List<GellaryData> objects)
		{
			super(context, R.layout.galleritem, objects);
			mActivity=context;
			mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount()
		{

			return super.getCount();
		}
		@Override
		public GellaryData getItem(int position)
		{

			return super.getItem(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder;

			if (convertView == null) 
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleritem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.mProgressBar=(ProgressBar)convertView.findViewById(R.id.progressBar1);
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mProgressBar.setId(position);
			holder.imageview.setId(position);

			Picasso.with(MatchedUserProfile.this) //
			.load(getItem(position).getImageUrl()) //
			/*.placeholder(R.drawable.placeholder) *///
			.error(R.drawable.error) //
			.resize(imageHeightandWIdth[0], imageHeightandWIdth[1]) //
			.into(holder.imageview);
			//			if (getItem(position).getmBitmap()!=null)
			//			{
			//				
			//			      holder.mProgressBar.setVisibility(View.GONE);
			//			      holder.imageview.setImageBitmap(getItem(position).getmBitmap());
			//			}
			//			else 
			//			{
			//				
			//			}
			return convertView;
		}



		class ViewHolder 
		{
			ImageView imageview;
			ProgressBar  mProgressBar;

		}
	}



	private class ImageAdapterForGellaryInterested extends ArrayAdapter<GellaryData>
	{

		private LayoutInflater mInflater;
		RequestQueue mRequestQueue = Volley.newRequestQueue(MatchedUserProfile.this);
		Typeface HelveticaLTStd_Light=Typeface.createFromAsset(getAssets(),"fonts/HelveticaLTStd-Light.otf");


		private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		public ImageAdapterForGellaryInterested(Context context, List<GellaryData> objects)
		{
			super(context, R.layout.myintrested, objects);
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		@Override
		public int getCount()
		{


			return super.getCount();
		}
		@Override
		public GellaryData getItem(int position)
		{

			return super.getItem(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder;

			if (convertView == null) 
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.myintrested, null);
				holder.imageview = (NetworkImageView) convertView.findViewById(R.id.userIterestgalley);
				holder.textView=(TextView)convertView.findViewById(R.id.myintrestedname);


				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setTypeface(HelveticaLTStd_Light);
			holder.textView.setTextColor(Color.rgb(124, 124, 124));
			holder.textView.setTextSize(15);
			holder.imageview.setImageResource(R.drawable.circled_book_icon);
			holder.textView.setId(position);
			holder.imageview.setId(position);

			String interestedName=getItem(position).getInterestedName();

			if (interestedName!=null && interestedName.length()>0){

				interestedName=interestedName.substring(0,interestedName.indexOf(" "));
			}
			else {
				interestedName="";	
			}
			holder.textView.setText(interestedName);
			holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader);

			return convertView;
		}


		class ViewHolder {
			NetworkImageView imageview;
			TextView  textView;
		}
	}




	private class ImageAdapterForGellaryfriends extends ArrayAdapter<GellaryData>
	{
		//Activity mActivity=getActivity();
		private LayoutInflater mInflater;
		RequestQueue mRequestQueue = Volley.newRequestQueue(MatchedUserProfile.this);
		Typeface HelveticaLTStd_Light=Typeface.createFromAsset(getAssets(),"fonts/HelveticaLTStd-Light.otf");
		private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		public ImageAdapterForGellaryfriends(Context context, List<GellaryData> objects)
		{
			super(context, R.layout.myintrested, objects);
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//logDebug("ImageAdapterForGellaryInterested   context "+context);
			//logDebug("ImageAdapterForGellaryInterested   objects "+objects);
		}
		@Override
		public int getCount()
		{

			//logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
			return super.getCount();
		}
		@Override
		public GellaryData getItem(int position)
		{

			return super.getItem(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder;


			if (convertView == null) 
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.myintrested, null);
				holder.imageview = (NetworkImageView) convertView.findViewById(R.id.userIterestgalley);
				holder.textView=(TextView)convertView.findViewById(R.id.myintrestedname);

				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setTypeface(HelveticaLTStd_Light);
			holder.textView.setTextColor(Color.rgb(124, 124, 124));
			holder.textView.setTextSize(15);
			holder.textView.setId(position);
			holder.imageview.setId(position);

			String friendName=getItem(position).getInterestedName();
			if (friendName!=null&&friendName.length()>0) 
			{
				friendName=friendName.substring(0, friendName.indexOf(" "));
			}
			else 
			{
				friendName="not found";
			}

			holder.textView.setText(friendName);
			holder.imageview.setImageResource(R.drawable.multi_user_icon);


			//holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader, "CircularImge", MatChedUserProfile.this);
			holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader);

			return convertView;
		}

		class ViewHolder 
		{
			NetworkImageView imageview;
			TextView  textView;

		}
	}


	@Override
	public void onStart() 
	{
		super.onStart();

		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(this, CommonConstant.flurryKey);


	}

	@Override
	public void onStop()
	{
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(this);

	}







	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}





	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {

				SessionManager mSessionManager =new SessionManager(MatchedUserProfile.this);
				String [] params={mSessionManager.getFacebookId(),mSessionManager.getMatchedUserFacebookId()};
				try {

					//getMysharedIntreste(params);
					//new BackGroundTaskForGetUserInterest().equals(params);
				} 
				catch (Exception e) {
					// TODO: handle exception
					getOpenedSession();
				}
			}

		}
	}


	@Override
	public void onClick(View v) {

		if (v.getId()==R.id.likeButton) {
			likeMatchedUser("1");
		}
		if (v.getId()==R.id.dislikebutton) {
			likeMatchedUser("2");
		}else{
			finish();
		}

	}

	private void likeMatchedUser(String action)
	{
		SessionManager mSessionManager=new SessionManager(MatchedUserProfile.this);
		String sessionToke=mSessionManager.getUserToken();
		String devieceId=Ultilities.getDeviceId(MatchedUserProfile.this);
		String MatchedUserFacebookId=mSessionManager.getMatchedUserFacebookId();
		String userAction=action;
		String [] params={sessionToke,devieceId,MatchedUserFacebookId,userAction};

		new BackGroundTaskForInviteAction().execute(params);

	}
	private class BackGroundTaskForInviteAction extends AsyncTask<String, Void, Void>
	{

		private String inviteActionResponse;
		private List<NameValuePair>inviteactionparamlist;
		private InviteActionData mActionData;
		private Ultilities mUltilities=new Ultilities();

		@Override
		protected Void doInBackground(String... params)
		{

			try {
				inviteactionparamlist=mUltilities.getInviteActionParameter(params);
				inviteActionResponse=   mUltilities.makeHttpRequest(CommonConstant.inviteaction_url,CommonConstant.methodeName,inviteactionparamlist);
				Gson gson = new Gson();
				mActionData=   gson.fromJson(inviteActionResponse, InviteActionData.class);
			} catch (Exception e){
				Log.d("Exception"," Excpetion occured ="+e);
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog=mUltilities.GetProcessDialog(MatchedUserProfile.this);
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try 
			{
				if(mDialog !=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				handleResult();

			} 
			catch (Exception e) {
				Log.d("Exception"," PostExecute Excpetion occured ="+e);
				handleResult();
			}
		}
		private void handleResult(){
			if (mActionData.getErrNum()==29 && mActionData.getErrFlag()==0){
				//				SessionManager mSessionManager =new SessionManager(MatchedUserProfile.this);
				//				mSessionManager.isInviteActionSucess(true);
				((HomeFragment)((MainActivity)MainActivity.context).currentFragment).removeLikedDislikedCard();
				finish();
			}
			else if (mActionData.getErrNum()==37&&mActionData.getErrFlag()==1) {
				ErrorMessage("alrte",mActionData.getErrMsg());
			}
			else
			{
				ErrorMessage("alrte","sorry Server Error! Please try again after sometime!");
			}
		}

	}

	private void ErrorMessage(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MatchedUserProfile.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageMandetoryFiledMissing(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MatchedUserProfile.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();
				finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}



}
