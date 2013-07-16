SuperG Unity plugin
===================

Integration guide
----------------

#####Please note: SuperG is going to be the new name for Thumbr. Both names are currently used.

### Step 1: 
* In your Unity project directory, create the directory Plugins/Android in your Assets folder.
* Copy/merge the contents of the folder 'plugin' to Assets/Plugins/Android

### Step 2
* In your Unity project / scene, create a GUI Texture Game Object and rename it 'SuperG'
* Add the component Startup.cs to this Game Object.

### Step 3
* Edit the Startup.cs file to suit your needs.
#####Please note: The settings in the Startup.cs will have to be replaced by the settings, given to you by your game manager.

##Methods
There are 4 interactive methods:

* ThumbrJavaClass.CallStatic("superG_OpenDialog",this);

This will open the superG dialog for user registration

* ThumbrJavaClass.CallStatic("superG_AdInline",x,y,width,height);
This will open an 'inline ad banner'

* ThumbrJavaClass.CallStatic("superG_AdOverlay");
This will open an 'overlay banner'

* ThumbrJavaClass.CallStatic("superG_AdInterstitial");
This will open a page screen filling 'interstitial banner'