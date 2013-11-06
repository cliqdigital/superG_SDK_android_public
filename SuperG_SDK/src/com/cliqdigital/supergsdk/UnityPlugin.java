package com.cliqdigital.supergsdk;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.MadsAdView.MadsAdView;
import com.cliqdigital.supergsdk.SuperG.OnInterstitialCloseListener;
import com.cliqdigital.supergsdk.SuperG.OnPushMessageListener;
import com.cliqdigital.supergsdk.components.AppsFlyerHelper;
import com.cliqdigital.supergsdk.utils.EVA;
import com.cliqdigital.supergsdk.utils.Log;
import com.cliqdigital.supergsdk.utils.ProfileObject;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class UnityPlugin extends UnityPlayerActivity implements OnInterstitialCloseListener{
	public static String 	sid;
	public static String 	client_id;
	public static int 		gameOrientation;
	public static int 		superGOrientation;
	public static boolean 	showButtonClose;
	public static double 	buttonWidth;
	public static int 		updateTimeInterval;
	public static int 		showCloseButtonTime;
	public static String 	tablet_Inline_zoneid;
	public static String 	tablet_Inline_secret;
	public static String 	tablet_Overlay_zoneid;
	public static String 	tablet_Overlay_secret;
	public static String 	tablet_Interstitial_zoneid;
	public static String 	tablet_Interstitial_secret;
	public static String 	phone_Inline_zoneid;
	public static String 	phone_Inline_secret;
	public static String 	phone_Overlay_zoneid;
	public static String 	phone_Overlay_secret;
	public static String 	phone_Interstitial_zoneid;
	public static String 	phone_Interstitial_secret;
	public static String 	action;
	public static String 	appsFlyerKey;	
	public static Boolean 	debug;		
	public static String 	registerUrl;
	public static String 	score_game_id;
	public static int 		hideThumbrCloseButton;
	public static String 	SDKLayout;
	public static String 	country;
	public static String 	locale;
	public static String 	appsFlyerId;

	public static SuperG SuperG ;

	//@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);		

		Locale l = Locale.getDefault();
		if(country == ""){country = l.getCountry();}
		if(locale == ""){locale = l.getLanguage()+"_"+country;}

		//IMPORT APPSFLYER		
		AppsFlyerHelper appsflyerhelper = new AppsFlyerHelper();
		appsflyerhelper.sendTracking(this,appsFlyerKey);
		appsFlyerId = appsflyerhelper.getAppsflyerId(this);

		// SET VIEW
		super.onCreate(savedInstanceState);

		//CALL THE SuperG LIBRARY
		SuperG=new SuperG(this, 2);
		SuperG.setEnableButtonClose(showButtonClose);
		SuperG.setToastDebug(true);
		SuperG.setLayout(SDKLayout);
		SuperG.setOrientation(superGOrientation);
		SuperG.setAction(action);
		SuperG.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="
				+client_id
				+"&handset_id="+appsFlyerId
				);			
		SuperG.adInit();//initialize Ads if you are showing Advertisements    

		//THIS IS A LISTENER FOR PUSH MESSAGES. YOU CAN SET CERTAIN TASKS BASED ON THE 'action' PARAMETER 
		SuperG.setPushMessageListener(new OnPushMessageListener(){
			@Override
			public void onPushEvent(Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getExtras().getString("action");
				String message = intent.getExtras().getString("message");
				if(message != ""){
					UnityPlayer.UnitySendMessage("superG", "pushMessageReceived", message); 
				}
				if(action != ""){
					UnityPlayer.UnitySendMessage("superG", "pushActionReceived", action);	
				}
			}	
		});

	}


	public static void setAdSettings(){

		SharedPreferences settings = UnityPlayer.currentActivity.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		settings.edit().putString("score_game_id",score_game_id).commit();
		settings.edit().putInt("updateTimeInterval", updateTimeInterval).commit();		
		settings.edit().putInt("showCloseButtonTime", showCloseButtonTime).commit();
		settings.edit().putInt("hideThumbrCloseButton", hideThumbrCloseButton).commit();
		settings.edit().putString("tablet_Inline_zoneid", tablet_Inline_zoneid).commit();		
		settings.edit().putString("tablet_Inline_secret", tablet_Inline_secret).commit();		
		settings.edit().putString("tablet_Overlay_zoneid", tablet_Overlay_zoneid).commit();		
		settings.edit().putString("tablet_Overlay_secret", tablet_Overlay_secret).commit();		
		settings.edit().putString("tablet_Interstitial_zoneid", tablet_Interstitial_zoneid).commit();		
		settings.edit().putString("tablet_Interstitial_secret", tablet_Interstitial_secret).commit();		
		settings.edit().putString("phone_Inline_zoneid", phone_Inline_zoneid).commit();		
		settings.edit().putString("phone_Inline_secret", phone_Inline_secret).commit();		
		settings.edit().putString("phone_Overlay_zoneid", phone_Overlay_zoneid).commit();		
		settings.edit().putString("phone_Overlay_secret", phone_Overlay_secret).commit();		
		settings.edit().putString("phone_Interstitial_zoneid", phone_Interstitial_zoneid).commit();		
		settings.edit().putString("phone_Interstitial_secret", phone_Interstitial_secret).commit();		
		settings.edit().putString("sid", sid).commit();
		settings.edit().putString("client_id", client_id).commit();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public static void superG_OpenDialog() {
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {

				SuperG=new SuperG(UnityPlayer.currentActivity, 2);	
				SuperG.setEnableButtonClose(showButtonClose);
				SuperG.setToastDebug(true);
				SuperG.setLayout(SDKLayout);

				SuperG.setAction(action);
				SuperG.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="+client_id
						+"&handset_id="+appsFlyerId
						);

				if( com.cliqdigital.supergsdk.SuperG.isTabletDevice(UnityPlayer.currentActivity) == true) {
					//this is a tablet
					gameOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					superGOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				}else{
					//this is not a tablet
					gameOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					superGOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				}	
				SuperG.setOrientation(superGOrientation);
				SuperG.buttonREGISTER();
			}

		});

	}	


	public static void superG_Initialize(String Usid, String Uclient_id, boolean UshowButtonClose, double UbuttonWidth,
			int UupdateTimeInterval, int UshowCloseButtonTime, String Utablet_Inline_zoneid, String Utablet_Inline_secret, String Utablet_Overlay_zoneid, String Utablet_Overlay_secret, String Utablet_Interstitial_zoneid, String Utablet_Interstitial_secret, String Uphone_Inline_zoneid, String Uphone_Inline_secret, String Uphone_Overlay_zoneid, String Uphone_Overlay_secret, String Uphone_Interstitial_zoneid, String Uphone_Interstitial_secret, String Uaction, String UappsFlyerKey, boolean Udebug, String UregisterUrl, String Uscore_game_id, int UhideThumbrCloseButton, String USDKLayout, String Ucountry, String Ulocale
			, String UappsFlyerId
			) {
		sid = Usid;
		client_id = Uclient_id;
		showButtonClose = UshowButtonClose;
		buttonWidth = UbuttonWidth;
		updateTimeInterval = UupdateTimeInterval;
		showCloseButtonTime = UshowCloseButtonTime;
		tablet_Inline_zoneid = Utablet_Inline_zoneid;
		tablet_Inline_secret = Utablet_Inline_secret;
		tablet_Overlay_zoneid = Utablet_Overlay_zoneid;
		tablet_Overlay_secret = Utablet_Overlay_secret;
		tablet_Interstitial_zoneid = Utablet_Interstitial_zoneid;
		tablet_Interstitial_secret = Utablet_Interstitial_secret;
		phone_Inline_zoneid = Uphone_Inline_zoneid;
		phone_Inline_secret = Uphone_Inline_secret;
		phone_Overlay_zoneid = Uphone_Overlay_zoneid;
		phone_Overlay_secret = Uphone_Overlay_secret;
		phone_Interstitial_zoneid = Uphone_Interstitial_zoneid;
		phone_Interstitial_secret = Uphone_Interstitial_secret;
		action = Uaction;
		appsFlyerKey = UappsFlyerKey;
		debug = Udebug;
		registerUrl = UregisterUrl;
		score_game_id = Uscore_game_id;
		hideThumbrCloseButton = UhideThumbrCloseButton;
		SDKLayout = USDKLayout;
		country = Ucountry;
		locale = Ulocale;
		appsFlyerId = UappsFlyerId;

		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				AppsFlyerHelper appsflyerhelper = new AppsFlyerHelper();
				appsflyerhelper.sendTracking(UnityPlayer.currentActivity,appsFlyerKey);
				appsFlyerId = appsflyerhelper.getAppsflyerId(UnityPlayer.currentActivity);		
			}
		});
	}	

	public static void superG_AdOverlay(){

		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				UnityPlugin.setAdSettings();
				LinearLayout av2 = (LinearLayout) UnityPlayer.currentActivity.findViewById(77665544);
				View av = (MadsAdView) UnityPlayer.currentActivity.findViewById(11223344);
				try {
					((ViewGroup) av.getParent()).removeView(av);
					((ViewGroup) av.getParent()).removeView(av2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LinearLayout ad_view_wrapper = new LinearLayout(UnityPlayer.currentActivity);
				RelativeLayout ad_view = new RelativeLayout(UnityPlayer.currentActivity);

				ad_view_wrapper.setId(77665544);
				LayoutParams rlp = new RelativeLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				WindowManager wm = (WindowManager) UnityPlayer.currentActivity.getSystemService(Context.WINDOW_SERVICE);

				SuperG=new SuperG(UnityPlayer.currentActivity, 2);	
				ad_view_wrapper.addView(ad_view);
				UnityPlayer.currentActivity.addContentView(ad_view_wrapper, rlp);

				SuperG.adOverlay(ad_view);

			}
		});
	}	

	public static void superG_AdInline(int x,int y,int w,int h){
		final int xValue=x;
		final int yValue=y;
		final int wValue=w;
		final int hValue=h;		
		Log.i("SuperGSDK","open inline with: "+xValue+"x"+yValue+"x"+wValue+"x"+hValue);
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				UnityPlugin.setAdSettings();
				LinearLayout av = (LinearLayout) UnityPlayer.currentActivity.findViewById(99887766);

				try {
					((ViewGroup) av.getParent()).removeView(av);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LinearLayout ad_view_wrapper = new LinearLayout(UnityPlayer.currentActivity);
				RelativeLayout ad_view = new RelativeLayout(UnityPlayer.currentActivity);

				ad_view_wrapper.setId(99887766);
				LayoutParams rlp = new RelativeLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				WindowManager wm = (WindowManager) UnityPlayer.currentActivity.getSystemService(Context.WINDOW_SERVICE);				
				Display display = wm.getDefaultDisplay();
				ad_view_wrapper.setPadding(0, yValue, display.getWidth()-wValue-xValue, display.getHeight()-hValue-yValue);

				SuperG=new SuperG(UnityPlayer.currentActivity, 2);	
				ad_view_wrapper.addView(ad_view);
				UnityPlayer.currentActivity.addContentView(ad_view_wrapper, rlp);

				SuperG.adInline(ad_view);

			}
		});
	}	


	public static void superG_AdInterstitial(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				RelativeLayout av = (RelativeLayout) UnityPlayer.currentActivity.findViewById(88776655);

				try {
					((ViewGroup) av.getParent()).removeView(av);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				UnityPlugin.setAdSettings();
				RelativeLayout ad_view = new RelativeLayout(UnityPlayer.currentActivity);
				ad_view.setId(88776655);
				View topview = UnityPlayer.currentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
				SuperG=new SuperG(UnityPlayer.currentActivity, 2);	
				((ViewGroup) topview).addView(ad_view);
				SuperG.adInterstitial(ad_view);

			}
		});

	}	

	public static void superG_RemoveAdInterstitial(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				final View av = (MadsAdView) UnityPlayer.currentActivity.findViewById(9875737);
				final View av2 = (RelativeLayout) UnityPlayer.currentActivity.findViewById(88776655);


				try {
					((ViewGroup) av.getParent()).removeView(av);
					((ViewGroup) av2.getParent()).removeView(av);	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}	

	public static void superG_RemoveAdOverlay(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				View av = (MadsAdView) UnityPlayer.currentActivity.findViewById(11223344);
				View av2 = (LinearLayout) UnityPlayer.currentActivity.findViewById(77665544);
				try {
					((ViewGroup) av.getParent()).removeView(av);
					((ViewGroup) av.getParent()).removeView(av2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}	

	public static void superG_RemoveAdInline(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				View av = (LinearLayout) UnityPlayer.currentActivity.findViewById(99887766);

				try {
					((ViewGroup) av.getParent()).removeView(av);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	public static void superG_AchievementEarnedEvent(String achievement){
		EVA eva = new EVA();
		eva.achievementEarned(UnityPlayer.currentActivity, achievement);
	}


	public static void superG_ClickEvent(String clicked_item){
		EVA eva = new EVA();
		eva.click(UnityPlayer.currentActivity, clicked_item);
	}

	public static void superG_PurchaseEvent(String currency,String payment_method,String price,String purchased_item){
		EVA eva = new EVA();
		//usage: eva.purchase(context, currency, payment_method, price, purchased_item)
		eva.purchase(UnityPlayer.currentActivity, currency, payment_method, price, purchased_item);
	}

	public static void superG_StartLevelEvent(String app_mode, String level, String score_type, String score_value){
		EVA eva = new EVA();
		eva.startLevel(UnityPlayer.currentActivity, app_mode, level, score_type, score_value);
	}

	public static void superG_FinishLevelEvent(String app_mode,String level,String score_type,String score_value){
		EVA eva = new EVA();
		eva.finishLevel(UnityPlayer.currentActivity, app_mode, level, score_type, score_value);
	}

	public static void superG_UpSellEvent(String currency, String payment_method){
		EVA eva = new EVA();
		eva.upSell(UnityPlayer.currentActivity, currency, payment_method);
	}	

	public static void superG_resume(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				SuperG=new SuperG(UnityPlayer.currentActivity, 2);
				SuperG.resume();
			}
		});
	}

	public static void superG_pause(){
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				SuperG=new SuperG(UnityPlayer.currentActivity, 2);
				SuperG.pause(UnityPlayer.currentActivity);
			}
		});
	}

	//	@Override
	//	protected void onPause() {
	//		super.onPause();		
	//		SuperG.pause(this);
	//	}	
	//	@Override
	//	protected void onResume(){
	//		super.onResume();
	//		SuperG.resume(); 
	//	}
	/*
	 * THIS FUNCTION IS CALLED WHEN THE THUMBR SDK WINDOW CLOSES.
	 */

	public void dismiss(ProfileObject profileObject) {
		String json = "";
		if(profileObject != null){
			json = "{\"mID\":\""+profileObject.mID+"\",\"mUserName\":\""+profileObject.mUserName+"\",\"mStatus\":\""+profileObject.mStatus+"\",\"mEmail\":\""+profileObject.mEmail+"\",\"mSurname\":\""+profileObject.mSurname+"\",\"mGender\":\""+profileObject.mGender+"\",\"mAge\":\""+profileObject.mAge+"\",	\"mDOB\":\""+profileObject.mDOB+"\",\"mLocale\":\""+profileObject.mLocale+"\",\"mCity\":\""+profileObject.mCity+"\",\"mIncome\":\""+profileObject.mIncome+"\",	\"mCountry\":\""+profileObject.mCountry+"\",	\"mAddress\":\""+profileObject.mAddress+"\",\"mZipCode\":\""+profileObject.mZipCode+"\",\"mNewsLetter\":\""+profileObject.mNewsLetter+"\",\"mFirstName\":\""+profileObject.mFirstName+"\",\"mMsisdn\":\""+profileObject.mSisdn+"\",\"mHousenr\":\""+profileObject.mHousenr+"\"}";
		}

		Log.i("SuperGSDK","dismiss listener called this function " + json);
		//SEND A MESSAGE TO THE UNITY APPLICATION
		UnityPlayer.UnitySendMessage("superG", "dismissMessage", json);  	
	}
	public static void interstitialClosed(){
		Log.i("SuperGSDK","The interstitial closed");
	}

	@Override
	public void onEvent() {
		// TODO Auto-generated method stub

	} 


}
