package com.game.guessbill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.guessbill.data.GlobalDataManager;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;

public class SearchAdapter extends BaseAdapter {
	private Context mContext=null;
	ArrayList<Player>displayFriends=new ArrayList<Player>();
		
	View[]selectedPlayers=new View[4];
	PlayerBuffer allLoadedFriends;
	ImageManager imageManager;
	View friendsListView;

	public SearchAdapter(Context context,PlayerBuffer friends,View view){
		mContext=context;
		allLoadedFriends=friends;
		imageManager=ImageManager.create(context);
		friendsListView=view;

		selectedPlayers[0]=view.findViewById(R.id.firstPlayerLayout);
		selectedPlayers[1]=view.findViewById(R.id.secondPlayerLayout);
		selectedPlayers[2]=view.findViewById(R.id.thirdPlayerLayout);
		selectedPlayers[3]=view.findViewById(R.id.fourthPlayerLayout);

		for(int count=1;count<4;count++){
			selectedPlayers[count].findViewById(R.id.Cross).setOnClickListener(selectedPlayerDeleteListener);
			selectedPlayers[count].setVisibility(View.GONE);
		}
	}
	public void setAllLoadedFriends(PlayerBuffer buffer){
		allLoadedFriends=buffer;
	}
	private void  filterSearchPlayers(){
		displayFriends.clear();
		boolean alreadyInvited=false;
        String  searPlayer=GlobalDataManager.getInstance().searchPlayer;
        
        if(searPlayer.equalsIgnoreCase("") && GlobalDataManager.getInstance().invitees.size()==0){
        	for(int count=0;count<allLoadedFriends.getCount();count++){
				displayFriends.add(allLoadedFriends.get(count));
			}
        }
        else{
			for(int count=0;count<allLoadedFriends.getCount();count++){
				if(searPlayer.equalsIgnoreCase("") 	|| allLoadedFriends.get(count).getDisplayName().toLowerCase().startsWith(searPlayer.toLowerCase())){

					alreadyInvited=false;
					for(int players=0;players<GlobalDataManager.getInstance().invitees.size();players++){
						if(GlobalDataManager.getInstance().invitees.get(players).getPlayerId().equalsIgnoreCase(allLoadedFriends.get(count).getPlayerId())){
							alreadyInvited=true;
							break;
						}
					}

					if(!alreadyInvited){
							displayFriends.add(allLoadedFriends.get(count));
						
					}
					
				}
			}
		}
		
		sortPlayersList();

	}
	private void sortPlayersList(){
		Collections.sort(displayFriends, new Comparator<Player>() {
		    public int compare(Player playerOne, Player playertwo) {
		        return playerOne.getDisplayName().toLowerCase().compareTo(playertwo.getDisplayName().toLowerCase());
		    }
		});
	}
	@Override
	public int getCount() {
		filterSearchPlayers();
		return displayFriends.size();
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
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.invite_friend_row, null, false);
			ScalingUtility.getInstance((Activity)mContext).scaleView(convertView);
		}

		TextView nameText=(TextView)convertView.findViewById(R.id.name);
		ImageView pic=(ImageView)convertView.findViewById(R.id.playerPic);
		String name=displayFriends.get(position).getDisplayName();
		name=formatString(name);
		nameText.setText(name);
		
		CheckBox inviteCheckBox=(CheckBox)convertView.findViewById(R.id.inviteSelectBox);
		inviteCheckBox.setChecked(false);
		
		if(GlobalDataManager.getInstance().invitees.size()<3){
			inviteCheckBox.setOnClickListener(checkChangeListener);	
			inviteCheckBox.setTag(position);
		}else{
			inviteCheckBox.setEnabled(false);
			inviteCheckBox.setClickable(false);
		}
		imageManager.loadImage(pic, displayFriends.get(position).getIconImageUri());

		return convertView;
	}
	private String formatString(String name){
		if(name.length()<=18){
			return name;
		}
		int lastSpaceIndex=name.lastIndexOf(" ");
		String formattedString=name.substring(0,lastSpaceIndex)+"\n"+name.substring(lastSpaceIndex+1);
		return formattedString;
	}
	OnClickListener checkChangeListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position=(Integer)v.getTag();
			int index=GlobalDataManager.getInstance().invitees.size();
			TextView nameTextView=(TextView)selectedPlayers[index+1].findViewById(R.id.PlayerName);
			nameTextView.setText(displayFriends.get(position).getDisplayName());
			selectedPlayers[index+1].setVisibility(View.VISIBLE);
			GlobalDataManager.getInstance().invitees.add(displayFriends.get(position));
			notifyDataSetChanged();
		}
	};
	OnClickListener selectedPlayerDeleteListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			int tag=Integer.valueOf((String)v.getTag());
			selectedPlayers[tag].setVisibility(View.GONE);
			GlobalDataManager.getInstance().invitees.remove(tag-1);

			for(int count=0;count<GlobalDataManager.getInstance().invitees.size();count++){
				TextView nameTextView=(TextView)selectedPlayers[count+1].findViewById(R.id.PlayerName);
				nameTextView.setText(GlobalDataManager.getInstance().invitees.get(count).getDisplayName());
			}
			
           for(int count=(GlobalDataManager.getInstance().invitees.size()+1);count<selectedPlayers.length;count++){
            	selectedPlayers[count].setVisibility(View.GONE);
            }
            notifyDataSetChanged();
		}
	};
}
