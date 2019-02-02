package  com.appdupe.flamerchatmodul.db;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appdupe.flamerapp.pojo.ImageDetail;
import com.appdupe.flamerapp.pojo.LikeMatcheddataForListview;
import com.embed.anddroidpushntificationdemo11.ChatMessageList;
public class DatabaseHandler 
{

	private static boolean mDebugLog = true;
	private static    String mDebugTag = "MainActivity";
	void logDebug(String msg)
	{

		if (mDebugLog) 
		{
			Log.d(mDebugTag, msg);
		}
	}


	void logError(String msg)
	{

		if (mDebugLog) 
		{
			Log.e(mDebugTag, msg);
		}
	}

	private static String TAG = "DatabaseHandler" ;
	private static String DATABASE_NAME = "TinderModuldb";
	private static int DATABASE_VERSION = 1;

	//for CHAT DATA
	private static String CHAT_MESSAGE_TABLE="chat_message_table";

	private static String FACEBOOK_ID="facebook_id";
	private static String CHAT_MESSAGE="chat_message";
	private static String MESSAGE_DATE="message_date";
	private static String USERNAME_DATA="username";
	private static String SENDER_ID="sender_id";
	private static String RECEIVER_ID="receiver_id";
	private static String FLAG_FOR_SUCCESS="message_success";

	//for MATCH LIST
	private static String MATCH_LIST_TABLE="match_list_table";

	private static String USER_FACEBOOK_ID="user_facebook_id";
	private static String SENDER_FACEBOOK_ID="sender_facebook_id";
	private static String SENDER_PIC_URL="sender_pic_url";
	private static String SENDER_FILE_PATH="sender_file_path";
	private static String SENDER_ID_NAME="sender_id_name";
	private static String SENDER_ladt="sender_last_date";
	private static String SENDER_flag="flag";


	private static String Current_User_Image_info_TABLE="user_infor_table";
	private static String Url="Url";
	private static String imageImafoTavble_columid="colum_id";
	private static String sdcardpath="sdcardpath";
	private static String imageorder="imageoder";
	private static String Image_info_USERFACEBOOKID="user_facebookid";



	SQLiteDatabase tinderdatabase = null;

	DatabaseHelper mDBHelper = null;



	private Context mContext = null;

	public DatabaseHandler(Context aContext) {

		mContext = aContext;
		if(null == mDBHelper)
		{
			mDBHelper = new DatabaseHelper(mContext);

		}
		//Add sample data for testing
		//addSampleData();
	}

	private class DatabaseHelper extends SQLiteOpenHelper{

		DatabaseHelper(Context aContext) 
		{
			super(aContext,DATABASE_NAME, null, DATABASE_VERSION);  

		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{  

			//for cate data
			db.execSQL("create table " + CHAT_MESSAGE_TABLE + " ("+SENDER_ID+" VARCHAR,"+RECEIVER_ID+" VARCHAR,"+CHAT_MESSAGE+" VARCHAR,"+
					USERNAME_DATA+" VARCHAR,"+FACEBOOK_ID+" VARCHAR,"+FLAG_FOR_SUCCESS+" VARCHAR,"+
					MESSAGE_DATE+" VARCHAR)");

			//for Mached data 
			db.execSQL("create table " + MATCH_LIST_TABLE + " ("+USER_FACEBOOK_ID+" VARCHAR,"+SENDER_FACEBOOK_ID+" VARCHAR,"+SENDER_PIC_URL+" VARCHAR,"+
					SENDER_FILE_PATH+" VARCHAR,"+SENDER_ID_NAME+" VARCHAR,"+SENDER_ladt+" VARCHAR,"+SENDER_flag+" VARCHAR)");
			db.execSQL("create table " + Current_User_Image_info_TABLE + " ("+imageImafoTavble_columid+" integer primary key autoincrement,"+Image_info_USERFACEBOOKID+" VARCHAR,"+Url+" VARCHAR,"+
					sdcardpath+" VARCHAR,"+imageorder+" integer"+")");//


		}     

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{  
			onCreate(db);
		}
	}


	// add detail with key

