package net.kfkx.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.kfkx.phone.WebBlack;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


public class WebBlackService {
	private static final String TAG = "ContactService";
	private Context context;
	
	public WebBlackService(Context context) {
		this.context = context;
	}
	public int getVersion() throws Exception{
		String urlPath = "http://guanjia.koufeikexing.com/koufeikexing/defener/phonedown.php?version=1&platform=2&onlyver=1";
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream inStream = conn.getInputStream();
		if(conn.getResponseCode()==200)
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser(); // 创建解析器
			WebBlackHandler handler = new WebBlackHandler();
			saxParser.parse(inStream, handler);
			inStream.close();
			conn.disconnect();
			return handler.getVersion();
		}else{
			throw new Exception("url connection fail:"+ urlPath);
		}
	}
	public List<WebBlack> query() throws Exception{
		String urlPath = "http://guanjia.koufeikexing.com/koufeikexing/defener/phonedown.php?version=1&platform=2";
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream inStream = conn.getInputStream();
		if(conn.getResponseCode()==200){
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser(); // 创建解析器
			// 设置解析器的相关特性，http://xml.org/sax/features/namespaces = true
			// 表示开启命名空间特性
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
	private String inputStream2String(InputStream is){
	   BufferedReader in = new BufferedReader(new InputStreamReader(is));
	   StringBuffer buffer = new StringBuffer();
	   String line = "";
	   try {
		while ((line = in.readLine()) != null){
		     buffer.append(line);
		   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return buffer.toString();
	}
}
