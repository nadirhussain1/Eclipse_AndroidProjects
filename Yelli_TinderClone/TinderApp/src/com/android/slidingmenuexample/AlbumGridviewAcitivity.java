package com.android.slidingmenuexample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdupe.flamerapp.LoginUsingFacebook;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.pojo.FacebookAlbumData;
import com.appdupe.flamerapp.pojo.FacebookAlbumSource;
import com.appdupe.flamerapp.pojo.GridViewData;
import com.appdupe.flamerapp.pojo.ImageDeleteData;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.UpdateSessionData;
import com.appdupe.flamerapp.pojo.UploadImage;
import com.appdupe.flamerapp.pojo.UploadeImageUrl;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtilities;
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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

public class AlbumGridviewAcitivity extends Activity {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private  ProgressDialog mdialog=null;
	final int PIC_CROP = 2;
	private GridView gridView=null;
	private Button doneButton=null;
	private ArrayList<GridViewData>gridViewlist=null;
	private ImageAdapter mAdapter=null;
	private int selectedGridItemPosition=-1;
	private String alubumid="";
	private String alubumName="";
	private TextView  alubumNameTextivew=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View albumsGridLayout = inflater.inflate(R.layout.new_gridview_layout, null, false);
		ScalingUtility.getInstance(this).scaleView(albumsGridLayout);
		setContentView(albumsGridLayout);

		Session session =initSession(savedInstanceState);

		gridView=(GridView)findViewById(R.id.PhoneImageGrid);
		alubumNameTextivew=(TextView)findViewById(R.id.alubumNameTextivew);
		doneButton=(Button)findViewById(R.id.editDonebuton);

		doneButton.setOnClickListener(doneClickListener);
		gridViewlist=new ArrayList<GridViewData>();
		mAdapter=new ImageAdapter(this, gridViewlist);
		gridView.setAdapter(mAdapter);

