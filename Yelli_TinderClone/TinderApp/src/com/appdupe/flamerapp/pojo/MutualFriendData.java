package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class MutualFriendData {
	
	@SerializedName("firstName")
	String firstName;
	
	@SerializedName("lastName")
	String lastName;
	@SerializedName("picUrl")
	String picUrl;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
