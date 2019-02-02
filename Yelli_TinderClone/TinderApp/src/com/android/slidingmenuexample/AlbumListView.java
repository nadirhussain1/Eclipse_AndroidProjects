package com.android.slidingmenuexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appdupe.flamerapp.R;
import com.appdupe.flamerapp.pojo.AlubumListData;
import com.appdupe.flamerapp.pojo.FQLFirstSet;
import com.appdupe.flamerapp.pojo.FQLSecondResult;
import com.appdupe.flamerapp.pojo.FacebookAlbumFQLResultData;
import com.appdupe.flamerapp.pojo.ListviewAlubumData;
import com.appdupe.flamerapp.utility.CommonConstant;
import com.appdupe.flamerapp.utility.ScalingUtility;
import com.appdupe.flamerapp.utility.SessionManager;
import com.appdupe.flamerapp.utility.Ultilities;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class AlbumListView  extends Activity implements OnItemClickListener{

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private TextView imageGalleryTextview=null;	
	private ArrayList<ListviewAlubumData>alubumList=null;
	private AlubumListViewAdapter mAlubumListViewAdapter=null;
	private ListView alubumListview=null;
	private  Dialog mdialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View albumView = inflater.inflate(R.layout.albums_list, null, false);
		ScalingUtility.getInstance(this).scaleView(albumView);
		setContentView(albumView);

		alubumListview=(ListView)findViewById(R.id.alubumlistview);
		imageGalleryTextview=(TextView)findViewById(R.id.imagegallerytextview);
		imageGalleryTextview.setText("Albums");
		applyFont();
		initAdapter();
		initSession(savedInstanceState);
	}
	private void applyFont(){
		Typeface HelveticaLTStd_Light=Typeface.createFromAsset(getAssets(),"fonts/HelveticaLTStd-Light.otf");
		imageGalleryTextview.setTypeface(HelveticaLTStd_Light);
		imageGalleryTextview.setTextColor(Color.rgb(255, 255, 255));
		imageGalleryTextview.setTextSize(20);
	}
	private void initAdapter(){
		alubumList=new ArrayList<ListviewAlubumData>();
		mAlubumListViewAdapter=new AlubumListViewAdapter(this, alubumList);
		alubumListview.setAdapter(mAlubumListViewAdapter);
		alubumListview.setOnItemClickListener(this);
	}
	private void initSession(Bundle savedInstanceState){
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null){ 
			if (savedInstanceState != null)  {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
		}
		if (session.isOpened()) {
			getUserAllAlubum();
		}
		else{
			getOpenedSession();
		}
	}

	private void getUserAllAlubum(){
		SessionManager mSessionManager=new SessionManager(this);

		String[] params={mSessionManager.getFacebookId()};
		String fqlQuery=	"{\"query2\":\"select aid,photo_count,cover_pid, name from album where owner = "+params[0]+" and type != 'wall' ORDER BY cover_pid\", \"query1\":\"select pid, src from photo where pid in (SELECT cover_pid from album where owner ="+params[0]+" ORDER BY cover_pid) ORDER BY pid\"}";
		Bundle param = new Bundle();
		param.putString("q", fqlQuery);
		Session session = Session.getActiveSession();
		Request request = new Request(session,"/fql",param,HttpMethod.GET,new Request.Callback(){         
			public void onCompleted(Response response) {
				String []  pramas={response.toString()} ;
				new BackGroundTaskForGetAlubumData().execute(pramas);
			}                  
		});

		Request.executeBatchAsync(request);    

	}
	private class BackGroundTaskForGetAlubumData extends AsyncTask<String , Void, Void>
	{
		private static final String TAG = "BackGroundTaskForGetAlubumData";
		Ultilities mUltilities=new Ultilities();

		private String alubumResponse;
		private AlubumListData mAlubumListData;
		private FacebookAlbumFQLResultData mFaceBookAlubumData=null;
		private FacebookAlbumFQLResultData mFaceBookAlubumData1=null;
		private ArrayList<FQLFirstSet>alubumpickurlList=null;
		private ArrayList<FQLSecondResult>alubumNameList=null;
		private ArrayList<FacebookAlbumFQLResultData>facebookArrayList=null;
		private ListviewAlubumData mListviewAlubumData=null;


		@Override
		protected Void doInBackground(String... params) 
		{
			try 
			{
				alubumResponse=params[0];
				alubumResponse=alubumResponse.substring(alubumResponse.indexOf("state=")+6, alubumResponse.indexOf("}, error:"));
				alubumResponse=alubumResponse.replaceFirst("fql_result_set", "fql_result_set1");
				Gson gson = new Gson();
				mAlubumListData=   gson.fromJson(alubumResponse, AlubumListData.class);
				facebookArrayList=mAlubumListData.getFacebookArrayList();
				mFaceBookAlubumData=facebookArrayList.get(0);
				mFaceBookAlubumData1=facebookArrayList.get(1);

				alubumpickurlList=mFaceBookAlubumData.getImageList();
				alubumNameList=mFaceBookAlubumData1.getAlbumList();

				for (int i = 0; i < alubumpickurlList.size(); i++) {
					String alubumName=alubumNameList.get(i).getName();
					String alubumId=alubumNameList.get(i).getAlubumId();
					int photocount=alubumNameList.get(i).getPhotoCount();
					String pickUrl=alubumpickurlList.get(i).getPickUrl();

					mListviewAlubumData=new ListviewAlubumData();
					mListviewAlubumData.setAlubumid(alubumId);
					mListviewAlubumData.setAlubumName(alubumName);
					mListviewAlubumData.setPickUrl(pickUrl);
					mListviewAlubumData.setPhotoCount(photocount);
					alubumList.add(mListviewAlubumData);
				}

			} catch (Exception e) {
				//logError("BackGroundTaskForGetAlubumData  Exception "+e);
			}	
			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			if (mdialog!=null) {
				mdialog.dismiss();
			}
			mAlubumListViewAdapter.notifyDataSetChanged();
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mdialog=mUltilities.GetProcessDialog(AlbumListView.this);
			mdialog.setCancelable(false);
			mdialog.show();
		}
	}

	private void getOpenedSession(){
		Session.openActiveSession(this, true, statusCallback);
	}



	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(this, CommonConstant.flurryKey);
	}

	@Override
	public void onStop(){
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private class AlubumListViewAdapter extends ArrayAdapter<ListviewAlubumData>{
		private Activity mActivity=null;
		private LayoutInflater mInflater=null;

		public AlubumListViewAdapter(Activity context,List<ListviewAlubumData> objects) {
			super(context, R.layout.album_list_item, objects);

			mActivity=context;
			mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}

		@Override
		public int getCount() {

			return super.getCount();
		}

		@Override
		public ListviewAlubumData getItem(int position) {

			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.album_list_item, null);
				ScalingUtility.getInstance(AlbumListView.this).scaleView(convertView);

				holder.imageview = (ImageView)convertView.findViewById(R.id.albumImageView);
				holder.textview=(TextView)convertView.findViewById(R.id.albumNameView);
				holder.photocount=(TextView)convertView.findViewById(R.id.photoCountView);

				convertView.setTag(holder);

			}
			else {
				holder = (ViewHolder) convertView.getTag();
				holder.imageview.setImageResource(R.drawable.multi_user_icon);

			}

			holder.textview.setId(position);
			holder.imageview.setId(position);
			holder.photocount.setId(position);
			holder.photocount.setText(""+getItem(position).getPhotoCount()+" Photos");
			holder.textview.setText(""+getItem(position).getAlubumName());

			holder.imageview.setImageResource(R.drawable.multi_user_icon);
			Picasso.with(AlbumListView.this) //
			.load(getItem(position).getPickUrl()) //
			.fit()
			.centerInside()
			.into(holder.imageview);

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView  textview;
			TextView photocount;
		}

	}
	private class SessionStatusCallback implements Session.StatusCallback{
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			getUserAllAlubum();

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


		ListviewAlubumData mAlubumData=(ListviewAlubumData)arg0.getItemAtPosition(arg2);
		String alubumId=mAlubumData.getAlubumid();
		String alubumName=mAlubumData.getAlubumName();

		Bundle mBundle=new Bundle();
		mBundle.putString(CommonConstant.ALUBUMNAME, alubumName);
		mBundle.putString(CommonConstant.ALUBUMID, alubumId);

		if (mAlubumData.getPhotoCount()>0){
			Intent mIntent=new Intent(AlbumListView.this, AlbumGridviewAcitivity.class);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
		}
	}

}
