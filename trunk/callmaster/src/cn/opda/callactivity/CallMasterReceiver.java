package cn.opda.callactivity;


import java.util.List;

import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.service.BlackListSqliteService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CallMasterReceiver extends BroadcastReceiver {
	private static final String TAG = "CallMasterReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(context);
		List<Blacklist> list = blackListSqliteService.findUnSend();
		
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null) {
			for(Blacklist blacklist : list){
				blacklist.setUptype(Blacklist.HAVED);
				SendUp.addToWeb(blacklist, context);
				blackListSqliteService.update(blacklist);
			}
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
