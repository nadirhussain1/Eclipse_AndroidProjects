package com.humby.bloodalcohol;

import java.math.BigDecimal;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.adwhirl.util.AdWhirlUtil;
import com.example.android.trivialdrivesample.GoogleInAppModule;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

public class BACActivity extends SherlockActivity implements OnClickListener,AdListener
{
	//----------------- Home Screen Id's Declaration Start-----------------//
	private Display display = null;
	private ViewGroup.LayoutParams paramsAdhirl,params,paramsProfile,paramsButton;
	private TextView txtProfileName,txtWeight,txtHeight,txtGender,fixTxtHeight,fixTxtWeight,fixTxtGender;
	private TextView txtMinute,txtLevel,fixTxtLevel,fixTxtMinutes,fixTxtDrinks,fixTxtHaveHad,fixTxtPast;
	public static TextView txtDiscription,txtPercentile;

	private Button btnCallApps,btnShare;
	private ImageView imgLiquor,imgCocktail,profileImage;
	private LinearLayout llProfileView,llLevelLeft,llLevelRight,llMinuteLeft,llMinuteRight,llDescription,llAdwhirlView;
	private boolean flagCheck = false;
	private ActionBar mActionBar;
	public static BACPrefrence bacPrefrence = null;
	String TEST_PRODUCT_ID="in_app_product_99";
	//String TEST_PRODUCT_ID="android.test.purchased";
	public static final String SHARED_PREFS_KEY = "BAC_prefs";

	private static boolean falgOtherApp = true;
	private float listTime[]=new float[]{5,10,15,20,30,45,60,75,(float) 1.5,2,(float) 2.5,3,(float) 3.5,4,5,6};
	private int selectedTimeIndex=0;
	public static MenuItem menuItem=null;
	//private ArrayList<Integer> listMinutes = new ArrayList<Integer>();
	//private ArrayList<Double> listHours = new ArrayList<Double>();
	private SharedPreferences appSharedPrefs;
	private SharedPreferences.Editor prefsEditor;
	public static AdView bannerAdView=null;
	private InterstitialAd interstitial;
	private String ADMOB_INTERSTITIAL_UNIT_ID="a75a461bb4184d0c";
	IabHelper mHelper;

	private static final String USER_PREFS = "SPORT_PREFS";
	//------------------------------ End ------------------------------------//


	//--------------- Profile Popup Id's Declaration Start--------------//
	private Dialog xmlDialog = null;
	private TextView btnPopupCancel,btnPopupSet;
	private TextView txtPopupName,txtPopupGender,txtPopupHeight,txtPopupWeight,txtPopupFt,txtPopupLBS,txtPopupInches;
	private EditText edittxtPopupName,edittxtPopupHeight,edittxtPopupWeight,edittxtPopupFt;
	private ViewGroup.LayoutParams paramsPopUp = null;
	private ImageView imageFemale,imageMale;
	//-------------------------- End ---------------------------------//

	//-------------- Drink Popup Id's Declaration Strat ----------------//
	private LinearLayout llPopDrinkLayout,llLiquor,llWhine,llBeer,llCocktail;
	private TextView txtHeaderLiquor,txtHeaderWhine,txtHeaderBeer,txtHeaderCockTail;
	private TextView txtOzLiquor,txtOzBeer,txtOzCock,txtOzWhine;
	private TextView txtDisLiqour,txtDisWhine,txtDisBeer,txtDisCock,txtPopupOk;
	//------------------------ End -------------------------------------//

