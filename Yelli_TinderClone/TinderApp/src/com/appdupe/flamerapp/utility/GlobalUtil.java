package com.appdupe.flamerapp.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.appdupe.flamerapp.pojo.YelliEducationData;
import com.appdupe.flamerapp.pojo.YelliWorkData;

import android.util.Log;

public class GlobalUtil {
	private static boolean mDebugLog = true;
	public static void logDebug(String mDebugTag,String msg){		
		if (mDebugLog) {
			Log.d(mDebugTag, msg);
		}
	}
	
	public static Date convertToDate(String dateString){
		if(dateString==null || dateString.trim().toString().length()==0){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (java.text.ParseException e) {
			Log.d("Datedebug", "Exception");
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(convertedDate);
		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		
		Log.d("Datedebug", "YEAR="+year+"MONTH="+monthOfYear);
		Log.d("ConvertedDate", ""+convertedDate);
		return convertedDate;

	}
	public static String parseEducationRow(YelliEducationData educationData) {
        String educationString = "";

        if (educationData.getDegree() != null && !educationData.getDegree().isEmpty()) {
        	educationString = educationString +  educationData.getDegree()  +  " from ";
        }

        educationString = educationString + educationData.getInstitution() + " "  +  educationData.getType() ;

        if (educationData.getGraduation() != null && !educationData.getGraduation().isEmpty() && !educationData.getGraduation().equals("0000-00-00")) {

        	educationString = educationString  +  " - Class of " + educationData.getGraduation();
        }
        return educationString;
    }
	public static String parseWorkExperienceRow(YelliWorkData workData) {
        String workExperienceString = "";
        if(workData.getPosition() != null && !workData.getPosition().isEmpty()){
        	workExperienceString = workExperienceString + workData.getPosition() + " with ";
        }else{
        	workExperienceString = workExperienceString +  "Employed with ";
        }

        if (workData.getCompany() != null && !workData.getCompany().isEmpty()) {
        	workExperienceString = workExperienceString + workData.getCompany();
        }

        
        return workExperienceString;
    }
	

}
