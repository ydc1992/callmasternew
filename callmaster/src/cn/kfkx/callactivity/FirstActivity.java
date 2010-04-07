package cn.kfkx.callactivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.kfkx.contact.BaseBlackList;
import cn.kfkx.contact.CallHistoryList;
import cn.kfkx.contact.ContactList;
import cn.kfkx.dao.DataBaseHelper;
import cn.kfkx.mes.BackStage;
import cn.kfkx.mes.Test;
import cn.kfkx.phone.OpdaState;
import cn.kfkx.service.ShareService;


import cn.kfkx.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FirstActivity extends Activity {
	protected int my_requestCode = 2550;
	static final int DATE_DIALOG_ID = 0;
	GridView gridview;
	ProgressDialog pbarDialog;
	private static final String TAG = "FirstActivity";
	private Integer[] mImageIds = { R.drawable.search, R.drawable.blacklist,
			R.drawable.recorder, R.drawable.contact,R.drawable.msg, R.drawable.setting,
			R.drawable.help,R.drawable.about,R.drawable.update};
	private Integer[] mNameIds = { R.string.findarea, R.string.phonestop,
			R.string.callhostory,R.string.messagestop, R.string.contact, R.string.set,
			R.string.help,R.string.about,R.string.change};
	private Handler handler = new Handler(){
		@Override
    	public void handleMessage(Message msg) {
    			switch (msg.what) {
    			case 1:
    				Toast.makeText(FirstActivity.this, R.string.versionSame, Toast.LENGTH_SHORT).show();
    			}
		super.handleMessage(msg);
	}};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		/*
		 * �ӹ�����ֱ�Ӷ�ȡ���ݿ�
		 */
		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {

			myDbHelper.createDataBase();
			myDbHelper.close();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		File dir = new File("/data/data/cn.kfkx/shared_prefs/kfkx.xml");
		SharedPreferences sharedPreferences = ShareService.getShare(this, "kfkx");
		int startService = sharedPreferences.getInt(OpdaState.STATESERVICE, 1);
		int messageService = 0;
		final Editor editor = sharedPreferences.edit();
		if (!dir.exists()) {
			editor.putInt(OpdaState.STATESERVICE, 1);
			editor.putInt(OpdaState.BLACKVERSION, 0);
			editor.putInt(OpdaState.BEGINAUTO, 1);
			editor.putInt(OpdaState.DWONAUTO, 1);
			editor.putInt(OpdaState.BLACKSERVICE, 1);
			editor.putInt(OpdaState.MESSAGESERVICE, 0);
			editor.putInt(OpdaState.AREASERVICE, 1);
			editor.putLong(OpdaState.DOWNTIME, new Date().getTime());
			editor.putLong(OpdaState.UPTIME, new Date().getTime());
			editor.commit();
			LayoutInflater factory = LayoutInflater.from(this);
			final View editview = factory.inflate(R.layout.firstset, null);
			Builder my = new AlertDialog.Builder(this);
			my.setView(editview);
			final CheckBox startServiceButton = (CheckBox) editview.findViewById(R.id.firststartService);
			final CheckBox beginAutoButton = (CheckBox) editview.findViewById(R.id.firstbeginAuto);
			final CheckBox firstNetBox = (CheckBox) editview.findViewById(R.id.firstNetService);
			final CheckBox sendUpBox= (CheckBox) editview.findViewById(R.id.firstSendUp);
			//final CheckBox messageBox= (CheckBox) editview.findViewById(R.id.firstSetMessage);
			startServiceButton.setChecked(true);
			beginAutoButton.setChecked(true);
			firstNetBox.setChecked(true);
			sendUpBox.setChecked(true);
			//messageBox.setChecked(true);
			my.setPositiveButton(R.string.add,
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							if(startServiceButton.isChecked()==false){
								editor.remove(OpdaState.STATESERVICE);
								editor.putInt(OpdaState.STATESERVICE, 0);
								editor.commit();
							}
							if(startServiceButton.isChecked()==true){
								editor.remove(OpdaState.STATESERVICE);
								editor.putInt(OpdaState.STATESERVICE, 1);
								editor.commit();
							}
							if(beginAutoButton.isChecked()==false){
								editor.remove(OpdaState.BEGINAUTO);
								editor.putInt(OpdaState.BEGINAUTO, 0);
								editor.commit();
							}
							if(beginAutoButton.isChecked()==true){
								editor.remove(OpdaState.BEGINAUTO);
								editor.putInt(OpdaState.BEGINAUTO, 1);
								editor.commit();
							}
							if(firstNetBox.isChecked()==false){
								editor.remove(OpdaState.DOWNTIME);
								editor.remove(OpdaState.DWONAUTO);
								editor.putInt(OpdaState.DWONAUTO, 0);
								editor.commit();
							}
							if(firstNetBox.isChecked()==true){
				            	long time = new Date().getTime();
				            	editor.remove(OpdaState.DOWNTIME);
				            	editor.putLong(OpdaState.DOWNTIME, time);
								editor.remove(OpdaState.DWONAUTO);
								editor.putInt(OpdaState.DWONAUTO, 1);
								editor.commit();
							}
							if(sendUpBox.isChecked()==false){
								editor.remove(OpdaState.UPTIME);
								editor.remove(OpdaState.SENDUP);
								editor.putInt(OpdaState.SENDUP, 0);
								editor.commit();
							}
							if(sendUpBox.isChecked()==true){
								long uptime = new Date().getTime();
								editor.remove(OpdaState.UPTIME);
								editor.putLong(OpdaState.UPTIME, uptime);
								editor.remove(OpdaState.SENDUP);
								editor.putInt(OpdaState.SENDUP, 1);
								editor.commit();
							}
						}
					});
			my.show();
			}
		messageService = sharedPreferences.getInt(OpdaState.MESSAGESERVICE, 1);
		    if(startService==1){
		    	Intent serviceIntent = new Intent(this, CallService.class);
		    	Intent serIntent = new Intent(this, BlackListService.class);
		    	this.startService(serIntent);
		    	this.startService(serviceIntent);
		    	if(messageService == 1){
			    	Intent messageIntent = new Intent(this, BackStage.class);
					messageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					this.startService(messageIntent);
		    	}
			}
		
		// ------------------------------------
		gridview = (GridView) findViewById(R.id.gridview);
		// ���ɶ�̬���飬����ת������
		List<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 9; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mImageIds[i]);// ���ͼ����Դ��ID
			map.put("ItemNameText", FirstActivity.this.getString(mNameIds[i]));
			lstImageItem.add(map);
		}

		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		SimpleAdapter saImageItems = new SimpleAdapter(this, // ûʲô����
				lstImageItem,// ������Դ
				R.layout.night_item,// night_item��XMLʵ��

				// ��̬������ImageItem��Ӧ������
				new String[] { "ItemImage", "ItemNameText" },

				// ImageItem��XML�ļ������һ��ImageView,3��TextView ID
				new int[] { R.id.ItemImage, R.id.ItemNameText });
		// ��Ӳ�����ʾ
		gridview.setAdapter(saImageItems);
		// �����Ϣ����
		gridview.setOnItemClickListener(new ItemClickListener());

		// ------------------------------------
	}


	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
				// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {

			/* new һ�� Intent ���󣬲�ָ�� class */
			Intent intent = new Intent();
			if (arg2 == 0) {
				intent.setClass(FirstActivity.this, CallActivity.class);
				beginActivity(intent, arg2);
			} else if (arg2 == 1) {
				intent.setClass(FirstActivity.this, BaseBlackList.class);
				beginActivity(intent, arg2);
			} else if (arg2 == 2) {
				intent.setClass(FirstActivity.this, CallHistoryList.class);
				beginActivity(intent, arg2);
			} else if (arg2 == 3) {
				intent.setClass(FirstActivity.this, ContactList.class);
				beginActivity(intent, arg2);
			} else if (arg2 == 4) {
				intent.setClass(FirstActivity.this, Test.class);
				beginActivity(intent, arg2);
			}else if (arg2 == 5) {
				intent.setClass(FirstActivity.this, SetActivity.class);
				beginActivity(intent, arg2);
			}  else if (arg2 == 6) {
				intent.setClass(FirstActivity.this, HelpActivity.class);
				beginActivity(intent, arg2);
			} else if (arg2 == 7) {
				View view = View.inflate(FirstActivity.this, R.layout.about, null);
				AlertDialog.Builder myBuilder = new AlertDialog.Builder(FirstActivity.this);
				myBuilder.setIcon(android.R.drawable.ic_dialog_info);
				myBuilder.setTitle(R.string.aboutversion);
				myBuilder.setView(view);
				// myBuilder.setMessage(R.string.aboutinfo);
				myBuilder.setPositiveButton(R.string.aboutversion,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								try {
								} catch (Exception e) {
									Log.e(TAG, e.getMessage());
								}
							}
						}).show();
			}else if (arg2 == 8) {
				
	    		
				ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				boolean flag = false;
				if (info != null) {
					final ProgressDialog progressDialog = new ProgressDialog(FirstActivity.this);
		    		progressDialog.setTitle(R.string.change);
		    		progressDialog.setMessage("�����°汾");
		    		progressDialog.setCancelable(false);
					progressDialog.show();
					new Thread(new Runnable() {
						public void run() {
						    try {
								Thread.sleep(1500);
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);//������Ϣ
							} catch (Exception e) {
								Log.e(TAG, e.getMessage());
							} 
							progressDialog.cancel();
							
						}
					}).start();
					while(flag){
						Toast.makeText(FirstActivity.this, R.string.versionSame, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(FirstActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
			}
			
			
		}
	}
	private void beginActivity(Intent intent, int i){
		Bundle bundle = new Bundle();
		bundle.putInt("constellation_id", i);
		intent.putExtras(bundle);
		/* ���� Activity Result */
		startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			/* ȡ������ Activity2 �����ݣ�����ʾ�ڻ����� */
			pbarDialog.cancel();
			break;
		default:
			break;
		}
	}

}
