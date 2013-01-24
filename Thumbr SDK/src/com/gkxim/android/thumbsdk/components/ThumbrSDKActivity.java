/**
 * 
 */
package com.gkxim.android.thumbsdk.components;

import java.util.HashMap;
import java.util.Map;

import com.gkxim.android.thumbsdk.R;
import com.gkxim.android.thumbsdk.R.id;
import com.gkxim.android.thumbsdk.R.layout;
import com.gkxim.android.thumbsdk.R.string;
import com.gkxim.android.thumbsdk.utils.TBrLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author HP
 * 
 */
public class ThumbrSDKActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.thumbr_layout_main);
			mWebView = (WebView) findViewById(R.id.tbrlay_main_webview);
			if (mWebView != null) {
				mWebView.getSettings().setJavaScriptEnabled(true);
				Map<String, String> extraHeaders = new HashMap<String, String>();
				  extraHeaders.put("X-Thumbr-Method", "sdk");					
				mWebView.loadUrl(getResources().getString(R.string.homelink),extraHeaders);
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			TBrLog.fl(0,
					"Failed at: onCreate() of class ThumbrSDKActivity with exception: "
							+ e.getMessage());
		}
	}

	private class ThumbrWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Map<String, String> extraHeaders = new HashMap<String, String>();
			  extraHeaders.put("X-Thumbr-Method", "sdk");			
			view.loadUrl(url,extraHeaders);
			return true;
		}
	}

}
