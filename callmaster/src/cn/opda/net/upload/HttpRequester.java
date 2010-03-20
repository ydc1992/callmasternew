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
 * http请求发送器
 */
public class HttpRequester {
	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
	 *   <FORM METHOD=POST ACTION="http://192.168.0.200:8080/ssi/fileload/test.do" enctype="multipart/form-data">
			<INPUT TYPE="text" NAME="name">
			<INPUT TYPE="text" NAME="id">
			<input type="file" name="imagefile"/>
		    <input type="file" name="zip"/>
		 </FORM>
	 * @param actionUrl 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.opda.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params 请求参数 key为参数名,value为参数值
	 * @param file 上传文件
	 */
	public static String post(String actionUrl, Map<String, String> params, FormFile[] files) {
	    try {	        
	        String BOUNDARY = "---------7d4a6d158c9"; //数据分隔线
	        String MULTIPART_FORM_DATA = "multipart/form-data";
	        
	        URL url = new URL(actionUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(6* 1000);
	        conn.setDoInput(true);//允许输入
	        conn.setDoOutput(true);//允许输出
	        conn.setUseCaches(false);//不使用Cache
	        conn.setRequestMethod("POST");	        
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<String, String> entry : params.entrySet()) {//构建表单字段内容
	            sb.append("--");
	            sb.append(BOUNDARY);
	            sb.append("\r\n");
	            sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
	            sb.append(entry.getValue());
	            sb.append("\r\n");
	        }
	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	        outStream.write(sb.toString().getBytes());//发送表单字段数据
	        for(FormFile file : files){//发送文件数据
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
	
	/**
	 * 提交数据到服务器
	 * @param actionUrl 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.opda.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params 请求参数 key为参数名,value为参数值
	 * @param file 上传文件
	 */
	public static String post(String actionUrl, Map<String, String> params, FormFile file) {
	   return post(actionUrl, params, new FormFile[]{file});
	}
	
	/**
	 * 提交数据到服务器
	 * @param actionUrl 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.opda.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params 请求参数 key为参数名,value为参数值
	 */
	public static String post(String actionUrl, Map<String, String> params) {
		   HttpPost httpPost = new HttpPost(actionUrl);
		   List<NameValuePair> list = new ArrayList<NameValuePair>();
		   for (Map.Entry<String, String> entry : params.entrySet()) {//构建表单字段内容
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
