package com.android.slidingmenuexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;

public class EditProfileActivity extends Activity implements OnClickListener{	

	private Button editDoneButon=null;
	private ProgressDialog mDialog=null;
	private EditText descripEditor=null;
	private TextView aboutTextView=null;
	

	private static final String TAG = "EditProfileActivity";
	private String aboutUser=null;

	private ImageView[] imageContainerViews=new ImageView[6];
	private ImageView[] addImageViews=new ImageView[6];
	private ImageView[] replaceImageViews=new ImageView[6];
	
	private int ProfilePictureHeightAndWidth[]=new int[2];
	private int otherePictureHeightAndWidth[]=new int[2];
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View editProfileScreen = inflater.inflate(R.layout.new_editprofile_screen, null, false);
		ScalingUtility.getInstance(this).scaleView(editProfileScreen);
		setContentView(editProfileScreen);
		
		initViewsArrays();
		initClicks();
		loadDimensions();
		
		descripEditor=(EditText)findViewById(R.id.descripEditor);
		editDoneButon=(Button)findViewById(R.id.doneTextview);
		editDoneButon.setOnClickListener(editDoneClickListener);
		aboutTextView=(TextView)findViewById(R.id.aboutTextView);

		SessionManager mSessionManager=new SessionManager(this);
		aboutUser=mSessionManager.getUserAbout();
		aboutTextView.setText("About "+mSessionManager.getUserProfileName());
		
		if (aboutUser!=null && aboutUser.length()>0){
			descripEditor.setText(aboutUser);		
		}

		BackGroundTaskForDownloadProfileImage task=new BackGroundTaskForDownloadProfileImage();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		else
			task.execute("");

