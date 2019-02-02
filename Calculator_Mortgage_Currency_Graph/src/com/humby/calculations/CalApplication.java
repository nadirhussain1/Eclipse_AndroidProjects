package com.humby.calculations;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(
		mailTo = "dannyklay@gmail.com",
		customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },                
		mode = ReportingInteractionMode.TOAST,
		logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" },
		resToastText = R.string.crash_toast_text, formKey = "")

public class CalApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
}
