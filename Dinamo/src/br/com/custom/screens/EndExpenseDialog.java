package br.com.custom.screens;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import br.com.dinamo.AddExpenseActivity;
import br.com.dinamo.R;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class EndExpenseDialog {
	private  Dialog   endDialog=null;
	private  Activity callerActivity=null;

	public EndExpenseDialog(Activity activity){
		callerActivity=activity;

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.expense_remove_dialog, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);
		
		endDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		endDialog.setContentView(dialogView);

		Button removeButton=(Button)dialogView.findViewById(R.id.removeCalButton);
		Button deleteButton=(Button)dialogView.findViewById(R.id.deleteHistoryButton);

		removeButton.setOnClickListener(removeCalListener);
		deleteButton.setOnClickListener(deleteHistoryListener);
	}
	public void showDialog(){
		endDialog.show();
	}
	public void cancelDialog(){
		endDialog.cancel();
	}
	OnClickListener removeCalListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			DatePickerDialog datePickDialog=new DatePickerDialog(callerActivity,datePickerListener, year, month, day);
			datePickDialog.show();
		}
	};
	OnDateSetListener datePickerListener=new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			Calendar cal = Calendar.getInstance();
			int hour=cal.get(Calendar.HOUR);
			int mint=cal.get(Calendar.MINUTE);
			int second=cal.get(Calendar.SECOND);

			String formattedDateString=""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+hour+":"+mint+":"+second;
			((AddExpenseActivity)callerActivity).removeFromCalAction(SharedData.getInstance().convertToDate(formattedDateString));
			cancelDialog();
		}
	};
	OnClickListener deleteHistoryListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			((AddExpenseActivity)callerActivity).performDeleteAction();
			cancelDialog();
		}
	};
}
