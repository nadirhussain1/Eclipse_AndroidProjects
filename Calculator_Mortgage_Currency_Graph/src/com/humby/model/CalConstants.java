package com.humby.model;

public interface CalConstants {
	public String AD_UNIT_ID ="ca-app-pub-8857883061140930/9811587801";
	public String AD_INTERSITIAL_ID="ca-app-pub-8857883061140930/6299718206";
	public String SETTINGS_PRO_ITEM="android.test.purchased";
	public String IN_APP_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsr2hFs2StGeyiYaGOuEykWEcfldzMFymwSz/4CJvQ4XWlhUybmq4usWG/H5xK9LG2jChm4+3TtIyP93tOpo3KyOPhXeAquStHxV2isctclQBFtb5gPY49CXGDtfSrDnpAF3NzIFiXoSvFppn5tazqVV716+zGY7wArBUZyjGLb9k2Pz80SnP4K4KlMk/ZHsXCSs2+WgqL3igbqMjb883j5UfJBP1f7hQ8pnHQCIf86gPaQI/OAwuFmNelQS+Un6o++I+lCNDW/jlKmnDR6Xi6lj6HkdW/SJf3RawI3jykNBQaMkIKobOv7tLKoKrevDHKOPs5WwxfZczFnlpLqm6xwIDAQAB";
	public int TOTAL_DRAWER_ITEMS=8;
	public int AMERICAN_DECIMAL_SEPARATOR=1;
	public int EUROPEAN_DECIMAL_SEPARATOR=2;
	public int GRAPHIC_ID=5;
	public int HOME_ID=0;
	public int CURRENCY_CONVERTER_ID=1;
	public int MEASURE_CONV_ID=2;
	public int TIPPING_CONV_ID=3;
	public int LOAN_CAL_ID=4;
	public int GRAPH_CAL_ID=5;
	public int SETTINGS_NAV_ID=7;
	public int VIBRATION_DURATION=20;

	public int RESULT_LISTVIEW_HEIGHT=498;
	public int RESULT_LISTVIEW_FULL_HEIGHT=893;
	public String SALES_TAX_SETTING_KEY="SalesTaxSettingskey";
	public String SALES_TAX_VALUE_STRING="SalesTaxValue";
	public short SALES_TAX_ON=1;
	public short SALES_TAX_OFF=2;
	public short SALES_TAX_NOT_SET=-1;
	public short IS_GLASS_THEME=1;
	public int IS_TEMPERATURE_CONVERSION=4;
	public short IS_RANKINE_SCALE=3;
	public short IS_CELSIUS_SCALE=0;
	public short IS_FARENHEIT_SCALE=1;
	public short IS_KELVIN_SCALE=2;
	public String ROBOT_MEDIUM="Roboto-Medium.ttf";
	public String ROBOT_REGULAR="Roboto-Regular.ttf";
	public String ROBOT_LIGHT="Roboto-Light.ttf";
	public String ROBOT_BOLD="Roboto-Bold.ttf";
	public String ROBOT_CONDENSED="Roboto-Condensed.ttf";
	public String ROBOTO_LIGHT_ITALIC="Roboto-LightItalic.ttf";
	public String ALEGREYA_BOLDITALIC="Alegreya-BoldItalic.otf";
	public String ALEGREYA_ITALIC="Alegreya-Italic.otf";
	
