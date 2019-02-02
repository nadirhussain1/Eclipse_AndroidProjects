package br.com.requests;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;

public class PutRequest extends AsyncTask<Object,Void, Object>{ 
	String     requestUrl=null;
	JSONObject jsonObject=null;
	AsyncResponseHandler postResponseHandler=null;
	InputStream resultInputStream =null;
    int statusCode=-1;
    
	public PutRequest(String baseURL,JSONObject jsonObject,AsyncResponseHandler listener){
		this.requestUrl=baseURL;
		this.jsonObject=jsonObject;
		postResponseHandler=listener;
	}
	@Override
	protected String doInBackground(Object... arg0){
		return postRequest(jsonObject);
	}
	public String postRequest(JSONObject jsonObject){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		HttpResponse response;

		try {
			HttpPut putRequest = new HttpPut(requestUrl);
			StringEntity se = new StringEntity(jsonObject.toString());  
			putRequest.setEntity(se);
			putRequest.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			putRequest.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			putRequest.addHeader("content-type","application/json;charset=utf-8");
			putRequest.addHeader("accept","application/json");
			response = client.execute(putRequest);
	
			if(response!=null){
				resultInputStream = response.getEntity().getContent();
				statusCode=response.getStatusLine().getStatusCode();
			}

		} catch(Exception e) {
			e.printStackTrace();
			return e.toString();
		}

		return SharedData.getInstance().getStringFromInputStream(resultInputStream);
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		String resultString=(String)result;
		postResponseHandler.onResult(resultString,statusCode);
	}
	

}


