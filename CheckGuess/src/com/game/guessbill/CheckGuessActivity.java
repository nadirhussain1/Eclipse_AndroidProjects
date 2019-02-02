/* Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.game.guessbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.guessbill.data.GlobalDataManager;
import com.game.guessbill.data.PlayerModel;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.OnPlayersLoadedListener;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameActivity;

/**
 * Button Clicker 2000. A minimalistic game showing the multiplayer features of
 * the Google Play game services API. The objective of this game is clicking a
 * button. Whoever clicks the button the most times within a 20 second interval
 * wins. It's that simple. This game can be played with 2, 3 or 4 players. The
 * code is organized in sections in order to make understanding as clear as
 * possible. We start with the integration section where we show how the game
 * is integrated with the Google Play game services API, then move on to
 * game-specific UI and logic. INSTRUCTIONS: To run this sample, please set up
 * a project in the Developer Console. Then, place your app ID on
 * res/values/ids.xml. Also, change the package name to the package name you
 * used to create the client ID in Developer Console. Make sure you sign the
 * APK with the certificate whose fingerprint you entered in Developer Console
 * when creating your Client Id.
 *
 * @author Bruno Oliveira (btco), 2013-04-26
 */
public class CheckGuessActivity extends BaseGameActivity implements View.OnClickListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener {

	/*
	 * API INTEGRATION SECTION. This section contains the code that integrates
	 * the game with the Google Play game services API.
	 */

	final static String TAG = "CheckGuess";
	final static int RC_INVITATION_INBOX = 10001;
	final static int RC_WAITING_ROOM = 10002;
	String mRoomId = null;
	String mMyId = null;
	String myname=null;
	boolean isMyFirstEntry=true;
	int loadTimes=0;
	String mIncomingInvitationId = null;
	boolean mWaitRoomDismissedFromCode = false;
	final static int[] CLICKABLES = {
		R.id.playonOnePhoneButton, R.id.playonOtherPhoneButton,
		R.id.nextGamePageButton, R.id.setNewGameButton, R.id.continueButton,
		R.id.adminSeeWhoPays,R.id.resetButton,R.id.submitGuessButton,R.id.pendingInvitationsButton,R.id.acceptInvitationButton,
		R.id.backButton,R.id.previewBackButton

	};
	final static int[] SCREENS = {
		R.id.home_screen, R.id.remoteFriendsScreen, R.id.inputScreen,
		R.id.mainScreenWaitBar,R.id.previewScreen
	};
	int mCurScreen = -1;

	// Score of other participants. We update this as we receive their scores
	// from the network.
	Map<String, Integer> mParticipantScore = new HashMap<String, Integer>();
	ArrayList<Participant> mParticipants = null;
	HashMap<String,String> receivedParticipantsId = null;
	TextView playersNamesText=null;
	//Set<String> mFinishedParticipants = new HashSet<String>();


	private View friendsListScreen=null;
	private View inputScreen=null;
	private View previewScreen=null;
	private View invitationPopUp=null;

	int bufferLoadedLength=0;
	SearchAdapter playersSearchAdapter=null;
	boolean isDonePressed=false;
	InputScreen inputScreenHandler=null;
	PreViewScreen preViewScreenHandler=null;
	Button continueButton=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Debug","OnCreate");
		GlobalDataManager.getInstance().context=this;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mainView=inflater.inflate(R.layout.main_layout, null);
		ScalingUtility.getInstance(this).scaleView(mainView);
		setContentView(mainView);

