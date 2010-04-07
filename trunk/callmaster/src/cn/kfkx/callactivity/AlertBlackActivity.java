package cn.kfkx.callactivity;

import cn.kfkx.phone.Blacklist;
import cn.kfkx.service.BlackListSqliteService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AlertBlackActivity extends Activity {
	private static final String TAG = "AlertBlackActivity";
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "come on"); 
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		String number = getIntent().getStringExtra("number");
		Blacklist blacklist = blackService.findByNumber(number);
		alertDialogBuilder.setTitle("疑似名单操作");
		alertDialogBuilder.setMessage("是否拨打"+blacklist.getType()+"电话--"+blacklist.getRemark());
		alertDialogBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	String number = getIntent().getStringExtra("number");
		    	blackService.deleteByNumber(number);
		    	Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+number));
        		startActivity(callIntent);
        		AlertBlackActivity.this.finish();
		    }
		});
		alertDialogBuilder.setNeutralButton("否", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
        		AlertBlackActivity.this.finish();
		    	//finish();
		    }
		});
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.show();
	}
}
