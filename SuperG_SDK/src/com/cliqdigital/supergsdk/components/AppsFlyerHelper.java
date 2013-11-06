package com.cliqdigital.supergsdk.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import com.cliqdigital.supergsdk.utils.Log;

public class AppsFlyerHelper {

	@SuppressWarnings("unused")
	public String getAppsflyerId(Context context){
		Method method = null;
		try {
			Class<?> AppsFlyerLib = Class.forName("com.appsflyer.AppsFlyerLib");
			Class<? extends Context> c = context.getClass();
			for(Method m : AppsFlyerLib.getDeclaredMethods())
		        if(m.getName().equals("getAppsFlyerUID")){
		        	method = m;
		        }
			return (String) method.invoke(AppsFlyerLib, context);
		} catch(ClassNotFoundException e){
			Log.e("AppsFlyer","Class not found, AppsFlyer is not used");
		} catch(IllegalAccessException e){
			Log.e("AppsFlyer","Illegal Access");
		} catch(InvocationTargetException e){
			Log.e("AppsFlyer","InvocationTarget Exception");
		} catch(NullPointerException e){
			Log.e("AppsFlyer","InvocationTarget Exception");
		}		
		return "";
	}
	
	@SuppressWarnings("unused")
	public void sendTracking(Context context,String appsFlyerKey){
		Method method = null;
		try {
			Class<?> AppsFlyerLib = Class.forName("com.appsflyer.AppsFlyerLib");
			Class<? extends Context> c = context.getClass();
			for(Method m : AppsFlyerLib.getDeclaredMethods())
		        if(m.getName().equals("sendTracking")){
		        	method = m;
		        }
			method.invoke(AppsFlyerLib, context, appsFlyerKey);
		} catch(ClassNotFoundException e){
			Log.e("AppsFlyer","Class not found, appsflyer is not found");
		} catch(IllegalAccessException e){
			Log.e("AppsFlyer","Illegal Access");
		} catch(InvocationTargetException e){
			Log.e("AppsFlyer","InvocationTarget Exception");
		} catch(NullPointerException e){
			Log.e("AppsFlyer","InvocationTarget Exception");
		}
	}
}
