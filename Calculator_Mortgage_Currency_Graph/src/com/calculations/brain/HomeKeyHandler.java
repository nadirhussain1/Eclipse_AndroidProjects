package com.calculations.brain;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Vibrator;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.support.calculations.CalcItem;
import com.support.calculations.ComplexNumber;
import com.support.calculations.ComplexNumberStr;
import com.support.calculations.Primitive;

public class HomeKeyHandler {
	private Calculator     currentCalculator=null;
	private CalcEditText   inputExpressionArea=null;
	private TextView       resultDisplayArea=null;

	private int            IS_FROM_CALCULATOR=-1;
	private Vibrator       myVib=null;
	private DecimalFormat decimalFormat = null;

	String [] simpleButtonImagesNames=new String[]{"button_00","button_0","button_decimal","button_1","button_2","button_3","button_4","button_5","button_6","button_7","button_8","button_9","button_multiply","button_divide","button_add","button_subtract"};
	String [] advanceImagesNames=new String[]{"adv_pi","adv_addsub","adv_ln","adv_inverse","adv_tenpow","adv_mod","adv_sin","adv_cos","adv_tan","adv_carrot","adv_left_paren","adv_right_paren","adv_exp","adv_factorial","adv_eul","adv_asin","adv_acos","adv_atan"};
	String [] miscelButtonImageNames=new String []{"button_percent","clear_home_selector","home_backspace_selector","button_sqrt"};

	public HomeKeyHandler(View view,Calculator callerObject,int IS_FROM_PAGE){
		this.currentCalculator=callerObject;
		IS_FROM_CALCULATOR=IS_FROM_PAGE;
		setUpUserInteractionWithButton(view);
		myVib = (Vibrator) SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.VIBRATOR_SERVICE);

