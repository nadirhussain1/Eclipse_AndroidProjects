package com.app.motorbluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity{
	private static int SPLASH_TIME_OUT = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(mainIntent);

				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}
