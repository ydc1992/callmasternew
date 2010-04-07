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
import android.os.Bundle;

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

public class BlackList extends Activity {
	private LinearLayout lt;
	private ListView list;
	private EditText et;
	private ToDoDB tb;
	private String text = "";
	private List ls = null;
	private String shit = "";
	private static final int RE = 0x000001;
	private Cursor c = null;
	private WhiteList wt;

	protected void onCreate(Bundle savedInstanceState) {
		// MessageService messageService = new MessageService(this);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.blackmessage);
		tb = new ToDoDB(this);
		ls = new ArrayList();
		wt=new WhiteList(this);
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
						BlackList.this, R.layout.listmessage, c,
						new String[] { ToDoDB.FIELD_TEXT },
						new int[] { R.id.listTextView1 });
				list.setAdapter(adapter);

			}
		}

		c = tb.select();
		while (c.moveToNext()) {
			ls.add(c.getString(c.getColumnIndex(tb.FIELD_TEXT)));
			// �ϴ����ƶ�
			// messageService.saveByNumber(c.getString(c.getColumnIndex(tb.FIELD_TEXT)));
		}

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(BlackList.this,
				R.layout.listmessage, c, new String[] { ToDoDB.FIELD_TEXT },
				new int[] { R.id.listTextView1 });
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (String) ls.get(position);

				new AlertDialog.Builder(BlackList.this).setTitle("�����˺���")
						.setMessage("Ҫ�Ѵ˺���").setPositiveButton("ɾ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										tb.delete(text);
										Cursor c = tb.select();
										SimpleCursorAdapter adapter = new SimpleCursorAdapter(
												BlackList.this,
												R.layout.listmessage,
												c,
												new String[] { ToDoDB.FIELD_TEXT },
												new int[] { R.id.listTextView1 });
										list.setAdapter(adapter);

									}

								}).setNegativeButton("ת����������",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										tb.delete(text);
										Cursor c2 = tb.select();
										SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(
												BlackList.this,
												R.layout.listmessage,
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
								}).setNeutralButton("ȡ��",
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
			Intent it = new Intent(BlackList.this, Test.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			BlackList.this.startActivity(it);}

		return super.onKeyDown(keyCode, event);
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
		MenuItem m_shift = menu.add(0, 4, 4, R.string.shift_tab);
		{

			m_shift.setIcon(R.drawable.ic_menu_rotate);
		}

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case 0:
			LayoutInflater factory = LayoutInflater.from(this);
			View editview = factory.inflate(R.layout.editmess, null);
			Builder my = new AlertDialog.Builder(this);
			my.setView(editview);
			my.setTitle("�������");
			et = (EditText) editview.findViewById(R.id.EditText01);

			my.setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							String text_t = et.getText().toString();
							if (text_t.indexOf("+86") != -1) {
								text_t = text_t.substring(3);
								// Toast.makeText(BlackList.this,
								// "�������+86", Toast.LENGTH_LONG)
								// .show();
								// return;
							}
							Cursor cr_wt=wt.select();
							if(cr_wt.moveToNext()){
								String number=cr_wt.getString(1);
								if(text_t.equals(number)){
									Toast.makeText(BlackList.this,"����������",Toast.LENGTH_SHORT).show();
									return;
									
								}
								
								
							}
							Cursor cr = tb.select();
							if (cr.getCount() > 0) {
								while (cr.moveToNext()) {
									String number = cr.getString(1);
									if (text_t.equals(number)) {
										Toast.makeText(BlackList.this,
												"������������", Toast.LENGTH_LONG)
												.show();
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
									BlackList.this, R.layout.listmessage, c,
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
					BlackList.this, R.layout.listmessage, c,
					new String[] { ToDoDB.FIELD_TEXT },
					new int[] { R.id.listTextView1 });
			list.setAdapter(adapter);

			break;
		case 2:
			tb.deleteall();

			Cursor c1 = tb.select();
			SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(
					BlackList.this, R.layout.listmessage, c1,
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
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			BlackList.this.startActivity(it);
			break;
			//this.finish();

		}

		return super.onOptionsItemSelected(item);
	}

}
