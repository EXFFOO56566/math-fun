package com.arkay.funwithmaths;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

public class SplashScreenActivity extends Activity {
	protected boolean _active=true;
	protected int _splashTime=35000;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		Thread splshThread = new Thread(){
			@Override
			public void run(){
					startMainScreen();
					finish();
				}
		};
        Handler handler = new Handler();
        handler.postDelayed(splshThread, 2000);
	}
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	    Window window = getWindow();
	    window.setFormat(PixelFormat.RGBA_8888);
	}
	
	public void startMainScreen(){
		Intent inst = new Intent(this,HomeMenuActivity.class);
		startActivity(inst);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			_active = false;
		}
		return true;
	}
}


  /*  private final int SPLASH_DISPLAY_LENGHT = 1000;

    *//** Called when the activity is first created. *//*
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                 Create an Intent that will start the Menu-Activity. 
                Intent mainIntent = new Intent(SplashScreenActivity.this,HomeMenuActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }*/
