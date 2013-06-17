SuperG SDK for Android
=========================


Revision history
----------------
<table>
	<tr>
		<td>2.0.32</td>
		<td>
		-Improved interstitial advertisements<br />
		-Added ad serving-only demo
		</td>
	</tr>
	<tr>
		<td>2.0.31</td>
		<td>
		-Personalized Ad serving support<br />
		-Technical updates
		</td>
	</tr>
	<tr>
		<td>2.0.3</td>
		<td>
			-Ad serving support<br />
			-Added SSL support<br />
			-Some small bug fixes
		</td>
	</tr>
	<tr>
		<td>2.0.22</td>
		<td>
		-Added support for alternative layout<br />
		-Catch url's that contain 'openinbrowser' and open them in browser<br />
		-Send along SDK version number to Thumbr server<br />
		-Improved orientation behavior<br />
		</td>
	</tr>
	<tr>
		<td>2.0.21</td><td>
		-Better Thumbr T-button resizing<br />
		-Added visual SDK version to Thumbr window<br />
		-Added Customizable Orientation<br />
		-Added extra parameters to registration flow: default,registration,optional_registration<br />
		-Added a counter to the 'opens' of the Thumbr SDK window<br />
		-Let external URL's (within the SDK window) be opened in the default browser<br />
		-Bug fix: better orientation handling in general<br />
		-Let Thumbr server know that back end is loaded from within the SDK (via GET param (&sdk=1) + via HEADER (x-thumbr-method))<br />
		-Added version header (X-Thumbr-Version)<br />
		</td>
	</tr>
	<tr>
		<td>2.0.2</td><td>
		-Updated version number to match iOS version (for better release planning)<br />
		-Added Animated Thumbr T-button support<br />
		-Added SDK version number to Thumbr screen<br />
		-Bug fixes<br />
		</td>
	</tr>	
	<tr>
		<td>1.1</td><td>
		- Added Appsflyer support
		</td>
	</tr>	
	<tr>
		<td>1.0.1</td><td>
		- Bug fixes
		</td>
	</tr>	
	<tr>
		<td>1.0</td><td>
		- Initital version
		</td>
	</tr>	
</table>

SuperG SDK installation
-----------------------

### Step 1: 
Unzip the SDK package and keep the directories ‘ThumbSDK’ and ‘TestApp’ OR YOUR APPLICATION within the same directory.

### Step 2: 
Take **Vendor/AF-Android-SDK-v1.3.9.jar** and add it to the lib-folder of your Android project.
Don't forget to add it to the build path.

###Step 3: 
Add a reference to the Thumbr SDK library project. Follow these steps:

1. 	Import the Thumbr SDK project into Eclipse
2.	In the Package Explorer, right-click YOUR project and select Properties.	
3.	In the Properties window, select the “Android” properties group at left and locate the Library properties at right.
4.	Click Add to open the Project Selection dialog.
5.	From the list of available library project, select the Thumbr SDK project and click OK.
6.	When the dialog closes, click Apply in the Properties window.
7.	Click OK to close the Properties window
As soon as the Properties dialog closes, Eclipse rebuilds your project.

### Step 4 (optional): 
Add the TestApp project to Eclipse to understand the methods better.

### Step 5: 
Update your AndroidManifest.xml with at least the settings below:


	<?xml version="1.0" encoding="utf-8"?>
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
	    package="com.yourcompany.yourgame"
	    android:versionCode="1"
	    android:versionName="1.0" >
	        <uses-sdk android:targetSdkVersion="16" android:minSdkVersion="10" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.READ_CALENDAR" />
    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    	
	    <application
	        android:icon="@drawable/ic_launcher"
	        android:debuggable="true"        
	        android:label="YourLabel" > 
	        
	        <receiver android:name="com.appsflyer.MultipleInstallBroadcastReceiver" android:exported="true">
				<intent-filter>
				<action android:name="com.android.vending.INSTALL_REFERRER" />
				</intent-filter>
			</receiver>
			
	         <activity
	            android:name="com.yourcompany.yourgame.yourActivity"
	            android:configChanges="orientation|screenSize|keyboardHidden"
	            android:screenOrientation="landscape"
	            android:label="@string/app_name" >
	            <intent-filter>
	                <action android:name="android.intent.action.MAIN" />
	                <category android:name="android.intent.category.LAUNCHER" />
	            </intent-filter>
	        </activity>
	        
	    </application>
	
	</manifest>


