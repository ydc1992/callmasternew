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
import android.content.SharedPreferences;
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
		
		SharedPreferences sharedPreferences = context.getSharedPreferences("opda", Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
		int startService = sharedPreferences.getInt("startService", 1);
		int beginAuto = sharedPreferences.getInt("beginAuto", 1);
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
		
			if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){//������������
				if(beginAuto == 1){
					
					Intent serviceIntent = new Intent(context, CallService.class);
					serviceIntent.putExtra("startService", startService);
					Intent serIntent = new Intent(context, BlackListService.class);
					serIntent.putExtra("startService", startService);
					context.startService(serIntent);
					context.startService(serviceIntent);
				}
			}
		
		/*if (intent.getAction().equals(mACTION)) {

			StringBuilder sb = new StringBuilder();
			StringBuilder sb1 = new StringBuilder();
			// ����intent����������
			Bundle bundle = intent.getExtras();
			// �ж�intent �Ƿ�������
			if (bundle != null) {
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				 �ع������Ŷ���array,�������յ����󳤶�������array�Ĵ�С���j�p 
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < myOBJpdus.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}

				 �������Ķ��źϲ��Զ�����Ϣ��stringbuilder�� 
				for (SmsMessage currentMessage : messages) {

					 �����˵ĵ绰���� 
					sb.append(currentMessage.getDisplayOriginatingAddress());
					// ��������
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

	}*/
	}
}
