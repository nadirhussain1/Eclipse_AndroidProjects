package com.calculations.screen;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.humby.adapters.DefaultCurrencyAdapter;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class DefaultCurrencyScreen{

	private Dialog currencySettigsDialog=null;
	private DefaultCurrencyAdapter defaultAdapter=null;
	private ListView currencySettingsList=null;
	private View dialogView=null;

	public DefaultCurrencyScreen(){

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 dialogView = inflater.inflate(R.layout.default_currency_selection_list, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);

		currencySettingsList=(ListView)dialogView.findViewById(R.id.currListView);
		defaultAdapter=new DefaultCurrencyAdapter(CountryCurrencyLoader.currenciesList);
		currencySettingsList.setAdapter(defaultAdapter);

		Button doneButton=(Button)dialogView.findViewById(R.id.doneButton);
		doneButton.setOnClickListener(doneButtonClickListener);

		currencySettigsDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		currencySettigsDialog.setContentView(dialogView);

		applyFontToTextViews();
	}
	private void applyFontToTextViews(){
		TextView dialogHeading=(TextView)dialogView.findViewById(R.id.DialogHeading);
		SharedData.getInstance().applyFontToTextView(dialogHeading, CalConstants.ROBOT_BOLD);
		
	}
	public void  showCurrencySettings(){
		currencySettigsDialog.show();
	}
	public OnClickListener doneButtonClickListener=new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			currencySettigsDialog.cancel();

		}
	};


}
