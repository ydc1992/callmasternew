package cn.opda.net.upload;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
/**
 * http��������
 */
public class HttpRequester {
	/**
	 * ֱ��ͨ��HTTPЭ���ύ���ݵ�������,ʵ����������ύ����:
	 *   <FORM METHOD=POST ACTION="http://192.168.0.200:8080/ssi/fileload/test.do" enctype="multipart/form-data">
			<INPUT TYPE="text" NAME="name">
			<INPUT TYPE="text" NAME="id">
			<input type="file" name="imagefile"/>
		    <input type="file" name="zip"/>
		 </FORM>
	 * @param actionUrl �ϴ�·��(ע������ʹ��localhost��127.0.0.1������·�����ԣ���Ϊ����ָ���ֻ�ģ�����������ʹ��http://www.opda.cn��http://192.168.1.10:8080������·������)
	 * @param params ������� keyΪ������,valueΪ����ֵ
	 * @param file �ϴ��ļ�
	 */
	public static String post(String actionUrl, Map<String, String> params, FormFile[] files) {
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
	        for(FormFile file : files){//�����ļ�����
	        	StringBuilder split = new StringBuilder();
	 	        split.append("--");
	 	        split.append(BOUNDARY);
	 	        split.append("\r\n");
	 	        split.append("Content-Disposition: form-data;name=\""+ file.getFormname()+"\";filename=\""+ file.getFilname() + "\"\r\n");
	 	        split.append("Content-Type: "+ file.getContentType()+"\r\n\r\n");
	 	        outStream.write(split.toString().getBytes());
	 	        outStream.write(file.getData(), 0, file.getData().length);
	 	        outStream.write("\r\n".getBytes());
	        }
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
	
	/**
	 * �ύ���ݵ�������
	 * @param actionUrl �ϴ�·��(ע������ʹ��localhost��127.0.0.1������·�����ԣ���Ϊ����ָ���ֻ�ģ�����������ʹ��http://www.opda.cn��http://192.168.1.10:8080������·������)
	 * @param params ������� keyΪ������,valueΪ����ֵ
	 * @param file �ϴ��ļ�
	 */
	public static String post(String actionUrl, Map<String, String> params, FormFile file) {
	   return post(actionUrl, params, new FormFile[]{file});
	}
	
	/**
	 * �ύ���ݵ�������
	 * @param actionUrl �ϴ�·��(ע������ʹ��localhost��127.0.0.1������·�����ԣ���Ϊ����ָ���ֻ�ģ�����������ʹ��http://www.opda.cn��http://192.168.1.10:8080������·������)
	 * @param params ������� keyΪ������,valueΪ����ֵ
	 */
	public static String post(String actionUrl, Map<String, String> params) {
		   HttpPost httpPost = new HttpPost(actionUrl);
		   List<NameValuePair> list = new ArrayList<NameValuePair>();
		   for (Map.Entry<String, String> entry : params.entrySet()) {//�������ֶ�����
			   list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		   }
		   try {
			httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				return EntityUtils.toString(httpResponse.getEntity());
		    }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;  
    }
}
