package cn.opda.callactivity;


import java.util.List;

import cn.opda.message.BackStage;
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
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class CallMasterReceiver extends BroadcastReceiver {
	private static final String TAG = "CallMasterReceiver";
	private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED";
	private String phone="";
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
		if (intent.getAction().equals(mACTION)) {

			StringBuilder sb = new StringBuilder();
			StringBuilder sb1 = new StringBuilder();
			// 接受intent传来的数据
			Bundle bundle = intent.getExtras();
			// 判断intent 是否有数据
			if (bundle != null) {
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				/* 构建短信对象array,并依据收到对象长度来创建array的大小 */
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < myOBJpdus.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}

				/* 将送来的短信合并自定义信息于stringbuilder中 */
				for (SmsMessage currentMessage : messages) {

					/* 发信人的电话号码 */
					sb.append(currentMessage.getDisplayOriginatingAddress());
					// 短信内容
					sb1.append(currentMessage.getDisplayMessageBody());
					
				}

			}

			String phone_t = sb.toString();
			String body = sb1.toString();
			
//			 if (phone.length() == 11 && isPhoneNumberValid(phone)) {
			if(phone_t.length()==14){
				phone=phone_t.substring(3,phone_t.length());
				
			}else{
				phone=phone_t;
			}
		
			Intent tn = new Intent(context, BackStage.class);
			Bundle bn = new Bundle();
			bn.putString("se_a", phone);
			bn.putString("se_b", body);
			tn.putExtras(bn);
            tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(tn);

		//}

	}
	}
}
