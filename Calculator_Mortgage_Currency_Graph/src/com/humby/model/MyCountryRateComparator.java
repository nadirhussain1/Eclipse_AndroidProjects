package com.humby.model;

import java.util.Comparator;


public class MyCountryRateComparator implements Comparator<CountryCurrencyRate> {

	public int compare(CountryCurrencyRate leftValue, CountryCurrencyRate rightVaue) {
		return leftValue.getCountryName().compareToIgnoreCase(rightVaue.getCountryName());
	} 

}