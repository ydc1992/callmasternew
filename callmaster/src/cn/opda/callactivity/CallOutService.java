package cn.opda.callactivity;

import cn.opda.R;
import cn.opda.phone.Blacklist;
import cn.opda.phone.Phone;
import cn.opda.service.BelongingService;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.PhoneSqliteService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CallOutService extends Service {
	private BelongingService belongservice = new BelongingService(this);
	private PhoneSqliteService phoneService = new PhoneSqliteService(this);
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private static final String TAG = "CallOutService";
	private Phone phone = new Phone("δ֪", "δ֪", "");
	//private Intent intent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
	}
	@Override
	public void onStart(Intent intent, int startId) {
		String number = intent.getStringExtra("num");        
		int firstNum = Integer.parseInt(String.copyValueOf(number.toCharArray(), 0, 1));
		int secondNum = Integer.parseInt(String.copyValueOf(number.toCharArray(), 1, 1));
		if(blackService.findByNumber(number)!=null){
			Blacklist blacklist = blackService.findByNumber(number);
			if(blacklist.getType().equals("һ����")||blacklist.getType().equals("�߶��շ�")){
				Toast.makeText(CallOutService.this, R.string.tip, Toast.LENGTH_LONG).show();
				/*Intent intentAlert = new Intent(this,AlertBlackActivity.class);
				intentAlert.putExtra("number", number);
				intentAlert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentAlert);*/
			}
			/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("������������");
			alertDialogBuilder.setMessage("�Ƿ񲦴����Ƶ绰");
			alertDialogBuilder.setPositiveButton("��", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	
			    }
			});
			alertDialogBuilder.setNeutralButton("��", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	
			    }
			});
			alertDialogBuilder.setCancelable(true);
			alertDialogBuilder.show();*/
		}
		if(number.length()==11||number.length()==12){
			if(firstNum==0){
				if(secondNum==1||secondNum==2){
					Phone phonetemp = phoneService.findByAreaNum(String.copyValueOf(number.toCharArray(), 0, 3));
					if(phonetemp!=null){
						phone = phonetemp;
					}
				}else {
					Phone phonetemp = phoneService.findByAreaNum(String.copyValueOf(number.toCharArray(), 0, 4));
					if(phonetemp!=null){
						phone = phonetemp;
					}
				}
			}else if(firstNum==1){
				try {
					String pre = "0"+belongservice.read(number);
					Phone phonetemp = phoneService.findByAreaNum(pre);
					if(phonetemp!=null){
						phone = phonetemp;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Toast.makeText(getApplicationContext(), phone.getProvince()+phone.getCity(), Toast.LENGTH_LONG).show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
