package br.com.requests;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import br.com.global.DinamoConstants;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetPhotoRequest  extends AsyncTask<Object,Void,Object>{
	private AsyncImageGetHandler requestHandler = null;
	private String getRequestUrl = null;
	private InputStream resultInputStream = null;
	Context mContext=null;
	int statusCode=-1;
    int requestId=-1;
    
	public GetPhotoRequest(AsyncImageGetHandler requestHandler, String url,int requestCode){
		this.requestHandler = requestHandler;
		getRequestUrl= url;
		requestId=requestCode;
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		return startRequest();

	}
	private Bitmap startRequest(){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		HttpResponse response;
		Bitmap bitmap=null;
		try {
			HttpGet getRequest = new HttpGet(getRequestUrl);
			getRequest.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			getRequest.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			getRequest.addHeader("content-type","image/jpeg");
			response = client.execute(getRequest);
			if(response!=null){
				resultInputStream = response.getEntity().getContent();
				statusCode=response.getStatusLine().getStatusCode();
				bitmap=BitmapFactory.decodeStream(resultInputStream);
				resultInputStream.close();
			}

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		requestHandler.onResult((Bitmap)result,statusCode,requestId);

	}
}
