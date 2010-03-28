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
	private String gsd = "ʯ��ׯ";
	// ������
	private WhiteList w = null;
	// ������
	private ToDoDB tb = null;
	// ����������
	private String white_phone = "";
	// ����������
	private String Black_phone = "";
	// ��������Ϣ���ݿ�
	private BlackMessage b_ku = null;
	// ������Ϣ���ݿ�

	private YiSiMessage y_ku = null;
	// ����cursor
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
		// ����ϵ�˱Ƚ�

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
		
		// ���Ѳ��绰���ѽӵ绰�Ƚ�
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

		// ���������ĺ���Ƚ�
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

		// ���������ĺ���Ƚϲ������ŷ������ݿ�

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
			// ע����ű仯����
			this.getContentResolver().registerContentObserver(
					Uri.parse("content://sms/"), true, content);
		}
		Log.i(TAG, c_b.getCount()+"!!!!!!!!!!!!!!!!!!!!");
		// ���������˵����ݿ�
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
			// ע����ű仯����
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
		 * @Description �����ű��͸ı�ʱ�����ø÷��� ��Ҫ����Ȩ�� android.permission.READ_SMS��ȡ����
		 *              android.permission.WRITE_SMSд����
		 * @Author Snake
		 * @Date 2010-1-12
		 */

		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);

			// ��ȡ�ռ�����ָ������Ķ���
			cursor = getContentResolver().query(Uri.parse("content://sms"),
					new String[] { "_id", "address", "read" },
					" address=? and read=?", new String[] { phone, "0" },
					"date desc");

			ContentValues values = new ContentValues();
            values.put("read", "1"); // �޸Ķ���Ϊ�Ѷ�ģʽ
            cursor.moveToFirst();
            while (cursor.isLast()) {
                // ���µ�ǰδ������״̬Ϊ�Ѷ�
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
