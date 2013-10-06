package com.codeindustria.nim;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Playground extends Activity implements OnClickListener{
	TextView tvHeapState;
	TextView tvTurn;
	TextView tvLastMove;
	Button bHeapSelector;
	Button bStickSelector;
	Button bOk;
	int selectHeap = 0;
	int selectSticks = 0;
	int res[];
	int i, j;
	int okSoundId, failSoundId, finalVictoryId, finalFailId;
	int cnt = 0;
	String heapState;
	NimClass nim;
	AlertDialog.Builder alert;
	SoundPool okSound, failSound, finalVictory, finalFail;
	boolean clickable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground);
		init();
	}
	
	private void init() {
		tvHeapState = (TextView) findViewById(R.id.tvHeapState);
		tvTurn = (TextView) findViewById(R.id.tvTurn);
		tvLastMove = (TextView) findViewById(R.id.tvLastMove);
		bHeapSelector = (Button) findViewById(R.id.bHeapSelector);
		bStickSelector = (Button) findViewById(R.id.bStickSelector);
		bOk = (Button) findViewById(R.id.bOk);
		
		bHeapSelector.setOnClickListener(this);
		bStickSelector.setOnClickListener(this);
		bOk.setOnClickListener(this);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "neon.ttf");
 		tvHeapState.setTypeface(font);
 		tvHeapState.setTextSize(28);
 		
 		tvTurn.setTypeface(font);
 		tvTurn.setTextSize(20);
 		
 		tvLastMove.setTypeface(font);
 		tvLastMove.setTextSize(15);
 		
 		bHeapSelector.setTypeface(font);
 		bHeapSelector.setTextSize(25);
 		
 		bStickSelector.setTypeface(font);
 		bStickSelector.setTextSize(25);
 		
 		nim = new NimClass();
 		
 		res = new int[2];
 		
 		alert = new AlertDialog.Builder(this);
 		alert.setCancelable(false);
 		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
 		alert.create();
 		
 		okSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
 		okSoundId = okSound.load(this, R.raw.button, 0);
 		failSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
 		failSoundId = failSound.load(this, R.raw.failbuzzer, 0);
 		finalVictory = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
 		finalVictoryId = finalVictory.load(this, R.raw.finalvictory, 0);
 		finalFail = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
 		finalFailId = finalFail.load(this, R.raw.finalfail, 0);
	}

	@Override
	public void onClick(View v) {
		if (clickable) {
			switch (v.getId()) {
			case R.id.bHeapSelector:
				bHeapSelectorAction();
				break;
				
			case R.id.bStickSelector:
				bSticksSelectorAction();
				break;
				
			case R.id.bOk:
				bOkAction();
				break;
			}
		}
	}
	
	private void bHeapSelectorAction() {
		while (true) {
			selectHeap = 1 + selectHeap % NimClass.no_heaps;
			if (nim._board[selectHeap-1] != 0) break;
		}
		
		bHeapSelector.setText("Heap:   " + selectHeap);
		
		selectSticks = 0;
		bStickSelector.setText("Sticks: ?");
	}
	
	private void bSticksSelectorAction() {
		if (selectHeap != 0) {
			selectSticks = 1 + selectSticks % NimClass._board[selectHeap-1];
			bStickSelector.setText("Sticks: " + selectSticks);
		}
	}
	
	private void bOkAction() {
		if (selectHeap != 0 && selectSticks != 0) {		
			okSound.play(okSoundId, 1, 1, 0, 0, 1);
			
			nim._board[selectHeap-1] -= selectSticks;
			
			printHeapState();
			
			if (nim.playWith == 0) { // play with master
				// look for winner after player's move
				if (nim.lookForWinner() == 0) {
					if (nim.gameType == 0) {
						alert.setMessage("You Lose!");
						finalFail.play(finalFailId, 1, 1, 0, 0, 1);
					}
					else {
						alert.setMessage("You Win!");
						finalVictory.play(finalVictoryId, 1, 1, 0, 0, 1);
					}
					alert.show();
					return;
				}
				
				MasterTurn();
	 			
				// Look for winner after Master's turn
	 			if (nim.lookForWinner() == 0) {
					if (nim.gameType == 0) {
						alert.setMessage("You Win!");
						finalVictory.play(finalVictoryId, 1, 1, 0, 0, 1);
					}
					else {
						alert.setMessage("You Lose!");
						finalFail.play(finalFailId, 1, 1, 0, 0, 1);
					}
					alert.show();
					return;
				}
	 			
			}
			else { // play with friend
				// just look for winner
				if (nim.lookForWinner() == 0) {
					if (nim.gameType == 0) {
						if (cnt == 0) {
							alert.setMessage("Friend WON!");
						}
						else {
							alert.setMessage("You WON!");
						}
					}
					else {
						if (cnt == 0) {
							alert.setMessage("You WON!");
						}
						else {
							alert.setMessage("Friend WON!");
						}
					}
					finalVictory.play(finalVictoryId, 1, 1, 0, 0, 1);
					alert.show();
					return;
				}
				cnt = (cnt + 1) % 2;
				
				if (cnt == 0) {
					tvTurn.setText("Your turn!");
					tvLastMove.setText("Last Move: Friend took " + selectSticks + " sticks from HEAP #" + selectHeap);
				}
				else {
					tvTurn.setText("Friend's turn!");
					tvLastMove.setText("Last Move: You took " + selectSticks + " sticks from HEAP #" + selectHeap);
				}
			}
 			 			
 			// Prepare for what's coming
			bHeapSelector.setText("Heap:   ?");
			bStickSelector.setText("Sticks: ?");
			selectHeap = 0;
			selectSticks = 0;
		}
		else {
			failSound.play(failSoundId, 1, 1, 0, 0, 1);
		}
	}
	
	private void printHeapState() {
		heapState = "";
		for (i = 0; i < nim.no_heaps; i++) {
			heapState = heapState + "HEAP #" + (i+1) + ":";
			for (j = 0; j < nim._board[i]; j++) {
				heapState = heapState + " |";
			}
			heapState = heapState + "\n";
		}
		tvHeapState.setText(heapState);
	}
	
	private void makeItSleep(int millis) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + millis) { }
	}
	
	private void MasterTurn() {		
		clickable = false;
		res = nim.computerMove();
		tvTurn.setText("Master's turn");
		
		Thread masterThread = new Thread(new Runnable() {
			@Override
			public void run() {
				makeItSleep(500);
				runOnUiThread(new Runnable() {
					public void run() {
						bHeapSelector.setText("Heap:   " + res[0]);
					}
				});
				makeItSleep(500);
				runOnUiThread(new Runnable() {
					public void run() {
						bStickSelector.setText("Sticks: " + res[1]);
					}
				});
				makeItSleep(500);
				runOnUiThread(new Runnable() {
					public void run() {
						tvLastMove.setText("Last Move: Master took " + res[1] + " sticks from HEAP #" + res[0]);
						printHeapState();
						tvTurn.setText("Your turn");
						okSound.play(okSoundId, 1, 1, 0, 0, 1);
						bHeapSelector.setText("Heap:   ?");
						bStickSelector.setText("Sticks: ?");
						clickable = true;
					}
				});
			}
		});
		masterThread.start();
	}

}