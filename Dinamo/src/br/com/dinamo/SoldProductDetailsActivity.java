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
import android.widget.ImageView;
import android.widget.TextView;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class SoldProductDetailsActivity extends Activity {
	private  int       selectedItemPosition=0;
	private  TextView  dateTextView=null;
	private  TextView  paymentMethodValue=null;
	private  TextView  ProductValueView=null;
	private  TextView  quantityTextView=null;
	private  TextView  priceTextView=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		selectedItemPosition=getIntent().getIntExtra("POSITION",0);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.sold_product_details, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);

		SharedData.getInstance().sendScreenName("Sell Event Detail Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
		displayRealData();
	}
	
	private void displayRealData(){
		Date date=SharedData.getInstance().soldEventsList.get(selectedItemPosition).getDate();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		String formatedDateString=""+day+"/"+(monthOfYear+1)+"/"+year;
		
		String priceValue=String.format("%.2f", SharedData.getInstance().soldEventsList.get(selectedItemPosition).getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceTextView.setText(priceValue);
	
		
		quantityTextView.setText(""+SharedData.getInstance().soldEventsList.get(selectedItemPosition).getQuantity());
		dateTextView.setText(""+formatedDateString);
		paymentMethodValue.setText(""+SharedData.getInstance().soldEventsList.get(selectedItemPosition).getPayMethod().getName());
		ProductValueView.setText(""+SharedData.getInstance().soldEventsList.get(selectedItemPosition).getProduct().getName());
		
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.soldProductDetailsLable);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceTextView=(TextView)findViewById(R.id.ValorValueTextView);
		SharedData.getInstance().applyFontToTextView(priceTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.quantityStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		quantityTextView=(TextView)findViewById(R.id.quantityValueTextView);
		SharedData.getInstance().applyFontToTextView(quantityTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.productStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		ProductValueView=(TextView)findViewById(R.id.productValueTextView);
		SharedData.getInstance().applyFontToTextView(ProductValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.receiptStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.receiptValueTextView);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.payMethodStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		paymentMethodValue=(TextView)findViewById(R.id.payMethodValueTextView);
		SharedData.getInstance().applyFontToTextView(paymentMethodValue, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		View backIconImage=(View)findViewById(R.id.backClickArea);
		ImageView editIconImage=(ImageView)findViewById(R.id.editIcon);
		backIconImage.setOnClickListener(backIconClickListener);
		editIconImage.setOnClickListener(editActivityLauncher);
	}
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnClickListener editActivityLauncher=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Edit Sell Event Button");
			Intent mainIntent = new Intent(SoldProductDetailsActivity.this, AddSellProductActivity.class);
			mainIntent.putExtra("ID", DinamoConstants.EDIT_EVENT_ACTIVITY_ID);
			mainIntent.putExtra("POSITION", selectedItemPosition);
			startActivity(mainIntent);
			finish();
			
		}
	};
}
