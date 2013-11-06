package com.cliqdigital.supergsdk.utils;
//package com.gkxim.android.thumbsdk.utils;
//
//import java.util.ArrayList;
//
//import com.facebook.Session;
//import com.facebook.Session.OpenRequest;
//import com.facebook.SessionState;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import com.cliqdigital.supergsdk.utils.Log;
//
//public class FacebookHelper{
//
//	Context context;
//	String MY_APP_ID = "160421370828993";
//	Session session;
//	Activity activity;
//	
//
//	public FacebookHelper(Context context) {
//		this.context = context;
//		activity = (Activity) context;
//	}
//
//	public boolean isFacebookInstalled() {
//		try {
//			context.getPackageManager().getApplicationInfo(
//					"com.facebook.katana", 0);
//			return true;
//		} catch (PackageManager.NameNotFoundException e) {
//			return false;
//		}
//	}
//
//	public void openSession() {
//		if(isFacebookInstalled()){
//			ArrayList<String> permissions = new ArrayList<String>();
//			permissions.add("user_events");
//			permissions.add("friends_online_presence");
//
//			session = new Session.Builder((Activity) context).setApplicationId(MY_APP_ID).build();
//            Session.setActiveSession(session);
//            OpenRequest openRequest = new Session.OpenRequest(activity).setCallback(new FacebookSessionStatusCallback());
//            openRequest.setPermissions(permissions);
//            session.openForRead(openRequest);
//		}
//		else {
//			Log.i("facebook","Facebook not installed.");
//		}
//		
//	}
//	
//	public void State(){
//		session = Session.getActiveSession();
//		if(session==null){
//			Log.i("facebook","Session is null");
//		}
//		else {
//			Log.i("facebook",session.getState().toString());
//		}
//		
//	}
//
//	public void closeFBSession() {
//		session = Session.getActiveSession();
//		if (session != null) {
//			session.closeAndClearTokenInformation();
//		}
//	}
//	
//	public String getAccessToken(){
//		session = Session.getActiveSession();
//		if(session == null){
//			openSession();
//		}
//		return session.getAccessToken();
//	}
//	
//	private class FacebookSessionStatusCallback implements Session.StatusCallback {
//	    @Override
//	    public void call(Session session, SessionState state, Exception exception) {
//	            getAccessToken();
//	            Log.i("facebookAccesToken",getAccessToken());
//	    }
//	}
//}
