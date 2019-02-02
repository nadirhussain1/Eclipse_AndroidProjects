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
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class MeasureUnitsAdapter extends BaseAdapter{
	private String[] adapterListNames=null;
	private DropDownMenuWindow containerDropDown=null;
	private MeasureResultAdapter measureResultConvAdapter=null;


	public MeasureUnitsAdapter(String[] measureUnits,DropDownMenuWindow containerWindow,MeasureResultAdapter measureResultAdapter){
		adapterListNames=measureUnits;
		containerDropDown=containerWindow;
		measureResultConvAdapter=measureResultAdapter;
	}
	@Override
	public int getCount() {
		return adapterListNames.length;
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
		String textString=adapterListNames[position];
		unitNameTextView.setText(textString);
        
		if(position==SharedData.getInstance().prevSeleMeasurementIndex){
			unitNameTextView.setTextColor(Color.parseColor("#31C0F5"));
		}else{
			unitNameTextView.setTextColor(Color.parseColor("#BBBBBB"));	
		}
		convertView.setTag(position);
		convertView.setOnClickListener(menuListRowClickListener);
		return convertView;
	}
	OnClickListener menuListRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedRow) {
			int position=(Integer) clickedRow.getTag();
			measureResultConvAdapter.updateSelectedMeasurementType(position);
			containerDropDown.dismissWindow();
		}
	};

}
