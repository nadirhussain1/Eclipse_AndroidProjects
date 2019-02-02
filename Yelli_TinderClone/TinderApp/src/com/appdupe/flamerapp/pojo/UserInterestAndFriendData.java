package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserInterestAndFriendData 
{
	@SerializedName("data")
   ArrayList<UserInterestAndFriendQueryData>datalist;

	public ArrayList<UserInterestAndFriendQueryData> getDatalist() {
		return datalist;
	}

	public void setDatalist(ArrayList<UserInterestAndFriendQueryData> datalist) {
		this.datalist = datalist;
	}
	
}
