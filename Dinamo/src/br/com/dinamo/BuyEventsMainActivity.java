package br.com.dinamo;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.adapters.BuyEventListAdapter;
import br.com.data.model.BoughtProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class BuyEventsMainActivity extends Activity{
	public static BuyEventListAdapter listAdapter=null;
	private   boolean hideAddOption=false;
	private String date = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		hideAddOption=getIntent().getBooleanExtra("HideAdd", false);
		date = getIntent().getStringExtra("date");
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.buy_event_main_layout, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);

		SharedData.getInstance().sendScreenName("Buy Events Main Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.boughtEventsLableTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.addbuyEventButton);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		Button addBuyEventButton=(Button)findViewById(R.id.addbuyEventButton);
		View backIconImage=(View)findViewById(R.id.backClickArea);
		ListView buyEventsListView=(ListView)findViewById(R.id.monthlyBoughtListView);
		listAdapter=new BuyEventListAdapter();
		buyEventsListView.setAdapter(listAdapter);
		buyEventsListView.setOnItemClickListener(buyItemClickListener);

		if(hideAddOption){
			addBuyEventButton.setVisibility(View.GONE);	
		}else{
			addBuyEventButton.setOnClickListener(addBuyEventListener);
		}
		backIconImage.setOnClickListener(backIconClickListener);
	}
	public static void refreshAdapter(boolean isDeleted){
		if(isDeleted){
			for(int count=0;count<UserDataManager.getInstance().boughtEventsList.size();count++){
				BoughtProduct clone=UserDataManager.getInstance().boughtEventsList.get(count);
				if(clone.isDeleted){
					UserDataManager.getInstance().boughtEventsList.remove(count);
					break;
				}
			}
		}
		listAdapter.refreshDataOfAdapter();

	}
	OnClickListener addBuyEventListener=new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			SharedData.getInstance().sendEventNameToAnalytics("Add Buy Event Button 1");
			Intent mainIntent = new Intent(BuyEventsMainActivity.this, AddBuyEventActivity.class);
			mainIntent.putExtra("ID", DinamoConstants.ADD_EVENT_ACTIVITY_ID);
			startActivity(mainIntent);
		}
	};
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnItemClickListener buyItemClickListener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			SharedData.getInstance().sendEventNameToAnalytics("Buy Event Detail Button");
			Intent mainIntent = new Intent(BuyEventsMainActivity.this, BuyEventDetailsActivity.class);
			mainIntent.putExtra("POSITION",position);
			startActivity(mainIntent);
		}
	};
}
