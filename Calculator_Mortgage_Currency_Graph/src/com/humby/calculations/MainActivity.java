package com.humby.calculations;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.calculations.android.inapp.GoogleInAppModule;
import com.calculations.preferences.CalculationsPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.humby.adapters.DrawerListAdapter;
import com.humby.fragments.CalculatorFragment;
import com.humby.fragments.CurrencyFragment;
import com.humby.fragments.GraphFragment;
import com.humby.fragments.LoanFragment;
import com.humby.fragments.MeasureFragment;
import com.humby.fragments.SettingsFragment;
import com.humby.fragments.TipFragment;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private  DrawerListAdapter adapter=null;
	private CharSequence mTitle;
	private boolean isTouched=false;
	private boolean isSwitchClicked=false;

	String[] navMenuTitles=null;

	View copyItemView;
	View hisotryItemView;
	View switchItemView;
	View refreshItemView;
	View currencyItemView;
	public static ImageView measureItemView;
	View tipItemView;
	Runnable mPendingRunnable=null;
	LayoutInflater inflater=null;
	Fragment [] fragmentsArr=new Fragment[9];
	private InterstitialAd interstitial=null;
	private AdIntersitialCounter adCounter=null;
	private boolean isAnimationComplete=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.main, null, false);
		ScalingUtility.getInstance().scaleView(view);
		setContentView(view);
		loadSavedData();
		handleNavigationDrawer(savedInstanceState);
		GoogleInAppModule.getGoogleInAppModule().InitializeGoogleInApp(SharedData.getInstance().mContext,CalConstants.IN_APP_PUBLIC_KEY);
		initAdsIntersitial();
	}
	private void initAdsIntersitial(){
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(CalConstants.AD_INTERSITIAL_ID); 
		interstitial.setAdListener(adListener);
		adCounter=new AdIntersitialCounter(120*1000,1000);
		adCounter.start();
	}
	private void showAds(){
		AdRequest adRequest = new AdRequest.Builder()
		.build();
		interstitial.loadAd(adRequest);
	}
	private void loadSavedData(){
		SharedData.getInstance().prevSelectedMeasCatIndex=CalculationsPreferences.getInstance().getPrevSelectedMeasurementCat();
	}

	@Override
	public void onBackPressed() {
		if(SharedData.getInstance().SelectedDrawerItem==CalConstants.HOME_ID){
			super.onBackPressed();
		}else{
			SharedData.getInstance().SelectedDrawerItem=0;
			supportInvalidateOptionsMenu();
			adapter.notifyDataSetChanged();
			displaySelectedSliderViewPage(fragmentsArr[0],CalConstants.HOME_ID);

		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_actions, menu);

		MenuItem copyItem=menu.findItem(R.id.action_copy);
		View copyActionView=inflater.inflate(R.layout.copy_action_view, null, false);
		copyItem=MenuItemCompat.setActionView(copyItem,copyActionView);
		copyItemView=MenuItemCompat.getActionView(copyItem);
		copyActionView.setOnClickListener(copyActionClickListener);

		MenuItem historyItem=menu.findItem(R.id.action_history);
		View historyActionView=inflater.inflate(R.layout.history_action_view, null, false);
		historyItem=MenuItemCompat.setActionView(historyItem,historyActionView);
		hisotryItemView=MenuItemCompat.getActionView(historyItem);
		historyActionView.setOnClickListener(historyActionClickListener);

		MenuItem switchItem=menu.findItem(R.id.action_switch);
		View switchActionView=inflater.inflate(R.layout.switch_action_view, null, false);
		switchItem=MenuItemCompat.setActionView(switchItem,switchActionView);
		switchItemView=MenuItemCompat.getActionView(switchItem);
		switchActionView.setOnClickListener(switchCalActionListener);

		MenuItem refreshItem=menu.findItem(R.id.action_refresh);
		View refreshActionView=inflater.inflate(R.layout.refresh_action_view, null, false);
		refreshItem=MenuItemCompat.setActionView(refreshItem,refreshActionView);
		refreshItemView=MenuItemCompat.getActionView(refreshItem);
		refreshItemView.setOnClickListener(refreshCalActionListener);

		MenuItem currencyItem=menu.findItem(R.id.action_currency);
		View currencyActionView=inflater.inflate(R.layout.dollar_action_view, null, false);
		currencyItem=MenuItemCompat.setActionView(currencyItem,currencyActionView);
		currencyItemView=MenuItemCompat.getActionView(currencyItem);
		currencyActionView.setOnClickListener(currencyCalActionListener);

		MenuItem measureItem=menu.findItem(R.id.action_measure);
		ImageView measureActionView=(ImageView)inflater.inflate(R.layout.measure_action_view, null, false);
		measureActionView.setImageResource(getSavedMeasurementActionImageId());
		measureItem=MenuItemCompat.setActionView(measureItem,measureActionView);
		measureItemView=(ImageView)MenuItemCompat.getActionView(measureItem);
		measureActionView.setOnClickListener(measureCalActionListener);

		MenuItem tipItem=menu.findItem(R.id.action_tip);
		View tipActionView=inflater.inflate(R.layout.tip_action_view, null, false);
		tipItem=MenuItemCompat.setActionView(tipItem,measureActionView);
		tipItemView=MenuItemCompat.getActionView(tipItem);
		tipActionView.setOnClickListener(tipCalActionListener);


		return super.onCreateOptionsMenu(menu);
	}
	private int getSavedMeasurementActionImageId(){
		int prevSelectedMeasurement=CalculationsPreferences.getInstance().getPrevSelectedMeasurementCat();
		return SharedData.getInstance().abMeasureThumbnails[prevSelectedMeasurement];
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!GoogleInAppModule.getGoogleInAppModule().IsHandledPurchaseTransactionResult(requestCode,resultCode,data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}

	}
	OnClickListener measureCalActionListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){
				MeasureFragment calfragment=(MeasureFragment)(getSupportFragmentManager().findFragmentByTag(navMenuTitles[2]));
				if(calfragment!=null){
					calfragment.displayMeasurementDropDown(measureItemView);
				}
			}else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	};
	OnClickListener refreshCalActionListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){
				CurrencyFragment calfragment=(CurrencyFragment)(getSupportFragmentManager().findFragmentByTag(navMenuTitles[1]));
				if(calfragment!=null){
					calfragment.refreshActionCurrencies();
				}
			}else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	};
	OnClickListener currencyCalActionListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){
				CurrencyFragment calfragment=(CurrencyFragment)(getSupportFragmentManager().findFragmentByTag(navMenuTitles[1]));
				if(calfragment!=null){
					calfragment.displayCurrenciesDropDown();
				}
			}else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	};
	OnClickListener copyActionClickListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){
				CalculatorFragment calfragment=(CalculatorFragment)(getSupportFragmentManager().findFragmentByTag(navMenuTitles[0]));
				if(calfragment!=null){
					calfragment.copyResultToClipBoard();
				}
			}else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	};
	OnClickListener historyActionClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){

			}
			else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	};
	OnClickListener switchCalActionListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){
				SharedData.getInstance().homeCalInput=SharedData.getInstance().homeCalInputEditArea.getText().toString();
				SharedData.getInstance().homeCalResultInput=SharedData.getInstance().homeCalResultTextView.getText().toString();
				if(SharedData.getInstance().simpleAdvanceSwitch==0){
					((ImageView)switchItemView).setImageResource(R.drawable.ab_simple);
					SharedData.getInstance().simpleAdvanceSwitch=1;
					CalculatorFragment fragment=new CalculatorFragment();
					isSwitchClicked=true;

					displaySelectedSliderViewPage(fragment,CalConstants.HOME_ID);
				}else{
					((ImageView)switchItemView).setImageResource(R.drawable.ab_advanced);
					SharedData.getInstance().simpleAdvanceSwitch=0;
					isSwitchClicked=true;
					CalculatorFragment fragment=new CalculatorFragment();
					displaySelectedSliderViewPage(fragment,CalConstants.HOME_ID);
				}
			}else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		}
	};
	OnClickListener tipCalActionListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if(!drawerOpen){

			}
			else{
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		}
	};
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(isAnimationComplete){
			isAnimationComplete=false;
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
				adapter.notifyDataSetChanged();
				if(!drawerOpen){
					Animation fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);	
					fade_out.setFillAfter(true);
					fade_out.setAnimationListener(fadeOutAnimListener);
					
					if(SharedData.getInstance().SelectedDrawerItem==CalConstants.HOME_ID){
						copyItemView.startAnimation(fade_out);
						hisotryItemView.startAnimation(fade_out);
						switchItemView.startAnimation(fade_out);
					}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.CURRENCY_CONVERTER_ID){
						refreshItemView.startAnimation(fade_out);
						currencyItemView.startAnimation(fade_out);
					}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.MEASURE_CONV_ID){
						measureItemView.startAnimation(fade_out);		
					}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.TIPPING_CONV_ID){
						tipItemView.startAnimation(fade_out);		
					}

				}


			}
		}
		return true;
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem copyItem=menu.findItem(R.id.action_copy);
		MenuItem historyItem=menu.findItem(R.id.action_history);
		MenuItem switchItem=menu.findItem(R.id.action_switch);	
		MenuItem refreshItem=menu.findItem(R.id.action_refresh);	
		MenuItem currencyItem=menu.findItem(R.id.action_currency);
		MenuItem measureItem=menu.findItem(R.id.action_measure);
		MenuItem tipItem=menu.findItem(R.id.action_tip);

		if(SharedData.getInstance().SelectedDrawerItem==CalConstants.HOME_ID){
			copyItem.setVisible(true);
			historyItem.setVisible(true);
			switchItem.setVisible(true);

			refreshItem.setVisible(false);
			currencyItem.setVisible(false);
			measureItem.setVisible(false);
			tipItem.setVisible(false);
		}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.CURRENCY_CONVERTER_ID){
			refreshItem.setVisible(true);
			currencyItem.setVisible(true);

			copyItem.setVisible(false);
			historyItem.setVisible(false);
			switchItem.setVisible(false);
			measureItem.setVisible(false);
			tipItem.setVisible(false);

		}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.MEASURE_CONV_ID){
			measureItem.setVisible(true);

			copyItem.setVisible(false);
			historyItem.setVisible(false);
			switchItem.setVisible(false);
			refreshItem.setVisible(false);
			currencyItem.setVisible(false);
			tipItem.setVisible(false);

		}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.TIPPING_CONV_ID){
			tipItem.setVisible(true);	
			measureItem.setVisible(false);
			copyItem.setVisible(false);
			historyItem.setVisible(false);
			switchItem.setVisible(false);
			refreshItem.setVisible(false);
			currencyItem.setVisible(false);
		}else{
			tipItem.setVisible(false);		
			measureItem.setVisible(false);
			copyItem.setVisible(false);
			historyItem.setVisible(false);
			switchItem.setVisible(false);
			refreshItem.setVisible(false);
			currencyItem.setVisible(false);
		}
		return true;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void handleNavigationDrawer(Bundle savedInstanceState){
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_titles);
		adapter = new DrawerListAdapter(navMenuTitles);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setFocusableInTouchMode(false);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		mTitle=getTitle();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_navigation_drawer, R.string.app_name, R.string.app_name ){
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				isTouched=false;
				applyFadeInAnimation();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mTitle);
				isTouched=false;
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setOnTouchListener(touchListener);

		if (savedInstanceState == null) {
			initAllFragments();
			displaySelectedSliderViewPage(fragmentsArr[0],0);
		}
	}
	private void applyFadeInAnimation(){
		if(!isSwitchClicked){
			supportInvalidateOptionsMenu();
		}else{
			isSwitchClicked=false;
		}

		Animation fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);	
		fade_in.setFillAfter(true);
		fade_in.setAnimationListener(fadeAnimationListener);
		if(SharedData.getInstance().SelectedDrawerItem==CalConstants.HOME_ID){
			copyItemView.startAnimation(fade_in);
			hisotryItemView.startAnimation(fade_in);
			switchItemView.startAnimation(fade_in);
		}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.CURRENCY_CONVERTER_ID){
			refreshItemView.startAnimation(fade_in);
			currencyItemView.startAnimation(fade_in);
		}
		else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.MEASURE_CONV_ID){
			measureItemView.startAnimation(fade_in);		
		}else if(SharedData.getInstance().SelectedDrawerItem==CalConstants.TIPPING_CONV_ID){
			tipItemView.startAnimation(fade_in);		
		}


	}
	OnTouchListener touchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!isTouched){
				isTouched=true;
				Log.d("DTouch","true");
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
				if(drawerOpen){
					Animation fade_in = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);	
					fade_in.setFillAfter(true);

					copyItemView.startAnimation(fade_in);
					hisotryItemView.startAnimation(fade_in);
					switchItemView.startAnimation(fade_in);
				}else{
					Animation fade_out = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);	
					fade_out.setFillAfter(true);

					copyItemView.startAnimation(fade_out);
					hisotryItemView.startAnimation(fade_out);
					switchItemView.startAnimation(fade_out);
				}
			}
			return false;
		}
	};

	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			adapter.notifyDataSetChanged();
			if(position==6){
				Toast.makeText(MainActivity.this,"Themes coming soon!", Toast.LENGTH_SHORT).show();
			}else{
				displaySelectedSliderViewPage(fragmentsArr[position],position);
			}
		}
	}
	private void  initAllFragments(){
		fragmentsArr[0]=new CalculatorFragment();
		fragmentsArr[1]=new CurrencyFragment();
		fragmentsArr[2]=new MeasureFragment();
		fragmentsArr[3]=new TipFragment();
		fragmentsArr[4]=new LoanFragment();
		fragmentsArr[5]=new GraphFragment();
		fragmentsArr[6]=new CalculatorFragment();
		fragmentsArr[7]=new SettingsFragment();

		//		SharedData.getInstance().simpleAdvanceSwitch=1;
		//		fragmentsArr[8]=new CalculatorFragment();
		//		SharedData.getInstance().simpleAdvanceSwitch=0;

	}
	private void displaySelectedSliderViewPage(Fragment fragment,final int position){
		SharedData.getInstance().SelectedDrawerItem=position;
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.frame_container,fragment,navMenuTitles[position]).commit();
		mDrawerLayout.closeDrawer(mDrawerList);
		setTitle(navMenuTitles[position]);
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
	}
	AnimationListener fadeAnimationListener=new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			copyItemView.setVisibility(View.VISIBLE);
			hisotryItemView.setVisibility(View.VISIBLE);
			switchItemView.setVisibility(View.VISIBLE);

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimationComplete=true;

		}
	};
	AnimationListener fadeOutAnimListener=new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimationComplete=true;
			
		}
	};
	AdListener adListener=new AdListener() {
		@Override
		public void onAdLoaded() {	
			interstitial.show();
			adCounter=new AdIntersitialCounter(90*1000,1000);
			adCounter.start();
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {

		}
		@Override
		public void onAdClosed(){
		}
		public void onAdLeftApplication(){

		}
	};
	private class AdIntersitialCounter extends CountDownTimer{

		public AdIntersitialCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			if(!CalculationsPreferences.getInstance().getInAppPurchaseStatus()){
				showAds();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}

	}

}
