package com.appdupe.flamerapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slidingmenuexample.MainActivity;
import com.appdupe.flamerapp.R;

public class DrawerListAdapter extends BaseAdapter{
    private String [] itemNames=null;
    private int [] itemImages={R.drawable.drawer_home_icon,R.drawable.drawer_profile_icon,R.drawable.drawer_messages_icon,R.drawable.drawer_settings_icon,R.drawable.drawer_invite_icon};
	private Context mContext=null;

    public DrawerListAdapter(Context context){
    	mContext=context;
    	itemNames=context.getResources().getStringArray(R.array.drawer_item_names);
	}
	@Override
	public int getCount() {
		return 5;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_row_item, null);
        }
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.itemIconView);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.itemTitleView);
        imgIcon.setBackgroundResource(itemImages[position]);
        txtTitle.setText(itemNames[position]);
        
        imgIcon.setImageBitmap(null);
        if(position==1){
        	imgIcon.setImageBitmap(((MainActivity)mContext).userProfileImage);
        }
		return convertView;
	}

}
