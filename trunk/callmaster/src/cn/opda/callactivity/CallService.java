package cn.opda.callactivity;


import java.util.Timer;
import java.util.TimerTask;

import cn.opda.phone.Phone;
import cn.opda.service.BelongingService;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.PhoneSqliteService;
import cn.opda.service.WebBlackSqliteService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallService extends Service {
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private WebBlackSqliteService webBlackService = new WebBlackSqliteService(this);
	private static final String TAG = "TelService";
	private BelongingService belongservice = new BelongingService(this);
	private PhoneSqliteService phoneService = new PhoneSqliteService(this);
	SharedPreferences sharedPreferences = this.getSharedPreferences("opda", Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
	int startService = sharedPreferences.getInt("startService", 1);
	int beginAuto = sharedPreferences.getInt("beginAuto", 1);
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	public void onCreate() {
		if(startService==1){
			Log.i(TAG, "++++++++++");
			/* 取得电话服务 */
			final TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			PhoneStateListener listener = new PhoneStateListener(){	
				boolean isRunning = false;  
				Phone phone = new Phone("未知", "未知", "");
				@Override
				public void onCallStateChanged(int state, String incomingNumber) {
					final String number = incomingNumber;
					switch (state){
					case TelephonyManager.CALL_STATE_IDLE:  //无任何状态时 
						isRunning = false;
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:  //接起电话时 
						isRunning = false;
						break;	
					case TelephonyManager.CALL_STATE_RINGING:  //电话进来时 
						isRunning = true;
						if (blackService.findByNumber(incomingNumber)== null&&webBlackService.findByNumber(incomingNumber)==null){
							
							
							Timer timer = new Timer();
							int firstNum = Integer.parseInt(String.copyValueOf(number.toCharArray(), 0, 1));
							int secondNum = Integer.parseInt(String.copyValueOf(number.toCharArray(), 1, 1));
							if(firstNum==0){
								if(secondNum==1||secondNum==2){
									Phone phonetemp = phoneService.findByAreaNum(String.copyValueOf(number.toCharArray(), 0, 3));
									if(phonetemp!=null){
										phone = phonetemp;
									}
								}else {
									Phone phonetemp = phoneService.findByAreaNum(String.copyValueOf(number.toCharArray(), 0, 4));
									if(phonetemp!=null){
										phone = phonetemp;
									}
								}
							}else if(firstNum==1){
								try {
									String pre = "0"+belongservice.read(number);
									Phone phonetemp = phoneService.findByAreaNum(pre);
									if(phonetemp!=null){
										phone = phonetemp;
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							timer.schedule(new TimerTask(){  
								Toast mToast = Toast.makeText(getApplicationContext(), phone.getProvince()+phone.getCity(), Toast.LENGTH_LONG);  
								public void run() {  
									while(isRunning){  
										mToast.show();  
									}  
								}  
								
							}, 10);
						}
						
						break;
					}
					super.onCallStateChanged(state, incomingNumber);
				}        	
			};
			//监听电话的状态
			telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
}
