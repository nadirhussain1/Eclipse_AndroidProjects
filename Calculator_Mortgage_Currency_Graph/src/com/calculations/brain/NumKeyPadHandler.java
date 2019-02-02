package com.calculations.brain;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import android.os.Vibrator;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculations.preferences.CalculationsPreferences;
import com.humby.adapters.CurrResultAdapter;
import com.humby.adapters.MeasureResultAdapter;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class NumKeyPadHandler {
	private int IS_FROM_PAGE=-1;
	private Vibrator myVib=null;
	private View mainCalcView=null;
	private Double inputValue=0.0;

	private EditText loanAmountEditor=null;
	private EditText loanRatePercEditor=null;
	private EditText loanMonthsEditor=null;


	private EditText currentSelectedEditor=null;
	private EditText tippingTotalBillEditor=null;
	private EditText tippingPeopleEditor=null;
	private EditText tippingTipEditor=null;
	private String currentEditorInput="";

	CurrResultAdapter currResultAdapter=null; 
	MeasureResultAdapter measureResultAdapter=null;
	DecimalFormat decimalFormat = null;

	public NumKeyPadHandler(int currentCalId,View view){
		IS_FROM_PAGE=currentCalId;
		mainCalcView=view;
		myVib = (Vibrator)SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.VIBRATOR_SERVICE);

		if(SharedData.getInstance().currentDecimalSeparator==CalConstants.AMERICAN_DECIMAL_SEPARATOR){
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.US);
			decimalFormat=(DecimalFormat)formatter;
		}else{
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.GERMAN);
			decimalFormat=(DecimalFormat)formatter;
		}

		attachListenerToButtons();
		initDisplayOutputOfCal();
	}
	private void initDisplayOutputOfCal(){
		if(IS_FROM_PAGE==CalConstants.CURRENCY_CONVERTER_ID){
			ListView listView=(ListView)mainCalcView.findViewById(R.id.resultListView);
			TextView selectedCurrencyName=(TextView)mainCalcView.findViewById(R.id.unitTypeTextArea);
			currResultAdapter=new CurrResultAdapter(selectedCurrencyName);
			listView.setAdapter(currResultAdapter);
		}else if(IS_FROM_PAGE==CalConstants.MEASURE_CONV_ID){
			ListView listView=(ListView)mainCalcView.findViewById(R.id.resultListView);
			TextView selectedMeasureName=(TextView)mainCalcView.findViewById(R.id.unitTypeTextArea);
			measureResultAdapter=new MeasureResultAdapter(selectedMeasureName);
			listView.setAdapter(measureResultAdapter);
		}
	}
	private void handleInputChangeOfCal(){
		if(IS_FROM_PAGE==CalConstants.CURRENCY_CONVERTER_ID){
			currResultAdapter.updateUserInput(inputValue);
		}else if(IS_FROM_PAGE==CalConstants.MEASURE_CONV_ID){
			measureResultAdapter.updateUserInput(inputValue);
		}else if(IS_FROM_PAGE==CalConstants.TIPPING_CONV_ID){
			updateTippingCalculationsResults();
		}else if(IS_FROM_PAGE==CalConstants.LOAN_CAL_ID){
			updateLoanCalResults();
		}
	}
	public CurrResultAdapter getCurrencyAdapter(){
		return currResultAdapter;
	}
	public MeasureResultAdapter getMeasureResultAdapter(){
		return measureResultAdapter;
	}
	private void attachInputListener(){
		if(IS_FROM_PAGE==CalConstants.TIPPING_CONV_ID){
			tippingTotalBillEditor=(EditText)mainCalcView.findViewById(R.id.TotalBillInputEditArea);
			tippingPeopleEditor=(EditText)mainCalcView.findViewById(R.id.PeopleBillInputEditArea);
			tippingTipEditor=(EditText)mainCalcView.findViewById(R.id.TipInputEditArea);

			tippingTotalBillEditor.setOnTouchListener(multipleEditorTouchListener);
			tippingPeopleEditor.setOnTouchListener(multipleEditorTouchListener);
			tippingTipEditor.setOnTouchListener(multipleEditorTouchListener);

			currentSelectedEditor=tippingTotalBillEditor;
			currentSelectedEditor.setSelection(currentSelectedEditor.length());
		}else if(IS_FROM_PAGE==CalConstants.LOAN_CAL_ID){
			loanAmountEditor=(EditText)mainCalcView.findViewById(R.id.loanAmountInputEditArea);
			loanRatePercEditor=(EditText)mainCalcView.findViewById(R.id.ratePerInputEditArea);
			loanMonthsEditor=(EditText)mainCalcView.findViewById(R.id.monthsInputEditArea);

			loanAmountEditor.setOnTouchListener(mulLoanEditorTouchListener);
			loanRatePercEditor.setOnTouchListener(mulLoanEditorTouchListener);
			loanMonthsEditor.setOnTouchListener(mulLoanEditorTouchListener);

			currentSelectedEditor=loanAmountEditor;
			currentSelectedEditor.setSelection(currentSelectedEditor.length());
		}
		else{
			currentSelectedEditor=(EditText)mainCalcView.findViewById(R.id.unitInputEditArea);
			currentSelectedEditor.setOnTouchListener(inputAreaTouchListener);
			currentSelectedEditor.setSelection(currentSelectedEditor.length());
		}
	}
	private void attachListenerToButtons(){
		attachInputListener();
		Button [] keyPadButtonsArr=new Button[15];
		keyPadButtonsArr[0]=(Button)mainCalcView.findViewById(R.id.digitSevenButton);
		keyPadButtonsArr[1]=(Button)mainCalcView.findViewById(R.id.digitEightButton);
		keyPadButtonsArr[2]=(Button)mainCalcView.findViewById(R.id.digitNineButton);
		keyPadButtonsArr[3]=(Button)mainCalcView.findViewById(R.id.numPadClearButton);
		keyPadButtonsArr[4]=(Button)mainCalcView.findViewById(R.id.numPadBackSpaceButton);
		keyPadButtonsArr[5]=(Button)mainCalcView.findViewById(R.id.digitFourButton);
		keyPadButtonsArr[6]=(Button)mainCalcView.findViewById(R.id.digitFiveButton);
		keyPadButtonsArr[7]=(Button)mainCalcView.findViewById(R.id.digitSixButton);
		keyPadButtonsArr[8]=(Button)mainCalcView.findViewById(R.id.digitZeroButton);
		keyPadButtonsArr[9]=(Button)mainCalcView.findViewById(R.id.digitDoubleZeroButton);
		keyPadButtonsArr[10]=(Button)mainCalcView.findViewById(R.id.digitOneButton);
		keyPadButtonsArr[11]=(Button)mainCalcView.findViewById(R.id.digitTwoButton);
		keyPadButtonsArr[12]=(Button)mainCalcView.findViewById(R.id.digitThreeButton);
		keyPadButtonsArr[13]=(Button)mainCalcView.findViewById(R.id.digitDecimalButton);
		keyPadButtonsArr[14]=(Button)mainCalcView.findViewById(R.id.keyPadHideButton);



		keyPadButtonsArr[0].setOnClickListener(digitSevenClickListener);
		keyPadButtonsArr[1].setOnClickListener(digitEightClickListener);
		keyPadButtonsArr[2].setOnClickListener(digitNineClickListener);
		keyPadButtonsArr[3].setOnClickListener(clearButtonClickListener);
		keyPadButtonsArr[4].setOnClickListener(backSpaceButtonClickListener);
		keyPadButtonsArr[5].setOnClickListener(digitFourClickListener);
		keyPadButtonsArr[6].setOnClickListener(digitFiveClickListener);
		keyPadButtonsArr[7].setOnClickListener(digitSixClickListener);
		keyPadButtonsArr[8].setOnClickListener(zeroButtonClickListener);
		keyPadButtonsArr[9].setOnClickListener(doubleZeroButtonClickListener);
		keyPadButtonsArr[10].setOnClickListener(digitOneClickListener);
		keyPadButtonsArr[11].setOnClickListener(digitTwoClickListener);
		keyPadButtonsArr[12].setOnClickListener(digitThreeClickListener);
		keyPadButtonsArr[13].setOnClickListener(dotButtonClickListener);
		keyPadButtonsArr[14].setOnClickListener(hideButtonClickListener);
	}
	private void processInputEffect(){
		inputValue=0.0;

		if(SharedData.getInstance().isVibrationEnabled){
			myVib.vibrate(CalConstants.VIBRATION_DURATION);
		}

		String inputValueString= currentEditorInput;
		if(!currentEditorInput.equalsIgnoreCase("")){
			currentSelectedEditor.setText(decimalFormat.format(Double.valueOf(currentEditorInput)));
			currentSelectedEditor.setSelection(currentSelectedEditor.length());
		}else{
			currentSelectedEditor.setText("");
			currentSelectedEditor.setSelection(0);
		}

		if(isValidDouble(inputValueString)){
			if(inputValueString.contains("-")){
				if(inputValueString.length()>1){
					inputValue=solveExpression(inputValueString); 
				}
			}else{
				inputValue=solveExpression(inputValueString);
			}

		}
		handleInputChangeOfCal();
	}
	private void updateLoanCalResults(){
		Double  totalLoanAmount=0.0;
		Double  loanRatePercentage=0.0;
		int     loanMonths=0;

		if(loanAmountEditor.getText()!=null && isValidDouble(loanAmountEditor.getText().toString())){
			try {
				totalLoanAmount=decimalFormat.parse(loanAmountEditor.getText().toString()).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if(loanRatePercEditor.getText()!=null && isValidDouble(loanRatePercEditor.getText().toString())){
			try {
				loanRatePercentage=decimalFormat.parse(loanRatePercEditor.getText().toString()).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(loanMonthsEditor.getText()!=null && isValidDouble(loanMonthsEditor.getText().toString())){
			try {
				loanMonths=decimalFormat.parse(loanMonthsEditor.getText().toString()).intValue();		
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		calculateLoanResults(loanRatePercentage,loanMonths,totalLoanAmount);
	}
	private void calculateLoanResults(double interestRate,int noOfMonths,double loanAmount){
		String [] mortgageValues=new String[8];
		Double denominator=1.0;
		Double monthlyPayment=0.0;
		Double biWeeklyPayment=0.0;
		NumberFormat formatter=NumberFormat.getInstance(Locale.US);

		interestRate/=1200;

		denominator=1-Math.pow((1+interestRate), (-1*noOfMonths) );
		if(denominator!=0){
			monthlyPayment=((interestRate*loanAmount)/denominator);
		}

		monthlyPayment=roundDouble(monthlyPayment, 2);
		int noOfPayments=(noOfMonths);
		Double totalAmount=(monthlyPayment*noOfPayments);
		totalAmount=roundDouble(totalAmount,2);
		Double interestPaid=totalAmount-loanAmount;
		interestPaid=roundDouble(interestPaid,2);

		mortgageValues[0]=monthlyPayment.toString();
		mortgageValues[1]=String.valueOf(noOfPayments);
		mortgageValues[2]=interestPaid.toString();
		mortgageValues[3]=totalAmount.toString();

		interestRate*=12;
		int noOfBiWeekPayments=(noOfMonths/12)*26;
		double power=(12*noOfBiWeekPayments)/26;
		Double Z=Math.pow((1+interestRate/12),power);
		if(Z!=1){
			biWeeklyPayment=((interestRate/26)*loanAmount*Z)/(Z-1);
		}

		biWeeklyPayment=roundDouble(biWeeklyPayment,2);
		totalAmount=noOfBiWeekPayments*biWeeklyPayment;
		totalAmount=roundDouble(totalAmount,2);
		interestPaid=totalAmount-loanAmount;
		interestPaid=roundDouble(interestPaid,2);

		mortgageValues[4]=biWeeklyPayment.toString();
		mortgageValues[5]=String.valueOf(noOfBiWeekPayments);
		mortgageValues[6]=interestPaid.toString();
		mortgageValues[7]=totalAmount.toString();

		for(int i=0;i<mortgageValues.length;i++){

			if(mortgageValues[i].equalsIgnoreCase("0.0")){
				mortgageValues[i]="0";	
			}
		}
		((TextView)mainCalcView.findViewById(R.id.monthlyPaymentAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[0])));
		((TextView)mainCalcView.findViewById(R.id.NoOfPaymentsAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[1])));
		((TextView)mainCalcView.findViewById(R.id.totalInterestAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[2])));
		((TextView)mainCalcView.findViewById(R.id.totalPaymentsAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[3])));
		
		((TextView)mainCalcView.findViewById(R.id.BiWeekPaymentAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[4])));
		((TextView)mainCalcView.findViewById(R.id.BiWeekNoPaymentsAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[5])));
		((TextView)mainCalcView.findViewById(R.id.BiTotalInterestAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[6])));
		((TextView)mainCalcView.findViewById(R.id.BiTotalPayAmount)).setText(decimalFormat.format(Double.parseDouble(mortgageValues[7])));
	}
	private void updateTippingCalculationsResults(){
		Double billAmount=0.0;
		int    noOfPeople=0;
		Double tipPercentage=0.0;

		if(tippingTotalBillEditor.getText()!=null && isValidDouble(tippingTotalBillEditor.getText().toString())){
			try {
				billAmount=decimalFormat.parse(tippingTotalBillEditor.getText().toString()).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if(tippingPeopleEditor.getText()!=null && isValidDouble(tippingPeopleEditor.getText().toString())){
			try {
				noOfPeople=decimalFormat.parse(tippingPeopleEditor.getText().toString()).intValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(tippingTipEditor.getText()!=null && isValidDouble(tippingTipEditor.getText().toString())){
			try {
				tipPercentage=decimalFormat.parse(tippingTipEditor.getText().toString()).doubleValue();
				tipPercentage*=0.01;
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		SharedData.getInstance().updateTippingValues(billAmount, noOfPeople, tipPercentage*100);
		calculateTippingResults(billAmount,noOfPeople,tipPercentage);
	}
	private void calculateTippingResults(double billAmount,int noOfPeople,double tipPercentage){
		Double totalTipOfGroup=0.0;
		Double tipPerPerson=0.0;
		Double totalGroupBill=0.0;
		Double totalPersonBill=0.0;
		Double salesTaxOfGroup=0.0;
		Double salesTaxOfPerson=0.0;

		String [] tippingValues=new String[6];

		int salesTaxFlag=CalculationsPreferences.getInstance().getSalesTaxSettingFlag();
		float salesTaxValue=CalculationsPreferences.getInstance().getSalesTaxValuefromPref();
		salesTaxValue=(float) (salesTaxValue*0.01);

		if(salesTaxFlag==CalConstants.SALES_TAX_ON ){
			totalTipOfGroup=(tipPercentage*billAmount)/(1+salesTaxValue);
			salesTaxOfGroup=billAmount-(billAmount/(1+salesTaxValue));
			totalGroupBill=billAmount+totalTipOfGroup;
		}
		else if(salesTaxFlag==CalConstants.SALES_TAX_OFF) {
			totalTipOfGroup=(tipPercentage*billAmount);
			salesTaxOfGroup=billAmount*salesTaxValue;
			totalGroupBill=billAmount+totalTipOfGroup+salesTaxOfGroup;
		}else{
			totalTipOfGroup=(tipPercentage*billAmount);
			salesTaxOfGroup=0.0;
			totalGroupBill=billAmount+totalTipOfGroup;	
		}

		if(noOfPeople!=0){
			tipPerPerson=totalTipOfGroup/noOfPeople;
			totalPersonBill=totalGroupBill/noOfPeople;
			salesTaxOfPerson=salesTaxOfGroup/noOfPeople;
		}  

		totalTipOfGroup=roundDouble(totalTipOfGroup,2);
		salesTaxOfGroup=roundDouble(salesTaxOfGroup,2);
		totalGroupBill=roundDouble(totalGroupBill,2);
		tipPerPerson=roundDouble(tipPerPerson,2);
		totalPersonBill=roundDouble(totalPersonBill,2);
		salesTaxOfPerson=roundDouble(salesTaxOfPerson,2);


		tippingValues[0]=tipPerPerson.toString();
		tippingValues[1]=salesTaxOfPerson.toString();
		tippingValues[2]=totalPersonBill.toString();

		tippingValues[3]=totalTipOfGroup.toString();
		tippingValues[4]=salesTaxOfGroup.toString();
		tippingValues[5]=totalGroupBill.toString();

		String doubleZero="00";
		String singleZero="0";

		for(int i=0;i<tippingValues.length;i++){
			int decimalIndex=tippingValues[i].indexOf('.');
			if(tippingValues[i].equalsIgnoreCase("0.0")){
				tippingValues[i]="0";	
			}else if(decimalIndex!=-1 && tippingValues[i]!="n/a" && tippingValues[i].substring(decimalIndex+1).length()<2){
				int length=tippingValues[i].substring(decimalIndex).length();

				if(length==0){
					tippingValues[i]+=doubleZero;
				}else{
					tippingValues[i]+=singleZero;
				}
			}
		}

		((TextView)mainCalcView.findViewById(R.id.TipPerPersonAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[0])));
		((TextView)mainCalcView.findViewById(R.id.TaxPerPersonAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[1])));
		((TextView)mainCalcView.findViewById(R.id.TotalPerPersonAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[2])));

		((TextView)mainCalcView.findViewById(R.id.TipGroupAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[3])));
		((TextView)mainCalcView.findViewById(R.id.TaxGroupAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[4])));
		((TextView)mainCalcView.findViewById(R.id.TotalGroupAmount)).setText(decimalFormat.format(Double.parseDouble(tippingValues[5])));
	}
	private Double solveExpression(String inputValueString){

		if(inputValueString.contains("-")){
			Double number=Double.valueOf(inputValueString.substring(1));
			return -1*number;			
		}
		return Double.valueOf(inputValueString);
	}

	OnTouchListener inputAreaTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			SharedData.getInstance().isKeyPadVisible=true;

			currentSelectedEditor.append("");
			currentSelectedEditor.setSelection(currentSelectedEditor.length());
			mainCalcView.findViewById(R.id.keyPadLayout).setVisibility(View.VISIBLE);

			if(IS_FROM_PAGE!=CalConstants.TIPPING_CONV_ID){
				ListView resultListView=(ListView)mainCalcView.findViewById(R.id.resultListView);
				int height=ScalingUtility.getInstance().resizeProvidedHeight(CalConstants.RESULT_LISTVIEW_HEIGHT);
				RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);	
				layoutParams.addRule(RelativeLayout.ABOVE,R.id.keyPadLayout);
				resultListView.setLayoutParams(layoutParams);
			}
			return true;
		}
	};
	OnTouchListener mulLoanEditorTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (view.getId()) {
			case R.id.loanAmountInputEditArea:
				currentSelectedEditor=loanAmountEditor;
				currentEditorInput="";
				Selection.setSelection(loanAmountEditor.getText(), loanAmountEditor.length());
				loanAmountEditor.requestFocus();
				break;
			case R.id.ratePerInputEditArea:
				currentSelectedEditor=loanRatePercEditor;
				currentEditorInput="";
				Selection.setSelection(loanRatePercEditor.getText(), loanRatePercEditor.length());
				loanRatePercEditor.requestFocus();
				break;
			case R.id.monthsInputEditArea:
				currentSelectedEditor=loanMonthsEditor;
				currentEditorInput="";
				Selection.setSelection(loanMonthsEditor.getText(), loanMonthsEditor.length());
				loanMonthsEditor.requestFocus();
				break;

			}
			mainCalcView.findViewById(R.id.keyPadLayout).setVisibility(View.VISIBLE);
			SharedData.getInstance().isKeyPadVisible=true;
			return true;

		}
	};
	OnTouchListener multipleEditorTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (view.getId()) {
			case R.id.TotalBillInputEditArea:
				currentSelectedEditor=tippingTotalBillEditor;
				currentEditorInput="";
				Selection.setSelection(tippingTotalBillEditor.getText(), tippingTotalBillEditor.length());
				tippingTotalBillEditor.requestFocus();
				break;
			case R.id.PeopleBillInputEditArea:
				currentSelectedEditor=tippingPeopleEditor;
				currentEditorInput="";
				Selection.setSelection(tippingPeopleEditor.getText(), tippingPeopleEditor.length());
				tippingPeopleEditor.requestFocus();
				break;
			case R.id.TipInputEditArea:
				currentSelectedEditor=tippingTipEditor;
				currentEditorInput="";
				Selection.setSelection(tippingTipEditor.getText(), tippingTipEditor.length());
				tippingTipEditor.requestFocus();
				break;

			}
			mainCalcView.findViewById(R.id.keyPadLayout).setVisibility(View.VISIBLE);
			SharedData.getInstance().isKeyPadVisible=true;
			return true;

		}
	};
	OnClickListener hideButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			SharedData.getInstance().isKeyPadVisible=false;
			mainCalcView.findViewById(R.id.keyPadLayout).setVisibility(View.GONE);

			if(IS_FROM_PAGE!=CalConstants.TIPPING_CONV_ID && IS_FROM_PAGE!=CalConstants.LOAN_CAL_ID){
				ListView resultListView=(ListView)mainCalcView.findViewById(R.id.resultListView);
				int height=ScalingUtility.getInstance().resizeProvidedHeight(CalConstants.RESULT_LISTVIEW_FULL_HEIGHT);
				RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);	
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				resultListView.setLayoutParams(layoutParams);
			}
		}
	};
	public OnClickListener digitSevenClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="7";
				processInputEffect();
			}

		}
	};
	public OnClickListener digitEightClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="8";
				processInputEffect();
			}

		}
	};
	public OnClickListener digitNineClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="9";
				processInputEffect();
			}

		}
	};
	public OnClickListener clearButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			currentEditorInput="";
			processInputEffect();

		}
	};
	public OnClickListener backSpaceButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (currentEditorInput.length() >= 1) {
				currentEditorInput = currentEditorInput.substring(0, currentEditorInput.length() - 1);
			}
			processInputEffect();

		}
	};
	public OnClickListener digitFourClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="4";	
				processInputEffect();
			}
		}
	};
	public OnClickListener digitFiveClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="5";	
				processInputEffect();
			}

		}
	};
	public OnClickListener digitSixClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="6";
				processInputEffect();
			}

		}
	};
	public OnClickListener zeroButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="0";
				processInputEffect();
			}

		}
	};
	public OnClickListener doubleZeroButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(2)){
				currentEditorInput+="00";
				processInputEffect();
			}

		}
	};
	public OnClickListener digitOneClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="1";
				processInputEffect();
			}
		}
	};
	public OnClickListener digitTwoClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="2";
				processInputEffect();
			}

		}
	};
	public OnClickListener digitThreeClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+="3";
				processInputEffect();
			}

		}
	};
	public OnClickListener dotButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(shouldAppendInput(1)){
				currentEditorInput+=".";
				processInputEffect();
			}

		}
	};
	private boolean shouldAppendInput(int noOfDigits){
		//		if(IS_FROM_PAGE==CalConstants.MORTGAGE_CONV_ID){
		//
		//			if((currentSelectedEditor==secondInputValueContainer && (currentSelectedEditor.length()+noOfDigits)>6) || (currentSelectedEditor==fourthInputValueContainer && (currentSelectedEditor.length()+noOfDigits)>5) ){
		//
		//				return false;
		//			}
		//		}
		return true;
	}
	private boolean isValidDouble(String numberText){
		return((numberText.indexOf('.') != ( numberText.length()-1)) && !numberText.equalsIgnoreCase("") );

	}
	private  Double roundDouble(Double unroundedNumber,int precisionLimit){
		if(isValidDouble(unroundedNumber.toString())){
			BigDecimal bd = new BigDecimal(unroundedNumber);
			BigDecimal rounded = bd.setScale(precisionLimit, BigDecimal.ROUND_CEILING);
			return rounded.doubleValue();
		}
		return 1.0;
	}


}