	public long addImagedetal(ArrayList<ImageDetail>imagelist)
	{

		ImageDetail imageDetail;
		String userfacebookid;
		String imageurl;
		String sdcardPath;
		int imageOrder;
		long count=0;
		boolean detailsStored = true;

		try 
		{
			tinderdatabase = mDBHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			if (imagelist!=null && imagelist.size()>0)
			{

				for (int i = 0; i < imagelist.size(); i++)
				{
					imageDetail=imagelist.get(i);
					userfacebookid=imageDetail.getUserFacebookid();
					imageurl=imageDetail.getImageUrl();
					sdcardPath=imageDetail.getSdcardpath();
					imageOrder=imageDetail.getImageOrder();




					values.put(Image_info_USERFACEBOOKID, userfacebookid);
					values.put(Url, imageurl);
					values.put(sdcardpath, sdcardPath);
					values.put(imageorder, imageOrder);
					count = tinderdatabase.insertOrThrow(Current_User_Image_info_TABLE, null,values);


				}
			}
			else {
				logError("addImagedetal  imagelist null or empty");
			}

			//values.put("Category_id", catogory_id);

		}
		catch (SQLiteException lSqlEx) 
		{

			logError("addImagedetal  Could not open addDetail database   SQLiteException "+lSqlEx);

			detailsStored = false;
		}
		catch (SQLException lEx)
		{
			Log.e(TAG, "Could not insert addDetail data");
			Log.e(TAG, "Exception:" + lEx.getMessage());
			logError("addImagedetal  Could not insert addDetail data  SQLException "+lEx);
			detailsStored = false;
		}
		finally 
		{
			if (tinderdatabase != null)
				tinderdatabase.close();
		}

		return count;
	}


	//updateContact()
	// Updating single contact
	public String [] updateOrInsertImagepath(ImageDetail imageDetail) 
	{
		String previousImageUrl=null;
		String imageSdCardpath=null;
		String [] imageUrlAndPath= new java.lang.String[2];
		try {  	
			String userfacebookid;
			String imageurl;
			String sdcardPath;
			int imageOrder;
			Cursor cursor;

			tinderdatabase = mDBHelper.getWritableDatabase();  
			ContentValues values = new ContentValues();

			userfacebookid=imageDetail.getUserFacebookid();
			imageurl=imageDetail.getImageUrl();
			sdcardPath=imageDetail.getSdcardpath();
			imageOrder=imageDetail.getImageOrder();


			String selectQuary="select imageoder,Url,sdcardpath from user_infor_table where imageoder="+"'"+imageOrder+"'";
			cursor=tinderdatabase.rawQuery(selectQuary, null);
			Log.d("PhotoDebug","Update Database ---Outside Loop");
			if (cursor!=null && cursor.moveToFirst() && cursor.getCount()>0){	

				previousImageUrl=cursor.getString(1);
				imageSdCardpath=cursor.getString(2);
				imageUrlAndPath[0]=previousImageUrl;
				imageUrlAndPath[1]=imageSdCardpath;

				values.put(Image_info_USERFACEBOOKID, userfacebookid);
				values.put(Url, imageurl);
				values.put(sdcardpath, sdcardPath);

				int isUpdated=  tinderdatabase.update(Current_User_Image_info_TABLE, values, imageorder + " = ?",new String[] { String.valueOf(imageOrder) });
				Log.d("PhotoDebug","Update Database ---IsUpdated="+isUpdated);

			}
			else {
				previousImageUrl="imageNotFount";
				imageUrlAndPath[0]=previousImageUrl;

				values.put(Image_info_USERFACEBOOKID, userfacebookid);
				values.put(Url, imageurl);
				values.put(sdcardpath, sdcardPath);
				values.put(imageorder, imageOrder);
				long row=tinderdatabase.insertOrThrow(Current_User_Image_info_TABLE, null,values);	
				Log.d("PhotoDebug","Insert Row="+row);
			}
		} 
		catch (SQLiteException lSqlEx) {
			previousImageUrl="imageNotFount";
			imageUrlAndPath[0]=previousImageUrl;
			logError("updateOrInsertImagepath  Could not open updateOrInsertImagepath database   SQLiteException "+lSqlEx);		
		}
		catch (SQLException lEx){
			previousImageUrl="imageNotFount";
			imageUrlAndPath[0]=previousImageUrl;
			Log.e(TAG, "Could not insert addDetail data");
			Log.e(TAG, "Exception:" + lEx.getMessage());
			logError("updateOrInsertImagepath  Could not insert updateOrInsertImagepath data  SQLException "+lEx);

		}
		finally {
			if (tinderdatabase != null)
				tinderdatabase.close();
		}

		return imageUrlAndPath;



	}



