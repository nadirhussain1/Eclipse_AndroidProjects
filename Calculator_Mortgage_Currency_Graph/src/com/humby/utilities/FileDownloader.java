package com.humby.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class FileDownloader extends AsyncTask<String, Void, String> { 
	private Handler handler;

	public FileDownloader(Context context, Handler handler){	
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result!=null && !result.equalsIgnoreCase("")){
			handler.sendEmptyMessage(0);
		}else{
			handler.sendEmptyMessage(-1);
		}
	}



	@Override
	protected String doInBackground(String... url) {
		StringBuilder builder = new StringBuilder();
		//Find the directory for the SD Card using the API

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url[0]);
		try {

			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				File sdcard = Environment.getExternalStorageDirectory();
				File file = new File(sdcard,"/calculations");
				if(!file.exists()){
					file.mkdirs();
				}
				file = new File(file, "latest.json");
				FileWriter output = new FileWriter(file);

				String line;
				while ((line = reader.readLine()) != null) {
					//publishProgress();
					output.append(line);
					builder.append(line);
				}
				content.close();
				output.flush();
				output.close();
			} else {
				Log.e("LatestCurrencyFileDownloader", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}  

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);

	}


}
