package com.humby.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.calculations.brain.NumKeyPadHandler;
import com.calculations.screen.CurrencySelectListScreen;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CurrencyFragment extends Fragment {
	private View currCalView=null;
	NumKeyPadHandler numKeyPadHandler=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		currCalView=inflater.inflate(R.layout.curren_measu_page, container, false);
		ScalingUtility.getInstance().scaleView(currCalView);
		
		CountryCurrencyLoader currencyLoader=CountryCurrencyLoader.getInstance();
		currencyLoader.populateCountriesCurrencies();
		
		numKeyPadHandler=new NumKeyPadHandler(CalConstants.CURRENCY_CONVERTER_ID, currCalView);
		EditText inputAreaText=(EditText)currCalView.findViewById(R.id.unitInputEditArea);
		inputAreaText.setText(""+SharedData.getInstance().discardZerosAfterDecimal(String.valueOf(SharedData.getInstance().currencyInput)));
		inputAreaText.setSelection(inputAreaText.length());
		
		TextView unitType=(TextView)currCalView.findViewById(R.id.unitTypeTextArea);
		SharedData.getInstance().applyFontToTextView(inputAreaText, CalConstants.ROBOT_MEDIUM);
		SharedData.getInstance().applyFontToTextView(unitType, CalConstants.ROBOT_MEDIUM);
		
		return currCalView;

	}
	
	public void refreshActionCurrencies(){
		
	}
	public void displayCurrenciesDropDown(){
		CurrencySelectListScreen currenciesListDropDown=new CurrencySelectListScreen(numKeyPadHandler.getCurrencyAdapter());
		currenciesListDropDown.showCurrencySettings();
	}
	
}
