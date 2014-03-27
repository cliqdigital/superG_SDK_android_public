package com.cliqdigital.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cliqdigital.supergsdk.SuperG;
import com.cliqdigital.supergsdk.SuperG.OnPushMessageListener;
import com.cliqdigital.supergsdk.utils.EVA;


public class MainActivity extends Activity{


	SuperG superG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//CALL THE SUPERG LIBRARY AND REGISTER SETTINGS
		superG=new SuperG(this);

		superG.setPushMessageListener(new OnPushMessageListener(){
			@Override
			public void onPushEvent(Intent intent) {
				try {
					String action = intent.getExtras().getString("action");
					String message = intent.getExtras().getString("message");

					Log.i("SuperG","Push message was received. Message is:"+message);

					if(action.equals("customEvent")){
						Log.i("SuperG","Action '"+action+"' was received. Message is:"+message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});


		EVA eva = new EVA();
		eva.genericEvent(this, "MyEvent","It fired!");

		// DEFAULT STUFF
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onPause() {
		superG.pause(this);
		super.onPause();
	}

	@Override
	protected void onResume(){
		superG.resume();
		super.onResume();       
	}

}
