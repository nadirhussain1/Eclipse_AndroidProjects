package com.topitfree.hotpinkkeyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsManager {
	private Context mContext=null;
	public SettingsManager(View view,Context context){
		mContext=context;
		CheckBox soundCheckBox=(CheckBox)view.findViewById(R.id.soundCheckBox);
		CheckBox vibrationCheckBox=(CheckBox)view.findViewById(R.id.VibrationCheckBox);
		CheckBox arrowsCheckBox=(CheckBox)view.findViewById(R.id.ArrowsCheckBox);
		CheckBox traceCheckBox=(CheckBox)view.findViewById(R.id.TraceCheckBox);

		RelativeLayout switchKeyBoardLayout=(RelativeLayout)view.findViewById(R.id.keyBoardSelection);
		switchKeyBoardLayout.setOnClickListener(keyBoardSwitcher);
		TextView enableKeyboards=(TextView)view.findViewById(R.id.keyboardEnable);
		enableKeyboards.setOnClickListener(enablekeyboardListener);
		RelativeLayout moreCoolAppsListener=(RelativeLayout)view.findViewById(R.id.moreCoolApps);
		moreCoolAppsListener.setOnClickListener(moreAppsListener);
		RelativeLayout soundvolumeAdjust=(RelativeLayout)view.findViewById(R.id.soundSettings);
		soundvolumeAdjust.setOnClickListener(soundVolumeChangeListener);

		if(!KeyBoardPreferences.getInstance(context).getSoundSettings()){
			soundCheckBox.setChecked(false);
		}
		if(!KeyBoardPreferences.getInstance(context).getVibrationSettings()){
			vibrationCheckBox.setChecked(false);
		}
		if(! KeyBoardPreferences.getInstance(context).getArrowsSettings()){
			arrowsCheckBox.setChecked(false);
		}
		//		if(! KeyBoardPreferences.getInstance(context).getTraceKeySettings()){
		//			traceCheckBox.setChecked(false);
		//		}
		soundCheckBox.setOnCheckedChangeListener(checkChangeListener);
		vibrationCheckBox.setOnCheckedChangeListener(checkChangeListener);
		arrowsCheckBox.setOnCheckedChangeListener(checkChangeListener);
		//traceCheckBox.setOnCheckedChangeListener(checkChangeListener);

	}
	OnClickListener moreAppsListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent marketIntent=new Intent(Intent.ACTION_VIEW);
			marketIntent.setData(Uri.parse("market://search?q=pub:Top It Free Apps L.L.C."));
			mContext.startActivity(marketIntent);


		}
	};
	OnClickListener keyBoardSwitcher=new OnClickListener() {

		@Override
		public void onClick(View view) {

			InputMethodManager imeManager = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE); 
			if (imeManager != null) {
				imeManager.showInputMethodPicker();
			} else {
				Toast.makeText(mContext,"Not possible to switch", Toast.LENGTH_LONG).show();
			}
		}
	};
	OnClickListener enablekeyboardListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			showAlert();
			//			InputMethodManager imeManager = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			//			List<InputMethodInfo> InputMethods = imeManager.getInputMethodList();;
			//			InputMethods.get(0).loadLabel(mContext.getPackageManager()).toString();


		}
	};
	OnClickListener soundVolumeChangeListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
			AudioManager mAudioManager = (AudioManager)mContext.getSystemService(mContext.AUDIO_SERVICE);
			int maxprogress=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			alertDialogBuilder.setTitle("Adjust Sound Volume");
			View view=View.inflate(mContext, R.layout.seekbar, null);
			SeekBar volumeBar=(SeekBar)view.findViewById(R.id.volumeSeekbar);
			
			int progress=KeyBoardPreferences.getInstance(mContext).getVolumeLevel();
			if(progress==-1){
				progress=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2;
				KeyBoardPreferences.getInstance(mContext).saveVolumelevel(progress);
			}
			volumeBar.setProgress(progress);
			volumeBar.setOnSeekBarChangeListener(volumeSeekBarListener);
			volumeBar.setMax(maxprogress);

			alertDialogBuilder.setView(view);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}
			});


			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
	};
	@SuppressWarnings("deprecation")
	private void showAlert(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view  = inflater.inflate(R.layout.alertdialog, null, false);
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		
		TextView title = new TextView(mContext);
		title.setText("Step 1");
		title.setPadding(0, 20, 0, 20);
		title.setBackgroundColor(Color.parseColor("#282828"));
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setTextColor(Color.WHITE);
		title.setTextSize(30);
		builder.setCustomTitle(title);

		builder.setView(view);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mContext.startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));	
			}
		});
		AlertDialog alertDialog =builder.create();
		alertDialog.show();
	}
	OnCheckedChangeListener checkChangeListener=new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			CheckBox clickedBox=(CheckBox)buttonView;

			switch (clickedBox.getId()) {
			case R.id.soundCheckBox:
				KeyBoardPreferences.getInstance(mContext).saveSoundSettingsFlag(clickedBox.isChecked());
				break;
			case R.id.VibrationCheckBox:
				KeyBoardPreferences.getInstance(mContext).saveVibrationSettingsFlag(clickedBox.isChecked());
				break;
			case R.id.ArrowsCheckBox:
				KeyBoardPreferences.getInstance(mContext).saveArrowKeysSettingsFlag(clickedBox.isChecked());
				Toast.makeText(mContext,"Change orientation to apply new settings", Toast.LENGTH_SHORT).show();
				break;
			case R.id.TraceCheckBox:
				KeyBoardPreferences.getInstance(mContext).saveTraceKeySettingsFlag(clickedBox.isChecked());
				Toast.makeText(mContext,"Change orientation to apply new settings", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	};
	OnSeekBarChangeListener volumeSeekBarListener=new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {


		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {


		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			KeyBoardPreferences.getInstance(mContext).saveVolumelevel(progress);

		}
	};

}
