package com.gkxim.android.thumbsdk.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random; 
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.appsflyer.AppsFlyerLib;
import com.gkxim.android.thumbsdk.utils.Event;


@SuppressLint("SimpleDateFormat")
public class EVA {


	//	PUBLIC EVENT LOGGING FUNCTIONS

	public void appInstalled(Context context){
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String firstlaunch = settings.getString("firstlaunch", "");
		if(firstlaunch == ""){
			DatabaseHandler db = new DatabaseHandler(context);
			Event event = new Event(this.getGuid(context),this.getDate(),"installation","{bodybodybody}");
			db.addEvent(event);
			settings.edit().putString("action", "launched").commit();		
		}
		detectInstalledApplications(context);
		getInstalledApplications(context);
		
	}


	//PRIVATE FUNCTIONS
	private String getDate(){ 
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");
		return sdf.format(currentTime);
	}

	private String getShortDate(){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSS");
		return sdf.format(currentTime);
	}

	private void saveEvent(Context context, String name,List bagged){
		DatabaseHandler db = new DatabaseHandler(context );		
	}

	private String hashString(String entity, String salt){
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(salt.getBytes(), "HmacSHA1"));
			byte[] bs = mac.doFinal(entity.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bs.length; i++){
				sb.append(Integer.toString((bs[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private void syncEvents(Context context){
		DatabaseHandler db = new DatabaseHandler(context );
		List<Event> events = db.getAllEvents();
		for (Event ev : events) {
			String log = "Guid: "+ev.getGuid()+" ,Date: " + ev.getDate() + " ,Name: " + ev.getName() + "Body" + ev.getBody();
			Log.d("ThumbrSDK", log);
		}		
	}

	private String getGuid(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return getShortDate() + hashIt(tm.getDeviceId());
	}

	private String hashIt(String entity){
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			String salt = "f368aaa8d63104735fe3";
			mac.init(new SecretKeySpec(salt.getBytes(), "HmacSHA1"));
			byte[] bs = mac.doFinal(entity.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 7; i++){
				sb.append(Integer.toString((bs[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String getInstallationId(Context context){
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String installationId = settings.getString("installationId", "");
		if(installationId == ""){
			String allowedCharacters = "-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			final Random random=new Random();
			final StringBuilder sb=new StringBuilder();
			for(int i=0;i<64;++i){
				sb.append(allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
			}
			settings.edit().putString("installationId", sb.toString()).commit();
			return sb.toString();
		}else{
			return installationId;
		}
	}

	private String getAdvertisingId(){
		return "";
	}

	private String getAndroidId(Context context){
		return Secure.getString(context.getContentResolver(),Secure.ANDROID_ID); 
	}

	private String getAppsflyerId(Context context){
		return AppsFlyerLib.getAppsFlyerUID(context);
	}

	private String getDeviceToken(){
		return "";
	}

	private String getRegistrationId(){
		//@TODO: Get the Google Cloud Messaging token
		return "";
	}

	private String getProfileId(Context context){
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String profile_id = settings.getString("thumbr_id","");		
		return profile_id;
	}

	private String getSid(Context context){
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String sid = settings.getString("sid","");		
		return sid;
	}

	private String getTrackingId(){
		return "";
	}

	private String getVendorId(){
		return "";
	}

	private String getBrand(){
		return Build.BRAND;
	}

	private String getDevice(){
		return Build.DEVICE;
	}

	private String getOs(){
		return "Android";
	}

	private String getOsVersion(){
		return Build.VERSION.RELEASE;
	}

	private String getDeviceCountry(Context context){
		return context.getResources().getConfiguration().locale.getCountry();
	}

	private String getDeviceLanguage(){
		return Locale.getDefault().getDisplayLanguage();
	}

	private String getInstalledApplications(Context context){
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String installedApplications = settings.getString("installedApplications", "");
		Log.i("ThumbrSDK",installedApplications);
		return installedApplications;
	}

	private void detectInstalledApplications(Context context) {
		String json  = "";
		List packs = context.getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packs.size();i++) {
			PackageInfo p = (PackageInfo) packs.get(i);
			if ((p.versionName == null) || p.packageName.contains("android.") || p.packageName.contains("samsung.")) {//no sys packages
				
				continue ;
			}
			String appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
			String pname = p.packageName;

			json = json + "{\"" + pname + "\":\""+appname+"\"},";
		}
		try {
			json =json.substring(0, json.length() - 1);//remove the last comma
		} catch (Exception e) {}
		
		json = "\"devices\" : ["+json+"]";
		
		SharedPreferences settings = context.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		settings.edit().putString("installedApplications", json).commit();		
		
	}	
	
}