package org.httpproxy.app;
import org.httpproxy.lib.HttpProxyException;
import org.httpproxy.lib.HttpProxyServerMgr;

import android.app.Application;


public class App extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		//HttpProxyServerMgr.getInstance().stopProxyServer();
	}
  
	
}
