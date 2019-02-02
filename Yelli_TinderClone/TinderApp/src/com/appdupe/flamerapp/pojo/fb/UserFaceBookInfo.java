package com.appdupe.flamerapp.pojo.fb;

import java.util.ArrayList;
import java.util.List;

import com.appdupe.flamerapp.pojo.AgeRange;
import com.appdupe.flamerapp.pojo.Hometown;
import com.appdupe.flamerapp.pojo.Location;
import com.appdupe.flamerapp.pojo.UserLikes;
import com.appdupe.flamerapp.pojo.fb.education.FacebookEducationType;
import com.appdupe.flamerapp.pojo.fb.work.FacebookWorkType;
import com.google.gson.annotations.SerializedName;

public class UserFaceBookInfo {
	@SerializedName("likes")
	private UserLikes userLikes;

	@SerializedName("education")
	private List<FacebookEducationType> education = new ArrayList<FacebookEducationType>();

	@SerializedName("work")
	private List<FacebookWorkType> work;

	@SerializedName("hometown")
	private Hometown homeTown;

	@SerializedName("first_name")
	private String firstName;
	
	@SerializedName("last_name")
	private String lastName;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("id")
	private long faceBookId;
	
	@SerializedName("relationship_status")
	private String relationship_status;
	
	@SerializedName("birthday")
	private String birthday;
	
	@SerializedName("bio")
	private String bio;
	
	@SerializedName("gender")
	private String gender;
	
	@SerializedName("name")
	private String name;

	@SerializedName("location")
	private Location location;

	@SerializedName("interested_in")
	private String[] interestedIn;
	
	@SerializedName("age_range")
	private AgeRange ageRange;

	public String getFirstName() {
		return firstName;
	}

	public List<FacebookEducationType> getEducation() {
		return education;
	}

	public List<FacebookWorkType> getWork() {
		return work;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AgeRange getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(AgeRange ageRange) {
		this.ageRange = ageRange;
	}

	public String[] getInterestedIn() {
		return interestedIn;
	}

	public void setInterestedIn(String[] interestedIn) {
		this.interestedIn = interestedIn;
	}

	public long getFaceBookId() {
		return faceBookId;
	}

	public void setFaceBookId(long faceBookId) {
		this.faceBookId = faceBookId;
	}

	public String getRelationship_status() {
		return relationship_status;
	}

	public void setRelationship_status(String relationship_status) {
		this.relationship_status = relationship_status;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Hometown getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(Hometown homeTown) {
		this.homeTown = homeTown;
	}

	public UserLikes getUrserLikes() {
		return userLikes;
	}

	public void setUrserLikes(UserLikes urserLikes) {
		this.userLikes = urserLikes;
	}

	@SerializedName("religion")
	private String religion;

	public String getReligion() {
		return religion;
	}

	public void setRelgion(String religion) {
		this.religion = religion;
	}

}
