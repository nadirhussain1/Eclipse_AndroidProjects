package com.mobive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobive.bean.Deal;
import com.mobive.bean.Response;
import com.mobive.bean.User;
import com.mobive.dealme.DataUtility;
import com.mobive.dealme.DealMePreferences;

public class Util {
	
	public static Deal selectedDeal=null;
    static int  userId;
	
	
	public static int getUserId() {
		return userId;
	}
	public static void setUserId(int Id) {
	    userId = Id;
	}

	public static  Deal getSelectedDeal() {
		return selectedDeal;
	}


	public static String ConvertToJson(InputStream inputStream)
	{
		String jsonString=null;
		
		try
		{ 
			if(inputStream != null)
			{
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];

				Reader reader = new BufferedReader( new InputStreamReader(inputStream));
				int n;

				while((n = reader.read(buffer))!=-1)
				{
					writer.write(buffer, 0, n);
				}
				inputStream.close();

				jsonString = writer.toString();
				return jsonString;
			}
		}

		catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return jsonString;
	}
	public static Response parseResponse(String result) {
		Response response=null;
		JSONObject fullResponse;
		try {
			fullResponse = new JSONObject(result);

			JSONObject payloadObject=new JSONObject();
			JSONObject metaDataObject=new JSONObject();

			payloadObject=fullResponse.getJSONObject("payload");
			metaDataObject=fullResponse.getJSONObject("metadata");

			JSONObject userObject=new JSONObject();
			User user=new User();
			ArrayList<String>keyWords=new ArrayList<String>();
			userObject=payloadObject.getJSONObject("user");

			user.setId(userObject.getInt("id"));
			user.setName(userObject.getString("name"));
			user.setEmail(userObject.getString("email"));
			user.setEnabled(userObject.getBoolean("enabled"));
			user.setCity(userObject.getString("city"));
			response=new Response();
			response.getPayload().setUser(user);

			JSONArray dealsJsonArrayList=userObject.getJSONArray("deals");
			if(dealsJsonArrayList!=null){
				JSONObject dealsObject=dealsJsonArrayList.getJSONObject(0);
				String dealsSlugs=dealsObject.getString("deals");
				DealMePreferences.getInstance().saveUserSelectedDealsCatgories(dealsSlugs);
				populateDealsTitles(dealsSlugs);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return response;
		}
		return response;
	}
	private static void populateDealsTitles(String dealsSlugs ){
		String dealsTitles="All Deals";
		int commaIndex=dealsSlugs.indexOf(',');
		String tag="";
		while(commaIndex!=-1){
			tag=dealsSlugs.substring(0,commaIndex);
			
			for(int i=0;i<DataUtility.getItems().size();i++){
				if(DataUtility.getItems().get(i).getSlug().equalsIgnoreCase(tag)){
					dealsTitles=dealsTitles+","+DataUtility.getItems().get(i).getTitle();
					break;
				}
			}
			dealsSlugs=dealsSlugs.substring(commaIndex+1);
			commaIndex=dealsSlugs.indexOf(',');
		}
		
		for(int i=0;i<DataUtility.getItems().size();i++){
			if(DataUtility.getItems().get(i).getSlug().equalsIgnoreCase(dealsSlugs)){
				dealsTitles=dealsTitles+","+DataUtility.getItems().get(i).getTitle();
				break;
			}
		}
		DealMePreferences.getInstance().saveUserSelectedDealsTitles(dealsTitles);
		
	}
	
}
