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
import br.com.adapters.SellEventListAdapter;
import br.com.data.model.SoldProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class SellEventsMainActivity extends Activity{
	public  static SellEventListAdapter listAdapter=null;
	private boolean hideAddOption=false;
	private String date = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		hideAddOption=getIntent().getBooleanExtra("HideAdd", false);
		date = getIntent().getStringExtra("date");

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View sellEventsView = inflater.inflate(R.layout.sell_event_main_layout, null, false);
		ScalingUtility.getInstance().scaleView(sellEventsView);
		setContentView(sellEventsView);

		SharedData.getInstance().sendScreenName("Sell Events Main Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.sellEventsLableTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.addSellEventButton);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		Button addSellEventButton=(Button)findViewById(R.id.addSellEventButton);
		View backIconImage=(View)findViewById(R.id.backClickArea);
		ListView sellEventsListView=(ListView)findViewById(R.id.soldEventsListView);
		listAdapter=new SellEventListAdapter();
		sellEventsListView.setAdapter(listAdapter);
		sellEventsListView.setOnItemClickListener(buyItemClickListener);
		backIconImage.setOnClickListener(backIconClickListener);

		if(hideAddOption){
			addSellEventButton.setVisibility(View.GONE);
		}else{
			addSellEventButton.setOnClickListener(addSellEventListener);
		}
	}
	public static void refreshAdapter(boolean isDeleted){
		if(isDeleted){
			for(int count=0;count<UserDataManager.getInstance().soldEventsList.size();count++){
				SoldProduct clone=UserDataManager.getInstance().soldEventsList.get(count);
				if(clone.isDeleted){
					UserDataManager.getInstance().soldEventsList.remove(count);
					break;
				}
			}
		}
		listAdapter.refreshDataAdapter();

	}
	
	OnClickListener addSellEventListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			SharedData.getInstance().sendEventNameToAnalytics("Add Sell Event Button 1");
			Intent mainIntent = new Intent(SellEventsMainActivity.this, AddSellProductActivity.class);
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
			SharedData.getInstance().sendEventNameToAnalytics("Sell Event Detail Button");

			Intent mainIntent = new Intent(SellEventsMainActivity.this, SoldProductDetailsActivity.class);
			mainIntent.putExtra("POSITION",position);
			startActivity(mainIntent);
		}
	};
}
