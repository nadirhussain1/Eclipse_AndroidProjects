package com.game.guessbill;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.game.guessbill.data.GlobalDataManager;

public class InputScreen{
	static EditText userNameEditor=null;
	static EditText mOutputView=null;
	Button submitButton=null;
	Button continueButton=null;
	TextView helpText=null;
	TextView playersNamesText=null;
    
	public InputScreen(View view){
		userNameEditor=(EditText)view.findViewById(R.id.userNameEditText);
		mOutputView=(EditText)view.findViewById(R.id.guessAmountEditText);
		playersNamesText=(TextView)view.findViewById(R.id.PlayersNamesText);
		clearInputData();
		view.setVisibility(View.VISIBLE);

		Button calculatorButton=(Button)view.findViewById(R.id.CalculatorButton);
		calculatorButton.setOnClickListener(calculatorButtonListener);

		userNameEditor.addTextChangedListener(userNameTextChangeWatcher);
		mOutputView.addTextChangedListener(guessInputChangeWatcher);

		helpText=(TextView)view.findViewById(R.id.firstHelpText);
		submitButton=(Button)view.findViewById(R.id.submitGuessButton);
		continueButton=(Button)view.findViewById(R.id.continueButton);
		
		submitButton.setEnabled(false);
		continueButton.setEnabled(false);
		
		GlobalDataManager.getInstance().currentScreen=R.id.inputScreen;
	}

	OnClickListener calculatorButtonListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			CalculatorScreen screen=new CalculatorScreen();
			screen.showDialog();

		}
	};
	public void clearInputData(){
		mOutputView.setText("");
		mOutputView.setSelection(0);
		userNameEditor.setText("");
		

	}

	private  boolean isValidInput(){
		if(mOutputView.getText()!=null && mOutputView.getText().toString().trim().length()>0 && userNameEditor.getText()!=null && userNameEditor.getText().toString().trim().length()>0 ){
			return true;
		}
		return false;
	}

	private static Double parseDouble(String number){
		try
		{
			Double input=Double.parseDouble(number);
			return GlobalDataManager.getInstance().round(input, 2);

		}
		catch(NumberFormatException e)
		{
			return 0.0;
		}
	}
	public boolean continueClick(){
		GlobalDataManager.getInstance().amount=0.0;
		GlobalDataManager.getInstance().userName=null;

		if(isValidInput()){
			GlobalDataManager.getInstance().amount=parseDouble(mOutputView.getText().toString());
			GlobalDataManager.getInstance().userName=userNameEditor.getText().toString();
			return true;
		}
		return false;
	}
	TextWatcher userNameTextChangeWatcher=new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {		
		}

		@Override
		public void afterTextChanged(Editable s) {
			submitButton.setEnabled(false);
			if(mOutputView.getText().length()==0 && userNameEditor.getText().length()==0){
				helpText.setText(" Please enter your name and guess");
			}
			else if(mOutputView.getText().length()==0){
				helpText.setText("Please enter your guess");
			}else if(userNameEditor.getText().length()==0){
				helpText.setText("Please enter your name");
			}else{
				helpText.setText("");
				submitButton.setEnabled(true);
			}
		}
	};
	TextWatcher guessInputChangeWatcher=new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			submitButton.setEnabled(false);
			if(mOutputView.getText().length()==0 && userNameEditor.getText().length()==0){
				helpText.setText(" Please enter your name and guess");
			}
			else if(mOutputView.getText().length()==0){
				helpText.setText("Please enter your guess");
			}else if(userNameEditor.getText().length()==0){
				helpText.setText("Please enter your name");
			}else{
				helpText.setText("");
				submitButton.setEnabled(true);
			}

		}
	};



}
