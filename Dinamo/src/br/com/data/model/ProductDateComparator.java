package br.com.data.model;

import java.util.Comparator;

public class ProductDateComparator implements Comparator<Product> {

	@Override
	public int compare(Product productOne, Product productTwo) {
		if (productOne.getDate().after(productTwo.getDate())){
			return -1;
		}
		else if (productOne.getDate().before(productTwo.getDate())){
			return 1;
		}
		return 0;
	}

}
