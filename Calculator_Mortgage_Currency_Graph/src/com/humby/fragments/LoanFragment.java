package com.humby.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.calculations.brain.NumKeyPadHandler;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class LoanFragment extends Fragment {
	private View loanCalView=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		loanCalView=inflater.inflate(R.layout.loan_calculator_layout, container, false);
		ScalingUtility.getInstance().scaleView(loanCalView);
		applyFontToViews();
		new NumKeyPadHandler(CalConstants.LOAN_CAL_ID, loanCalView);
		return loanCalView;

	}
	private void applyFontToViews(){
	      EditText 	loanAmountEditText=(EditText)loanCalView.findViewById(R.id.loanAmountInputEditArea);
	      EditText  ratePercEditText=(EditText)loanCalView.findViewById(R.id.ratePerInputEditArea);
	      EditText  totalMonthsEditText=(EditText)loanCalView.findViewById(R.id.monthsInputEditArea);
	      TextView  loanAmountLabel=(TextView)loanCalView.findViewById(R.id.loanAmountInputLabel);
	      TextView  ratePerLabel=(TextView)loanCalView.findViewById(R.id.rateInputLabel);
	      TextView  totalMonthsLabel=(TextView)loanCalView.findViewById(R.id.monthsInputLabel);
	      TextView  monthlyStaticTextView=(TextView)loanCalView.findViewById(R.id.monthlyStaticText);
	      TextView  monthPayTextView=(TextView)loanCalView.findViewById(R.id.monthPaymentStaticText);
	      TextView  monthPayAmountTextView=(TextView)loanCalView.findViewById(R.id.monthlyPaymentAmount);
	      TextView  numberOfPayTextView=(TextView)loanCalView.findViewById(R.id.NoOfPaymentsStaticText);
	      TextView  numOfPayAmountTextView=(TextView)loanCalView.findViewById(R.id.NoOfPaymentsAmount);
	      TextView  totalInterestTextView=(TextView)loanCalView.findViewById(R.id.TotalInterestStaticText);
	      TextView  totalIntAmountTextView=(TextView)loanCalView.findViewById(R.id.totalInterestAmount);
	      TextView  totalPayStatTextView=(TextView)loanCalView.findViewById(R.id.TotalPaymentsStaticText);
	      TextView  totalPayAmountTextView=(TextView)loanCalView.findViewById(R.id.totalPaymentsAmount);
	      TextView  biweeklyStaticText=(TextView)loanCalView.findViewById(R.id.BiWeeklyStaticText);
	      TextView  biWeekPayTextView=(TextView)loanCalView.findViewById(R.id.BiWeekPaymentStaticText);
	      TextView  biWeekPayAmountTextView=(TextView)loanCalView.findViewById(R.id.BiWeekPaymentAmount);
	      TextView  biWeekNoPayStatTextView=(TextView)loanCalView.findViewById(R.id.BiWeekNoPaymentsStaticText);
	      TextView  biWeekNoPayAmountTextView=(TextView)loanCalView.findViewById(R.id.BiWeekNoPaymentsAmount);
	      TextView  biWeekInterStaticTextView=(TextView)loanCalView.findViewById(R.id.BiTotalInterStaticText);
	      TextView  biWeekInterAmountTextView=(TextView)loanCalView.findViewById(R.id.BiTotalInterestAmount);
	      TextView  biWeekTotalPayStatTextView=(TextView)loanCalView.findViewById(R.id.BiTotalPayStaticText);
	      TextView  biWeekTotalPayAmountTextView=(TextView)loanCalView.findViewById(R.id.BiTotalPayAmount);
	     
	      SharedData.getInstance().applyFontToTextView(loanAmountEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(ratePercEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(totalMonthsEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(loanAmountLabel, CalConstants.ROBOT_MEDIUM);
	      SharedData.getInstance().applyFontToTextView(ratePerLabel, CalConstants.ROBOT_MEDIUM);
	      SharedData.getInstance().applyFontToTextView(totalMonthsLabel, CalConstants.ROBOT_MEDIUM);
	      
	     
	      
	      SharedData.getInstance().applyFontToTextView(monthPayTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(numberOfPayTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(totalInterestTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(totalPayStatTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(biWeekPayTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(biWeekNoPayStatTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(biWeekInterStaticTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(biWeekTotalPayStatTextView, CalConstants.ROBOT_BOLD);
	      
	      SharedData.getInstance().applyFontToTextView(monthPayAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(numOfPayAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(totalIntAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(totalPayAmountTextView, CalConstants.ROBOT_LIGHT);   
	      SharedData.getInstance().applyFontToTextView(biWeekPayAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(biWeekNoPayAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(biWeekInterAmountTextView, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(biWeekTotalPayAmountTextView, CalConstants.ROBOT_LIGHT);
	     
	      
	      SharedData.getInstance().applyFontToTextView(biweeklyStaticText, CalConstants.ROBOT_CONDENSED);
	      SharedData.getInstance().applyFontToTextView(monthlyStaticTextView, CalConstants.ROBOT_CONDENSED);
	      
	    
	}

}
