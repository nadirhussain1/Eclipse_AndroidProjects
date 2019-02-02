package com.humby.adapters;

import android.app.Activity;
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

public class DrawerListAdapter extends BaseAdapter {
	private String [] drawerItemsTitles=new String[CalConstants.TOTAL_DRAWER_ITEMS];
	private int [] unSelectedImagesId=new int[]{R.drawable.calculator_unselected,R.drawable.currency_unselected,R.drawable.currency_unselected,R.drawable.tipping_unselected,R.drawable.mortgage_unselected,R.drawable.graphing_unselected};
	private int [] selectedImagesId=new int[]{R.drawable.calculator_selected,R.drawable.currency_selected,R.drawable.currency_selected,R.drawable.tipping_selected,R.drawable.mortgage_selected,R.drawable.graphing_selected};

	public DrawerListAdapter(String[]titles){
		drawerItemsTitles=titles;
	}
	@Override
	public int getCount() {

		return CalConstants.TOTAL_DRAWER_ITEMS;
	}

	@Override
	public Object getItem(int position) {	
		return drawerItemsTitles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) SharedData.getInstance().mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_row, null);
			ScalingUtility.getInstance().scaleView(convertView);
		}

		TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
		TextView themeTitle = (TextView) convertView.findViewById(R.id.themeTitle);
		SharedData.getInstance().applyFontToTextView(itemTitle, CalConstants.ROBOT_MEDIUM);
		SharedData.getInstance().applyFontToTextView(themeTitle, CalConstants.ROBOT_REGULAR);
		
		ImageView selector=(ImageView)convertView.findViewById(R.id.SelectorImage);
		ImageView themeSelector=(ImageView)convertView.findViewById(R.id.themeSelectorImage);
		View separatorLight=convertView.findViewById(R.id.dividerLight);
		View separatorDark=convertView.findViewById(R.id.dividerDark);

		themeSelector.setVisibility(View.INVISIBLE);
		convertView.setBackgroundColor(Color.parseColor("#ECECEC"));
		
		if(position<6){
			themeTitle.setVisibility(View.GONE);		
			itemTitle.setVisibility(View.VISIBLE);	
			itemTitle.setText(drawerItemsTitles[position]);

			if(position==SharedData.getInstance().SelectedDrawerItem){
				itemTitle.setTextColor(Color.parseColor("#18B4F6"));
				convertView.setBackgroundColor(Color.parseColor("#F7F7F7"));
				if(position==2){
					selector.setBackgroundResource(SharedData.getInstance().selectedMeasureThumbnails[SharedData.getInstance().prevSelectedMeasCatIndex]);
				}else{
					selector.setBackgroundResource(selectedImagesId[position]);
				}

			}else{
				itemTitle.setTextColor(Color.parseColor("#999999"));
				if(position==2){
					selector.setBackgroundResource(SharedData.getInstance().normalMeasureThumbnails[SharedData.getInstance().prevSelectedMeasCatIndex]);
				}else{
					selector.setBackgroundResource(unSelectedImagesId[position]);
				}
				
			}
		}else{
			themeTitle.setVisibility(View.VISIBLE);
			themeTitle.setText(drawerItemsTitles[position]);
			itemTitle.setVisibility(View.GONE);
            selector.setVisibility(View.INVISIBLE);
            
			if(position==SharedData.getInstance().SelectedDrawerItem){
				themeTitle.setTextColor(Color.parseColor("#18B4F6"));
				themeSelector.setVisibility(View.VISIBLE);
				convertView.setBackgroundColor(Color.parseColor("#F7F7F7"));

			}else{
				themeTitle.setTextColor(Color.parseColor("#555555"));
			}
		}


		if(position>=5){
			separatorLight.setVisibility(View.GONE);
			separatorDark.setVisibility(View.VISIBLE);
		}else{
			separatorLight.setVisibility(View.VISIBLE);
			separatorDark.setVisibility(View.GONE);	
		}

		return convertView;
	}

}
