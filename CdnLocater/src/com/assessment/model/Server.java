package com.assessment.model;

public class Server {
	private String address;
	private String timeZone;
	private String network;
	private LocationPoint location;

	public Server(String address,String timeZone,String network){
		this.address=address;
		this.timeZone=timeZone;
		this.network=network;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocationPoint getLocation() {
		return location;
	}
	public void setLocation(LocationPoint location) {
		this.location = location;
	}
}
