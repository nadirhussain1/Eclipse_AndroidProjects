package br.com.requests;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.os.AsyncTask;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;

public class DeleteRequest extends AsyncTask<Object,Void , Object> {

	private AsyncResponseHandler requestHandler = null;
	private String delteUrl = null;
	private InputStream resultInputStream = null;
	Context mContext=null;
	int statusCode=-1;


	public DeleteRequest(AsyncResponseHandler requestHandler, String url){
		this.requestHandler = requestHandler;
		delteUrl= url;
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
			HttpDelete delteRequest = new HttpDelete(delteUrl);
			delteRequest.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			delteRequest.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			delteRequest.addHeader("content-type","application/json;charset=utf-8");
			delteRequest.addHeader("accept","application/json");
			response = client.execute(delteRequest);

			if(response!=null){
				resultInputStream = response.getEntity().getContent();
				statusCode=response.getStatusLine().getStatusCode();
			}

			responseString=SharedData.getInstance().getStringFromInputStream(resultInputStream);
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

}
