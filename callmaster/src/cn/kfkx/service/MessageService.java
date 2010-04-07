package cn.kfkx.service;


import cn.kfkx.net.upload.SendUp;
import cn.kfkx.phone.Blacklist;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MessageService {
	private static final String TAG = "MessageService";
	private Context context;
	public MessageService(){}
	public MessageService(Context context) {
		this.context = context;
	}
	public void saveByNumber(String number){
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(context);
		Blacklist blacklist = new Blacklist();
		blacklist.setNumber(number);
		blacklist.setType(Blacklist.TYPE_MESSAGE);
		blacklist.setUptype(Blacklist.HAVE_NO);
		blackListSqliteService.saveall(blacklist);
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null) {
			Blacklist black = blackListSqliteService.findByNumber(number);
			SendUp.addToWeb(blacklist, context);
			black.setUptype(Blacklist.HAVED);
			blackListSqliteService.update(black);
		}
	}
}
