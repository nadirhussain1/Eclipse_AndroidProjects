package com.calculations.screen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.calculations.preferences.CalculationsPreferences;
import com.humby.adapters.CurrResultAdapter;
import com.humby.adapters.CurrencyListSelector;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.CountryCurrencyRate;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CurrencySelectListScreen{
	Dialog currencySettigsDialog=null;
	CurrencyListSelector currencySelectAdapter=null;
	ListView currencySettingsList=null;
	CurrResultAdapter currencyConvAdapter=null;
    View dialogView=null;
    
	public CurrencySelectListScreen(CurrResultAdapter adapter){
		currencyConvAdapter=adapter;

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dialogView = inflater.inflate(R.layout.currencies_selection_list, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);

		Button selectButton=(Button)dialogView.findViewById(R.id.selectAllButton);
		currencySettingsList=(ListView)dialogView.findViewById(R.id.currListView);
		currencySelectAdapter=new CurrencyListSelector(CountryCurrencyLoader.currenciesList);
		currencySelectAdapter.setSelectAllTextView(selectButton);
		currencySettingsList.setAdapter(currencySelectAdapter);

		Button doneButton=(Button)dialogView.findViewById(R.id.doneButton);
		doneButton.setOnClickListener(doneButtonClickListener);

		currencySettigsDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		currencySettigsDialog.setContentView(dialogView);
		
		applyFont();
	}
	private void applyFont(){
		TextView dialogHeading=(TextView)dialogView.findViewById(R.id.DialogHeading);
		TextView selectAllTextView=(TextView)dialogView.findViewById(R.id.selectAllButton);
		
		SharedData.getInstance().applyFontToTextView(dialogHeading, CalConstants.ROBOT_BOLD);
		SharedData.getInstance().applyFontToTextView(selectAllTextView, CalConstants.ROBOT_LIGHT);
	}
	public void  showCurrencySettings(){
		currencySettigsDialog.show();
	}
	public OnClickListener doneButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			saveSettings();
			currencySettigsDialog.cancel();

		}
	};
	private void saveSettings(){
		if(currencySelectAdapter.getNoOfSelection()>0){

			String selectedCurrencies="";
			CountryCurrencyRate tempObject=null;
			String currencyName=null;
			Double rate=1.0;
			String countryCode=null;
			int updatedSelectedCurrency=0;
			int prevSelectedIndex=CalculationsPreferences.getInstance().getPrevSelectedCurrency();
			String prevSelectedCurrencyName="United States Dollar";
			if(prevSelectedIndex!=-1 ){
				prevSelectedCurrencyName=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(prevSelectedIndex).getCountryName();
			}
			CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.clear();

			for(int count=1;count<=CountryCurrencyLoader.getInstance().currenciesList.size()+1;count++){
				Object value=0;
				HashMap adapterEntriesStates =currencySelectAdapter.getEnteryAdapterStates();
				Iterator entries = adapterEntriesStates.entrySet().iterator();
				while (entries.hasNext()) {
					Entry thisEntry = (Entry) entries.next();
					int key = (Integer)thisEntry.getKey();
					value = thisEntry.getValue(); 
					if(key==count && value.equals(1)){
						selectedCurrencies=selectedCurrencies+","+CountryCurrencyLoader.currenciesList.get(count-1).getCountryCode();
						countryCode=CountryCurrencyLoader.getInstance().currenciesList.get(count-1).getCountryCode();
						currencyName=CountryCurrencyLoader.getInstance().currenciesList.get(count-1).getCountryName();
						rate=CountryCurrencyLoader.getInstance().currenciesList.get(count-1).getRate();
						tempObject=new CountryCurrencyRate(countryCode, currencyName, rate);

						CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.add(tempObject);
						if(currencyName.equalsIgnoreCase(prevSelectedCurrencyName)){
							updatedSelectedCurrency=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()-1;
						}
					}
				}

			}
			if(CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()==CountryCurrencyLoader.getInstance().currenciesList.size()){
				selectedCurrencies="";
			}
			CalculationsPreferences.getInstance().saveSelectedCurrenciesCode(selectedCurrencies);
			CalculationsPreferences.getInstance().saveSelectedCurrencies(CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList);
			if(currencyConvAdapter!=null){
				currencyConvAdapter.notifyCurrencyDataChanged(updatedSelectedCurrency);
			}	
		}
	}

}
