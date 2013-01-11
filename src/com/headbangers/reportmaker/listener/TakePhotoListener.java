package com.headbangers.reportmaker.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.tools.ImageHelper;

public class TakePhotoListener implements OnClickListener {

	private Activity context;

	private Battle battle;
	private String photoName;
	private int resultCode;

	private FilesystemService fs = new FilesystemService();

	public TakePhotoListener(Activity context, Battle battle, String photoName,
			int returnedResultCode) {
		this.battle = battle;
		this.photoName = photoName;
		this.resultCode = returnedResultCode;

		this.context = context;
	}

	@Override
	public void onClick(View v) {

		ImageHelper.takePhoto(this.context, fs, battle, photoName, resultCode);

	}

}
