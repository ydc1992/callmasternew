package cn.kfkx.message;

import java.util.ArrayList;
import java.util.List;

import cn.kfkx.service.MessageService;


import cn.kfkx.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class BlackList extends Activity {
	private LinearLayout lt;
	private ListView list;
	private EditText et;
	private ToDoDB tb;
	private String text = "";
	private List ls = null;
	private String shit = "";
	private static final int RE = 0x000001;

	protected void onCreate(Bundle savedInstanceState) {
		MessageService messageService = new MessageService(this);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.black);
		tb = new ToDoDB(this);
		ls = new ArrayList();
		lt = (LinearLayout) this.findViewById(R.id.LinearLayout01);
		LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		list = new ListView(this);
		lt.addView(list, parm);
		Bundle b = this.getIntent().getExtras();
		if (b != null) {

			shit = b.getString("shift_balck");
			if (shit != null) {
				tb.insert(shit);
				Cursor c = tb.select();
				SimpleCursorAdapter adapter = new SimpleCursorAdapter(
						BlackList.this, R.layout.list, c,
						new String[] { ToDoDB.FIELD_TEXT },
						new int[] { R.id.listTextView1 });
				list.setAdapter(adapter);

			}
		}

		Cursor c = tb.select();
		while (c.moveToNext()) {
			ls.add(c.getString(c.getColumnIndex(tb.FIELD_TEXT)));
			//上传到云端
			messageService.saveByNumber(c.getString(c.getColumnIndex(tb.FIELD_TEXT)));
		}
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(BlackList.this,
				R.layout.list, c, new String[] { ToDoDB.FIELD_TEXT },
				new int[] { R.id.listTextView1 });
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (String) ls.get(position);

				new AlertDialog.Builder(BlackList.this).setTitle("操作此号码")
						.setMessage("要把此号码").setPositiveButton("删除",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										tb.delete(text);
										Cursor c = tb.select();
										SimpleCursorAdapter adapter = new SimpleCursorAdapter(
												BlackList.this,
												R.layout.list,
												c,
												new String[] { ToDoDB.FIELD_TEXT },
												new int[] { R.id.listTextView1 });
										list.setAdapter(adapter);

									}

								}).setNegativeButton("转移至白名单",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										tb.delete(text);
										Cursor c2 = tb.select();
										SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(
												BlackList.this,
												R.layout.list,
												c2,
												new String[] { ToDoDB.FIELD_TEXT },
												new int[] { R.id.listTextView1 });
										list.setAdapter(adapter2);
										Intent i = new Intent(BlackList.this,
												White.class);
										if (!text.equals("")) {
											Bundle b = new Bundle();
											b.putString("shift_white", text);
											i.putExtras(b);
										}
										BlackList.this.startActivity(i);

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

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem me_edit = menu.add(0, 0, 0, R.string.edit);
		{
			me_edit.setIcon(R.drawable.ic_menu_edit);

		}
		// menu.add(0, 1, 1, R.string.delete);
		MenuItem deleteall = menu.add(0, 2, 2, R.string.deleteall);
		{

			deleteall.setIcon(R.drawable.ic_menu_close_clear_cancel);

		}
		MenuItem mm = menu.add(0, 3, 3, R.string.shift);
		{
			mm.setIcon(R.drawable.ic_menu_invite);

		}
		MenuItem m_shift=menu.add(0, 4, 4, R.string.shift_tab);{
			
			m_shift.setIcon(R.drawable.ic_menu_rotate);
		}

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case 0:
			LayoutInflater factory = LayoutInflater.from(this);
			View editview = factory.inflate(R.layout.editmessage, null);
			Builder my = new AlertDialog.Builder(this);
			my.setView(editview);
			et = (EditText) editview.findViewById(R.id.EditText01);

			my.setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							String text_t = et.getText().toString();
							if (text_t.equals("")) {

								return;
							}
							tb.insert(text_t);
							Cursor c = tb.select();
							ls.add(text_t);
							SimpleCursorAdapter adapter = new SimpleCursorAdapter(
									BlackList.this, R.layout.list, c,
									new String[] { ToDoDB.FIELD_TEXT },
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
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					BlackList.this, R.layout.list, c,
					new String[] { ToDoDB.FIELD_TEXT },
					new int[] { R.id.listTextView1 });
			list.setAdapter(adapter);

			break;
		case 2:
			tb.deleteall();

			Cursor c1 = tb.select();
			SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(
					BlackList.this, R.layout.list, c1,
					new String[] { ToDoDB.FIELD_TEXT },
					new int[] { R.id.listTextView1 });
			list.setAdapter(adapter1);

			break;
		case 3:

			Intent i = new Intent(BlackList.this, White.class);
			BlackList.this.startActivity(i);
			break;
		case 4:
			Intent it = new Intent(BlackList.this, Test.class);
			BlackList.this.startActivity(it);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

}
