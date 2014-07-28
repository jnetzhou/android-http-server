package org.httpproxy.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * http代理客户端，暂支持简单的get,post方法
 * @author zhouzhineng
 *
 */
public class HttpProxyClient {

	public final static String TAG = "HttpProxyClient";
	private HttpClient mHttpClient;
	private OnDataResponseCallback mOnDataResponseCallback;
	private ExecutorService mThreadPoolExecutor;
	
	/**
	 * 返回数据处理接口
	 * @author zhouzhineng
	 *
	 */
	public interface OnDataResponseCallback{
		public void handleGet(HttpEntity entity);
		public void handlePost(HttpEntity entity);
	}
	
	public HttpProxyClient() {
		mHttpClient = new DefaultHttpClient();
		mThreadPoolExecutor = Executors.newCachedThreadPool();
	}
	
	/**
	 * 设置数据返回的处理接口
	 * @param onDataResponseCallback
	 */
	public void setDataResponseCallback(OnDataResponseCallback onDataResponseCallback) {
		mOnDataResponseCallback = onDataResponseCallback;
	}
	
	
	public void doGet(final String url) {
		mThreadPoolExecutor.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				get(url);
			}
			
		});
		
	}
	
	private void get(String url) {
		HttpUriRequest request = new HttpGet(url);
		try {
			HttpResponse response = mHttpClient.execute(request);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				//InputStream is = entity.getContent();
				if(mOnDataResponseCallback != null) {
					mOnDataResponseCallback.handleGet(entity);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doPost(final String url,final Map<String,String> params) {
		mThreadPoolExecutor.submit(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				post(url,params);
			}
			
		});
	}
	
	private void post(String url,Map<String,String> params) {
		
		List<BasicNameValuePair> valuePairList = null;
		HttpPost post = new HttpPost(url);
		if(params != null) {
			Set<String> keySet = params.keySet();
			valuePairList = new ArrayList<BasicNameValuePair>();
			for (String key : keySet) {
				BasicNameValuePair value = new BasicNameValuePair(key,params.get(key));
			    valuePairList.add(value);
			}
			
			try {
				HttpEntity entity = new UrlEncodedFormEntity(valuePairList);
				post.setEntity(entity);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			HttpResponse response = mHttpClient.execute(post);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
				HttpEntity entity = response.getEntity();
				//InputStream is = entity.getContent();
				if(mOnDataResponseCallback != null) {
					mOnDataResponseCallback.handlePost(entity);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