	public ArrayList<ImageDetail>getImageDetail()
	{

		ArrayList<ImageDetail>infoList = null;
		Cursor cursor = null;
		try
		{
			//tinderdatabase = mDBHelper.getWritableDatabase();
			tinderdatabase = mDBHelper.getReadableDatabase();
			String[] columns = new String[]{imageorder,sdcardpath,Url,imageImafoTavble_columid};
			//String whereClause = "Category_id =?";
			//String[] whereArgs = new String[] {""+categoryid};
			cursor = tinderdatabase.query(Current_User_Image_info_TABLE,columns,null,null, null, null, null);


			if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
			{


				infoList=new ArrayList<ImageDetail>();
				do
				{
					ImageDetail imageDetail=new ImageDetail();
					int  imageOrder=cursor.getInt(0);
					String  sdcardPath=cursor.getString(1);
					String  Url=cursor.getString(2);
					int  imageImafoTavble_columid=cursor.getInt(3);

					imageDetail.setImageOrder(imageOrder);
					imageDetail.setSdcardpath(sdcardPath);
					imageDetail.setImageUrl(Url);
					imageDetail.setCoulumid(imageImafoTavble_columid);

					infoList.add(imageDetail);

				}
				while(cursor.moveToNext());
			}
			else
			{
				//System.out.println("getCategoryName Cursor is null or empty");
				logError("getImageDetail  Cursor is null or empty");
			}
		}
		catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "Could not open getCategoryName database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());
			logError("getImageDetail  Exception:"+lSqlEx.getMessage());

			infoList=null;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "Could not getCategoryName data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
			logError("getImageDetail  Exception:"+lEx.getMessage());
			infoList=null;

		} 
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return infoList;
	}




