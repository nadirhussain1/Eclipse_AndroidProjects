package br.com.dinamo;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.custom.screens.DropDownMenuWindow;
import br.com.custom.screens.PicChooseDialog;
import br.com.data.model.User;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.PostRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.utilities.CNPJUtil;
import br.com.utilities.CPFUtil;
import br.com.utilities.Mask;
import br.com.utilities.ScalingUtility;

public class SignUpActivity extends Activity {

	private  View      dropDownInflatedView=null;
	private  ListView  dropDownListView=null;
	private  TextView  userIdOptionsTextView=null;
	private  View      optionDropDownAnchorView=null;
	private  LayoutInflater inflater=null;
	private  Button    createAccountButton=null;
	private  Button   alreadyHaveAccountButton=null;
	private  EditText signUpEditor=null;
	private  EditText cpfEditor=null;
	private  EditText pinPasswordEditor=null;
	private  TextWatcher cpfMask;
	private  TextWatcher cnpjMask;
	private  String[] userIdOptions=new String[]{"CPF","CNPJ"};
	private  int selectedUserIdCat=0;
	DropDownMenuWindow dropDownWindow=null;

	private String userPhotoFilePath=null;
	private Bitmap userImageBitmap=null;
	private ImageButton userImageView=null;
	private PicChooseDialog chooseDialog=null;
	private User signUpUser=null;
	private ProgressDialog progressDialog;
	private JSONObject sessionObj;

	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private void hideProgressDialog() {
		progressDialog.hide();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View signUpView = inflater.inflate(R.layout.sign_up_layout, null, false);
		ScalingUtility.getInstance().scaleView(signUpView);
		setContentView(signUpView);


		applyFontsToTextOnScreen();
		initUserClicksListeners();
		SharedData.getInstance().sendScreenName("Signup Screen");


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DinamoConstants.CAMERA_REQUEST_CODE) {
			handleCameraResponse(resultCode);
		}else if(requestCode==DinamoConstants.GALLERY_REQUEST_CODE){
			try {
				handleGalleryChooser(resultCode,data);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void handleGalleryChooser(int resultCode,Intent data) throws FileNotFoundException, IOException{
		if(chooseDialog!=null){
			chooseDialog.cancelDialog();
		}

		if (resultCode == RESULT_OK && data!=null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			userPhotoFilePath = cursor.getString(columnIndex);
			cursor.close();


			userImageBitmap=SharedData.getInstance().decodeBitmap(userPhotoFilePath,DinamoConstants.SIGN_UP_PHOTO_SIZE);
			userImageBitmap=SharedData.getInstance().getCorrectRotatedBitmap(userImageBitmap, userPhotoFilePath);
			userImageView.setImageBitmap(userImageBitmap);
		}
	}
	private void handleCameraResponse(int resultCode){
		if(chooseDialog!=null){
			chooseDialog.cancelDialog();
		}

		if (resultCode == RESULT_OK) {
			try {
				userImageBitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), SharedData.getInstance().selectedImageUri);	
				userImageBitmap=Bitmap.createScaledBitmap(userImageBitmap,DinamoConstants.SIGN_UP_PHOTO_SIZE,DinamoConstants.SIGN_UP_PHOTO_SIZE, false);
				userImageBitmap=SharedData.getInstance().getCorrectRotatedBitmap(userImageBitmap, SharedData.getInstance().selectedImageUri.getPath());
				userImageView.setImageBitmap(userImageBitmap);
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, getString(R.string.photo_not_taken), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, getString(R.string.photo_not_taken), Toast.LENGTH_SHORT).show();
		}
	}
	private void initUserClicksListeners(){
		dropDownInflatedView=inflater.inflate(R.layout.drop_down_list, null, false);
		dropDownListView=(ListView)dropDownInflatedView.findViewById(R.id.generalMenuList);
		optionDropDownAnchorView=findViewById(R.id.cpfLayout);

		userIdOptionsTextView=(TextView)findViewById(R.id.cpfTextView);
		ImageView userIdOptionsImageView=(ImageView)findViewById(R.id.cpfArrowImageView);
		userImageView=(ImageButton)findViewById(R.id.signUpPhotoButton);

		userIdOptionsTextView.setOnClickListener(userIdOptionsClickListener);
		userIdOptionsImageView.setOnClickListener(userIdOptionsClickListener);
		userImageView.setOnClickListener(userImageChooseClickListener);

		alreadyHaveAccountButton.setOnClickListener(alreadyAccountButtonListener);
		createAccountButton.setOnClickListener(createAccountButtonListener);

		cpfMask = Mask.insert("###.###.###-##", cpfEditor);
		cpfEditor.addTextChangedListener(cpfMask);

		cnpjMask = Mask.insert("##.###.###/####-##", cpfEditor);

	}

	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.signUpLabelTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.signUpAddPhotoTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		signUpEditor=(EditText)findViewById(R.id.signUpNameEditText);
		SharedData.getInstance().applyFontToTextView(signUpEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.cpfTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		cpfEditor=(EditText)findViewById(R.id.cpfUserIdEditText);
		SharedData.getInstance().applyFontToTextView(cpfEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		pinPasswordEditor=(EditText)findViewById(R.id.pinCodeEditText);
		SharedData.getInstance().applyFontToTextView(pinPasswordEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		createAccountButton=(Button)findViewById(R.id.createAccountButton);
		SharedData.getInstance().applyFontToTextView(createAccountButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		alreadyHaveAccountButton=(Button)findViewById(R.id.alreadyAccountButton);
		SharedData.getInstance().applyFontToTextView(alreadyHaveAccountButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void createAccount(){
		boolean isConnected=SharedData.getInstance().isInternetAvailable();
		if(!isConnected){
			SharedData.getInstance().displayMessageAlert(getString(R.string.connect_alert_title),getString(R.string.signup_internet_alert_dialog),true);	
		}else{
			String userName=signUpEditor.getText().toString();
			String userId=cpfEditor.getText().toString();
			String password=pinPasswordEditor.getText().toString();
			boolean isValid=validateUserId(userId);
			//isValid=true;
			if(userName.trim().length()>0 && userId.trim().length()>0 && password.trim().length()>0 && isValid){
				userId=removeMask(userId);
				postSignUpRequest(userName,userId,password);
			}else{
				SharedData.getInstance().displayMessageAlert(getString(R.string.invalid_alert_data_title),getString(R.string.signup_invalid_data_alert),false);	
			}
		}

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
	private void postSignUpRequest(String userName,String userId,String password){
		JSONObject jsonObject=constructSignUpJson(userName, userId, password);
		if(jsonObject!=null){
			PostRequest postRequest=new PostRequest(DinamoConstants.SIGN_UP_BASE_URL, jsonObject, responseHandler);
			SharedData.executeTask(postRequest);
			showProgressDialog("Cadastrando sua conta...");
		}
	}
	private void saveUserToLocalDatabase(User user){
		if(DinamoDatabaseHelper.databaseHelper !=null){
			DinamoDatabaseHelper.getInstance().closeExistingDatabase();
		}

		long primaryKey=DinamoDatabaseHelper.getInstance(user.getUserId()).addUser(user);
		if(primaryKey !=-1){
			signUpUser=user;
			signUpUser.setPrimaryKey(primaryKey);
			if(userImageBitmap !=null){
				savePhotoToLocalDatabase();
				//sendPhotoUploadRequest(user.getServerId());
			}else{
				displaySignUpAlert(signUpUser.getPassword());
			}
		}else {
			SharedData.getInstance().displayMessageAlert(getString(R.string.error_alert_title),getString(R.string.error_server_alert),false);
		}
	}
	private JSONObject constructSignUpJson(String userName,String userId,String password){

		try {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("name", userName);
			jsonObject.put("username", userId);
			//jsonObject.put("passwordz", password);
			jsonObject.put("senha", password);
			jsonObject.put("password", password);

			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	AsyncResponseHandler responseHandler=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			hideProgressDialog();
			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
				try {
					JSONObject jsonObject = new JSONObject(response);
					String serverId=jsonObject.getString("id");
					String name=jsonObject.getString("name");
					String userId=jsonObject.getString("username");
					//String password=jsonObject.getString("passwordz");
					String password=jsonObject.getString("senha");
					User user=new User(name, password, serverId,0, userId);
					saveUserToLocalDatabase(user);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				SharedData.getInstance().displayMessageAlert(getString(R.string.error_alert_title),getString(R.string.error_server_alert),false);
			}
		}
	};
	//	AsyncResponseHandler photoPutRequestCallBackHandler=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				signUpUser.setUserPhoto(userImageBitmap);
	//				DinamoDatabaseHelper.getInstance(signUpUser.getUserId()).updateUser(signUpUser);
	//				launchLoginActivity();
	//			}
	//		}
	//	};
	private void savePhotoToLocalDatabase(){
		signUpUser.setUserPhoto(userImageBitmap);
		DinamoDatabaseHelper.getInstance(signUpUser.getUserId()).updateUser(signUpUser);
		displaySignUpAlert(signUpUser.getPassword());
	}
	//	private void sendPhotoUploadRequest(String id){
	//		String url=DinamoConstants.SIGN_UP_BASE_URL+"/"+id+"/photo";
	//		ImageUploadRequest uploadRequest=new ImageUploadRequest(url, userImageBitmap, photoPutRequestCallBackHandler);
	//		uploadRequest.execute();
	//	}
	private void launchLoginActivity(){
		Intent mainIntent = new Intent(SignUpActivity.this, LogInActivity.class);
		startActivity(mainIntent);
		SignUpActivity.this.finish();
	}
	private void processLoginAutomatically(String userId,String password){
		Intent mainIntent = new Intent(SignUpActivity.this, LogInActivity.class);

		mainIntent.putExtra("UserId", userId);
		mainIntent.putExtra("Password", password);

		startActivity(mainIntent);

		SignUpActivity.this.finish();
	}
	private OnClickListener userIdOptionsClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			openUserIdOptionsDropWindow();
		}
	};
	private OnClickListener userImageChooseClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Add Profile Photo Button");
			chooseDialog=new PicChooseDialog(SignUpActivity.this);
			chooseDialog.showDialog();
		}
	};
	private OnClickListener alreadyAccountButtonListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Already Registered Button");
			launchLoginActivity();
		}
	};
	private OnClickListener createAccountButtonListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			createAccount();
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
				userIdOptionsTextView.setText(userIdOptions[selectedUserIdCat]);

				cpfEditor.removeTextChangedListener(cnpjMask);
				cpfEditor.removeTextChangedListener(cpfMask);
				if(selectedUserIdCat==0){
					cpfEditor.addTextChangedListener(cpfMask);
				}else{
					cpfEditor.addTextChangedListener(cnpjMask);	
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
	public void displaySignUpAlert(final String password){
		String title=getResources().getString(R.string.sign_up_title);
		String message=getResources().getString(R.string.sign_up_alert_message);

		new AlertDialog.Builder(this).setMessage(message) 
		.setTitle(title) 
		.setCancelable(true) 
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton){
				String userId=cpfEditor.getText().toString();
				processLoginAutomatically(userId, password);
			} 
		}) 
		.show(); 
	}
}
