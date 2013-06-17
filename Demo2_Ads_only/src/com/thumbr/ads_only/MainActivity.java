package com.thumbr.ads_only;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gkxim.android.thumbsdk.FunctionThumbrSDK;
import com.gkxim.android.thumbsdk.FunctionThumbrSDK.OnInterstitialCloseListener;

@SuppressLint("ShowToast")
public class MainActivity extends Activity{

	//AD SERVING SETTINGS
	private int updateTimeInterval = 0;//Initial number of seconds before Ad refresh (setting will be updated from server)
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
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences settings = this.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		settings.edit().putInt("updateTimeInterval", updateTimeInterval).commit();		
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


		// SET VIEW
		super.onCreate(savedInstanceState);

		//CALL THE THUMBR LIBRARY
		thumbr = new FunctionThumbrSDK(this, 2);
		thumbr.adInit();//initialize Ads

		/* Interstitial advertisement close listener
		   If you use the thumbr.adInterstitial(ad_view) method, you can use this listener to know when it closes. 
		 */
		thumbr.setInterstitialCloseListener(new OnInterstitialCloseListener(){
			public void onEvent(){
				Log.i("ThumbrSDK","The interstitial advertisement was closed. You can resume your application.");
				Toast.makeText(getApplicationContext(), "The application has been notified about closing the interstitial", 6000).show();
				//resumeYourApp();
			}
		});		


		final RelativeLayout ad_view = (RelativeLayout) findViewById(R.id.ad_view);
		
		/*Load an inline advertisement*/
		thumbr.adInline(ad_view);
		
		/*Load an overlay advertisement*/
		//thumbr.adOverlay(ad_view);
		
		/*Load an interstitial advertisement*/
		//thumbr.adInterstitial(ad_view);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



}
