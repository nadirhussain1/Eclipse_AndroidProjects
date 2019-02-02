package com.game.guessbill.data;

import android.net.Uri;

public class PlayerModel {
     private String name;
     private String addViaName; 
     private Double guessAmount;
     public Double revealedAmount;
     public Double difference;
     private String remoteParticipantId;
     private boolean isReady;
     private Uri  iconImageUri=null;
     
     public PlayerModel(String paticipantId,String name,String addedVia,Double inputAmount,Double diff,boolean isReady){
    	 remoteParticipantId=paticipantId;
    	 this.name=name;
    	 addViaName=addedVia;
    	 guessAmount=inputAmount;
    	 difference=diff;
    	 this.isReady=isReady;
     }
     public void setViaName(String name){
    	 addViaName=name; 
     }
     public void setName(String name){
    	 this.name=name; 
     }
     public void setGuessAmount(Double amount){
    	 this.guessAmount=amount; 
     }
     public void setGuessDifference(Double diff){
    	 this.difference=diff; 
     }
     public void setReadyFlag(boolean ready){
    	 isReady=ready; 
     }
     public String getGooglePlusId(){
    	return remoteParticipantId; 
     }
     public String getName(){
    	 return name;
     }
     public String getAddedVia(){
    	 return addViaName;
     }
     public boolean getReady(){
    	 return isReady;
     }
     public Double getAmount(){
    	 return guessAmount;
     }
     public Uri getIconUri(){
    	 return iconImageUri;
     }
     public void setIconUri(Uri imageUri){
    	 iconImageUri=imageUri;
     }
}
