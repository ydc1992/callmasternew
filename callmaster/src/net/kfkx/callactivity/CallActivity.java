package net.kfkx.callactivity;



import net.kfkx.phone.Phone;
import net.kfkx.service.BelongingService;
import net.kfkx.service.PhoneSqliteService;
import net.kfkx.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CallActivity extends Activity {
	private PhoneSqliteService phoneService;
	private BelongingService belongingService;
	private EditText phoneText ;
	private TextView locationView;
	private TextView areacodeView;
	private static final String TAG = "CallActivity";
    /** Called when the activity is first created. */
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // land do nothing is ok
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // port do nothing is ok
            }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	phoneService = new PhoneSqliteService(this);
    	belongingService = new BelongingService(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area);
        Button button = (Button) findViewById(R.id.viewButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				phoneText = (EditText) findViewById(R.id.phonecode);
		    	locationView = (TextView) findViewById(R.id.location);
		    	areacodeView = (TextView) findViewById(R.id.areacode);
				String code = phoneText.getText().toString();
				String firstcode = null;
				String secondcode = null;
				int lentth = code.length();
				if(lentth>0){
					firstcode = String.copyValueOf(code.toCharArray(),0,1);}
				if(lentth>1){
					secondcode = String.copyValueOf(code.toCharArray(),1,1);
				}
				
				
				if(firstcode!=null&&firstcode.length()>0&&
						secondcode!=null&&secondcode.length()>0&&
						firstcode.equals("0")&&code.length()>=10&&code.length()<=11){
					if(Integer.parseInt(secondcode)<=2){
						String temp = String.copyValueOf(code.toCharArray(), 0, 3);
						Phone phone = phoneService.findByAreaNum(temp);
						if(phone==null){
							phone = new Phone("未知地区", "", "");
						}
						areacodeView.setText(phone.getAreaCode());
						locationView.setText(phone.getProvince()+phone.getCity());
					}else {
						String temp = String.copyValueOf(code.toCharArray(), 0, 4);
						Phone phone = phoneService.findByAreaNum(temp);
						if(phone==null){
							phone = new Phone("未知地区", "", "");
						}
						areacodeView.setText(phone.getAreaCode());
						locationView.setText(phone.getProvince()+phone.getCity());
					}
				}else if(firstcode!=null&&firstcode.length()>0&&
						firstcode.equals("1")&&code.length()==11){
					try {
						String temp = "0"+belongingService.read(code);
						Phone phone = phoneService.findByAreaNum(temp);
						if(phone==null){
							phone = new Phone("未知地区", "", "");
						}
						areacodeView.setText(phone.getAreaCode());
						locationView.setText(phone.getProvince()+phone.getCity());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Phone phone = new Phone("未知地区", "", "");
					areacodeView.setText(phone.getAreaCode());
					locationView.setText(phone.getProvince()+phone.getCity());
				}
			}
		});
    }
}