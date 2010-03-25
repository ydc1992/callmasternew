package cn.opda.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.content.res.AssetManager;

import cn.opda.phone.WebBlack;

public class WebBlackService {
	private static final String TAG = "ContactService";
	private Context context;
	
	public WebBlackService(Context context) {
		this.context = context;
	}
	public int getVersion() throws Exception{
		String urlPath = "file:////sdcard/test.xml";
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream inStream = conn.getInputStream();
		if(conn.getResponseCode()==200)
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser(); // ����������
			// ���ý�������������ԣ�http://xml.org/sax/features/namespaces = true
			// ��ʾ���������ռ�����
		//	saxParser.setProperty("http://xml.org/sax/features/namespaces", true);
			WebBlackHandler handler = new WebBlackHandler();
			saxParser.parse(inStream, handler);
			inStream.close();
			conn.disconnect();
			return handler.getVersion();
		}else{
			throw new Exception("url connection fail:"+ urlPath);
		}
	}
	public int getTestVersion() throws Exception{
		AssetManager am = context.getAssets();  
		InputStream inStream = am.open("test.xml");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser(); // ����������
		WebBlackHandler handler = new WebBlackHandler();
		saxParser.parse(inStream, handler);
		inStream.close();
		return handler.getVersion();
	}
	public List<WebBlack> testquery() throws Exception{
		AssetManager am = context.getAssets();  
		InputStream inStream = am.open("test.xml");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser(); // ����������
		WebBlackHandler handler = new WebBlackHandler();
		saxParser.parse(inStream, handler);
		inStream.close();
		return handler.getBlacks();
	}
	
	public List<WebBlack> query() throws Exception{
		String urlPath = "???????????????";
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