package br.com.data.model;

import java.util.Date;

public class ExpenseProduct extends Product{
	
	DinamoObject category;
	DinamoObject periodicityType;
	private long primaryKey;
	private Date endDate;
	private Date nextDueDate;
    
	
	public ExpenseProduct(){
		endDate=null;	
	}
	public long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public DinamoObject getCategory() {
		return category;
	}
	public void setCategory(DinamoObject category) {
		this.category = category;
	}
	public DinamoObject getPeriodicityType() {
		return periodicityType;
	}
	public void setPeriodicityType(DinamoObject periodicityType) {
		this.periodicityType = periodicityType;
	}
	public Date getNextDueDate() {
		return nextDueDate;
	}
	public void setNextDueDate(Date nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

}
