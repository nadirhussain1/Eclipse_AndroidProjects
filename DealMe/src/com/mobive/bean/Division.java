package com.mobive.bean;

public class Division {
	String name="";
    
    String url="";
    
    String country="";
    
    double lon=0.0f;
    
    double lat=0.0f;
    
    int active=0;
    
    int time_zone_diff=0;
    
    String slug="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getTime_zone_diff() {
		return time_zone_diff;
	}

	public void setTime_zone_diff(int time_zone_diff) {
		this.time_zone_diff = time_zone_diff;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
    
}
