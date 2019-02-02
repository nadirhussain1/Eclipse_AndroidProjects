package com.mobive.bean;

public class Location {
    
	String smart_locality="";
    
    String locality="";
    
    double lon=0.0f;
    
    String phone="";
    
    String state="";
    
    String address="";
    
    double lat=0.0f;
    
    long id= 0;
    
    String zip_code="";

	public String getSmart_locality() {
		return smart_locality;
	}

	public void setSmart_locality(String smart_locality) {
		this.smart_locality = smart_locality;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

}
