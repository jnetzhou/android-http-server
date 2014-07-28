package org.httpproxy.lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * http代理服务器管理器
 * @author zhouzhineng
 *
 */
public class HttpProxyServerMgr {

	public final static String TAG = "HttpProxyServerMgr";
	private static HttpProxyServerMgr mInstance;
	private Context mContext;
	private Intent mIntent;
	private HttpProxyAuthentication mAuthentication;
	
	//用于鉴权的用户名/密码
	private String mUserName;
	private String mPassword;
	
	private HttpProxyServerMgr() {
		
	}
	
	public static HttpProxyServerMgr getInstance() {
		if(mInstance == null) {
			mInstance = new HttpProxyServerMgr();
		}
		return mInstance;
	}
	
	/**
	 * 设定一些默认配置值
	 */
	public void initDefaultConfigs(String webroot,int port,boolean isNeedAuthencate) {
		
		HttpProxyConfigs.WEBROOT = webroot;
		HttpProxyConfigs.PORT = port;
		HttpProxyConfigs.isNeedAuthencate = isNeedAuthencate;
	}
	
	/**
	 * 读取配置信息
	 */
	private void readServerConfig(Context context) {
		
		if(context != null) {
			SharedPreferences sp = context.getSharedPreferences(HttpProxyConfigs.PROXY_CONFIG_FILE, 0);
			boolean isFirstStart = sp.getBoolean(HttpProxyConfigs.config_first_start, true);
			//若不是第一次启动，下列值才有使用价值,否则按默认设置
			if(!isFirstStart) {
				HttpProxyConfigs.isAuthencated = sp.getBoolean(HttpProxyConfigs.config_authencated, false);
				HttpProxyConfigs.isNeedAuthencate = sp.getBoolean(HttpProxyConfigs.config_need_authencate, false);
				HttpProxyConfigs.PORT = sp.getInt(HttpProxyConfigs.config_port, -1);
				HttpProxyConfigs.WEBROOT = sp.getString(HttpProxyConfigs.config_webroot, "/");
			}
			
		}
	}
	
	/**
	 * 保存配置信息
	 * @param context
	 */
	private void storeServerConfig(Context context) {
		if(context != null) {
			SharedPreferences sp = context.getSharedPreferences(HttpProxyConfigs.PROXY_CONFIG_FILE, 0);
			sp.edit().putBoolean(HttpProxyConfigs.config_authencated, HttpProxyConfigs.isAuthencated).commit();
			sp.edit().putBoolean(HttpProxyConfigs.config_need_authencate, HttpProxyConfigs.isNeedAuthencate).commit();
			sp.edit().putInt(HttpProxyConfigs.config_port, HttpProxyConfigs.PORT).commit();
			sp.edit().putString(HttpProxyConfigs.config_webroot, HttpProxyConfigs.WEBROOT).commit();
			sp.edit().putBoolean(HttpProxyConfigs.config_first_start, false).commit();
		}
	}
	
	/**
	 * 设置鉴权模块
	 * @param authentication
	 */
	public void setAuthenticateModule(HttpProxyAuthentication authentication) {
		mAuthentication = authentication;
	}
	
	/**
	 * 代理服务器是否运行中
	 * @return
	 */
	public boolean isProxyServerRunning() {
		return HttpProxyConfigs.isRunning;
	}
	
	/**
	 * 启动代理服务器
	 * @param context
	 * @return
	 * @throws HttpProxyException
	 */
	public boolean startProxyServer(Context context) throws HttpProxyException {
		if(context == null) {
			return false;
		}
		//初始化
		readServerConfig(context);
		//需要鉴权且尚未授权
		if(HttpProxyConfigs.isNeedAuthencate && !HttpProxyConfigs.isAuthencated) {
			
			if(mAuthentication != null) {
				HttpProxyConfigs.isAuthencated = mAuthentication.authenticate(mUserName, mPassword);
				//HttpProxyConfigs.isAuthencated = mAuthentication.authenticate(...param);
			} else {
				throw new HttpProxyException("You have to set Authentication Module by function setAuthenticateModule()!");
			}
		}
		//鉴权失败
		if(HttpProxyConfigs.isNeedAuthencate && !HttpProxyConfigs.isAuthencated) {
			return false;
		}
		
		mIntent = new Intent(context,HttpProxyService.class);
		mContext = context;
		mContext.startService(mIntent);
		return true;
	}
	
	/**
	 * 关闭代理服务器
	 */
	public void stopProxyServer() {
		if(mContext == null) {
			return;
		}
		storeServerConfig(mContext);
		mContext.stopService(mIntent);
	}
}
