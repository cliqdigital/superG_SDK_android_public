using UnityEngine;
using System.Collections;

public class Startup : MonoBehaviour {
	public bool paused;
	
	public static AndroidJavaClass SuperGJavaClass;
	//APP SPECIFIC SETTINGS
	public static string sid = "*";
	public static string client_id = "84758475-476574";

	//HIDE THE CLOSE BUTTON (ONLY USE IN-SDK PLAY BUTTON)
	public static bool showButtonClose = true;
	//THUMBR BUTTON WIDTH/HEIGHT, RELATIVE TO SCREEN WIDTH (MAX. 120PX)
	public static double buttonWidth = 0.15;


	//AD SERVING SETTINGS
	public static int updateTimeInterval = 0;//number of seconds before Ad refresh
	public static int showCloseButtonTime = 6;//Number of seconds before the Ad close button appears

	public string tablet_Inline_zoneid = 		"1356888057";
	public string tablet_Inline_secret = 		"20E1A8C6655F7D3E";
	public string tablet_Overlay_zoneid = 		"3356907052";
	public string tablet_Overlay_secret = 		"ADAA22CB6D2AFDD3";
	public string tablet_Interstitial_zoneid = "7356917050";
	public string tablet_Interstitial_secret = "CB45B76FE96C8896";
	public string phone_Inline_zoneid = 		"0345893057";
	public string phone_Inline_secret = 		"04F006733229C984";
	public string phone_Overlay_zoneid = 		"7345907052";
	public string phone_Overlay_secret = 		"AEAAA69F395BA8FA";
	public string phone_Interstitial_zoneid = 	"9345913059";
	public string phone_Interstitial_secret = 	"04B882960D362099";

	/*
	 * OTHER, MORE GENERIC SETTINGS
	 */
	public static string action = 		"registration";
	public static string appsFlyerKey = 	"9ngR4oQcH5qz7qxcFb7ftd";	
	public static bool 	 debug = 		true; //OPTIONALLY SWITCH TO 'true' DURING IMPLEMENTATION		
	public static string registerUrl = 	"http://gasp.superg.mobi/auth/authorize?";
	public static string score_game_id = "";
	public static int    hideThumbrCloseButton = 0;//hide the close button from the Thumbr Window? 1 or 0
	public static string SDKLayout = 		"thumbr";


	///LEAVE THESE VALUES EMPTY, UNLESS YOU KNOW WHAT YOU'RE DOING
	public static string country = 		"";//eg: DE or NL
	public static string locale = 		"";//eg: nl_NL or de_DE
	public static string appsFlyerId =	"";
	public static string v = "";
	
	// Use this for initialization
	void Start () {
			SuperGJavaClass = new AndroidJavaClass("com.cliqdigital.supergsdk.UnityPlugin");
			SuperGJavaClass.CallStatic("superG_Initialize",sid, client_id, showButtonClose, buttonWidth, updateTimeInterval, showCloseButtonTime, tablet_Inline_zoneid, tablet_Inline_secret, tablet_Overlay_zoneid, tablet_Overlay_secret, tablet_Interstitial_zoneid, tablet_Interstitial_secret, phone_Inline_zoneid, phone_Inline_secret, phone_Overlay_zoneid, phone_Overlay_secret, phone_Interstitial_zoneid, phone_Interstitial_secret, action, appsFlyerKey, debug, registerUrl, score_game_id, hideThumbrCloseButton, SDKLayout, country, locale, appsFlyerId);
	}
	
	//This function is called when the SuperG window is closed
	//The user data is returned as a json object
	public void dismissMessage(string message){
		Screen.orientation = ScreenOrientation.Landscape;
		Debug.Log(message);
	}

	//This function is called when a push message is received
	public void pushMessageReceived(string message){
		Debug.Log(message);
	}
	
	//This function is called when a push message contains an 'action'. 
	public void pushActionReceived(string action){
		Debug.Log(action);
	}	
	
