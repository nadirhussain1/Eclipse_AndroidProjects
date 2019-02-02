package com.mobive.dealme;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humby.dealular.R;
import com.mobive.net.PostRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class AccountsEditor {
	View accountsView=null;
	EditText firstNameEditValue=null;
	EditText lastNameEditValue=null;
	EditText EmailEditText=null;
	EditText passwordEditText=null;

	ImageView emailEditorPencil=null;
	ImageView passwordEditorPencil=null;
	ImageView firstNameEditorPenicl=null;
	ImageView lastNamePencilEditor=null;

	public AccountsEditor(){
		accountsView = View.inflate(DataUtility.getContext(), R.layout.acountsetting, null);
		firstNameEditValue=(EditText)accountsView.findViewById(R.id.firstName);
		lastNameEditValue=(EditText)accountsView.findViewById(R.id.lastName);
		EmailEditText=(EditText)accountsView.findViewById(R.id.emailid);
		passwordEditText=(EditText)accountsView.findViewById(R.id.passwordValue);

		emailEditorPencil=(ImageView)accountsView.findViewById(R.id.emailEditor);
		passwordEditorPencil=(ImageView)accountsView.findViewById(R.id.passwordEditor);
		firstNameEditorPenicl=(ImageView)accountsView.findViewById(R.id.firstNameEditor);
		lastNamePencilEditor=(ImageView)accountsView.findViewById(R.id.lastNameEditor);
		emailEditorPencil.setOnClickListener(emailEditClickListener);
		passwordEditorPencil.setOnClickListener(passwordClickListener);
		firstNameEditorPenicl.setOnClickListener(firstNameEditClickListener);
		lastNamePencilEditor.setOnClickListener(secondNameEditClickListener);


	}
	public View getAccountsScreenView(){
		populateStoredValues();
		return accountsView;
	}
	public void populateStoredValues(){
		EmailEditText.setText(DealMePreferences.getInstance().getUserEmail());
		int passwordLength=DealMePreferences.getInstance().getUserPassword().length();
		String passwrodHidden="";
		for(int count=0;count<passwordLength;count++){
			passwrodHidden+="*";
		}
		passwordEditText.setText(passwrodHidden);

		firstNameEditValue.setText(DealMePreferences.getInstance().getFirstName());
		lastNameEditValue.setText(DealMePreferences.getInstance().getLastName());

	}
	OnClickListener passwordClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			View passwordEditingView=View.inflate(DataUtility.getContext(),R.layout.login_pop_up, null);
			final EditText newPassword=(EditText)passwordEditingView.findViewById(R.id.email);
			final EditText confirmPassword=(EditText)passwordEditingView.findViewById(R.id.password);
			((TextView)passwordEditingView.findViewById(R.id.emailTextView)).setText("NEW PASSWORD");
			((TextView)passwordEditingView.findViewById(R.id.passwordText)).setText("CONFIRM PASSWORD");
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder.setView(passwordEditingView);
			alertDialogBuilder.setTitle("EDIT PASSWORD");
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if(newPassword.getText()==null || confirmPassword.getText()==null){
						Toast.makeText(DataUtility.getContext(),"Password must be at least 7 letters/digits long", Toast.LENGTH_SHORT).show();
					}else{
						String newPasswordvalue=newPassword.getText().toString();
						String confirmPassValue=confirmPassword.getText().toString();

						if(newPasswordvalue.length()<7){
							Toast.makeText(DataUtility.getContext(),"Password must be at least 7 letters/digits long", Toast.LENGTH_SHORT).show();
						}else if(!newPasswordvalue.equals(confirmPassValue)){
							Toast.makeText(DataUtility.getContext(),"The passwords you entered don't match.", Toast.LENGTH_SHORT).show();
						}else{
							DealMePreferences.getInstance().saveUserPassword(newPasswordvalue);
							constructJSONAndSaveToServer();
							dialog.cancel();	
						}
					}
				}
			});
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();	
				}
			});
			AlertDialog editingDialog = alertDialogBuilder.create();
			editingDialog.show();

		}
	};
	OnClickListener emailEditClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			View editorView = View.inflate(DataUtility.getContext(), R.layout.editor_settings_pop, null);
			final EditText generalEditorField=(EditText)editorView.findViewById(R.id.editTextPop);
			generalEditorField.setText(DealMePreferences.getInstance().getUserEmail());
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder.setTitle("EDIT EMAIL ADDRESS");
			alertDialogBuilder.setView(editorView);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if(generalEditorField.getText()!=null && DataUtility.isValidEmail(generalEditorField.getText().toString())){
						DealMePreferences.getInstance().saveUserEmail(generalEditorField.getText().toString());
						constructJSONAndSaveToServer();
						dialog.cancel();	
					}else{
						Toast.makeText(DataUtility.getContext(),"Enter valid email address.", Toast.LENGTH_SHORT).show();
					}

				}

			});
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();	
				}
			});
			AlertDialog editingDialog = alertDialogBuilder.create();
			editingDialog.show();

		}
	};
	OnClickListener secondNameEditClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			View editorView = View.inflate(DataUtility.getContext(), R.layout.editor_settings_pop, null);
			final EditText generalEditorField=(EditText)editorView.findViewById(R.id.editTextPop);
			generalEditorField.setText(DealMePreferences.getInstance().getLastName());
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder.setTitle("EDIT LAST NAME");
			alertDialogBuilder.setView(editorView);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if(generalEditorField.getText()!=null && !generalEditorField.getText().toString().equalsIgnoreCase(" ")){
						DealMePreferences.getInstance().saveLastName(generalEditorField.getText().toString());
						dialog.cancel();
					}else{
						Toast.makeText(DataUtility.getContext(),"Enter Valid Name", Toast.LENGTH_SHORT).show();	
					}
				}
			});
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();	
				}
			});
			
			AlertDialog editingDialog = alertDialogBuilder.create();
			editingDialog.show();
			
		}
	};
	OnClickListener firstNameEditClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			View editorView = View.inflate(DataUtility.getContext(), R.layout.editor_settings_pop, null);
			final EditText generalEditorField=(EditText)editorView.findViewById(R.id.editTextPop);
			generalEditorField.setText(DealMePreferences.getInstance().getFirstName());
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DataUtility.getContext());
			alertDialogBuilder.setTitle("EDIT FIRST NAME");
			alertDialogBuilder.setView(editorView);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if(generalEditorField.getText()!=null){
						DealMePreferences.getInstance().saveFirstName(generalEditorField.getText().toString());
						dialog.cancel();
					}else{
						Toast.makeText(DataUtility.getContext(),"Enter Valid Name", Toast.LENGTH_SHORT).show();	
					}
				}
			});
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();	
				}
			});
			AlertDialog editingDialog = alertDialogBuilder.create();
			editingDialog.show();
		}
	};
	
	private void saveSettingsToServer(JSONObject json){
		new PostRequest(DealMeConstants.updateUserUrl, json, dealsPostListener ).execute();
	}
	RequestListener dealsPostListener=new RequestListener() {

		@Override
		public void onSuccess(InputStream inputStream) {
			String result=Util.ConvertToJson(inputStream);
			String message="";
			try {
			     JSONObject responseJSON=new JSONObject(result);
                 message=responseJSON.getJSONObject("metadata").getString("message");
		     } catch (JSONException e) {
			// TODO Auto-generated catch block
			    e.printStackTrace();
		     }
            Toast.makeText(DataUtility.getContext(),message, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(String message) {
			//Toast.makeText(DataUtility.getContext(),message, Toast.LENGTH_SHORT);

		}
	};
	private void constructJSONAndSaveToServer(){
		JSONObject json=new JSONObject();
		try {
            String userName=DealMePreferences.getInstance().getFirstName()+","+DealMePreferences.getInstance().getLastName();
			json.put("user",""+DataUtility.getUser().getId());
			json.put("email",DealMePreferences.getInstance().getUserEmail());
			json.put("password",DealMePreferences.getInstance().getUserPassword());
			json.put("name", userName);
			saveSettingsToServer(json);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		
	}
}
