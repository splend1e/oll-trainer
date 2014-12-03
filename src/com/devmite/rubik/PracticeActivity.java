package com.devmite.rubik;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmite.rubik.database.MySQLiteHelper;
import com.devmite.rubik.model.Record;
import com.devmite.rubik.util.Stopwatch;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

@SuppressLint("NewApi")
public class PracticeActivity extends Activity implements OnClickListener {

	final int MSG_START_TIMER = 0;
	final int MSG_STOP_TIMER = 1;
	final int MSG_UPDATE_TIMER = 2;
	final int SECS_IN_A_MIN = 5;

	private Stopwatch timer = new Stopwatch();
	private final int REFRESH_RATE = 10;

	private boolean isStarted, isNext = false;
	private ArrayList<Integer> arrayPos;
	private int algoType = MySQLiteHelper.OLL /* TEMPORARY */, algoNum;

	public static final String MY_PREFERENCES = "my_prefs";
	public static final String KEY_AD_INTERVAL = "ad_interval";
	public static final int MAX_AD_INTERVAL = 2;

	TextView minTextView, secTextView, miliTextView, separatorText,
			separatorText2, algoScrambleText, algoSolveText, algoSolveTitle;
	ImageView imageContent;
	Button btnStartStop;

	private static final int WAIT_TIME = 5000;

	// Your interstitial ad unit ID.
	private static final String AD_UNIT_ID = "ca-app-pub-9410555971413444/6629160418";

	private InterstitialAd interstitial;
	private Timer waitTimer;
	private boolean interstitialLoaded = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopwatch);

		SharedPreferences sharedPref = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);

		int count = sharedPref.getInt(KEY_AD_INTERVAL, 0);
		if (count >= MAX_AD_INTERVAL) {
			adSetup();
		}

		setNextCountAdInterval();

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean showSolving = sharedPrefs.getBoolean(
				getResources().getString(R.string.show_solving), true);

		Bundle extras = getIntent().getExtras();
		arrayPos = (ArrayList<Integer>) extras.get(getResources().getString(
				R.string.positions));
		int[] arrayImg = extras.getIntArray("arrayImg");

		String[] scramble = getResources().getStringArray(
				R.array.algorithms_scramble);
		String[] solve = getResources()
				.getStringArray(R.array.algorithms_solve);

		Random r = new Random();
		int low = 0;
		int high = arrayPos.size();
		int rand = r.nextInt(high - low) + low;
		algoNum = arrayPos.get(rand);

		algoScrambleText = (TextView) findViewById(R.id.scrambleAlgoText);
		algoScrambleText.setText(scramble[algoNum]);

		imageContent = (ImageView) findViewById(R.id.imageContent);
		imageContent.setImageResource(arrayImg[arrayPos.get(rand)]);

		if (showSolving) {
			algoSolveTitle = (TextView) findViewById(R.id.solveAlgoTitle);
			algoSolveTitle.setText("Solve Algorithm");

			algoSolveText = (TextView) findViewById(R.id.solveAlgoText);
			algoSolveText.setText(solve[arrayPos.get(rand)]);
		} else {
			algoScrambleText.setTextSize(algoScrambleText.getTextSize() + 4);
			imageContent.setVisibility(ImageView.GONE);
		}

		secTextView = (TextView) findViewById(R.id.timeSecond);
		separatorText = (TextView) findViewById(R.id.separator);
		minTextView = (TextView) findViewById(R.id.timeMin);
		separatorText2 = (TextView) findViewById(R.id.separator2);
		miliTextView = (TextView) findViewById(R.id.timeMili);

		btnStartStop = (Button) findViewById(R.id.ButtonStartStop);
		btnStartStop.setOnClickListener(this);

	}

	private void adSetup() {
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(AD_UNIT_ID);

		// Create ad request.
		AdRequest adRequest = new AdRequest.Builder().build();

		// Begin loading your interstitial.
		interstitial.loadAd(adRequest);

		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				interstitialLoaded = true;
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// The interstitial failed to load. Start the application.
				Log.d("onAdFailedToLoad", "" + errorCode);
			}
		});

	}

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

	public void onClick(View v) {
		isStarted = !isStarted;

		if (btnStartStop == v) {
			if (isStarted) {
				// after clicking "Next Random", go to the next random screen in
				// the list of chosen algorithm(s)
				if (isNext) {
					String key = getResources().getString(R.string.positions);
					Intent intent = new Intent(this, PracticeActivity.class);
					intent.putExtra(key, arrayPos);

					int[] arrayImg = new int[] { R.drawable.oll_1,
							R.drawable.oll_2, R.drawable.oll_3,
							R.drawable.oll_4, R.drawable.oll_5,
							R.drawable.oll_6, R.drawable.oll_7,
							R.drawable.oll_8, R.drawable.oll_9,
							R.drawable.oll_10, R.drawable.oll_11,
							R.drawable.oll_12, R.drawable.oll_13,
							R.drawable.oll_14, R.drawable.oll_15,
							R.drawable.oll_16, R.drawable.oll_17,
							R.drawable.oll_18, R.drawable.oll_19,
							R.drawable.oll_20, R.drawable.oll_21,
							R.drawable.oll_22, R.drawable.oll_23,
							R.drawable.oll_24, R.drawable.oll_25,
							R.drawable.oll_26, R.drawable.oll_27,
							R.drawable.oll_28, R.drawable.oll_29,
							R.drawable.oll_30, R.drawable.oll_31,
							R.drawable.oll_32, R.drawable.oll_33,
							R.drawable.oll_34, R.drawable.oll_35,
							R.drawable.oll_36, R.drawable.oll_37,
							R.drawable.oll_38, R.drawable.oll_39,
							R.drawable.oll_40, R.drawable.oll_41,
							R.drawable.oll_42, R.drawable.oll_43,
							R.drawable.oll_44, R.drawable.oll_45,
							R.drawable.oll_46, R.drawable.oll_47,
							R.drawable.oll_48, R.drawable.oll_49,
							R.drawable.oll_50, R.drawable.oll_51,
							R.drawable.oll_52, R.drawable.oll_53,
							R.drawable.oll_54, R.drawable.oll_55,
							R.drawable.oll_56, R.drawable.oll_57 };

					if (interstitialLoaded) {
						interstitial.show();
					}

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

				// add record
				String timeLabel = minTextView.getText() + ":"
						+ secTextView.getText() + ":" + miliTextView.getText();
				int timeValue = Integer
						.parseInt((String) minTextView.getText())
						* 60
						* 100
						+ Integer.parseInt((String) secTextView.getText())
						* 100
						+ Integer.parseInt((String) miliTextView.getText());
				Record rec = new Record(MySQLiteHelper.OLL, algoNum, timeValue,
						timeLabel);
				MySQLiteHelper sqlite = new MySQLiteHelper(this);
				sqlite.insertRecord(rec);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practice_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent i;

		switch (item.getItemId()) {
		case R.id.action_history:
			i = new Intent(this, GraphActivity.class);
			String keyAlgoNum = getResources().getString(R.string.algo_num);
			String keyAlgoType = getResources().getString(R.string.algo_type);

			i.putExtra(keyAlgoNum, algoNum);
			i.putExtra(keyAlgoType, MySQLiteHelper.OLL);

			startActivity(i);
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private void setNextCountAdInterval() {
		SharedPreferences sharedPref = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		int count = sharedPref.getInt(KEY_AD_INTERVAL, 0);
		count = count <= MAX_AD_INTERVAL ? count + 1 : 0;
		editor.putInt(KEY_AD_INTERVAL, count);
		editor.apply();

	}

}