package br.com.dinamo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.storage.DinamoPrefernces;

public class NavigatorMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);
		
		int status=DinamoPrefernces.getInstance(this).getUserLogInStatus();
		Intent mainIntent=null;

		switch (status) {
		case DinamoConstants.MAIN_SCREEN:
			mainIntent= new Intent(NavigatorMainActivity.this, MainScreenActivity.class);
			break;
		case DinamoConstants.LOG_IN_SCREEN:
			mainIntent= new Intent(NavigatorMainActivity.this, LogInActivity.class);
			break;
		default:
			mainIntent = new Intent(NavigatorMainActivity.this, SignUpActivity.class);
			startActivity(mainIntent);
			break;
		}
		startActivity(mainIntent);
		finish();

	}



}
