package cn.opda.callactivity;


import java.util.List;

import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.phone.WebBlack;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.WebBlackService;
import cn.opda.service.WebBlackSqliteService;
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
		WebBlackService webBlackService = new WebBlackService(context);
		WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(context);
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
			try {
				int version = webBlackService.getVersion();
				int oldVersion = blackSqliteService.findVersion();
				Log.i(TAG, "++++++++"+oldVersion+"------"+version);
				if(version != oldVersion || oldVersion == 0){
					List<WebBlack> weblist = webBlackService.query();
					blackSqliteService.updateWebBlack(weblist);
				}
				Log.i(TAG, "-----------");
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
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
