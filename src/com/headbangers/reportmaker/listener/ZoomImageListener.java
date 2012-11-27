package com.headbangers.reportmaker.listener;

import java.io.File;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.headbangers.reportmaker.ImageHelper;

public class ZoomImageListener implements OnClickListener {

	private Activity context;
	private File image;
	private String imageName;

	public ZoomImageListener(Activity context, File image, String imageName) {
		this.context = context;
		this.image = image;
		this.imageName = imageName;
	}

	@Override
	public void onClick(View v) {
		ImageHelper.showImageInDialog(image, context, imageName);
	}

}
