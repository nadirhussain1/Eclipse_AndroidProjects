package com.mobive.dealme;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.humby.dealular.R;
import com.mobive.dealme.deal_items.EntryAdapter;
import com.mobive.net.PostRequest;
import com.mobive.net.RequestListener;
import com.mobive.util.Util;

public class MyDealsSelectorView {
	View dealsSelectorView=null;
	public EntryAdapter adapter =null;
	ListView dealsListView=null;

	public MyDealsSelectorView(){
		dealsSelectorView=View.inflate(DataUtility.getContext(),R.layout.screen3_content, null);
		adapter = new EntryAdapter(DataUtility.getContext(), DataUtility.getItems());
		dealsListView=((ListView)dealsSelectorView.findViewById(R.id.ListView1));
		dealsListView.setAdapter(adapter);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}
	public View getDealsSelectorView(){
		return dealsSelectorView;
	}
	
	public boolean saveSelectedDealsSettings(){
		if(adapter.getNoOfSelection()>0){
			HashMap adapterEntriesStates =adapter.getEnteryAdapterStates();
			Iterator entries = adapterEntriesStates.entrySet().iterator();
			String newSelectedCatgories="";
			String newSelectedDealsTitles="All Deals";

			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				int key = (Integer)thisEntry.getKey();
				Object value = thisEntry.getValue(); 
				if(value.equals(1))
				{   newSelectedDealsTitles=newSelectedDealsTitles+","+DataUtility.getItems().get(key).getTitle().toString();
				if(newSelectedCatgories.equalsIgnoreCase("")){
					newSelectedCatgories=DataUtility.getItems().get(key).getSlug().toString();
				}else{
					newSelectedCatgories=newSelectedCatgories+","+DataUtility.getItems().get(key).getSlug().toString();
				}

				}
			}
			DealMePreferences.getInstance().saveUserSelectedDealsTitles(newSelectedDealsTitles);
			DealMePreferences.getInstance().saveUserSelectedDealsCatgories(newSelectedCatgories);
			
			JSONObject json=new JSONObject();
			try {

				json.put("user",""+DataUtility.getUser().getId());
				json.put("deals", newSelectedCatgories);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			new PostRequest(DealMeConstants.serverDealsSaveURL, json, dealsPostListener ).execute();

			return true;
		}
		else{
			Toast.makeText(DataUtility.getContext(), "Please select at least One category for your feed", Toast.LENGTH_SHORT).show();
		}
		return false;
	
	}
	RequestListener dealsPostListener=new RequestListener() {

		@Override
		public void onSuccess(InputStream inputStream) {
			String result=Util.ConvertToJson(inputStream);
			String message="";
			try {
			     JSONObject responseJSON=new JSONObject(result);
                 message=responseJSON.getJSONObject("metadata").getString("message");
		     } catch (JSONException e) {
			// TODO Auto-generated catch block
			    e.printStackTrace();
		     }
            //Toast.makeText(DataUtility.getContext(),message, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(String message) {
			//Toast.makeText(DataUtility.getContext(),message, Toast.LENGTH_SHORT);

		}
	};
}
