package com.gkxim.android.thumbsdk.utils;
/***
 * registration login, after finish of registration, sdk will send it.
 * @author chenjiazeng
 *
 */
public interface WSRegisterListener
{	
	/***
	 *   registration login, after finish of registration, sdk will send it.
	 * @param code     (login status，code == WSStateCode.SUCCESS (login success) code == WSStateCode.FAILED (login failed)) 
	 * @param ProfileObject	
	 * @param msg      (reason of failed)
	 */
	public void callback(int code, String accessToken,String msg);
}
