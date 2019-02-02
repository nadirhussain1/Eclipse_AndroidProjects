package com.assessment.requests;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.assessment.constants.AppConstants;
import com.assessment.interfaces.AsyncLocaterResponseCallBack;
import com.assessment.model.Server;
import com.assessment.utilities.GlobalUtility;

public class IpLocaterRequest extends AsyncTask{
	private  String sourceIp="";
	private AsyncLocaterResponseCallBack callBackListener;
	private Server serverData=null;
	private String success="false";

	public IpLocaterRequest(String ip,AsyncLocaterResponseCallBack callBackListener){
		sourceIp=ip;
		this.callBackListener=callBackListener;

	}
	@Override
	protected String doInBackground(Object... params) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		String responseString="";
		InputStream resultStream=null;

		try {
			HttpGet getRequest = new HttpGet(AppConstants.IP_LOOK_BASE_URL+sourceIp);
			getRequest.addHeader("accept","application/json");
			HttpResponse response = client.execute(getRequest);

			if(response!=null){
				int statusCode=response.getStatusLine().getStatusCode();
				if(statusCode == HttpStatus.SC_OK){
					resultStream = response.getEntity().getContent();
					if(resultStream !=null){
						success="true";
						responseString=GlobalUtility.convertStreamToString(resultStream);
						populateServerModel(responseString);	
					}

				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.toString();
		}

		return success;

	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		String resultString=(String)result;
		callBackListener.onResult(resultString,serverData);

	}
	private void populateServerModel(String responseString){
		try {
			JSONObject mainJsonObject=new JSONObject(responseString);
			String country=mainJsonObject.getString("country");
			String city=mainJsonObject.getString("city");
			String region=mainJsonObject.getString("region");
			String timeZone=mainJsonObject.getString("timeZone");
			String network=mainJsonObject.getString("network");
			
			if(timeZone.equalsIgnoreCase("null")){
				timeZone="Time Zone not available";
			}
			if(network.equalsIgnoreCase("null")){
				network="Network not available";
			}
 
			String address="";
			if(region !=null && !region.contentEquals("null") && !region.contentEquals("Unknown")){
				address+=region;
			}
			if(city !=null && !city.contentEquals("null") && !city.contentEquals("Unknown")){
				if(address.equalsIgnoreCase("")){
					address+=city;
				}else{
					address+=","+city;
				}
			}
			if(country !=null && !country.contentEquals("null") && !country.contentEquals("Unknown")){
				if(address.equalsIgnoreCase("")){
					address+=country;
				}else{
					address+=","+country;
				}
			}
			serverData=new Server(address, timeZone, network);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
