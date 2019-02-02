package com.appdupe.flamerapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.slidingmenuexample.MainActivity;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.embed.anddroidpushntificationdemo11.ChatActivity;

public class MessagesActivity extends Activity{
	private ListView messagesListView=null;
	private ArrayList<LikeMatcheddataForListview> matchesList=null;
	private Context mContext=null;
	private int PIC_SIZE=0;
	private SessionManager sessionManager=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View messageView = inflater.inflate(R.layout.new_messages_screen, null, false);
		ScalingUtility.getInstance(this).scaleView(messageView);
		setContentView(messageView);
		
		sessionManager=new SessionManager(this);
        PIC_SIZE=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.MATCHES_LIST_VAR_SIZE);
		matchesList=MainActivity.arryList;
		if(matchesList!=null && matchesList.size()>0){
			messagesListView=(ListView)findViewById(R.id.messagesListView);
			messagesListView.setAdapter(new MessagesAdapter());
			messagesListView.setOnItemClickListener(messagesItemClickListener);
		}
		findViewById(R.id.backButtonClickArea).setOnClickListener(backClickListener);
	}
	private OnClickListener backClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		   MessagesActivity.this.finish();	
		}
	};
	private OnItemClickListener messagesItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			LikeMatcheddataForListview matcheddataForListview=(LikeMatcheddataForListview)matchesList.get(position);

			String faceboolid=matcheddataForListview.getFacebookid();
			Bundle mBundle=new Bundle();
			mBundle.putString(CommonConstant.FRIENDFACEBOOKID, faceboolid);
			mBundle.putString(CommonConstant.CHECK_FOR_PUSH_OR_NOT, "1");

			Intent mIntent=new Intent(mContext,ChatActivity.class);
			mIntent.putExtras(mBundle);
			mContext.startActivity(mIntent);
			
			MessagesActivity.this.finish();
			
		}
	};

	private class MessagesAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return matchesList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.messages_list_row, null);
	            ScalingUtility.getInstance(((Activity)mContext)).scaleView(convertView);
	        }
			ImageView userImageView=(ImageView)convertView.findViewById(R.id.matchIconView);
			TextView  matchNameView=(TextView)convertView.findViewById(R.id.matchNameView);
			TextView  messageView=(TextView)convertView.findViewById(R.id.messageTextView);
			TextView  timeView=(TextView)convertView.findViewById(R.id.timeTextView);
			
			userImageView.setImageBitmap(getCircleBitmap(matchesList.get(position).getFilePath()));
			matchNameView.setText(matchesList.get(position).getUserName());
			String lastMessage=sessionManager.getLastMessage(matchesList.get(position).getFacebookid());
			String lastMsgTime=sessionManager.getLastMsgTime(matchesList.get(position).getFacebookid());
			messageView.setText(lastMessage);
			timeView.setText(lastMsgTime);
			return convertView;
		}
		private Bitmap getCircleBitmap(String imagePath){
			Ultilities mUltilities=new Ultilities();
			Bitmap bitmapimage = mUltilities.showImage(imagePath);
			ScalingUtilities mScalingUtilities =new ScalingUtilities();
			Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,PIC_SIZE, PIC_SIZE, ScalingLogic.CROP);
			bitmapimage.recycle();
			Bitmap resultBitmap= mUltilities.getCircleBitmap(cropedBitmap, 1);
			cropedBitmap.recycle();

			return resultBitmap;
		}

	}

}
