package cn.opda.callactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.opda.phone.Phone;
import cn.opda.phone.WebBlack;
import cn.opda.service.BlackListSqliteService;
import cn.opda.service.PhoneSqliteService;
import cn.opda.service.WebBlackService;
import cn.opda.service.WebBlackSqliteService;
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
		int version = webBlackService.getTestVersion();
		List<WebBlack> list = webBlackService.testquery();
		for(WebBlack webBlack : list){
			blackSqliteService.save(webBlack);
			Log.i(TAG, webBlack.getNumber()+"+++++++++++"+webBlack.getType());
		}
	}
	public void testGaoXinBao() throws Exception{
		WebBlackSqliteService webBlackService = new WebBlackSqliteService(getContext());
		String area = webBlackService.findArea("15210439426");
		Log.i(TAG, area+"++++++++");
	}
	public void testT() throws Exception{
		BlackListSqliteService blackListSqliteService = new BlackListSqliteService(getContext());
		WebBlackSqliteService webBlackSqliteService = new WebBlackSqliteService(getContext());
		List<WebBlack> blacklists  =  webBlackSqliteService.findAll();
		int count = webBlackSqliteService.getCount();
		Log.i(TAG, count+"++++++");
		for(WebBlack blacklist : blacklists){
			Log.i(TAG, blacklist.getBlackid()+"___"+blacklist.getType()+"++++++");
			/*String result = SendUp.addToWeb(blacklist, getContext());
			Log.i(TAG, result+"++++++++++++++++++");*/
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
