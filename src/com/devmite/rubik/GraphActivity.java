package com.devmite.rubik;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.devmite.rubik.database.MySQLiteHelper;
import com.devmite.rubik.database.Record;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.BarGraph.OnBarClickedListener;
import com.echo.holographlibrary.HoloGraphAnimate;

public class GraphActivity extends Activity {
	BarGraph bg;
	int[] lastFive;
	String[] lastFiveLabels, lastFiveDate;

	int top, algoNum, algoType, bestIndex;
	final int NUM_RECORD = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bargraph);

		Bundle extras = getIntent().getExtras();
		algoNum = extras.getInt(getResources().getString(R.string.algo_num));
		algoType = extras.getInt(getResources().getString(R.string.algo_type));

		final Resources resources = getResources();

		lastFive = new int[NUM_RECORD];
		lastFiveLabels = new String[NUM_RECORD];
		lastFiveDate = new String[NUM_RECORD];

		MySQLiteHelper sqlite = new MySQLiteHelper(this);
		List<Record> list = sqlite.getRecords(MySQLiteHelper.OLL, algoNum);

		TextView textLabelGraph = (TextView) findViewById(R.id.textLabelGraph);

		// find best time
		int best = Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			int timeValue = list.get(i).getTimeValue();
			if (timeValue < best) {
				best = timeValue;
				bestIndex = i;
			}
		}

		if (list.size() > 0) {

			TextView textBest = (TextView) findViewById(R.id.textBest);
			textBest.setText("BEST TIME : " + list.get(bestIndex).getTimeLabel());

			// get {{NUM_RECORD}} latest records and present it in ASC order
			// (oldest date first)

			top = (list.size() >= NUM_RECORD) ? NUM_RECORD : list.size();

			for (int i = 0; i < top; i++) {

				int j = Math.abs(top - 1 - i);
				lastFive[j] = list.get(i).getTimeValue();
				lastFiveLabels[j] = list.get(i).getTimeLabel();

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
				Date resultdate = new Date(list.get(i).getDate());
				lastFiveDate[j] = sdf.format(resultdate);

			}

			ArrayList<Bar> aBars = new ArrayList<Bar>();
			Bar bar;

			bg = (BarGraph) findViewById(R.id.bargraph);
			bg.setShowAxis(true);
			bg.setBars(aBars);

			for (int i = 0; i < top; i++) {
				bar = new Bar();
				bar.setColor(resources.getColor(R.color.green_light));
				bar.setSelectedColor(resources
						.getColor(R.color.transparent_orange));
				bar.setName(lastFiveDate[i]);
				bar.setValue(1000 / lastFive[i]);
				bar.setValueString(lastFiveLabels[i]);
				aBars.add(bar);

				// insertBar(i);
			}

			bg.setOnBarClickedListener(new OnBarClickedListener() {

				@Override
				public void onClick(int index) {
					Bar bar = bg.getBars().get(index);
					Toast.makeText(
							getApplicationContext(),
							bar.getName() + "\n"
									+ String.valueOf(bar.getValueString()),
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			textLabelGraph.setText("No history available");
			textLabelGraph.setTextSize(textLabelGraph.getTextSize() + 6);
		}

	}

	private void insertBar(int position) {
		bg.cancelAnimating(); // must clear existing to call
		// onAnimationEndListener cleanup
		// BEFORE adding new bars
		final Resources resources = getResources();

		Bar bar = new Bar();
		bar.setColor(resources.getColor(R.color.green_light));
		bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
		bar.setName(lastFiveDate[position]);
		bar.setValue(1000 / lastFive[position]);
		bar.setGoalValue(bar.getValue());
		bar.setValueString(lastFiveLabels[position]);
		bar.mAnimateSpecial = HoloGraphAnimate.ANIMATE_INSERT;
		bg.getBars().add(position, bar);

		// for (Bar b : bg.getBars()) {
		// System.out.println("b.getValue(): "+b.getValue());
		// b.setGoalValue(b.getValue());
		// b.setValueString(b.getValueString());
		// Log.d("goal val", String.valueOf(b.getGoalValue()));
		// }

		bg.setDuration(1200);// default if unspecified is 300 ms
		bg.setInterpolator(new AccelerateDecelerateInterpolator());

		bg.setAnimationListener(getAnimationListener());
		bg.animateToGoalValues();

		// int newPosition = bg.getBars().size() == 0 ? 0 : new Random()
		// .nextInt(bg.getBars().size());
		// Bar bar = new Bar();
		// bar.setColor(Color.parseColor("#AA0000FF"));
		// bar.setName("Insert bar " + String.valueOf(bg.getBars().size()));
		// bar.setValue(0);
		// bar.mAnimateSpecial = HoloGraphAnimate.ANIMATE_INSERT;
		// bg.getBars().add(newPosition, bar);
		// for (Bar b : bg.getBars()) {
		// b.setGoalValue((float) Math.random() * 1000);
		// b.setValuePrefix("$");// display the prefix throughout the
		// // animation
		// Log.d("goal val", String.valueOf(b.getGoalValue()));
		// }
		// bg.setDuration(1200);// default if unspecified is 300 ms
		// bg.setInterpolator(new AccelerateDecelerateInterpolator());// Don't
		// // use
		// // over/undershoot
		// // interpolator
		// // for
		// // insert/delete
		// bg.setAnimationListener(getAnimationListener());
		// bg.animateToGoalValues();

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public Animator.AnimatorListener getAnimationListener() {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					ArrayList<Bar> newBars = new ArrayList<Bar>();
					// Keep bars that were not deleted
					for (Bar b : bg.getBars()) {
						if (b.mAnimateSpecial != HoloGraphAnimate.ANIMATE_DELETE) {
							b.mAnimateSpecial = HoloGraphAnimate.ANIMATE_NORMAL;
							newBars.add(b);
						}
					}
					bg.setBars(newBars);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			};
		else
			return null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph_menu, menu);
		return true;
	}

	private void deleteAllHistory() {
		MySQLiteHelper sql = new MySQLiteHelper(this);
		sql.dropRecords(null);
		this.finish();
	}

	private void deleteAlgoHistory() {
		MySQLiteHelper sql = new MySQLiteHelper(this);
		sql.deleteAlgoRecords(algoType, algoNum);
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent i;

		switch (item.getItemId()) {

		case R.id.action_clear_all_history:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("Confirm clear all history");
			builder.setMessage("Are you sure you want to delete all history database?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							deleteAllHistory();
							dialog.dismiss();
						}
					});

			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
							dialog.dismiss();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

			break;

		case R.id.action_clear_algo_history:
			builder = new AlertDialog.Builder(this);

			builder.setTitle("Confirm clear history for this algorithm");
			builder.setMessage("Are you sure you want to delete history for this algorithm?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							deleteAlgoHistory();
							dialog.dismiss();
						}
					});

			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
							dialog.dismiss();
						}
					});

			alert = builder.create();
			alert.show();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

}