package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FacebookAlbumFQLResultData 
{
	
	@SerializedName("fql_result_set1")
    private ArrayList<FQLFirstSet> imgList;
	@SerializedName("fql_result_set")
    private ArrayList<FQLSecondResult> albumList;
	public ArrayList<FQLFirstSet> getImageList() {
		return imgList;
	}
	public void setImageList(ArrayList<FQLFirstSet> pickList) 
	{
		this.imgList = pickList;
	}
	public ArrayList<FQLSecondResult> getAlbumList() 
	{
		return albumList;
	}
	public void setAlbumList(ArrayList<FQLSecondResult> alubumnamList) 
	{
		this.albumList = alubumnamList;
	}
    
}
