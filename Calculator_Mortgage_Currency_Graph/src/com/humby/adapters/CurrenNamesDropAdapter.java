package com.humby.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calculations.screen.DropDownMenuWindow;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CurrenNamesDropAdapter extends BaseAdapter {

	private DropDownMenuWindow containerDropDown=null;
	private CurrResultAdapter currenConAdapter=null;
	
	public CurrenNamesDropAdapter(DropDownMenuWindow containerWindow,CurrResultAdapter resultUpdateAdapter){
		containerDropDown=containerWindow;
		currenConAdapter=resultUpdateAdapter;
	}


	@Override
	public int getCount() {
		int size=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size();
		if(size==0){
			size=CountryCurrencyLoader.getInstance().currenciesList.size();
		}
		return size;

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.general_menu_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);
		}
		
		TextView unitNameTextView=(TextView)convertView.findViewById(R.id.unitNameTextView);
		SharedData.getInstance().applyFontToTextView(unitNameTextView, CalConstants.ROBOT_REGULAR);
		
		if(position==SharedData.getInstance().prevSeleCurrencyIndex){
			unitNameTextView.setTextColor(Color.parseColor("#31C0F5"));
		}else{
			unitNameTextView.setTextColor(Color.parseColor("#BBBBBB"));	
		}
		String textString=null;
		
		int size=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.size();
         if(size>0){
				textString=CountryCurrencyLoader.getInstance().checkedCurrenciesArrayList.get(position).getCountryName();

			}else{
				textString=CountryCurrencyLoader.getInstance().currenciesList.get(position).getCountryName();

			}
       
        unitNameTextView.setText(textString);
		convertView.setTag(position);
		convertView.setOnClickListener(menuListRowClickListener);
		return convertView;

	}

	OnClickListener menuListRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedRow) {
			int position=(Integer) clickedRow.getTag();
			currenConAdapter.updateCurrencyConversion(position);
			containerDropDown.dismissWindow();
		}
	};
	


}
