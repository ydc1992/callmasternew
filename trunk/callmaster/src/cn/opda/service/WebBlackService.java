package cn.opda.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.util.Log;

import cn.opda.phone.WebBlack;

public class WebBlackService {
	private static final String TAG = "ContactService";
	private Context context;
	
	public WebBlackService(Context context) {
		super();
		this.context = context;
	}

	public List<WebBlack> query() throws Exception{
		String urlPath = "http://guanjia.koufeikexing.com/koufeikexing/defener/phone.php?";
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream inStream = conn.getInputStream();
		if(conn.getResponseCode()==200){
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser(); // ����������
			// ���ý�������������ԣ�http://xml.org/sax/features/namespaces = true
			// ��ʾ���������ռ�����
		//	saxParser.setProperty("http://xml.org/sax/features/namespaces", true);
			WebBlackHandler handler = new WebBlackHandler();
			saxParser.parse(inStream, handler);
			inStream.close();
			conn.disconnect();
			return handler.getBlacks();
		}else{
			throw new Exception("url connection fail:"+ urlPath);
		}
	}
	
	private String readData(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while( (length = inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, length);
		}
		String out = new String(outStream.toByteArray());
		outStream.close();
		inStream.close();
		return out;
	}
}