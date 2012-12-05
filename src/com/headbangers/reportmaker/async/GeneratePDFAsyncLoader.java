package com.headbangers.reportmaker.async;

import com.headbangers.reportmaker.service.IPDFService;

import android.app.Activity;

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
	}

}
