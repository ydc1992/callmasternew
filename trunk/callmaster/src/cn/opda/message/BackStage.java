package cn.opda.message;

import cn.opda.service.WebBlackSqliteService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.CallLog.Calls;
import android.provider.Contacts.People;
import android.provider.Contacts.People.Phones;
import android.util.Log;

public class BackStage extends Service {
	private static final String TAG = "BackStage";

	private String phone = "";
	private String body = "";
	private String number = "";
	private String c_number = "";
	private String gsd = "石家庄";
	// 白名单
	private WhiteList w = null;
	// 黑名单
	private ToDoDB tb = null;
	// 白名单号码
	private String white_phone = "";
	// 黑名单号码
	private String Black_phone = "";
	// 黑名单信息数据库
	private BlackMessage b_ku = null;
	// 疑似信息数据库

	private YiSiMessage y_ku = null;
	// 短信cursor
	private Cursor c_xinxi = null;
	private ContentResolver cr = null;

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onStart(Intent intent, int startId) {
		WebBlackSqliteService webBlackSqliteService = new WebBlackSqliteService(this);
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		w = new WhiteList(this);
		tb = new ToDoDB(this);
		b_ku = new BlackMessage(this);
		y_ku = new YiSiMessage(this);
		cr = this.getContentResolver();
		Bundle b = intent.getExtras();
		phone = b.getString("se_a");
		body = b.getString("se_b");
		// 跟联系人比较

		String[] pro = { People.NUMBER };
		Cursor cursor = cr.query(android.provider.Contacts.Phones.CONTENT_URI,
				null, Phones.NUMBER+ " = " + phone, null, Contacts.People.DEFAULT_SORT_ORDER);
		if(cursor.getCount()>0){
			Intent i = new Intent(BackStage.this, Notyfy.class);
			Bundle be = new Bundle();
			be.putString("no_phone", phone);
			be.putString("no_body", body);
			i.putExtras(be);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BackStage.this.startActivity(i);
		}
		Log.i(TAG, cursor.getCount()+"+++++++++++++++++++");
		
		// 跟已拨电话和已接电话比较
		Cursor c_dail = cr.query(CallLog.Calls.CONTENT_URI,
				null, Calls.NUMBER+ " = " + phone, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		if(c_dail.getCount()>0){
			Intent i = new Intent(BackStage.this, Notyfy.class);
			Bundle be = new Bundle();
			be.putString("no_phone", phone);
			be.putString("no_body", body);
			i.putExtras(be);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BackStage.this.startActivity(i);
		}
		Log.i(TAG, c_dail.getCount()+"------------------");

		// 跟白名单的号码比较
		Cursor c = w.findByNumber(phone);
		if(c.getCount()>0){
			Intent i = new Intent(BackStage.this, Notyfy.class);
			Bundle be = new Bundle();
			be.putString("no_phone", phone);
			be.putString("no_body", body);
			i.putExtras(be);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BackStage.this.startActivity(i);
		}
		Log.i(TAG, c.getCount()+"@@@@@@@@@@@@@@@@@@");

		// 跟黑名单的号码比较并将短信放入数据库

		Cursor c_b = tb.findByNumber(phone);
		if(c_b.getCount()>0){
			try {
				Log.i(TAG, webBlackSqliteService.findArea(phone)+"555555555555");
				b_ku.insert(phone, body, gsd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.getMessage());
			}
			Intent i = new Intent(BackStage.this, Notyfy.class);
			Bundle be = new Bundle();
			be.putString("no_phone", phone);
			be.putString("no_body", body);
			i.putExtras(be);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BackStage.this.startActivity(i);

			SmsContent content = new SmsContent(new Handler(), phone);
			// 注册短信变化监听
			this.getContentResolver().registerContentObserver(
					Uri.parse("content://sms/"), true, content);
		}
		Log.i(TAG, c_b.getCount()+"!!!!!!!!!!!!!!!!!!!!");
		// 放入疑似人的数据库
		if (cursor.getCount()==0 & c_dail.getCount()==0
				& c.getCount()==0 & c_b.getCount()==0) {

			try {
				y_ku.insert(phone, body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.getMessage());
			}
			Intent i = new Intent(BackStage.this, Notyfy.class);
			Bundle be = new Bundle();
			be.putString("no_phone", phone);
			be.putString("no_body", body);
			i.putExtras(be);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BackStage.this.startActivity(i);

			SmsContent content = new SmsContent(new Handler(), phone);
			// 注册短信变化监听
			this.getContentResolver().registerContentObserver(
					Uri.parse("content://sms/"), true, content);

		}

	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class SmsContent extends ContentObserver {
		private Cursor cursor = null;
		private String phone = "";

		public SmsContent(Handler handler, String phone) {
			super(handler);
			this.phone = phone;
		}

		/**
		 * @Description 当短信表发送改变时，调用该方法 需要两种权限 android.permission.READ_SMS读取短信
		 *              android.permission.WRITE_SMS写短信
		 * @Author Snake
		 * @Date 2010-1-12
		 */

		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);

			// 读取收件箱中指定号码的短信
			cursor = getContentResolver().query(Uri.parse("content://sms"),
					new String[] { "_id", "address", "read" },
					" address=? and read=?", new String[] { phone, "0" },
					"date desc");

			ContentValues values = new ContentValues();
            values.put("read", "1"); // 修改短信为已读模式
            cursor.moveToFirst();
            while (cursor.isLast()) {
                // 更新当前未读短信状态为已读
                getContentResolver().update(Uri.parse("content://sms/inbox"),
                        values, " _id=?",
                        new String[] { "" + cursor.getInt(0) });
                cursor.moveToNext();
            }

            getContentResolver().delete(Uri.parse("content://sms"),
                    "address=?", new String[] { phone });
            // cursor.moveToNext();

            Log.i("test", phone);
		}
	}
}
