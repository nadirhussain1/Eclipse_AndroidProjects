package com.mobive.bean;

import java.util.ArrayList;

public class User {
	int id=0;
	String name="";
	String email="";
	boolean enabled=false;
	String city="";
	ArrayList<String> keywords=new ArrayList<String>();
	ArrayList<String> deals=new ArrayList<String>();
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public ArrayList<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	public ArrayList<String> getDeals() {
		return deals;
	}
	public void setDeals(ArrayList<String> deals) {
		this.deals = deals;
	}

}
