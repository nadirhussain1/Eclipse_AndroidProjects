package com.calculations.android.inapp;

import android.util.Log;
import android.view.View;

import com.calculations.android.inapp.util.IabHelper;
import com.calculations.android.inapp.util.IabResult;
import com.calculations.android.inapp.util.Purchase;
import com.calculations.preferences.CalculationsPreferences;
import com.humby.fragments.SettingsFragment;

public class PostTransactionListener {
	static final String TAG = "PostTransaction";
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        	GoogleInAppModule googleInAppModule= GoogleInAppModule.getGoogleInAppModule();
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
            	googleInAppModule.complain("InAPP Purchase Failed");
            	return;
            }
            SettingsFragment.goProLayout.setVisibility(View.GONE);
            CalculationsPreferences.getInstance().saveInAppPurchaseStatus(true);
            if (purchase.getSku().equals(googleInAppModule.getProductId())) {
            	googleInAppModule.alert("InAPP Purchase Successful");
            	
            }
          }
    };
    
    public IabHelper.OnIabPurchaseFinishedListener getPostTransactionListener(){
    	return mPurchaseFinishedListener;
    }

}
