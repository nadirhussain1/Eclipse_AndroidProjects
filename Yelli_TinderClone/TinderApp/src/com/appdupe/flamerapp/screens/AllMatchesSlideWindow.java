package com.appdupe.flamerapp.screens;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.slidingmenuexample.MainActivity;
import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.adapters.MatchedDataAdapter;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.embed.anddroidpushntificationdemo11.ChatActivity;

public class AllMatchesSlideWindow {
	private ListView matcheslistview=null;
	public  static List<LikeMatcheddataForListview> matchesList=null;
	public  static MatchedDataAdapter matchesAdapter=null;
	private static int selectedMatchIndex=0;
	private Context mContext=null;
	private View rightMenuRootView=null;

	public AllMatchesSlideWindow(Context context,List<LikeMatcheddataForListview> listdata,View rootView){
		matchesList=listdata;
		mContext=context;
		rightMenuRootView=rootView;

		matcheslistview=(ListView)rightMenuRootView.findViewById(R.id.menu_right_ListView);	
		matchesAdapter=new MatchedDataAdapter(context, matchesList);
		matcheslistview.setAdapter(matchesAdapter);
		matcheslistview.setOnItemClickListener(matchesItemClickListener);
	}
	public void showScreen(){
		rightMenuRootView.setVisibility(View.VISIBLE);
		rightMenuRootView.startAnimation(outToLeftAnimation());

	}
	public void hideScreen(){
		rightMenuRootView.startAnimation(inFromLeftAnimation());
		rightMenuRootView.setVisibility(View.GONE);
		matchesAdapter=null;
		MainActivity.allMatchesWindow=null;
	}
	public static void refreshMatchesList(){
		if(matchesAdapter!=null){
			matchesList.remove(selectedMatchIndex);
			matchesAdapter.notifyDataSetChanged();
		}
	}
	private OnItemClickListener matchesItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
			selectedMatchIndex=position;
			LikeMatcheddataForListview matcheddataForListview=(LikeMatcheddataForListview)arg0.getItemAtPosition(position);

			String faceboolid=matcheddataForListview.getFacebookid();
			Bundle mBundle=new Bundle();
			mBundle.putString(CommonConstant.FRIENDFACEBOOKID, faceboolid);
			mBundle.putString(CommonConstant.CHECK_FOR_PUSH_OR_NOT, "1");

			Intent mIntent=new Intent(mContext,ChatActivity.class);
			mIntent.putExtras(mBundle);
			mContext.startActivity(mIntent);
			hideScreen();

		}
	};
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  1.0f,Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
}
