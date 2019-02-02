package br.com.custom.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import br.com.dinamo.AddBuyEventActivity;
import br.com.dinamo.AddExpenseActivity;
import br.com.dinamo.AddSellProductActivity;
import br.com.dinamo.R;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class DeleteDialog {

	private  Dialog deleteDialog=null;
	private  Activity callerActivity=null;
	private  int launcherCode=0;

	public DeleteDialog(int code,Activity activity){
		callerActivity=activity;
		launcherCode=code;
		
		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.delete_dialog_layout, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);
		
		deleteDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		deleteDialog.setContentView(dialogView);

		TextView dialogTitle=(TextView)dialogView.findViewById(R.id.dialogTitleTextView);
		TextView dialogMessage=(TextView)dialogView.findViewById(R.id.deleteDialogMessage);

		if(code==DinamoConstants.COMPRAS_SCREEN){
			dialogTitle.setText(SharedData.getInstance().mContext.getString(R.string.delete_compras_alert_title));
			dialogMessage.setText(SharedData.getInstance().mContext.getString(R.string.delete_compras_alert_message));
		}else if(code==DinamoConstants.VENDA_SCREEN){
			dialogTitle.setText(SharedData.getInstance().mContext.getString(R.string.delete_venda_alert_title));
			dialogMessage.setText(SharedData.getInstance().mContext.getString(R.string.delete_venda_alert_message));
		}else if(code==DinamoConstants.EXPENSE_SCREEN){
			dialogTitle.setText(SharedData.getInstance().mContext.getString(R.string.delete_expense_alert_title));
			dialogMessage.setText(SharedData.getInstance().mContext.getString(R.string.delete_expense_alert_message));
		}


		TextView cancelButton=(TextView)dialogView.findViewById(R.id.cancelButtonView);
		TextView addOkButton=(TextView)dialogView.findViewById(R.id.okButtonView);

		cancelButton.setOnClickListener(addDialogCancelListner);
		addOkButton.setOnClickListener(addDialogOkListner);
	}
	public void showDialog(){
		deleteDialog.show();
	}
	public void cancelDialog(){
		deleteDialog.cancel();
	}
    private void deletDialogActions(){
    	if(launcherCode==DinamoConstants.COMPRAS_SCREEN){
			((AddBuyEventActivity)callerActivity).performDeleteAction();
		}else if(launcherCode==DinamoConstants.VENDA_SCREEN){
			((AddSellProductActivity)callerActivity).performDeleteAction();
		}else if(launcherCode==DinamoConstants.EXPENSE_SCREEN){
			((AddExpenseActivity)callerActivity).performDeleteAction();
		}
		deleteDialog.cancel();
    }
	OnClickListener addDialogOkListner=new OnClickListener() {

		@Override
		public void onClick(View arg0) {	
			deletDialogActions();
		}
	};
	OnClickListener addDialogCancelListner=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			deleteDialog.cancel();

		}
	};
	
}



