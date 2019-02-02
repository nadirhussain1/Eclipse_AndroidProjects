package com.android.slidingmenuexample;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.GellaryData;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.utility.AlertDialogManager;
import com.appdupe.flamerapp.utility.ConnectionDetector;
import com.appdupe.flamerapp.utility.ExtendedGallery;
import com.appdupe.flamerapp.utility.FontsUtil;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.facebook.Session;
import com.facebook.SessionState;

public class ProfileFragment extends Fragment {

	private ExtendedGallery imageExtendedGallery=null;
	private LinearLayout image_count=null;

	private TextView userNameTextView=null;
	private TextView userAgeTextView=null;
	private TextView aboutNameTextView=null;
	private TextView aboutDescrTextView=null;
	private TextView totalMutualFriendsTextView=null;
	private TextView educationTextView=null;
	private TextView workTextView=null;
	private TextView  totalPhotosTextView = null;

	private ArrayList<GellaryData>imageList;
	private ImageAdapterForGellary mAdapterForGellary;
	private ProgressDialog mDialog=null;
	private double [] imageHeightandWIdth=new double[2];
	private ConnectionDetector cd=null;
	private  int totalGalleryImages=0;
	private  View [] dotViews=null ;

	private View profileView=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		profileView = inflater.inflate(R.layout.login_user_profile, null, false);
		ScalingUtility.getInstance(getActivity()).scaleView(profileView);

		initLayoutResource();

