package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class QueryOneResult 
{

	@SerializedName("name")
	private String interestName;
	@SerializedName("pic_square")
	private String interestPicUlt;
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public String getInterestPicUlt() {
		return interestPicUlt;
	}
	public void setInterestPicUlt(String interestPicUlt) {
		this.interestPicUlt = interestPicUlt;
	}
	
	
	
	
}
