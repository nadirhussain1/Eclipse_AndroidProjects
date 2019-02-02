package br.com.dinamo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.data.model.BoughtProduct;
import br.com.data.model.DinamoObject;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.SoldProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncImageGetHandler;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.DeleteRequest;
import br.com.requests.GetPhotoRequest;
import br.com.requests.GetRequest;
import br.com.requests.ImageUploadRequest;
import br.com.requests.LoadDatabaseHandler;
import br.com.requests.PostRequest;
import br.com.requests.PutRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.storage.DinamoPrefernces;
import br.com.utilities.ScalingUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainScreenActivity extends Activity {
	private ArrayList<GetRequest> loadDataRequests=null;
	private ArrayList<GetPhotoRequest> photoLoadRequests=null;
	private int currRunningGetRequestIndex=0;
	private int currentSyncReqIndex=0;
	private int arrayIndex=0;

	private ArrayList<DinamoObject> unSyncEstablishments=null;
	private ArrayList<DinamoObject> unSynCategories=null;
	private ArrayList<DinamoObject> unSynProducts=null;
	private ArrayList<BoughtProduct> unSynBoughtProducts=null;
	private ArrayList<SoldProduct>  unSynSoldProducts=null;
	private ArrayList<ExpenseProduct> unsyncExpenseProducts=null;

	private Button buyEventButton =null;
	private Button sellEventButton =null;
	private Button expensesButton=null;
	private Button reportsScreenButton=null;
	private TextView UserNameTitleView=null;
	private ImageView userPhotoImageView=null;
	private RelativeLayout syncLayout=null;

	String PUSH_SENDER_ID = "511832716560";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;
	String TAG="MainActivity";
	ProgressDialog progressDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		loadPreferencesData();

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View signUpView = inflater.inflate(R.layout.main_screen_layout, null, false);
		ScalingUtility.getInstance().scaleView(signUpView);
		setContentView(signUpView);

		SharedData.getInstance().sendScreenName("Home Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();

		setGCMPushNotifications();
		loadUserData();

	}
	@Override
	protected void onDestroy() {
		UserDataManager.getInstance().onDestroy();
		SharedData.getInstance().resetInstance();
		super.onDestroy();

	}
	@Override
	protected void onResume() {
		super.onResume();	
		checkPlayServices();
		showSyncDialogAfterAday();
	}
	@Override
	public void onBackPressed() {
		if(progressDialog!=null && progressDialog.isShowing()){
			return;
		}else{
			MainScreenActivity.this.finish();
		}
	}
	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	private void setGCMPushNotifications(){
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = DinamoPrefernces.getInstance(this).getRegistrationId();
			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,DinamoConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}
	private void loadPreferencesData(){	
		int status=DinamoPrefernces.getInstance(this).getUserLogInStatus();
		if(status==DinamoConstants.MAIN_SCREEN){
			SharedData.getInstance().currentUser=DinamoPrefernces.getInstance(this).getCurrentUser();
		}
		DinamoPrefernces.getInstance(this).saveUserLogInStatus(DinamoConstants.MAIN_SCREEN);
	}
	private void loadUserData(){
		SharedData.getInstance().monthsNameList=getResources().getStringArray(R.array.months_names);
		SharedData.getInstance().shortMonthNameList=getResources().getStringArray(R.array.short_months_names);
		SharedData.getInstance().daysNames=getResources().getStringArray(R.array.days_names);

		if(SharedData.getInstance().currentUser.getUserPhoto() !=null){
			userPhotoImageView.setImageBitmap(SharedData.getInstance().currentUser.getUserPhoto());
		}

		if(SharedData.getInstance().currentUser.isInitFromServer==0){
			if(SharedData.getInstance().isInternetAvailable()){
				showProgressDialog("Aguarde! Carregando dados iniciais do servidor");
				loadDataFromServer();
			}else{
				SharedData.getInstance().displayMessageAlert(getString(R.string.syn_alert_title),getString(R.string.syn_alert_message), false);
			}
		}
		//		else if(SharedData.getInstance().isInternetAvailable()){
		//			showProgressDialog("Aguarde! Sincronizando seus dados com o servidor");
		//			synchronizeData();
		else{
			showProgressDialog("Internet indisponivel. Carregando dados locais");
			loadDataFromDatabase();
		}
	}
	private void loadDataFromDatabase(){
		LoadDatabaseHandler loader=new LoadDatabaseHandler(databaseCallBackHandler);
		SharedData.executeTask(loader);
	}
	private void loadDataFromServer(){
		loadDataRequests=new ArrayList<GetRequest>();
		GetRequest request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PAYMENT_METHODS_URL, DinamoConstants.GET_PAY_METHOD_REQ_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PERIODICITY_URL, DinamoConstants.GET_PERIODICITY_REQ_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_CATEGORIES_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_CAT_REQ_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_ESTABLISHMENTS_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_EST_REQ_ID);
		loadDataRequests.add(request);


		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PRODUCTS_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_PROD_REQ_ID);
		loadDataRequests.add(request);

		//Get Default Products, Establishments, Categories

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PUBLIC_CATEGORIES_URL, DinamoConstants.GET_CAT_REQ_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PUBLIC_ESTABLISHMENTS_URL, DinamoConstants.GET_EST_REQ_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.GET_PUBLIC_PRODUCTS_URL, DinamoConstants.GET_PROD_REQ_ID);
		loadDataRequests.add(request);

		//end default

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.BUY_ALL_EVENTS_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_BUY_EVENTS_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.SELL_ALL_EVENTS_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_SELL_EVENTS_ID);
		loadDataRequests.add(request);

		request=new GetRequest(loadDataResponseHandler,DinamoConstants.EXPENSES_ALL_EVENT_URL+SharedData.getInstance().currentUser.getServerId(), DinamoConstants.GET_EXPENSE_EVENTS_ID);
		loadDataRequests.add(request);

		SharedData.executeTask(loadDataRequests.get(currRunningGetRequestIndex));
	}
	private void loadBuyEventPhotos(){
		photoLoadRequests=new ArrayList<GetPhotoRequest>();
		int size=UserDataManager.getInstance().boughtEventsList.size();
		GetPhotoRequest photoRequest=null;

		if(size>0){
			for(int count=0;count<size;count++){
				String id=UserDataManager.getInstance().boughtEventsList.get(count).getId();
				String url=DinamoConstants.BUY_EVENT_URL+"/"+id+"/photo";
				photoRequest=new GetPhotoRequest(buyEventImageCallBackListener, url,count);
				photoLoadRequests.add(photoRequest);
			}
			SharedData.executeTask(photoLoadRequests.get(0));
		}
		else{
			//loadUserPhoto();
			saveServerDownloadedDataToDatabase();
		}

	}
	private void applyFontsToTextOnScreen(){
		userPhotoImageView=(ImageView)findViewById(R.id.userPhoto);

		UserNameTitleView=(TextView)findViewById(R.id.mainScreenUserNameView);
		SharedData.getInstance().applyFontToTextView(UserNameTitleView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		sellEventButton=(Button)findViewById(R.id.sellEventButton);
		SharedData.getInstance().applyFontToTextView(sellEventButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		buyEventButton=(Button)findViewById(R.id.buyEventButton);
		SharedData.getInstance().applyFontToTextView(buyEventButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		expensesButton=(Button)findViewById(R.id.expenseEventButton);
		SharedData.getInstance().applyFontToTextView(expensesButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		reportsScreenButton=(Button)findViewById(R.id.reportsMainButton);
		SharedData.getInstance().applyFontToTextView(reportsScreenButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		syncLayout=(RelativeLayout)findViewById(R.id.syncLayout);
		TextView syncLabel=(TextView)findViewById(R.id.syncLabel);
		SharedData.getInstance().applyFontToTextView(syncLabel, DinamoConstants.HELVETICA_NEUE_CONDENSED);

		UserNameTitleView.setText(SharedData.getInstance().currentUser.getUserName());

	}
	private void initUserClicksListeners(){
		buyEventButton.setOnClickListener(buyEventButtonClickListener);
		sellEventButton.setOnClickListener(sellEventButtonClickListener);
		expensesButton.setOnClickListener(expensesButtonClickListener);
		reportsScreenButton.setOnClickListener(reportEventClickListener);
		syncLayout.setOnClickListener(syncClickListener);

		Button logOutButton=(Button)findViewById(R.id.logoutButton);
		logOutButton.setOnClickListener(logOutCliclListener);
		controlClick(false);
	}
	private void controlClick(boolean enabled){
		buyEventButton.setClickable(enabled);
		sellEventButton.setClickable(enabled);
		expensesButton.setClickable(enabled);
		reportsScreenButton.setClickable(enabled);
	}
	private void launchBuyEventButtonScreen(){
		int size=UserDataManager.getInstance().boughtEventsList.size();

		if(SharedData.getInstance().boughtEventsList==null){
			SharedData.getInstance().boughtEventsList=new ArrayList<BoughtProduct>();
		}else{
			SharedData.getInstance().boughtEventsList.clear();
		}

		for(int count=0;count<size;count++){
			BoughtProduct clone=UserDataManager.getInstance().boughtEventsList.get(count);
			SharedData.getInstance().boughtEventsList.add(clone);
		}	
		controlClick(true);

		Intent mainIntent = new Intent(MainScreenActivity.this, BuyEventsMainActivity.class);
		mainIntent.putExtra("HideAdd", false);
		startActivity(mainIntent);
	}
	private void launchSellEventButtonScreen(){
		int size=UserDataManager.getInstance().soldEventsList.size();

		if(SharedData.getInstance().soldEventsList==null){
			SharedData.getInstance().soldEventsList=new ArrayList<SoldProduct>();
		}else{
			SharedData.getInstance().soldEventsList.clear();
		}

		for(int count=0;count<size;count++){
			SoldProduct clone=UserDataManager.getInstance().soldEventsList.get(count);
			SharedData.getInstance().soldEventsList.add(clone);
		}	
		controlClick(true);
		Intent mainIntent = new Intent(MainScreenActivity.this, SellEventsMainActivity.class);
		startActivity(mainIntent);
	}
	private void launchExpensesMainScreen(){
		int size=UserDataManager.getInstance().expenseEventsList.size();
		if(SharedData.getInstance().expenseEventsList==null){
			SharedData.getInstance().expenseEventsList=new ArrayList<ExpenseProduct>();
		}else{
			SharedData.getInstance().expenseEventsList.clear();
		}

		for(int count=0;count<size;count++){
			ExpenseProduct clone=UserDataManager.getInstance().expenseEventsList.get(count);
			Date currentDate=new Date();
			Date endDate=clone.getEndDate();

			if((endDate==null || (endDate!=null && endDate.after(currentDate)))){
				clone.setNextDueDate(findNextDueDate(clone));
				SharedData.getInstance().expenseEventsList.add(clone);
			}
		}	

		controlClick(true);
		Intent mainIntent = new Intent(MainScreenActivity.this, ExpensesMainActivity.class);
		startActivity(mainIntent);	
	}
	private void synchronizeDataChecks(){
		if(!isUnSyncData()){
			Toast.makeText(this,getString(R.string.backup_already_sync), Toast.LENGTH_SHORT).show();
		}else{
			startSyncProcess();
		}
	}
	private boolean isUnSyncData(){
		unSyncEstablishments=DinamoDatabaseHelper.getInstance().getUnSyncEstablishments();
		unSynCategories=DinamoDatabaseHelper.getInstance().getUnSyncCategories();
		unSynProducts=DinamoDatabaseHelper.getInstance().getUnSyncProducts();
		unSynBoughtProducts=DinamoDatabaseHelper.getInstance().getUnSyncBoughtProducts();
		unSynSoldProducts=DinamoDatabaseHelper.getInstance().getUnSyncSoldProducts();
		unsyncExpenseProducts=DinamoDatabaseHelper.getInstance().getUnSyncEexpenses(); 

		if(unSyncEstablishments.size()==0 && unSynCategories.size()==0 && unSynProducts.size()==0 && unSynBoughtProducts.size()==0 && unSynSoldProducts.size()==0 && unsyncExpenseProducts.size()==0){
			return false;
		}
		return true;
	}
	private void showSyncDialogAfterAday(){
		long lastSyncTime=DinamoPrefernces.getInstance(this).getLastSyncTime();
		long timePassed=(System.currentTimeMillis()-lastSyncTime)/(1000*60*60);
		
		if(SharedData.getInstance().hasItemBeenAdded){
			SharedData.getInstance().hasItemBeenAdded=false;
			isUnSyncData();
			
			String title=getString(R.string.sync_day_alert);
			String message=getString(R.string.sync_added_message);;
			showSyncDialog(title,message);
		}
		else if(timePassed>=24 && isUnSyncData()){
			String title=getString(R.string.sync_day_alert);
			String message=getString(R.string.sync_day_message);
			
			showSyncDialog(title,message);	
		}

	}
	private void showSyncDialog(String title,String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(title);

		alertDialogBuilder
		.setMessage(message)
		.setIcon(R.drawable.ic_launcher)
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				startSyncProcess();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});


		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	private void startSyncProcess(){
		currentSyncReqIndex=0;
		if (checkConn(getBaseContext())) {
			showProgressDialog("Aguarde! Sincronizando seus dados com o servidor");
			DinamoPrefernces.getInstance(this).saveLastSyncTime(System.currentTimeMillis());
			manageSynRequests();
		}
		else {
			Toast.makeText(this,getString(R.string.backup_sync_offline), Toast.LENGTH_SHORT).show();	
		}
	}
	private void manageSynRequests(){
		if(currentSyncReqIndex==0){
			if(arrayIndex<unSyncEstablishments.size()){
				launchEstabPostRequest(arrayIndex);
				return;
			}else{
				currentSyncReqIndex=1; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==1){
			if(arrayIndex<unSynCategories.size()){
				launchCatPostRequest(arrayIndex);
				return;

			}else{
				currentSyncReqIndex=2; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==2){
			if(arrayIndex<unSynProducts.size()){
				launchProductsPostRequest(arrayIndex);
				return; 
			}else{
				currentSyncReqIndex=3; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==3){
			if(arrayIndex<unSynBoughtProducts.size()){
				launchBoughtSynRequest(arrayIndex);
				return; 
			}else{
				currentSyncReqIndex=4; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==4){
			if(arrayIndex<unSynBoughtProducts.size()){
				launchBoughtPhotosSynRequest(arrayIndex);
				return; 
			}else{
				currentSyncReqIndex=5; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==5){
			if(arrayIndex<unSynSoldProducts.size()){
				launchSoldSynRequest(arrayIndex);
				return;  
			}else{
				currentSyncReqIndex=6; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex==6){
			if(arrayIndex<unsyncExpenseProducts.size()){
				launchExpenseSynRequest(arrayIndex);
				return;
			}else{
				currentSyncReqIndex=7; 
				arrayIndex=0;
			}	 
		}
		if(currentSyncReqIndex>=7){
			unSyncEstablishments.clear();
			unSynCategories.clear();
			unSynProducts.clear();
			unSynBoughtProducts.clear();
			unSynSoldProducts.clear();
			unsyncExpenseProducts.clear();

			progressDialog.cancel();
			Toast.makeText(this,getString(R.string.backup_sync_ok), Toast.LENGTH_SHORT).show();
			//			progressDialog.setMessage("Loading offline data from database");
			//			loadDataFromDatabase();
		}

	}
	private void launchEstabPostRequest(int index){
		String name=unSyncEstablishments.get(index).getName();
		JSONObject json=constructCommonJson(name);
		PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_ESTABLISHMENT_URL, json,syncCallbackListener);
		SharedData.executeTask(postRequest);
	}
	private void launchCatPostRequest (int index){
		String name=unSynCategories.get(index).getName();
		JSONObject json=constructCommonJson(name);
		PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_CATEGORIES_URL, json,syncCallbackListener);
		SharedData.executeTask(postRequest);
	}
	private void launchProductsPostRequest(int index){
		String name=unSynProducts.get(index).getName();
		JSONObject json=constructCommonJson(name);
		PostRequest postRequest=new PostRequest(DinamoConstants.INSERT_PRODUCT_URL, json,syncCallbackListener);
		SharedData.executeTask(postRequest);
	}
	private void launchBoughtSynRequest(int index){
		JSONObject jsonObject=constructBoughtJSon(unSynBoughtProducts.get(index));
		String id=unSynBoughtProducts.get(index).getId();
		boolean isDeleted=unSynBoughtProducts.get(index).isDeleted;

		if(id.trim().length()>0){
			if(isDeleted){
				DeleteRequest delteRequest=new DeleteRequest(deleteSynCallBackListener, DinamoConstants.BUY_EVENT_URL+"/"+unSynBoughtProducts.get(index).getId());
				SharedData.executeTask(delteRequest);
			}else{
				String url=DinamoConstants.BUY_EVENT_URL+"/"+unSynBoughtProducts.get(index).getId();
				PutRequest putRequest=new PutRequest(url, jsonObject,syncCallbackListener);
				SharedData.executeTask(putRequest);
			}
		}else{	
			String url=DinamoConstants.BUY_EVENT_URL;
			PostRequest postRequest=new PostRequest(url, jsonObject,syncCallbackListener);
			SharedData.executeTask(postRequest);
		}
	}
	private void launchBoughtPhotosSynRequest(int index){
		if(unSynBoughtProducts.get(index).getProductPhoto() !=null){
			String eventServerId=unSynBoughtProducts.get(index).getId();
			String url=DinamoConstants.BUY_EVENT_URL+"/"+eventServerId+"/photo";
			ImageUploadRequest uploadRequest=new ImageUploadRequest(url,unSynBoughtProducts.get(index).getProductPhoto(),syncCallbackListener);
			SharedData.executeTask(uploadRequest);
		}else{
			arrayIndex++;
			manageSynRequests();
		}
	}
	private void launchSoldSynRequest(int index){
		JSONObject jsonObject=constructSoldJson(unSynSoldProducts.get(index));
		String id=unSynSoldProducts.get(index).getId();
		boolean isDeleted=unSynSoldProducts.get(index).isDeleted;

		if(id.trim().length()>0){
			if(isDeleted){
				DeleteRequest delteRequest=new DeleteRequest(deleteSynCallBackListener, DinamoConstants.SELL_EVENT_URL+"/"+unSynSoldProducts.get(index).getId());
				SharedData.executeTask(delteRequest);
			}else{
				String url=DinamoConstants.SELL_EVENT_URL+"/"+unSynSoldProducts.get(index).getId();
				PutRequest putRequest=new PutRequest(url, jsonObject,syncCallbackListener);
				SharedData.executeTask(putRequest);
			}
		}else{	
			String url=DinamoConstants.SELL_EVENT_URL;
			PostRequest postRequest=new PostRequest(url, jsonObject,syncCallbackListener);
			SharedData.executeTask(postRequest);
		}
	}
	private void launchExpenseSynRequest(int index){
		JSONObject jsonObject=constructExpenseJson(unsyncExpenseProducts.get(index));
		String id=unsyncExpenseProducts.get(index).getId();
		boolean isDeleted=unsyncExpenseProducts.get(index).isDeleted;

		if(id.trim().length()>0){
			if(isDeleted){
				DeleteRequest delteRequest=new DeleteRequest(deleteSynCallBackListener, DinamoConstants.EXPENSES_EVENT_URL+"/"+unsyncExpenseProducts.get(index).getId());
				SharedData.executeTask(delteRequest);
			}else{
				String url=DinamoConstants.EXPENSES_EVENT_URL+"/"+unsyncExpenseProducts.get(index).getId();
				PutRequest putRequest=new PutRequest(url, jsonObject,syncCallbackListener);
				SharedData.executeTask(putRequest);
			}
		}else{
			String url=DinamoConstants.EXPENSES_EVENT_URL;
			PostRequest postRequest=new PostRequest(url, jsonObject,syncCallbackListener);
			SharedData.executeTask(postRequest);
		}
	}
	private JSONObject constructCommonJson(String nameValue){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("name", nameValue);
			jsonObject.put("user", SharedData.getInstance().currentUser.getServerId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;

	}
	private JSONObject constructBoughtJSon(BoughtProduct product){	
		String establishmentId=product.getEstablishment().getId();
		if(establishmentId.trim().length()==0){
			establishmentId=DinamoDatabaseHelper.getInstance().getEstablishmentId(product.getEstablishment().getName());
		}
		String userId=SharedData.getInstance().currentUser.getServerId();
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("value",product.getPriceValue());
			jsonObject.put("paymentdate",formatDate(product.getDate()));
			jsonObject.put("establishment",establishmentId);
			jsonObject.put("user",userId);
			jsonObject.put("paymentmethod",product.getPayMethod().getId());
			jsonObject.put("notes",product.getNotes());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;

	}
	private JSONObject constructSoldJson(SoldProduct product){
		String productId=product.getProduct().getId();
		if(productId.trim().length()==0){
			productId=DinamoDatabaseHelper.getInstance().getProductId(product.getProduct().getName());
		}
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("value",product.getPriceValue());
			jsonObject.put("receiptdate",formatDate(product.getDate()));
			jsonObject.put("product",productId);
			jsonObject.put("user",SharedData.getInstance().currentUser.getServerId());
			jsonObject.put("paymentmethod",product.getPayMethod().getId());
			jsonObject.put("quantity",product.getQuantity());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	private JSONObject constructExpenseJson(ExpenseProduct product){
		String catId=product.getCategory().getId();
		if(catId.trim().length()==0){
			catId=DinamoDatabaseHelper.getInstance().getCategoryId(product.getCategory().getName());
		}

		JSONObject jsonObject=new JSONObject();
		String endDate="";
		if(product.getEndDate()!=null){
			endDate=product.getEndDate().toString();
		}
		try {
			jsonObject.put("value",product.getPriceValue());
			jsonObject.put("startdate",  formatDate(product.getDate()));
			jsonObject.put("periodicity",product.getPeriodicityType().getId());
			jsonObject.put("user",SharedData.getInstance().currentUser.getServerId());
			jsonObject.put("category",catId);
			jsonObject.put("enddate",endDate);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
	private String formatDate(Date date){
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

		String formattedDateString=""+year+"-"+(month+1)+"-"+day+" "+hour+":"+mint+":"+second;
		return formattedDateString;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		RegisterTask task=new RegisterTask();
		SharedData.executeTask(task);

	}
	private void sendRegistrationIdToBackend() {
		if(SharedData.getInstance().isInternetAvailable()){
			try {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("token", regid);
				jsonObject.put("platform","android");
				jsonObject.put("user_id",SharedData.getInstance().currentUser.getServerId());
				PostRequest postPushTokenRequest=new PostRequest(DinamoConstants.PUSH_TOKEN_URL, jsonObject,pushCallBackListener);
				SharedData.executeTask(postPushTokenRequest);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


	}
	AsyncResponseHandler databaseCallBackHandler=new AsyncResponseHandler() {
		@Override
		public void onResult(String response,int responseCode) {
			controlClick(true);
			progressDialog.cancel();

		}
	};
	AsyncResponseHandler loadDataResponseHandler=new AsyncResponseHandler() {
		@Override
		public void onResult(String response,int responseCode) {
			currRunningGetRequestIndex++;
			if(currRunningGetRequestIndex<loadDataRequests.size()){
				loadDataRequests.get(currRunningGetRequestIndex).execute();
			}else{
				loadBuyEventPhotos();
			}
		}
	};
	AsyncResponseHandler syncCallbackListener=new AsyncResponseHandler() {
		@Override
		public void onResult(String response,int responseCode) {
			String id="";
			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS || responseCode==DinamoConstants.HTTP_PUT_SUCCESS){
				try {
					JSONObject mainJsonObject=new JSONObject(response);
					id=mainJsonObject.getString("id");
					updateModelAndDatabaseSynchronized(id);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			arrayIndex++;
			manageSynRequests();
		}
	};
	AsyncResponseHandler deleteSynCallBackListener=new AsyncResponseHandler() {
		@Override
		public void onResult(String response,int responseCode) {
			if(responseCode==DinamoConstants.HTTP_GET_SUCCESS){
				deleteSynchronizedItemsfromDb();
			}
			arrayIndex++;
			manageSynRequests();
		}
	};
	private void updateModelAndDatabaseSynchronized(String id){
		if(currentSyncReqIndex==0){
			unSyncEstablishments.get(arrayIndex).isSynChronized=true;
			unSyncEstablishments.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateEstablishment(unSyncEstablishments.get(arrayIndex), true);
		}else if(currentSyncReqIndex==1){
			unSynCategories.get(arrayIndex).isSynChronized=true;
			unSynCategories.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateCategory(unSynCategories.get(arrayIndex), true);
		}else if(currentSyncReqIndex==2){
			unSynProducts.get(arrayIndex).isSynChronized=true;
			unSynProducts.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateProduct(unSynProducts.get(arrayIndex), true);
		}else if(currentSyncReqIndex==3 || currentSyncReqIndex==4){
			if(currentSyncReqIndex==4 || unSynBoughtProducts.get(arrayIndex).getProductPhoto()==null){
				unSynBoughtProducts.get(arrayIndex).isSynchronized=true;
			}
			unSynBoughtProducts.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateBoughtProduct(unSynBoughtProducts.get(arrayIndex), true);
		}else if(currentSyncReqIndex==5){
			unSynSoldProducts.get(arrayIndex).isSynchronized=true;
			unSynSoldProducts.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateSoldProduct(unSynSoldProducts.get(arrayIndex), true);
		}else if(currentSyncReqIndex==6){
			unsyncExpenseProducts.get(arrayIndex).isSynchronized=true;
			unsyncExpenseProducts.get(arrayIndex).setId(id);
			DinamoDatabaseHelper.getInstance().addUpdateExpenseProduct(unsyncExpenseProducts.get(arrayIndex), true);	
		}
	}
	private void deleteSynchronizedItemsfromDb(){
		if(currentSyncReqIndex==3){
			long primarykey=unSynBoughtProducts.get(arrayIndex).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteBoughtProduct(primarykey);
		}else if(currentSyncReqIndex==4){
			long primarykey=unSynSoldProducts.get(arrayIndex).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteSoldProduct(primarykey);
		}else if(currentSyncReqIndex==5){
			long primarykey=unsyncExpenseProducts.get(arrayIndex).getPrimaryKey();
			DinamoDatabaseHelper.getInstance().deleteExpenseProduct(primarykey);	
		}
	}
	private void saveServerDownloadedDataToDatabase(){

		//Inital data Error Handling
		if (UserDataManager.getInstance().paymentMethods.isEmpty() || UserDataManager.getInstance().periodicity.isEmpty()) {
			controlClick(true);
			progressDialog.cancel();
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainScreenActivity.this);
			alertDialog.setMessage(R.string.inital_data_invalid).setCancelable(false)
			.setPositiveButton("Tentar Novamente", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					SharedData.getInstance().currentUser.isInitFromServer=0;
					DinamoDatabaseHelper.getInstance().updateUser(SharedData.getInstance().currentUser);
					finish();
				}
			});
			alertDialog.show();
		}
		else {
			DinamoDatabaseHelper.getInstance().addPayMethods(UserDataManager.getInstance().paymentMethods);
			DinamoDatabaseHelper.getInstance().addPeriodicities(UserDataManager.getInstance().periodicity);
			DinamoDatabaseHelper.getInstance().addProducts(UserDataManager.getInstance().products);
			DinamoDatabaseHelper.getInstance().addCatgories(UserDataManager.getInstance().catagories);
			DinamoDatabaseHelper.getInstance().addEstablishments(UserDataManager.getInstance().establishments);
			DinamoDatabaseHelper.getInstance().addExpenseProducts(UserDataManager.getInstance().expenseEventsList);
			DinamoDatabaseHelper.getInstance().addSoldProducts(UserDataManager.getInstance().soldEventsList);
			DinamoDatabaseHelper.getInstance().addBoughtProducts(UserDataManager.getInstance().boughtEventsList);

			//Log.i("paymentmethods:",UserDataManager.getInstance().paymentMethods.toString());
			//Log.i("periodicity:",UserDataManager.getInstance().periodicity.toString());			

			SharedData.getInstance().currentUser.isInitFromServer=1;
			DinamoDatabaseHelper.getInstance().updateUser(SharedData.getInstance().currentUser);
			DinamoPrefernces.getInstance(MainScreenActivity.this).updateUserDownloadStatus();

			controlClick(true);
			progressDialog.cancel();
		}
	}
	public static Date findNextDueDate(ExpenseProduct product){
		Date nextDueDate=null;
		Calendar cal=Calendar.getInstance();
		cal.setTime(product.getDate());

		int dueDay=cal.get(Calendar.DAY_OF_MONTH);
		int dueMonth=cal.get(Calendar.MONTH);
		int dueYear=cal.get(Calendar.YEAR);

		Date currentDate=new Date();
		cal.setTime(currentDate);
		int currentDay=cal.get(Calendar.DAY_OF_MONTH);
		int currentMonth=cal.get(Calendar.MONTH);
		int currentYear=cal.get(Calendar.YEAR);


		String periodicity=product.getPeriodicityType().getName();

		if(periodicity.equalsIgnoreCase(DinamoConstants.WEEK_PERIODCITY)){
			cal.setTime(product.getDate());
			if(product.getDate().before(currentDate)){
				long difference=currentDate.getTime()-product.getDate().getTime();
				float days=(difference/(1000*60*60*24));
				int weeks=(int) Math.ceil(days/7);

				cal.setTime(product.getDate());
				cal.add(Calendar.DAY_OF_MONTH, weeks*7);
			}

		}else if(periodicity.equalsIgnoreCase(DinamoConstants.MONTH_PERIODICITY)){
			cal.setTime(product.getDate());
			if(product.getDate().before(currentDate)){
				cal.set(Calendar.YEAR, currentYear);
				int month=currentMonth+1;
				if(month==1 && dueDay>28){
					dueDay=28;
				}
				cal.set(Calendar.MONTH, month);
				cal.set(Calendar.DAY_OF_MONTH, dueDay);
			}
		}else if(periodicity.equalsIgnoreCase(DinamoConstants.ANNUAL_PERIODICITY)){
			cal.setTime(product.getDate());
			if(product.getDate().before(currentDate) ){
				cal.set(Calendar.YEAR, (currentYear+1));
				cal.set(Calendar.MONTH, dueMonth);
				cal.set(Calendar.DAY_OF_MONTH, dueDay);
			}
		}
		String formattedDateString=""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" 00"+":00"+":00";
		nextDueDate=SharedData.getInstance().convertToDate(formattedDateString);

		return nextDueDate;
	}

	AsyncImageGetHandler buyEventImageCallBackListener=new AsyncImageGetHandler() {
		@Override
		public void onResult(Bitmap  bitmap,int responseCode,int requestId) {
			if(responseCode==DinamoConstants.HTTP_GET_SUCCESS){
				if(requestId<UserDataManager.getInstance().boughtEventsList.size()){
					BoughtProduct boughtProduct=UserDataManager.getInstance().boughtEventsList.get(requestId);
					boughtProduct.setProductPhoto(bitmap);
					DinamoDatabaseHelper.getInstance().addUpdateBoughtProduct(boughtProduct, true);	
				}
			}
			if((requestId+1)<(photoLoadRequests.size())){
				photoLoadRequests.get(requestId+1).execute();
			}else{
				//loadUserPhoto();
				saveServerDownloadedDataToDatabase();
			}
		}
	};
	AsyncResponseHandler pushCallBackListener=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			if(responseCode==DinamoConstants.HTTP_POST_SUCCESS){
				Log.d("Push","Response"+response);
			}
			else{
				Log.d("Push","Response"+response);
			}
		}
	};

	private OnClickListener buyEventButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			controlClick(false);
			SharedData.getInstance().sendEventNameToAnalytics("Buy Events Main Button");
			launchBuyEventButtonScreen();

		}
	};
	private OnClickListener sellEventButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			controlClick(false);
			SharedData.getInstance().sendEventNameToAnalytics("Sell Events Main Button");
			launchSellEventButtonScreen();
		}
	};
	private OnClickListener logOutCliclListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			DinamoDatabaseHelper.getInstance().closeExistingDatabase();
			DinamoPrefernces.getInstance(MainScreenActivity.this).clearPreferencesData();

			Intent intent=new Intent(MainScreenActivity.this,LogInActivity.class);
			startActivity(intent);
			MainScreenActivity.this.finish();

		}
	};
	private OnClickListener expensesButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			controlClick(false);
			SharedData.getInstance().sendEventNameToAnalytics("Expenses Events Main Button");
			launchExpensesMainScreen();

		}
	};
	private OnClickListener reportEventClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Results Main Button");
			Intent mainIntent = new Intent(MainScreenActivity.this, FinalResultActivity.class);
			startActivity(mainIntent);	

		}
	};
	private OnClickListener syncClickListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			synchronizeDataChecks();	
		}
	};

	private static boolean checkConn(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

	private class RegisterTask extends AsyncTask<Object,Void,Object>{

		@Override
		protected String doInBackground(Object... params) {
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(MainScreenActivity.this);
				}
				regid = gcm.register(PUSH_SENDER_ID);
				msg = "Device registered, registration ID=" + regid;
				sendRegistrationIdToBackend();
				DinamoPrefernces.getInstance(MainScreenActivity.this).storeRegistrationId(regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;
		}
		@Override
		protected void onPostExecute(Object msg) {
			Log.d("RegistrationId",(String)msg);	
		}

	}
}
