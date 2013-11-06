/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cliqdigital.supergsdk.utils;

import static com.cliqdigital.supergsdk.utils.EVA.SENDER_ID;
import com.cliqdigital.supergsdk.utils.EVA;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import com.cliqdigital.supergsdk.utils.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cliqdigital.supergsdk.SuperG;
import com.cliqdigital.supergsdk.R;
import com.google.android.gcm.GCMBaseIntentService;


@SuppressLint("HandlerLeak")
@SuppressWarnings({ "unused", "deprecation" })
public class GCMIntentService extends GCMBaseIntentService{


	public GCMIntentService() {
		super(SENDER_ID);
	}

	private static final String TAG = "GCMIntentService";
	private static String toastUp = "no";

	@Override
	protected void onRegistered(Context context, String registrationId) {
		SharedPreferences settings = context.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);							
		settings.edit().putString("registration_id", registrationId).commit();	 
		Log.i(TAG, "Device registered: regId = " + registrationId);
	}

	
	@Override
	protected void onUnregistered(Context context, String arg1) {
		SharedPreferences settings = context.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);							
		settings.edit().putString("registration_id", "").commit();	
	}


	@Override
	protected void onError(Context arg0, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}


	private boolean checkVibrationPermission()
	{
		String permission = "android.permission.VIBRATE";
		int res = getBaseContext().checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);            
	}


	@Override
	public void onMessage(final Context context, final Intent intent)
	{
		
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		if(!pm.isScreenOn()){

			final
			WakeLock mWakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "App");
			mWakeLock.acquire();

			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mWakeLock.release();
				}
			}, 10000);


		}

		//CUSTOM SOUNDS
		NotificationCompat.Builder builder;
		try {
			builder = new NotificationCompat.Builder(context)  
			.setSmallIcon(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon)
			//			.setContentTitle(intent.getStringExtra("title"))  
			.setContentText(intent.getStringExtra("message"));

			if(intent.getExtras().getString("sound")==null || intent.getExtras().getString("sound")=="default"){
				builder.setDefaults(Notification.DEFAULT_SOUND);
			}

			else{
				String sound_name = intent.getExtras().getString("sound");
				if(sound_name != null){
					try {
						int sound = context.getResources().getIdentifier( sound_name , "raw" , context.getPackageName() );

						if (sound != 0){
							builder.setSound(Uri.parse("android.resource://"+context.getPackageName()+"/raw/"+sound_name));
						}
						else{
							builder.setDefaults(Notification.DEFAULT_SOUND);
						}
					}
					catch(Exception e) {
						builder.setDefaults(Notification.DEFAULT_SOUND);
					}
				}else{
					builder.setDefaults(Notification.DEFAULT_SOUND);
				}
			}

			

			//SET VIBRATION
			//Log.i(TAG,"[GCMIntentService] Message Received! " + intent.getStringExtra("message"));
			handleMessage(intent);
			int dot = 50;      // Length of a Morse Code "dot" in milliseconds
			int dash = 100;     // Length of a Morse Code "dash" in milliseconds
			int long_dash = 400;
			int short_gap = 50;    // Length of Gap Between dots/dashes
			int medium_gap = 500;   // Length of Gap Between Letters
			int long_gap = 1000; 
			long[] pattern = {
					0,  // Start immediately
					long_dash,medium_gap,long_dash
			};
			if(checkVibrationPermission()){
				builder.setVibrate(pattern);
			}

			//CAST THE MESSAGE TO THE PUSHEVENTMESSAGE LISTENER (OVERRIDDEN IN THE MAIN ACTIVITY)
			try {
				SuperG.pListener.onPushEvent(intent);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String action = intent.getExtras().getString("action");
			if(action == "showInterstitialAd"){
				SharedPreferences settings = context.getSharedPreferences("SuperGSettings", Context.MODE_PRIVATE);							
				settings.edit().putString("open_with_interstitial", "true").commit();
			}

			Intent mActivity = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
			Intent notificationIntent = new Intent(mActivity);

			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   
					PendingIntent.FLAG_UPDATE_CURRENT);  
			builder.setContentIntent(contentIntent);  

			// Add as notification  
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
			manager.notify(1, builder.build()); 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	private void handleMessage( Intent intent )
	{


		try {
			
			final String message = intent.getExtras().getString("message");
			final String title = intent.getExtras().getString("title");
			Thread t = new Thread()
			{
				public void run()
				{					
					EVA Eva = new EVA();
					Eva.registerPush(getApplicationContext());
					
					Message myMessage = new Message();
					Bundle resBundle = new Bundle();
					resBundle.putString( "message", message );
					resBundle.putString( "title", title );
					myMessage.setData( resBundle );
					handler.sendMessage( myMessage );
					Eva.syncEvents(getApplicationContext(), true);
				}
			};
			t.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Handler handler = new Handler()
	{public void handleMessage( Message msg )
	{
		try {
			final Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);

			final Context context = getApplicationContext();
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View toastView = li.inflate(R.layout.thumbr_layout_toast, null);
			toast.setView(toastView);


			Resources appR = getApplicationContext().getResources(); 
			String app_name = (String) appR.getText(appR.getIdentifier("app_name","string", getApplicationContext().getPackageName()));
			TextView titletext = (TextView) toastView.findViewById(R.id.tbrlay_toast_title);
			titletext.setText(app_name);

			TextView text = (TextView) toastView.findViewById(R.id.tbrlay_toast_message);
			text.setText(Html.fromHtml(msg.getData().getString( "message" )));			
			try
			{
				int app_icon = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
				ImageView toasticon = (ImageView) toastView.findViewById(R.id.ToastIcon);
				toasticon.setImageResource(app_icon);
			}
			catch (NameNotFoundException e)
			{
				Log.v(TAG, e.getMessage());
			}
			toastUp = "yes";
			toast.show();
			toastView.bringToFront();
			new CountDownTimer(6000, 1000)
			{
				public void onTick(long millisUntilFinished) {if(toastUp=="yes"){toast.show();}}
				public void onFinish() {if(toastUp=="yes"){toast.show();}}

			}.start();


			toastView.findViewById(R.id.superGToastMessage).setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toast.cancel();
					String packageName = context.getPackageName();
					Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
					String className = launchIntent.getComponent().getClassName();
					//Inform the user the button has been clicked
					Intent it = new Intent("intent.bring.to.front");
					it.setComponent(new ComponentName(context.getPackageName(), className));
					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.getApplicationContext().startActivity(it);
					toastUp="no";
					KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
					KeyguardLock lock = manager.newKeyguardLock("abc");
					lock.disableKeyguard(); 
				}
			});

			//			toastView.findViewById(R.id.toastClose).setOnClickListener( new View.OnClickListener() {
			//				@Override
			//				public void onClick(View v) {
			//					toast.cancel();
			//					toastUp="no";
			//				}
			//			});
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	};

}