package br.com.dinamo;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.custom.screens.PicChooseDialog;
import br.com.data.model.BoughtProduct;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.requests.AsyncResponseHandler;
import br.com.requests.ImageUploadRequest;
import br.com.storage.DinamoDatabaseHelper;
import br.com.utilities.ScalingUtility;

public class TakePhotoActivity extends Activity{
	private  Button skipPhotoButton=null;
	private  Button takePhotoButton=null;
	private  boolean hasTakenPhoto=false;
	private  PicChooseDialog chooseDialog=null;
	private  String  eventServerId="";
	private  int  localArrayIndex=0;
	private  ImageView centralImageView=null;
	private  Bitmap buyEventBitmap=null;
	private  BoughtProduct boughtProduct=null;
	private  boolean  isEdit=false;
	ProgressDialog progressDialog=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		eventServerId=getIntent().getStringExtra("ServerId");
		localArrayIndex=getIntent().getIntExtra("localId",-1);
		isEdit=getIntent().getBooleanExtra("Edit", false);


		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.add_picture_buy_event, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);


		boughtProduct=SharedData.getInstance().boughtEventsList.get(localArrayIndex);


		centralImageView=(ImageView)findViewById(R.id.buyEventImageView);
		if( localArrayIndex!=-1 && boughtProduct.getProductPhoto()!=null){
			centralImageView.setImageBitmap(boughtProduct.getProductPhoto());
		}

		SharedData.getInstance().sendScreenName("Take Cupom Photo Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
	}
	private void setEnabled(boolean flag){
		skipPhotoButton.setEnabled(flag);
		takePhotoButton.setEnabled(flag);
	}
	private void showProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	@Override
	public void onBackPressed() {
		if(progressDialog!=null && progressDialog.isShowing()){
			return;
		}else{
			BuyEventsMainActivity.refreshAdapter(false);
			TakePhotoActivity.this.finish();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DinamoConstants.CAMERA_REQUEST_CODE) {
			handleCameraResponse(resultCode);
		}else if(requestCode==DinamoConstants.GALLERY_REQUEST_CODE){
			try {
				handleGalleryChooser(resultCode,data);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void handleGalleryChooser(int resultCode,Intent data) throws FileNotFoundException, IOException{
		if(chooseDialog !=null){
			chooseDialog.cancelDialog();
		}

		if (resultCode == RESULT_OK && data!=null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String photoFilePath = cursor.getString(columnIndex);
			cursor.close();

			buyEventBitmap=SharedData.getInstance().decodeBitmap(photoFilePath,DinamoConstants.TAKE_PHOTO_SIZE);
			buyEventBitmap=SharedData.getInstance().getCorrectRotatedBitmap(buyEventBitmap, photoFilePath);
			centralImageView.setImageBitmap(buyEventBitmap);

			takePhotoButton.setText("SALVAR E CONTINUAR");
			hasTakenPhoto=true;

		}
	}
	private void handleCameraResponse(int resultCode){
		if(chooseDialog!=null){
			chooseDialog.cancelDialog();
		}

		if (resultCode == RESULT_OK) {

			buyEventBitmap=SharedData.getInstance().decodeCameraBitmap(SharedData.getInstance().selectedImageUri, DinamoConstants.TAKE_PHOTO_SIZE);
			//buyEventBitmap=Bitmap.createScaledBitmap(buyEventBitmap,DinamoConstants.TAKE_PHOTO_SIZE,DinamoConstants.TAKE_PHOTO_SIZE, false);
			buyEventBitmap=SharedData.getInstance().getCorrectRotatedBitmap(buyEventBitmap, SharedData.getInstance().selectedImageUri.getPath());
			centralImageView.setImageBitmap(buyEventBitmap);
			takePhotoButton.setText("SALVAR E CONTINUAR");

			hasTakenPhoto=true;
		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		}
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.addPhotoHeadlabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.flashOnLabelText);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_BOLD);
		skipPhotoButton=(Button)findViewById(R.id.skipPhotoStepButton);
		SharedData.getInstance().applyFontToTextView(skipPhotoButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		takePhotoButton=(Button)findViewById(R.id.takePhotoButton);
		SharedData.getInstance().applyFontToTextView(takePhotoButton, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		skipPhotoButton.setOnClickListener(skipPhotoButtonListener);
		takePhotoButton.setOnClickListener(choosePhotoListener);
	}
	private void postPhotoToBackend(){
//		if(SharedData.getInstance().isInternetAvailable() && eventServerId.trim().length()>0 && buyEventBitmap!=null){
//			postPhotoToServer();
//		}else{
			boughtProduct.setProductPhoto(buyEventBitmap);
			boughtProduct.isSynchronized=false;
			savePhotoToDatabase();
	//	}
	}
	private void savePhotoToDatabase(){
		DinamoDatabaseHelper.getInstance().addUpdateBoughtProduct(boughtProduct, true);
		progressDialog.cancel();
		BuyEventsMainActivity.refreshAdapter(false);
		TakePhotoActivity.this.finish();
	}
//	private void postPhotoToServer(){
//		String url=DinamoConstants.BUY_EVENT_URL+"/"+eventServerId+"/photo";
//		ImageUploadRequest uploadRequest=new ImageUploadRequest(url,buyEventBitmap, uploadCallBackListener);
//		uploadRequest.execute();
//
//	}
	AsyncResponseHandler uploadCallBackListener=new AsyncResponseHandler() {

		@Override
		public void onResult(String response,int responseCode) {
			if(responseCode==DinamoConstants.HTTP_PUT_SUCCESS){
				boughtProduct.setProductPhoto(buyEventBitmap);
				boughtProduct.isSynchronized=true;
			}else{
				boughtProduct.isSynchronized=false;
			}
			savePhotoToDatabase();
		}
	};
	OnClickListener skipPhotoButtonListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedData.getInstance().sendEventNameToAnalytics("Skip Cupom Photo Button");
			BuyEventsMainActivity.refreshAdapter(false);
			TakePhotoActivity.this.finish();
		}
	}; 
	OnClickListener choosePhotoListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(!hasTakenPhoto){
				SharedData.getInstance().sendEventNameToAnalytics("Add Cupom Photo Button");
				chooseDialog=new PicChooseDialog(TakePhotoActivity.this);
				chooseDialog.showDialog();
			}else{
				showProgressDialog("Aguarde enquanto enviamos a foto...");
				setEnabled(false);
				postPhotoToBackend();
			}
		}
	};
}
