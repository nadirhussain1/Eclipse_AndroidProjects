package com.game.model;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.game.flappybird.R;


public class SoundManager {
	private static SoundManager soundManager=null;
	private SoundPool soundPool=null;
	private HashMap<Integer, Integer> soundPoolMap=null;
	private int soundPoolsLoadedSounds=0;
	Context mContext=null;

	public static SoundManager getInstance(){
		if(soundManager==null){
			soundManager=new SoundManager(SharedData.getInstance().mContext);
		}
		return soundManager;
	}
	private  SoundManager(Context context){
		mContext=context;
		initSoundPool(context);
	}
	private void initSoundPool(Context context){
		soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(soundLoadCompleteListener);
		soundPoolMap = new HashMap<Integer, Integer>();	
		soundPoolMap.put(R.raw.dead, soundPool.load(context,R.raw.dead, 1));
		soundPoolMap.put(R.raw.bird_flap, soundPool.load(context,R.raw.bird_flap, 1));
		soundPoolMap.put(R.raw.fall, soundPool.load(context,R.raw.fall, 1));
		soundPoolMap.put(R.raw.score, soundPool.load(context,R.raw.score, 1));
	}


	public  void playSound(int soundID) {

		if(soundPoolsLoadedSounds==4){
			soundPool.play(soundPoolMap.get(soundID),1.0f,1.0f, 1, 0, 1f);
		}
	}

	public void killSoundManager(){
//		//soundPool.release();
//		//soundPool=null;
//		soundPoolMap.clear();
//		soundManager=null;
	}
	OnLoadCompleteListener soundLoadCompleteListener=new OnLoadCompleteListener() {

		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

			soundPoolsLoadedSounds++;
		}
	};



}
