package com.devmite.rubik.helper;

import android.content.Context;

public class Storage {
	private static Context cntStorageContext = null;

	public static Context getContext() {
		return cntStorageContext;
	}

	public static void setContext(Context context) {
		Storage.cntStorageContext = context;
	}

};