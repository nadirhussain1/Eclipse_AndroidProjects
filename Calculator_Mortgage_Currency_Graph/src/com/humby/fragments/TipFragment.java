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

public class TipFragment extends Fragment {
	private View tipCalView=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		tipCalView=inflater.inflate(R.layout.tipping_claculator, container, false);
		ScalingUtility.getInstance().scaleView(tipCalView);
		applyFontToViews();
		new NumKeyPadHandler(CalConstants.TIPPING_CONV_ID, tipCalView);
		return tipCalView;

	}
	private void applyFontToViews(){
	      EditText 	totalBillEditText=(EditText)tipCalView.findViewById(R.id.TotalBillInputEditArea);
	      EditText  peopleEditText=(EditText)tipCalView.findViewById(R.id.PeopleBillInputEditArea);
	      EditText  tipInputEditText=(EditText)tipCalView.findViewById(R.id.TipInputEditArea);
	      TextView  totalBillLabel=(TextView)tipCalView.findViewById(R.id.TotalBillInputLabel);
	      TextView  peopleBillLabel=(TextView)tipCalView.findViewById(R.id.PeopleBillInputLabel);
	      TextView  tipBillLabel=(TextView)tipCalView.findViewById(R.id.TipInputLabel);
	      TextView  perPersonText=(TextView)tipCalView.findViewById(R.id.perPersonStaticText);
	      TextView  tipPersonStatic=(TextView)tipCalView.findViewById(R.id.TipPerPersonStaticText);
	      TextView  tipPerPersonAmount=(TextView)tipCalView.findViewById(R.id.TipPerPersonAmount);
	      TextView  taxPerPersonTextView=(TextView)tipCalView.findViewById(R.id.TaxPerPersonStaticText);
	      TextView  taxPersonAmount=(TextView)tipCalView.findViewById(R.id.TaxPerPersonAmount);
	      TextView  totalPersonStatic=(TextView)tipCalView.findViewById(R.id.TotalPerPersonStaticText);
	      TextView  totalPersonAmount=(TextView)tipCalView.findViewById(R.id.TotalPerPersonAmount);
	      TextView  entireGroupText=(TextView)tipCalView.findViewById(R.id.entireGroupStaticText);
	      TextView  tipGroupStaticText=(TextView)tipCalView.findViewById(R.id.TipGroupStaticText);
	      TextView  tipGroupAmount=(TextView)tipCalView.findViewById(R.id.TipGroupAmount);
	      TextView  taxGroupStatic=(TextView)tipCalView.findViewById(R.id.TaxGroupStaticText);
	      TextView  taxGroupAmount=(TextView)tipCalView.findViewById(R.id.TaxGroupAmount);
	      TextView  totalStaticGroup=(TextView)tipCalView.findViewById(R.id.TotalGroupStaticText);
	      TextView  totalGroupAmount=(TextView)tipCalView.findViewById(R.id.TotalGroupAmount);
	      
	      SharedData.getInstance().applyFontToTextView(totalBillEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(peopleEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(tipInputEditText, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(totalBillLabel, CalConstants.ROBOT_MEDIUM);
	      SharedData.getInstance().applyFontToTextView(peopleBillLabel, CalConstants.ROBOT_MEDIUM);
	      SharedData.getInstance().applyFontToTextView(tipBillLabel, CalConstants.ROBOT_MEDIUM);
	      
	      SharedData.getInstance().applyFontToTextView(perPersonText, CalConstants.ROBOT_CONDENSED);
	      SharedData.getInstance().applyFontToTextView(entireGroupText, CalConstants.ROBOT_CONDENSED);
	      
	      SharedData.getInstance().applyFontToTextView(tipPersonStatic, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(taxPerPersonTextView, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(totalPersonStatic, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(totalStaticGroup, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(taxGroupStatic, CalConstants.ROBOT_BOLD);
	      SharedData.getInstance().applyFontToTextView(tipGroupStaticText, CalConstants.ROBOT_BOLD);
	      
	      SharedData.getInstance().applyFontToTextView(tipPerPersonAmount, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(taxPersonAmount, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(tipGroupAmount, CalConstants.ROBOT_LIGHT);
	      SharedData.getInstance().applyFontToTextView(taxGroupAmount, CalConstants.ROBOT_LIGHT);
	      
	      SharedData.getInstance().applyFontToTextView(totalPersonAmount, CalConstants.ROBOT_REGULAR);
	      SharedData.getInstance().applyFontToTextView(totalGroupAmount, CalConstants.ROBOT_REGULAR);
	}
}
