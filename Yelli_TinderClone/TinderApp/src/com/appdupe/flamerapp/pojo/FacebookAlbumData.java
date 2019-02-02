package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FacebookAlbumData 
{
	@SerializedName("data")
    private ArrayList<FacebookAlbumSource> alubumScrsList;

	public ArrayList<FacebookAlbumSource> getAlubumScrsList() {
		return alubumScrsList;
	}

	public void setAlubumScrsList(ArrayList<FacebookAlbumSource> alubumScrsList) {
		this.alubumScrsList = alubumScrsList;
	}
	
}
