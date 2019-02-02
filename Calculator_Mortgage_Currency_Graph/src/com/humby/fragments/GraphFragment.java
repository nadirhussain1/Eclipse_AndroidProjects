package com.humby.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculations.brain.CalculationsState;
import com.calculations.brain.HomeKeyHandler;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class GraphFragment extends Fragment{
	private View  graphicPageMainView=null;
	private CalculationsState appState=null;
	private Button goDrawGraphicButton=null;
	private RelativeLayout goDrawGraphClickedArea=null;
	private RelativeLayout simpleButtonsLayout=null;
	private GraphView graphView=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		graphicPageMainView=inflater.inflate(R.layout.graph_layout, container, false);
		ScalingUtility.getInstance().scaleView(graphicPageMainView);
		applyFontsToText();
		initGraphObjects();
		return graphicPageMainView;

	}
	private void initGraphObjects(){
		appState = new CalculationsState();
		SharedData.getInstance().appState=appState;
		new HomeKeyHandler(graphicPageMainView,appState.getGraphCalc(),CalConstants.GRAPH_CAL_ID);
	}
	private void applyFontsToText(){
		EditText inputEditor=(EditText)graphicPageMainView.findViewById(R.id.DisplayInputExpArea);
		TextView YEqauationTextView=(TextView)graphicPageMainView.findViewById(R.id.YEquationTextView);
		SharedData.getInstance().applyFontToTextView(inputEditor,CalConstants.ROBOT_LIGHT);
		SharedData.getInstance().applyFontToTextView(YEqauationTextView, CalConstants.ROBOT_MEDIUM);

		goDrawGraphicButton=(Button)graphicPageMainView.findViewById(R.id.GoButton);
		goDrawGraphClickedArea=(RelativeLayout)graphicPageMainView.findViewById(R.id.GoDrawButtonLayout);
		goDrawGraphClickedArea.setOnClickListener(graphDrawButtonClickListener);
		goDrawGraphicButton.setOnClickListener(graphDrawButtonClickListener);
		
		simpleButtonsLayout=(RelativeLayout)graphicPageMainView.findViewById(R.id.simpleButtonsLayout);
		inputEditor.setOnTouchListener(inputAreaTouchListener);
	}
	
	OnTouchListener inputAreaTouchListener=new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			simpleButtonsLayout.setVisibility(View.VISIBLE);
			goDrawGraphicButton.setText("");
			goDrawGraphicButton.setBackgroundResource(R.drawable.graphing_button_send);
            SharedData.getInstance().isGraphiCalculatorVisible=false;
            appState.resetCalcs();
            
            return true;
		}
	};
	OnClickListener graphDrawButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(!SharedData.getInstance().isGraphiCalculatorVisible){
				SharedData.getInstance().isGraphiCalculatorVisible=true;
				simpleButtonsLayout.setVisibility(View.GONE);
				goDrawGraphicButton.setText("X");
				goDrawGraphicButton.setBackgroundDrawable(null);
				goDrawGraphicButton.setBackgroundColor(Color.parseColor("#00000000"));

				if(graphView==null){
					LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View newView=inflater.inflate(R.layout.graph_view, null, false);
					((RelativeLayout)(graphicPageMainView.findViewById(R.id.graphViewWrapperLayout))).addView(newView);
					graphView=(GraphView)graphicPageMainView.findViewById(R.id.graph);
					graphView.graphMode = graphView.GRAPH;
					appState.setGraphView(graphView);
				}else{
					appState.setGraphView(graphView);
					graphView.getCalculationsState().initFns ();
					graphView.getCalculationsState().plotFns ();
				}
			}else{
				SharedData.getInstance().isGraphiCalculatorVisible=false;
				simpleButtonsLayout.setVisibility(View.VISIBLE);
				goDrawGraphicButton.setText("");
				goDrawGraphicButton.setBackgroundResource(R.drawable.graphing_button_send);  
	            appState.resetCalcs();
			}

		}
	};

}
