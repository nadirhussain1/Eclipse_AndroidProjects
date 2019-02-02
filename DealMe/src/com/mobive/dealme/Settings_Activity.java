

package com.mobive.dealme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.humby.dealular.R;

public class Settings_Activity extends SherlockFragmentActivity implements TabListener,OnPageChangeListener {
	private static final String KEY_POSITION="position";
	private int lastVisitedTab=0;
	private ViewPager pager=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataUtility.setContext(this);
		setContentView(R.layout.main);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setTitle("Settings");


		pager=(ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new SampleAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(this);

		ActionBar bar=getSupportActionBar();
		bar.setHomeButtonEnabled(true);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setHomeButtonEnabled(true);
		bar.setIcon(R.drawable.back);

		for (int i=0; i < 3; i++) {
			if(i==0){
				bar.addTab(bar.newTab()
						.setText("My Deals")
						.setTabListener(this).setTag(i));

			}
			if(i==1){
				bar.addTab(bar.newTab()
						.setText("My Account")
						.setTabListener(this).setTag(i));
			}
			if(i==2){
				bar.addTab(bar.newTab()
						.setText("ABOUT")
						.setTabListener(this).setTag(i));
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId) {

		case android.R.id.home:

			if(lastVisitedTab==0 ){
				checkDealsSettings(-1);	
			}else{
				finish();
			}

		}
		return true;	

	}


	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		pager.setCurrentItem(state.getInt(KEY_POSITION));
		lastVisitedTab=state.getInt("lastVisitedTab");
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

		state.putInt(KEY_POSITION, pager.getCurrentItem());
		state.putInt("lastVisitedTab", lastVisitedTab);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Integer position=(Integer)tab.getTag();
		if(lastVisitedTab==0 && position!=0){
			checkDealsSettings(position);		
		}else{
			lastVisitedTab=position;
			pager.setCurrentItem(position);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// no-op
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// no-op
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// no-op
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// no-op
	}

	@Override
	public void onPageSelected(int position) {

		getSupportActionBar().setSelectedNavigationItem(position);

	}
	@Override
	public void onBackPressed() {
		if(lastVisitedTab==0 ){
			checkDealsSettings(-1);	
		}else{
			finish();
		}
	};
	private void checkDealsSettings(final int position){
		final MyDealsSelectorView selector=DataUtility.selector;
		if(selector!=null && selector.adapter.getHasUserChangedSettings()){
			selector.adapter.setHasUserChangedSettings(false);
			View mydealsView = View.inflate(DataUtility.getContext(), R.layout.deals_settings_pop, null);	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder.setTitle("Save Changes");
			alertDialogBuilder.setView(mydealsView);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					selector.saveSelectedDealsSettings();
					dialog.cancel();
					Toast.makeText(DataUtility.getContext(),"Changes Will take affect from next launch", Toast.LENGTH_SHORT).show();
					if(position==-1){
						((Activity)DataUtility.getContext()).finish();
					}else{
						lastVisitedTab=position;
						pager.setCurrentItem(position);
					}
				}

			});
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					if(position==-1){
						((Activity)DataUtility.getContext()).finish();
					}else{
						lastVisitedTab=position;
						pager.setCurrentItem(position);
					}
				}
			});
			AlertDialog editingDialog = alertDialogBuilder.create();
			editingDialog.show();
		}else{
			if(position==-1){
				((Activity)DataUtility.getContext()).finish();
			}else{
				lastVisitedTab=position;
				pager.setCurrentItem(position);
			}
		}
	}
}