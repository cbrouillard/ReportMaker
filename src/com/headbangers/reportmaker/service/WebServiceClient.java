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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.async.ExportOnWebAsyncLoader;
import com.headbangers.reportmaker.pojo.Battle;

public class WebServiceClient {

	private Activity from;
	private ObjectMapper jsonMapper;
	private FilesystemService fs = FilesystemService.getInstance();

	private String host;
	private String createReport;

	public WebServiceClient(Activity from) {
		this.jsonMapper = new ObjectMapper();
		this.from = from;
	}

	public int export(Battle battle, String login, String pass) {
		// this.host = (String) from.getResources().getXml(R.xml.data)
		// .getProperty("host"); // getString(R.string.host);
		// this.createReport = (String) from.getResources().getXml(R.xml.data)
		// .getProperty("createReport");

		this.host = (String) from.getResources().getString(R.string.host);
		this.createReport = (String) from.getResources().getString(
				R.string.createReport);

		Log.d("Export", "Url = " + host + createReport);

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

			// Effacement du fichier zip
			zipFile.delete();

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

		HttpPost request = new HttpPost(URI.create(host + createReport));

		// ArrayList<NameValuePair> nameValuePairs = new
		// ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("reportData", json));
		// nameValuePairs.add(new BasicNameValuePair("user", login));
		// nameValuePairs.add(new BasicNameValuePair("pass", pass));

		Log.d("JSON", "#########");
		Log.d("JSON", json);
		Log.d("JSON", "#########");

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

	public void exportAsync(final Battle battle, final String user,
			final String pass) {

		// si non connecté au wifi, alors demande de confirmation
		ConnectivityManager connManager = (ConnectivityManager) from
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mWifi.isConnected()) {
			new AlertDialog.Builder(from)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.export_battle_web)
					.setMessage(R.string.really_export_on_web)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									WebServiceClient.this.launchExport(battle,
											user, pass);
								}

							}).setNegativeButton(R.string.no, null).show();

		} else {
			launchExport(battle, user, pass);
		}
	}

	private void launchExport(Battle battle, String user, String pass) {
		ExportOnWebAsyncLoader asyncLoader = new ExportOnWebAsyncLoader(from,
				this);
		asyncLoader.setDialogText(from.getResources().getString(
				R.string.web_exporting));
		asyncLoader.execute(battle, user, pass);
	}

}
