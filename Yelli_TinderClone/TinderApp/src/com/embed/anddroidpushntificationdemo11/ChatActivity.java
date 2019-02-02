package com.embed.anddroidpushntificationdemo11;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slidingmenuexample.BitmapLruCache;
import com.android.slidingmenuexample.MatchedUserProfile;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.screens.AllMatchesSlideWindow;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.google.gson.Gson;

public class ChatActivity extends Activity implements OnClickListener{


	private ImageLoader imageLoader=null;
	private EditText chatEditText=null;
	private Button sendChatMessageButton=null;
	private BroadcastReceiver receiver=null;
	private String strFriendFbId="";//xolo
	private String deviceId/*="911328850098461"*/;//lenovo
	private boolean bFlagForHistoryMessage=true;

	private ChatMessageList objMessageData=null;
	private AwesomeAdapter messageAdapter=null;
	private ListView chatListView=null;
	private ArrayList<ChatMessageList> listChatData=new ArrayList<ChatMessageList>();
	private String userFacebookid="";
	private String sessiosntoken="";

	private IntentFilter filter=null;
	private TextView senderName=null;
	private String friendImageUrl="";
	private String currentUserImageUrl="";
	private String checkForPush="1";
	private Button userinfoimageview=null;
	private Button popumenubutton=null;
	private int startLimit=0,limit=30,pageNum=0;
	private  ProgressDialog mdialog=null;
	private Bitmap friendBitmap=null;
	private Bitmap userBitmap=null;
	private int  picDimensions=0;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View chatScreenView = inflater.inflate(R.layout.chat_new_screen, null, false);
		ScalingUtility.getInstance(this).scaleView(chatScreenView);
		setContentView(chatScreenView);

		initComponent();

