package com.gkxim.android.thumbsdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import android.util.Log;

public class EVA {

	//	PUBLIC EVENT LOGGING FUNCTIONS

	public void installation(){
		Log.i("ThumbrSDK",this.getDate());
	}

	//PRIVATE FUNCTIONS
	private String getDate(){ 
		final Date currentTime = new Date();
		final SimpleDateFormat sdf =
		new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss a z");
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println("GMT time: " + sdf.format(currentTime));
		return null;
	}

	private String getShortDate(){
		return null;
	}

	private void openDB(){
		
	}

	private void saveEvent(String name,List bagged){
		
	}

	private String hashString(String data, String salt){
		return null;
	}

	private void syncEvents(){
		
	}

	private String getGuid(){
		return null;
	}

	private String hashIt(String input){
		return null;
	}

	private String getInstallationId(){
		return null;
	}

	private String getRandom(){
		return null;
	}

	private String getAdvertisingId(){
		return null;
	}

	private String getAndroidId(){
		return null;
	}

	private String getAppsflyerId(){
		return null;
	}

	private String getDeviceToken(){
		return null;
	}

	private String getRegistrationId(){
		return null;
	}

	private String getProfileId(){
		return null;
	}

	private String getSid(){
		return null;
	}

	private String getTrackingId(){
		return null;
	}

	private String getVendorId(){
		return null;
	}

	private String getBrand(){
		return null;
	}

	private String getDevice(){
		return null;
	}

	private String getOsVersion(){
		return null;
	}

	private String getDeviceCountry(){
		return null;
	}

	private String getDeviceLanguage(){
		return null;
	}

	private void detectInstalledApplications(){
		
	}

	private String getInstalledApplications(){
		return null;
	}

}

