package com.assessment.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class GlobalUtility {

	public static boolean isInternetAvailable(Context mContext){
		ConnectivityManager connMgr = (ConnectivityManager)mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;   
	}
	public static void displayMessageAlert(final Context mContext,String title,String message,final boolean closeApp){
		new AlertDialog.Builder(mContext).setMessage(message) 
		.setTitle(title) 
		.setCancelable(true) 
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.cancel();
				if(closeApp){
					((Activity)mContext).finish();
				}
			} 
		}) 
		.show(); 
	}
	public static void displayExceptionToast(Context mContext,String message){
		Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();
	}
	public static String convertStreamToString(InputStream inputStream){
		BufferedReader buffReader = null;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			buffReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = buffReader.readLine()) != null) {
				stringBuilder.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffReader != null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();
	}

}
