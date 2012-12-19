package com.headbangers.reportmaker.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.headbangers.reportmaker.AuthActivity;
import com.headbangers.reportmaker.R;
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

		Log.d("Export", "Code = " + result);

		switch (result) {
		case 403:
			// mauvais user ou mauvaise auth. On propose de se réauthentifier
			new AlertDialog.Builder(fromContext)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.bad_auth_title)
					.setMessage(R.string.bad_auth)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent action = new Intent(fromContext,
											AuthActivity.class);
									action.putExtra(AuthActivity.FORCEMODE_ARG,
											true);
									fromContext.startActivityForResult(action,
											AuthActivity.EXPORT_AFTER_AUTH);
								}

							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}

							}).show();
			break;

		case 500:
			// erreur système. On propose de réessayer plus tard
			Toast.makeText(fromContext, R.string.webexport_fail,
					Toast.LENGTH_LONG).show();
			break;
		case 200:
			// OK :)
			Toast.makeText(fromContext, R.string.webexport_ok,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

}
