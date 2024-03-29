package cn.kfkx.callactivity;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.kfkx.dao.DataBaseHelper;
import cn.kfkx.phone.OpdaState;
import cn.kfkx.phone.Phone;
import cn.kfkx.service.BelongingService;
import cn.kfkx.service.BlackListSqliteService;
import cn.kfkx.service.PhoneSqliteService;
import cn.kfkx.service.ShareService;
import cn.kfkx.service.WebBlackSqliteService;



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
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	public void onCreate() {
		
		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {

			myDbHelper.createDataBase();
			myDbHelper.close();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}
			Log.i(TAG, "++++++++++");
			/* 取得电话服务 */
			final TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			PhoneStateListener listener = new PhoneStateListener(){	
				boolean isRunning = false;  
				Phone phone = new Phone("未知地区", "", "");
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
						SharedPreferences preferences = ShareService.getShare(CallService.this, "kfkx");
						if(preferences.getInt(OpdaState.STATESERVICE, 1)==1){
							SharedPreferences sharedPreferences = ShareService.getShare(CallService.this, "kfkx");
							int areaState = sharedPreferences.getInt(OpdaState.AREASERVICE, 1);
						    if (blackService.findByNumber(incomingNumber)== null&&webBlackService.findByNumber(incomingNumber)==null&&areaState==1){
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
