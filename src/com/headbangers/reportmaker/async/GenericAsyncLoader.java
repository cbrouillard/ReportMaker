package com.headbangers.reportmaker.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class GenericAsyncLoader<P, R> extends AsyncTask<P, Void, R> {

	protected Activity fromContext;

	private ProgressDialog dialog;
	protected String dialogText = null;

	private boolean showDialog = true;

	public GenericAsyncLoader(Activity context) {
		this.fromContext = context;
	}

	public GenericAsyncLoader(Activity context, boolean showDialog) {
		this(context);
		this.showDialog = showDialog;
	}

	@Override
	protected void onPreExecute() {
		if (showDialog) {
			dialog = new ProgressDialog(fromContext);
			dialog.setIndeterminate(true);
			dialog.setMessage(dialogText != null ? dialogText
					: "En cours de chargement");
			dialog.show();
		}
	}

	public GenericAsyncLoader<P, R> setDialogText(String dialogText) {
		this.dialogText = dialogText;
		return this;
	}

	@Override
	protected void onPostExecute(R result) {
		if (showDialog) {
			dialog.dismiss();
		}
	};

}
