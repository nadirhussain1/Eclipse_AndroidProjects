package br.com.global;

public interface DinamoConstants {
	public String HELVETICA_NEUE_CONDENSED="helvetica_condensed.ttf";
	public String HELVETICA_NEUE_BOLD="helvetica_bold.otf";
	public String HELVETICA_NEUE_BOLDCOND="helvetica_boldcond.otf";
	public String HELVETICA_NEUE_LIGHT="helvetica_light.otf";
	public String HELVETICA_NEUE_LIGHTCOND="helvetica_lightcond.otf";
	public String HELVETICA_NEUE_MEDCOND="helvetica_medcond.otf";
	public String HELVETICA_NEUE_MEDIUM="helvetica_medium.otf";
	public String HELVETICA_NEUE_THINCOND="helvetica_thincond.otf";
	public int HTTP_POST_SUCCESS=201;
	public int HTTP_GET_SUCCESS=200;
	public int HTTP_PUT_SUCCESS=200;
	public int LOG_SIGN_DROP_WIDTH=210;
	public int MAIN_DROP_WIDTH=650;
	public int SIGN_UP_PHOTO_SIZE=100;
	public int TAKE_PHOTO_SIZE=500;
	public int ADD_EVENT_ACTIVITY_ID=1;
	public int EDIT_EVENT_ACTIVITY_ID=2;
	public int CAMERA_REQUEST_CODE=1;
	public int GALLERY_REQUEST_CODE=2;

	public int SIGN_UP_SCREEN=0;
	public int LOG_IN_SCREEN=1;
	public int MAIN_SCREEN=2;
	public int COMPRAS_SCREEN=3;
	public int VENDA_SCREEN=4;
	public int EXPENSE_SCREEN=5;

	public int GET_PAY_METHOD_REQ_ID=1;
	public int GET_PERIODICITY_REQ_ID=2;
	public int GET_CAT_REQ_ID=3;
	public int GET_EST_REQ_ID=4;
	public int GET_PROD_REQ_ID=5;
	public int GET_BUY_EVENTS_ID=6;
	public int GET_SELL_EVENTS_ID=7;
	public int GET_EXPENSE_EVENTS_ID=8;

	//public String API_TOKEN="MDI2MTRhZDktNjU4Yi00NjJjLTgxNTMtNmU0OTZiOThlYWI0";
	/* DEV
	public String API_TOKEN="RbgbjgTQcazkqorowPrMQXZ1BzjJba/QesRT2yJUvC8HXwguxO1D9h+iq6vsX2FArtfAcYHzMEY0pK/UvjyhbA==";
	public String CHAR_CONTENT_TYPE="application/json;charset=utf-8";
	public String ACCEPT_STRING="application/json";
	public String PUSH_TOKEN_URL="http://contafacil.dev.goldarkapi.com/v1/push/devices";
	public String SIGN_UP_BASE_URL="http://contafacil.dev.goldarkapi.com/v1/users";
	public String LOG_IN_URL="http://contafacil.dev.goldarkapi.com/v1/users?userid=";
	public String PERIODICITY_POST_URL="http://contafacil.dev.goldarkapi.com/v1/periodicity";
	public String BUY_EVENT_URL="http://contafacil.dev.goldarkapi.com/v1/buy";
	public String BUY_ALL_EVENTS_URL="http://contafacil.dev.goldarkapi.com/v1/buy?user=";
	public String SELL_EVENT_URL="http://contafacil.dev.goldarkapi.com/v1/sell";
	public String SELL_ALL_EVENTS_URL="http://contafacil.dev.goldarkapi.com/v1/sell?user=";
	public String GET_PAYMENT_METHODS_URL="http://contafacil.dev.goldarkapi.com/v1/paymentmethods";
	public String GET_PERIODICITY_URL="http://contafacil.dev.goldarkapi.com/v1/periodicity";
	public String GET_CATEGORIES_URL="http://contafacil.dev.goldarkapi.com/v1/categories?user=";
	public String INSERT_CATEGORIES_URL="http://contafacil.dev.goldarkapi.com/v1/categories";
	public String GET_ESTABLISHMENTS_URL="http://contafacil.dev.goldarkapi.com/v1/establishments?user=";
	public String INSERT_ESTABLISHMENT_URL="http://contafacil.dev.goldarkapi.com/v1/establishments";
	public String GET_PRODUCTS_URL="http://contafacil.dev.goldarkapi.com/v1/products?user=";
	public String INSERT_PRODUCT_URL="http://contafacil.dev.goldarkapi.com/v1/products";
	public String EXPENSES_EVENT_URL="http://contafacil.dev.goldarkapi.com/v1/expenses";
	public String EXPENSES_ALL_EVENT_URL="http://contafacil.dev.goldarkapi.com/v1/expenses?user=";
	*/
	
