//package com.android.slidingmenuexample;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.appdupe.flamerapp.R;
//import com.appdupe.flamerapp.pojo.ImageDeleteData;
//import com.appdupe.flamerapp.pojo.ImageDetail;
//import com.appdupe.flamerapp.utility.CommonConstant;
//import com.appdupe.flamerapp.utility.ScalingUtilities;
//import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
//import com.appdupe.flamerapp.utility.SessionManager;
//import com.appdupe.flamerapp.utility.Ultilities;
//import com.appdupe.flamerchatmodul.db.DatabaseHandler;
//import com.google.gson.Gson;
//
//public class EditProfileActivity1 extends Activity implements OnClickListener,TextWatcher
//{	
//	private static final String TAG = "EditeProfileActivity";
//	 private static boolean mDebugLog = true;
//	   	private static    String mDebugTag = "EditeProfileActivity";
//		void logDebug(String msg)
//		{			
//			if (mDebugLog) 
//			{
//				Log.d(mDebugTag, msg);
//			}
//		}
//		
//		void logError(String msg)
//		{
//			
//			if (mDebugLog) 
//			{
//				Log.e(mDebugTag, msg);
//			}
//		}
//		
//	private ImageView userprofilpicture;
//	//private  ImageView fiveImageview;
//	private  ImageView firtstImage;
//	private  ImageView secondImage;
//	private  ImageView thirdimageview;
//	private  ImageView fourtthimageview;
//	private ImageView fifthImageview;
//	
//	private ProgressBar progressprofileImage;
//	
//	private ProgressBar progressbarforfrirstImage;
//	private ProgressBar progressbarforsecondImage;
//	private ProgressBar progressbarforthirdImage;
//	private ProgressBar progressbarforfourthImage;
//	
//	private ProgressBar progressbarforfifthImage;
//	
//	//private int imageIndex;
//	private EditText userbouteditext;
//	private TextView textnumbertextview,editprofileTextivew;
//	private static int textCount;
//	private String aboutUser;
//	private boolean isProFileImageSeted;
//	private boolean isFirstImageSeted;
//	private boolean isSecondImageSeted;
//	private boolean isThirdImageSeted;
//	private boolean isFourthImageSeted;
//	private boolean isFifthImageSeted;
//	private RelativeLayout profileImagelayout;
//	private RelativeLayout plusbuttonlayout;
//	private RelativeLayout firtstImagelayout;
//	private RelativeLayout secondImagelayout;
//	private RelativeLayout thirdimageviewlayout;
//	private RelativeLayout fourtthimageviewlayout;
//	private LinearLayout deleteandeditbuttonlayout;
//	private RelativeLayout fifthimageviewlayout;
//	private Button plusbutton;
//	private boolean isprofileImageselected;
//	private boolean isFirstImageselected;
//	private boolean issecondImageselected;
//	private boolean isThirdImageImageselected;
//	private boolean isFourthImageselected;
//	private boolean isFifthImageselected;
//	
//	
//	private Button swepbutton;
//	private Button editbuton;
//	private Button exchangeprofile;
//	private Button deletbutton;
//	
//	
//	//private int profileImagex;
//	//private int profileImagey;
//	
//	//private int firstImagex;
//	//private int firstImagey;
//	
////	private boolean isProfileimageselectedforonclick;
////	private boolean isFirstimageselectedforonclick;
////	private boolean issecondmageselectedforonclick;
////	private boolean isthirdimageselectedforonclick;
////	private boolean isfourthimageselectedforonclick;
//	//private RelativeLayout editprofilemainlayout;
////	private boolean isfifthimageselectedforonclick;
//	
//	private boolean isPlushButtonvisible;
//	
////	private boolean flagForFileImageAndFirstImage;
//	//private SessionManager mSessionManager;
//	//RelativeLayout firtstImagelayout, profileImagelayout;
//	private ProgressDialog mDialog;
//	
//	//private int topOffset;
//	
//	//private boolean flagForonlyProfileImageSelected;
//        @Override
//        protected void onCreate(Bundle savedInstanceState) 
//        {
//        	super.onCreate(savedInstanceState);
//        	setContentView(R.layout.editprofilescreen);
//        //	 mSessionManager=new SessionManager(this);
//        	userprofilpicture=(ImageView)findViewById(R.id.userprofilpicture);
//           	firtstImage=(ImageView)findViewById(R.id.firtstImage);
//        	secondImage=(ImageView)findViewById(R.id.secondImage);
//        	thirdimageview=(ImageView)findViewById(R.id.thirdimageview);
//        	fourtthimageview=(ImageView)findViewById(R.id.fourtthimageview);
//        	fifthImageview=(ImageView)findViewById(R.id.fifthImageview);
//           	editprofileTextivew=(TextView)findViewById(R.id.editprofileTextivew);
//            Typeface topbartextviewFont=Typeface.createFromAsset(getAssets(), "fonts/HelveticaLTStd-Light.otf");
//            editprofileTextivew.setTypeface(topbartextviewFont);
//            editprofileTextivew.setTextColor(Color.rgb(255, 255, 255));
//            editprofileTextivew.setTextSize(20);
//        	
//            progressprofileImage=(ProgressBar)findViewById(R.id.progressprofileImage);
//        	progressbarforfrirstImage=(ProgressBar)findViewById(R.id.progressbarforfrirstImage);
//        	progressbarforsecondImage=(ProgressBar)findViewById(R.id.progressbarforsecondImage);
//        	progressbarforthirdImage=(ProgressBar)findViewById(R.id.progressbarforthirdImage);
//        	progressbarforfourthImage=(ProgressBar)findViewById(R.id.progressbarforfourthImage);
//        	progressbarforfifthImage=(ProgressBar)findViewById(R.id.progressbarforfifthImage);
//        //	editprofilemainlayout=(RelativeLayout)findViewById(R.id.editprofilemainlayout);
//        	
//        	progressprofileImage.setVisibility(View.GONE);
//			progressbarforfrirstImage.setVisibility(View.GONE);
//        	progressbarforsecondImage.setVisibility(View.GONE);
//        	progressbarforthirdImage.setVisibility(View.GONE);
//				
//			progressbarforfourthImage.setVisibility(View.GONE);
//			progressbarforfifthImage.setVisibility(View.GONE);
//        	
//			DisplayMetrics dm = new DisplayMetrics();
//	    	getWindowManager().getDefaultDisplay().getMetrics(dm);
//	    //	int topOffset = dm.heightPixels - editprofilemainlayout.getMeasuredHeight();
//			
//        	userprofilpicture.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
//        	{
//        	    public void onGlobalLayout() 
//        	    {
//        	    	userprofilpicture.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//        	        int[] locations = new int[2];
//        	        userprofilpicture.getLocationOnScreen(locations);
//        	      // 	profileImagex = locations[0];
//        	    	//profileImagey = locations[1];
//        	    
//        	    }
//        	});
//        	
//        	firtstImage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
//        	{
//        	    public void onGlobalLayout() 
//        	    {
//        	    	firtstImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//        	        int[] locations = new int[2];
//        	        firtstImage.getLocationOnScreen(locations);
//        	       // firstImagex = locations[0];
//        	       // firstImagey = locations[1];
//        	       
//        	    }
//        	});
//        	
//        	userbouteditext=(EditText)findViewById(R.id.userbouteditext);
//        	textnumbertextview=(TextView)findViewById(R.id.textnumbertextview);
//        	profileImagelayout=(RelativeLayout)findViewById(R.id.profileImagelayout);
//        	plusbuttonlayout=(RelativeLayout)findViewById(R.id.plusbuttonlayout);
//        	firtstImagelayout=(RelativeLayout)findViewById(R.id.firtstImagelayout);
//        	secondImagelayout=(RelativeLayout)findViewById(R.id.secondImagelayout);
//        	thirdimageviewlayout=(RelativeLayout)findViewById(R.id.thirdimageviewlayout);
//        	fourtthimageviewlayout=(RelativeLayout)findViewById(R.id.fourtthimageviewlayout);
//        	deleteandeditbuttonlayout=(LinearLayout)findViewById(R.id.deleteandeditbuttonlayout);
//        	fifthimageviewlayout=(RelativeLayout)findViewById(R.id.fifthimageviewlayout);
//        	
//        	swepbutton=(Button)findViewById(R.id.swepbutton); // make gone
//        	plusbutton=(Button)findViewById(R.id.plusbutton);
//        	editbuton=(Button)findViewById(R.id.editbuton);
//        	exchangeprofile=(Button)findViewById(R.id.exchangeprofile); // make gone
//        	deletbutton=(Button)findViewById(R.id.deletbutton);
//        	
//        	plusbuttonlayout.setVisibility(View.GONE);
//        	SessionManager mSessionManager=new SessionManager(this);
//        	aboutUser=mSessionManager.getUserAbout();
//        	if (aboutUser!=null&&aboutUser.length()>0)
//        	{
//        		userbouteditext.setText(aboutUser);
//            	
//            	textCount=userbouteditext.getText().toString().length();
//            	textCount=500-textCount;
//            	textnumbertextview.setText(""+textCount);
//			}
//        	
//        	userbouteditext.addTextChangedListener(this);
//        //	fiveImageview=(ImageView)findViewById(R.id.fiveImageview);
//        	new BackGroundTaskForDownloadProfileImage().execute(); 
//        //	Ultilities mUltilities=new Ultilities();
//        	
//        	try 
//        	{
//        		userprofilpicture.setOnClickListener(this);
//        		
//        		firtstImage.setOnClickListener(this);
//        		secondImage.setOnClickListener(this);
//        		thirdimageview.setOnClickListener(this);
//        		fourtthimageview.setOnClickListener(this);
//        		fifthImageview.setOnClickListener(this);
//           		plusbutton.setOnClickListener(this);
//        		exchangeprofile.setOnClickListener(this);
//        		deletbutton.setOnClickListener(this);
//        	    editbuton.setOnClickListener(this);
//			} catch (Exception e)
//			{
//				logError("onCreate   Exception "+e);
//			}
//        }
//        
//    	private  class BackGroundTaskForDownloadProfileImage extends AsyncTask<String, Void, Void>
//    	{
//                // private Utility mUtility=new Utility();
//    		  private Ultilities mUltilities =new Ultilities();
//               // private File PickDirectory;
//               //  private File appDirectory;
//               //  private File [] imageFileArray;
//               //  private SessionManager mSessionManager=new SessionManager(EditeProfileActivity.this);
//                // GellaryData   mGellaryData;
//                // private File imageFile;
//             // private int index;   
//              private int ProfilePictureHeightAndWidth[]=mUltilities.getImageHeightAndWidthForEditProfileScreen(EditProfileActivity1.this);;
//          	private int otherePictureHeightAndWidth[]=mUltilities.getImageHeightAndWidthForEditProfileScreenForOther(EditProfileActivity1.this); ;
//          	private ScalingUtilities mScalingUtilities=new ScalingUtilities();
//          	private ArrayList<ImageDetail>imagelistFormdatabase;
//			private DatabaseHandler databaseHandler=new DatabaseHandler(EditProfileActivity1.this);
//    			 // logDebug("BackGroundTaskForDownload   doInBackground appDirectory "+appDirectory);
//    		     // File	 _picDir  = new File(appDirectory, getResources().getString(R.string.imagedire));
//       //   private File profilePickDirectory;
//           
//    		@Override
//    		protected Void doInBackground(String... params) 
//    		{
//    			
//    			try 
//    			{
//    				//appDirectory = mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
//    				//logDebug("BackGroundTaskForDownloadProfileImage  doInBackground sdCardPath "+appDirectory);
//    				//PickDirectory=new File(appDirectory, getResources().getString(R.string.imagedire));
//    				//new File(appDirectory, getResources().getString(R.string.imagedire));
//    				
//    				imagelistFormdatabase=databaseHandler.getImageDetail();
//    				
//    				//logDebug("BackGroundTaskForDownloadProfileImage  doInBackground PickDirectory "+PickDirectory);
//    				//if (PickDirectory.isDirectory())
//    				//{
//    				//	imageFileArray=PickDirectory.listFiles();
//    					//logDebug("BackGroundTaskForDownloadProfileImage  doInBackground imageFileArray "+imageFileArray);
//    					if (imagelistFormdatabase!=null && imagelistFormdatabase.size()>0) 
//    					{
//    						//logDebug("BackGroundTaskForDownloadProfileImage  doInBackground imageFileArray size is "+imageFileArray.length);
//    						for (int i = 0; i < imagelistFormdatabase.size(); i++) 
//    						{
//								switch (imagelistFormdatabase.get(i).getImageOrder()-1) 
//								{
//								case 0:
//									try
//									{
//										final Bitmap usrFirstImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										final Bitmap scaledBitmap=mScalingUtilities.createScaledBitmap(usrFirstImage, ProfilePictureHeightAndWidth[1], ProfilePictureHeightAndWidth[0], ScalingLogic.CROP);
//										usrFirstImage.recycle();
//										runOnUiThread(new Runnable()
//										{
//											public void run()
//											{
//												userprofilpicture.setImageBitmap(scaledBitmap);
//												
//												isProFileImageSeted=true;
//												
//											}
//										});
//									} 
//									catch (Exception e) 
//									{
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground   profile image not fount Exception "+e);
//									}
//									
//									
//									break;
//								case 1:
//									try {
//										final Bitmap usrsecondImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										final Bitmap usrsecondscaledBitmap=mScalingUtilities.createScaledBitmap(usrsecondImage, otherePictureHeightAndWidth[1], otherePictureHeightAndWidth[0], ScalingLogic.CROP);
//										usrsecondImage.recycle();
//										runOnUiThread(new Runnable()
//										{
//											public void run()
//											{
//												firtstImage.setImageBitmap(usrsecondscaledBitmap);
//												
//												isFirstImageSeted=true;
//												
//											}
//										});
//									} catch (Exception e) 
//									{
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground First image not found Exception "+e);
//									}
//									
//									break;
//								case 2:
//									try 
//									{
//										final Bitmap usrthirdImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										
//										final Bitmap  usrthircaledBitmap=mScalingUtilities.createScaledBitmap(usrthirdImage, otherePictureHeightAndWidth[1], otherePictureHeightAndWidth[0], ScalingLogic.CROP);
//										runOnUiThread(new Runnable()
//										{
//											public void run() 
//											{
//												secondImage.setImageBitmap(usrthircaledBitmap);
//												
//												isSecondImageSeted=true;
//											
//												
//											}
//										});
//									} catch (Exception e) 
//									{	
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground second image not found Exception "+e);
//									}
//									
//									break;
//								case 3:
//									try 
//									{
//										final Bitmap usrfourthImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										final Bitmap  usrfourthscaledBitmap=mScalingUtilities.createScaledBitmap(usrfourthImage, otherePictureHeightAndWidth[1], otherePictureHeightAndWidth[0], ScalingLogic.CROP);
//										runOnUiThread(new Runnable()
//										{
//											public void run() 
//											{
//												thirdimageview.setImageBitmap(usrfourthscaledBitmap);
//											
//												isThirdImageSeted=true;
//												
//											}
//										});
//									} catch (Exception e) {
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground third image not found Exception "+e);
//									}
//									
//									break;
//								case 4:
//									try 
//									{
//										final Bitmap usrfifthImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										final Bitmap  usrfifthImagescaledBitmap=mScalingUtilities.createScaledBitmap(usrfifthImage, otherePictureHeightAndWidth[1], otherePictureHeightAndWidth[0], ScalingLogic.CROP);
//										runOnUiThread(new Runnable()
//										{
//											public void run()
//											{
//												fourtthimageview.setImageBitmap(usrfifthImagescaledBitmap);
//												isFourthImageSeted=true;
//												
//											}
//										});
//									} catch (Exception e) 
//									{
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground fourth image not found Exception "+e);
//									}
//									
//									break;
//								case 5:
//									try 
//									{
//										final Bitmap usrsixthImage=mUltilities.showImage(imagelistFormdatabase.get(i).getSdcardpath());
//										final Bitmap  usrsixthImagescaledBitmap=mScalingUtilities.createScaledBitmap(usrsixthImage, otherePictureHeightAndWidth[1], otherePictureHeightAndWidth[0], ScalingLogic.CROP);
//										runOnUiThread(new Runnable()
//										{
//											public void run()
//											{
//												fifthImageview.setImageBitmap(usrsixthImagescaledBitmap);
//												isFifthImageSeted=true;
//											}
//										});
//									} catch (Exception e) 
//									{
//										logError("BackGroundTaskForDownloadProfileImage  doInBackground fifth image not found Exception "+e);
//									}
//									
//									break;
//								
//								
//								}
//							}
//    					}
//    	 
//    			//	}
//    				
//    			 
//    			  // mAdapterForGellary.notifyDataSetChanged();
//    			} catch (Exception e) 
//    			{
//    				 logError("BackGroundTaskForDownloadProfileImage  doInBackground Exception"+e);
//    			}
//    			return null;
//    		}
//
//    		@Override
//    		protected void onPostExecute(Void result) 
//    		{
//    			super.onPostExecute(result);
//    			try 
//    			{
//    				mDialog.dismiss();
//    				if (imagelistFormdatabase!=null) 
//    				{
//						if (imagelistFormdatabase.size()<6) 
//						{
//							plusbuttonlayout.setVisibility(View.VISIBLE);
//							isPlushButtonvisible=true;
//						}
//						else 
//						{
//							plusbuttonlayout.setVisibility(View.GONE);
//							isPlushButtonvisible=false;
//						}
//					}
//    			} catch (Exception e) 
//    			{
//    			}
//    		}
//    		
//    		@Override
//    		protected void onPreExecute() 
//    		{    			
//    			super.onPreExecute();
//    			mDialog=mUltilities.GetProcessDialog(EditProfileActivity1.this);
//    			mDialog.setMessage("Please Wait...");
//    			mDialog.setCancelable(false);
//    			mDialog.show();    			
//    		}    		
//    	}
//    	
//		@Override
//		public void onClick(View v)  // TODO onclick
//		{
//			
//			swepbutton.setVisibility(View.GONE);
//			exchangeprofile.setVisibility(View.GONE);
//			
//			try 
//			{
//				SessionManager manager=new SessionManager(EditProfileActivity1.this);
//				
//				if (v.getId()==R.id.userprofilpicture) 
//				{
//					//logDebug("onClick   userprofilpicture  flagforprofileImage"+flagforprofileImage);
//					//
//					if (isProFileImageSeted) 
//					{
//						if (isprofileImageselected) 
//						{
//							isprofileImageselected=false;
//							profileImagelayout.setBackgroundResource(R.drawable.profile_image_boader);
//							if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected) 
//							{
//								if (isPlushButtonvisible) 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.VISIBLE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								else 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.GONE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								
//							}
//							else if (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// only first selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								deletbutton.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							else if (!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// only second selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								deletbutton.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// only third selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								deletbutton.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							
//							else if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected) 
//							{
//								// only fourth selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								deletbutton.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							
//							else if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								// only fifth selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								deletbutton.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							
//							else if (isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// if first and second imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// if first and third imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								// if first and forth imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								// if first and fifth imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							
//							else if (!isFirstImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// if second and third imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								// if second and fourth imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								// if second and fith imageis selected
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&isFifthImageselected) 
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//								
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//						}
//						else 
//						{
//							isprofileImageselected=true;
//							profileImagelayout.setBackgroundResource(R.drawable.profile_image_boader_on);
//							if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.VISIBLE);
//								
//								//editbuton.setBackgroundResource(R.drawable.edit_btn);
//								
//							}
//							else if (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//0 1
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
//							}
//							else if (!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//0 2
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//0 3
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								//0 4
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
//							}
//							else if (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								//0 5
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							
//							
//						}
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(1);
//					}
//					
//				}
//				if (v.getId()==R.id.firtstImage) 
//				{
//					if (isFirstImageSeted) 
//					{
//						if (isFirstImageselected) 
//						{
//							isFirstImageselected=false;
//							firtstImagelayout.setBackgroundResource(R.drawable.add_image_boader);
//							if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								if (isPlushButtonvisible) 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.VISIBLE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								else 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.GONE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
////								deleteandeditbuttonlayout.setVisibility(View.GONE);
////								plusbuttonlayout.setVisibility(View.VISIBLE);
////								plusbutton.setVisibility(View.VISIBLE);
////								plusbutton.setBackgroundResource(R.drawable.plus_btn);
////								swepbutton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// 0
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.GONE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
////								plusbuttonlayout.setVisibility(View.GONE);
////								plusbutton.setVisibility(View.VISIBLE);
////								plusbutton.setBackgroundResource(R.drawable.edit_btn);
////								swepbutton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//						}
//						else 
//						{
//							isFirstImageselected=true;
//							firtstImagelayout.setBackgroundResource(R.drawable.add_image_boader_on);
//							if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							//	isFirstimageselectedforonclick=true;
//								
//								
//								
//								
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//0
//								//swepbutton.setVisibility(View.GONE);
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.user_star_icon);
//								
////								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
////								plusbuttonlayout.setVisibility(View.GONE);
////								deletbutton.setVisibility(View.GONE);
////								exchangeprofile.setVisibility(View.VISIBLE);
////								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								//5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//							
//							
//							
//							else if (isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 2
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//						}
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(2);
//					}
//					
//				}
//				if (v.getId()==R.id.secondImage) 
//				{
//					if (isSecondImageSeted)
//					{
//						if (issecondImageselected) 
//						{
//							issecondImageselected=false;
//							secondImagelayout.setBackgroundResource(R.drawable.add_image_boader);
//														
//							if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								if (isPlushButtonvisible) 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.VISIBLE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								else 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.GONE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
////								deleteandeditbuttonlayout.setVisibility(View.GONE);
////								plusbuttonlayout.setVisibility(View.VISIBLE);
////								plusbutton.setVisibility(View.VISIBLE);
////								plusbutton.setBackgroundResource(R.drawable.plus_btn);
////								swepbutton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// 0
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.edit_btn);
//								swepbutton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//						}
//						else 
//						{
//							issecondImageselected=true;
//							secondImagelayout.setBackgroundResource(R.drawable.add_image_boader_on);
//							
//							if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//							   editbuton.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//0
//								//swepbutton.setVisibility(View.GONE);
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
////								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
////								plusbuttonlayout.setVisibility(View.GONE);
////								deletbutton.setVisibility(View.GONE);
////								exchangeprofile.setVisibility(View.VISIBLE);
////								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								//5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//							
//							
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 2
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!isThirdImageImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else {
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//						}
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(3);
//					}
//					
//				}
//				if (v.getId()==R.id.thirdimageview) 
//				{
//					if (isThirdImageSeted)
//					{
//						if (isThirdImageImageselected) 
//						{
//							isThirdImageImageselected=false;
//							thirdimageviewlayout.setBackgroundResource(R.drawable.add_image_boader);
//							if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								if (isPlushButtonvisible) 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.VISIBLE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								else 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.GONE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
////								deleteandeditbuttonlayout.setVisibility(View.GONE);
////								plusbuttonlayout.setVisibility(View.VISIBLE);
////								plusbutton.setVisibility(View.VISIBLE);
////								plusbutton.setBackgroundResource(R.drawable.plus_btn);
////								swepbutton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								// 0
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.edit_btn);
//								swepbutton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//						}
//						else 
//						{
//							isThirdImageImageselected=true;
//							thirdimageviewlayout.setBackgroundResource(R.drawable.add_image_boader_on);
//							
//							
//							if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								//0
//								//swepbutton.setVisibility(View.GONE);
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
////								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
////								plusbuttonlayout.setVisibility(View.GONE);
////								deletbutton.setVisibility(View.GONE);
////								exchangeprofile.setVisibility(View.VISIBLE);
////								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected) 
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected) 
//							{
//								//5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//							
//							
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 2
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&isFourthImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isFourthImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isFourthImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//										
//						}
//					}
//					
//						else 
//						{
//							Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//							startActivity(mIntent);
//							manager.setImageIndex(4);
//						}
//						
//					
//					
//				}
//				if (v.getId()==R.id.fourtthimageview) 
//				{
//					if (isFourthImageSeted) 
//					{
//						if (isFourthImageselected) 
//						{
//							isFourthImageselected=false;
//							fourtthimageviewlayout.setBackgroundResource(R.drawable.add_image_boader);
//							if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								if (isPlushButtonvisible) 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.VISIBLE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
//								else 
//								{
//									deleteandeditbuttonlayout.setVisibility(View.GONE);
//									plusbuttonlayout.setVisibility(View.GONE);
//									plusbutton.setVisibility(View.VISIBLE);
//									plusbutton.setBackgroundResource(R.drawable.plus_btn);
//									swepbutton.setVisibility(View.GONE);
//								}
////								deleteandeditbuttonlayout.setVisibility(View.GONE);
////								plusbuttonlayout.setVisibility(View.VISIBLE);
////								plusbutton.setVisibility(View.VISIBLE);
////								plusbutton.setBackgroundResource(R.drawable.plus_btn);
////								swepbutton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								// 0
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.edit_btn);
//								swepbutton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								//1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//							}
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 1
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.GONE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.image_swap);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//						}
//						else 
//						{
//							isFourthImageselected=true;
//							fourtthimageviewlayout.setBackgroundResource(R.drawable.add_image_boader_on);
//							if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.VISIBLE);
//								exchangeprofile.setBackgroundResource(R.drawable.user_star_icon);
//								plusbuttonlayout.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								//0
//								//swepbutton.setVisibility(View.GONE);
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.VISIBLE);
//								plusbutton.setVisibility(View.VISIBLE);
//								plusbutton.setBackgroundResource(R.drawable.image_swap);
//								
////								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
////								plusbuttonlayout.setVisibility(View.GONE);
////								deletbutton.setVisibility(View.GONE);
////								exchangeprofile.setVisibility(View.VISIBLE);
////								editbuton.setVisibility(View.GONE);
//								
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								//2
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								//3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected) 
//							{
//								//4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected) 
//							{
//								//5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.VISIBLE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//							
//							
//							
//							
//							else if (isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 2
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 3
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 0 4
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 0 5
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							else if (!isprofileImageselected&&isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 2 3
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 2 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 2 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFifthImageselected)
//							{
//								// 3 4
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 3 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&isFifthImageselected)
//							{
//								// 4 5
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE);
//								editbuton.setVisibility(View.GONE);
//							}
//							else if(!isprofileImageselected)
//							{
//								deleteandeditbuttonlayout.setVisibility(View.VISIBLE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							else 
//							{
//								deleteandeditbuttonlayout.setVisibility(View.GONE);
//								plusbuttonlayout.setVisibility(View.GONE);
//								deletbutton.setVisibility(View.VISIBLE);
//								exchangeprofile.setVisibility(View.GONE );
//								editbuton.setVisibility(View.GONE);
//							}
//							
//							
//						}
//					}
//					else
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(5);
//					}
//					
//				}
//				if (v.getId()==R.id.fifthImageview)
//				{
//					if (isFifthImageSeted) 
//					{
//						if (isFifthImageselected) 
//						{
//							isFifthImageselected=false;
//							fifthimageviewlayout.setBackgroundResource(R.drawable.add_image_boader);
//						}
//						else 
//						{
//							isFifthImageselected=true;
//							fifthimageviewlayout.setBackgroundResource(R.drawable.add_image_boader_on);
//						}
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(6);
//					}
//				}
//				if (v.getId()==R.id.plusbutton)
//				{
//					if (isProFileImageSeted) 
//					{
//						
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(1);
//						finish();
//						return ;
//					}
//					if (isFirstImageSeted) 
//					{
//						
//					}
//					else
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(2);
//						finish();
//						return ;
//					}
//					if (isSecondImageSeted)
//					{
//						
//					}
//					else
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(3);
//						finish();
//						return ;
//					}
//					if (isThirdImageSeted) {
//						
//					}
//					else {
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(4);
//						finish();
//						return ;
//					}
//					if (isFourthImageSeted)
//					{
//						
//					}
//					else 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(5);
//						finish();
//						return ;
//					}
//					if (isFifthImageSeted)
//					{
//						
//					}
//					else {
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(6);
//						finish();
//						return ;
//					}
//				}
//				if (v.getId()==R.id.editbuton) 
//				{
//					if (isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(1);
//						
//					}
//					else if (!isprofileImageselected&&isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(2);
//					}
//					else if (!isprofileImageselected&&!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(3);
//					}
//					
//					else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(4);
//					}
//					else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(5);
//					}
//					else if (!isprofileImageselected&&!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//					{
//						Intent mIntent =new Intent(EditProfileActivity1.this, AlbumListView.class);
//						startActivity(mIntent);
//						manager.setImageIndex(6);
//					}
//					
//					
//					
//				}
//				if (v.getId()==R.id.deletbutton) 
//				{
//					
//					logDebug("delete button press");
//					if (isprofileImageselected)
//					{
//						
//					}
//					else 
//					{
//						
//						
//						
//						
//						//first image seledcted
//						if (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{
//																		
//							String [] imageoderArray={"2"};
//						     deleteImage(imageoderArray);
//												
//						}	
//						
//						//second image seledcted
//						else if (!isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{
//							
//							
//													
//							String [] imageoderArray={"3"};
//							
//							deleteImage(imageoderArray);
//							
//							
//							//swapView(userprofilpicture,firtstImage);
//						}	
//						//third image seledcted
//						else if  (!isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{
//							
//							
//													
//							String [] imageoderArray={"4"};
//							
//							deleteImage(imageoderArray);
//							
//							
//							//swapView(userprofilpicture,firtstImage);
//						}
//						//fourth image seledcted
//						else if  (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//						{
//							
//							
//													
//							String [] imageoderArray={"5"};
//							
//							deleteImage(imageoderArray);
//							
//							
//							//swapView(userprofilpicture,firtstImage);
//						}	
//						//fifth image seledcted
//						else if  (!isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//						{
//							
//							
//													
//							String [] imageoderArray={"6"};
//							
//							deleteImage(imageoderArray);
//							
//							
//							//swapView(userprofilpicture,firtstImage);
//						}	
//						
//						//frits and second image seledcted
//						else if  (isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{		
//							String [] imageoderArray={"2","3"};
//							
//							deleteImage(imageoderArray);
//							
//							
//							//swapView(userprofilpicture,firtstImage);
//						}
//						
//						//frits and thiid image seledcted
//						else if  (isFirstImageselected&&!issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","4"};
//							deleteImage(imageoderArray);
//					    	//swapView(userprofilpicture,firtstImage);
//						}
//						
//						//frits and fourth image seledcted
//						else if  (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","5"};
//						    deleteImage(imageoderArray);
//							//swapView(userprofilpicture,firtstImage);
//						}
//						//frits and fifth image seledcted
//						else if  (isFirstImageselected&&!issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","6"};
//							deleteImage(imageoderArray);
//							//swapView(userprofilpicture,firtstImage);
//						}
//						
//						
//						
//						//frits and second third image seledcted
//						else if  (isFirstImageselected&&issecondImageselected&&isThirdImageImageselected&&!isFourthImageselected&&!isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","3","4"};
//							deleteImage(imageoderArray);
//							//swapView(userprofilpicture,firtstImage);
//						}
//						
//						//frits and second fourth image seledcted
//						else if  (isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&isFourthImageselected&&!isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","3","5"};
//							deleteImage(imageoderArray);
//							//swapView(userprofilpicture,firtstImage);
//						}
//						//frits and second fifth image seledcted
//						else if  (isFirstImageselected&&issecondImageselected&&!isThirdImageImageselected&&!isFourthImageselected&&isFifthImageselected) 
//						{
//							String [] imageoderArray={"2","3","6"};
//							deleteImage(imageoderArray);
//							//swapView(userprofilpicture,firtstImage);
//						}
//						
//					}
//				
//				}
//				if (v.getId()==R.id.exchangeprofile) 
//				{
//					
//					
////				if (isFirstimageselectedforonclick&&!isProfileimageselectedforonclick&&!issecondmageselectedforonclick&&!isthirdimageselectedforonclick&&!isfourthimageselectedforonclick&&!isfifthimageselectedforonclick) 
////					{
////						logDebug("exchange firstimage to profileimage");
////						
////										
////						swapView(userprofilpicture,firtstImage);
////					}				
////				else if (flagForFileImageAndFirstImage)				
////				    {
////						
////					}
//				}
//			
//			} catch (Exception e) 
//			{
//				logError("onClick  Exception "+e);
//				
//			}
//			
//			
//			swepbutton.setVisibility(View.GONE);
//			exchangeprofile.setVisibility(View.GONE);
//		} // TODO
//        
////		private void swapView(ImageView imageView1,ImageView imageView2)
////		{
////			float x1,y1,x2,y2;
////			x1 = profileImagelayout.getX();
////			x2 = firtstImagelayout.getX();
////			y1 = profileImagelayout.getY();
////			y2 = firtstImagelayout.getY();
////			
////			
////			
////			 TranslateAnimation anim = new TranslateAnimation( 0, x2 , 0, y2  );
////			    anim.setDuration(10000);
////			 //   anim.setFillAfter( true );
////			    profileImagelayout.startAnimation(anim);
////				 TranslateAnimation anim1 = new TranslateAnimation( 0, -x1  , 0, y1 );
////				    anim1.setDuration(10000);
////				 //   anim.setFillAfter( true );
////				    firtstImagelayout.startAnimation(anim1);
////			
//////			ObjectAnimator imageOneAnimator = ObjectAnimator.ofFloat(
//////					imageView1, "X", profileImagex, firstImagex);
//////			ObjectAnimator imageTwoAnimator = ObjectAnimator.ofFloat(
//////					imageView2, "X", firstImagex, profileImagex	,firstImagey,profileImagex);
//////			imageOneAnimator.setDuration(10000);
//////			imageTwoAnimator.setDuration(10000);
//////			AnimatorSet set = new AnimatorSet();
//////			set.playTogether(imageOneAnimator, imageTwoAnimator);
//////			set.start();
////		}
//        
//        @Override
//        protected void onResume()
//        {
//        	super.onResume();
//        	//SessionManager manager=new SessionManager(EditeProfileActivity.this);
////        	if (manager.getIsImageSet())
////        	{
////        		reLoadWholeActivity();
////        		manager.removeImageSet();
////				
////			}
////        	else 
////        	{
////				// Image not set no 
////			}
//        	
//        }
//        
////        private void reLoadWholeActivity()
////        {
////        	Intent intent = getIntent();
////            overridePendingTransition(0, 0);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////            finish();
////            overridePendingTransition(0, 0);
////            startActivity(intent);
////        }
////        private  class BackGroundTaskForDownloadProfileImage extends AsyncTask<String, Void, Void>
////    	{
////                // private Utility mUtility=new Utility();
////    		  private Ultilities mUltilities =new Ultilities();
////                 private File PickDirectory;
////                 private File sdCardPath;
////                 private File [] imageFileArray;
////                 private SessionManager mSessionManager=new SessionManager(getActivity());
////                 GellaryData   mGellaryData;
////                 private File imageFile;
////    		@Override
////    		protected Void doInBackground(String... params) 
////    		{
////    			logDebug("BackGroundTaskForDownloadProfileImage  doInBackground ");
////    			try 
////    			{
////    				sdCardPath=mUltilities.getSdCardPath();
////    				logDebug("BackGroundTaskForDownloadProfileImage  doInBackground sdCardPath "+sdCardPath);
////    				PickDirectory=new File(sdCardPath, getResources().getString(R.string.imagedire));
////    				logDebug("BackGroundTaskForDownloadProfileImage  doInBackground PickDirectory "+PickDirectory);
////    				if (PickDirectory.isDirectory())
////    				{
////    					imageFileArray=PickDirectory.listFiles();
////    					logDebug("BackGroundTaskForDownloadProfileImage  doInBackground imageFileArray "+imageFileArray);
////    					if (imageFileArray.length>0) 
////    					{
////    						logDebug("BackGroundTaskForDownloadProfileImage  doInBackground imageFileArray.length "+imageFileArray.length);
////    						getActivity().runOnUiThread(new Runnable() 
////    						{
////    							
////    							@Override
////    							public void run() 
////    							{
////    								mAdapterForGellary.notifyDataSetChanged();
////    								count = imageFileArray.length;
////    						        System.out.println("Gallery Image Count======>>>" + count);
////    						        logDebug("BackGroundTaskForUserProfile   onPostExecute count  "+count);
////
////    						       page_text = new TextView[count];
////    						        for (int i = 0; i < count; i++) 
////    						        {
////    						            page_text[i] = new TextView(getActivity());
////    						            page_text[i].setText(".");
////    						            page_text[i].setTextSize(45);
////    						            page_text[i].setTypeface(null, Typeface.BOLD);
////    						            page_text[i].setTextColor(android.graphics.Color.GRAY);
////    						            count_layout.addView(page_text[i]);
////    						        }
////    								
////    							}
////    						});
////    						
////    						
////    						 for (int i = 0; i < imageFileArray.length; i++) 
////    						   {
////    							  imageList.add(new GellaryData());
////    							   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground imge file is"+imageFileArray[i].getAbsolutePath());
////    							   Bitmap mBitmap=mUltilities.showImage(imageFileArray[i].getAbsolutePath());
////    							   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground mBitmap"+mBitmap);
////    							   
////    							   imageList.get(i).setmBitmap(mBitmap);
////    							  getActivity().runOnUiThread(new Runnable() 
////    							  {
////    								
////    								@Override
////    								public void run()
////    			                    {
////    									mAdapterForGellary.notifyDataSetChanged();
////    									
////    								}
////    							});
////    						   }
////    					}
////    					else 
////    					{
////    						//  directory is exist but no file inside the directory
////    						String profilepick=mSessionManager.getUserPrifilePck();
////    						
////    						String profilepick1=mSessionManager.getUserPrifilePck1();
////    						String profilepick2=mSessionManager.getUserPrifilePck2();
////    						String profilepick3=mSessionManager.getUserPrifilePck3();
////    						String profilepick4=mSessionManager.getUserPrifilePck4();
////    						
////    						if (!profilepick.equals("not found"))
////    						{  
////    							mGellaryData=new GellaryData();
////    							mGellaryData.setImageUrl(profilepick);
////    							imageList.add(mGellaryData);
////    						}
////    						if (!profilepick1.equals("not found"))
////    						{  
////    							mGellaryData=new GellaryData();
////    							mGellaryData.setImageUrl(profilepick1);
////    							imageList.add(mGellaryData);
////    						}
////    						if (!profilepick2.equals("not found"))
////    						{  
////    							mGellaryData=new GellaryData();
////    							mGellaryData.setImageUrl(profilepick2);
////    							imageList.add(mGellaryData);
////    						}
////    						if (!profilepick3.equals("not found"))
////    						{  
////    							mGellaryData=new GellaryData();
////    							mGellaryData.setImageUrl(profilepick3);
////    							imageList.add(mGellaryData);
////    				    	}
////    					if (!profilepick4.equals("not found"))
////    						{  
////    							mGellaryData=new GellaryData();
////    							mGellaryData.setImageUrl(profilepick4);
////    							imageList.add(mGellaryData);
////    						}
////    					getActivity().runOnUiThread(new Runnable() 
////    					{    						
////    						@Override
////    						public void run() 
////    						{
////    							mAdapterForGellary.notifyDataSetChanged();
////    							count = imageExtendedGallery.getAdapter().getCount();
////    					        System.out.println("Gallery Image Count======>>>" + count);
////    					        logDebug("BackGroundTaskForUserProfile   onPostExecute count  "+count);
////
////    					       page_text = new TextView[mAdapterForGellary.getCount()];
////    					        for (int i = 0; i < count; i++) 
////    					        {
////    					            page_text[i] = new TextView(getActivity());
////    					            page_text[i].setText(".");
////    					            page_text[i].setTextSize(45);
////    					            page_text[i].setTypeface(null, Typeface.BOLD);
////    					            page_text[i].setTextColor(android.graphics.Color.GRAY);
////    					            count_layout.addView(page_text[i]);
////    					        }
////    							
////    						}
////    					});
////    					    						
////    					for (int i = 0; i < imageList.size(); i++) 
////    					{
////    						 switch (i) 
////    						 {
////    						 case 0:
////    							 mSessionManager.setProFilePicture(imageList.get(i).getImageUrl());
////    							 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    							 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    							 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    							 break;
////    						 case 1:
////    							 mSessionManager.setProFilePicture1(imageList.get(i).getImageUrl());
////    							 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    							 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    							 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    							 break;
////    						 case 2:
////    							 mSessionManager.setProFilePicture2(imageList.get(i).getImageUrl());
////    							  imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    							 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    							 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    							 break;
////    						 case 3:
////    							 mSessionManager.setProFilePicture3(imageList.get(i).getImageUrl());
////    							 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    							 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    							 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    							 break;
////    						 case 4:
////    							 mSessionManager.setProFilePicture4(imageList.get(i).getImageUrl());
////    							 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    							 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    							 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    							 break;
////    						}
////    					}
////    					
////    					imageFileArray=PickDirectory.listFiles();
////    					if (imageFileArray.length>0) 
////    					{
////    						 for (int i = 0; i < imageFileArray.length; i++) 
////    						   {
////    							   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground image url"+imageList.get(i).getImageUrl());
////    							   Bitmap mBitmap=mUltilities.showImage(imageFileArray[i].getAbsolutePath());
////    							   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground mBitmap"+mBitmap);
////    							   
////    							   imageList.get(i).setmBitmap(mBitmap);
////    							  getActivity().runOnUiThread(new Runnable() 
////    							  {
////    								
////    								@Override
////    								public void run()
////    			                    {
////    									mAdapterForGellary.notifyDataSetChanged();
////    									
////    								}
////    							});
////    						   }	
////    					}
////    						
////    						
////    					}
////    					
////    					 
////    				}
////    				else
////    				{
////    					// directory is deleted need to donload image again 
////    					
////    					
////
////    					
////    					String profilepick=mSessionManager.getUserPrifilePck();
////    					
////    					String profilepick1=mSessionManager.getUserPrifilePck1();
////    					String profilepick2=mSessionManager.getUserPrifilePck2();
////    					String profilepick3=mSessionManager.getUserPrifilePck3();
////    					String profilepick4=mSessionManager.getUserPrifilePck4();
////    					
////    					if (!profilepick.equals("not found"))
////    					{  
////    						mGellaryData=new GellaryData();
////    						mGellaryData.setImageUrl(profilepick);
////    						imageList.add(mGellaryData);
////    					}
////    					if (!profilepick1.equals("not found"))
////    					{  
////    						mGellaryData=new GellaryData();
////    						mGellaryData.setImageUrl(profilepick1);
////    						imageList.add(mGellaryData);
////    					}
////    					if (!profilepick2.equals("not found"))
////    					{  
////    						mGellaryData=new GellaryData();
////    						mGellaryData.setImageUrl(profilepick2);
////    						imageList.add(mGellaryData);
////    					}
////    					if (!profilepick3.equals("not found"))
////    					{  
////    						mGellaryData=new GellaryData();
////    						mGellaryData.setImageUrl(profilepick3);
////    						imageList.add(mGellaryData);
////    			    	}
////    				if (!profilepick4.equals("not found"))
////    					{  
////    						mGellaryData=new GellaryData();
////    						mGellaryData.setImageUrl(profilepick4);
////    						imageList.add(mGellaryData);
////    					}
////    				
////    				getActivity().runOnUiThread(new Runnable() 
////    				{
////    					
////    					@Override
////    					public void run() 
////    					{
////    						mAdapterForGellary.notifyDataSetChanged();
////    						count = imageExtendedGallery.getAdapter().getCount();
////    				        System.out.println("Gallery Image Count======>>>" + count);
////    				        logDebug("BackGroundTaskForUserProfile   onPostExecute count  "+count);
////
////    				       page_text = new TextView[mAdapterForGellary.getCount()];
////    				        for (int i = 0; i < count; i++) 
////    				        {
////    				            page_text[i] = new TextView(getActivity());
////    				            page_text[i].setText(".");
////    				            page_text[i].setTextSize(45);
////    				            page_text[i].setTypeface(null, Typeface.BOLD);
////    				            page_text[i].setTextColor(android.graphics.Color.GRAY);
////    				            count_layout.addView(page_text[i]);
////    				        }
////    						
////    					}
////    				});
////    					
////    				for (int i = 0; i < imageList.size(); i++) 
////    				{
////    					 switch (i) 
////    					 {
////    					 case 0:
////    						 mSessionManager.setProFilePicture(imageList.get(i).getImageUrl());
////    						 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    						 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    						 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    						 break;
////    					 case 1:
////    						 mSessionManager.setProFilePicture1(imageList.get(i).getImageUrl());
////    						 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    						 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    						 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    						 break;
////    					 case 2:
////    						 mSessionManager.setProFilePicture2(imageList.get(i).getImageUrl());
////    						  imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    						 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    						 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    						 break;
////    					 case 3:
////    						 mSessionManager.setProFilePicture3(imageList.get(i).getImageUrl());
////    						 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    						 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    						 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    						 break;
////    					 case 4:
////    						 mSessionManager.setProFilePicture4(imageList.get(i).getImageUrl());
////    						 imageFile=	mUltilities.createFile(getResources().getString(R.string.imagedire), getResources().getString(R.string.imagefilename)+i+".jpg");
////    						 logDebug("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory doInBackground imageFile is profile "+imageFile.isFile());
////    						 com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(imageList.get(i).getImageUrl().replaceAll(" ","%20"),imageFile);
////    						 break;
////    					}
////    				}
////    					
////    				sdCardPath=mUltilities.getSdCardPath();
////    				PickDirectory=new File(sdCardPath, getResources().getString(R.string.imagedire));
////    				imageFileArray=PickDirectory.listFiles();	
////    				 for (int i = 0; i < imageFileArray.length; i++) 
////    				   {
////    					   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground image url"+imageList.get(i).getImageUrl());
////    					   Bitmap mBitmap=mUltilities.showImage(imageFileArray[i].getAbsolutePath());
////    					   logDebug("BackGroundTaskForDownloadProfileImage  doInBackground mBitmap"+mBitmap);
////    					   
////    					   imageList.get(i).setmBitmap(mBitmap);
////    					  getActivity().runOnUiThread(new Runnable() 
////    					  {
////    						
////    						@Override
////    						public void run()
////    	                    {
////    						
////    							
////    						}
////    					});
////    				   }	
////    				
////    				}
////    			 
////    			  // mAdapterForGellary.notifyDataSetChanged();
////    			} catch (Exception e) 
////    			{
////    				 logDebug("BackGroundTaskForDownloadProfileImage  doInBackground Exception"+e);
////    			}
////    			return null;
////    		}
////			
////    		@Override
////    		protected void onPostExecute(Void result) 
////    		{
////    			super.onPostExecute(result);
////    			try 
////    			{
////    				
////    			} catch (Exception e) 
////    			{
////    			}    			
////    		}
////			
////    		@Override
////    		protected void onPreExecute() 
////    		{
////    			super.onPreExecute();
////    			 
////    		}
////    		
////    	}
//
//		@Override
//		public void afterTextChanged(Editable s) 
//		{
//			
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {	
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) 
//		{
//			try {
//				textCount=userbouteditext.getText().toString().length();
//	        	textCount=500-textCount;
//	        	textnumbertextview.setText(""+textCount);
//			} catch (Exception e) 
//			{
//			}			
//		}
//				
//		private void UpdateUserPersonaDescription()
//		{
//	  		finish();
//	  		String userUpdatePrefrence=userbouteditext.getText().toString();
//	       
//	       if (userUpdatePrefrence!=null&&userUpdatePrefrence.length()>0)
//	       {
//	    	 
//	    	   if (aboutUser!=null&&aboutUser.length()>0)
//	    	   {
//	    		   if (userUpdatePrefrence.equals(aboutUser)) 
//	  			 {
//	  				// no need to call update same 
//	    			 
//	  			 }
//	    		   else 
//	    		     {
//	    			            /// user change persnal description 
//	    			   SessionManager mSessionManager=new SessionManager(this);
//				       String deviceId=Ultilities.getDeviceId(this);
//				       String userSession=mSessionManager.getUserToken();
//				       mSessionManager.setUserAbout(userUpdatePrefrence);
//				      // userUpdatePrefrence="";
//				       String [] params ={userSession,deviceId,userUpdatePrefrence};
//				       new BackgroundTaskForUpdatePersnalDescription().execute(params);
//				    	
//				     }
//			    }
//	    	   else 
//	    	   {
//				   /// user change persnal description 
//	    		   SessionManager mSessionManager=new SessionManager(this);
//			       String deviceId=Ultilities.getDeviceId(this);
//			       String userSession=mSessionManager.getUserToken();
//			      // userUpdatePrefrence="";
//			       mSessionManager.setUserAbout(userUpdatePrefrence);
//			       String [] params ={userSession,deviceId,userUpdatePrefrence};
//			       new BackgroundTaskForUpdatePersnalDescription().execute(params);
//			    	
//			    }			
//		   }
//	       else 
//	       {
//	    	  
//			    if (aboutUser!=null&&aboutUser.length()>0) 
//			    {
//			    	
//				}
//			    else 
//			    {
//					// user  want to remove their persional description
//			       SessionManager mSessionManager=new SessionManager(this);
//			       String deviceId=Ultilities.getDeviceId(this);
//			       String userSession=mSessionManager.getUserToken();
//			       userUpdatePrefrence="";
//			       
//			       mSessionManager.setUserAbout(userUpdatePrefrence);
//			       String [] params ={userSession,deviceId,userUpdatePrefrence};
//			       new BackgroundTaskForUpdatePersnalDescription().execute(params);
//			    	
//				}
//		   }
//	}
//		
//  private class BackgroundTaskForUpdatePersnalDescription extends AsyncTask<String, Void, Void>
//  {
//	  Ultilities mUltilities=new Ultilities();
//	  private List<NameValuePair>uploadNameValuePairList;
//		private String uploadResponse;
//		private boolean updateuccessFully;
//	@Override
//	protected Void doInBackground(String... params) 
//	{
//		try {
//			String [] uploadParameter={params[0],params[1],params[2]};
//			uploadNameValuePairList=mUltilities.getParameterForUpdateUserPersionlDescription(uploadParameter);		
//			
//			uploadResponse=   mUltilities.makeHttpRequest(CommonConstant.editUseProfile_url,CommonConstant.methodeName,uploadNameValuePairList);
//			logDebug("BackgroundTaskForUpdatePersnalDescription  uploadResponse   "+uploadResponse);
//			
//			updateuccessFully=true;
//		} catch (Exception e)
//		{
//			logError("BackgroundTaskForUpdatePersnalDescription doInBackground "+e);
//			updateuccessFully=false;
//		}
//		
//		return null;
//	}
//	@Override
//	protected void onPostExecute(Void result) 
//	{
//		super.onPostExecute(result);
//		
//		
//	}
//	@Override
//	protected void onPreExecute() 
//	{
//		super.onPreExecute();
//	}
//	  
//  }
//  
//  @Override
//  public void onBackPressed() 
//  {
//	//super.onBackPressed();
//	  UpdateUserPersonaDescription();
//	
//  }
//  
//  private void deleteImage(String [] imageOrderArray)
//  {
//	 new  BackGroundTaskForDeleteImage().execute(imageOrderArray);
//  }
//  private class BackGroundTaskForDeleteImage extends AsyncTask<String, Void, Void>
//  {
//
//	  
//	private List<NameValuePair>deleteImageParams;
//	  private String deleteImageResponse;
//	  private Ultilities ultilities=new Ultilities();
//	  private String imageIndex;
//	  private ImageDeleteData imageDeleteData;
//	  private boolean success=true;
//	  private ArrayList<ImageDetail>imagelistFormdatabase;
//	  private ImageDetail imageDetail;
//	//  private File appDirectory;
//	 // private File PickDirectory;
//	 // private File selectedfile;
//		private DatabaseHandler databaseHandler=new DatabaseHandler(EditProfileActivity1.this);
//		private SessionManager manager=new SessionManager(EditProfileActivity1.this);
//		  String deviceid=Ultilities.getDeviceId(EditProfileActivity1.this);
//		  String sessionToken= manager.getUserToken();
//	@Override
//	protected Void doInBackground(String... params)
//	{
//		try 
//		{
//			
//			 for (int i = 0; i < params.length; i++)
//			 {
//				imageIndex=params[i];
//			 switch (Integer.parseInt(imageIndex)) 
//			 {
//			 case 1:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressprofileImage.setVisibility(View.GONE);
//						 userprofilpicture.setImageDrawable(null);
//						 userprofilpicture.setImageResource(R.drawable.profile_image_boader);
//
//
//					 }
//				 });
//				 break;
//
//			 case 2:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressbarforfrirstImage.setVisibility(View.VISIBLE);
//
//					 }
//				 });
//
//				 break;
//			 case 3:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressbarforsecondImage.setVisibility(View.VISIBLE);
//												
//					 }
//				 });
//
//				 break;
//			 case 4:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressbarforthirdImage.setVisibility(View.VISIBLE);
//
//					 }
//				 });
//				 break;
//			 case 5:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressbarforfourthImage.setVisibility(View.VISIBLE);
//					
//										
//					 }
//				 });
//				 break;
//
//			 case 6:
//				 runOnUiThread(new Runnable() 
//				 {
//					 public void run() 
//					 {
//						 progressbarforfifthImage.setVisibility(View.VISIBLE);
//
//											
//					 }
//				 });
//				 break;
//			 }
//			 }
//			
//			String imageName="";
//			imagelistFormdatabase=databaseHandler.getImageDetailByImageOrder(params);
//			
//			if (imagelistFormdatabase!=null&&imagelistFormdatabase.size()>0)
//			{ 
//				for (int i = 0; i < imagelistFormdatabase.size(); i++) 
//				{
//					imageDetail=imagelistFormdatabase.get(i);
//					
//					imageName=imageName+imageDetail.getImageUrl()+",";
//					
//				}				
//			}
//			else
//			{
//				
//			}
//			
//			 String [] deleteParams={sessionToken,deviceid,imageName,""+"2"};
//			deleteImageParams=ultilities.getDeleteParameter(deleteParams);
//			Log.i(TAG, "deleteImageParams : "+deleteImageParams);
//			deleteImageResponse=ultilities.makeHttpRequest(CommonConstant.imagedelete_url, CommonConstant.methodeName, deleteImageParams);
//			Log.e(TAG, "deleteImageResponse : "+deleteImageResponse);
//			
//			Gson gson = new Gson();
//			 imageDeleteData=   gson.fromJson(deleteImageResponse, ImageDeleteData.class);
//			 if (imageDeleteData.getErrNum()==23&&imageDeleteData.getErrFlag()==0) 
//			 {  
//				 databaseHandler.deleteImagedetail(params);
//				 for (int i = 0; i < imagelistFormdatabase.size(); i++)
//				 {
//					String filePath= imagelistFormdatabase.get(i).getSdcardpath();
//					
//					File mFile=new File(filePath);
//					mFile.delete();
//		    	 }
//				 //appDirectory = ultilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
//				
//				 
//				 
//				// PickDirectory=new File(appDirectory, getResources().getString(R.string.imagedire));
//				// selectedfile=new File(PickDirectory, getResources().getString(R.string.imagefilename)+imageIndex+".jpg");
//				 
//				 for (int i = 0; i < params.length; i++)
//				 {
//					imageIndex=params[i];
//				 switch (Integer.parseInt(imageIndex)) 
//				 {
//				 case 1:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressprofileImage.setVisibility(View.GONE);
//							 userprofilpicture.setImageDrawable(null);
//							 userprofilpicture.setImageResource(R.drawable.profile_image_boader);
//
//
//						 }
//					 });
//					 break;
//
//				 case 2:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressbarforfrirstImage.setVisibility(View.GONE);
//							 firtstImage.setImageDrawable(null);
//							 firtstImage.setImageResource(R.drawable.add_image);
//							 deleteandeditbuttonlayout.setVisibility(View.GONE);
//							 isFirstImageSeted=false;
//							 isFirstImageselected=false;
//						 }
//					 });
//
//					 break;
//				 case 3:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressbarforsecondImage.setVisibility(View.GONE);
//							 secondImage.setImageDrawable(null);
//							 secondImage.setImageResource(R.drawable.add_image);
//							 deleteandeditbuttonlayout.setVisibility(View.GONE);
//							 isSecondImageSeted=false;
//							 issecondImageselected=false;							
//						 }
//					 });
//
//					 break;
//				 case 4:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressbarforthirdImage.setVisibility(View.GONE);
//							 thirdimageview.setImageDrawable(null);
//							 thirdimageview.setImageResource(R.drawable.add_image);
//							 deleteandeditbuttonlayout.setVisibility(View.GONE);
//							 isThirdImageSeted=false;
//							 isThirdImageImageselected=false;
//						 }
//					 });
//					 break;
//				 case 5:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressbarforfourthImage.setVisibility(View.GONE);
//							 fourtthimageview.setImageDrawable(null);
//							 fourtthimageview.setImageResource(R.drawable.add_image);
//							 deleteandeditbuttonlayout.setVisibility(View.GONE);
//							 isFourthImageSeted=false;
//							 isFourthImageselected=false;
//											
//						 }
//					 });
//					 break;
//
//				 case 6:
//					 runOnUiThread(new Runnable() 
//					 {
//						 public void run() 
//						 {
//							 progressbarforfifthImage.setVisibility(View.GONE);
//							 fifthImageview.setImageDrawable(null);
//							 fifthImageview.setImageResource(R.drawable.add_image);
//							 deleteandeditbuttonlayout.setVisibility(View.GONE);
//							 isFifthImageSeted=false;
//							 isFifthImageselected=false;
//												
//						 }
//					 });
//					 break;
//				 }
//				 }
//
//			 }
//			 else 
//			  {
//				 success=false;
//			  }
//			 
//		} 
//		
//		catch (Exception e) 
//		{
//			logError("BackGroundTaskForDeleteImage  doInBackground  Exception "+e);
//			success=false;
//		}
//		
//		
//		
//		return null;
//	}
//	 @Override
//	protected void onPostExecute(Void result) 
//	 {
//		super.onPostExecute(result);
//		if (success) 
//		{
//			deleteandeditbuttonlayout.setVisibility(View.GONE);
//			plusbuttonlayout.setVisibility(View.VISIBLE);
//			plusbutton.setVisibility(View.VISIBLE);
//			plusbutton.setBackgroundResource(R.drawable.plus_btn);
//		}
//		else 
//		{
//			
//		}
//	 }
//	 @Override
//	protected void onPreExecute() 
//	 {
//		super.onPreExecute();
//	 }
//  }
//
//}
