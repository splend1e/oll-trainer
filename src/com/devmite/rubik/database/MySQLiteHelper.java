package com.devmite.rubik.database;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "RecordDB";
	// Records table name
	private static final String TABLE_RECORDS = "records";

	// Records Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_ALGO_TYPE = "algo_type";
	private static final String KEY_ALGO_NUM = "algo_num";
	private static final String KEY_TIME_LABEL = "time_label";
	private static final String KEY_TIME_VALUE = "time_value";
	private static final String KEY_DATE = "date";

	// Constants for algoType
	public static final int OLL = 1;
	public static final int PLL = 2;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RECORD_TABLE = "CREATE TABLE records ( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ALGO_TYPE
				+ " INTEGER, " + KEY_ALGO_NUM + " INTEGER," + KEY_TIME_LABEL
				+ " TEXT," + KEY_TIME_VALUE + " INTEGER," + KEY_DATE
				+ " INTEGER" + ")";

		db.execSQL(CREATE_RECORD_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropRecords(db);
	}

	public void dropRecords(SQLiteDatabase db) {
		if (db == null){
			db = this.getWritableDatabase();
		}
		// Drop older records table if existed
		db.execSQL("DROP TABLE IF EXISTS records");

		// create fresh records table
		this.onCreate(db);
	}
	
	public void deleteAlgoRecords(int algoType, int algoNum){
		String query = "DELETE FROM " + TABLE_RECORDS + " WHERE " + KEY_ALGO_TYPE + "=" + algoType + " AND " + KEY_ALGO_NUM + "="
				+ algoNum + ";";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(query);
		db.close();
	}

	public void addRecord(Record record) {
		Log.d("addrecord", record.toString());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_ALGO_TYPE, record.getAlgoType());
		values.put(KEY_ALGO_NUM, record.getAlgoNum());
		values.put(KEY_TIME_LABEL, record.getTimeLabel());
		values.put(KEY_TIME_VALUE, record.getTimeValue());
		values.put(KEY_DATE, record.getDate());

		// 3. insert
		db.insert(TABLE_RECORDS, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values

		// 4. close
		db.close();
	}

	public List<Record> getRecords(int algoType, int algoNum) {
		List<Record> records = new LinkedList<Record>();
		// 1. build the query
		String query = "SELECT * FROM " + TABLE_RECORDS + " WHERE "
				+ KEY_ALGO_TYPE + "=" + algoType + " AND " + KEY_ALGO_NUM + "="
				+ algoNum + " ORDER BY " + KEY_DATE + " DESC;";
		// 2. get reference to writable DB
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build record and add it to list
		Record record = null;
		if (cursor.moveToFirst()) {
			do {
				record = new Record();
				record.setId(Integer.parseInt(cursor.getString(0)));
				record.setAlgoType(Integer.parseInt(cursor.getString(1)));
				record.setAlgoNum(Integer.parseInt(cursor.getString(2)));
				record.setTimeLabel(cursor.getString(3));
				record.setTimeValue(cursor.getInt(4));
				record.setDate(cursor.getInt(5));
				// Add record to records
				records.add(record);
			} while (cursor.moveToNext());
		}

		Log.d("getAllrecords()", records.toString());

		// return records
		return records;
	}

}
