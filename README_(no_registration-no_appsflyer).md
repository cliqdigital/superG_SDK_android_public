SuperG SDK for Android
=========================


SuperG SDK installation (no registration, no Appsflyer)
-----------------------

### Prerequisites
* Push messaging is enabled by default
* Event logging is enabled by default. You can set custom events by following the guidelines in the part 'Optional 1'

### Step 1: 
Unzip the SDK package.
If you are using the Demo App, be sure to keep the SDK and the Demo App in the same directory.


###Step 2: 
Add a reference to the SuperG SDK library project. Follow these steps:

1. 	Import the SuperG SDK project into Eclipse:
2.	In the Package Explorer, right-click YOUR project and select Properties.	
3.	In the Properties window, select the “Android” properties group at left and locate the Library properties at right.
4.	Click Add to open the Project Selection dialog.
5.	From the list of available library project, select the SuperG SDK project and click OK.
6.	When the dialog closes, click Apply in the Properties window.
7.	Click OK to close the Properties window
As soon as the Properties dialog closes, Eclipse rebuilds your project.

### Step 3 (optional): 
Add the TestApp project to Eclipse to understand the methods better.

### Step 4: 
Update your AndroidManifest.xml with at least the settings below.

*A lot of permissions are asked, this is because the advertising functionality actually requires most of them!!!*


	<?xml version="1.0" encoding="utf-8"?>
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
	    package="com.yourcompany.your_app"
	    android:versionCode="1"
	    android:versionName="1.0" >
	        <uses-sdk android:targetSdkVersion="17" android:minSdkVersion="8" />

	<permission
        android:name="com.yourcompany.your_app.permission.C2D_MESSAGE"
        android:protectionLevel="signature" />
    <uses-permission android:name="com.gkxim.tqhung.thumbr.dev_demo.permission.C2D_MESSAGE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.READ_CALENDAR" />
    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	<uses-permission android:name="android.permission.VIBRATE" />
	<uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
    	
	    <application
	        android:icon="@drawable/ic_launcher"
	        android:debuggable="true"        
	        android:label="YourLabel" > 
	        
	         <activity
	            android:name="com.yourcompany.your_app.yourActivity"
	            android:configChanges="orientation|screenSize|keyboardHidden"
	            android:screenOrientation="portrait"
	            android:label="@string/app_name" >
	            <intent-filter>
	                <action android:name="android.intent.action.MAIN" />
	                <category android:name="android.intent.category.LAUNCHER" />
	            </intent-filter>
	        </activity>
        <activity
            android:name="com.adgoji.mraid.adview.AdActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode" />

        <receiver
            android:name="com.cliqdigital.supergsdk.utils.GCMReceiver"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
                <category android:name="com.yourcompany.your_app" />
            </intent-filter>
        </receiver>
        <service android:name="com.cliqdigital.supergsdk.utils.GCMIntentService" />	        
	    </application>
	
	</manifest>


### Step 5:

If you are using Advertisements add these ad_views to your layout (s)

	<RelativeLayout
        android:id="@+id/ad_view"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerInParent="true"
        android:layout_gravity="bottom">
    </RelativeLayout>	
	
	<RelativeLayout
        android:id="@+id/ad_view_interstitial"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true">
    </RelativeLayout>	

        	
