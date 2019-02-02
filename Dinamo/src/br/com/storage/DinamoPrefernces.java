package br.com.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import br.com.data.model.User;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;


public class DinamoPrefernces {
	private static DinamoPrefernces prefInstance=null;
	private SharedPreferences sharedPreferences=null;
	private SharedPreferences.Editor editor=null;

	public static DinamoPrefernces getInstance(Context context) {
		if(prefInstance==null){
			prefInstance=new DinamoPrefernces(context);
		}
		return prefInstance;
	}
	private DinamoPrefernces(Context context){
		sharedPreferences = context.getSharedPreferences("DinamoApplication", 0);
		editor = sharedPreferences.edit();
	}
	public void clearPreferencesData(){
		editor.clear();
		editor.commit();
		prefInstance=null;
	}
	public void saveUserLogInStatus(int status){
		editor.putInt("USER_LOGIN_STATUS",status);
		editor.commit();
	}
	public int getUserLogInStatus(){
		return sharedPreferences.getInt("USER_LOGIN_STATUS", 0);
	}
	public void saveLastLogUserId(String userId){
		editor.putString("USER_LOGIN_ID",userId);
		editor.commit();
	}
	public String getLastLogUserId(){
		return sharedPreferences.getString("USER_LOGIN_ID", "");
	}
	public void saveLastSyncTime(long milliSeconds){
		editor.putLong("SYNC_TIME",milliSeconds);
		editor.commit();
	}
	public long getLastSyncTime(){
		return sharedPreferences.getLong("SYNC_TIME", System.currentTimeMillis());
	}
	public void saveCurrentUser(User currentUser){
		String imageBytesString="";
		if(currentUser.getUserPhoto()!=null){
			byte [] imageBytes=SharedData.getBytes(currentUser.getUserPhoto());
			imageBytesString= Base64.encodeToString(imageBytes, Base64.DEFAULT);
		}

		editor.putLong("primaryKey",currentUser.getPrimaryKey());
		editor.putString("userName", currentUser.getUserName());
		editor.putString("userPassword", currentUser.getPassword());
		editor.putString("userId", currentUser.getUserId());
		editor.putString("ServerId", currentUser.getServerId());
		editor.putString("UserPhoto", imageBytesString);

		editor.commit();

	}
	public void updateUserDownloadStatus(){
		editor.putInt("InitServer", 1);
		editor.commit();
	}
	public User getCurrentUser(){
		long primaryKey=sharedPreferences.getLong("primaryKey", -1);
		String name=sharedPreferences.getString("userName","");
		String password=sharedPreferences.getString("userPassword","");
		String user_id=sharedPreferences.getString("userId", "");
		String serverId=sharedPreferences.getString("ServerId", "");
		int initServer=sharedPreferences.getInt("InitServer", 0);

		String imageByteString=sharedPreferences.getString("UserPhoto","");
		byte[] imageAsBytes = Base64.decode(imageByteString.getBytes(),Base64.DEFAULT);
		Bitmap bitmap= SharedData.getPhoto(imageAsBytes);

		User user=new User(name, password, serverId, initServer, user_id);
		user.setPrimaryKey(primaryKey);
		user.setUserPhoto(bitmap);

		return user;

	}
	public String getRegistrationId(){
		String registrationId = sharedPreferences.getString(DinamoConstants.PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		int registeredVersion = sharedPreferences.getInt(DinamoConstants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = SharedData.getInstance().getAppVersion();
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}
	public void storeRegistrationId(String regId){
		int appVersion = SharedData.getInstance().getAppVersion();
		editor.putString(DinamoConstants.PROPERTY_REG_ID, regId);
		editor.putInt(DinamoConstants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();	
	}
	public void saveAppVersion(){

	}

}
