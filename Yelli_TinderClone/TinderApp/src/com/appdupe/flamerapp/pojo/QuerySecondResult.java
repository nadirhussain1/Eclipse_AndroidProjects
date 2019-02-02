package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class QuerySecondResult 
{
	@SerializedName("name")
	private String friendName;
	@SerializedName("pic_square")
	private String friendPicUlt;
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendPicUlt() {
		return friendPicUlt;
	}
	public void setFriendPicUlt(String friendPicUlt) {
		this.friendPicUlt = friendPicUlt;
	}
}
