package com.humby.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.calculations.brain.Calculator;
import com.calculations.brain.HomeKeyHandler;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.CountryCurrencyLoader;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class CalculatorFragment extends android.support.v4.app.Fragment {
	private View homeCalView=null;
	private EditText inputDisplayArea=null;
	private HomeKeyHandler  homeKeyHandler =null;
	private TextView resultDisplayArea=null;  
	public  ArrayList<String>historyArrayList=new ArrayList<String>(50);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(SharedData.getInstance().simpleAdvanceSwitch==0){
			homeCalView=inflater.inflate(R.layout.home_simple, container, false);
		}else{
			homeCalView=inflater.inflate(R.layout.home_advanced, container, false);
		}
		ScalingUtility.getInstance().scaleView(homeCalView);
		resultDisplayArea=(TextView)homeCalView.findViewById(R.id.resultDisplayArea);
		
		linkClickHandlerAndDisplay();
		applyFont();
		CountryCurrencyLoader currencyLoader=CountryCurrencyLoader.getInstance();
		currencyLoader.populateCountriesCurrencies();
		return homeCalView;
	}


	@SuppressWarnings("deprecation")
	public void copyResultToClipBoard(){
		String message="Enter equation to copy result";
		if(resultDisplayArea.getText()!=null && !resultDisplayArea.getText().toString().equalsIgnoreCase("") ){
			message="Result Copied";
			ClipboardManager clipboard = (ClipboardManager)SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.CLIPBOARD_SERVICE);
			clipboard.setText(resultDisplayArea.getText().toString());
		}
		Toast.makeText(SharedData.getInstance().mContext,message, Toast.LENGTH_SHORT).show();

	}
	private void linkClickHandlerAndDisplay(){
		inputDisplayArea=(EditText)homeCalView.findViewById(R.id.DisplayInputExpArea);
		inputDisplayArea.setOnTouchListener(InputAreaTouchListener);
	

		Button equalButton=(Button)homeCalView.findViewById(R.id.equalsButton);
		equalButton.setOnClickListener(solveExpressionListener);

		if(SharedData.getInstance().simpleAdvanceSwitch==0){
			String image_name=SharedData.getInstance().selectedTheme+"_"+"button_equals";
			int resId=convertStringToId(image_name);
			equalButton.setBackgroundResource(resId);
		}else{
			String image_name=SharedData.getInstance().selectedTheme+"_"+"adv_equals";
			int resId=convertStringToId(image_name);
			equalButton.setBackgroundResource(resId);
		}
		if(SharedData.getInstance().homeCalculator==null){
			SharedData.getInstance().homeCalculator=new Calculator();
		}
		homeKeyHandler=new HomeKeyHandler(homeCalView, SharedData.getInstance().homeCalculator,CalConstants.HOME_ID);
	}
	private void applyFont(){
		SharedData.getInstance().applyFontToTextView(inputDisplayArea, CalConstants.ROBOT_MEDIUM);
		SharedData.getInstance().applyFontToTextView(resultDisplayArea, CalConstants.ROBOT_LIGHT);
	}
	private int convertStringToId(String image_name){
		return SharedData.getInstance().mContext.getResources().getIdentifier(image_name , "drawable", SharedData.getInstance().mContext.getPackageName());
	}
	OnTouchListener InputAreaTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return true;
		}
	};

	OnClickListener solveExpressionListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			homeKeyHandler.solveMainCalculatorExpression(historyArrayList);
			if(SharedData.getInstance().isShowingHistory){
				//historyListAdapter.notifyDataSetChanged();
			}
		}
	};
}
