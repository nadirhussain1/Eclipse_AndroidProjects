package com.mobive.dealme;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.humby.dealular.R;
import com.mobive.adapters.DealMeAdapter;
import com.mobive.bean.Deal;
import com.mobive.net.GetRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class Home_Activity extends SherlockActivity {
	int index=20;
	ListView dealsListView=null;
	String baseUrl= "http://api.yipit.com/v1/deals";
	ProgressDialog progressDialog=null;
	String nextURL="";
	boolean inProgress=false;
	DealMeAdapter adapter=null;
	String city,tag;
	ArrayList<Deal> deals=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		DataUtility.setContext(this);
		DataUtility.lastVisitedActivity=DealMeConstants.HOME_ACTIVITY_ID;
		dealsListView=(ListView)findViewById(R.id.dealListView);
		dealsListView.setOnScrollListener(new ListScroll());

		city=DealMePreferences.getInstance().getSelectedCity();
		tag=DealMePreferences.getInstance().getSelectedDeals();
		DataUtility.getUser().setDeals(DealMePreferences.getInstance().getUserSelectedDealsTitles());

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.sherlock_spinner_item,DataUtility.getUser().getDeals());
		listAdapter.setDropDownViewResource(R.layout.list_layout);
		/** Setting dropdown items and item navigation listener for the actionbar */
		getSupportActionBar().setNavigationMode(getSupportActionBar().NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(listAdapter, navigationListener);        

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String url="";
		if (!city.equalsIgnoreCase("") && !tag.equalsIgnoreCase("")) {
			url = baseUrl + "/?" + "key=" + DealMeConstants.YIPIT_API_KEY + "&format=json&division="
					+ city + "&tag=" + tag;
		} else {
			url = baseUrl + "/?" + "key=" + DealMeConstants.YIPIT_API_KEY + "&format=json";
		}

		loadDealsRequest(url);


	}
	private void loadDealsRequest(final String url){
		Log.d("Catgories"," URL Request "+url);
		inProgress=true;
		progressDialog=ProgressDialog.show(this, "", "Loading Deals");
		DataUtility.shouldLoadNextDeals=false;
		Thread getThread=new Thread(new Runnable() {

			@Override
			public void run() {
				new GetRequest(requestListener, url, Home_Activity.this).execute();	
			}
		});
		getThread.run();
	}
	OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if(itemPosition>0){
				String title=DataUtility.getUser().getDeals().get(itemPosition);
				DataUtility.selectedDealSlug=findSlugAgainstTitle(title);
				tag=DataUtility.selectedDealSlug;
				Log.d("DEBUG","Navigation slug click "+tag);
				String url = baseUrl + "/?" + "key=" + DealMeConstants.YIPIT_API_KEY + "&format=json&division="+ city + "&tag=" + tag;
				dealsListView.setAdapter(null);
				adapter=null;
				loadDealsRequest(url);

				return true;
			}
			return false;
		}
	};
	private String  findSlugAgainstTitle(String title){
		String slug="";
		for(int count=0;count<DataUtility.getItems().size();count++){
			if(title.equalsIgnoreCase(DataUtility.getItems().get(count).getTitle())){
				slug=DataUtility.getItems().get(count).getSlug();
				break;
			}
		}
		return slug;
	}
	class ListScroll implements OnScrollListener
	{
		int currentScrollState=0;
		int currentVisibleItemCount=0;
		int currentFirstVisibleItem=0;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			this.currentScrollState = scrollState;

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
		{
			this.currentFirstVisibleItem = firstVisibleItem;
			this.currentVisibleItemCount = visibleItemCount;
			if ((firstVisibleItem + visibleItemCount == totalItemCount) && totalItemCount!=0 && visibleItemCount!=0 && firstVisibleItem!=0) {

				if (!inProgress && DataUtility.shouldLoadNextDeals && nextURL!=null && !nextURL.equalsIgnoreCase("")) {
					inProgress = true;
					loadDealsRequest(nextURL);

				}
			}

		}
	};
	@Override
	public void onBackPressed() {	
		File dir = new File(Environment.getExternalStorageDirectory(),"DealMe");
		deleteCoveroidDirectory(dir);
		super.onBackPressed();
	}
	public static boolean  deleteCoveroidDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteCoveroidDirectory(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main_menu, menu);		
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.menu_refresh:
			String url=baseUrl+"/?"+"key="+DealMeConstants.YIPIT_API_KEY+"&format=json&division="+city+"&tag="+tag;

			if(inProgress==false)
			{
				inProgress=true;
				loadDealsRequest(url);
			}
			return true;


		case R.id.menu_item_1:

			Intent intent = new Intent(Home_Activity.this, Settings_Activity.class);
			startActivity(intent);

			return true;
		case android.R.id.home:

			return true;

		}
		return super.onOptionsItemSelected(item);
	}


	RequestListener requestListener=new RequestListener() {

		@Override
		public void onSuccess(InputStream inputStream) {
			progressDialog.dismiss();
			inProgress=false;
			String jsonStr=Util.ConvertToJson(inputStream);
			JSONObject jsonObject=null;
			JSONArray jsonArray=null;
			try {
				jsonObject = new JSONObject(jsonStr);
				JSONObject metaObj=jsonObject.getJSONObject("meta");
				if(metaObj.has("next"))
				{
					nextURL=metaObj.getString("next");
				}
				jsonObject=jsonObject.getJSONObject("response");
				jsonArray=jsonObject.getJSONArray("deals");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(jsonArray==null)
			{
				Toast.makeText(Home_Activity.this, "Array null", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Gson gson=new Gson();
				java.lang.reflect.Type listType = new TypeToken<List<Deal>>() {}.getType();
				deals=gson.fromJson(jsonArray.toString(),listType);
				DataUtility.shouldLoadNextDeals=false;
				Log.d("Catgories","Total Deals Received "+deals.size());
				if(adapter==null){
					adapter=new DealMeAdapter(Home_Activity.this, deals);
					dealsListView.setAdapter(adapter);

				}else if(deals.size()>0){
					Log.d("Catgories","New Next Deals "+deals.size());
					adapter.getDeals().addAll(deals);
					adapter.notifyDataSetChanged();

				}
			}

		}

		@Override
		public void onFail(String message) {
			progressDialog.dismiss();
			inProgress=false;
			Log.i("JSON_Result","onFail "+message);
		}
	};




}
