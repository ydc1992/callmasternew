package cn.opda.message;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class BackStage extends Service {
	private static final String TAG = "BackStage";

	SMSObserver smsObserver = null;
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
//		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		manager.cancelAll();
        
//		SmsContent content = new SmsContent(new Handler());
//		// ×¢²á¶ÌÐÅ±ä»¯¼àÌý
//		this.getContentResolver().registerContentObserver(
//				Uri.parse("content://sms/"), true, content);
//        smsObserver = new SMSObserver(new Handler(), this);
//        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);

	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getContentResolver().unregisterContentObserver(smsObserver);
		super.onDestroy();
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
        smsObserver = new SMSObserver(new Handler(), this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
	}
}
