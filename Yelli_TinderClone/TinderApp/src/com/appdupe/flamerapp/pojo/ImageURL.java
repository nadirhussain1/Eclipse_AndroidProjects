package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class ImageURL {
   @SerializedName("src_big")
	  private String src;

public String getSrc() {
	return src;
}

public void setSrc(String src) {
	this.src = src;
}
}
