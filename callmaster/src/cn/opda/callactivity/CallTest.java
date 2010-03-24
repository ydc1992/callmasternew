package cn.opda.callactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.opda.net.upload.HttpRequester;
import cn.opda.phone.Blacklist;
import cn.opda.phone.Phone;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.PhoneSqliteService;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import android.util.Log;

public class CallTest extends AndroidTestCase {
	private static final String TAG = "callTest";
	public void testSave (){
		PhoneSqliteService phoneSqliteService = new PhoneSqliteService(getContext());
		List<Phone> phones = readTxt("/sdcard/x.txt");
		Log.i(TAG, "+++++++++++++"+phones.size());
        for(Phone phone : phones){
        	phoneSqliteService.save(phone);
        }
	}
	public void testT() throws Exception{
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(getContext());
		List<Blacklist> blacklists  =  blackListSqliteService.findAll();
		for(Blacklist blacklist : blacklists){
			Log.i(TAG, blacklist.getBlackid()+"___-----"+blacklist.getUptype()+"%%%"+blacklist.getType()+"++++++");
			/*String result = SendUp.addToWeb(blacklist, getContext());
			Log.i(TAG, result+"++++++++++++++++++");*/
		}
	}
	public void testContect() throws Exception{
	/*	Cursor cursor = getContext().getContentResolver().query(Contacts.People.CONTENT_URI,  
		        null,  
		        null,  
		        null, null);*/
		
		//Object ob = Class.forName("android.provider.ContactsContract").newInstance();
		Class.forName("android.provider.ContactsContract");
		/*StringBuilder content = new StringBuilder();
    	while(cursor.moveToNext()){    		 
    		String contactId = cursor.getString(cursor.getColumnIndex(People._ID));  
    		 String name = cursor.getString(cursor.getColumnIndex(People.NAME));  
    		 content.append("姓名："+ name);
    		 Cursor phones = getContext().getContentResolver().query(Phones.CONTENT_URI,  
    			        null,  
    			        Phones.PERSON_ID +" = "+ contactId,  
    			        null, null);
    		 content.append("的电话有：");
    		while (phones.moveToNext()) {
    			String phoneNumber = phones.getString(phones.getColumnIndex(Phones.NUMBER));
    			content.append(phoneNumber).append(",");
    		}  
    		phones.close();
        }*/
		//Log.i(TAG, content.toString());
	}
	public void getnetTest(){
		//GetNet.hasInternet(activity)
	}
	public void testfindByNumber(){
		BlackListSqliteService service = new BlackListSqliteService(getContext());
		Blacklist blacklist = service.findByNumber("15811363682");
		Log.i(TAG, blacklist.getBlackid()+"tttt"+blacklist.getNumber());
	}
	
	public void testFind(){
		PhoneSqliteService phoneService = new PhoneSqliteService(getContext());
		Phone phone = phoneService.find(1);
		phoneService.update(phone);
			Log.i(TAG, phone.toString());
	}
	public void testFindAll(){
		PhoneSqliteService phoneService = new PhoneSqliteService(getContext());
		List<Phone> phones = phoneService.findAll();
		for(Phone phone : phones){
		}
	}
	private  List<Phone> readTxt(String filename){
		List<Phone> phones = new ArrayList<Phone>();
		try{
			FileReader fr = new FileReader(new File(filename));
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			while(s!=null){
			 	String b[]=s.split(","); //this is what i have to remember, confused by it for a long time,lol
			 	Phone phone = new Phone();
			 	phone.setProvince(b[0]);
			 	phone.setCity(b[1]);
			 	phone.setAreaCode(b[2]);
			 	phones.add(phone);
				s = br.readLine();
			}
			br.close();
		}catch(IOException e){
			System.out.println(e);
		}
		return phones;
	}
	private String addToWeb(Blacklist blacks){
    	String url = "http://guanjia.koufeikexing.com/koufeikexing/defener/phone.php?";
    	TelephonyManager tm = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);  
    	String imei = tm.getDeviceId();  
    	String tel = tm.getLine1Number(); 
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", imei+"");
		params.put("number",blacks.getNumber()+"");
		params.put("type", blacks.getType()+"");
		params.put("timelength", blacks.getTimelength()+"");
		params.put("timehappen", blacks.getTimehappen()+"");
		params.put("remark", blacks.getRemark()+"");
		params.put("version", "1.5");
		params.put("platform", "2");
		String s = HttpRequester.httpPost(url, params);
		return s;
    }
}
