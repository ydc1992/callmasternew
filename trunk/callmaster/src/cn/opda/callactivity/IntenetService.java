package cn.opda.callactivity;

import java.util.List;


import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.service.BlackListSqliteService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class IntenetService extends Service {
	private static final String TAG = "IntenetService";
	private WifiManager wifiManager;
	private BlackListSqliteService blackListSqliteService = new BlackListSqliteService(this);
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		List<Blacklist> blacklists  =  blackListSqliteService.findUnSend();
       /* wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
        	for(Blacklist blacklist : blacklists){
				Log.i(TAG, "+++++++++++");
				blacklist.setUptype(Blacklist.HAVED);
				blackListSqliteService.update(blacklist);
				SendUp.addToWeb(blacklist, this);
			}
		}*/
		ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null) {
			for(Blacklist blacklist : blacklists){
				Log.i(TAG, "+++++++++++");
				blacklist.setUptype(Blacklist.HAVED);
				SendUp.addToWeb(blacklist, this);
				blackListSqliteService.update(blacklist);
			}
		} 
	}
}
