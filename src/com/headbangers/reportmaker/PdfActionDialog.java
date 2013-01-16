package com.headbangers.reportmaker;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class PdfActionDialog extends Dialog {

	private Activity mContext = null;

	private String filePath;

	private ImageButton consult;
	private ImageButton share;

	public PdfActionDialog(Activity context, String filePath) {
		super(context, R.style.dialogBackground);
		this.mContext = context;
		this.filePath = filePath;

	}

	/**
	 * Standard Android on create method that gets called when the activity
	 * initialized.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pdf_actions_dialog);
		consult = (ImageButton) findViewById(R.id.action_consult);
		share = (ImageButton) findViewById(R.id.action_share);

		consult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = new File(PdfActionDialog.this.filePath);

				if (file.exists()) {
					Uri path = Uri.fromFile(file);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(path, "application/pdf");
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					try {
						PdfActionDialog.this.mContext.startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(PdfActionDialog.this.mContext,
								R.string.no_application_pdf, Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				File file = new File(PdfActionDialog.this.filePath);

				if (file.exists()) {
					Uri path = Uri.fromFile(file);

					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("application/pdf");
					intent.putExtra(Intent.EXTRA_STREAM, path);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(
							android.content.Intent.EXTRA_SUBJECT,
							mContext.getResources().getString(
									R.string.share_subject));
					intent.putExtra(android.content.Intent.EXTRA_TEXT, mContext
							.getResources().getString(R.string.share_text));

					try {
						PdfActionDialog.this.mContext.startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(PdfActionDialog.this.mContext,
								R.string.no_application_share, Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});

	}

}
