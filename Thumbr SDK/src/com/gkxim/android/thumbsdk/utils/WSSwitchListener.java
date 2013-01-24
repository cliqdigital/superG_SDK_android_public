package com.gkxim.android.thumbsdk.utils;

/***
 * account-exchange login, after exchange of user account, the request will be sent by sdk
 * @author chenjiazeng
 *
 */
public interface WSSwitchListener
{	
	/***
	 *   account-exchange login, after exchange of user account, the request will be sent by sdk
	 * @param code     (exchange status，code == WSStateCode.SUCCESS (exchange success) code == WSStateCode.FAILED (exchange failed)) 
	 * @param ProfileObject
	 * @param msg      (reason of failed)
	 */
	public void callback(int code, String accessToken,String msg);
}
