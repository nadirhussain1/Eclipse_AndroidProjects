package com.appdupe.flamerapp.utility;

public class CommonConstant 
{
	
	private static String urlPath = "http://yalli.speednikah.com/";
	
	private static String hostUrl = urlPath +"process_1.php/";
	
	public static final String login_url=hostUrl+"login";
	public static final String uploadImage_url=hostUrl+"uploadImage";
	public static final String findMatch_url=hostUrl+"findMatches";
	public static final String getProfile_url=hostUrl+"getProfile";
	public static final String getliked_url=hostUrl+"getProfileMatches";
	public static final String imagedelete_url=hostUrl+"deleteImage";
	public static final String inviteaction_url=hostUrl+"inviteAction";
	public static final String updatePrefrence_url=hostUrl+"updatePreferences";
	public static final String UpdateToken_url=hostUrl+"updateSession";
	public static final String Update_Location_Url=hostUrl+"updateLocation";
	public static final String logout_url=hostUrl+"logout";
	public static final String deleteUserAccount_url=hostUrl+"deleteAccount";
	public static final String blockMatch_url=hostUrl+"blockUser";
	public static final String editUseProfile_url=hostUrl+"editProfile";
	
	// Added
	public static final String sendMessage_url=hostUrl+"sendMessage";
	public static final String getChatHistory_url=hostUrl+"getChatHistory";
	public static final String getChatMessage_url=hostUrl+"getChatMessage";
	
    public static final String ImageHostUrl=urlPath + "pics/";
	public static final int  ChunkSize=524288;
	
	public static final String methodeName="POST";
	public static final String flurryKey="6Z4Z9HMGHKBW3Q88WCFZ";

	public static final String facebooAuthenticationType="1";
	public static final String deviceType="2";
	public static final String FromLogin="fromLogin";
	public static final String Fromsignup="fromsignup";
	public static final String Fromsplash="fromsplash";
	public static final String ALUBUMID="lubumid";
	public static final String ALUBUMNAME="alubumName";
	public static final String FRIENDFACEBOOKID="friendfacebookid";
	public static final String CHECK_FOR_PUSH_OR_NOT="check_for_push";
	public static boolean isMatchedFound=false;
	public static String isFromChatScreen="isfromChateScreen";
	public static String isLikde="1";
	public static String isDisliked="2";

}
