package com.mobive.dealme.deal_items;


public class EntryItem implements Item{

	public final String title;
	public final String slug;

	public String getTitle() {
		return title;
	}


	public EntryItem(String title,String slug) {
		this.title = title;
		this.slug=slug;
	}
	
	
	@Override
	public boolean isSection() {
		return false;
	}


	@Override
	public String getSlug() {
		// TODO Auto-generated method stub
		return slug;
	}

}
