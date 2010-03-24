package cn.opda.callactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.opda.R;
import cn.opda.contact.BaseBlackList;
import cn.opda.contact.CallHistoryList;
import cn.opda.contact.ContactList;
import cn.opda.net.upload.GetNet;
import cn.opda.net.upload.SendUp;
import cn.opda.phone.Blacklist;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class FirstActivity extends Activity {
	protected int my_requestCode = 2550;
	static final int DATE_DIALOG_ID = 0;
	GridView gridview;
	ProgressDialog pbarDialog;
	private static final String TAG = "FirstActivity";
	private Integer[] mImageIds = {
			R.drawable.search, R.drawable.blanklist, R.drawable.hostory, R.drawable.contact
			,R.drawable.ic_menu_call,R.drawable.add
	};
	private Integer[] mNameIds = {
			R.string.findarea, R.string.blacklist, R.string.callhostory, R.string.contact,
			R.string.help,R.string.set
	}; 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        /*
         * 从工程中直接读取数据库
         */
        /*DataBaseHelper myDbHelper = new DataBaseHelper(this);
        myDbHelper = new DataBaseHelper(this);
 
        try {
 
        	myDbHelper.createDataBase();
 
	 	} catch (IOException ioe) {
	 
	 		throw new Error("Unable to create database");
	 
	 	}
	 
	 	try {
	 
	 		myDbHelper.openDataBase();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        //------------------------------------
        Intent serviceIntent = new Intent(this, CallService.class);
		Intent serIntent = new Intent(this, BlackListService.class);
		Intent intenetIntent = new Intent(this, IntenetService.class);
		
		startService(intenetIntent);
		startService(serIntent);
		startService(serviceIntent);
        gridview = (GridView) findViewById(R.id.gridview);
		// 生成动态数组，并且转入数据
		List<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 6; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mImageIds[i]);// 添加图像资源的ID
			map.put("ItemNameText", FirstActivity.this.getString(mNameIds[i]));
			lstImageItem.add(map);
		}
		
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.night_item,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemNameText" },

				// ImageItem的XML文件里面的一个ImageView,3个TextView ID
				new int[] { R.id.ItemImage, R.id.ItemNameText });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
		
        //------------------------------------
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.about:
			View view = View.inflate(this, R.layout.about, null);

			AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {

			/* new 一个 Intent 对象，并指定 class */
			Intent intent = new Intent();
			if(arg2==0){ 
				intent.setClass(FirstActivity.this, CallActivity.class);
			}
			else if(arg2==1){
				intent.setClass(FirstActivity.this, BaseBlackList.class);
			}
			else if(arg2==2){
				intent.setClass(FirstActivity.this, CallHistoryList.class);
			}
			else if(arg2==3){
				intent.setClass(FirstActivity.this, ContactList.class);
			}
			else if(arg2==4){
				intent.setClass(FirstActivity.this, HelpActivity.class);
			}
			else if(arg2==5){
				intent.setClass(FirstActivity.this, SetActivity.class);
			}
			
			/* new 一个 Bundle 对象，并将要传递的数据传入 */
			Bundle bundle = new Bundle();
			bundle.putInt("constellation_id", arg2);
			/* 将 Bundle 对象 assign 给 Intent */
			intent.putExtras(bundle);
			/* 调用 Activity Result */
			startActivity(intent);
			//startActivityForResult(intent, my_requestCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
			case RESULT_OK :
			/* 取得来自 Activity2 的数据，并显示于画面上 */
				pbarDialog.cancel();
				break ;
			default :
				break ;
		}
	}


}
