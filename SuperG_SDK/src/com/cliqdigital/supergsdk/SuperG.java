package com.cliqdigital.supergsdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.MadsAdView.MadsAdView;
import com.adgoji.mraid.adview.AdView;
import com.adgoji.mraid.adview.AdViewCore;
import com.adgoji.mraid.adview.AdViewCore.MadsOnOrmmaListener;
import com.adgoji.mraid.jsbridge.listeners.AdExpandListener;
import com.cliqdigital.supergsdk.components.AppsFlyerHelper;
import com.cliqdigital.supergsdk.components.ThumbrWebViewDialog;
import com.cliqdigital.supergsdk.utils.APIServer;
import com.cliqdigital.supergsdk.utils.EVA;
import com.cliqdigital.supergsdk.utils.Log;
import com.cliqdigital.supergsdk.utils.ProfileObject;
import com.cliqdigital.supergsdk.utils.SuperGLog;
import com.cliqdigital.supergsdk.utils.WSLoginListener;
import com.cliqdigital.supergsdk.utils.WSRegisterListener;
import com.cliqdigital.supergsdk.utils.WSStateCode;
import com.cliqdigital.supergsdk.utils.WSSwitchListener;
import com.unity3d.player.UnityPlayer;



@TargetApi(Build.VERSION_CODES.BASE)
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class SuperG implements OnCancelListener,MadsOnOrmmaListener{
	
	private static final String TAG = "SuperGSDK";
	
	static final String SENDER_ID = "906501791685";
	
	public static Context mContext;
	public WSSwitchListener switchListener=null;
	public ThumbrWebViewDialog mDialog;
	public static final String VALID_LOGIN = "Valid";
	public static final String LOGGEDIN = "LoggedIn";
	public static final String ACCESSTOKEN = "";
	public int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
	public static final int Density_TV = 213;
	public static final int Density_XHIGH = 320;
	public static final int SIZE_XLARGE = 4;
	public String mGASPId, mGameId;
	public boolean isShowbuttonClose = true;
	public boolean isConfigchange_orientation = false;

	public static final int MY_ORIENTATION = 0x0080;
	public String linkRegister = "";
	public String linkSwitch = "";
	public String linkPortal = "";
	public String theKey = "49b26e3ac8701cf4c5840587d1d5e6eba01ab329b9179f6aef925f362a98065f";

	public OnScoreSavedListener onScoreSavedListener;
	public String SDKLayout;
	protected MadsAdView adView;
	public OnInterstitialCloseListener mListener;

	public Object ad_view;
	public final BroadcastReceiver networkstate = new NetworkState();
	
	public interface OnScoreSavedListener {
		public void onScoreSaved(List<NameValuePair> returnlist);
	}

	public void setOnScoreSavedListener(OnScoreSavedListener listener) {
		onScoreSavedListener = listener;
	}
	public void onScoreSaved(List<NameValuePair> returnlist) {
	}
	
	public static OnPushMessageListener pListener;
	public interface OnPushMessageListener{
		public void onPushEvent(Intent intent);
	}


	public Runnable setPushMessageListener(OnPushMessageListener eventListener) {
		pListener=eventListener;
		return null;
	}

	public boolean closeButtonClosed;
	
	/**
	 * 
	 * @param context
	 * @param sid
	 *            :identifies the game and is mandatory
	 */
	public SuperG(Context context, int sid) {
		
		this.setPushMessageListener(new OnPushMessageListener(){

			@Override
			public void onPushEvent(Intent intent) {
				//Keep empty, will be overridden
			}	
		});
		EVA eva = new EVA();
		eva.syncEvents(context,false);
		eva.sessionTimer(context);
		this.closeButtonClosed = false;
		System.setProperty("http.keepAlive", "false");
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mContext = context;
		linkRegister = APIServer.getURLRegister(sid, (Activity) context);	
		linkSwitch = APIServer.getURLSwitchAccount(sid, (Activity) context);
		linkPortal = APIServer.getURLPrtal();
		check_importConfichange();
		mGASPId = SuperGLog.createSuperGID((Activity) mContext);
		mGameId = SuperGLog.createGameID((Activity) mContext);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(networkstate, intentFilter);

		if(isOnline()){
			getAdSettings();
		}
		
		Log.i(TAG, "SuperG initialized");
	}


	public void setEnableButtonClose(boolean flag) {
		isShowbuttonClose = flag;
	}

	public void setAction(String action){
		if(action !="" && action!="registration" && action!="optional_registration"){
			action = "default";
		}
		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		settings.edit().putString("action", action).commit();
	}
	public String getAction(){
		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		String action = settings.getString("action", "");
		if(action !="" && action!="registration" && action!="optional_registration"){
			action = "default";
		}	       
		return action;
	}
	public int getCount(){
		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		int count = settings.getInt("count", 0);
		count = count + 1;
		settings.edit().putInt("count", count).commit();		
		return count;	
	}

	public void buttonREGISTER() {
		Log.i(TAG,"Register button clicked");
		//		if (!isLogined()) {
		//
		//
		//			if(registerListener!=null)
		//			{				
		//				registerListener.callback(WSStateCode.FAILED,null, "Connection failed");
		//				Log.i(TAG,"Existing login failed, going into registration mode");
		//			}
		ShowDialog(linkRegister);
		//		} else {
		//			if(registerListener!=null)
		//			{							
		//				registerListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).getString(SuperG.ACCESSTOKEN,""), "LoginSuccess");
		//				Log.i(TAG,"Logged in successful with accestoken" + SuperG.ACCESSTOKEN);
		//			}
		//			buttonPORTAL();
		//		}
	}

	public void buttonSWITCH() {
		ThumbrWebViewDialog.accToken="";
		if (!isLoggedIn()) {
			if(switchListener!=null)
			{				
				switchListener.callback(WSStateCode.FAILED,null, "Connection failed");
			}
		}else{
			if(switchListener!=null)
			{				
				switchListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).getString(SuperG.ACCESSTOKEN,""), "LoginSuccess");
			}			
		}
		mContext.getSharedPreferences(SuperG.LOGGEDIN,Context.MODE_PRIVATE).edit().putBoolean(SuperG.LOGGEDIN,false).commit();
		mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).edit().putString(ACCESSTOKEN, null ).commit();
		Log.i(TAG,"linkswitch url: "+linkSwitch);
		ShowDialog(linkSwitch);
	}

	public void buttonCOOKIES() {
		superGClearCookies();		
		Log.i(TAG,"Clear cookie button clicked");
	}	


	public void superGClearCookies() {

		mDialog = new ThumbrWebViewDialog(mContext, isShowbuttonClose);

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();	
		mContext.getSharedPreferences(SuperG.LOGGEDIN,Context.MODE_PRIVATE).edit()
		.putBoolean(SuperG.LOGGEDIN,false).commit();
		mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).edit()
		.putString(SuperG.ACCESSTOKEN,null).commit();	    
		mDialog.dismiss();

		SuperGLog.lt(mContext, 3, "Cookies cleared & logged out");

	}		

	public void buttonPORTAL() {
		SuperGLog.lt(mContext, 3, "ButtonPORTAL :: ShowDialog :: "+linkPortal+getAccessToken());
		ShowDialog(linkPortal+getAccessToken());

	}

	public void buttonExit() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void ShowDialog(String link) {
		//SET THE REQUESTED ORIENTATION
		//((Activity) mContext).setRequestedOrientation(requestedOrientation);

		//attach action to link
		link = link + "&action=" + this.getAction() + "&sdk=1&count=" + getCount();

		mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE).edit().putString("SDKLayout", getLayout()).commit();
		mDialog = new ThumbrWebViewDialog(mContext, isShowbuttonClose);
		try {
			mDialog.setOnDismissListener((OnDismissListener) mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mDialog.setURL(link);// mContext.getResources().getString(R.string.Loginlink));
		mDialog.show();

	}



	public void check_importConfichange() {
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

	public class NetworkState extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			SuperGLog.l(SuperGLog.TMB_LOGTYPE_INFO, "Network connectivity change");
			if (intent.getExtras() != null) {
				@SuppressWarnings("deprecation")
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					SuperGLog.l(SuperGLog.TMB_LOGTYPE_INFO,
							"Network " + ni.getTypeName() + " connected");
					mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putBoolean("isOnline", true).commit();
				}
			}
			if (intent.getExtras().getBoolean(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
				SuperGLog.l(SuperGLog.TMB_LOGTYPE_INFO,
						"There's no network connectivity");
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putBoolean("isOnline", false).commit();
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.showNotNetwork();
				}
			}
		}
	}

	public String getGameId() {
		return mGameId;
	}

	public String getGASPId() {
		return mGASPId;
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

	public void setLayout(String SDKLayout){
		this.SDKLayout = SDKLayout;
	}

	public String getLayout(){
		if(SDKLayout.equals("")){
			SDKLayout = "thumbr";
		}
		return SDKLayout;
	}

	/**
	 * 
	 * @param b
	 */
	public void setToastDebug(boolean b) {
		Log.setDEBUG(b);
		SuperGLog.bToastDebug = false;
	}

	/**
	 * 
	 * @param requestedOrientation
	 */
	public void setOrientation(int requestedOrientation) {
		this.requestedOrientation = requestedOrientation;
	}

	@SuppressWarnings("deprecation")
	public static boolean isTabletDevice(Context activityContext) {
		TelephonyManager manager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE && display.getWidth() >= 728 ) {
			// True, this is a tablet!
			return true;
		}
		// False, this is not a tablet!
		return false;
	}



	public boolean isLoggedIn() {
		boolean flag = mContext.getSharedPreferences(SuperG.LOGGEDIN,
				Context.MODE_PRIVATE).getBoolean(LOGGEDIN, false);

		return flag;
	}

	/*
	 * ******Just added ****
	 */
	// Allows the user to set a Login Listener and react to the event
	public void setOnLoginListener(WSLoginListener loginlistener) {
	}

	/*
	 * ******Just added ****
	 */
	// Allows the user to set a Registers Listener and react to the event
	public void setOnRegistersListener(WSRegisterListener registerlistener) {
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
		return mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).getString(SuperG.ACCESSTOKEN,"");
	}

	public ProfileObject didLoginUser(){
		@SuppressWarnings("unused")
		SharedPreferences gamesettings = mContext.getSharedPreferences("SuperGScoreSettings", Context.MODE_PRIVATE);        

		AppsFlyerHelper appflyerhelper = new AppsFlyerHelper();
		
		APIServer sver=new APIServer(mContext);
		ProfileObject obj=sver.getProfile(getAccessToken());
		if(obj != null){
			if(obj.getmEmail() != "" && obj.getmEmail() != null && obj.getmEmail() != "null" ){
				Log.i(TAG,"didLoginUser = true with email: " + obj.getmEmail());
				mContext.getSharedPreferences(SuperG.LOGGEDIN,Context.MODE_PRIVATE).edit().putBoolean(LOGGEDIN, true).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("ID", obj.getmID().toString()).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("Username",obj.getmUserName() ).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("handset_id",appflyerhelper.getAppsflyerId(mContext)).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("superg_id",obj.getmID() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("address",obj.getmAddress() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("city",obj.getmCity() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("country",obj.getmCountry() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("email",obj.getmEmail() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("firstname",obj.getmFirstName() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("locale",obj.getmLocale() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("msisdn",obj.getmSisdn() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("newsletter",obj.getmNewsLetter() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("status",obj.getmStatus() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("surname",obj.getmSurname() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("username",obj.getmUserName() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("zipcode",obj.getmZipCode() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("gender",obj.getmGender() ).commit();				
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("age",obj.getmAge() ).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("date_of_birth",obj.getmDOB() ).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("housenr",obj.getmHousenr() ).commit();
				mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).edit().putString("token",getAccessToken() ).commit();				
			}else{
				mContext.getSharedPreferences(SuperG.LOGGEDIN,Context.MODE_PRIVATE).edit().putBoolean(LOGGEDIN, false).commit();
			}
			mContext.getSharedPreferences(SuperG.ACCESSTOKEN,Context.MODE_PRIVATE).edit().putString(ACCESSTOKEN, getAccessToken() ).commit();				
			
			
			Log.i(TAG,"ID: "+obj.getmID());
			Log.i(TAG,"Username: "+obj.getmUserName());



			//			if((obj.getmID() != "" && obj.getmUserName()!="") 
			//					&& (gamesettings.getString("userCreated", "") == "" || gamesettings.getString("userCreated", "") != obj.getmUserName()) ){
			//
			//				Log.i(TAG,"Creating player (score api)");
			//				this.createPlayer();
			//			}		
		}

		return obj;


	}


	//Helper methods
	@SuppressWarnings("deprecation")
	public String ReadFromfile(String fileName, Context context) {
		StringBuilder ReturnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets()
					.open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				ReturnString.append(line);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return ReturnString.toString();
	}

	public int rand(){
		Random r = new Random();
		int i1=r.nextInt();
		return i1;
	}

	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i=0; i<messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}	

	public void adInit(){
		MadsAdView.init(mContext);
	}

	public void pause(Context context){
		Log.i("MRAID","Putting ads to sleep");
		
		try {
			context.unregisterReceiver(networkstate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MadsAdView.sleepSession();
	}

	public void resume(){
		
		try {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mContext.registerReceiver(networkstate, intentFilter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("MRAID","Resuming ads");		
		try {
			MadsAdView.resume();
			EVA eva = new EVA();
			eva.applicationGetFocus(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isOnline() {Log.i(TAG,"isOnline");
	ConnectivityManager cm =
			(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();

	if (netInfo != null && netInfo.isConnectedOrConnecting() && mContext.getSharedPreferences("SuperGSettings",Context.MODE_PRIVATE).getBoolean("isOnline",false) == true) {
		return true;
	}
	return false;
	}


	public void getAdSettings() {
		try {
			this.didLoginUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
			String sid = settings.getString("sid","");
			String url = "http://ads.superg.mobi/adserver/?getAdSettings=1&debug=0&sid="+sid;
			Log.i(TAG,"Get adsettings from: "+url);

			try {
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);

				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream is = httpEntity.getContent();           

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				try {
					String line;
					line = reader.readLine();
					String[] RowData = line.split(",");
					if(RowData[0] != null){
						settings.edit().putInt("updateTimeIntervalOverride", Integer.parseInt(RowData[0])).commit();
					}

					if(RowData[1] != null){
						settings.edit().putInt("showCloseButtonTime", Integer.parseInt(RowData[1])).commit();
					}
					else{
						settings.edit().putInt("showCloseButtonTime", settings.getInt("showCloseButtonTime",6)).commit();	
					}

					if(RowData[2] != null){
						settings.edit().putInt("hideThumbrCloseButton", Integer.parseInt(RowData[2])).commit();
					}
					else{
						settings.edit().putInt("hideThumbrCloseButton", settings.getInt("hideThumbrCloseButton", 0)).commit();	
					}

					Log.i(TAG,"INLINE ADS UPDATE TIME INTERVAL: "+RowData[0]);
					Log.i(TAG,"INTERSTITIAL ADS SHOW CLOSE BUTTON AFER N SECONDS: "+RowData[1]);
					Log.i(TAG,"HIDE THUMBR WINDOW CLOSEBUTTON: "+RowData[2]);
				}
				catch (IOException ex) {
					// handle exception
					Log.i(TAG,"Cannot read settings");
				}


			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} catch (Exception e) {
			Log.i(TAG,"Cannot update Ads time interval: "+e);
		}
	}


	public String getAdSetting(String adType,String dataType){
		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);
		if(dataType == "secret" || dataType == "zoneid"){
			if(isTabletDevice(mContext)==true){
				if(adType =="inline"){
					return settings.getString("tablet_Inline_"+dataType,"");
				}
				else if(adType == "overlay"){
					return settings.getString("tablet_Overlay_"+dataType,"");				
				}
				else if(adType == "interstitial"){
					return settings.getString("tablet_Interstitial_"+dataType,"");
				}
			}
			else{
				if(adType =="inline"){
					return settings.getString("phone_Inline_"+dataType,"");
				}
				else if(adType == "overlay"){
					return settings.getString("phone_Overlay_"+dataType,"");
				}
				else if(adType == "interstitial"){
					return settings.getString("phone_Interstitial_"+dataType,"");
				}			
			}
		}
		return "";
	}

	public interface OnInterstitialCloseListener{
		public void onEvent();
	}

	public void setInterstitialCloseListener(OnInterstitialCloseListener eventListener) {
		mListener=eventListener;
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		((ViewGroup) this.ad_view).removeAllViews();
	}


	@SuppressLint("SetJavaScriptEnabled")
	@JavascriptInterface
	public void adInterstitial(final RelativeLayout ad_view){


		final ProgressDialog progress = ProgressDialog.show(mContext, "","loading...", false,false,(OnCancelListener) this);
		this.ad_view=ad_view;
		Runnable mMyRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				progress.setCancelable(true);
			}
		};
		Handler myHandler = new Handler();
		myHandler.postDelayed(mMyRunnable, 5000);


		final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		//Display display = wm.getDefaultDisplay();
		//		int adHeight = display.getHeight();
		//		int adWidth = display.getWidth();
		//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ad_view.getLayoutParams();
		//		params.height = adHeight;
		//		params.width = adWidth;
		//		ad_view.setLayoutParams(params);

		final SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("interstitial","secret"), getAdSetting("interstitial","zoneid"));
		WebSettings aWS = adView.getSettings();
		aWS.setJavaScriptCanOpenWindowsAutomatically(true);
		aWS.setJavaScriptEnabled(true);

		adView.addJavascriptInterface(this, "CUSTOMANDROID");		

		adView.setAdserverURL("http://ads.superg.mobi/adserver/");

		adView.setId(9875737);
		adView.setInternalBrowser(true);
		adView.setContentAlignment(true);
		adView.setLocationDetection(true);
		adView.setMadsAdType("interstitial");
		adView.setUpdateTime(0);
		//adView.setShowCloseButtonTime(settings.getInt("showCloseButtonTime",6));
		adView.setEnableExpandInActivity(false);

		adView.setZip(settings.getString("zipcode",""));
		adView.setGender(settings.getString("gender",""));
		try{
			adView.setAge(Integer.parseInt(settings.getString("age","")));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		adView.setCity(settings.getString("city",""));
		adView.setCountry(settings.getString("country",""));
		Hashtable<String, Object> map = new Hashtable<String,Object>();
		try{
			map.put("id",URLEncoder.encode(settings.getString("id",""), "UTF-8"));	
			map.put("sid",URLEncoder.encode(settings.getString("sid",""), "UTF-8"));
			map.put("client_id",URLEncoder.encode(settings.getString("client_id",""), "UTF-8"));
			map.put("handset_id",URLEncoder.encode(settings.getString("handset_id",""), "UTF-8"));
			map.put("superg_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));		
			map.put("address",URLEncoder.encode(settings.getString("address",""), "UTF-8"));
			map.put("city",URLEncoder.encode(settings.getString("city",""), "UTF-8"));
			map.put("country",URLEncoder.encode(settings.getString("country",""), "UTF-8"));
			map.put("email",URLEncoder.encode(settings.getString("email",""), "UTF-8"));
			map.put("firstname",URLEncoder.encode(settings.getString("firstname",""), "UTF-8"));
			map.put("locale",URLEncoder.encode(settings.getString("locale",""), "UTF-8"));
			map.put("msisdn",URLEncoder.encode(settings.getString("msisdn",""), "UTF-8"));
			map.put("newsletter",URLEncoder.encode(settings.getString("newsletter",""), "UTF-8"));
			map.put("status",URLEncoder.encode(settings.getString("status",""), "UTF-8"));
			map.put("surname",URLEncoder.encode(settings.getString("surname",""), "UTF-8"));
			map.put("username",URLEncoder.encode(settings.getString("username",""), "UTF-8"));
			map.put("zipcode",URLEncoder.encode(settings.getString("zipcode",""), "UTF-8"));
			map.put("gender",URLEncoder.encode(settings.getString("gender",""), "UTF-8"));
			map.put("age",URLEncoder.encode(settings.getString("age",""), "UTF-8"));
			map.put("date_of_birth",URLEncoder.encode(settings.getString("date_of_birth",""), "UTF-8"));
			map.put("housenr",URLEncoder.encode(settings.getString("housenr",""), "UTF-8"));
			map.put("token",URLEncoder.encode(settings.getString("token",""), "UTF-8"));
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			map.put("sig",getHash(theKey, settings.getString("superg_id", "")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		adView.setCustomParameters(map);


		if(isOnline() && getAdSetting("interstitial","zoneid") != ""){
			adView.update();
		}
		else{return;}



		ad_view.addView(adView);
		adView.setAdExpandListener(new AdExpandListener() {
			@Override
			public void onExpand() {

			}
			@Override
			public void onClose() {
				ad_view.removeAllViews();
				ad_view.setBackgroundColor(Color.TRANSPARENT);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ad_view.getLayoutParams();
				params.height = 0;
				ad_view.setLayoutParams(params);
				if(mListener!=null) {mListener.onEvent();

				}
				try {
					if(isClass("com.unity3d.player.UnityPlayer") && UnityPlayer.currentActivity != null ){
						UnityPlugin.interstitialClosed();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		adView.setOnAdDownload(new AdViewCore.OnAdDownload() 
		{

			@Override
			public void begin(AdView sender) {

			}

			@Override
			public void end(final AdView sender) 
			{		
				adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.superg.mobi/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})();");
				adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {var oldVersion = mraid.close;mraid.close = function() {var result = oldVersion.apply(this, arguments);CUSTOMANDROID.closeListen(); return result;}};" +  "})();");

				//after 1 second do stuff
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {if(mraid.getExpandProperties().useCustomClose == true){CUSTOMANDROID.hideNativeCloseButton();}else{CUSTOMANDROID.showNativeCloseButton();}};" +  "})();");
						progress.dismiss();
						ad_view.setBackgroundColor(Color.parseColor("#AA000000"));
					}
				}, 1000);

				//after x seconds, show the close button (if not cancelled by mraid CustomButton)
				final Handler handler2 = new Handler();
				handler2.postDelayed(new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {if(mraid.getExpandProperties().useCustomClose == true){CUSTOMANDROID.hideNativeCloseButton();}else{CUSTOMANDROID.showNativeCloseButton();}};" +  "})();");

						progress.dismiss();
						//ad_view.setBackgroundColor(Color.TRANSPARENT);

						//WE ADD OUR OWN CLOSE BUTTON TO THE INTERSTITIAL, BECAUSE WE CAN HARDLY CONTROL THE STANDARD MADS BUTTON
						Display display = wm.getDefaultDisplay();
						
						int adHeight = display.getHeight();
						int adWidth = display.getWidth();	
						int buttonSize;
						if(adWidth > adHeight) {
							buttonSize = (adWidth / 25);
						} else{
							buttonSize = (adWidth / 10);
						}
						int buttonMargin = buttonSize / 10;
						ImageButton closeButton = new ImageButton(mContext);
						closeButton.setImageResource(R.drawable.ads_close_button);
						closeButton.setScaleType(ScaleType.FIT_END);
						closeButton.setPadding(0,0,0,0);
						closeButton.setBackgroundColor(Color.TRANSPARENT);
						closeButton.setId(234234432);

						RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(buttonSize, buttonSize);

						ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
						ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						ButtonParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);

						closeButton.setLayoutParams(ButtonParams);

						if(closeButtonClosed == false && ad_view.getChildCount() > 0){
							ad_view.addView(closeButton);
						}

						closeButton.bringToFront();
						closeButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Log.i(TAG,"Close the interstitial.");
								if(mListener!=null) {mListener.onEvent();
								}
								try {
									if(isClass("com.unity3d.player.UnityPlayer") && UnityPlayer.currentActivity != null ){
										UnityPlugin.interstitialClosed();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}				              
								ad_view.removeAllViews();
							}
						});
					}
				}, (settings.getInt("showCloseButtonTime",6) * 1000));


				//after 8 second do more stuff
				final Handler handler3 = new Handler();
				handler3.postDelayed(new Runnable() {
					@Override
					public void run() {
						View closeButton = ((Activity) mContext).findViewById(234234432);
						if(((Activity) mContext).findViewById(234234432) != null ){
							closeButton.bringToFront();
						}
					}
				}, 8000);


			}

			@Override
			public void error(AdView sender, String error) {
				Log.e("MRAID", "Error in ad download phase: " + error);
				progress.dismiss();
				if(mListener!=null) {mListener.onEvent();

				}
				try {
					if(isClass("com.unity3d.player.UnityPlayer") && UnityPlayer.currentActivity != null ){
						UnityPlugin.interstitialClosed();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void noad(AdView sender) {
				Log.d("MRAID", "The ad server responded by telling us no ad is available");
				progress.dismiss();
				if(mListener!=null) {mListener.onEvent();

				}
				try {
					if(isClass("com.unity3d.player.UnityPlayer") && UnityPlayer.currentActivity != null ){
						UnityPlugin.interstitialClosed();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});		

	}

	public void adOverlay(final RelativeLayout ad_view){
		ad_view.removeAllViews();

		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("overlay","secret"), getAdSetting("overlay","zoneid"));
		adView.setAdserverURL("http://ads.superg.mobi/adserver/");
//		adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setId(11223344);
		adView.setInternalBrowser(true);
		adView.setContentAlignment(true);
		adView.setLocationDetection(true);
		adView.setMadsAdType("overlay");
		adView.setUpdateTime(0);
		//adView.setShowCloseButtonTime(settings.getInt("showCloseButtonTime",6));

		adView.setEnableExpandInActivity(true);
		adView.setZip(settings.getString("zipcode",""));
		adView.setGender(settings.getString("gender",""));
		try{
			adView.setAge(Integer.parseInt(settings.getString("age","")));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		adView.setCity(settings.getString("city",""));
		adView.setCountry(settings.getString("country",""));
		Hashtable<String, Object> map = new Hashtable<String,Object>();
		try{
			map.put("id",URLEncoder.encode(settings.getString("id",""), "UTF-8"));	
			map.put("sid",URLEncoder.encode(settings.getString("sid",""), "UTF-8"));
			map.put("client_id",URLEncoder.encode(settings.getString("client_id",""), "UTF-8"));
			map.put("handset_id",URLEncoder.encode(settings.getString("handset_id",""), "UTF-8"));
			map.put("superg_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));		
			map.put("address",URLEncoder.encode(settings.getString("address",""), "UTF-8"));
			map.put("city",URLEncoder.encode(settings.getString("city",""), "UTF-8"));
			map.put("country",URLEncoder.encode(settings.getString("country",""), "UTF-8"));
			map.put("email",URLEncoder.encode(settings.getString("email",""), "UTF-8"));
			map.put("firstname",URLEncoder.encode(settings.getString("firstname",""), "UTF-8"));
			map.put("locale",URLEncoder.encode(settings.getString("locale",""), "UTF-8"));
			map.put("msisdn",URLEncoder.encode(settings.getString("msisdn",""), "UTF-8"));
			map.put("newsletter",URLEncoder.encode(settings.getString("newsletter",""), "UTF-8"));
			map.put("status",URLEncoder.encode(settings.getString("status",""), "UTF-8"));
			map.put("surname",URLEncoder.encode(settings.getString("surname",""), "UTF-8"));
			map.put("username",URLEncoder.encode(settings.getString("username",""), "UTF-8"));
			map.put("zipcode",URLEncoder.encode(settings.getString("zipcode",""), "UTF-8"));
			map.put("gender",URLEncoder.encode(settings.getString("gender",""), "UTF-8"));
			map.put("age",URLEncoder.encode(settings.getString("age",""), "UTF-8"));
			map.put("date_of_birth",URLEncoder.encode(settings.getString("date_of_birth",""), "UTF-8"));
			map.put("housenr",URLEncoder.encode(settings.getString("housenr",""), "UTF-8"));
			map.put("token",URLEncoder.encode(settings.getString("token",""), "UTF-8"));
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		try {
			map.put("sig",getHash(theKey, settings.getString("superg_id", "")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		adView.setCustomParameters(map);

		if(isOnline() && getAdSetting("overlay","zoneid") != ""){
			adView.update();
		}
		else{
			return;}

		adView.setAdExpandListener(new AdExpandListener() {
			@Override
			public void onExpand() {

			}
			@Override
			public void onClose() {
				@SuppressWarnings("unused")
				Runnable mMyRunnable = new Runnable()
				{
					@Override
					public void run()
					{
						ad_view.setBackgroundColor(Color.TRANSPARENT);
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ad_view.getLayoutParams();
						params.height = 0;
						ad_view.setLayoutParams(params);
					}
				};
			}
		});

		adView.setOnAdDownload(new AdViewCore.OnAdDownload() 
		{

			@Override
			public void begin(AdView sender) {

			}

			@Override
			public void end(final AdView sender) 
			{
				adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.suprg.mobi/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})()");
				adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.superg.mobi/adserver/js/mraid.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})()");
			}

			@Override
			public void error(AdView sender, String error) {
				Log.e("MRAID", "Error in ad download phase: " + error);

			}

			@Override
			public void noad(AdView sender) {
				Log.d("MRAID", "The ad server responded by telling us no ad is available");

			}
		});		

	}

	public String getHash(String key,String id) throws NoSuchAlgorithmException, UnsupportedEncodingException{

		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String text = key + ":" + id;
			md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
			byte[] result = md.digest();
			StringBuilder sb = new StringBuilder();

			for (byte b : result) // This is your byte[] result..
			{
				sb.append(String.format("%02x", b));
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.BASE)
	@SuppressLint("NewApi")
	public void adInline(final RelativeLayout ad_view)
	{

		ad_view.removeAllViews();		
		SharedPreferences settings = mContext.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("inline","secret"), getAdSetting("inline","zoneid"));

		adView.setAdserverURL("http://ads.superg.mobi/adserver/");
//		adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setId(1);
		adView.setInternalBrowser(true);
		adView.setContentAlignment(true);
		adView.setLocationDetection(true);
		adView.setMadsAdType("inline");
		if( settings.getInt("updateTimeIntervalOverride",0) >= 0 ){
			adView.setUpdateTime(settings.getInt("updateTimeIntervalOverride",0));
			Log.i(TAG,"update Time Interval Override is used: "+settings.getInt("updateTimeIntervalOverride",0));
		}else{
			adView.setUpdateTime(settings.getInt("updateTimeInterval",0));
		}
		//adView.setShowCloseButtonTime(settings.getInt("showCloseButtonTime",6));
		adView.setEnableExpandInActivity(true);

		adView.setZip(settings.getString("zipcode",""));
		adView.setGender(settings.getString("gender",""));
		try{
			adView.setAge(Integer.parseInt(settings.getString("age","")));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		adView.setCity(settings.getString("city",""));
		adView.setCountry(settings.getString("country",""));
		Hashtable<String, Object> map = new Hashtable<String,Object>();
		try{
			map.put("id",URLEncoder.encode(settings.getString("id",""), "UTF-8"));	
			map.put("sid",URLEncoder.encode(settings.getString("sid",""), "UTF-8"));
			map.put("client_id",URLEncoder.encode(settings.getString("client_id",""), "UTF-8"));
			map.put("handset_id",URLEncoder.encode(settings.getString("handset_id",""), "UTF-8"));
			map.put("superg_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("superg_id",""), "UTF-8"));		
			map.put("address",URLEncoder.encode(settings.getString("address",""), "UTF-8"));
			map.put("city",URLEncoder.encode(settings.getString("city",""), "UTF-8"));
			map.put("country",URLEncoder.encode(settings.getString("country",""), "UTF-8"));
			map.put("email",URLEncoder.encode(settings.getString("email",""), "UTF-8"));
			map.put("firstname",URLEncoder.encode(settings.getString("firstname",""), "UTF-8"));
			map.put("locale",URLEncoder.encode(settings.getString("locale",""), "UTF-8"));
			map.put("msisdn",URLEncoder.encode(settings.getString("msisdn",""), "UTF-8"));
			map.put("newsletter",URLEncoder.encode(settings.getString("newsletter",""), "UTF-8"));
			map.put("status",URLEncoder.encode(settings.getString("status",""), "UTF-8"));
			map.put("surname",URLEncoder.encode(settings.getString("surname",""), "UTF-8"));
			map.put("username",URLEncoder.encode(settings.getString("username",""), "UTF-8"));
			map.put("zipcode",URLEncoder.encode(settings.getString("zipcode",""), "UTF-8"));
			map.put("gender",URLEncoder.encode(settings.getString("gender",""), "UTF-8"));
			map.put("age",URLEncoder.encode(settings.getString("age",""), "UTF-8"));
			map.put("date_of_birth",URLEncoder.encode(settings.getString("date_of_birth",""), "UTF-8"));
			map.put("housenr",URLEncoder.encode(settings.getString("housenr",""), "UTF-8"));
			map.put("token",URLEncoder.encode(settings.getString("token",""), "UTF-8"));
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			map.put("sig",getHash(theKey, settings.getString("superg_id", "")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adView.setCustomParameters(map);

		if(isOnline() && getAdSetting("inline","zoneid") != ""){
			adView.update();
		}
		else{
			return;
		}

		RelativeLayout rl = new RelativeLayout(mContext);

		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		adView.setMaxwidth(display.getWidth());
		int adHeight=56;
		if(display.getWidth() > display.getHeight()){
			adHeight = (int) ((display.getWidth()*0.6)/6);
		}
		else{
			adHeight = display.getWidth()/6;
		}

		adView.setMaxheight(adHeight);

		//		if(ad_view.getBottom() <= 50){
		//			lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//		}
		//		else{
		//			lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);	
		//		}
		lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);	

		rl.addView(adView);
		ad_view.addView(rl,lay);
		ad_view.bringToFront();
		adView.setAdExpandListener(new AdExpandListener() {
			@Override
			public void onExpand() {

			}
			@Override
			public void onClose() {
				@SuppressWarnings("unused")
				Runnable mMyRunnable = new Runnable()
				{
					@Override
					public void run()
					{
						ad_view.setBackgroundColor(Color.TRANSPARENT);
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ad_view.getLayoutParams();
						params.height = 0;
						ad_view.setLayoutParams(params);
					}
				};
			}
		});

		if (android.os.Build.VERSION.SDK_INT < 11)
		{
			adView.setMaxwidth(display.getWidth());
			int staticAdHeight=56;
			if(display.getWidth() > display.getHeight()){
				staticAdHeight = (int) ((display.getWidth()*0.6)/6.4);
			}
			else{
				staticAdHeight = (int) (display.getWidth()/6.4);
			}			
			ad_view.getLayoutParams().height=staticAdHeight;

			adView.setOnAdDownload(new AdViewCore.OnAdDownload() 
			{

				@Override
				public void begin(AdView sender) {


				}

				@Override
				public void end(final AdView sender) 
				{
					adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.superg.mobi/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})();");
					adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://m-dev.superg.mobi/adserver/js/mraid.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})();");
				}

				@Override
				public void error(AdView sender, String error) {
					Log.e("MRAID", "Error in ad download phase: " + error);

				}

				@Override
				public void noad(AdView sender) {
					Log.d("MRAID", "The ad server responded by telling us no ad is available");

				}
			});			

		}
		else
		{

			adView.setOnAdDownload(new AdViewCore.OnAdDownload() 
			{

				@Override
				public void begin(AdView sender) {
					if(isOnline())
					{
						Log.d("MRAID", "Beginning ad download");
						Log.i("MRAID","settings: "+getAdSetting("inline","secret")+" :: "+getAdSetting("inline","zoneid"));
						//						((Activity) mContext).runOnUiThread(new Runnable() {
						//							public void run() {
						//								final ValueAnimator va = ValueAnimator.ofInt((int)ad_view.getHeight(), 0);
						//								va.setDuration(50);
						//								va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						//									public void onAnimationUpdate(ValueAnimator animation) {
						//										Integer value = (Integer) animation.getAnimatedValue();
						//										ad_view.getLayoutParams().height = value.intValue();
						//										ad_view.requestLayout();
						//									}
						//
						//								});
						//
						//								Runnable mMyRunnable = new Runnable()
						//								{
						//									@Override
						//									public void run()
						//									{
						//										va.start();
						//									}
						//								};
						//								Handler myHandler = new Handler();
						//								myHandler.postDelayed(mMyRunnable, 5);
						//							}
						//						});
					}
				}
				@TargetApi(Build.VERSION_CODES.BASE)
				@SuppressLint("NewApi")
				@Override
				public void end(final AdView sender) 
				{
					if(isOnline())
					{

						((Activity) mContext).runOnUiThread(new Runnable() {
							@SuppressWarnings("unused")
							public void run() {
								WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
								Display display = wm.getDefaultDisplay();
								adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.superg.mobi/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s);" +  "})();");

								//								int adHeight=54;//for now.
								//
								//								if(display.getWidth() > display.getHeight()){
								//									adHeight = (int) ((display.getWidth()*0.6)/6.4);
								//								}
								//								else{
								//									adHeight = (int) (display.getWidth()/6.4);
								//								}
								//
								//								Log.e("MRAID","Ad height:"+adHeight);
								//
								//								Log.i("MRAID","Final height: "+adHeight);
								//								final ValueAnimator va = ValueAnimator.ofInt(0, adHeight);
								//								va.setDuration(500);
								//								va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
								//									public void onAnimationUpdate(ValueAnimator animation) {
								//										Integer value = (Integer) animation.getAnimatedValue();
								//										ad_view.getLayoutParams().height = value.intValue();
								//										ad_view.requestLayout();
								//									}
								//
								//								});
								//
								//								Runnable mMyRunnable = new Runnable()
								//								{
								//									@Override
								//									public void run()
								//									{
								//										va.start();
								//									}
								//								};
								//								Handler myHandler = new Handler();
								//								myHandler.postDelayed(mMyRunnable, 1000);

							}
						});
					}
				}

				@TargetApi(Build.VERSION_CODES.BASE)
				@Override
				public void error(AdView sender, String error) {
					Log.e("MRAID", "Error in ad download phase: " + error);

					if(isOnline()){
						((Activity) mContext).runOnUiThread(new Runnable() {
							public void run() {
								ValueAnimator va = ValueAnimator.ofInt((int)ad_view.getHeight(), 0);
								va.setDuration(100);
								va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
									public void onAnimationUpdate(ValueAnimator animation) {
										Integer value = (Integer) animation.getAnimatedValue();
										ad_view.getLayoutParams().height = value.intValue();
										ad_view.requestLayout();
									}
								});
								va.start();
							}});
					}
				}

				@Override
				public void noad(AdView sender) {
					Log.d("MRAID", "The ad server responded by telling us no ad is available");
					if(isOnline())
					{
						((Activity) mContext).runOnUiThread(new Runnable() {
							public void run() {
								ValueAnimator va = ValueAnimator.ofInt((int)ad_view.getHeight(), 0);
								va.setDuration(100);
								va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
									public void onAnimationUpdate(ValueAnimator animation) {
										Integer value = (Integer) animation.getAnimatedValue();
										ad_view.getLayoutParams().height = value.intValue();
										ad_view.requestLayout();
									}
								});
								va.start();     
							}});
					}
				}
			});
		}
//		if(isOnline()){
//			getAdSettings();
//		}
		
	}
	@Override
	public void event(AdView arg0, String arg1, String arg2) {

		// TODO Auto-generated method stub
	}

	public String closeListen(){
		//MADS SDK does not properly listen to the mraid.close() function, so we're listening to it and closing and removing the adView ourselves.
		Log.i(TAG,"closeListen was called");
		try {((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				if(mListener!=null) {mListener.onEvent();

				}
				try {
					if(isClass("com.unity3d.player.UnityPlayer") && UnityPlayer.currentActivity != null ){
						UnityPlugin.interstitialClosed();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((ViewGroup) ad_view).removeAllViews();
			}
		});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}


	public String hideNativeCloseButton(){/*for MADS adserver*/
		Log.i(TAG,"hideNativeCloseButton was called");
		try {
			View closeButton = ((Activity) mContext).findViewById(234234432);
			this.closeButtonClosed = true;
			if(((Activity) mContext).findViewById(234234432) != null ){
				closeButton.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String showNativeCloseButton(){/*for MADS adserver*/
		Log.i(TAG,"showNativeCloseButton was called");
		try {

			this.closeButtonClosed = false;
			//closeButton.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}	

	public TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{

		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{

		}
	}
	};

	
	public boolean isClass(String className)
	{
	    boolean exist = true;
	    try 
	    {
	        Class.forName(className);
	    } 
	    catch (ClassNotFoundException e) 
	    {
	        exist = false;
	    }
	    return exist;
	}
}
