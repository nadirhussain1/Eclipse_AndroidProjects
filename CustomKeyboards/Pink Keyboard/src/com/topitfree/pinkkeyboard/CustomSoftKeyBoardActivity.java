package com.topitfree.pinkkeyboard;

import android.app.Activity;
import static com.topitfree.pinkkeyboard.CommonUtilities.SENDER_ID;
import static com.topitfree.pinkkeyboard.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.topitfree.pinkkeyboard.CommonUtilities.EXTRA_MESSAGE;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class CustomSoftKeyBoardActivity extends Activity   {
	TextView lblMessage=null;
    AsyncTask<Void, Void, Void> mRegisterTask;
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector connectonDetector;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view  = inflater.inflate(R.layout.settings, null, false);
		ScalingUtility.getInstance(this).scaleView(view);
		new SettingsManager(view, this);
		setContentView(view);
		registerPushNotifications();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d("Hello","Event"+event.getAction());
		return super.dispatchKeyEvent(event);
	}
	
	 private void registerPushNotifications(){
    	 connectonDetector = new ConnectionDetector(getApplicationContext());
    	 
         // Check if Internet present
         if (!connectonDetector.isConnectingToInternet()) {
             // Internet Connection is not present
             alert.showAlertDialog(this,
                     "Internet Connection Error",
                     "Please connect to working Internet connection", false);
             // stop executing code by return
             return;
         }
          
             
          
         // Make sure the device has the proper dependencies.
         GCMRegistrar.checkDevice(this);
         GCMRegistrar.checkManifest(this);  
         registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
         final String regId = GCMRegistrar.getRegistrationId(this);
  
         // Check if regid already presents
         if (regId.equals("")) {
             // Registration is not present, register now with GCM           
             GCMRegistrar.register(this, SENDER_ID);
         } else {

                 final Context context = this;
                 mRegisterTask = new AsyncTask<Void, Void, Void>() {
  
                     @Override
                     protected Void doInBackground(Void... params) {
                         // Register on our server
                         // On server creates a new user
                         ServerUtilities.register(context, regId);
                         return null;
                     }
  
                     @Override
                     protected void onPostExecute(Void result) {
                         mRegisterTask = null;
                     }
  
                 };
                 mRegisterTask.execute(null, null, null);
             }
         }
   // }
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
          
            WakeLocker.release();
        }
    };
     
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
