package com.headbangers.reportmaker;

import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;

public class ImageHelper {

	public static void showImageInDialog(File imageFile, Context context,
			String imageTitle) {

		if (!imageFile.exists()) {
			return;
		}

		final Dialog dialog = new Dialog(context);

		dialog.setContentView(R.layout.zoom_dialog);
		dialog.setTitle(imageTitle);

		ImageView zoom = (ImageView) dialog.findViewById(R.id.image);
		try {
			setPic(imageFile.getAbsolutePath(), zoom);
		} catch (IOException e) {
			return;
		}

		zoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);

		dialog.show();
	}

	public static void setPic(String photoPath, ImageView view)
			throws IOException {

		ExifInterface exif = new ExifInterface(photoPath);
		int exifOrientation = exif
				.getAttributeInt(ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);

		int rotate = 0;

		switch (exifOrientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;

		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;

		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		}

		// Get the dimensions of the View
		int targetW = 512;
		int targetH = 1; // view.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

		if (rotate != 0) {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			// Setting pre rotate
			Matrix mtx = new Matrix();
			mtx.preRotate(rotate);

			// Rotating Bitmap & convert to ARGB_8888, required by tess
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		}

		view.setImageBitmap(bitmap);
	}

}
