package com.example.android.trivialdrivesample;

import android.util.Log;
import android.view.View;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Purchase;
import com.humby.bloodalcohol.BACActivity;

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
			BACActivity.bacPrefrence.saveInAppPurchaseFlag(true);
			BACActivity.bannerAdView.setVisibility(View.GONE);
			BACActivity.menuItem.setVisible(false);
			if (purchase.getSku().equals(googleInAppModule.getProductId())) {
				googleInAppModule.alert("InAPP Purchase Successful");

			}
		}
	};

	public IabHelper.OnIabPurchaseFinishedListener getPostTransactionListener(){
		return mPurchaseFinishedListener;
	}

}
