using UnityEngine;
using System.Collections;

public class Startup : MonoBehaviour {
	
	public static AndroidJavaClass ThumbrJavaClass;
	//APP SPECIFIC SETTINGS
	public static string sid = "com.thumbr.dragonsvsunicorns";
	public static string client_id = "84758475-476574";

	//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
	public static bool showButtonClose = true;
	//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
	public static double buttonWidth = 0.15;


	//AD SERVING SETTINGS
	public static int updateTimeInterval = 0;//number of seconds before Ad refresh
	public static int showCloseButtonTime = 6;//Number of seconds before the Ad close button appears

	public static string tablet_Inline_zoneid = 		"1356888057";
	public static string tablet_Inline_secret = 		"20E1A8C6655F7D3E";
	public static string tablet_Overlay_zoneid = 		"3356907052";
	public static string tablet_Overlay_secret = 		"ADAA22CB6D2AFDD3";
	public static string tablet_Interstitial_zoneid = "7356917050";
	public static string tablet_Interstitial_secret = "CB45B76FE96C8896";
	public static string phone_Inline_zoneid = 		"0345893057";
	public static string phone_Inline_secret = 		"04F006733229C984";
	public static string phone_Overlay_zoneid = 		"7345907052";
	public static string phone_Overlay_secret = 		"AEAAA69F395BA8FA";
	public static string phone_Interstitial_zoneid = 	"9345913059";
	public static string phone_Interstitial_secret = 	"04B882960D362099";

	/*
	 * OTHER, MORE GENERIC SETTINGS
	 */
	public static string action = 		"registration";
	public static string appsFlyerKey = 	"9ngR4oQcH5qz7qxcFb7ftd";	
	public static bool debug = 		false; //OPTIONALLY SWITCH TO 'true' DURING IMPLEMENTATION		
	public static string registerUrl = 	"http://gasp.thumbr.com/auth/authorize?";
	public static string score_game_id = "";
	public static int hideThumbrCloseButton = 0;//hide the close button from the Thumbr Window? 1 or 0
	public static string SDKLayout = 		"thumbr";


	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	public static string country = 		"";//eg: DE or NL
	public static string locale = 		"";//eg: nl_NL or de_DE
	public static string appsFlyerId =	"";
	public static string v = "";
	
	// Use this for initialization
	void Start () {
			ThumbrJavaClass = new AndroidJavaClass("com.gkxim.android.thumbsdk.UnityPlugin");
			ThumbrJavaClass.CallStatic("superG_Initialize",sid, client_id, showButtonClose, buttonWidth, updateTimeInterval, showCloseButtonTime, tablet_Inline_zoneid, tablet_Inline_secret, tablet_Overlay_zoneid, tablet_Overlay_secret, tablet_Interstitial_zoneid, tablet_Interstitial_secret, phone_Inline_zoneid, phone_Inline_secret, phone_Overlay_zoneid, phone_Overlay_secret, phone_Interstitial_zoneid, phone_Interstitial_secret, action, appsFlyerKey, debug, registerUrl, score_game_id, hideThumbrCloseButton, SDKLayout, country, locale, appsFlyerId);
	}
	
	//This function is called when the Thumbr window is closed
	//The user data is returned as a json object
	public void dismissMessage(string message){
		Debug.Log(message);
	}

	
   void OnGUI(){
		if( Application.platform == RuntimePlatform.Android ) {

			ThumbrJavaClass = new AndroidJavaClass("com.gkxim.android.thumbsdk.UnityPlugin");

       		if (GUI.Button(new Rect (15, 125, 250, 100), "THUMBR"))
       		{	
				Screen.orientation = ScreenOrientation.Portrait;
				ThumbrJavaClass.CallStatic("superG_OpenDialog",this);
       		}	
			
	   		if (GUI.Button(new Rect (15, 350, 250, 100), "Inline Ad"))
       		{	
				Screen.orientation = ScreenOrientation.Portrait;
				/* superG_AdInline
				 * parameters:
				 * 
				 * x position
				 * y position
				*/
				ThumbrJavaClass.CallStatic("superG_AdInline",0,(Screen.height-120),720,120);
       		}
			
			if (GUI.Button(new Rect (15, 475, 250, 100), "Overlay Ad"))
       		{	
				Screen.orientation = ScreenOrientation.Portrait;
				ThumbrJavaClass.CallStatic("superG_AdOverlay");
       		}	
			
	   		if (GUI.Button(new Rect (15, 600, 250, 100), "Interstitial Ad"))
       		{	
				Screen.orientation = ScreenOrientation.Portrait;
				ThumbrJavaClass.CallStatic("superG_AdInterstitial");
       		}				
		}	
    }		
	
	// Update is called once per frame
	void Update () {
	
	}
}
