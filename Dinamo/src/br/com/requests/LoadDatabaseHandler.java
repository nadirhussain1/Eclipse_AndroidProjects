package br.com.requests;

import android.os.AsyncTask;
import br.com.data.model.UserDataManager;
import br.com.storage.DinamoDatabaseHelper;

public class LoadDatabaseHandler extends AsyncTask<Object,Void, Object>{
	AsyncResponseHandler callBackHandler;
	public LoadDatabaseHandler(AsyncResponseHandler requestHandler){
		callBackHandler=requestHandler;
	}
	@Override
	protected Object doInBackground(Object... params) {
		UserDataManager.getInstance().establishments=DinamoDatabaseHelper.getInstance().getEstablishments();
		UserDataManager.getInstance().catagories=DinamoDatabaseHelper.getInstance().getCategories();
		UserDataManager.getInstance().products=DinamoDatabaseHelper.getInstance().getProducts();
		UserDataManager.getInstance().periodicity=DinamoDatabaseHelper.getInstance().getPeriodicity();
		UserDataManager.getInstance().paymentMethods=DinamoDatabaseHelper.getInstance().getPaymentMethods();
		UserDataManager.getInstance().boughtEventsList=DinamoDatabaseHelper.getInstance().getBoughtProducts();
		UserDataManager.getInstance().soldEventsList=DinamoDatabaseHelper.getInstance().getAllSoldProducts();
		UserDataManager.getInstance().expenseEventsList=DinamoDatabaseHelper.getInstance().getAllExpenseProducts();
		return "";
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		String resultString=(String)result;
		callBackHandler.onResult(resultString,-1);

	}

}
