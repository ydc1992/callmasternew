package cn.opda.message;


import cn.opda.phone.OpdaState;
import cn.opda.service.ShareService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BackStage extends Service {
	private static final String TAG = "BackStage";

	SMSObserver smsObserver = null;
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		SharedPreferences sharedPreferences = ShareService.getShare(this, "opda");
		int startService = sharedPreferences.getInt(OpdaState.STATESERVICE, 1);
		int messageService = sharedPreferences.getInt(OpdaState.MESSAGESERVICE, 1);
		Log.i(TAG, startService+"@@@@@@@@@@@@"+messageService);
		if(smsObserver!=null){
			if((startService != 1)||(messageService != 1)){
				getContentResolver().unregisterContentObserver(smsObserver);
				Log.i(TAG, "|||||||||||||||||||||");
				onDestroy();
			}
		}
		if((startService == 1)&&(messageService == 1)){
			smsObserver = new SMSObserver(new Handler(), this);
	        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
	        Log.i(TAG, "----------------------");
		}
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

		SharedPreferences sharedPreferences = ShareService.getShare(this, "opda");
		int startService = sharedPreferences.getInt(OpdaState.STATESERVICE, 1);
		int messageService = sharedPreferences.getInt(OpdaState.MESSAGESERVICE, 1);
		Log.i(TAG, startService+"++++++++"+messageService);
		if((startService == 1)&&(messageService == 1)){
			smsObserver = new SMSObserver(new Handler(), this);
	        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
		}
	}
}
