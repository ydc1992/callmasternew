package cn.kfkx.contact;

import cn.kfkx.adapter.CallCursorAdapter;
import cn.kfkx.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BaseContactList extends ListActivity {
	private static final String TAG = "BaseContactList";
	protected String sort = "DESC";
	
	/**
	 * 根据查询条件或排序方式，进行数据查询并将查询到的结果填充到list的行
	 * @param where
	 * @param order
	 */
	protected void setListAdapter(String where, String order){
		if (order == null){
			order = CallLog.Calls.DEFAULT_SORT_ORDER;
		}
		Cursor cursor = getContentResolver().query(Contacts.People.CONTENT_URI,  
		        null,  
		        null,  
		        null, null);
		/*Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, where, null, order);*/
		//将cursor生命周期交由activity来管理
		startManagingCursor(cursor);
		CallCursorAdapter adapter = new CallCursorAdapter(this,
				R.layout.contactinfo, cursor,
				new String[] { "number", "name"},
				new int[] { R.id.TextNumber,R.id.TextName});
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final View vv = v;
		LinearLayout layout = (LinearLayout)vv;
		final String num = ((TextView) layout.findViewById(R.id.TextNumber)).getText().toString();
		final String name = ((TextView) layout.findViewById(R.id.TextName)).getText().toString();
		/*LinearLayout layout = (LinearLayout)v;
		TextView numberText = (TextView)layout.findViewById(R.id.TextNumber);
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+numberText.getText().toString()));
		startActivity(callIntent);*/
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.contactTitle);
			alertDialogBuilder.setMessage(name);
	        alertDialogBuilder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+num));
	        		startActivity(callIntent);
	            }
	        });
	        alertDialogBuilder.setNegativeButton(R.string.editcontact, new DialogInterface.OnClickListener() {
	        	final Cursor cursor = getContentResolver().query(Contacts.People.CONTENT_URI,  
	    		        null,  
	    		        Contacts.People.NUMBER +" = "+num,  
	    		        null, null);
	        	public void onClick(DialogInterface dialog, int which) {
	        		//startActivity(new Intent(Intent.ACTION_EDIT, )); 
	        		cursor.moveToFirst();
	        		String id = cursor.getString(cursor.getColumnIndex("_id"));
	        		Uri uri = Uri.parse("content://contacts/people/"+id);
	        		startActivity(new Intent(Intent.ACTION_EDIT, uri)); 
	        	}
	        });
	        alertDialogBuilder.setNeutralButton(R.string.sms, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	Uri smsToUri = Uri.parse("smsto://"+num);
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					BaseContactList.this.startActivity(smsIntent);
	            }
	        });
	        alertDialogBuilder.setCancelable(true);

	        final AlertDialog alertDialog = alertDialogBuilder.create();

	        alertDialog.show();

	}
}
