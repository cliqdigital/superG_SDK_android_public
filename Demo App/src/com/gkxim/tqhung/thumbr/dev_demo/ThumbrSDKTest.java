package com.gkxim.tqhung.thumbr.dev_demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
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
import android.widget.Toast;
import com.appsflyer.AppsFlyerLib;
import com.gkxim.android.thumbsdk.FunctionThumbrSDK;
import com.gkxim.android.thumbsdk.utils.ProfileObject;

public class ThumbrSDKTest extends Activity implements OnClickListener,OnDismissListener{

	/*
	 * SETTINGS
	 */

	//APP SPECIFIC SETTINGS
	private String sid = "";
	private String client_id = "";
	private String score_game_id = "";

	//DEFAULT GAME ORIENTATION (USED IN onDismiss() BELOW, TO SWITCH BACK AFTER SDK CLOSE)
	private int gameOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//TELL THE THUMBR SDK IN WHICH ORIENTATION TO OPEN
	private int thumbrSDKOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
	private boolean showButtonClose = true;
	//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
	private double buttonWidth = 0.25;

	/*
	 * OTHER, MORE GENERIC SETTINGS (LEAVE AS IS)
	 */
	private String action = "registration";
	private String appsFlyerKey = "9ngR4oQcH5qz7qxcFb7ftd";	
	private Boolean debug = false; //OPTIONALLY SWITCH TO 'true' DURING IMPLEMENTATION		
	private String registerUrl = "http://gasp.thumbr.com/auth/authorize?";
	private String switchUrl = "http://gasp.thumbr.com/auth/authorize?";
	private String portalUrl = "http://m.thumbr.com?";

	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	private String SDKLayout = "thumbr";
	private String country = "";//eg: DE or NL
	private String locale = "";//eg: nl_NL or de_DE
	private String appsFlyerId="";
	private int requestedOrientation;		
	/*
	 * END SETTINGS
	 */

	FunctionThumbrSDK thumbr;
	private String layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		//IMPORT APPSFLYER
		AppsFlyerLib.sendTracking(this,appsFlyerKey);
		appsFlyerId = AppsFlyerLib.getAppsFlyerUID(this);

		this.getSharedPreferences("ThumbrSettings", MODE_PRIVATE).edit().putString("score_game_id",score_game_id).commit();
		this.layout="appsilike";
		// SET VIEW
		super.onCreate(savedInstanceState);
		setContentView(com.gkxim.tqhung.thumbr.demo.R.layout.home);

		//CREATE BUTTON LISTENERS
		ImageButton bt=(ImageButton) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.bt_re);
		bt.setOnClickListener(this);   

		//CALL THE THUMBR LIBRARY
		thumbr=new FunctionThumbrSDK(this, 2);
		thumbr.setEnableButtonClose(showButtonClose);
		thumbr.setToastDebug(debug);
		thumbr.setLayout(SDKLayout);
		thumbr.setOrientation(thumbrSDKOrientation);
		thumbr.setAction(action);
		thumbr.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);			

		//DISABLE FOLLOWING LINE NOT TO OPEN THE WINDOW ON APP STARTUP
		//thumbr.buttonREGISTER();
	}



	//LOAD AND ANIMATE THE THUMBR LOGO :: RESIZE IT TO A PERCENTAGE OF THE SCREEN WIDTH
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageButton thumbrLogo = (ImageButton) findViewById(com.gkxim.tqhung.thumbr.demo.R.id.bt_re);//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) (size.x * buttonWidth);
		//if(width > 120){width = 120;}
		LayoutParams params =new LinearLayout.LayoutParams(width,width);        
		thumbrLogo.setLayoutParams(params);

		thumbrLogo.setBackgroundResource(com.gkxim.tqhung.thumbr.demo.R.drawable.anim_thumbr_logo);
		AnimationDrawable thumbrLogoAnimation = (AnimationDrawable) thumbrLogo.getBackground();
		thumbrLogoAnimation.start();    
	}

	//CATCH BUTTON CLICKS
	@Override
	public void onClick(View v) {   
		if(v.getId()==com.gkxim.tqhung.thumbr.demo.R.id.bt_re){//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT!!
			thumbr.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);            
			thumbr.buttonREGISTER();
		}             
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
		thumbr.setAction("optional_registration");
	} 

	//EXAMPLE FUNCTION OF RETURN VALUES
	public void getUserData(){
		ProfileObject ojb=thumbr.didLoginUser();            
		if(ojb!=null){

			if(ojb.getmEmail() != "null"){
				//OPTIONALLY CHANGE THE THUMBR SDK ORIENTATION AFTER USER HAS SUBSCRIBED (WAR2GLORY NEEDS THIS SETTING!!!)
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


}
