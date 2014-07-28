package org.httpproxy.lib;

/**
 * http 代理服务器鉴权类,鉴权功能根据具体需求实现
 * @author zhouzhineng
 *
 */
public interface HttpProxyAuthentication {
	
	public final static String TAG = "HttpProxyAuthentication";
	
	public  boolean authenticate(String username,String passwd);
	
	public  boolean authenticate(String...param);

}
