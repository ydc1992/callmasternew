package net.kfkx.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.kfkx.phone.Phone;
import net.kfkx.service.BelongingService;
import net.kfkx.service.PhoneSqliteService;

import net.kfkx.R;


import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;


/**
 * ��ȡ���ݺ󣬽����ݿ���䵽ָ���õ�����
 * @author Snake
 *
 */
public class CallCursorAdapter extends ResourceCursorAdapter {
	private BelongingService belongservice ;
	private PhoneSqliteService phoneService ;
	private static final String TAG = "CallCursorAdapter";
	final int DAY = 1440;				//һ��ķ���ֵ
	private int[] mTo;
	private String[] mOriginalFrom;
	
	public CallCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c);
		mOriginalFrom = from;
		mTo = to;
	}
	
	/**
	 * �����ݵ���ͼ��
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		belongservice = new BelongingService(context);
		phoneService = new PhoneSqliteService(context);
		
		// TODO Auto-generated method stub
		View[] views = new View[mTo.length];
		for (int i=0; i<views.length; i++){
			views[i] = view.findViewById(mTo[i]);
			String value = cursor.getString(cursor.getColumnIndex(mOriginalFrom[i]));
			
			if (views[i] instanceof TextView){
				if ("date".equals(mOriginalFrom[i])){
					long callTime = Long.parseLong(value);
					long newTime = new Date().getTime();
					long duration = (newTime - callTime) / (1000*60);
					
					if (duration < 60){
						value = duration+"����ǰ";
					}else if (duration >= 60 && duration < DAY){
						value = (duration/60)+"Сʱǰ";
					}else if (duration >= DAY && duration < DAY*2){
						value = "����";
					}else if (duration >= DAY*2 && duration < DAY*3){
						value = "ǰ��";
					}else if (duration >= DAY*7){
						SimpleDateFormat sdf = new SimpleDateFormat("M��dd��");
						value = sdf.format(new Date(callTime));
					}else{
						value = (duration/DAY)+"��ǰ";
					}
				}else if ("type".equals(mOriginalFrom[i])){
					ImageView imageView = (ImageView) view.findViewById(R.id.callicon);
					int type = Integer.parseInt(value);
					if (CallLog.Calls.INCOMING_TYPE == type){
						imageView.setImageResource(android.R.drawable.sym_call_incoming);
						value = "";
					}else if (CallLog.Calls.OUTGOING_TYPE == type){
						value = "";
						imageView.setImageResource(android.R.drawable.sym_call_outgoing);
					}else if (CallLog.Calls.MISSED_TYPE == type){
						value = "";
						imageView.setImageResource(android.R.drawable.sym_call_missed);
					}
				}else if ("name".equals(mOriginalFrom[i])){
					if (null == value || "".equals(value)){
						value = cursor.getString(cursor.getColumnIndex("number"));
						/*TextView textView = (TextView) view.findViewById(R.id.TextNumber);
						textView.setText("");*/
					}
				}else if("number".equals(mOriginalFrom[i])){
					int num = 0;
					String fullnum = "";
					try {
						fullnum = cursor.getString(cursor.getColumnIndex("number"));
						num = belongservice.read(fullnum);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						Log.e(TAG, e1.toString());
					}
					if(fullnum!=null){
						if(String.copyValueOf(fullnum.toCharArray(), 0, 1).equals("0")&&fullnum.length()==11){
							String temp = String.copyValueOf(String.valueOf(fullnum).toCharArray(), 1, 1);
							int ii = Integer.parseInt(temp);
							if(ii<=2){
								String pre = String.copyValueOf(String.valueOf(fullnum).toCharArray(), 0, 3);
								Phone phone = phoneService.findByAreaNum(pre);
								if(phone == null){
									phone = new Phone("δ֪����", "δ֪����", "");
								}
								TextView areaView = (TextView)view.findViewById(R.id.area);
								areaView.setText(phone.getProvince()+" "+phone.getCity()+" "+phone.getAreaCode());
							}else{
								String pre = String.copyValueOf(String.valueOf(fullnum).toCharArray(), 0, 4);
								Phone phone = phoneService.findByAreaNum(pre);
								if(phone == null){
									phone = new Phone("δ֪����", "δ֪����", "");
								}
								TextView areaView = (TextView)view.findViewById(R.id.area);
								areaView.setText(phone.getProvince()+" "+phone.getCity()+" "+phone.getAreaCode());
							}
						}else if(String.copyValueOf(fullnum.toCharArray(), 0, 1).equals("1")&&fullnum.length()==11){
							String nn = "0"+num;
							Phone phone = phoneService.findByAreaNum(nn);
							if(phone == null){
								phone = new Phone("δ֪����", "δ֪����", "");
							}
							TextView areaView = (TextView)view.findViewById(R.id.area);
							areaView.setText(phone.getProvince()+" "+phone.getCity()+" "+phone.getAreaCode());
						}else{
							Phone phone = new Phone("δ֪����", "δ֪����", "");
							TextView areaView = (TextView)view.findViewById(R.id.area);
							areaView.setText(phone.getProvince()+" "+phone.getCity()+" "+phone.getAreaCode());
						}
					}
					
				}
				
				setText((TextView)views[i], value);
			}
		}
		
		//final Context mContext = context;
		//final TextView mNumber = (TextView)view.findViewById(R.id.TextNumber);
		/*ImageView mailButton = (ImageView)view.findViewById(R.id.MailButton);
		if (mailButton != null){
			//Ϊ�������ͼ����Ӵ����¼���ʹ����뷢�Ͷ��Ž���
			mailButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Uri smsToUri = Uri.parse("smsto://"+mNumber.getText());
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					mContext.startActivity(smsIntent);
				}
			});
		}*/
	}
	
	public void setText(TextView v, String text){
		v.setText(text);
	}
}
