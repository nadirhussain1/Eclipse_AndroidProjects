package com.humby.model;

public class CountryCurrencyRate {

	private String countryCode;
	private String countryName;
	private Double rate;

	public CountryCurrencyRate(String countryCode, String countryName, Double rate){
		this.setCountryCode(countryCode);
		this.setCountryName(countryName);
		this.setRate(rate);
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String toString()
	{
		return countryName;
	}


}