	public ArrayList<ImageDetail>getImageDetailByImageOrder(String [] imagrderarray)
	{


		ArrayList<ImageDetail>infoList = null;
		infoList=new ArrayList<ImageDetail>();
		String imageOrderFormUser;
		Cursor cursor = null;
		try
		{
			//tinderdatabase = mDBHelper.getWritableDatabase();
			tinderdatabase = mDBHelper.getReadableDatabase();
			String[] columns = new String[]{imageorder,sdcardpath,Url,imageImafoTavble_columid};
			String whereClause = "imageoder =?";
			for (int i = 0; i < imagrderarray.length; i++) 
			{
				imageOrderFormUser=imagrderarray[i];

				String[] whereArgs = {imageOrderFormUser};

				cursor = tinderdatabase.query(Current_User_Image_info_TABLE,columns,whereClause,whereArgs, null, null, null);

				if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
				{



					do
					{
						ImageDetail imageDetail=new ImageDetail();
						int  imageOrder=cursor.getInt(0);
						String  sdcardPath=cursor.getString(1);
						String  Url=cursor.getString(2);
						int  imageImafoTavble_columid=cursor.getInt(3);

						imageDetail.setImageOrder(imageOrder);
						imageDetail.setSdcardpath(sdcardPath);
						imageDetail.setImageUrl(Url);
						imageDetail.setCoulumid(imageImafoTavble_columid);

						infoList.add(imageDetail);

					}
					while(cursor.moveToNext());
				}
				else
				{
					//System.out.println("getCategoryName Cursor is null or empty");
					logError("getImageDetailByImageOrder  Cursor is null or empty");
				}

			}	
		}
		catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "Could not open getCategoryName database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());
			logError("getImageDetailByImageOrder  Exception:"+lSqlEx.getMessage());

			infoList=null;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "Could not getCategoryName data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
			logError("getImageDetailByImageOrder  Exception:"+lEx.getMessage());
			infoList=null;

		} 
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return infoList;
	}

	public boolean deleteImagedetail(String [] imageOrderArray)
	{

		boolean favoritedatalist = false;
		String imageorder;
		//DealDetails d=null;
		Cursor cursor = null;
		try
		{
			tinderdatabase = mDBHelper.getReadableDatabase();
			//String[] columns = new String[]{"KEY_IMAGE"};
			String whereClause = "imageoder =?";
			for (int i = 0; i < imageOrderArray.length; i++) 
			{
				imageorder=imageOrderArray[i];
				String[] whereArgs = {imageorder};
				tinderdatabase.delete(Current_User_Image_info_TABLE, whereClause, whereArgs);	
				favoritedatalist=true;
				if (favoritedatalist)
				{

				}

			}
		}
		catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "deleteImagedetail Could not open database");
			Log.e(TAG, "deleteImagedetail Exception:"+lSqlEx.getMessage());
			favoritedatalist=false;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "deleteImagedetail Could not fetch trip data");
			Log.e(TAG, "deleteImagedetail Exception:"+lEx.getMessage());
			favoritedatalist=false;

		}
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return favoritedatalist;

	}	




	public boolean deleteUserData()
	{



		boolean favoritedatalist = false;

		//DealDetails d=null;
		Cursor cursor = null;
		try
		{
			tinderdatabase = mDBHelper.getReadableDatabase();
			//String[] columns = new String[]{"KEY_IMAGE"};
			/*String whereClause = "USERID =?";*/
			/*String[] whereArgs = new String[] {userid};*/
			tinderdatabase.delete(Current_User_Image_info_TABLE, null, null);	
			favoritedatalist=true;
			if (favoritedatalist)
			{

			}


		}
		catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "deleteUserData Could not open database");
			Log.e(TAG, "deleteUserData Exception:"+lSqlEx.getMessage());
			favoritedatalist=false;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "deleteUserData Could not fetch trip data");
			Log.e(TAG, "deleteUserData Exception:"+lEx.getMessage());
			favoritedatalist=false;

		}
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return favoritedatalist;

	}




	public boolean deleteMatchedlist(){
		boolean favoritedatalist = false;
		Cursor cursor = null;
		try{
			tinderdatabase = mDBHelper.getReadableDatabase();
			tinderdatabase.delete(MATCH_LIST_TABLE, null, null);	
			favoritedatalist=true;
		}
		catch(SQLiteException lSqlEx){
			Log.e(TAG, "deleteUserData Could not open database");
			Log.e(TAG, "deleteUserData Exception:"+lSqlEx.getMessage());
			favoritedatalist=false;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "deleteUserData Could not fetch trip data");
			Log.e(TAG, "deleteUserData Exception:"+lEx.getMessage());
			favoritedatalist=false;

		}
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return favoritedatalist;

	}

	public boolean insertMessageData(ChatMessageList objMessageData) {

		boolean detailsStored = true;
		tinderdatabase = mDBHelper.getWritableDatabase();
		try {

			String str=objMessageData.getStrSenderFacebookId();
			ContentValues values = new ContentValues();
			values.put(DatabaseHandler.FACEBOOK_ID, str);
			values.put(DatabaseHandler.CHAT_MESSAGE, objMessageData.getStrMessage());
			values.put(DatabaseHandler.MESSAGE_DATE, objMessageData.getStrDateTime());
			values.put(DatabaseHandler.USERNAME_DATA, objMessageData.getStrSendername());
			values.put(DatabaseHandler.SENDER_ID, objMessageData.getStrSenderFacebookId());
			values.put(DatabaseHandler.RECEIVER_ID, objMessageData.getStrReceiverId());
			values.put(DatabaseHandler.FLAG_FOR_SUCCESS, objMessageData.getStrFlagForMessageSuccess());

			long count = tinderdatabase.insertOrThrow(CHAT_MESSAGE_TABLE, null,values);
			System.out.println("Inserted trip details row id:......" + count);
		}catch (Exception e) {
			e.printStackTrace();
			detailsStored = false;
		}
		finally {
			if (tinderdatabase != null)
				tinderdatabase.close();
		}


		return detailsStored;
	}
	

	public int deleteBlockedUserMatch(String match_fb_id){
		int delete=-1;
		try{
			tinderdatabase = mDBHelper.getWritableDatabase();
			delete=tinderdatabase.delete(MATCH_LIST_TABLE, "sender_facebook_id=? ", new String[]{match_fb_id});
		}catch(SQLiteException exception){
			Log.d("Database",exception.toString());
		}
		return delete;

	}
	public boolean insertMatchList(ArrayList<LikeMatcheddataForListview> matchlist,String usrFacebookId) {

		boolean detailsStored = true;
		tinderdatabase = mDBHelper.getWritableDatabase();
		try {

			for (int i = 0; i < matchlist.size(); i++) {
				LikeMatcheddataForListview objMatchData=matchlist.get(i);
				String fbId=objMatchData.getFacebookid();
				Cursor mCursor1 = tinderdatabase.rawQuery("SELECT * FROM " + MATCH_LIST_TABLE + " WHERE    sender_facebook_id=? ", new String[]{fbId});

				LikeMatcheddataForListview matcheddataForListview=matchlist.get(i);

				ContentValues values = new ContentValues();
				values.put(DatabaseHandler.USER_FACEBOOK_ID, usrFacebookId);
				values.put(DatabaseHandler.SENDER_FACEBOOK_ID, matcheddataForListview.getFacebookid());
				values.put(DatabaseHandler.SENDER_PIC_URL, matcheddataForListview.getImageUrl());
				values.put(DatabaseHandler.SENDER_FILE_PATH, matcheddataForListview.getFilePath());
				values.put(DatabaseHandler.SENDER_ID_NAME, matcheddataForListview.getUserName());
				values.put(DatabaseHandler.SENDER_ladt, matcheddataForListview.getladt());
				values.put(DatabaseHandler.SENDER_flag, matcheddataForListview.getFlag());


				if (mCursor1 != null && mCursor1.getCount()>0) {

					String whereClause1 = DatabaseHandler.USER_FACEBOOK_ID+" =?";
					String[] whereArgs1 = new String[] {""+fbId};

					long count = tinderdatabase.update(MATCH_LIST_TABLE, values,whereClause1, whereArgs1);
					if (count<=0){
						detailsStored=false;
						break;
					}

					logError("insertMatchList  count "+count);


				}
				else{
					long count = tinderdatabase.insertOrThrow(MATCH_LIST_TABLE, null,values);
					if (count<=0){
						detailsStored=false;
						break;
					}
					System.out.println("Inserted trip details row id:......" + count);
					logError("insertMatchList  count "+count);
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
			detailsStored = false;
			logError("insertMatchList  Exception "+e);
		}
		finally {
			if (tinderdatabase != null)
				tinderdatabase.close();
		}


		return detailsStored;
	}



	public ArrayList<LikeMatcheddataForListview>getUserFindMatch(){
		ArrayList<LikeMatcheddataForListview>infoList = null;
		Cursor cursor = null;
		try{
			tinderdatabase = mDBHelper.getReadableDatabase();
			String qary="select user_facebook_id,sender_facebook_id,sender_pic_url,sender_file_path,sender_id_name from match_list_table" ;
			cursor=tinderdatabase.rawQuery(qary, null);
			if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0){

				infoList=new ArrayList<LikeMatcheddataForListview>();
				do{
					LikeMatcheddataForListview matcheddataForListview=new LikeMatcheddataForListview();
					String  SENDER_FACEBOOK_ID=cursor.getString(1);
					String  SENDER_PIC_URL=cursor.getString(2);
					String  SENDER_FILE_PATH=cursor.getString(3);
					String  SENDER_ID_NAME=cursor.getString(4);
					matcheddataForListview.setFacebookid(SENDER_FACEBOOK_ID);
					matcheddataForListview.setImageUrl(SENDER_PIC_URL);;
					matcheddataForListview.setFilePath(SENDER_FILE_PATH);;
					matcheddataForListview.setUserName(SENDER_ID_NAME);;

					infoList.add(matcheddataForListview);

				}
				while(cursor.moveToNext());
			}
			else{
				logError("getUserFindMatch  Cursor is null or empty");
			}
		}
		catch(SQLiteException lSqlEx){
			Log.e(TAG, "Could not open getCategoryName database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());
			logError("getUserFindMatch  Exception:"+lSqlEx.getMessage());

			infoList=null;
		}
		catch(SQLException lEx){
			Log.e(TAG, "Could not getCategoryName data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
			logError("getUserFindMatch  Exception:"+lEx.getMessage());
			infoList=null;
		} 
		finally{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return infoList;
	}




	public LikeMatcheddataForListview getSenderDetail(String sendderFacebookid)
	{

		// ArrayList<CategoryDetailValueData>infoList = null;
		//DealDetails d=null;

		Cursor cursor = null;
		LikeMatcheddataForListview matcheddataForListview=null;
		try
		{
			tinderdatabase = mDBHelper.getReadableDatabase();

			//String[] columns = new String[]{USER_FACEBOOK_ID,SENDER_FACEBOOK_ID,SENDER_PIC_URL,SENDER_FILE_PATH,SENDER_ID_NAME};

			String quary=	"SELECT  user_facebook_id ,sender_facebook_id,sender_pic_url,sender_file_path,sender_id_name  FROM match_list_table where  sender_facebook_id="+"'"+sendderFacebookid+"'";


			String[] columns = new String[]{USER_FACEBOOK_ID,SENDER_FACEBOOK_ID,SENDER_PIC_URL,SENDER_FILE_PATH,SENDER_ID_NAME};
			String whereClause ="SENDER_FACEBOOK_ID=?";
			String[] whereArgs = new String[] {""+sendderFacebookid};
			//cursor = tinderdatabase.query(MATCH_LIST_TABLE,columns,whereClause,whereArgs, null, null, null);
			cursor=tinderdatabase.rawQuery(quary, null);

			if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
			{

				//infoList=new ArrayList<CategoryDetailValueData>();
				do
				{

					matcheddataForListview=new LikeMatcheddataForListview();
					//					WHAT IN THE WORLD IS THIS????!!!	    			
					//	    			USER_FACEBOOK_ID=cursor.getString(0);
					//	    			SENDER_FACEBOOK_ID=cursor.getString(1);
					//	    			SENDER_PIC_URL=cursor.getString(2);
					//	    			SENDER_FILE_PATH=cursor.getString(3);
					//	    			SENDER_ID_NAME=cursor.getString(4);
					matcheddataForListview.setImageUrl(cursor.getString(2));
					matcheddataForListview.setFilePath(cursor.getString(3));
					matcheddataForListview.setUserName(cursor.getString(4));



				}
				while(cursor.moveToNext());
			}
			else
			{
				System.out.println("getCategoryName Cursor is null or empty");
				logError("getSenderDetail  getCategoryName Cursor is null or empty");
				matcheddataForListview=null;
			}
		}
		catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "Could not open getCategoryName database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());
			logError("getSenderDetail  Exception:"+lSqlEx.getMessage());
			matcheddataForListview=null;

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "Could not getCategoryName data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
			logError("getSenderDetail  Exception:"+lEx.getMessage());
			matcheddataForListview=null;


		} 
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return matcheddataForListview;
	}











	public ArrayList<ChatMessageList> getChatMessages(String strFriendFBId) {
		ArrayList<ChatMessageList> listChatMessage=new ArrayList<ChatMessageList>();
		Cursor cursor = null;
		try
		{
			/*//for CATEGORY MASTER
			private static String CHAT_MESSAGE_TABLE="chat_message_table";
			private static String FACEBOOK_ID="facebook_id";
			private static String CHAT_MESSAGE="chat_message";
			private static String MESSAGE_DATE="message_date";
			private static String USERNAME_DATA="username";*/

			tinderdatabase = mDBHelper.getReadableDatabase();
			/*String[] columns = new String[]{DatabaseHandler.FACEBOOK_ID,
					DatabaseHandler.CHAT_MESSAGE,
					DatabaseHandler.MESSAGE_DATE,
					DatabaseHandler.USERNAME_DATA,
					DatabaseHandler.UNIQUE_USER_ID
	    			};*/

			final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt  where " +
					"  cmt.sender_id="+strFriendFBId+" or cmt.receiver_id="+strFriendFBId;

			/*final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt  where " +
					"  cmt.unique_user_id in ("+strFbIdUser+","+strFbIdFriend+")";*/

			/*final String MY_QUERY = "SELECT * FROM   chat_message_table cmt  where "+
		    "cmt.unique_user_id like "+strFbIdUser+" or cmt.unique_user_id like "+strFbIdFriend;*/
			cursor=tinderdatabase.rawQuery(MY_QUERY, null);
			/*String whereClause = DatabaseHandler.FACEBOOK_ID+" =?";
		      String[] whereArgs = new String[] {strFbIdUser,strFbIdFriend};
			cursor = tinderdatabase.query(CHAT_MESSAGE_TABLE, columns, whereClause, whereArgs, null, null, null);*/

			if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
			{

				//    d= new DealDetails();
				do
				{
					ChatMessageList ObjChatMessage=new ChatMessageList();
					ObjChatMessage.setStrSenderFacebookId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.FACEBOOK_ID)));
					ObjChatMessage.setStrMessage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.CHAT_MESSAGE)));
					ObjChatMessage.setStrDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.MESSAGE_DATE)));
					ObjChatMessage.setStrSendername(cursor.getString(cursor.getColumnIndex(DatabaseHandler.USERNAME_DATA)));
					//ObjChatMessage.setStrUniqueId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.UNIQUE_USER_ID)));

					listChatMessage.add(ObjChatMessage);
					System.out.println("Getting favorote heritagspot detail");



				}
				while(cursor.moveToNext());
			}
			else
			{
				System.out.println("Cursor is null or empty");
			}
		}catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
		}
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return listChatMessage;
	}

	public ArrayList<ChatMessageList> getChatMessages(String strFriendFBId,int start,int limit) {
		ArrayList<ChatMessageList> listChatMessage=new ArrayList<ChatMessageList>();
		Cursor cursor = null;
		try
		{
			/*//for CATEGORY MASTER
			private static String CHAT_MESSAGE_TABLE="chat_message_table";
			private static String FACEBOOK_ID="facebook_id";
			private static String CHAT_MESSAGE="chat_message";
			private static String MESSAGE_DATE="message_date";
			private static String USERNAME_DATA="username";*/

			tinderdatabase = mDBHelper.getReadableDatabase();
			/*String[] columns = new String[]{DatabaseHandler.FACEBOOK_ID,
					DatabaseHandler.CHAT_MESSAGE,
					DatabaseHandler.MESSAGE_DATE,
					DatabaseHandler.USERNAME_DATA,
					DatabaseHandler.UNIQUE_USER_ID
	    			};*/

			/*final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt  where " +
					"  cmt.sender_id="+strFriendFBId+" or cmt.receiver_id="+strFriendFBId;*/

			/*final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt  where " +
					"  cmt.sender_id="+strFriendFBId+" or cmt.receiver_id="+strFriendFBId+" limit "+start+","+end;
			 */

			final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt where cmt.sender_id="+strFriendFBId
					+" or cmt.receiver_id="+strFriendFBId+" order by "+MESSAGE_DATE+" desc limit "+start+","+limit;

			/*final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt order by "+MESSAGE_DATE+" desc limit "+start+","+limit;*/

			/*final String MY_QUERY = "SELECT * FROM "+
					" chat_message_table cmt  where " +
					"  cmt.unique_user_id in ("+strFbIdUser+","+strFbIdFriend+")";*/

			/*final String MY_QUERY = "SELECT * FROM   chat_message_table cmt  where "+
		    "cmt.unique_user_id like "+strFbIdUser+" or cmt.unique_user_id like "+strFbIdFriend;*/
			cursor=tinderdatabase.rawQuery(MY_QUERY, null);
			/*String whereClause = DatabaseHandler.FACEBOOK_ID+" =?";
		      String[] whereArgs = new String[] {strFbIdUser,strFbIdFriend};
			cursor = tinderdatabase.query(CHAT_MESSAGE_TABLE, columns, whereClause, whereArgs, null, null, null);*/

			if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
			{

				//    d= new DealDetails();
				do
				{
					ChatMessageList ObjChatMessage=new ChatMessageList();
					Log.i(TAG, "getChatMessages senderid FACEBOOK_ID......"+cursor.getString(cursor.getColumnIndex(DatabaseHandler.FACEBOOK_ID)));
					ObjChatMessage.setStrSenderFacebookId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.FACEBOOK_ID)));
					ObjChatMessage.setStrMessage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.CHAT_MESSAGE)));
					ObjChatMessage.setStrDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.MESSAGE_DATE)));
					ObjChatMessage.setStrSendername(cursor.getString(cursor.getColumnIndex(DatabaseHandler.USERNAME_DATA)));
					//ObjChatMessage.setStrUniqueId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.UNIQUE_USER_ID)));

					listChatMessage.add(ObjChatMessage);
					System.out.println("Getting favorote heritagspot detail");




				}
				while(cursor.moveToNext());
			}
			else
			{
				System.out.println("Cursor is null or empty");
			}
		}catch(SQLiteException lSqlEx)
		{
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:"+lSqlEx.getMessage());

		}
		catch(SQLException lEx)
		{
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:"+lEx.getMessage());
		}
		finally
		{
			if(tinderdatabase != null)
				tinderdatabase.close();
			if(cursor != null)
				cursor.close();
		}
		return listChatMessage;
	}


}
