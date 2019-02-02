package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class MatchesData 
{
	@SerializedName("imgCnt")
	private int  imgCnt;
	private Bitmap mBitmap;
	
	@SerializedName("firstName")
    private String firstName;
	@SerializedName("fbId")
    private String fbId;
	@SerializedName("pPic")
    private String pPic;
	@SerializedName("sex")
    private int  sex;
    @SerializedName("persDesc")
    private String persDesc;
    @SerializedName("age")
    private int age;
	@SerializedName("latitude")
    private String latitude;
	@SerializedName("longitude")
    private String longitude;
	
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
	
	@SerializedName("imageCount")
	private String imageCount;
	
	
	
	public String getImageCount() {
		return imageCount;
	}

	public void setImageCount(String imageCount) {
		this.imageCount = imageCount;
	}
	
	@SerializedName("sharedFriends")
	private String sharedFriends;
	
	
	
	public String getSharedFriends() {
		return sharedFriends;
	}

	public void setSharedFriends(String sharedFriends) {
		this.sharedFriends = sharedFriends;
	}
	
	@SerializedName("sharedLikes")
	private String sharedLikes;
	
	
	
	public String getSharedLikes() {
		return sharedLikes;
	}

	public void setSharedLikes(String sharedLikes) {
		this.sharedLikes = sharedLikes;
	}
	

	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getpPic() {
		return pPic;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPersDesc() {
		return persDesc;
	}
	public void setPersDesc(String persDesc) {
		this.persDesc = persDesc;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
			
}
