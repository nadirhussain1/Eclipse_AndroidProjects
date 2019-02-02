package com.game.guessbill;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.game.guessbill.data.GlobalDataManager;

public class CalculatorScreen implements OnClickListener {
    
	static EditText calcInput=null;
	private static boolean isExpressionSolved=false;
	Dialog calculatorDialog=null;
	
	public CalculatorScreen(){
		LayoutInflater inflater = (LayoutInflater)GlobalDataManager.getInstance().context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View calcView=inflater.inflate(R.layout.calculator_screen, null);
		ScalingUtility.getInstance((Activity)GlobalDataManager.getInstance().context).scaleView(calcView);
		bindCalculatorKeyListeners(calcView);
		
		calcInput=(EditText)calcView.findViewById(R.id.calculateEditText);
		calcInput.setOnTouchListener(calInputTouchListener);
		
		Button doneButton=(Button)calcView.findViewById(R.id.calculatorDoneButton);
		doneButton.setOnClickListener(calDoneListener);
		Button cancelButton=(Button)calcView.findViewById(R.id.calculatorCancelButton);
		cancelButton.setOnClickListener(cancelCliclListener);
		
		calculatorDialog=new Dialog(GlobalDataManager.getInstance().context);
		calculatorDialog.setContentView(calcView);
		calculatorDialog.setTitle("Calculator");
	}
	public void showDialog(){
		calculatorDialog.show();
	}
	OnClickListener calDoneListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			doneClick();
			
		}
	};
	OnClickListener cancelCliclListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			calculatorDialog.cancel();
		}
	};
	public void doneClick(){
		if(!isExpressionSolved){
			evaluateExpression();	
		}
		InputScreen.mOutputView.setText(calcInput.getText().toString());
		InputScreen.mOutputView.setSelection(InputScreen.mOutputView.length());
		calculatorDialog.cancel();
	}
	
	private void addToOutput(String symbol) {
		if (isSign(symbol)) {
			if (symbolNotFirst() && symbolNotBefore()) {
				CharSequence sequence = Html.fromHtml(calcInput.getText() + symbol);
				calcInput.setText(sequence);
				calcInput.setSelection(calcInput.getText().length());
			}
		} else {
			CharSequence sequence = Html.fromHtml(calcInput.getText() + symbol);
			calcInput.setText(sequence);
			calcInput.setSelection(calcInput.getText().length());
		}
	}
	private boolean isSign(String input) {
		return (input.equals("+") || input.equals("-") || input.equals("x") || input.equals("/"));
	}

	private boolean symbolNotFirst() {
		return calcInput.getText().toString().length() != 0;
	}

	private boolean symbolNotBefore() {
		char lastSymbol = calcInput.getText().charAt(calcInput.getText().length() - 1);
		boolean as = (lastSymbol != "+".toCharArray()[0] && lastSymbol != "x".toCharArray()[0] && lastSymbol != "/"
				.toCharArray()[0]);
		return as;
	}
	private void clearOutput() {
		calcInput.setText("");
		calcInput.setSelection(0);
	}
	private void backspaceOutput() {
		String text = calcInput.getText().toString();
		if (text.length() - 1 > 0) {
			text = text.substring(0, text.length() - 1);
		} else {
			text = "";
		}
		calcInput.setText(text);
		calcInput.setSelection(text.length());
	}
	private static void evaluateExpression(){
		if(calcInput.getText()!=null && calcInput.getText().toString().trim().length()>0){
			String polishNotation = ExpressionUtils.sortingStation(calcInput.getText().toString(), "(", ")");
			isExpressionSolved=true;
			if (polishNotation.split(" ")[0].matches("\\D*")) {
				calcInput.setText("0");
				calcInput.setSelection(1);
				return;
			}
			String result = ExpressionUtils.calculateExpression(polishNotation);
			calcInput.setText(result);
			calcInput.setSelection(result.length());
		}
	}

	
	OnTouchListener calInputTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			InputMethodManager imm = (InputMethodManager)GlobalDataManager.getInstance().context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(calcInput.getWindowToken(), 0);
			Selection.setSelection(calcInput.getText(), calcInput.length());
			calcInput.requestFocus();
			return true;
		}
	};
	private void bindCalculatorKeyListeners(View view){
		view.findViewById(R.id.buttonNine).setOnClickListener(this);
		view.findViewById(R.id.buttonEight).setOnClickListener(this);
		view.findViewById(R.id.buttonSeven).setOnClickListener(this);
		view.findViewById(R.id.buttonSix).setOnClickListener(this);
		view.findViewById(R.id.buttonFive).setOnClickListener(this);
		view.findViewById(R.id.buttonFour).setOnClickListener(this);
		view.findViewById(R.id.buttonThree).setOnClickListener(this);
		view.findViewById(R.id.buttonTwo).setOnClickListener(this);
		view.findViewById(R.id.buttonOne).setOnClickListener(this);
		view.findViewById(R.id.buttonZero).setOnClickListener(this);
		view.findViewById(R.id.buttonAdd).setOnClickListener(this);
		view.findViewById(R.id.buttonSubtract).setOnClickListener(this);
		view.findViewById(R.id.buttonMultiply).setOnClickListener(this);
		view.findViewById(R.id.buttonDivide).setOnClickListener(this);
		view.findViewById(R.id.buttonEqual).setOnClickListener(this);
		view.findViewById(R.id.clearButton).setOnClickListener(this);
		view.findViewById(R.id.deleteButton).setOnClickListener(this);
		view.findViewById(R.id.buttonDot).setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		isExpressionSolved=false;
		switch (v.getId()) {
		case R.id.buttonAdd:
			addToOutput("+");
			break;
		case R.id.buttonSubtract:
			addToOutput("-");
			break;
		case R.id.buttonDivide:
			addToOutput("/");
			break;
		case R.id.buttonMultiply:
			addToOutput("*");
			break;
		case R.id.buttonEqual:
			evaluateExpression();
			break;
		case R.id.clearButton:
			clearOutput();
			break;
		case R.id.deleteButton:
			backspaceOutput();
			break;
		case R.id.buttonZero:
			addToOutput("0");
			break;
		case R.id.buttonOne:
			addToOutput("1");
			break;
		case R.id.buttonTwo:
			addToOutput("2");
			break;
		case R.id.buttonThree:
			addToOutput("3");
			break;
		case R.id.buttonFour:
			addToOutput("4");
			break;
		case R.id.buttonFive:
			addToOutput("5");
			break;
		case R.id.buttonSix:
			addToOutput("6");
			break;
		case R.id.buttonSeven:
			addToOutput("7");
			break;
		case R.id.buttonEight:
			addToOutput("8");
			break;
		case R.id.buttonNine:
			addToOutput("9");
			break;
		case R.id.buttonDot:
			addToOutput(".");
			break;
		}
	}


}
