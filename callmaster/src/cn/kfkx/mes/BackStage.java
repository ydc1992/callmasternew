package cn.kfkx.mes;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class BackStage extends Service {
	

	SMSObserver smsObserver = null;

	public void onStart(Intent intent, int startId) {
		 smsObserver = new SMSObserver(new Handler(), this);
		getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, smsObserver);

	}

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

	public void onDestroy() {
		// TODO Auto-generated method stub
		 getContentResolver().unregisterContentObserver(smsObserver);
		super.onDestroy();
		
	}

	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 smsObserver = new SMSObserver(new Handler(), this);
		this.getContentResolver().unregisterContentObserver(smsObserver);

	}
}
