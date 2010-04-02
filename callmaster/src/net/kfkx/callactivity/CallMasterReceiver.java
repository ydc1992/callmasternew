package net.kfkx.callactivity;


import java.io.IOException;
import java.util.List;

import net.kfkx.dao.DataBaseHelper;
import net.kfkx.net.upload.SendUp;
import net.kfkx.phone.Blacklist;
import net.kfkx.phone.OpdaState;
import net.kfkx.phone.WebBlack;
import net.kfkx.service.BlackListSqliteService;
import net.kfkx.service.ShareService;
import net.kfkx.service.WebBlackService;
import net.kfkx.service.WebBlackSqliteService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CallMasterReceiver extends BroadcastReceiver {
	private static final String TAG = "CallMasterReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		WebBlackService webBlackService = new WebBlackService(context);
		WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(context);
		SharedPreferences sharedPreferences = ShareService.getShare(context, "kfkx");
		int startService = sharedPreferences.getInt(OpdaState.STATESERVICE, 1);
		int beginAuto = sharedPreferences.getInt(OpdaState.BEGINAUTO, 1);
		int changeNet = sharedPreferences.getInt(OpdaState.NETSERVICE, 1);
		int sendUpService = sharedPreferences.getInt(OpdaState.SENDUP, 1);
		int netService = sharedPreferences.getInt(OpdaState.NETSERVICE, 1);
		int oldVersion = sharedPreferences.getInt(OpdaState.BLACKVERSION, 1);
		
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(context);
		List<Blacklist> list = blackListSqliteService.findUnSend();
		
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && sendUpService == 1) {
			Log.i(TAG, "11111111111111");
			if(startService==1&&list.size()>0&&changeNet==1){
				for(Blacklist blacklist : list){
					blacklist.setUptype(Blacklist.HAVED);
					SendUp.addToWeb(blacklist, context);
					blackListSqliteService.update(blacklist);
				}
			}
		}
		if(info != null && netService == 1){
			Log.i(TAG, "222222222222222");
			try {
				int version = webBlackService.getVersion();
				if(version != oldVersion || netService == 0){
					List<WebBlack> weblist = webBlackService.query();
					blackSqliteService.updateWebBlack(weblist);
					//Toast.makeText(context, "更新成功", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){ 
			if(startService == 1){
				String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);        
				Intent callintent = new Intent(context, CallOutService.class);
				callintent.putExtra("num", number);
				context.startService(callintent);
			}
		}
	
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){//开机启动服务
			if((beginAuto == 1)&&(startService == 1)){
				Intent serviceIntent = new Intent(context, CallService.class);
				Intent serIntent = new Intent(context, BlackListService.class);
				context.startService(serIntent);
				context.startService(serviceIntent);
			}
		}
		
		/*if (intent.getAction().equals(mACTION)) {
				Intent tn = new Intent(context, BackStage.class);
	            tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(tn);
		}*/
	}
}
