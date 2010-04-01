package net.kfkx.net.upload;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.kfkx.phone.Blacklist;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SendUp {
	public static String addToWeb(Blacklist blacks, Context context) {
		String url = "http://guanjia.koufeikexing.com/koufeikexing/defener/phone.php";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", imei + "");
		params.put("number", blacks.getNumber() + "");
		int type = blacks.getType();
		String temp = "";
		switch (type) {
		case 0:
			temp = "0000F";
			break;
		case 1:
			temp = "0F000";
			break;
		case 2:
			temp = "00F00";
			break;
		case 3:
			temp = "000F0";
			break;
		case 4:
			temp = "F0000";
			break;
		}
		params.put("type", temp + "");
		if (blacks.getTimelength() == null) {
			params.put("timelength", "");
		} else {
			params.put("timelength", blacks.getTimelength() + "");
		}
		if (blacks.getTimehappen() == null) {
			params.put("timehappen", "");
		} else {
			params.put("timehappen", blacks.getTimehappen() + "");
		}
		if (blacks.getRemark() == null) {
			params.put("remark", "");
		} else {
			// URLEncoder encoder = new URLEncoder();
			String remark = URLEncoder.encode(blacks.getRemark().toString());
			if (remark == null) {
				params.put("remark", "");
			} else {

				params.put("remark", remark);
			}
		}
		params.put("version", "1.5");
		params.put("platform", "2");
		String s = HttpRequester.get(url, params);
		Log.i("SendUp", s);
		return s;
	}
}
