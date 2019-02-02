package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class YelliWorkData {

	@SerializedName("position")
	String position;

	
	@SerializedName("company")
	String company;
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	


}
