package com.codeindustria.nim;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Logo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
 		
 		// Sound
 		MediaPlayer sound = MediaPlayer.create(Logo.this, R.raw.beat);
 		final int seconds = sound.getDuration();
 		sound.start();
 		
 		// Sleep while music is playing
 		Thread timer = new Thread(){
			public void run() {
				try {
					sleep(seconds);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Intent intentWelcome = new Intent("android.intent.action.WELCOME");
					startActivity(intentWelcome);
				}
			}
		};
		timer.start();
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logo, menu);
        return true;
    }
    
}
