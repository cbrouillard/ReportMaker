package com.headbangers.reportmaker.listener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.tools.ImageHelper;

public class TakePhotoListener implements OnClickListener {

	private Activity fromActivity;
	private Fragment fromFragment;

	private Battle battle;
	private String photoName;
	private int resultCode;

	private FilesystemService fs = new FilesystemService();

	protected TakePhotoListener(Battle battle, String photoName,
			int returnedResultCode) {
		this.battle = battle;
		this.photoName = photoName;
		this.resultCode = returnedResultCode;
	}

	public TakePhotoListener(Activity context, Battle battle, String photoName,
			int returnedResultCode) {
		this(battle, photoName, returnedResultCode);

		this.fromActivity = context;
	}

	public TakePhotoListener(Fragment context, Battle battle, String photoName,
			int returnedResultCode) {
		this(battle, photoName, returnedResultCode);

		this.fromFragment = context;
	}

	@Override
	public void onClick(View v) {

		if (fromActivity != null) {

			ImageHelper.takePhoto(this.fromActivity, fs, battle, photoName,
					resultCode);
		} else {
			ImageHelper.takePhoto(this.fromFragment, fs, battle, photoName,
					resultCode);
		}

	}

}