### Step 6 ACTIVITY FILE:
Update the *activity file*, where SuperG will be called (usually your main activity)
Look at the demo application to see a [complete implementation](https://github.com/cliqdigital/thumbr_SDK_android_public/blob/master/Demo%20App/src/com/gkxim/tqhung/thumbr/dev_demo/ThumbrSDKTest.java) 


	package com.yourcompany.yourapp;

**Import at least these libraries**

	import java.util.Locale;
	import android.app.Activity;
	import android.content.Context;
	import android.content.Intent;
	import android.content.SharedPreferences;
	import android.content.pm.ActivityInfo;
	import android.os.Bundle;
	import android.os.Handler;
	import android.util.Log;
	import android.view.Menu;
	import android.widget.RelativeLayout;
	import android.widget.Toast;
	import com.cliqdigital.supergsdk.SuperG;
	import com.cliqdigital.supergsdk.SuperG.OnInterstitialCloseListener;
	import com.cliqdigital.supergsdk.SuperG.OnPushMessageListener;
	import com.cliqdigital.supergsdk.utils.EVA;


		
**Open your Main activity class and enter your app specific settings (provided by your SuperG Game Manager). 
THE AD SERVING SETTINGS NEED TO BE REPLACED. THE SETTINGS BELOW ARE DEMO SETTINGS ONLY**
		
	/*
	 * SETTINGS
	 */
	private Boolean debug = false; //OPTIONALLY SWITCH TO 'true' DURING DEVELOPMENT		
	//APP SPECIFIC SETTINGS
	private String sid = "com.yourcompany.your_app";
	private String client_id = "84758475-476574";
	
	//AD SERVING SETTINGS
	private int updateTimeInterval = 15;//number of seconds before Ad refresh
	private int autocloseInterstitialTime = 600;//number of seconds before interstitial Ad closes
	private int showCloseButtonTime = 6;//Number of seconds before the Ad close button appears
	private String tablet_Inline_zoneid = "1356888057";
	private String tablet_Inline_secret = "20E1A8C6655F7D3E";
	private String tablet_Overlay_zoneid = "3356907052";
	private String tablet_Overlay_secret = "ADAA22CB6D2AFDD3";
	private String tablet_Interstitial_zoneid = "7356917050";
	private String tablet_Interstitial_secret = "CB45B76FE96C8896";
	private String phone_Inline_zoneid = "0345893057";
	private String phone_Inline_secret = "04F006733229C984";
	private String phone_Overlay_zoneid = "7345907052";
	private String phone_Overlay_secret = "AEAAA69F395BA8FA";
	private String phone_Interstitial_zoneid = "9345913059";
	private String phone_Interstitial_secret = "04B882960D362099";
	
	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	private String country = "";//eg: DE or NL
	private String locale = "";//eg: nl_NL or de_DE

	/*
	 * END SETTINGS
	 */

**Define the 'superG' object and add / edit onPause and onResume functions**

	SuperG superG;

	@Override
	protected void onPause() {
	superG.pause(this);
	super.onPause();
	}	
	@Override
	protected void onResume(){
		superG.resume();
		super.onResume();		
	}
	

**Modify your onCreate function**

		@Override
		protected void onCreate(Bundle savedInstanceState) {
		
		Locale l = Locale.getDefault();
		if(country == ""){country = l.getCountry();}
		if(locale == ""){locale = l.getLanguage()+"_"+country;}

	
		//SET AD SERVER SETTINGS
		SharedPreferences settings = this.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		settings.edit().putInt("updateTimeInterval", updateTimeInterval).commit();
		settings.edit().putInt("autocloseInterstitialTime", autocloseInterstitialTime).commit();
		settings.edit().putInt("showCloseButtonTime", showCloseButtonTime).commit();
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
			
			// SET VIEW FOR YOUR APPLICATION
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);//your activity content view
			
											
			//CALL THE SUPERG LIBRARY AND REGISTER SETTINGS
			superG=new SuperG(this, 2);
			superG.setToastDebug(debug);
			superG.adInit();//initialize Ads if you are showing Ads

			superG.setInterstitialCloseListener(new OnInterstitialCloseListener(){
				public void onEvent(){
				Log.i(TAG,"The interstitial advertisement was closed. You can resume your game.");
				//Toast.makeText(getApplicationContext(), "The game has been notified about closing the interstitial", 6000).show();
				//resumeYourGame();
			}
		});	

		//THIS IS AN (OVERRIDE) LISTENER FOR PUSH MESSAGES. YOU CAN SET CERTAIN TASKS BASED ON THE 'action' PARAMETER 
		superG.setPushMessageListener(new OnPushMessageListener(){
			@Override
			public void onPushEvent(Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getExtras().getString("action");
				String message = intent.getExtras().getString("message");

				if(action.equals("customEvent")){
					Log.i(TAG,"Action '"+action+"' was received. Message is:"+message);
				}
			}	
		});
				
			}

	
### Advertisements	
	
**To show Advertisements use (any of) these functions at appropriate points in your application**

	//INLINE ADVERTISEMENT:
		final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view);
		superG.adInline(ad_view);	

	//OVERLAY ADVERTISEMENT:	
		final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view);
		superG.adOverlay(ad_view);

	//INTERSTITIAL ADVERTISEMENT:
		final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view_interstitial);
		superG.adInterstitial(ad_view);
					


###Optional 1: Custom event logging
Basis events are logged and stored by default.
You can use the following extra methods to store other events:

Achievement event

			EVA eva = new EVA();
			//usage: eva.achievementEarned(context, achievement_name)
			eva.achievementEarned(this, "FoundGold");

Click event

			EVA eva = new EVA();
			//usage: eva.click(context, clicked_item)
			eva.click(this, "SomeButton");


Purchase event

			EVA eva = new EVA();
			//usage: eva.purchase(context, currency, payment_method, price, purchased_item)
			eva.purchase(this, "EUR", "in-app-purchase", "0.99", "ProStatus");


Start Level event

			EVA eva = new EVA();
			//usage: eva.startLevel(context, app_mode, level, score_type, score_value)
			eva.startLevel(this, "basic", "1", "points", "0");

Finish Level event

			EVA eva = new EVA();
			//usage: eva.finishLevel(context, app_mode, level, score_type, score_value)
			eva.finishLevel(this, "basic", "1", "points", "120");

Upsell event

			EVA eva = new EVA();
			//usage: eva.upSell(context, currency, payment_method)
			eva.upSell(this, "EUR", "in-app-purchase");

###Optional 2: Proguard

Add these lines to your Proguard configuration:

	-dontwarn com.unity3d.**
	-dontwarn com.appsflyer.**

If Proguard still gives you errors, please look at the progruard.cfg in the Demo project.
