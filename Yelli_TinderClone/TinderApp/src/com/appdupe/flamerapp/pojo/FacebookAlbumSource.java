package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class FacebookAlbumSource
{
	@SerializedName("src_big")
   private String pickUrl;

public String getPickUrl() {
	return pickUrl;
}

public void setPickUrl(String pickUrl) {
	this.pickUrl = pickUrl;
}
   
}
