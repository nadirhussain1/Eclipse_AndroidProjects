package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class YelliEducationData {

	
	@SerializedName("degree")
	String degree;

	@SerializedName("institution")
	String institution;
	@SerializedName("type")
	String type;
	
	@SerializedName("graduation")
	String graduation;
	
	
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGraduation() {
		return graduation;
	}
	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}
	
	
	
}
