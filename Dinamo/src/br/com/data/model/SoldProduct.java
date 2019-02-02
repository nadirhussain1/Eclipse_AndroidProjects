package br.com.data.model;

public class SoldProduct extends Product {

	
	private String quantity;
	private DinamoObject product;
	private DinamoObject payMethod;
	private long primaryKey;
	
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public DinamoObject getProduct() {
		return product;
	}
	public void setProduct(DinamoObject name) {
		this.product = name;
	}
	public DinamoObject getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(DinamoObject payMethod) {
		this.payMethod = payMethod;
	}

}
