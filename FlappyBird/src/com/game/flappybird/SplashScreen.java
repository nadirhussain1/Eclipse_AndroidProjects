package com.game.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	private static int SPLASH_TIME_OUT = 5000;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
 
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
           
                Intent mainIntent = new Intent(SplashScreen.this, Settings.class);
                startActivity(mainIntent);
 
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
