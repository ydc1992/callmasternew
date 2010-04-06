package net.kfkx.callactivity;

import java.io.IOException;

import net.kfkx.dao.DataBaseHelper;
import net.kfkx.phone.Blacklist;
import net.kfkx.phone.OpdaState;
import net.kfkx.phone.Phone;
import net.kfkx.phone.WebBlack;
import net.kfkx.service.BelongingService;
import net.kfkx.service.BlackListSqliteService;
import net.kfkx.service.PhoneSqliteService;
import net.kfkx.service.ShareService;
import net.kfkx.service.WebBlackSqliteService;
import net.kfkx.R;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class CallOutService extends Service {
    private BelongingService belongservice = new BelongingService(this);
    private PhoneSqliteService phoneService = new PhoneSqliteService(this);
    private BlackListSqliteService blackService = new BlackListSqliteService(this);
    private WebBlackSqliteService webBlackService = new WebBlackSqliteService(this);
    private static final String TAG = "CallOutService";
    private Phone phone = new Phone("Î´ÖªµØÇø", "", "");
    // private Intent intent;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
    	
		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {

			myDbHelper.createDataBase();
			myDbHelper.close();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}
    }

    @Override
    public void onStart(Intent intent, int startId) {
    		SharedPreferences sharedPreferences = ShareService.getShare(this, "kfkx");
    		int blackstate = sharedPreferences.getInt(OpdaState.BLACKSERVICE, 1);
			int areaState = sharedPreferences.getInt(OpdaState.AREASERVICE, 1);
    		String number = intent.getStringExtra("num");
    		int firstNum = Integer.parseInt(String.copyValueOf(
    				number.toCharArray(), 0, 1));
    		int secondNum = Integer.parseInt(String.copyValueOf(number
    				.toCharArray(), 1, 1));
    		Blacklist blacklist = blackService.findByNumber(number);
    		WebBlack webBlack = webBlackService.findByNumber(number);
    		
    		if (blacklist != null && blackstate==1) {
    			if (blacklist.getType() == Blacklist.TYPE_ONESOUND
    					|| blacklist.getType() == Blacklist.TYPE_OVERCHARGE) {
    				Toast.makeText(CallOutService.this, R.string.tip,
    						Toast.LENGTH_LONG).show();
    			}
    		}
    		
    		else if (webBlack != null && blackstate==1) {
    			if (webBlack.getType() == WebBlack.TYPE_ONESOUND
    					|| webBlack.getType() == WebBlack.TYPE_OVERCHARGE) {
    				Toast.makeText(CallOutService.this, R.string.tip,
    						Toast.LENGTH_LONG).show();
    			} 
    		}else {
    			if(areaState==1){
    				if (number.length() == 11 || number.length() == 12) {
    					if (firstNum == 0) {
    						if (secondNum == 1 || secondNum == 2) {
    							Phone phonetemp = phoneService.findByAreaNum(String
    									.copyValueOf(number.toCharArray(), 0, 3));
    							if (phonetemp != null) {
    								phone = phonetemp;
    							}
    						} else {
    							Phone phonetemp = phoneService.findByAreaNum(String
    									.copyValueOf(number.toCharArray(), 0, 4));
    							if (phonetemp != null) {
    								phone = phonetemp;
    							}
    						}
    					} else if (firstNum == 1) {
    						try {
    							String pre = "0" + belongservice.read(number);
    							Phone phonetemp = phoneService.findByAreaNum(pre);
    							if (phonetemp != null) {
    								phone = phonetemp;
    							}
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
    					}
    					Toast.makeText(getApplicationContext(),
    							phone.getProvince() + phone.getCity(),
    							Toast.LENGTH_LONG).show();
    				}
    			}
    	}
        
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
