package com.mobive.net;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.mobive.util.Util;

import android.os.AsyncTask;
import android.util.Log;

public class PostRequest extends AsyncTask { 
	String baseURL=null;
	JSONObject jsonObject=null;
	RequestListener listener=null;
	public PostRequest(String baseURL,JSONObject jsonObject,RequestListener listener)
	{
		this.baseURL=baseURL;
		this.jsonObject=jsonObject;
		this.listener=listener;
	}
	@Override
	protected InputStream doInBackground(Object... arg0){
		
	return sendRequest(jsonObject);
	}
	public InputStream sendRequest(JSONObject jsonObject)
	{
		HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        InputStream in =null;
        try {
            HttpPost post = new HttpPost(baseURL);
            
            StringEntity se = new StringEntity( jsonObject.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if(response!=null){
                in = response.getEntity().getContent(); //Get the data in the entity                
            }

        } catch(Exception e) {
            e.printStackTrace();
//            createDialog("Error", "Cannot Estabilish Connection");
        }

return in;
	}
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result==null)
		{
			listener.onFail("There was an error");
		}
		else
		{
		listener.onSuccess((InputStream)result);	
		}
	}
	
}


