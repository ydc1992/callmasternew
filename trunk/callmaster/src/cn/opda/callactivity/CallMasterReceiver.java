package cn.opda.callactivity;


import java.util.List;

import cn.opda.message.BackStage;
import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.phone.OpdaState;
import cn.opda.phone.WebBlack;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.ShareService;
import cn.opda.service.WebBlackService;
import cn.opda.service.WebBlackSqliteService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CallMasterReceiver extends BroadcastReceiver {
	private static final String TAG = "CallMasterReceiver";
	private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		WebBlackService webBlackService = new WebBlackService(context);
		WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(context);
		
		SharedPreferences sharedPreferences = ShareService.getShare(context, "opda");
		int startService = sharedPreferences.getInt(OpdaState.STATESERVICE, 1);
		int beginAuto = sharedPreferences.getInt(OpdaState.BEGINAUTO, 1);
		int messageService = sharedPreferences.getInt(OpdaState.MESSAGESERVICE, 1);
		Log.i(TAG, startService+"++++++++++"+beginAuto);
		
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(context);
		List<Blacklist> list = blackListSqliteService.findUnSend();
		
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null) {
			if(startService==1){
				for(Blacklist blacklist : list){
					blacklist.setUptype(Blacklist.HAVED);
					SendUp.addToWeb(blacklist, context);
					blackListSqliteService.update(blacklist);
				}
				try {
					int version = webBlackService.getVersion();
					int oldVersion = blackSqliteService.findVersion();
					if(version != oldVersion || oldVersion == 0){
						List<WebBlack> weblist = webBlackService.query();
						blackSqliteService.updateWebBlack(weblist);
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
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
		
		if (intent.getAction().equals(mACTION)) {

				Log.i(TAG, "##########################################################");
				Intent tn = new Intent(context, BackStage.class);
	            tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(tn);
		}
	}
}
