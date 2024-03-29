package cn.kfkx.mes;

import java.util.ArrayList;
import java.util.List;

import cn.kfkx.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class White extends Activity {

	private LinearLayout lt;
	private ListView list;
	private EditText et;
	private WhiteList tb;
	private List ls;
	private String text = "";
	private String shit = "";
	private String phone = "";
	private String cursor_phone = "";
	private String body = "";
	private String w_number = "";
	private Cursor c = null;
	private ToDoDB t_wt;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.whitemessage);
        t_wt=new ToDoDB(this);
		tb = new WhiteList(this);
		ls = new ArrayList();
		lt = (LinearLayout) this.findViewById(R.id.LinearLayout02);
		list = new ListView(this);
		LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lt.addView(list, parm);

		Bundle b = this.getIntent().getExtras();
		if (b != null) {

			shit = b.getString("shift_white");
			if (shit != null) {
				tb.insert(shit);
				Cursor c = tb.select();
				SimpleCursorAdapter adapter = new SimpleCursorAdapter(
						White.this, R.layout.listmessage, c,
						new String[] { WhiteList.FIELD_TEXT },
						new int[] { R.id.listTextView1 });
				list.setAdapter(adapter);

			}
		}
		c = tb.select();
		while (c.moveToNext()) {
			w_number = c.getString(c.getColumnIndex(tb.FIELD_TEXT));
			ls.add(w_number);
		}
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(White.this,
				R.layout.listmessage, c, new String[] { WhiteList.FIELD_TEXT },
				new int[] { R.id.listTextView1 });
		list.setAdapter(adapter);
		c.moveToFirst();
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				text = (String) ls.get(position);

				new AlertDialog.Builder(White.this).setTitle("操作此号码")
						.setMessage("要把此号码").setPositiveButton("删除",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										tb.delete(text);
										Cursor c = tb.select();
										SimpleCursorAdapter adapter = new SimpleCursorAdapter(
												White.this,
												R.layout.listmessage,
												c,
												new String[] { WhiteList.FIELD_TEXT },
												new int[] { R.id.listTextView1 });
										list.setAdapter(adapter);

									}

								}).setNegativeButton("转移至黑名单",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										tb.delete(text);
										Cursor c2 = tb.select();
										SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(
												White.this,
												R.layout.listmessage,
												c2,
												new String[] { WhiteList.FIELD_TEXT },
												new int[] { R.id.listTextView1 });
										list.setAdapter(adapter2);
										Intent i = new Intent(White.this,
												BlackList.class);
										if (!text.equals("")) {
											Bundle b = new Bundle();
											b.putString("shift_balck", text);
											i.putExtras(b);
										}

										White.this.startActivity(i);

									}
								}).setNeutralButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();

			}

		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent it = new Intent(White.this, Test.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			White.this.startActivity(it);}

		return super.onKeyDown(keyCode, event);
		}


	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem edit = menu.add(0, 0, 0, R.string.whiteedit);
		{
			edit.setIcon(R.drawable.ic_menu_edit);
		}
		// menu.add(0, 1, 1, R.string.delete);
		MenuItem delete = menu.add(0, 2, 2, R.string.deleteall);
		{
			delete.setIcon(R.drawable.ic_menu_close_clear_cancel);
		}
		MenuItem mu = menu.add(0, 3, 3, R.string.shiftblack);
		{
			mu.setIcon(R.drawable.ic_menu_blocked_user);
		}
		MenuItem m_shift = menu.add(0, 4, 4, R.string.shift_tab);
		{
			m_shift.setIcon(R.drawable.ic_menu_rotate);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case 0:
			LayoutInflater factory = LayoutInflater.from(this);
			View editview = factory.inflate(R.layout.editmess, null);
			Builder my = new AlertDialog.Builder(this);
			my.setTitle("输入号码");
			my.setView(editview);
			et = (EditText) editview.findViewById(R.id.EditText01);
			my.setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							String text_t = et.getText().toString();
							if (text_t.indexOf("+86") != -1) {
								text_t = text_t.substring(3);
								// Toast.makeText(White.this,
								// "不能添加+86", Toast.LENGTH_LONG)
								// .show();
								// return;
							}
							Cursor cr_wt=t_wt.select();
							while(cr_wt.moveToNext()){
								
								String number=cr_wt.getString(1);
								if(number.equals(text_t)){
									
									Toast.makeText(White.this,"黑名单中已有",Toast.LENGTH_SHORT).show();
									return;
									
								}
								
								
								
								
							}
							Cursor cr = tb.select();
							if (cr.getCount() > 0) {
								while (cr.moveToNext()) {
									String number = cr.getString(1);
									if (text_t.equals(number)) {
										Toast.makeText(White.this, "白名单中已有",
												Toast.LENGTH_LONG).show();
										return;
									}
								}
							}
							if (text_t.equals("")) {

								return;
							}
							tb.insert(text_t);
							Cursor c = tb.select();
							ls.add(text_t);
							SimpleCursorAdapter adapter = new SimpleCursorAdapter(
									White.this, R.layout.listmessage, c,
									new String[] { WhiteList.FIELD_TEXT },
									new int[] { R.id.listTextView1 });
							list.setAdapter(adapter);

						}
					});
			my.setNegativeButton(R.string.no, null);
			my.show();
			break;
		case 1:

			tb.delete(text);
			Cursor c = tb.select();
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(White.this,
					R.layout.listmessage, c, new String[] { WhiteList.FIELD_TEXT },
					new int[] { R.id.listTextView1 });
			list.setAdapter(adapter);
			break;
		case 2:

			tb.deleteall();

			Cursor c1 = tb.select();
			SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(White.this,
					R.layout.listmessage, c1, new String[] { ToDoDB.FIELD_TEXT },
					new int[] { R.id.listTextView1 });
			list.setAdapter(adapter1);

			break;
		case 3:

			Intent i = new Intent(White.this, BlackList.class);
			White.this.startActivity(i);

			break;
		case 4:
			Intent it = new Intent(White.this, Test.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			White.this.startActivity(it);
			break;
		// this.finish();
		}

		return super.onOptionsItemSelected(item);
	}

}
