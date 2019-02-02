package com.mobive.dealme;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.humby.dealular.R;
import com.mobive.bean.City;
import com.mobive.bean.Division;
import com.mobive.bean.Response;
import com.mobive.bean.User;
import com.mobive.dealme.deal_items.EntryItem;
import com.mobive.dealme.deal_items.Item;
import com.mobive.dealme.deal_items.SectionItem;
import com.mobive.net.GetRequest;
import com.mobive.net.PostRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class Screen1_Activity extends SherlockActivity {
	private AlertDialog alertDialog = null;
	ArrayList<City> cities;
	CharSequence[] items ;
	CharSequence[] items_slug ;
	int length;
	AlertDialog.Builder builder;
	ArrayList<Division> divisions=null;
	ProgressDialog progressDialog=null;
	ImageView loginBtn=null;
	View activityOneParentView=null;
    String enteredPassword=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityOneParentView=View.inflate(this, R.layout.screen1_content, null);
		setContentView(activityOneParentView);
		populateCatgoriesTitlesAndSlugs();
		
		DataUtility.setContext(this);
		DataUtility.setSelectedCitySlug("new-york");
        checkIfAlreadySignedUp();
        
		loginBtn=(ImageView)findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(loginButtonClickListener);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		builder= new AlertDialog.Builder(this);
		((TextView)findViewById(R.id.city_name)).setTextSize(15);
		((RelativeLayout)findViewById(R.id.city_chooser)).setOnClickListener(citySelectionListListener);
	
	}

	OnClickListener loginButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			View loginPopUp = View.inflate(DataUtility.getContext(), R.layout.login_pop_up, null);
			final EditText emailText=(EditText)loginPopUp.findViewById(R.id.email);
			final EditText passText=(EditText)loginPopUp.findViewById(R.id.password);
			
			ScalingUtility.getInstance((Activity)DataUtility.getContext()).scaleView(loginPopUp);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder
            .setTitle("Log In")
			.setView(loginPopUp)
			.setCancelable(false)
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					logUser(emailText.getText().toString(),passText.getText().toString());
					dialog.cancel();	//current curent activity
				}
			});
			alertDialogBuilder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();	//current curent activity
				}
			});

			AlertDialog logInAlert = alertDialogBuilder.create();
			logInAlert.show();


		}
	};
	private void logUser(String email,String password){
		if(DataUtility.isValidEmail(email)){
			
			JSONObject json=new JSONObject();
			try {
				json.put("email", email);
				json.put("password", password);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			enteredPassword=password;
			new PostRequest(DealMeConstants.baseURLLogin, json,logUserToHomeScreenListener).execute();
			progressDialog=ProgressDialog.show(Screen1_Activity.this, "", "Logging In");
			
		}else{
			Toast.makeText(this,"Your Email and Password are not correct.", Toast.LENGTH_SHORT).show();
		}
	}
	OnClickListener citySelectionListListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
            if(alertDialog!=null){
			     alertDialog.show();
            }else{
            	Toast.makeText(Screen1_Activity.this,"Check your Internet connection.", Toast.LENGTH_SHORT).show();
            }
		}
	};
	OnClickListener nextScreenListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
            DealMePreferences.getInstance().saveSelectedCity(DataUtility.selectedCitySlug);
			Intent intent = new Intent(getApplicationContext(), Screen2_Activity.class);
			intent.putExtra("Login", false);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0,0); //0 for no animation
			finish();
		}
	};
	public void checkIfAlreadySignedUp()
	{
		boolean isAlreadySgnedUp=DealMePreferences.getInstance().getSignedUpStatus();
		if(isAlreadySgnedUp)
		{
			String email=DealMePreferences.getInstance().getUserEmail();
			String password=DealMePreferences.getInstance().getUserPassword();
            logUser(email, password);
		}
		else
		{
			String url=DealMeConstants.YIPIT_BASE_URL+"/?"+"key="+DealMeConstants.YIPIT_API_KEY+"&limit=200"+"&format=json";
			progressDialog=ProgressDialog.show(this, "", "Loading Cities");
			new GetRequest(loadCitiesRequestListener, url, this).execute();
		}
	}
	RequestListener logUserToHomeScreenListener=new  RequestListener() {


		public void onSuccess(InputStream inputStream) {
			
			String result=Util.ConvertToJson(inputStream);
			Response response=null;
			response = Util.parseResponse(result);	
			if(response!=null){
				User user=response.getPayload().getUser();
				DataUtility.setUser(user);
				DealMePreferences.getInstance().saveUserEmailAndPassword(user.getEmail(),enteredPassword);
                DealMePreferences.getInstance().saveSignUpStatus(true);
                DealMePreferences.getInstance().saveSelectedCity(user.getCity());
                
				Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0,0); //0 for no animation
				finish();		
				progressDialog.cancel();
			}else{
				String message="There was an error.Check Internet Connection.";
				try {
				     JSONObject responseJSON=new JSONObject(result);
                      message=responseJSON.getJSONObject("metadata").getString("message");
			     } catch (JSONException e) {
				// TODO Auto-generated catch block
				    e.printStackTrace();
			     }
				progressDialog.cancel();
			    Toast.makeText(Screen1_Activity.this,message, Toast.LENGTH_SHORT).show();
			}
		}


		public void onFail(String message) {
			// TODO Auto-generated method stub
			progressDialog.cancel();
			Toast.makeText(Screen1_Activity.this, "You can't be logged in. Check Internet Connection", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.screen1_actionbar);

		((ImageView)findViewById(R.id.btn_next)).setOnClickListener(nextScreenListener);

		return true;
	}
	RequestListener loadCitiesRequestListener=new RequestListener() {

		@Override
		public void onSuccess(InputStream inputStream) {
			progressDialog.dismiss();
			String jsonStr=Util.ConvertToJson(inputStream);
			JSONObject jsonObject=null;
			JSONArray jsonArray=null;
			try {
				jsonObject = new JSONObject(jsonStr);

				jsonObject=jsonObject.getJSONObject("response");

				jsonArray=jsonObject.getJSONArray("divisions");
				Gson gson=new Gson();
				jsonArray.length();

				java.lang.reflect.Type listType = new TypeToken<List<Division>>() {}.getType();
				divisions=gson.fromJson(jsonArray.toString(), listType);
				items= new String[divisions.size()];
				items_slug= new String[divisions.size()];
				Log.i("devisionzero",divisions.get(0).getName()+" ");
				for(int i=0;i<divisions.size();i++)
				{

					items[i]=divisions.get(i).getName();
					items_slug[i]=divisions.get(i).getSlug();
					Log.i("item", items[i]+"");
				}
				//				Toast.makeText(Screen1_Activity.this, ""+divisions.size(), Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			builder.setTitle("Select Your City:");	
			builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int item) {
					((TextView)findViewById(R.id.city_name)).setText(items[item]);	//change the city name text valu
					DataUtility.setSelectedCitySlug(""+items_slug[item]);
					dialogInterface.dismiss();
				}
			});
			alertDialog = builder.create();

		}

		@Override
		public void onFail(String message) {
			progressDialog.dismiss();
			Log.i("JSON_Result","onFail "+message);
		}
	};
	public void getLocationOfUser()
	{

		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(Screen1_Activity.this, "Disabled"+provider, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLocationChanged(Location location) {
				Toast.makeText(Screen1_Activity.this, location.getLongitude()+" "+location.getLatitude(), Toast.LENGTH_SHORT).show();
			}
		});
		//	 Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	}
	private void populateCatgoriesTitlesAndSlugs(){
		ArrayList<Item>items=new ArrayList<Item>();
		items.add(new SectionItem("ACTIVITIES & ADVENTURES","activities-adventures"));
		items.add(new EntryItem("Bowling","bowling"));
		items.add(new EntryItem("City Tours","city-tours"));
		items.add(new EntryItem("Comedy Clubs","comedy-clubs"));
		items.add(new EntryItem("Concerts","concerts"));
		items.add(new EntryItem("Dance Classes","dance-classes"));
		items.add(new EntryItem("Golf","golf"));
		items.add(new EntryItem("Life Skills Classes","life-skills-classes"));
		items.add(new EntryItem("Museums","museums"));
		items.add(new EntryItem("Outdoor Adventures","outdoor-adventures"));
		items.add(new EntryItem("Theater","theate"));
		items.add(new EntryItem("Skiing","skiing"));
		items.add(new EntryItem("Skydiving","skydiving"));
		items.add(new EntryItem("Sporting Events","sporting-events"));
		items.add(new EntryItem("Wine Tasting","wine-tasting"));
		items.add(new SectionItem("DINING & NIGHTLIFE","dining-nightlife"));
		items.add(new EntryItem("Bar Club","bar-club"));
		items.add(new EntryItem("Restaurants","restaurants"));
		items.add(new SectionItem("FITNESS","fitness"));
		items.add(new EntryItem("Boot Camp","boot-camp"));
		items.add(new EntryItem("Fitness Classes","fitness-classes"));
		items.add(new EntryItem("Gym","gym"));
		items.add(new EntryItem("Martial Arts","martial-arts"));
		items.add(new EntryItem("Personal Training","personal-training"));
		items.add(new EntryItem("Pilates","pilates"));
		items.add(new EntryItem("Yoga","yoga"));



		items.add(new SectionItem("HEALTH & BEAUTY","health-beauty"));

		items.add(new EntryItem("Chiropractic","chiropractic"));
		items.add(new EntryItem("Dental","denta"));
		items.add(new EntryItem("Dermatology","dermatology"));
		items.add(new EntryItem("Eye Vision","eye-vision"));
		items.add(new EntryItem("Facial","facial"));
		items.add(new EntryItem("Hair Removal","hair-removal"));
		items.add(new EntryItem("Hair Salon","hair-salon"));
		items.add(new EntryItem("Makeup","makeup"));
		items.add(new EntryItem("Manicure Pedicure","manicure-pedicure"));
		items.add(new EntryItem("Massage","massage"));
		items.add(new EntryItem("Spa","spa"));
		items.add(new EntryItem("Tanning","tanning"));
		items.add(new EntryItem("Teeth Whitening","teeth-whitening"));

		items.add(new SectionItem("RETAIL & SERVICES","retail-services"));
		items.add(new EntryItem("Automotive Services","automotive-services"));
		items.add(new EntryItem("Food Grocery","food-grocery"));
		items.add(new EntryItem("Home Services","home-services"));
		items.add(new EntryItem("Mens Clothing","mens-clothing"));
		items.add(new EntryItem("Photography Services","photography-services"));
		items.add(new EntryItem("Treats","treats"));
		items.add(new EntryItem("Womens Clothing","womens-clothing"));

		items.add(new SectionItem("OTHERS","others"));
		items.add(new EntryItem("Baby","baby"));
		items.add(new EntryItem("College","college"));
		items.add(new EntryItem("Pets","pets"));
		items.add(new EntryItem("Gay","gay"));
		items.add(new EntryItem("Kids","kids"));
		items.add(new EntryItem("Bridal","bridal"));
		items.add(new EntryItem("Travel","travel"));
		items.add(new EntryItem("Jewish","jewish"));
		
		DataUtility.setItems(items);
	}
}
