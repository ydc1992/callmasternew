package cn.kfkx.mes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class Sms extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// if (intent.getAction().equals(mACTION)) {
		//
		// Intent tn = new Intent(context, BackStage.class);
		//
		// tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startService(tn);
		//
		// // }
		//
		// }
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {// 开机启动服务
			SharedPreferences sharedata = context.getSharedPreferences("data",
					0);
			boolean flag = sharedata.getBoolean("item", false);
			if (flag) {
				Intent serviceIntent = new Intent(context, BackStage.class);
				serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(serviceIntent);
			}
		}

	}

}
