package br.com.requests;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import br.com.data.model.BoughtProduct;
import br.com.data.model.DinamoObject;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.SoldProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;

public class GetRequest extends AsyncTask<Object,Void, Object> {

	private AsyncResponseHandler requestHandler = null;
	private String getRequestUrl = null;
	private InputStream resultInputStream = null;
	Context mContext=null;
	int statusCode=-1;
	int requestCode=-1;

	public GetRequest(AsyncResponseHandler requestHandler, String url,int requestId){
		this.requestHandler = requestHandler;
		getRequestUrl= url;
		requestCode=requestId;
	}

	@Override
	protected String doInBackground(Object... params) {
		return startRequest();

	}
	private String startRequest(){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		HttpResponse response;
		String responseString="";
		try {
			HttpGet getRequest = new HttpGet(getRequestUrl);
			getRequest.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			getRequest.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			getRequest.addHeader("content-type","application/json;charset=utf-8");
			getRequest.addHeader("accept","application/json");
			response = client.execute(getRequest);

			if(response!=null){
				resultInputStream = response.getEntity().getContent();
				statusCode=response.getStatusLine().getStatusCode();
			}

			responseString=SharedData.getInstance().getStringFromInputStream(resultInputStream);

			if(requestCode !=-1 && requestCode<=DinamoConstants.GET_PROD_REQ_ID){
				populateJavaCommonModel(responseString);
			}else if(requestCode==DinamoConstants.GET_BUY_EVENTS_ID){
				populateBoughtEvents(responseString);
			}else if(requestCode==DinamoConstants.GET_SELL_EVENTS_ID){
				populateSoldEvents(responseString);
			}else if(requestCode==DinamoConstants.GET_EXPENSE_EVENTS_ID){
				populateExpenses(responseString);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.toString();
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		String resultString=(String)result;
		requestHandler.onResult(resultString,statusCode);

	}
	private void populateBoughtEvents(String responseString){
		JSONArray mainDataArray;

		try {
			JSONObject mainJsonObject=new JSONObject(responseString);
			mainDataArray = mainJsonObject.getJSONArray("data");
			for(int count=0;count<mainDataArray.length();count++){
				BoughtProduct boughtProduct=new BoughtProduct();
				boughtProduct.setPriceValue(Double.valueOf(mainDataArray.getJSONObject(count).getString("value")));
				boughtProduct.setId(mainDataArray.getJSONObject(count).getString("id"));
				boughtProduct.setNotes(mainDataArray.getJSONObject(count).getString("notes"));
				
				JSONObject pay = mainDataArray.getJSONObject(count).getJSONObject("paymentmethod");
				String methodId = pay.getString("id");
				boughtProduct.setPayMethod(findNameOfMethod(methodId));
				
				
				JSONObject est = mainDataArray.getJSONObject(count).getJSONObject("establishment");
				String estId = est.getString("id");
				boughtProduct.setEstablishment(findNameOfEstablishment(estId));
				
				String dateString=mainDataArray.getJSONObject(count).getString("paymentdate");
				boughtProduct.setDate(SharedData.getInstance().convertToDate(dateString));
				boughtProduct.isSynchronized=true;
				UserDataManager.getInstance().boughtEventsList.add(boughtProduct);

			}

			//DinamoDatabaseHelper.getInstance().addBoughtProducts(UserDataManager.getInstance().boughtEventsList);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void populateSoldEvents(String responseString){
		JSONArray mainDataArray;

		try {
			JSONObject mainJsonObject=new JSONObject(responseString);
			mainDataArray = mainJsonObject.getJSONArray("data");
			for(int count=0;count<mainDataArray.length();count++){
				SoldProduct soldProduct=new SoldProduct();
				soldProduct.setPriceValue(Double.valueOf(mainDataArray.getJSONObject(count).getString("value")));
				soldProduct.setId(mainDataArray.getJSONObject(count).getString("id"));
				soldProduct.setQuantity(mainDataArray.getJSONObject(count).getString("quantity"));
				
				JSONObject pay = mainDataArray.getJSONObject(count).getJSONObject("paymentmethod");
				String methodId = pay.getString("id");
				soldProduct.setPayMethod(findNameOfMethod(methodId));
				
				
				JSONObject prod = mainDataArray.getJSONObject(count).getJSONObject("product");
				String prodId = prod.getString("id");
				soldProduct.setProduct(findNameOfProd(prodId));
				
				
				String dateString=mainDataArray.getJSONObject(count).getString("receiptdate");
				soldProduct.setDate(SharedData.getInstance().convertToDate(dateString));
				soldProduct.isSynchronized=true;
				UserDataManager.getInstance().soldEventsList.add(soldProduct);

			}
			//DinamoDatabaseHelper.getInstance().addSoldProducts(UserDataManager.getInstance().soldEventsList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void populateExpenses(String responseString){
		JSONArray mainDataArray;

		try {
			JSONObject mainJsonObject=new JSONObject(responseString);
			mainDataArray = mainJsonObject.getJSONArray("data");
			for(int count=0;count<mainDataArray.length();count++){
				ExpenseProduct expenseProduct=new ExpenseProduct();
				expenseProduct.setPriceValue(Double.valueOf(mainDataArray.getJSONObject(count).getString("value")));
				expenseProduct.setId(mainDataArray.getJSONObject(count).getString("id"));
				
				JSONObject peri = mainDataArray.getJSONObject(count).getJSONObject("periodicity");
				String periodId = peri.getString("id");
				expenseProduct.setPeriodicityType(findNameOfPeriodicity(periodId));
				
				JSONObject cat = mainDataArray.getJSONObject(count).getJSONObject("category");
				String catId = cat.getString("id");
				expenseProduct.setCategory(findNameOfCat(catId));
				
				String dateString=mainDataArray.getJSONObject(count).getString("startdate");
				expenseProduct.setDate(SharedData.getInstance().convertToDate(dateString));
				expenseProduct.isSynchronized=true;
				String endDateString=mainDataArray.getJSONObject(count).getString("enddate");
				expenseProduct.setEndDate(SharedData.getInstance().convertToDate(endDateString));
				
				UserDataManager.getInstance().expenseEventsList.add(expenseProduct);

			}
			//DinamoDatabaseHelper.getInstance().addExpenseProducts(UserDataManager.getInstance().expenseEventsList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private DinamoObject  findNameOfCat(String methodId){
		String name="";
		for(int count=0;count<UserDataManager.getInstance().catagories.size();count++){
			if(methodId.contentEquals(UserDataManager.getInstance().catagories.get(count).getId())){
				name= UserDataManager.getInstance().catagories.get(count).getName();
				break;
			}
		}
		return new DinamoObject(methodId,name);
	}


	private DinamoObject  findNameOfPeriodicity(String methodId){
		String name="";
		for(int count=0;count<UserDataManager.getInstance().periodicity.size();count++){
			if(methodId.contentEquals(UserDataManager.getInstance().periodicity.get(count).getId())){
				name= UserDataManager.getInstance().periodicity.get(count).getName();
				break;
			}
		}
		return new DinamoObject(methodId,name);
	}
	private DinamoObject  findNameOfEstablishment(String methodId){
		String name="";
		for(int count=0;count<UserDataManager.getInstance().establishments.size();count++){
			if(methodId.contentEquals(UserDataManager.getInstance().establishments.get(count).getId())){
				name= UserDataManager.getInstance().establishments.get(count).getName();
				break;
			}
		}
		return new DinamoObject(methodId,name);
	}
	private DinamoObject  findNameOfProd(String methodId){
		String name="";
		for(int count=0;count<UserDataManager.getInstance().products.size();count++){
			if(methodId.contentEquals(UserDataManager.getInstance().products.get(count).getId())){
				name= UserDataManager.getInstance().products.get(count).getName();
				break;
			}
		}
		return new DinamoObject(methodId,name);
	}
	private DinamoObject  findNameOfMethod(String methodId){
		String name="";
		
		for(int count=0;count<UserDataManager.getInstance().paymentMethods.size();count++){
			if(methodId.contentEquals(UserDataManager.getInstance().paymentMethods.get(count).getId())){
				name= UserDataManager.getInstance().paymentMethods.get(count).getName();
				break;
			}
		}
		return new DinamoObject(methodId,name);
	}
	private void populateJavaCommonModel(String responseString){
		ArrayList<DinamoObject>popList=new ArrayList<DinamoObject>();
		JSONArray mainDataArray;

		try {
			JSONObject mainJsonObject=new JSONObject(responseString);
			mainDataArray = mainJsonObject.getJSONArray("data");
			for(int count=0;count<mainDataArray.length();count++){
				DinamoObject dinamoObj=new DinamoObject();
				dinamoObj.setName(mainDataArray.getJSONObject(count).getString("name"));
				dinamoObj.setId(mainDataArray.getJSONObject(count).getString("id"));
				dinamoObj.isSynChronized=true;
				popList.add(dinamoObj);
			}
			if(requestCode==DinamoConstants.GET_PAY_METHOD_REQ_ID){
				UserDataManager.getInstance().paymentMethods=popList;
				//DinamoDatabaseHelper.getInstance().addPayMethods(popList);

			}else if(requestCode==DinamoConstants.GET_PERIODICITY_REQ_ID){
				UserDataManager.getInstance().periodicity=popList;
				//DinamoDatabaseHelper.getInstance().addPeriodicities(popList);

			}else if(requestCode==DinamoConstants.GET_PROD_REQ_ID){
				UserDataManager.getInstance().products=popList;
				//DinamoDatabaseHelper.getInstance().addProducts(popList);

			}else if(requestCode==DinamoConstants.GET_CAT_REQ_ID){
				UserDataManager.getInstance().catagories=popList;
				//DinamoDatabaseHelper.getInstance().addCatgories(popList);

			}else if(requestCode==DinamoConstants.GET_EST_REQ_ID){
				UserDataManager.getInstance().establishments=popList;
				//DinamoDatabaseHelper.getInstance().addEstablishments(popList);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
