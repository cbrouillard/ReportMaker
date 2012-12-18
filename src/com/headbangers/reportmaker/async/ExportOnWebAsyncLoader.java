package com.headbangers.reportmaker.async;

import android.app.Activity;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.WebServiceClient;

public class ExportOnWebAsyncLoader extends GenericAsyncLoader<Battle, String> {

	private WebServiceClient client;

	public ExportOnWebAsyncLoader(Activity context, WebServiceClient client) {
		super(context);
		this.client = client;
	}

	@Override
	protected String doInBackground(Battle... param) {

		this.client.export(param[0], "test", "test");
		return "test";
	}

}
