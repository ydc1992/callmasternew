package cn.kfkx.callactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.kfkx.phone.Phone;
import cn.kfkx.phone.WebBlack;
import cn.kfkx.service.MessageService;
import cn.kfkx.service.PhoneSqliteService;
import cn.kfkx.service.WebBlackService;
import cn.kfkx.service.WebBlackSqliteService;

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
	public void testFile() throws Exception{
		WebBlackSqliteService blackSqliteService = new WebBlackSqliteService(getContext());
		int i = 0xF0;
		WebBlackService webBlackService = new WebBlackService(getContext());
		WebBlackSqliteService webSqliteService = new WebBlackSqliteService(getContext());
		//int version = webBlackService.getVersion();
		List<WebBlack> lists = webSqliteService.findAll();
		//List<WebBlack> list = webBlackService.query();
		//boolean boo = webSqliteService.updateWebBlack(list);
		//webSqliteService.deleteAll();
		/*for(WebBlack webBlack : list){
		    webSqliteService.save(webBlack);
        }*/
		//Log.i(TAG, version+"+++++");
		for(WebBlack webBlack : lists){
			//blackSqliteService.save(webBlack);
			Log.i(TAG, webBlack.getNumber()+"+++++++++++"+webBlack.getType());
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
	private boolean checkSize(String path){
    	boolean boo = false;
    	File file = new File(path);
		Log.i(TAG, file.length()+"+++++++++++++");
    	if(file.length()>5000){
    		boo = true;
    	}
    	return boo;
    }
	public void tesY(){
		boolean boo = checkSize("/data/data/net.kfkx/databases/kfkx");
		Log.i(TAG, boo+"+++++++++++++");
	}
}
