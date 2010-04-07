package cn.kfkx.mes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BlackMessage extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "todo_bm";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "todo_blacktabl";
	public final static String FIELD_id = "_id";
	public final static String FIELD_TEXT = "todo_text";
	public final static String FIELD_NUMBER = "todo_numbertext";
	public final static String FIELD_GSD="todo_gsd";

	public BlackMessage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String sql = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_id
				+ " INTEGER PRIMARY KEY," + FIELD_NUMBER + " TEXT,"
				+ FIELD_TEXT + " TEXT,"+FIELD_GSD + " TEXT)";

		db.execSQL(sql);

	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);

	}

	public Cursor select() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	public void delete(String text) {
		SQLiteDatabase db = this.getWritableDatabase();
//		String where = FIELD_NUMBER + " = ?";
//		String[] whereValue = { text };
//		db.delete(TABLE_NAME, where, whereValue);
		String where = FIELD_TEXT + " = ?";
		String[] whereValue = { text };
		db.delete(TABLE_NAME, where, whereValue);
		
//		db.execSQL("delete from todo_blacktabl where _id = ?",
//				new Object[] { text });
		db.close();
	}

	public void deleteall() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);

	}

	public void insert(String number, String text,String gsd) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* 將新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(FIELD_NUMBER, number);
		cv.put(FIELD_TEXT, text);
		cv.put(FIELD_GSD,gsd);
		

		db.insert(TABLE_NAME, null, cv);

	}

}
