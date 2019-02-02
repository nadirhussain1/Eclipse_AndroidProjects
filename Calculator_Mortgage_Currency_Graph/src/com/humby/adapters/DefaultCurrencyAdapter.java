package com.humby.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calculations.preferences.CalculationsPreferences;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyRate;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class DefaultCurrencyAdapter extends BaseAdapter{
	private ArrayList<CountryCurrencyRate> items;
	 private String []currSymbolsArr=null;
	 
	public DefaultCurrencyAdapter(ArrayList<CountryCurrencyRate> items){
		this.items=items;
		currSymbolsArr=SharedData.getInstance().mContext.getResources().getStringArray(R.array.currencies_symbols);
		SharedData.getInstance().prevSeleCurrencyIndex=CalculationsPreferences.getInstance().getPrevSelectedCurrency();
	}
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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
		
		if(position==SharedData.getInstance().prevSeleCurrencyIndex){
			currencyName.setTextColor(Color.parseColor("#069DDD"));
			currencySymobl.setTextColor(Color.parseColor("#069DDD"));
		}else{
			currencyName.setTextColor(Color.parseColor("#353535"));
			currencySymobl.setTextColor(Color.parseColor("#353535"));
		}
		convertView.setTag(position);
		convertView.setOnClickListener(currSelectRowClickListener);
		
		return convertView;
	}
	OnClickListener currSelectRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {
			int position=(Integer)clickedView.getTag();
			SharedData.getInstance().prevSeleCurrencyIndex=position;
			CalculationsPreferences.getInstance().savePrevSelectedCurrency(position);
			notifyDataSetChanged();
		}
	};

}
