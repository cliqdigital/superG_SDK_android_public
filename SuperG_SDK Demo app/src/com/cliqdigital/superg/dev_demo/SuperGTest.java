package com.cliqdigital.superg.dev_demo;

import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.cliqdigital.supergsdk.SuperG;
import com.cliqdigital.supergsdk.SuperG.OnInterstitialCloseListener;
import com.cliqdigital.supergsdk.SuperG.OnPushMessageListener;
import com.cliqdigital.supergsdk.components.AppsFlyerHelper;
import com.cliqdigital.supergsdk.utils.EVA;
import com.cliqdigital.supergsdk.utils.ProfileObject;


@SuppressLint("ShowToast")
@SuppressWarnings("unused")
public class SuperGTest extends Activity implements OnClickListener,OnDismissListener{

	private static final String TAG = "TESTAPP"; 

	/*
	 * SETTINGS
	 */

	//APP SPECIFIC SETTINGS
	private String sid = "*";
	private String client_id = "84758475-476574";

	//DEFAULT GAME ORIENTATION (USED IN onDismiss() BELOW, TO SWITCH BACK AFTER SDK CLOSE)
	private int gameOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//TELL THE SUPERG SDK IN WHICH ORIENTATION TO OPEN
	private int superGOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
	private boolean showButtonClose = true;
	//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
	private double buttonWidth = 0.15;


	//AD SERVING SETTINGS
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
	private int updateTimeInterval = 0;//number of seconds before Ad refresh
	private int showCloseButtonTime = 6;//Number of seconds before the Ad close button appears
	
	/*
	 * OTHER, MORE GENERIC SETTINGS
	 */
	private String action = 		"registration";
	private String appsFlyerKey = 	"9ngR4oQcH5qz7qxcFb7ftd";	
	private Boolean debug = 		true; //OPTIONALLY SWITCH TO 'true' DURING DEVELOPMENT		
	private String registerUrl = 	"http://gasp.superg.mobi/auth/authorize?";

	private int hideThumbrCloseButton = 0;//hide the close button from the Thumbr Window? 1 or 0
	private String SDKLayout = 		"thumbr";


	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	private String country = 		"";//eg: DE or NL
	private String locale = 		"";//eg: nl_NL or de_DE
	private String appsFlyerId =	"";
	SuperG superG;
	private View bt;

	@Override
	protected void onPause() {
		superG.pause(this);
		g.stop();
		super.onPause();
	}	
	@Override
	protected void onResume(){
		superG.resume();
		g.start();
		super.onResume();		
	}


	private Glass g;
	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//		StrictMode.setThreadPolicy(policy);


		Locale l = Locale.getDefault();
		if(country == ""){country = l.getCountry();}
		if(locale == ""){locale = l.getLanguage()+"_"+country;}

		// IMPORT APPSFLYER
		AppsFlyerHelper appsflyerhelper = new AppsFlyerHelper();
		appsflyerhelper.sendTracking(this,appsFlyerKey);
		appsFlyerId = appsflyerhelper.getAppsflyerId(this);

		SharedPreferences settings = this.getSharedPreferences(
				"SuperGSettings", Context.MODE_PRIVATE);
		settings.edit().putInt("updateTimeInterval", updateTimeInterval).commit();
		settings.edit().putInt("showCloseButtonTime", showCloseButtonTime).commit();
		settings.edit().putInt("hideThumbrCloseButton", hideThumbrCloseButton).commit();
		settings.edit().putString("tablet_Inline_zoneid", tablet_Inline_zoneid).commit();
		settings.edit().putString("tablet_Inline_secret", tablet_Inline_secret).commit();
		settings.edit().putString("tablet_Overlay_zoneid", tablet_Overlay_zoneid).commit();
		settings.edit().putString("tablet_Overlay_secret", tablet_Overlay_secret).commit();
		settings.edit().putString("tablet_Interstitial_zoneid",tablet_Interstitial_zoneid).commit();
		settings.edit().putString("tablet_Interstitial_secret",tablet_Interstitial_secret).commit();
		settings.edit().putString("phone_Inline_zoneid", phone_Inline_zoneid).commit();
		settings.edit().putString("phone_Inline_secret", phone_Inline_secret).commit();
		settings.edit().putString("phone_Overlay_zoneid", phone_Overlay_zoneid).commit();
		settings.edit().putString("phone_Overlay_secret", phone_Overlay_secret).commit();
		settings.edit().putString("phone_Interstitial_zoneid",phone_Interstitial_zoneid).commit();
		settings.edit().putString("phone_Interstitial_secret",phone_Interstitial_secret).commit();
		settings.edit().putString("sid", sid).commit();
		settings.edit().putString("client_id", client_id).commit();


