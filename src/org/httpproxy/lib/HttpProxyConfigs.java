package org.httpproxy.lib;

/**
 * http 代理服务器常量类
 * @author zhouzhineng
 */
public class HttpProxyConfigs {

	public final static String TAG = "HttpProxyConstants";
	
	public final static String LIB_TAG = "HttpProxyLib";
	
	//debug标志
	public static boolean isDebug = false;
	
	//代理服务器默认根目录
	public static String WEBROOT = "/";
	
	//代理服务器默认通信端口
	public static int PORT = 7766;
	
	//代理服务器是否运作中
	public static boolean isRunning = false;
	
	//是否需要鉴权
	public static boolean isNeedAuthencate = false;
	
	//是否已经授权
	public static boolean isAuthencated = false;
	
	public final static String SUFFIX_ZIP = "..zip";
	
	public final static String SUFFIX_DEL = "..del";
	
	//鉴权服务器地址,根据实际填写
	public final static String AUTHEN_SERVER_ADDR = "";
	
	//代理服务器配置文件
	public final static String PROXY_CONFIG_FILE = "http_proxy_server_config_file";
	
	//是否第一次启动代理服务器
	public final static String config_first_start = "first_start";
	
	//通信端口
	public final static String config_port = "port";
	
	//web根目录
	public final static String config_webroot = "webroot";
	
	//代理服务器配置中的鉴权key
	public final static String config_authencated = "authencated";
	
	//代理服务器配置中是否需要鉴权key
	public final static String config_need_authencate = "need_authencate";
	
}
