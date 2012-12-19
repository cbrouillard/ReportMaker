package com.headbangers.reportmaker.async;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.WebServiceClient;

public class ExportOnWebAsyncLoader extends GenericAsyncLoader<Object, Integer> {

	private WebServiceClient client;

	public ExportOnWebAsyncLoader(Activity context, WebServiceClient client) {
		super(context);
		this.client = client;
	}

	@Override
	protected Integer doInBackground(Object... param) {

		Battle battle = (Battle) param[0];
		String user = (String) param[1];
		String pass = (String) param[2];

		int code = this.client.export(battle, user, pass);

		return code;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		// TODO renseigner l'utilisateur sur la réussite ou non
		Log.d("Export", "Code = " + result);
		Toast.makeText(fromContext, "Code = " + result, Toast.LENGTH_LONG)
				.show();
	}

}
