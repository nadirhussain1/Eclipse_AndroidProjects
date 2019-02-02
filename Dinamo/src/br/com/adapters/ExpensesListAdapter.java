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
import br.com.data.model.ExpenseDateComparator;
import br.com.data.model.ExpenseProduct;
import br.com.dinamo.R;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class ExpensesListAdapter extends BaseAdapter{

	private ArrayList<ExpenseProduct> list=null;

	public ExpensesListAdapter(){
		list = SharedData.getInstance().expenseEventsList;
		sortOutValidItems();
	}
	private void sortOutValidItems(){	
		Collections.sort(list,new ExpenseDateComparator());	
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expenses_list_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);		
		}

		TextView expenseDueDate=(TextView)convertView.findViewById(R.id.eventDate);
		TextView expenseCatName=(TextView)convertView.findViewById(R.id.CatName);
		TextView periodNamView=(TextView)convertView.findViewById(R.id.PeriodName);
		TextView expenseBillAmountView=(TextView)convertView.findViewById(R.id.eventBillAmount);

		
		double amount=list.get(position).getPriceValue();
		
		String formatValue=String.format("%.2f", amount);
		configureAmountSize(expenseBillAmountView,"R$ "+formatValue);

		expenseDueDate.setText(""+getNextDueDate(list.get(position).getNextDueDate()));
		expenseCatName.setText(list.get(position).getCategory().getName()+":");
		periodNamView.setText(list.get(position).getPeriodicityType().getName());
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
	private String getNextDueDate(Date nextDueDate){
		String nextDateString="";
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(nextDueDate);
		
		nextDateString=""+calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
		
		return nextDateString;
		
	}
	

}