		// SET VIEW
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);

		// CREATE THUMBR BUTTON LISTENER
		//CREATE BUTTON LISTENERS
		try{
			bt = (Button) findViewById(R.id.bt_re);
			//Normal Button
		}
		catch (ClassCastException cce){
			bt = (ImageButton) findViewById(R.id.bt_re);
			//Animated Thumbr Logo
		}
		bt.setOnClickListener(this);

		//CREATE AD BUTTON LISTENERS (FOR DEMO ONLY)
		Button bt_inline=(Button) findViewById(R.id.inline);
		bt_inline.setOnClickListener(this);
		Button bt_overlay=(Button) findViewById(R.id.overlay);
		bt_overlay.setOnClickListener(this);
		Button bt_interstitial=(Button) findViewById(R.id.interstitial);

		bt_interstitial.setOnClickListener(this);		

		Button bt_achievement=(Button) findViewById(R.id.achievement);
		bt_achievement.setOnClickListener(this);
		Button bt_click=(Button) findViewById(R.id.click);
		bt_click.setOnClickListener(this);
		Button bt_purchase=(Button) findViewById(R.id.purchase);
		bt_purchase.setOnClickListener(this);		
		Button bt_start_level=(Button) findViewById(R.id.start_level);
		bt_start_level.setOnClickListener(this);
		Button bt_finish_level=(Button) findViewById(R.id.finish_level);
		bt_finish_level.setOnClickListener(this);
		Button bt_up_sell=(Button) findViewById(R.id.up_sell);
		bt_up_sell.setOnClickListener(this);			 	


		//CALL THE SUPERG LIBRARY
		superG=new SuperG(this, 2);
		superG.setEnableButtonClose(showButtonClose);
		superG.setToastDebug(debug);
		superG.setLayout(SDKLayout);
		superG.setOrientation(superGOrientation);
		superG.setAction(action);
		superG.setLinkRegister(registerUrl+"response_type=token&country="+country+"&locale="+locale+"&sid="+sid+"&client_id="+client_id+"&handset_id="+appsFlyerId);			
		superG.adInit();//initialize Ads if you are showing Ads

		FrameLayout fl;
		Drawable my_background;
		double Scale = 1.1;

		if( SuperG.isTabletDevice(this) == true) {
			//this is a tablet
			fl = (FrameLayout) findViewById(R.id.glass);
			my_background = (Drawable) getResources().getDrawable(R.drawable.galaxy_land);
			Scale = 1.5;

			gameOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			superGOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}else{
			//this is not a tablet
			fl = (FrameLayout) findViewById(R.id.glass);
			my_background = (Drawable) getResources().getDrawable(R.drawable.galaxy);
			gameOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			superGOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		g = new Glass(this,  fl, my_background);
		g.scale(Scale);
		g.start();

		//DISABLE FOLLOWING LINE NOT TO OPEN THE WINDOW ON APP STARTUP
		//superG.buttonREGISTER();


		/* Interstitial advertisement close listener
		   If you use the superG.adInterstitial(ad_view) method, you can use this listener to know when it closes. 
		 */

		superG.setInterstitialCloseListener(new OnInterstitialCloseListener(){
			public void onEvent(){
				//Log.i(TAG,"The interstitial advertisement was closed. You can resume your game.");
				Toast.makeText(getApplicationContext(), "The game has been notified about closing the interstitial", 6000).show();
				//resumeYourGame();
			}
		});	

		//THIS IS A LISTENER FOR PUSH MESSAGES. YOU CAN SET CERTAIN TASKS BASED ON THE 'action' PARAMETER 
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

	// LOAD AND ANIMATE THE THUMBR LOGO :: RESIZE IT TO A PERCENTAGE OF THE
	// SCREEN WIDTH
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		// THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
		double buttonWidth = 0.2;

		try{
			if(bt.getTag().equals("ThumbrLogo")){
				logoanimation.animation anim = new logoanimation.animation();
				anim.animateLogo((ImageButton)bt,this,buttonWidth);
			}
		}
		catch (ClassCastException cce){
			Log.i("ThumbrLogo","Animation not used");
		}
	}

	//CATCH BUTTON CLICKS
	@Override
	public void onClick(View v) {   
		//OPEN SUPERG SDK

		if(v.getId()==R.id.bt_re){//CHANGE THIS PATH TO THE THUMBR BUTTON IN YOUR LAYOUT!!
			superG.buttonREGISTER();
		}
		//END OPEN SUPERG SDK



		//START ADVERTISEMENT ACTIONS

		if(v.getId()==R.id.inline){

			final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view);
			superG.adInline(ad_view);

			//ad_view.setMinimumWidth(minWidth);
		}
		if(v.getId()==R.id.overlay){
			final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view);	
			superG.adOverlay(ad_view);
		}
		if(v.getId()==R.id.interstitial){
			final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view_interstitial);

			superG.adInterstitial(ad_view);
		}
		//END ADVERTISEMENT ACTIONS


		//START EVENT LOGGING ACTIONS

		if(v.getId()==R.id.achievement){
			EVA eva = new EVA();
			//usage: eva.achievementEarned(context, achievement_name)
			eva.achievementEarned(this, "FoundGold");
		}

		if(v.getId()==R.id.click){
			EVA eva = new EVA();
			//usage: eva.click(context, clicked_item)
			eva.click(this, "SomeButton");
		}

		if(v.getId()==R.id.purchase){
			EVA eva = new EVA();
			//usage: eva.purchase(context, currency, payment_method, price, purchased_item)
			eva.purchase(this, "EUR", "in-app-purchase", "0.99", "ProStatus");
		}

		if(v.getId()==R.id.start_level){
			EVA eva = new EVA();
			//usage: eva.startLevel(context, app_mode, level, score_type, score_value)
			eva.startLevel(this, "basic", "1", "points", "0");
		}
		if(v.getId()==R.id.finish_level){
			EVA eva = new EVA();
			//usage: eva.finishLevel(context, app_mode, level, score_type, score_value)
			eva.finishLevel(this, "basic", "1", "points", "120");
		}
		if(v.getId()==R.id.up_sell){
			EVA eva = new EVA();
			//usage: eva.upSell(context, currency, payment_method)
			eva.upSell(this, "EUR", "in-app-purchase");
		}		
		//END EVENT LOGGING ACTIONS

	} 
	//END CATCH BUTTON CLICKS




	/*
	 * THIS FUNCTION IS CALLED WHEN THE SUPERG SDK WINDOW CLOSES.
	 */
	@Override
	public void onDismiss(DialogInterface dialog) {
		Log.i(TAG,"dismiss listener called this function");
		//SET THE ORIENTATION BACK TO GAME PREFERENCE
		setRequestedOrientation(gameOrientation);
		//CALL THE EXAMPLE USER DATA FUNCTION
		getUserData();

		//AFTER FIRST OPEN, LET THE SDK KNOW REGISTRATION IS OPTIONAL FROM NOW ON. REGISTRATION FLOW WILL STILL BE SHOWN EVERY #N TIMES (#N = DETERMINED SERVER SIDE)
		//superG.setAction("optional_registration");
	} 



	//EXAMPLE FUNCTION OF RETURN VALUES
	@SuppressLint("ShowToast")
	public void getUserData(){
		Thread thread = new Thread()
		{
			@Override
			public void run() {		
				ProfileObject ojb=superG.didLoginUser();            
				if(ojb!=null){
					//DO SOMETHING WITH THE USER OBJECT

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
				}
			}
		};

		thread.start();		
	}


}