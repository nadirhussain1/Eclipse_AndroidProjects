package com.assessment.interfaces;

import com.assessment.model.Server;

public interface AsyncLocaterResponseCallBack {
	public void onResult(String resultCode,Server server);
}
