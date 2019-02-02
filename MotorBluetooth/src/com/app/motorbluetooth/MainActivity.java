package com.app.motorbluetooth;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.commands.ATCommandOutput;
import com.app.connect.ATService;
import com.app.dialogs.OnOffDialog;
import com.app.utilities.BluePref;
import com.app.utilities.MotorConstants;
import com.app.utilities.ScalingUtility;
import com.app.utilities.SharedData;

public class MainActivity extends Activity implements OnClickListener {

	public static final String LOG_TAG = "MainActivity";
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int GALLERY_REQUEST_CODE=1;
	public static final boolean DEBUG = true;

	private String mConnectedDeviceName = null;
	private BluetoothAdapter mBluetoothAdapter = null;
	private ATService mSerialService = null;
	private NotificationManager notificationManager;

	boolean isConnected=false;
	public static boolean isClicked = false;
	private MenuItem mMenuItemConnect;
	private TextView connectStatusTextView=null;

	String cmdAString = "";
	String cmdBString = "";

	RelativeLayout speedButtonsLayout=null;
	RelativeLayout infoLayout=null;
	Button powerOnButton=null;
	Button backgroundToggleButton=null;
	Button presetAButton=null;
	Button presetBButton=null;
	ImageView machineABLogo=null;
	ImageViewTouch backgroundTouchImage=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View parentView=inflater.inflate(R.layout.activity_main, null, false);
		ScalingUtility.getInstance(this).scaleView(parentView);
		setContentView(parentView);

		connectStatusTextView=(TextView)parentView.findViewById(R.id.ConnectStatusText);
		notificationManager = ((NotificationManager)getSystemService("notification"));
		
