package br.com.dinamo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.custom.screens.VisualizeDialog;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class BuyEventDetailsActivity extends Activity {
	
	private  Button    visualizePhotoButton=null;
	private  int       selectedItemPosition=0;
	private  TextView  dateTextView=null;
	private  TextView  paymentMethodValue=null;
	private  TextView  establishValueView=null;
	private  TextView  notesTextView=null;
	private  TextView  priceTextView=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		selectedItemPosition=getIntent().getIntExtra("POSITION",0);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.buy_event_details_layout, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);

		SharedData.getInstance().sendScreenName("Buy Event Detail Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
		displayRealData();
	}
	private void displayRealData(){
		Date date=SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getDate();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		String formatedDateString=""+day+"/"+(monthOfYear+1)+"/"+year;
		
		String priceValue=String.format("%.2f", SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceTextView.setText(priceValue);
		
		dateTextView.setText(""+formatedDateString);
		paymentMethodValue.setText(""+SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getPayMethod().getName());
		establishValueView.setText(""+SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getEstablishment().getName());
		notesTextView.setText(""+SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getNotes());
		
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.buyEventDetailsLable);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceTextView=(TextView)findViewById(R.id.ValorValueTextView);
		SharedData.getInstance().applyFontToTextView(priceTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.payDayStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.paymentDayValueTextView);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.payMethodStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		paymentMethodValue=(TextView)findViewById(R.id.paymentMethodValueTextView);
		SharedData.getInstance().applyFontToTextView(paymentMethodValue, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		establishValueView=(TextView)findViewById(R.id.establishNameTextView);
		SharedData.getInstance().applyFontToTextView(establishValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.establishStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		notesTextView=(TextView)findViewById(R.id.notesTextView);
		SharedData.getInstance().applyFontToTextView(notesTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		visualizePhotoButton=(Button)findViewById(R.id.visualizePhotoButton);
		SharedData.getInstance().applyFontToTextView(visualizePhotoButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		View backIconImage=(View)findViewById(R.id.backClickArea);
		ImageView editIconImage=(ImageView)findViewById(R.id.editIcon);
		backIconImage.setOnClickListener(backIconClickListener);
		editIconImage.setOnClickListener(editActivityLauncher);
		visualizePhotoButton.setOnClickListener(visualizePhotoClickListener);
	}
   OnClickListener visualizePhotoClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("View Cupom Button");
			VisualizeDialog dialog=new VisualizeDialog(SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getProductPhoto());
			dialog.showDialog();		
		}
	};
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnClickListener editActivityLauncher=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Edit Buy Event");
			Intent mainIntent = new Intent(BuyEventDetailsActivity.this, AddBuyEventActivity.class);
			mainIntent.putExtra("ID", DinamoConstants.EDIT_EVENT_ACTIVITY_ID);
			mainIntent.putExtra("POSITION", selectedItemPosition);
			startActivity(mainIntent);
			finish();

		}
	};
}
