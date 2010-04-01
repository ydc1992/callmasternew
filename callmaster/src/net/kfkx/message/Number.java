package net.kfkx.message;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.widget.Toast;

public class Number extends Activity {

	private String phone;
	private String number;
	private String body;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle b = this.getIntent().getExtras();
		phone = b.getString("a");
		body = b.getString("b");
		ContentResolver cr = this.getContentResolver();
		String[] pro = { People.NUMBER };
		Cursor cursor = cr.query(android.provider.Contacts.People.CONTENT_URI,
				pro, null, null, Contacts.People.DEFAULT_SORT_ORDER);
		while (cursor.moveToNext()) {
			number = cursor.getString(cursor.getColumnIndex(People.NUMBER));
			if (phone.equals(number)) {
				Toast.makeText(this, "这是属于联系人里的名单", Toast.LENGTH_LONG);
				Number.this.finish();
			}

		}		
//		Intent i = new Intent(Number.this, BlackList.class);
//		Bundle be = new Bundle();
//		be.putString("c", phone);
//		be.putString("d",body);
//		Number.this.startActivity(i);
		
		Intent i=new Intent(Number.this,White.class);
		Bundle be=new Bundle();
		be.putString("phone",phone);
		be.putString("body",body);
		i.putExtras(be);
		Number.this.startActivity(i);
		
		

	}

}
