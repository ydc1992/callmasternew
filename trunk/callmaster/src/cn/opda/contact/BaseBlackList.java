package cn.opda.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.opda.R;
import cn.opda.callactivity.IntenetService;
import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import cn.opda.service.BlackListSqliteService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class BaseBlackList extends Activity {
	private static final String TAG = "BaseBlackList";
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private static final int AddContact= 1;
	private static final int EXITContact= 2;
	String typename;
	private ListView listView ;
	private SimpleAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blackmain);
        
		show();
	}
	public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, AddContact, 0, R.string.add)
        	.setShortcut('3', 'a')
        	.setIcon(R.drawable.add);
        
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, BaseCallHistoryList.class), null, intent, 0, null);

        menu.add(0, EXITContact, 0, R.string.back)
    		.setShortcut('4', 'd')
    		.setIcon(R.drawable.exit);
        return true;
        
    }
    
    //处理菜单操作
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case AddContact:
        	
        	/*RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup); 
			Log.i(TAG,radioGroup+"+++++++++++==");
			radioGroup.check(R.id.onesound);
			RadioButton radioButton = (RadioButton) findViewById(
			    radioGroup.getCheckedRadioButtonId());

			//Log.i(TAG, String.valueOf(radioButton.getText()));

			radioGroup.setOnCheckedChangeListener(
			    new RadioGroup.OnCheckedChangeListener() {
			    public void onCheckedChanged(
			        RadioGroup group,
			        int checkedId) {
			        RadioButton radioButton = (RadioButton) findViewById(checkedId);
			        typename = String.valueOf(radioButton.getText());
			        Log.i(TAG, String.valueOf(radioButton.getText()));
			    }
			});*/
        	
        	LayoutInflater factory = LayoutInflater.from(this);
			final View editview = factory.inflate(R.layout.edit, null);
        	Builder my = new AlertDialog.Builder(this);
			my.setView(editview);
			final EditText editText = (EditText)editview.findViewById(R.id.editnumber);
			final EditText editRemarkText = (EditText)editview.findViewById(R.id.editremark);
			RadioGroup radioGroup = (RadioGroup) editview.findViewById(R.id.radioGroup); 
			radioGroup.check(R.id.onesound);
			RadioButton radioButton = (RadioButton) findViewById(
			    radioGroup.getCheckedRadioButtonId());


			radioGroup.setOnCheckedChangeListener(
			    new RadioGroup.OnCheckedChangeListener() {
			    public void onCheckedChanged(
			        RadioGroup group,
			        int checkedId) {
			        RadioButton radioButton = (RadioButton) editview.findViewById(checkedId);
			        typename = String.valueOf(radioButton.getText());
			    }
			});
			
			my.setPositiveButton(R.string.add,
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							
							String number = editText.getText().toString();
							String type = typename;
							int tt = 6;
							if(type==null){
								type = "一声响";
							}
							
							String remark = editRemarkText.getText().toString();
							if (number.equals("")) {
								return;
							}
							Blacklist blacklist = new Blacklist(number,type,remark,Blacklist.HAVE_NO);
							blackService.savepart(blacklist);
							ConnectivityManager connectivity = (ConnectivityManager)BaseBlackList.this.getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo info = connectivity.getActiveNetworkInfo();
							if (info != null) {
									Log.i(TAG, "+++++++++++");
									Blacklist black = blackService.findByNumber(number);
									SendUp.addToWeb(black, BaseBlackList.this);
									Log.i(TAG, "------------------");
									black.setUptype(Blacklist.HAVED);
									blackService.update(black);
							}
							show();
						}
					});
			my.setNegativeButton(R.string.cancel, null);
			my.show();
			break;
        case EXITContact:
        	this.finish();
            //return true;
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void show(){
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        listView = (ListView) findViewById(R.id.blacklist);
		List<Blacklist> blacks = blackService.findAll();
		for(Blacklist black : blacks){
			Map map = new HashMap<String, Object>();
			map.put("number", black.getNumber());
			map.put("type", black.getType());
			map.put("remark", black.getRemark());
			list.add(map);
		}
    	adapter = new SimpleAdapter(BaseBlackList.this, list, R.layout.blackitem, 
				new String[]{"number","type","remark"},
				new int[]{R.id.blacknumberview,R.id.blackNumberType,R.id.blackNumberRemark});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> map =  (Map<String, Object>) parent.getItemAtPosition(position);
				final String nn = (String) map.get("number");
				final String type = (String) map.get("type");
				final String remark = (String) map.get("remark");
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseBlackList.this);
				alertDialogBuilder.setTitle(R.string.handle);
				alertDialogBuilder.setMessage(R.string.blackhandle);
				alertDialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				    	blackService.deleteByNumber(nn);
				    	show();
				    }
				});
				alertDialogBuilder.setNegativeButton(R.string.addtoweb, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new Thread(new Runnable() {
							public void run() {
								//String tt = BaseBlackList.this.addToWeb(new Blacklist(nn, type, remark, Blacklist.HAVE_NO));
								//Log.i(TAG, tt+"++++++-------000---++++");
							}
						}).start();
					}
				});
				alertDialogBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				    	
				    }
				});
				alertDialogBuilder.setCancelable(true);
				alertDialogBuilder.show();
				
			}
			
		});
    }
}
