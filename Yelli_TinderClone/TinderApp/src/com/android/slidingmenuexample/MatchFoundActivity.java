package com.android.slidingmenuexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.Ultilities;
import com.appdupe.flamerchatmodul.db.DatabaseHandler;
import com.embed.anddroidpushntificationdemo11.ChatActivity;

public class MatchFoundActivity extends Activity implements OnClickListener {

	private TextView 	matchesNameTextView=null;
	private ImageView 	userImageview=null;
	private ImageView   friendImageview=null;
	private Button 		sendMessageButton=null;
	private Button      continueBrowsingButton=null;
	private String 		strSenderFbId="";
	private int  pictureDimensions;
	private LikeMatcheddataForListview matchedData=new LikeMatcheddataForListview();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View matchedUserView = inflater.inflate(R.layout.match_found_new_screen, null, false);
		ScalingUtility.getInstance(this).scaleView(matchedUserView);
		setContentView(matchedUserView);

		pictureDimensions=ScalingUtility.getInstance(this).resizeAspectFactor(FlamerConstants.MATCH_FOUND_PIC_SIZE);
		initComponent();
		Bundle bucket=getIntent().getExtras();
		if (bucket!=null) {
			strSenderFbId=bucket.getString("SENDER_FB_ID");
			String strSenderuserName=bucket.getString("SENDER_USERNAME");
			matchesNameTextView.setText("You and "+strSenderuserName+" have liked each other.");
			
			matchedData.setUserName(strSenderuserName);
			matchedData.setFacebookid(strSenderFbId);
		}

		setProfilePick(userImageview);
		setFriendImage();	
		sendMessageButton.setOnClickListener(this);
		continueBrowsingButton.setOnClickListener(this);
		
		if(MainActivity.arryList==null){
			MainActivity.arryList=new ArrayList<LikeMatcheddataForListview>();
		}
		MainActivity.arryList.add(matchedData);
	}

	private void setFriendImage() {
		DatabaseHandler mDatabaseHandler=new DatabaseHandler(this);
		Ultilities mUltilities=new Ultilities();

		LikeMatcheddataForListview matcheddataForListview= mDatabaseHandler.getSenderDetail(strSenderFbId);
		String imagePath=matcheddataForListview.getFilePath();
		Bitmap bitmapimage = mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(imagePath);
		ScalingUtilities mScalingUtilities =new ScalingUtilities();
		Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,pictureDimensions, pictureDimensions, ScalingLogic.CROP);
		bitmapimage.recycle();
		Bitmap friendImage= mUltilities.getCircleBitmap(cropedBitmap, 1);
		friendImageview.setImageBitmap(friendImage);
		cropedBitmap.recycle();
		
		matchedData.setFilePath(matcheddataForListview.getFilePath());
		matchedData.setImageUrl(matcheddataForListview.getImageUrl());
	}


	private void setProfilePick(ImageView userProfilImage){
		Ultilities mUltilities=new Ultilities();

		DatabaseHandler mdaDatabaseHandler=new DatabaseHandler(this);
		String imageOrderArray[]={"1"};
		ArrayList<ImageDetail>imagelist=	mdaDatabaseHandler.getImageDetailByImageOrder(imageOrderArray);
		if (imagelist!=null && imagelist.size()>0) {

			Bitmap	bitmapimage = mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(imagelist.get(0).getSdcardpath());
			Bitmap cropedBitmap=null;
			ScalingUtilities mScalingUtilities =new ScalingUtilities();
			Bitmap mBitmap=null;
			if (bitmapimage!=null) {
				cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,pictureDimensions,pictureDimensions, ScalingLogic.CROP);
				bitmapimage.recycle();
				mBitmap=	 mUltilities.getCircleBitmap(cropedBitmap, 1);
				cropedBitmap.recycle();
				userProfilImage.setImageBitmap(mBitmap);
			}		   
		}
	}
	private void initComponent(){
		matchesNameTextView	    =(TextView) findViewById(R.id.matchNameTextView);
		userImageview		    =(ImageView) findViewById(R.id.userImageView);
		friendImageview		    =(ImageView) findViewById(R.id.friendImageView);
		sendMessageButton	    =(Button) findViewById(R.id.sendMessageButton);
		continueBrowsingButton	=(Button) findViewById(R.id.continueBrowButton);
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.sendMessageButton) {
			Bundle mBundle=new Bundle();
			mBundle.putString(CommonConstant.FRIENDFACEBOOKID, strSenderFbId);
			mBundle.putString(CommonConstant.CHECK_FOR_PUSH_OR_NOT, "1");

			Intent mIntent=new Intent(MatchFoundActivity.this, ChatActivity.class);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			finish();
		}
		if (v.getId()==R.id.continueBrowButton) {
			finish();
		}
	}
}
