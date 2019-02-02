package com.example.android.trivialdrivesample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;

public class GoogleInAppModule {
	
	    String PRODUCT_ID="";
	    int RC_REQUEST = 1000;
	    IabHelper mHelper;
	    static final String TAG = "InAppModule";
	    boolean mIsPremium = false;
	    Context mContext=null;
	    private static GoogleInAppModule googleInAppModule=null;
	  
	    
	    private GoogleInAppModule(){
	    	
	    }
	    public static GoogleInAppModule getGoogleInAppModule(){
	    	
	    	if(googleInAppModule==null){
	    		googleInAppModule=new GoogleInAppModule();
	    	}
	    	
	    	return googleInAppModule;
	    	
	    }
	    
	    public void InitializeGoogleInApp(Context context,String appEncodedInAppKey){
	      mContext=context;
	  	  mHelper = new IabHelper(mContext, appEncodedInAppKey);
	        mHelper.enableDebugLogging(true);

	        // Start setup. This is asynchronous and the specified listener
	        // will be called once setup completes.
	        Log.d(TAG, "Starting setup.");
	        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	            public void onIabSetupFinished(IabResult result) {
	                Log.d(TAG, "Setup finished.");

	                if (!result.isSuccess()) {
	                    // Oh noes, there was a problem.
	                    complain("Problem setting up in-app billing: " + result);
	                    return;
	                }

	                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.     
	            }
	        });
	  	  
	    }
	    public void performTransactionAndBuyProduct(String PRODUCT_ID){
	        this.PRODUCT_ID=PRODUCT_ID;
	        mHelper.queryInventoryAsync(mGotInventoryListener);
	    	
	    }
	    // Listener that's called when we finish querying the items we own
	    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	            Log.d(TAG, "Query inventory finished.");
	            if (result.isFailure()) {
	                complain("Failed to query inventory: " + result);
	                return;
	            }

	            Log.d(TAG, "Query inventory was successful.");

	            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
	            if (inventory.hasPurchase(PRODUCT_ID)) {
	                consumeProduct(inventory.getPurchase(PRODUCT_ID));
	                
	            }else{
	            	startInAPP();
	            }
	            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
	            
	            return;
	            
	        }
	    };
	    
	    private void startInAPP(){
	    	PostTransactionListener listenerObject=new PostTransactionListener();
	    	mHelper.launchPurchaseFlow((Activity)mContext,PRODUCT_ID, RC_REQUEST, listenerObject.getPostTransactionListener());
	    }
	   
	    public void complain(String message) {
	        Log.e(TAG, "****  Error: " + message);
	        alert("Error: " + message);
	    }
	    void alert(String message) {
	        AlertDialog.Builder bld = new AlertDialog.Builder(mContext);
	        bld.setMessage(message);
	        bld.setNeutralButton("OK", null);
	        Log.d(TAG, "Showing alert dialog: " + message);
	        bld.create().show();
	    }
	    
	 // Called when consumption is complete
	    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
	        public void onConsumeFinished(Purchase purchase, IabResult result) {
	            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
	            if (result.isSuccess()) {
	                // successfully consumed, so we apply the effects of the item in our
	                // game world's logic, which in our case means filling the gas tank a bit
	                Log.d(TAG, "Consumption successful. Provisioning.");
	                CounterClass counter=new CounterClass(30, 1000);
	                counter.start();
	                
	               // alert("Already purchased Item has been disowned");
	            }
	            else {
	                complain("User already Own this Item " + result);
	            }
	            Log.d(TAG, "End consumption flow.");
	        }
	    };
	    
	    public void destroyInAppResources(){
	    	 if (mHelper != null) mHelper.dispose();
	           mHelper = null;	
	    }
	    public void consumeProduct(Purchase purchase){
	    	
	    	 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
	    }
	    public boolean IsHandledPurchaseTransactionResult(int requestCode, int resultCode, Intent data){
	    	
	    	return mHelper.handleActivityResult(requestCode, resultCode, data);
	    }
	    
	    public String getProductId(){
	    	return PRODUCT_ID;
	    }
	    
	    public class CounterClass extends CountDownTimer{

	    	public CounterClass(long millisInFuture, long countDownInterval) {
	    		super(millisInFuture, countDownInterval);
	    		// TODO Auto-generated constructor stub
	    	}

	    	@Override
	    	public void onFinish() {
	    		// TODO Auto-generated method stub
	    		startInAPP();
	    		
	    	}

	    	@Override
	    	public void onTick(long millisUntilFinished) {
	    		// TODO Auto-generated method stub
	    		
	    	}
	    }
	   

}
