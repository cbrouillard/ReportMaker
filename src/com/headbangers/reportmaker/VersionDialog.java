package com.headbangers.reportmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class VersionDialog extends Dialog {

	public static final float VERSION = 2.1f;
	public static final String CURRENT_VERSION_KEY = "current_version";

	private static Context mContext = null;

	public VersionDialog(Context context) {
		super(context, R.style.dialogBackground);
		mContext = context;
		this.setCanceledOnTouchOutside(true);
	}

	/**
	 * Standard Android on create method that gets called when the activity
	 * initialized.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.version);
		//
		TextView tv = (TextView) findViewById(R.id.info_text);
		tv.setText(Html.fromHtml(mContext.getResources().getString(
				R.string.first_launch)));
	}

	public static String readRawTextFile(int id) {
		InputStream inputStream = mContext.getResources().openRawResource(id);
		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;
		StringBuilder text = new StringBuilder();
		try {
			while ((line = buf.readLine()) != null)
				text.append(line);
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}
}
