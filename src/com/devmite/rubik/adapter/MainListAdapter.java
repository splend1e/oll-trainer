package com.devmite.rubik.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devmite.rubik.R;
import com.devmite.rubik.model.ItemAlgorithm;

public class MainListAdapter extends BaseAdapter {

	private List<ItemAlgorithm> mList;
	private LayoutInflater inflater;
	
	public MainListAdapter(Context context, List<ItemAlgorithm> mListe) {
		inflater = LayoutInflater.from(context);
		this.mList = mListe;
	}

	public List<ItemAlgorithm> getmList() {
		return mList;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ItemAlgorithm getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listview_layout, null);

		LinearLayout item = (LinearLayout) convertView.findViewById(R.id.outer);
		int myColor = Color.parseColor("#88FFFFFF");
		item.setBackgroundColor(myColor);

		ImageView image = (ImageView) convertView.findViewById(R.id.img);
		image.setImageBitmap(mList.get(position).getImg());
		image.setPadding(5, 5, 0, 5);

		TextView titleText = (TextView) convertView.findViewById(R.id.title);
		titleText.setText(mList.get(position).getTitle());

		TextView descText = (TextView) convertView
				.findViewById(R.id.descTextView);
		descText.setText(mList.get(position).getDesc());

		final CheckBox cb1Recup = (CheckBox) convertView.findViewById(R.id.cb1);
		cb1Recup.setChecked(mList.get(position).isChecked());
		cb1Recup.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				if (cb1Recup.isChecked() == true) {
					mList.get(position).setCb1(true);
				} else {
					mList.get(position).setCb1(false);
				}

			}
		});
		if (mList.get(position).getDesc().compareTo("") == 0) {
			descText.setHeight(0);
			descText.setWidth(0);
		}
		return convertView;
	}

}
