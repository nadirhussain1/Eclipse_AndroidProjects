package br.com.dinamo;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import br.com.data.model.DinamoObject;
import br.com.data.model.SoldProduct;
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

public class AddSellProductActivity extends Activity {
	private  View   addSellProductView =null;
	private  int    launcherActId=1;
	private  int currSelectedProdIndex=-1;
	private  int currSelectedPayMethodIndex=0;

	private  EditText priceValueEditor=null;
	private  EditText quantityEditor=null;
	private  TextView dateTextView=null;
	private  TextView paymentMethodValue=null;
	private  TextView productValueView=null;
	private  TextView quantityView=null;

	private  Button addSellProductButton=null;
	private  Button saveProductButton=null;
	private  Button deleteProductButton=null;

	private RelativeLayout payMethodLayout=null;
	private RelativeLayout productLayout=null;

	private ListView payMethodListView=null;
	private ListView addProdListView=null;
	private View     payDropDownView=null;
	private View     addProdDropDownView=null;
	private LayoutInflater inflater=null;
	private DropDownMenuWindow dropDownWindow=null;
	private DropDownAdapter dropDownAdapter=null;
	private Dialog quantityDialog=null;
	private String quantityInput="1";
	private String formattedDateString="";
	private SoldProduct soldProduct=null;
	private int selectedItemPosition=-1;
	private ProgressDialog progressDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		launcherActId=getIntent().getIntExtra("ID", DinamoConstants.ADD_EVENT_ACTIVITY_ID);
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			addSellProductView = inflater.inflate(R.layout.add_sell_product_layout, null, false);
			SharedData.getInstance().sendScreenName("Add Sell Events Screen");
		}else{
			addSellProductView = inflater.inflate(R.layout.edit_sold_product_layout, null, false);
			selectedItemPosition=getIntent().getIntExtra("POSITION",0);

			SharedData.getInstance().sendScreenName("Sell Event Edit Screen");
		}
		ScalingUtility.getInstance().scaleView(addSellProductView);
		setContentView(addSellProductView);

		applyFontsToTextOnScreen();
		initUserClicksListeners();

		if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
			displaySelectedItemData();
		}else{
			findDefaultPaymentMethodIndex();
			setDateClicker(null);
		}
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.addSellEventHeadTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceValueEditor=(EditText)findViewById(R.id.valorEditText);
		SharedData.getInstance().applyFontToTextView(priceValueEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.quantityStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		quantityView=(TextView)findViewById(R.id.quantityValue);
		SharedData.getInstance().applyFontToTextView(quantityView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.productStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		productValueView=(TextView)findViewById(R.id.productNameValue);
		SharedData.getInstance().applyFontToTextView(productValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.receiptDateStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.receiptDateValue);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.paymentMethodStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		paymentMethodValue=(TextView)findViewById(R.id.paymentMethodValue);
		SharedData.getInstance().applyFontToTextView(paymentMethodValue, DinamoConstants.HELVETICA_NEUE_CONDENSED);

		if(launcherActId==1){
			addSellProductButton=(Button)findViewById(R.id.addSoldProductButton);
			SharedData.getInstance().applyFontToTextView(addSellProductButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}else{
			saveProductButton=(Button)findViewById(R.id.saveSoldProductButton);
			deleteProductButton=(Button)findViewById(R.id.DeleteSoldProductButton);
			SharedData.getInstance().applyFontToTextView(saveProductButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			SharedData.getInstance().applyFontToTextView(deleteProductButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}
	}
	private void initUserClicksListeners(){
		payDropDownView=inflater.inflate(R.layout.drop_down_list, null, false);
		addProdDropDownView=inflater.inflate(R.layout.add_button_drop_list, null, false);
		ScalingUtility.getInstance().scaleView(payDropDownView);
		ScalingUtility.getInstance().scaleView(addProdDropDownView);

		payMethodListView=(ListView)payDropDownView.findViewById(R.id.generalMenuList);
		addProdListView=(ListView)  addProdDropDownView.findViewById(R.id.generalMenuList);

		TextView addProductButton=(TextView)addProdDropDownView.findViewById(R.id.addButton);
		addProductButton.setText(getString(R.string.add_product_label));

		payMethodLayout=(RelativeLayout)findViewById(R.id.paymentMethodInputLayout);
		productLayout=(RelativeLayout)findViewById(R.id.ProductInputLayout);
		RelativeLayout quantityInputLayout=(RelativeLayout)findViewById(R.id.QuantityInputLayout);
		quantityInputLayout.setOnClickListener(quantityClickListener);

		View backIconImage=(View)findViewById(R.id.backClickArea);
		backIconImage.setOnClickListener(backIconClickListener);
		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			addSellProductButton.setOnClickListener(createSoldProductListener);
		}else{
			saveProductButton.setOnClickListener(saveSoldProdListener);
			deleteProductButton.setOnClickListener(deleteSoldProdClickListener);
		}

		payMethodLayout.setOnClickListener(payMethodDropOpenListener);
		productLayout.setOnClickListener(prodDropOpenListener);
		addProductButton.setOnClickListener(addProdCliclListener);

		priceValueEditor.addTextChangedListener(new NumberTextWatcher(priceValueEditor));
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
		AddSellProductActivity.this.finish();
	}
	private void hideDialog(){
		if(progressDialog !=null){
			progressDialog.cancel();
		}
	}
	@Override
	public void onBackPressed() {
		if(progressDialog!=null && progressDialog.isShowing()){
			return;
		}else{
			AddSellProductActivity.this.finish();
		}
	}
	private void findDefaultPaymentMethodIndex(){
		String payMethod="Dinheiro";
		for(int count=0;count<UserDataManager.getInstance().paymentMethods.size();count++){
			if(payMethod.contentEquals(UserDataManager.getInstance().paymentMethods.get(count).getName())){
				currSelectedPayMethodIndex=count;
				break;

			}
		}
	}
	private void displaySelectedItemData(){
		String productName="";
		String payMethod="";
		productName=SharedData.getInstance().soldEventsList.get(selectedItemPosition).getProduct().getName();
		payMethod=SharedData.getInstance().soldEventsList.get(selectedItemPosition).getPayMethod().getName();

		for(int count=0;count<UserDataManager.getInstance().products.size();count++){
			if(productName.contentEquals(UserDataManager.getInstance().products.get(count).getName())){
				currSelectedProdIndex=count;
				break;

			}
		}
		for(int count=0;count<UserDataManager.getInstance().paymentMethods.size();count++){
			if(payMethod.contentEquals(UserDataManager.getInstance().paymentMethods.get(count).getName())){
				currSelectedPayMethodIndex=count;
				break;

			}
		}
		setDateClicker(SharedData.getInstance().soldEventsList.get(selectedItemPosition).getDate());

		String priceValue=String.format("%.2f", SharedData.getInstance().soldEventsList.get(selectedItemPosition).getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceValueEditor.setText(priceValue);

		paymentMethodValue.setText(payMethod);
		productValueView.setText(productName);
		quantityView.setText(SharedData.getInstance().soldEventsList.get(selectedItemPosition).getQuantity());
	}
	//	private void enableButtons(boolean enable){
	//		if(addSellProductButton!=null){
	//			addSellProductButton.setClickable(enable);
	//		}
	//		if(deleteProductButton!=null){
	//			deleteProductButton.setClickable(enable);
	//		}
	//		if(saveProductButton!=null){
	//			saveProductButton.setClickable(enable);
	//		}
	//
	//	}
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

		RelativeLayout dateLayout=(RelativeLayout)findViewById(R.id.receiptDateInputLayout);
		dateLayout.setOnClickListener(dateSelectListener);
	}
	private void openQuantityDialog(){
		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.add_quantity_dialog, null, false);
		quantityDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		quantityDialog.setContentView(dialogView);

		quantityEditor=(EditText)dialogView.findViewById(R.id.quantityInputEditor);
		quantityEditor.setText(quantityView.getText().toString());
		TextView cancelButton=(TextView)dialogView.findViewById(R.id.cancelButtonView);
		TextView addOkButton=(TextView)dialogView.findViewById(R.id.doneButtonView);

		cancelButton.setOnClickListener(quantityCancelListener);
		addOkButton.setOnClickListener(quantityDoneListener);

		quantityDialog.show();
	}
	private void addSoldProductToDatabase(SoldProduct product,boolean update){
		long primaryKey=DinamoDatabaseHelper.getInstance().addUpdateSoldProduct(product,update);
		if(!update){
			product.setPrimaryKey(primaryKey);
			UserDataManager.getInstance().soldEventsList.add(product);
			SharedData.getInstance().soldEventsList.add(product);

		}
		SellEventsMainActivity.refreshAdapter(false);

	}
	private void deleteItemFromLocalData(boolean shouldDeleteFromDb){
		if(shouldDeleteFromDb){
			long primaryKey=SharedData.getInstance().soldEventsList.get(selectedItemPosition).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteSoldProduct(primaryKey);

			SharedData.getInstance().soldEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().soldEventsList.remove(selectedItemPosition);
		}else{
			SharedData.getInstance().soldEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().soldEventsList.get(selectedItemPosition).isSynchronized=false;

			DinamoDatabaseHelper.getInstance().addUpdateSoldProduct(SharedData.getInstance().soldEventsList.get(selectedItemPosition), true);
			SharedData.getInstance().soldEventsList.remove(selectedItemPosition);
		}

		SellEventsMainActivity.refreshAdapter(true);
		closeActivity();
	}
	OnClickListener dateSelectListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			DatePickerDialog datePickDialog=new DatePickerDialog(AddSellProductActivity.this,datePickerListener, year, month, day);
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
	private void prepareAddUpdateSoldProdPost(String url){
		String valuePrice=priceValueEditor.getText().toString();
		valuePrice=valuePrice.replace(",", ".");
		boolean isValidPrice=SharedData.getInstance().isValidDecimalString(valuePrice);

		if(isValidPrice && currSelectedProdIndex!=-1){

			double value=Double.valueOf(valuePrice);
			String userId=SharedData.getInstance().currentUser.getServerId();
			String quantity=quantityInput;

			String productId=UserDataManager.getInstance().products.get(currSelectedProdIndex).getId();
			String productName=UserDataManager.getInstance().products.get(currSelectedProdIndex).getName();
			DinamoObject productObject=new DinamoObject(productId,productName);

			String payMethodId=UserDataManager.getInstance().paymentMethods.get(currSelectedPayMethodIndex).getId();
			String payMethodName=UserDataManager.getInstance().paymentMethods.get(currSelectedPayMethodIndex).getName();
			DinamoObject paymentMethodObject=new DinamoObject(payMethodId,payMethodName);

			if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).setPriceValue(value);
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).setDate(SharedData.getInstance().convertToDate(formattedDateString));
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).setProduct(productObject);
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).setPayMethod(paymentMethodObject);
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).setQuantity(quantityInput);;
				SharedData.getInstance().soldEventsList.get(selectedItemPosition).isSynchronized=false;
			}else{
				soldProduct=new SoldProduct();
				soldProduct.setPriceValue(value);
				soldProduct.setDate(SharedData.getInstance().convertToDate(formattedDateString));
				soldProduct.setProduct(productObject);
				soldProduct.setPayMethod(paymentMethodObject);
				soldProduct.setQuantity(quantity);
				soldProduct.setId("");
				soldProduct.isSynchronized=false;  	
			}


			if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
				addSoldProductToDatabase(soldProduct,false);
			}else{
				addSoldProductToDatabase(SharedData.getInstance().soldEventsList.get(selectedItemPosition),true);
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
	OnClickListener prodDropOpenListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dropDownWindow=new DropDownMenuWindow(addProdDropDownView,DinamoConstants.MAIN_DROP_WIDTH,20,-20);
			dropDownWindow.showCalculatorMenu(productLayout);	

			dropDownAdapter=new DropDownAdapter(UserDataManager.getInstance().products);
			addProdListView.setAdapter(dropDownAdapter);
			addProdListView.setOnItemClickListener(prodListItemListener);
		}
	};
	OnItemClickListener paymentMethodListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			paymentMethodValue.setText(UserDataManager.getInstance().paymentMethods.get(index).getName());
			currSelectedPayMethodIndex=index;
			dropDownWindow.dismissWindow();
		}
	};
	OnItemClickListener prodListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			productValueView.setText(UserDataManager.getInstance().products.get(index).getName());
			currSelectedProdIndex=index;
			dropDownWindow.dismissWindow();
		}
	};
	OnClickListener quantityCancelListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			quantityDialog.cancel();

		}
	};
	OnClickListener quantityDoneListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(quantityEditor.getText().toString().trim().length()>0){
				quantityInput=quantityEditor.getText().toString();
				quantityView.setText(quantityInput);
			}
			quantityDialog.cancel();
		}
	};

	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnClickListener deleteSoldProdClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Delete Sell Event Button");
			DeleteDialog deleteDialog=new DeleteDialog(DinamoConstants.VENDA_SCREEN,AddSellProductActivity.this);
			deleteDialog.showDialog();
		}
	};
	public void performDeleteAction(){
		String itemId=SharedData.getInstance().soldEventsList.get(selectedItemPosition).getId();
		if(itemId.equalsIgnoreCase("")){
			deleteItemFromLocalData(true);
		}else if(!SharedData.getInstance().isInternetAvailable()){
			deleteItemFromLocalData(false);
		}else{
			showProgressDialog(getString(R.string.aguarde_apagado));
			DeleteRequest delteRequest=new DeleteRequest(deleteCallBackHandler, DinamoConstants.SELL_EVENT_URL+"/"+SharedData.getInstance().soldEventsList.get(selectedItemPosition).getId());
			SharedData.executeTask(delteRequest);
		}
	}
	OnClickListener createSoldProductListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_adicionado));
			SharedData.getInstance().sendEventNameToAnalytics("Add Sell Event Button 2");
			prepareAddUpdateSoldProdPost(DinamoConstants.SELL_EVENT_URL);
		}
	};
	OnClickListener saveSoldProdListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_salvo));
			SharedData.getInstance().sendEventNameToAnalytics("Save Sell Event Button");
			prepareAddUpdateSoldProdPost(DinamoConstants.SELL_EVENT_URL+"/"+SharedData.getInstance().soldEventsList.get(selectedItemPosition).getId());
		}
	};
	OnClickListener quantityClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			openQuantityDialog();
		}
	};
	OnClickListener addProdCliclListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AddDialog addDialog=new AddDialog(dropDownAdapter,DinamoConstants.GET_PROD_REQ_ID);
			addDialog.showDialog();

		}
	};
	//	AsyncResponseHandler addEventCallBackListener=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				soldProduct.isSynchronized=true;
	//				try {
	//					JSONObject mainJsonObject=new JSONObject(response);
	//					String id=mainJsonObject.getString("id");
	//					soldProduct.setId(id);
	//				} catch (JSONException e) {
	//					e.printStackTrace();
	//				}
	//
	//			}else{
	//				soldProduct.isSynchronized=false;
	//			}
	//			addSoldProductToDatabase(soldProduct, false);
	//			closeActivity();
	//		}
	//	};
	//
	//	AsyncResponseHandler updateEventCallBackHandler=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				SharedData.getInstance().soldEventsList.get(selectedItemPosition).isSynchronized=true;
	//			}else{
	//				SharedData.getInstance().soldEventsList.get(selectedItemPosition).isSynchronized=false;
	//			}
	//			addSoldProductToDatabase(SharedData.getInstance().soldEventsList.get(selectedItemPosition), true);
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
