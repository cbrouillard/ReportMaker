package com.headbangers.reportmaker.listener;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;

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

		File imageFile = new File(fs.getRootBattle(battle), this.photoName);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));

		this.context.startActivityForResult(takePictureIntent, resultCode);

	}

}
