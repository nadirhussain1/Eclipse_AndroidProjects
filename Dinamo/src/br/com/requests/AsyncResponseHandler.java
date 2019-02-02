package br.com.requests;


public interface AsyncResponseHandler {
	public void onResult(String resultCode, int statusCode);
	
}
