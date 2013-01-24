package com.gkxim.android.thumbsdk;

import org.xmlpull.v1.XmlPullParser;
import com.gkxim.android.thumbsdk.components.ThumbrWebViewDialog;
import com.gkxim.android.thumbsdk.utils.APIServer;
import com.gkxim.android.thumbsdk.utils.ProfileObject;
import com.gkxim.android.thumbsdk.utils.TBrLog;
import com.gkxim.android.thumbsdk.utils.WSLoginListener;
import com.gkxim.android.thumbsdk.utils.WSRegisterListener;
import com.gkxim.android.thumbsdk.utils.WSStateCode;
import com.gkxim.android.thumbsdk.utils.WSSwitchListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.CookieManager;


public class FunctionThumbrSDK {

	private Context mContext;
	private WSLoginListener loginListener=null;	
	private WSRegisterListener registerListener=null;
	private WSSwitchListener switchListener=null;
	private ThumbrWebViewDialog mDialog;
	public static final String VALID_LOGINED = "Valid";
	public static final String LOGINED = "Logined";
	public static final String ACCESSTOKEN = "";
	public int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
	private boolean isRequestedOrientation = false;
	public static final int Density_TV = 213;
	public static final int Density_XHIGH = 320;
	public static final int SIZE_XLARGE = 4;
	private int sid = 0;
	private int oldOrientation;
	private String mThumbrId, mGameId;
	private boolean isShowbutonClose = true;
	private boolean isConfigchange_orientation = false;
	
	public static final int MY_ORIENTATION = 0x0080;
	private static final int MODE_WORLD_READABLE = 1;

	private String linkRegister = "";
	private String linkSwitch = "";
	private String linkPortal = "";

	
	/**
	 * 
	 * @param context
	 * @param sid
	 *            :identifies the game and is mandatory
	 */
	public FunctionThumbrSDK(Context context, int sid) {
		mContext = context;
		linkRegister = APIServer.getURLRegister(sid, (Activity) context);	
		linkSwitch = APIServer.getURLSwitchAccount(sid, (Activity) context);
		linkPortal = APIServer.getURLPrtal();
		this.sid = sid;
		boolean table = isTabletDevice(context);
		check_importConfichange();
		mThumbrId = TBrLog.createThumbrID((Activity) mContext);
		mGameId = TBrLog.createGameID((Activity) mContext);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(new NetworkStateReceiver(), intentFilter);
	}

	public void setEnableButtonClose(boolean flag) {
		isShowbutonClose = flag;
	}
	
	public void setAction(String action){
		if(action !="" && action!="registration" && action!="optional_registration"){
			action = "default";
			}
	       SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", ((Activity)mContext).MODE_PRIVATE);
	       settings.edit().putString("action", action).commit();
	}
	public String getAction(){
           SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", ((Activity)mContext).MODE_PRIVATE);
	       String action = settings.getString("action", "");
			if(action !="" && action!="registration" && action!="optional_registration"){
				action = "default";
				}	       
	       return action;
	}
	public int getCount(){
	       SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", ((Activity)mContext).MODE_PRIVATE);
	       int count = settings.getInt("count", 0);
	       count = count + 1;
	       settings.edit().putInt("count", count).commit();		
	       return count;	
	}
	
