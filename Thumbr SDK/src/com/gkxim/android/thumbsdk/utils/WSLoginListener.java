package com.gkxim.android.thumbsdk.utils;

/***
 * temp/normal login, the login request will be sent by SDK.
 * @author chenjiazeng
 *
 */
public interface WSLoginListener
{	
	/***
	 *  temp/normal login-API, the login request will be sent by SDK.
	 * @param code     (login status，code == WSStateCode.SUCCESS (login success) code == WSStateCode.FAILED (login failed))
	 * @param ProfileObject
	 * @param isTemp   (account status, ture is temp account, false not)
	 * @param msg      (reason of failed)
	 */
	public void callback(int code, String accessToken, boolean isTemp ,String msg);
}
