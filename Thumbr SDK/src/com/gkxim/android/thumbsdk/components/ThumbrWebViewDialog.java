 package com.gkxim.android.thumbsdk.components;
 import java.util.Hashtable;
 import java.util.Locale;
 import com.gkxim.android.thumbsdk.FunctionThumbrSDK;
 import com.gkxim.android.thumbsdk.R;
 import com.gkxim.android.thumbsdk.utils.APIServer;
 import com.gkxim.android.thumbsdk.utils.ProfileObject;
 import com.gkxim.android.thumbsdk.utils.TBrLog;

 import android.app.Activity;
 import android.app.Dialog;
 import android.app.ProgressDialog;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.DialogInterface.OnDismissListener;
 import android.content.pm.ActivityInfo;
 import android.content.res.Configuration;
 import android.graphics.Bitmap;
 import android.graphics.drawable.AnimationDrawable;
 import android.graphics.drawable.ColorDrawable;
 import android.graphics.drawable.Drawable;
 import android.net.ConnectivityManager;
 import android.net.NetworkInfo;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Handler;
 import android.util.Config;
 import android.util.Log;
 import android.view.Display;
 import android.view.Gravity;
 import android.view.KeyEvent;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.View.OnLongClickListener;
 import android.view.View.OnTouchListener;
 import android.view.ViewGroup;
 import android.view.Window;
 import android.view.WindowManager;
 import android.webkit.CookieManager;
 import android.webkit.CookieSyncManager;
 import android.webkit.WebSettings;
 import android.webkit.WebView;
 import android.webkit.WebViewClient;
 import android.widget.Button;
 import android.widget.FrameLayout;
 import android.widget.ImageButton;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.PopupWindow;
 import android.widget.RelativeLayout.LayoutParams;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;

