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

public class ExpenseDetailsActivity extends Activity{
	private  TextView  dateTextView=null;
	private  TextView  categoryTextView=null;
	private  TextView  PeriodicityTextView=null;
	private  TextView  priceTextView=null;
	private  int       selectedItemPosition=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		selectedItemPosition=getIntent().getIntExtra("POSITION",0);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.expenses_detail, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);

		SharedData.getInstance().sendScreenName("Expense Event Detail Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
		displayRealData();
	}
	private void displayRealData(){
		Date date=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getDate();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		String formatedDateString=""+day+"/"+(monthOfYear+1)+"/"+year;
		
		String priceValue=String.format("%.2f", SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceTextView.setText(priceValue);
			
		dateTextView.setText(""+formatedDateString);
		categoryTextView.setText(""+SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getCategory().getName());
		PeriodicityTextView.setText(""+SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getPeriodicityType().getName());
		
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.expenseDetailsTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceTextView=(TextView)findViewById(R.id.ValorValueTextView);
		SharedData.getInstance().applyFontToTextView(priceTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.catgoriesStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		categoryTextView=(TextView)findViewById(R.id.catagoriesValueTextView);
		SharedData.getInstance().applyFontToTextView(categoryTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.dueDateStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.dueDateValueTextView);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.PeriodicityStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		PeriodicityTextView=(TextView)findViewById(R.id.periodicityValueTextView);
		SharedData.getInstance().applyFontToTextView(PeriodicityTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
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
			SharedData.getInstance().sendEventNameToAnalytics("Edit Expense Event Button");
			Intent mainIntent = new Intent(ExpenseDetailsActivity.this, AddExpenseActivity.class);
			mainIntent.putExtra("ID", DinamoConstants.EDIT_EVENT_ACTIVITY_ID);
			mainIntent.putExtra("POSITION", selectedItemPosition);
			startActivity(mainIntent);
			finish();
			
		}
	};
}
