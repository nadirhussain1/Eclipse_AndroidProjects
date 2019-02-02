package br.com.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.data.model.BoughtProduct;
import br.com.data.model.DinamoObject;
import br.com.data.model.ExpenseProduct;
import br.com.data.model.SoldProduct;
import br.com.data.model.User;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.global.TableNameConstants;



public class DinamoDatabaseHelper {
	public static DinamoDatabaseHelper databaseHelper=null;
	private final  int DATABASE_VERSION = 1;
	private SQLiteDatabase dbObject = null;

	public static DinamoDatabaseHelper getInstance() {
		if (databaseHelper == null) {
			String userId=SharedData.getInstance().currentUser.getUserId();
			databaseHelper = new DinamoDatabaseHelper(userId);
		}
		return databaseHelper;
	}
	public static DinamoDatabaseHelper getInstance(String userId){
		if (databaseHelper == null) {
			databaseHelper = new DinamoDatabaseHelper(userId);
		}
		return databaseHelper;
	}
	private  DinamoDatabaseHelper(String databasePath){
		DatabaseOpenHelper openHelper = new DatabaseOpenHelper(databasePath+".db");
		dbObject = openHelper.getWritableDatabase();
	}
	public void closeExistingDatabase(){
		closeConnection();
		databaseHelper=null;
	}
	private void closeConnection() {
		try {
			if (dbObject != null && dbObject.isOpen()) {
				dbObject.close();
				System.gc();
			}
			SQLiteDatabase.releaseMemory();
		} catch (Exception e) {

		} finally {
			if (dbObject != null && dbObject.isOpen()) {
				SQLiteDatabase.releaseMemory();
				dbObject.close();
			}
		}
	}
	public long addUser(User user){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			values.put(TableNameConstants.TABLE_NAME_COL, user.getUserName()); 
			values.put(TableNameConstants.COL_PASSWORD,user.getPassword()); 
			values.put(TableNameConstants.COL_SERVER_ID, user.getServerId()); 
			values.put(TableNameConstants.COL_USER_ID, user.getUserId());
			values.put(TableNameConstants.COL_INIT_SERVER, user.isInitFromServer);
			if(user.getUserPhoto()!=null){
				values.put(TableNameConstants.COL_BITMAP, SharedData.getBytes(user.getUserPhoto()));
			}
			primaryKey=dbObject.insert(TableNameConstants.TABLE_USERS, null, values);
		}
		return primaryKey;
	}
	public void updateUser(User user){
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			values.put(TableNameConstants.TABLE_NAME_COL, user.getUserName()); 
			values.put(TableNameConstants.COL_PASSWORD,user.getPassword()); 
			values.put(TableNameConstants.COL_SERVER_ID, user.getServerId()); 
			values.put(TableNameConstants.COL_USER_ID, user.getUserId());
			values.put(TableNameConstants.COL_INIT_SERVER, user.isInitFromServer);
			if(user.getUserPhoto()!=null){
				values.put(TableNameConstants.COL_BITMAP, SharedData.getBytes(user.getUserPhoto()));
			}
			int result=dbObject.update(TableNameConstants.TABLE_USERS, values, TableNameConstants.COL_USER_ID + " = " + user.getUserId(), null);
		}
	}
	public long addUpdateEstablishment(DinamoObject estObject,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int sync=0;
			if(estObject.isSynChronized){
				sync=1;
			}
			values.put(TableNameConstants.TABLE_NAME_COL, estObject.getName()); 
			values.put(TableNameConstants.TABLE_ID_COL, estObject.getId()); 
			values.put(TableNameConstants.COL_SYNC, sync); 
			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_ESTABLISHMENT, null, values);
			}else{
				dbObject.update(TableNameConstants.TABLE_ESTABLISHMENT, values, TableNameConstants.PRIMARY_KEY + " = " + estObject.getPrimaryKey(), null);	
			}
		}
		return primaryKey;
	}
	public void addEstablishments(ArrayList<DinamoObject> estObjectsList){
		for(int count=0;count<estObjectsList.size();count++){
			long primaryKey=addUpdateEstablishment(estObjectsList.get(count),false);
			estObjectsList.get(count).setPrimaryKey(primaryKey);
		}
	}

	public long addUpdateCategory(DinamoObject estObject,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int sync=0;
			if(estObject.isSynChronized){
				sync=1;
			}
			values.put(TableNameConstants.TABLE_NAME_COL, estObject.getName()); 
			values.put(TableNameConstants.TABLE_ID_COL, estObject.getId()); 
			values.put(TableNameConstants.COL_SYNC, sync); 

			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_CATEGORIES, null, values);
			}else{
				dbObject.update(TableNameConstants.TABLE_CATEGORIES, values, TableNameConstants.PRIMARY_KEY + " = " + estObject.getPrimaryKey(), null);
			}
		}
		return primaryKey;
	}
	public void addCatgories(ArrayList<DinamoObject> catObjectsList){
		for(int count=0;count<catObjectsList.size();count++){
			long primaryKey=addUpdateCategory(catObjectsList.get(count),false);
			catObjectsList.get(count).setPrimaryKey(primaryKey);

		}
	}
	public long addUpdateProduct(DinamoObject productObject,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int sync=0;
			if(productObject.isSynChronized){
				sync=1;
			}

			values.put(TableNameConstants.TABLE_NAME_COL, productObject.getName()); 
			values.put(TableNameConstants.TABLE_ID_COL, productObject.getId()); 
			values.put(TableNameConstants.COL_SYNC, sync); 
			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_PRODUCTS, null, values);
			}else{
				dbObject.update(TableNameConstants.TABLE_PRODUCTS, values, TableNameConstants.PRIMARY_KEY + " = " + productObject.getPrimaryKey(), null);
			}
		}
		return primaryKey;
	}
	public void addProducts(ArrayList<DinamoObject> productObjectsList){
		for(int count=0;count<productObjectsList.size();count++){
			long primaryKey=addUpdateProduct(productObjectsList.get(count),false);
			productObjectsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	public long addPeriod(DinamoObject periodObject){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			values.put(TableNameConstants.TABLE_NAME_COL, periodObject.getName()); 
			values.put(TableNameConstants.TABLE_ID_COL, periodObject.getId()); 
			primaryKey=dbObject.insert(TableNameConstants.TABLE_PERIODICITY, null, values);
		}
		return primaryKey;
	}
	public void addPeriodicities(ArrayList<DinamoObject> periodObjectsList){
		for(int count=0;count<periodObjectsList.size();count++){
			long primaryKey=addPeriod(periodObjectsList.get(count));
			periodObjectsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	public long addPayMethod(DinamoObject payMethodObject){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			values.put(TableNameConstants.TABLE_NAME_COL, payMethodObject.getName()); 
			values.put(TableNameConstants.TABLE_ID_COL, payMethodObject.getId()); 
			primaryKey=dbObject.insert(TableNameConstants.TABLE_PAY_METHODS, null, values);
		}
		return primaryKey;
	}
	public void addPayMethods(ArrayList<DinamoObject> methodsObjectsList){
		for(int count=0;count<methodsObjectsList.size();count++){
			long primaryKey=addPayMethod(methodsObjectsList.get(count));
			methodsObjectsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	public long addUpdateBoughtProduct(BoughtProduct boughtProduct,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int isSync=0;
			int isDelted=0;
			if(boughtProduct.isSynchronized){
				isSync=1;
			}
			if(boughtProduct.isDeleted){
				isDelted=1;
			}
			values.put(TableNameConstants.COL_PRICE, String.valueOf(boughtProduct.getPriceValue())); 
			values.put(TableNameConstants.COL_SYNC,  isSync); 
			values.put(TableNameConstants.COL_DELTED,  isDelted);
			values.put(TableNameConstants.TABLE_ID_COL,  boughtProduct.getId());
			values.put(TableNameConstants.COL_ESTABLISHMENT_NAME,  boughtProduct.getEstablishment().getName());
			values.put(TableNameConstants.COL_PAY_METHOD_NAME,  boughtProduct.getPayMethod().getName());
			values.put(TableNameConstants.COL_ESTABLISHMENT_ID,  boughtProduct.getEstablishment().getId());
			values.put(TableNameConstants.COL_PAY_METHOD_ID,  boughtProduct.getPayMethod().getId());
			values.put(TableNameConstants.COL_NOTES,  boughtProduct.getNotes());
			values.put(TableNameConstants.COL_DATE, formatDateString(boughtProduct.getDate()));

			if(boughtProduct.getProductPhoto()!=null){
				values.put(TableNameConstants.COL_BITMAP, SharedData.getBytes(boughtProduct.getProductPhoto()));
			}
			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_BOUGHT_PRODUCTS, null, values);
			}else{
				int result=dbObject.update(TableNameConstants.TABLE_BOUGHT_PRODUCTS, values, TableNameConstants.PRIMARY_KEY + " = " + boughtProduct.getPrimaryKey(), null);
			}
		}
		return primaryKey;
	}
	public long addUpdateSoldProduct(SoldProduct soldProduct,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int isSync=0;
			int isDelted=0;

			if(soldProduct.isSynchronized){
				isSync=1;
			}
			if(soldProduct.isDeleted){
				isDelted=1;
			}
			values.put(TableNameConstants.COL_PRICE, String.valueOf(soldProduct.getPriceValue())); 
			values.put(TableNameConstants.COL_SYNC,  isSync); 
			values.put(TableNameConstants.COL_DELTED,  isDelted);
			values.put(TableNameConstants.TABLE_ID_COL,  soldProduct.getId());
			values.put(TableNameConstants.COL_PRODUCT_NAME,  soldProduct.getProduct().getName());
			values.put(TableNameConstants.COL_PAY_METHOD_NAME,    soldProduct.getPayMethod().getName());
			values.put(TableNameConstants.COL_PRODUCT_ID,  soldProduct.getProduct().getId());
			values.put(TableNameConstants.COL_PAY_METHOD_ID,    soldProduct.getPayMethod().getId());
			values.put(TableNameConstants.COL_QUANTIRY,      soldProduct.getQuantity());
			values.put(TableNameConstants.COL_DATE,          formatDateString(soldProduct.getDate()));

			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_SOLD_PRODUCTS, null, values);
			}else{
				dbObject.update(TableNameConstants.TABLE_SOLD_PRODUCTS, values, TableNameConstants.PRIMARY_KEY + " = " + soldProduct.getPrimaryKey(), null);
			}
		}
		return primaryKey;
	}
	public long addUpdateExpenseProduct(ExpenseProduct expenseProduct,boolean update){
		long primaryKey=-1;
		if(dbObject!=null && dbObject.isOpen()){
			ContentValues values = new ContentValues();
			int isSync=0;
			int isDelted=0;
			String endDate="";

			if(expenseProduct.isSynchronized){
				isSync=1;
			}
			if(expenseProduct.isDeleted){
				isDelted=1;
			}
			if(expenseProduct.getEndDate() !=null){
				endDate=formatDateString(expenseProduct.getEndDate());	
			}
			values.put(TableNameConstants.COL_PRICE, String.valueOf(expenseProduct.getPriceValue())); 
			values.put(TableNameConstants.COL_SYNC,  isSync); 
			values.put(TableNameConstants.COL_DELTED,  isDelted);
			values.put(TableNameConstants.TABLE_ID_COL,  expenseProduct.getId());
			values.put(TableNameConstants.COL_CATEGORY_NAME,        expenseProduct.getCategory().getName());
			values.put(TableNameConstants.COL_PERIODICITY_NAME,     expenseProduct.getPeriodicityType().getName());
			values.put(TableNameConstants.COL_CATEGORY_ID,          expenseProduct.getCategory().getId());
			values.put(TableNameConstants.COL_PERIODICITY_ID,       expenseProduct.getPeriodicityType().getId());
			values.put(TableNameConstants.COL_DATE,                 formatDateString(expenseProduct.getDate()));
			values.put(TableNameConstants.COL_END_DATE,             endDate);

			if(!update){
				primaryKey=dbObject.insert(TableNameConstants.TABLE_EXPENSE_PRODUCTS, null, values);
			}else{
				dbObject.update(TableNameConstants.TABLE_EXPENSE_PRODUCTS, values, TableNameConstants.PRIMARY_KEY + " = " + expenseProduct.getPrimaryKey(), null);
			}
		}
		return primaryKey;
	}
	public boolean deleteBoughtProduct(long primarykey) {
		boolean deleted = false;
		try {
			if (dbObject != null && dbObject.isOpen()) {
				dbObject.delete(TableNameConstants.TABLE_BOUGHT_PRODUCTS, TableNameConstants.PRIMARY_KEY + " = ?", new String [] {String.valueOf(primarykey)});
				deleted = true;
			}
		} catch (Exception e) {
			deleted = false;
		}
		return deleted;
	}
	public boolean deleteSoldProduct(long primarykey) {
		boolean deleted = false;
		try {
			if (dbObject != null && dbObject.isOpen()) {
				dbObject.delete(TableNameConstants.TABLE_SOLD_PRODUCTS, TableNameConstants.PRIMARY_KEY + " = ?", new String [] {String.valueOf(primarykey)});
				deleted = true;
			}
		} catch (Exception e) {
			deleted = false;
		}
		return deleted;
	}
	public boolean deleteExpenseProduct(long primarykey) {
		boolean deleted = false;
		try {
			if (dbObject != null && dbObject.isOpen()) {
				dbObject.delete(TableNameConstants.TABLE_EXPENSE_PRODUCTS, TableNameConstants.PRIMARY_KEY + " = ?", new String [] {String.valueOf(primarykey)});
				deleted = true;
			}
		} catch (Exception e) {
			deleted = false;
		}
		return deleted;
	}
	public String formatDateString(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);

		int monthOfYear = cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR);
		int mint=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);

		String formatedDateString=""+year+"-"+(monthOfYear+1)+"-"+day+" "+hour+":"+mint+":"+second;

		return formatedDateString;
	}
	public void addBoughtProducts(ArrayList<BoughtProduct> boughtProductsList){
		for(int count=0;count<boughtProductsList.size();count++){
			long primaryKey=addUpdateBoughtProduct(boughtProductsList.get(count),false);
			boughtProductsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	public void addSoldProducts(ArrayList<SoldProduct> soldProductsList){
		for(int count=0;count<soldProductsList.size();count++){
			long primaryKey=addUpdateSoldProduct(soldProductsList.get(count),false);
			soldProductsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	public void addExpenseProducts(ArrayList<ExpenseProduct> expenseProductsList){
		for(int count=0;count<expenseProductsList.size();count++){
			long primaryKey=addUpdateExpenseProduct(expenseProductsList.get(count),false);
			expenseProductsList.get(count).setPrimaryKey(primaryKey);
		}
	}
	private DinamoObject populateSynCommObject(Cursor cursor){
		DinamoObject dinamoObject=new DinamoObject();
		dinamoObject.setPrimaryKey(cursor.getLong(0));
		if(cursor.getInt(1)==0){
			dinamoObject.isSynChronized=false;
		}else{
			dinamoObject.isSynChronized=true;
		}
		dinamoObject.setName(cursor.getString(2));
		dinamoObject.setId(cursor.getString(3));	

		return dinamoObject;
	}
	private DinamoObject populateUnSynCommObject(Cursor cursor){
		DinamoObject dinamoObject=new DinamoObject();
		dinamoObject.setPrimaryKey(cursor.getLong(0));
		dinamoObject.setName(cursor.getString(1));
		dinamoObject.setId(cursor.getString(2));	

		return dinamoObject;
	}
	private BoughtProduct populateBoughtObject(Cursor cursor){
		BoughtProduct product=new BoughtProduct();


		product.setPrimaryKey(cursor.getLong(0));
		product.setPriceValue(Double.valueOf(cursor.getString(1)));
		int syn=cursor.getInt(2);
		int delted=cursor.getInt(3);

		Date date=SharedData.getInstance().convertToDate(cursor.getString(4));
		product.setDate(date);

		product.setEstablishment(new DinamoObject(cursor.getString(7),cursor.getString(5)));
		product.setPayMethod(new DinamoObject(cursor.getString(8),cursor.getString(6)));
		product.setProductPhoto(SharedData.getPhoto(cursor.getBlob(9)));
		product.setId(cursor.getString(10));
		product.setNotes(cursor.getString(11));


		if(syn==0){
			product.isSynchronized=false;
		}else{
			product.isSynchronized=true;
		}
		if(delted==0){
			product.isDeleted=false;
		}else{
			product.isDeleted=true;
		}
		return product;
	}
	private SoldProduct populateSoldObject(Cursor cursor){
		SoldProduct product=new SoldProduct();
		product.setPrimaryKey(cursor.getLong(0));
		product.setPriceValue(Double.valueOf(cursor.getString(1)));
		int syn=cursor.getInt(2);
		int delted=cursor.getInt(3);
		Date date=SharedData.getInstance().convertToDate(cursor.getString(4));
		product.setDate(date);

		product.setPayMethod(new DinamoObject(cursor.getString(7),cursor.getString(5)));
		product.setProduct(new DinamoObject(cursor.getString(8),cursor.getString(6)));
		product.setId(cursor.getString(9));
		product.setQuantity(cursor.getString(10));


		if(syn==0){
			product.isSynchronized=false;
		}else{
			product.isSynchronized=true;
		}

		if(delted==0){
			product.isDeleted=false;
		}else{
			product.isDeleted=true;
		}
		return product;
	}
	private ExpenseProduct populateExpenseObject(Cursor cursor){
		ExpenseProduct product=new ExpenseProduct();
		product.setPrimaryKey(cursor.getLong(0));
		product.setPriceValue(Double.valueOf(cursor.getString(1)));
		int syn=cursor.getInt(2);
		int delted=cursor.getInt(3);
		Date date=SharedData.getInstance().convertToDate(cursor.getString(4));
		Date endDate=SharedData.getInstance().convertToDate(cursor.getString(8));
		product.setDate(date);
		product.setEndDate(endDate);

		product.setCategory(new DinamoObject(cursor.getString(7),cursor.getString(5)));
		product.setPeriodicityType(new DinamoObject(cursor.getString(10),cursor.getString(6)));
		product.setId(cursor.getString(9));

		if(syn==0){
			product.isSynchronized=false;
		}else{
			product.isSynchronized=true;
		}

		if(delted==0){
			product.isDeleted=false;
		}else{
			product.isDeleted=true;
		}
		return product;
	}
	public User getUser(String user_id){
		Cursor cursor = null ;
		User user=null;
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_USERS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_USER_ID + "=");
				query.append(user_id);

				cursor = dbObject.rawQuery(query.toString(), null);
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					user=new User(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getString(6));
					user.setPrimaryKey(cursor.getLong(0));
					user.setUserPhoto(SharedData.getPhoto(cursor.getBlob(5)));
				}
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return user;
	}
	public String getEstablishmentId(String name){
		Cursor cursor = null ;
		String  id="";
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_ESTABLISHMENT);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.TABLE_NAME_COL+" ='");
				query.append(name+"'");

				cursor = dbObject.rawQuery(query.toString(), null);
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					id=cursor.getString(3);
				}
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return id;   
	}
	public String getCategoryId(String name){
		Cursor cursor = null ;
		String  id="";
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_CATEGORIES);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.TABLE_NAME_COL+" ='");
				query.append(name+"'");

				cursor = dbObject.rawQuery(query.toString(), null);
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					id=cursor.getString(3);
				}
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return id;   
	}
	public String getProductId(String name){
		Cursor cursor = null ;
		String  id="";
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.TABLE_NAME_COL+"='");
				query.append(name+"'");

				Log.d("Query ",""+query);
				cursor = dbObject.rawQuery(query.toString(), null);
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					id=cursor.getString(3);
				}
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return id;   	
	}
	public ArrayList<DinamoObject> getEstablishments(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>estabList=new ArrayList<DinamoObject>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				String query = DinamoConstants.dbSelectAll+TableNameConstants.TABLE_ESTABLISHMENT;
				cursor = dbObject.rawQuery(query,new String[]{});
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){
					estabList.add(populateSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return estabList;
	}
	public ArrayList<DinamoObject> getUnSyncEstablishments(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>estabList=new ArrayList<DinamoObject>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_ESTABLISHMENT);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					estabList.add(populateSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return estabList;
	}

	public ArrayList<DinamoObject> getCategories(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>catList=new ArrayList<DinamoObject>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				String query = DinamoConstants.dbSelectAll+TableNameConstants.TABLE_CATEGORIES;
				cursor = dbObject.rawQuery(query,new String[]{});
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){
					catList.add(populateSynCommObject(cursor));	
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return catList;
	}
	public ArrayList<DinamoObject> getUnSyncCategories(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>catList=new ArrayList<DinamoObject>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_CATEGORIES);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					catList.add(populateSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return catList;
	}

	public ArrayList<DinamoObject> getProducts(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>prodList=new ArrayList<DinamoObject>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				String query = DinamoConstants.dbSelectAll+TableNameConstants.TABLE_PRODUCTS;
				cursor = dbObject.rawQuery(query,new String[]{});
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){
					prodList.add(populateSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return prodList;
	}
	public ArrayList<DinamoObject> getUnSyncProducts(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>prodList=new ArrayList<DinamoObject>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					prodList.add(populateSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return prodList;
	}
	public ArrayList<DinamoObject> getPeriodicity(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>periodList=new ArrayList<DinamoObject>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				String query = DinamoConstants.dbSelectAll+TableNameConstants.TABLE_PERIODICITY;
				cursor = dbObject.rawQuery(query,new String[]{});
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){		
					periodList.add(populateUnSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return periodList;
	}
	public ArrayList<DinamoObject> getPaymentMethods(){
		Cursor cursor = null ;
		ArrayList<DinamoObject>methodsList=new ArrayList<DinamoObject>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				String query = DinamoConstants.dbSelectAll+TableNameConstants.TABLE_PAY_METHODS;
				cursor = dbObject.rawQuery(query,new String[]{});
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){
					methodsList.add(populateUnSynCommObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return methodsList;
	}
	public ArrayList<BoughtProduct> getBoughtProducts(){
		Cursor cursor = null ;
		ArrayList<BoughtProduct>productList=new ArrayList<BoughtProduct>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_BOUGHT_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_DELTED+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();

				for(int count=0;count<cursor.getCount();count++){	
					productList.add(populateBoughtObject(cursor));	
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return productList;
	}
	public ArrayList<BoughtProduct> getUnSyncBoughtProducts(){
		Cursor cursor = null ;
		ArrayList<BoughtProduct>boughtList=new ArrayList<BoughtProduct>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_BOUGHT_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					boughtList.add(populateBoughtObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return boughtList;
	}
	public ArrayList<SoldProduct> getAllSoldProducts(){
		Cursor cursor = null ;
		ArrayList<SoldProduct>productList=new ArrayList<SoldProduct>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_SOLD_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_DELTED+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);

				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){	
					productList.add(populateSoldObject(cursor));	
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return productList;
	}
	public ArrayList<SoldProduct> getUnSyncSoldProducts(){
		Cursor cursor = null ;
		ArrayList<SoldProduct>soldList=new ArrayList<SoldProduct>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_SOLD_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					soldList.add(populateSoldObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return soldList;
	}
	public ArrayList<ExpenseProduct> getAllExpenseProducts(){
		Cursor cursor = null ;
		ArrayList<ExpenseProduct>productList=new ArrayList<ExpenseProduct>();

		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_EXPENSE_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_DELTED+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){	
					productList.add(populateExpenseObject(cursor));	
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return productList;
	}
	public ArrayList<ExpenseProduct> getUnSyncEexpenses(){
		Cursor cursor = null ;
		ArrayList<ExpenseProduct>expenseList=new ArrayList<ExpenseProduct>();
		try {
			if (dbObject!= null && dbObject.isOpen()) {
				StringBuilder query = new StringBuilder();
				query.append(DinamoConstants.dbSelectAll);
				query.append(TableNameConstants.TABLE_EXPENSE_PRODUCTS);
				query.append(DinamoConstants.dbWhere);
				query.append(" "+TableNameConstants.COL_SYNC+"=");
				query.append(0);

				cursor = dbObject.rawQuery(query.toString(), null);
				cursor.moveToFirst();
				for(int count=0;count<cursor.getCount();count++){
					expenseList.add(populateExpenseObject(cursor));
					cursor.moveToNext();
				}	
			}	
		} catch (Exception e) {
			Log.d("Exception",e.toString());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return expenseList;
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper{

		public DatabaseOpenHelper(String databaseName) {
			super(SharedData.getInstance().mContext,databaseName,null, DATABASE_VERSION);	
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createUserTable(db);
			createEstablishmentTable(db);
			createCategoriesTable(db);
			createProductTable(db);
			createPeriodTable(db);
			createPAYMethodsTable(db);
			createBoughtProductTable(db);
			createSoldProductTable(db);
			createExpenseTable(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
		private void createEstablishmentTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_ESTABLISHMENT + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_SYNC 			                + " INTEGER, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 				+ " TEXT)");
		}
		private void createCategoriesTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_CATEGORIES + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_SYNC 			                + " INTEGER, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 				+ " TEXT)");
		}
		private void createProductTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_PRODUCTS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_SYNC 			                + " INTEGER, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 				+ " TEXT)");
		}
		private void createPeriodTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_PERIODICITY + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 				+ " TEXT)");
		}
		private void createPAYMethodsTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_PAY_METHODS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 				+ " TEXT)");
		}
		private void createBoughtProductTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_BOUGHT_PRODUCTS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_PRICE 			        + " TEXT, "
					+ TableNameConstants.COL_SYNC 			        + " INTEGER, "
					+ TableNameConstants.COL_DELTED 			    + " INTEGER, "
					+ TableNameConstants.COL_DATE 			        + " TEXT, "
					+ TableNameConstants.COL_ESTABLISHMENT_NAME 	+ " TEXT, "
					+ TableNameConstants.COL_PAY_METHOD_NAME 		+ " TEXT, "
					+ TableNameConstants.COL_ESTABLISHMENT_ID 		+ " TEXT, "
					+ TableNameConstants.COL_PAY_METHOD_ID 			+ " TEXT, "
					+ TableNameConstants.COL_BITMAP 			    + " BLOB, "
					+ TableNameConstants.TABLE_ID_COL 			    + " TEXT, "
					+ TableNameConstants.COL_NOTES 				    + " TEXT)");
		}
		private void createSoldProductTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_SOLD_PRODUCTS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_PRICE 			        + " TEXT, "
					+ TableNameConstants.COL_SYNC 			        + " INTEGER, "
					+ TableNameConstants.COL_DELTED 			    + " INTEGER, "
					+ TableNameConstants.COL_DATE 			        + " TEXT, "
					+ TableNameConstants.COL_PAY_METHOD_NAME 		+ " TEXT, "
					+ TableNameConstants.COL_PRODUCT_NAME 		    + " TEXT, "
					+ TableNameConstants.COL_PAY_METHOD_ID 			+ " TEXT, "
					+ TableNameConstants.COL_PRODUCT_ID 		    + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 			    + " TEXT, "
					+ TableNameConstants.COL_QUANTIRY 				+ " TEXT)");
		}
		private void createExpenseTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_EXPENSE_PRODUCTS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.COL_PRICE 			        + " TEXT, "
					+ TableNameConstants.COL_SYNC 			        + " INTEGER, "
					+ TableNameConstants.COL_DELTED 			    + " INTEGER, "
					+ TableNameConstants.COL_DATE 			        + " TEXT, "
					+ TableNameConstants.COL_CATEGORY_NAME 			+ " TEXT, "
					+ TableNameConstants.COL_PERIODICITY_NAME 	    + " TEXT, "
					+ TableNameConstants.COL_CATEGORY_ID 		    + " TEXT, "
					+ TableNameConstants.COL_END_DATE 		        + " TEXT, "
					+ TableNameConstants.TABLE_ID_COL 			    + " TEXT, "
					+ TableNameConstants.COL_PERIODICITY_ID 		+ " TEXT)");
		}
		private void createUserTable(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + TableNameConstants.TABLE_USERS + " ("
					+ TableNameConstants.PRIMARY_KEY						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ TableNameConstants.TABLE_NAME_COL 			        + " TEXT, "
					+ TableNameConstants.COL_PASSWORD 			            + " TEXT, "
					+ TableNameConstants.COL_SERVER_ID 			            + " TEXT, "
					+ TableNameConstants.COL_INIT_SERVER 			        + " INTEGER, "
					+ TableNameConstants.COL_BITMAP 			    + " BLOB, "
					+ TableNameConstants.COL_USER_ID 		+ " TEXT not null unique)");
		}

	}
}
