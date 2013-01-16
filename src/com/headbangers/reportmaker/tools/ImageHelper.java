package com.headbangers.reportmaker.tools;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.DrawableManager;
import com.headbangers.reportmaker.service.FilesystemService;

public class ImageHelper {

	public static DrawableManager drawableManager = new DrawableManager();

	public static void showImageInDialog(final File imageFile,
			final Activity context, String imageTitle) {

		if (!imageFile.exists()) {
			return;
		}

		final Dialog dialog = new Dialog(context, R.style.dialogBackground);

		dialog.setContentView(R.layout.zoom_dialog);
		dialog.setTitle(imageTitle);

		ImageView zoom = (ImageView) dialog.findViewById(R.id.image);
		ImageButton share = (ImageButton) dialog
				.findViewById(R.id.action_share);
		setPic(imageFile.getAbsolutePath(), zoom);

		zoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri path = Uri.fromFile(imageFile);

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("application/image");
				intent.putExtra(Intent.EXTRA_STREAM, path);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context
						.getResources().getString(R.string.image_share_subject));
				intent.putExtra(android.content.Intent.EXTRA_TEXT, context
						.getResources().getString(R.string.image_share_text));

				try {
					context.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(context, R.string.no_application_share,
							Toast.LENGTH_LONG).show();
				}

			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);

		dialog.show();
	}

	public static void setPicAsync(Activity context, String photoPath,
			ImageView view) {
		drawableManager.fetchDrawableOnThread(context, photoPath, view,
				R.drawable.damier);
	}

	public static Bitmap rotateAndResize(String photoPath, int defaultW)
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
		int targetW = defaultW;
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

		return bitmap;
	}

	public static void setPic(String photoPath, ImageView view) {

		Bitmap bitmap;
		try {
			bitmap = rotateAndResize(photoPath,
					view.getWidth() != 0 ? view.getWidth() : 512);
			view.setImageBitmap(bitmap);
		} catch (IOException e) {
		}
	}

	public static void takePhoto(Activity context, FilesystemService fs,
			Battle battle, String photoName, int returnResultCode) {

		File imageFile = new File(fs.getRootBattle(battle), photoName);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));

		context.startActivityForResult(takePictureIntent, returnResultCode);
	}

	public static Bitmap photoAsPDFBitmap(File rootBattle, String photoName) {

		File completeFile = new File(rootBattle, photoName);

		try {
			return rotateAndResize(completeFile.getAbsolutePath(), 300);
		} catch (IOException e) {
			return null;
		}
	}

	public static Bitmap photoAsPDFBitmap(String photoPath) {

		File completeFile = new File(photoPath);

		try {
			return rotateAndResize(completeFile.getAbsolutePath(), 300);
		} catch (IOException e) {
			return null;
		}
	}

}
