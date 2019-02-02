package br.com.dinamo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.custom.screens.DropDownMenuWindow;
import br.com.data.model.User;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.GetRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.storage.DinamoPrefernces;
import br.com.utilities.CNPJUtil;
import br.com.utilities.CPFUtil;
import br.com.utilities.Mask;
import br.com.utilities.ScalingUtility;

public class LogInActivity extends Activity{
	private  View      dropDownInflatedView=null;
	private  ListView  dropDownListView=null;
	private  View      optionDropDownAnchorView=null;
	private  TextView  selectedUserIdType=null;
	private   LayoutInflater inflater=null;
	private EditText userIdEditor=null;
	private EditText passwordEditor=null;
	private Button noAccountButton=null;
	private  TextView forgotPassButton=null;
	private Button enterButton=null;
	private  TextWatcher cpfMask;
	private  TextWatcher cnpjMask;
	private  String[] userIdOptions=new String[]{"CPF","CNPJ"};
	private  int selectedUserIdCat=0;
	DropDownMenuWindow dropDownWindow=null;
	private String userPasswordInput="";
	private String userId="";
	private String plainUserId="";
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		DinamoPrefernces.getInstance(this).saveUserLogInStatus(DinamoConstants.LOG_IN_SCREEN);

		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View signUpView = inflater.inflate(R.layout.login_layout, null, false);
		ScalingUtility.getInstance().scaleView(signUpView);
		setContentView(signUpView);

		applyFontsToTextOnScreen();
		initUserClicksListeners();
		SharedData.getInstance().sendScreenName("LogIn Screen");

