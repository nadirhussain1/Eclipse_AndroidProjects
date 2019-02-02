package com.mobive.dealme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.humby.dealular.R;
import com.mobive.util.Util;

public class Promotion_Activity extends SherlockActivity {
	ActionBar actionBar;
	RelativeLayout ImgLayout;
	TextView disText;
	TextView priceTxt;
	TextView discountTxt;
	TextView timeLeftTxt;
	TextView dealProviderName;
	TextView adrpart1Txt;
	TextView adrpart2Txt;  
	TextView dateTxt;
	TextView timeTxt;
	TextView amPmTxt;   
	TextView discrptionTxt; 
	Button calenderImg;
	Button mapImg;

	//Button purchaseBtn;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_detail_row);
		DataUtility.setContext(this);
		DataUtility.lastVisitedActivity=DealMeConstants.PROMOTION_ACTIVITY_ID;
		
		actionBar=getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.back);
		actionBar.setDisplayShowTitleEnabled(true);

		ImgLayout = (RelativeLayout) findViewById(R.id.dealImage);
		calenderImg = (Button) findViewById(R.id.calenderImg);
		mapImg = (Button) findViewById(R.id.mapIt);
		disText = (TextView) findViewById(R.id.descriptionText);
		timeLeftTxt =  (TextView) findViewById(R.id.timeLeftButton);
		dateTxt =  (TextView) findViewById(R.id.date);
		timeTxt =  (TextView) findViewById(R.id.time);

		InputStream input = null;
		try {
			input = new URL(Util.selectedDeal.getImages().getImage_small().toString()).openStream();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Bitmap bitmap=BitmapFactory.decodeStream(input);
		ImgLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));


		calenderImg.setOnClickListener(calendarClickListener);
		mapImg.setOnClickListener(mapClickListener);
		//purchaseBtn.setOnClickListener(purchaseButtonClickListener);

		disText.setText(Util.selectedDeal.getTitle());
		String priceFormatted=Util.selectedDeal.getPrice().getFormatted();
		priceTxt = (TextView) findViewById(R.id.priceButton);
		priceTxt.setText(priceFormatted.substring(1, priceFormatted.length()));
		discountTxt = (TextView) findViewById(R.id.discountAmountText);
		String discountAmount=(int)Util.selectedDeal.getDiscount().getRaw()+"";
		discountTxt.setText(discountAmount);


		SimpleDateFormat dateAdded=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {

			Date endingDate=dateAdded.parse(Util.selectedDeal.getEnd_date());
			long difference=(endingDate.getTime()+Util.selectedDeal.getDivision().getTime_zone_diff())-new Date().getTime();

			long days=(difference / (1000 * 60 * 60 * 24));
			timeLeftTxt.setText(days+"");
		} catch (ParseException e) {
			e.printStackTrace();
		}


		dealProviderName =  (TextView) findViewById(R.id.companyProvider);
		dealProviderName.setText(Util.selectedDeal.getBusiness().getName());
		actionBar.setTitle(Util.selectedDeal.getBusiness().getName());

		adrpart1Txt =  (TextView) findViewById(R.id.AddressFirstLine);
		adrpart2Txt =  (TextView) findViewById(R.id.AddressSecondLine);
		if(Util.selectedDeal.getBusiness().getLocations()!=null &&Util.selectedDeal.getBusiness().getLocations().size()>0){
			adrpart1Txt.setText(Util.selectedDeal.getBusiness().getLocations().get(0).getAddress()+".");
			adrpart2Txt.setText(Util.selectedDeal.getBusiness().getLocations().get(0).getLocality()+" "+Util.selectedDeal.getBusiness().getLocations().get(0).getSmart_locality()+" "+Util.selectedDeal.getBusiness().getLocations().get(0).getZip_code());

		}
		long offsetHours=Util.selectedDeal.getDivision().getTime_zone_diff();
		long offset =offsetHours*60*60*1000;
		SimpleDateFormat dateCreated=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date=dateCreated.parse(Util.selectedDeal.getEnd_date());
			long createdTime=date.getTime()+offset;
			String test=getCreatedTimeStr(createdTime);
			dateTxt.setText(test);
			String time=getTimeStr(createdTime);
			timeTxt.setText(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		discrptionTxt =  (TextView) findViewById(R.id.DetailsDescription);
		discrptionTxt.setText(Util.selectedDeal.getDescription().toString());

	}

	OnClickListener calendarClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar cal = Calendar.getInstance();
			Date startDate = null;
			Date endDate = null;
			SimpleDateFormat dateAdded=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			try {
				startDate=dateAdded.parse(Util.selectedDeal.getDate_added());
				endDate=dateAdded.parse(Util.selectedDeal.getEnd_date());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", startDate.getTime());
			intent.putExtra("allDay", true);
			intent.putExtra("rrule", "FREQ=DAILY;COUNT=1");
			intent.putExtra("endTime", endDate.getTime());
			intent.putExtra("title", Util.selectedDeal.getTitle());
			startActivity(intent);	

		}
	};
	OnClickListener mapClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(Util.selectedDeal.getBusiness().getLocations()!=null &&Util.selectedDeal.getBusiness().getLocations().size()>0 )

			{
				String url = "http://maps.google.com/maps?q=" + Util.selectedDeal.getBusiness().getLocations().get(0).getAddress();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(i);
			}
			else
			{
				Toast.makeText(Promotion_Activity.this, "Location not available for this deal.", Toast.LENGTH_SHORT).show();
			}




		}
	};
	OnClickListener purchaseButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Util.selectedDeal.getUrl()));
		startActivity(i);


		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.promotions_main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId) {

		case android.R.id.home:

			finish();
			return true;

		case R.id.menu_purchase:
			if(Util.selectedDeal != null) {
				Intent shareIntent = new Intent(Intent.ACTION_VIEW);
				shareIntent.setData(Uri.parse(Util.selectedDeal.getMobile_url()));
				startActivity(shareIntent);

			}
			return true;

		case R.id.settings_menu:
			Intent intent = new Intent(Promotion_Activity.this, Settings_Activity.class);
			startActivity(intent);

			return true;
			
		case R.id.share_menu:
			if(Util.selectedDeal != null) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, Util.selectedDeal.getMobile_url());
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this deal out!");
				startActivity(shareIntent);

			}

			return true;

		}
		return super.onOptionsItemSelected(item);	

	}


	public String getTimeStr(long millis)
	{

		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		int hours=calendar.get(Calendar.HOUR);
		if(hours==0)
		{
			hours=12;
		}
		int mins=calendar.get(Calendar.MINUTE);
		String minutes=""+mins;
		if(mins==0){
			minutes="00";
		}
		int amOrPm=calendar.get(Calendar.AM_PM);
		if(amOrPm==1)
			return hours+":"+minutes+" PM";
		else
			return hours+":"+minutes+" AM";
	}
	public String getCreatedTimeStr(long millis)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		int month=calendar.get(Calendar.MONTH);
		int daysOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
		return getDayOfWeek(daysOfWeek)+", "+getMonthName(month)+" "+calendar.get(Calendar.DATE);
	}
	public String getDayOfWeek(int dayOfWeek)
	{
		if(dayOfWeek==1)
		{
			return "Sunday";
		}
		else if(dayOfWeek==2)
		{
			return "Monday";
		}
		else if(dayOfWeek==3)
		{
			return "Tuesday";
		}
		else if(dayOfWeek==4)
		{
			return "Wednesday";
		}
		else if(dayOfWeek==5)
		{
			return "Thursday";
		}
		else if(dayOfWeek==6)
		{
			return "Friday";
		}
		else if(dayOfWeek==7)
		{
			return "SATURDAY";
		}
		return "";
	}
	public String getMonthName(int month)
	{
		if(month==0)
		{
			return "Januray";
		}
		else if(month==1)
		{
			return "February";
		}
		else if(month==2)
		{
			return "March";
		}
		else if(month==3)
		{
			return "April";
		}
		else if(month==4)
		{
			return "May";
		}
		else if(month==5)
		{
			return "June";
		}
		else if(month==6)
		{
			return "July";
		}
		else if(month==7)
		{
			return "August";
		}
		else if(month==8)
		{
			return "September";
		}
		else if(month==9)
		{
			return "October";
		}
		else if(month==10)
		{
			return "November";
		}
		else if(month==11)
		{
			return "December";
		}
		return "";
	}
}
