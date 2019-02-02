package com.appdupe.flamerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class AgeRange 
{
	@SerializedName("max")
	private int max;
	@SerializedName("min")
	private int min;
	
	public int getMax() 
	{
		return max;
	}
	public void setMax(int max) 
	{
		this.max = max;
	}
	public int getMin() 
	{
		return min;
	}
	public void setMin(int min) 
	{
		this.min = min;
	}
}