	//---------------- GetJar Id's Declaration-----------------------//
	// private com.adwhirl.AdWhirlLayout adwhirl_layout = null;
	//private int index = 0
	private String applicationKey = "5c854253-3adb-41f4-b343-7c34aa048299";
	private String encriptionKey = "0000MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLzCiQo/KH5JHL6wEx6tLiaR74WA6jH+aC5EbG8JN8wqHs1Qp9X8xbfXAXvAvjyG1iuy4z3FPlOqTAH6bGmaAoQIpKnIaTNz5IPM3nJ1zV7bC3tbvIlBhICPvZpEIBs/qeRVOVw0Y3mYInh0GI77BgN4D+ZlkR8LfKyK53/QyVzwIDAQAB";
	//---------------------- End -----------------------------------//    
	private static boolean flag = false;
	public static Context mContext=null;
	public void adWhirlGeneric() {
		Log.e(AdWhirlUtil.ADWHIRL, "In adWhirlGeneric()");
	}
	String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgKloQaLdVmE3OYaDCWx38IPRPhml4H+728GD1aSGGCMgyXQb8YDCJsEiU63YlAObv6rS3AKDmzdA5+E5wMGhO8WnZomJNQ0ezxX5BSdXl0OQYi0NKvmGayrAyzNF0tCWep0R4THvUdOPTrvShRxsjSrFsLm/iaPzFqc854z3mdrx4Sh7ZBx6QN02mdx97K6ZpTbF2+UU8lM7Z1m7v80EyiKLodXmIJzoT5hhoDcUupPLA801ay+9CP11lqdGQk/KjHgYKcbYpSGYg6HAcBp18nh6fBO/ibvjv5AqfqV0U2drJZJMrXBU/ZATLdJF7RszxBx9fMI5V7aS19gyclFl6wIDAQAB";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.blood_alcohol_cal);
		GoogleInAppModule.getGoogleInAppModule().InitializeGoogleInApp(this, "Encoded KEY WILL BE GIVEN HERE");
		interstitial = new InterstitialAd(this, ADMOB_INTERSTITIAL_UNIT_ID);

		display = this.getWindowManager().getDefaultDisplay();
		bacPrefrence = new BACPrefrence(this);
		if(!flag)
		{
			bacPrefrence.setMaleKey(true);
		}
		//		bacPrefrence.setMaleKey(true);
		init();

		boolean isAlreadyInAppDone=bacPrefrence.getInAppPurchaseStatus();
		bannerAdView=(AdView)findViewById(R.id.adViewbanner);
		if(!isAlreadyInAppDone){	

			AdRequest request=new AdRequest();
			request.addTestDevice("9C51AF3FFBBE2D02322770C129028C8B");
			bannerAdView.loadAd(request);
		}else{
			bannerAdView.setVisibility(View.GONE);
		}

