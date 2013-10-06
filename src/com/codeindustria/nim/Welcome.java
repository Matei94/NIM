package com.codeindustria.nim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends Activity implements OnClickListener, OnCheckedChangeListener{
	Button bAbout;
	Button bPlay;
	Button bExit;
	TextView tvGameDescription;
	RadioGroup rgGameType;
	RadioGroup rgOpponent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// rgGameType.check(R.id.rbMisere);
		// rgOpponent.check(R.id.rbMaster);
	}
	
	private void init() {
		rgGameType = (RadioGroup) findViewById(R.id.rgGameType);
		rgOpponent = (RadioGroup) findViewById(R.id.rgOpponent);
		rgGameType.setOnCheckedChangeListener(this);
		rgOpponent.setOnCheckedChangeListener(this);
		
		bAbout = (Button) findViewById(R.id.bAbout);
		bPlay = (Button) findViewById(R.id.bPlay);
		bExit = (Button) findViewById(R.id.bExit);
		bAbout.setOnClickListener(this);
		bPlay.setOnClickListener(this);
		bExit.setOnClickListener(this);
		
		tvGameDescription = (TextView) findViewById(R.id.tvGameDescription);
		Typeface font;
 		font = Typeface.createFromAsset(getAssets(), "digistrip.ttf");
 		tvGameDescription.setTypeface(font);
	}
	
	long timeFirstPress = 0;
	Toast exitToast;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bAbout:
			Intent intentAbout = new Intent("android.intent.action.ABOUT");
			startActivity(intentAbout);
			break;
		case R.id.bPlay:
			Intent intentPlayground = new Intent("android.intent.action.PLAYGROUND");
			startActivity(intentPlayground);
			break;
		case R.id.bExit:
			if (System.currentTimeMillis() < timeFirstPress + 2000) {
				finish();
			} else {
				timeFirstPress = System.currentTimeMillis();
				exitToast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
				exitToast.show();
			}
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbMisere:
			NimClass.gameType = 0;
			break;
		case R.id.rbNormal:
			NimClass.gameType = 1;
			break;
		case R.id.rbMaster:
			NimClass.playWith = 0;
			break;
		case R.id.rbFriend:
			NimClass.playWith = 1;
			break;
		}
	}
}
