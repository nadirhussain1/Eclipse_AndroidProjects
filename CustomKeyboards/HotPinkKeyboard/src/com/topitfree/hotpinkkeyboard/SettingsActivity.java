package com.topitfree.hotpinkkeyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.topitfree.hotpinkkeyboard.R;

public class SettingsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view  = inflater.inflate(R.layout.settings, null, false);
		ScalingUtility.getInstance(this).scaleView(view);
		new SettingsManager(view, this);
		setContentView(view);

	}

}
