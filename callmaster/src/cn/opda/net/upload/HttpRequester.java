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
	 * �ύ���ݵ�������
	 * @param actionUrl �ϴ�·��
	 * @param params ������� keyΪ������,valueΪ����ֵ
	 */

	public static String get(String actionUrl, Map<String, String> params) {
	    try {	        
	    	StringBuilder builder = new StringBuilder();
			   builder.append(actionUrl);
			   builder.append("?");
			   for (Map.Entry<String, String> entry : params.entrySet()) {//�������ֶ�����
				   builder.append(entry.getKey());
				   builder.append("=");
				   builder.append(entry.getValue());
				   builder.append("&");
			   }
			   String temp = builder.toString();
			   String uri = temp.copyValueOf(temp.toCharArray(), 0, temp.length()-1);
	        String BOUNDARY = "---------7d4a6d158c9"; //���ݷָ���
	        String MULTIPART_FORM_DATA = "multipart/form-data";
	        
	        URL url = new URL(uri);
	        Log.i(TAG, url.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(6* 1000);
	        conn.setDoInput(true);//��������
	        conn.setDoOutput(true);//�������
	        conn.setUseCaches(false);//��ʹ��Cache
	        conn.setRequestMethod("GET");	        
	        conn.setRequestProperty("Connection", "Close");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
	        
	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();//���ݽ�����־	       
	        outStream.write(end_data);
	        outStream.flush();
	        int cah = conn.getResponseCode();
	        if (cah != 200) throw new RuntimeException("����urlʧ��");
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
		   for (Map.Entry<String, String> entry : params.entrySet()) {//�������ֶ�����
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
