
package com.humby.model;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import com.calculations.brain.CalculationsState;
import com.calculations.brain.Calculator;
import com.humby.calculations.R;

public class SharedData {

	private static  SharedData instance = null;
	public   Context   mContext=null;
	public  int        SelectedDrawerItem=0;
	public  int        simpleAdvanceSwitch=0;
	public  boolean    isSolvedPressed=false;
	public  boolean    isVibrationEnabled=true;
	public  int        currentDecimalSeparator=CalConstants.AMERICAN_DECIMAL_SEPARATOR;
	public  boolean    isShowingHistory=false;
	public  String     selectedTheme="light";
	public  String     homeCalInput="";
	public  String     homeCalResultInput="";
	public  EditText   homeCalInputEditArea=null;
	public  TextView   homeCalResultTextView=null;
	public  Calculator homeCalculator=null;
	
	public  int        prevSeleCurrencyIndex=-1;
	public  int        prevSeleMeasurementIndex=-1;
	public  int        prevSelectedMeasCatIndex=-1;
	public  boolean    isKeyPadVisible=true;
	public  boolean    isItemAlreadyPurchased=false;
	public boolean    isGraphiCalculatorVisible=false;
	public  double     currencyInput=1;
	public  double     measurementInput=1;
	public  double     tipBillAmount=0;
	public  int        tipNoOfPeople=0;
	public  double     tipPercentage=0;
	public boolean   isInAPPSetUp=false;
	public  CalculationsState appState=null;

	public int [] abMeasureThumbnails=new int []{R.drawable.ab_length,R.drawable.ab_area,R.drawable.ab_weight,R.drawable.ab_time,R.drawable.ab_temperature,R.drawable.ab_volume,R.drawable.ab_pcmemory,R.drawable.ab_force,R.drawable.ab_cooking,R.drawable.ab_angles,R.drawable.ab_energy,R.drawable.ab_power,R.drawable.ab_speed,R.drawable.ab_density};
	public int [] normalMeasureThumbnails=new int []{R.drawable.nav_length,R.drawable.nav_area,R.drawable.nav_weight,R.drawable.nav_time,R.drawable.nav_temperature,R.drawable.nav_volume,R.drawable.nav_pcmemory,R.drawable.nav_force,R.drawable.nav_cooking,R.drawable.nav_angles,R.drawable.nav_energy,R.drawable.nav_power,R.drawable.nav_speed,R.drawable.nav_density};
	public int [] selectedMeasureThumbnails=new int []{R.drawable.selected_length,R.drawable.selected_area,R.drawable.selected_weight,R.drawable.selected_time,R.drawable.selected_temperature,R.drawable.selected_volume,R.drawable.selected_pcmemory,R.drawable.selected_force,R.drawable.selected_cooking,R.drawable.selected_angles,R.drawable.selected_energy,R.drawable.selected_power,R.drawable.selected_speed,R.drawable.selected_density};
	
	public static SharedData getInstance() {
		if(instance==null){
			instance=new SharedData();
		}
		return instance;

	}
	public void killInstance(){
		instance=null;
	}
	public void setContext(Context context){
		mContext=context;
	}
	public static String discardZerosAfterDecimal(String originalString){
		int dotIndex=originalString.indexOf('.');

		if(dotIndex==-1){
			return originalString;
		}

		char[] decimalString=originalString.substring(dotIndex+1).toCharArray();
		for(int count=0;count<decimalString.length;count++){
			if(decimalString[count]!='0'){
				return originalString;
			}
		}
		return originalString.substring(0,dotIndex);

	}
	public static String RoundNine(String originalString){
		int dotIndex=originalString.indexOf('.');
		if(dotIndex==-1){
			return originalString;
		}
		char[] decimalString=originalString.substring(dotIndex+1).toCharArray();
		int length=decimalString.length;
		if(length>4){
			length=4;
		}
		for(int count=0;count<length;count++){
			if(!(decimalString[count]=='9' || decimalString[count]=='.') ){
				return originalString;
			}

		}
		double result=Double.valueOf(originalString);
		return String.valueOf(Math.ceil(result));
	}
	public static String RoundZero(String originalString){
		int dotIndex=originalString.indexOf('.');
		if(dotIndex==-1){
			return originalString;
		}
		char[] decimalString=originalString.substring(dotIndex+1).toCharArray();

		int length=decimalString.length;
		if(length>2){
			length=2;
		}
		if(originalString.substring(0,1).equalsIgnoreCase("0")){
			return originalString;
		}
		for(int count=0;count<length;count++){
			if(!(decimalString[count]=='0' || decimalString[count]=='.') ){
				return originalString;
			}

		}

		return originalString.substring(0,dotIndex);
	}
	public void applyFontToTextView(TextView view,String fontName){
		Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(), fontName);
		view.setTypeface(typeFace);
	}
	public  void updateTippingValues(double amount,int people,double tip){
		tipBillAmount=amount;
		tipNoOfPeople=people;
		tipPercentage=tip;
	}
	public int convertStringToId(String name){
		return mContext.getResources().getIdentifier(name, "drawable",mContext.getPackageName());
	}
}
