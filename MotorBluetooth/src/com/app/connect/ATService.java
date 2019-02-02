package com.app.connect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.commands.ATCommandOutput;
import com.app.motorbluetooth.R;
import com.app.utilities.MotorConstants;

public class ATService{
	private static final String TAG = "BluetoothReadService";
	private static final boolean D = true;
	private Handler mHandler=null;
	private ATConnectThread mConnectedThread=null;
	private int mState;
	int OBD_SERVICE_RUNNING_NOTIFY=4;

	PendingIntent contentIntent = null;
	Context mContext = null;
	NotificationManager notifyMan = null;
	Intent notificationIntent = null;

	public ATService(Context context,Handler handler){
		mContext=context;
		mHandler=handler;

		notifyMan = ((NotificationManager)context.getSystemService("notification"));
		notificationIntent = new Intent(context, ATService.class);
		contentIntent = PendingIntent.getActivity(context, 0, this.notificationIntent, 0);
	}
	public void notifyMessage(){
		long when = System.currentTimeMillis();
		Notification notification = new Notification(R.drawable.ic_launcher, "Getting data", when);
		notification.setLatestEventInfo(mContext, "OBD Service Running", "", contentIntent);
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notifyMan.notify(OBD_SERVICE_RUNNING_NOTIFY, notification);
	}
	public synchronized void setState(int state) {
		mState = state;
		mHandler.obtainMessage(MotorConstants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}
	public synchronized int getState() {
		return mState;
	}
	public synchronized void start() {
		if (mConnectedThread != null) {
			mConnectedThread.cancel(); 
			mConnectedThread = null;
		}
		setState(MotorConstants.STATE_NONE);
	}
	public synchronized void connect(BluetoothDevice device) {
		if(mConnectedThread!=null && mConnectedThread.isAlive()){
			mConnectedThread=null;
		}
		if (mConnectedThread == null) {
			setState(MotorConstants.STATE_CONNECTING);
			notifyMessage();
			mConnectedThread = new ATConnectThread(device,mHandler,this);
			if(mState==MotorConstants.STATE_CONNECTED && mConnectedThread!=null){
				mConnectedThread.start();
			}
		}
	}
	public synchronized void stop() {
		if (mConnectedThread != null) {
			mConnectedThread.cancel(); 
			mConnectedThread = null;
		}

		setState(MotorConstants.STATE_NONE);
	}
	private void sendMessageToHandler(String message){
		Message msg = mHandler.obtainMessage(MotorConstants.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(MotorConstants.TOAST,message);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
	public void updateOuputCommand(ATCommandOutput paramATCommandOutput){
		synchronized (this) {
			if (mState != MotorConstants.STATE_CONNECTED){
				sendMessageToHandler("Bluetooth not connected.Command not sent");
				return;
			}
		}
		if(mConnectedThread!=null){
			mConnectedThread.setOutPutCommand(paramATCommandOutput);
		}else{
			sendMessageToHandler("Can't send Command thread closed");
		}	
	}


}
