package br.com.data.model;

import java.util.Comparator;

public class ExpenseDateComparator implements Comparator<ExpenseProduct> {

	@Override
	public int compare(ExpenseProduct productOne, ExpenseProduct productTwo) {
		
		return productOne.getNextDueDate().compareTo(productTwo.getNextDueDate());
	}

}