### Step 5:
Create the Thumbr button in your layout (eg. the main screen)

Register button (bt_re) (within linear layout)

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|right"
        android:orientation="horizontal" >
        
        <ImageButton
            android:layout_weight="1"
            android:id="@+id/bt_re"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
			android:background="@drawable/thumbr_00030"                    
             />
    </LinearLayout>

Switch User button (bt_switch) :: OPTIONAL :: THIS BUTTON IS USED TO LET THE USER SWITCH TO ANOTHER USER ACCOUNT

	<Button android:id="@+id/bt_switch" 
	style="?android:attr/buttonStyleSmall" 
	android:layout_width="wrap_content" 
	android:layout_height="wrap_content" 
	android:layout_gravity="bottom|left" 
	android:text="Switch User" />
	
If you are using Advertisements add these ad_views to your layout (s)

	<RelativeLayout
        android:id="@+id/ad_view"
        android:layout_width="fill_parent"
        android:layout_height="0dp"
        android:layout_alignParentBottom="false"
        android:layout_alignParentTop="true"
        android:layout_centerInParent="true"
        android:layout_gravity="bottom"
        android:background="@color/transparent" >
    </RelativeLayout>	
	
	<RelativeLayout
        android:id="@+id/ad_view_interstitial"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="@color/transparent" >
    </RelativeLayout>	
	
