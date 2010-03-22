package cn.opda.service;

import java.util.ArrayList;
import java.util.List;

import cn.opda.dao.DataBaseHelper;
import cn.opda.phone.Blacklist;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackListSqliteService {
	private Context context;
	private DataBaseHelper dbOpenHelper;
	public BlackListSqliteService(Context context) {
		this.context = context;
		dbOpenHelper = new DataBaseHelper(context);
	}
	public void saveall (Blacklist black ){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into blacklist (number, type, remark, timelength, timehappen, uptype) values (?,?,?,?,?,?)" , 
				new Object[]{black.getNumber(),black.getType(),black.getRemark(),black.getTimelength(),black.getTimehappen(),black.getUptype()});
		db.close();
	}
	public void savepart (Blacklist black ){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into blacklist (number,type,remark,uptype) values (?,?,?,?)" , 
				new Object[]{black.getNumber(),black.getType(),black.getRemark(),black.getUptype()});
		db.close();
	}
	public void delete (int blackid){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from blacklist where blackid = ?" , new Object[]{blackid});
		db.close();
	}
	public void deleteByNumber (String number){
    SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    db.execSQL("delete from blacklist where number = ?" , new Object[]{number});
    db.close();
  }
	public void update (Blacklist blacklist){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update blacklist set number = ?,type = ?,remark = ? ,timelength = ?, timehappen = ?, uptype = ? where blackid = ?" ,
				 new Object[]{blacklist.getNumber(),blacklist.getType(),blacklist.getRemark(),blacklist.getTimelength()
				, blacklist.getTimehappen(),blacklist.getUptype(),blacklist.getBlackid()});
		db.close();
	}
	public Blacklist find (Integer blackid){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacklist where blackid = ?", new String[]{blackid.toString()});
		Blacklist phone = null;
		if(cursor.moveToFirst()){
			phone = new Blacklist(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
					  cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
		}
		cursor.close();
		db.close();
		return phone;
	}
	 public Blacklist findByNumber (String number){
	    SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
	    Cursor cursor = db.rawQuery("select * from blacklist where number = ?", new String[]{number});
	    Blacklist phone = null;
	    if(cursor.moveToFirst()){
	      phone = new Blacklist(cursor.getInt(0), cursor.getString(1),cursor.getInt(2), cursor.getString(3),
				  cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
	    }
	    cursor.close();
	    db.close();
	    return phone;
	  }
	public int getCount (){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacklist",null);
		int count = 0;
		if(cursor.moveToFirst()){
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}
	public List<Blacklist> findAll (){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		List<Blacklist> blacks = new ArrayList<Blacklist>(); 
		Cursor cursor = db.rawQuery("select * from blacklist ", null);
		while(cursor.moveToNext()){
		  Blacklist phone = new Blacklist(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
				  cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
		  blacks.add(phone);
		}
		cursor.close();
		db.close();
		return blacks;
	}
	public List<Blacklist> findUnSend(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		List<Blacklist> blacks = new ArrayList<Blacklist>(); 
		Cursor cursor = db.rawQuery("select * from blacklist where uptype = ?", new String[]{String.valueOf(Blacklist.HAVE_NO)});
		while(cursor.moveToNext()){
			Blacklist phone = new Blacklist(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
					cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
			blacks.add(phone);
		}
		cursor.close();
		db.close();
		return blacks;
	}
}
