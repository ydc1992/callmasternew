package cn.opda.service;


import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

public class BelongingService {
	private static final String TAG = "BelongingService";
	private Context context;
    public BelongingService(Context context) {
		this.context = context;
	}
    public int read(String number) throws Exception{
    	InputStream is =null;
    	String prenum = String.copyValueOf(number.toCharArray(), 0, 3);
    	String num = String.copyValueOf(number.toCharArray(), 3, 4);
    	int nn = Integer.parseInt(num);
    	int pre = Integer.parseInt(prenum);
    	switch (pre) {
		case 130:
			is = readFromCdcard("m130.idx");
			break;
		case 131:
			is = readFromCdcard("m131.idx");
			break;
		case 132:
			is = readFromCdcard("m132.idx");
			break;
		case 133:
			is = readFromCdcard("m133.idx");
			break;
		case 134:
			is = readFromCdcard("m134.idx");
			break;
		case 135:
			is = readFromCdcard("m135.idx");
			break;
		case 136:
			is = readFromCdcard("m136.idx");
			break;
		case 137:
			is = readFromCdcard("m137.idx");
			break;
		case 138:
			is = readFromCdcard("m138.idx");
			break;
		case 139:
			is = readFromCdcard("m131.idx");
			break;
		case 150:
			is = readFromCdcard("m150.idx");
			break;
		case 151:
			is = readFromCdcard("m151.idx");
			break;
		case 152:
			is = readFromCdcard("m152.idx");
			break;
		case 153:
			is = readFromCdcard("m153.idx");
			break;
		case 154:
			is = readFromCdcard("m154.idx");
			break;
		case 155:
			is = readFromCdcard("m155.idx");
			break;
		case 156:
			is = readFromCdcard("m156.idx");
			break;
		case 157:
			is = readFromCdcard("m157.idx");
			break;
		case 158:
			is = readFromCdcard("m158.idx");
			break;
		case 159:
			is = readFromCdcard("m159.idx");
			break;
		case 188:
			is = readFromCdcard("m188.idx");
			break;
		case 189:
			is = readFromCdcard("m189.idx");
			break;

		default:
			break;
		}
    	
    	byte[] buf = new byte[2];
		//is.reset();
		is.skip(nn * 2);
		is.read(buf, 0, 2);
		//is.close();
		return byteToInt(buf);
    }
	public static int byteToInt(byte[] b) {  
        int mask = 0xff;  
        int temp = 0;  
        int n = 0;  
        for(int i=0;i<b.length;i++){  
        	//¸ßµÍ×Ö½Ú»»Î»
        	temp = (b[i]&mask) << 8 * i;  
        	n|=temp;  
        }  
        return n;  
	} 
	public InputStream readFromCdcard(String name)throws Exception{
		AssetManager am = context.getAssets();  
			InputStream is = am.open(name);
			return is;
		/*if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File file = new File("");
			File savefile = new File(file, name);
			FileInputStream fileInputStream = new FileInputStream(savefile);
			return fileInputStream;
		}else{
			throw new Exception("SDcard not exsit");
		}*/
	}
}
