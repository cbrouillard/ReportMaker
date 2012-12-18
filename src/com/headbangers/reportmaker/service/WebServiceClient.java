package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.util.Log;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.async.ExportOnWebAsyncLoader;
import com.headbangers.reportmaker.pojo.Battle;

public class WebServiceClient {

	private ObjectMapper jsonMapper;
	private FilesystemService fs = new FilesystemService();

	public WebServiceClient() {
		this.jsonMapper = new ObjectMapper();
	}

	public void export(Battle battle, String login, String pass) {

		try {

			// Ecriture du JSON contenant le rapport
			String json = jsonMapper.writeValueAsString(battle);

			// Création d'un zip contenant l'intégralité des photos
			File rootBattle = fs.getRootBattle(battle);
			Compress compress = new Compress(rootBattle.list(),
					rootBattle.getAbsolutePath() + File.separator + "data.zip");
			compress.zip();
			File zipFile = compress.getZipFile();

			// Appel au webservice
			callCreateWebservice(json, zipFile, login, pass);

		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void callCreateWebservice(String json, File zipFile, String login,
			String pass) {

		// HostnameVerifier hostnameVerifier =
		// org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		//
		// DefaultHttpClient client = new DefaultHttpClient();
		//
		// SchemeRegistry registry = new SchemeRegistry();
		// SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		// socketFactory
		// .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		// registry.register(new Scheme("https", socketFactory, 443));
		// SingleClientConnManager mgr = new SingleClientConnManager(
		// client.getParams(), registry);
		// DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
		// client.getParams());
		//
		// // Set verifier
		// HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost request = new HttpPost(
				URI.create("https://10.0.2.2:8443/reportmaker/web/createReport"));

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("reportData", json));

		try {

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(request);

			Log.d("WebserviceClient", response.toString());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void exportAsync(Activity fromContext, Battle battle, String string,
			String string2) {
		ExportOnWebAsyncLoader asyncLoader = new ExportOnWebAsyncLoader(
				fromContext, this);
		asyncLoader.setDialogText(fromContext.getResources().getString(
				R.string.web_exporting));
		asyncLoader.execute(battle);

	}

}
