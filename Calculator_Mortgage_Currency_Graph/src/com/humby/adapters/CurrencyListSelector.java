package com.humby.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.calculations.preferences.CalculationsPreferences;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.CountryCurrencyRate;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CurrencyListSelector extends ArrayAdapter<CountryCurrencyRate>{

	HashMap<Integer, Integer> enteryAdapterStates=new HashMap<Integer, Integer>();
	private int noOfSelection=0;
	private boolean hasUserChangedSettings=false;
	private ArrayList<CountryCurrencyRate> items;
	private Button selectAllButton=null;
	private int isSelectAll=1;
    private String []currSymbolsArr=null;
    
	public void setSelectAllTextView(Button selectButton){
		selectAllButton=selectButton;
		selectAllButton.setOnClickListener(selectAllClickListener);
		currSymbolsArr=SharedData.getInstance().mContext.getResources().getStringArray(R.array.currencies_symbols);
	}
	public HashMap<Integer, Integer> getEnteryAdapterStates() {
		return enteryAdapterStates;
	}

	public void setEnteryAdapterStates(HashMap<Integer, Integer> enteryAdapterStates) {
		this.enteryAdapterStates = enteryAdapterStates;
	}
	public int getNoOfSelection(){
		return noOfSelection;
	}
	public boolean getHasUserChangedSettings(){
		return hasUserChangedSettings;
	}
	public void setHasUserChangedSettings(boolean changed){
		hasUserChangedSettings=changed;
	}
	public CurrencyListSelector(ArrayList<CountryCurrencyRate> items) {
		super(SharedData.getInstance().mContext, 0, items);
		this.items = items;
		checkMarkAlreadyEntries();
	}
	private void checkMarkAlreadyEntries(){
		String selectedDeals=CalculationsPreferences.getInstance().getSelectedCurrenciesCodes();
		for(int count=0;count<items.size();count++){
			if(selectedDeals.equalsIgnoreCase("")||(count>0 && selectedDeals.contains(CountryCurrencyLoader.currenciesList.get(count-1).getCountryCode()))){
				enteryAdapterStates.put(count, 1);
				noOfSelection++;
			}else{
				enteryAdapterStates.put(count,0);
			}
		}
	}
	private void selectOrDeselectAll(){
		enteryAdapterStates.clear();
		noOfSelection=0;
		for(int count=0;count<items.size();count++){
			enteryAdapterStates.put(count, isSelectAll);
			if(isSelectAll==1){
				noOfSelection++;
			}
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return items.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView currencyName=null;
		TextView currencySymobl=null;

		if(convertView==null){
			LayoutInflater inflator = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.curr_selection_row, null);
			ScalingUtility.getInstance().scaleView(convertView);
			
			currencyName=(TextView)convertView.findViewById(R.id.currNameTextView);
			currencySymobl=(TextView)convertView.findViewById(R.id.currencySymbol);
			
			SharedData.getInstance().applyFontToTextView(currencyName, CalConstants.ROBOT_REGULAR);
			SharedData.getInstance().applyFontToTextView(currencySymobl, CalConstants.ROBOT_REGULAR);
		}else{
			currencyName=(TextView)convertView.findViewById(R.id.currNameTextView);
			currencySymobl=(TextView)convertView.findViewById(R.id.currencySymbol);
		}

		
		CountryCurrencyRate rate = items.get(position);
		currencyName.setText(rate.getCountryName());
		currencySymobl.setText(currSymbolsArr[position]);

		if (enteryAdapterStates.containsKey(position)) {
			if (enteryAdapterStates.get(position) == 0) {
				currencyName.setTextColor(Color.parseColor("#353535"));
				currencySymobl.setTextColor(Color.parseColor("#353535"));
				currencyName.setTag(0);
			} else if (enteryAdapterStates.get(position) == 1) {
				currencyName.setTextColor(Color.parseColor("#069DDD"));
				currencySymobl.setTextColor(Color.parseColor("#069DDD"));
				currencyName.setTag(1);
			}
		}

		convertView.setTag(position);
		convertView.setOnClickListener(currSelectRowClickListener);
		return convertView;
	}
	OnClickListener currSelectRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {
			isSelectAll=0;
			selectAllButton.setBackgroundResource(R.drawable.select_all_off);

			int position=(Integer)clickedView.getTag();
			TextView currencyName=(TextView)clickedView.findViewById(R.id.currNameTextView);
			int isSelected=(Integer)currencyName.getTag();

			if(isSelected==1){
				itemUnSelectHandle(position);			
			}else{
				itemSelectedHandle(position);
			}


		}
	};
	private void itemSelectedHandle(int position){
		enteryAdapterStates.put(position, 1);
		noOfSelection++;
		notifyDataSetChanged();
	}
	private void itemUnSelectHandle(int position){
		enteryAdapterStates.put(position, 0);
		noOfSelection--;
		notifyDataSetChanged();
	}
	OnClickListener selectAllClickListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(isSelectAll==1){
				isSelectAll=0;
				selectAllButton.setBackgroundResource(R.drawable.select_all_off);
			}else{
				selectAllButton.setBackgroundResource(R.drawable.select_all_on);
				isSelectAll=1;
			}
			selectOrDeselectAll();

		}
	};
	public void clearHashMap(){
		enteryAdapterStates.clear();
	}

}
