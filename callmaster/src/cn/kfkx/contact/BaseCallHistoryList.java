package cn.kfkx.contact;


import cn.kfkx.adapter.CallCursorAdapter;
import cn.kfkx.net.upload.SendUp;
import cn.kfkx.phone.Blacklist;
import cn.kfkx.phone.OpdaState;
import cn.kfkx.service.BlackListSqliteService;
import cn.kfkx.service.ShareService;
import cn.kfkx.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * ListActivity基类，实现了每行单击事件和查询数据方法
 * @author Snake
 *
 */
public class BaseCallHistoryList extends ListActivity {
	private BlackListSqliteService blackService = new BlackListSqliteService(this);
	private static final String TAG = "BaseContactList";
	protected String sort = "DESC";
	String typename;
	
	/**
	 * 根据查询条件或排序方式，进行数据查询并将查询到的结果填充到list的行
	 * @param where
	 * @param order
	 */
	protected void setListAdapter(String where, String order){
		if (order == null){
			order = CallLog.Calls.DEFAULT_SORT_ORDER+" limit 0, 100";
		}
		
		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, where, null, order);
		//将cursor生命周期交由activity来管理
		startManagingCursor(cursor);
		CallCursorAdapter adapter = new CallCursorAdapter(this,
				R.layout.callhostoryinfo, cursor,
				new String[] { "number", "name", "date", "type"},
				new int[] { R.id.TextNumber,R.id.TextName, R.id.TextDuration, R.id.TextType});
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
		final Cursor cursor = getContentResolver().query(Contacts.People.CONTENT_URI,  
		        null,  
		        Contacts.People.NUMBER +" = "+num,  
		        null, null);
		if(cursor.getCount()>0){
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.hostoryTitle);
			alertDialogBuilder.setMessage(name);
	        alertDialogBuilder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+num));
	        		startActivity(callIntent);
	            }
	        });
	        alertDialogBuilder.setNegativeButton(R.string.editcontact, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
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
					BaseCallHistoryList.this.startActivity(smsIntent);
	            }
	        });
	        alertDialogBuilder.setCancelable(true);

	        final AlertDialog alertDialog = alertDialogBuilder.create();

	        alertDialog.show();
		}
		else if(blackService.findByNumber(num)!=null){
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.hostoryTitle);
			alertDialogBuilder.setMessage(num);
	        alertDialogBuilder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+num));
	        		startActivity(callIntent);
	            }
	        });
	        alertDialogBuilder.setNegativeButton(R.string.deletefromblack, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        			blackService.deleteByNumber(num);
	        	}
	        });
	        alertDialogBuilder.setNeutralButton(R.string.sms, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	Uri smsToUri = Uri.parse("smsto://"+num);
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					BaseCallHistoryList.this.startActivity(smsIntent);
	            }
	        });
	        alertDialogBuilder.setCancelable(true);

	        final AlertDialog alertDialog = alertDialogBuilder.create();

	        alertDialog.show();

		}else{
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.hostoryTitle);
			alertDialogBuilder.setMessage(num);
	        alertDialogBuilder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+num));
	        		startActivity(callIntent);
	            }
	        });
	        alertDialogBuilder.setNegativeButton(R.string.addblack, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		LayoutInflater factory = LayoutInflater.from(BaseCallHistoryList.this);
	    			final View editview = factory.inflate(R.layout.hostory_edit, null);
	            	Builder my = new AlertDialog.Builder(BaseCallHistoryList.this);
	    			my.setView(editview);
	    			final EditText editRemarkText = (EditText)editview.findViewById(R.id.editremarkHostory);
	    			RadioGroup radioGroup = (RadioGroup) editview.findViewById(R.id.radioGroupHostory); 
	    			radioGroup.check(R.id.onesoundHostory);
	    			RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());


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
	    							int type1 = 0;
	    							if("一声响".equals(typename)){
	    								type1 = Blacklist.TYPE_ONESOUND;
	    							}else if("高额收费".equals(typename)){
	    								type1 = Blacklist.TYPE_OVERCHARGE;
	    							}else if("推销".equals(typename)){
	    								type1 = Blacklist.TYPE_PROMOTION;
	    							}else if("其他".equals(typename)){
	    								type1 = Blacklist.TYPE_OTHER;
	    							}else if("短信".equals(typename)){
	    								type1 = Blacklist.TYPE_MESSAGE;
	    							}
	    										
	    							String remark = editRemarkText.getText().toString();
	    							Blacklist blacklist = new Blacklist(num,type1,remark,Blacklist.HAVE_NO);
	    							blackService.savepart(blacklist);
									SharedPreferences sharedPreferences = ShareService.getShare(BaseCallHistoryList.this, "kfkx");
									int sendUpService = sharedPreferences.getInt(OpdaState.SENDUP, 1);
	    							ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    							NetworkInfo info = connectivity.getActiveNetworkInfo();
	    							if (info != null && sendUpService == 1) {
	    									Blacklist black = blackService.findByNumber(num);
	    									SendUp.addToWeb(black, BaseCallHistoryList.this);
	    									black.setUptype(Blacklist.HAVED);
	    									blackService.update(black);
	    							}
	    			    			Toast.makeText(BaseCallHistoryList.this, R.string.addsuccess, Toast.LENGTH_SHORT).show();
	    						}
	    					});
	    			my.setNegativeButton(R.string.cancel, null);
	    			my.show();
	        		
	        	}
	        });
	        alertDialogBuilder.setNeutralButton(R.string.sms, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	Uri smsToUri = Uri.parse("smsto://"+num);
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					BaseCallHistoryList.this.startActivity(smsIntent);
	            }
	        });
	        alertDialogBuilder.setCancelable(true);

	        final AlertDialog alertDialog = alertDialogBuilder.create();

	        alertDialog.show();

		}
	}
}