		if(bacPrefrence.getNameKey().equalsIgnoreCase("") || bacPrefrence.getGenderKey().equalsIgnoreCase("") || bacPrefrence.getHeightKey().equalsIgnoreCase("") || bacPrefrence.getWeightKey().equalsIgnoreCase(""))
		{
			profilePopupDialog();
		}else{

			profileShow();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!GoogleInAppModule.getGoogleInAppModule().IsHandledPurchaseTransactionResult(requestCode,resultCode,data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}

	}
	private void initializeFullScreenAd(){
        Toast.makeText(this,"Loading. It may take few seconds.", Toast.LENGTH_SHORT).show();
		AdRequest adRequest = new AdRequest();
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(this);
	}
	@Override
	public void onReceiveAd(Ad ad) {
		interstitial.show();
	}
	private void init()
	{
		//------------------- Id's Initialization Start---------------------------//
		//    	adwhirl_layout = (AdWhirlLayout)findViewById(R.id.adwhirl_layout);
		//	     adwhirl_layout.setAdWhirlInterface(this);

		txtProfileName = (TextView)findViewById(R.id.txtProfileName);
		txtWeight = (TextView)findViewById(R.id.txtWeight);
		txtHeight = (TextView)findViewById(R.id.txtHeight);
		txtGender = (TextView)findViewById(R.id.txtGender);
		txtLevel = (TextView)findViewById(R.id.txtLevel);
		txtMinute = (TextView)findViewById(R.id.txtMinutes);
		txtPercentile = (TextView)findViewById(R.id.txtPercentile);
		txtDiscription = (TextView)findViewById(R.id.txtDiscription);

		fixTxtHeight = (TextView)findViewById(R.id.fixTxtHeight);
		fixTxtWeight = (TextView)findViewById(R.id.fixTxtWeight);
		fixTxtGender = (TextView)findViewById(R.id.fixTxtGender);
		fixTxtHaveHad = (TextView)findViewById(R.id.fixTxtHaveHad);
		fixTxtPast = (TextView)findViewById(R.id.fixTxtPast);
		fixTxtDrinks = (TextView)findViewById(R.id.fixTxtDrinks);

		fixTxtMinutes = (TextView)findViewById(R.id.fixTxtMinutes);
		fixTxtLevel = (TextView)findViewById(R.id.fixTxtLevel);

		profileImage = (ImageView)findViewById(R.id.profileImage);
		llLevelLeft = (LinearLayout)findViewById(R.id.llLevelLeft);
		llLevelRight = (LinearLayout)findViewById(R.id.llLevelRight);
		llMinuteLeft = (LinearLayout)findViewById(R.id.llMinuteLeft);
		llMinuteRight = (LinearLayout)findViewById(R.id.llMinuteRight);
		llAdwhirlView = (LinearLayout)findViewById(R.id.llAdwhirlView);

		llDescription = (LinearLayout)findViewById(R.id.llDescription); 

		btnCallApps = (Button)findViewById(R.id.btnCallApps);
		btnShare = (Button)findViewById(R.id.btnShare);

		llProfileView = (LinearLayout)findViewById(R.id.llProfileView);
		//-------------------- Id's Initialization End ---------------------------//

		//Start------------------- ActionSherLock Bar ------------------------------------//
		Drawable aBackground = getResources().getDrawable(R.drawable.ab_background);
		mActionBar = getSupportActionBar();
		mActionBar.setLogo(R.drawable.ab_logo);
		mActionBar.setBackgroundDrawable(aBackground);
		mActionBar.setTitle("");
		//End--------------------- ActionSherLock Bar ------------------------------------//

		paramsProfile  = llProfileView.getLayoutParams();
		paramsProfile.height = (int) (display.getHeight()/5.5);
		llProfileView.setLayoutParams(paramsProfile);

		paramsButton = btnCallApps.getLayoutParams();
		paramsButton.height = (int) (display.getHeight()/12);
		btnCallApps.setLayoutParams(paramsButton);

		paramsButton = btnShare.getLayoutParams();
		paramsButton.height = (int) (display.getHeight()/12);
		btnShare.setLayoutParams(paramsButton);
		//End------------- Setting Width or Height according to Device Size -------------------//




		llLevelLeft.setOnClickListener(this);
		llLevelRight.setOnClickListener(this);
		llMinuteLeft.setOnClickListener(this);
		llMinuteRight.setOnClickListener(this);
		btnCallApps.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		profileImage.setOnClickListener(this);



		if(display.getWidth() >= 720 && display.getWidth() < 800){
			setHomeTextSize(30, 14, 48, 22, 18, 16, 43);
		} else if(display.getWidth() >= 800){
			setHomeTextSize(45, 24, 52, 34, 28, 26, 65);
		}

	}

	//------------------ Method Call My Profile Pop-Up ------------------------//

