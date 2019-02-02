package br.com.custom.screens;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.dinamo.R;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class VisualizeDialog {
	private  Dialog    visualDialog=null;
	private  ImageView itemImageView=null;
	
	public VisualizeDialog(Bitmap bitmap){
		
		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.visualize_dialog, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);
		
		visualDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		visualDialog.setContentView(dialogView);

		itemImageView=(ImageView)dialogView.findViewById(R.id.itemPhoto);
		itemImageView.setImageBitmap(bitmap);
		Button closeButton=(Button)dialogView.findViewById(R.id.closeButton);

		closeButton.setOnClickListener(closeDialogListener);
	}
	public void showDialog(){
		visualDialog.show();
	}
	public void cancelDialog(){
		visualDialog.cancel();
	}
	OnClickListener closeDialogListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			visualDialog.cancel();
			
		}
	};
}