public class ThumbrWebViewDialog extends Dialog implements
		android.view.View.OnClickListener {

		
	private String url_hasdcode ="access_token=";
	private final int CONNECTION_TIMEOUT = 30000;
	private final int CLOSE_TIMEOUT = 2000;
	private Drawable mAnimLoading = null;
	private static final int CONST_BTN_CLOSE_ID = android.R.id.closeButton;
	private static final int CONST_BTN_ABOUT_TITLE = android.R.id.button1;
	private String mURL;
	private WebView mWebView = null;
	private boolean mLoadCompleted = false;
	protected String mProcessingTitle;
	private View dialogTimeOut;
	private Context mContext = null;
	private int oldOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
	private boolean mAnimationRun=false;	
	private boolean isShowButtonClose=true;
	boolean isRequested = false;
	private RelativeLayout aView = null;
	public boolean isNetwork = true;
	private Dialog dialog = null;
	private  popupLoading pLoading = null;
	private boolean finished = false;
	private AnimationDrawable anim;
	private FrameLayout frameLayout = null;
	private String url_Before="";
	public static String accToken="";
	public static String voidvar="";	
	public static String ERRORMESSAGE = TBrLog.SystemErrorMessage.TYPE_4;
	Handler mHandler = new Handler();
	
	private class TimeOut implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!mLoadCompleted) {
				if(mWebView != null)
					mWebView.stopLoading();
				mLoadCompleted = true;
				getWindow()
				.setBackgroundDrawable(
						new ColorDrawable(
								android.graphics.Color.TRANSPARENT));
				ThumbrWebViewDialog.this
					.setContentView(dialogTimeOut);
				ThumbrWebViewDialog.this.show();
				//mHandler.postDelayed(mCloseTimeOut, CLOSE_TIMEOUT);
			
			}
		}
		
	}
	
	public class CloseTimeOut implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			ThumbrWebViewDialog.this.dismiss();
		}
	}

	CloseTimeOut mCloseTimeOut = new CloseTimeOut();
	TimeOut timeOut = new TimeOut();

	public String getURL() {
		return mURL;
	}

	public void setURL(String mURL) {
		this.mURL = mURL;
		TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "Loading URL: " + mURL);
		mLoadCompleted = false;

		try {
			if (mWebView != null) {
				mWebView.loadUrl(mURL);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			TBrLog.fl(0,
					"Failed at: setURL(.. ) of class ThumbrWebViewDialog with exception: "
							+ e.getMessage());
		}

	}

	public void setProcessingTitle(String pProcessingTitle) {
		if (pProcessingTitle == null) {
			pProcessingTitle = "";
		}
		this.mProcessingTitle = pProcessingTitle;
	}

	public ThumbrWebViewDialog(Context context,Boolean showButtonClose) {
		super(context, R.style.Dialog_Fullscreen);
		isShowButtonClose=showButtonClose;
		mAnimationRun=false;
		mContext = context;
		isNetwork = isNetworkAvailable();
		dialogTimeOut = getLayoutInflater().inflate(R.layout.image_time_out,
				null);
//		TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "ThumbrWebViewDialog");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mProcessingTitle = "Please wait...";
		Context theContext = getContext();
		aView = (RelativeLayout) getWebViewLayout(theContext, 0);
		setContentView(aView);
		if (mWebView != null) {
			WebSettings aWS = mWebView.getSettings();
			aWS.setJavaScriptCanOpenWindowsAutomatically(true);
			aWS.setJavaScriptEnabled(true);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			mWebView.setWebViewClient(new onWebViewClient());
		}
		frameLayout = (FrameLayout)findViewById(R.id.web_fram);
		//mHandler.postDelayed(timeOut, CONNECTION_TIMEOUT);
		
	}
	
	
	public void setOldOrientation(int oldOrientation, boolean isRequested) {
		this.oldOrientation = oldOrientation;
		this.isRequested = isRequested;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TBrLog.l(TBrLog.TMB_LOGTYPE_INFO,
				"onCreate Dialog. " + mWebView == null ? "(NULL)"
						: "(NOT NULL)");

		try {
			if (mWebView == null) {
				Context theContext = getContext();
				aView = (RelativeLayout) getWebViewLayout(theContext, 0);
				setContentView(aView);
				if (mWebView != null) {
					WebSettings aWS = mWebView.getSettings();
					aWS.setJavaScriptCanOpenWindowsAutomatically(true);
					aWS.setJavaScriptEnabled(true);
					CookieManager cookieManager = CookieManager.getInstance();
					cookieManager.setAcceptCookie(true);
					mWebView.setWebViewClient(new onWebViewClient());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			TBrLog.fl(0,
					"Failed at: onCreate(.. ) of class ThumbrWebViewDialog with exception: "
							+ e.getMessage());
		}
	}

	private ViewGroup getWebViewLayout(Context theContext) {
		try {
			RelativeLayout result = new RelativeLayout(theContext);
			RelativeLayout.LayoutParams lp_llheader = null;
			RelativeLayout.LayoutParams lp_relative = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			result.setLayoutParams(lp_relative);
			if (mWebView == null) {

				ImageView imIcon = new ImageView(theContext);
				imIcon.setImageResource(R.drawable.thumbr);
				lp_llheader = new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp_llheader.addRule(RelativeLayout.ALIGN_PARENT_LEFT
						| RelativeLayout.ALIGN_PARENT_TOP);
				imIcon.setLayoutParams(lp_llheader);
				imIcon.setId(CONST_BTN_ABOUT_TITLE);
				imIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TBrLog.lt(v.getContext(), 0, "About ThumBr Dialog");
					}
				});

				ImageView imClose = new ImageView(theContext);
				imClose.setImageResource(R.drawable.btn_close);
				lp_llheader = new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp_llheader.addRule(RelativeLayout.ALIGN_PARENT_RIGHT
						| RelativeLayout.ALIGN_PARENT_TOP);
				imClose.setLayoutParams(lp_llheader);
				imClose.setId(CONST_BTN_CLOSE_ID);
				imClose.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TBrLog.lt(v.getContext(), 0,
								"play now (1)");
						Log.i("ThumbrSDK","Access token: "+accToken);
						 //((Activity) mContext).setRequestedOrientation(oldOrientation);
						if(accToken != null && accToken != ""){
						setURL("thumbr://stop?access_token="+accToken);
						}else{
							setURL("thumbr://stop");					
						}
					}
				});

				mWebView = new WebView(theContext);
				WebSettings aWS = mWebView.getSettings();
				aWS.setJavaScriptCanOpenWindowsAutomatically(true);
				aWS.setJavaScriptEnabled(true);
				aWS.setUseWideViewPort(true);
				aWS.setBuiltInZoomControls(true);
				aWS.setSupportZoom(true);
				lp_llheader = new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				lp_llheader.addRule(RelativeLayout.ALIGN_PARENT_LEFT
						| RelativeLayout.ALIGN_PARENT_RIGHT
						| RelativeLayout.ALIGN_PARENT_BOTTOM);
				lp_llheader.addRule(RelativeLayout.BELOW, imIcon.getId());
				lp_llheader.addRule(RelativeLayout.BELOW, imClose.getId());
				mWebView.setLayoutParams(lp_llheader);

				result.addView(imIcon);
				result.addView(imClose);
				result.addView(mWebView);

			}
			return (ViewGroup) result;
		} catch (Exception e) {
			// TODO: handle exception
			TBrLog.fl(
					0,
					"Failed at: getWebViewLayout(.. ) of class ThumbrWebViewDialog with exception: "
							+ e.getMessage());
			return null;
		}

	}

	private ViewGroup getWebViewLayoutFromResource(Context theContext) {

		RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.thumbr_layout_dialog_webview, null);

		mWebView = (WebView) rl.findViewById(R.id.tbrlay_dialog_webview);

		WebSettings aWS = mWebView.getSettings();
		aWS.setSavePassword(false);
		ImageView imClose = (ImageView) rl
				.findViewById(R.id.img_dialog_header_close);
		if(!isShowButtonClose){
			imClose.setVisibility(View.GONE);
		}
		if (imClose != null) {
			imClose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {

						TBrLog.lt(v.getContext(), 0,
								"play now (2)");
						//ThumbrWebViewDialog.this.dismiss();
						Log.i("ThumbrSDK","Access token: "+accToken);
						 //((Activity) mContext).setRequestedOrientation(oldOrientation);						
						if(accToken != null && accToken != ""){
						setURL("thumbr://stop?access_token="+accToken);
						}
						else if(mURL.contains("/start?")){
							setURL("thumbr://stop?");							
						}
						else{
							setURL("thumbr://stop");				
						}
					} catch (Exception e) {
						// TODO: handle exception
						TBrLog.fl(
								0,
								"Failed at: getWebViewLayoutFromResource{... onClick(.. ) } of class ThumbrWebViewDialog with exception: "
										+ e.getMessage());
					}

				}
			});
		}
		return rl;
	}

	private ViewGroup getWebViewLayout(Context theContext, int pFrom) {
		switch (pFrom) {
		case 1:
			return getWebViewLayout(theContext);
		default:
			return getWebViewLayoutFromResource(theContext);
		}
	}

	public void onCreateDialog() {
		
		dialog = new Dialog(getContext(),R.style.Dialog_Fullscreen);
		dialog.setOnDismissListener((OnDismissListener) mContext);
		
		if (dialog != null) {
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			if(Locale.getDefault().getLanguage().equals(mContext.getResources().getString(R.string.Dutch))){
				dialog.setContentView(R.layout.popup_layout_for_dutch);
			}else if(Locale.getDefault().getLanguage().equals(mContext.getResources().getString(R.string.German))){
				dialog.setContentView(R.layout.popup_layout_for_german);
			}
			else{
				dialog.setContentView(R.layout.popup_layout_for_english);
			}

			
			ImageView imClose = (ImageView) dialog.findViewById(R.id.img_dialog_header_close);
			if (imClose != null) {
				imClose.setOnClickListener(new View.OnClickListener() {

					
					@Override
					public void onClick(View v) {
						try {
							Log.i("ThumbrSDK","NO CONNECTION VIEW DISMISSED");
							dialog.dismiss();
							
							((Activity) mContext).setRequestedOrientation(oldOrientation);
							//return;
						} catch (Exception e) {
							// TODO: handle exception
							TBrLog.fl(
									0,
									"Cannot dismiss dialog for some reason: "
											+ e.getMessage());
						}

					}
				});
			}
			
			
			ImageButton bt = (ImageButton) dialog.findViewById(R.id.button1);
			bt.setOnClickListener(this);		
		}
	}
	
	
	private void onCreatePopupLoading(){
		Context theContext = getContext();
		pLoading = new popupLoading(theContext);
		pLoading.onShow();
	}
	
	private class popupLoading extends Dialog{
		ImageView img = null;
		public popupLoading(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_popup_layout);
			img= (ImageView) findViewById(R.id.img_loading_popup);
		}
		public void onShow(){
			show();
		}
		
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
			// TODO Auto-generated method stub
			super.onWindowFocusChanged(hasFocus);
			mAnimLoading = getContext().getResources().getDrawable(
					R.drawable.anim_loading);
			if(img != null)
				img.setBackgroundDrawable(mAnimLoading);
			anim = (AnimationDrawable) img.getBackground();
			anim.start();
		}
	}
	
	private void onCreateAnimation(){
		
		mAnimLoading = mContext.getResources().getDrawable(
				R.drawable.anim_loading);
		ImageView imgv = null;
		if(!finished)
			imgv= (ImageView) findViewById(R.id.img_loading);
		else
			imgv= (ImageView) findViewById(R.id.img_loading_web);
		
		if (imgv != null)
			imgv.setBackgroundDrawable(mAnimLoading);
		anim = (AnimationDrawable) imgv.getBackground();
		anim.start();
		mAnimationRun=true;
	}

	public void showNotNetwork(){
			onCreateDialog();
			if (dialog != null) {
				dialog.show();
			}
			mLoadCompleted = true;
			dismiss();
	}
	@Override
	public void show() {
		if (!isNetwork) {
			mLoadCompleted = true;
			onCreateDialog();
			if (dialog != null) {
				dialog.show();
			}
			dismiss();
			return;
		}
		super.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			Log.w("NTT", "onKeyDown");
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		Log.w("NTT", "onBackPressed");
		// TODO Auto-generated method stub
		if (!mLoadCompleted && finished == false) {
			Log.w("NTT", "onBackPressed");
			mLoadCompleted = true;
			//dismiss();
			super.onBackPressed();
		}
	}

	public boolean isNetworkAvailable() {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = null;
			if (connectivityManager != null) {
				networkInfo = connectivityManager
						.getActiveNetworkInfo();
			}
			return networkInfo == null ? false : networkInfo.isConnected();
		} catch (Exception e) {
			// TODO: handle exception
			TBrLog.fl(
					0,
					"Failed at: isNetworkAvailable(.. )  of class ThumbrWebViewDialog with exception: "
							+ e.getMessage());
			return false;
		}
	}

	public class onWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "Load url: " + url
					+ " is completed.");
			if(url_Before.equals(url))
				return;
			finished = true;
			try {
				
				if(anim != null && anim.isRunning())
					anim.stop();
				
				isNetwork = isNetworkAvailable();
				if (mLoadCompleted || !isNetwork) {
					if (!isNetwork)
						((Activity) mContext)
								.setRequestedOrientation(oldOrientation);
					return;
				}

				view.setVisibility(View.VISIBLE);
				frameLayout.setVisibility(View.GONE);
				ThumbrWebViewDialog.this.hide();
				ThumbrWebViewDialog.this.setContentView(aView);
				TBrLog.l(TBrLog.TMB_LOGTYPE_DEBUG, view.getContentHeight()
						+ ", " + view.getContentDescription());
				ThumbrWebViewDialog.this.show();
				Log.w("NTT", "ThumbrWebViewDialog.this.show()");
				mLoadCompleted = true;
				url_Before=url;
			} catch (Exception e) {
				// TODO: handle exception
				TBrLog.fl(0,
						"Failed at: onPageFinished(.. ) of class ThumbrWebViewDialog with exception: "
								+ e.getMessage());
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);			
			parserIntercept(url);
			if(url.contains("thumbr://stop")){
				mLoadCompleted=true;
				if(url.contains(url_hasdcode)){
					isLogined();
					///mWebView.loadUrl(APIServer.getURLPrtal());
					//return;
				}
				TBrLog.lt(mContext, 0,
						"Start Game");
				ThumbrWebViewDialog.this.dismiss();
				return;
			}else if(!url.contains("thumbr.com") && !url.contains("cliqdigital.com")){
				//Load in external browser
				mHandler.removeCallbacks(timeOut);
				view.stopLoading();				
				mLoadCompleted=true;	
				view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				
				Log.i("ThumbrSDK","Url is opened in default browser");

				//ThumbrWebViewDialog.this.dismiss();				
				return;
			}
			if(mWebView.isShown()){
				mLoadCompleted = false;
//				mHandler.removeCallbacks(timeOut);
//				mHandler.postDelayed(timeOut, CONNECTION_TIMEOUT);
			}
			TBrLog.l(TBrLog.TMB_LOGTYPE_INFO, "start to load url: " + url);
			if (mLoadCompleted || !isNetwork)
				return;
			try {

				if (isNetwork) {
					if(finished == false){
						ThumbrWebViewDialog.this
							.setContentView(R.layout.loading_layout);
					}
					else{
						frameLayout.setVisibility(View.VISIBLE);
					}
					
					onCreateAnimation();
					if(url.contains(url_hasdcode)){
						isLogined();
						///mWebView.loadUrl(APIServer.getURLPrtal());
						//return;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				TBrLog.fl(0,
						"Failed at: onPageStarted(.. ) of class ThumbrWebViewDialog with exception: "
								+ e.getMessage());
			}

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			TBrLog.fl(0, "Couldn't connect to " + failingUrl + " due to "
					+ errorCode + " error");
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button1) {
			isNetwork = isNetworkAvailable();
			if(isNetwork){
				mLoadCompleted=false;
				mWebView.reload();
				this.show();
				dialog.dismiss();
			}
		}
	}
	
	
	
	private boolean parserIntercept(String s){

		if(s.contains("access_token=")){
			s = s.replace("#","&");
						
			Log.i("ThumbrSDK","intercept url string"+s);
			Uri uri=Uri.parse(s);
			accToken = uri.getQueryParameter("access_token");
			
			return true;
		}else{			
			return false;
		}
	
	}
	public String get_AccToken(){
		return accToken;
	}
	
	
	/**
	 * get profile of user then logined
	 * @return
	 */
	public ProfileObject getProfile(){
		ProfileObject obj=null;		
		APIServer server=new APIServer(mContext);
		obj=server.getProfile(get_AccToken());		
		return obj;	
	}
	
	
	public boolean hasEmail(){
		ProfileObject obj=null;		
		APIServer server=new APIServer(mContext);
		obj=server.getProfile(get_AccToken());			
		if(obj!=null){
			if(obj.getmEmail()!=null && obj.getmEmail()!="null"){
			Log.i("ThumbrSDK","Has e-mail: "+obj.getmEmail());
			return true;
			}
		}
		return false;
	}
	
	//check login
	public boolean isLogined(){
		if(getProfile()!=null && hasEmail()){
			Log.i("ThumbrSDK","log in succeeded with acc token: "+get_AccToken());
			mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit()
			.putBoolean(FunctionThumbrSDK.LOGINED,true).commit();
			mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).edit()
			.putString(FunctionThumbrSDK.ACCESSTOKEN,get_AccToken()).commit();
			return true;
		}
		mContext.getSharedPreferences(FunctionThumbrSDK.LOGINED,((Activity)mContext).MODE_PRIVATE).edit()
		.putBoolean(FunctionThumbrSDK.LOGINED,false).commit();
		mContext.getSharedPreferences(FunctionThumbrSDK.ACCESSTOKEN,((Activity)mContext).MODE_PRIVATE).edit()
		.putString(FunctionThumbrSDK.ACCESSTOKEN,get_AccToken()).commit();
		return false;
	}
}
	