package net.kfkx.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kfkx.net.upload.SendUp;
import net.kfkx.phone.Blacklist;
import net.kfkx.phone.OpdaState;
import net.kfkx.phone.WebBlack;
import net.kfkx.service.BlackListSqliteService;
import net.kfkx.service.ShareService;
import net.kfkx.service.WebBlackService;
import net.kfkx.service.WebBlackSqliteService;


import net.kfkx.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BaseBlackList extends Activity {
	private static final String TAG = "BaseBlackList";
	private WebBlackService webBlackService = new WebBlackService(this);
	private WebBlackSqliteService webSqliteService = new WebBlackSqliteService(this);
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private static final int AddContact = 1;
	private static final int EXITContact = 2;
	private static final int UPDATEBLACK = 3;
	private static final int SENDUP = 4;
	private Handler handler = new Handler(){
	public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
			    Toast.makeText(BaseBlackList.this, R.string.updatesuccess, Toast.LENGTH_LONG).show();
				break;
			case 2:
			    Toast.makeText(BaseBlackList.this, R.string.updatefalse, Toast.LENGTH_LONG).show();
			    break;
			case 3:
			    Toast.makeText(BaseBlackList.this, R.string.versionSame, Toast.LENGTH_LONG).show();
			    break;
			case 4:
                Toast.makeText(BaseBlackList.this, R.string.netWrong, Toast.LENGTH_LONG).show();
                break;
			case 5:
				Toast.makeText(BaseBlackList.this, R.string.upSuccess, Toast.LENGTH_LONG).show();
				break;
			}
		super.handleMessage(msg);
	}};
	String typename;
	private ListView listView ;
	private SimpleAdapter adapter;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blackmain);
        
		show();
	}
	public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, AddContact, 0, R.string.add)
        	.setIcon(android.R.drawable.ic_menu_zoom);
        
        menu.add(0, UPDATEBLACK, 0, R.string.blacklistdown)
		.setIcon(android.R.drawable.ic_menu_rotate);
        
        menu.add(0, EXITContact, 0, R.string.back)
        .setIcon(android.R.drawable.ic_menu_revert);
        
        menu.add(0, SENDUP, 0, R.string.blacklistup)
        .setIcon(android.R.drawable.ic_menu_upload);
        
        return true;
        
    }
    
    //处理菜单操作
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case AddContact:
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
							int type = 0;
							if("一声响".equals(typename)){
								type = Blacklist.TYPE_ONESOUND;
							}else if("高额收费".equals(typename)){
								type = Blacklist.TYPE_OVERCHARGE;
							}else if("推销".equals(typename)){
								type = Blacklist.TYPE_PROMOTION;
							}else if("其他".equals(typename)){
								type = Blacklist.TYPE_OTHER;
							}else if("短信".equals(typename)){
								type = Blacklist.TYPE_MESSAGE;
							}
							String remark = editRemarkText.getText().toString();
							if (number.equals("")) {
								return;
							}
							if(blackService.findByNumber(number)!=null){
								Toast.makeText(BaseBlackList.this, R.string.haveSave, Toast.LENGTH_SHORT).show();
							}else{
								Blacklist blacklist = new Blacklist(number,type,remark,Blacklist.HAVE_NO);
								blackService.savepart(blacklist);
								SharedPreferences sharedPreferences = ShareService.getShare(BaseBlackList.this, "kfkx");
								int sendUpService = sharedPreferences.getInt(OpdaState.SENDUP, 1);
								ConnectivityManager connectivity = (ConnectivityManager)BaseBlackList.this.getSystemService(Context.CONNECTIVITY_SERVICE);
								NetworkInfo info = connectivity.getActiveNetworkInfo();
								if (info != null && sendUpService == 1) {
										Log.i(TAG, "+++++++++++");
										Blacklist black = blackService.findByNumber(number);
										SendUp.addToWeb(black, BaseBlackList.this);
										Log.i(TAG, "------------------");
										black.setUptype(Blacklist.HAVED);
										blackService.update(black);
							}
							
							}
							show();
						}
					});
			my.setNegativeButton(R.string.cancel, null);
			my.show();
			break;
        case UPDATEBLACK:
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("同步云端数据");
            progressDialog.setMessage("同步中");
            progressDialog.setCancelable(true);
            ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            progressDialog.show();
            if (info != null) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            
                            SharedPreferences sharedPreferences = ShareService.getShare(BaseBlackList.this, "kfkx");
                            Editor editor = sharedPreferences.edit();
                            int blackversion = sharedPreferences.getInt(OpdaState.BLACKVERSION, 0);
                            int serviceVersion = webBlackService.getVersion();
                            if(blackversion != serviceVersion){
                                List<WebBlack> weblist = webBlackService.query();
                                webSqliteService.updateWebBlack(weblist);
                                editor.remove(OpdaState.BLACKVERSION);
                                editor.putInt(OpdaState.BLACKVERSION, serviceVersion);
                                editor.commit();
                                Message msg = new Message();
                                msg.what = 1;
                                progressDialog.cancel();
                                handler.sendMessage(msg);
                            }else{
                                Message msg = new Message();
                                msg.what = 3;
                                handler.sendMessage(msg);
                                progressDialog.cancel();
                            }
                        } catch (Exception e) {
                            Message msg = new Message();
                            msg.what = 4;
                            handler.sendMessage(msg);
                            progressDialog.cancel();
                            Log.e(TAG, e.getMessage());
                        } 
                    }
                }).start();
                
            }else{
                progressDialog.cancel();
                Toast.makeText(BaseBlackList.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
            }
            break;
        case SENDUP:
        	ConnectivityManager conn = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netInfo = conn.getActiveNetworkInfo();
    		final List<Blacklist> bblist = blackService.findUnSend();
    		if (netInfo != null){
    			if(bblist.size()==0){
        			Toast.makeText(BaseBlackList.this, R.string.haveNoUnSend, Toast.LENGTH_SHORT).show();
        		}else{
        			new Thread(new Runnable() {
        				public void run() {
        					// TODO Auto-generated method stub
        					for(Blacklist blacklist : bblist){
        						blacklist.setUptype(Blacklist.HAVED);
        						SendUp.addToWeb(blacklist, BaseBlackList.this);
        						blackService.update(blacklist);
        					}
        					Message msg = new Message();
        					msg.what = 5;
        					handler.sendMessage(msg);
        				}
        			}).start();
        		}
    				
    		}else{
    			Toast.makeText(BaseBlackList.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
    		}
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
		if(blacks.size()==0){
			Toast.makeText(this, R.string.blacklistShuoMing, Toast.LENGTH_LONG).show();
		}
		for(Blacklist black : blacks){
			Map map = new HashMap<String, Object>();
			map.put("number", black.getNumber());
			int type = black.getType();
			String temp = "";
			switch (type) {
			case 0:
				temp = "一声响";
				break;
			case 1:
				temp = "高额收费";
				break;
			case 2:
				temp = "推销";
				break;
			case 3:
				temp = "其他";
				break;
			case 4:
				temp = "短信";
				break;
			}
			map.put("type", temp);
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
