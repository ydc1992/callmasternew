package cn.opda.net.upload;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


import android.util.Log;
/**
 * http��������
 */
public class HttpRequester {
	/**
	 * �ύ���ݵ�������
	 * @param actionUrl �ϴ�·��
	 * @param params ������� keyΪ������,valueΪ����ֵ
	 */

	public static String httpPost(String actionUrl, Map<String, String> params) {
		try {	        
	        String BOUNDARY = "---------7d4a6d158c9"; //���ݷָ���
	        String MULTIPART_FORM_DATA = "multipart/form-data";
	        
	        URL url = new URL(actionUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(6* 1000);
	        conn.setDoInput(true);//��������
	        conn.setDoOutput(true);//�������
	        conn.setUseCaches(false);//��ʹ��Cache
	        conn.setRequestMethod("POST");	        
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<String, String> entry : params.entrySet()) {//�������ֶ�����
	            sb.append("--");
	            sb.append(BOUNDARY);
	            sb.append("\r\n");
	            sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
	            sb.append(entry.getValue());
	            sb.append("\r\n");
	        }
	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	        outStream.write(sb.toString().getBytes());//���ͱ��ֶ�����
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
}
