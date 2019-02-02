package com.mobive.dealme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.humby.dealular.R;

public class Screen3_Activity extends SherlockActivity {

	MyDealsSelectorView selector=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataUtility.setContext(this);
		selector=new MyDealsSelectorView();
		setContentView(selector.getDealsSelectorView());
		

		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.screen3_actionbar);

		((ImageView)findViewById(R.id.btn_prev)).setEnabled(false);
		((ImageView)findViewById(R.id.btn_prev)).setClickable(false);
		((ImageView)findViewById(R.id.btn_next)).setOnClickListener(nextButtonClickListener);

		return true;
	}
	
	OnClickListener nextButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {

			boolean isSaved=selector.saveSelectedDealsSettings();
			if(isSaved){

				Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0,0); //0 for no animation
				finish();
			} 
		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	

}
