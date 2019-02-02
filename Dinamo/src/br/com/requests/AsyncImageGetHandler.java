package br.com.requests;

import android.graphics.Bitmap;

public interface AsyncImageGetHandler {
	public void onResult(Bitmap bitmap, int statusCode,int requestId);
}
