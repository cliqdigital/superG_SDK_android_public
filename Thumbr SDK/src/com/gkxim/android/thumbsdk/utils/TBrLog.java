package com.gkxim.android.thumbsdk.utils;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gkxim.android.thumbsdk.R;

/**
 * 
 */

/**
 * @author Timon Trinh
 * 
 */
public class TBrLog {

	private static final String LOG_TAG = "ThumbrSDK";

	public static final int TMB_LOGTYPE_INFO = 0;
	public static final int TMB_LOGTYPE_DEBUG = 1;
	public static final int TMB_LOGTYPE_WARNING = 2;
	public static final int TMB_LOGTYPE_ERROR = 3;
	public static boolean bDebugging = true;
	public static boolean bToastDebug = true;
	private static Toast mToast = null;
	private static Context mContext = null;
	private static File mLogFile;
	private static String mPathFile;
	private static final String GKIM_LOGFILE = "ThumbrSDK.log";
	private static TextView mMessage;

	public static void fl(int mode, String message) {
		if (bDebugging) {
			switch (mode) {
			case 3:
				Log.e(LOG_TAG, message);
				break;
			case 2:
				Log.w(LOG_TAG, message);
				break;
			case 1:
				Log.i(LOG_TAG, message);
				break;
			default:
				Log.d(LOG_TAG, message);
				break;
			}
			if (mLogFile == null) {
//				try {
//					mPathFile = Environment
//							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//							+ "/" + GKIM_LOGFILE;
//					Log.i(LOG_TAG, "Writing log into: " + mPathFile);
//					mLogFile = new File(mPathFile);
//					if (mLogFile != null) {
//						BufferedWriter bw = new BufferedWriter(new FileWriter(
//								mLogFile, true));
//						if (bw != null) {
//							bw.write(message);
//						}
//						bw.close();
//					}
//				} catch (IOException e) {
//					// TODO: Silent
//				}
			}
		}

	}

	public static void l(int type, String message) {
		if (message == null || bDebugging == false) {
			return;
		}

		switch (type) {
		case TMB_LOGTYPE_DEBUG:
			Log.d(LOG_TAG, message);
			break;
		case TMB_LOGTYPE_WARNING:
			Log.w(LOG_TAG, message);
			break;
		case TMB_LOGTYPE_ERROR:
			Log.e(LOG_TAG, message);
			break;
		default:
			Log.i(LOG_TAG, message);
			break;
		}

	}

	public static void t(Context context, String message) {
		if (context == null || !bToastDebug) {
			return;
		}
		if (mToast == null || mContext != context) {
			mMessage = null;
			mContext = context;
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewGroup vg = (ViewGroup) inflater.inflate(
					R.layout.thumbr_layout_toast, null);
			mMessage = (TextView) vg.findViewById(R.id.tbrlay_toast_message);

			mToast = new Toast(mContext);
			mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			mToast.setView(vg);
			
		}
		if (mMessage != null) {
			mMessage.setText(message);
		}
		mToast.show();
	}

	public static void lt(Context context, int type, String message) {
		l(type, message);
		t(context, message);
	}
	/**
	 * class for taking an error connection message
	 *@TYPE_1: No network connection 
	 *@TYPE_2: No internet connection is available
	 *@TYPE_3: Please turn on the mobile or wi-fi connections
	 *@TYPE_4: Cannot connect to Internet. Please check your connection settings and try again
	 */
	public static class SystemErrorMessage{
		/**
		 *No network connection
		 */
		public static String TYPE_1 = "No network connection";
		/**
		 * No internet connection is available
		 */
		public static String TYPE_2 = "No internet connection is available"; 
		/**
		 * Please turn on the mobile or wi-fi connections
		 */
		public static String TYPE_3= "Please turn on the mobile or wi-fi connections";
		/**
		 * Cannot connect to Internet. Please check your connection settings and try again
		 */
		public static String TYPE_4="Cannot connect to Internet.\nPlease check your connection settings and try again";
	}
	static public String createThumbrID(Activity act) {
		String androidId = "androidId_Null";
		try {
			androidId = Secure.getString(act.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {
			Log.e("AppConfig", e.getMessage());
		}
		if (androidId == null)
			androidId = "androidId_Null";
		String tmDeviceId = "DeviceId_Null";
		String package_name=act.getPackageName();
		String package_Thumbr="com.gkxim.android.thumbsdk";
		try {
			TelephonyManager tm = (TelephonyManager) act.getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			tmDeviceId = tm.getDeviceId();
		} catch (Exception e) {
			Log.e("AppConfig", e.getMessage());
		}
		if (tmDeviceId == null)
			tmDeviceId = "DeviceId_Null";
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDeviceId.hashCode() << 32)
						|package_Thumbr.hashCode()|package_name.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}
	
	static public String createGameID(Activity act) {
		String androidId = "androidId_Null";
		try {
			androidId = Secure.getString(act.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {
			Log.e("AppConfig", e.getMessage());
		}
		if (androidId == null)
			androidId = "androidId_Null";
		String tmDeviceId = "DeviceId_Null";
		String package_name=act.getPackageName();
		try {
			TelephonyManager tm = (TelephonyManager) act.getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			tmDeviceId = tm.getDeviceId();
		} catch (Exception e) {
			Log.e("AppConfig", e.getMessage());
		}
		if (tmDeviceId == null)
			tmDeviceId = "DeviceId_Null";
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDeviceId.hashCode() << 32)
						|package_name.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}


}
