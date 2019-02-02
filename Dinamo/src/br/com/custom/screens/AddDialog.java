package br.com.custom.screens;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import br.com.adapters.DropDownAdapter;
import br.com.data.model.DinamoObject;
import br.com.data.model.UserDataManager;
import br.com.dinamo.R;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.storage.DinamoDatabaseHelper;
import br.com.utilities.ScalingUtility;

public class AddDialog {
	private  Dialog addDialog=null;
	private  EditText newNameEditor=null;
	private  String  addNewItemName="";
	DropDownAdapter customAdapter=null;
	int postAPIId=-1;

	public AddDialog(DropDownAdapter adapter,int code){
		customAdapter=adapter;
		postAPIId=code;

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.add_item_dialog, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);
		
		addDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		addDialog.setContentView(dialogView);

		TextView dialogTitle=(TextView)dialogView.findViewById(R.id.dialogTitleTextView);
		if(postAPIId==DinamoConstants.GET_PROD_REQ_ID){
			dialogTitle.setText(SharedData.getInstance().mContext.getString(R.string.new_product_label));
		}else if(postAPIId==DinamoConstants.GET_CAT_REQ_ID){
			dialogTitle.setText(SharedData.getInstance().mContext.getString(R.string.new_categories_label));
		}
		newNameEditor=(EditText)dialogView.findViewById(R.id.newNameEditor);
		TextView cancelButton=(TextView)dialogView.findViewById(R.id.cancelButtonView);
		TextView addOkButton=(TextView)dialogView.findViewById(R.id.addButtonOkView);

		cancelButton.setOnClickListener(addDialogCancelListner);
		addOkButton.setOnClickListener(addDialogOkListner);
	}
	public void showDialog(){
		addDialog.show();
	}
	public void cancelDialog(){
		addDialog.cancel();
	}
//	private void addNewItemToBackend(String name){
//		JSONObject jsonObject=constructJSON(name);
//		if(postAPIId==DinamoConstants.GET_EST_REQ_ID){
//			PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_ESTABLISHMENT_URL, jsonObject, postRequestCallBackListener);
//			postRequest.execute();
//		}else if(postAPIId==DinamoConstants.GET_PROD_REQ_ID){
//			PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_PRODUCT_URL, jsonObject, postRequestCallBackListener);
//			postRequest.execute();
//		}else if(postAPIId==DinamoConstants.GET_CAT_REQ_ID){
//			PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_CATEGORIES_URL, jsonObject, postRequestCallBackListener);
//			postRequest.execute();
//		}
//	}
//	private JSONObject constructJSON(String nameValue){
//		JSONObject jsonObject=new JSONObject();
//		try {
//			jsonObject.put("name", nameValue);
//			jsonObject.put("user", SharedData.getInstance().currentUser.getServerId());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return jsonObject;
//	}
	private void addNewItemToDatabase(DinamoObject dinamoObject){
		if(postAPIId==DinamoConstants.GET_EST_REQ_ID){
			UserDataManager.getInstance().establishments.add(dinamoObject);
			DinamoDatabaseHelper.getInstance().addUpdateEstablishment(dinamoObject,false);
		}else if(postAPIId==DinamoConstants.GET_PROD_REQ_ID){
			UserDataManager.getInstance().products.add(dinamoObject);
			DinamoDatabaseHelper.getInstance().addUpdateProduct(dinamoObject,false);
		}else if(postAPIId==DinamoConstants.GET_CAT_REQ_ID){
			UserDataManager.getInstance().catagories.add(dinamoObject);
			DinamoDatabaseHelper.getInstance().addUpdateCategory(dinamoObject,false);
		}
		customAdapter.notifyDataSetChanged();
	}
	OnClickListener addDialogOkListner=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			addNewItemName=newNameEditor.getText().toString();
			if(addNewItemName.trim().length()>0){
//				if(SharedData.getInstance().isInternetAvailable()){
//					addNewItemToBackend(addNewItemName);
//				}else{
					DinamoObject object=new DinamoObject();
					object.setName(addNewItemName);
					object.isSynChronized=false;
					addNewItemToDatabase(object);
				//}
			}
			addDialog.cancel();

		}
	};
	OnClickListener addDialogCancelListner=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			addDialog.cancel();

		}
	};
	AsyncResponseHandler postRequestCallBackListener=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			DinamoObject dinamoObject=new DinamoObject();
			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
				try {
					JSONObject jsonObject=new JSONObject(response);
					dinamoObject.setName(jsonObject.getString("name"));
					dinamoObject.setId(jsonObject.getString("id"));	
					dinamoObject.isSynChronized=true;
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}else{
				dinamoObject.setName(addNewItemName);
				dinamoObject.isSynChronized=false;
			}
			addNewItemToDatabase(dinamoObject);
		}

	};
}



