package cn.opda.message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class WhiteList extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "todo_wt";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "todo_whitetabl";
	public final static String FIELD_id = "_id";
	public final static String FIELD_TEXT = "todo_text";

	public WhiteList(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub

	}

	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_id
				+ " INTEGER primary key autoincrement, " + " " + FIELD_TEXT
				+ " text)";
		db.execSQL(sql);

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);

	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	public boolean findByNumber(String number){
		
		boolean rt = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from todo_whitetabl where todo_text = ?", new String[]{number});
		
		if(cursor.getCount() > 0){
			
			rt = true;
		}
			
		cursor.close();
		return rt;
	}
	public void insert(String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* 將新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(FIELD_TEXT, text);
		db.insert(TABLE_NAME, null, cv);

	}

	public void delete(String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_TEXT + " = ?";
		String[] whereValue = { text };
		db.delete(TABLE_NAME, where, whereValue);
	}

	public void deleteall() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);

	}

}
