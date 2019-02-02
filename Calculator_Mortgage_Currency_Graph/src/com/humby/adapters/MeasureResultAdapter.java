package com.humby.adapters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.calculations.preferences.CalculationsPreferences;
import com.calculations.screen.DropDownMenuWindow;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class MeasureResultAdapter extends BaseAdapter {
	private TextView updateSelectedView=null;
	private View     dropDownXmlView=null;
	private ListView dropDownListView=null;
	private String[][]interactivetwoDList=null;
	private int currentDisplayedListIndex=0;
	private int currentSelectedMeasurement=-1;
	private Double measurementConversionFactor=1.00;
	private Double [] temperatureValues=new Double[4];
	private Double inputValue=1.00;
	DecimalFormat decimalFormat = null;

	public MeasureResultAdapter(TextView selectedUnitText){

		if(SharedData.getInstance().currentDecimalSeparator==CalConstants.AMERICAN_DECIMAL_SEPARATOR){
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.US);
			decimalFormat=(DecimalFormat)formatter;
		}else{
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.GERMAN);
			decimalFormat=(DecimalFormat)formatter;
		}
		
		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dropDownXmlView = inflater.inflate(R.layout.general_menu_list, null, false);
		dropDownListView=(ListView)dropDownXmlView.findViewById(R.id.generalMenuList);
		
		currentDisplayedListIndex=SharedData.getInstance().prevSelectedMeasCatIndex;
		updateSelectedView=selectedUnitText;
		updateSelectedView.setOnClickListener(switchInputTypeListener);
		loadStringArrays();
		retrievePrevSavedValues();
		restorePrevState();
	}
	private void retrievePrevSavedValues(){
		currentSelectedMeasurement=CalculationsPreferences.getInstance().getPrevSelectedMeasurementUnit();
		SharedData.getInstance().prevSeleMeasurementIndex=currentSelectedMeasurement;
		inputValue=SharedData.getInstance().measurementInput;
	}
	private void restorePrevState(){
		updateSelectedView.setText(interactivetwoDList[currentDisplayedListIndex][currentSelectedMeasurement].toUpperCase());
		updateMeasurementConversionFactor();
	}
	private void loadStringArrays(){
		String lengthMeasurementTypes[] =SharedData.getInstance().mContext.getResources().getStringArray(R.array.lengthtypes_array);
		String areaMeasurementTypes[]   =SharedData.getInstance().mContext.getResources().getStringArray(R.array.areatypes_array);
		String weightMeasurementTypes[] =SharedData.getInstance().mContext.getResources().getStringArray(R.array.weighttypes_array);
		String timeMeasurementTypes[]   =SharedData.getInstance().mContext.getResources().getStringArray(R.array.timetypes_array);
		String temperatureMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.temptypes_array);
		String volumeMeasurementUnitTypes[] =SharedData.getInstance().mContext.getResources().getStringArray(R.array.volumetypes_array);
		String memoryMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.memorytypes_array);
		String forceMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.forcetypes_array);
		String cookingMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.cookingtypes_array);
		String anglesMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.angletypes_array);
		String energyMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.energytypes_array);
		String powerMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.powertypes_array);
		String speedMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.speedtypes_array);
		String densityMeasurementTypes[]=SharedData.getInstance().mContext.getResources().getStringArray(R.array.densitytypes_array);

		interactivetwoDList=new String[][]{lengthMeasurementTypes,areaMeasurementTypes,weightMeasurementTypes,timeMeasurementTypes,temperatureMeasurementTypes,volumeMeasurementUnitTypes,memoryMeasurementTypes,forceMeasurementTypes,cookingMeasurementTypes,anglesMeasurementTypes,energyMeasurementTypes,powerMeasurementTypes,speedMeasurementTypes,densityMeasurementTypes};
	}
	public void updateSelectedMeasurementType(int position){
		if(position<interactivetwoDList[currentDisplayedListIndex].length){
			SharedData.getInstance().prevSeleMeasurementIndex=position;
			currentSelectedMeasurement=position;
			String selectedType=interactivetwoDList[currentDisplayedListIndex][position];
			if(updateSelectedView!=null){
				updateSelectedView.setText(selectedType.toUpperCase());
			}

			updateMeasurementConversionFactor();
			CalculationsPreferences.getInstance().savePrevSelectedMeasurementUnitType(position);
		}

	}
	private void updateMeasurementConversionFactor(){
		if(currentDisplayedListIndex<CalConstants.measurementValues.length){
			if(currentDisplayedListIndex==CalConstants.IS_TEMPERATURE_CONVERSION){
				populateTemperatureList(inputValue,(short) currentSelectedMeasurement);
			}
			else{
				measurementConversionFactor=inputValue/CalConstants.measurementValues[currentDisplayedListIndex][currentSelectedMeasurement];
			}
		}
		this.notifyDataSetChanged();
	}
	private void populateTemperatureList(double sourceValue,short selectedTempUnitType){
		switch (selectedTempUnitType) {
		case CalConstants.IS_CELSIUS_SCALE:
			convertFromCelsiusToOtherUnits(sourceValue);
			break;
		case CalConstants.IS_FARENHEIT_SCALE:
			sourceValue=(sourceValue-32)*0.556;
			convertFromCelsiusToOtherUnits(sourceValue);
			break;
		case CalConstants.IS_KELVIN_SCALE:
			sourceValue=(sourceValue-273.15);
			convertFromCelsiusToOtherUnits(sourceValue);
			break;
		case CalConstants.IS_RANKINE_SCALE:
			sourceValue=(sourceValue-491.67)/1.8;
			convertFromCelsiusToOtherUnits(sourceValue);
			break;

		default:
			break;
		}
	}
	private void convertFromCelsiusToOtherUnits(double sourceValue){
		temperatureValues[0]=sourceValue;
		temperatureValues[1]=sourceValue*1.8+32.00;
		temperatureValues[2]=sourceValue+273.15;
		temperatureValues[3]=(sourceValue*1.8)+491.67;

	}
	private void prepareMenuWindow(){
	
		DropDownMenuWindow dropDownWindow=new DropDownMenuWindow(dropDownXmlView);
		dropDownWindow.showCalculatorMenu(updateSelectedView);	

		MeasureUnitsAdapter measureUnitAdapter=new MeasureUnitsAdapter(interactivetwoDList[currentDisplayedListIndex],dropDownWindow,this);
		dropDownListView.setAdapter(measureUnitAdapter);
	}
	public void updateUserInput(Double inputValueMeas) {
		this.inputValue = inputValueMeas;
		updateMeasurementConversionFactor();
		SharedData.getInstance().measurementInput=inputValue;


	}
	public void updateCurrentSelectedListIndex(int index ){
		currentDisplayedListIndex=(short) index;
		currentSelectedMeasurement=0;
		SharedData.getInstance().prevSeleMeasurementIndex=currentSelectedMeasurement;
		updateCurrentSelectedListIndex();
		CalculationsPreferences.getInstance().savePrevSelectedMeasurementCat(index);
		CalculationsPreferences.getInstance().savePrevSelectedMeasurementUnitType(currentSelectedMeasurement);
	}
	private void updateCurrentSelectedListIndex(){

		updateSelectedView.setText(interactivetwoDList[currentDisplayedListIndex][currentSelectedMeasurement].toUpperCase());
		updateMeasurementConversionFactor();

	}
	private static Double roundDouble(Double unroundedNumber,short precisionLimit){
		BigDecimal bd = new BigDecimal(unroundedNumber);
		BigDecimal rounded = bd.setScale(precisionLimit, BigDecimal.ROUND_CEILING);
		return rounded.doubleValue();
	}
	OnClickListener switchInputTypeListener=new OnClickListener() {

		@Override
		public void onClick(View view) {			
			prepareMenuWindow();
		}
	};
	OnClickListener resultListRowClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {
			String clickedResult=(String) clickedView.getTag();
			ClipboardManager clipboard = (ClipboardManager)SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.CLIPBOARD_SERVICE);
			clipboard.setText(clickedResult);
			Toast.makeText(SharedData.getInstance().mContext, "Result copied to clipboard", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public int getCount() {

		return (interactivetwoDList[currentDisplayedListIndex].length-1);	
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.measure_result_list_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);
		}
		if(position>=currentSelectedMeasurement){
			position=position+1;
		}
		TextView inputUnitTypeView=(TextView)convertView.findViewById(R.id.resultTextTitle);
		TextView resultValueTextView=(TextView)convertView.findViewById(R.id.resultTextAmount);
		SharedData.getInstance().applyFontToTextView(inputUnitTypeView, CalConstants.ROBOT_CONDENSED);
		SharedData.getInstance().applyFontToTextView(resultValueTextView, CalConstants.ROBOT_LIGHT);

		inputUnitTypeView.setText("");
		resultValueTextView.setText("");

		Double resultValue=-1.00;

		if(currentDisplayedListIndex<CalConstants.measurementValues.length){

			if(currentDisplayedListIndex==CalConstants.IS_TEMPERATURE_CONVERSION && position<temperatureValues.length){
				resultValue=temperatureValues[position];	
			}
			else if(position<CalConstants.measurementValues[currentDisplayedListIndex].length){
				resultValue=measurementConversionFactor*CalConstants.measurementValues[currentDisplayedListIndex][position];

			}
			int decimalIndex=resultValue.toString().indexOf('.');
			if(resultValue !=-1){
				if(resultValue>1){
					resultValue= roundDouble(resultValue,(short)2);
				}else{
					resultValue= roundDouble(resultValue,(short)6);
				}
				String truncatedDecimal=BigDecimal.valueOf(resultValue).toPlainString();
				int index=11;
				if(decimalIndex==index){
					index--;
				}	
				truncatedDecimal=SharedData.getInstance().RoundNine(truncatedDecimal);
				truncatedDecimal=SharedData.getInstance().RoundZero(truncatedDecimal);
				if(truncatedDecimal.length()>10){
					truncatedDecimal=truncatedDecimal.substring(0,index);	
				}
				truncatedDecimal=SharedData.getInstance().discardZerosAfterDecimal(truncatedDecimal);
				truncatedDecimal=decimalFormat.format(Double.parseDouble(truncatedDecimal));
				convertView.setTag(truncatedDecimal);
				resultValueTextView.setText(truncatedDecimal);
			}


		}

		if(position<interactivetwoDList[currentDisplayedListIndex].length){
			inputUnitTypeView.setText(interactivetwoDList[currentDisplayedListIndex][position].toUpperCase());
		}

		inputUnitTypeView.setLines(1);
		inputUnitTypeView.setMaxLines(1);
		inputUnitTypeView.setSingleLine(true);
		inputUnitTypeView.setSelected(true);
		inputUnitTypeView.setHorizontalFadingEdgeEnabled(true);
		inputUnitTypeView.setFadingEdgeLength(70);
		inputUnitTypeView.setEllipsize(TruncateAt.MARQUEE);
		inputUnitTypeView.setHorizontallyScrolling(true);

		resultValueTextView.setLines(1);
		resultValueTextView.setMaxLines(1);
		resultValueTextView.setSingleLine(true);
		resultValueTextView.setSelected(true);
		resultValueTextView.setHorizontalFadingEdgeEnabled(true);
		resultValueTextView.setFadingEdgeLength(70);

		convertView.setOnClickListener(resultListRowClickListener);
		return convertView;	
	}

}
