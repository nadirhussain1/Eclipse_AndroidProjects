package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserFacebookLikeData 
{
	 @SerializedName("data")
   private ArrayList<UserFacebookLikeId>likeid;

public ArrayList<UserFacebookLikeId> getLikeid() {
	return likeid;
}

public void setLikeid(ArrayList<UserFacebookLikeId> likeid) {
	this.likeid = likeid;
}
   
}