	/*PROD*/
	public String API_TOKEN="0WypXscOxLG1q5UozPBE+Z9z74fmSBqzpGQH5z9rQyT8JMnGoQ2bqHgGomGzBfQl3FozlvMU8rdwe0pt/smeuA==";
	public String ACCESS_TOKEN="u+BO9P+JVNL3CcKt0NUuGRQs+jHeL82gAIiDHk5XT5aAzQWQLNHWlLHXxgpWcMCNMXWIgGrwU+8ovrpgZh9yLA==";
	public String CHAR_CONTENT_TYPE="application/json;charset=utf-8";
	public String ACCEPT_STRING="application/json";
	public String PUSH_TOKEN_URL="http://dinamo.goldarkapi.com/push/devices";
	public String SIGN_UP_BASE_URL="http://dinamo.goldarkapi.com/users";
	public String LOG_IN_URL="http://dinamo.goldarkapi.com/users?username=";
	public String PERIODICITY_POST_URL="http://dinamo.goldarkapi.com/periodicity";
	public String BUY_EVENT_URL="http://dinamo.goldarkapi.com/buy";
	public String BUY_ALL_EVENTS_URL="http://dinamo.goldarkapi.com/buy?user=";
	public String SELL_EVENT_URL="http://dinamo.goldarkapi.com/sell";
	public String SELL_ALL_EVENTS_URL="http://dinamo.goldarkapi.com/sell?user=";
	public String GET_PAYMENT_METHODS_URL="http://dinamo.goldarkapi.com/paymentmethods";
	public String GET_PERIODICITY_URL="http://dinamo.goldarkapi.com/periodicity";
	public String GET_CATEGORIES_URL="http://dinamo.goldarkapi.com/categories?user=";
	public String GET_PUBLIC_CATEGORIES_URL="http://dinamo.goldarkapi.com/categories?public=true";
	public String INSERT_CATEGORIES_URL="http://dinamo.goldarkapi.com/categories";
	public String GET_ESTABLISHMENTS_URL="http://dinamo.goldarkapi.com/establishments?user=";
	public String GET_PUBLIC_ESTABLISHMENTS_URL="http://dinamo.goldarkapi.com/establishments?public=true";
	public String INSERT_ESTABLISHMENT_URL="http://dinamo.goldarkapi.com/establishments";
	public String GET_PRODUCTS_URL="http://dinamo.goldarkapi.com/products?user=";
	public String GET_PUBLIC_PRODUCTS_URL="http://dinamo.goldarkapi.com/products?public=true";
	public String INSERT_PRODUCT_URL="http://dinamo.goldarkapi.com/products";
	public String EXPENSES_EVENT_URL="http://dinamo.goldarkapi.com/expenses";
	public String EXPENSES_ALL_EVENT_URL="http://dinamo.goldarkapi.com/expenses?user=";
	
	/*HOMOL
	public String API_TOKEN="ZnQ9FmtYJYtWJANK3+xVJsOBrclbUJtM6ey6hKKCIznycaRaT0PWcOvhj8NQvmxN0cEgpCJkDn4dO0A3uY77aw==";
	public String ACCESS_TOKEN="RLDL4sGPYuwb7h24QBgPaoDhED3CSfKeoECYWT8ovn3U+Rz14V/qsQ9EQ53TP5TaqQi04a8db46NQBsojNawJg==";
	public String CHAR_CONTENT_TYPE="application/json;charset=utf-8";
	public String ACCEPT_STRING="application/json";
	public String PUSH_TOKEN_URL="http://contafacil.goldarkapi.com/push/devices";
	public String SIGN_UP_BASE_URL="http://contafacil.goldarkapi.com/users";
	public String LOG_IN_URL="http://contafacil.goldarkapi.com/users?username=";
	public String PERIODICITY_POST_URL="http://contafacil.goldarkapi.com/periodicity";
	public String BUY_EVENT_URL="http://contafacil.goldarkapi.com/buy";
	public String BUY_ALL_EVENTS_URL="http://contafacil.goldarkapi.com/buy?user=";
	public String SELL_EVENT_URL="http://contafacil.goldarkapi.com/sell";
	public String SELL_ALL_EVENTS_URL="http://contafacil.goldarkapi.com/sell?user=";
	public String GET_PAYMENT_METHODS_URL="http://contafacil.goldarkapi.com/paymentmethods";
	public String GET_PERIODICITY_URL="http://contafacil.goldarkapi.com/periodicity";
	public String GET_CATEGORIES_URL="http://contafacil.goldarkapi.com/categories?user=";
	public String GET_PUBLIC_CATEGORIES_URL="http://contafacil.goldarkapi.com/categories?public=true";
	public String INSERT_CATEGORIES_URL="http://contafacil.goldarkapi.com/categories";
	public String GET_ESTABLISHMENTS_URL="http://contafacil.goldarkapi.com/establishments?user=";
	public String GET_PUBLIC_ESTABLISHMENTS_URL="http://contafacil.goldarkapi.com/establishments?public=true";
	public String INSERT_ESTABLISHMENT_URL="http://contafacil.goldarkapi.com/establishments";
	public String GET_PRODUCTS_URL="http://contafacil.goldarkapi.com/products?user=";
	public String GET_PUBLIC_PRODUCTS_URL="http://contafacil.goldarkapi.com/products?public=true";
	public String INSERT_PRODUCT_URL="http://contafacil.goldarkapi.com/products";
	public String EXPENSES_EVENT_URL="http://contafacil.goldarkapi.com/expenses";
	public String EXPENSES_ALL_EVENT_URL="http://contafacil.goldarkapi.com/expenses?user=";
	*/
	
	public   String EXTRA_MESSAGE = "message";
	public   String PROPERTY_REG_ID = "registration_id";
	public   String PROPERTY_APP_VERSION = "appVersion";
	public   int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public  String WEEK_PERIODCITY="Semanal";
	public  String MONTH_PERIODICITY="Mensal";
	public  String ANNUAL_PERIODICITY="Anual";

	String dbSelectAll = "select * from ";
	String dbWhere = " where ";
	String dbEqual = " = ";
	String dbAnd = " AND ";
	String dbInsertInto = "insert into  ";
	String dbEndBracket = ")";
	String dbDeleteFrom = "delete from ";
	String dbPrimaryKeyEqual = " primaryKey=";
	String MYSTERY_STRING = "? ? ?";

}
