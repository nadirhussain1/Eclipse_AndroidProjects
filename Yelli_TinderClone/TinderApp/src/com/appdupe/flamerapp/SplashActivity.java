package com.appdupe.flamerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.android.slidingmenuexample.MainActivity;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;

public class SplashActivity extends Activity{
	/** The _splash time. */
	protected int _splashTime = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View splashView = inflater.inflate(R.layout.splash_activity, null, false);
		ScalingUtility.getInstance(this).scaleView(splashView);
		setContentView(splashView);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {	
				navigateToNext();
			}
		},_splashTime);
		
	}
	private void navigateToNext(){
		
		SessionManager mSessionManager=new SessionManager(SplashActivity.this);
		if (mSessionManager.isLoggedIn()) {
			Intent mIntent=new Intent(SplashActivity.this, MainActivity.class);
			mIntent.putExtra("FROM_SPLASH", true);
			startActivity(mIntent);
		}
		else {
			Intent mIntent=new Intent(SplashActivity.this, LoginUsingFacebook.class);
			startActivity(mIntent);
		}
		SplashActivity.this.finish();
	}
}
