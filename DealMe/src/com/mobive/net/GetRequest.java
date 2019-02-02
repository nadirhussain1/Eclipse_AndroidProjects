package com.mobive.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class GetRequest extends AsyncTask {
	
	private RequestListener requestHandler = null;
	
	private URL url = null;
	private HttpURLConnection con = null;
	private InputStream responseStream = null;
	private Object customAttribute=null;
	private Timer timer;
	private int timeout = 35;
	boolean requestCompleted=false;
	Context context=null;
	Exception e=null;
	public GetRequest(RequestListener requestHandler, String url,Context context)
	{
		Log.i("url", url);
		requestCompleted=false;
		this.context=context;
		this.requestHandler = requestHandler;
		this.customAttribute=customAttribute;
			try {
				this.url = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}

	@Override
	protected String doInBackground(Object... params) {
		if(isOnline())
		return connect();
		else
		{
			return "No Internet Connectivity";
		}
	}
	private String connect()
	{
		startTimer();
		try 
		{
			if( url != null)
			{
				con = (HttpURLConnection)url.openConnection();
				con.setConnectTimeout(15000);// 15 seconds
				con.setReadTimeout(15000);// 15 seconds
				con.connect();
				responseStream = con.getInputStream();   
				Log.i("Response code", con.getResponseCode()+"");
			}
		}
		catch (FileNotFoundException e) 
		{
			this.e=e;
			e.printStackTrace();
			return "File Not Found";
		}
		catch (MalformedURLException e) 
		{
			this.e=e;
			e.printStackTrace();
			return "Malformed URL";
		}
		catch (IOException e) 
		{
			this.e=e;
			e.printStackTrace();
			return "I/O Exception";     
		}
		return "success"; 
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		String resultString=(String)result;
		requestCompleted=true;
		if(timer!=null)
		timer.cancel();
		
		if(resultString.equalsIgnoreCase("success"))
		{
			if( requestHandler != null && responseStream != null )
			{
				requestHandler.onSuccess(responseStream);
			}
			else
			{
				requestHandler.onFail("There was An Error from server");
			}
		}
		else if(resultString.equalsIgnoreCase("File Not Found"))
		{
			requestHandler.onFail("No Response From Server");
		}
		else if(resultString.equalsIgnoreCase("Malformed URL"))
		{
			requestHandler.onFail("No Response From Server");
		}
		else if(resultString.equalsIgnoreCase("I/O Exception"))
		{
			requestHandler.onFail("No Response From Server");
		}
		else if(resultString.equalsIgnoreCase("No Internet Connectivity"))
		{
			requestHandler.onFail("No Response From Server");
		}
		Log.i("result", "on complete "+customAttribute);
	}
	private void startTimer()
	{		
		timer = new Timer();
		timer.schedule(new CancelAsyncTask(), timeout * 1000);
	}
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if(requestHandler!=null)
			requestHandler.onFail("No Response From Server");
		
	}
	private class CancelAsyncTask extends TimerTask 
	{
		public void run() 
		{
			if(!requestCompleted)
			{
				cancelRequest();
			}
			else
			{
				requestCompleted = false;
			}
		}
	}
	
	public void cancelRequest()
	{
		if(this != null && ((this.getStatus() == AsyncTask.Status.RUNNING) || (this.getStatus() == AsyncTask.Status.PENDING)))
		{
			this.cancel(true);
		}
	}
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    

	    return false;
	}
	
}
