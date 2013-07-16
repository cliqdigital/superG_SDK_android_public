package com.gkxim.android.thumbsdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.util.Log;
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
import com.adgoji.mraid.interfaces.AdViewCoreInterface;
import com.adgoji.mraid.jsbridge.listeners.AdExpandListener;

import com.appsflyer.AppsFlyerLib;
import com.gkxim.android.thumbsdk.components.ThumbrWebViewDialog;
import com.gkxim.android.thumbsdk.utils.APIServer;
import com.gkxim.android.thumbsdk.utils.ProfileObject;
import com.gkxim.android.thumbsdk.utils.TBrLog;
import com.gkxim.android.thumbsdk.utils.WSLoginListener;
import com.gkxim.android.thumbsdk.utils.WSRegisterListener;
import com.gkxim.android.thumbsdk.utils.WSStateCode;
import com.gkxim.android.thumbsdk.utils.WSSwitchListener;
import com.nineoldandroids.*;
import com.unity3d.player.UnityPlayer;



@TargetApi(Build.VERSION_CODES.BASE)
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class FunctionThumbrSDK implements OnCancelListener,MadsOnOrmmaListener{

	public static Context mContext;
	public WSSwitchListener switchListener=null;
	public ThumbrWebViewDialog mDialog;
	public static final String VALID_LOGINED = "Valid";
	public static final String LOGINED = "Logined";
	public static final String ACCESSTOKEN = "";
	public int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
	public static final int Density_TV = 213;
	public static final int Density_XHIGH = 320;
	public static final int SIZE_XLARGE = 4;
	public String mThumbrId, mGameId;
	public boolean isShowbutonClose = true;
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

	public interface OnScoreSavedListener {
		public void onScoreSaved(List<NameValuePair> returnlist);
	}

	public void setOnScoreSavedListener(OnScoreSavedListener listener) {
		onScoreSavedListener = listener;
	}
	public void onScoreSaved(List<NameValuePair> returnlist) {
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
	public boolean closeButtonClosed;
	/**
	 * 
	 * @param context
	 * @param sid
	 *            :identifies the game and is mandatory
	 */
	public FunctionThumbrSDK(Context context, int sid) {
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
		mThumbrId = TBrLog.createThumbrID((Activity) mContext);
		mGameId = TBrLog.createGameID((Activity) mContext);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(new NetworkState(), intentFilter);
		//initScores();

		if(isOnline()){
			getAdSettings();
		}


		Log.i("ThumbrSDK", "Thumbr initialized");
	}




	public void setEnableButtonClose(boolean flag) {
		isShowbutonClose = flag;
	}

	public void setAction(String action){
		if(action !="" && action!="registration" && action!="optional_registration"){
			action = "default";
		}
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		settings.edit().putString("action", action).commit();
	}
	public String getAction(){
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String action = settings.getString("action", "");
		if(action !="" && action!="registration" && action!="optional_registration"){
			action = "default";
		}	       
		return action;
	}
	public int getCount(){
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		int count = settings.getInt("count", 0);
		count = count + 1;
		settings.edit().putInt("count", count).commit();		
		return count;	
	}

	public void buttonREGISTER() {
		Log.i("ThumbrSDK","Register button clicked");
		//		if (!isLogined()) {
		//
		//
		//			if(registerListener!=null)
		//			{				
		//				registerListener.callback(WSStateCode.FAILED,null, "Connection failed");
		//				Log.i("ThumbrSDK","Existing login failed, going into registration mode");
		//			}
		ShowDialog(linkRegister);
		//		} else {
		//			if(registerListener!=null)
		//			{							
		//				registerListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,""), "LoginSuccess");
		//				Log.i("ThumbrSDK","Logged in successful with accestoken" + FunctionThumbrSDK.ACCESSTOKEN);
		//			}
		//			buttonPORTAL();
		//		}
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
				switchListener.callback(WSStateCode.SUCCESS,mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,""), "LoginSuccess");
			}			
		}
		mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,Context.MODE_PRIVATE).edit().putBoolean(FunctionThumbrSDK.LOGINED,false).commit();
		mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).edit().putString(ACCESSTOKEN, null ).commit();
		Log.i("ThumbrSDK","linkswitch url: "+linkSwitch);
		ShowDialog(linkSwitch);
	}

	public void buttonCOOKIES() {
		ThumbrClearCookies();		
		Log.i("ThumbrSDK","Clear cookie button clicked");
	}	


	public void ThumbrClearCookies() {

		mDialog = new ThumbrWebViewDialog(mContext, isShowbutonClose);

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();	
		mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,Context.MODE_PRIVATE).edit()
		.putBoolean(FunctionThumbrSDK.LOGINED,false).commit();
		mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).edit()
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

	public void ShowDialog(String link) {
		//SET THE REQUESTED ORIENTATION
		//((Activity) mContext).setRequestedOrientation(requestedOrientation);

		//attach action to link
		link = link + "&action=" + this.getAction() + "&sdk=1&count=" + getCount();

		mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE).edit().putString("SDKLayout", getLayout()).commit();
		mDialog = new ThumbrWebViewDialog(mContext, isShowbutonClose);
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
			TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "Network connectivity change");
			if (intent.getExtras() != null) {
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					TBrLog.l(TBrLog.TMB_LOGTYPE_INFO,
							"Network " + ni.getTypeName() + " connected");
					mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putBoolean("isOnline", true).commit();
				}
			}
			if (intent.getExtras().getBoolean(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
				TBrLog.l(TBrLog.TMB_LOGTYPE_INFO,
						"There's no network connectivity");
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putBoolean("isOnline", false).commit();
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
		TBrLog.bToastDebug = false;
	}

	/**
	 * 
	 * @param requestedOrientation
	 */
	public void setOrientation(int requestedOrientation) {
		this.requestedOrientation = requestedOrientation;
	}

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



	public boolean isLogined() {
		boolean flag = mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,
				Context.MODE_PRIVATE).getBoolean(LOGINED, false);

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
		return mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).getString(FunctionThumbrSDK.ACCESSTOKEN,"");
	}

	public ProfileObject didLoginUser(){
		@SuppressWarnings("unused")
		SharedPreferences gamesettings = mContext.getSharedPreferences("ThumbrScoreSettings", Context.MODE_PRIVATE);        

		APIServer sver=new APIServer(mContext);
		ProfileObject obj=sver.getProfile(getAccessToken());
		if(obj != null){
			if(obj.getmEmail() != "" && obj.getmEmail() != null && obj.getmEmail() != "null" ){
				Log.i("ThumbrSDK","didLoginUser = true with email: " + obj.getmEmail());
				mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,Context.MODE_PRIVATE).edit().putBoolean(LOGINED, true).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("ID", obj.getmID().toString()).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("Username",obj.getmUserName() ).commit();

				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("handset_id",AppsFlyerLib.getAppsFlyerUID(mContext).toString() ).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("thumbr_id",obj.getmID() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("address",obj.getmAddress() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("city",obj.getmCity() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("country",obj.getmCountry() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("email",obj.getmEmail() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("firstname",obj.getmFirstName() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("locale",obj.getmLocale() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("msisdn",obj.getmSisdn() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("newsletter",obj.getmNewsLetter() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("status",obj.getmStatus() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("surname",obj.getmSurname() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("username",obj.getmUserName() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("zipcode",obj.getmZipCode() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("gender",obj.getmGender() ).commit();				
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("age",obj.getmAge() ).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("date_of_birth",obj.getmDOB() ).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("housenr",obj.getmHousenr() ).commit();
				mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).edit().putString("token",getAccessToken() ).commit();				
			}else{
				mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,Context.MODE_PRIVATE).edit().putBoolean(LOGINED, false).commit();
			}
			mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,Context.MODE_PRIVATE).edit().putString(ACCESSTOKEN, getAccessToken() ).commit();				
			Log.i("ThumbrSDK","ID: "+obj.getmID());
			Log.i("ThumbrSDK","Username: "+obj.getmUserName());



			//			if((obj.getmID() != "" && obj.getmUserName()!="") 
			//					&& (gamesettings.getString("userCreated", "") == "" || gamesettings.getString("userCreated", "") != obj.getmUserName()) ){
			//
			//				Log.i("ThumbrSDK","Creating player (score api)");
			//				this.createPlayer();
			//			}		
		}

		return obj;


	}



	//SCORE METHODS

	public void send (Map<String, String> params, String method) {
		SharedPreferences gamesettings = mContext.getSharedPreferences("ThumbrScoreSettings", Context.MODE_PRIVATE);        

		//LOCAL STORAGE
		if(method.equals("editPlayer") || method.equals("updatePlayerField") || method.equals("createScore")){
			Map<String, String> map = params;
			if(map != null){
				for (Entry<String, String> entry : map.entrySet()) {
					//store it
					String newkey = "";
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();

					if(key.equals("bonus")){newkey="assets:bonus_value";}
					else if(key.equals("gold")){newkey="assets:gold_value";}
					else if(key.equals("money")){newkey="assets:money_value";}
					else if(key.equals("kills")){newkey="assets:kills_value";}
					else if(key.equals("lives")){newkey="assets:lives_value";}
					else if(key.equals("xp")){newkey="assets:xp_value";}
					else if(key.equals("energy")){newkey="assets:energy_value";}


					try{
						if(newkey.equals("")==false){
							Log.i("ThumbrSDK","newkey: "+newkey);
							gamesettings.edit().putString(newkey, value).commit();
						}
					}catch(Exception e){
						Log.i("ThumbrSDK","Error storing locally");
					}
				}    
			}
		}

		//SERVER STORAGE
		Log.i("ThumbrSDK",method.toString()+": "+params.toString());
		long timestamp = System.currentTimeMillis();

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		List<NameValuePair> returnlist = new ArrayList<NameValuePair>(2);

		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
		String score_game_id = settings.getString("score_game_id", "");


		Log.i("ThumbrSDK","input parameters: "+params.toString());
		String URL = "http://cliq.twimmer.com/score/score_proxy.php?game_id="+score_game_id; 	       

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("method", method));
		nameValuePairs.add(new BasicNameValuePair("action", "curl_request"));
		nameValuePairs.add(new BasicNameValuePair("game_id", score_game_id));
		nameValuePairs.add(new BasicNameValuePair("response", "JSON"));
		nameValuePairs.add(new BasicNameValuePair("platform", "Android"));

		String username=mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).getString("Username", "");
		String ID=mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).getString("ID", "");

		if(username == "" && ID == ""){
			returnlist.add(new BasicNameValuePair("error", "User is not logged in" ));
		}
		else{
			Log.i("ThumbrSDK",username+" / "+md5(ID));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", md5(ID)));

			Map<String, String> map = params;
			if(map != null){
				for (Entry<String, String> entry : map.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}    
			}
			String result = new String();            result = "";

			InputStream is = null;
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response1 = httpclient.execute(httppost);
				HttpEntity entity = response1.getEntity();
				is = entity.getContent();

			}catch (Exception e)
			{	if(method.equals("edit") || method.equals("create") || method.equals("update")){
				storeForLater(params,method,timestamp);
			}
			Log.e("ThumbrSDK", "Error in http connection "+e.toString());

			}

			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				is.close();

				result = sb.toString();

				if(result.substring(0,1).trim().contains("[")){
					result = result.substring(0, result.length() - 1).substring(1);//remove some pesky brackets; Scoreoid, Y U NO CONSEQUENT?
				}

				Log.i("ThumbrSDK","Raw JSON return: "+result);
				//parse JSON 
				JSONObject jObject = new JSONObject(result.trim());

				@SuppressWarnings("unchecked")
				Iterator<String> ObjectKeys = jObject.keys();

				while(ObjectKeys.hasNext()){
					String ObjectKey = ObjectKeys.next();

					if(ObjectKey.toString().equals("error") || ObjectKey.toString().equals("success")){
						//ERROR OR SUCCESS (WITHOUT ANY OTHER ARGUMENTS)
						returnlist.add(new BasicNameValuePair(ObjectKey, jObject.get(ObjectKey).toString() ));
						if(method == "createPlayer" && ObjectKey.toString().equals("success")){
							String currentUser = mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).getString("Username", "");
							gamesettings.edit().putString("userCreated", currentUser).commit();
							Log.i("ThumbrSDK","Player is created (score api)");
						}
					}
					else{
						JSONObject gameObject = jObject.getJSONObject(ObjectKey);

						@SuppressWarnings("unchecked")
						Iterator<String> keys = gameObject.keys();
						while(keys.hasNext()){
							String key = keys.next().toString();
							String value = gameObject.get(key).toString(); 
							returnlist.add(new BasicNameValuePair(key, value));
						}
					}    	

					for (NameValuePair temp : returnlist) {
						String key = temp.getName();
						String value = temp.getValue();
						gamesettings.edit().putString(key, value).commit();
					}    	    
				}
			}

			catch(Exception e){
				storeForLater(params,method,timestamp);
				Log.e("ThumbrSDK","Parse error: "+e.toString());
			}
			//Log.i("ThumbrSDK","return list: "+returnlist.toString());
		}
		onScoreSavedListener.onScoreSaved(returnlist);
		return;
	}

	//Store score calls that cannot be delivered
	public void storeForLater(Map<String, String> params, String method, Long timestamp){
		if(params != null){    	
			SharedPreferences prefs = mContext.getSharedPreferences("storedGameActions", Context.MODE_PRIVATE);
			String theJson="{\"method\":\""+method.toString()+"\",";
			if(params != null){
				for (Entry<String, String> entry : params.entrySet()) {
					theJson+="\""+entry.getKey().toString()+"\":\""+entry.getValue().toString()+"\","; 
				}
			}	
			theJson=theJson.substring(0, theJson.length() - 1);
			theJson+="}";
			prefs.edit().putString(timestamp.toString(), theJson).commit();
			Log.i("ThumbrSDK","Stored score request for later");
		}
	}

	//Synchronize score calls that could not be delivered before
	public void synchronize(){
		SharedPreferences storedGameActions = mContext.getSharedPreferences("storedGameActions", Context.MODE_PRIVATE);
		mContext.getSharedPreferences("storedGameActions", Context.MODE_PRIVATE).edit().clear().commit();//clear all prefs

		Map<String, ?> allStoredGameActions = storedGameActions.getAll();
		if(allStoredGameActions != null){
			for (Entry<String, ?> entry : allStoredGameActions.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().toString();     	        
				Log.i("ThumbrSDK",key+":"+value);
				try{
					JSONObject jObject = new JSONObject(value);
					Map<String,String> map = new HashMap<String,String>();

					@SuppressWarnings("unchecked")
					Iterator<String> iter = jObject.keys();
					while(iter.hasNext()){
						String key2 = iter.next();
						String value2 = jObject.get(key2).toString();
						map.put(key2,value2);
					}
					String method=jObject.get("method").toString();
					Log.i("ThumbrSDK","Resent score request");
					send(map,method);

				}catch(Exception e){
					Log.i("ThumbrSDK","Cannot convert to Json Object");
				}     	    	
			}
		}

	}


	public void initScores(){
		//mContext.getSharedPreferences("ThumbrScoreSettings", Context.MODE_PRIVATE).edit().clear().commit();//clear all prefs
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrScoreSettings", Context.MODE_PRIVATE);
		if(settings.getString("initialized", "").equals("1")==false){
			try {
				String json = this.ReadFromfile("gamesettings.json",mContext);
				JSONObject jObject = new JSONObject(json);
				//INIT SCORES
				JSONObject scores = (JSONObject) jObject.get("scores");

				String enable_scores;
				if(scores.getString("enable_scores").equals("1")){enable_scores="1";}else{enable_scores="0";}
				settings.edit().putString("enable_scores", enable_scores).commit();

				String my_scores_shown;
				if(scores.getString("my_scores_shown").equals("1")){my_scores_shown="1";}else{my_scores_shown="0";}
				settings.edit().putString("my_scores_shown", my_scores_shown).commit();			

				String current_score_default_value;
				if(scores.getString("current_score_default_value").equals("1")){current_score_default_value="1";}else{current_score_default_value="0";}
				settings.edit().putString("current_score_default_value", current_score_default_value).commit();

				String my_record_default_value;
				if(scores.getString("my_record_default_value").equals("1")){my_record_default_value="1";}else{my_record_default_value="0";}
				settings.edit().putString("my_record_default_value", my_record_default_value).commit();

				String high_scores_shown;
				if(scores.getString("high_scores_shown").equals("1")){high_scores_shown="1";}else{high_scores_shown="0";}
				settings.edit().putString("high_scores_shown", high_scores_shown).commit();

				JSONArray all_scores = (JSONArray) scores.getJSONObject("high_scores").getJSONArray("high_score");
				for (int i = 0; i < all_scores.length(); i++) {
					JSONObject row = all_scores.getJSONObject(i);
					String index = row.get("index").toString();
					String value = row.get("value").toString();
					String name = row.get("name").toString();
					settings.edit().putString("high_scores:"+index, value).commit();
					settings.edit().putString("high_score_names:"+index, name).commit();
				}

				//INIT LEVELS
				JSONObject levels = (JSONObject) jObject.get("levels");
				String enable_levels;
				if(levels.getString("enable_levels").equals("1")){enable_levels="1";}else{enable_levels="0";}
				settings.edit().putString("enable_levels", enable_levels).commit();
				JSONArray all_levels = (JSONArray) levels.getJSONObject("levels").getJSONArray("level");
				for (int i = 0; i < all_levels.length(); i++) {
					JSONObject row = all_levels.getJSONObject(i);
					String index = row.get("index").toString();
					String name = row.get("name").toString();
					String unlocked = row.get("unlocked").toString();
					settings.edit().putString("levels:"+index, name).commit();
					settings.edit().putString("levels_unlocked:"+index, unlocked).commit();
				}

				//INIT GOALS
				JSONObject goals = (JSONObject) jObject.get("goals");
				String enable_goals;
				if(goals.getString("enable_goals").equals("1")){enable_goals="1";}else{enable_goals="0";}
				settings.edit().putString("enable_goals", enable_goals).commit();
				JSONArray all_goals = (JSONArray) goals.getJSONObject("goals").getJSONArray("goal");
				for (int i = 0; i < all_goals.length(); i++) {
					JSONObject row = all_goals.getJSONObject(i);
					String index = row.get("index").toString();
					String name = row.get("name").toString();
					String unlocked = row.get("unlocked").toString();
					settings.edit().putString("goals:"+index, name).commit();
					settings.edit().putString("goals_unlocked:"+index, unlocked).commit();
				}

				//INIT ASSETS
				JSONObject assets = (JSONObject) jObject.get("assets");
				String enable_assets;
				if(assets.getString("enable_assets").equals("1")){enable_assets="1";}else{enable_assets="0";}
				settings.edit().putString("enable_assets", enable_assets).commit();

				JSONObject all_assets = assets.getJSONObject("assets");

				@SuppressWarnings("unchecked")
				Iterator<String> keys = all_assets.keys();
				while(keys.hasNext()){
					String key = keys.next().toString();
					String value = all_assets.get(key).toString(); 
					settings.edit().putString("assets:"+key, value).commit();
				}

				//INIT INVENTORY
				JSONObject inventory = (JSONObject) jObject.get("inventory");
				String enable_inventory;
				if(inventory.getString("enable_inventory").equals("1")){enable_inventory="1";}else{enable_inventory="0";}
				settings.edit().putString("enable_inventory", enable_inventory).commit();

				JSONArray all_inventory = (JSONArray) inventory.getJSONObject("inventory").getJSONArray("item");
				for (int i = 0; i < all_inventory.length(); i++) {
					JSONObject row = all_inventory.getJSONObject(i);
					String index = row.get("index").toString();
					String name = row.get("name").toString();
					String unlocked = row.get("unlocked").toString();
					settings.edit().putString("inventory:"+index, name).commit();
					settings.edit().putString("inventory_unlocked:"+index, unlocked).commit();
				}

				settings.edit().putString("initialized", "1").commit();

				//Loop through settings
				Map<String, ?> items = settings.getAll();
				for(String s : items.keySet()){
					Log.i("ThumbrSDK",s+" = "+items.get(s).toString());
				}


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




	//ALL THE PUBLIC SCORE METHODS

	/*
	 * show Scores
	 */
	public void showScores(){
		String query="";

		SharedPreferences settings = mContext.getSharedPreferences("ThumbrScoreSettings", Context.MODE_PRIVATE);

		TreeMap<String, ?> items = new TreeMap<String, Object>(settings.getAll());

		for(String s : items.keySet()){
			String k=s;//the key
			String v=items.get(s).toString();//the value

			if(k.equals("enable_levels")){query+="i[]=levels&";}
			else if(k.equals("enable_scores")){query+="i[]=scores&";}
			else if(k.equals("enable_goals")){query+="i[]=goals&";}
			else if(k.equals("enable_assets")){query+="i[]=assets&";}
			else if(k.equals("enable_inventory")){query+="i[]=inventory&";}

			else if(k.contains("assets:") && k.contains("_value")){
				String id=k.replace("assets:","").replace("_value","");

				if(id.equals("bonus")){query+="ab="+v+"&";}
				if(id.equals("gold")){query+="ag="+v+"&";}
				if(id.equals("money")){query+="am="+v+"&";}
				if(id.equals("kills")){query+="ak="+v+"&";}
				if(id.equals("lives")){query+="al="+v+"&";}
				if(id.equals("xp")){query+="ax="+v+"&";}
				if(id.equals("energy")){query+="ae="+v+"&";}
			}
			else if(k.contains("assets:") && k.contains("_shown") && v.equals("1")){
				String id=k.replace("assets:","").replace("_shown","");

				if(id.equals("bonus")){query+="a[]="+id+"&";}
				if(id.equals("gold")){query+="a[]="+id+"&";}
				if(id.equals("money")){query+="a[]="+id+"&";}
				if(id.equals("kills")){query+="a[]="+id+"&";}
				if(id.equals("lives")){query+="a[]="+id+"&";}
				if(id.equals("xp")){query+="a[]="+id+"&";}
				if(id.equals("energy")){query+="a[]="+id+"&";}				
			}
			else if(k.contains("levels:")){
				String index= k.replace("levels:","").replaceFirst("^0+(?!$)", ""); 
				String unlocked=items.get("levels_unlocked:"+k.replace("levels:","")).toString();
				query+="l[]="+index+","+unlocked+","+v+"&";
			}
			else if(k.contains("high_scores:")){String index= k.replace("high_scores:","").replaceFirst("^0+(?!$)", ""); query+="s"+index+"="+v+"&";}
			else if(k.contains("high_score_names:")){String index= k.replace("high_score_names:","").replaceFirst("^0+(?!$)", ""); query+="su"+index+"="+v+"&";}

			else if(k.contains("inventory:")){
				String index= k.replace("inventory:","").replaceFirst("^0+(?!$)", ""); 
				String unlocked=items.get("inventory_unlocked:"+k.replace("inventory:","")).toString();
				query+="n[]="+index+","+unlocked+","+v+"&";
			}
			else if(k.contains("goals:")){
				String index= k.replace("goals:","").replaceFirst("^0+(?!$)", ""); 
				String unlocked=items.get("goals_unlocked:"+k.replace("goals:","")).toString();
				query+="g[]="+index+","+unlocked+","+v+"&";
			}
		}

		ShowDialog("http://twimmer.com/scoreoid?rand="+rand()+"&"+query);
	}

	/*
	 ** createPlayer**;
	 ** method creates the player, based on the thumbr stored settings.
	 **/
	public void createPlayer(){
		Map<String, String> params = new HashMap<String, String>();
		send(params,"createPlayer");
	}

	/*
	 ** getNotification**;
	 ** method lets you pull your game's in game notifications.
	 **/
	public void getNotification()	
	{	
		Map<String, String> scoreParams = new HashMap<String, String>();

		String method = "getNotification";
		send(scoreParams,method);
	}


	/*
	 ** getGameTotal**;
	 ** gets the total for the following game field's bonus, gold, money, kills, lifes, time_played and unlocked_levels.
	 *  REQUIRED: field                         String Value, (bonus, gold, money, kills, lifes, time_played, unlocked_levels)
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void getGameTotal(String field,Map<String, String>scoreParams)	
	{	if(field == null){return;}
	String method = "getGameTotal";
	scoreParams.put("field",field);
	send(scoreParams,method);
	}


	/*
	 ** getGameLowest**;
	 ** gets the lowest value for the following game field's bonus, gold, money, kills, lifes, time_played and unlocked_levels.
	 *  REQUIRED: field                         String Value, (bonus, gold, money, kills, lifes, time_played, unlocked_levels)
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void getGameLowest(String field,Map<String, String>scoreParams)	
	{	if(field == null){return;}
	String method = "getGameLowest";
	scoreParams.put("field",field);
	send(scoreParams,method);
	}


	/*
	 ** getGameTop**;
	 ** gets the top value for the following game field's bonus, gold, money, kills, lifes, time_played and unlocked_levels.
	 *  REQUIRED: field                         String Value, (bonus, gold, money, kills, lifes, time_played, unlocked_levels)
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void getGameTop(String field,Map<String, String>scoreParams)	
	{	if(field == null){return;}
	String method = "getGameTop";
	scoreParams.put("field",field);
	send(scoreParams,method);
	}


	/*
	 ** getGameAverage**;
	 ** gets the average for the following game field's bonus, gold, money, kills, lifes, time_played and unlocked_levels.
	 *  REQUIRED: field                         String Value, (bonus, gold, money, kills, lifes, time_played, unlocked_levels)
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void getGameAverage(String field,Map<String, String>scoreParams)	
	{	if(field == null){return;}
	String method = "getGameAverage";
	scoreParams.put("field",field);
	send(scoreParams,method);
	}


	/*
	 ** getPlayers**;
	 ** API method lets you get all the players for a specif game.getGameField
	 *  OPTIONAL: order_by                      String Value, (date or score)
	 *  OPTIONAL: order                         String Value, asc or desc
	 *  OPTIONAL: limit                         Number Value, the limit, "20" retrieves rows 1 - 20 | "10,20" retrieves 20 scores starting from the 10th
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      String Value, needs to match the string value that was used when creating the player
	 **/
	public void getPlayers(Map<String, String>scoreParams)	
	{	
		String method = "getPlayers";
		send(scoreParams,method);
	}


	/*
	 ** getGameField**;
	 ** method lets you pull a specific field from your game info.
	 *  OPTIONAL: field                         name,short_description,description,game_type,version,levels,platform,play_url,website_url,created,updated,player_count,scores_count,locked,status,
	 **/
	public void getGameField(Map<String, String>scoreParams)	
	{	
		String method = "getGameField";
		send(scoreParams,method);
	}


	/*
	 ** getGame**;
	 ** method lets you pull all your game information.
	 **/
	public void getGame()	
	{	
		Map<String, String> scoreParams = new HashMap<String, String>();

		String method = "getGame";
		send(scoreParams,method);
	}


	/*
	 ** getPlayerScores**;
	 ** method lets you pull all the scores for a player.
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: difficulty                    Integer Value as string, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void getPlayerScores(Map<String, String>scoreParams)	
	{	
		String method = "getPlayerScores";
		send(scoreParams,method);
	}


	/*
	 ** getPlayer**;
	 ** method Check if player exists and returns the player information.
	 **/
	public void getPlayer()	
	{	
		Map<String, String> scoreParams = new HashMap<String, String>();

		String method = "getPlayer";
		send(scoreParams,method);
	}


	/*
	 ** editPlayer**;
	 ** method lets you edit your player information.
	 *  OPTIONAL: unique_id                     Integer Value as string,
	 *  OPTIONAL: first_name                    The players first name [String]
	 *  OPTIONAL: last_name                     The players last name [String]
	 *  OPTIONAL: created                       The date the player was created calculated by Scoreoid [YYYY-MM-DD hh:mm:ss]
	 *  OPTIONAL: updated                       The last time the player was updated calculated by Scoreoid [YYYY-MM-DD hh:mm:ss]
	 *  OPTIONAL: bonus                         The players bonus [Integer as string]
	 *  OPTIONAL: achievements                  The players achievements [String, comma-separated]
	 *  OPTIONAL: best_score                    The players best score calculated by Scoreoid [Integer as string]
	 *  OPTIONAL: gold                          The players gold [Integer as string]
	 *  OPTIONAL: money                         The players money [Integer as string]
	 *  OPTIONAL: kills                         The players kills [Integer as string]
	 *  OPTIONAL: lives                         The players lives [Integer as string]
	 *  OPTIONAL: time_played                   The time the player played [Integer as string]
	 *  OPTIONAL: unlocked_levels               The players unlocked levels [Integer as string]
	 *  OPTIONAL: unlocked_items                The players unlocked items [String, comma-separated]
	 *  OPTIONAL: inventory                     The players inventory [String, comma-separated]
	 *  OPTIONAL: last_level                    The players last level [Integer as string]
	 *  OPTIONAL: current_level                 The players current level [Integer as string]
	 *  OPTIONAL: current_time                  The players current time [Integer as string]
	 *  OPTIONAL: current_bonus                 The players current bonus [Integer as string]
	 *  OPTIONAL: current_kills                 The players current kills [Integer as string]
	 *  OPTIONAL: current_achievements          The players current achievements [String, comma-separated]
	 *  OPTIONAL: current_gold                  The players current gold [Integer as string]
	 *  OPTIONAL: current_unlocked_levels       The players current unlocked levels [Integer as string]
	 *  OPTIONAL: current_unlocked_items        The players current unlocked items [String, comma-separated]
	 *  OPTIONAL: current_lifes                 The players current lifes [Integer as string]
	 *  OPTIONAL: xp                            The players XP [Integer as string]
	 *  OPTIONAL: energy                        The players energy [Integer as string]
	 *  OPTIONAL: boost                         The players energy [Integer as string]
	 *  OPTIONAL: latitude                      The players GPS latitude [Integer as string]
	 *  OPTIONAL: longitude                     The players GPS longitude [Integer as string]
	 *  OPTIONAL: game_state                    The players game state [String]
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void editPlayer(Map<String, String>scoreParams)	
	{	
		String method = "editPlayer";
		send(scoreParams,method);
	}


	/*
	 ** countPlayers**;
	 ** method lets you count all your players.
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 **/
	public void countPlayers(Map<String, String>scoreParams)	
	{	
		String method = "countPlayers";
		send(scoreParams,method);
	}


	/*
	 ** updatePlayerField**;
	 ** method lets you update your player field's.
	 *  REQUIRED: field                         unique_id,first_name,last_name,created,updated,bonus,achievements,gold,money,kills,lives,time_played,unlocked_levels,unlocked_items,inventory,last_level,current_level,current_time,current_bonus,current_kills,current_achievements,current_gold,current_unlocked_levels,current_unlocked_items,current_lifes,xp,energy,boost,latitude,longitude,game_state,platform,
	 *  REQUIRED: value                         String Value, the field value
	 **/
	public void updatePlayerField(String field,String value,Map<String, String>scoreParams)	
	{	if(field == null){return;}if(value == null){return;}
	String method = "updatePlayerField";
	scoreParams.put("field",field);
	scoreParams.put("value",value);
	send(scoreParams,method);
	}


	/*
	 ** countBestScores**;
	 ** method lets you count all your game best scores.
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 *  OPTIONAL: difficulty                    Integer Value as String, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void countBestScores(Map<String, String>scoreParams)	
	{	
		String method = "countBestScores";
		send(scoreParams,method);
	}


	/*
	 ** getAverageScore**;
	 ** method lets you get all your game average scores.
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 *  OPTIONAL: difficulty                    Integer Value as STring, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void getAverageScore(Map<String, String>scoreParams)	
	{	
		String method = "getAverageScore";
		send(scoreParams,method);
	}


	/*
	 ** getBestScores**;
	 ** method lets you get all your games best scores.
	 *  OPTIONAL: order_by                      String Value, (date or score)
	 *  OPTIONAL: order                         String Value, asc or desc
	 *  OPTIONAL: limit                         Number Value, the limit, "20" retrieves rows 1 - 20 | "10,20" retrieves 20 scores starting from the 10th
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 *  OPTIONAL: difficulty                    Integer Value as String, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void getBestScores(Map<String, String>scoreParams)	
	{	
		String method = "getBestScores";
		send(scoreParams,method);
	}


	/*
	 ** countScores**;
	 ** method lets you count all your game scores.
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 *  OPTIONAL: difficulty                    Integer Value as String, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void countScores(Map<String, String>scoreParams)	
	{	
		String method = "countScores";
		send(scoreParams,method);
	}


	/*
	 ** getScores**;
	 ** method lets you pull all your game scores.
	 *  OPTIONAL: order_by                      String Value, (date or score)
	 *  OPTIONAL: order                         String Value, asc or desc
	 *  OPTIONAL: limit                         Number Value, the limit, "20" retrieves rows 1 - 20 | "10,20" retrieves 20 scores starting from the 10th
	 *  OPTIONAL: start_date                    String Value, YYY-MM-DD format
	 *  OPTIONAL: end_date                      String Value, YYY-MM-DD format
	 *  OPTIONAL: platform                      The players platform needs to match the string value that was used when creating the player
	 *  OPTIONAL: difficulty                    Integer Value as String, between 1 to 10 (don't use 0 as it's the default value)
	 **/
	public void getScores(Map<String, String>scoreParams)	
	{	
		String method = "getScores";
		send(scoreParams,method);
	}


	/*
	 ** createScore**;
	 ** method lets you create a user score.
	 *  REQUIRED: score                         Integer Value as String,
	 *  OPTIONAL: platform                      String Value,
	 *  OPTIONAL: unique_id                     Integer Value as String,
	 *  OPTIONAL: difficulty                    Integer Value as String, between 1 to 10 (don\t use 0 as it's the default value)
	 **/
	public void createScore(String score,Map<String, String>scoreParams)	
	{	if(score == null){return;}
	String method = "createScore";
	scoreParams.put("score",score);
	send(scoreParams,method);
	}

	//Helper methods
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

	public void pause(){
		Log.i("MRAID","Putting ads to sleep");
		MadsAdView.sleepSession();
	}

	public void resume(){
		Log.i("MRAID","Resuming ads");		
		MadsAdView.resume();
	}

	public boolean isOnline() {Log.i("ThumbrSDK","isOnline");
	ConnectivityManager cm =
			(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();

	if (netInfo != null && netInfo.isConnectedOrConnecting() && mContext.getSharedPreferences("ThumbrSettings",Context.MODE_PRIVATE).getBoolean("isOnline",false) == true) {
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

			SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
			String sid = settings.getString("sid","");
			String url = "http://ads.thumbr.com/adserver/?getAdSettings=1&debug=0&sid="+sid;
			Log.i("ThumbrSDK","Get adsettings from: "+url);

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

					Log.i("ThumbrSDK","INLINE ADS UPDATE TIME INTERVAL: "+RowData[0]);
					Log.i("ThumbrSDK","INTERSTITIAL ADS SHOW CLOSE BUTTON AFER N SECONDS: "+RowData[1]);
					Log.i("ThumbrSDK","HIDE THUMBR WINDOW CLOSEBUTTON: "+RowData[2]);
				}
				catch (IOException ex) {
					// handle exception
					Log.i("ThumbrSDK","Cannot read settings");
				}


			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} catch (Exception e) {
			Log.i("THUMBR","Cannot update Ads time interval: "+e);
		}
	}


	public String getAdSetting(String adType,String dataType){
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);
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
		Display display = wm.getDefaultDisplay();


		//		int adHeight = display.getHeight();
		//		int adWidth = display.getWidth();
		//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ad_view.getLayoutParams();
		//		params.height = adHeight;
		//		params.width = adWidth;
		//		ad_view.setLayoutParams(params);

		final SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("interstitial","secret"), getAdSetting("interstitial","zoneid"));
		WebSettings aWS = adView.getSettings();
		aWS.setJavaScriptCanOpenWindowsAutomatically(true);
		aWS.setJavaScriptEnabled(true);

		adView.addJavascriptInterface(this, "CUSTOMANDROID");		

		adView.setAdserverURL("http://ads.thumbr.com/adserver/");

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
			map.put("thumbr_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));		
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
			map.put("sig",getHash(theKey, settings.getString("thumbr_id", "")));
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
				if(UnityPlayer.currentActivity != null ){
					UnityPlugin.interstitialClosed();
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
				adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.thumbr.com/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s)" +  "})()");
				adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {var oldVersion = mraid.close;mraid.close = function() {var result = oldVersion.apply(this, arguments);CUSTOMANDROID.closeListen(); return result;}};" +  "})()");

				//after 1 second do stuff
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {if(mraid.getExpandProperties().useCustomClose == true){CUSTOMANDROID.hideNativeCloseButton();}else{CUSTOMANDROID.showNativeCloseButton();}};" +  "})()");
						progress.dismiss();
						ad_view.setBackgroundColor(Color.parseColor("#AA000000"));
					}
				}, 1000);

				//after x seconds, show the close button (if not cancelled by mraid CustomButton)
				final Handler handler2 = new Handler();
				handler2.postDelayed(new Runnable() {
					@Override
					public void run() {
						adView.loadUrl("javascript:(function() {" + "if (typeof mraid !== 'undefined') {if(mraid.getExpandProperties().useCustomClose == true){CUSTOMANDROID.hideNativeCloseButton();}else{CUSTOMANDROID.showNativeCloseButton();}};" +  "})()");

						progress.dismiss();
						ad_view.setBackgroundColor(Color.TRANSPARENT);

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
								Log.i("ThumbrSDK","Close the interstitial.");
								if(mListener!=null) {mListener.onEvent();
								}
								if(UnityPlayer.currentActivity != null ){
									UnityPlugin.interstitialClosed();
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
				if(UnityPlayer.currentActivity != null ){
					UnityPlugin.interstitialClosed();
				}
			}

			@Override
			public void noad(AdView sender) {
				Log.d("MRAID", "The ad server responded by telling us no ad is available");
				progress.dismiss();
				if(mListener!=null) {mListener.onEvent();

				}
				if(UnityPlayer.currentActivity != null ){
					UnityPlugin.interstitialClosed();
				}
			}
		});		

	}

	public void adOverlay(final RelativeLayout ad_view){
		//ad_view.removeAllViews();

		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("overlay","secret"), getAdSetting("overlay","zoneid"));
		adView.setAdserverURL("http://ads.thumbr.com/adserver/");
		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setId(1);
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
			map.put("thumbr_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));		
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
			map.put("sig",getHash(theKey, settings.getString("thumbr_id", "")));
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
				adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.thumbr.com/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s)" +  "})()");
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

	@TargetApi(Build.VERSION_CODES.BASE)
	@SuppressLint("NewApi")
	public void adInline(final RelativeLayout ad_view)
	{

		ad_view.removeAllViews();		
		SharedPreferences settings = mContext.getSharedPreferences("ThumbrSettings", Context.MODE_PRIVATE);

		final MadsAdView adView = new MadsAdView(mContext, getAdSetting("inline","secret"), getAdSetting("inline","zoneid"));

		adView.setAdserverURL("http://ads.thumbr.com/adserver/");
		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setId(1);
		adView.setInternalBrowser(true);
		adView.setContentAlignment(true);
		adView.setLocationDetection(true);
		adView.setMadsAdType("inline");
		if( settings.getInt("updateTimeIntervalOverride",0) >= 0 ){
			adView.setUpdateTime(settings.getInt("updateTimeIntervalOverride",0));
			Log.i("ThumbrSDK","update Time Interval Override is used: "+settings.getInt("updateTimeIntervalOverride",0));
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
			map.put("thumbr_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));
			map.put("profile_id",URLEncoder.encode(settings.getString("thumbr_id",""), "UTF-8"));		
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
			map.put("sig",getHash(theKey, settings.getString("thumbr_id", "")));
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
					adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.thumbr.com/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s)" +  "})()");
					adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://m-dev.thumbr.com/adserver/js/mraid.js\";document.getElementsByTagName('body') [0].appendChild(s)" +  "})()");
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
							public void run() {
								WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
								Display display = wm.getDefaultDisplay();
								adView.loadUrl("javascript:(function() {" + "var s = document.createElement(\"script\");s.type = \"text/javascript\";s.src = \"http://ads.thumbr.com/adserver/js/OrmmaAdController.js\";document.getElementsByTagName('body') [0].appendChild(s)" +  "})()");

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
		if(isOnline()){
			getAdSettings();
		}
	}
	@Override
	public void event(AdView arg0, String arg1, String arg2) {

		// TODO Auto-generated method stub
	}

	public String closeListen(){
		//MADS SDK does not properly listen to the mraid.close() function, so we're listening to it and closing and removing the adView ourselves.
		Log.i("ThumbrSDK","closeListen was called");
		try {((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				if(mListener!=null) {mListener.onEvent();

				}
				if(UnityPlayer.currentActivity != null ){
					UnityPlugin.interstitialClosed();
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
		Log.i("ThumbrSDK","hideNativeCloseButton was called");
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
		Log.i("ThumbrSDK","showNativeCloseButton was called");
		try {

			this.closeButtonClosed = false;
			//closeButton.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}	


}

