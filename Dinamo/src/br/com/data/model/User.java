package br.com.data.model;

import android.graphics.Bitmap;

public class User {
	private String userName;
	private String userId;
	private String serverId;
	private String password;
	private long primaryKey;
	private Bitmap userPhoto;
	public  int    isInitFromServer;

	public User(String name,String pass,String server_id,int initServer,String user_id){
		userName=name;
		userId=user_id;
		password=pass;
		serverId=server_id;
		isInitFromServer=initServer;
		primaryKey=-1;
		userPhoto=null;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public Bitmap getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(Bitmap userPhoto) {
		this.userPhoto = userPhoto;
	}

}
