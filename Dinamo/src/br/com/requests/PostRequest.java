package br.com.requests;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;

public class PostRequest extends AsyncTask<Object, Void, Object> { 
	private String baseURL=null;
	private JSONObject jsonObject=null;
	private AsyncResponseHandler postResponseHandler=null;
	private InputStream resultInputStream =null;
    private int statusCode=-1;
    
	public PostRequest(String baseURL,JSONObject jsonObject,AsyncResponseHandler listener){
		this.baseURL=baseURL;
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
			HttpPost post = new HttpPost(baseURL);
			StringEntity se = new StringEntity(jsonObject.toString(),"UTF-8");  
			post.setEntity(se);
			post.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			post.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			post.addHeader("content-type","application/json;charset=utf-8");
			post.addHeader("accept","application/json");
			response = client.execute(post);
	
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


