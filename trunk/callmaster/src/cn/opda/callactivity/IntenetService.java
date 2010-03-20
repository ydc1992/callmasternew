package cn.opda.callactivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.opda.net.upload.GetNet;
import cn.opda.net.upload.HttpRequester;
import cn.opda.phone.Blacklist;
import cn.opda.service.BlackListSqliteService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;

public class IntenetService extends Service {
	private BlackListSqliteService blackListSqliteService = new BlackListSqliteService(this);
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		List<Blacklist> blacklists  =  blackListSqliteService.findUnSend();
		boolean bool = GetNet.hasInternet(this);
		if(bool==true){
			for(Blacklist blacklist : blacklists){
				addToWeb(blacklist);
			}
		}else{
			
		}
	}
    private String addToWeb(Blacklist blacks){
    	String url = "http://guanjia.koufeikexing.com/koufeikexing/defener/phone.php?";
    	TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);  
    	String imei = tm.getDeviceId();  
    	String tel = tm.getLine1Number(); 
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", imei+"");
		params.put("number",blacks.getNumber()+"");
		params.put("type", blacks.getType()+"");
		params.put("timelength", blacks.getTimelength()+"");
		params.put("timehappen", blacks.getTimehappen()+"");
		params.put("remark", blacks.getRemark()+"");
		params.put("version", "1.5");
		params.put("platform", "2");
		String s = HttpRequester.post(url, params);
		return s;
    }
}
