package com.app.commands;


public class ATCommandOutput{
	
	private String ATcommandDsc = null;
	private String ATcommandKey = null;
	private String ATcontent = null;
	
	public ATCommandOutput(String paramString1, String paramString2, String paramString3){
		this.ATcontent = paramString1;
		this.ATcommandDsc = paramString3;
		this.ATcommandKey = paramString2;
	}

	public ATCommandOutput(ATCommandOutput paramATCommandOutput){
		new ATCommandOutput(paramATCommandOutput.ATcontent, paramATCommandOutput.ATcommandDsc, paramATCommandOutput.ATcommandKey);
	}

	public String getATcommandDsc(){
		return this.ATcommandDsc;
	}

	public String getATcommandKey(){
		return this.ATcommandKey;
	}

	public String getATcontent(){
		return this.ATcontent;
	}

	public void setATcommandDsc(String paramString){
		this.ATcommandDsc = paramString;
	}

	public void setATcommandKey(String paramString){
		this.ATcommandKey = paramString;
	}

	public void setATcontent(String paramString){
		synchronized (ATcontent) {
			ATcontent = paramString;	
		}
	}
	
}
