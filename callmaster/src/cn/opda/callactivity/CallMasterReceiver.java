package cn.opda.callactivity;


import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CallMasterReceiver extends BroadcastReceiver {
	private static final String TAG = "CallMasterReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null) {
			Log.i(TAG, "+++++++++++");
		} 
		if(intent.getAction().equals(Intent.ACTION_GTALK_SERVICE_CONNECTED)){ 
			Log.i(TAG, "++++++qqqqqqqqq++++");
			Intent intenetIntent = new Intent(context, IntenetService.class);
			context.startService(intenetIntent);
		}
		if(intent.getAction().equals(Intent.ACTION_GTALK_SERVICE_DISCONNECTED)){ 
			Log.i(TAG, "----------");
		}
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){ 
			String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);        
	        Intent callintent = new Intent(context, CallOutService.class);
	        callintent.putExtra("num", number);
	        context.startService(callintent);
		}
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){//开机启动服务
			Intent serviceIntent = new Intent(context, CallService.class);
			Intent serIntent = new Intent(context, BlackListService.class);
			Intent intenetIntent = new Intent(context, IntenetService.class);
			context.startService(intenetIntent);
			context.startService(serIntent);
			context.startService(serviceIntent);
		}
	}
}
