package com.codeindustria.nim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settings extends Activity implements OnClickListener{
	Button bDone;
	Button bAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		setContentView(R.layout.settings);
		init();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	private void init() {
		bDone = (Button) findViewById(R.id.bDone);
		bAbout = (Button) findViewById(R.id.bAbout);
		bDone.setOnClickListener(this);
		bAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bDone:
			finish();
			break;
		case R.id.bAbout:
			Intent intentAbout = new Intent("android.intent.action.ABOUT");
			startActivity(intentAbout);
			break;
		}
	}

}
