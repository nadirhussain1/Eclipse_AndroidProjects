package com.mobive.dealme.deal_items;

public class SectionItem implements Item{

	private final String title;
	private final String slug;
	
	public SectionItem(String title,String slug) {
		this.title = title;
		this.slug = slug;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

	@Override
	public String getSlug() {
		// TODO Auto-generated method stub
		return slug;
	}

}
