package com.cliqdigital.supergsdk.utils;


public class Log {
	
	private static boolean DEBUG = false;
	
	public static void setDEBUG(boolean value){
		DEBUG = value;
	}
	
	public static void d(String TAG,String Message){
		if(DEBUG){
			android.util.Log.d(TAG,Message);
		}
	}
	
	public static void e(String TAG,String Message){
		if(DEBUG){
			android.util.Log.e(TAG,Message);
		}
	}
	
	public static void i(String TAG,String Message){
		if(DEBUG){
			android.util.Log.i(TAG,Message);
		}
	}
	
	public static void v(String TAG,String Message){
		if(DEBUG){
			android.util.Log.v(TAG,Message);
		}
	}
	
	public static void w(String TAG,String Message){
		if(DEBUG){
			android.util.Log.w(TAG,Message);
		}
	}
	
	public static void wtf(String TAG,String Message){
		if(DEBUG){
			android.util.Log.wtf(TAG,Message);
		}
	}
}
