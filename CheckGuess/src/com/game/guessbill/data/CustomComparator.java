package com.game.guessbill.data;

import java.util.Comparator;

public class CustomComparator implements Comparator<PlayerModel> {

	@Override
	public int compare(PlayerModel objOne, PlayerModel ObjTwo) {
		
		return objOne.difference.compareTo(ObjTwo.difference);
	}
   
}