		RequestQueue mRequestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));

		Bundle bucket=getIntent().getExtras();
		if (bucket!=null) {
			checkForPush=bucket.getString(CommonConstant.CHECK_FOR_PUSH_OR_NOT);
			if (checkForPush!=null && checkForPush.equals("1")) {
				strFriendFbId=bucket.getString(CommonConstant.FRIENDFACEBOOKID);

			} else{                
				Bundle bucket1=getIntent().getBundleExtra("PUSH_MESSAGE_BUNDLE");
				strFriendFbId=bucket1.getString("sfid");		
			}

		} 
		setProfilePick();
		DatabaseHandler mDatabaseHandler=new DatabaseHandler(this);
		LikeMatcheddataForListview matcheddataForListview=	mDatabaseHandler.getSenderDetail(strFriendFbId);
		friendImageUrl=matcheddataForListview.getFilePath();
		String senderNamevalues=matcheddataForListview.getUserName();
		loadDimensions();
		loadBitmaps();

		//senderimage.setImageUrl(senderimageUrl, imageLoader);
		senderName.setText(senderNamevalues); 

		com.appdupe.flamerapp.utility.SessionManager mSessionManager=new com.appdupe.flamerapp.utility.SessionManager(this);
		userFacebookid=mSessionManager.getFacebookId();
		sessiosntoken=mSessionManager.getUserToken();

		deviceId =Ultilities.getDeviceId(ChatActivity.this);
		messageAdapter=new AwesomeAdapter(ChatActivity.this, listChatData);
		chatListView.setAdapter(messageAdapter);

		if (bFlagForHistoryMessage) {
			String []params={sessiosntoken,deviceId,strFriendFbId,""};
			new BackgroundforMessagehistory().execute(params);
		} else {
			new BackgroundForGetDataFromDB().execute();
		}

		filter = new IntentFilter();
		filter.addAction("com.embed.anddroidpushntificationdemo11.push");

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bucket=intent.getExtras();

				String strMessage=bucket.getString("MESSAGE_FOR_PUSH");
				String strMessageType=bucket.getString("MESSAGE_FOR_PUSH_MESSAGETYPE");
				String strMessageId=bucket.getString("MESSAGE_FOR_PUSH_MESSAGEID");
				String strSenderName=bucket.getString("MESSAGE_FOR_PUSH_SENDERNAME");
				String strDateTime=bucket.getString("MESSAGE_FOR_PUSH_DATETIME");
				String strFacebookId=bucket.getString("MESSAGE_FOR_PUSH_FACEBOOKID");

				if (strMessageType.equals("2")) {
					String []params={sessiosntoken,deviceId,strMessageId};
					new BackgroundForPullMessage().execute(params);
				} else {
					ChatMessageList objMessageData=new ChatMessageList();
					objMessageData.setStrMessage(strMessage);
					objMessageData.setStrDateTime(strDateTime);
					objMessageData.setStrSenderFacebookId(strFacebookId);
					objMessageData.setStrSendername(strSenderName);
					objMessageData.setStrFlagForMessageSuccess("1");
					objMessageData.setStrReceiverId(userFacebookid);
					//Log.i(TAG, "onReceive setStrSenderId...."+strFriendFbId);
					objMessageData.setStrSenderId(strFriendFbId);
					if (strFriendFbId.equals(strFacebookId)) {
						listChatData.add(objMessageData);
					}else if(!strFacebookId.contentEquals(userFacebookid)){
						Toast.makeText(ChatActivity.this, "You have message from :"+strSenderName, Toast.LENGTH_SHORT).show();
					}
					new BackgroundForInsertMessageDB().execute(objMessageData);
				}


				messageAdapter.notifyDataSetChanged();
				chatListView.setSelection(listChatData.size());
			}
		};

		sendChatMessageButton.setOnClickListener(this);
		userinfoimageview.setOnClickListener(this);
		findViewById(R.id.backButtonClickArea).setOnClickListener(this);
	}


	private void setProfilePick(){
		DatabaseHandler mdaDatabaseHandler=new DatabaseHandler(this);
		String imageOrderArray[]={"1"};
		ArrayList<ImageDetail>imagelist=	mdaDatabaseHandler.getImageDetailByImageOrder(imageOrderArray);
		if (imagelist!=null && imagelist.size()>0) {
			currentUserImageUrl=imagelist.get(0).getSdcardpath();
		}
	}
	private void loadBitmaps(){
		friendBitmap=getCircleBitmap(currentUserImageUrl);
		userBitmap=getCircleBitmap(friendImageUrl);
	}
	private void loadDimensions(){
		picDimensions=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.CHAT_PIC_SIZE);
	}
	private Bitmap getCircleBitmap(String imagePath){
		Ultilities mUltilities=new Ultilities();
		Bitmap bitmapimage = mUltilities.showImage(imagePath);
		ScalingUtilities mScalingUtilities =new ScalingUtilities();
		Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,picDimensions, picDimensions, ScalingLogic.CROP);
		bitmapimage.recycle();
		Bitmap resultBitmap= mUltilities.getCircleBitmap(cropedBitmap, 1);
		cropedBitmap.recycle();

		return resultBitmap;
	}


	private void initComponent(){

		popumenubutton=(Button)findViewById(R.id.popumenubutton);
		popumenubutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final PopupMenu popup = new PopupMenu(ChatActivity.this, popumenubutton);
				popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						if(item.getItemId()==R.id.blockMatchAction){
							blockMatch(false);
						}else if(item.getItemId()==R.id.reportMatchAction){
							reportMatch();
						}
						popup.dismiss();
						return true;
					}
				});

				popup.show();//showing popup menu
			}
		});

		chatListView=(ListView)findViewById(R.id.messagesList);
		chatEditText=(EditText) findViewById(R.id.chat_editText);
		sendChatMessageButton=(Button) findViewById(R.id.send_chat_message_button);

		senderName=(TextView)findViewById(R.id.senderName);
		userinfoimageview=(Button)findViewById(R.id.userinfoimageview);		

		Typeface	topbartextviewFont=Typeface.createFromAsset(getAssets(), "fonts/HelveticaLTStd-Light.otf");
		senderName.setTypeface(topbartextviewFont);
		senderName.setTextColor(Color.rgb(255, 255, 255));
		senderName.setTextSize(20);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver!=null) {
			registerReceiver(receiver, filter);
		} 
		SessionManager session=new SessionManager(ChatActivity.this);
		session.setFirstScreen(true);
	}


	@Override
	protected void onPause() {
		super.onPause();
		SessionManager session=new SessionManager(ChatActivity.this);
		session.setFirstScreen(false);
		unregisterReceiver(receiver);
	}
	@Override
	protected void onStop(){
		super.onStop();
	}
	private void showProgressDialog(){
		mdialog=new Ultilities().GetProcessDialog(ChatActivity.this);
		mdialog.setCancelable(false);
		mdialog.setTitle("Bloacking User");
		mdialog.setMessage("Please wait...");
		mdialog.show();
	}
	private void hideProgressDialog(){
		if(mdialog !=null){
			mdialog.cancel();
		}
	}
	private void blockMatch(boolean isReport){
		showProgressDialog();

		SessionManager mSessionManager=new SessionManager(this);
		String sessionToke=mSessionManager.getUserToken();
		String  deviceid=Ultilities.getDeviceId(this);
		String [] params={sessionToke,deviceid,strFriendFbId,"5"};
		new BlockMatchTask(isReport).execute(params);
	}
	private void reportMatch(){
		blockMatch(true);
	}
	private void sendEmail(){
		Intent email = new Intent(Intent.ACTION_SEND/*Intent.ACTION_VIEW*/);
		email.setType("message/rfc822"); 
		email.putExtra(Intent.EXTRA_EMAIL, "ahs241@gmail.com");
		email.putExtra(Intent.EXTRA_SUBJECT, "Block User="+strFriendFbId);

		startActivity(Intent.createChooser(email, "Choose an Email client :"));	
	}
	private class BackgroundForGetDataFromDB extends AsyncTask<String, Void, ArrayList<ChatMessageList>>{        

		@Override
		protected ArrayList<ChatMessageList> doInBackground(String... params) {
			DatabaseHandler objDBHandler=new DatabaseHandler(ChatActivity.this);
			if (pageNum==0) {
				listChatData.clear();
			} 
			return objDBHandler.getChatMessages( strFriendFbId,startLimit,limit);
		}
		@Override
		protected void onPostExecute(ArrayList<ChatMessageList> result) {
			super.onPostExecute(result);
			Collections.reverse(result);
			listChatData.addAll(0,result);

			messageAdapter.notifyDataSetChanged();

			if (listChatData!=null && listChatData.size()>0) {
				chatListView.setSelection(listChatData.size());
			}

		}
	}
	private class BackgroundForPullMessage extends AsyncTask<String, Void, ChatMessageData>{

		@Override
		protected ChatMessageData doInBackground(String... arg0) {

			Utility myUtility=new Utility();
			List<NameValuePair> sendMessageReqList=myUtility.getPullMessageReq(arg0);
			String messageResponse=myUtility.makeHttpRequest(CommonConstant.getChatMessage_url, CommonConstant.methodeName, sendMessageReqList);
			ChatMessageData objChatMessage=null;
			if (messageResponse!=null) {
				Gson gson = new Gson();
				objChatMessage=gson.fromJson(messageResponse, ChatMessageData.class);
				if (objChatMessage!=null) {
					if (objChatMessage.getErrFlag()==0) {
						List<ChatMessageList> listChat=objChatMessage.getListChat();
						ChatMessageList objChatMessageData=listChat.get(0);
						objChatMessageData.setStrFlagForMessageSuccess("1");
						objChatMessageData.setStrSenderId(objChatMessageData.getStrSenderFacebookId());
						objChatMessageData.setStrReceiverId(userFacebookid);
						DatabaseHandler objDBHandler=new DatabaseHandler(ChatActivity.this);
						listChatData.add(objChatMessageData);
						objDBHandler.insertMessageData(objChatMessageData);

					} 
				} 
			}else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ChatActivity.this, R.string.request_timeout, Toast.LENGTH_SHORT).show();
					}
				});
			}


			return objChatMessage;
		}
		@Override
		protected void onPostExecute(ChatMessageData result) {
			super.onPostExecute(result);

			messageAdapter.notifyDataSetChanged();
			chatListView.setSelection(listChatData.size());
		}
	}
	private class BackgroundforMessagehistory extends AsyncTask<String, Void, ChatMessageData>{

		@Override
		protected ChatMessageData doInBackground(String... params) {
			Utility myUtility=new Utility();
			List<NameValuePair> sendMessageReqList=myUtility.getPullHistoryReq(params);
			String messageResponse=myUtility.makeHttpRequest(CommonConstant.getChatHistory_url, CommonConstant.methodeName, sendMessageReqList);
			ChatMessageData objChatMessage=null;
			if (messageResponse!=null) {
				Gson gson = new Gson();
				objChatMessage=gson.fromJson(messageResponse, ChatMessageData.class);

				if (objChatMessage!=null && objChatMessage.getErrFlag()==0) {	
					List<ChatMessageList> listChat=objChatMessage.getListChat();
					ChatMessageList objChatMessageData=listChat.get(0);

					objChatMessageData.setStrSenderId(objChatMessageData.getStrSenderFacebookId());
					objChatMessageData.setStrReceiverId(userFacebookid);
					objChatMessageData.setStrFlagForMessageSuccess("1");

					DatabaseHandler objDBHandler=new DatabaseHandler(ChatActivity.this);
					objDBHandler.insertMessageData(objChatMessageData);
					bFlagForHistoryMessage = false;	
				} 
			}else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ChatActivity.this, R.string.request_timeout, Toast.LENGTH_SHORT).show();
					}
				});
			}
			return objChatMessage;

		}
		@Override
		protected void onPostExecute(ChatMessageData result) {
			super.onPostExecute(result);
			if (result!=null && result.getErrFlag()==0) {		
				List<ChatMessageList> listChat=result.getListChat();
				listChatData.clear();
				listChatData.addAll(listChat);
				messageAdapter.notifyDataSetChanged();
				if (listChatData!=null && listChatData.size()>0) {
					chatListView.setSelection(listChatData.size());
				}
			} 
		}
	}
	private class BackgroundForInsertMessageDB extends AsyncTask<ChatMessageList, Void, String>{
		@Override
		protected String doInBackground(ChatMessageList... params) {
			DatabaseHandler objDBHandler=new DatabaseHandler(ChatActivity.this);
			params[0].setStrReceiverId(userFacebookid);
			params[0].setStrFlagForMessageSuccess("1");
			objDBHandler.insertMessageData(params[0]);
			return null;
		}
	}

	public class AwesomeAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<ChatMessageList> mMessages;

		public AwesomeAdapter(Context context, ArrayList<ChatMessageList> messages) {
			super();
			this.mContext = context;
			this.mMessages = messages;
		}
		@Override
		public int getCount() {
			return mMessages.size();
		}
		@Override
		public Object getItem(int position) {		
			return mMessages.get(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatMessageList message = (ChatMessageList) this.getItem(position);
			ViewHolder holder; 

			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.new_sms_row, null, false);
				ScalingUtility.getInstance((Activity)mContext).scaleView(convertView);

				holder = new ViewHolder();
				holder.sentMessagesLayout=(RelativeLayout)convertView.findViewById(R.id.sentMessagesLayout);
				holder.receivedMessageLayout=(RelativeLayout)convertView.findViewById(R.id.receivedMessagesLayout);
				holder.sentMessageTextView = (TextView) convertView.findViewById(R.id.sentTextView);
				holder.receivedMessageTextView = (TextView) convertView.findViewById(R.id.receivedTextView);
				holder.senderImageView=(ImageView)convertView.findViewById(R.id.senderImageView);
				holder.receiverImage=(ImageView)convertView.findViewById(R.id.receiverImageView);

				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}

			if(userFacebookid.equals(""+message.getStrSenderFacebookId())){
				holder.receivedMessageLayout.setVisibility(View.GONE);
				holder.sentMessagesLayout.setVisibility(View.VISIBLE);

				holder.sentMessageTextView.setText(message.getStrMessage());
				holder.senderImageView.setImageBitmap(friendBitmap);

			}
			else{		
				holder.receivedMessageLayout.setVisibility(View.VISIBLE);
				holder.sentMessagesLayout.setVisibility(View.GONE);

				holder.receivedMessageTextView.setText(message.getStrMessage());
				holder.receiverImage.setImageBitmap(userBitmap);
			}
			return convertView;
		}

		private class ViewHolder{
			TextView sentMessageTextView,receivedMessageTextView;
			ImageView senderImageView,receiverImage;	
			RelativeLayout sentMessagesLayout,receivedMessageLayout;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	private class BackGroundForSendMessage extends AsyncTask<String, Void, SendMessageResponse>{
	
		@Override
		protected SendMessageResponse doInBackground(String... arg0) {
			Utility myUtility=new Utility();
			List<NameValuePair> sendMessageReqList=myUtility.getSendMessageReq(arg0);
			String messageResponse=myUtility.makeHttpRequest(CommonConstant.sendMessage_url, CommonConstant.methodeName, sendMessageReqList);
			SendMessageResponse sendMessagerespObj=null;
			
			if (messageResponse!=null) {
				
				Gson gson = new Gson();
				sendMessagerespObj=gson.fromJson(messageResponse, SendMessageResponse.class);

				objMessageData =new ChatMessageList();
				objMessageData.setStrSenderFacebookId(userFacebookid);
				objMessageData.setStrMessage(arg0[3]);
				objMessageData.setStrDateTime("");
				objMessageData.setStrSendername("");
				objMessageData.setStrSenderId(userFacebookid);
				objMessageData.setStrReceiverId(strFriendFbId);
				
				String date=DateFormat.getDateTimeInstance().format(new Date());
				SessionManager session=new SessionManager(ChatActivity.this);
				session.setLastMessage(strFriendFbId, arg0[3]);
				session.setLastMsgTime(strFriendFbId, date);

				DatabaseHandler objDBHandler=new DatabaseHandler(ChatActivity.this);
				if (sendMessagerespObj!=null && sendMessagerespObj.getStatusNumber()==0) {
					objMessageData.setStrFlagForMessageSuccess("1");
					objDBHandler.insertMessageData(objMessageData);

				} else{
					objMessageData.setStrFlagForMessageSuccess("0");
					objDBHandler.insertMessageData(objMessageData);
				}
			}
			return sendMessagerespObj;
		}

		@Override
		protected void onPostExecute(SendMessageResponse result) {
			super.onPostExecute(result);
			if(result==null){
				Toast.makeText(ChatActivity.this, R.string.request_timeout, Toast.LENGTH_SHORT).show();	
			}
//			if (result!=null && result.getStatusNumber()!=0) {	
//				//Toast.makeText(ChatActivity.this, result.getStatusMessage()	, Toast.LENGTH_SHORT).show();	
//			}
		}
	}
	private class BlockMatchTask extends AsyncTask<String,Void, Void>{
		private Ultilities mUltilities=null;
		private String httpResponse=null;
		private List<NameValuePair>paramtersPairList=null;
		private boolean shouldReportUser=false;

		public BlockMatchTask(boolean shouldReport){
			mUltilities=new Ultilities();
			shouldReportUser=shouldReport;
		}
		@Override
		protected Void doInBackground(String... params) {
			paramtersPairList=mUltilities.getBlockMatchParamters(params);
			httpResponse= mUltilities.makeHttpRequest(CommonConstant.blockMatch_url,CommonConstant.methodeName,paramtersPairList);
			DatabaseHandler mDatabaseHandler =new DatabaseHandler(ChatActivity.this);
			int delete=mDatabaseHandler.deleteBlockedUserMatch(params[2]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);		
			hideProgressDialog();
			AllMatchesSlideWindow.refreshMatchesList();
			if(shouldReportUser){
				sendEmail();
			}
			ChatActivity.this.finish();
		}

	}
	@Override
	public void onClick(View arg0){
		if (arg0.getId()==R.id.send_chat_message_button) {
			String chatMessage=chatEditText.getText().toString().trim();

			if (chatMessage!=null && chatMessage.length()>0) {
				/*if (Utility.isNetworkAvailable(ChatActivity.this)) {*/
				objMessageData =new ChatMessageList();
				objMessageData.setStrSenderFacebookId(userFacebookid);
				objMessageData.setStrMessage(chatMessage);
				objMessageData.setStrDateTime("");
				objMessageData.setStrSendername("");
				objMessageData.setStrSenderId(userFacebookid);
				objMessageData.setStrReceiverId(strFriendFbId);

				listChatData.add(objMessageData);
				chatEditText.setText("");
				messageAdapter.notifyDataSetChanged();
				chatListView.setSelection(listChatData.size());

				String params[]={sessiosntoken,deviceId,strFriendFbId,chatMessage};
				new BackGroundForSendMessage().execute(params);
			} 
			else {
				Toast.makeText(ChatActivity.this, R.string.enter_message	, Toast.LENGTH_SHORT).show();
			}
		}
		else if (arg0.getId()==R.id.loadmore_button) {
			pageNum=1;
			startLimit=startLimit+limit;
			String params[]={"1"};
			new BackgroundForGetDataFromDB().execute(params);	
		}
		else if (arg0.getId()==R.id.senderimage){
			SessionManager mSessionManager =new SessionManager(ChatActivity.this);
			mSessionManager.setMatchedUserFacebookId(strFriendFbId);
			mSessionManager.setImageIndexForLikeDislike(0);

			Intent mIntent=new Intent(ChatActivity.this, MatchedUserProfile.class);
			Bundle bundle=new Bundle();
			bundle.putBoolean(CommonConstant.isFromChatScreen, true);
			mIntent.putExtras(bundle);
			startActivity(mIntent);
		}
		else if (arg0.getId()==R.id.userinfoimageview){
			SessionManager mSessionManager =new SessionManager(ChatActivity.this);
			mSessionManager.setMatchedUserFacebookId(strFriendFbId);
			mSessionManager.setImageIndexForLikeDislike(0);

			Intent mIntent=new Intent(ChatActivity.this, MatchedUserProfile.class);
			Bundle bundle=new Bundle();
			bundle.putBoolean(CommonConstant.isFromChatScreen, true);
			mIntent.putExtras(bundle);

			startActivity(mIntent);
		}else if(arg0.getId()==R.id.backButtonClickArea){
			finish();
		}
	}

}
