package org.httpproxy.lib;

/**
 * 自定义http proxy exception
 * @author zhouzhineng
 *
 */
public class HttpProxyException extends Exception{

	private static final long serialVersionUID = -306131397488587989L;
	public static final String TAG = "HttpProxyException";
	
	public HttpProxyException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		
	}

}
