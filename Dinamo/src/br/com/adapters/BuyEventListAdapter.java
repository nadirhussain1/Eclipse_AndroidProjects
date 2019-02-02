package br.com.adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.data.model.BoughtProduct;
import br.com.data.model.ProductDateComparator;
import br.com.dinamo.R;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class BuyEventListAdapter extends BaseAdapter {
	private ArrayList<BoughtProduct> list=null;

	public BuyEventListAdapter(){
		list = SharedData.getInstance().boughtEventsList;
		sortOutValidItems();
	}
	private void sortOutValidItems(){
		Collections.sort(list,new ProductDateComparator());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public void refreshDataOfAdapter(){
		sortOutValidItems();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.bought_event_list_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);

		}

		TextView eventDateTextView=(TextView)convertView.findViewById(R.id.eventDate);
		TextView establishmentNameTextView=(TextView)convertView.findViewById(R.id.establishmentName);
		TextView eventBillAmountView=(TextView)convertView.findViewById(R.id.eventBillAmount);
		ImageView eventPhoto=(ImageView)convertView.findViewById(R.id.eventPhoto);

		if(list.get(position).getProductPhoto() !=null){
			eventPhoto.setImageResource(R.drawable.camera_icon);
		}else{
			eventPhoto.setImageBitmap(null);
		}

		Date date=list.get(position).getDate();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);

		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		String formatedDateString=""+day+"/"+(monthOfYear+1)+"/"+year;


		String formatValue=String.format("%.2f", list.get(position).getPriceValue());
		configureAmountSize(eventBillAmountView,"R$ "+formatValue);
		eventDateTextView.setText(""+formatedDateString);
		establishmentNameTextView.setText(""+list.get(position).getEstablishment().getName());
		return convertView;
	}
	private void configureAmountSize(TextView eventBillAmountView,String amount){
		amount=amount.replace(".", ",");
		SpannableString spannableString=  new SpannableString(amount);
		spannableString.setSpan(new RelativeSizeSpan(1.25f),2,getIndexOfDecimal(amount), 0); 	
		eventBillAmountView.setText(spannableString);
	}
	private int getIndexOfDecimal(String amount){
		int index=amount.indexOf(",");
		if(index==-1){
			index=amount.length();
		}
		return index;
	}


}
