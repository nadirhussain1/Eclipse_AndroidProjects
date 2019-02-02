package com.appdupe.flamerapp.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontsUtil {
	
	public static void applyFontToText(Context mContext, TextView textView, String fontName){
		Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),fontName);
		textView.setTypeface(typeFace);

	}

}
