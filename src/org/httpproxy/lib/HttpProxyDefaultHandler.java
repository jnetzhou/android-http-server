package org.httpproxy.lib;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class HttpProxyDefaultHandler implements HttpRequestHandler {

	public final static String TAG = "HttpProxyDefaultHandler";
	
	private String mWebRoot;

	public HttpProxyDefaultHandler(final String webRoot) {
		mWebRoot = webRoot;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {
		String target = request.getRequestLine().getUri();
		target = target.substring(0,
				target.length() - HttpProxyConfigs.SUFFIX_DEL.length());
		final File file = new File(mWebRoot, target);
		deleteFile(file);
		// 回复客户端处理消息
		response.setStatusCode(HttpStatus.SC_OK);
		StringEntity entity = new StringEntity(file.exists() ? "删除失败" : "删除成功",
				"UTF-8");
		response.setEntity(entity);
	}

	/** 递归删除File */
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null != files) {
				for (File f : files) {
					deleteFile(f);
				}
			}
			if (!HttpProxyFileHandler.hasWfsDir(file)) {
				file.delete();
			}
		} else {
			if (!HttpProxyFileHandler.hasWfsDir(file)) {
				file.delete();
			}
		}
	}

}
