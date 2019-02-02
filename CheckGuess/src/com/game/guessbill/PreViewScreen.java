package com.game.guessbill;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.guessbill.data.GlobalDataManager;

public class PreViewScreen {
	RelativeLayout adminLayout=null;
	ResultListAdapter adapter=null;
	TextView finalText=null;
	EditText finalBillEditor=null;
	Button resetButton=null;
	Button backButton=null;
	View backButtonborder=null;
	ProgressBar finalProgressbar=null;
	private int maxInputLength=0;
	private int currentInputDigit=0;
	
	public PreViewScreen(View view){
		ListView finalList=(ListView)view.findViewById(R.id.previewPlayersList);
		finalList.setAdapter(null);
		adapter=new ResultListAdapter(false);
		finalList.setAdapter(adapter);
		
		adminLayout=(RelativeLayout)view.findViewById(R.id.adminLayout);
		finalText=(TextView)view.findViewById(R.id.finalBillText);
		finalBillEditor=(EditText)view.findViewById(R.id.finalBillEditText);
		resetButton=(Button)view.findViewById(R.id.resetButton);
		backButton=(Button)view.findViewById(R.id.previewBackButton);
		backButtonborder=view.findViewById(R.id.backbarborder);
		finalProgressbar=(ProgressBar)view.findViewById(R.id.finalProgressBar);
		backButton.setVisibility(View.VISIBLE);
		backButtonborder.setVisibility(View.VISIBLE);
		finalProgressbar.setVisibility(View.GONE);

		finalBillEditor.setText("");
		finalText.setText("");
		resetButton.setVisibility(View.GONE);

		adminLayout.setVisibility(View.VISIBLE);
		finalText.setVisibility(View.GONE);
		if(!GlobalDataManager.getInstance().isAdmin && GlobalDataManager.getInstance().isRemoteGame){
			adminLayout.setVisibility(View.GONE);
			finalText.setVisibility(View.VISIBLE);		
		}
		view.setVisibility(View.VISIBLE);
		
		GlobalDataManager.getInstance().currentScreen=R.id.previewScreen;
	}
	
	public boolean seeWhoPayClick(){
		String finalInput=finalBillEditor.getText().toString();
		if(finalInput.length()<1 || finalInput.equalsIgnoreCase(" ")){
			Toast.makeText(GlobalDataManager.getInstance().context,"Enter Valid Input", Toast.LENGTH_SHORT).show();
			return false;
		}else{
			GlobalDataManager.getInstance().finalBillamount=Double.valueOf(finalInput);
			nonAdminSeeWhoPay();
			return true;
		}
	}
	public void nonAdminSeeWhoPay(){
		backButton.setVisibility(View.GONE);
		backButtonborder.setVisibility(View.GONE);
		
		adminLayout.setVisibility(View.GONE);	
		String finalAmount=GlobalDataManager.getInstance().appendZerosFormatDouble(GlobalDataManager.getInstance().finalBillamount);
		finalText.setText("Final Bill Amount:"+finalAmount);
		finalText.setVisibility(View.VISIBLE);
	    calculateMaxInputLength();
	    
		adapter.isFinalScreen(true);
		adapter.notifyDataSetChanged();
		resetButton.setVisibility(View.VISIBLE);
		
		if(currentInputDigit<(maxInputLength-1)){
			finalProgressbar.setVisibility(View.VISIBLE);
			RevealCounter counter=new RevealCounter(5000,1000);
			counter.start();
			
		}
		
	}
	private void calculateMaxInputLength(){
		for(int count=0;count<GlobalDataManager.getInstance().playersList.size();count++){
			Double amount=GlobalDataManager.getInstance().playersList.get(count).getAmount();
			int totalLength=amount.toString().length();
			if(maxInputLength<totalLength)
			{
				maxInputLength=totalLength;
			}
		}
	}
	private class RevealCounter extends CountDownTimer{
      
		public RevealCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			currentInputDigit++;
			adapter.setCurrentInputDigit(currentInputDigit);
			adapter.notifyDataSetChanged();
			
			if(currentInputDigit<(maxInputLength-1)){
				RevealCounter counter=new RevealCounter(2000,1000);
				counter.start();		
			}else{
				finalProgressbar.setVisibility(View.GONE);
			}
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
			
		}
		
	}
	

}
