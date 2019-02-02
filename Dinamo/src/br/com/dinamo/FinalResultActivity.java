package br.com.dinamo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.data.model.BoughtProduct;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.SoldProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class FinalResultActivity extends Activity{
	TextView dayPeriodSelectorView=null;
	TextView weekPeriodSelectorView=null;
	TextView monthPeriodSelectorView=null;
	TextView yearPeriodSelectorView=null;
	TextView selectedPeriodValueView=null;
	TextView soldProductsValueView=null;
	TextView boughtProductsValueView=null;
	TextView surplusValueView=null;
	TextView expenseValueView=null;
	TextView finalAmountValueView=null;

	private int selectedPeriodType=3;
	private int totalDaysInMonth=30;
	private int currentDayOfMonth;
	private int currentMonth;
	private int currentWeek;

	private int startDateOfWeek;
	private int endDateOfWeek;
	private int startMonthOfWeek;
	private int endMonthOfWeek;

	private int currentYear;
	private int selectedYear;
	private int selectedMonth=0;
	private int currentDayOfWeek;

	private double soldItemsPrice=0;
	private double boughtItemsPrice=0;
	private double expenseItemsPrice=0; 

	private RelativeLayout boughtItemsLayout=null;
	private RelativeLayout soldItemsLayout=null;
	private RelativeLayout expenseItemsLayout=null;
	private RelativeLayout surplusLayout=null;
	private RelativeLayout finalLayout=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View sellEventsView = inflater.inflate(R.layout.final_result_layout, null, false);
		ScalingUtility.getInstance().scaleView(sellEventsView);
		setContentView(sellEventsView);

		SharedData.getInstance().sendScreenName("Results Screen");
		applyFontToTextOnScreen();
		adjustTextSizeOfViews();
		displayRealData();
		initClicks();

	}
	private void displayRealData(){
		Calendar calendar=Calendar.getInstance();
		totalDaysInMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		currentDayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
		currentDayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
		currentMonth=calendar.get(Calendar.MONTH);
		currentWeek=calendar.get(Calendar.WEEK_OF_YEAR);
		currentYear=calendar.get(Calendar.YEAR);
		selectedYear=currentYear;
		selectedMonth=currentMonth;
		updateMonth();
	}
	private void updateYear(){
		selectedPeriodValueView.setText(""+selectedYear);
		calculateYearlyPrices();
		updateTextViewsValues();
	}
	private void updateDay(){
		if(currentDayOfMonth>=totalDaysInMonth){
			currentDayOfMonth=totalDaysInMonth-1;
		}else if(currentDayOfMonth<1){
			currentDayOfMonth=1;
		}
		if(currentDayOfWeek<1 || currentDayOfWeek>7){
			currentDayOfWeek=1;
		}
		CalculateDaysPrice();
		String currentDayName=SharedData.getInstance().daysNames[currentDayOfWeek-1]+" "+currentDayOfMonth+"/"+(selectedMonth+1)+"/"+currentYear;
		selectedPeriodValueView.setText(currentDayName);
		updateTextViewsValues();
	}
	private void updateWeek(){
		if(currentWeek<1){
			currentWeek=1;
		}else if(currentWeek>52){
			currentWeek=52;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);
		calendar.set(Calendar.YEAR, currentYear);

		startDateOfWeek=calendar.get(Calendar.DAY_OF_MONTH);
		startMonthOfWeek=calendar.get(Calendar.MONTH);
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMM yyyy"); // PST`
		Date startDate = calendar.getTime();
		String dateFormatString = formatter.format(startDate);

		calendar.add(Calendar.DATE, 6);
		Date enddate = calendar.getTime();
		dateFormatString=dateFormatString+"-"+formatter.format(enddate);

		endDateOfWeek=calendar.get(Calendar.DAY_OF_MONTH);
		endMonthOfWeek=calendar.get(Calendar.MONTH);

		calculateWeekPrices();
		selectedPeriodValueView.setText(dateFormatString);
		updateTextViewsValues();
	}
	private void updateMonth(){
		if(selectedMonth>11){
			selectedMonth=11;
		}else if(selectedMonth<0){
			selectedMonth=0;
		}
		calculateMonthsPrices();
		String currentMonthName=SharedData.getInstance().monthsNameList[selectedMonth]+"/"+currentYear;
		selectedPeriodValueView.setText(currentMonthName);
		updateTextViewsValues();
	}
	private void updateTextViewsValues(){
		double finalPriceValue=soldItemsPrice-boughtItemsPrice-expenseItemsPrice;
		double surplusValue=(soldItemsPrice-boughtItemsPrice);

		String  boughtItemsPriceString=String.format("%.2f", boughtItemsPrice);
		String  soldItemPriceString=String.format("%.2f", soldItemsPrice);
		String  surplusPriceString=String.format("%.2f", surplusValue);
		String  expenseValueString=String.format("%.2f", expenseItemsPrice);
		String  finalPriceString=String.format("%.2f", finalPriceValue);

		boughtItemsPriceString=boughtItemsPriceString.replace(".", ",");
		soldItemPriceString=soldItemPriceString.replace(".", ",");
		surplusPriceString=surplusPriceString.replace(".", ",");
		expenseValueString=expenseValueString.replace(".", ",");
		finalPriceString=finalPriceString.replace(".", ",");

		boughtProductsValueView.setText("-"+boughtItemsPriceString);
		soldProductsValueView.setText(""+soldItemPriceString);
		expenseValueView.setText("-"+expenseValueString);
		surplusValueView.setText(""+surplusPriceString);
		finalAmountValueView.setText(""+finalPriceString);

		surplusLayout.setBackgroundColor(Color.parseColor("#F04C4E"));
		finalLayout.setBackgroundColor(Color.parseColor("#F04C4E"));

		if(surplusValue>=0){
			surplusLayout.setBackgroundColor(Color.parseColor("#1BA58D"));
		}
		if(finalPriceValue>=0){
			finalLayout.setBackgroundColor(Color.parseColor("#1BA58D"));
		}
	}
	private void CalculateDaysPrice(){
		boughtItemsPrice=0;
		soldItemsPrice=0;
		expenseItemsPrice=0;

		SharedData.getInstance().boughtEventsList=new ArrayList<BoughtProduct>();
		SharedData.getInstance().soldEventsList=new ArrayList<SoldProduct>();
		SharedData.getInstance().expenseEventsList=new ArrayList<ExpenseProduct>();

		Calendar calendar=Calendar.getInstance();
		for(int count=0;count<UserDataManager.getInstance().boughtEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().boughtEventsList.get(count).getDate());
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH);
			int day=calendar.get(Calendar.DAY_OF_MONTH);
			if(day==currentDayOfMonth && month==selectedMonth && year==currentYear){
				boughtItemsPrice+=UserDataManager.getInstance().boughtEventsList.get(count).getPriceValue();
				SharedData.getInstance().boughtEventsList.add(UserDataManager.getInstance().boughtEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().soldEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().soldEventsList.get(count).getDate());
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH);
			int day=calendar.get(Calendar.DAY_OF_MONTH);

			if(day==currentDayOfMonth && month==selectedMonth && year==currentYear){	
				double itemAmount=UserDataManager.getInstance().soldEventsList.get(count).getPriceValue()*Double.valueOf(UserDataManager.getInstance().soldEventsList.get(count).getQuantity());
				soldItemsPrice+=itemAmount;
				SharedData.getInstance().soldEventsList.add(UserDataManager.getInstance().soldEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().expenseEventsList.size();count++){
			Date startDate=UserDataManager.getInstance().expenseEventsList.get(count).getDate();
			Date endDate=UserDataManager.getInstance().expenseEventsList.get(count).getEndDate();
			String periodcity=UserDataManager.getInstance().expenseEventsList.get(count).getPeriodicityType().getName();

			String filterDateString=""+currentYear+"-"+(selectedMonth+1)+"-"+currentDayOfMonth+" 23:59:59";
			Date filterDate=SharedData.getInstance().convertToDate(filterDateString);

			if(endDate!=null){
				calendar.setTime(endDate);
				String endDateString=""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
				endDate=SharedData.getInstance().convertToDate(endDateString);
			}


			if(!startDate.after(filterDate) &&
					((endDate==null) || endDate.after(filterDate)))
			{

				double itemAmount=0;

				if(periodcity.equalsIgnoreCase(DinamoConstants.WEEK_PERIODCITY)){
					itemAmount=	(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/7);
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.MONTH_PERIODICITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/30);
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.ANNUAL_PERIODICITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/365);
					expenseItemsPrice+=itemAmount;
				}else{
					itemAmount=UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue();
					expenseItemsPrice+=itemAmount;
				}

				Date nextDueDate=MainScreenActivity.findNextDueDate(UserDataManager.getInstance().expenseEventsList.get(count));	
				UserDataManager.getInstance().expenseEventsList.get(count).setNextDueDate(nextDueDate);
				SharedData.getInstance().expenseEventsList.add(UserDataManager.getInstance().expenseEventsList.get(count));
			}

		}
	}
	private void calculateWeekPrices(){
		boughtItemsPrice=0;
		soldItemsPrice=0;
		expenseItemsPrice=0;
		SharedData.getInstance().boughtEventsList=new ArrayList<BoughtProduct>();
		SharedData.getInstance().soldEventsList=new ArrayList<SoldProduct>();
		SharedData.getInstance().expenseEventsList=new ArrayList<ExpenseProduct>();

		Calendar calendar=Calendar.getInstance();

		for(int count=0;count<UserDataManager.getInstance().boughtEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().boughtEventsList.get(count).getDate());
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH);
			int day=calendar.get(Calendar.DAY_OF_MONTH);
			//if((day<=endDateOfWeek && day>=startDateOfWeek) && (month==startMonthOfWeek || month==endMonthOfWeek) && year==currentYear){
			if((startMonthOfWeek==endMonthOfWeek && month==startMonthOfWeek && (day<=endDateOfWeek && day>=startDateOfWeek)) || (startMonthOfWeek!=endMonthOfWeek && (day>=startDateOfWeek || day<=endDateOfWeek) && (month==startMonthOfWeek || month==endMonthOfWeek)) && year==currentYear){
				boughtItemsPrice+=UserDataManager.getInstance().boughtEventsList.get(count).getPriceValue();
				SharedData.getInstance().boughtEventsList.add(UserDataManager.getInstance().boughtEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().soldEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().soldEventsList.get(count).getDate());
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH);
			int day=calendar.get(Calendar.DAY_OF_MONTH);

			//if((day<=endDateOfWeek && day>=startDateOfWeek) && (month==startMonthOfWeek || month==endMonthOfWeek) && year==currentYear){
			if((startMonthOfWeek==endMonthOfWeek && month==startMonthOfWeek && (day<=endDateOfWeek && day>=startDateOfWeek)) || (startMonthOfWeek!=endMonthOfWeek && (day>=startDateOfWeek || day<=endDateOfWeek) && (month==startMonthOfWeek || month==endMonthOfWeek)) && year==currentYear){
				double itemAmount=UserDataManager.getInstance().soldEventsList.get(count).getPriceValue()*Double.valueOf(UserDataManager.getInstance().soldEventsList.get(count).getQuantity());
				soldItemsPrice+=itemAmount;
				SharedData.getInstance().soldEventsList.add(UserDataManager.getInstance().soldEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().expenseEventsList.size();count++){
			Date startDate=UserDataManager.getInstance().expenseEventsList.get(count).getDate();
			Date endDate=UserDataManager.getInstance().expenseEventsList.get(count).getEndDate();
			String periodcity=UserDataManager.getInstance().expenseEventsList.get(count).getPeriodicityType().getName();

			String filterDateString=""+currentYear+"-"+(endMonthOfWeek+1)+"-"+endDateOfWeek+" 00:00:00";
			Date filterDate=SharedData.getInstance().convertToDate(filterDateString);

			if(endDate!=null){
				calendar.setTime(endDate);
				String endDateString=""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
				endDate=SharedData.getInstance().convertToDate(endDateString);
			}


			if(!startDate.after(filterDate) &&
					((endDate==null) || endDate.after(filterDate)))
			{
				double itemAmount=0;

				if(periodcity.equalsIgnoreCase(DinamoConstants.WEEK_PERIODCITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue());
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.MONTH_PERIODICITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/4);
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.ANNUAL_PERIODICITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/52);		
					expenseItemsPrice+=itemAmount;
				}else{
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*7);
					expenseItemsPrice+=itemAmount;
				}

				Date nextDueDate=MainScreenActivity.findNextDueDate(UserDataManager.getInstance().expenseEventsList.get(count));	
				UserDataManager.getInstance().expenseEventsList.get(count).setNextDueDate(nextDueDate);
				SharedData.getInstance().expenseEventsList.add(UserDataManager.getInstance().expenseEventsList.get(count));
			}

		}
	}
	private void calculateMonthsPrices(){
		boughtItemsPrice=0;
		soldItemsPrice=0;
		expenseItemsPrice=0;

		SharedData.getInstance().boughtEventsList=new ArrayList<BoughtProduct>();
		SharedData.getInstance().soldEventsList=new ArrayList<SoldProduct>();
		SharedData.getInstance().expenseEventsList=new ArrayList<ExpenseProduct>();

		Calendar calendar=Calendar.getInstance();
		for(int count=0;count<UserDataManager.getInstance().boughtEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().boughtEventsList.get(count).getDate());
			if(calendar.get(Calendar.MONTH)==selectedMonth && calendar.get(Calendar.YEAR)==currentYear){
				boughtItemsPrice+=UserDataManager.getInstance().boughtEventsList.get(count).getPriceValue();
				SharedData.getInstance().boughtEventsList.add(UserDataManager.getInstance().boughtEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().soldEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().soldEventsList.get(count).getDate());
			if(calendar.get(Calendar.MONTH)==selectedMonth && calendar.get(Calendar.YEAR)==currentYear){	
				double itemAmount=UserDataManager.getInstance().soldEventsList.get(count).getPriceValue()*Double.valueOf(UserDataManager.getInstance().soldEventsList.get(count).getQuantity());
				soldItemsPrice+=itemAmount;
				SharedData.getInstance().soldEventsList.add(UserDataManager.getInstance().soldEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().expenseEventsList.size();count++){
			Date startDate=UserDataManager.getInstance().expenseEventsList.get(count).getDate();
			Date endDate=UserDataManager.getInstance().expenseEventsList.get(count).getEndDate();
			String periodcity=UserDataManager.getInstance().expenseEventsList.get(count).getPeriodicityType().getName();

			calendar.setTime(new Date());
			calendar.set(Calendar.MONTH, selectedMonth);
			int totalDaysOfMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			String filterDateString=""+currentYear+"-"+(selectedMonth+1)+"-"+totalDaysOfMonth+" 00:00:00";
			Date filterDate=SharedData.getInstance().convertToDate(filterDateString);

			if(endDate!=null){
				calendar.setTime(endDate);
				String endDateString=""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
				endDate=SharedData.getInstance().convertToDate(endDateString);
			}

			if(!startDate.after(filterDate) &&
					((endDate==null) || endDate.after(filterDate))){		

				double itemAmount=0;

				if(periodcity.equalsIgnoreCase(DinamoConstants.WEEK_PERIODCITY)){
					if(calculateDaysPassed(startDate, filterDate)>=totalDaysOfMonth){
						itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*4);
					}else{
						itemAmount=UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*calculateWeeksPassed(startDate,filterDate);
					}

					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.MONTH_PERIODICITY)){
					itemAmount=UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue();
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.ANNUAL_PERIODICITY)){
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()/12);
					expenseItemsPrice+=itemAmount;
				}else{
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*30);
					expenseItemsPrice+=itemAmount;
				}
				Date nextDueDate=MainScreenActivity.findNextDueDate(UserDataManager.getInstance().expenseEventsList.get(count));	
				UserDataManager.getInstance().expenseEventsList.get(count).setNextDueDate(nextDueDate);
				SharedData.getInstance().expenseEventsList.add(UserDataManager.getInstance().expenseEventsList.get(count));
			}

		}
	}
	private void calculateYearlyPrices(){
		boughtItemsPrice=0;
		soldItemsPrice=0;
		expenseItemsPrice=0;

		SharedData.getInstance().boughtEventsList=new ArrayList<BoughtProduct>();
		SharedData.getInstance().soldEventsList=new ArrayList<SoldProduct>();
		SharedData.getInstance().expenseEventsList=new ArrayList<ExpenseProduct>();

		Calendar calendar=Calendar.getInstance();
		for(int count=0;count<UserDataManager.getInstance().boughtEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().boughtEventsList.get(count).getDate());
			if(calendar.get(Calendar.YEAR)==selectedYear){
				boughtItemsPrice+=UserDataManager.getInstance().boughtEventsList.get(count).getPriceValue();
				SharedData.getInstance().boughtEventsList.add(UserDataManager.getInstance().boughtEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().soldEventsList.size();count++){
			calendar.setTime(UserDataManager.getInstance().soldEventsList.get(count).getDate());
			if(calendar.get(Calendar.YEAR)==selectedYear){
				double itemAmount=UserDataManager.getInstance().soldEventsList.get(count).getPriceValue()*Double.valueOf(UserDataManager.getInstance().soldEventsList.get(count).getQuantity());
				soldItemsPrice+=itemAmount;
				SharedData.getInstance().soldEventsList.add(UserDataManager.getInstance().soldEventsList.get(count));
			}
		}
		for(int count=0;count<UserDataManager.getInstance().expenseEventsList.size();count++){
			Date startDate=UserDataManager.getInstance().expenseEventsList.get(count).getDate();
			Date endDate=UserDataManager.getInstance().expenseEventsList.get(count).getEndDate();
			String periodcity=UserDataManager.getInstance().expenseEventsList.get(count).getPeriodicityType().getName();

			String filterDateString=""+selectedYear+"-"+(12)+"-"+31+" 00:00:00";
			Date filterDate=SharedData.getInstance().convertToDate(filterDateString);

			if(endDate!=null){
				calendar.setTime(endDate);
				String endDateString=""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
				endDate=SharedData.getInstance().convertToDate(endDateString);
			}


			if(!startDate.after(filterDate) &&
					((endDate==null) || endDate.after(filterDate))){

				double itemAmount=0;

				if(periodcity.equalsIgnoreCase(DinamoConstants.WEEK_PERIODCITY)){
					if(calculateDaysPassed(startDate, filterDate)>=365){
						itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*52);
					}else{
						itemAmount=UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*calculateWeeksPassed(startDate, filterDate);
					}
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.MONTH_PERIODICITY)){
					calendar.setTime(startDate);
					int startMonth=calendar.get(Calendar.MONTH);
					int startYear=calendar.get(Calendar.YEAR);
					if(startYear<selectedYear || startMonth==0){
						itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*12);
					}else{
						itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*(12-startMonth));
					}
					expenseItemsPrice+=itemAmount;
				}else if(periodcity.equalsIgnoreCase(DinamoConstants.ANNUAL_PERIODICITY)){
					itemAmount=UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue();
					expenseItemsPrice+=itemAmount;
				}else{
					itemAmount=(UserDataManager.getInstance().expenseEventsList.get(count).getPriceValue()*365);
					expenseItemsPrice+=itemAmount;
				}

				Date nextDueDate=MainScreenActivity.findNextDueDate(UserDataManager.getInstance().expenseEventsList.get(count));	
				UserDataManager.getInstance().expenseEventsList.get(count).setNextDueDate(nextDueDate);
				SharedData.getInstance().expenseEventsList.add(UserDataManager.getInstance().expenseEventsList.get(count));
			}

		}
	}
	private void adjustTextSizeOfViews(){
		TextView textView =(TextView)findViewById(R.id.soldEventsPriceLabel);
		SpannableString spannableString=  new SpannableString("VENDAS (R$)");
		spannableString.setSpan(new RelativeSizeSpan(1.3f),0,6, 0); 	
		textView.setText(spannableString);

		textView =(TextView)findViewById(R.id.boughtEventsPriceLabel);
		spannableString=  new SpannableString("COMPRAS (R$)");
		spannableString.setSpan(new RelativeSizeSpan(1.3f),0,7, 0); 	
		textView.setText(spannableString);

		textView =(TextView)findViewById(R.id.expensesTextLabel);
		spannableString=  new SpannableString(getResources().getString(R.string.expenses_result_title));
		spannableString.setSpan(new RelativeSizeSpan(1.3f),0,18, 0); 	
		textView.setText(spannableString);
	}
	private void applyFontToTextOnScreen(){
		dayPeriodSelectorView=(TextView)findViewById(R.id.DayLabelArea);
		weekPeriodSelectorView=(TextView)findViewById(R.id.WeekLabelArea);
		monthPeriodSelectorView=(TextView)findViewById(R.id.monthLabelArea);
		yearPeriodSelectorView=(TextView)findViewById(R.id.yearLabelArea);
		selectedPeriodValueView=(TextView)findViewById(R.id.PeriodValueTextView);
		soldProductsValueView=(TextView)findViewById(R.id.soldEventsAmount);
		boughtProductsValueView=(TextView)findViewById(R.id.BoughtEventsAmount);
		surplusValueView=(TextView)findViewById(R.id.surplusAmountTextView);
		expenseValueView=(TextView)findViewById(R.id.expenseAmount);
		finalAmountValueView=(TextView)findViewById(R.id.finalAmountValueTextView);
		boughtItemsLayout=(RelativeLayout)findViewById(R.id.boughtEventsLayout);
		soldItemsLayout=(RelativeLayout)findViewById(R.id.soldEventsLayout);
		expenseItemsLayout=(RelativeLayout)findViewById(R.id.expensesLayout);
		surplusLayout=(RelativeLayout)findViewById(R.id.surplusLayout);
		finalLayout=(RelativeLayout)findViewById(R.id.finalLayout);

		View leftArrowClickedArea=findViewById(R.id.leftArrowClickedArea);
		View rightArrowClickedArea=findViewById(R.id.rightArrowClickedArea);
		boughtItemsLayout.setOnClickListener(boughtEventsMainListener);
		soldItemsLayout.setOnClickListener(soldEventsMainListener);
		expenseItemsLayout.setOnClickListener(expenseEventsMainListener);

		leftArrowClickedArea.setOnClickListener(decrementPeriodListener);
		rightArrowClickedArea.setOnClickListener(incrementPeriodListener);

		SharedData.getInstance().applyFontToTextView(dayPeriodSelectorView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		SharedData.getInstance().applyFontToTextView(weekPeriodSelectorView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		SharedData.getInstance().applyFontToTextView(monthPeriodSelectorView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		SharedData.getInstance().applyFontToTextView(yearPeriodSelectorView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		SharedData.getInstance().applyFontToTextView(selectedPeriodValueView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		SharedData.getInstance().applyFontToTextView(soldProductsValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		SharedData.getInstance().applyFontToTextView(boughtProductsValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		SharedData.getInstance().applyFontToTextView(expenseValueView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		SharedData.getInstance().applyFontToTextView(surplusValueView, DinamoConstants.HELVETICA_NEUE_THINCOND);
		SharedData.getInstance().applyFontToTextView(finalAmountValueView, DinamoConstants.HELVETICA_NEUE_THINCOND);

		TextView textView =(TextView)findViewById(R.id.reportsHeadTextView);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView =(TextView)findViewById(R.id.soldEventsPriceLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView =(TextView)findViewById(R.id.boughtEventsPriceLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView =(TextView)findViewById(R.id.surplusTextLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);
		textView =(TextView)findViewById(R.id.expensesTextLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView =(TextView)findViewById(R.id.finalAmountStaticTextLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_BOLDCOND);		

	}
	private void initClicks(){
		dayPeriodSelectorView.setOnClickListener(periodTypeSelectorListener);
		weekPeriodSelectorView.setOnClickListener(periodTypeSelectorListener);
		monthPeriodSelectorView.setOnClickListener(periodTypeSelectorListener);
		yearPeriodSelectorView.setOnClickListener(periodTypeSelectorListener);
		findViewById(R.id.backClickArea).setOnClickListener(backIconClickListener);

	}
	private int calculateDaysPassed(Date startDate,Date dueDate){
		long difference=dueDate.getTime()-startDate.getTime();
		int days=(int) (difference/(1000*60*60*24));

		return days;
	}
	
	private int calculateWeeksPassed(Date startDate,Date dueDate){
		double daysInWeek=7.0;
		return (int) Math.ceil(calculateDaysPassed(startDate,dueDate)/daysInWeek);
	}
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			FinalResultActivity.this.finish();
		}
	};
	OnClickListener boughtEventsMainListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainIntent=new Intent(FinalResultActivity.this, BuyEventsMainActivity.class);
			mainIntent.putExtra("HideAdd", true);
			mainIntent.putExtra("date", selectedPeriodValueView.getText().toString());
			startActivity(mainIntent);		
		}
	};
	OnClickListener soldEventsMainListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainIntent=new Intent(FinalResultActivity.this, SellEventsMainActivity.class);
			mainIntent.putExtra("date", selectedPeriodValueView.getText().toString());
			mainIntent.putExtra("HideAdd", true);
			startActivity(mainIntent);		
		}
	};
	OnClickListener expenseEventsMainListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainIntent=new Intent(FinalResultActivity.this, ExpensesMainActivity.class);
			mainIntent.putExtra("date", selectedPeriodValueView.getText().toString());
			mainIntent.putExtra("HideAdd", true);
			startActivity(mainIntent);
		}
	};
	OnClickListener periodTypeSelectorListener=new OnClickListener() {

		@Override
		public void onClick(View clickedView) {
			dayPeriodSelectorView.setBackgroundColor(Color.parseColor("#111A2F"));
			weekPeriodSelectorView.setBackgroundColor(Color.parseColor("#111A2F"));
			monthPeriodSelectorView.setBackgroundColor(Color.parseColor("#111A2F"));
			yearPeriodSelectorView.setBackgroundColor(Color.parseColor("#111A2F"));

			clickedView.setBackgroundColor(Color.parseColor("#1D2843"));

			switch (clickedView.getId()) {
			case R.id.DayLabelArea :
				selectedPeriodType=1;
				updateDay();
				break;
			case R.id.WeekLabelArea :
				selectedPeriodType=2;
				updateWeek();
				break;
			case R.id.monthLabelArea :
				selectedPeriodType=3;
				updateMonth();
				break;
			case R.id.yearLabelArea :
				selectedPeriodType=4;
				updateYear();
				break;
			default:
				break;
			}
		}
	};
	OnClickListener decrementPeriodListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(selectedPeriodType==1){
				currentDayOfMonth--;
				currentDayOfWeek--;
				updateDay();
			}else if(selectedPeriodType==2){
				currentWeek--;
				updateWeek();
			}
			else if(selectedPeriodType==3){
				selectedMonth--;
				updateMonth();
			}else if(selectedPeriodType==4){
				selectedYear--;
				updateYear();
			}

		}
	};
	OnClickListener incrementPeriodListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(selectedPeriodType==1){
				currentDayOfMonth++;
				currentDayOfWeek++;
				updateDay();
			}else if(selectedPeriodType==2){
				currentWeek++;
				updateWeek();
			}
			else if(selectedPeriodType==3){
				selectedMonth++;
				updateMonth();
			}else if(selectedPeriodType==4){
				selectedYear++;
				updateYear();
			}
		}
	};
}
