package com.app.motorbluetooth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.utilities.ScalingUtility;
import com.app.utilities.SharedData;

public class DiscoveryDialg{

	private BluetoothAdapter bluetoothAdapter = null;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private DeviceDiscoveryAdapter discovAdapter=null;
	private Dialog discoveryDialog=null;
	Context mContext=null;
	ProgressBar scanWaitBar=null;

	public DiscoveryDialg(Context context){
		mContext=context;
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.discovered_devices_list, null, false);
		ScalingUtility.getInstance((Activity)context).scaleView(view);

		discovAdapter=new DeviceDiscoveryAdapter();
		ListView listView=(ListView)view.findViewById(R.id.paired_devices_listView);
		listView.setAdapter(discovAdapter);
		listView.setOnItemClickListener(deviceSelectListener);

		Button scanButton=(Button)view.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(scanDeviceCliclListener);
		scanWaitBar=(ProgressBar)view.findViewById(R.id.scanBar);

		IntentFilter discoveryFilter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
		context.registerReceiver(discoveryReceiver, discoveryFilter);
		IntentFilter foundFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
		context.registerReceiver(foundReceiver, foundFilter);

		findBoundDevices();

		discoveryDialog=new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		discoveryDialog.setTitle("Devices");
		discoveryDialog.setContentView(view);
		discoveryDialog.setOnDismissListener(dismissListener);
	}
	public void findBoundDevices(){
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		BluetoothDevice localBluetoothDevice;
		Iterator localIterator=null;

		if (pairedDevices.size() > 0)
		{
			localIterator = pairedDevices.iterator();	
			while(localIterator.hasNext()) {
				localBluetoothDevice = (BluetoothDevice)localIterator.next();
				devices.add(localBluetoothDevice);
			}
			discovAdapter.notifyDataSetChanged();
		}

	}
	OnDismissListener dismissListener=new OnDismissListener() {
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			MainActivity.isClicked=false;
			
		}
	};
	public void showDailog(){
		discoveryDialog.show();
	}
	public void cancelDailog(){
		discoveryDialog.cancel();
	}
	private BroadcastReceiver foundReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			scanWaitBar.setVisibility(View.GONE);
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			devices.add(device);
			discovAdapter.notifyDataSetChanged();
		}
	};
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)  
		{
			scanWaitBar.setVisibility(View.GONE);
			mContext.unregisterReceiver(foundReceiver);
			mContext.unregisterReceiver(this);
			//discoveryFinished = true;
		}
	};
	OnClickListener scanDeviceCliclListener=new OnClickListener() {

		@Override
		public void onClick(View view) {

			scanWaitBar.setVisibility(View.VISIBLE);
			if (bluetoothAdapter.isDiscovering()) {
				bluetoothAdapter.cancelDiscovery();
			}
			bluetoothAdapter.startDiscovery();		
		}
	};
	OnItemClickListener deviceSelectListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {

			bluetoothAdapter.cancelDiscovery();
			SharedData.getInstance().selectedDeviceAddress = devices.get(position).getAddress();
			cancelDailog();
			((MainActivity)mContext).handleDeviceSelected();
		}
	};
	private class DeviceDiscoveryAdapter extends BaseAdapter{

		@Override
		public int getCount() {	
			return devices.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.device_row, null, false);
				ScalingUtility.getInstance((Activity)mContext).scaleView(convertView);
			}

			TextView deviceNameTextView=(TextView)convertView.findViewById(R.id.device_name);
			TextView deviceAddTextView=(TextView)convertView.findViewById(R.id.device_address);

			deviceNameTextView.setText(devices.get(position).getName());
			deviceAddTextView.setText(devices.get(position).getAddress());
			convertView.setTag(position);

			return convertView;
		}

	}


}
