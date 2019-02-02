package com.humby.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.calculations.preferences.CalculationsPreferences;
import com.humby.calculations.R;
import com.humby.utilities.FileDownloader;

public class CountryCurrencyLoader {
	public static ArrayList<CountryCurrencyRate> currenciesList=null;
	private static CountryCurrencyLoader countryCurrencyLoader =null ;
	public static ArrayList<CountryCurrencyRate>checkedCurrenciesArrayList=null;
	public static ArrayList<String>checkedCurrenciesNames=null;
	private static short attempts=0;
	private static boolean isDataAlreadyParsed=false;
	private static ArrayList<String>removedCurrenciesList;

	public static CountryCurrencyLoader getInstance() {
		if(countryCurrencyLoader==null){
			countryCurrencyLoader=new CountryCurrencyLoader();
		}
		return countryCurrencyLoader;
	}
	private CountryCurrencyLoader() {
		currenciesList=new ArrayList<CountryCurrencyRate>();
		checkedCurrenciesArrayList=new ArrayList<CountryCurrencyRate>();
		checkedCurrenciesNames=new ArrayList<String>();
		removedCurrenciesList=new ArrayList<String>();

		removedCurrenciesList.add("BMD");
		removedCurrenciesList.add("XOF");
		removedCurrenciesList.add("XAF");
		removedCurrenciesList.add("XPF");
		removedCurrenciesList.add("CLF");
		removedCurrenciesList.add("FKP");
		removedCurrenciesList.add("GMD");
		removedCurrenciesList.add("MGA");
		removedCurrenciesList.add("MRO");
		removedCurrenciesList.add("MMK");
		removedCurrenciesList.add("XDR");
		removedCurrenciesList.add("XCD");
	}
	public void populateCountriesCurrencies(){
		if(currenciesList==null || currenciesList.size()==0){
			populateCountriesNamesFromJson();
		}
		loadCurrencyInfo();

	}
	private static void loadSelectedCurrencies(){
		ArrayList<String>selectedCurrencies=CalculationsPreferences.getInstance().getSelectedCurrencies();
		CountryCurrencyRate tempObject=null;
		String currencyName=null;
		Double rate=1.0;
		String countryCode=null;
		checkedCurrenciesArrayList.clear();
		checkedCurrenciesNames.clear();
		for(int count=0;count<currenciesList.size();count++){

			currencyName=currenciesList.get(count).getCountryName();
			rate=currenciesList.get(count).getRate();
			countryCode=currenciesList.get(count).getCountryCode();
			tempObject=new CountryCurrencyRate(countryCode, currencyName, rate);

			if(selectedCurrencies!=null && selectedCurrencies.size()>0){
				if(selectedCurrencies.contains(currenciesList.get(count).getCountryName())){

					checkedCurrenciesArrayList.add(tempObject);
					checkedCurrenciesNames.add(tempObject.getCountryCode());
				}
			}else{

				checkedCurrenciesArrayList.add(tempObject);
				checkedCurrenciesNames.add(tempObject.getCountryCode());
			}
			Collections.sort(checkedCurrenciesArrayList, new MyCountryRateComparator());
		}

	}
	private void populateCountriesNamesFromJson(){
		String language=SharedData.getInstance().mContext.getString(R.string.device_lang);
		String countriesFileName="countries.json";

		if(language.equalsIgnoreCase("Spanish")){
			countriesFileName="countries_es.json";
		}else if(language.equalsIgnoreCase("Japanese")){
			countriesFileName="countries_ja.json";
		}else if(language.equalsIgnoreCase("Korean")){
			countriesFileName="countries_ko.json";
		}else if(language.equalsIgnoreCase("Port")){
			countriesFileName="countries_por.json";
		}

		JSONObject countries = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(SharedData.getInstance().mContext.getAssets().open(countriesFileName)));
			StringBuilder s = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}			
			countries = new JSONObject(s.toString());
		} catch (Exception e){
		}
		if (countries != null){
			@SuppressWarnings("unchecked")
			Iterator<String> iter = countries.keys();
			while(iter.hasNext()){
				String key = iter.next();
				String name;
				if(!removedCurrenciesList.contains(key)){
					try {
						name = countries.getString(key);
						CountryCurrencyRate temp = new CountryCurrencyRate(key, name, 1d);
						currenciesList.add(temp);
					} catch (Exception e) {

						Log.d("CountryCurrency","populateCountriesNamesFromJson "+e);
					}
				}
			}
		}

	}
	private void loadCurrencyInfo(){

		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/calculations/latest.json");
		//if the file doesnt exist, then download it while showing progress bar
		Date filedate = new Date(file.lastModified());
		Date now = new Date();
		Long difference = now.getTime() - filedate.getTime();
		Long oneDay = 86400000l;
		if ((!file.exists()) || (difference > oneDay))
		{
			downloadCurrency();
		} else if(!isDataAlreadyParsed)
		{
			handler.sendEmptyMessage(0);

		}


	}

	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//			if (attempts>3)
			//			{
			//				Toast.makeText(DataUtility.getContext(), "Error downloading Latest currencies,Check Internet Connection", Toast.LENGTH_SHORT).show();
			//			}			
			if(msg.what==0 || attempts>3){
				parseCurrencyInfo();
				isDataAlreadyParsed=true;
			}else {
				downloadCurrency();
			}
		}
	};
	private static void parseCurrencyInfo()
	{
		JSONObject currencyData=null;
		try {
			String fileCurrency = parseCurrencyInfoFromFile();
			if(fileCurrency!=null){
				currencyData = new JSONObject(fileCurrency);
				currencyData = currencyData.getJSONObject("rates");
				@SuppressWarnings("unchecked")
				Iterator<String> iter = currencyData.keys();
				while(iter.hasNext()){
					String key = iter.next();
					Double value = currencyData.getDouble(key);
					for (int i=0; i<currenciesList.size(); i++){
						if (currenciesList.get(i).getCountryCode().compareTo(key) == 0){
							currenciesList.get(i).setRate(value);
							break;
						}
					}
				}

			}
		} catch (Exception e) {
			currencyData = null;
			Log.d("CountryCurrencyLoader","parseCurrencyInfo() "+e);
			//downloadCurrency();
		}
		Collections.sort(currenciesList, new MyCountryRateComparator());
		loadSelectedCurrencies();
	}

	private static String parseCurrencyInfoFromFile(){
		//Get the text file
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/calculations/latest.json");
		FileReader sdCardreader=null;
		InputStreamReader assetsFileReader=null;
		BufferedReader br=null;
		StringBuilder text = new StringBuilder();
		String line=null;

		if(file!=null){
			try {
				sdCardreader=new FileReader(file);
				br=new BufferedReader(sdCardreader);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(br==null){
			try {
				assetsFileReader=new InputStreamReader(SharedData.getInstance().mContext.getAssets().open("latest.json"));
				br=new BufferedReader(assetsFileReader);
			} catch (IOException e) {
				e.printStackTrace();
				return null;		
			}

		}

		try {
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			br.close();
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

		return text.toString();	
	}

	private static void downloadCurrency(){

		attempts++;
		//String url = "http://184.72.228.156/latest.json";
		String url="http://54.225.78.104/latest.json";
		FileDownloader downloadFile = new FileDownloader(SharedData.getInstance().mContext, handler);
		downloadFile.execute(url);
	}

}
