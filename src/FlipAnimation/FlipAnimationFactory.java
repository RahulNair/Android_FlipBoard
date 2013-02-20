package FlipAnimation;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import FlipAnimation.AnimationFactory.FlipDirection;
import FlipAnimation.FlipAnimation.ScaleUpDownEnum;

public class FlipAnimationFactory {
	
	public static enum FlipDirection {
		LEFT_RIGHT, 
		RIGHT_LEFT;
	};
	public static enum ViewPosition {
		VIEW_TOP_HALF, 
		VIEW_BOTTOM_HALF;
	};
	public static enum ViewAction {
		OPEN_VIEW, 
		CLOSE_VIEW;
	};
		
	public static Animation[] flipAnimation(final View fromView,ViewPosition viewPosition,ViewAction viewAction ,long duration, Interpolator interpolator) {
		System.out.println("---- viewPosition "+viewPosition);
		Animation[] result = new Animation[2];
		float centerX;
		float centerY;

		if (viewPosition == ViewPosition.VIEW_TOP_HALF) {
			centerX = fromView.getWidth() / 2.0f;
			centerY = 240; 
				
		}else if (viewPosition == ViewPosition.VIEW_BOTTOM_HALF) {
			centerX = fromView.getWidth() / 2.0f;
			centerY = 0; 
		}else{
			centerX = 0;
			centerY = 0;
		}
		
//		Animation inFlip = new FlipAnimation(dir.getStartDegreeForSecondView(), dir.getEndDegreeForSecondView(), centerX, centerY, FlipAnimation.SCALE_DEFAULT, FlipAnimation.ScaleUpDownEnum.SCALE_NONE,true,false);
		Animation inFlip = new FlipAnimation(centerX,centerY,FlipAnimation.SCALE_DEFAULT,FlipAnimation.ScaleUpDownEnum.SCALE_NONE,viewAction,viewPosition);
		inFlip.setDuration(duration);
		inFlip.setFillAfter(true);
		System.out.println("---interpolator "+interpolator);
		inFlip.setInterpolator(interpolator==null?new LinearInterpolator():interpolator);
		inFlip.setStartOffset(duration);   
		
		AnimationSet inAnimation = new AnimationSet(true); 
		inAnimation.addAnimation(inFlip); 
		result[0] = inAnimation; 
		
		return result;
		
	}
}