	public void buttonREGISTER() {
		Log.i("ThumbrSDK","Register button clicked");
		if (!isLogined()) {
			
			
			if(registerListener!=null)
			{				
				registerListener.callback(WSStateCode.FAILED,null, "Connection failed");
				Log.i("ThumbrSDK","Existing login failed, going into registration mode");
			}
			ShowDialog(linkRegister);
		} else {
			if(registerListener!=null)
			{							
				registerListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,""), "LoginSuccess");
				Log.i("ThumbrSDK","Logged in successful with accestoken" + FunctionThumbrSDK.ACCESSTOKEN);
			}
			buttonPORTAL();
		}
	}

	public void buttonSWITCH() {
		ThumbrWebViewDialog.accToken="";
		if (!isLogined()) {
			if(switchListener!=null)
			{				
				switchListener.callback(WSStateCode.FAILED,null, "Connection failed");
			}
		}else{
			if(switchListener!=null)
			{				
				switchListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,""), "LoginSuccess");
			}			
		}
		mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit().putBoolean(FunctionThumbrSDK.LOGINED,false).commit();
		mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).edit().putString(ACCESSTOKEN, null ).commit();
		Log.i("ThumbrSDK","linkswitch url: "+linkSwitch);
		ShowDialog(linkSwitch);
	}

	public void buttonCOOKIES() {
		ThumbrClearCookies();		
		Log.i("ThumbrSDK","Clear cookie button clicked");
	}	
	
	
	private void ThumbrClearCookies() {

		mDialog = new ThumbrWebViewDialog(mContext, isShowbutonClose);
		
		CookieManager cookieManager = CookieManager.getInstance();
	    cookieManager.removeAllCookie();	
	    mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit()
		.putBoolean(FunctionThumbrSDK.LOGINED,false).commit();
		mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).edit()
		.putString(FunctionThumbrSDK.ACCESSTOKEN,null).commit();	    
		mDialog.dismiss();

		TBrLog.lt(mContext, 3, "Cookies cleared & logged out");
		
	}		
	
	public void buttonPORTAL() {
		TBrLog.lt(mContext, 3, "ButtonPORTAL :: ShowDialog :: "+linkPortal+getAccessToken());
		ShowDialog(linkPortal+getAccessToken());
		
	}

	public void buttonExit() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	private void ShowDialog(String link) {
		
		//attach action to link
		link = link + "&action=" + this.getAction() + "&count=" + getCount();
		
		mDialog = new ThumbrWebViewDialog(mContext, isShowbutonClose);
		mDialog.setOnDismissListener((OnDismissListener) mContext);
		mDialog.setURL(link);// mContext.getResources().getString(R.string.Loginlink));
		mDialog.show();

		//SET THE REQUESTED ORIENTATION
		((Activity) mContext).setRequestedOrientation(requestedOrientation);
		
	}

	
	
	private void check_importConfichange() {
		try {
			int index = 1;
			int lastindex = 0;
			Activity act = (Activity) mContext;
			String str = act.getPackageName();
			String NameClassActivity = act.getComponentName().getClassName();
			NameClassActivity = NameClassActivity.replace(str, "");

			AssetManager am = mContext.createPackageContext(
					mContext.getPackageName(), 0).getAssets();
			XmlResourceParser xml = am
					.openXmlResourceParser("AndroidManifest.xml");
			int eventType = xml.getEventType();
			xmlloop: while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (!xml.getName().matches("activity")) {
						;
					} else {
						String name = "";
						attrloop: for (int j = 0; j < xml.getAttributeCount(); j++) {
							if (xml.getAttributeName(j).matches("name")) {
								name = xml.getAttributeValue(j);
								index = name.indexOf(NameClassActivity);
								lastindex = name.lastIndexOf(NameClassActivity);
							}

							if (xml.getAttributeName(j)
									.matches("configChanges")) {
								if (index == lastindex && index > 0) {
									int hex = xml.getAttributeIntValue(j, 0);
									if ((hex & MY_ORIENTATION) == MY_ORIENTATION) {
										isConfigchange_orientation = true;
										break attrloop;
									}
								}
							}
						}
					}
					break;
				}
				if (isConfigchange_orientation)
					break xmlloop;
				eventType = xml.nextToken();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class NetworkStateReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "Network connectivity change");
			if (intent.getExtras() != null) {
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					TBrLog.l(TBrLog.TMB_LOGTYPE_INFO,
							"Network " + ni.getTypeName() + " connected");
				}
			}
			if (intent.getExtras().getBoolean(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
				TBrLog.l(TBrLog.TMB_LOGTYPE_INFO,
						"There's no network connectivity");
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.showNotNetwork();
				}
			}
		}
	}

	public String getGameId() {
		return mGameId;
	}

	public String getThumbrId() {
		return mThumbrId;
	}

	public void setLinkRegister(String linkRegister) {
		this.linkRegister = linkRegister;
	}

	public void setLinkSwitch(String linkSwitch) {
		this.linkSwitch = linkSwitch;
	}

	public void setLinkPortal(String linkPortal) {
		this.linkPortal = linkPortal;
	}

	public String getLinkPortal() {
		return linkPortal+getAccessToken();
	}

	public String getLinkRegister() {
		return linkRegister;
	}

	public String getLinkSwitch() {
		return linkSwitch;
	}

	/**
	 * 
	 * @param b
	 */
	public void setToastDebug(boolean b) {
		TBrLog.bToastDebug = false;
	}

	/**
	 * 
	 * @param requestedOrientation
	 */
	public void setOrientation(int requestedOrientation) {
		this.requestedOrientation = requestedOrientation;
		isRequestedOrientation = true;
	}

	private static boolean isTabletDevice(Context activityContext) {
		// Verifies if the Generalized Size of the device is XLARGE to be
		// considered a Tablet
		boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == SIZE_XLARGE & Configuration.SCREENLAYOUT_SIZE_LARGE >= 4);

		// If XLarge, checks if the Generalized Density is at least MDPI
		// (160dpi)
		if (xlarge) {
			DisplayMetrics metrics = new DisplayMetrics();
			Activity activity = (Activity) activityContext;
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			// MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
			// DENSITY_TV=213, DENSITY_XHIGH=320
			if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
					|| metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
//					|| metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH
					|| metrics.densityDpi == Density_TV
					|| metrics.densityDpi == Density_XHIGH) {

				// Yes, this is a tablet!
				return true;
			}
		}

		// No, this is not a tablet!
		return false;
	}

	public boolean isLogined() {
		boolean flag = mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,
				((Activity) mContext).MODE_PRIVATE).getBoolean(LOGINED, false);
		 
		return flag;
	}
	
	/*
	 * ******Just added ****
	 */
	// Allows the user to set a Login Listener and react to the event
	public void setOnLoginListener(WSLoginListener loginlistener) {
		this.loginListener = loginlistener;
	}
	
	/*
	 * ******Just added ****
	 */
	// Allows the user to set a Registers Listener and react to the event
	public void setOnRegistersListener(WSRegisterListener registerlistener) {
		registerListener = registerlistener;
	}
	/*
	 * ******Just added ****
	 */
	// Allows the user to set a Switch Listener and react to the event
	public void setOnSwitchListener(WSSwitchListener switchlistener) {
		switchListener = switchlistener;
	}
	/*
	 * ******Just added ****
	 */
	public  String getAccessToken(){
		return mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,"");
	}
	
	public ProfileObject didLoginUser(){
		APIServer sver=new APIServer(mContext);
		ProfileObject obj=sver.getProfile(getAccessToken());
		if(obj != null){
				if(obj.getmEmail() != "" && obj.getmEmail() != null && obj.getmEmail() != "null" ){
					Log.i("ThumbrSDK","didLoginUser = true with email: " + obj.getmEmail());
			mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit().putBoolean(LOGINED, true).commit();
				}else{
					mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit().putBoolean(LOGINED, false).commit();
				}
			mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).edit().putString(ACCESSTOKEN, getAccessToken() ).commit();				
		}
		return obj;
		
	}
}
