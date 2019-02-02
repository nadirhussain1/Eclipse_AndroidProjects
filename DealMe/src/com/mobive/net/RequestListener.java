package com.mobive.net;

import java.io.InputStream;

public interface RequestListener {
public void onSuccess(InputStream inputStream);
public void onFail(String message);
}
