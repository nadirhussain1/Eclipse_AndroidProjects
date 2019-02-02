package com.mobive.adapters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.humby.dealular.R;
import com.mobive.bean.Deal;
import com.mobive.dealme.DataUtility;
import com.mobive.dealme.Promotion_Activity;
import com.mobive.dealme.ScalingUtility;
import com.mobive.net.GetRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class DealMeAdapter extends BaseAdapter{

	Context context=null;
	ArrayList<Deal> deals=null;
	ArrayList<GetRequest>downloadRequests=null;
	int currentRequest=0;
	private int totalDownloadRequests=0;
    private int alreadyDownloadedImages=0;
    
	public ArrayList<Deal> getDeals() {
		return deals;
	}
	public void setDeals(ArrayList<Deal> deals) {
		this.deals = deals;
	}

	public DealMeAdapter(Context context,ArrayList<Deal> deals)
	{
		this.context=context;
		this.deals=deals;
		downloadImages(this);

	}
	public void downloadImages(BaseAdapter adapter){
		if(currentRequest==totalDownloadRequests){
			currentRequest=0;
			totalDownloadRequests=0;

			if(downloadRequests==null){
				downloadRequests=new ArrayList<GetRequest>();
			}else{
				downloadRequests.clear();
			}
			
			for(int count=alreadyDownloadedImages;count<deals.size();count++){
				String imageURl=deals.get(count).getImages().getImage_big();
				String imageName=extractImageName(imageURl);
				if(loadBitmapFromSdCard(imageName)==null){
					IconRequestListener listener=new IconRequestListener(count,this,imageName);
					GetRequest request=new GetRequest(listener,imageURl, DataUtility.getContext());
					downloadRequests.add(request);
					totalDownloadRequests++;
				}
			}
			alreadyDownloadedImages=deals.size();
			if(totalDownloadRequests==0){
				DataUtility.shouldLoadNextDeals=true;
			}
			else{
				Thread thread = new Thread()
				{
					@Override
					public void run() {

						for(int count=0;count<downloadRequests.size();count++){
							downloadRequests.get(count).execute();
						}
					}
				};

				thread.start();
			}
		}

	}
	OnClickListener dealClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position=(Integer)(v.getTag());
			Util.selectedDeal=deals.get(position);
			Intent intent = new Intent(context, Promotion_Activity.class);
			context.startActivity(intent);		

		}
	};

	@Override
	public int getCount() {
		downloadImages(this);
		return deals.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		if( convertView == null )	
		{
			convertView = inflater.inflate(R.layout.deal_row, null, true);
		}
		RelativeLayout parentLayout=(RelativeLayout)convertView.findViewById(R.id.parentLayout);
		ImageView poweredByLayout=(ImageView)convertView.findViewById(R.id.poweredByLayout);
		poweredByLayout.setVisibility(View.GONE);

		if(position==0)
		{
			poweredByLayout.setVisibility(View.VISIBLE);

		}
		parentLayout.setVisibility(View.VISIBLE);
		Deal deal=deals.get(position);
		ImageView iconImage=(ImageView)convertView.findViewById(R.id.iconImage);
		ProgressBar imageDownloadBar=(ProgressBar)convertView.findViewById(R.id.waitbar);
		String imageName=extractImageName(deal.getImages().getImage_big());
		iconImage.setTag(imageName);

		BitmapManager.INSTANCE.loadBitmap(imageName,iconImage,imageDownloadBar);
		TextView descriptionText=(TextView)convertView.findViewById(R.id.descriptionText);
		descriptionText.setText(deal.getTitle());

		TextView titleText=(TextView)convertView.findViewById(R.id.titleText);
		titleText.setText(deal.getSource().getName());
		convertView.setOnClickListener(dealClickListener);


		String priceFormatted=deal.getPrice().getFormatted();
		String priceSign=priceFormatted.substring(0, 1);

		TextView addressText=(TextView)convertView.findViewById(R.id.addressText);
		if(deal.getBusiness().getLocations()!=null &&deal.getBusiness().getLocations().size()>0)
			addressText.setText(deal.getBusiness().getLocations().get(0).getAddress()+" "+deal.getBusiness().getLocations().get(0).getSmart_locality());

		TextView timeText=(TextView)convertView.findViewById(R.id.timeText);
		timeText.setText(timeDifference(deal.getDate_added(),deal.getDivision().getTime_zone_diff()));

		TextView priceSignView=(TextView)convertView.findViewById(R.id.priceSignButton);
		priceSignView.setText(priceSign);

		TextView priceView=(TextView)convertView.findViewById(R.id.priceButton);
		priceView.setText(priceFormatted.substring(1, priceFormatted.length()));

		TextView discountText=(TextView)convertView.findViewById(R.id.discountAmountText);
		String discountAmount=(int)deal.getDiscount().getRaw()+"";
		discountText.setText(discountAmount);

		TextView daysLeftText=(TextView)convertView.findViewById(R.id.timeLeftButton);
		SimpleDateFormat dateAdded=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {

			Date endingDate=dateAdded.parse(deal.getEnd_date());
			long difference=(endingDate.getTime()+deal.getDivision().getTime_zone_diff())-new Date().getTime();

			long days=(difference / (1000 * 60 * 60 * 24));
			daysLeftText.setText(days+"");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		convertView.setTag(position);
		return convertView;
	}
	public String timeDifference(String postAddedTime,int offsetHours)
	{
		Log.i("offset", offsetHours+"");
		SimpleDateFormat dateAdded=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date=dateAdded.parse(postAddedTime);
			Date date2=new Date();
			long offset =offsetHours*60*60*1000;
			long diffInMillis=date2.getTime()-(date.getTime()+offset);
			long diffInSecs=diffInMillis/1000;
			if(diffInSecs<60)
			{
				if(diffInSecs==1)
					return diffInSecs+" second ago ";
				return diffInSecs+" seconds ago ";
			}
			else
			{
				long diffInMin=diffInSecs/60;
				if(diffInMin<60)
				{
					if(diffInMin==1)
						return diffInMin+" minute ago";
					return diffInMin+" minutes ago";
				}
				else
				{
					long diffInHrs=diffInMin/60;
					if(diffInHrs<24)
					{
						if(diffInHrs==1)
							return diffInHrs+" hour ago";	
						return diffInHrs+" hours ago";
					}
					else
					{
						long diffInDays=diffInHrs/24;
						if(diffInDays==1)
							return diffInDays+" day ago";
						return diffInDays+" days ago";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date2=new Date();
		return "";
	}
	class IconRequestListener  implements RequestListener
	{
		int position=0;
		BaseAdapter adapter=null;
		String imageName="";
		public IconRequestListener(int position,BaseAdapter badapter,String name)
		{
			this.position=position;
			this.adapter=badapter;
			imageName=name;
		}
		@Override
		public void onSuccess(final InputStream inputStream) {
			Thread imageThread=new Thread(new Runnable() {

				@Override
				public void run() {

					saveStreamToImage(inputStream,adapter,imageName);

				}

			});
			imageThread.run();

		}

		@Override
		public void onFail(String message) {

		}
	};
	public void recycleBitmap(Bitmap bitmap)
	{
		if(bitmap!=null && !bitmap.isRecycled())
		{
			bitmap.recycle();
			bitmap=null;
		}
	}
	public String extractImageName(String imageURL){
		String name="";
		int lastIndex=imageURL.lastIndexOf("/");
		if(lastIndex!=-1){
			name=imageURL.substring(lastIndex+1);
		}
		return name;
	}
	public Bitmap loadBitmapFromSdCard(String imageName){
		File dir = new File(Environment.getExternalStorageDirectory(),"DealMe");
		Bitmap mBitmap=null;
		if(dir.exists())
		{
			mBitmap = BitmapFactory.decodeFile(dir.getPath()+"/"+imageName);
		}
		return mBitmap;

	}
	public void saveStreamToImage(InputStream inputStream,BaseAdapter adapter,String imageName){
		Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
		
		Resources resources =context.getResources();
		float marginWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, resources.getDisplayMetrics());
		float requiredWidth=((((Activity)context).getWindowManager().getDefaultDisplay().getWidth())-marginWidth);
		float requiredHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200, resources.getDisplayMetrics());
		
		if(bitmap!=null){
			Matrix matrix = new Matrix();
			float widthfactor=requiredWidth/bitmap.getWidth();
			float heightfactor=requiredHeight/bitmap.getHeight();
			matrix.postScale(widthfactor, heightfactor);
			Bitmap updatedBitmap= Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, false);
			
			OutputStream fos = null;                
			File dir = new File(Environment.getExternalStorageDirectory(),"DealMe");
			if(!dir.exists()){
				dir.mkdir();
			}

			File filePath=new File(dir,imageName);

			try {
				fos = new FileOutputStream(filePath);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				updatedBitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {

					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			currentRequest++;
			Log.d("Catgories","Total Downloaded Images "+currentRequest);
			if(currentRequest==totalDownloadRequests){
				DataUtility.shouldLoadNextDeals=true;
				adapter.notifyDataSetChanged();
			}

			
		}
	}
}
