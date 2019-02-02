package com.mobive.dealme;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.humby.dealular.R;
import com.mobive.alert_keywords.KeywordItem;
import com.mobive.alert_keywords.KeywordItemAdapter;
import com.mobive.bean.Response;
import com.mobive.net.PostRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class Screen4_Activity extends SherlockActivity {

	private static ArrayList<KeywordItem> items = new ArrayList<KeywordItem>();
	private static KeywordItemAdapter adapter = null;
	private static ListView dynamicList = null;
	private static Context context = null;
	private EditText textbox = null;
	ProgressDialog progressDialog=null;
	String baseUrlGetUser = "http://localhost:8080/DealMe/KeyWord/save";
	String baseUrlKeywords = "http://54.225.83.224:8080/DealMe/KeyWord/save";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen4_content);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		context = this;
		
		dynamicList = ((ListView)findViewById(R.id.ListView1));
		updateAdapter();
		
		textbox = (EditText)findViewById(R.id.textbox);
		
		((Button)findViewById(R.id.add_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				items.add(new KeywordItem(textbox.getText().toString()));
				JSONObject json=new JSONObject();
				
			    try {
			    	int userId=DataUtility.getUser().getId();
			    	json.put("keyword", textbox.getText().toString());
					json.put("user", userId);
					
//					json.put("keyword", "thsiskeyword");
//					json.put("user", 4);
					Log.i("keyword and user",textbox.getText().toString()+ " User "+userId);	
				} catch (JSONException e) {
					e.printStackTrace();
				}
				new PostRequest(baseUrlKeywords, json, new  RequestListener() {
					
				
					public void onSuccess(InputStream inputStream) {
			//{"payload":{"user":{"id":2,"name":"fhh@vh.com","email":"fhh@vh.com","enabled":true,"keywords":null,"deals":null}},"metadata":{"status":"SUCCESS","message":"user saved Successfully!"}}
						Gson gson=new Gson();
						Type listType = new TypeToken<List<Response>>() {}.getType();
//						Log.i("KeyWords", Util.ConvertToJson(inputStream));
						Log.i("KeyWordResponse", Util.ConvertToJson(inputStream)+" ");
						Response response=gson.fromJson(Util.ConvertToJson(inputStream), Response.class);
						
//						Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//						startActivity(intent);
//						overridePendingTransition(0,0); //0 for no animation
//						finish();		
//						progressDialog.cancel();
					}
					
				
					public void onFail(String message) {
						// TODO Auto-generated method stub
//						progressDialog.cancel();
						Log.i("error",message);
						Toast.makeText(Screen4_Activity.this, "There was an error", Toast.LENGTH_SHORT).show();
					}
				}).execute();
				updateAdapter();
				textbox.setText("");
			}
		});
		
		//create alert dialog
		{
			//prepare the alert dialog view and register checkbox listeners
			View dialogLayout = View.inflate(this, R.layout.screen4_popup, null);
			((CheckBox)dialogLayout.findViewById(R.id.checkbox_device_notification)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//TODO:
				}
			});
			((CheckBox)dialogLayout.findViewById(R.id.checkbox_email_message)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//TODO:
				}
			});
			
			//init alsert dialog
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
				.setTitle("Alerts!")
				.setView(dialogLayout)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();	//current curent activity
							}
						});

			// create alert dialog and show it
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
//		Location location=DealMeLocationManager.getInstance(getApplicationContext()).getLocation();
//		if(location!=null)
//		{
//			Toast.makeText(context, "Not nUll", Toast.LENGTH_SHORT).show();
//		}
	}

	
	
	public static void updateAdapter() {
		adapter = new KeywordItemAdapter(context, items);
		dynamicList.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.screen4_actionbar);
		
		((ImageView)findViewById(R.id.btn_prev)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Screen3_Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0,0); //0 for no animation
				finish();
			}
		});
		((ImageView)findViewById(R.id.btn_next)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0,0); //0 for no animation
				finish();
			}
		});
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.btn_prev:
//			// left button, do something
//			return true;
//		case R.id.screen_name:
//			// center button
//			return true;
//		case R.id.btn_next:
//			// right button
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}
