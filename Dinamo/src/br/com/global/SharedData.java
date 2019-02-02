
package br.com.global;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import br.com.data.model.BoughtProduct;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.SoldProduct;
import br.com.data.model.User;
import br.com.dinamo.DinamoApplication;
import br.com.utilities.ScalingUtility;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SharedData {

	private static     SharedData instance = null;
	public  Context    mContext=null;
	public  User       currentUser=null;
	public  int        loadDataAPITraceNumber=0;
	public  String []  monthsNameList=null;
	public  String []  shortMonthNameList=null;
	public  String []  daysNames=null;
	public  Uri        selectedImageUri=null;
	public  Bitmap     userImageBitmap=null;

	public ArrayList<BoughtProduct>  boughtEventsList=null;
	public ArrayList<SoldProduct>    soldEventsList=null;
	public ArrayList<ExpenseProduct> expenseEventsList=null;
	public boolean hasItemBeenAdded=false;

	public static SharedData getInstance() {
		if(instance==null){
			instance=new SharedData();
		}
		return instance;
	}
	public void resetInstance(){
		boughtEventsList=null;
		soldEventsList=null;
		expenseEventsList=null;
		selectedImageUri=null;
		userImageBitmap=null;
		currentUser=null;
		loadDataAPITraceNumber=0;
		monthsNameList=null;
		shortMonthNameList=null;
		daysNames=null;
	}
	public void setContext(Context context){
		mContext=context;
	}
	public void applyFontToTextView(TextView view,String fontName){
		Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(), fontName);
		view.setTypeface(typeFace);
	}
	public boolean isInternetAvailable(){
		ConnectivityManager connMgr = (ConnectivityManager)mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;   	
	}
	public void displayMessageAlert(String title,String message,final boolean closeApp){
		new AlertDialog.Builder(mContext).setMessage(message) 
		.setTitle(title) 
		.setCancelable(true) 
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton){
				if(closeApp){
					((Activity)mContext).finish();
				}
			} 
		}) 
		.show(); 
	}

	public  String getStringFromInputStream(InputStream stream) {

		BufferedReader buffReader = null;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {

			buffReader = new BufferedReader(new InputStreamReader(stream));
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
	public Date convertToDate(String dateString){
		if(dateString==null || dateString.trim().toString().length()==0){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (java.text.ParseException e) {
			Log.d("Datedebug", "Exception");
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(convertedDate);
		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		
		Log.d("Datedebug", "YEAR="+year+"MONTH="+monthOfYear);
		Log.d("ConvertedDate", ""+convertedDate);
		return convertedDate;

	}
	public static Bitmap decodeBitmap(String path,int photoSize){	
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int requiredWidth=ScalingUtility.getInstance().resizeAspectFactor(photoSize);
		options.inSampleSize=calculateInSampleSize(options,requiredWidth,requiredWidth);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path, options);
	}
	public  Bitmap decodeCameraBitmap(Uri uriPath,int photoSize){	
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap=null;
		try {
			BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uriPath),null, options);
			int requiredWidth=ScalingUtility.getInstance().resizeAspectFactor(photoSize);
			options.inSampleSize=calculateInSampleSize(options,requiredWidth,requiredWidth);
			options.inJustDecodeBounds=false;
			bitmap= BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uriPath),null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;

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
	public static byte[] getBytes(Bitmap bitmap) {
		if(bitmap ==null){
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG,100, stream);
		return stream.toByteArray();

	}
	public static Bitmap getPhoto(byte[] image) {
		if(image !=null){
			return BitmapFactory.decodeByteArray(image, 0, image.length);
		}
		return null;
	}
	public void sendScreenName(String path){
		Tracker tracker = ((DinamoApplication)((Activity)mContext).getApplication()).getTracker();   
		tracker.setScreenName(path);
		tracker.send(new HitBuilders.AppViewBuilder().build());      
		tracker.setScreenName(null);
	}
	public void sendEventNameToAnalytics(String labelId){
		Tracker tracker = ((DinamoApplication)((Activity)mContext).getApplication()).getTracker();   
		tracker.send(new HitBuilders.EventBuilder()
		.setCategory("Button")
		.setAction("Click")
		.setLabel(labelId)
		.build());
	}
	public  int getAppVersion() {
		try {
			PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	public boolean isValidDecimalString(String value){
		int occurences=0;
		if(value==null || value.trim().length()==0 || value.equalsIgnoreCase(".")){
			return false;
		}
		for(int count=0;count<value.length();count++){
			if(value.charAt(count)=='.'){
				occurences++;
			}
			if(occurences==2){
				return false;
			}
		}
		return true;
	}
	public Bitmap getCorrectRotatedBitmap(Bitmap originalBitmap,String imageUriPath){
		try {
			ExifInterface exif = new ExifInterface(imageUriPath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			int angle=getDegree(exifOrientation);
			return rotateBitmap(originalBitmap,angle);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return originalBitmap;

	}
	private int getDegree(int exifOrientation){
		int rotate = 0;
		switch (exifOrientation) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;
		}
		return rotate;	
	}
	private  Bitmap rotateBitmap(Bitmap bm, int degree) {
		Bitmap bitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), matrix, true);


		return bitmap;
	}
	public static void executeTask(AsyncTask task){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	    else
	    	task.execute();	
	}
	

}