		Bundle extras = getIntent().getExtras(); 
		alubumid =extras.getString(CommonConstant.ALUBUMID);
		alubumName=extras.getString(CommonConstant.ALUBUMNAME);
		alubumNameTextivew.setText(alubumName);
		getAlubumPick(alubumid ,session);
	}
	private Session initSession(Bundle savedInstanceState){
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
		return session;
	}


	private void getAlubumPick(String alubumId ,Session session){

		if (session.isOpened()) {
			String fqlQuery=	"SELECT src_big from photo where aid ="+"'"+alubumId+"'";

			Bundle param = new Bundle();
			param.putString("q", fqlQuery);
			Request request = new Request(session,"/fql",param,HttpMethod.GET,new Request.Callback(){         
				public void onCompleted(Response response) {	
					String []  pramas={response.toString()} ;
					new BackGroundTaskForGetAlubumImage().execute(pramas);	
				}                  
			});

			Request.executeBatchAsync(request);    
		}
		else {
			getOpenedSession();
		}
	}
	private OnClickListener doneClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			performAllUpdateOperations();

		}
	};


	private class BackGroundTaskForGetAlubumImage extends AsyncTask<String, Void, Void>{
		Ultilities mUltilities=new Ultilities();

		private FacebookAlbumData mAlbumData=null;
		private ArrayList<FacebookAlbumSource>imgUrlList=null;
		private String albumImgResponse=null;
		private GridViewData mGridViewData=null;
		private boolean flagForsuccess=false;
		private ScalingUtilities mScalingUtilities=new ScalingUtilities();

		@Override
		protected Void doInBackground(String... params) 
		{
			try
			{
				File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
				File	 _picDir  = new File(appDirectory, getResources().getString(R.string.alubumpic));

				albumImgResponse=params[0];
				albumImgResponse=albumImgResponse.substring(albumImgResponse.indexOf("state=")+6, albumImgResponse.indexOf("}, error:"));
				Gson gson = new Gson();
				mAlbumData=   gson.fromJson(albumImgResponse, FacebookAlbumData.class);
				imgUrlList=mAlbumData.getAlubumScrsList();

				if (imgUrlList!=null && imgUrlList.size()>0) {

					for (int i = 0; i < imgUrlList.size(); i++ ){

						mGridViewData=new GridViewData();
						mGridViewData.setPicUrl(imgUrlList.get(i).getPickUrl());
						gridViewlist.add(mGridViewData);
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								mdialog.dismiss();
								mAdapter.notifyDataSetChanged();
							}
						});
					}


					for (int i = 0; i < gridViewlist.size(); i++) {

						File imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.gridviewimage)+i+".jpg");
						com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(imgUrlList.get(i).getPickUrl().replaceAll(" ","%20"),imageFile);
						gridViewlist.get(i).setFilePath(imageFile);

					}

					flagForsuccess=true;
				}
				else
				{
					flagForsuccess=false;
				}
			} catch (Exception e) 
			{
				//logError("BackGroundTaskForGetAlubumImage  doInBackground    Exception "+e);
				flagForsuccess=false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(mdialog !=null){
				mdialog.dismiss();
			}
			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mdialog=mUltilities.GetProcessDialog(AlbumGridviewAcitivity.this);
			mdialog.setCancelable(false);
			mdialog.show();
		}
	}

	private void getOpenedSession(){
		Session.openActiveSession(this, true, statusCallback);
	}

	@Override
	public void onStart() 
	{
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
	protected void onPause() 
	{
		super.onPause();

		if (mdialog!=null) 
		{
			mdialog.dismiss();
			mdialog.cancel();
			mdialog=null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

	}

	private void performAllUpdateOperations(){	
		BackgroundTaksForCopySelectedImage task=new BackgroundTaksForCopySelectedImage();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		else
			task.execute((Void[])null);


	}

	private class BackgroundTaksForCopySelectedImage extends AsyncTask<Void, Void, Void>
	{
		Ultilities mUltilities=new Ultilities();
		private int ImageIndex;
		private boolean flagForUploadsuccess=true;
		private SessionManager mSessionManager=new SessionManager(AlbumGridviewAcitivity.this);
		private String userSessionToken;
		private String deviceId;
		private int imageOrder;
		private String imageFlage="2";

		private List<NameValuePair>deleteImageParams=null;
		private String deleteImageResponse="";


		private File copyImageToSdCard(){
			Log.d("PhotoDebug","Copy To SdCard");
			Ultilities mUltilities=new Ultilities();
			File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
			File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedire));

			File imageFile=	mUltilities.createFileInSideDirectory(_picDir, getResources().getString(R.string.imagefilename)+(ImageIndex-1)+".jpg");
			imageFile.delete();

			Utility.addBitmapToSdCardFromURL(gridViewlist.get(selectedGridItemPosition).getPicUrl().replaceAll(" ","%20"),imageFile);
			//mUltilities.copyImageTOSdCard(imageFile, gridViewlist.get(selectedGridItemPosition).getBitmap());

			return imageFile;
		}
		private String[] updateImageUrlInLocalDatabase(File imageFile){
			String userFacebookid=mSessionManager.getFacebookId();
			String sdcardpath=imageFile.getAbsolutePath();
			String imageUrl=gridViewlist.get(selectedGridItemPosition).getPicUrl();
			Log.d("PhotoDebug","Url saved in LocalDatabase="+imageUrl);

			ImageDetail imageDetail=new ImageDetail();
			imageDetail.setUserFacebookid(userFacebookid);
			imageDetail.setImageOrder(imageOrder);
			imageDetail.setImageUrl(imageUrl);
			imageDetail.setSdcardpath(sdcardpath);

			DatabaseHandler mDatabaseHandler=new DatabaseHandler(AlbumGridviewAcitivity.this);
			return mDatabaseHandler.updateOrInsertImagepath(imageDetail);
		}
		private void deletePrevImageUrl(String[] previousImageUrl){

			if ((imageOrder!=1) && ((previousImageUrl!=null && previousImageUrl.length>0)) && (!previousImageUrl[0].equals("imageNotFount"))){

				String [] deleteParams={userSessionToken,deviceId,previousImageUrl[0],imageFlage};
				deleteImageParams=mUltilities.getDeleteParameter(deleteParams);
				deleteImageResponse=mUltilities.makeHttpRequest(CommonConstant.imagedelete_url, CommonConstant.methodeName, deleteImageParams);

				Gson gson = new Gson();
				ImageDeleteData imageDeleteData=   gson.fromJson(deleteImageResponse, ImageDeleteData.class);
			}

		}


		@Override
		protected Void doInBackground(Void... params) {
			Log.d("PhotoDebug","Inside DoInBackground");

			userSessionToken=mSessionManager.getUserToken();
			deviceId=Ultilities.getDeviceId(AlbumGridviewAcitivity.this);

			ImageIndex=mSessionManager.getImageIndex();
			imageOrder=ImageIndex;


			File imageFile=copyImageToSdCard();

			if (imageOrder==1){
				imageFlage="1";
			}

			String[] previousImageUrl=updateImageUrlInLocalDatabase(imageFile);
			deletePrevImageUrl(previousImageUrl);

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mdialog!=null){
				mdialog.dismiss();
			}


			SessionManager mSessionManager=new SessionManager(AlbumGridviewAcitivity.this);
			if (imageOrder==1){
				mSessionManager.setIsProfileImageChanged(true);
			}
			else{
				mSessionManager.setIsProfileImageChanged(false);
			}
			mSessionManager.setIsImageChange(true);
			Log.d("PhotoDebug","PostExecute UploadImageUrl");
			uploadImageUrl(ImageIndex);

		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("PhotoDebug","CopyPreExecute");
			mdialog=mUltilities.GetProcessDialog(AlbumGridviewAcitivity.this);
			mdialog.setCancelable(false);
			mdialog.setTitle("Uploading image on server");
			mdialog.setMessage("Please wait...");
			mdialog.show();

		}

	}

	// update Image Url on server
	private void uploadImageUrl(int ImageIndex){

		String imageUrl=gridViewlist.get(selectedGridItemPosition).getPicUrl();
		SessionManager mSessionManager=new SessionManager(this);
		String deviceid;
		String  sessionToken;
		deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(this);
		sessionToken=mSessionManager.getUserToken();
		String [] params={sessionToken,deviceid,imageUrl,""+ImageIndex};


		BackGroundTasForUploadSelectedImage task=new BackGroundTasForUploadSelectedImage();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		else
			task.execute(params);


	}



	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}





	public class ImageAdapter extends ArrayAdapter<GridViewData> 
	{
		private LayoutInflater mInflater=null;
		private RelativeLayout.LayoutParams mLayoutParams=null;
		private Ultilities ultilities=new Ultilities();

		public ImageAdapter(Activity activity,ArrayList<GridViewData>list) {   
			super(activity, R.layout.gridviewitem, list);

			mLayoutParams=ultilities.getRelativelayoutParams(ultilities.getImageHeightAndWidthForAlubumGridView(activity)[1], ultilities.getImageHeightAndWidthForAlubumGridView(activity)[0]);
			mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

			this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public GridViewData getItem(int position){
			return super.getItem(position);
		}

		@Override
		public long getItemId(int position){
			return super.getItemId(position);
		}

		@Override
		public void remove(GridViewData object){
			super.remove(object);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) 
			{
				holder = new ViewHolder();               
				convertView = mInflater.inflate(R.layout.gridviewitem, null);
				ScalingUtility.getInstance(AlbumGridviewAcitivity.this).scaleView(convertView);
				holder.imageview = (ImageView) convertView.findViewById(R.id.gridviewthumbImage);
				holder.borderLayout=(RelativeLayout)convertView.findViewById(R.id.mgridviewImageParentlayout);

				holder.imageview.setLayoutParams(mLayoutParams);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageview.setId(position);
			if(position==selectedGridItemPosition){
				holder.borderLayout.setBackgroundResource(R.drawable.add_image_boader_on);
			}else{
				holder.borderLayout.setBackgroundResource(R.drawable.add_image_boader);
			}

			holder.imageview.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					if(selectedGridItemPosition==position){
						selectedGridItemPosition=-1;
						doneButton.setVisibility(View.GONE);
					}else{
						selectedGridItemPosition=position;
						doneButton.setVisibility(View.VISIBLE);
						notifyDataSetChanged();

					}

				}
			});


			Picasso.with(AlbumGridviewAcitivity.this) //
			.load(getItem(position).getPicUrl()) //
			.fit()
			.centerInside()
			.into(holder.imageview);


			holder.id = position;
			return convertView;
		}
	}
	class ViewHolder {
		ImageView imageview;
		RelativeLayout borderLayout;
		int id;
	}


	private class BackGroundTasForUploadSelectedImage extends AsyncTask<String, Void, Void>{

		Ultilities mUltilities=new Ultilities();
		private List<NameValuePair>uploadNameValuePairList;
		private String uploadResponse;
		private boolean uploadImageurlsuccessFully=false;
		private UploadImage mUploadImage;

		@Override
		protected Void doInBackground(String... params)
		{
			try 
			{
				Log.d("PhotoDebug","Do IN Background UploadTask");
				String [] uploadParameter={params[0],params[1],params[2],params[3]};
				uploadNameValuePairList=mUltilities.getUploadSingleImage(uploadParameter);
				uploadResponse=   mUltilities.makeHttpRequest(CommonConstant.uploadImage_url,CommonConstant.methodeName,uploadNameValuePairList);
				Gson gson = new Gson();

				mUploadImage=   gson.fromJson(uploadResponse, UploadImage.class);
				if (mUploadImage.getErrNum()==18 && mUploadImage.getErrFlag()==0) 
				{
					if (Integer.parseInt(params[3])==1) 
					{
						if (mUploadImage.getProfFlag()==1) {
							uploadImageurlsuccessFully=true;
						}	
					}
					else 
					{
						ArrayList<UploadeImageUrl>images=mUploadImage.getImages();
						if (images!=null && images.size()>0)
						{
							UploadeImageUrl uploadeImageUrl=images.get(0);
							if (uploadeImageUrl!=null && uploadeImageUrl.getFlag()==1){

								uploadImageurlsuccessFully=true;
							}

						}


					}
				}

				if (uploadImageurlsuccessFully) {
					//					File	 appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
					//					File	 _picDir  = new File(appDirectory, getResources().getString(R.string.alubumpic));
					//					mUltilities.deleteNon_EmptyDir(_picDir);	
				}
			} 
			catch(JsonSyntaxException ex){
				uploadImageurlsuccessFully=false;
			}
			catch (Exception e) {	
				uploadImageurlsuccessFully=false;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			Log.d("PhotoDebug","UploadImage PostExecuteeeeeeeee ");
			mdialog.dismiss(); 
			if (uploadImageurlsuccessFully) {		
				Intent mIntent=new Intent(AlbumGridviewAcitivity.this, EditProfileActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}else {
				ErrorMessageInvalidSessionToken("Alert",mUploadImage.getErrorMassage());
			}
		}
		@Override
		protected void onPreExecute(){

			super.onPreExecute();
			mdialog=mUltilities.GetProcessDialog(AlbumGridviewAcitivity.this);
			mdialog.setCancelable(false);
			mdialog.show();

		}

	}



	private void getUpdateSessionToken()
	{
		new BackGroundTaskForUpdateToken().execute();
	}


	private class BackGroundTaskForUpdateToken extends AsyncTask<String , Void, Void>
	{
		private Ultilities mUltilities=new Ultilities();
		private String  sessionToken;
		private SessionManager mSessionManager=new SessionManager(AlbumGridviewAcitivity.this);
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
				deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(AlbumGridviewAcitivity.this);
				sessionToken=mSessionManager.getUserToken();
				facebookId=mSessionManager.getFacebookId();
				String [] updateTokenParameter={sessionToken,deviceid,facebookId};
				updateTokenparameterlist=mUltilities.getUpdateTokeParameter(updateTokenParameter);
				//logDebug("BackGroundTaskForUpdateToken   doInBackground findMathchResponse "+updatedTokenResponse);
				updatedTokenResponse=   mUltilities.makeHttpRequest(CommonConstant.UpdateToken_url,CommonConstant.methodeName,updateTokenparameterlist);
				//logDebug("BackGroundTaskForUpdateToken   doInBackground findMathchResponse "+updatedTokenResponse);
				Gson gson = new Gson();




				mUpdateSessionData=   gson.fromJson(updatedTokenResponse, UpdateSessionData.class);
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
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


				}
				else if (mUpdateSessionData.getErrNum()==60&&mUpdateSessionData.getErrFlag()==1) 
				{
					//ErrorMessage("Alert",mUpdateSessionData.getErrMsg());
				}

			} 
			catch (Exception e) 
			{
				// TODO: handle exception

			}
		}
		@Override
		protected void onPreExecute() 
		{

			super.onPreExecute();
		}

	}



	private void ErrorMessage(String title,String message)
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

				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{

				dialog.dismiss();
				Intent mIntent=new Intent(AlbumGridviewAcitivity.this, EditProfileActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();


			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageInvalidSessionToken(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Session session = Session.getActiveSession();
				if (!session.isClosed()){
					session.closeAndClearTokenInformation();
				}

				SessionManager mSessionManager=new SessionManager(AlbumGridviewAcitivity.this);
				mSessionManager.logoutUser();

				Intent intent=new Intent(AlbumGridviewAcitivity.this, LoginUsingFacebook.class);
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






	private class SessionStatusCallback implements Session.StatusCallback
	{
		@Override
		public void call(Session session, SessionState state, Exception exception) 
		{
			// updateView();
			//			logDebug("SessionStatusCallback  ");
			//			logDebug("SessionStatusCallback Session  "+session);
			//			logDebug("SessionStatusCallback  SessionState "+state);
			//			logDebug("SessionStatusCallback  Exception "+exception);
			//getUserAllAlubum();

		}
	} 


	private void ErrorMessage()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.alert));
		builder.setMessage(getResources().getString(R.string.retriedmessage));

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Session mSession=Session.getActiveSession();
				getAlubumPick(alubumid ,mSession);

				dialog.dismiss();
				finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}



}
