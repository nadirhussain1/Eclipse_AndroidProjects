package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserProfileImages 
{ 
	@SerializedName("data")
    private ArrayList<ImageURL> data;

	public ArrayList<ImageURL> getData() {
		return data;
	}

	public void setData(ArrayList<ImageURL> data) {
		this.data = data;
	}

	
	
	
}
