package br.com.data.model;

import android.graphics.Bitmap;


public class BoughtProduct extends Product{

	private DinamoObject establishment;
	private DinamoObject payMethod;
	private String notes;
	private Bitmap productPhoto;
	
	public BoughtProduct(){
		productPhoto=null;
	}
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isSynchronized() {
		return isSynchronized;
	}
	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	public Bitmap getProductPhoto() {
		return productPhoto;
	}
	public void setProductPhoto(Bitmap productPhoto) {
		this.productPhoto = productPhoto;
	}
	public DinamoObject getEstablishment() {
		return establishment;
	}
	public void setEstablishment(DinamoObject establishment) {
		this.establishment = establishment;
	}
	public DinamoObject getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(DinamoObject payMethod) {
		this.payMethod = payMethod;
	}
}
