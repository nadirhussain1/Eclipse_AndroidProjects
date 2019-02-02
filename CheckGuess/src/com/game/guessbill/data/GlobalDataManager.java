package com.game.guessbill.data;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;

import com.game.guessbill.R;
import com.google.android.gms.games.Player;

public class GlobalDataManager {
	private static GlobalDataManager globalDataManager=null;
	public   ArrayList<PlayerModel> playersList=null;
	public ArrayList<Player> invitees;
	
	public Context context;
	public boolean isRemoteGame=false;
	public boolean isAdmin=true;
	public double amount;
	public String userName;
	public double finalBillamount;
	public String searchPlayer="";
	public String adminId="";
	public int currentScreen=-1;
	
	private GlobalDataManager(){
		playersList=new ArrayList<PlayerModel>();
		invitees=new ArrayList<Player>();
	}
	public static GlobalDataManager getInstance(){
		if(globalDataManager==null){
			globalDataManager=new GlobalDataManager();
		}
		return globalDataManager;
	}
	public void reset(){
		if(playersList!=null){
			playersList.clear();
		}
		if(invitees!=null){
			invitees.clear();
		}
		finalBillamount=0.0;
		isRemoteGame=false;
		isAdmin=true;
		amount=0.0;
		userName="";
		searchPlayer="";
		adminId="";
		
	}
	public  double round(double value, int precision) {
		BigDecimal bd = new BigDecimal(value);
	    BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
	    return rounded.doubleValue();
	}
	public String appendZerosFormatDouble(Double number){
		String numString=String.valueOf(number);
		int dotIndex=numString.indexOf('.');
		if((dotIndex+2)==numString.length()){
			numString+="0";
		}
		return numString;
	}
}