	public  Double lengthValuesInTermsOfCentimeters[]={1.00,0.0328084,0.393700787402,0.00001,0.01,6.21371/Math.pow(10, 6),10.00,10000000.00,2.36220472,0.0109361};
	public  Double AreaValuesInTermsOfAcre[]={1.00,0.404686,40468564.2,43560.00,6272640.00,0.00404686,4046.86,0.0015625,4046856420.00,4840.00};
	public  Double WeightValuesInTermsOfCarat[]={1.00,20.00,0.2,0.0002,200.00,0.00705479239,0.000440924524,0.0000314946089};
	public  Double timeInTermsOfDay[]={1.00,24.00,1440.00,86400.00,0.142857};
	public  Double volumeInTermsOfCubicCentimeter[]={1.00,0.0000353146667,0.0610237441,0.000001,0.00000130795062,0.0338140227,0.0002641172052,0.001,0.00211337642,0.00105668821};
	public  Double temperatureEmpty[]={};
	public  Double pcMemoryInTermsOfBits[]={1.00,0.125,0.0001220703125,1.1920928955078/Math.pow(10, 7),1.1641532182693481/Math.pow(10, 10),1.1368683772161603/Math.pow(10, 13),1.1102230246251565/Math.pow(10, 16),6.938893903907228/Math.pow(10, 18),6.776263578034403/Math.pow(10, 21),6.617444900424222/Math.pow(10, 24)};
	public  Double forceInTermsOfNewton[]={1.00,1/Math.pow(10, 18),1/Math.pow(10, 15),1/Math.pow(10, 12),0.000000001,0.000001,0.001,0.01,0.1,10.00,100.00,1000.00,1000000.00,1000000000.00,1*Math.pow(10, 12),1*Math.pow(10, 15),1*Math.pow(10, 18),100000.00,1.00,100.00,101.97162129,0.10197162,0.000112404,0.000100361,0.000101972,0.000224809,0.000224809,0.224808943,3.59694309,7.233013851,7.233013851,101.971621298,0.101971621};
	public  Double cookingInTermsOfCane[]={1.00,0.02916666667,0.00578245961550,0.0050596521635,0.0071614583336,0.0070564516131,0.0069444444442,0.045536869472,0.04375,0.022768434736,0.023498316452,82.805882775,336.00,828.05882775,0.82805882775,0.029242621528,50.53125,0.00082805882775,828058827750000.00,828058.82775,3.6429495578,3.312235311,3.5,1344.00,8.2805882775,0.082805882775,3.312235311,224.0,16128.0,1.09375,0.18214747789,0.18798653162,0.21875,5.8287192924,7.0,0.0082805882775,
		                                    0.0028912298077,0.0034722222223,18.666666667,0.00082805882775,0.82805882775,0.1075401075,8.2805882775/Math.pow(10, 7),828058.82775,828.05882775,13988.926302,13440.0,29.143596462,28.0,0.091073738944,0.09399326581,2688.0,1.4571798231,1.503892253,1.75,0.0016865507212,0.0017361111111,28.0,0.72317021916,0.76672113681,0.72858991155,0.75194612648,0.875,82.805882775,82.805882775,16.561176555,28.0,55.20392185,58.287192924,56.000000001,165.61176555,233.1487717,168.0}; 
	public Double   anglesInTermsOfRadian[]={1.00,1018.5916358,63.661977237,57.295779513,3437.7467707,206264.80625,5.0928581789};
	public Double  energyValuesInTermsOfAttoJoule[]={1.00,2.777777778/Math.pow(10, 25),9.4781707775/Math.pow(10, 22),9.4845138281/Math.pow(10, 22),2.3884589663/Math.pow(10, 19),2.3890925762/Math.pow(10, 19),2.3884589663/Math.pow(10,22),2.3900573614/Math.pow(10, 19),5.2656507647/Math.pow(10, 19),1/Math.pow(10,16),3.7767267147/Math.pow(10, 25),1/Math.pow(10, 17),1/Math.pow(10, 19),2.7777777778/Math.pow(10, 23),9.4781608956/Math.pow(10, 28),6.24150648,1/Math.pow(10, 11),1/Math.pow(10, 36),2.7777777778/Math.pow(10, 40),0.001,7.3756217557/Math.pow(10,19),2.3730360457/Math.pow(10,17),6.3196276031/Math.pow(10,27),7.5895567699/Math.pow(10,27),6.3196276031/Math.pow(10,27),7.5895567699/Math.pow(10,27),5.6830066406/Math.pow(10,27),6.825006825/Math.pow(10,27),5.6830066406/Math.pow(10,27),6.825006825/Math.pow(10,27),5.8556549436/Math.pow(10,27),7.0323488045/Math.pow(10,27),8.2641127059/Math.pow(10,27),9.9247861544/Math.pow(10,27),6.2176981256/Math.pow(10,27),7.4671445639/Math.pow(10,27),5.8556549436/Math.pow(10,27),7.0323488045/Math.pow(10,27),5.2687555871/Math.pow(10,27),6.3275120223/Math.pow(10,27),2.3884589663/Math.pow(10,28),2.3890295762/Math.pow(10,28),6.24150648/Math.pow(10,9),1/Math.pow(10,27),2.7777777778/Math.pow(10,31),2.3890295762/Math.pow(10,19),0.22937104487,1/Math.pow(10,20),2.7777777778/Math.pow(10,24),3.7250614123/Math.pow(10,25),9.1979396615/Math.pow(10,27),1.4161193295/Math.pow(10,16),8.8507461068/Math.pow(10,18),1/Math.pow(10,18),
			                                          2.3890295762/Math.pow(10,22),2.3884589663/Math.pow(10,22),2.3900573614/Math.pow(10,22),0.00624150648,2.3890295762/Math.pow(10,22),1.019716213/Math.pow(10,19),1/Math.pow(10,21),1.019716213/Math.pow(10,19),2.3900573614/Math.pow(10,31),2.7777777778/Math.pow(10,25),9.8692326672/Math.pow(10,21), 0.00000624150648,2.3884589663/Math.pow(10,25),2.3890295762/Math.pow(10,25),1/Math.pow(10,24),1/Math.pow(10,17),2.3900573614/Math.pow(10,34),2.7777777778/Math.pow(10,28),1.019716213/Math.pow(10,19),1/Math.pow(10,12),1/Math.pow(10,15),2.7777777778/Math.pow(10,26),1/Math.pow(10,9),1/Math.pow(10,18),1/Math.pow(10,33),2.7777777778/Math.pow(10,37),3.7767267147/Math.pow(10,25),0.000001,9.4781707775/Math.pow(10,40),9.4781707775/Math.pow(10,37),6.24150648/Math.pow(10,12),1/Math.pow(10,30),2.7777777778/Math.pow(10,34),9.4781707775/Math.pow(10,27),9.4804342797/Math.pow(10,27),2.3890295762/Math.pow(10,25),2.3900573614/Math.pow(10,28),3.4120842375/Math.pow(10,29),2.3884589663/Math.pow(10,29),2.7777777778/Math.pow(10,22),1/Math.pow(10,18),1000000.0,1/Math.pow(10,42),2.7777777778/Math.pow(10,46),1000.0,1/Math.pow(10,39),2.7777777778/Math.pow(10,43)};
	public Double  powerValuesInTermsOfAttowatt[]={1.00,3.4121416351/Math.pow(10,18),5.6869027252/Math.pow(10, 20),9.4781712087/Math.pow(10, 22),8.5984522786/Math.pow(10,16),1.4330753798/Math.pow(10,17),2.3884589663/Math.pow(10,19),1/Math.pow(10,16),1.3596216173/Math.pow(10,21),7.5006167382/Math.pow(10,13),1/Math.pow(10,17),1/Math.pow(10,19),3.6/Math.pow(10,8),5.9999999999/Math.pow(10,10),1/Math.pow(10,11),3.6/Math.pow(10,8),5.9999999999/Math.pow(10,10),1/Math.pow(10,11),1/Math.pow(10,36),0.001,2.6552238321/Math.pow(10,15),4.4253730534/Math.pow(10,17),7.3756217557/Math.pow(10,19),8.5429297646/Math.pow(10,14),1.4238216274/Math.pow(10,15),2.3730360457/Math.pow(10,17),1/Math.pow(10,27),3.6709783668/Math.pow(10,11),6.1182972777/Math.pow(10,13),1.019716213/Math.pow(10,14),1/Math.pow(10,20),1.3410220924/Math.pow(10,21),1.3404825737/Math.pow(10,21),1.3596216173/Math.pow(10,21),1.3404053118/Math.pow(10,21),1.3522943391/Math.pow(10,15),3.6/Math.pow(10,15),5.9999999999/Math.pow(10,17),1/Math.pow(10,18),8.5984522786/Math.pow(10,19),1.4330753798/Math.pow(10,20),2.3884589663/Math.pow(10,22),3.6709783668/Math.pow(10,16),6.1182972777/Math.pow(10,18),1.019716213/Math.pow(10,19),3.6709783668/Math.pow(10,16),6.1182972777/Math.pow(10,18),1.019716213/Math.pow(10,19),1/Math.pow(10,21),1/Math.pow(10,24),1/Math.pow(10,12),3.4121416351/Math.pow(10,24),1/Math.pow(10,15),1/Math.pow(10,9),3.6/Math.pow(10,15),5.9999999999/Math.pow(10,17),1/Math.pow(10,18),1/Math.pow(10,33),1.3596216173/Math.pow(10,21), 0.000001,1.019716213/Math.pow(10,21),2.3730360457/Math.pow(10,17),1/Math.pow(10,30),2.8434513626/Math.pow(10,22),1/Math.pow(10,18),1000000.0,1/Math.pow(10,42),1000.0,1/Math.pow(10,39)};
	public Double  speedValuesInTermsOfCPS[]={1.00,0.03280839895,0.3937007874,0.036,0.00001, 0.019438444925, 0.00002938669958,0.01,0.022369362921,0.0000062137119224,10.0,3.335640952/Math.pow(10,11),0.00002938669958};
	public Double  densityValuesInTermsOfGCF[]={1.00,0.00057870370371,0.16054365256,0.13368055556,0.0010033978285, 0.0010443793403, 0.04013591314,0.033420138889,0.0000022883519009,2288351900.9,2.2883519009,2.2883519009/Math.pow(10,9),2.2883519009,0.0022883519009,0.0022883519009,2.2883519009/Math.pow(10,9),0.0000022883519009, 2.2883519009/Math.pow(10, 8),22883519.009,0.022883519009,2.2883519009/Math.pow(10,20),2.2883519009/Math.pow(10,11),0.0022883519009,0.022883519009,0.000022883519009,0.000022883519009,2.2883519009/Math.pow(10,11),2.2883519009/Math.pow(10,8),2.2883519009/Math.pow(10,9),2288351.9009,0.0022883519009,2.2883519009/Math.pow(10,21),2.2883519009/Math.pow(10,12),0.0022883519009,0.0000022883519009,0.0000022883519009,2.2883519009/Math.pow(10,12),2.2883519009/Math.pow(10,9),9.3876244896,10.514139429,1.7219387755/Math.pow(10,9),1.9285714286/Math.pow(10,9),2.2883519009,2.2883519009/Math.pow(10,9),2.2883519009/Math.pow(10,9),2.2883519009/Math.pow(10,12),2.2883519009/Math.pow(10,12),2.2883519009,2288351900900000.0,2288351.9009,2.2883519009/Math.pow(10,12),0.0022883519009,2.2883519009/Math.pow(10,21),2288351.9009,2288.3519009,2288.3519009,0.0022883519009,2.2883519009,0.0022883519009,2288351900900.0,2288.3519009,0.0000022883519009,2288.3519009,2.2883519009,2.2883519009,0.0000022883519009,0.0022883519009,2288.3519009,2288351900900000000.0, 2288351900.9,2.2883519009,2288351900.9,2288351.9009,2288351.9009,2.2883519009,2288.3519009,0.0022857142858,0.0000013227513227,0.00036695692013,0.00030555555555,0.00014285714286,8.2671957673/Math.pow(10,8),21028278.857,0.0038571428572,0.000022934807508,0.000019097222222,2288.3519009,0.0000022883519009,0.0000022883519009,2.2883519009/Math.pow(10, 9),2.2883519009/Math.pow(10, 9),0.0000025009310392,0.000002292478362,0.0000022883519009};
	
	public  Double  measurementValues[][]={lengthValuesInTermsOfCentimeters,AreaValuesInTermsOfAcre,WeightValuesInTermsOfCarat,timeInTermsOfDay,temperatureEmpty,volumeInTermsOfCubicCentimeter,pcMemoryInTermsOfBits,forceInTermsOfNewton,cookingInTermsOfCane,anglesInTermsOfRadian,energyValuesInTermsOfAttoJoule,powerValuesInTermsOfAttowatt,speedValuesInTermsOfCPS,densityValuesInTermsOfGCF};
}