		if(SharedData.getInstance().currentDecimalSeparator==CalConstants.AMERICAN_DECIMAL_SEPARATOR){
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.US);
			decimalFormat=(DecimalFormat)formatter;
		}else{
			NumberFormat formatter=NumberFormat.getNumberInstance(Locale.GERMAN);
			decimalFormat=(DecimalFormat)formatter;
		}
	}
	private void setUpUserInteractionWithButton(View view){
		inputExpressionArea=(CalcEditText)view.findViewById(R.id.DisplayInputExpArea);
		inputExpressionArea.setOnLongClickListener(longPressResultCopyListener);
		handleSimpleCalculatorClicks(view);
		handleMiscelleneousButtons(view);
		if(SharedData.getInstance().simpleAdvanceSwitch==1 || IS_FROM_CALCULATOR==CalConstants.GRAPH_CAL_ID){
			handleAdvancedCalculatorClicks(view);
		}
		if(IS_FROM_CALCULATOR==CalConstants.HOME_ID){
			inputExpressionArea.setText(SharedData.getInstance().homeCalInput);
			resultDisplayArea.setText(SharedData.getInstance().homeCalResultInput);
			SharedData.getInstance().homeCalInputEditArea=inputExpressionArea;
			SharedData.getInstance().homeCalResultTextView=resultDisplayArea;
		}

	}
	private void handleSimpleCalculatorClicks(View view){
		Button [] simpleButtonsArr=new Button[16];

		simpleButtonsArr[0]=(Button)view.findViewById(R.id.digitDoubleZeroButton);
		simpleButtonsArr[1]=(Button)view.findViewById(R.id.digitZeroButton);
		simpleButtonsArr[2]=(Button)view.findViewById(R.id.digitDecimalButton);
		simpleButtonsArr[3]=(Button)view.findViewById(R.id.digitOneButton);
		simpleButtonsArr[4]=(Button)view.findViewById(R.id.digitTwoButton);
		simpleButtonsArr[5]=(Button)view.findViewById(R.id.digitThreeButton);
		simpleButtonsArr[6]=(Button)view.findViewById(R.id.digitFourButton);
		simpleButtonsArr[7]=(Button)view.findViewById(R.id.digitFiveButton);
		simpleButtonsArr[8]=(Button)view.findViewById(R.id.digitSixButton);
		simpleButtonsArr[9]=(Button)view.findViewById(R.id.digitSevenButton);
		simpleButtonsArr[10]=(Button)view.findViewById(R.id.digitEightButton);
		simpleButtonsArr[11]=(Button)view.findViewById(R.id.digitNineButton);
		simpleButtonsArr[12]=(Button)view.findViewById(R.id.multiplyButton);
		simpleButtonsArr[13]=(Button)view.findViewById(R.id.divideButton);
		simpleButtonsArr[14]=(Button)view.findViewById(R.id.addButton);
		simpleButtonsArr[15]=(Button)view.findViewById(R.id.subtractButton);



		simpleButtonsArr[0].setOnClickListener(makeClickListener("00","00"));
		simpleButtonsArr[1].setOnClickListener(makeClickListener("0","0"));
		simpleButtonsArr[2].setOnClickListener(makeClickListener(".","."));
		simpleButtonsArr[3].setOnClickListener(makeClickListener("1","1"));
		simpleButtonsArr[4].setOnClickListener(makeClickListener("2","2"));
		simpleButtonsArr[5].setOnClickListener(makeClickListener("3","3"));
		simpleButtonsArr[6].setOnClickListener(makeClickListener("4","4"));
		simpleButtonsArr[7].setOnClickListener(makeClickListener("5","5"));
		simpleButtonsArr[8].setOnClickListener(makeClickListener("6","6"));
		simpleButtonsArr[9].setOnClickListener(makeClickListener("7","7"));
		simpleButtonsArr[10].setOnClickListener(makeClickListener("8","8"));
		simpleButtonsArr[11].setOnClickListener(makeClickListener("9","9"));
		simpleButtonsArr[12].setOnClickListener(makeClickListener("*","*"));
		simpleButtonsArr[13].setOnClickListener(makeClickListener("÷","÷"));
		simpleButtonsArr[14].setOnClickListener(makeClickListener("+","+"));
		simpleButtonsArr[15].setOnClickListener(makeClickListener("-","-"));

		for(int count=0;count<simpleButtonsArr.length;count++){
			String image_name=SharedData.getInstance().selectedTheme+"_"+simpleButtonImagesNames[count];
			int resourceId=convertStringToId(image_name);
			simpleButtonsArr[count].setBackgroundResource(resourceId);

		}
	}
	private int convertStringToId(String image_name){
		return SharedData.getInstance().mContext.getResources().getIdentifier(image_name , "drawable", SharedData.getInstance().mContext.getPackageName());
	}
	private void handleAdvancedCalculatorClicks(View view){
		Button [] advButtonsArr=new Button[18];

		advButtonsArr[0]=(Button)view.findViewById(R.id.PiFunctionButton);
		advButtonsArr[1]=(Button)view.findViewById(R.id.PlusMinusFunctionButton);
		advButtonsArr[2]=(Button)view.findViewById(R.id.NaturalLogFunctionButton);
		advButtonsArr[3]=(Button)view.findViewById(R.id.OnePowerFunctionButton);
		advButtonsArr[4]=(Button)view.findViewById(R.id.TenPowerFunctionButton);
		advButtonsArr[5]=(Button)view.findViewById(R.id.ModFunctionButton);
		advButtonsArr[6]=(Button)view.findViewById(R.id.SinFunctionButton);
		advButtonsArr[7]=(Button)view.findViewById(R.id.CosFunctionButton);
		advButtonsArr[8]=(Button)view.findViewById(R.id.TangentFunctionButton);
		advButtonsArr[9]=(Button)view.findViewById(R.id.CarRotFunctionButton);
		advButtonsArr[10]=(Button)view.findViewById(R.id.LeftParenthesisButton);
		advButtonsArr[11]=(Button)view.findViewById(R.id.RightParenthesisButton);
		advButtonsArr[12]=(Button)view.findViewById(R.id.ExpFunctionButton);
		advButtonsArr[13]=(Button)view.findViewById(R.id.FactorialButton);
		advButtonsArr[14]=(Button)view.findViewById(R.id.EulerButton);
		advButtonsArr[15]=(Button)view.findViewById(R.id.ASinFunctionButton);
		advButtonsArr[16]=(Button)view.findViewById(R.id.ACosFunctionButton);
		advButtonsArr[17]=(Button)view.findViewById(R.id.ATangentFunctionButton);

		advButtonsArr[0].setOnClickListener(makeClickListener("PI","pi"));
		advButtonsArr[1].setOnClickListener(plusMinusClickListener);
		advButtonsArr[2].setOnClickListener(makeFnClickListener("Log","ln"));	
		advButtonsArr[3].setOnClickListener(oneByXClickListener);	
		advButtonsArr[4].setOnClickListener(TenPowerXClickListener);
		advButtonsArr[5].setOnClickListener(makeClickListener("%","%"));	
		advButtonsArr[6].setOnClickListener(makeFnClickListener("Sin","sin"));
		advButtonsArr[7].setOnClickListener(makeFnClickListener("Cos","cos"));	
		advButtonsArr[8].setOnClickListener(makeFnClickListener("Tan","tan"));
		advButtonsArr[9].setOnClickListener(makeClickListener("^","^"));
		advButtonsArr[10].setOnClickListener(makeClickListener("(","("));
		advButtonsArr[11].setOnClickListener(makeClickListener(")",")"));
		advButtonsArr[12].setOnClickListener(makeClickListener("E","E"));
		advButtonsArr[13].setOnClickListener(factorialButtonClickListener);
		advButtonsArr[14].setOnClickListener(makeClickListener("e","e"));	
		advButtonsArr[15].setOnClickListener(makeFnClickListener("Arcsin","arcsin"));
		advButtonsArr[16].setOnClickListener(makeFnClickListener("Arccos","arccos"));
		advButtonsArr[17].setOnClickListener(makeFnClickListener("Arctan","arctan"));

		for(int count=0;count<advButtonsArr.length;count++){
			String image_name=SharedData.getInstance().selectedTheme+"_"+advanceImagesNames[count];
			int resourceId=convertStringToId(image_name);
			advButtonsArr[count].setBackgroundResource(resourceId);

		}

	}
	private void handleMiscelleneousButtons(View view){
		Button [] miscButtonsArr=new Button[4];
		if(IS_FROM_CALCULATOR==CalConstants.HOME_ID){
			miscButtonsArr[0]=(Button)view.findViewById(R.id.percentButton);
			miscButtonsArr[0].setOnClickListener(percentBtnClickListener);
		}else{
			miscButtonsArr[0]=(Button)view.findViewById(R.id.xButtonButton);
			miscButtonsArr[0].setOnClickListener(makeClickListener("x","x"));	
		}
		miscButtonsArr[1]=(Button)view.findViewById(R.id.clearButton);
		miscButtonsArr[2]=(Button)view.findViewById(R.id.backspaceButton);
		miscButtonsArr[3]=(Button)view.findViewById(R.id.sqrtButton);

		miscButtonsArr[1].setOnClickListener(clrBtnClickListener);
		miscButtonsArr[2].setOnClickListener(bspcBtnClickListener);
		miscButtonsArr[3].setOnClickListener(makeFnClickListener("Sqrt","sqrt"));

		for(int count=0;count<miscButtonsArr.length;count++){
			String image_name=SharedData.getInstance().selectedTheme+"_"+miscelButtonImageNames[count];
			int resourceId=convertStringToId(image_name);
			if(!(IS_FROM_CALCULATOR==CalConstants.GRAPHIC_ID && count==0)){
				miscButtonsArr[count].setBackgroundResource(resourceId);
			}

		}
		if(IS_FROM_CALCULATOR==CalConstants.HOME_ID){
			resultDisplayArea=(TextView)view.findViewById(R.id.resultDisplayArea);
			resultDisplayArea.setOnLongClickListener(longPressResultCopyListener);
		}


	}
	private OnClickListener makeClickListener(final String token,final String viewString) {
		return new OnClickListener() {
			public void onClick(View v) {
				if(SharedData.getInstance().isVibrationEnabled){
					myVib.vibrate(CalConstants.VIBRATION_DURATION);
				}
				if(SharedData.getInstance().isSolvedPressed && IS_FROM_CALCULATOR==CalConstants.HOME_ID){
					addResultAsFirstOperand(token);
					resultDisplayArea.setText("");
					SharedData.getInstance().isSolvedPressed=false;
				}
				if(token.equals("00")){
					currentCalculator.addToken("0",1, getIndex());
					updateView(1,"0");
					currentCalculator.addToken("0",1, getIndex());
					updateView(1,"0");
				}else{
					currentCalculator.addToken(token, viewString.length(), getIndex());
					updateView(viewString.length(),viewString);
				}
			}
		};
	}

	private OnClickListener makeFnClickListener(final String token,final String viewString) {
		return new OnClickListener() {
			public void onClick(View v) {
				if(SharedData.getInstance().isVibrationEnabled){
					myVib.vibrate(CalConstants.VIBRATION_DURATION);
				}
				addCalcFn (token, viewString);
			}
		};
	}
	private void addResultAsFirstOperand(String token){
		String operator="-+*÷=^%";
		String sqrtSymbol="\u221A";

		if(operator.contains(token) || sqrtSymbol.equalsIgnoreCase(token) ){
			double result=Double.valueOf(currentCalculator.result);
			setIndex(0);
			currentCalculator.tokens.add(0,new ComplexNumber(result, 0));
			currentCalculator.tokenLens.add(0,currentCalculator.result.length());
			updateView(currentCalculator.result.length(), currentCalculator.result);
		}
	}
	private OnClickListener percentBtnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			if(SharedData.getInstance().isSolvedPressed && IS_FROM_CALCULATOR==CalConstants.HOME_ID){
				addResultAsFirstOperand("%");
				resultDisplayArea.setText("");
				SharedData.getInstance().isSolvedPressed=false;
			}

			int operatorIndex=-1;
			int startPercentNuIndex=0;

			for(int count=(currentCalculator.tokens.size()-1);count>0;count--){
				if(Calculator.isPrimitive(currentCalculator.tokens.get(count).toString())){
					if(operatorIndex==-1){
						operatorIndex=count;
					}else{
						startPercentNuIndex=count+1;
						break;
					}

				}
			}

			if(operatorIndex>0){
				currentCalculator.tokens.add(operatorIndex+1, new Primitive ("("));
				currentCalculator.tokenLens.add(operatorIndex+1, 1);

				currentCalculator.tokens.add(new Primitive ("*"));
				currentCalculator.tokenLens.add(1);
				for(int i=startPercentNuIndex;i<operatorIndex;i++){
					CalcItem copy=currentCalculator.tokens.get(i).copy();	
					currentCalculator.tokens.add(copy);
					currentCalculator.tokenLens.add(currentCalculator.tokenLens.get(i));
				}
				currentCalculator.tokens.add(new Primitive (")"));
				currentCalculator.tokenLens.add(1);
			}
			currentCalculator.tokens.add(new Primitive ("/"));
			currentCalculator.tokenLens.add(1);
			currentCalculator.tokens.add(new ComplexNumber (100.0,0));
			currentCalculator.tokenLens.add(3);
			currentCalculator.viewStr = currentCalculator.viewStr+"%";
			String disPlayString=currentCalculator.viewStr;
			currentCalculator.execute();
			setIndex (currentCalculator.viewStr.length());
			disPlayString=replaceSymbols(disPlayString);
			inputExpressionArea.setSelection(disPlayString.length());

			if(resultDisplayArea!=null){
				resultDisplayArea.setText(currentCalculator.result);
			}
			SharedData.getInstance().isSolvedPressed=true;


		}
	};
	private OnClickListener oneByXClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			int tokenSize=currentCalculator.tokens.size();
			int startIndex=tokenSize-1;
			int length=0;

			if(SharedData.getInstance().isSolvedPressed && IS_FROM_CALCULATOR==CalConstants.HOME_ID){	
				resultDisplayArea.setText("");
				SharedData.getInstance().isSolvedPressed=false;
			}
			while(startIndex>=0 && (currentCalculator.tokens.get(startIndex).isNumStr() || currentCalculator.tokens.get(startIndex).isConstant())){
				length+=currentCalculator.tokenLens.get(startIndex);
				startIndex--;

			}
			startIndex=startIndex+1;
			currentCalculator.tokens.add(startIndex, new ComplexNumberStr("1"));
			currentCalculator.tokenLens.add(startIndex, 1);	
			currentCalculator.tokens.add(startIndex+1,new Primitive ("/"));
			currentCalculator.tokenLens.add(startIndex+1, 1);

			StringBuffer st = new StringBuffer(currentCalculator.viewStr);
			st.insert((currentCalculator.viewStr.length()-length),"1/");
			currentCalculator.viewStr =st.toString();
			setIndex (currentCalculator.viewStr.length());
			String displayString=replaceSymbols(currentCalculator.viewStr);
			//inputExpressionArea.setText(displayString);
			inputExpressionArea.setSelection(displayString.length());

		}
	};
	OnClickListener plusMinusClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			if(SharedData.getInstance().isSolvedPressed && IS_FROM_CALCULATOR==CalConstants.HOME_ID){
				addResultAsFirstOperand("+");
				resultDisplayArea.setText("");
				SharedData.getInstance().isSolvedPressed=false;
			}
			int tokenSize=currentCalculator.tokens.size();
			int startIndex=tokenSize-1;
			int length=0;
			String insertString="+";

			while(startIndex>=0 && (currentCalculator.tokens.get(startIndex).isNumStr() || currentCalculator.tokens.get(startIndex) instanceof ComplexNumber || currentCalculator.tokens.get(startIndex).isConstant())){
				length+=currentCalculator.tokenLens.get(startIndex);
				startIndex--;

			}
			if(tokenSize!=0 && startIndex<0){
				startIndex=0;
			}
			if(startIndex>=0){
				StringBuffer st = new StringBuffer(currentCalculator.viewStr);
				if(currentCalculator.tokens.get(startIndex).isPrimitive()){
					Primitive primitive=(Primitive)currentCalculator.tokens.get(startIndex);

					if(primitive.getVal()=='+'){
						insertString="-";
						length++;
						currentCalculator.tokens.remove(startIndex);
						st.deleteCharAt((currentCalculator.viewStr.length()-length));
						currentCalculator.tokenLens.remove(startIndex);

					}else if(primitive.getVal()=='-'){
						insertString="+";
						length++;
						currentCalculator.tokens.remove(startIndex);
						st.deleteCharAt((currentCalculator.viewStr.length()-length));
						currentCalculator.tokenLens.remove(startIndex);

					}else{
						startIndex++;
						//length--;
					}
				}

				currentCalculator.tokens.add(startIndex, new Primitive(insertString));
				currentCalculator.tokenLens.add(startIndex, 1);	
				st.insert((currentCalculator.viewStr.length()-length), insertString);
				currentCalculator.viewStr =st.toString();
				setIndex (currentCalculator.viewStr.length());
				String displayString=replaceSymbols(currentCalculator.viewStr);
				//inputExpressionArea.setText(displayString);
				inputExpressionArea.setSelection(displayString.length());

			}
		}
	};
	OnClickListener TenPowerXClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			int tokenSize=currentCalculator.tokens.size();
			int startIndex=tokenSize-1;
			int length=0;
			if(SharedData.getInstance().isSolvedPressed && IS_FROM_CALCULATOR==CalConstants.HOME_ID){	
				resultDisplayArea.setText("");
				SharedData.getInstance().isSolvedPressed=false;
			}
			while(startIndex>=0 && (currentCalculator.tokens.get(startIndex).isNumStr() || currentCalculator.tokens.get(startIndex).isConstant())){
				length+=currentCalculator.tokenLens.get(startIndex);
				startIndex--;
			}
			startIndex=startIndex+1;
			currentCalculator.tokens.add(startIndex, new ComplexNumberStr("1"));
			currentCalculator.tokenLens.add(startIndex, 1);	


			currentCalculator.tokens.add(startIndex+1, new ComplexNumberStr("0"));
			currentCalculator.tokenLens.add(startIndex+1, 1);	

			currentCalculator.tokens.add(startIndex+2,new Primitive ("^"));
			currentCalculator.tokenLens.add(startIndex+2, 1);

			currentCalculator.tokens.add(startIndex+3,new Primitive ("("));
			currentCalculator.tokenLens.add(startIndex+3, 1);

			StringBuffer st = new StringBuffer(currentCalculator.viewStr);
			st.insert((currentCalculator.viewStr.length()-length),"10^(");
			currentCalculator.viewStr =st.toString();
			setIndex (currentCalculator.viewStr.length());
			String displayString=replaceSymbols(currentCalculator.viewStr);
			//inputExpressionArea.setText(displayString);
			inputExpressionArea.setSelection(displayString.length());

		}
	};
	OnClickListener factorialButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			int tokenSize=currentCalculator.tokens.size();
			int startIndex=tokenSize-1;
			int length=0;
			String numString="";
			ComplexNumberStr complexNumberObj=null;
			int factorialNumber=0;
			double factorialResult=1.0;

			if(tokenSize>0){
				while(startIndex>=0 && (currentCalculator.tokens.get(startIndex).isNumStr())){
					length+=currentCalculator.tokenLens.get(startIndex);
					complexNumberObj=(ComplexNumberStr)currentCalculator.tokens.get(startIndex);
					numString=complexNumberObj.toString()+numString;
					currentCalculator.tokens.remove(startIndex);
					currentCalculator.tokenLens.remove(startIndex);
					startIndex--;
				}
				if(!numString.equals("")){

					if(numString.contains(".")){
						Toast.makeText(SharedData.getInstance().mContext, "Only Integers have Factorial", Toast.LENGTH_SHORT);
					}
					else
					{
						factorialNumber=Integer.valueOf(numString);
						while(factorialNumber>0){
							factorialResult*=factorialNumber;
							factorialNumber--;
						}

						startIndex=startIndex+1;
						double result=limitDigitsAfterExponent(factorialResult);
						currentCalculator.tokens.add(startIndex, new ComplexNumber(result,0));
						currentCalculator.tokenLens.add(startIndex, String.valueOf(result).length());
						currentCalculator.viewStr=currentCalculator.viewStr.substring(0, (currentCalculator.viewStr.length()-length))+result;
						setIndex (currentCalculator.viewStr.length());
						String displayString=replaceSymbols(currentCalculator.viewStr);
						//inputExpressionArea.setText(displayString);
						inputExpressionArea.setSelection(displayString.length());
					}
				}
			}
		}
	};
	private String replaceSymbols(String displayString){
		displayString=displayString.replace("sqrt", "\u221A");
		displayString=displayString.replace("\u221A(", "\u221A");
		displayString=displayString.replace("arcsin", "asin");
		displayString=displayString.replace("arccos", "acos");
		displayString=displayString.replace("arctan", "atan");
		inputExpressionArea.setText(displayString);
		return displayString;
	}

	private OnClickListener clrBtnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			currentCalculator.tokens.clear();
			currentCalculator.tokenLens.clear();
			currentCalculator.viewStr = "";
			setIndex (0);
			inputExpressionArea.setText(currentCalculator.viewStr);
			inputExpressionArea.setSelection(getIndex());
			if(resultDisplayArea!=null){
				resultDisplayArea.setText("");
			}

		}
	};
	private OnClickListener bspcBtnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(SharedData.getInstance().isVibrationEnabled){
				myVib.vibrate(CalConstants.VIBRATION_DURATION);
			}
			if (getIndex() > 0 && !SharedData.getInstance().isSolvedPressed) {
				int bspcNum = currentCalculator.bspcHelper(getIndex(), true);
				currentCalculator.viewStr = currentCalculator.viewStr.substring(0,getIndex()-bspcNum) +
						currentCalculator.viewStr.substring(getIndex(),currentCalculator.viewStr.length());
				int newInd = getIndex()-bspcNum;
				inputExpressionArea.setText(currentCalculator.viewStr);
				setIndex(newInd);
				inputExpressionArea.setSelection(getIndex());
			}else if(SharedData.getInstance().isSolvedPressed){
				inputExpressionArea.setText("");
				if(resultDisplayArea!=null){
					resultDisplayArea.setText("");
				}
			}
		}
	};
	private void addCalcFn (String token, String viewString) {
		if(SharedData.getInstance().isSolvedPressed && resultDisplayArea!=null){
			resultDisplayArea.setText("");
		}
		currentCalculator.addToken(token, viewString.length(), getIndex());
		updateView (viewString.length (), viewString);
		currentCalculator.addToken("(", 1, getIndex());
		updateView(1,"(");
	}

	private int getIndex () {
		return currentCalculator.getViewIndex ();
	}
	private void updateView(int ind, String ins) {
		StringBuffer st = new StringBuffer(currentCalculator.viewStr);

		st.insert(getIndex (),ins);
		currentCalculator.viewStr = st.toString();
		int newInd = getIndex() + ind;
		Log.e("calc", "updateView: "+getIndex()+" "+currentCalculator.viewStr.length());
		if(newInd > currentCalculator.viewStr.length()) newInd = 0;
		if(newInd < 0) newInd = currentCalculator.viewStr.length();
		setIndex(newInd);
		String displayString=currentCalculator.viewStr;
		displayString=replaceSymbols(displayString);

		inputExpressionArea.setSelection(displayString.length());
//		if(IS_FROM_CALCULATOR==CalConstants.HOME_ID){
//			SharedData.getInstance().homeCalInput=displayString;
//		}
	}
	private void setIndex (int ind) {
		currentCalculator.setViewIndex (ind);
	}
	private  Double limitDigitsAfterExponent(Double unroundedNumber){
		String unroundString=unroundedNumber.toString();
		int exponentialIndex=0;
		if(unroundString.contains("E") && unroundString.length()>8){
			exponentialIndex=unroundString.indexOf("E");
			if(exponentialIndex>=8){
				unroundString=unroundString.substring(0,8)+unroundString.substring(exponentialIndex);
			}else{
				unroundString=unroundString.substring(0,8);
			}
			return Double.parseDouble(unroundString);
		}
		return unroundedNumber;


	}

	public void solveMainCalculatorExpression(ArrayList<String> historyBackUp){

		if(SharedData.getInstance().isVibrationEnabled){
			myVib.vibrate(CalConstants.VIBRATION_DURATION);
		}

		if(currentCalculator.viewStr != null && currentCalculator.viewStr.length() > 0) {
			String displayString=currentCalculator.viewStr+" =";
			if(currentCalculator.execute()) {	
				setIndex (0);
				SharedData.getInstance().isSolvedPressed=true;
				displayString=replaceSymbols(displayString);	
				inputExpressionArea.setSelection(displayString.length());

				currentCalculator.result=SharedData.getInstance().discardZerosAfterDecimal(currentCalculator.result);
				if(resultDisplayArea!=null){
					resultDisplayArea.setText(decimalFormat.format(Double.valueOf(currentCalculator.result)));
				}
				if(historyBackUp.size()>=50){
					historyBackUp.remove(49);
				}

				historyBackUp.add(0,displayString+" "+currentCalculator.result) ;
				SharedData.getInstance().homeCalResultInput=currentCalculator.result;
				//CalculationsPreferences.getInstance().saveHistoryofMainCalculator(historyBackUp);
			} 
			else {
				Toast.makeText(SharedData.getInstance().mContext, "Error: Could not calculate", Toast.LENGTH_SHORT).show();
			}
		} 
	}
	OnLongClickListener longPressResultCopyListener=new OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			copyResultToClipBoard();
			return true;
		}
	};
	@SuppressWarnings("deprecation")
	public void copyResultToClipBoard(){

		ClipboardManager clipboard = (ClipboardManager)SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.CLIPBOARD_SERVICE);
		clipboard.setText(resultDisplayArea.getText().toString());
		Toast.makeText(SharedData.getInstance().mContext, "Result copied to clipboard", Toast.LENGTH_SHORT).show();

	}
	public void copyEquation(){
		ClipboardManager clipboard = (ClipboardManager) SharedData.getInstance().mContext.getSystemService(SharedData.getInstance().mContext.CLIPBOARD_SERVICE);
		clipboard.setText(inputExpressionArea.getText().toString()+resultDisplayArea.getText().toString());
		Toast.makeText(SharedData.getInstance().mContext, "Equation copied to clipboard", Toast.LENGTH_SHORT).show();
	}

}
