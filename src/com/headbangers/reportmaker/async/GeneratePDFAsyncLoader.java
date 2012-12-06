package com.headbangers.reportmaker.async;

import android.app.Activity;

import com.headbangers.reportmaker.PdfActionDialog;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.service.IPDFService;

public class GeneratePDFAsyncLoader extends GenericAsyncLoader<Long, String> {

	private IPDFService pdfService;

	public GeneratePDFAsyncLoader(Activity context, IPDFService pdfService) {
		super(context);
		this.pdfService = pdfService;
	}

	@Override
	protected String doInBackground(Long... params) {
		return pdfService.exportBattle(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// Boite de dialoge "On fait quoi ?"
		PdfActionDialog actions = new PdfActionDialog(this.fromContext, result);
		actions.setTitle(this.fromContext.getResources().getString(
				R.string.pdf_action_title));
		actions.show();

	}

}
