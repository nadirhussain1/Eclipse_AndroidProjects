package com.game.flappybird;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.game.model.FlappyConstants;
import com.game.model.SharedData;
import com.game.utilities.GamePreferences;
import com.game.utilities.ScalingUtility;

public class DiffSettings {
	Dialog diffDialog=null;
	Context mContext=null;

	public DiffSettings(Context context){

		mContext=context;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.diff_layout, null, false);
		ScalingUtility.getInstance((Activity)mContext).scaleView(view);

		diffDialog=new Dialog(mContext,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		diffDialog.setContentView(view);

		Button button=(Button)view.findViewById(R.id.NormalDiffButton);
		button.setOnClickListener(diffClickListener);
		button=(Button)view.findViewById(R.id.HardDiffButton);
		button.setOnClickListener(diffClickListener);
	}
	public void showDialog(){
		if(diffDialog!=null){
			diffDialog.show();
		}
	}
	OnClickListener diffClickListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {

			if(clickedView.getId()==R.id.NormalDiffButton){
				GamePreferences.getInstance().saveGameDiffLevel(FlappyConstants.NORMAL_MODE);

			}else{
				GamePreferences.getInstance().saveGameDiffLevel(FlappyConstants.HARD_MODE);

			}
			diffDialog.cancel();
		}
	};
}
