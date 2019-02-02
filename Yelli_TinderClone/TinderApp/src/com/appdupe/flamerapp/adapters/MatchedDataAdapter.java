package com.appdupe.flamerapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.constants.FlamerConstants;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.utility.ScalingUtilities;
import com.appdupe.flamerapp.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;

public class MatchedDataAdapter extends ArrayAdapter<LikeMatcheddataForListview>{
	private LayoutInflater mInflater=null;
	private SessionManager sessionManager=null;
	private int PIC_SIZE=0;
	public MatchedDataAdapter(Context context,List<LikeMatcheddataForListview> objects) {
		super(context, R.layout.matchedlistviewitem, objects);
		PIC_SIZE=ScalingUtility.getInstance(((Activity)context)).conPxToDp(FlamerConstants.MATCHES_LIST_CONSTANT_SIZE);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sessionManager=new SessionManager(context);
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public LikeMatcheddataForListview getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.matchedlistviewitem, null);
			holder.imageview = (ImageView) convertView.findViewById(R.id.userimage);
			holder.textview=(TextView)convertView.findViewById(R.id.userName);
			holder.lastMasage=(TextView)convertView.findViewById(R.id.lastmessage);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textview.setText(getItem(position).getUserName());
		holder.imageview.setImageBitmap(getCircleBitmap(getItem(position).getFilePath()));
		holder.lastMasage.setText(sessionManager.getLastMessage(getItem(position).getFacebookid()));

		return convertView;
	}
	private Bitmap getCircleBitmap(String imagePath){
		Ultilities mUltilities=new Ultilities();
		Bitmap bitmapimage = mUltilities.showImage(imagePath);
		ScalingUtilities mScalingUtilities =new ScalingUtilities();
		Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,PIC_SIZE, PIC_SIZE, ScalingLogic.CROP);
		bitmapimage.recycle();
		Bitmap resultBitmap= mUltilities.getCircleBitmap(cropedBitmap, 1);
		cropedBitmap.recycle();

		return resultBitmap;
	}


	private class ViewHolder {
		ImageView imageview;
		TextView  textview;
		TextView lastMasage;
	}
}