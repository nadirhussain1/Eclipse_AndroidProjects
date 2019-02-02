package com.android.slidingmenuexample;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdupe.flamerapp.LoginUsingFacebook;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.globaldata.FlamerPreferences;
import com.appdupe.flamerapp.pojo.FindMatchData;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.InviteActionData;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.pojo.MatchesData;
import com.appdupe.flamerapp.pojo.UpdateSessionData;
import com.appdupe.flamerapp.pojo.YelliEducationData;
import com.appdupe.flamerapp.pojo.YelliWorkData;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ConnectionDetector;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment implements OnDragListener,OnClickListener,com.squareup.picasso.Callback{
	private Animation animation=null;
	private LayoutInflater mInflater=null;

	private RelativeLayout swipeviewlayout=null;
	private RelativeLayout matchFindingLayout=null;
	private RelativeLayout matchFoundLayout=null;

	private ImageView userProfilImage=null;
	private ImageView waveAnimatedImageView=null;
	private ImageView likeFlashIconView=null;
	private ImageView dislikeFlashIconView=null;

	private TextView messagetextview=null;
	private Button matchedUserInfoButton=null;
	private Button likeButton,dislikeButton=null;
	private Button inviteButton=null;

	private ArrayList<MatchesData>matchedDataList=null;
	private String machedUserFaceBookid="";

	private int remainingDailyPushes;
	private int [] matchUserHeightAndWidth=new int[2];
	private int [] profileImageHeightAndWidth=new int[2];
	
	private boolean downloadcallfirsttime=true;
	private  int imageindex=0;
	private int  MatchCount;
	private int numberOfImageDownload=3;
	
	private int imageDownloadingCount=0;

	private int _xDelta;
	private int _yDelta;

	//////////////////////////    
	private int windowwidth; 
	private  int screenCenter; 
	private  int x_cord, y_cord;
	private int Likes = 0; 
	private  float alphaValue = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mInflater=inflater;
		View view = inflater.inflate(R.layout.home_fragment, null);
		ScalingUtility.getInstance(getActivity()).scaleView(view);
        screenCenter=ScalingUtility.getInstance(getActivity()).getCurrentWidth()/2;
		matchFoundLayout=(RelativeLayout)view.findViewById(R.id.matchFoundLayout);
		swipeviewlayout=(RelativeLayout)view.findViewById(R.id.swipeInfoLayout);
		matchFindingLayout=(RelativeLayout)view.findViewById(R.id.matchFindingLayout);

		waveAnimatedImageView=(ImageView)view.findViewById(R.id.circleimageview);
		userProfilImage=(ImageView)view.findViewById(R.id.userProfilImage);
		likeFlashIconView=(ImageView)view.findViewById(R.id.likeFlashIcon);
		dislikeFlashIconView=(ImageView)view.findViewById(R.id.dislikeFlashIcon);
		messagetextview=(TextView)view.findViewById(R.id.messagetextview);

		matchedUserInfoButton=(Button)view.findViewById(R.id.matchedUserInfoButton);
		likeButton=(Button)view.findViewById(R.id.likeButton);
		dislikeButton=(Button)view.findViewById(R.id.dislikeButton);
		inviteButton=(Button)view.findViewById(R.id.inviteButton);

		inviteButton.setVisibility(View.GONE);
		
		initClicks();
		loadDimensionsOfViews();
		Ultilities mUltilities=new Ultilities();
		setProfilePic(userProfilImage,profileImageHeightAndWidth[1],profileImageHeightAndWidth[0]);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
		waveAnimatedImageView.startAnimation(animation); 

		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		if (!connectionDetector.isConnectingToInternet()) {	
			mUltilities.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
		}else if(Session.getActiveSession()!=null && !Session.getActiveSession().isOpened()){	
			Session.openActiveSession(getActivity(), true, new SessionStatusCallback());	
		}else{
			Log.d("JSONRESPONSE"," Session Alread="+Session.getActiveSession());
			findMatch();
		}

		return view;

	}
	private void initClicks(){
		matchedUserInfoButton.setOnClickListener(this);
		likeButton.setOnClickListener(this);
		dislikeButton.setOnClickListener(this);
		inviteButton.setOnClickListener(this);	
	}
	private void loadDimensionsOfViews(){
		matchUserHeightAndWidth[0]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.MATCHED_USER_PIC_WIDTH);
		matchUserHeightAndWidth[1]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.MATCHED_USER_PIC_HEIGHT);

		profileImageHeightAndWidth[0]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.USER_PROFILE_PIC_WIDTH);
		profileImageHeightAndWidth[1]=ScalingUtility.getInstance(getActivity()).resizeAspectFactor(FlamerConstants.USER_PROFILE_PIC_HEIGHT);

	}
	private void loadImageIntoImageView(ImageView imageView,String imageUrl){
		Picasso.with(getActivity()) .load(imageUrl) 
		.error(R.drawable.error) 
		.resize(matchUserHeightAndWidth[0], matchUserHeightAndWidth[1])	 
		.into(imageView,this);
	}

	private void getAndDisplayMutualFriends(TextView mutualFriendsTextView,final String fbId){

		Session myFbSession = Session.getActiveSession();
		Bundle params = new Bundle();
		params.putString("fields", "context.fields(mutual_friends)");
		/* make the API call */
		new Request(
				myFbSession,
				"/{"+fbId+"}",
				params,
				HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						Log.d("JSONRESPONSE","FB ID="+fbId);
						Log.d("JSONRESPONSE",""+response.toString());
					}
				}
				).executeAsync();
	}

	
	private void addView(final ArrayList<MatchesData> matchedDataList, int remainingDailyPushes){
		MatchCount=remainingDailyPushes < matchedDataList.size() ? remainingDailyPushes : matchedDataList.size()  ;
		for (int count = 0; count <matchedDataList.size(); count++){	
			swipeviewlayout.setVisibility(View.INVISIBLE);
			View matchRootView=mInflater.inflate(R.layout.home_match_layout, null);
			ScalingUtility.getInstance(getActivity()).scaleView(matchRootView);

			ImageView matchPicView=(ImageView)matchRootView.findViewById(R.id.matchedUserPic);
			ImageView cameraIconView=(ImageView)matchRootView.findViewById(R.id.cameraIcon);
			ImageView mutualFriendsIconView=(ImageView)matchRootView.findViewById(R.id.mutualFriendsIcon);

			TextView nameTextView=(TextView)matchRootView.findViewById(R.id.nameTextView);
			TextView ageTextView=(TextView)matchRootView.findViewById(R.id.ageTextView);
			TextView totalPhotosView=(TextView)matchRootView.findViewById(R.id.totalPhotosTextView);
			TextView addressTextView=(TextView)matchRootView.findViewById(R.id.addressTextView);
			TextView educationTextView=(TextView)matchRootView.findViewById(R.id.educationTextView);
			TextView workTextView=(TextView)matchRootView.findViewById(R.id.workTextView);
			TextView totalMutualFriendsView=(TextView)matchRootView.findViewById(R.id.totalMutualFriendsTextView);
			TextView totalMutualLikesView=(TextView)matchRootView.findViewById(R.id.totalMutualLikesTextView);
			
			loadImageIntoImageView(matchPicView,matchedDataList.get(count).getpPic());
			nameTextView.setText(matchedDataList.get(count).getFirstName()+", ");
			ageTextView.setText(matchedDataList.get(count).getAge()+" ");
			
			ArrayList<YelliEducationData> education = matchedDataList.get(count).getEducation();
			ArrayList<YelliWorkData> work = matchedDataList.get(count).getWork();
			
			if(education != null && education.size() != 0 ){
				YelliEducationData educationData = education.get(0);
				educationTextView.setText(parseEducationRow(educationData));
				
			}
			
			if(work != null && work.size() != 0 ){
				YelliWorkData workData = work.get(0);
				workTextView.setText(parseWorkExperienceRow(workData));
				
			}
			
			
			int imageCount = Integer.parseInt(matchedDataList.get(count).getImageCount());
			DecimalFormat formatter = new DecimalFormat("00");
			String imageCountFormatted = formatter.format(imageCount);
			totalPhotosView.setText(imageCountFormatted);
			

			Location matchLocation=new Location("Match_Location");
			matchLocation.setLatitude(Double.valueOf(matchedDataList.get(count).getLatitude()));
			matchLocation.setLongitude(Double.valueOf(matchedDataList.get(count).getLongitude()));
			addressTextView.setText("("+calculateDistance(matchLocation)+")");
			totalMutualFriendsView.setText(formatter.format(Integer.parseInt(matchedDataList.get(count).getSharedFriends())));
			totalMutualLikesView.setText(formatter.format(Integer.parseInt(matchedDataList.get(count).getSharedLikes())));
			//getAndDisplayMutualFriends(totalMutualFriendsView,matchedDataList.get(count).getFbId());

			cameraIconView.setOnClickListener(this);
			mutualFriendsIconView.setOnClickListener(this);
			SwipeTouchListener touchListener=new SwipeTouchListener();
			//touchListener.setImageViews(likeButton, dislikeButton);
			matchRootView.setOnTouchListener(touchListener);
			swipeviewlayout.addView(matchRootView);	
		}
		swipeviewlayout.setVisibility(View.VISIBLE);
		matchFoundLayout.setVisibility(View.VISIBLE);
		matchFindingLayout.setVisibility(View.GONE);
	}

	protected String parseWorkExperienceRow(YelliWorkData workData) {
        String workExperienceString = "";
        if(workData.getPosition() != null && !workData.getPosition().isEmpty()){
        	workExperienceString = workExperienceString + workData.getPosition() + " with ";
        }else{
        	workExperienceString = workExperienceString +  "Employed with ";
        }

        if (workData.getCompany() != null && !workData.getCompany().isEmpty()) {
        	workExperienceString = workExperienceString + workData.getCompany();
        }

        
        return workExperienceString;
    }
	
	protected String parseEducationRow(YelliEducationData educationData) {
        String educationString = "";

        if (educationData.getDegree() != null && !educationData.getDegree().isEmpty()) {
        	educationString = educationString +  educationData.getDegree()  +  " from ";
        }

        educationString = educationString + educationData.getInstitution() + " "  +  educationData.getType() ;

        if (educationData.getGraduation() != null && !educationData.getGraduation().isEmpty() && !educationData.getGraduation().equals("0000-00-00")) {

        	educationString = educationString  +  " - Class of " + educationData.getGraduation();
        }
        return educationString;
    }
    

	
	
	
	
	private String calculateDistance(Location matchLocation){
		SessionManager sessionManager=new SessionManager(getActivity());
		sessionManager.setDistaceUnit("Mi");
		Location myLocation=FlamerPreferences.getInstance(getActivity()).getUserGeoLocation();
		float distance= myLocation.distanceTo(matchLocation);
		if(sessionManager.getDistaceUnit().equalsIgnoreCase("Km")){
			distance/=1000;
			return ""+distance+" Km";
		}else{
			distance/=1609.34;
			return ""+distance+" Miles";
		}

	}
	private void setProfilePic(ImageView userProfilImage,int height,int width){

		Ultilities mUltilities=new Ultilities();
		try {
			DatabaseHandler mdaDatabaseHandler=new DatabaseHandler(getActivity());
			String imageOrderArray[]={"1"};
			ArrayList<ImageDetail>imagelist=	mdaDatabaseHandler.getImageDetailByImageOrder(imageOrderArray);

			if (imagelist!=null && imagelist.size()>0) {

				Bitmap	bitmapimage = mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(imagelist.get(0).getSdcardpath());
				Bitmap cropedBitmap=null;
				ScalingUtilities mScalingUtilities =new ScalingUtilities();
				Bitmap mBitmap=null;
				if (bitmapimage!=null) {
					cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage, width, height, ScalingLogic.CROP);
					bitmapimage.recycle();
					mBitmap=	 mUltilities.getCircleBitmap(cropedBitmap, 1);
					cropedBitmap.recycle();
					userProfilImage.setImageBitmap(mBitmap);
				}

			}

		} catch (Exception e) {		
			//ImageView [] params={userProfilImage};
			//new BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory().execute(params);
		}

	}


	private void findMatch(){
		Log.d("JSONRESPONSE","Inside FindMatch()...");
		new BackGroundTaskForFindMatch().execute();
	}


	private class BackGroundTaskForFindMatch extends AsyncTask<String, Void, Void>{
		private Ultilities mUltilities=new Ultilities();
		private String  sessionToken;
		private SessionManager mSessionManager=new SessionManager(getActivity());
		private String findMatchResponse;
		private List<NameValuePair> findMatchNameValuePairList;
		private String deviceid;
		private FindMatchData mFindMatchData;
		private boolean success=true;

		@Override
		protected Void doInBackground(String... params) {
			try {
				deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(getActivity());
				sessionToken=mSessionManager.getUserToken();
				String [] findMatchParamere={sessionToken,deviceid};
				findMatchNameValuePairList=mUltilities.getFindMatchParameter(findMatchParamere);
				findMatchResponse=   mUltilities.makeHttpRequest(CommonConstant.findMatch_url,CommonConstant.methodeName,findMatchNameValuePairList);
				Gson gson = new Gson();
				mFindMatchData=   gson.fromJson(findMatchResponse, FindMatchData.class);
			} 
			catch (Exception e) {
				success=false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try { 
				if (success) {
					if ((mFindMatchData.getErrNum()==21 || mFindMatchData.getErrNum()==61) && mFindMatchData.getErrFlag()==1) {
						// no match found 
						//swipeviewlayout.setVisibility(View.GONE);
						matchFoundLayout.setVisibility(View.GONE);
						messagetextview.setText(mFindMatchData.getErorrMassage());
						CommonConstant.isMatchedFound=false;
					}
					else if (mFindMatchData.getErrNum()==9 && mFindMatchData.getErrFlag()==1) {	
						ErrorMessage("Alert",mFindMatchData.getErorrMassage());
						CommonConstant.isMatchedFound=false;
					}
					else if (mFindMatchData.getErrNum()==31 && mFindMatchData.getErrFlag()==1){
						getUpdateSessionToken();
						
					}
					else {
						matchedDataList=mFindMatchData.getMatches();
						remainingDailyPushes = mFindMatchData.getRemainingPushes();
						downloadImagesAndAddViews(numberOfImageDownload);
					}
				}
				else { 
					messagetextview.setText("Yalli could not locate matches. We'll get them to you when we find them!");
					ErrorMessageRequesTimeOut("Alert ", "Request timeout",getActivity());
					CommonConstant.isMatchedFound=false;
				}
			} 
			catch (Exception e) {
				messagetextview.setText("Yalli could not locate matches. We'll get them to you when we find them!");
				ErrorMessageRequesTimeOut("Alert ", "Request timeout",getActivity());
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}


	}


	private void downloadImagesAndAddViews(int numberOfImageDownload){
		CommonConstant.isMatchedFound=true;

		if (matchedDataList!=null && matchedDataList.size()>0) {
			String imageDownload[]={""+numberOfImageDownload};
			new BackGroundTaskForDownLoadMatcheduserImage().execute(imageDownload);
		}
		else {
			//	swipeviewlayout.setVisibility(View.GONE);
			matchFoundLayout.setVisibility(View.GONE);
			matchFindingLayout.setVisibility(View.VISIBLE);
			waveAnimatedImageView.setVisibility(View.VISIBLE);
			messagetextview.setText("There's no one new around you. We'll keep looking.");
			waveAnimatedImageView.startAnimation(animation);
			inviteButton.setVisibility(View.VISIBLE);
		}


	}

	private class BackGroundTaskForDownLoadMatcheduserImage extends AsyncTask<String, Void, Void>{
		@Override
		protected Void doInBackground(String... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (downloadcallfirsttime) {
				addView(matchedDataList, remainingDailyPushes);
			}
			else{
				CommonConstant.isMatchedFound=false;
			}
		}
	}

	private void showSwipeView(){
		animation.cancel();
		animation.reset();

		waveAnimatedImageView.clearAnimation();
		downloadcallfirsttime=false;
		//swipeviewlayout.setVisibility(View.VISIBLE);
		matchFoundLayout.setVisibility(View.VISIBLE);
		matchFindingLayout.setVisibility(View.GONE);
		waveAnimatedImageView.setVisibility(View.GONE);
		CommonConstant.isMatchedFound=false;
	}

	private void getUpdateSessionToken(){
		new BackGroundTaskForUpdateToken().execute();
	}

	private class BackGroundTaskForUpdateToken extends AsyncTask<String , Void, Void>
	{
		private Ultilities mUltilities=new Ultilities();
		private String  sessionToken;
		private SessionManager mSessionManager=new SessionManager(getActivity());
		private String updatedTokenResponse;
		private List<NameValuePair>updateTokenparameterlist;
		private String deviceid;
		private  String facebookId;
		private UpdateSessionData mUpdateSessionData;

		@Override
		protected Void doInBackground(String... params) 
		{
			try 
			{
				deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(getActivity());
				sessionToken=mSessionManager.getUserToken();
				facebookId=mSessionManager.getFacebookId();
				String [] updateTokenParameter={sessionToken,deviceid,facebookId};
				updateTokenparameterlist=mUltilities.getUpdateTokeParameter(updateTokenParameter);
				updatedTokenResponse=   mUltilities.makeHttpRequest(CommonConstant.UpdateToken_url,CommonConstant.methodeName,updateTokenparameterlist);
				Gson gson = new Gson();
				mUpdateSessionData=   gson.fromJson(updatedTokenResponse, UpdateSessionData.class);

			} 
			catch (Exception e) 
			{
			}


			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			try 
			{
				if (mUpdateSessionData.getErrNum()==59&&mUpdateSessionData.getErrFlag()==0) 
				{

					mSessionManager.setUserToken(mUpdateSessionData.getToken());
					findMatch();

				}
				else if (mUpdateSessionData.getErrNum()==60&&mUpdateSessionData.getErrFlag()==1) 
				{
					ErrorMessage("Alert",mUpdateSessionData.getErrMsg());
				}

			} 
			catch (Exception e) 
			{
				getActivity().finish();
			}
		}
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}

	}


	@Override
	public boolean onDrag(View layoutview, DragEvent dragevent) {
		int action = dragevent.getAction();
		if(action==DragEvent.ACTION_DROP){
			View view = (View) dragevent.getLocalState();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);

			swipeviewlayout.addView(view,lp);
			view.setVisibility(View.VISIBLE);
		}
		return true;
	}

	private void ErrorMessage(String title,String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				SessionManager mSessionManager=new SessionManager(getActivity());
				mSessionManager.logoutUser();
				Intent intent=new Intent(getActivity(), LoginUsingFacebook.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				dialog.dismiss();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageRequesTimeOut(String title,String message,final Context context){
		if(context !=null){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.likeButton) {

			likeButton.setEnabled(false);
			dislikeButton.setEnabled(false);
			int viewCount=swipeviewlayout.getChildCount();
			if(viewCount>0){

				int selectedImageIndex=viewCount-1;
				MatchesData matchesData=matchedDataList.get(selectedImageIndex);	
				machedUserFaceBookid=matchesData.getFbId();
				likeMatchedUser(CommonConstant.isLikde);

				RelativeLayout animatedview=(RelativeLayout)swipeviewlayout.getChildAt(selectedImageIndex);
				//				Button likesbutton=(Button)animatedview.getChildAt(3);
				//				likesbutton.setAlpha(1);	
				imageindex=imageindex+1;
				remainingDailyPushes--;
				rotedandRansletAnimation(true,selectedImageIndex,animatedview);

			}
		}else if (v.getId()==R.id.dislikeButton){
			likeButton.setEnabled(false);
			dislikeButton.setEnabled(false);
			int viewCount=swipeviewlayout.getChildCount();

			if(viewCount>0){
				int selectedImageIndex=viewCount-1;
				MatchesData matchesData=matchedDataList.get(selectedImageIndex);
				machedUserFaceBookid=matchesData.getFbId();
				likeMatchedUser(CommonConstant.isDisliked);

				RelativeLayout	animatedview=(RelativeLayout)swipeviewlayout.getChildAt(selectedImageIndex);
				//				Button dislikesButton=(Button)animatedview.getChildAt(4);
				//				dislikesButton.setAlpha(1);

				imageindex=imageindex+1;
				remainingDailyPushes--;
				rotedandRansletAnimation(false,selectedImageIndex,animatedview);

			}


		}
		else if (v.getId()==R.id.inviteButton){
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}else {

			int viewCount=swipeviewlayout.getChildCount();
			int selectedImageIndex=viewCount-1;

			MatchesData matchesData=matchedDataList.get(selectedImageIndex);
			machedUserFaceBookid=matchesData.getFbId();

			SessionManager mSessionManager =new SessionManager(getActivity());
			mSessionManager.setMatchedUserFacebookId(machedUserFaceBookid);
			mSessionManager.setImageIndexForLikeDislike(selectedImageIndex);
			mSessionManager.setLastDownloadImageIndex(numberOfImageDownload);

			Intent mIntent=new Intent(getActivity(), MatchedUserProfile.class);
			Bundle bundle=new Bundle();
			bundle.putBoolean(CommonConstant.isFromChatScreen, false);

			bundle.putString("Latitude", matchesData.getLatitude());
			bundle.putString("Longitude",matchesData.getLongitude());
			mIntent.putExtras(bundle);
			startActivity(mIntent);

		}

	}

	private void rotedandRansletAnimation(boolean isLiked,final int viewindex ,RelativeLayout relativeLayout)
	{
		AnimationSet snowMov1 =null;
		if (isLiked) {
			snowMov1 = new AnimationSet(true);
			RotateAnimation rotate1 = new RotateAnimation(0,20, Animation.RELATIVE_TO_SELF,0.5f , Animation.RELATIVE_TO_SELF,0.0f );
			rotate1.setStartOffset(50);
			rotate1.setDuration(1000);
			snowMov1.addAnimation(rotate1);
			TranslateAnimation trans1 =  new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.5f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
			trans1.setDuration(1000);
			snowMov1.addAnimation(trans1);
		}
		else {
			snowMov1 = new AnimationSet(true);
			RotateAnimation rotate1 = new RotateAnimation(0,-20, Animation.RELATIVE_TO_SELF,0.5f , Animation.RELATIVE_TO_SELF,0.0f );
			rotate1.setStartOffset(50);
			rotate1.setDuration(1000);
			snowMov1.addAnimation(rotate1);
			TranslateAnimation trans1 =  new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.5f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
			trans1.setDuration(1000);
			snowMov1.addAnimation(trans1);
		}

		relativeLayout.startAnimation(snowMov1);
		snowMov1.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation){

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dislikeButton.setEnabled(true);
				likeButton.setEnabled(true);
				swipeviewlayout.removeViewAt(viewindex);
				if (viewindex==0 || remainingDailyPushes<=0){
					hideSwipeLayout();
				}
			}
		});


	}

	private void hideSwipeLayout(){
		//swipeviewlayout.setVisibility(View.GONE);
		matchFoundLayout.setVisibility(View.GONE);
		matchFindingLayout.setVisibility(View.VISIBLE);
		inviteButton.setVisibility(View.VISIBLE);
		waveAnimatedImageView.setVisibility(View.VISIBLE);
		waveAnimatedImageView.startAnimation(animation);

		if(remainingDailyPushes <= 0){
			messagetextview.setText("Those were your daily prospects. Check tomorrow for a new batch.");
		}else{
			messagetextview.setText("Waiting for more matches, we will alert you when we find them.");
		}
		imageindex=0;
		MatchCount=0;
	}

	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			Log.d("JSONRESPONSE","Session Status CallBack");
			findMatch();
		}
	}

	@Override
	public void onError() {
		imageDownloadingCount=imageDownloadingCount+1;
		if (imageDownloadingCount==MatchCount) {
			showSwipeView();
		}
	}

	@Override
	public void onSuccess() {
		imageDownloadingCount=imageDownloadingCount+1;
		if (imageDownloadingCount==MatchCount) {
			showSwipeView();
		}

	}



	private void likeMatchedUser(String action){
		SessionManager mSessionManager=new SessionManager(getActivity());
		String sessionToke=mSessionManager.getUserToken();
		String devieceId=Ultilities.getDeviceId(getActivity());
		String MatchedUserFacebookId=machedUserFaceBookid;

		

		if (MatchedUserFacebookId!=null && MatchedUserFacebookId.length()>0){
			String [] params={sessionToke,devieceId,MatchedUserFacebookId,action};
			new BackGroundTaskForInviteAction().execute(params);
		}
	}


	private class BackGroundTaskForInviteAction extends AsyncTask<String, Void, Void>{
		private String inviteActionResponse;
		private List<NameValuePair>inviteactionparamlist;
		private InviteActionData mActionData;
		private Ultilities mUltilities=new Ultilities();

		@Override
		protected Void doInBackground(String... params){
			try {
				inviteactionparamlist=mUltilities.getInviteActionParameter(params);
				inviteActionResponse=   mUltilities.makeHttpRequest(CommonConstant.inviteaction_url,CommonConstant.methodeName,inviteactionparamlist);
				Gson gson = new Gson();
				mActionData=   gson.fromJson(inviteActionResponse, InviteActionData.class);
				String curenttime=mUltilities.getCurrentGmtTime();

				if (mActionData!=null){
					if ( mActionData.getErrNum()==55) {

						File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
						File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedirematchuserdirectory));
						File imageFile=	mUltilities.createFileInSideDirectory(_picDir, mActionData.getuName()+mActionData.getuFbId()+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(mActionData.getpPic().replaceAll(" ","%20"),imageFile);

						ArrayList<LikeMatcheddataForListview> matchlist=new ArrayList<LikeMatcheddataForListview>();

						LikeMatcheddataForListview objMatchData=new LikeMatcheddataForListview();
						objMatchData.setFacebookid(mActionData.getuFbId());
						objMatchData.setFilePath(imageFile.getAbsolutePath());
						objMatchData.setUserName(mActionData.getuName());
						objMatchData.setFlag("3");
						objMatchData.setImageUrl(mActionData.getpPic());					
						objMatchData.setladt(curenttime);

						matchlist.add(objMatchData);

						DatabaseHandler objDatabaseHandler=new DatabaseHandler(getActivity());
						SessionManager mSessionManager =new SessionManager(getActivity());
						String userFaceBookid=mSessionManager.getFacebookId();
						objDatabaseHandler.insertMatchList(matchlist, userFaceBookid);

					} 
				}

			} 
			catch (Exception e) {
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mActionData!=null) {
				if ( mActionData.getErrNum()==55) {

					Intent matchIntent=new Intent(getActivity(), MatchFoundActivity.class);
					matchIntent.putExtra("SENDER_FB_ID", mActionData.getuFbId());
					matchIntent.putExtra("SENDER_USERNAME", mActionData.getuName());
					startActivity(matchIntent);
				} 
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		SessionManager mSessionManager =new SessionManager(getActivity());
//		if (mSessionManager.getIsInviteActionSucess()){
//			mSessionManager.isInviteActionSucess(false);
//			imageindex=mSessionManager.getImageIndexForLikeDislike();
//			swipeviewlayout.removeViewAt(imageindex);
//		}
	}
	public void removeLikedDislikedCard(){
		SessionManager mSessionManager =new SessionManager(getActivity());
		imageindex=mSessionManager.getImageIndexForLikeDislike();
		swipeviewlayout.removeViewAt(imageindex);
		if(swipeviewlayout.getChildCount()==0){
			hideSwipeLayout();	
		}
	}
	private void flashLikeDislikeIcon(int like){
		if(like==1){
			likeFlashIconView.setVisibility(View.VISIBLE);
		}else{
			dislikeFlashIconView.setVisibility(View.VISIBLE);
		}
		Handler handler=new Handler();
		Runnable flashRunnable=new Runnable() {
			
			@Override
			public void run() {
				likeFlashIconView.setVisibility(View.GONE);	
				dislikeFlashIconView.setVisibility(View.GONE);	
			}
		};
		handler.postDelayed(flashRunnable, 1000);
	}
	private class SwipeTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View parentRootView, MotionEvent event) {

			x_cord = (int) event.getRawX();
			y_cord = (int) event.getRawY();

			final int X = (int) event.getRawX();
			final int Y = (int) event.getRawY();

			switch (event.getAction() & MotionEvent.ACTION_MASK){

			case MotionEvent.ACTION_DOWN:
				RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) parentRootView.getLayoutParams();
				_xDelta = X - lParams.leftMargin;
				_yDelta = Y - lParams.topMargin;
				break;

			case MotionEvent.ACTION_MOVE:
				x_cord = (int) event.getRawX();
				y_cord = (int) event.getRawY();
				parentRootView.setX(X - _xDelta);
				parentRootView.setY(Y - _yDelta);

				if (x_cord >= screenCenter) {
					parentRootView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
					if (x_cord > (screenCenter + (screenCenter/4))) {
						
						//if (x_cord > (windowwidth - (screenCenter / 4))) {
							Likes = 1;
//						} else {
//							Likes = 0;
//						}
					} else {
						Likes = 0;
					}
				} else {
					// rotate
					parentRootView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
					if (x_cord < (screenCenter - (screenCenter/4))) {
						//if (x_cord < screenCenter / 4){
							Likes = 2;
//						} else {
//							Likes = 0;
//						}
					} else {
						Likes = 0;
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				x_cord = (int) event.getRawX();
				y_cord = (int) event.getRawY();

				if (Likes == 0) {
					parentRootView.setRotation(0);
				} 
				else if (Likes == 1) {
					flashLikeDislikeIcon(1);
					remainingDailyPushes--;
					imageindex=imageindex+1;
					if (imageindex==MatchCount){
						hideSwipeLayout();
					}

					int viewCount=swipeviewlayout.getChildCount();

					MatchesData matchesData=matchedDataList.get(viewCount-1);
					machedUserFaceBookid=matchesData.getFbId();
					swipeviewlayout.removeViewAt(viewCount-1);
					
					likeMatchedUser(CommonConstant.isLikde);
				} else if (Likes == 2){
					flashLikeDislikeIcon(2);
					imageindex=imageindex+1;
					remainingDailyPushes--;

					if (imageindex==MatchCount){
						hideSwipeLayout();
					}
					int viewCount=swipeviewlayout.getChildCount();
					MatchesData matchesData=matchedDataList.get(viewCount-1);
					machedUserFaceBookid=matchesData.getFbId();
					swipeviewlayout.removeViewAt(viewCount-1);
					
					likeMatchedUser(CommonConstant.isDisliked);
				}
				break;
			default:
				break;
			}
			return true;	
		}
	}
	
}
