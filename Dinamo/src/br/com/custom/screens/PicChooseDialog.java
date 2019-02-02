package br.com.custom.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.dinamo.R;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class PicChooseDialog {
	private  Dialog chooseDialog=null;
	private  Activity callerActivity=null;

	public PicChooseDialog(Activity activity){
		callerActivity=activity;

		LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView=inflater.inflate(R.layout.choose_image_dialog, null, false);
		ScalingUtility.getInstance().scaleView(dialogView);
		
		chooseDialog=new Dialog(SharedData.getInstance().mContext,android.R.style.Theme_Translucent_NoTitleBar);
		chooseDialog.setContentView(dialogView);

		Button openGalleryButton=(Button)dialogView.findViewById(R.id.OpenGalleryButton);
		Button launchCameraButton=(Button)dialogView.findViewById(R.id.launchCameraButton);

		openGalleryButton.setOnClickListener(openGalleryListener);
		launchCameraButton.setOnClickListener(launchCameraListener);
	}
	public void showDialog(){
		chooseDialog.show();
	}
	public void cancelDialog(){
		chooseDialog.cancel();
	}
	OnClickListener openGalleryListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			callerActivity.startActivityForResult(intent, DinamoConstants.GALLERY_REQUEST_CODE);
		}
	};
	OnClickListener launchCameraListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, "Photo");
			values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
			Uri cameraPhotoUri = callerActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			SharedData.getInstance().selectedImageUri=cameraPhotoUri;			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	
			intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			callerActivity.startActivityForResult(intent, DinamoConstants.CAMERA_REQUEST_CODE);

		}
	};
}
