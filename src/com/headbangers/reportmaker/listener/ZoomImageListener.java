package com.headbangers.reportmaker.listener;

import java.io.File;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.headbangers.reportmaker.tools.ImageHelper;

public class ZoomImageListener implements OnClickListener {

	private Activity context;
	private File image;
	private String imageName;
	private ImageView from;

	public ZoomImageListener(Activity context, File image, String imageName,
			ImageView from) {
		this.context = context;
		this.image = image;
		this.imageName = imageName;
		this.from = from;
	}

	@Override
	public void onClick(View v) {
		ImageHelper.showImageInDialog(image, context, imageName, from);
	}

}
