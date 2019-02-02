package com.humby.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculations.android.inapp.GoogleInAppModule;
import com.calculations.screen.DefaultCurrencyScreen;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class SettingsFragment extends Fragment{
	private View settingsView=null;
	private AdView bannerAdView=null;
	private TextView hapticSecondTextView=null;
	private TextView deciSepSecondTextView=null;
    public static RelativeLayout goProLayout=null;
	ImageView hapticImageView=null;
	ImageView decimalSepImageView=null;
	ImageView currSymbolImageView=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		settingsView=inflater.inflate(R.layout.settings_screen, container, false);
		ScalingUtility.getInstance().scaleView(settingsView);
		//GoogleInAppModule.getGoogleInAppModule().consumeItem(CalConstants.SETTINGS_PRO_ITEM);
		initializeControlClicks();
		applyFont();
		
		return settingsView;
	}
	private void applyFont(){

		String hapticText="<font color=#8B8B8B>Button vibration</font> <font color=#1FAFEC>on</font> <font color=#8B8B8B> or off</font>";
		hapticSecondTextView=(TextView)settingsView.findViewById(R.id.hapticFeedSecondText);
		hapticSecondTextView.setText(Html.fromHtml(hapticText));

		deciSepSecondTextView=(TextView)settingsView.findViewById(R.id.deciIndicatSecondText);
		String decimalSepText="<font color=#8B8B8B>European or </font> <font color=#1FAFEC>American </font> <font color=#8B8B8B>standard</font>";
		deciSepSecondTextView.setText(Html.fromHtml(decimalSepText));
		
		TextView currSecondTextView=(TextView)settingsView.findViewById(R.id.CurrenSecondText);
		TextView salesTaxTextView=(TextView)settingsView.findViewById(R.id.salesTaxSecondText);
		TextView rateTextView=(TextView)settingsView.findViewById(R.id.rateAppSecondText);
		
		TextView noAdsTextView=(TextView)settingsView.findViewById(R.id.NoAdsText);
		TextView sweetThemesTextView=(TextView)settingsView.findViewById(R.id.sweetThemesText);
		TextView justDollarTextView=(TextView)settingsView.findViewById(R.id.JustDollarText);
		
		TextView hapticHeadTextView=(TextView)settingsView.findViewById(R.id.hapticFeedHeadText);
		TextView decimalHeadTextView=(TextView)settingsView.findViewById(R.id.decimalIndiHeadText);
		TextView currHeadTextView =(TextView)settingsView.findViewById(R.id.CurrencyHeadText);
		TextView salesHeadTextView=(TextView)settingsView.findViewById(R.id.SalesTaxHeadText);
		TextView rateHeadTextView=(TextView)settingsView.findViewById(R.id.rateAppHeadText);
		
		SharedData.getInstance().applyFontToTextView(hapticHeadTextView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(decimalHeadTextView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(currHeadTextView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(salesHeadTextView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(rateHeadTextView, CalConstants.ROBOT_CONDENSED);
		
		
		SharedData.getInstance().applyFontToTextView(noAdsTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(sweetThemesTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(justDollarTextView, CalConstants.ALEGREYA_BOLDITALIC);
		
		SharedData.getInstance().applyFontToTextView(hapticSecondTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(deciSepSecondTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(currSecondTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(salesTaxTextView, CalConstants.ALEGREYA_BOLDITALIC);
		SharedData.getInstance().applyFontToTextView(rateTextView, CalConstants.ALEGREYA_BOLDITALIC);
	}
	@Override
	public void onResume() {
		super.onResume();
		if (bannerAdView != null) {
			bannerAdView.resume();
		}
	}

	@Override
	public void onPause() {
		if (bannerAdView != null) {
			bannerAdView.pause();
		}
		super.onPause();
	}

	private void initializeControlClicks(){
		bannerAdView=(AdView)settingsView.findViewById(R.id.adBanner);
		AdRequest adRequest = new AdRequest.Builder()
		.addTestDevice("9C51AF3FFBBE2D02322770C129028C8B")
		.build();
		bannerAdView.loadAd(adRequest);
       
		goProLayout=(RelativeLayout)settingsView.findViewById(R.id.GoProLayout);
		RelativeLayout hapticFeedbackLayout=(RelativeLayout)settingsView.findViewById(R.id.HapticFeedbackLayout);
		RelativeLayout thousandSeparatorLayout=(RelativeLayout)settingsView.findViewById(R.id.thousandsLayout);
		RelativeLayout currencySymbLayout=(RelativeLayout)settingsView.findViewById(R.id.currSymbolLayout);
		RelativeLayout salesTaxLayout=(RelativeLayout)settingsView.findViewById(R.id.salesTaxLayout);
		RelativeLayout rateAppLayout=(RelativeLayout)settingsView.findViewById(R.id.rateAppLayout);

		hapticFeedbackLayout.setOnClickListener(hapticFeedbackClickListener);
		thousandSeparatorLayout.setOnClickListener(thousandSeparatorClickListener);
		currencySymbLayout.setOnClickListener(currSymbolClickListener);
		salesTaxLayout.setOnClickListener(salesTaxClickListener);
		rateAppLayout.setOnClickListener(rateAppClickListener);
		goProLayout.setOnClickListener(goProClickListener);

		hapticImageView=(ImageView)settingsView.findViewById(R.id.HapticImage);
		decimalSepImageView=(ImageView)settingsView.findViewById(R.id.decimalIndicImage);
		currSymbolImageView=(ImageView)settingsView.findViewById(R.id.currSymbolImage);
		
		if(!GoogleInAppModule.getGoogleInAppModule().performTransactionAndBuyProduct(CalConstants.SETTINGS_PRO_ITEM, true)){
			goProLayout.setVisibility(View.VISIBLE);
		}

	}
	OnClickListener hapticFeedbackClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {

			if(SharedData.getInstance().isVibrationEnabled){
				SharedData.getInstance().isVibrationEnabled=false;
				hapticImageView.setBackgroundResource(R.drawable.hapticfeedbackoff);
				String hapticText="<font color=#8B8B8B>Button vibration on or</font> <font color=#028BC6>off</font>";
				hapticSecondTextView.setText(Html.fromHtml(hapticText));
			}else{
				SharedData.getInstance().isVibrationEnabled=true;
				hapticImageView.setBackgroundResource(R.drawable.hapticfeedbackon);
				String hapticText="<font color=#8B8B8B>Button vibration</font> <font color=#028BC6> on</font> <font color=#8B8B8B> or off</font>";
				hapticSecondTextView.setText(Html.fromHtml(hapticText));
			}

		}
	};
	OnClickListener thousandSeparatorClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().currentDecimalSeparator==CalConstants.AMERICAN_DECIMAL_SEPARATOR){
				SharedData.getInstance().currentDecimalSeparator=CalConstants.EUROPEAN_DECIMAL_SEPARATOR;
				decimalSepImageView.setBackgroundResource(R.drawable.europeantd);
				String decimalSepText="<font color=#028BC6>European </font> <font color=#8B8B8B>or American standard</font>";
				deciSepSecondTextView.setText(Html.fromHtml(decimalSepText));
			}else{
				SharedData.getInstance().currentDecimalSeparator=CalConstants.AMERICAN_DECIMAL_SEPARATOR;
				decimalSepImageView.setBackgroundResource(R.drawable.americantd);
				String decimalSepText="<font color=#8B8B8B>European or </font> <font color=#028BC6>American</font> <font color=#8B8B8B>standard</font>";
				deciSepSecondTextView.setText(Html.fromHtml(decimalSepText));
			}

		}
	};
	OnClickListener currSymbolClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
          DefaultCurrencyScreen defaultScreen=new DefaultCurrencyScreen();
          defaultScreen.showCurrencySettings();
		}
	};
	OnClickListener salesTaxClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {


		}
	};
	OnClickListener rateAppClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {


		}
	};
	OnClickListener goProClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			GoogleInAppModule.getGoogleInAppModule().performTransactionAndBuyProduct(CalConstants.SETTINGS_PRO_ITEM,false);
		}
	};

}
