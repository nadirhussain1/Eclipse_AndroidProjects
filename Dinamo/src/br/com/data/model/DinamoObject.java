package br.com.data.model;

public class DinamoObject {
	private String id;
	private String name;
	private long primaryKey;
	public boolean isSynChronized;

	public DinamoObject(){
		id="";
		primaryKey=-1;
		isSynChronized=false;
	}
	public DinamoObject(String id,String name){
		this.id=id;
		this.name=name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

}
