package com.headbangers.reportmaker.service;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.headbangers.reportmaker.tools.ImageHelper;

public class DrawableManager {

	public DrawableManager() {
	}

	public Drawable fetchDrawable(Activity context, String path) {
		Bitmap bitmap = null;
		File imageFile = new File(path);

		if (!imageFile.exists() && imageFile.getPath().endsWith(".thumb")) {
			// Création du thumb à la volée
			String realPhotoPath = path.substring(0, path.indexOf(".thumb"))
					.replace("/thumbs/", "/");
			bitmap = ImageHelper.createThumbnail(realPhotoPath);
		} else {

			bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

		}

		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

		return drawable;

	}

	@SuppressLint("HandlerLeak")
	public void fetchDrawableOnThread(final Activity context,
			final String urlString, final ImageView imageView,
			final int defaultImageId) {

		imageView.setImageResource(defaultImageId);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.obj != null) {
					imageView.setImageDrawable((Drawable) message.obj);
				} else {
					imageView.setImageResource(defaultImageId);
				}
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				Drawable drawable = fetchDrawable(context, urlString);
				Message message = handler.obtainMessage(1, drawable);
				handler.sendMessage(message);
			}
		};
		thread.start();
	}

}
