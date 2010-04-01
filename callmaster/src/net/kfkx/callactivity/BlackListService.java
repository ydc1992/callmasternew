package net.kfkx.callactivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.kfkx.contact.BaseCallHistoryList;
import net.kfkx.net.upload.SendUp;
import net.kfkx.phone.Blacklist;
import net.kfkx.phone.OpdaState;
import net.kfkx.phone.WebBlack;
import net.kfkx.service.BlackListSqliteService;
import net.kfkx.service.ShareService;
import net.kfkx.service.WebBlackSqliteService;

import net.kfkx.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.provider.Contacts;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.util.Log;

public class BlackListService extends Service {
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private WebBlackSqliteService webBlackService = new WebBlackSqliteService(this);
	private static final String TAG = "BlackListService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // land do nothing is ok
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // port do nothing is ok
            }
    }
	@Override
	public void onCreate() {
            TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            PhoneStateListener listener = new PhoneStateListener(){	
                boolean isRunning = false;
                long time = 0;
                long ringingtime = 0;
                public void onCallStateChanged(int state, String incomingNumber) {
                    final String num = incomingNumber;
                    
                    switch (state){
                    
                    case TelephonyManager.CALL_STATE_IDLE:  //���κ�״̬ʱ 
                        isRunning = false;
                        if(ringingtime != 0){
                            time = new Date().getTime() - ringingtime;
                            Log.i(TAG, "++++++++++"+time);
                            if(time < 3000){
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
    									SharedPreferences sharedPreferences = ShareService.getShare(BlackListService.this, "opda");
    									int sendUpService = sharedPreferences.getInt(OpdaState.SENDUP, 1);
                                        ConnectivityManager connectivity = (ConnectivityManager)BlackListService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                                        if (connectivity != null && sendUpService == 1) {
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
                                /* �����ֻ�״̬Ϊ����ʱ������Ϊ���� */
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                audioManager.getStreamVolume(AudioManager.STREAM_RING);
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:  //����绰ʱ 
                        isRunning = false;
                        time = 0;
                        ringingtime = 0;
                        break;	
                    case TelephonyManager.CALL_STATE_RINGING:  //�绰����ʱ
                        isRunning = true;
                        SharedPreferences preferences = ShareService.getShare(BlackListService.this, "opda");
                        int blackstate = preferences.getInt(OpdaState.BLACKSERVICE, 1);
                        int startstate = preferences.getInt(OpdaState.STATESERVICE, 1);
                        if((startstate == 1)&&(blackstate == 1)){
                            ringingtime =  new Date().getTime();
                            Log.i(TAG, "2222222222222222()"+ringingtime);
                            Blacklist blacklist = blackService.findByNumber(incomingNumber);
                            WebBlack webBlack = webBlackService.findByNumber(incomingNumber);
                            if (blacklist!= null){
                                Timer timer = new Timer();
                                if(blacklist.getType()==Blacklist.TYPE_PROMOTION){
                                    try
                                    {
                                        timer.schedule(new TimerTask(){  
                                            Toast mToast = Toast.makeText(BlackListService.this, R.string.tuixiaoAlert, Toast.LENGTH_LONG);  
                                            public void run() {  
                                                while(isRunning){  
                                                    mToast.show();  
                                                }  
                                            }  
                                        }, 10);
                                        /* ����Ϊ���� */
                                        changeToSilentMode();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        break;
                                    }
                                }else if(blacklist.getType()==Blacklist.TYPE_OTHER){
                                    try
                                    {
                                        timer.schedule(new TimerTask(){  
                                            Toast mToast = Toast.makeText(BlackListService.this, R.string.saoraoAlert, Toast.LENGTH_LONG);  
                                            public void run() {  
                                                while(isRunning){  
                                                    mToast.show();  
                                                }  
                                            }  
                                        }, 10);
                                        /* ����Ϊ���� */
                                        changeToSilentMode();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                            }
                            if(webBlack!=null){
                                if(webBlack.getType()==WebBlack.TYPE_PROMOTION){
                                    try
                                    {
                                        /* ����Ϊ���� */
                                        changeToSilentMode();
                                        Toast.makeText(BlackListService.this,
                                                getString(R.string.tuixiaoAlert), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        break;
                                    }
                                }else if(webBlack.getType()==WebBlack.TYPE_OTHER){
                                    try
                                    {
                                        /* ����Ϊ���� */
                                        changeToSilentMode();
                                        Toast.makeText(BlackListService.this,
                                                getString(R.string.saoraoAlert), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    super.onCallStateChanged(state, incomingNumber);
                }        	
            };
            //�����绰��״̬
            telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	private void changeToSilentMode(){
		 AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		 if(audioManager!=null){
			 audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		 }
	}
	public void onDestroy() {
	}

	public void onStart(Intent intent, int startId) {
	}
}
