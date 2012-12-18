package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

	private Activity from;
	private ObjectMapper jsonMapper;
	private FilesystemService fs = new FilesystemService();

	public WebServiceClient(Activity from) {
		this.jsonMapper = new ObjectMapper();
		this.from = from;
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

		MyHttpClient httpClient = new MyHttpClient(from);

		HttpPost request = new HttpPost(
				URI.create("https://10.0.2.2:8443/reportmaker/web/createReport"));

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("reportData", json));

		try {

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpClient.execute(request);

			Log.d("WebserviceClient", response.toString());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void exportAsync(Battle battle, String string, String string2) {
		ExportOnWebAsyncLoader asyncLoader = new ExportOnWebAsyncLoader(from,
				this);
		asyncLoader.setDialogText(from.getResources().getString(
				R.string.web_exporting));
		asyncLoader.execute(battle);

	}

}
