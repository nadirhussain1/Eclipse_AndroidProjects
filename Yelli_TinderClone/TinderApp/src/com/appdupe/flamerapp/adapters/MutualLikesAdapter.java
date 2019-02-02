package com.appdupe.flamerapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.pojo.MutualLikeData;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.squareup.picasso.Picasso;

public class MutualLikesAdapter extends BaseAdapter{
	private ArrayList<MutualLikeData> likesList=null;
	private Context mContext=null;

	public MutualLikesAdapter(Context context,ArrayList<MutualLikeData> list){
		likesList=list;
		mContext=context;
	}
	@Override
	public int getCount() {
		return likesList.size();
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
		if(convertView ==null){
			LayoutInflater  mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.mutual_gallery_item, null);
			ScalingUtility.getInstance((Activity)mContext).scaleView(convertView);
		}

		TextView nameTextView=(TextView)convertView.findViewById(R.id.mutualNameTextView);
		ImageView picView=(ImageView)convertView.findViewById(R.id.mutualImageView);

		nameTextView.setText(likesList.get(position).getLikeName());
		if(likesList.get(position).getPicUrl()!=null && !TextUtils.isEmpty(likesList.get(position).getPicUrl())){			
			Picasso.with(mContext) 
			.load(likesList.get(position).getPicUrl()) 
			.fit()
			.centerInside()
			.into(picView);
		}
		return convertView;
	}

}
