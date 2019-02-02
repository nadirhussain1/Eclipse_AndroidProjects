package br.com.data.model;

import java.util.ArrayList;

public class UserDataManager {
	private static UserDataManager dataManager=null;
    public ArrayList<DinamoObject>paymentMethods;
    public ArrayList<DinamoObject>establishments;
    public ArrayList<DinamoObject>periodicity;
    public ArrayList<DinamoObject>products;
    public ArrayList<DinamoObject>catagories;
    
    public ArrayList<BoughtProduct>boughtEventsList;
    public ArrayList<SoldProduct> soldEventsList;
    public ArrayList<ExpenseProduct> expenseEventsList;
    
	public static UserDataManager getInstance(){
		if(dataManager==null){
			dataManager=new UserDataManager();
		}
		return dataManager;
	}
	private UserDataManager(){
		paymentMethods=new ArrayList<DinamoObject>();
		establishments=new ArrayList<DinamoObject>();
		periodicity=new ArrayList<DinamoObject>();
		products=new ArrayList<DinamoObject>();
		catagories=new ArrayList<DinamoObject>();
		boughtEventsList=new ArrayList<BoughtProduct>();
		soldEventsList=new ArrayList<SoldProduct>();
		expenseEventsList=new ArrayList<ExpenseProduct>();
	}
	public void onDestroy(){
		paymentMethods.clear();
		establishments.clear();
		periodicity.clear();
		products.clear();
		catagories.clear();
		boughtEventsList.clear();
		soldEventsList.clear();
		expenseEventsList.clear();
		dataManager=null;
	}
}
