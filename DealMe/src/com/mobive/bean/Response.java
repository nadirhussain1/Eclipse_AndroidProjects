package com.mobive.bean;

public class Response {
Payload payload=new Payload();
MetaData metaData=new MetaData();
public Payload getPayload() {
	return payload;
}
public void setPayload(Payload payload) {
	this.payload = payload;
}
public MetaData getMetaData() {
	return metaData;
}
public void setMetaData(MetaData metaData) {
	this.metaData = metaData;
}
}
