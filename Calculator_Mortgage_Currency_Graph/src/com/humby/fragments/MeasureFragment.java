package com.humby.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.calculations.brain.NumKeyPadHandler;
import com.calculations.preferences.CalculationsPreferences;
import com.humby.adapters.MeasureTypesAdapter;
import com.humby.calculations.MainActivity;
import com.humby.calculations.R;
import com.humby.model.CalConstants;
import com.humby.model.SharedData;
import com.humby.utilities.ScalingUtility;

public class MeasureFragment extends Fragment {
	private View measureCalView=null;
	NumKeyPadHandler numKeyPadHandler=null;
	private  String  measureTypesList[];
	private ListView actionMeasureTypesListView=null;
	private View fadeBackground=null;
	private Animation animShow, animHide,drawerFadeInAnim,drawerFadeOutAnim;
	private boolean isDropDownVisible=false;
	private boolean isClicked=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		measureCalView=inflater.inflate(R.layout.curren_measu_page, container, false);
		ScalingUtility.getInstance().scaleView(measureCalView);
		initMeasurementViews();

		return measureCalView;

	}
	private void initMeasurementViews(){
		int prevSelectedMeasurement=CalculationsPreferences.getInstance().getPrevSelectedMeasurementCat();
		SharedData.getInstance().prevSelectedMeasCatIndex=prevSelectedMeasurement;

		EditText inputAreaText=(EditText)measureCalView.findViewById(R.id.unitInputEditArea);
		inputAreaText.setText(""+SharedData.getInstance().discardZerosAfterDecimal(String.valueOf(SharedData.getInstance().measurementInput)));
		inputAreaText.setSelection(inputAreaText.length());

		measureTypesList=SharedData.getInstance().mContext.getResources().getStringArray(R.array.generaltypes_array);
		numKeyPadHandler=new NumKeyPadHandler(CalConstants.MEASURE_CONV_ID, measureCalView);

		TextView unitType=(TextView)measureCalView.findViewById(R.id.unitTypeTextArea);
		SharedData.getInstance().applyFontToTextView(inputAreaText, CalConstants.ROBOT_MEDIUM);
		SharedData.getInstance().applyFontToTextView(unitType, CalConstants.ROBOT_MEDIUM);

		fadeBackground=measureCalView.findViewById(R.id.slideMenufadeBackground);
		actionMeasureTypesListView=(ListView)measureCalView.findViewById(R.id.generalMenuList);
		actionMeasureTypesListView.setOnItemClickListener(listItemClickListener);

		animShow = AnimationUtils.loadAnimation(SharedData.getInstance().mContext, R.anim.popup_show);
		animHide = AnimationUtils.loadAnimation(SharedData.getInstance().mContext, R.anim.popup_hide);
		drawerFadeInAnim=AnimationUtils.loadAnimation(SharedData.getInstance().mContext,R.anim.slide_drawer_fade_in);
		drawerFadeOutAnim=AnimationUtils.loadAnimation(SharedData.getInstance().mContext, R.anim.slide_drawer_fade_out);
		
		animHide.setAnimationListener(slideAnimationListner);
		animShow.setAnimationListener(slideAnimationListner);
	}

	public void displayMeasurementDropDown(View anchor){
		if(!isClicked ){
			isClicked=true;
			if(!isDropDownVisible){
				MeasureTypesAdapter menuListAdapter=new MeasureTypesAdapter(measureTypesList);	
				actionMeasureTypesListView.setAdapter(menuListAdapter);
				actionMeasureTypesListView.setVisibility(View.VISIBLE);
				actionMeasureTypesListView.startAnimation(animShow );
				fadeBackground.setVisibility(View.VISIBLE);
				fadeBackground.startAnimation(drawerFadeInAnim);			
			}
			else{
				hideSlideListView();
			}
		}
	}
	private void hideSlideListView(){
		actionMeasureTypesListView.startAnimation(animHide);
		fadeBackground.startAnimation(drawerFadeOutAnim);
	}
	OnItemClickListener listItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> view, View arg1, int position,long arg3) {
			hideSlideListView();	
			MainActivity.measureItemView.setImageResource(SharedData.getInstance().abMeasureThumbnails[position]);

			numKeyPadHandler.getMeasureResultAdapter().updateCurrentSelectedListIndex(position);
			SharedData.getInstance().prevSelectedMeasCatIndex=position;
			SharedData.getInstance().prevSeleMeasurementIndex=0;

		}
	};
	AnimationListener slideAnimationListner=new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if(!isDropDownVisible){
				isDropDownVisible=true;
			}else{
				actionMeasureTypesListView.setVisibility(View.GONE);
				fadeBackground.setVisibility(View.GONE);
				isDropDownVisible=false;
			}
			isClicked=false;

		}
	};

}
