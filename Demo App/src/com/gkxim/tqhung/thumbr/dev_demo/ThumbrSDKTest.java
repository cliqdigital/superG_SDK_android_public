package com.gkxim.tqhung.thumbr.dev_demo;

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
import com.gkxim.android.thumbsdk.FunctionThumbrSDK.OnInterstitialCloseListener;
import com.gkxim.android.thumbsdk.utils.ProfileObject;
import com.gkxim.android.thumbsdk.utils.EVA;

@SuppressLint("ShowToast")
public class ThumbrSDKTest extends Activity implements OnClickListener,OnDismissListener{

	/*
	 * SETTINGS
	 */

	//APP SPECIFIC SETTINGS
	private String sid = "com.thumbr.dragonsvsunicorns";
	private String client_id = "84758475-476574";
	

	//DEFAULT GAME ORIENTATION (USED IN onDismiss() BELOW, TO SWITCH BACK AFTER SDK CLOSE)
	private int gameOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//TELL THE THUMBR SDK IN WHICH ORIENTATION TO OPEN
	private int thumbrSDKOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
	private boolean showButtonClose = true;
	//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
	private double buttonWidth = 0.15;

	
	//AD SERVING SETTINGS
	private int updateTimeInterval = 0;//number of seconds before Ad refresh
	private int showCloseButtonTime = 6;//Number of seconds before the Ad close button appears

	private String tablet_Inline_zoneid = 		"1356888057";
	private String tablet_Inline_secret = 		"20E1A8C6655F7D3E";
	private String tablet_Overlay_zoneid = 		"3356907052";
	private String tablet_Overlay_secret = 		"ADAA22CB6D2AFDD3";
	private String tablet_Interstitial_zoneid = "7356917050";
	private String tablet_Interstitial_secret = "CB45B76FE96C8896";
	private String phone_Inline_zoneid = 		"0345893057";
	private String phone_Inline_secret = 		"04F006733229C984";
	private String phone_Overlay_zoneid = 		"7345907052";
	private String phone_Overlay_secret = 		"AEAAA69F395BA8FA";
	private String phone_Interstitial_zoneid = 	"9345913059";
	private String phone_Interstitial_secret = 	"04B882960D362099";

	/*
	 * OTHER, MORE GENERIC SETTINGS
	 */
	private String action = 		"registration";
	private String appsFlyerKey = 	"9ngR4oQcH5qz7qxcFb7ftd";	
	private Boolean debug = 		false; //OPTIONALLY SWITCH TO 'true' DURING IMPLEMENTATION		
	private String registerUrl = 	"http://gasp.thumbr.com/auth/authorize?";
	private String score_game_id = "";
	private int hideThumbrCloseButton = 0;//hide the close button from the Thumbr Window? 1 or 0
	private String SDKLayout = 		"thumbr";

	
	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	private String country = 		"";//eg: DE or NL
	private String locale = 		"";//eg: nl_NL or de_DE
	private String appsFlyerId =	"";
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





	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Locale l = Locale.getDefault();
		if(country == ""){country = l.getCountry();}
		if(locale == ""){locale = l.getLanguage()+"_"+country;}

		//IMPORT APPSFLYER
		AppsFlyerLib.sendTracking(this,appsFlyerKey);
		appsFlyerId = AppsFlyerLib.getAppsFlyerUID(this);

		SharedPreferences settings = this.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
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

		// SET VIEW
		super.onCreate(savedInstanceState);
		setContentView(com.gkxim.tqhung.thumbr.demo.R.layout.home);

