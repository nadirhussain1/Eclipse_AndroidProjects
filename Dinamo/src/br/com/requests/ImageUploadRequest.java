package br.com.requests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;

public class ImageUploadRequest extends AsyncTask<Object,Void, Object>{
	String     requestUrl=null;
	AsyncResponseHandler postResponseHandler=null;
	Bitmap sourceImageBitmap=null;
	InputStream resultInputStream =null;
	int statusCode=-1;

	public ImageUploadRequest(String baseURL,Bitmap bitmap,AsyncResponseHandler listener){
		this.requestUrl=baseURL;
		this.sourceImageBitmap=bitmap;
		postResponseHandler=listener;
	}

	@Override
	protected Object doInBackground(Object... params) {
		String imageDataBytes=convertBitmapToStringByte();
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		HttpResponse response; 
		try {
			HttpPut putRequest = new HttpPut(requestUrl);
			StringEntity se = new StringEntity(imageDataBytes);
			ByteArrayEntity be = new ByteArrayEntity(convertBitmapToByteArray());
			putRequest.setEntity(be);
			putRequest.addHeader("X-Api-Token",DinamoConstants.API_TOKEN);
			putRequest.addHeader("X-Access-Token",DinamoConstants.ACCESS_TOKEN);
			putRequest.addHeader("content-type","image/jpeg");
			putRequest.addHeader("accept","application/json");
			response = client.execute(putRequest);

			if(response!=null){
				resultInputStream = response.getEntity().getContent();
				statusCode=response.getStatusLine().getStatusCode();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return SharedData.getInstance().getStringFromInputStream(resultInputStream);

	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		String resultString=(String)result;
		postResponseHandler.onResult(resultString,statusCode);
	}
	private String convertBitmapToStringByte(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		sourceImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArray = baos.toByteArray();
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}
	
	private byte[] convertBitmapToByteArray(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		sourceImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

}
