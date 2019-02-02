package br.com.data.model;

import java.util.Date;

public class Product {
	private double priceValue;
	private Date  date;
	private String id;
	private long primaryKey;
	public  boolean isSynchronized;
	public  boolean isDeleted;

	public Product(){
		id="";
		isSynchronized=false;
		isDeleted=false;
		date=null;
	}
	
	public double getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(double priceValue) {
		this.priceValue = priceValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	
}
