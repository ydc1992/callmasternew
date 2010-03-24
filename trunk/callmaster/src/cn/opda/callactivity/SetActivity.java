package cn.opda.callactivity;

import java.util.List;

import cn.opda.R;
import cn.opda.phone.WebBlack;
import cn.opda.service.WebBlackService;
import cn.opda.service.WebBlackSqliteService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SetActivity extends Activity {
	boolean isFinish = false;
	private WebBlackService webBlackService = new WebBlackService(this);
	private WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(this);
	private static final String TAG = "SetActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.webset);
		final ProgressDialog progressDialog = new ProgressDialog(SetActivity.this);
		progressDialog.setTitle("This is Title");
		progressDialog.setMessage("This is Message");
		progressDialog.setCancelable(true);
		ImageButton button = (ImageButton) findViewById(R.id.manualCopy);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null) {
					try {
						int version = webBlackService.getVersion();
						int oldVersion = blackSqliteService.findVersion();
						if(version == oldVersion){
							List<WebBlack> weblist = webBlackService.query();
							boolean boo = blackSqliteService.updateWebBlack(weblist);
							if(boo==true){
								isFinish = true;
							}else{
								Toast.makeText(SetActivity.this, R.string.updatefalse, Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(SetActivity.this, R.string.versionSame, Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				}else{
					Toast.makeText(SetActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
	}
}
