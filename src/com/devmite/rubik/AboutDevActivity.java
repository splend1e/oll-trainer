package com.devmite.rubik;

import com.devmite.rubik.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutDevActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		TextView wwwText = (TextView) findViewById(R.id.wwwDevmite);

		wwwText.setText(Html.fromHtml("<a href=\"http://www.devmite.com\">devmite.com</a> "));
		wwwText.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
