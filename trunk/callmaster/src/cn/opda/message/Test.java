package cn.opda.message;

import java.util.ArrayList;
import java.util.List;

import cn.opda.R;



import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Test extends TabActivity {
	// 声明TabHost对象
	TabHost mTabHost;
	private ListView list1;
	private ListView list2;

	private BlackMessage bage;
	private YiSiMessage yage;
	private List listt;
	private ToDoDB tb;
	private String yisi_number = "";
	private boolean flag = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		listt = new ArrayList();
		tb = new ToDoDB(this);

		list1 = (ListView) this.findViewById(R.id.ListView01);
		list2 = (ListView) this.findViewById(R.id.ListView02);

		// 取得TabHost对象
		mTabHost = getTabHost();

		/* 为TabHost添加标签 */
		// 新建一个newTabSpec(newTabSpec)
		// 设置其标签和图标(setIndicator)
		// 设置内容(setContent)

		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("可疑信息",
				this.getResources().getDrawable(R.drawable.doubtful)).setContent(R.id.ListView02));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(
				"黑名单收件箱 ", this.getResources().getDrawable(R.drawable.black)).setContent(R.id.ListView01));
		mTabHost.setCurrentTab(0);

		bage = new BlackMessage(this);
		yage = new YiSiMessage(this);

		final Cursor cursor_bage = bage.select();
		Cursor cursor_yage = yage.select();
		final Cursor cursor_tb = tb.select();
		while (cursor_yage.moveToNext()) {
			String number = cursor_yage.getString(cursor_yage
					.getColumnIndex(yage.FIELD_NUMBER));
			listt.add(number);
		}

		SimpleCursorAdapter adapter_bage = new SimpleCursorAdapter(Test.this,
				android.R.layout.simple_list_item_2, cursor_bage, new String[] {
						BlackMessage.FIELD_NUMBER, BlackMessage.FIELD_TEXT },
				new int[] { android.R.id.text1, android.R.id.text2 });
		list1.setAdapter(adapter_bage);
		SimpleCursorAdapter adapter_yage = new SimpleCursorAdapter(Test.this,
				R.layout.message, cursor_yage, new String[] {
						YiSiMessage.FIELD_NUMBER, YiSiMessage.FIELD_TEXT },
				new int[] { R.id.listTextView1, R.id.listTextView2 });
		list2.setAdapter(adapter_yage);
		list2.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(Test.this)
				.setTitle("转移号码")
				.setMessage("确认要将此号码放入黑名单吗？").setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								yisi_number = listt.get(i + 1).toString();
								while (cursor_tb.moveToNext()) {
									String bage_number = cursor_tb.getString(1);
									if (yisi_number.endsWith(bage_number)) {
										flag = false;
										Toast.makeText(Test.this, "黑名单中已有",
												Toast.LENGTH_LONG).show();
										Intent it = new Intent(Test.this,
												BlackList.class);
										it
												.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										Test.this.startActivity(it);

									}
									// else {
									//
									// tb.insert(list.get(i + 1).toString());
									// }
								}
								if (flag) {
									tb.insert(listt.get(i + 1).toString());
								}

							}
						})
				/* 砞﹚铬跌怠ㄆン */
				.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						yage.delete(listt.get(which+3).toString());
						Cursor cursor_yage = yage.select();
						SimpleCursorAdapter adapter_yage = new SimpleCursorAdapter(Test.this,
								R.layout.message, cursor_yage, new String[] {
										YiSiMessage.FIELD_NUMBER, YiSiMessage.FIELD_TEXT },
								new int[] { R.id.listTextView1, R.id.listTextView2 });
						list2.setAdapter(adapter_yage);
					}
				})		
				.setNegativeButton("不要", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();

			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem mu = menu.add(0, 0, 0, "转到黑名单");
		{

			mu.setIcon(R.drawable.ic_menu_blocked_user);

		}

		MenuItem mm = menu.add(0, 1, 1, "转到白名单");
		{
           mm.setIcon(R.drawable.ic_menu_invite);
		}

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case 0:
			Intent i = new Intent(Test.this, BlackList.class);
			Test.this.startActivity(i);

			break;
		case 1:
			Intent it = new Intent(Test.this, White.class);
			Test.this.startActivity(it);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
