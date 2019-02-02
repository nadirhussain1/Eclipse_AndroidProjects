package com.calculations.preferences;

import java.util.ArrayList;
import java.util.Map;

import android.content.SharedPreferences;

import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyRate;
import com.humby.model.SharedData;


public class CalculationsPreferences {
	private static CalculationsPreferences calculationsPreferences=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;

	public static CalculationsPreferences getInstance() {
		if(calculationsPreferences==null){
			calculationsPreferences=new CalculationsPreferences();
		}
		return calculationsPreferences;
	}
	private CalculationsPreferences(){

		sharedPreferences = SharedData.getInstance().mContext.getSharedPreferences("Calculations", 0);
		editor = sharedPreferences.edit();
	}
	public void saveSalesTaxSettingFlag(int flag){
		editor.putInt(CalConstants.SALES_TAX_SETTING_KEY,flag);
		editor.commit();
	}
	public int getSalesTaxSettingFlag(){
		return sharedPreferences.getInt(CalConstants.SALES_TAX_SETTING_KEY, CalConstants.SALES_TAX_NOT_SET);
	}
	public void saveSalesTaxValueToPref(float salesTaxValue){

		editor.putFloat(CalConstants.SALES_TAX_VALUE_STRING,salesTaxValue);
		editor.commit();

	}
	public float  getSalesTaxValuefromPref(){
		return sharedPreferences.getFloat(CalConstants.SALES_TAX_VALUE_STRING, 0);
	}
	public void saveSelectedCurrencies(ArrayList<CountryCurrencyRate> selectedCurrencies){

		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("selectedCurrencies_")){

				editor.remove(entry.getKey());	

			}
		}

		if(selectedCurrencies!=null){
			for(int i=0;i<selectedCurrencies.size();i++){
				editor.putString("selectedCurrencies_"+i, selectedCurrencies.get(i).getCountryName());

			}
			editor.commit();
		}
	}
	public ArrayList<String> getSelectedCurrencies(){
		ArrayList<String> selectedCurrencies=new ArrayList<String>();
		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("selectedCurrencies_")){

				selectedCurrencies.add(entry.getValue().toString());	

			}
		}
		return selectedCurrencies;
	}
	public void saveHistoryofMainCalculator(ArrayList<String> historyOperations){
		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("savedHistory_")){

				editor.remove(entry.getKey());	

			}
		}

		if(historyOperations!=null){
			for(int i=0;i<historyOperations.size();i++){
				editor.putString("savedHistory_"+i, historyOperations.get(i));

			}
			editor.commit();
		}

	}
	public ArrayList<String> getSavedHistoryOfMainCalculator(){

		ArrayList<String> savedHistory=new ArrayList<String>();
		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("savedHistory_")){

				savedHistory.add(entry.getValue().toString());	

			}
		}
		return savedHistory;
	}
	public void savePrevSelectedMeasurementCat(int index){
		editor.putInt("PrevMeasurementCategory",index);
		editor.commit();
	}
	public void savePrevSelectedMeasurementUnitType(int index){
		editor.putInt("PrevMeasurementUnit",index);
		editor.commit();
	}
	public void savePrevSelectedCurrency(int index){
		editor.putInt("PrevSelectedCurrency",index);
		editor.commit();
	}
	public int getPrevSelectedMeasurementCat(){
		return sharedPreferences.getInt("PrevMeasurementCategory", 0);
	}
	public int getPrevSelectedMeasurementUnit(){
		return sharedPreferences.getInt("PrevMeasurementUnit", 0);
	}
	public int getPrevSelectedCurrency(){
		return sharedPreferences.getInt("PrevSelectedCurrency", -1);
	}
	public void saveMainCalInput(String mainInput){
		editor.putString("MainCalculatorInput",mainInput);
		editor.commit();
	}
	public void saveMainCalResultOutput(String mainInput){
		editor.putString("MainCalculatorResult",mainInput);
		editor.commit();
	}
	public String getMainCalInput(){
		return sharedPreferences.getString("MainCalculatorInput", "");
	}
	public String getMainCalResult(){
		return sharedPreferences.getString("MainCalculatorResult", "");
	}
	public void saveHapticVibrationSettings(boolean isEnabled){
		editor.putBoolean("HapticVibration",isEnabled);
		editor.commit();
	}
	public boolean getHapticVibrationSettings(){
		return sharedPreferences.getBoolean("HapticVibration",false);
	}
	public void saveUnSelectedCalculators(ArrayList<Integer> unSelectedCalculators){
		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("UnSelectedCalculators_")){

				editor.remove(entry.getKey());	

			}
		}

		if(unSelectedCalculators!=null){
			for(int i=0;i<unSelectedCalculators.size();i++){
				editor.putInt("UnSelectedCalculators_"+i, unSelectedCalculators.get(i));

			}
			editor.commit();
		}

	}
	public ArrayList<Integer>getUnSelectedCalculators(){
		ArrayList<Integer> unSelectedcalculators=new ArrayList<Integer>();
		Map<String,?> keys = sharedPreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("UnSelectedCalculators_")){

				unSelectedcalculators.add(Integer.valueOf(entry.getValue().toString()));	

			}
		}
		return unSelectedcalculators;
	}
	public void saveMeasurementInput(double value){
		editor.putFloat("MeasurementInput",(float)value);
		editor.commit();

	}
	public double getMeasurementInput(){
		return sharedPreferences.getFloat("MeasurementInput", 1);
	}
	public void saveCurrencyInput(double value){
		editor.putFloat("CurrencyInput",(float)value);
		editor.commit();

	}
	public double getCurrencyInput(){
		return sharedPreferences.getFloat("CurrencyInput", 1);
	}
	public void saveMortgageInputValues(double propertyValue,double loanPercentage,double loanAmount,double interest,int noOfMonths){
		editor.putFloat("MortgagePropertyInput",(float)propertyValue);
		editor.putFloat("MortgageLoanPercent",(float)loanPercentage);
		editor.putFloat("MortgageLoanAmount",(float)loanAmount);
		editor.putFloat("MortgageInterest",(float)interest);
		editor.putInt("MortgageMonths", noOfMonths);
		editor.commit();

	}
	public int getMortgageMonths(){
		return sharedPreferences.getInt("MortgageMonths", 0);

	}
	public double[] getMortgageInputValues(){
		double inputValues[]=new double[4];
		inputValues[0]=sharedPreferences.getFloat("MortgagePropertyInput", 0);
		inputValues[1]=sharedPreferences.getFloat("MortgageLoanPercent", 0);
		inputValues[2]=sharedPreferences.getFloat("MortgageLoanAmount", 0);
		inputValues[3]=sharedPreferences.getFloat("MortgageInterest", 0);

		return inputValues;

	}
	public void saveTippingCalInput(double billAmount,int people,double tipPercentage){
		editor.putFloat("TippingBillAmount",(float)billAmount);
		editor.putFloat("TipPercentage",(float)tipPercentage);
		editor.putInt("TippingPeople", people);
		editor.commit();
	}
	public double getTippingBillAmount(){
		return sharedPreferences.getFloat("TippingBillAmount", 0);
	}
	public double getTipPercentage(){
		return sharedPreferences.getFloat("TipPercentage", 0);
	}
	public int getNoOfPeople(){
		return sharedPreferences.getInt("TippingPeople", 0);
	}
	public void saveThemeSettings(int selectedTheme){
		editor.putInt("SelectedTheme", selectedTheme);
	}
	public int getSelectedTheme(){
		return sharedPreferences.getInt("SelectedTheme", CalConstants.IS_GLASS_THEME);
	}
	public void saveSelectedCurrenciesCode(String selectedCodes){
		editor.putString("SelectedCodes", selectedCodes);
		editor.commit();
	}
	public String getSelectedCurrenciesCodes(){
		return sharedPreferences.getString("SelectedCodes", "");
	}
	public void saveInAppPurchaseStatus(boolean value){
		editor.putBoolean("INAPP_PURCHASE", value);
		editor.commit();
	}
	public boolean getInAppPurchaseStatus(){
		return sharedPreferences.getBoolean("INAPP_PURCHASE", false);
	}
}
