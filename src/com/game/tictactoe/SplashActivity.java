package com.game.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    
	private boolean isAnimationFinished = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        ImageView rotateImgView = (ImageView)this.findViewById(R.id.board_img);
        Animation rotate_out = AnimationUtils.loadAnimation(this, R.anim.rotate_out);
        rotate_out.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimationFinished = true;
			}
			
		});
        rotateImgView.startAnimation(rotate_out);
    }
    
    public boolean onTouchEvent(MotionEvent event) {
    	if(isAnimationFinished){
    		if(event.getAction()==MotionEvent.ACTION_DOWN){
    			// 动画结束，跳转到菜单
        		Utils.startMenuActivity(this);
        		finish();
        		return true;
    		}
    	}
    	return super.onTouchEvent(event);
    }
    
}