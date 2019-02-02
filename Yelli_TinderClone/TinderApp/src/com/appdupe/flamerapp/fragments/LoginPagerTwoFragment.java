package com.appdupe.flamerapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.utility.ScalingUtility;

public class LoginPagerTwoFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View pagerOneView=inflater.inflate(R.layout.login_pager_two,container, false);
		ScalingUtility.getInstance(getActivity()).scaleView(pagerOneView);
		
		return pagerOneView;
	}
}
