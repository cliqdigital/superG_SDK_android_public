package logoanimation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class animation {

	public void animateLogo(ImageButton thumbrLogo, Activity activity, double buttonWidth){

		Display display = activity.getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = (int) (display.getWidth() * buttonWidth);
		if (width > 120) {
			width = 120;
		}// optional

		LayoutParams params = new LinearLayout.LayoutParams(width, width);
		thumbrLogo.setLayoutParams(params);

		thumbrLogo.setBackgroundResource(com.cliqdigital.thumbrtanimation.R.drawable.thumbr_00030);
		thumbrLogo.setBackgroundResource(com.cliqdigital.thumbrtanimation.R.drawable.anim_thumbr_logo);
		AnimationDrawable thumbrLogoAnimation = (AnimationDrawable) thumbrLogo.getBackground();
		thumbrLogoAnimation.start();
	}
}
