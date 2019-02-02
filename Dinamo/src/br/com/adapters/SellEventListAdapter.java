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
import android.widget.TextView;
import br.com.data.model.ProductDateComparator;
import br.com.data.model.SoldProduct;
import br.com.dinamo.R;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class SellEventListAdapter extends BaseAdapter{

	private ArrayList<SoldProduct> list=null;
	
	public SellEventListAdapter(){
		list = SharedData.getInstance().soldEventsList;
		sortOutValidItems();
	}
	private void sortOutValidItems(){
		Collections.sort(list,new ProductDateComparator());	
	}
	
	public void refreshDataAdapter(){
		sortOutValidItems();
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sell_events_list_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);

		}
		TextView eventDateTextView=(TextView)convertView.findViewById(R.id.eventDate);
		TextView productNameTextView=(TextView)convertView.findViewById(R.id.productName);
		TextView eventBillAmountView=(TextView)convertView.findViewById(R.id.eventBillAmount);
		

		Date date=list.get(position).getDate();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);

		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		String formatedDateString=""+day+"/"+(monthOfYear+1)+"/"+year;

		String formatValue=String.format("%.2f", list.get(position).getPriceValue()*Integer.valueOf(list.get(position).getQuantity()));
		configureAmountSize(eventBillAmountView,"R$ "+formatValue);
		eventDateTextView.setText(""+formatedDateString);
		productNameTextView.setText(list.get(position).getProduct().getName());

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
