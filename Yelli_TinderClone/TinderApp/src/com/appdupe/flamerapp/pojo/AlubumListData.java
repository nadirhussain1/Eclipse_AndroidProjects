package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AlubumListData
{	
	@SerializedName("data")
    private  ArrayList<FacebookAlbumFQLResultData>  facebookArrayList;
	
	public ArrayList<FacebookAlbumFQLResultData> getFacebookArrayList() 
	{
		return facebookArrayList;
	}
	
	public void setFacebookArrayList(ArrayList<FacebookAlbumFQLResultData> facebookArrayList) 
	{
		this.facebookArrayList = facebookArrayList;
	}
}
