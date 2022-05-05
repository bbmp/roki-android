package com.robam.common.services;


import com.legent.pojos.AbsPojoManagerWithMap;
import com.robam.common.pojos.dictionary.StoveAlarm;

public class StoveAlarmManager extends AbsPojoManagerWithMap<StoveAlarm,Short> {

	final static public short Key_None = 255;
	final static public short Key_InnerError = 0;
	final static public short Key_WithoutPan = 1;
	final static public short Key_IGBT_Open = 2;
	final static public short Key_Voltage_High = 3;
	final static public short Key_Voltage_Low = 4;
	final static public short Key_Top_Overheated = 5;
	final static public short Key_Top_Shorting = 6;
	final static public short Key_Remove_Protected = 7;
	final static public short Key_IGBT_Shorting = 8;
	final static public short Key_IGBT_Overheated = 9;
	final static public short Key_Shell_Overheated = 10;
	final static public short Key_Unknow_11 = 11;
	final static public short Key_Unknow_12 = 12;
	final static public short Key_Bluetooth_Error = 13;

	final static public short E_None = 255;
	final static public short E_0 = 0;
	final static public short E_1 = 1;
	final static public short E_2 = 2;
	final static public short E_3 = 3;
	final static public short E_4 = 4;
	final static public short E_5 = 5;
	final static public short E_6 = 6;
	final static public short E_7 = 7;
	final static public short E_8 = 8;
	final static public short E_9 = 9;
	final static public short E_D = 13;



	static private StoveAlarmManager instance = new StoveAlarmManager();

	synchronized static public StoveAlarmManager getInstance() {
		return instance;
	}

	private StoveAlarmManager() {
	}

}
