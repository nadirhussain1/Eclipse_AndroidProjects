package com.mobive.dealme;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.humby.dealular.R;
import com.mobive.bean.Response;
import com.mobive.bean.User;
import com.mobive.net.PostRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class Screen2_Activity extends SherlockActivity {

	EditText emailEditText=null;
	EditText passEditText=null;
	ProgressDialog dialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2_content);
		DataUtility.setContext(this);
		emailEditText=(EditText)findViewById(R.id.email);
		passEditText=(EditText)findViewById(R.id.password);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.screen2_actionbar);

		((ImageView)findViewById(R.id.btn_prev)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Screen1_Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0,0); //0 for no animation
				finish();
			}
		});

		((ImageView)findViewById(R.id.btn_next)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = ((EditText)findViewById(R.id.email)).getText().toString();
				String password = ((EditText)findViewById(R.id.password)).getText().toString();
				SharedPreferences pref = Screen2_Activity.this.getSharedPreferences("DealsUrl", Context.MODE_PRIVATE);

				if (DataUtility.isValidEmail(email))
				{			//check for valid email
					if (password.length() >= 7) {		//check for valid password

						JSONObject json=new JSONObject();
						try {
							json.put("email", emailEditText.getText().toString());
							json.put("name", emailEditText.getText().toString());
							json.put("password", passEditText.getText().toString());
							json.put("city", DataUtility.selectedCitySlug);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						new PostRequest(DealMeConstants.baseURLSignUp, json, new  RequestListener() {
							public void onSuccess(InputStream inputStream) {
								
								String singupResponse=Util.ConvertToJson(inputStream);
								Response response=null;
								response = Util.parseResponse(singupResponse);
								if(response!=null)
								{
									User user=response.getPayload().getUser();
									DataUtility.setUser(user);
									DealMePreferences.getInstance().saveUserEmailAndPassword(emailEditText.getText().toString(), passEditText.getText().toString());
									DealMePreferences.getInstance().saveSignUpStatus(true);
									Intent intent = new Intent(getApplicationContext(), Screen3_Activity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
									startActivity(intent);
									overridePendingTransition(0,0); //0 for no animation
									finish();		
									dialog.cancel();
								}
								else
								{
									String message="There was an error.Check Internet Connection.";
									try {
									     JSONObject responseJSON=new JSONObject(singupResponse);
                                          message=responseJSON.getJSONObject("metadata").getString("message");
								     } catch (JSONException e) {
									// TODO Auto-generated catch block
									    e.printStackTrace();
								     }

								    Toast.makeText(Screen2_Activity.this,message, Toast.LENGTH_SHORT).show();
								    dialog.cancel();
								}

							}

							public void onFail(String message) {
								// TODO Auto-generated method stub
								dialog.cancel();
								Toast.makeText(Screen2_Activity.this, "There was an error.Check Internet Connection.", Toast.LENGTH_SHORT).show();
							}
						}).execute();
						dialog=ProgressDialog.show(Screen2_Activity.this, "", "Signing Up");


					} else {
						Toast.makeText(getApplicationContext(), "Password must be at least 7 letters/digits long", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return true;
	}

}
