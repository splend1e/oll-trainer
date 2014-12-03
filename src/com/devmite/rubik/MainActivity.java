package com.devmite.rubik;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.devmite.rubik.R;
import com.devmite.rubik.adapter.MainListAdapter;
import com.devmite.rubik.lib.FloatingActionButton;
import com.devmite.rubik.model.ItemAlgorithm;

public class MainActivity extends Activity {
	final int TOTAL_DATA = 57;
	ArrayList<Integer> arrayPos = new ArrayList<Integer>();
	List<ItemAlgorithm> listItems = new ArrayList<ItemAlgorithm>();
	int[] arrayImg;
	MainListAdapter adapter;
	
	private void populateData() {
		String[] algorithms = getResources().getStringArray(
				R.array.algorithms_solve);
		arrayImg = new int[] { R.drawable.oll_1, R.drawable.oll_2,
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
		int j;
		for (int i = 0; i < TOTAL_DATA; i++) {
			j = i + 1;
			listItems.add(new ItemAlgorithm(BitmapFactory.decodeResource(
					getResources(), arrayImg[i]), "OLL Algorithm " + j,
					algorithms[i], false));
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		populateData();
	
		ListView listView = (ListView) findViewById(R.id.listview);

		adapter = new MainListAdapter(this, listItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
//				CheckBox cb = (CheckBox) view.findViewById(R.id.cb1);
//				cb.setChecked(checked);
				
//				 ItemAlgorithm item = listItems.get(position);
				// Toast.makeText(getApplicationContext(), ""+item.getDesc(),
				// Toast.LENGTH_LONG).show();
			}
		});

		FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.go))
				.withButtonColor(Color.WHITE)
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 40, 15).create();

		fabButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				actionNext();
			}
		});
	}

	@Override
	protected void onResume() {
		arrayPos.removeAll(arrayPos);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent i;
		
		switch (item.getItemId()) {
		case R.id.action_select_all:
			selectAll();
			break;
		case R.id.action_deselect_all:
			deselectAll();
			break;
		case R.id.action_next:
			actionNext();
			return true;
		case R.id.action_settings:
			i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, 1);
            break;
		case R.id.action_about_dev:
			i = new Intent(this, AboutDevActivity.class);
			startActivity(i);
			break;
		case R.id.action_history:
			i = new Intent(this, GraphActivity.class);
			startActivity(i);
			break;
		case R.id.action_save_lesson:
			
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private void collectArrayPos() {
		for (int i = 0; i < TOTAL_DATA; i++) {
			if (listItems.get(i).isChecked()) {
				arrayPos.add(i);
			}
		}
	}
	
	  
	private void actionNext() {
		collectArrayPos();

		if (arrayPos.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.please_choose),
					Toast.LENGTH_LONG).show();
		} else {
			String key = getResources().getString(R.string.positions);
			Intent intent = new Intent(this, PracticeActivity.class);
			intent.putExtra(key, arrayPos);
			intent.putExtra("arrayImg", arrayImg);
			startActivity(intent);
		}
	}
	
	private void selectAll() {
		for (int i = 0; i < TOTAL_DATA; i++) {
			listItems.get(i).setCb1(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	private void deselectAll() {
		for (int i = 0; i < TOTAL_DATA; i++) {
			listItems.get(i).setCb1(false);
		}
		adapter.notifyDataSetChanged();
	}
}
