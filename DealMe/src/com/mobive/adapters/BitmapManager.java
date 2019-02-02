package com.mobive.adapters;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public enum BitmapManager {
	INSTANCE;

	private final Map<String, SoftReference<Bitmap>> cache;
	private final ExecutorService pool;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	

	BitmapManager() {
		cache = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(4);
	}

	

	public Bitmap getBitmapFromCache(String url) {
		if (cache.containsKey(url)) {
			return cache.get(url).get();
		}

		return null;
	}

	public void queueJob(final String imageName, final ImageView imageView,final ProgressBar waitBar) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(imageName)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
						waitBar.setVisibility(View.GONE);
					} else {
						imageView.setImageBitmap(null);
						waitBar.setVisibility(View.VISIBLE);
					}
				}
			}
		};

		pool.submit(new Runnable() {
			@Override
			public void run() {
				final Bitmap bmp = loadBitmapFromSdCard(imageName);
				Message message = Message.obtain();
				message.obj = bmp;
				Log.d(null, "Item downloaded: " + imageName);

				handler.sendMessage(message);
			}
		});
	}

	public void loadBitmap(final String imageName, final ImageView imageView,ProgressBar waitBar) {
		imageViews.put(imageView, imageName);
		Bitmap bitmap = getBitmapFromCache(imageName);

		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			waitBar.setVisibility(View.GONE);
		} else {
			waitBar.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(null);
			queueJob(imageName, imageView,waitBar);
		}
	}

	private Bitmap loadBitmapFromSdCard(String imageName) {
		File dir = new File(Environment.getExternalStorageDirectory(),"DealMe");
		Bitmap mBitmap=null;
		if(dir.exists())
		{
			mBitmap = BitmapFactory.decodeFile(dir.getPath()+"/"+imageName);
		}
		return mBitmap;
	}
}
