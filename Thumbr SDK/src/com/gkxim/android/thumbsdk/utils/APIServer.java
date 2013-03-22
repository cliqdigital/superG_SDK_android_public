package com.gkxim.android.thumbsdk.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class APIServer {
	public static final String CLIENT_ID = "&client_id=";
	public static final String SID = "&sid=";
	public static final String RESPONSE_TYPE = "&response_type=";
	public static final String URL_SERVER = "http://178.79.146.93/auth/authorize?";
	public static final String STEP = "&step=";
	public static String clientID = "84758475-476574";
	public static String sID = "playnow-test";
	public static String step_SwitchAccount = "switchaccount";
	public static String respone = "token";
	public static String LinkPortal = "http://mobile.thumbr.com/start/?access_token=";
	public static String profileURL = "http://gasp.thumbr.com/api/v1/profile?access_token=";
	public static Context mContext;
//oPZiT3z3VQ-hSIbZySqSkkz1QgNrUD5Y3fsAW7hmDlM
	public APIServer(Context context) {
		mContext = context;
		
	}

	public ProfileObject getProfile(String accToken) {
		// get json
		try {

			ProfileObject prof = new ProfileObject();
			String url = profileURL + accToken;
			JSONHelper parser = new JSONHelper();
			JSONObject jo = parser.getJSONObject(url);
			
			if (jo != null) {
				if (jo.has("id")) {
					prof.setmID(jo.getString("id"));
				}
				if (jo.has("username")) {
					prof.setmUserName(jo.getString("username"));
				}
				if (jo.has("status")) {
					prof.setmStatus(jo.getString("status"));
				}
				if (jo.has("email")) {
					prof.setmEmail(jo.getString("email"));
				}
				if (jo.has("surname")) {
					prof.setmSurname(jo.getString("surname"));
				}
				if (jo.has("gender")) {
					prof.setmGender(jo.getString("gender"));
				}
				if (jo.has("date_of_birth")) {
					prof.setmDOB(jo.getString("date_of_birth"));
				}
				if (jo.has("locale")) {
					prof.setmLocale(jo.getString("locale"));
				}
				if (jo.has("city")) {
					prof.setmCity(jo.getString("city"));
				}
				if (jo.has("address")) {
					prof.setmAddress(jo.getString("address"));
				}
				if (jo.has("zipcode")) {
					prof.setmZipCode(jo.getString("zipcode"));
				}
				if (jo.has("newsletter")) {
					prof.setmNewsLetter(jo.getString("newsletter"));
				}
				if (jo.has("firstname")) {
					prof.setmFirstName(jo.getString("firstname"));
				}
				if (jo.has("msisdn")) {
					prof.setmSisdn(jo.getString("msisdn"));
				}
				if (jo.has("housenr")) {
					prof.setmHousenr(jo.getString("housenr"));
				}
				if (jo.has("message")) {
					prof.setmMessage(jo.getString("message"));
				}
				if (jo.has("code")) {
					prof.setmCode(jo.getString("code"));
				}
				if (jo.has("description")) {
					prof.setmDesscription(jo.getString("description"));
				}
				if (jo.has("httpCode")) {
					prof.setmHttpCode(jo.getString("httpCode"));
					return null;
				}
			
				return prof;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static String getURLRegister(int sid,Activity act) {
		clientID=TBrLog.createThumbrID(act);
//		String url = URL_SERVER + CLIENT_ID + clientID + SID + sid
//				+ RESPONSE_TYPE + respone;
		String url = URL_SERVER + CLIENT_ID + "84758475-476574" + SID + "playnow-test"
				+ RESPONSE_TYPE + respone;
		return url;
	}

	public static String getURLSwitchAccount(int sid,Activity act) {
		clientID=TBrLog.createThumbrID(act);
//		String url = URL_SERVER + CLIENT_ID + clientID + SID + sid
//				+ RESPONSE_TYPE + respone + STEP + step_SwitchAccount;
		String url = URL_SERVER + CLIENT_ID + "84758475-476574" + SID + "playnow-test"
				+ RESPONSE_TYPE + respone+ STEP + step_SwitchAccount;
		return url;
	}

	public static String getURLPrtal()//(String accToken) 
	{
		
		String url = LinkPortal  ;
		return url;
	}
	
	
	
	
	
}