		intiaLizeScreensAndClicks();

	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("Debug","OnPause");
	};
	@Override
	protected void onResume() {
		super.onResume();
		if(GlobalDataManager.getInstance().currentScreen!=-1){
			switchToScreen(GlobalDataManager.getInstance().currentScreen);
		}else{
			setSignInMessages(getString(R.string.signing_in), getString(R.string.signing_out));
		}
		Log.d("Debug","OnResume");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("Debug","OnDestroy");
	};
	private void intiaLizeScreensAndClicks(){
		friendsListScreen=findViewById(R.id.inviteFriendsScreen);
		inputScreen=findViewById(R.id.inputScreen);
		previewScreen=findViewById(R.id.previewScreen);;
		invitationPopUp=findViewById(R.id.invitation_popup);
		continueButton=(Button)inputScreen.findViewById(R.id.continueButton);
		playersNamesText=(TextView)findViewById(R.id.PlayersNamesText);

		for (int id : CLICKABLES) {
			findViewById(id).setOnClickListener(this);
		}
		EditText searchName=(EditText)findViewById(R.id.searchEditText);
		searchName.addTextChangedListener(searchTextChangeListener);


	}
	/**
	 * Called by the base class (BaseGameActivity) when sign-in has failed. For
	 * example, because the user hasn't authenticated yet. We react to this by
	 * showing the sign-in button.
	 */
	@Override
	public void onSignInFailed() {
		if(GlobalDataManager.getInstance().currentScreen!=-1){
			switchToScreen(GlobalDataManager.getInstance().currentScreen);
		}else{
			switchToScreen(R.id.home_screen);
		}
	}

	/**
	 * Called by the base class (BaseGameActivity) when sign-in succeeded. We
	 * react by going to our main screen.
	 */
	@Override
	public void onSignInSucceeded() {
		if(GlobalDataManager.getInstance().currentScreen!=-1){
			switchToScreen(GlobalDataManager.getInstance().currentScreen);
		}else{
			switchToRemoteScreen();
		}
	}
	private void initiateOtherPhonePlayersProcess(){
		if (!verifyPlaceholderIdsReplaced()) {
			showAlert("Error", "Sample not set up correctly. Please see README.");
			return;
		}
		receivedParticipantsId=new HashMap<String, String>();

		if(!isSignedIn()){
			beginUserInitiatedSignIn();	
		}else{
			switchToRemoteScreen();
		}
	}
	private void switchToRemoteScreen(){
		GlobalDataManager.getInstance().isRemoteGame=true;
		getGamesClient().registerInvitationListener(this);
		if (getInvitationId() != null) {
			acceptInviteToRoom(getInvitationId());
			return;
		}
		getGamesClient().loadInvitablePlayers(playerLoadListenr, 20, false);
		switchToScreen(R.id.remoteFriendsScreen);
	}
	private void doneClick(boolean isDone){
		if(isDone && GlobalDataManager.getInstance().playersList.size()==0){
			Toast.makeText(GlobalDataManager.getInstance().context,"Add at least two players to continue", Toast.LENGTH_SHORT).show();
			return;
		}
		if(inputScreenHandler.continueClick()){
			isDonePressed=isDone;
			showInputConfirmAlert();
		}else if(isDone && GlobalDataManager.getInstance().playersList.size()>=2 ){
			preViewScreenHandler=new PreViewScreen(previewScreen);
			switchToScreen(R.id.previewScreen);	
		}else{
			Toast.makeText(GlobalDataManager.getInstance().context,"Enter valid input", Toast.LENGTH_SHORT).show();
		}
	}
	private void showInputConfirmAlert(){
		String formattedAmount=GlobalDataManager.getInstance().appendZerosFormatDouble(GlobalDataManager.getInstance().amount);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("$"+formattedAmount);

		View view=View.inflate(this,R.layout.confirm_pop, null);
		TextView confirmText=(TextView)view.findViewById(R.id.confirmId);	
		confirmText.setText(GlobalDataManager.getInstance().userName+",you have entered a guess of "+"$"+formattedAmount);

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setPositiveButton("Confirm",inputConfirmAlertListener);
		alertDialogBuilder.setNegativeButton("Edit",inputEditClickListener);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
	private void showBackButtonAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("CANCEL");

		View view=View.inflate(this,R.layout.confirm_pop, null);
		TextView confirmText=(TextView)view.findViewById(R.id.confirmId);	
		confirmText.setText("Do you want to reset players?");

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setPositiveButton("Confirm",new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
				resetGameVars();

			}
		});
		alertDialogBuilder.setNegativeButton("Cancel",new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	private void addPlayerEntryAndRemoteBroadCast(){
		if(GlobalDataManager.getInstance().isRemoteGame)
		{
			if(isMyFirstEntry){
				for(int count=0;count<GlobalDataManager.getInstance().playersList.size();count++){
					if(GlobalDataManager.getInstance().playersList.get(count).getGooglePlusId().equalsIgnoreCase(mMyId)){
						GlobalDataManager.getInstance().playersList.get(count).setName(GlobalDataManager.getInstance().userName);
						GlobalDataManager.getInstance().playersList.get(count).setGuessAmount(GlobalDataManager.getInstance().amount);
						GlobalDataManager.getInstance().playersList.get(count).setReadyFlag(true);
						isMyFirstEntry=false;
						break;
					}
				}
			}else{
				String name=GlobalDataManager.getInstance().userName;
				Double amount=GlobalDataManager.getInstance().amount;
				PlayerModel player=new PlayerModel("",name, myname, amount,-1.0, true);
				GlobalDataManager.getInstance().playersList.add(player);
			}
			constructMessage(false);
		}else{
			String name=GlobalDataManager.getInstance().userName;
			Double amount=GlobalDataManager.getInstance().amount;
			PlayerModel player=new PlayerModel("",name, myname, amount,-1.0, isDonePressed);
			GlobalDataManager.getInstance().playersList.add(player);
		}
	}
	OnClickListener inputConfirmAlertListener=new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			addPlayerEntryAndRemoteBroadCast();
			int totalPlayers=GlobalDataManager.getInstance().playersList.size();
			String playersNames=playersNamesText.getText()+GlobalDataManager.getInstance().playersList.get(totalPlayers-1).getName()+",";
			playersNamesText.setText(playersNames);
			
			if(isDonePressed){
				preViewScreenHandler=new PreViewScreen(previewScreen);
				dialog.cancel();
				switchToScreen(R.id.previewScreen);
			}else{
				inputScreenHandler.clearInputData();
			}
			if(GlobalDataManager.getInstance().playersList.size()>=2){
				continueButton.setEnabled(true);
			}

		}
	};
	OnClickListener inputEditClickListener=new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();

		}
	};
	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {

		case R.id.playonOnePhoneButton:
			startGame(false);
			break;
		case R.id.resetButton:
			resetGameVars();
			break;
		case R.id.playonOtherPhoneButton:
			initiateOtherPhonePlayersProcess();
			break;
		case R.id.pendingInvitationsButton:
			intent = getGamesClient().getInvitationInboxIntent();
			switchToScreen(R.id.mainScreenWaitBar);
			startActivityForResult(intent, RC_INVITATION_INBOX);
			break;
		case R.id.acceptInvitationButton:
			// user wants to accept the invitation shown on the invitation
			// popup
			// (the one we got through the OnInvitationReceivedListener).
			findViewById(R.id.acceptWaitBar).setVisibility(View.VISIBLE);
			acceptInviteToRoom(mIncomingInvitationId);
			mIncomingInvitationId = null;
			break;
		case R.id.nextGamePageButton:
			handleSelectPlayersResult();
			break;
		case R.id.continueButton:
			doneClick(true);
			break;
		case R.id.submitGuessButton:
			doneClick(false);
			break;
		case R.id.adminSeeWhoPays:
			if(preViewScreenHandler.seeWhoPayClick() && GlobalDataManager.getInstance().isRemoteGame){
				constructMessage(true);
			}
			break;
		case R.id.backButton:
			backButtonHanlder();
			break;
		case R.id.previewBackButton:
			switchToScreen(R.id.inputScreen);
			break;
		}
	}
	private void backButtonHanlder(){
		if(GlobalDataManager.getInstance().playersList.size()>0){
			showBackButtonAlert();
		}else{
			resetGameVars();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int responseCode,Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		switch (requestCode){

		case RC_INVITATION_INBOX:
			// we got the result from the "select invitation" UI (invitation inbox). We're
			// ready to accept the selected invitation:
			handleInvitationInboxResult(responseCode, intent);
			break;
		case RC_WAITING_ROOM:
			// ignore result if we dismissed the waiting room from code:
			if (mWaitRoomDismissedFromCode) break;

			// we got the result from the "waiting room" UI.
			if (responseCode == Activity.RESULT_OK) {
				// player wants to start playing
				Log.d(TAG, "Starting game because user requested via waiting room UI.");
				// let other players know we're starting.
				broadcastStart();
				// start the game!
				startGame(true);
			} else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
				// player actively indicated that they want to leave the room
				leaveRoom();
			} else if (responseCode == Activity.RESULT_CANCELED) {
				/* Dialog was cancelled (user pressed back key, for
				 * instance). In our game, this means leaving the room too. In more
				 * elaborate games,this could mean something else (like minimizing the
				 * waiting room UI but continue in the handshake process). */
				leaveRoom();
			}

			break;
		}
	}

	// Handle the result of the "Select players UI" we launched when the user clicked the
	// "Invite friends" button. We react by creating a room with those players.
	private void handleSelectPlayersResult() {
		if(GlobalDataManager.getInstance().invitees.size()>0){
			playersSearchAdapter=null;
			ListView friendsList=(ListView)findViewById(R.id.friendsList);
			friendsList.setAdapter(null);
			switchToScreen(R.id.mainScreenWaitBar);

			ArrayList<String> invitees=new ArrayList<String>();
			for(int count=0;count<GlobalDataManager.getInstance().invitees.size();count++){
				invitees.add(GlobalDataManager.getInstance().invitees.get(count).getPlayerId());
			}

			RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
			rtmConfigBuilder.addPlayersToInvite(invitees);
			rtmConfigBuilder.setMessageReceivedListener(this);
			rtmConfigBuilder.setRoomStatusUpdateListener(this);

			keepScreenOn();
			getGamesClient().createRoom(rtmConfigBuilder.build());
		}else{
			Toast.makeText(GlobalDataManager.getInstance().context,"Select one player atleast", Toast.LENGTH_SHORT).show();
		}

	}

	// Handle the result of the invitation inbox UI, where the player can pick an invitation
	// to accept. We react by accepting the selected invitation, if any.
	private void handleInvitationInboxResult(int response, Intent data) {
		if (response != Activity.RESULT_OK) {
			Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
			switchToMainScreen();
			return;
		}

		Log.d(TAG, "Invitation inbox UI succeeded.");
		Invitation inv = data.getExtras().getParcelable(GamesClient.EXTRA_INVITATION);
		// accept invitation
		acceptInviteToRoom(inv.getInvitationId());
	}

	// Accept the given invitation.
	void acceptInviteToRoom(String invId) {
		// accept the invitation
		Log.d(TAG, "Accepting invitation: " + invId);
		RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
		roomConfigBuilder.setInvitationIdToAccept(invId)
		.setMessageReceivedListener(this)
		.setRoomStatusUpdateListener(this);
		//switchToScreen(R.id.mainScreenWaitBar);
		keepScreenOn();
		resetGameVars();
		GlobalDataManager.getInstance().isAdmin=false;
		getGamesClient().joinRoom(roomConfigBuilder.build());
	}

	// Activity is going to the background. We have to leave the current room.
	@Override
	public void onStop() {
		Log.d(TAG, "**** got onStop");

		// if we're in a room, leave it.
		leaveRoom();

		// stop trying to keep the screen on
		stopKeepingScreenOn();

		//switchToScreen(R.id.mainScreenWaitBar);
		super.onStop();
	}

	// Activity just got to the foreground. We switch to the wait screen because we will now
	// go through the sign-in flow (remember that, yes, every time the Activity comes back to the
	// foreground we go through the sign-in flow -- but if the user is already authenticated,
	// this flow simply succeeds and is imperceptible).
	@Override
	public void onStart() {
		//switchToScreen(R.id.mainScreenWaitBar);
		super.onStart();
	}

	// Handle back key to make sure we cleanly leave a game if we are in the middle of one
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.inputScreen) {
			leaveRoom();
			return true;
		}
		return super.onKeyDown(keyCode, e);
	}

	// Leave the room.
	void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		mSecondsLeft = 0;
		stopKeepingScreenOn();
		if (mRoomId != null) {
			getGamesClient().leaveRoom(this, mRoomId);
			mRoomId = null;
			//switchToScreen(R.id.mainScreenWaitBar);
		} else {
			//switchToMainScreen();
		}
	}

	// Show the waiting room UI to track the progress of other players as they enter the
	// room and get connected.
	void showWaitingRoom(Room room) {
		mWaitRoomDismissedFromCode = false;

		// minimum number of players required for our game
		final int MIN_PLAYERS = 2;
		Intent i = getGamesClient().getRealTimeWaitingRoomIntent(room, MIN_PLAYERS);

		// show waiting room UI
		startActivityForResult(i, RC_WAITING_ROOM);
	}

	// Forcibly dismiss the waiting room UI (this is useful, for example, if we realize the
	// game needs to start because someone else is starting to play).
	void dismissWaitingRoom() {
		mWaitRoomDismissedFromCode = true;
		finishActivity(RC_WAITING_ROOM);
	}

	// Called when we get an invitation to play a game. We react by showing that to the user.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		// We got an invitation to play a game! So, store it in
		// mIncomingInvitationId
		// and show the popup on the screen.
		mIncomingInvitationId = invitation.getInvitationId();
		((TextView) findViewById(R.id.inviterName)).setText(invitation.getInviter().getDisplayName() + " has invited you");
		invitationPopUp.setVisibility(View.VISIBLE);
		//switchToScreen(mCurScreen); // This will show the invitation popup
	}

	/*
	 * CALLBACKS SECTION. This section shows how we implement the several games
	 * API callbacks.
	 */

	// Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
	// is connected yet).
	@Override
	public void onConnectedToRoom(Room room) {
		Log.d(TAG, "onConnectedToRoom.");

		// get room ID, participants and my ID:
		mRoomId = room.getRoomId();
		mParticipants = room.getParticipants();
		mMyId = room.getParticipantId(getGamesClient().getCurrentPlayerId());

		// print out the list of participants (for debug purposes)
		Log.d(TAG, "Room ID: " + mRoomId);
		Log.d(TAG, "My ID " + mMyId);
		Log.d(TAG, "<< CONNECTED TO ROOM>>");
	}

	// Called when we've successfully left the room (this happens a result of voluntarily leaving
	// via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
	@Override
	public void onLeftRoom(int statusCode, String roomId) {
		// we have left the room; return to main screen.
		Log.d(TAG, "onLeftRoom, code " + statusCode);
		switchToMainScreen();
	}

	// Called when we get disconnected from the room. We return to the main screen.
	@Override
	public void onDisconnectedFromRoom(Room room) {
		mRoomId = null;
		showGameError();
	}

	// Show error message about game being cancelled and return to main screen.
	void showGameError() {
		showAlert(getString(R.string.error), getString(R.string.game_problem));
		switchToMainScreen();
	}

	// Called when room has been created
	@Override
	public void onRoomCreated(int statusCode, Room room) {
		Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
		if (statusCode != GamesClient.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
			showGameError();
			return;
		}

		// show the waiting room UI
		showWaitingRoom(room);
	}

	// Called when room is fully connected.
	@Override
	public void onRoomConnected(int statusCode, Room room) {
		Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
		if (statusCode != GamesClient.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
			showGameError();
			return;
		}
		updateRoom(room);
	}

	@Override
	public void onJoinedRoom(int statusCode, Room room) {
		Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
		if (statusCode != GamesClient.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
			showGameError();
			return;
		}
		if(GlobalDataManager.getInstance().isAdmin){
			GlobalDataManager.getInstance().adminId=mMyId;
			broadCastAdminId();
		}
		findViewById(R.id.acceptWaitBar).setVisibility(View.GONE);
		// show the waiting room UI
		showWaitingRoom(room);
	}

	// We treat most of the room update callbacks in the same way: we update our list of
	// participants and update the display. In a real game we would also have to check if that
	// change requires some action like removing the corresponding player avatar from the screen,
	// etc.
	@Override
	public void onPeerDeclined(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onPeerInvitedToRoom(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onPeerJoined(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onPeerLeft(Room room, List<String> peersWhoLeft) {
		updateRoom(room);
	}

	@Override
	public void onRoomAutoMatching(Room room) {
		updateRoom(room);
	}

	@Override
	public void onRoomConnecting(Room room) {
		updateRoom(room);
	}

	@Override
	public void onPeersConnected(Room room, List<String> peers) {
		updateRoom(room);
	}

	@Override
	public void onPeersDisconnected(Room room, List<String> peers) {
		updateRoom(room);
	}

	void updateRoom(Room room) {
		mParticipants = room.getParticipants();
		//updatePeerScoresDisplay();
	}

	/*
	 * GAME LOGIC SECTION. Methods that implement the game's rules.
	 */

	// Current state of the game:
	int mSecondsLeft = -1; // how long until the game ends (seconds)
	final static int GAME_DURATION = 20; // game duration, seconds.
	int mScore = 0; // user's current score

	// Reset game variables in preparation for a new game.
	void resetGameVars() {
		mSecondsLeft = GAME_DURATION;
		mScore = 0;
		loadTimes=0;
		isMyFirstEntry=true;
		myname="";

		if(mParticipantScore!=null){
			mParticipantScore.clear();
		}
		if(receivedParticipantsId!=null){
			receivedParticipantsId.clear();
		}
		if(mParticipants!=null){
			mParticipants.clear();
		}
		GlobalDataManager.getInstance().reset();

		playersSearchAdapter=null;
		EditText searchName=(EditText)findViewById(R.id.searchEditText);
		searchName.removeTextChangedListener(searchTextChangeListener);
		searchName.setText("");
		searchName.addTextChangedListener(searchTextChangeListener);
		switchToMainScreen();
	}

	private void costructAdminId(byte[] buf){
		int index=1;
		String id="";
		while((char)buf[index]!=','){
			id+=(char)buf[index];
			index++;
		}
		GlobalDataManager.getInstance().adminId=id;
	}
	private void handleReceivedNameAndValue(String sender,byte[] buf,boolean isFinished){
		String sendernName="";
		Double guessAmount=0.0;

		int index=1;
		while((char)buf[index]!=','){
			sendernName+=(char)buf[index];
			index++;
		}
		index++;
		guessAmount=reConstructReceivedAmount(buf,index);

		String addVia="Google+Id";


		if(receivedParticipantsId!=null && receivedParticipantsId.containsKey(sender)){
			addVia=receivedParticipantsId.get(sender);
			PlayerModel player=new PlayerModel("", sendernName, addVia, guessAmount,-1.0, true);
			GlobalDataManager.getInstance().playersList.add(player);
			if(isFinished){
				for(int count=0;count<GlobalDataManager.getInstance().playersList.size();count++){
					if(GlobalDataManager.getInstance().playersList.get(count).getGooglePlusId().equalsIgnoreCase(sender)){
						GlobalDataManager.getInstance().playersList.get(count).setReadyFlag(true);
					}
				}
			}
		}else{
			for(int count=0;count<GlobalDataManager.getInstance().playersList.size();count++){
				if(GlobalDataManager.getInstance().playersList.get(count).getGooglePlusId().equalsIgnoreCase(sender)){
					GlobalDataManager.getInstance().playersList.get(count).setName(sendernName);
					GlobalDataManager.getInstance().playersList.get(count).setGuessAmount(guessAmount);
					GlobalDataManager.getInstance().playersList.get(count).setReadyFlag(isFinished);
					break;
				}
			}
			if(receivedParticipantsId==null){
				receivedParticipantsId=new HashMap<String, String>();
			}
			receivedParticipantsId.put(sender, sendernName);
		}


	}
	private Double reConstructReceivedAmount(byte[] buf, int startIndex){
		String amountString="";

		for(int digit=startIndex;digit<buf.length;digit++){
			amountString+=(char)buf[digit];
		}
		return Double.valueOf(amountString);
	}
	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte[] buf = rtm.getMessageData();
		String sender = rtm.getSenderParticipantId();

		if ((char)buf[0] == 'S') 
		{
			// someone else started to play -- so dismiss the waiting room and
			// get right to it!
			Log.d(TAG, "Starting game because we got a start message.");
			dismissWaitingRoom();
			startGame(true);
		}
		else if((char)buf[0] == 'N')
		{
			handleReceivedNameAndValue(sender,buf,false);

		}
		else if((char)buf[0] == 'R')
		{
			handleReceivedNameAndValue(sender,buf,true);
		}
		else if((char)buf[0] == 'F'){
			GlobalDataManager.getInstance().finalBillamount=reConstructReceivedAmount(buf,1);
			preViewScreenHandler.nonAdminSeeWhoPay();
		}
		else if((char)buf[0] == 'A'){
			costructAdminId(buf);
		}
		if(preViewScreenHandler!=null){
			preViewScreenHandler.adapter.notifyDataSetChanged();
		}

	}
	void startGame(boolean isRemote) {
		GlobalDataManager.getInstance().isRemoteGame = isRemote;

		switchToScreen(R.id.inputScreen);
		inputScreenHandler=new InputScreen(inputScreen);

		if(GlobalDataManager.getInstance().isRemoteGame){
			for(int count=0;count<mParticipants.size();count++){
				String id = mParticipants.get(count).getParticipantId();
				PlayerModel player=new PlayerModel(id,"",mParticipants.get(count).getDisplayName(),-1.0, -1.0, false);
				player.setIconUri(mParticipants.get(count).getIconImageUri());
				GlobalDataManager.getInstance().playersList.add(player);

				if(id.equalsIgnoreCase(mMyId)){
					myname=mParticipants.get(count).getDisplayName();
				}
			}
		}
	}
	private void sendBroadcast(byte[] mMsgBuf){
		// Send to every other participant.
		for (Participant p : mParticipants) {
			if (p.getParticipantId().equals(mMyId))
				continue;
			if (p.getStatus() != Participant.STATUS_JOINED)
				continue;

			// final score notification must be sent via reliable message
			getGamesClient().sendReliableRealTimeMessage(null, mMsgBuf, mRoomId,
					p.getParticipantId());

		}	
	}
	private void broadCastAdminId(){
		byte[] mMsgBuf=null;
		int messageLength=mMyId.length()+1;
		mMsgBuf=new byte[messageLength];
		mMsgBuf[0] = (byte)'A';

		for(int idIndex=0;idIndex<messageLength-1;idIndex++){
			mMsgBuf[idIndex+1]=(byte) mMyId.charAt(idIndex);
		}
		sendBroadcast(mMsgBuf);
	}
	void constructMessage(boolean finalScore) {
		if (!GlobalDataManager.getInstance().isRemoteGame)
			return; 
		byte[] mMsgBuf=null;
		if(finalScore){
			String message=String.valueOf(GlobalDataManager.getInstance().finalBillamount);
			int messageLength=message.length()+1;
			mMsgBuf=new byte[messageLength];
			mMsgBuf[0] = (byte)'F';
			for(int nameIndex=0;nameIndex<messageLength-1;nameIndex++){
				mMsgBuf[nameIndex+1]=(byte) message.charAt(nameIndex);
			}
		}
		else {
			String message=GlobalDataManager.getInstance().userName+","+String.valueOf(GlobalDataManager.getInstance().amount);
			int messageLength=message.length()+1;
			mMsgBuf=new byte[messageLength];
			mMsgBuf[0] = (byte)'N';
			if(isDonePressed){
				mMsgBuf[0] = (byte)'R';
			}
			for(int nameIndex=0;nameIndex<messageLength-1;nameIndex++){
				mMsgBuf[nameIndex+1]=(byte) message.charAt(nameIndex);
			}
		}
		sendBroadcast(mMsgBuf);

	}

	// Broadcast a message indicating that we're starting to play. Everyone else
	// will react
	// by dismissing their waiting room UIs and starting to play too.
	void broadcastStart() {
		if (!GlobalDataManager.getInstance().isRemoteGame)
			return; // playing single-player mode
		byte[] mMsgBuf=new byte[2];
		mMsgBuf[0] = 'S';
		mMsgBuf[1] = (byte) 0;
		for (Participant p : mParticipants) {
			if (p.getParticipantId().equals(mMyId))
				continue;
			if (p.getStatus() != Participant.STATUS_JOINED)
				continue;
			getGamesClient().sendReliableRealTimeMessage(null, mMsgBuf, mRoomId,
					p.getParticipantId());
		}
	}

	/*
	 * UI SECTION. Methods that implement the game's UI.
	 */

	// This array lists everything that's clickable, so we can install click
	// event handlers.


	void switchToScreen(int screenId) {
		GlobalDataManager.getInstance().currentScreen=screenId;
		for (int id : SCREENS) {
			findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
		}
		mCurScreen = screenId;
		// should we show the invitation popup?
		boolean showInvPopup;
		if (mIncomingInvitationId == null) {
			// no invitation, so no popup
			showInvPopup = false;
		} else{
			showInvPopup = (mCurScreen == R.id.home_screen || mCurScreen == R.id.remoteFriendsScreen);
		}
		findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
	}

	void switchToMainScreen() {
		//		if(isSignedIn()){
		//			playersSearchAdapter=null;
		//			EditText searchName=(EditText)findViewById(R.id.searchEditText);
		//			searchName.removeTextChangedListener(searchTextChangeListener);
		//			searchName.setText("");
		//			switchToScreen(R.id.remoteFriendsScreen);
		//			searchName.addTextChangedListener(searchTextChangeListener);
		//			getGamesClient().loadInvitablePlayers(playerLoadListenr, 20, false);
		//		}else{
		switchToScreen(R.id.home_screen); 
		GlobalDataManager.getInstance().currentScreen=R.id.home_screen;
		//}

	}



	/*
	 * MISC SECTION. Miscellaneous methods.
	 */

	/**
	 * Checks that the developer (that's you!) read the instructions. IMPORTANT:
	 * a method like this SHOULD NOT EXIST in your production app! It merely
	 * exists here to check that anyone running THIS PARTICULAR SAMPLE did what
	 * they were supposed to in order for the sample to work.
	 */
	boolean verifyPlaceholderIdsReplaced() {
		final boolean CHECK_PKGNAME = true; // set to false to disable check
		// (not recommended!)

		// Did the developer forget to change the package name?
		if (CHECK_PKGNAME && getPackageName().startsWith("com.google.example."))
			return false;

		// Did the developer forget to replace a placeholder ID?
		int res_ids[] = new int[] {
				R.string.app_id
		};
		for (int i : res_ids) {
			if (getString(i).equalsIgnoreCase("ReplaceMe"))
				return false;
		}
		return true;
	}

	// Sets the flag to keep this screen on. It's recommended to do that during
	// the
	// handshake when setting up a game, because if the screen turns off, the
	// game will be
	// cancelled.
	void keepScreenOn() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	// Clears the flag that keeps the screen on.
	void stopKeepingScreenOn() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	OnPlayersLoadedListener playerLoadListenr=new OnPlayersLoadedListener() {

		@Override
		public void onPlayersLoaded(int arg0, PlayerBuffer buffer) {
			if(!GlobalDataManager.getInstance().isAdmin){
				playersSearchAdapter=null;
				ListView friendsList=(ListView)findViewById(R.id.friendsList);
				friendsList.setAdapter(null);
				loadTimes=0;
				return;
			}
			loadTimes++;
			if(playersSearchAdapter==null){
				playersSearchAdapter=new SearchAdapter(CheckGuessActivity.this, buffer, friendsListScreen);
				ListView friendsList=(ListView)findViewById(R.id.friendsList);
				friendsList.setAdapter(playersSearchAdapter);
			}else if(loadTimes>=5){
				loadTimes=0;
				playersSearchAdapter.setAllLoadedFriends(buffer);
				playersSearchAdapter.notifyDataSetChanged();
			}

			if(bufferLoadedLength<buffer.getCount()){
				bufferLoadedLength=buffer.getCount();
				getGamesClient().loadMoreInvitablePlayers(playerLoadListenr, 20);
			}else{
				findViewById(R.id.circularBar).setVisibility(View.GONE);
			}


		}
	};

	TextWatcher searchTextChangeListener=new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable editable) {
			GlobalDataManager.getInstance().searchPlayer=editable.toString();
			if(playersSearchAdapter!=null){
				playersSearchAdapter.notifyDataSetChanged();
			}

		}
	};
}