		//new BackGroundTaskForDownloadProfileImage().execute(); 	
	}
	private void loadDimensions(){
		ProfilePictureHeightAndWidth[0]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.EDIT_LARGE_PIC_WIDTH);
		ProfilePictureHeightAndWidth[1]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.EDIT_LARGE_PIC_HEIGHT);
		
		otherePictureHeightAndWidth[0]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.USER_PROFILE_PIC_WIDTH);
		otherePictureHeightAndWidth[1]=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.USER_PROFILE_PIC_HEIGHT);
	}
	private void initViewsArrays(){
		imageContainerViews[0]=(ImageView)findViewById(R.id.profileImageView);
		imageContainerViews[1]=(ImageView)findViewById(R.id.firstImageView);
		imageContainerViews[2]=(ImageView)findViewById(R.id.secondImageView);
		imageContainerViews[3]=(ImageView)findViewById(R.id.thirdImageView);
		imageContainerViews[4]=(ImageView)findViewById(R.id.fourthImageView);
		imageContainerViews[5]=(ImageView)findViewById(R.id.fifthImageView);
		
		addImageViews[0]=(ImageView)findViewById(R.id.profileAddImageView);
		addImageViews[1]=(ImageView)findViewById(R.id.firstAddImageView);
		addImageViews[2]=(ImageView)findViewById(R.id.secondAddImageView);
		addImageViews[3]=(ImageView)findViewById(R.id.thirdAddImageView);
		addImageViews[4]=(ImageView)findViewById(R.id.fourthAddImageView);
		addImageViews[5]=(ImageView)findViewById(R.id.fifthAddImageView);
		
		replaceImageViews[0]=(ImageView)findViewById(R.id.profileReplaceImageView);
		replaceImageViews[1]=(ImageView)findViewById(R.id.firstReplaceImageView);
		replaceImageViews[2]=(ImageView)findViewById(R.id.secondReplaceImageView);
		replaceImageViews[3]=(ImageView)findViewById(R.id.thirdReplaceImageView);
		replaceImageViews[4]=(ImageView)findViewById(R.id.fourthReplaceImageView);
		replaceImageViews[5]=(ImageView)findViewById(R.id.fifthReplaceImageView);
		
		for(int count=0;count<replaceImageViews.length;count++){
			replaceImageViews[count].setVisibility(View.GONE);
		}

			
	}
	private void initClicks(){
		for(int count=0;count<imageContainerViews.length;count++){
			addImageViews[count].setOnClickListener(this);
			replaceImageViews[count].setOnClickListener(this);
		}
	}
	OnClickListener editDoneClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			updateUserPersonalDescription();
			EditProfileActivity.this.finish();
		}
	};

	private  class BackGroundTaskForDownloadProfileImage extends AsyncTask<String, Void, Void>{

		private Ultilities mUltilities =null;
		
		private ScalingUtilities mScalingUtilities=null;;
		private ArrayList<ImageDetail>imagelistFormdatabase=null;
		private DatabaseHandler databaseHandler=null;

		private BackGroundTaskForDownloadProfileImage(){
			mUltilities=new Ultilities();
			mScalingUtilities=new ScalingUtilities();
			databaseHandler=new DatabaseHandler(EditProfileActivity.this);
		}

		private void setBitmapToImageContainer(){
			imagelistFormdatabase=databaseHandler.getImageDetail();
			if (imagelistFormdatabase!=null && imagelistFormdatabase.size()>0) {
				for (int i = 0; i < imagelistFormdatabase.size(); i++){

					final Bitmap usrFirstImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
					int imageWidth=0;
					int imageHeight=0;

					final int imageOrder=imagelistFormdatabase.get(i).getImageOrder()-1;
					if(imageOrder==0){
						imageWidth=ProfilePictureHeightAndWidth[0];
						imageHeight=ProfilePictureHeightAndWidth[1];
					}else{
						imageWidth=otherePictureHeightAndWidth[0];
						imageHeight=otherePictureHeightAndWidth[1];
					}
					if(usrFirstImage !=null){
						final Bitmap scaledBitmap=mScalingUtilities.createScaledBitmap(usrFirstImage,imageWidth,imageHeight, ScalingLogic.CROP);
						usrFirstImage.recycle();
						runOnUiThread(new Runnable(){
							public void run(){
								imageContainerViews[imageOrder].setImageBitmap(scaledBitmap);
								addImageViews[imageOrder].setVisibility(View.GONE);
								replaceImageViews[imageOrder].setVisibility(View.VISIBLE);
							}
						});	
					}
				}
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			setBitmapToImageContainer();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(mDialog !=null){
				mDialog.dismiss();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {    			
			super.onPreExecute();
			mDialog=mUltilities.GetProcessDialog(EditProfileActivity.this);
			mDialog.setMessage("Please Wait...");
			mDialog.setCancelable(false);
			mDialog.show();    			
		}    		
	}

	@Override
	public void onClick(View v){
		SessionManager manager=new SessionManager(this);
		int imageIndex=0;

		if (v.getId()==R.id.profileAddImageView || v.getId()==R.id.profileReplaceImageView) {	
			imageIndex=1;
		}
		else if (v.getId()==R.id.firstAddImageView || v.getId()==R.id.firstReplaceImageView) {
			imageIndex=2;
		}
		else if (v.getId()==R.id.secondAddImageView || v.getId()==R.id.secondReplaceImageView) {
			imageIndex=3;		
		}
		else if (v.getId()==R.id.thirdAddImageView || v.getId()==R.id.thirdReplaceImageView) {	
			imageIndex=4;
		}
		else if (v.getId()==R.id.fourthAddImageView || v.getId()==R.id.fourthReplaceImageView) {
			imageIndex=5;
		}
		else if (v.getId()==R.id.fifthAddImageView || v.getId()==R.id.fifthReplaceImageView){
			imageIndex=6;	
		}

		manager.setImageIndex(imageIndex);
		Intent mIntent =new Intent(EditProfileActivity.this, AlbumListView.class);
		startActivity(mIntent);

	} 



	@Override
	protected void onResume(){
		super.onResume();
	}

		private void updateUserPersonalDescription()
		{
			finish();
			String userUpdatePrefrence=descripEditor.getText().toString();
	
			if (userUpdatePrefrence!=null && userUpdatePrefrence.length()>0)
			{
	
				if (aboutUser!=null &&aboutUser.length()>0)
				{
					if (userUpdatePrefrence.equals(aboutUser)) 
					{
						 //no need to call update same 
	
					}
					else 
					{
						// user change persnal description 
						SessionManager mSessionManager=new SessionManager(this);
						String deviceId=Ultilities.getDeviceId(this);
						String userSession=mSessionManager.getUserToken();
						mSessionManager.setUserAbout(userUpdatePrefrence);
						 userUpdatePrefrence="";
						String [] params ={userSession,deviceId,userUpdatePrefrence};
						new BackgroundTaskForUpdatePersnalDescription().execute(params);
	
					}
				}
				else 
				{
					// user change persnal description 
					SessionManager mSessionManager=new SessionManager(this);
					String deviceId=Ultilities.getDeviceId(this);
					String userSession=mSessionManager.getUserToken();
					mSessionManager.setUserAbout(userUpdatePrefrence);
					String [] params ={userSession,deviceId,userUpdatePrefrence};
					new BackgroundTaskForUpdatePersnalDescription().execute(params);
	
				}			
			}
			else 
			{
	
				if (aboutUser!=null&&aboutUser.length()>0) 
				{
	
				}
				else 
				{
					// user  want to remove their persional description
					SessionManager mSessionManager=new SessionManager(this);
					String deviceId=Ultilities.getDeviceId(this);
					String userSession=mSessionManager.getUserToken();
					userUpdatePrefrence="";
	
					mSessionManager.setUserAbout(userUpdatePrefrence);
					String [] params ={userSession,deviceId,userUpdatePrefrence};
					new BackgroundTaskForUpdatePersnalDescription().execute(params);
	
				}
			}
		}
	
		private class BackgroundTaskForUpdatePersnalDescription extends AsyncTask<String, Void, Void>
		{
			Ultilities mUltilities=new Ultilities();
			private List<NameValuePair>uploadNameValuePairList;
			private String uploadResponse;
			private boolean updateuccessFully;
			@Override
			protected Void doInBackground(String... params) 
			{
				try {
					String [] uploadParameter={params[0],params[1],params[2]};
					uploadNameValuePairList=mUltilities.getParameterForUpdateUserPersionlDescription(uploadParameter);		
	
					uploadResponse=   mUltilities.makeHttpRequest(CommonConstant.editUseProfile_url,CommonConstant.methodeName,uploadNameValuePairList);
					Log.d(TAG,"BackgroundTaskForUpdatePersnalDescription  uploadResponse   "+uploadResponse);
	
					updateuccessFully=true;
				} catch (Exception e)
				{
					Log.e(TAG,"BackgroundTaskForUpdatePersnalDescription doInBackground "+e);
					updateuccessFully=false;
				}
	
				return null;
			}
			@Override
			protected void onPostExecute(Void result) 
			{
				super.onPostExecute(result);
			}
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
			}	  
		}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		

	}



}