		imageHeightandWIdth[0]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.USER_GALLERY_PIC_WIDTH);
		imageHeightandWIdth[1]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.USER_GALLERY_PIC_HEIGHT);

		imageList=new ArrayList<GellaryData>();
		mAdapterForGellary=new ImageAdapterForGellary(getActivity(), imageList);
		imageExtendedGallery.setAdapter(mAdapterForGellary);


		//userIntestedgallery=(HorizontalListView)view.findViewById(R.id.userIntestedgallery);
		//  userfriendgallery=(HorizontalListView)view.findViewById(R.id.userfriendgallery);
		//gallery_paging=(RelativeLayout)view.findViewById(R.id.gallery_paging);
		//	    useFriendlayout=(RelativeLayout)view.findViewById(R.id.useFriendlayout);
		//	    useInterest=(RelativeLayout)view.findViewById(R.id.useInterest);
		//imagegalleylayout=(RelativeLayout)view.findViewById(R.id.imagegalleylayout);
		//userAboutLayout=(LinearLayout)view.findViewById(R.id.userAboutLayout);
		//userAboutLayout.setVisibility(View.GONE);


		new BackGroundTaskForDownloadProfileImage().execute();

		//		userintrestdata=new ArrayList<GellaryData>();
		//
		//		mAdapterForGellaryInterested=new ImageAdapterForGellaryInterested(getActivity(), userintrestdata);
		//		userIntestedgallery.setAdapter(mAdapterForGellaryInterested);

		//		userFriendlist=new ArrayList<GellaryData>();
		//		mAdapterForGellaryfriends=new ImageAdapterForGellaryfriends(getActivity(), userFriendlist);
		//		userfriendgallery.setAdapter(mAdapterForGellaryfriends);

		cd = new ConnectionDetector(getActivity());
		if (cd.isConnectingToInternet()) {
			//getUserInterest();
		}
		else{
			AlertDialogManager.internetConnetionErrorAlertDialog(getActivity());
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


		return profileView;
	}
	private void initLayoutResource(){

		imageExtendedGallery=(ExtendedGallery)profileView.findViewById(R.id.imageExtendedGallery);
		image_count=(LinearLayout)profileView.findViewById(R.id.image_count);
		userNameTextView=(TextView)profileView.findViewById(R.id.nameTextView);
		userAgeTextView=(TextView)profileView.findViewById(R.id.ageTextView);


		aboutNameTextView=(TextView)profileView.findViewById(R.id.aboutNameTextView);
		aboutDescrTextView=(TextView)profileView.findViewById(R.id.perDescriptionTextView);
		totalMutualFriendsTextView=(TextView)profileView.findViewById(R.id.totalMutualFriendsTextView);
		educationTextView=(TextView)profileView.findViewById(R.id.educationTextView);
		workTextView=(TextView)profileView.findViewById(R.id.workTextView);
		totalPhotosTextView = (TextView)profileView.findViewById(R.id.totalPhotosTextView);
		applyFontToText();	
	}
	private void applyFontToText(){
		FontsUtil.applyFontToText(getActivity(), userNameTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(getActivity(), userAgeTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(getActivity(), totalMutualFriendsTextView, FlamerConstants.HELVETICA_LIGHT);
		FontsUtil.applyFontToText(getActivity(), aboutNameTextView, FlamerConstants.HELVETICA_ROMAN);
		FontsUtil.applyFontToText(getActivity(), aboutDescrTextView, FlamerConstants.HELVETICA_ROMAN);	
	}

	@Override
	public void onResume(){
		super.onResume();
		SessionManager sessionManager=new SessionManager(getActivity());
		if (sessionManager.isImageChange()){
			sessionManager.setIsImageChange(false);	
			new BackGroundTaskForDownloadProfileImage().execute();
		}
		else if (sessionManager.getUserAbout()!=null && sessionManager.getUserAbout().length()>0) {
			aboutDescrTextView.setText(sessionManager.getUserAbout());
		}
	}

	//	private void getUserProfile()
	//	{
	//		SessionManager mSessionManager =new SessionManager(getActivity());
	//		String userFaceBookid=mSessionManager.getFacebookId();
	//		String userSessionToken=mSessionManager.getUserToken();
	//		String userDeviceId=Ultilities.getDeviceId(getActivity());		
	//
	//		String [] params={userSessionToken,userDeviceId,userFaceBookid};
	//		new BackGroundTaskForUserProfile().execute(params);
	//	}


	//	private void getUserInterest()
	//	{
	//
	//		Session mSession=Session.getActiveSession();
	//		SessionManager mSessionManager =new SessionManager(getActivity());
	//
	//		String [] params={mSessionManager.getFacebookId()};
	//		try 
	//		{
	//			if (mSession.isOpened())
	//			{
	//				getMyIntreste(params);
	//
	//			}
	//			else 
	//			{
	//				getOpenedSession();
	//			}
	//		} 
	//		catch (Exception e) 
	//		{
	//			getOpenedSession();
	//		}
	//	}

	//	private void getMyIntreste(String [] params)
	//	{
	//		Session mSession=Session.getActiveSession();
	//
	//		String fqlQuery="{\"query1\":\"SELECT pic_square,name from page where page_id IN (SELECT page_id FROM page_fan WHERE uid="+"'"+params[0]+"'"+" )\",\"query2\":\"SELECT  name, pic_square  FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 ="+"'"+params[0]+"'"+")\"}";
	//
	//
	//		Bundle param = new Bundle();
	//		param.putString("q", fqlQuery);
	//
	//		Request request = new Request(mSession,"/fql",param,HttpMethod.GET,new Request.Callback()
	//		{         
	//			public void onCompleted(Response response) 
	//			{
	//				String []  params={response.toString()} ;
	//
	//				// new BackGroundTaskForGetUserInterest().execute(params);
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
	//		private Ultilities mUltilities=new Ultilities();
	//		private UserInterestAndFriendQueryData andFriendQuaryDataIntrest;
	//		private UserInterestAndFriendQueryData andFriendQuaryDatafriend;
	//		private ArrayList<QueryOneResult> interestList;
	//		private ArrayList<QuerySecondResult> FriendList;
	//		int interestImageHeigthandwidth[]=mUltilities.getImageHeightAndWidthForInrestAndFriendsLyout(getActivity());
	//
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//			try 
	//			{
	//				intrestrespons=params[0];
	//
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
	//					getActivity().runOnUiThread(new Runnable()
	//					{						
	//						@Override
	//						public void run() 
	//						{
	//							//							
	//							layoutParams=mUltilities.getRelativelayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, interestImageHeigthandwidth[0]);
	//							layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	//							layoutParams.addRule(RelativeLayout.BELOW,R.id.useFriendlayout);
	//							layoutParams.setMargins(0, 5, 0, 0);
	//							//	useInterest.setLayoutParams(layoutParams);
	//							mAdapterForGellaryInterested.notifyDataSetChanged();
	//
	//						}
	//					});
	//
	//				}
	//
	//				//		  		if (FriendList!=null&&FriendList.size()>0) 
	//				//		  		{
	//				//		  			for (int i = 0; i < FriendList.size(); i++) 
	//				//			  		{
	//				//			  			GellaryData mGellaryData=new GellaryData();
	//				//			  			mGellaryData.setImageUrl(FriendList.get(i).getFriendPicUlt());
	//				//			  			mGellaryData.setInterestedName(FriendList.get(i).getFriendName());
	//				//			  			// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
	//				//			  			// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());
	//				//			  			
	//				//			  			userFriendlist.add(mGellaryData);
	//				//			  			
	//				//					}
	//				//			  		getActivity().runOnUiThread(new Runnable()
	//				//	                 {
	//				//						
	//				//						@Override
	//				//						public void run() {
	//				//							layoutParams=mUltilities.getRelativelayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, interestImageHeigthandwidth[0]);
	//				//							layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	//				//							layoutParams.addRule(RelativeLayout.BELOW,R.id.useragelayout);
	//				//							layoutParams.setMargins(0, 5, 0, 0);
	//				//							//useFriendlayout.setLayoutParams(layoutParams);
	//				//							
	//				//							mAdapterForGellaryfriends.notifyDataSetChanged();		
	//				//						}
	//				//					});
	//				//				}
	//				//		  		else 
	//				//		  		{
	//				//					// no any friends		  			  
	//				//				}
	//
	//			} 
	//			catch (Exception e)
	//			{
	//				//logError("BackGroundTaskForGetUserInterest  doInBackground exception"+e);
	//			}
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
	//
	//	}

	//	private class DownloadFriendImage extends AsyncTask<String, Void, String>
	//	{
	//
	//		@Override
	//		protected String doInBackground(String... params) 
	//		{
	//			if (userFriendlist!=null&&userFriendlist.size()>0) 
	//			{
	//				
	//			
	//			 for (int i = 0; i < userFriendlist.size(); i++) 
	//			   {
	//				  
	//	  			Ultilities ultilities=new Ultilities();
	//				 
	//	  			 
	//				   Bitmap mBitmapTem=Utility.getBitmap(userFriendlist.get(i).getImageUrl());
	//				  // Bitmap scaledBitmap=Bitmap.createScaledBitmap(mBitmapTem, interestImageHeigthandwidth[1], interestImageHeigthandwidth[0], true);
	//				   mBitmapTem.recycle();
	//			 	   getActivity().runOnUiThread(new Runnable() 
	//				  {
	//					
	//					@Override
	//					public void run()
	//					{
	//						mAdapterForGellaryfriends.notifyDataSetChanged();
	//						
	//					}
	//				});
	//			   }	
	//			}
	//			
	//			return null;
	//		}
	//		
	//	}

	//	private class DownLoadInteresImage extends AsyncTask<String, Void, Void>
	//	{
	//
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//
	//			if (userintrestdata!=null&&userintrestdata.size()>0) 
	//			{
	//				for (int i = 0; i < userintrestdata.size(); i++) 
	//				{
	//
	//					Ultilities ultilities=new Ultilities();
	//					// int interestImageHeigthandwidth[]=ultilities.getImageHeightAndWidthForInrestAndFriends(getActivity());
	//					Bitmap mBitmapTem=Utility.getBitmap(userintrestdata.get(i).getImageUrl());
	//					Bitmap scaledBitmap= Bitmap.createScaledBitmap(mBitmapTem, 20, 20, true);
	//					mBitmapTem.recycle();
	//					Bitmap mBitmap=	 ultilities.getCircleBitmap(scaledBitmap, 16);
	//					scaledBitmap.recycle();
	//
	//					//  userintrestdata.get(i).setmBitmap(mBitmap);
	//					getActivity().runOnUiThread(new Runnable() 
	//					{
	//
	//						@Override
	//						public void run()
	//						{
	//							mAdapterForGellaryInterested.notifyDataSetChanged();
	//
	//						}
	//					});
	//				}
	//			}
	//			else 
	//			{
	//				///
	//			}
	//			return null;
	//		}
	//
	//	}
	//

	//	private class BackGroundTaskForGetUserFreinds extends AsyncTask<String, Void, Void>
	//	{
	//	private String userFriendsrespons;
	//		private UserFriendsData userFriendsdata;
	//		private ArrayList<UserFriendsData> userfriendsdatalist;
	////		private ArrayList<GellaryData>useliksdata;
	//		private Ultilities mUltilities=new Ultilities();
	//		private UserfriendData mUserfriendData;
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//			try 
	//			{
	//				userFriendsrespons=params[0];
	//				// logDebug("BackGroundTaskForGetUserFreinds  doInBackground "+userFriendsrespons);
	//				 Gson gson = new Gson();
	//				 userFriendsrespons=userFriendsrespons.substring(userFriendsrespons.indexOf("state=")+6, userFriendsrespons.indexOf("}, error:"));
	//				// logDebug("BackGroundTaskForGetUserFreinds  doInBackground   userFriendsrespons final"+userFriendsrespons);
	//				 mUserfriendData=   gson.fromJson(userFriendsrespons, UserfriendData.class);
	//		  		// logDebug("BackGroundTaskForGetUserFreinds   doInBackground mUserProFileData "+userFriendsdata);
	//		  		userfriendsdatalist=mUserfriendData.getData();
	//		  		//useliksdata=new ArrayList<GellaryData>();
	//		  		for (int i = 0; i < userfriendsdatalist.size(); i++) 
	//		  		{
	//		  			GellaryData mGellaryData=new GellaryData();
	//		  			mGellaryData.setImageUrl(userfriendsdatalist.get(i).getFriendPicurl());
	//		  			mGellaryData.setInterestedName(userfriendsdatalist.get(i).getFriendname());
	//		  			// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
	//		  			// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());
	//		  			
	//		  			userFriendlist.add(mGellaryData);		  			
	//				}
	//		  		getActivity().runOnUiThread(new Runnable()
	//                {
	//					@Override
	//					public void run() 
	//					{
	//						mAdapterForGellaryfriends.notifyDataSetChanged();
	//						
	//					}
	//				});
	//		  		
	//		  		
	//		  		 for (int i = 0; i < userFriendlist.size(); i++) 
	//				   {
	//		  			 	Ultilities ultilities=new Ultilities();
	//					//  int interestImageHeigthandwidth[]=ultilities.getImageHeightAndWidthForInrestAndFriends(getActivity());
	//		  			 
	//					   Bitmap mBitmapTem=Utility.getBitmap(userFriendlist.get(i).getImageUrl());
	//					   mBitmapTem.recycle();
	//					   getActivity().runOnUiThread(new Runnable() 
	//					   {
	//						
	//						@Override
	//						public void run()
	//	                    {
	//							mAdapterForGellaryfriends.notifyDataSetChanged();
	//							
	//						}
	//					});
	//				   }	
	//		  		
	//			} 
	//			catch (Exception e)
	//			{
	//				// logError("BackGroundTaskForGetUserFreinds  doInBackground exception"+e);
	//			}
	//			
	//	  		
	//				
	//			
	//			return null;
	//		}
	//
	//		@Override
	//		protected void onPostExecute(Void result) {
	//			super.onPostExecute(result);
	//			
	//		}
	//
	//		@Override
	//		protected void onPreExecute() 
	//		{
	//			super.onPreExecute();
	//		}
	//		
	//		
	//		
	//	}

	private void getOpenedSession()
	{
		Session.openActiveSession(getActivity(), true, statusCallback);
	}

	//	private class BackGroundTaskForUserProfile extends AsyncTask<String, Void, Void>
	//	{
	//		Ultilities mUltilities=new Ultilities();
	//		private String getProfileResponse;
	//		private List<NameValuePair>userProfileNameValuePairList;
	//		private UserProfileData mUserProFileData;
	//		private GellaryData mGellaryData;
	//		private File imageFile;
	//		private	SessionManager sessionManager=new SessionManager(getActivity());
	//
	//		@Override
	//		protected Void doInBackground(String... params) 
	//		{
	//			try {
	//
	//				File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory)); 
	//				File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedire));
	//				mUltilities.deleteNon_EmptyDir(_picDir);
	//				userProfileNameValuePairList=mUltilities.getUserProfileParameter(params);
	//				getProfileResponse=   mUltilities.makeHttpRequest(CommonConstant.getProfile_url,CommonConstant.methodeName,userProfileNameValuePairList);
	//
	//				Gson gson = new Gson();
	//				mUserProFileData=   gson.fromJson(getProfileResponse, UserProfileData.class);
	//
	//
	//				String [] images=mUserProFileData.getImages();
	//
	//				for (int i = 0; i < images.length; i++)
	//				{
	//					mGellaryData=new GellaryData();					 
	//					mGellaryData.setImageUrl(images[i]);
	//					imageList.add(mGellaryData);
	//
	//				}
	//
	//				for (int i = 0; i < imageList.size(); i++) 
	//				{
	//					switch (i) 
	//					{
	//					case 0:
	//						sessionManager.setProFilePicture(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//					case 1:
	//						sessionManager.setProFilePicture1(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//					case 2:
	//						sessionManager.setProFilePicture2(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//					case 3:
	//						sessionManager.setProFilePicture3(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//					case 4:
	//						sessionManager.setProFilePicture4(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//					case 5:
	//						sessionManager.setProFilePicture5(imageList.get(i).getImageUrl());
	//						imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+i+".jpg");
	//
	//						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
	//						break;
	//
	//					}
	//				}
	//
	//			} catch (Exception e) 
	//			{
	//				//logError("BackGroundTaskForUserProfile   doInBackground Exception"+e);
	//			}
	//
	//			return null;
	//		}
	//
	//		@Override
	//		protected void onPostExecute(Void result) 
	//		{
	//			super.onPostExecute(result);
	//
	//			try 
	//			{
	//
	//				mAdapterForGellary.notifyDataSetChanged();
	//
	//				new BackGroundTaskForDownloadProfileImage().execute();
	//				//  logDebug("BackGroundTaskForUserProfile   onPostExecute ueragetextviw  "+ueragetextviw);
	//				//  logDebug("BackGroundTaskForUserProfile   onPostExecute usernametextivew  "+usernametextivew);
	//
	//				sessionManager.setProFileIsCallde(true);;
	//
	//
	//				ueragetextviw.setText(""+mUserProFileData.getAge());
	//				usernametextivew.setText(""+mUserProFileData.getFirstName() + ",");
	//
	//				sessionManager.setUserProfileName(mUserProFileData.getFirstName());
	//				sessionManager.setUserAge(""+mUserProFileData.getAge());
	//				if (mUserProFileData.getPersDesc()!=null&&mUserProFileData.getPersDesc().length()>0) {
	//					sessionManager.setUserAbout("");
	//					aboutuserTextview.setText(""+mUserProFileData.getPersDesc());
	//				}
	//			} 
	//			catch (Exception e) 
	//			{
	//				//logError("BackGroundTaskForUserProfile   onPostExecute Exception  "+e);
	//			}
	//		}
	//
	//		@Override
	//		protected void onPreExecute() 
	//		{
	//			super.onPreExecute();
	//		}
	//
	//	}

	private  class BackGroundTaskForDownloadProfileImage extends AsyncTask<String, Void, Void>
	{

		private Ultilities mUltilities =new Ultilities();
		private SessionManager mSessionManager=new SessionManager(getActivity());
		private   GellaryData   mGellaryData;
		private ArrayList<ImageDetail>imagelistFormdatabase;
		private DatabaseHandler databaseHandler=new DatabaseHandler(getActivity());
		
		@Override
		protected Void doInBackground(String... params) 
		{		
			try 
			{
				imagelistFormdatabase=databaseHandler.getImageDetail();

				if (imageList!=null && imageList.size()>0){
					imageList.clear();
				}
				for (int i = 0; i <imagelistFormdatabase.size(); i++) {

					mGellaryData =  new GellaryData();
					mGellaryData.setImageUrl(imagelistFormdatabase.get(i).getImageUrl());

					Bitmap mBitmap=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
					double widthFactor=imageHeightandWIdth[0]/mBitmap.getWidth();
					double heightFactor=imageHeightandWIdth[1]/mBitmap.getHeight();
					double smallestFactor=Math.min(widthFactor, heightFactor);
					
					int scaledWidth=(int) (mBitmap.getWidth()*smallestFactor);
					int scaledHeight=(int) (mBitmap.getHeight()*smallestFactor);
					
					Bitmap scaledBtmap=Bitmap.createScaledBitmap(mBitmap, scaledWidth,scaledHeight, true);
					mBitmap.recycle();
					mGellaryData.setmBitmap(scaledBtmap);
					imageList.add(mGellaryData);
				}

			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			displayDataOnScreen();
			if(mDialog !=null){
				mDialog.dismiss();
			}

		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			mDialog=mUltilities.GetProcessDialog(getActivity());	
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

					dotViews[i] = new View(getActivity());

					int width=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(45);
					int height=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(44);
					int left=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(15);

					LinearLayout.LayoutParams linearParams=new LinearLayout.LayoutParams(width, height);
					linearParams.setMargins(left, 0, 0, 0);
					dotViews[i].setLayoutParams(linearParams);
					dotViews[i].setBackgroundResource(R.drawable.empty_dot);

					image_count.addView(dotViews[i]);

				}
				dotViews[0].setBackgroundResource(R.drawable.filled_dot);
				mAdapterForGellary.notifyDataSetChanged();
			}

			userAgeTextView.setText(""+mSessionManager.getUserAge());
			userNameTextView.setText(mSessionManager.getUserProfileName()+", ");
			educationTextView.setText(""+mSessionManager.getUserEducation());
			workTextView.setText(mSessionManager.getUserWork());
			DecimalFormat formatter = new DecimalFormat("00");
			String imageCountFormatted = formatter.format(imageList.size());
			totalPhotosTextView.setText(imageCountFormatted);
			

			if (mSessionManager.getUserAbout()!=null && mSessionManager.getUserAbout().length()>0){	
				aboutNameTextView.setText("About "+""+mSessionManager.getUserProfileName());
				aboutDescrTextView.setText(""+mSessionManager.getUserAbout());
			}else{
				aboutNameTextView.setVisibility(View.GONE);
				aboutDescrTextView.setVisibility(View.GONE);
			}
		}
	}

	private class ImageAdapterForGellary extends ArrayAdapter<GellaryData>
	{
		Activity mActivity=getActivity();
		private LayoutInflater mInflater;
		private Ultilities mUltilities=new Ultilities();

		public ImageAdapterForGellary(Context context, List<GellaryData> objects){
			super(context, R.layout.galleritem, objects);
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
			if (getItem(position).getmBitmap()!=null)
			{				
				holder.mProgressBar.setVisibility(View.GONE);
				holder.imageview.setImageBitmap(getItem(position).getmBitmap() /*Bitmap.createScaledBitmap(getItem(position).getmBitmap(), imageheightandWidth[1], imageheightandWidth[0], false)*/);
			}

			return convertView;
		}

		class ViewHolder 
		{
			ImageView imageview;
			ProgressBar  mProgressBar;

		}
	}

	//	private class ImageAdapterForGellaryInterested extends ArrayAdapter<GellaryData>
	//	{
	//		//Activity mActivity=getActivity();
	//		RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
	//		private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
	//		private LayoutInflater mInflater;
	//		Typeface HelveticaLTStd_Light=Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaLTStd-Light.otf");
	//		public ImageAdapterForGellaryInterested(Context context, List<GellaryData> objects)
	//		{
	//			super(context, R.layout.myintrested, objects);
	//			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//			//logDebug("ImageAdapterForGellaryInterested   context "+context);
	//			//logDebug("ImageAdapterForGellaryInterested   objects "+objects);
	//		}
	//		@Override
	//		public int getCount()
	//		{
	//			//logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
	//			return super.getCount();
	//		}
	//		@Override
	//		public GellaryData getItem(int position)
	//		{
	//			return super.getItem(position);
	//		}
	//		@Override
	//		public View getView(int position, View convertView, ViewGroup parent) 
	//		{
	//			ViewHolder holder;
	//			//logDebug("ImageAdapterForGellaryInterested  getView position "+position);
	//			//logDebug("ImageAdapterForGellaryInterested  getView convertView "+convertView);
	//			if (convertView == null) 
	//			{
	//				holder = new ViewHolder();
	//				convertView = mInflater.inflate(R.layout.myintrested, null);
	//				holder.imageview = (NetworkImageView)convertView.findViewById(R.id.userIterestgalley);
	//				holder.textView=(TextView)convertView.findViewById(R.id.myintrestedname);
	//				convertView.setTag(holder);
	//			}
	//			else 
	//			{
	//				holder = (ViewHolder) convertView.getTag();
	//			}
	//
	//			holder.textView.setTypeface(HelveticaLTStd_Light);
	//			holder.textView.setTextColor(Color.rgb(124, 124, 124));
	//			holder.textView.setTextSize(15);
	//
	//			holder.textView.setId(position);
	//			holder.imageview.setId(position);
	//			String userInrestName=getItem(position).getInterestedName();
	//
	//			if (userInrestName!=null&&userInrestName.length()>0)
	//			{
	//				if (userInrestName.indexOf(" ")!=-1) 
	//				{
	//					userInrestName=userInrestName.substring(0, userInrestName.indexOf(" "));
	//				}
	//				else 
	//				{
	//					//do nothing
	//				}
	//
	//			}
	//			else 
	//			{
	//				userInrestName="not found";
	//			}
	//			holder.textView.setText(userInrestName);
	//			holder.imageview.setImageResource(R.drawable.circled_book_icon);
	//			//holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader,"CircularImge",getActivity());
	//			holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader);
	//			//logDebug("ImageAdapterForGellaryInterested  interes name is "+getItem(position).getInterestedName());
	//
	//			return convertView;
	//		}
	//
	//		class ViewHolder 
	//		{
	//			NetworkImageView imageview;
	//
	//
	//			TextView  textView;
	//
	//		}
	//	}
	//
	//
	//	private class ImageAdapterForGellaryfriends extends ArrayAdapter<GellaryData>
	//	{
	//		//Activity mActivity=getActivity();
	//		private LayoutInflater mInflater;
	//		RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
	//		private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
	//		Typeface HelveticaLTStd_Light=Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaLTStd-Light.otf");
	//		public ImageAdapterForGellaryfriends(Context context, List<GellaryData> objects)
	//		{
	//			super(context, R.layout.myintrested, objects);
	//			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//			//logDebug("ImageAdapterForGellaryInterested   context "+context);
	//			//logDebug("ImageAdapterForGellaryInterested   objects "+objects);
	//		}
	//		@Override
	//		public int getCount()
	//		{
	//			//logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
	//			return super.getCount();
	//		}
	//		@Override
	//		public GellaryData getItem(int position)
	//		{
	//			return super.getItem(position);
	//		}
	//		@Override
	//		public View getView(int position, View convertView, ViewGroup parent) 
	//		{
	//			ViewHolder holder;
	//			//	logDebug("ImageAdapterForGellaryInterested  getView position "+position);
	//			//	logDebug("ImageAdapterForGellaryInterested  getView convertView "+convertView);
	//			if (convertView == null) 
	//			{
	//				holder = new ViewHolder();
	//				convertView = mInflater.inflate(R.layout.myintrested, null);
	//				holder.imageview = (NetworkImageView) convertView.findViewById(R.id.userIterestgalley);
	//				holder.textView=(TextView)convertView.findViewById(R.id.myintrestedname);
	//				convertView.setTag(holder);
	//			}
	//			else 
	//			{
	//				holder = (ViewHolder) convertView.getTag();
	//			}
	//
	//			holder.textView.setTypeface(HelveticaLTStd_Light);
	//			holder.textView.setTextColor(Color.rgb(124, 124, 124));
	//			holder.textView.setTextSize(15);
	//			holder.textView.setId(position);
	//			holder.imageview.setId(position);
	//			holder.imageview.setImageResource(R.drawable.multi_user_icon);
	//			String friendName=getItem(position).getInterestedName();
	//
	//			if (friendName!=null&&friendName.length()>0)
	//			{
	//				friendName=friendName.substring(0, friendName.indexOf(" "));
	//			}
	//			else {
	//				friendName="not found";
	//			}
	//			holder.textView.setText(friendName);
	//			//holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader,"CircularImge",getActivity());
	//			holder.imageview.setImageUrl(getItem(position).getImageUrl(), imageLoader);
	//
	//			return convertView;
	//		}
	//
	//		class ViewHolder 
	//		{
	//			NetworkImageView imageview;
	//			TextView  textView;			
	//		}
	//	}

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private class SessionStatusCallback implements Session.StatusCallback
	{
		@Override
		public void call(Session session, SessionState state, Exception exception) 
		{
			// updateView();

			if (session.isOpened()) 
			{
				///Session mSession=Session.getActiveSession();
				SessionManager mSessionManager =new SessionManager(getActivity());

				String [] params={mSessionManager.getFacebookId()};
				try 
				{
					//getMyIntreste(params);
					//new BackGroundTaskForGetUserInterest().equals(params);	        			
				} 
				catch (Exception e) 
				{
					getOpenedSession();
				}
			}

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mDialog!=null){
			mDialog.dismiss();	
		}
	}

}
