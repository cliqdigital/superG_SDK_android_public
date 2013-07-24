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
package com.gkxim.android.thumbsdk.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	static final String SENDER_ID = "906501791685";
	

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        
        Bundle extras = intent.getExtras();
        String message = extras.getString("message");
        String title = extras.getString("title");
        int icon = 0;
		long when = 0;
		
		//        displayMessage(context, message);
//        // notifies user
        @SuppressWarnings("deprecation")
		Notification notification = new Notification(icon, title, when);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //notification.defaults |= Notification.DEFAULT_SOUND; // To play default notification sound
//        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push); // to play custom notification sound
        NotificationManager nm = (NotificationManager)   getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }



}
