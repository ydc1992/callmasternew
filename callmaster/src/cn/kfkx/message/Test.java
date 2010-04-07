package cn.kfkx.message;

import java.util.ArrayList;
import java.util.List;

import cn.kfkx.R;

import android.app.AlertDialog;
import android.app.NotificationManager;
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
	private List list_text;
	private ToDoDB tb;
	private String yisi_number = "";
	private String black_number = "";
	private boolean flag = true;
	private List list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		listt = new ArrayList();
		tb = new ToDoDB(this);
		list = new ArrayList();
		list_text = new ArrayList();

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(CooperationNotification.SIMPLE_NOTIFICATION_ID);

		list1 = (ListView) this.findViewById(R.id.ListView01);
		list2 = (ListView) this.findViewById(R.id.ListView02);

		// 取得TabHost对象
		mTabHost = getTabHost();

		/* 为TabHost添加标签 */
		// 新建一个newTabSpec(newTabSpec)
		// 设置其标签和图标(setIndicator)
		// 设置内容(setContent)

		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("可疑信息",
				this.getResources().getDrawable(R.drawable.doubtful))
				.setContent(R.id.ListView02));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(
				"黑名单收件箱 ", this.getResources().getDrawable(R.drawable.black))
				.setContent(R.id.ListView01));
		mTabHost.setCurrentTab(0);

		bage = new BlackMessage(this);
		yage = new YiSiMessage(this);

		final Cursor cursor_black = bage.select();
		while (cursor_black.moveToNext()) {
			String text = cursor_black.getString(cursor_black
					.getColumnIndex(bage.FIELD_TEXT));
			list.add(text);
			// String
			// number=cursor_bage.getString(cursor_bage.getColumnIndex(bage.FIELD_NUMBER));
			// list.add(number);

		}
		Cursor cursor_yisi = yage.select();
		final Cursor cursor_tb = tb.select();
		if (cursor_yisi != null) {
			while (cursor_yisi.moveToNext()) {
				String number = cursor_yisi.getString(cursor_yisi
						.getColumnIndex(yage.FIELD_NUMBER));
				String text = cursor_yisi.getString(cursor_yisi
						.getColumnIndex(yage.FIELD_TEXT));
				listt.add(number);
				list_text.add(text);
			}
		}

		SimpleCursorAdapter adapter_bage = new SimpleCursorAdapter(Test.this,
				android.R.layout.simple_list_item_2, cursor_black,
				new String[] { BlackMessage.FIELD_NUMBER,
						BlackMessage.FIELD_TEXT }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		list1.setAdapter(adapter_bage);
		list1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(Test.this).setTitle("操作").setMessage(
						"确认要删除此消息吗?").setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								// bage.delete(Integer.toString(index));
								bage.delete(list.get(position).toString());
								list.remove(position);
								Cursor cursor_bage = bage.select();
								SimpleCursorAdapter adapter_bage = new SimpleCursorAdapter(
										Test.this,
										android.R.layout.simple_list_item_2,
										cursor_bage, new String[] {
												BlackMessage.FIELD_NUMBER,
												BlackMessage.FIELD_TEXT },
										new int[] { android.R.id.text1,
												android.R.id.text2 });
								list1.setAdapter(adapter_bage);

							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).show();
			}

		});

		SimpleCursorAdapter adapter_yage = new SimpleCursorAdapter(Test.this,
				R.layout.message, cursor_yisi, new String[] {
						YiSiMessage.FIELD_NUMBER, YiSiMessage.FIELD_TEXT },
				new int[] { R.id.listTextView1, R.id.listTextView2 });
		list2.setAdapter(adapter_yage);
		list2.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(Test.this).setTitle("转移号码").setMessage(
						"确认要将此号码放入黑名单吗？").setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								yisi_number = listt.get(i + 1).toString();
								while (cursor_tb.moveToNext()) {
									String bage_number = cursor_tb.getString(1);
									// if (yisi_number.endsWith(bage_number)) {
									if (yisi_number.indexOf(bage_number) != -1) {
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

									String number = listt.get(i + 1).toString();
									if (number.startsWith("+86")) {
										number = number.substring(3);
									} else if (number.startsWith("106")) {
										// number.replace("106", "");
									} else if (number.startsWith("12520")) {
										number = number.substring(5);
									}
									tb.insert(number);
								}

							}
						})
				/* 砞﹚铬跌怠ㄆン */
				.setNeutralButton(R.string.delete,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Cursor cursor_yage = yage.select();
								while (cursor_yage.moveToNext()) {
									list_text
											.add(cursor_yage
													.getString(cursor_yage
															.getColumnIndex(yage.FIELD_TEXT)));

								}
								String text=list_text.get(position).toString();

								yage.delete(text);
								list_text.remove(position);
								
								 Cursor cursor_yageg = yage.select();
								SimpleCursorAdapter adapter_yage = new SimpleCursorAdapter(
										Test.this, R.layout.message,
										cursor_yageg, new String[] {
												YiSiMessage.FIELD_NUMBER,
												YiSiMessage.FIELD_TEXT },
										new int[] { R.id.listTextView1,
												R.id.listTextView2 });
								list2.setAdapter(adapter_yage);
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
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
