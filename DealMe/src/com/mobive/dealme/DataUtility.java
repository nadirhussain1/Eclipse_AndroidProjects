package com.mobive.dealme;

import java.util.ArrayList;

import android.content.Context;

import com.mobive.bean.User;
import com.mobive.dealme.deal_items.Item;

public class DataUtility {
	private static Context mContext;
	public static String selectedCitySlug=null;
	public static boolean shouldLoadNextDeals=false;
	public static String selectedDealSlug="";
	private static User currentUser=null;
	public static short lastVisitedActivity=0;
	private static ArrayList<Item> items = new ArrayList<Item>();
	public static MyDealsSelectorView selector=null;
	
	public static void setContext(Context context){
		mContext=context;
	}
	public static Context getContext(){
		return mContext;
	}
	public static void setSelectedCitySlug(String selected){
		selectedCitySlug=selected;
	}
	public static boolean isValidEmail(String email){
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	public static void setUser(User user){
		currentUser=user;
	}
	public static User getUser(){
		return currentUser;
	}
	public static ArrayList<Item> getItems(){
		return items;
	}
	public static void setItems(ArrayList<Item> nitems){
		items=nitems;
	}

}
