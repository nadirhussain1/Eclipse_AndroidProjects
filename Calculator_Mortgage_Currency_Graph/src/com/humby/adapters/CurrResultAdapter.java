package com.humby.adapters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.calculations.preferences.CalculationsPreferences;
import com.calculations.screen.DropDownMenuWindow;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CurrResultAdapter extends BaseAdapter {
	private int currentSlectedCurrencyIndex=0;
	private Double inputValue=1.00;
	private TextView updateSelectedView=null;
	private Double currencyConversionFactor=1.00;
	HashMap<String,String> flagHashMap=null;
	DecimalFormat decimalFormat = null;

	public CurrResultAdapter(TextView selectedView){
		flagHashMap=new HashMap<String, String>();
		if(SharedData.getInstance().currentDecimalSeparator==CalConstants.AMERICAN_DECIMAL_SEPARATOR){
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.US);
			decimalFormat=(DecimalFormat)formatter;
		}else{
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.GERMAN);
			decimalFormat=(DecimalFormat)formatter;
		}

		updateSelectedView=selectedView;
		updateSelectedView.setOnClickListener(switchInputTypeListener);
		populateFlagStrings();
		retrievePrevSavedValues();
		restorePrevState();
	}
	private void populateFlagStrings(){	
		flagHashMap.put("United States Dollar", "unitedstates");
		flagHashMap.put("South African Rand", "southafrican");
		flagHashMap.put("South Korean Won", "southkorean");
		flagHashMap.put("North Korean Won", "northkorean");
		flagHashMap.put("New Zealand Dollar", "newzealand");
		flagHashMap.put("New Taiwan Dollar", "newtaiwan");
		flagHashMap.put("New Taiwan Dollar", "newtaiwan");
		flagHashMap.put("Sri Lankan Rupee", "srilankan");
		flagHashMap.put("São Tomé Dobra", "saotome");
	}
	private void retrievePrevSavedValues(){
		currentSlectedCurrencyIndex=CalculationsPreferences.getInstance().getPrevSelectedCurrency();
		if(currentSlectedCurrencyIndex==-1){
			findDefaultUSCurrencyIndex();
		}
		SharedData.getInstance().prevSeleCurrencyIndex=currentSlectedCurrencyIndex;
		CalculationsPreferences.getInstance().savePrevSelectedCurrency(currentSlectedCurrencyIndex);
		inputValue=SharedData.getInstance().currencyInput;

	}
	private void restorePrevState(){
		String currencyName="";
		if(currentSlectedCurrencyIndex<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()){
			currencyName=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(currentSlectedCurrencyIndex).getCountryName();
			currencyConversionFactor=inputValue/CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(currentSlectedCurrencyIndex).getRate();

			if(currencyName.equalsIgnoreCase("United States Dollar")){
				currencyName="U.S. DOLLARS";
			}
			if(updateSelectedView!=null){
				updateSelectedView.setText(currencyName.toUpperCase());
			}
		}
	}
	private void findDefaultUSCurrencyIndex(){

		for(int index=0;index<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size();index++){
			if(CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(index).getCountryName().equalsIgnoreCase("United States Dollar")){
				currentSlectedCurrencyIndex=index;
				return;
			}
		}
		currentSlectedCurrencyIndex=0;
	}
	private static Double roundDouble(Double unroundedNumber,short precisionLimit){
		BigDecimal bd = new BigDecimal(unroundedNumber);
		BigDecimal rounded = bd.setScale(precisionLimit, BigDecimal.ROUND_CEILING);
		return rounded.doubleValue();
	}
	public void updateCurrencyConversion(int currencyIndex){
		if(currencyIndex<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()){
			currentSlectedCurrencyIndex=currencyIndex;
			SharedData.getInstance().prevSeleCurrencyIndex=currencyIndex;
			updateCurrencyConversion();
			this.notifyDataSetChanged();
			CalculationsPreferences.getInstance().savePrevSelectedCurrency(currentSlectedCurrencyIndex);
		}
	}
	private void updateCurrencyConversion(){
		String currencyName="";
		if(currentSlectedCurrencyIndex<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()){
			currencyName=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(currentSlectedCurrencyIndex).getCountryName();
			currencyConversionFactor=inputValue/CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(currentSlectedCurrencyIndex).getRate();

			if(currencyName.equalsIgnoreCase("United States Dollar")){
				currencyName="U.S. DOLLARS";
			}
			if(updateSelectedView!=null){
				updateSelectedView.setText(currencyName.toUpperCase());
			}
		}
	}
	public void updateUserInput(Double inputCurrency) {
		this.inputValue = inputCurrency;
		updateCurrencyConversionFactor();
		SharedData.getInstance().currencyInput=inputValue;

	}
	public int getResourceId(String name){
		int id=SharedData.getInstance().mContext.getResources().getIdentifier(name,"drawable",SharedData.getInstance().mContext.getPackageName());
		return id;
	}
	private void updateCurrencyConversionFactor(){
		if(currentSlectedCurrencyIndex<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()){
			currencyConversionFactor=inputValue/CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(currentSlectedCurrencyIndex).getRate();
			this.notifyDataSetChanged();
		}
	}
	public void notifyCurrencyDataChanged(int updateSelectedCurrency){
		updateCurrencyConversion(updateSelectedCurrency);
		this.notifyDataSetChanged();
	}
	private void prepareMenuWindow(){

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View generalListView = inflater.inflate(R.layout.general_menu_list, null, false);
        ScalingUtility.getInstance().scaleView(generalListView);
        
		DropDownMenuWindow menu=new DropDownMenuWindow(generalListView);
		menu.showCalculatorMenu(updateSelectedView);

		ListView menuListView=(ListView)generalListView.findViewById(R.id.generalMenuList);
		CurrenNamesDropAdapter menuListAdapter=new CurrenNamesDropAdapter(menu,this);
		menuListView.setAdapter(menuListAdapter);
	}
	OnClickListener switchInputTypeListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			prepareMenuWindow();

		}
	};
	OnClickListener resultListRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {
			String clickedResult=(String) clickedView.getTag();
			ClipboardManager clipboard = (ClipboardManager)SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.CLIPBOARD_SERVICE);
			clipboard.setText(clickedResult);
			Toast.makeText(SharedData.getInstance().mContext, "Result copied to clipboard", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public int getCount() {

		return (CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()-1);
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.currency_result_list_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);
		}
		if(position>=currentSlectedCurrencyIndex){
			position=position+1;
		}
		TextView currencyNameTextView=(TextView)convertView.findViewById(R.id.resultTextTitle);
		TextView currencyAmount=(TextView)convertView.findViewById(R.id.resultTextAmount);
		ImageView flagImageView=(ImageView)convertView.findViewById(R.id.flagImageView);
		SharedData.getInstance().applyFontToTextView(currencyNameTextView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(currencyAmount, CalConstants.ROBOT_LIGHT);

		currencyNameTextView.setText("");
		currencyAmount.setText("");	
		flagImageView.setBackgroundResource(0);

		String currencyName="";
		Double rate=-1.0;

		if(position<CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size()){
			currencyName=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(position).getCountryName();
			rate=currencyConversionFactor*CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(position).getRate();

		}

		String subString=flagHashMap.get(currencyName);
		if(subString==null){
			int spaceIndex=currencyName.indexOf(' ');
			if(spaceIndex>0){
				subString=currencyName.substring(0, spaceIndex);	
			}else{
				subString=currencyName;
			}
		}

		int id=getResourceId(subString.toLowerCase()+"_flag");
		flagImageView.setBackgroundResource(id);

		if(currencyName.equalsIgnoreCase("United States Dollar")){
			currencyName="U.S. DOLLARS";
		}
		currencyNameTextView.setText(currencyName.toUpperCase());
		currencyNameTextView.setLines(1);
		currencyNameTextView.setMaxLines(1);
		currencyNameTextView.setSingleLine(true);
		currencyNameTextView.setSelected(true);
		currencyNameTextView.setHorizontalFadingEdgeEnabled(true);
		currencyNameTextView.setFadingEdgeLength(70);
		currencyNameTextView.setEllipsize(TruncateAt.MARQUEE);
		currencyNameTextView.setHorizontallyScrolling(true);

		currencyAmount.setLines(1);
		currencyAmount.setMaxLines(1);
		currencyAmount.setSingleLine(true);
		currencyAmount.setSelected(true);
		currencyAmount.setHorizontalFadingEdgeEnabled(true);
		currencyAmount.setFadingEdgeLength(70);

		rate=roundDouble(rate,(short)2);
		String formatValue="0";
		if(rate==0){
			currencyAmount.setText("0");
		}
		else if(rate!=-1){
			formatValue=decimalFormat.format(rate);
			currencyAmount.setText(formatValue);
		}
		convertView.setTag(formatValue);
		convertView.setOnClickListener(resultListRowClickListener);
		return convertView;
	}

}
