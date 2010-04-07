package cn.kfkx.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.kfkx.dao.DataBaseHelper;
import cn.kfkx.phone.Phone;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhoneSqliteService {
	private Context context;
	private DataBaseHelper dbOpenHelper;
	public PhoneSqliteService(Context context) {
		this.context = context;
		dbOpenHelper = new DataBaseHelper(context);
	}
	public void save (Phone phone){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into phone (province, city, areaCode) values (?,?,?)" , 
				new Object[]{phone.getProvince(),phone.getCity(),phone.getAreaCode()});
		db.close();
	}
	public void delete (int phoneid){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from phone where phoneid = ?" , new Object[]{phoneid});
		db.close();
	}
	public void update (Phone phone){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update phone set province = ?, city = ?, areaCode = ? where phoneid = ?" ,
				 new Object[]{phone.getProvince(), phone.getCity(), phone.getAreaCode(),   phone.getPhoneid()});
		db.close();
	}
	public Phone find (Integer phoneid){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from phone where phoneid = ?", new String[]{phoneid.toString()});
		Phone phone = null;
		if(cursor.moveToFirst()){
			phone = new Phone(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		cursor.close();
		db.close();
		return phone;
	}
	public Phone findByAreaNum (String areaCode){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from phone where areaCode = ?", new String[]{areaCode});
		Phone phone = null;
		if(cursor.moveToFirst()){
			phone = new Phone(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		cursor.close();
		db.close();
		return phone;
	}
	public Phone findTest (String phonepre){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from phone where phonepre = ?", new String[]{phonepre});
		Phone phone = null;
		if(cursor.moveToFirst()){
			phone = new Phone(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		cursor.close();
		db.close();
		return phone;
	}
	public int getCount(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from phone",null);
		int count = 0;
		if(cursor.moveToFirst()){
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}
	public List<Phone> findAll (){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		List<Phone> phones = new ArrayList<Phone>(); 
		Cursor cursor = db.rawQuery("select * from phone", null);
		while(cursor.moveToNext()){
			Phone phone = new Phone(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			phones.add(phone);
		}
		cursor.close();
		db.close();
		return phones;
	}
}
