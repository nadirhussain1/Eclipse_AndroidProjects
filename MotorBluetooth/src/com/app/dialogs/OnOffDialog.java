package com.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.app.motorbluetooth.MainActivity;
import com.app.motorbluetooth.R;
import com.app.utilities.ScalingUtility;
import com.app.utilities.SharedData;

public class OnOffDialog {
	Dialog onOffDialog=null;
	Context mContext=null;

	public OnOffDialog(Context context){
		mContext=context;

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.motor_onoff_layout, null, false);
		ScalingUtility.getInstance((Activity)context).scaleView(view);

		CompoundButton motorACheck=(CompoundButton)view.findViewById(R.id.MotorACheckBox);
		CompoundButton motorBCheck=(CompoundButton)view.findViewById(R.id.MotorBCheckBox);
		
		if(SharedData.getInstance().isMotorAOn){
			motorACheck.setChecked(true);
		}else{
			motorACheck.setChecked(false);
		}
		if(SharedData.getInstance().isMotorBOn){
			motorBCheck.setChecked(true);
		}else{
			motorBCheck.setChecked(false);
		}
		
		motorACheck.setOnCheckedChangeListener(motorACheckChangeListener);
		motorBCheck.setOnCheckedChangeListener(motorBCheckChangeListener);

		onOffDialog=new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		onOffDialog.setTitle("On/Off Settings");
		onOffDialog.setContentView(view);
		onOffDialog.setOnDismissListener(dismissListener);
	}
	public void showDialog(){
		onOffDialog.show();
	}
	public void hideDialog(){
		onOffDialog.cancel();
	}
	OnDismissListener dismissListener=new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			((MainActivity)mContext).handlePowerOnOffMotors();

		}
	};
	OnCheckedChangeListener motorACheckChangeListener=new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean arg1) {
			if(button.isChecked()){
				SharedData.getInstance().isMotorAOn=true;
			}else{
				SharedData.getInstance().isMotorAOn=false;  
			}

		}
	};
	OnCheckedChangeListener motorBCheckChangeListener=new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean arg1) {
			if(button.isChecked()){
				SharedData.getInstance().isMotorBOn=true;
			}else{
				SharedData.getInstance().isMotorBOn=false;  
			}

		}
	};
}
