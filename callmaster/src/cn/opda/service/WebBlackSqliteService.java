package cn.opda.service;

import java.util.ArrayList;
import java.util.List;

import cn.opda.dao.DataBaseHelper;
import cn.opda.phone.WebBlack;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WebBlackSqliteService {
	private Context context;
	private DataBaseHelper dbOpenHelper;
	public WebBlackSqliteService(Context context) {
		this.context = context;
		dbOpenHelper = new DataBaseHelper(context);
	}
	public void save (WebBlack webBlack ){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into webblack (number,type,remark,timehappen,timelength) values (?,?,?,?,?)" , 
				new Object[]{webBlack.getNumber(),webBlack.getType(),webBlack.getRemark(),webBlack.getTimehappen(),webBlack.getTimelength()});
		db.close();
	}
	public void delete (int webBlackid){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from webblack where blackid = ?" , new Object[]{webBlackid});
		db.close();
	}
	public void deleteByNumber (String number){
    SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    db.execSQL("delete from webblack where number = ?" , new Object[]{number});
    db.close();
  }
	public void update (WebBlack webblack){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update webblack set number = ?,type = ? ,remark = ? ,timehappen = ?,timelength = ?where blackid = ?" ,
				 new Object[]{webblack.getNumber(),webblack.getType(),webblack.getRemark(),
				webblack.getTimehappen(),webblack.getTimelength(),webblack.getBlackid()});
		db.close();
	}
	public WebBlack find (Integer webBlackid){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from webblack where blackid = ?", new String[]{webBlackid.toString()});
		WebBlack phone = null;
		if(cursor.moveToFirst()){
			phone = new WebBlack(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
		}
		cursor.close();
		db.close();
		return phone;
	}
	 public WebBlack findByNumber (String number){
	    SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
	    Cursor cursor = db.rawQuery("select * from webblack where number = ?", new String[]{number});
	    WebBlack phone = null;
	    if(cursor.moveToFirst()){
	      phone = new WebBlack(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
	    }
	    cursor.close();
	    db.close();
	    return phone;
	  }
	public int getCount (){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from webblack",null);
		int count = 0;
		if(cursor.moveToFirst()){
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}
	public List<WebBlack> findAll (){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		List<WebBlack> blacks = new ArrayList<WebBlack>();
		Cursor cursor = db.rawQuery("select * from webblack ", null);
		while(cursor.moveToNext()){
			WebBlack phone = new WebBlack(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
		  blacks.add(phone);
		}
		cursor.close();
		db.close();
		return blacks;
	}
}
