package cn.kfkx.callactivity;


import java.util.Date;
import java.util.List;

import cn.kfkx.net.upload.SendUp;
import cn.kfkx.phone.Blacklist;
import cn.kfkx.phone.OpdaState;
import cn.kfkx.phone.WebBlack;
import cn.kfkx.service.BlackListSqliteService;
import cn.kfkx.service.ShareService;
import cn.kfkx.service.WebBlackService;
import cn.kfkx.service.WebBlackSqliteService;


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
		int sendUpService = sharedPreferences.getInt(OpdaState.SENDUP, 1);
		int netService = sharedPreferences.getInt(OpdaState.DWONAUTO, 1);
		int oldVersion = sharedPreferences.getInt(OpdaState.BLACKVERSION, 1);
		long downTime = sharedPreferences.getLong(OpdaState.DOWNTIME, 1);
		long upTime = sharedPreferences.getLong(OpdaState.UPTIME, 1);
		
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(context);
		List<Blacklist> list = blackListSqliteService.findUnSend();
		
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if(sendUpService == 1){
			if((new Date().getTime()-upTime) > 1000*60*60*24){
				if(info!=null){
					for(Blacklist blacklist : list){
						blacklist.setUptype(Blacklist.HAVED);
						SendUp.addToWeb(blacklist, context);
						blackListSqliteService.update(blacklist);
					}
				}
			}
		}
		/*if (info != null && sendUpService == 1) {
			Log.i(TAG, "11111111111111");
			if(startService==1&&list.size()>0&&changeNet==1){
				for(Blacklist blacklist : list){
					blacklist.setUptype(Blacklist.HAVED);
					SendUp.addToWeb(blacklist, context);
					blackListSqliteService.update(blacklist);
				}
			}
		}*/
		if(netService == 1){
			if((new Date().getTime()-downTime) > 1000*60*60*24){
				if(info != null){
					Log.i(TAG, "222222222222222");
					try {
						int version = webBlackService.getVersion();
						if(version != oldVersion || netService == 0){
							List<WebBlack> weblist = webBlackService.query();
							blackSqliteService.updateWebBlack(weblist);
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				}
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
		
	}
}
