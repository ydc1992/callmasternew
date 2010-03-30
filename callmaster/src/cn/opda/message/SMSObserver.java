package cn.opda.message;

import java.util.ArrayList;
import java.util.List;

import cn.opda.service.WebBlackSqliteService;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.CallLog.Calls;
import android.provider.Contacts.People.Phones;
import android.util.Log;

/**
 * 拦截短信并将符合条件的短信插入到指定数据库，并删除inbox中的数据
 * 
 * @author Administrator
 * 
 */
public class SMSObserver extends ContentObserver {
	private Context ctx;
	private ContentResolver contentResolver;
	private final static String SMS_INBOX_URI = "content://sms//inbox";
	private final static String CONVERSATION_URI = "content://sms/conversations/";

	public static final String TAG = "sms";

	public final static String DB_NAME = "cooperation_db";
	public final static int VERSION = 1;

	// 联系人的cursor
	private Cursor c_lian = null;
	// 已拨电话的cursor
	private Cursor c_bo = null;
	// 白名单
	private WhiteList w = null;
	// 白名单的cursor
//	private Cursor c_white = null;
//	// 黑名单的Cursor
//	private Cursor c_balck = null;
	// 黑名单数据库
	private ToDoDB tb = null;
	// 黑名单信息数据库
	private BlackMessage b_ku = null;
	// 疑似信息数据库

	private YiSiMessage y_ku = null;
	private WebBlackSqliteService webBlackSqliteService = null;
	
	private int notificationtype = 0;

	public SMSObserver(Handler handler, Context ctx) {
		super(handler);
		this.ctx = ctx;
		this.contentResolver = ctx.getContentResolver();

		this.w = new WhiteList(ctx);
		this.tb = new ToDoDB(ctx);
		this.y_ku = new YiSiMessage(ctx);
		this.b_ku = new BlackMessage(ctx);
		webBlackSqliteService = new WebBlackSqliteService(ctx);
	}

