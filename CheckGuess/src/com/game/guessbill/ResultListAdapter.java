package com.game.guessbill;

import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.guessbill.data.CustomComparator;
import com.game.guessbill.data.GlobalDataManager;
import com.google.android.gms.common.images.ImageManager;

public class ResultListAdapter extends BaseAdapter {
	private boolean isFinalResultScreen=false;
	private int currentInputDigit=0;
	ImageManager imageManager;

	public ResultListAdapter(boolean isFinal){
		isFinalResultScreen=isFinal;
		imageManager=ImageManager.create(GlobalDataManager.getInstance().context);
	}
	private void computeDifference(){

		for(int count=0;count<GlobalDataManager.getInstance().playersList.size();count++){
			Double amount=GlobalDataManager.getInstance().playersList.get(count).getAmount();
			int totalLength=amount.toString().length();
			if(totalLength-(currentInputDigit+1)<0){
				continue;
			}
			String revealedString=amount.toString().substring(totalLength-(currentInputDigit+1));
			GlobalDataManager.getInstance().playersList.get(count).revealedAmount=Double.valueOf(revealedString);
			Double computeDiff=GlobalDataManager.getInstance().finalBillamount-Double.valueOf(revealedString);
			if(computeDiff<0){
				computeDiff*=-1;
			}
			computeDiff=GlobalDataManager.getInstance().round(computeDiff, 2);
			GlobalDataManager.getInstance().playersList.get(count).difference=computeDiff;
		}
		Collections.sort(GlobalDataManager.getInstance().playersList, new CustomComparator());
	}
	
	public void isFinalScreen(boolean flag){
		isFinalResultScreen=flag;
	}
	public void setCurrentInputDigit(int digit){
		currentInputDigit=digit;
	}
	@Override
	public int getCount() {
		if(isFinalResultScreen){
			computeDifference();
		}
		return GlobalDataManager.getInstance().playersList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)GlobalDataManager.getInstance().context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.preview_player_row, null, false);
			ScalingUtility.getInstance((Activity)GlobalDataManager.getInstance().context).scaleView(convertView);
		}

		TextView nameText=(TextView)convertView.findViewById(R.id.playername);
		TextView inputGuess=(TextView)convertView.findViewById(R.id.inputText);
		TextView differenceText=(TextView)convertView.findViewById(R.id.diffText);
		RelativeLayout finalInputDiffLayout=(RelativeLayout)convertView.findViewById(R.id.finalInputDiffLayout);
		View itemBackground=convertView.findViewById(R.id.scalingLayout);
		ProgressBar waitbar=(ProgressBar)convertView.findViewById(R.id.waitBar);
		TextView readyWaitText=(TextView)convertView.findViewById(R.id.readyWaitText);
		RelativeLayout readyWaitLayout=(RelativeLayout)convertView.findViewById(R.id.waitReadyLayout);

		if((GlobalDataManager.getInstance().playersList.get(position).getGooglePlusId().equalsIgnoreCase("") || GlobalDataManager.getInstance().playersList.get(position).getReady())
				||!GlobalDataManager.getInstance().isRemoteGame){
			itemBackground.setBackgroundColor(Color.parseColor("#3F9D2F"));
			waitbar.setVisibility(View.GONE);
			readyWaitText.setText("Ready");
		}else{
			itemBackground.setBackgroundColor(Color.parseColor("#F48500"));
			waitbar.setVisibility(View.VISIBLE);
			readyWaitText.setText("Waiting");
		}
		if(isFinalResultScreen){
			readyWaitLayout.setVisibility(View.GONE);
			finalInputDiffLayout.setVisibility(View.VISIBLE);
			if(position==0){
				itemBackground.setBackgroundColor(Color.parseColor("#FF0000"));
			}
			String inputAmount=GlobalDataManager.getInstance().appendZerosFormatDouble(GlobalDataManager.getInstance().playersList.get(position).revealedAmount);
			String diffAmount=GlobalDataManager.getInstance().appendZerosFormatDouble(GlobalDataManager.getInstance().playersList.get(position).difference);
			inputGuess.setText(inputAmount);
			differenceText.setText(diffAmount);
		}else{
			readyWaitLayout.setVisibility(View.VISIBLE);
			finalInputDiffLayout.setVisibility(View.GONE);

		}      
		String nameValue=GlobalDataManager.getInstance().playersList.get(position).getName();
		if(nameValue!=null && nameValue.equalsIgnoreCase("")){
			nameValue="Waiting";
		}
		if(GlobalDataManager.getInstance().playersList.get(position).getAddedVia()!=null && !GlobalDataManager.getInstance().playersList.get(position).getAddedVia().equalsIgnoreCase("")){
			nameValue=nameValue+" via "+GlobalDataManager.getInstance().playersList.get(position).getAddedVia();
		}
		nameText.setText(nameValue);
		
		ImageView playerPic=(ImageView)convertView.findViewById(R.id.playerPic);
		if(GlobalDataManager.getInstance().playersList.get(position).getIconUri()!=null){
			imageManager.loadImage(playerPic,GlobalDataManager.getInstance().playersList.get(position).getIconUri());
		}else{
			playerPic.setImageDrawable(null);
			playerPic.setBackgroundColor(Color.parseColor("#000000"));
		}
		TextView adminText=(TextView)convertView.findViewById(R.id.admintext);
		if(GlobalDataManager.getInstance().isRemoteGame && GlobalDataManager.getInstance().playersList.get(position).getGooglePlusId().equalsIgnoreCase(GlobalDataManager.getInstance().adminId)){
			adminText.setText("Admin");
			adminText.setVisibility(View.VISIBLE);
		}else{
			adminText.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	

}
