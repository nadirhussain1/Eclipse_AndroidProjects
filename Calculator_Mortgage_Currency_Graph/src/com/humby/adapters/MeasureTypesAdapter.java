package com.humby.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class MeasureTypesAdapter extends BaseAdapter{
	private String[] adapterListNames=null;
	
	
	public MeasureTypesAdapter(String[] measureTypes){
		adapterListNames=measureTypes;	
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
			convertView = inflater.inflate(R.layout.general_menu_image_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);
		}

		TextView unitNameTextView=(TextView)convertView.findViewById(R.id.unitNameTextView);
		SharedData.getInstance().applyFontToTextView(unitNameTextView, CalConstants.ROBOT_REGULAR);
		String textString=adapterListNames[position];
		unitNameTextView.setText(textString);

		ImageView unitImageView=(ImageView)convertView.findViewById(R.id.unitImage);
		
		if(SharedData.getInstance().prevSelectedMeasCatIndex==position){
			unitNameTextView.setTextColor(Color.parseColor("#069DDD"));
			unitImageView.setBackgroundResource(SharedData.getInstance().selectedMeasureThumbnails[position]);
		}else {
			unitNameTextView.setTextColor(Color.parseColor("#999999"));
			unitImageView.setBackgroundResource(SharedData.getInstance().normalMeasureThumbnails[position]);
		}
		
		return convertView;
	}
	
}