### Step 6 ACTIVITY FILE:
Update the *activity file*, where Thumbr will be called (usually your main activity)
Look at the demo application to see a [complete implementation](https://github.com/cliqdigital/thumbr_SDK_android_public/blob/master/Demo%20App/src/com/gkxim/tqhung/thumbr/dev_demo/ThumbrSDKTest.java) 


		package com.yourcompany.yourapp.youractivity;

**Import at least these libraries**

	import java.util.Locale;
	import android.annotation.SuppressLint;
	import android.app.Activity;
	import android.content.Context;
	import android.content.DialogInterface;
	import android.content.DialogInterface.OnDismissListener;
	import android.content.SharedPreferences;
	import android.content.pm.ActivityInfo;
	import android.graphics.drawable.AnimationDrawable;
	import android.os.Bundle;
	import android.os.StrictMode;
	import android.util.Log;
	import android.view.Display;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.view.ViewGroup.LayoutParams;
	import android.widget.Button;
	import android.widget.ImageButton;
	import android.widget.LinearLayout;
	import android.widget.RelativeLayout;
	import android.widget.Toast;	
	import com.appsflyer.AppsFlyerLib;
	import com.gkxim.android.thumbsdk.FunctionThumbrSDK;
	import com.gkxim.android.thumbsdk.utils.ProfileObject;

		
**Open your class and make sure to implement OnClickListener, OndismissListener**

		public class yourActivity extends Activity implements OnClickListener,OnDismissListener{
			
		/*
		 * SETTINGS
		 */
		 
**Enter your app specific settings (provided by your Thumbr Game Manager)**

		//APP SPECIFIC SETTINGS
		private String sid = "com.yourcompany.yourgame";
		private String client_id = "989598-8738744";
		
		//DEFAULT GAME ORIENTATION (USED IN onDismiss() BELOW, TO SWITCH BACK AFTER SDK CLOSE)
		private int gameOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		//TELL THE THUMBR SDK IN WHICH ORIENTATION TO OPEN
		private int thumbrSDKOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
		private boolean showButtonClose = false;
		//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
		private double buttonWidth = 0.2;

**Add more generic settings. <br />
THE AD SERVING SETTINGS NEED TO BE REPLACED. THE SETTINGS BELOW ARE DEMO SETTINGS ONLY**
		
	/*
	 * OTHER, MORE GENERIC SETTINGS
	 */
	private String action = "registration";
	private String appsFlyerKey = "9ngR4oQcH5qz7qxcFb7ftd";	
	private Boolean debug = false; //OPTIONALLY SWITCH TO 'true' DURING IMPLEMENTATION		
	private String registerUrl = "http://gasp.thumbr.com/auth/authorize?";
	private String switchUrl = "http://gasp.thumbr.com/auth/authorize?";
	private String portalUrl = "http://m.thumbr.com?";
	private String SDKLayout = "thumbr";
	
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
	private String appsFlyerId="";	
		/*
		 * END SETTINGS
		 */

**Define the 'thumbr' object and add / edit onPause and onResume functions**

		FunctionThumbrSDK thumbr;

	@Override
	protected void onPause() {
		thumbr.pause();
	super.onPause();
	}	
	@Override
	protected void onResume(){
		thumbr.resume();
		super.onResume();		
	}
	
**Modify your onCreate function**

		@Override
		protected void onCreate(Bundle savedInstanceState) {
		
		Locale l = Locale.getDefault();
		if(country == ""){country = l.getCountry();}
		if(locale == ""){locale = l.getLanguage()+"_"+country;}

			
			//IMPORT APPSFLYER
			AppsFlyerLib.sendTracking(this,appsFlyerKey);
			appsFlyerId = AppsFlyerLib.getAppsFlyerUID(this);
	
		//SET AD SERVER SETTINGS
		SharedPreferences settings = this.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		settings.edit().putString("score_game_id",score_game_id).commit();
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
			setContentView(com.yourcompany.yourgame.homescreen);
			
			//CREATE BUTTON LISTENERS
			ImageButton bt=(ImageButton) findViewById(com.yourcompany.yourapp.R.id.bt_re);
			bt.setOnClickListener(this);
			Button bt_switch=(Button) findViewById(com.yourcompany.yourapp.R.id.bt_switch);
			bt_switch.setOnClickListener(this);		
			
			//CALL THE THUMBR LIBRARY AND REGISTER SETTINGS
			thumbr=new FunctionThumbrSDK(this, 2);
			thumbr.setEnableButtonClose(showButtonClose);
			thumbr.setToastDebug(debug);
			thumbr.setOrientation(thumbrSDKOrientation);
			thumbr.setLayout(SDKLayout);
			thumbr.setAction(action);
		
		    /*
		     AT ANY POINT IN THE GAME, YOU CAN CHANGE THE ACTION BEHIND THE THUMBR BUTTON
		     ALL POSSIBLE VALUES:
		     
		     -default (the default behaviour)
		     -registration (forced registration form)
		     -optional_registration (registration is optional. 
		     A counter will determine when the registration will be shown again)
		     
		     */
			thumbr.setAction(action);
			
			//(OPTIONAL) AUTOMATICALLY START THE THUMBR SDK WHEN OPENING THE APPLICATION. 
			ONLY THE FIRST TIME, IN THIS EXAMPLE
			if(thumbr.getCount() < 2){
				thumbr.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale=" 
				+locale +"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);
				
			thumbr.adInit();//initialize Ads if you are showing Thumbr Ads

		//DISABLE FOLLOWING LINE NOT TO OPEN THE WINDOW ON APP STARTUP
				thumbr.buttonREGISTER();
			}

		}

**Load the animated Thumbr T-button and attach it to 'bt_re'**

		//LOAD AND ANIMATE THE THUMBR LOGO :: RESIZE IT TO A PERCENTAGE OF THE SCREEN WIDTH
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
		//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT
		ImageButton thumbrLogo = (ImageButton) findViewById(com.gkxim.tqhung.thumbr.R.id.bt_re);
		
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) (size.x * buttonWidth);
		if(width > 120){width = 120;}
		LayoutParams params =new LinearLayout.LayoutParams(width,width);		
		thumbrLogo.setLayoutParams(params);

		thumbrLogo.setBackgroundResource(com.gkxim.tqhung.thumbr.R.drawable.anim_thumbr_logo);
		AnimationDrawable thumbrLogoAnimation = (AnimationDrawable) thumbrLogo.getBackground();
		thumbrLogoAnimation.start();	
	}

