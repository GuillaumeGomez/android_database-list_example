package com.dbexample;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "db";
	private String	TABLE_NAME = "";
	private static final int DATABASE_VERSION = 1;

	private static final String KEY_ID = "id";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_TITLE = "name";

	public DatabaseHandler(Context context, String table_name) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		TABLE_NAME = table_name;
		this.onCreate(this.getWritableDatabase());
	}

	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESCRIPTION + " TEXT,"
				+ KEY_TITLE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		List<DataHandler> values_map = getValues();

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		onCreate(db);
		if (values_map.size() > 0){
			for (DataHandler entry : values_map ) {
				this.addValue(entry.title, entry.description);
			}
		}
	}

	public boolean addValue(String title, String description){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean ret_value = false;

		ContentValues values = new ContentValues();
		values.put(KEY_DESCRIPTION, description);
		values.put(KEY_TITLE, title);
		ret_value = (db.insert(TABLE_NAME, null, values) != -1);
		db.close();
		return ret_value;
	}

	public ArrayList<DataHandler> getValues() {
		ArrayList<DataHandler>	ret = new ArrayList<DataHandler>();
		int x = 0;

		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ret.add(new DataHandler(Integer.valueOf(cursor.getString(0)), cursor.getString(2), cursor.getString(1), x++));
			} while (cursor.moveToNext());
		}
		return ret;
	}

	public DataHandler	getValue(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,	KEY_DESCRIPTION, KEY_TITLE },
				KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor == null)
			return null;

		DataHandler data = null;

		while (cursor.moveToNext()){
			data = new DataHandler(Integer.valueOf(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		}
		return data;
	}

	public void	deleteData(int id){
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}
}
