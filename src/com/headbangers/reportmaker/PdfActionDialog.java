package com.headbangers.reportmaker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class PdfActionDialog extends Dialog {
	private static Context mContext = null;

	public PdfActionDialog(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * Standard Android on create method that gets called when the activity
	 * initialized.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pdf_actions_dialog);

	}

}
