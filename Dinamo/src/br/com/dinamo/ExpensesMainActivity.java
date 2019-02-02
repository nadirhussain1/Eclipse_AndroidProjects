package br.com.dinamo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.adapters.ExpensesListAdapter;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.UserDataManager;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class ExpensesMainActivity extends Activity {
	public  static ExpensesListAdapter listAdapter=null;
	private boolean hideAddOption=false;
	private String date = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedData.getInstance().setContext(this);

		hideAddOption=getIntent().getBooleanExtra("HideAdd", false);
		date = getIntent().getStringExtra("date");
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View boughtEventsView = inflater.inflate(R.layout.expenses_main_screen_layout, null, false);
		ScalingUtility.getInstance().scaleView(boughtEventsView);
		setContentView(boughtEventsView);
        
		SharedData.getInstance().sendScreenName("Expense Events Main Screen");
		applyFontsToTextOnScreen();
		initUserClicksListeners();
	}
	private void applyFontsToTextOnScreen(){
		TextView textView=(TextView)findViewById(R.id.expenseHeadLabel);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		textView=(TextView)findViewById(R.id.addExpensesButton);
		SharedData.getInstance().applyFontToTextView(textView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
	}
	private void initUserClicksListeners(){
		Button addExpenses=(Button)findViewById(R.id.addExpensesButton);
		View backIconImage=(View)findViewById(R.id.backClickArea);
		ListView expenseListView=(ListView)findViewById(R.id.expensesListView);
		listAdapter=new ExpensesListAdapter();
		expenseListView.setAdapter(listAdapter);
		expenseListView.setOnItemClickListener(expenseListRowCliclListenr);	
		backIconImage.setOnClickListener(backIconClickListener);
		
		if(hideAddOption){
			addExpenses.setVisibility(View.GONE);
		}else{
			addExpenses.setOnClickListener(addExpenseListener);
		}
	}
	
	public static void refreshAdapter(boolean isDeleted){
		if(isDeleted){
			for(int count=0;count<UserDataManager.getInstance().expenseEventsList.size();count++){
				ExpenseProduct clone=UserDataManager.getInstance().expenseEventsList.get(count);
				if(clone.isDeleted){
					UserDataManager.getInstance().expenseEventsList.remove(count);
					break;
				}
			}
		}
		listAdapter.refreshDataAdapter();

	}
	OnClickListener addExpenseListener=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			SharedData.getInstance().sendEventNameToAnalytics("Add Expense Event Button 1");
			Intent mainIntent = new Intent(ExpensesMainActivity.this, AddExpenseActivity.class);
			mainIntent.putExtra("ID", DinamoConstants.ADD_EVENT_ACTIVITY_ID);
			startActivity(mainIntent);
		}
	};
	OnClickListener backIconClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnItemClickListener expenseListRowCliclListenr=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			SharedData.getInstance().sendEventNameToAnalytics("Expense Event Detail Button");
			Intent mainIntent = new Intent(ExpensesMainActivity.this, ExpenseDetailsActivity.class);
			mainIntent.putExtra("POSITION",position);
			startActivity(mainIntent);
		}
	};
}


