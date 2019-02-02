package com.mobive.bean;

import java.util.ArrayList;

public class Deal {

	String yipit_url = "";

	String end_date = "";

	String title = "";

	String url = "";

	String yipit_title = "";

	String mobile_url = "";

	String date_added = "";

	int active = 0;

	long id = 0;

	String description = "";
	
	Business business=null;
	
	Division division=null;
	
	ArrayList<Tag> tags=null;
	
	Price price=null;
	
	Price value=null;
	
	Price discount=null;
	
	Source source=null;
	
	Images images=null;
	



	public String getYipit_url() {
		return yipit_url;
	}

	public void setYipit_url(String yipit_url) {
		this.yipit_url = yipit_url;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getYipit_title() {
		return yipit_title;
	}

	public void setYipit_title(String yipit_title) {
		this.yipit_title = yipit_title;
	}

	public String getMobile_url() {
		return mobile_url;
	}

	public void setMobile_url(String mobile_url) {
		this.mobile_url = mobile_url;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Price getValue() {
		return value;
	}

	public void setValue(Price value) {
		this.value = value;
	}

	public Price getDiscount() {
		return discount;
	}

	public void setDiscount(Price discount) {
		this.discount = discount;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}
	
}