		if(getIntent().getExtras() !=null){
			String userId=getIntent().getExtras().getString("UserId");
			plainUserId=removeMask(userId);	
			userPasswordInput=getIntent().getExtras().getString("Password");

			userIdEditor.setText(userId);
			passwordEditor.setText(userPasswordInput);

			String message=getString(R.string.login_process);
			showProgressDialog(message);	
			logInGetRequest();
		}

	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.LoginLabelTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		selectedUserIdType=(TextView)findViewById(R.id.cpfTextView);
		SharedData.getInstance().applyFontToTextView(selectedUserIdType, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		userIdEditor=(EditText)findViewById(R.id.LogInIdEditText);
		SharedData.getInstance().applyFontToTextView(userIdEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		passwordEditor=(EditText)findViewById(R.id.logInPinCodeEditText);
		SharedData.getInstance().applyFontToTextView(passwordEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		enterButton=(Button)findViewById(R.id.logInEnterButton);
		SharedData.getInstance().applyFontToTextView(enterButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		forgotPassButton=(TextView)findViewById(R.id.forgotPassLabelTextView);
		SharedData.getInstance().applyFontToTextView(forgotPassButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		noAccountButton=(Button)findViewById(R.id.NoAccountButton);
		SharedData.getInstance().applyFontToTextView(noAccountButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);

		userId=DinamoPrefernces.getInstance(this).getLastLogUserId();
		userIdEditor.setText(userId);
	}
	private void initUserClicksListeners(){
		dropDownInflatedView=inflater.inflate(R.layout.drop_down_list, null, false);
		dropDownListView=(ListView)dropDownInflatedView.findViewById(R.id.generalMenuList);
		optionDropDownAnchorView=findViewById(R.id.logInIdLayout);

		ImageView userIdOptionsImageView=(ImageView)findViewById(R.id.cpfArrowImageView);

		selectedUserIdType.setOnClickListener(userIdOptionsClickListener);
		userIdOptionsImageView.setOnClickListener(userIdOptionsClickListener);

		enterButton.setOnClickListener(logInClickListener);
		noAccountButton.setOnClickListener(noAccountListener);
		forgotPassButton.setOnClickListener(forgotPassClickListener);

		cpfMask = Mask.insert("###.###.###-##", userIdEditor);
		userIdEditor.addTextChangedListener(cpfMask);

		cnpjMask = Mask.insert("##.###.###/####-##", userIdEditor);


	}
	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private void hideProgressDialog() {
		if(progressDialog!=null){
			progressDialog.hide();
		}
	}

	private void initiateLogInProcess(){
		userId=userIdEditor.getText().toString();
		userPasswordInput=passwordEditor.getText().toString();
		boolean isValid=validateUserId(userId);
		//isValid=true;
		if(userId.trim().length()>0 && userPasswordInput.trim().length()>0 && isValid){
			plainUserId=removeMask(userId);
			verifyUserFromBackEnd();
		}else{
			SharedData.getInstance().displayMessageAlert(getString(R.string.invalid_alert_data_title),getString(R.string.signup_valid_data_alert),false);	
		}
	}
	private void verifyUserFromBackEnd(){
		User user=DinamoDatabaseHelper.getInstance(plainUserId).getUser(plainUserId);
		if(user !=null){
			if(user.getPassword().contentEquals(userPasswordInput)){	
				if(user.isInitFromServer==0 && !SharedData.getInstance().isInternetAvailable()){
					SharedData.getInstance().displayMessageAlert(getString(R.string.connect_alert_title),getString(R.string.login_internet_alert_dialog),true);	
				}else{
					DinamoPrefernces.getInstance(LogInActivity.this).saveLastSyncTime(System.currentTimeMillis());
					launchMainScreen(user);
				}	
			}else{
				SharedData.getInstance().displayMessageAlert(getString(R.string.connect_alert_title),getString(R.string.incorrect_password_alert_message),false);
			}
		}else if(SharedData.getInstance().isInternetAvailable()){
			logInGetRequest();
		}else{
			SharedData.getInstance().displayMessageAlert(getString(R.string.connect_alert_title),getString(R.string.no_offline_record_alert_message),true);		
		}

	}
	private void logInGetRequest(){
		GetRequest getRequest=new GetRequest(logInGetRequestHandler,DinamoConstants.LOG_IN_URL+plainUserId,-1);
		SharedData.executeTask(getRequest);
	}
	private String removeMask(String userId){
		userId=userId.replace(".","");
		userId=userId.replace("-","");
		userId=userId.replace("/","");

		return userId;
	}
	private boolean validateUserId(String userId){
		if(selectedUserIdCat==0){
			return CPFUtil.validate(userId);
		}else{
			return CNPJUtil.validate(userId);
		}
	}
	private void launchSignUpScreen(){
		Intent mainIntent = new Intent(LogInActivity.this, SignUpActivity.class);
		startActivity(mainIntent);
		LogInActivity.this.finish();	
	}
	private void launchMainScreen(User user){
		SharedData.getInstance().sendEventNameToAnalytics("Login Button");
		SharedData.getInstance().currentUser=user;
		DinamoPrefernces.getInstance(this).saveLastLogUserId(userId);
		DinamoPrefernces.getInstance(this).saveCurrentUser(user);

		Intent mainIntent = new Intent(LogInActivity.this, MainScreenActivity.class);
		startActivity(mainIntent);
		LogInActivity.this.finish();	
	}
	private void displayEmailAlert(){
		new AlertDialog.Builder(this).setMessage(R.string.retrieve_pass_alert_msg) 
		.setTitle(getString(R.string.pass_alert_title)) 
		.setCancelable(true) 
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton){
				launchEmailIntent();
			} 
		}) 
		.show(); 
	}
	private void launchEmailIntent(){
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dinamo@dinamo.com.br"});		  
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	OnClickListener logInClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			initiateLogInProcess();	
		}
	};
	OnClickListener noAccountListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			launchSignUpScreen();
		}
	};
	OnClickListener forgotPassClickListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			SharedData.getInstance().sendEventNameToAnalytics("Forgot Password Button");
			displayEmailAlert();
		}
	};
	OnClickListener userIdOptionsClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			openUserIdOptionsDropWindow();
		}
	};
	AsyncResponseHandler logInGetRequestHandler=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			hideProgressDialog();
			if(responseCode==DinamoConstants.HTTP_GET_SUCCESS){
				try {
					JSONObject logInJson=new JSONObject(response);
					JSONObject dataObject=logInJson.getJSONArray("data").getJSONObject(0);
					//String password=dataObject.getString("passwordz");
					String password=dataObject.getString("senha");
					String name=dataObject.getString("name");
					String serverId=dataObject.getString("id");
					String userId=dataObject.getString("username");

					if(password.contentEquals(userPasswordInput)){
						User user=new User(name, password, serverId,0, userId);
						long key=DinamoDatabaseHelper.getInstance(userId).addUser(user);
						user.setPrimaryKey(key);
						
						DinamoPrefernces.getInstance(LogInActivity.this).saveLastSyncTime(System.currentTimeMillis());
						launchMainScreen(user);
					}else{
						SharedData.getInstance().displayMessageAlert(getString(R.string.connect_alert_title),getString(R.string.incorrect_password_alert_message),false);
					}
				} catch (JSONException e) {
					SharedData.getInstance().displayMessageAlert(getString(R.string.error_alert_title),getString(R.string.no_server_record_alert_messageo),false);
					e.printStackTrace();
				}
			}else{
				SharedData.getInstance().displayMessageAlert(getString(R.string.error_alert_title),getString(R.string.no_server_record_alert_messageo),false);
			}
		}
	};
	private void openUserIdOptionsDropWindow(){
		dropDownWindow=new DropDownMenuWindow(dropDownInflatedView,DinamoConstants.LOG_SIGN_DROP_WIDTH,0,0);
		dropDownWindow.showCalculatorMenu(optionDropDownAnchorView);	

		DropDownAdapter dropDownAdapter=new DropDownAdapter();
		dropDownListView.setAdapter(dropDownAdapter);	
	}

	private class DropDownAdapter extends BaseAdapter{

		OnClickListener dropdownRowSelectListener=new OnClickListener() {

			@Override
			public void onClick(View clickedView) {
				selectedUserIdCat=(Integer)clickedView.getTag();
				selectedUserIdType.setText(userIdOptions[selectedUserIdCat]);
				userIdEditor.removeTextChangedListener(cnpjMask);
				userIdEditor.removeTextChangedListener(cpfMask);

				if(selectedUserIdCat==0){	
					userIdEditor.addTextChangedListener(cpfMask);
				}else{
					userIdEditor.addTextChangedListener(cnpjMask);	
				}
				dropDownWindow.dismissWindow();
			}
		};
		@Override
		public int getCount() {
			return userIdOptions.length;
		}

		@Override
		public Object getItem(int position) {
			return userIdOptions[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.drop_down_row, null, false);
				//ScalingUtility.getInstance().scaleView(convertView);
			}
			TextView unitNameTextView=(TextView)convertView.findViewById(R.id.dropdownRowTextView);
			SharedData.getInstance().applyFontToTextView(unitNameTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			unitNameTextView.setText(userIdOptions[position]);

			convertView.setTag(position);
			convertView.setOnClickListener(dropdownRowSelectListener);
			return convertView;
		}

	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {

			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP 
					&& (x < w.getLeft() || x >= w.getRight() 
					|| y < w.getTop() || y > w.getBottom()) ) { 
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		}
		return ret;
	}

}
