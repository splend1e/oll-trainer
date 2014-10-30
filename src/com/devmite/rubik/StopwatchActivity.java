package com.devmite.rubik;

import java.util.ArrayList;
import java.util.Random;

import com.devmite.rubik.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StopwatchActivity extends Activity implements OnClickListener {

	final int MSG_START_TIMER = 0;
	final int MSG_STOP_TIMER = 1;
	final int MSG_UPDATE_TIMER = 2;
	final int SECS_IN_A_MIN = 5;

	private Stopwatch timer = new Stopwatch();
	private final int REFRESH_RATE = 10;

	private boolean isStarted, isNext = false;
	private ArrayList<Integer> arrayPos;
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_TIMER:
				timer.start(); // start timer
				mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
				break;

			case MSG_UPDATE_TIMER:
				String var = "" + timer.getElapsedTimeMin();
				if (timer.getElapsedTimeMin() < 10) {
					var = "0" + timer.getElapsedTimeMin();
				}
				minTextView.setText(var);
				
				var = "" + timer.getElapsedTimeSecs();
				if (timer.getElapsedTimeSecs() < 10) {
					var = "0" + timer.getElapsedTimeSecs();
				}
				secTextView.setText(var);
				
				var = "" + timer.getElapsedTimeMili();
				if (timer.getElapsedTimeMili() < 10) {
					var = "0" + timer.getElapsedTimeMili();
				}
				miliTextView.setText(var);
				
				mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE); // text
																					// view
																					// is
																					// updated
																					// every
																					// second,
				break; // though the timer is still running
			case MSG_STOP_TIMER:
				
				mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
				timer.stop();// stop timer
				break;

			default:
				break;
			}
		}
	};

	TextView secTextView, minTextView, miliTextView, separatorText, separatorText2, algoScrambleText, algoSolveText, algoSolveTitle ;
	ImageView imageContent;
	Button btnStartStop;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopwatch);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean showSolving = sharedPrefs.getBoolean(getResources().getString(R.string.show_solving), true);
		
		Bundle extras = getIntent().getExtras();
		arrayPos = (ArrayList<Integer>) extras.get(getResources().getString(R.string.positions));
		int[] arrayImg = extras.getIntArray("arrayImg");
		
		String[] scramble = getResources().getStringArray(R.array.algorithms_scramble);
		String[] solve = getResources().getStringArray(R.array.algorithms_solve);
		
		Random r = new Random();
		int low = 0;
		int high = arrayPos.size();
		int rand = r.nextInt(high-low) + low;
		
		algoScrambleText = (TextView) findViewById(R.id.scrambleAlgoText);
		algoScrambleText.setText(scramble[arrayPos.get(rand)]);
		
		imageContent = (ImageView) findViewById(R.id.imageContent);
		imageContent.setImageResource(arrayImg[arrayPos.get(rand)]);
		
		if (showSolving){
			algoSolveTitle = (TextView) findViewById(R.id.solveAlgoTitle);
			algoSolveTitle.setText("Solve Algorithm");
			
			algoSolveText = (TextView) findViewById(R.id.solveAlgoText);
			algoSolveText.setText(solve[arrayPos.get(rand)]);
		}
		
		secTextView = (TextView) findViewById(R.id.timeSecond);
		separatorText = (TextView) findViewById(R.id.separator);
		minTextView = (TextView) findViewById(R.id.timeMin);
		separatorText2 = (TextView) findViewById(R.id.separator2);
		miliTextView = (TextView) findViewById(R.id.timeMili);
		
		btnStartStop = (Button) findViewById(R.id.ButtonStartStop);
		btnStartStop.setOnClickListener(this);
	}
	

	public void onClick(View v) {
		isStarted = !isStarted;
		
		if (btnStartStop == v) {
			if (isStarted){
				if (isNext){
					String key = getResources().getString(R.string.positions);
					Intent intent = new Intent(this, StopwatchActivity.class);
					intent.putExtra(key, arrayPos);
					
					int[] arrayImg = new int[] { R.drawable.oll_1, R.drawable.oll_2,
							R.drawable.oll_3, R.drawable.oll_4, R.drawable.oll_5,
							R.drawable.oll_6, R.drawable.oll_7, R.drawable.oll_8,
							R.drawable.oll_9, R.drawable.oll_10, R.drawable.oll_11,
							R.drawable.oll_12, R.drawable.oll_13, R.drawable.oll_14,
							R.drawable.oll_15, R.drawable.oll_16, R.drawable.oll_17,
							R.drawable.oll_18, R.drawable.oll_19, R.drawable.oll_20,
							R.drawable.oll_21, R.drawable.oll_22, R.drawable.oll_23,
							R.drawable.oll_24, R.drawable.oll_25, R.drawable.oll_26,
							R.drawable.oll_27, R.drawable.oll_28, R.drawable.oll_29,
							R.drawable.oll_30, R.drawable.oll_31, R.drawable.oll_32,
							R.drawable.oll_33, R.drawable.oll_34, R.drawable.oll_35,
							R.drawable.oll_36, R.drawable.oll_37, R.drawable.oll_38,
							R.drawable.oll_39, R.drawable.oll_40, R.drawable.oll_41,
							R.drawable.oll_42, R.drawable.oll_43, R.drawable.oll_44,
							R.drawable.oll_45, R.drawable.oll_46, R.drawable.oll_47,
							R.drawable.oll_48, R.drawable.oll_49, R.drawable.oll_50,
							R.drawable.oll_51, R.drawable.oll_52, R.drawable.oll_53,
							R.drawable.oll_54, R.drawable.oll_55, R.drawable.oll_56,
							R.drawable.oll_57 };
					
					intent.putExtra("arrayImg", arrayImg);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					isNext = true;
					mHandler.sendEmptyMessage(MSG_START_TIMER);
					btnStartStop.setText(R.string.stop);
				}
			} else {
				mHandler.sendEmptyMessage(MSG_STOP_TIMER);
				btnStartStop.setText(R.string.next_random);
			}
		}
	}

	/*
	 * Copyright (c) 2005, Corey Goldberg
	 * 
	 * StopWatch.java is free software; you can redistribute it and/or modify it
	 * under the terms of the GNU General Public License as published by the
	 * Free Software Foundation; either version 2 of the License, or (at your
	 * option) any later version.
	 */

	public class Stopwatch {
		private long startTime = 0;
		private boolean running = false;
		private long currentTime = 0;

		public void start() {
			this.startTime = System.currentTimeMillis();
			this.running = true;
		}

		public void stop() {
			this.running = false;
		}

		public void pause() {
			this.running = false;
			currentTime = System.currentTimeMillis() - startTime;
		}

		public void resume() {
			this.running = true;
			this.startTime = System.currentTimeMillis() - currentTime;
		}

		// elaspsed time in milliseconds
		public long getElapsedTimeMili() {
			long elapsed = 0;
			if (running) {
				elapsed = ((System.currentTimeMillis() - startTime) / 10) % 100;
			}
			return elapsed;
		}

		// elaspsed time in seconds
		public long getElapsedTimeSecs() {
			long elapsed = 0;
			if (running) {
				elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
			}
			return elapsed;
		}

		// elaspsed time in minutes
		public long getElapsedTimeMin() {
			long elapsed = 0;
			if (running) {
				elapsed = (((System.currentTimeMillis() - startTime) / 1000) / 60) % 60;
			}
			return elapsed;
		}

		// elaspsed time in hours
		public long getElapsedTimeHour() {
			long elapsed = 0;
			if (running) {
				elapsed = ((((System.currentTimeMillis() - startTime) / 1000) / 60) / 60);
			}
			return elapsed;
		}
	}
}