		//CREATE THUMBR BUTTON LISTENER
		ImageButton bt=(ImageButton) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.bt_re);
		bt.setOnClickListener(this);

		//CREATE AD BUTTON LISTENERS (FOR DEMO ONLY)
		Button bt_inline=(Button) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.inline);
		bt_inline.setOnClickListener(this);
		Button bt_overlay=(Button) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.overlay);
		bt_overlay.setOnClickListener(this);
		Button bt_interstitial=(Button) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.interstitial);
		bt_interstitial.setOnClickListener(this);		

		//CALL THE THUMBR LIBRARY
		thumbr=new FunctionThumbrSDK(this, 2);
		thumbr.setEnableButtonClose(showButtonClose);
		thumbr.setToastDebug(debug);
		thumbr.setLayout(SDKLayout);
		thumbr.setOrientation(thumbrSDKOrientation);
		thumbr.setAction(action);
		thumbr.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);			
		thumbr.adInit();//initialize Ads if you are showing Thumbr Ads


		
		if( FunctionThumbrSDK.isTabletDevice(this) == true) {
			//this is a tablet
			gameOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			thumbrSDKOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}else{
			//this is not a tablet
			gameOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			thumbrSDKOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		
		//DISABLE FOLLOWING LINE NOT TO OPEN THE WINDOW ON APP STARTUP
		//thumbr.buttonREGISTER();
		

		/* Interstitial advertisement close listener
		   If you use the thumbr.adInterstitial(ad_view) method, you can use this listener to know when it closes. 
		*/
		
		thumbr.setInterstitialCloseListener(new OnInterstitialCloseListener(){
			public void onEvent(){
				Log.i("ThumbrSDK","The interstitial advertisement was closed. You can resume your game.");
				Toast.makeText(getApplicationContext(), "The game has been notified about closing the interstitial", 6000).show();
				//resumeYourGame();
			}
		});		
		

		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		//LOAD AND ANIMATE THE THUMBR LOGO :: RESIZE IT TO A PERCENTAGE OF THE SCREEN WIDTH
		ImageButton thumbrLogo = (ImageButton) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.bt_re);//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT

		Display display = getWindowManager().getDefaultDisplay();

		int width = (int) (display.getWidth() * buttonWidth);
		if(width > 150){width = 150;}//optional

		LayoutParams params =new LinearLayout.LayoutParams(width,width);        
		thumbrLogo.setLayoutParams(params);

		thumbrLogo.setBackgroundResource(com.gkxim.tqhung.thumbr.demo.R.drawable.anim_thumbr_logo);
		AnimationDrawable thumbrLogoAnimation = (AnimationDrawable) thumbrLogo.getBackground();
		thumbrLogoAnimation.start();   

	}

	//CATCH BUTTON CLICKS
	@Override
	public void onClick(View v) {   
		//OPEN THUMBR SDK
		if(v.getId()==com.gkxim.tqhung.thumbr.demo.R.id.bt_re){//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT!!
			thumbr.buttonREGISTER();
		}

		//START ADVERTISEMENT ACTIONS
		if(v.getId()==com.gkxim.tqhung.thumbr.demo.R.id.inline){
			final RelativeLayout ad_view = (RelativeLayout) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.ad_view);
			thumbr.adInline(ad_view);	
		}
		if(v.getId()==com.gkxim.tqhung.thumbr.demo.R.id.overlay){
			final RelativeLayout ad_view = (RelativeLayout) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.ad_view);	
			thumbr.adOverlay(ad_view);
		}
		if(v.getId()==com.gkxim.tqhung.thumbr.demo.R.id.interstitial){
			final RelativeLayout ad_view = (RelativeLayout) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.ad_view_interstitial);
			thumbr.adInterstitial(ad_view);
		}
		//END ADVERTISEMENT ACTIONS

	} 


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

		//AFTER FIRST OPEN, LET THE SDK KNOW REGISTRATION IS OPTIONAL FROM NOW ON. REGISTRATION FLOW WILL STILL BE SHOWN EVERY #N TIMES (#N = DETERMINED SERVER SIDE)
		//thumbr.setAction("optional_registration");
	} 

	//EXAMPLE FUNCTION OF RETURN VALUES
	@SuppressLint("ShowToast")
	public void getUserData(){
		ProfileObject ojb=thumbr.didLoginUser();            
		if(ojb!=null){
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
			Log.i("Country",ojb.getmCountry());			
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



}