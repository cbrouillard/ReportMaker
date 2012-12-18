package com.headbangers.reportmaker.service;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;

import com.headbangers.reportmaker.R;

public class MyHttpClient extends DefaultHttpClient {

	final Context context;

	public MyHttpClient(Context context) {
		this.context = context;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		SSLSocketFactory socketFactory = newSslSocketFactory();

		// Remove en PROD !
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

		registry.register(new Scheme("https", socketFactory, 443));
		return new SingleClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(
					R.raw.mystore);
			try {
				trusted.load(in, "ez24get".toCharArray());
			} finally {
				in.close();
			}
			return new SSLSocketFactory(trusted);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}