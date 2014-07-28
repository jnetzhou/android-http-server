package org.httpproxy.lib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HttpProxyService extends Service {

	public final static String TAG = "HttpProxyService";
	private HttpProxyServer mWebServer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//mWebServer = new HttpProxyServer(HttpProxyConfigs.PORT, HttpProxyConfigs.WEBROOT);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mWebServer = new HttpProxyServer(HttpProxyConfigs.PORT, HttpProxyConfigs.WEBROOT);
		mWebServer.setDaemon(true);
		mWebServer.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mWebServer.close();
		super.onDestroy();
	}

}