	@Override
	public void onChange(boolean selfChange) {
		Log.v(TAG, "capture a message into inbox!");
		super.onChange(selfChange);
		contentResolver = ctx.getContentResolver();
		Uri uri = Uri.parse(SMS_INBOX_URI);
		String[] queryColumn = new String[] { "_id,thread_id,address,person,date,protocol,read,status,type,"
				+ "reply_path_present,subject,body,service_center" };
		Cursor cursor = contentResolver.query(uri, queryColumn, " read=?",
				new String[] { "0" }, "date desc");

		if (cursor != null) {
			List<SMSEntity> smsEntitys = buildSMSEntity(cursor);

			List<SMSEntity> filteredEntitys = filterSMSEntity(smsEntitys);
			deleteSpecSMS(filteredEntitys);

			if(notificationtype != 0){
				
//				ShowNotyfy(smsEntitys);
				 NotificationManager manager =
				 (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
				 CooperationNotification cn = new
				 CooperationNotification(ctx,manager,notificationtype);
				 cn.send();
			}
			// smsEntitys = helper.getUnreadSMS();
			// NotificationManager manager =
			// (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
			// CooperationNotification cn = new
			// CooperationNotification(ctx,smsEntitys,manager);
			// cn.send();
		}
	}

	private int deleteSpecSMS(List<SMSEntity> smsEntitys) {
		int count = 0;
		for (SMSEntity smsEntity : smsEntitys) {
			Uri thread = Uri.parse(CONVERSATION_URI + smsEntity.getThreadID());
			contentResolver.delete(thread, "_id=?", new String[] { String
					.valueOf(smsEntity.getId()) });
			count++;
		}
		return count;
	}

	public static List<SMSEntity> buildSMSEntity(Cursor cursor) {
		List<SMSEntity> smsEntitys = new ArrayList<SMSEntity>(10);

		while (cursor.moveToNext()) {
			SMSEntity smsEntity = new SMSEntity();
			smsEntity.setId(cursor.getLong(0));
			smsEntity.setThreadID(cursor.getLong(1));
			smsEntity.setFrom(cursor.getString(2));
			smsEntity.setPerson(cursor.getString(3));
			smsEntity.setDate(cursor.getString(4));
			smsEntity.setProtocol(cursor.getString(5));
			smsEntity.setRead(cursor.getInt(6));
			smsEntity.setStatus(cursor.getInt(7));
			smsEntity.setType(cursor.getString(8));
			smsEntity.setReplyPathPresent(cursor.getString(9));
			smsEntity.setSubject(cursor.getString(10));
			smsEntity.setBody(cursor.getString(11));
			smsEntity.setServiceCenter(cursor.getString(12));

			smsEntitys.add(smsEntity);
		}
		return smsEntitys;
	}

	private List<SMSEntity> filterSMSEntity(List<SMSEntity> smsEntitys) {
		List<SMSEntity> filteredSMSEntitys = new ArrayList<SMSEntity>();
		// for(SMSEntity smsEntity : smsEntitys){
		// if(smsEntity.getFrom().indexOf("18602240860") != -1){
		// // if(smsEntity.getBody().indexOf(FILTER_BODY) != -1){
		// filteredSMSEntitys.add(smsEntity);
		// }
		// }
		notificationtype = 0;
		
		for (SMSEntity smsEntity : smsEntitys) {

			String number = smsEntity.getFrom();
			String body = smsEntity.getBody();

			if (number.startsWith("+86")) {
				number = number.substring(3);
			} else if (number.startsWith("106")) {
				// number.replace("106", "");
			} else if (number.startsWith("12520")) {
				number = number.substring(5);
			}
			Log.i("test", number + "");

			// String[] pro = { People.NUMBER };
			// 跟联系人比较
			c_lian = contentResolver.query(
					android.provider.Contacts.Phones.CONTENT_URI, null,
					Phones.NUMBER + " = ?", new String[] { number },
					Contacts.People.DEFAULT_SORT_ORDER);
			if (c_lian.getCount() > 0) {

			} else {
				// 跟已拨电话和已接电话比较
				c_bo = contentResolver.query(CallLog.Calls.CONTENT_URI, null,
						Calls.NUMBER + " = ?", new String[] { number },
						CallLog.Calls.DEFAULT_SORT_ORDER);

				if (c_bo.getCount() > 0) {

				} else {
					// 跟白名单的号码比较
					// c_white = w.findByNumber(number);
					// if (c_white.getCount() > 0) {
					if (w.findByNumber(number)) {

					} else {
						// 跟黑名单的号码比较并将短信放入数据库
						// c_balck = tb.findByNumber(number);
						// if (c_balck.getCount() > 0) {
						if (tb.findByNumber(number)) {

							String area = "";
							try {
								area = webBlackSqliteService.findArea(number);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							b_ku.insert(number + "\t" + "\t" + "\t" + "\t"
									+ "\t" + area, body, "");

							filteredSMSEntitys.add(smsEntity);
//							ShowNotyfy(smsEntitys);
							notificationtype = 2;
						} else {
							// 和疑似人比较
							// if (c_lian.getCount() == 0 & c_bo.getCount() == 0
							// & c_white.getCount() == 0 & c_balck.getCount() ==
							// 0) {

							try {
								y_ku.insert(number, body);
								filteredSMSEntitys.add(smsEntity);
//								ShowNotyfy(smsEntitys);
								notificationtype = 1;
							} catch (Exception e) {
								// TODO Auto-generated catch block

							}
							// }
						}
					}
				}
				c_bo.close();
			}
			c_lian.close();
		}
		return filteredSMSEntitys;
	}

//	private void ShowNotyfy(List<SMSEntity> smsEntitys) {
//
//		// Intent it = new Intent(ctx, Notyfy.class);
//		// Bundle be = new Bundle();
//		// be.putString("no_phone", phone);
//		// be.putString("no_body", body);
//		// it.putExtras(be);
//		// it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// ctx.startActivity(it);
//		
//		
//		NotificationManager manager = (NotificationManager) ctx
//				.getSystemService(Context.NOTIFICATION_SERVICE);
////		manager.cancel(CooperationNotification.SIMPLE_NOTIFICATION_ID);
//		CooperationNotification cn = new CooperationNotification(ctx,
//				smsEntitys, manager);
//		cn.send();
//	}

}
