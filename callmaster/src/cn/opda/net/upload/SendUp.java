package cn.opda.net.upload;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.opda.phone.Blacklist;

public class SendUp {
		public  static String addToWeb(Blacklist blacks,Context context){
	    	String url = "http://guanjia.koufeikexing.com/koufeikexing/defener/phone.php";
	    	TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
	    	String imei = tm.getDeviceId();  
			Map<String, String> params = new HashMap<String, String>();
			params.put("imei",imei + "");
			params.put("number",blacks.getNumber()+"");
			params.put("type", blacks.getType()+"");
			params.put("timelength", blacks.getTimelength()+"");
			params.put("timehappen", blacks.getTimehappen()+"");
			params.put("remark", blacks.getRemark()+"");
			params.put("version", "1.5");
			params.put("platform", "2");
			String s = HttpRequester.httpPost(url, params);
			Log.i("SendUp", s);
			return s;
	    }
}
