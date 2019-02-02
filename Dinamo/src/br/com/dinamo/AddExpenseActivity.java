package br.com.dinamo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import br.com.custom.screens.DropDownMenuWindow;
import br.com.custom.screens.EndExpenseDialog;
import br.com.data.model.DinamoObject;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.DeleteRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.utilities.NumberTextWatcher;
import br.com.utilities.ScalingUtility;

public class AddExpenseActivity extends Activity {
	private  View expenseView =null;
	private  int launcherActId=1;

	private  Button saveExpenseButton=null;
	private  Button deleteExpensesButton=null;
	private  Button addExpenseButton=null;

	private  EditText priceValueEditor=null;
	private  TextView dateTextView=null;
	private  TextView PeriodTextView=null;
	private  TextView CatTextView=null;

	private int selectedItemPosition=-1;
	private ExpenseProduct expenseProduct=null;

	private RelativeLayout catLayout=null;
	private RelativeLayout periodLayout=null;

	private ListView periodListView=null;
	private ListView addCatListView=null;
	private View     periodDropDownView=null;
	private View     addCatDropDownView=null;
	private LayoutInflater inflater=null;
	private DropDownMenuWindow dropDownWindow=null;
	private DropDownAdapter dropDownAdapter=null;
	private String formattedDateString="";
	private int selectedCatId=-1;
	private int selectedPriodId=0;
	private ProgressDialog progressDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		launcherActId=getIntent().getIntExtra("ID", DinamoConstants.ADD_EVENT_ACTIVITY_ID);
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			expenseView = inflater.inflate(R.layout.add_expenses_layout, null, false);
			SharedData.getInstance().sendScreenName("Add Expense Events Screen");
		}else{
			expenseView = inflater.inflate(R.layout.edit_expenses_layout, null, false);
			selectedItemPosition=getIntent().getIntExtra("POSITION",0);
			SharedData.getInstance().sendScreenName("Expense Event Edit Screen");
		}
		ScalingUtility.getInstance().scaleView(expenseView);
		setContentView(expenseView);

		applyFontsToTextOnScreen();
		initUserClicksListeners();

		if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
			displaySelectedItemData();
		}else{
			findDefaultMensalIndex();
			setDateClicker(null);
		}
	}
	private void findDefaultMensalIndex(){
		String periodValue="Mensal";
		for(int count=0;count<UserDataManager.getInstance().periodicity.size();count++){
			if(periodValue.contentEquals(UserDataManager.getInstance().periodicity.get(count).getName())){
				selectedPriodId=count;
				break;
			}
		}
	}
	private void displaySelectedItemData(){
		String perdiodValue="";
		String CatValue="";
		perdiodValue=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getPeriodicityType().getName();
		CatValue=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getCategory().getName();

		for(int count=0;count<UserDataManager.getInstance().periodicity.size();count++){
			if(perdiodValue.contentEquals(UserDataManager.getInstance().periodicity.get(count).getName())){
				selectedPriodId=count;
				break;

			}
		}
		for(int count=0;count<UserDataManager.getInstance().catagories.size();count++){
			if(CatValue.contentEquals(UserDataManager.getInstance().catagories.get(count).getName())){
				selectedCatId=count;
				break;

			}
		}

		setDateClicker(SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getDate());

		String priceValue=String.format("%.2f", SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getPriceValue());
		priceValue=priceValue.replace(".", ",");
		priceValueEditor.setText(priceValue);

		CatTextView.setText(CatValue);
		PeriodTextView.setText(perdiodValue);


	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.addExpenseHeadTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.ValorStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		priceValueEditor=(EditText)findViewById(R.id.valorEditText);
		SharedData.getInstance().applyFontToTextView(priceValueEditor, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.categoriesStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		CatTextView=(TextView)findViewById(R.id.catgoriesValueTextView);
		SharedData.getInstance().applyFontToTextView(CatTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.dueDateTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		dateTextView=(TextView)findViewById(R.id.dueDateInput);
		SharedData.getInstance().applyFontToTextView(dateTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.periodicityStaticTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		PeriodTextView=(TextView)findViewById(R.id.periodcityValue);
		SharedData.getInstance().applyFontToTextView(PeriodTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);

		if(launcherActId==1){
			textView=(TextView)findViewById(R.id.informExpenseTextView);
			SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			addExpenseButton=(Button)findViewById(R.id.addExpenseButton);
			SharedData.getInstance().applyFontToTextView(addExpenseButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}else{
			saveExpenseButton=(Button)findViewById(R.id.saveExpensesButton);
			deleteExpensesButton=(Button)findViewById(R.id.DeleteExpensesButton);
			SharedData.getInstance().applyFontToTextView(saveExpenseButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
			SharedData.getInstance().applyFontToTextView(deleteExpensesButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		}

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
		RelativeLayout dateLayout=(RelativeLayout)findViewById(R.id.DueDateInputLayout);
		dateLayout.setOnClickListener(dateSelectListener);
	}

	private void initUserClicksListeners(){
		periodDropDownView=inflater.inflate(R.layout.drop_down_list, null, false);
		addCatDropDownView=inflater.inflate(R.layout.add_button_drop_list, null, false);
		ScalingUtility.getInstance().scaleView(periodDropDownView);
		ScalingUtility.getInstance().scaleView(addCatDropDownView);

		periodListView=(ListView)periodDropDownView.findViewById(R.id.generalMenuList);
		addCatListView=(ListView)addCatDropDownView.findViewById(R.id.generalMenuList);

		TextView addButton=(TextView)addCatDropDownView.findViewById(R.id.addButton);
		addButton.setText(getString(R.string.add_categoires_label));

		periodLayout=(RelativeLayout)findViewById(R.id.periodicityInputLayout);
		catLayout=(RelativeLayout)findViewById(R.id.categoriesInputLayout);

		View backIconImage=(View)findViewById(R.id.backClickArea);
		backIconImage.setOnClickListener(backIconClickListener);
		if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
			addExpenseButton.setOnClickListener(addExpenseListener);
		}else{
			saveExpenseButton.setOnClickListener(saveExpenseListener);
			deleteExpensesButton.setOnClickListener(deleteExpenseListener);
		}

		periodLayout.setOnClickListener(periodDropDownOpener);
		catLayout.setOnClickListener(catDropDownOpener);
		addButton.setOnClickListener(addCatListener);

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
		AddExpenseActivity.this.finish();

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
			AddExpenseActivity.this.finish();
		}
	}
	private void addExpenseProductToDatabase(ExpenseProduct product,boolean update){
		long primaryKey=DinamoDatabaseHelper.getInstance().addUpdateExpenseProduct(product,update);

		if(!update){
			product.setPrimaryKey(primaryKey);
			UserDataManager.getInstance().expenseEventsList.add(product);
			product.setNextDueDate(MainScreenActivity.findNextDueDate(product));
			SharedData.getInstance().expenseEventsList.add(product);
		}
		ExpensesMainActivity.refreshAdapter(false);
	}

	private void deleteItemFromLocalData(boolean shouldDeleteFromDb){
		if(shouldDeleteFromDb){
			long primaryKey=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteExpenseProduct(primaryKey);

			SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().expenseEventsList.remove(selectedItemPosition);
		}else{
			SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isDeleted=true;
			SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isSynchronized=false;
			DinamoDatabaseHelper.getInstance().addUpdateExpenseProduct(SharedData.getInstance().expenseEventsList.get(selectedItemPosition), true);

			SharedData.getInstance().expenseEventsList.remove(selectedItemPosition);
		}	
		ExpensesMainActivity.refreshAdapter(true);
		closeActivity();
	}
	private void prepareAddUpdateExpensePost(String url){
		String valuePrice=priceValueEditor.getText().toString();
		valuePrice=valuePrice.replace(",", ".");
		boolean isValidPrice=SharedData.getInstance().isValidDecimalString(valuePrice);

		if(isValidPrice && selectedCatId!=-1){
			double value=Double.valueOf(valuePrice);
			String userId=SharedData.getInstance().currentUser.getServerId();

			String catId=UserDataManager.getInstance().catagories.get(selectedCatId).getId();
			String catName=UserDataManager.getInstance().catagories.get(selectedCatId).getName();
			DinamoObject catObject=new DinamoObject(catId,catName);

			String periodId=UserDataManager.getInstance().periodicity.get(selectedPriodId).getId();
			String periodName=UserDataManager.getInstance().periodicity.get(selectedPriodId).getName();
			DinamoObject periodObject=new DinamoObject(periodId,periodName);
			String endDate="";

			if(launcherActId==DinamoConstants.EDIT_EVENT_ACTIVITY_ID){
				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).setPriceValue(value);
				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).setDate(SharedData.getInstance().convertToDate(formattedDateString));
				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).setCategory(catObject);
				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).setPeriodicityType(periodObject);
				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isSynchronized=false;
				if(SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getEndDate()!=null){
					endDate=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getEndDate().toString();
				}
			}else{
				expenseProduct=new ExpenseProduct();
				expenseProduct.setPriceValue(value);
				expenseProduct.setDate(SharedData.getInstance().convertToDate(formattedDateString));
				expenseProduct.setCategory(catObject);
				expenseProduct.setPeriodicityType(periodObject);
				expenseProduct.setId("");
				expenseProduct.isSynchronized=false;  	
			}


			if(launcherActId==DinamoConstants.ADD_EVENT_ACTIVITY_ID){
				addExpenseProductToDatabase(expenseProduct,false);
			}else{
				addExpenseProductToDatabase(SharedData.getInstance().expenseEventsList.get(selectedItemPosition),true);
			}
			
			SharedData.getInstance().hasItemBeenAdded=true;
			closeActivity();
		}
		else{
			hideDialog();
			SharedData.getInstance().displayMessageAlert(getString(R.string.invalid_alert_data_title),getString(R.string.signup_valid_data_alert), false);

		}


	}
	OnClickListener deleteExpenseListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Delete Expense Event Button");
			EndExpenseDialog expenseDialog=new EndExpenseDialog(AddExpenseActivity.this);
			expenseDialog.showDialog();
		}
	};
	public void removeFromCalAction(Date endDate){
		String itemId=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getId();
		SharedData.getInstance().expenseEventsList.get(selectedItemPosition).setEndDate(endDate);
		if(itemId.equalsIgnoreCase("") || !SharedData.getInstance().isInternetAvailable()){
			addExpenseProductToDatabase(SharedData.getInstance().expenseEventsList.get(selectedItemPosition), true);
		}else{
			showProgressDialog(getString(R.string.aguarde_apagado));
			String url=DinamoConstants.EXPENSES_EVENT_URL+"/"+SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getId();
			prepareAddUpdateExpensePost(url);
		}
	}
	public void performDeleteAction(){
		String itemId=SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getId();
		if(itemId.equalsIgnoreCase("")){
			deleteItemFromLocalData(true);
		}else if(!SharedData.getInstance().isInternetAvailable()){
			deleteItemFromLocalData(false);
		}else{
			showProgressDialog(getString(R.string.aguarde_apagado));
			DeleteRequest delteRequest=new DeleteRequest(deleteCallBackHandler, DinamoConstants.EXPENSES_EVENT_URL+"/"+SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getId());
			SharedData.executeTask(delteRequest);
		}
	}
	OnClickListener periodDropDownOpener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dropDownWindow=new DropDownMenuWindow(periodDropDownView,DinamoConstants.MAIN_DROP_WIDTH,20,-20);
			dropDownWindow.showCalculatorMenu(periodLayout);	
			dropDownAdapter=new DropDownAdapter(UserDataManager.getInstance().periodicity);
			periodListView.setAdapter(dropDownAdapter);
			periodListView.setOnItemClickListener(periodListItemListener);

		}
	};
	OnClickListener catDropDownOpener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dropDownWindow=new DropDownMenuWindow(addCatDropDownView,DinamoConstants.MAIN_DROP_WIDTH,20,-20);
			dropDownWindow.showCalculatorMenu(catLayout);	
			dropDownAdapter=new DropDownAdapter(UserDataManager.getInstance().catagories);
			addCatListView.setAdapter(dropDownAdapter);	
			addCatListView.setOnItemClickListener(catListItemListener);
		}
	};
	OnItemClickListener periodListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			PeriodTextView.setText(UserDataManager.getInstance().periodicity.get(index).getName());
			selectedPriodId=index;
			dropDownWindow.dismissWindow();
		}
	};
	OnItemClickListener catListItemListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {	
			CatTextView.setText(UserDataManager.getInstance().catagories.get(index).getName());
			selectedCatId=index;
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
			DatePickerDialog datePickDialog=new DatePickerDialog(AddExpenseActivity.this,datePickerListener, year, month, day);
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

			if(dayOfMonth==31 || (monthOfYear==1 && dayOfMonth==29) ){
				String title=getResources().getString(R.string.invlaid_date_title);
				String message=getResources().getString(R.string.invlaid_date_message);
				SharedData.getInstance().displayMessageAlert(title, message, false);
			}else{
				formattedDateString=""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+hour+":"+mint+":"+second;
				dateTextView.setText(""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
			}
		}
	};
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnClickListener addCatListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AddDialog addDialog=new AddDialog(dropDownAdapter,DinamoConstants.GET_CAT_REQ_ID);
			addDialog.showDialog();

		}
	};
	OnClickListener addExpenseListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_adicionado));
			SharedData.getInstance().sendEventNameToAnalytics("Add Expense Event Button 2");
			prepareAddUpdateExpensePost(DinamoConstants.EXPENSES_EVENT_URL);
		}
	};
	OnClickListener saveExpenseListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog(getString(R.string.aguarde_salvo));
			SharedData.getInstance().sendEventNameToAnalytics("Save Expense Event Button");
			prepareAddUpdateExpensePost(DinamoConstants.EXPENSES_EVENT_URL+"/"+SharedData.getInstance().expenseEventsList.get(selectedItemPosition).getId());
		}
	};
	//	AsyncResponseHandler addEventCallBackListener=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				expenseProduct.isSynchronized=true;
	//				try {
	//					JSONObject mainJsonObject=new JSONObject(response);
	//					String id=mainJsonObject.getString("id");
	//					expenseProduct.setId(id);
	//				} catch (JSONException e) {
	//					e.printStackTrace();
	//				}
	//
	//			}else{
	//				expenseProduct.isSynchronized=false;
	//			}
	//
	//			addExpenseProductToDatabase(expenseProduct, false);
	//			closeActivity();
	//		}
	//	};
	//	AsyncResponseHandler updateEventCallBackHandler=new AsyncResponseHandler() {
	//
	//		@Override
	//		public void onResult(String response,int responseCode) {
	//			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
	//				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isSynchronized=true;
	//			}else{
	//				SharedData.getInstance().expenseEventsList.get(selectedItemPosition).isSynchronized=false;
	//			}
	//			addExpenseProductToDatabase(SharedData.getInstance().expenseEventsList.get(selectedItemPosition), true);
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