**Catch the button clicks - make sure to use your local paths**	

		//CATCH BUTTON CLICKS
		@Override
		public void onClick(View v) {	
			if(v.getId()==com.yourcompany.yourapp.R.id.bt_re){
			//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT!!
				thumbr.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale=" 
				+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);			
				thumbr.buttonREGISTER();
			}	
			if(v.getId()==com.yourcompany.yourapp.R.id.bt_switch){
			//CHANGE THIS PATH TO THE SWITCH BUTTON IN YOUR LAYOUT!!
			
			thumbr.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//OPTIONALLY CHANGE THE THUMBR SDK ORIENTATION WHEN USER SWITCHES (WAR2GLORY NEEDS THIS SETTING!!!)
			
			thumbr.setEnableButtonClose(true);
			//OPTIONALLY SHOW THE CLOSE BUTTON :: SWITCH VIEW DOES NOT HAVE "PLAY-NOW" BUTTON"
				
			
			thumbr.setLinkSwitch(registerUrl+"response_type=token&step=switchaccount&country="+country+"&locale="
			+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);			
				thumbr.buttonSWITCH();
			}			
		}	
		
**Add onDismiss function to handle the Thumbr SDK window close**
	    
		/*
		 * THIS FUNCTION IS CALLED WHEN THE THUMBR SDK WINDOW CLOSES.
		 */
		@Override
		public void onDismiss(DialogInterface dialog) {
			Log.i("ThumbrSDK","dismiss listener called this function");
			//SET THE ORIENTATION BACK TO GAME PREFERENCE
			setRequestedOrientation(gameOrientation);
			//CALL THE EXAMPLE USER DATA FUNCTION
			getUserData();

			//AFTER FIRST OPEN, LET THE SDK KNOW REGISTRATION IS OPTIONAL FROM NOW ON. 
			//REGISTRATION FLOW WILL STILL BE SHOWN EVERY #N TIMES (#N = DETERMINED SERVER SIDE)
			thumbr.setAction("optional_registration");
		}	

**This is an example function that shows which data is sent back in the user object**

		//EXAMPLE FUNCTION OF RETURN VALUES
		public void getUserData(){
			ProfileObject ojb=thumbr.didLoginUser();			
			if(ojb!=null){
				
				if(ojb.getmEmail() != "null"){
					//OPTIONALLY CHANGE THE THUMBR SDK ORIENTATION AFTER USER HAS SUBSCRIBED 
					//(WAR2GLORY NEEDS THIS SETTING!!!)
					thumbr.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
				
				//DO SOMETHING WITH THE USER OBJECT
				if(debug == true){Toast.makeText(this,"User ID: "+ojb.getmID(), 4000).show();}
				Log.i("ID",ojb.getmID());
				Log.i("UserName",ojb.getmUserName());
				Log.i("Status",ojb.getmStatus());
				Log.i("Email",ojb.getmEmail());
				Log.i("Surname",ojb.getmSurname());
				Log.i("Gender",ojb.getmGender());
				Log.i("DOB",ojb.getmDOB());
				Log.i("Locale",ojb.getmLocale());
				Log.i("City",ojb.getmCity());
				Log.i("Address",ojb.getmAddress());
				Log.i("ZipCode",ojb.getmZipCode());
				Log.i("NewsLetter",ojb.getmNewsLetter());
				Log.i("FirstName",ojb.getmFirstName());
				Log.i("Sisdn",ojb.getmSisdn());
				Log.i("Housenr",ojb.getmHousenr());
				Log.i("Message",ojb.getmMessage());
				Log.i("Code",ojb.getmCode());
				Log.i("Description",ojb.getmDesscription());
			}else{
				if(debug == true){Toast.makeText(this,"Not logged in yet...", 4000).show();}				
			}
		}	

**To show Advertisements use these functions at appropirate points in your application**

	//INLINE ADVERTISEMENT:
		final RelativeLayout ad_view = (RelativeLayout) findViewById(com.yourcompany.yourapp.R.id.ad_view);
		thumbr.adInline(ad_view);

	//OVERLAY ADVERTISEMENT:	
		final RelativeLayout ad_view = (RelativeLayout) findViewById(com.yourcompany.yourapp.R.id.ad_view);
		thumbr.adOverlay(ad_view);

	//INTERSTITIAL ADVERTISEMENT:
		final RelativeLayout ad_view = (RelativeLayout) findViewById(com.yourcompany.yourapp.R.id.ad_view_interstitial);
		thumbr.adInterstitial(ad_view);
					
**Closing the class**	
		
	}
		
