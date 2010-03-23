package cn.opda.callactivity;

import java.util.List;

import cn.opda.R;
import cn.opda.phone.WebBlack;
import cn.opda.service.WebBlackService;
import cn.opda.service.WebBlackSqliteService;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class SetActivity extends Activity {
	private WebBlackService webBlackService = new WebBlackService(this);
	private WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(this);
	private static final String TAG = "SetActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.webset);
		ImageButton button = (ImageButton) findViewById(R.id.manualCopy);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null) {
					try {
						List<WebBlack> weblist = webBlackService.query();
						blackSqliteService.deleteAll();
						for(WebBlack webBlack : weblist){
							blackSqliteService.save(webBlack);
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		});
	}
}
