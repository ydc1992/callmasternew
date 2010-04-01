package net.kfkx.callactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kfkx.message.ToDoDB;
import net.kfkx.phone.Phone;
import net.kfkx.phone.WebBlack;
import net.kfkx.service.MessageService;
import net.kfkx.service.PhoneSqliteService;
import net.kfkx.service.WebBlackService;
import net.kfkx.service.WebBlackSqliteService;

import android.database.Cursor;
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
	public void testYYY(){
		ToDoDB doDB = new ToDoDB(getContext());
		Cursor cursor = doDB.select();
		while(cursor.moveToNext()){
			Log.i(TAG, cursor.getString(1)+"+++++");
		}
		
	}
	public void testFile() throws Exception{
		WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(getContext());
		int i = 0xF0;
		WebBlackService webBlackService = new WebBlackService(getContext());
		WebBlackSqliteService webSqliteService = new WebBlackSqliteService(getContext());
		int version = webBlackService.getVersion();
		List<WebBlack> lists = webSqliteService.findAll();
		//List<WebBlack> list = webBlackService.query();
		//boolean boo = webSqliteService.updateWebBlack(list);
		webSqliteService.deleteAll();
		/*for(WebBlack webBlack : list){
		    webSqliteService.save(webBlack);
        }*/
		//Log.i(TAG, boo+"+++++");
		for(WebBlack webBlack : lists){
			//blackSqliteService.save(webBlack);
			Log.i(TAG, webBlack.getNumber()+"+++++"+version+"++++++"+webBlack.getType());
		}
	}
	public void testold(){
	    WebBlackService webBlackService = new WebBlackService(getContext());
	    WebBlackSqliteService webSqliteService = new WebBlackSqliteService(getContext());
	    int oldVersion = webSqliteService.findVersion();
	    Log.i(TAG, oldVersion+"+++++++++++++");
	}
	public void testGaoXinBao() throws Exception{
		WebBlackSqliteService webBlackService = new WebBlackSqliteService(getContext());
		MessageService messageService = new MessageService(getContext());
		//String area = webBlackService.findArea("15210439426");
		messageService.saveByNumber("15210439426");
		//Log.i(TAG, area+"++++++++");
	}
	public void testT() throws Exception{
		WebBlackSqliteService webBlackSqliteService = new WebBlackSqliteService(getContext());
		//webBlackSqliteService.deleteAll();
		List<WebBlack> blacklists  =  webBlackSqliteService.findAll();
		int count = webBlackSqliteService.getCount();
		Log.i(TAG, count+"++++++");
		for(WebBlack blacklist : blacklists){
			Log.i(TAG, blacklist.getBlackid()+"___"+blacklist.getType()+"++++++"+blacklist.getNumber());
			//String result = SendUp.addToWeb(blacklist, getContext());
			//Log.i(TAG, result+"++++++++++++++++++");
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
}