		BluePref.getInstance(this).loadAppResumeData();
		initClicks();
		checkBluetooth();
		mSerialService=new ATService(this,mHandlerBT);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ( (mBluetoothAdapter != null)  && (!mBluetoothAdapter.isEnabled()) ) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_dialog_turn_on_bt)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.alert_dialog_warning_title)
			.setCancelable( false )
			.setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableIntent, REQUEST_ENABLE_BT);			
				}
			})
			.setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finishDialogNoBluetooth();            	
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}		

		if (mSerialService != null) {
			if (mSerialService.getState() == MotorConstants.STATE_NONE) {
				mSerialService.start();
			}
		}


	}
	public void onDestroy(){
		super.onDestroy();
		if (mSerialService != null){
			mSerialService.stop();
		}
		this.notificationManager.cancelAll();
	}
	@Override
	protected void onPause() {	
		super.onPause();	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			if(resultCode==RESULT_CANCELED){
				finishDialogNoBluetooth();
			}else if(resultCode==RESULT_OK){
			}
			break;
		case GALLERY_REQUEST_CODE:
			changeBackground(resultCode,data);
			break;
		default:
			break;
		}
	}
	private void changeBackground(int resultCode,Intent data){
		if (resultCode == RESULT_OK && data!=null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);


			int screenWidth=ScalingUtility.getInstance(this).getCurrentWidth();
			int screenheight=ScalingUtility.getInstance(this).getCurrentHeight();
			Bitmap bitmap=SharedData.getInstance().decodeBitmap(picturePath);
			Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap, screenWidth, screenheight, false);
			backgroundTouchImage.setImageBitmap(scaledBitmap);

			cursor.close();

		}
	}
	private void checkBluetooth(){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			finishDialogNoBluetooth();
			return;
		}
	}
	private void initClicks(){
		backgroundTouchImage=(ImageViewTouch)findViewById(R.id.backImageTouch);
		backgroundTouchImage.setImageBitmap(SharedData.getInstance().decodeResource(this, R.drawable.background_2));

		speedButtonsLayout=(RelativeLayout)findViewById(R.id.speedControlLayout);
		infoLayout=(RelativeLayout)findViewById(R.id.infoLayout);
		powerOnButton=(Button)findViewById(R.id.powerOnOfButton);
		backgroundToggleButton=(Button)findViewById(R.id.backgroundToggle);
		presetAButton=(Button)findViewById(R.id.PresetAButton);
		presetBButton=(Button)findViewById(R.id.PresetBButton);
		machineABLogo=(ImageView)findViewById(R.id.machineABLogo);

		findViewById(R.id.machineAUpButton).setOnClickListener(this);
		findViewById(R.id.machineADownButton).setOnClickListener(this);
		findViewById(R.id.machineBUpButton).setOnClickListener(this);
		findViewById(R.id.machineBDownButton).setOnClickListener(this);

		powerOnButton.setOnClickListener(powerOnOffClickListener);
		backgroundToggleButton.setOnClickListener(backToggleListener);
		presetAButton.setOnClickListener(presetMotorAValueListener);
		presetBButton.setOnClickListener(presetMotorBValueListener);
		findViewById(R.id.cloakButton).setOnClickListener(cloakButtonListener);

	}
	public void handleDeviceSelected(){
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(SharedData.getInstance().selectedDeviceAddress);
		mSerialService.connect(device);
		isClicked=false;
	}
	public void liveData(ATCommandOutput paramATCommandOutput){
		//connectStatusTextView.setText("Sending Command Output");
		mSerialService.updateOuputCommand(paramATCommandOutput);
	}
	public boolean onCreateOptionsMenu(Menu paramMenu){
		getMenuInflater().inflate(R.menu.option_menu, paramMenu);
		this.mMenuItemConnect = paramMenu.getItem(0);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.connect:

			if (getConnectionState() == MotorConstants.STATE_NONE) {
				discoverDevices();
			}
			else if (getConnectionState() == MotorConstants.STATE_CONNECTED) {
				mSerialService.stop();
				mSerialService.start();
			}
			return true;
		case R.id.preferences:
			exitApp();
			return true;
		default:
			return true;
		}

	}
	public int getConnectionState() {
		return mSerialService.getState();
	}
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	{
		if (paramInt==KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder.setMessage("Do you exit program?").setIcon(R.drawable.info_active).setTitle(R.string.app_name).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
					exitApp();
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
			});
			localBuilder.create().show();
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}
	public void exceptionAlert(){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setMessage("Exception occured. Do you want to close?").setIcon(R.drawable.info_active).setTitle(R.string.app_name).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){ 
				exitApp();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
		});
		localBuilder.create().show();
	}



	public void finishDialogNoBluetooth() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.alert_dialog_no_bt)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(R.string.app_name)
		.setCancelable( false )
		.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();            	
			}
		});
		AlertDialog alert = builder.create();
		alert.show(); 
	}
	private void discoverDevices(){
		DiscoveryDialg discDialog=new DiscoveryDialg(this);
		discDialog.showDailog();
	}
	public void handlePowerOnOffMotors(){
		if(SharedData.getInstance().isMotorAOn){
			constructMotorACommand();
		}else{
			cmdAString="";
		}
		if(SharedData.getInstance().isMotorBOn){
			constructMotorBCommand();
		}else{
			cmdBString="";
		}
		connectStatusTextView.setText("Command:"+cmdAString+cmdBString);
		initiateCommandSendingThread();
	}
	OnClickListener powerOnOffClickListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			OnOffDialog dialog=new OnOffDialog(MainActivity.this);
			dialog.showDialog();	
		}
	};
	OnClickListener backToggleListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, GALLERY_REQUEST_CODE);
		}
	};
	OnClickListener cloakButtonListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(SharedData.getInstance().isCloakedClicked){
				SharedData.getInstance().isCloakedClicked=false;

				speedButtonsLayout.setVisibility(View.VISIBLE);
				infoLayout.setVisibility(View.VISIBLE);
				powerOnButton.setVisibility(View.VISIBLE);
				backgroundToggleButton.setVisibility(View.VISIBLE);
				presetAButton.setVisibility(View.VISIBLE);
				presetBButton.setVisibility(View.VISIBLE);
				machineABLogo.setVisibility(View.VISIBLE);
			}else{
				SharedData.getInstance().isCloakedClicked=true;

				speedButtonsLayout.setVisibility(View.GONE);
				infoLayout.setVisibility(View.GONE);
				powerOnButton.setVisibility(View.GONE);
				backgroundToggleButton.setVisibility(View.GONE);
				presetAButton.setVisibility(View.GONE);
				presetBButton.setVisibility(View.GONE);
				machineABLogo.setVisibility(View.GONE);
			}
		}
	};
	OnClickListener presetMotorAValueListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			BluePref.getInstance(MainActivity.this).savePresetMotorAValue();
			Toast.makeText(MainActivity.this,"Value of Motor A Saved", Toast.LENGTH_SHORT).show();
		}
	};
	OnClickListener presetMotorBValueListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			BluePref.getInstance(MainActivity.this).savePresetMotorBValue();
			Toast.makeText(MainActivity.this,"Value of Motor B Saved", Toast.LENGTH_SHORT).show();
		}
	};
	private void constructMotorBCommand(){
		if(Integer.toHexString(SharedData.getInstance().speedMotorB).length()==1){
			cmdBString = ("670" + Integer.toHexString(SharedData.getInstance().speedMotorB));	
		}else{
			cmdBString = ("67" + Integer.toHexString(SharedData.getInstance().speedMotorB));
		}
	}
	private void constructMotorACommand(){
		if(Integer.toHexString(SharedData.getInstance().speedMotorA).length()==1){
			cmdAString = ("650" + Integer.toHexString(SharedData.getInstance().speedMotorA));	
		}else{
			cmdAString = ("65" + Integer.toHexString(SharedData.getInstance().speedMotorA));
		}
	}
	private void motorASpeedIncrease(){
		if(SharedData.getInstance().isMotorAOn){

			SharedData.getInstance().speedMotorA+=10;
			if(SharedData.getInstance().speedMotorA>255){
				SharedData.getInstance().speedMotorA=255;
			}
			constructMotorACommand();
		}else{
			Toast.makeText(this,"Turn Motor A on from power button", Toast.LENGTH_SHORT).show();
			cmdAString="";
		}

		if(SharedData.getInstance().isMotorBOn){
			constructMotorBCommand();
		}else{
			cmdBString="";
		}
	}
	private void motorBSpeedIncrease(){
		if(SharedData.getInstance().isMotorBOn){
			SharedData.getInstance().speedMotorB+=10;
			if(SharedData.getInstance().speedMotorB>255){
				SharedData.getInstance().speedMotorB=255;
			}
           constructMotorBCommand();
		}else{
			Toast.makeText(this,"Turn Motor B on from power button", Toast.LENGTH_SHORT).show();
			cmdBString="";
		}
		if(SharedData.getInstance().isMotorAOn){
			constructMotorACommand();
		}else{
			cmdAString="";
		}
	}
	private void motorASpeedDecrease(){
		if(SharedData.getInstance().isMotorAOn){
			SharedData.getInstance().speedMotorA-=10;
			if(SharedData.getInstance().speedMotorA<1){
				SharedData.getInstance().speedMotorA=1;
			}
			constructMotorACommand();
		}else{
			Toast.makeText(this,"Turn Motor A on from power button", Toast.LENGTH_SHORT).show();
			cmdAString="";
		}

		if(SharedData.getInstance().isMotorBOn){
			constructMotorBCommand();
		}else{
			cmdBString="";
		}
	}
	private void motorBSpeedDecrease(){
		if(SharedData.getInstance().isMotorBOn){
			SharedData.getInstance().speedMotorB-=10;
			if(SharedData.getInstance().speedMotorB<1){
				SharedData.getInstance().speedMotorB=1;
			}
			constructMotorBCommand();
			
		}else{
			Toast.makeText(this,"Turn Motor B on from power button", Toast.LENGTH_SHORT).show();
			cmdBString="";
		}

		if(SharedData.getInstance().isMotorAOn){
			constructMotorACommand();
		}else{
			cmdAString="";
		}
	}
	@Override
	public void onClick(View clickedView) {
		if(isClicked){
			return;
		}
		isClicked=true;

		if(!isConnected){
			discoverDevices();
		}
		else{
			switch (clickedView.getId()) {
			case R.id.machineAUpButton:
				motorASpeedIncrease();
				break;
			case R.id.machineADownButton:
				motorASpeedDecrease();
				break;
			case R.id.machineBDownButton:
				motorBSpeedDecrease();
				break;
			case R.id.machineBUpButton:
				motorBSpeedIncrease();
				break;	

			default:
				break;
			}
			connectStatusTextView.setText("Command:"+cmdAString+cmdBString);
			initiateCommandSendingThread();
		}

	}
	private void initiateCommandSendingThread(){
		new Thread(new Runnable(){
			public void run(){
				MainActivity.this.mHandler.sendEmptyMessage(1);	
				isClicked=false;
			}

		}).start();
	}
	public void exitApp() {
		mSerialService.stop();
		this.notificationManager.cancelAll();
		finish();
	}
	Handler mHandler = new Handler(){
		public void handleMessage(Message paramAnonymousMessage){
			super.handleMessage(paramAnonymousMessage);
			liveData(new ATCommandOutput((cmdAString + cmdBString), "", ""));

		}

	};
	private final Handler mHandlerBT = new Handler() {

		@Override
		public void handleMessage(Message msg) {  
			switch (msg.what) {
			case MotorConstants.MESSAGE_STATE_CHANGE:
				if(DEBUG) Log.i(LOG_TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case MotorConstants.STATE_CONNECTED:
					if (mMenuItemConnect != null) {
						mMenuItemConnect.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
						mMenuItemConnect.setTitle(R.string.disconnect);
					}
					//connectStatusTextView.setText(getString(R.string.title_connected_to)+mConnectedDeviceName);
					Toast.makeText(getApplicationContext(),getString(R.string.title_connected_to)+mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					isConnected=true;
					break;

				case MotorConstants.STATE_CONNECTING:
					//connectStatusTextView.setText(getString(R.string.title_connecting));
					Toast.makeText(getApplicationContext(),R.string.title_connecting,Toast.LENGTH_SHORT).show();
					break;

				case MotorConstants.STATE_LISTEN:
				case MotorConstants.STATE_NONE:
					isConnected=false;
					if (mMenuItemConnect != null) {
						mMenuItemConnect.setIcon(android.R.drawable.ic_menu_search);
						mMenuItemConnect.setTitle(R.string.connect);
					}
					//connectStatusTextView.setText(getString(R.string.title_not_connected));
					Toast.makeText(getApplicationContext(),R.string.title_not_connected,Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			case MotorConstants.MESSAGE_WRITE:
				isConnected=true;
				//connectStatusTextView.setText(""+"Click Button A Up,if doesn't work then click down");
				Toast.makeText(getApplicationContext(),"Response Received from Bluetooth",Toast.LENGTH_SHORT).show();
				break;     
			case MotorConstants.MESSAGE_DEVICE_NAME:
				isConnected=true;
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(MotorConstants.DEVICE_NAME);
				//connectStatusTextView.setText("Connected to "+ mConnectedDeviceName);
				Toast.makeText(getApplicationContext(), "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case MotorConstants.MESSAGE_TOAST:
				//connectStatusTextView.setText(msg.getData().getString(MotorConstants.TOAST));
				Toast.makeText(getApplicationContext(), msg.getData().getString(MotorConstants.TOAST),Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};    
}
