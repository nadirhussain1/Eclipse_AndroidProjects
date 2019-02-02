package br.com.dinamo;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.adapters.DropDownAdapter;
import br.com.custom.screens.AddDialog;
import br.com.custom.screens.DeleteDialog;
import br.com.custom.screens.DropDownMenuWindow;
import br.com.data.model.BoughtProduct;
import br.com.data.model.DinamoObject;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.DeleteRequest;
import br.com.requests.PostRequest;
import br.com.requests.PutRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.utilities.NumberTextWatcher;
import br.com.utilities.ScalingUtility;

public class AddBuyEventActivity extends Activity {
	private  View   boughtEventsView =null;
	private  int    launcherActId=1;
	private  int selectedEstId=-1;
	private  int paymentMethodIndex=0;


	private  EditText priceValueEditor=null;
	private  TextView dateTextView=null;
	private  TextView paymentMethodValue=null;
	private  TextView establishValue=null;
	private  EditText notesEditor=null;

	private  Button editPhotoButton=null;
	private  Button deleteBuyButton=null;
	private  Button saveButton=null;
	private  Button addBuyEventButton=null;

	private RelativeLayout payMethodLayout=null;
	private RelativeLayout estLayout=null;

	private ListView payMethodListView=null;
	private ListView addEstListView=null;
	private View     payDropDownView=null;
	private View     addEstDropDownView=null;
	private LayoutInflater inflater=null;
	private DropDownMenuWindow dropDownWindow=null;
	private DropDownAdapter dropDownAdapter=null;
	private String formattedDateString="";
	private int selectedItemPosition=-1;
	private BoughtProduct boughtProduct=null;
	private ProgressDialog progressDialog=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		launcherActId=getIntent().getIntExtra("ID", DinamoConstants.ADD_EVENT_ACTIVITY_ID);
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			boughtEventsView = inflater.inflate(R.layout.add_buy_event_layout, null, false);
			SharedData.getInstance().sendScreenName("Add Buy Events Screen");
		}else{
			boughtEventsView = inflater.inflate(R.layout.edit_buy_event, null, false);
			selectedItemPosition=getIntent().getIntExtra("POSITION",0);
			SharedData.getInstance().sendScreenName("Buy Event Edit Screen");
		}
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);

		applyFontsToTextOnScreen();
		initUserClicksListeners();

		if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
			displaySelectedItemData();
		}else{
			findDefaultPaymentMethodIndex();
			setDateClicker(null);
		}
	}
	@Override
	public void onBackPressed() {
		if(progressDialog!=null && progressDialog.isShowing()){
			return;
		}else{
			AddBuyEventActivity.this.finish();
		}
	}
	private void findDefaultPaymentMethodIndex(){
		String payMethod="Dinheiro";
		for(int count=0;count<UserDataManager.getInstance().paymentMethods.size();count++){
			if(payMethod.contentEquals(UserDataManager.getInstance().paymentMethods.get(count).getName())){
				paymentMethodIndex=count;
				break;

			}
		}
	}
	private void displaySelectedItemData(){
		String estabValue="";
		String payMethod="";
		boughtProduct=SharedData.getInstance().boughtEventsList.get(selectedItemPosition);
		estabValue=boughtProduct.getEstablishment().getName();
		payMethod=boughtProduct.getPayMethod().getName();


		for(int count=0;count<UserDataManager.getInstance().establishments.size();count++){
			if(estabValue.contentEquals(UserDataManager.getInstance().establishments.get(count).getName())){
				selectedEstId=count;
				break;

			}
		}
		for(int count=0;count<UserDataManager.getInstance().paymentMethods.size();count++){
			if(payMethod.contentEquals(UserDataManager.getInstance().paymentMethods.get(count).getName())){
				paymentMethodIndex=count;
				break;

			}
		}
		setDateClicker(boughtProduct.getDate());

		String priceValue=String.format("%.2f", boughtProduct.getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceValueEditor.setText(priceValue);

		paymentMethodValue.setText(payMethod);
		establishValue.setText(estabValue);
		notesEditor.setText(boughtProduct.getNotes());

	}
	private void showProgressDialog(String message){
		if(progressDialog ==null){
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	private void closeActivity(){
		if(progressDialog !=null){
			progressDialog.cancel();
		}
		AddBuyEventActivity.this.finish();
	}
	private void hideDialog(){
		if(progressDialog !=null){
			progressDialog.cancel();
		}
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.addBuyEventHeadTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceValueEditor=(EditText)findViewById(R.id.valorEditText);
		SharedData.getInstance().applyFontToTextView(priceValueEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.paymentDateStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.paymentDateValue);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.paymentMethodStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		paymentMethodValue=(TextView)findViewById(R.id.paymentMethodValue);
		SharedData.getInstance().applyFontToTextView(paymentMethodValue, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.establishStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		establishValue=(TextView)findViewById(R.id.establishValue);
		SharedData.getInstance().applyFontToTextView(establishValue, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		notesEditor=(EditText)findViewById(R.id.observationNotesEditText);
		SharedData.getInstance().applyFontToTextView(notesEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);

		if(launcherActId==1){
			textView=(TextView)findViewById(R.id.informPurcahseTextView);
			SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			addBuyEventButton=(Button)findViewById(R.id.addbuyEventButton);
			SharedData.getInstance().applyFontToTextView(addBuyEventButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}else{
			editPhotoButton=(Button)findViewById(R.id.editPhotoButton);
			SharedData.getInstance().applyFontToTextView(editPhotoButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			deleteBuyButton=(Button)findViewById(R.id.DeleteBuyEventButton);
			SharedData.getInstance().applyFontToTextView(deleteBuyButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			saveButton=(Button)findViewById(R.id.saveBuyEventButton);
			SharedData.getInstance().applyFontToTextView(saveButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}

	}
	//	private void enableButtons(boolean enable){
	//		if(addBuyEventButton!=null){
	//			addBuyEventButton.setClickable(enable);
	//		}
	//		if(editPhotoButton!=null){
	//			editPhotoButton.setClickable(enable);
	//		}
	//		if(deleteBuyButton!=null){
	//			deleteBuyButton.setClickable(enable);
	//		}
	//		if(saveButton!=null){
	//			saveButton.setClickable(enable);
	//		}
	//
	//	}
	private void initUserClicksListeners(){
		payDropDownView=inflater.inflate(R.layout.drop_down_list, null, false);
		addEstDropDownView=inflater.inflate(R.layout.add_button_drop_list, null, false);
		ScalingUtility.getInstance().scaleView(payDropDownView);
		ScalingUtility.getInstance().scaleView(addEstDropDownView);

		payMethodListView=(ListView)payDropDownView.findViewById(R.id.generalMenuList);
		addEstListView=(ListView)addEstDropDownView.findViewById(R.id.generalMenuList);
		TextView addButton=(TextView)addEstDropDownView.findViewById(R.id.addButton);

		payMethodLayout=(RelativeLayout)findViewById(R.id.paymentMethodInputLayout);
		estLayout=(RelativeLayout)findViewById(R.id.establishmentInputLayout);

		View backIconImage=(View)findViewById(R.id.backClickArea);
		backIconImage.setOnClickListener(backIconClickListener);

		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			addBuyEventButton=(Button)findViewById(R.id.addbuyEventButton);
			addBuyEventButton.setOnClickListener(addBuyEventListener);
		}else{
			saveButton.setOnClickListener(saveEventClickListener);
			deleteBuyButton.setOnClickListener(deleteBuyEventClickListener);
			editPhotoButton.setOnClickListener(editPhotoListener);
		}
		payMethodLayout.setOnClickListener(payMethodDropOpenListener);
		estLayout.setOnClickListener(estDropOpenListener);
		addButton.setOnClickListener(addEstCliclListener);

		priceValueEditor.addTextChangedListener(new NumberTextWatcher(priceValueEditor));
	}
	private void setDateClicker(Date date){
		Calendar cal = Calendar.getInstance();
		if(date!=null){
			cal.setTime(date);
		}
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int hour=cal.get(Calendar.HOUR);
		int mint=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);

		formattedDateString=""+year+"-"+(month+1)+"-"+day+" "+hour+":"+mint+":"+second;
		dateTextView.setText(""+day+"/"+(month+1)+"/"+year);

		RelativeLayout dateLayout=(RelativeLayout)findViewById(R.id.paymentDayInputLayout);
		dateLayout.setOnClickListener(dateSelectListener);
	}
	private void  addBoughtProductToDatabase(BoughtProduct product,boolean update){
		long primaryKey=DinamoDatabaseHelper.getInstance().addUpdateBoughtProduct(product,update);
		UserDataManager.getInstance().boughtEventsList.size();
		if(!update){
			product.setPrimaryKey(primaryKey);
			UserDataManager.getInstance().boughtEventsList.add(0,boughtProduct);
			SharedData.getInstance().boughtEventsList.add(0,boughtProduct);
		}
	}
	private void prepareAddUpdateBuyEventPost(String url){
		String valuePrice=priceValueEditor.getText().toString();
		valuePrice=valuePrice.replace(",", ".");
		boolean isValidPrice=SharedData.getInstance().isValidDecimalString(valuePrice);

		if(isValidPrice && selectedEstId!=-1){

			String notes=notesEditor.getText().toString();
			double value=Double.valueOf(valuePrice);
			String userId=SharedData.getInstance().currentUser.getServerId();

			String estId=UserDataManager.getInstance().establishments.get(selectedEstId).getId();
			String estName=UserDataManager.getInstance().establishments.get(selectedEstId).getName();
			DinamoObject establishment=new DinamoObject(estId,estName);

			String payMethodId=UserDataManager.getInstance().paymentMethods.get(paymentMethodIndex).getId();
			String payMethodName=UserDataManager.getInstance().paymentMethods.get(paymentMethodIndex).getName();
			DinamoObject paymentMethodObjet=new DinamoObject(payMethodId,payMethodName);

			if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
				boughtProduct.setPriceValue(value);
				boughtProduct.setDate(SharedData.getInstance().convertToDate(formattedDateString));
				boughtProduct.setEstablishment(establishment);
				boughtProduct.setPayMethod(paymentMethodObjet);
				boughtProduct.setNotes(notes);
				boughtProduct.isSynchronized=false;
			}else{
				boughtProduct=new BoughtProduct();
				boughtProduct.setPriceValue(value);
				boughtProduct.setDate(SharedData.getInstance().convertToDate(formattedDateString));
				boughtProduct.setEstablishment(establishment);
				boughtProduct.setPayMethod(paymentMethodObjet);
				boughtProduct.setNotes(notes);
				boughtProduct.setId("");
				boughtProduct.isSynchronized=false;  	
			}


			if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
				addBoughtProductToDatabase(boughtProduct,false);
				Intent mainIntent = new Intent(AddBuyEventActivity.this, TakePhotoActivity.class);
				mainIntent.putExtra("ServerId","");
				mainIntent.putExtra("localId",0);
				mainIntent.putExtra("Edit", false);
				startActivity(mainIntent);
			}else{
				addBoughtProductToDatabase(SharedData.getInstance().boughtEventsList.get(selectedItemPosition),true);
				BuyEventsMainActivity.refreshAdapter(false);
			}
			SharedData.getInstance().hasItemBeenAdded=true;
			closeActivity();


		}
		else{
			hideDialog();
			SharedData.getInstance().displayMessageAlert(getString(R.string.invalid_alert_data_title),getString(R.string.signup_valid_data_alert), false);

		}


	}
	OnClickListener payMethodDropOpenListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dropDownWindow=new DropDownMenuWindow(payDropDownView,DinamoConstants.MAIN_DROP_WIDTH,20,-20);
			dropDownWindow.showCalculatorMenu(payMethodLayout);	
			dropDownAdapter=new DropDownAdapter(UserDataManager.getInstance().paymentMethods);
			payMethodListView.setAdapter(dropDownAdapter);
			payMethodListView.setOnItemClickListener(paymentMethodListItemListener);

		}
	};
	OnClickListener estDropOpenListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dropDownWindow=new DropDownMenuWindow(addEstDropDownView,DinamoConstants.MAIN_DROP_WIDTH,20,-20);
			dropDownWindow.showCalculatorMenu(estLayout);	
			dropDownAdapter=new DropDownAdapter(UserDataManager.getInstance().establishments);
			addEstListView.setAdapter(dropDownAdapter);	
			addEstListView.setOnItemClickListener(estListItemListener);
		}
	};
	OnItemClickListener paymentMethodListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			paymentMethodValue.setText(UserDataManager.getInstance().paymentMethods.get(index).getName());
			paymentMethodIndex=index;
			dropDownWindow.dismissWindow();
		}
	};
	OnItemClickListener estListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			establishValue.setText(UserDataManager.getInstance().establishments.get(index).getName());
			selectedEstId=index;
			dropDownWindow.dismissWindow();
		}
	};
	OnClickListener dateSelectListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			DatePickerDialog datePickDialog=new DatePickerDialog(AddBuyEventActivity.this,datePickerListener, year, month, day);
			datePickDialog.show();

		}
	};
	OnDateSetListener datePickerListener=new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			Calendar cal = Calendar.getInstance();
			int hour=cal.get(Calendar.HOUR);
			int mint=cal.get(Calendar.MINUTE);
			int second=cal.get(Calendar.SECOND);

			formattedDateString=""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+hour+":"+mint+":"+second;
			dateTextView.setText(""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
		}
	};
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			closeActivity();
		}
	};
	OnClickListener addBuyEventListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_adicionado));
			SharedData.getInstance().sendEventNameToAnalytics("Add Buy Event Button 2");
			prepareAddUpdateBuyEventPost(DinamoConstants.BUY_EVENT_URL);
		}
	};
	OnClickListener deleteBuyEventClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics(getString(R.string.aguarde_apagado));
			DeleteDialog deleteDialog=new DeleteDialog(DinamoConstants.COMPRAS_SCREEN,AddBuyEventActivity.this);
			deleteDialog.showDialog();
		}
	};
	private void deleteItemFromLocalData(boolean shouldDeleteFromDb){
		if(shouldDeleteFromDb){
			long primaryKey=SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteBoughtProduct(primaryKey);

			SharedData.getInstance().boughtEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().boughtEventsList.remove(selectedItemPosition);
		}else{
			SharedData.getInstance().boughtEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().boughtEventsList.get(selectedItemPosition).isSynchronized=false;

			DinamoDatabaseHelper.getInstance().addUpdateBoughtProduct(SharedData.getInstance().boughtEventsList.get(selectedItemPosition), true);
			SharedData.getInstance().boughtEventsList.remove(selectedItemPosition);
		}
		BuyEventsMainActivity.refreshAdapter(true);
		closeActivity();
	}
	public void performDeleteAction(){
		String itemId=SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getId();

		if(itemId.equalsIgnoreCase("")){
			deleteItemFromLocalData(true);
		}else if(!SharedData.getInstance().isInternetAvailable()){
			deleteItemFromLocalData(false);
		}else{
			showProgressDialog(getString(R.string.aguarde_apagado));
			DeleteRequest delteRequest=new DeleteRequest(deleteCallBackHandler, DinamoConstants.BUY_EVENT_URL+"/"+SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getId());
			SharedData.executeTask(delteRequest);
		}
	}
	OnClickListener editPhotoListener=new OnClickListener() {

		@Override
		public void onClick(View v) {	
			Intent mainIntent = new Intent(AddBuyEventActivity.this, TakePhotoActivity.class);
			mainIntent.putExtra("ServerId",SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getId());
			mainIntent.putExtra("localId",selectedItemPosition);
			mainIntent.putExtra("Edit", true);
			startActivity(mainIntent);
		}
	};
	OnClickListener saveEventClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_salvo));

			SharedData.getInstance().sendEventNameToAnalytics("Save Buy Event Button");
			prepareAddUpdateBuyEventPost(DinamoConstants.BUY_EVENT_URL+"/"+SharedData.getInstance().boughtEventsList.get(selectedItemPosition).getId());
		}
	};
	//	AsyncResponseHandler addEventCallBackListener=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			SharedData.getInstance().sendEventNameToAnalytics("Add Cupom Event Button");
	//			String id="";
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				boughtProduct.isSynchronized=true;
	//				try {
	//					JSONObject mainJsonObject=new JSONObject(response);
	//					id=mainJsonObject.getString("id");
	//					boughtProduct.setId(id);
	//				} catch (JSONException e) {
	//					e.printStackTrace();
	//				}
	//
	//			}else{
	//				boughtProduct.isSynchronized=false;
	//			}
	//			
	//			addBoughtProductToDatabase(boughtProduct,false);
	//			
	//			Intent mainIntent = new Intent(AddBuyEventActivity.this, TakePhotoActivity.class);
	//			mainIntent.putExtra("ServerId",id);
	//			mainIntent.putExtra("localId",0);
	//			mainIntent.putExtra("Edit", false);
	//			startActivity(mainIntent);
	//
	//			closeActivity();
	//		}
	//	};
	//	AsyncResponseHandler updateEventCallBackHandler=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				SharedData.getInstance().boughtEventsList.get(selectedItemPosition).isSynchronized=true;
	//			}else{
	//				SharedData.getInstance().boughtEventsList.get(selectedItemPosition).isSynchronized=false;
	//			}
	//			addBoughtProductToDatabase(SharedData.getInstance().boughtEventsList.get(selectedItemPosition),true);
	//			BuyEventsMainActivity.refreshAdapter(false);
	//			closeActivity();
	//		}
	//	};
	AsyncResponseHandler deleteCallBackHandler=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			if(responseCode==DinamoConstants.HTTP_GET_SUCCESS){
				deleteItemFromLocalData(true);
			}else{
				deleteItemFromLocalData(false);
			}
		}
	};
	OnClickListener addEstCliclListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AddDialog addDialog=new AddDialog(dropDownAdapter,DinamoConstants.GET_EST_REQ_ID);
			addDialog.showDialog();

		}
	};
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
