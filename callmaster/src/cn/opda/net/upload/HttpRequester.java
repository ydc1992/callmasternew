package cn.opda.net.upload;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



import android.util.Log;
public class HttpRequester {
	private static final String TAG = "HttpRequester";
	/**
	 * 提交数据到服务器
	 * @param actionUrl 上传路径
	 * @param params 请求参数 key为参数名,value为参数值
	 */

	public static String get(String actionUrl, Map<String, String> params) {
	    try {	        
	    	StringBuilder builder = new StringBuilder();
			   builder.append(actionUrl);
			   builder.append("?");
			   for (Map.Entry<String, String> entry : params.entrySet()) {//构建表单字段内容
				   builder.append(entry.getKey());
				   builder.append("=");
				   builder.append(entry.getValue());
				   builder.append("&");
			   }
			   String temp = builder.toString();
			   String uri = temp.copyValueOf(temp.toCharArray(), 0, temp.length()-1);
	        String BOUNDARY = "---------7d4a6d158c9"; //数据分隔线
	        String MULTIPART_FORM_DATA = "multipart/form-data";
	        
	        URL url = new URL(uri);
	        Log.i(TAG, url.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(6* 1000);
	        conn.setDoInput(true);//允许输入
	        conn.setDoOutput(true);//允许输出
	        conn.setUseCaches(false);//不使用Cache
	        conn.setRequestMethod("GET");	        
	        conn.setRequestProperty("Connection", "Close");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
	        
	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();//数据结束标志	       
	        outStream.write(end_data);
	        outStream.flush();
	        int cah = conn.getResponseCode();
	        if (cah != 200) throw new RuntimeException("请求url失败");
	        InputStream is = conn.getInputStream();
	        int ch;
	        StringBuilder b = new StringBuilder();
	        while( (ch = is.read()) != -1 ){
	        	b.append((char)ch);
	        }
	        Log.i("ItcastHttpPost", b.toString());	        
	        outStream.close();
	        conn.disconnect();
	        return b.toString();
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	public static String post(String actionUrl, Map<String, String> params) {
		   StringBuilder builder = new StringBuilder();
		   builder.append(actionUrl);
		   builder.append("?");
		   for (Map.Entry<String, String> entry : params.entrySet()) {//构建表单字段内容
			   builder.append(entry.getKey());
			   builder.append("=");
			   builder.append(entry.getValue());
			   builder.append("&");
		   }
		   String temp = builder.toString();
		   String uri = temp.copyValueOf(temp.toCharArray(), 0, temp.length()-1);
		   Log.i(TAG, uri);
		   HttpGet httpGet = new HttpGet(uri);
		   
		   try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				return EntityUtils.toString(httpResponse.getEntity());
		    }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;  
 }
	
}
