package com.appdupe.flamerapp.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserInterestAndFriendQueryData 
{
	  
	    @SerializedName("fql_result_set1")
      private ArrayList<QueryOneResult>interestList;
	    @SerializedName("fql_result_set")
      private ArrayList<QuerySecondResult>FriendList;
		public ArrayList<QueryOneResult> getInterestList() {
			return interestList;
		}
		public void setInterestList(ArrayList<QueryOneResult> interestList) {
			this.interestList = interestList;
		}
		public ArrayList<QuerySecondResult> getFriendList() {
			return FriendList;
		}
		public void setFriendList(ArrayList<QuerySecondResult> friendList) {
			FriendList = friendList;
		}
	    
}