	private void profilePopupDialog() {

		xmlDialog = new Dialog(BACActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		xmlDialog.setContentView(R.layout.profile_popup);
		txtPopupName = (TextView)xmlDialog.findViewById(R.id.txtPopupName);
		txtPopupGender = (TextView)xmlDialog.findViewById(R.id.txtPopupGender);
		txtPopupHeight = (TextView)xmlDialog.findViewById(R.id.txtPopupHeight);
		txtPopupWeight = (TextView)xmlDialog.findViewById(R.id.txtPopupWeight);
		txtPopupFt = (TextView)xmlDialog.findViewById(R.id.txtPopupFt);
		txtPopupLBS = (TextView)xmlDialog.findViewById(R.id.txtPopupLBS);
		txtPopupInches = (TextView)xmlDialog.findViewById(R.id.txtPopupInches);

		edittxtPopupName = (EditText)xmlDialog.findViewById(R.id.edittxtName);
		edittxtPopupFt = (EditText)xmlDialog.findViewById(R.id.edittxtFt);
		edittxtPopupHeight = (EditText)xmlDialog.findViewById(R.id.edittxtHeight);
		edittxtPopupWeight = (EditText)xmlDialog.findViewById(R.id.edittxtWeight);

		imageMale = (ImageView)xmlDialog.findViewById(R.id.imageMale);
		imageFemale = (ImageView)xmlDialog.findViewById(R.id.imageFemale);


		btnPopupSet = (TextView)xmlDialog.findViewById(R.id.btnPopupSet);
		btnPopupCancel = (TextView)xmlDialog.findViewById(R.id.btnPopupCancel);

		if(display.getWidth() >= 720 && display.getWidth() < 800){
			setPopUpTextSize(18);
		}else if(display.getWidth() >= 800){
			setPopUpTextSize(28);
		}

		imageMale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				flag = false;
				bacPrefrence.setMaleKey(true);
				bacPrefrence.setFemaleKey(false);
				imageMale.setBackgroundResource(R.drawable.male_selected);
				imageFemale.setBackgroundResource(R.drawable.female);
			}
		});

		imageFemale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				flag = true;
				bacPrefrence.setMaleKey(false);
				bacPrefrence.setFemaleKey(true);
				imageMale.setBackgroundResource(R.drawable.male);
				imageFemale.setBackgroundResource(R.drawable.female_selected);
			}
		});

		btnPopupSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkOnOk("All Fields Must Be Filled In");
				flagCheck = true;
			}
		});

		btnPopupCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				checkOnCancel("You must fill out your profile information to get your blood alcohol level.");
			}
		});

		edittxtPopupName.setText(bacPrefrence.getNameKey());
		edittxtPopupWeight.setText(bacPrefrence.getWeightKey());
		if(!bacPrefrence.getHeightKey().equals(""))
		{
			int index = bacPrefrence.getHeightKey().indexOf(" ");
			edittxtPopupHeight.setText(bacPrefrence.getHeightKey().substring(0,(index-1)));
			edittxtPopupFt.setText(bacPrefrence.getHeightKey().substring((index+1),(bacPrefrence.getHeightKey().length()-1)));
		}
		if(bacPrefrence.getMaleKey()) {
			imageMale.setBackgroundResource(R.drawable.male_selected);
			imageFemale.setBackgroundResource(R.drawable.female);
		} else if(bacPrefrence.getFemaleKey()) {
			imageMale.setBackgroundResource(R.drawable.male);
			imageFemale.setBackgroundResource(R.drawable.female_selected);
		}

		xmlDialog.show();
		xmlDialog.setCancelable(false);
	}

	//--------------- Method Set Profile Data in Home Screen ------------//
	private void profileShow() {
		txtProfileName.setText(bacPrefrence.getNameKey());
		txtHeight.setText(bacPrefrence.getHeightKey());
		txtWeight.setText(bacPrefrence.getWeightKey()+" LBS");
		if(bacPrefrence.getMaleKey()) {
			txtGender.setText("MALE");
			profileImage.setBackgroundResource(R.drawable.male);
		}
		else {
			txtGender.setText("FEMALE");
			profileImage.setBackgroundResource(R.drawable.female);
		}
		txtLevel.setText("0");
		txtMinute.setText("5");
		txtPercentile.setText("0.000%");

	}

	//------------------ Check Validation on Ok Button -----------------------//
	private void checkOnOk(String msg) {
		if(!edittxtPopupName.getText().toString().equalsIgnoreCase("") && !edittxtPopupFt.getText().toString().equalsIgnoreCase("") && !edittxtPopupHeight.getText().toString().equalsIgnoreCase("") && !edittxtPopupWeight.getText().toString().equalsIgnoreCase("")) {

			xmlDialog.dismiss();
			bacPrefrence.setNameKey(edittxtPopupName.getText().toString().trim());						
			bacPrefrence.setWeightKey(edittxtPopupWeight.getText().toString().trim());
			bacPrefrence.setHeightKey(edittxtPopupHeight.getText().toString().trim()+"' "+edittxtPopupFt.getText().toString().trim()+"\"");

			if(bacPrefrence.getMaleKey())
				bacPrefrence.setGenderKey("MALE");
			else if(bacPrefrence.getFemaleKey())
				bacPrefrence.setGenderKey("FEMALE");
			profileShow();								

		} else {
			Toast.makeText(BACActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	//------------------ Check Validation on Cancel Button -----------------------//
	private void checkOnCancel(String msg) {
		if(!edittxtPopupName.getText().toString().trim().equals("")) {
			if(!edittxtPopupFt.getText().toString().trim().equals("")) {
				if(!edittxtPopupHeight.getText().toString().trim().equals("")){
					if(!edittxtPopupWeight.getText().toString().trim().equals("")){

					} else {
						Toast.makeText(BACActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(BACActivity.this, msg, Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(BACActivity.this, msg, Toast.LENGTH_SHORT).show();
			} 
		} else {
			Toast.makeText(BACActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}


	//Start------------ Set Text Size for different Screen -------------//
	private void setHomeTextSize(int a,int b,int c,int d,int e,int f,int g)
	{
		txtProfileName.setTextSize(a);
		fixTxtHeight.setTextSize(b);
		fixTxtWeight.setTextSize(b);
		fixTxtGender.setTextSize(b);
		txtHeight.setTextSize(b);
		txtWeight.setTextSize(b);
		txtGender.setTextSize(b);
		txtLevel.setTextSize(c);
		txtMinute.setTextSize(c);
		fixTxtHaveHad.setTextSize(d);
		fixTxtPast.setTextSize(d);
		fixTxtDrinks.setTextSize(d);
		fixTxtMinutes.setTextSize(d);
		fixTxtLevel.setTextSize(e);
		txtDiscription.setTextSize(f);
		txtPercentile.setTextSize(g);
	}
	private void setPopUpTextSize(int a)
	{
		txtPopupFt.setTextSize(a);
		txtPopupGender.setTextSize(a);
		txtPopupHeight.setTextSize(a);
		txtPopupInches.setTextSize(a);
		txtPopupLBS.setTextSize(a);
		txtPopupName.setTextSize(a);
		txtPopupWeight.setTextSize(a);

		btnPopupCancel.setTextSize(a);
		btnPopupSet.setTextSize(a);
	}
	//End------------ Set Text Size for different Screen -------------//

	//---------------------- Method get Button's event-----------------------//
	public void onClick(View v)
	{
		if(v == llLevelLeft)
		{
			String levelS = txtLevel.getText().toString().trim();
			int levelInt = Integer.parseInt(levelS);
			if(levelInt > 0)
			{
				levelInt -= 1;
				if(String.valueOf(levelInt).length() == 1) {
					txtLevel.setText(String.valueOf(levelInt));			
					//round(new BigDecimal(String.valueOf(alcoholCalculate(Integer.parseInt(txtLevel.getText().toString().trim()),Integer.parseInt(bacPrefrence.getWeightKey())))), 3, false);
					txtPercentile.setText(round(new BigDecimal(String.valueOf(alcoholCalculate(Integer.parseInt(txtLevel.getText().toString().trim()),Integer.parseInt(bacPrefrence.getWeightKey())))), 3, false)+"%");
				} else {
					txtPercentile.setText(round(new BigDecimal(String.valueOf(alcoholCalculate(Integer.parseInt(txtLevel.getText().toString().trim()),Integer.parseInt(bacPrefrence.getWeightKey())))), 3, false)+"%");
					txtLevel.setText(String.valueOf(levelInt));
				}
			}
		} else if(v == llLevelRight)
		{
			String levelS = txtLevel.getText().toString().trim();
			int levelInt = Integer.parseInt(levelS);
			if(levelInt < 99)
			{
				levelInt += 1;
				if(String.valueOf(levelInt).length() == 1) {
					txtLevel.setText(String.valueOf(levelInt));
					txtPercentile.setText(round(new BigDecimal(String.valueOf(alcoholCalculate(Integer.parseInt(txtLevel.getText().toString().trim()),Integer.parseInt(bacPrefrence.getWeightKey())))), 3, false)+"%");
				} else {
					txtLevel.setText(String.valueOf(levelInt));
					txtPercentile.setText(round(new BigDecimal(String.valueOf(alcoholCalculate(Integer.parseInt(txtLevel.getText().toString().trim()),Integer.parseInt(bacPrefrence.getWeightKey())))), 3, false)+"%");
				}
			}
		} else if(v == llMinuteLeft && selectedTimeIndex>0)
		{
			selectedTimeIndex--;

			if(selectedTimeIndex<=7){
				fixTxtMinutes.setText("MINUTES");
				txtMinute.setText(""+(int)listTime[selectedTimeIndex]);
			}else{
				txtMinute.setText(""+listTime[selectedTimeIndex]);
				fixTxtMinutes.setText("HOURS");
			}
			int drinks=Integer.parseInt(txtLevel.getText().toString());
			int weight=Integer.parseInt(bacPrefrence.getWeightKey());
			double calculatePercentage=alcoholCalculate(drinks,weight);
			BigDecimal percentage=new BigDecimal(String.valueOf(calculatePercentage));
			txtPercentile.setText(round(percentage, 3, false)+"%");

		} else if(v == llMinuteRight && selectedTimeIndex<(listTime.length-1))
		{
			selectedTimeIndex++;

			if(selectedTimeIndex<=7){
				fixTxtMinutes.setText("MINUTES");
				txtMinute.setText(""+(int)listTime[selectedTimeIndex]);
			}else{
				fixTxtMinutes.setText("HOURS");
				txtMinute.setText(""+listTime[selectedTimeIndex]);
			}
			int drinks=Integer.parseInt(txtLevel.getText().toString());
			int weight=Integer.parseInt(bacPrefrence.getWeightKey());
			double calculatePercentage=alcoholCalculate(drinks,weight);
			BigDecimal percentage=new BigDecimal(String.valueOf(calculatePercentage));
			txtPercentile.setText(round(percentage, 3, false)+"%");

		} else if(v == btnCallApps){

			if(!falgOtherApp) {
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, "Taxi Service Near Me");
				startActivity(intent);
			} else {

				initializeFullScreenAd();

			}

		} else if(v == btnShare) {
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = "My BAC is currently "+txtPercentile.getText().toString()+" courtesy of Blood Alcohol Calculator";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Blood Alcohol Percentage");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Sharevia"));
		} 
	}

	//------------  Method Calculate Alcohol Level ---------------//
	private double alcoholCalculate(int drink,int weight)
	{
		double bloodAlcoholPercentage = 0.0;
		double A = (drink * 0.55);
		double timeInHours=listTime[selectedTimeIndex];
		if(selectedTimeIndex<=7){
			timeInHours/=60;
		}

		if(bacPrefrence.getMaleKey()){	
			bloodAlcoholPercentage = ((A* 5.14) / (weight*0.73))-(.015*timeInHours);
		} else {

			bloodAlcoholPercentage = ((A* 5.14) / (weight*0.66))-(.015*timeInHours);	
		}

		if(bloodAlcoholPercentage<0){
			bloodAlcoholPercentage=0;
		}
		try{			
			if(bloodAlcoholPercentage >= 0 && bloodAlcoholPercentage <= .009)
			{	falgOtherApp = true;
			setTextColor("#03A312", "You should feel and appear normal.", R.drawable.other_apps);
			} else if(bloodAlcoholPercentage >= .01 && bloodAlcoholPercentage <= .029){

				falgOtherApp = true;
				setTextColor("#8CAB0F", "You may feel relaxed and talkative, but it is legal for you to drive. Only get in the driver's seat if you feel comfortable.", R.drawable.other_apps);
			} else if(bloodAlcoholPercentage >= .03 && bloodAlcoholPercentage <= .059){

				falgOtherApp = true;
				setTextColor("#CEBD00", "You're slightly buzzed. If you get in the car, obey the speed limits, be aware of your surroundings,  avoid distractions, and drive safely!", R.drawable.other_apps);
			} else if(bloodAlcoholPercentage >= .06 && bloodAlcoholPercentage <= .079){

				falgOtherApp = false;
				setTextColor("#CA610C", "It's barely legal for you to drive, and your reasoning, depth perception, peripheral vision, and recovery time are impaired. Get a ride from a friend, or call a cab.", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .08 && bloodAlcoholPercentage <= .099){

				falgOtherApp = false;
				setTextColor("#AE3201", "It is illegal for you to drive. Your reflexes and motor control are severely affected by alcohol, and you could get charged as a criminal if caught. Do yourself a favor and call a friend or cab.", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .1 && bloodAlcoholPercentage <= .19){

				falgOtherApp = false;
				setTextColor("#C60606", "Not only are your motor abilities severely hindered, but so is your judgement and emotions. By driving you are putting yourself and other people at risk. Do not drive!", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .2 && bloodAlcoholPercentage <= .29){

				falgOtherApp = false;
				setTextColor("#C60606", "You are heavily impaired. The risk of causing an accident is over 25%. Driving in this condition is highly illegal and can result in death for you or other innocent people.", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .3 && bloodAlcoholPercentage <= .39){

				falgOtherApp = false;
				setTextColor("#C60606", "Your alertness, memory, breathing, and equilibrium are severely affected. Driving in this condition is highly illegal and should not be considered an option.", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .4 && bloodAlcoholPercentage <= .49){

				falgOtherApp = false;
				setTextColor("#C60606", "In addition to complete impairment of motor skills and physical/emotional conduct, you are very likely to lose consciousness. DO NOT DRIVE UNDER ANY CIRCUMSTANCE!", R.drawable.call_a_cab);
			} else if(bloodAlcoholPercentage >= .5){

				falgOtherApp = false;
				setTextColor("#C60606", "You are poisoned with alcohol. Seek help immediately. DO NOT DRIVE UNDER ANY CIRCUMSTANCE!", R.drawable.call_a_cab);
			} 
			return bloodAlcoholPercentage;
		} catch (Exception e) {
			Log.e("In alcoholCalculate Method", "Some Time Color Code Generate Error", e);
		}
		return bloodAlcoholPercentage;
	}

	//------------ Method Set Color For Level,Percentile or Description ---------//
	private void setTextColor(String colorCode ,String discription, int drawable)
	{
		try {
			btnCallApps.setBackgroundResource(drawable);
			txtDiscription.setText(discription);
			fixTxtLevel.setTextColor(Color.parseColor(colorCode));
			txtPercentile.setTextColor(Color.parseColor(colorCode));
		} catch (Exception e) {
			Log.e("In setTextColor Method", "Some Time Color Code Generate Error", e);
		}		
	}

	//---------- Method Set number after decimal point -----------//

	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
		return d.setScale(scale, mode);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		boolean isAlreadyInAppDone=bacPrefrence.getInAppPurchaseStatus();
		if(isAlreadyInAppDone){
			inflater.inflate(R.menu.menu_without_ad, menu);
		}else{
			inflater.inflate(R.menu.activity_test_app, menu);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.myProfile:
			profilePopupDialog();
			return true;

		case R.id.whats_drink:
			xmlDialog = new Dialog(BACActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
			xmlDialog.setContentView(R.layout.mydrink_popup);

			llPopDrinkLayout = (LinearLayout)xmlDialog.findViewById(R.id.llPopDrinkLayout);
			llLiquor = (LinearLayout)xmlDialog.findViewById(R.id.llLiquor);
			llBeer = (LinearLayout)xmlDialog.findViewById(R.id.llBear);
			llCocktail = (LinearLayout)xmlDialog.findViewById(R.id.llCocktail);
			llWhine = (LinearLayout)xmlDialog.findViewById(R.id.llWhine);

			txtHeaderLiquor = (TextView)xmlDialog.findViewById(R.id.txtHeaderLiquor);
			txtHeaderWhine = (TextView)xmlDialog.findViewById(R.id.txtHeaderWhine);
			txtHeaderBeer = (TextView)xmlDialog.findViewById(R.id.txtHeaderBeer);
			txtHeaderCockTail = (TextView)xmlDialog.findViewById(R.id.txtHeaderCocktail);

			txtOzLiquor = (TextView)xmlDialog.findViewById(R.id.txtOZLiquor);
			txtOzBeer = (TextView)xmlDialog.findViewById(R.id.txtOzBeer);
			txtOzWhine = (TextView)xmlDialog.findViewById(R.id.txtOzWhine);
			txtOzCock = (TextView)xmlDialog.findViewById(R.id.txtOzCock);

			txtDisBeer  = (TextView)xmlDialog.findViewById(R.id.txtBeerDis);
			txtDisCock = (TextView)xmlDialog.findViewById(R.id.txtCockDis);
			txtDisLiqour = (TextView)xmlDialog.findViewById(R.id.txtLiquorDiscription);
			txtDisWhine = (TextView)xmlDialog.findViewById(R.id.txtWhineDiscription);

			txtPopupOk = (TextView)xmlDialog.findViewById(R.id.txtPopupOk);

			imgLiquor = (ImageView)xmlDialog.findViewById(R.id.imgLiquor);
			imgCocktail = (ImageView)xmlDialog.findViewById(R.id.imgCocktail);

			paramsPopUp  = llPopDrinkLayout.getLayoutParams();
			paramsPopUp.height = (int) ((display.getHeight()/6)*5);  
			llPopDrinkLayout.setLayoutParams(paramsPopUp);

			if(display.getWidth() >= 720 && display.getWidth() < 800){
				paramsPopUp  = llLiquor.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*1.5);  
				llLiquor.setLayoutParams(paramsPopUp);

				paramsPopUp  = llWhine.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*1.5);  
				llWhine.setLayoutParams(paramsPopUp);

				paramsPopUp  = llBeer.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*1.5);  
				llBeer.setLayoutParams(paramsPopUp);

				paramsPopUp  = llCocktail.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*1.5);  
				llCocktail.setLayoutParams(paramsPopUp);

				paramsPopUp = imgLiquor.getLayoutParams();
				paramsPopUp.width = 85;
				paramsPopUp.height = 115;
				imgLiquor.setLayoutParams(paramsPopUp);

				paramsPopUp = imgCocktail.getLayoutParams();
				paramsPopUp.width = 95;
				paramsPopUp.height = 125;
				imgCocktail.setLayoutParams(paramsPopUp);

				txtHeaderLiquor.setTextSize(18);
				txtHeaderWhine.setTextSize(18);
				txtHeaderCockTail.setTextSize(18);
				txtHeaderBeer.setTextSize(18);

				txtOzBeer.setTextSize(18);
				txtOzCock.setTextSize(18);
				txtOzLiquor.setTextSize(18);
				txtOzWhine.setTextSize(18);

				txtDisBeer.setTextSize(12);
				txtDisLiqour.setTextSize(12);
				txtDisCock.setTextSize(12);
				txtDisWhine.setTextSize(12);

			} else if(display.getWidth() >= 800) {
				paramsPopUp  = llLiquor.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*2);  
				llLiquor.setLayoutParams(paramsPopUp);

				paramsPopUp  = llWhine.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*2);  
				llWhine.setLayoutParams(paramsPopUp);

				paramsPopUp  = llBeer.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*2);  
				llBeer.setLayoutParams(paramsPopUp);

				paramsPopUp  = llCocktail.getLayoutParams();
				paramsPopUp.height = (int) ((display.getHeight()/6)*2);  
				llCocktail.setLayoutParams(paramsPopUp);

				paramsPopUp = imgLiquor.getLayoutParams();
				paramsPopUp.width = 95;
				paramsPopUp.height = 120;
				imgLiquor.setLayoutParams(paramsPopUp);

				paramsPopUp = imgCocktail.getLayoutParams();
				paramsPopUp.width = 100;
				paramsPopUp.height = 130;
				imgCocktail.setLayoutParams(paramsPopUp);

				txtHeaderLiquor.setTextSize(28);
				txtHeaderWhine.setTextSize(28);
				txtHeaderCockTail.setTextSize(28);
				txtHeaderBeer.setTextSize(28);

				txtOzBeer.setTextSize(28);
				txtOzCock.setTextSize(28);
				txtOzLiquor.setTextSize(28);
				txtOzWhine.setTextSize(28);

				txtDisBeer.setTextSize(22);
				txtDisLiqour.setTextSize(22);
				txtDisCock.setTextSize(22);
				txtDisWhine.setTextSize(22);
			}
			txtPopupOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					xmlDialog.dismiss();
				}
			});
			xmlDialog.show();
			return true;

		case R.id.removeAds:
			menuItem=item;
			GoogleInAppModule.getGoogleInAppModule().performTransactionAndBuyProduct(TEST_PRODUCT_ID);

			return true;

		case R.id.rateApp:
			return true;

		case R.id.moreFreeApps:
			initializeFullScreenAd();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}		
	}

	//Start---------------------- GetJar Ads Integration --------------------------------//

	//End----------------------------- Get Jar Integration --------------------------------//	    

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}
}