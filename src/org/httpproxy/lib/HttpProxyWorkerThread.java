package org.httpproxy.lib;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

public class HttpProxyWorkerThread extends Thread {

	public final static String TAG = "HttpProxyWorkerThread";
	private final HttpService mHttpService;
	private final HttpServerConnection mConnection;

	public HttpProxyWorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
		super();
		mHttpService = httpservice;
		mConnection = conn;
	}

	@Override
	public void run() {
		HttpContext context = new BasicHttpContext();
		try {
			while (!Thread.interrupted() && mConnection.isOpen()) {
				mHttpService.handleRequest(mConnection, context);
			}
		} catch (ConnectionClosedException ex) {
			System.err.println("Client closed connection");
		} catch (IOException ex) {
			System.err.println("I/O error: " + ex.getMessage());
		} catch (HttpException ex) {
			System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
		} finally {
			try {
				mConnection.shutdown();
			} catch (IOException ignore) {
			}
		}
	}

}
