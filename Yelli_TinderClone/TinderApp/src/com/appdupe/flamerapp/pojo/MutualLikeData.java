package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class MutualLikeData {
	
	@SerializedName("likeName")
	String likeName;
	
	@SerializedName("picUrl")
	String picUrl;
	
	public String getLikeName() {
		return likeName;
	}
	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
