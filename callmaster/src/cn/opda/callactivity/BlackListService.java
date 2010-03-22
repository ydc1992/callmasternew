package cn.opda.callactivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.opda.R;
import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.service.BlackListSqliteService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.provider.Contacts;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BlackListService extends Service {
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private static final String TAG = "BlackListService";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		PhoneStateListener listener = new PhoneStateListener(){	
			long time = 0;
			long ringingtime = 0;
			public void onCallStateChanged(int state, String incomingNumber) {
				final String num = incomingNumber;
			      switch (state){
			        case TelephonyManager.CALL_STATE_IDLE:  //无任何状态时 
			        	if(ringingtime != 0){
			        		time = new Date().getTime() - ringingtime;
			        		Log.i(TAG, "++++++++++"+time);
					        	if(time < 1000*3.5){
					        		if(blackService.findByNumber(num) == null){
					        			//CONTACT_ID
					        			Cursor cursor = getContentResolver().query(Contacts.People.CONTENT_URI,  
					        			        null,  
					        			        Contacts.People.NUMBER +" = "+num,  
					        			        null, null);
					        			Log.i(TAG, cursor.getCount()+"+++++++++++++++");
					        			if(cursor.getCount() == 0){
					        				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					        				String tt = format.format(new Date());
					        				Integer i = Integer.parseInt(time/1000+1+"");
					        				Blacklist blacklist = new Blacklist(num,Blacklist.TYPE_ONESOUND,"",tt,i,Blacklist.HAVE_NO);
					        				blackService.saveall(blacklist);
											ConnectivityManager connectivity = (ConnectivityManager)BlackListService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
											if (connectivity != null) {
													Log.i(TAG, "+++++++++++");
													Blacklist black = blackService.findByNumber(num);
													SendUp.addToWeb(black, BlackListService.this);
													Log.i(TAG, "------------------");
													black.setUptype(Blacklist.HAVED);
													blackService.update(black);
											}
						        			Toast.makeText(getApplicationContext(), R.string.addsuspicious, Toast.LENGTH_SHORT).show();
					        			}
					        		}
					        	}
					        	time = 0;
				                ringingtime = 0;
			        	}
			        	time = 0;
			        	//--------------------------------
			        	 try
			             {
			               AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			               if (audioManager != null)
			               {
			                 /* 设置手机状态为待机时，铃响为正常 */
			                 audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			                 audioManager.getStreamVolume(AudioManager.STREAM_RING);
			               }
			             } catch (Exception e)
			             {
			               e.printStackTrace();
			             }
			        	break;
			        case TelephonyManager.CALL_STATE_OFFHOOK:  //接起电话时 
			        	time = 0;
		                ringingtime = 0;
			        	break;	
			        case TelephonyManager.CALL_STATE_RINGING:  //电话进来时 
			        	ringingtime =  new Date().getTime();
			        	Log.i(TAG, "2222222222222222()"+ringingtime);
			        	if (blackService.findByNumber(incomingNumber)!= null){
			        		Blacklist blacklist = blackService.findByNumber(incomingNumber);
			        		if(blacklist.getType().equals("其他")||blacklist.getType().equals("推销")){
			        			 try
					              {
					                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					                if (audioManager != null)
					                {
					                  /* 设置为静音 */
					                	Log.i(TAG, "+++++++++++");
					                  /*Intent intent = new Intent(BlackListService.this, CloseActivity.class);
					                  startActivity(intent);*/
					                	audioManager.unloadSoundEffects();
					                  audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					                  audioManager.getStreamVolume(AudioManager.STREAM_RING);
					                  Toast.makeText(BlackListService.this,
					                      getString(R.string.str_msg), Toast.LENGTH_SHORT).show();
					                }
					              } catch (Exception e)
					              {
					                e.printStackTrace();
					                break;
					              }
			        		}
			             
			            }
			      }
			      super.onCallStateChanged(state, incomingNumber);
			}        	
		};
		//监听电话的状态
		telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
	}

	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart()");
	}
}