   void OnGUI(){
		if( Application.platform == RuntimePlatform.Android ) {

			SuperGJavaClass = new AndroidJavaClass("com.cliqdigital.supergsdk.UnityPlugin");

       		if (GUI.Button(new Rect (15, 125, 250, 100), "THUMBR"))
       		{	
				SuperGJavaClass.CallStatic("superG_OpenDialog",this);
				Screen.orientation = ScreenOrientation.Portrait;
       		}	
			
			
			//ADVERTISEMENTS, FOR THIS EXAMPLE TRIGGERED BY A BUTTON
			GUI.TextField (new Rect (15, 300, 100, 20), "Advertisements", 15);
			
	   		if (GUI.Button(new Rect (15, 350, 250, 100), "Inline Ad"))
       		{	
				/* superG_AdInline
				 * parameters:
				 * 
				 * x position
				 * y position
				*/
				SuperGJavaClass.CallStatic("superG_AdInline",0,(Screen.height-120),720,120);
       		}
			
			if (GUI.Button(new Rect (15, 475, 250, 100), "Overlay Ad"))
       		{	
				SuperGJavaClass.CallStatic("superG_AdOverlay");
       		}	
			
	   		if (GUI.Button(new Rect (15, 600, 250, 100), "Interstitial Ad"))
       		{	
				SuperGJavaClass.CallStatic("superG_AdInterstitial");
       		}	
			
			
			
			if (GUI.Button(new Rect (300, 350, 250, 100), "Remove Inline Ad"))
       		{	
				SuperGJavaClass.CallStatic("superG_RemoveAdInline");
       		}
			
			if (GUI.Button(new Rect (300, 475, 250, 100), "Remove Overlay Ad"))
       		{	
				SuperGJavaClass.CallStatic("superG_RemoveAdOverlay");
       		}	
			
	   		if (GUI.Button(new Rect (300, 600, 250, 100), "Remove Interstitial Ad"))
       		{	
				SuperGJavaClass.CallStatic("superG_RemoveAdInterstitial");
       		}
			
			
			
			
			//EVENTS, FOR THIS EXAMPLE TRIGGERED BY A BUTTON
			GUI.TextField (new Rect (15, 725, 50, 20), "Events", 6);
			
	   		if (GUI.Button(new Rect (15, 750, 250, 100), "Achievement Earned"))
       		{	
				/*
				 * superG_AchievementEarnedEvent
				 * Variables:
				 * String achievement
				 */
				SuperGJavaClass.CallStatic("superG_AchievementEarnedEvent","myAchievement");
       		}
	   		if (GUI.Button(new Rect (15, 875, 250, 100), "Click"))
       		{	
				/*
				 * superG_ClickEvent
				 * Variables:
				 * String clicked_item
				 */
				SuperGJavaClass.CallStatic("superG_ClickEvent","myClickedItem");
       		}
	   		if (GUI.Button(new Rect (15, 1000, 250, 100), "Purchase"))
       		{	
				/*
				 * superG_PurchaseEvent
				 * Variables:
				 * String currency
				 * String payment_method
				 * String price
				 * String purchased_item
				 */				
				SuperGJavaClass.CallStatic("superG_PurchaseEvent","EUR","inApp","0.99","myPurchasedItem");
       		}
	   		if (GUI.Button(new Rect (300, 750, 250, 100), "Start Level"))
       		{	
				/*
				 * superG_StartLevelEvent
				 * Variables:
				 * String app_mode
				 * String level
				 * String score_type
				 * String score_value
				 */				
				SuperGJavaClass.CallStatic("superG_StartLevelEvent","standardMode","1","coins","50");
       		}
	   		if (GUI.Button(new Rect (300, 875, 250, 100), "Finish Level"))
       		{	
				/*
				 * superG_FinishLevelEvent
				 * Variables:
				 * String app_mode
				 * String level
				 * String score_type
				 * String score_value
				 */				
				SuperGJavaClass.CallStatic("superG_FinishLevelEvent","standardMode","1","coins","50");
       		}
	   		if (GUI.Button(new Rect (300, 1000, 250, 100), "Up Sell"))
       		{	
				/*
				 * superG_UpSellEvent
				 * Variables:
				 * String currency
				 * String payment_method
				 */				
				SuperGJavaClass.CallStatic("superG_UpSellEvent","EUR","creditCard");
       		}			
			
			
			
		}	
    }		
	
	// Update is called once per frame
	void Update () {
	 	
		}
	
	
	void OnApplicationPause(bool pauseStatus) {
        paused = pauseStatus;
			if(paused)
	   {
	       SuperGJavaClass.CallStatic("superG_pause");
	   }
	   else
	   {
	       SuperGJavaClass.CallStatic("superG_resume");
	   }
    }
	
}
