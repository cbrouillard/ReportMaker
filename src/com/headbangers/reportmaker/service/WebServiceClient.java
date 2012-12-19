package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;

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

	public int export(Battle battle, String login, String pass) {

		try {

			// Ecriture du JSON contenant le rapport
			String json = jsonMapper.writeValueAsString(battle);

			// Création d'un zip contenant l'intégralité des photos
			File rootBattle = fs.getRootBattle(battle);
			Compress compress = new Compress(rootBattle, rootBattle.list(),
					rootBattle.getAbsolutePath() + File.separator + "data.zip");
			compress.zip();
			File zipFile = compress.getZipFile();

			// Appel au webservice
			int httpCode = callCreateWebservice(json, zipFile, login, pass);

			return httpCode;

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

		return 500;

	}

	private int callCreateWebservice(String json, File zipFile, String login,
			String pass) {

		MyHttpClient httpClient = new MyHttpClient(from);

		HttpPost request = new HttpPost(
				URI.create("https://10.0.2.2:8443/reportmaker/web/createReport"));

		// ArrayList<NameValuePair> nameValuePairs = new
		// ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("reportData", json));
		// nameValuePairs.add(new BasicNameValuePair("user", login));
		// nameValuePairs.add(new BasicNameValuePair("pass", pass));

		try {
			MultipartEntity entity = new MultipartEntity();

			entity.addPart("reportData",
					new StringBody(json, Charset.forName("UTF-8")));
			entity.addPart("user",
					new StringBody(login, Charset.forName("UTF-8")));
			entity.addPart("pass",
					new StringBody(pass, Charset.forName("UTF-8")));
			entity.addPart("photos", new FileBody(zipFile));

			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);

			return response.getStatusLine().getStatusCode();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 500;

	}

	public void exportAsync(Battle battle, String user, String pass) {
		ExportOnWebAsyncLoader asyncLoader = new ExportOnWebAsyncLoader(from,
				this);
		asyncLoader.setDialogText(from.getResources().getString(
				R.string.web_exporting));
		asyncLoader.execute(battle, user, pass);

	}

}
