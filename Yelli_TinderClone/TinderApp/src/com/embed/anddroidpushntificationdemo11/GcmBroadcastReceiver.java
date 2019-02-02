package com.embed.anddroidpushntificationdemo11;



import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.pojo.Likes;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.google.gson.Gson;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	private  Context mContext=null;
	private  String userFacebookid="";
	private  String strMessageID="";
	private  String strMessageType="";


	@Override
	public void onReceive(Context context, Intent intent) {
		mContext=context;

		SessionManager session=new SessionManager(context);
		boolean bFlagForCurrent=session.isFirstScreen();
		userFacebookid=session.getFacebookId();
		Log.i("", "push log sendNotification bFlagForCurrent........."+bFlagForCurrent);
		Bundle extras = intent.getExtras();
		String message=extras.getString("payload");
		String action=extras.getString("action");
		strMessageType=extras.getString("mt");

		strMessageID=extras.getString("mid");
		String strSenderName=extras.getString("sname");
		String strDateTime=extras.getString("dt");
		String strFacebookId=extras.getString("sfid");
		
		if(!userFacebookid.contentEquals(strFacebookId)){
			session.setLastMessage(strFacebookId, message.substring(message.indexOf(":")+1,message.length()));
			String date=DateFormat.getDateTimeInstance().format(new Date());
			session.setLastMsgTime(strFacebookId, date);
		}
		if (action.equals("3")) {
			ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
			// Start the service, keeping the device awake while it is launching.
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);
			fineLikedMatched(extras);
		} else {

			if (bFlagForCurrent) {
				Log.i("", "push log onReceive  extras......."+extras);

				Log.i("GcmBroadcastReceiver...............", "push log onReceive push demo strMessageType...............  "+strMessageType);
				Log.i("GcmBroadcastReceiver...............", "push log onReceive push demo strMessageID...............  "+strMessageID);
				Log.i("", "push log GcmBroadcastReceiver message......."+message);
				Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");

				homeIntent.putExtra("MESSAGE_FOR_PUSH", message);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_ACTION", action);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_MESSAGETYPE", strMessageType);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_MESSAGEID", strMessageID);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_SENDERNAME", strSenderName);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_DATETIME", strDateTime);
				homeIntent.putExtra("MESSAGE_FOR_PUSH_FACEBOOKID", strFacebookId);
				context.sendBroadcast(homeIntent);
			} else {
				Log.i("", "push log onReceive  extras......."+extras);

				ChatMessageList params=new ChatMessageList();
				params.setStrMessage(message);
				params.setStrSenderFacebookId(strFacebookId);
				params.setStrSenderId(strFacebookId);
				params.setStrReceiverId(userFacebookid);
				params.setStrDateTime(strDateTime);
				params.setStrSendername(strSenderName);
				params.setStrFlagForMessageSuccess("1");

				new BackGroundForSaveChat().execute(params);
				ComponentName comp = new ComponentName(context.getPackageName(),         GcmIntentService.class.getName());
				// Start the service, keeping the device awake while it is launching.
				startWakefulService(context, (intent.setComponent(comp)));
				setResultCode(Activity.RESULT_OK);
			}

			// Explicitly specify that GcmIntentService will handle the intent.
		} 


	}
	private void fineLikedMatched(Bundle extras){	
		/*String deviceid=Ultilities.getDeviceId(mContext);
			//logDebug("GcmBroadcastReceiver fineLikedMatched   deviceid"+deviceid);
			SessionManager mSessionManager=new SessionManager(mContext);

			String sessionToken=mSessionManager.getUserToken();
			//logDebug("GcmBroadcastReceiver fineLikedMatched   sessionToken"+sessionToken);
			Ultilities mUltilitie=new Ultilities();
			String	currentdeviceTime=mSessionManager.getLastUpdatedTime();
			String curenttime=mUltilitie.getCurrentGmtTime();
			mSessionManager.setLastUpdate(curenttime);*/

		Bundle params []={extras};
		new BackgroundTaskForFindLikeMatched().execute(params);
	}
	private class BackgroundTaskForFindLikeMatched extends AsyncTask<Bundle, Void, Void>
	{

		private Ultilities mUltilities=new Ultilities();
		private ArrayList<Likes>likesList=null;
		private LikeMatcheddataForListview matcheddataForListview;
		private  Bundle bundle;

		@Override
		protected Void doInBackground(Bundle... params) {
			bundle=params[0];
			File	 appDirectory = mUltilities.createAppDirectoy(mContext.getResources().getString(R.string.appdirectory));
			File	 _picDir  = new File(appDirectory, mContext.getResources().getString(R.string.imagedirematchuserdirectory));

			likesList= new ArrayList<Likes>();

			Likes likes=new Likes();
			String imgaeName=bundle.getString("mid");
			String imageUrl =CommonConstant.ImageHostUrl+imgaeName;
			String userFName=bundle.getString("sname");
			String senderFacebookId=bundle.getString("sfid");
			int activeFlag=3;
			String userFaceBookidFromPush=bundle.getString("from");
			String curenttime=mUltilities.getCurrentGmtTime();

			likes.setpPic(imageUrl);
			likes.setfName(userFName);
			likes.setFbId(senderFacebookId);;
			likes.setFlag(activeFlag);
			likes.setLadt(curenttime);;

			likesList.add(likes);

			ArrayList<LikeMatcheddataForListview> arryList=new ArrayList<LikeMatcheddataForListview>();
			for (int i = 0; i < likesList.size(); i++) {
				matcheddataForListview=new LikeMatcheddataForListview();
				String userName=likesList.get(i).getfName();
				String facebookid=likesList.get(i).getFbId();

				String picturl =likesList.get(i).getpPic();
				int falg=likesList.get(i).getFlag();
				matcheddataForListview.setFacebookid(facebookid);
				matcheddataForListview.setUserName(userName);
				matcheddataForListview.setImageUrl(picturl);
				matcheddataForListview.setFlag(""+falg);
				matcheddataForListview.setladt(likesList.get(i).getLadt());

				File imageFile=	mUltilities.createFileInSideDirectory(_picDir, userName+facebookid+".jpg");
				com.appdupe.flamerapp.utility.Utility.addBitmapToSdCardFromURL(likesList.get(i).getpPic().replaceAll(" ","%20"),imageFile);
				matcheddataForListview.setFilePath(imageFile.getAbsolutePath());

				arryList.add(matcheddataForListview);

			}
			DatabaseHandler mDatabaseHandler =new DatabaseHandler(mContext);
			SessionManager mSessionManager =new SessionManager(mContext);
			String userFacebookid=mSessionManager.getFacebookId();
			boolean isdataiserted=	mDatabaseHandler.insertMatchList(arryList,userFacebookid);

			return null;

		}

	}
	private class BackGroundForSaveChat extends AsyncTask<ChatMessageList, Void, String>{

		@Override
		protected String doInBackground(ChatMessageList... params) {
			if (strMessageType.equals("2")) {
				String sendMessageUrl = CommonConstant.getChatMessage_url ;
				Utility myUtility=new Utility();
				SessionManager session=new SessionManager(mContext);
				String sessionId=session.getUserToken();
				String deviceId=Ultilities.getDeviceId(mContext);
				String params1[]={sessionId,deviceId,strMessageID};
				List<NameValuePair> sendMessageReqList=myUtility.getPullMessageReq(params1);
				String messageResponse=myUtility.makeHttpRequest(sendMessageUrl, "POST", sendMessageReqList);
				ChatMessageData objChatMessage=null;
				if (messageResponse!=null) {
					Gson gson = new Gson();
					objChatMessage=gson.fromJson(messageResponse, ChatMessageData.class);
				}
				if (objChatMessage!=null) {
					if (objChatMessage.getErrFlag()==0) {
						params[0].setStrMessage(objChatMessage.getListChat().get(0).getStrMessage());
					} 
				} 
			} 

			DatabaseHandler objDbHandler=new DatabaseHandler(mContext);
			boolean bFlag=objDbHandler.insertMessageData(params[0]);
			Log.i("", "BackGroundForSaveChat bFlag......"+bFlag);
			return null;

		}
	}
}
