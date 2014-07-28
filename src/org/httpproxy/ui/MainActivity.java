package org.httpproxy.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.httpproxy.R;
import org.httpproxy.lib.HttpProxyClient;
import org.httpproxy.lib.HttpProxyClient.OnDataResponseCallback;
import org.httpproxy.lib.HttpProxyConfigs;
import org.httpproxy.lib.HttpProxyException;
import org.httpproxy.lib.HttpProxyServerMgr;
import org.httpproxy.lib.HttpProxyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnCheckedChangeListener {

	private ToggleButton toggleBtn;
	private TextView urlText;
    private WebView webView;
	private Intent intent;
	private Button showBtn;
	private String content;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initViews();
		initFiles();

		//intent = new Intent(this,HttpProxyService.class);
		
	}

	private void initViews() {
		toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
		toggleBtn.setOnCheckedChangeListener(this);
		urlText = (TextView) findViewById(R.id.urlText);
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.getSettings().setSupportZoom(true);  
		webView.getSettings().setDomStorageEnabled(true);  
		webView.getSettings().setPluginsEnabled(true);  
		webView.requestFocus();  
		webView.getSettings().setUseWideViewPort(true);  
		webView.getSettings().setLoadWithOverviewMode(true);  
		webView.getSettings().setSupportZoom(true);  
		webView.getSettings().setBuiltInZoomControls(true); 
		webView.setWebChromeClient(new WebChromeClient(){
			
		});
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				urlText.setText(url);
				return true;
			}
			
		});
		showBtn = (Button) findViewById(R.id.showBtn);
		showBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(HttpProxyConfigs.isRunning) {
					String ip = getLocalIpAddress();
					String url = "http://" + ip + ":" + HttpProxyConfigs.PORT + "/" + "sdcard/.wfs/index.html";
					urlText.setText(url);
					webView.loadUrl(url);
					//webView.loadUrl("http://bsl.foreveross.com:8080/bsl-web/mam/attachment/download/T1QthTByhT1RCvBVdK");
					/*final String url = "http://" + ip + ":" + HttpProxyConfigs.PORT + "/";
					//final String loadUrl = url + "sdcard/.wfs/html/ichangan/login/index.html";
					final String loadUrl = url + "sdcard/.wfs/index.html";
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							HttpProxyClient client = new HttpProxyClient();
							client.setDataResponseCallback(new OnDataResponseCallback() {
								
								@Override
								public void handlePost(HttpEntity entity) {
									// TODO Auto-generated method stub
									try {
										content = EntityUtils.toString(entity, "UTF-8");
										Log.i("XX",content);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
								@Override
								public void handleGet(HttpEntity entity) {
									// TODO Auto-generated method stub
									try {
										content = EntityUtils.toString(entity, "UTF-8");
										Log.i("XX",content);
										//webView.loadData(content,  "text/html; charset=UTF-8", null);
										String baseUrl =  url + "sdcard/.wfs/html/ichangan/";
										webView.loadDataWithBaseURL(null,content, "text/html",  "utf-8", null);
										//webView.loadData(URLEncoder.encode(content, "utf-8"), "text/html",  "utf-8");
										
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							client.doGet(loadUrl);
						}
						
					}).start();
					/*webView.setWebViewClient(new WebViewClient() {

						@Override
						public void onReceivedError(WebView view, int errorCode,
								String description, String failingUrl) {
							// TODO Auto-generated method stub
							super.onReceivedError(view, errorCode, description, failingUrl);
						}
						
					});
					webView.loadUrl(loadUrl);*/
				} else {
					Toast.makeText(MainActivity.this, "服务器尚未启动完成...", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

	private void initFiles() {
		new CopyUtil(this).assetsCopy();
		new CopyUtil(this).assetsCopy2();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			String ip = getLocalIpAddress();
			if (ip == null) {
				Toast.makeText(this, R.string.msg_net_off, Toast.LENGTH_SHORT)
						.show();
				urlText.setText("");
			} else {
				//startService(intent);
				try {
					HttpProxyServerMgr.getInstance().startProxyServer(this);
				} catch (HttpProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = "http://" + ip + ":" + HttpProxyConfigs.PORT + "/";
				urlText.setText(url);
				
				
			}
		} else {
			//stopService(intent);
			HttpProxyServerMgr.getInstance().stopProxyServer();
			urlText.setText("");
		}
	}

	/** 获取当前IP地址 */
	private String getLocalIpAddress() {
		try {
			// 遍历网络接口
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				// 遍历IP地址
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					// 非回传地址时返回
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onBackPressed() {
		if (intent != null) {
			stopService(intent);
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}