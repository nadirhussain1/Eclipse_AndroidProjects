package com.app.connect;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.commands.ATCommandOutput;
import com.app.utilities.MotorConstants;
import com.app.utilities.SharedData;

public class ATConnectThread extends Thread{
	private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	ATCommandOutput cmdOutput  =null;
	OutputStream    outStream     = null;
	BluetoothSocket mmSocket   = null;
	BluetoothDevice blueDevice =null;
	byte[] byteCommands=null;
	Handler mHandler=null;
	private ATService parentService=null;
	public boolean keepSendingCommand=true;

	public ATConnectThread(BluetoothDevice device,Handler handler,ATService service){
		blueDevice = device;
		mHandler=handler;
		parentService=service;
		cmdOutput=new ATCommandOutput("64006600","","");
		initCreateSocketAndStreams(device);
		connectSocket();

	}
	private void initCreateSocketAndStreams(BluetoothDevice device){
		OutputStream tmpOut = null;
		try {
			mmSocket = device.createRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);
			tmpOut = mmSocket.getOutputStream();
		} catch (IOException exception) {
			parentService.setState(MotorConstants.STATE_NONE);
			sendMessageToHandler("Error Creating Socket="+exception.toString());
			cancel();
		}
		outStream = tmpOut;
	}
	private void connectSocket(){
		try {
			mmSocket.connect();
			parentService.setState(MotorConstants.STATE_CONNECTED);
			try {
				sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException exception) {
			parentService.setState(MotorConstants.STATE_NONE);
			sendMessageToHandler("Error Connecting Socket="+exception.toString());
			cancel();
		}
	}
	private void sendMessageToHandler(String message){
		Message msg = mHandler.obtainMessage(MotorConstants.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(MotorConstants.TOAST,message);
		msg.setData(bundle);
		mHandler.sendMessage(msg);	
	}
	public void setOutPutCommand(ATCommandOutput paramATCommandOutput){
		keepSendingCommand=false;

		cmdOutput=paramATCommandOutput;
		byteCommands=SharedData.getInstance().hexStringToBytes(paramATCommandOutput.getATcontent());

		keepSendingCommand=true;
	}
	private void SendsOutputCommand(){
		if(byteCommands!=null){
			try {
				synchronized (byteCommands) {
					outStream.write(byteCommands);
					outStream.flush();
				}
			}
			catch (IOException e) {
				parentService.setState(MotorConstants.STATE_NONE);
				close();
			}
		}

	}

	public void cancel(){
		close();
	}

	private void close(){
		try {
			this.mmSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;

	}
	public void run(){
		while(true){
			if(keepSendingCommand){
				SendsOutputCommand();
			}
		}
	}


}
