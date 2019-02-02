package br.com.dinamo;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class DinamoApplication extends Application{
	private static final String PROPERTY_ID = "UA-50600778-1";

	public DinamoApplication() {
		super();

	}
	public synchronized Tracker getTracker() {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		return analytics.newTracker(PROPERTY_ID);
	}
}
