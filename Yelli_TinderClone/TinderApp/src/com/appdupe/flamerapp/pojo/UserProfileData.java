package com.appdupe.flamerapp.pojo;


import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class UserProfileData 
{
	
	
	@SerializedName("distance")
	private double distance;
	 @SerializedName("errNum")
     private int errNum;
	 @SerializedName("errFlag")
	 private int errFlag;
	 @SerializedName("errMsg")
	 private String errMsg;
	 public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@SerializedName("education")
	private ArrayList<YelliEducationData>education;

	public ArrayList<YelliEducationData> getEducation() {
		return education;
	}

	public void setEducation(ArrayList<YelliEducationData> education) {
		this.education = education;
	}
	
	@SerializedName("work")
	private ArrayList<YelliWorkData>work;

	public ArrayList<YelliWorkData> getWork() {
		return work;
	}
	
	public void setWork(ArrayList<YelliWorkData> work) {
		this.work = work;
	}
	
	@SerializedName("mutualLikes")
	private ArrayList<MutualLikeData>mutualLikes;

	public ArrayList<MutualLikeData> getMutualLikes() {
		return mutualLikes;
	}
	
	@SerializedName("mutualFriends")
	private ArrayList<MutualFriendData>mutualFriends;

	public ArrayList<MutualFriendData> getMutualFriends() {
		return mutualFriends;
	}
	
	@SerializedName("imageCount")
	private String imageCount;
	
	
	
	public String getImageCount() {
		return imageCount;
	}

	public void setImageCount(String imageCount) {
		this.imageCount = imageCount;
	}


	@SerializedName("profilePic")
	 private String profilePic;
	 @SerializedName("age")
	 private int age;
	 @SerializedName("lastActive")
	 private String lastActive;
	 @SerializedName("persDesc")
	 private String persDesc;
	 @SerializedName("images")
	 private String [] images;
	 
	 @SerializedName("firstName")
	 private String firstName;
	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String[] getImages() 
	{
		return images;
	}


	public void setImages(String[] images) 
	{
		this.images = images;
	}


	public int getErrNum() {
		return errNum;
	}


	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}


	public int getErrFlag() {
		return errFlag;
	}


	public void setErrFlag(int errFlag) {
		this.errFlag = errFlag;
	}


	public String getErrMsg() {
		return errMsg;
	}


	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public String getProfilePic() {
		return profilePic;
	}


	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getLastActive() {
		return lastActive;
	}


	public void setLastActive(String lastActive) {
		this.lastActive = lastActive;
	}


	public String getPersDesc() {
		return persDesc;
	}


	public void setPersDesc(String persDesc) {
		this.persDesc = persDesc;
	}


	

	 
	    
	    
	   
	   
	   
	   
	    
	
	
}
