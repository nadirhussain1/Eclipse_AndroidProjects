package com.app.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SharedData {

	private static SharedData sharedDataInstane=null;
	public  String selectedDeviceAddress="";
	public  boolean isMotorAOn=false;
	public  boolean isMotorBOn=false;
	public boolean isCloakedClicked=false;
	public  int speedMotorA=255;
	public  int speedMotorB=255;
	public static boolean isPagerEnabled=true;

	public static SharedData getInstance(){
		if(sharedDataInstane==null){
			sharedDataInstane=new SharedData();
		}
		return sharedDataInstane;
	}
	public static Bitmap decodeBitmap(String path){
		int requiredWidth=860;
		int requiredHeight=500;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize=calculateInSampleSize(options,requiredWidth,requiredHeight);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path, options);
	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	public Bitmap decodeResource(Context context,int id){
		return BitmapFactory.decodeResource(context.getResources(),id);
	}
	public byte[] hexStringToBytes(String paramString) {
		byte[] arrayOfByte;
		if ((paramString == null) || (paramString.equals(""))) {
			arrayOfByte = null;
		}

		String str = paramString.toUpperCase();
		int i = str.length() / 2;
		char[] arrayOfChar = str.toCharArray();
		arrayOfByte = new byte[i];
		for (int j = 0; j < i; j++)
		{
			int k = j * 2;
			arrayOfByte[j] = ((byte)(charToByte(arrayOfChar[k]) << 4 | charToByte(arrayOfChar[(k + 1)])));
		}
		return arrayOfByte;
	}
	private byte charToByte(char paramChar) {
		return (byte)"0123456789ABCDEF".indexOf(paramChar);
	}

}
