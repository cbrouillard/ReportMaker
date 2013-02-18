package com.headbangers.reportmaker.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.ZoomImageListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.DrawableManager;
import com.headbangers.reportmaker.service.FilesystemService;

public class ImageHelper {

	public static DrawableManager drawableManager = new DrawableManager();

	private static final String THUMB_EXTENSION = ".thumb";
	private static FilesystemService fs = FilesystemService.getInstance();

	public static void showImageInDialog(final File imageFile,
			final Activity context, String imageTitle,
			final ImageView fromViewComponent) {

		if (!imageFile.exists()) {
			return;
		}

		final Dialog dialog = new Dialog(context, R.style.dialogBackground);

		dialog.setContentView(R.layout.zoom_dialog);
		dialog.setTitle(imageTitle);
		dialog.setCanceledOnTouchOutside(true);

		ImageView zoom = (ImageView) dialog.findViewById(R.id.image);
		ImageButton share = (ImageButton) dialog
				.findViewById(R.id.action_share);
		ImageButton delete = (ImageButton) dialog
				.findViewById(R.id.action_delete);
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

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Effacer le thumb
				deleteThumbIfExists(computePhotoPathToGetThumbnailPath(imageFile
						.getAbsolutePath()));

				// Effacer l'image
				imageFile.delete();

				// Fermer la boite
				dialog.dismiss();

				// Rafraichir l'image
				fromViewComponent.setImageResource(R.drawable.damier);
				fromViewComponent.setVisibility(View.GONE);
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

		deleteThumbIfExists(getThumbnailPath(battle, imageFile));

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));

		context.startActivityForResult(takePictureIntent, returnResultCode);
	}

	public static void takePhoto(Fragment context, FilesystemService fs,
			Battle battle, String photoName, int resultCode) {
		File imageFile = new File(fs.getRootBattle(battle), photoName);

		deleteThumbIfExists(imageFile.getAbsolutePath() + THUMB_EXTENSION);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));

		context.startActivityForResult(takePictureIntent, resultCode);

	}

	public static Bitmap photoAtAnySize(String photoPath, int size) {
		File completeFile = new File(photoPath);

		try {
			return rotateAndResize(completeFile.getAbsolutePath(), size);
		} catch (IOException e) {
			return null;
		}
	}

	public static Bitmap photoAtAnySize(File rootBattle, String photoName,
			int size) {
		File completeFile = new File(rootBattle, photoName);
		try {
			Log.d("ImageHelper",
					"Resizing photo : " + completeFile.getAbsolutePath());
			return rotateAndResize(completeFile.getAbsolutePath(), size);
		} catch (IOException e) {
			return null;
		}
	}

	private static void deleteThumbIfExists(String thumbPath) {
		File thumb = new File(thumbPath);
		if (thumb.exists()) {
			thumb.delete();
		}
	}

	private static String getThumbnailPath(String imagePath) {
		return imagePath + THUMB_EXTENSION;
	}

	public static String getThumbnailPath(Battle battle, File image) {
		File root = fs.getRootBattle(battle);

		return getThumbnailPath(root.getAbsolutePath() + "/thumbs/"
				+ image.getName());
	}

	public static Bitmap createThumbnail(String file) {
		try {
			File image1 = new File(file);

			if (image1.exists() && !image1.isDirectory()) {
				Bitmap thumb;
				thumb = ImageHelper.rotateAndResize(image1.getAbsolutePath(),
						512);
				FileOutputStream writer = new FileOutputStream(
						computePhotoPathToGetThumbnailPath(image1
								.getAbsolutePath()));
				thumb.compress(CompressFormat.JPEG, 100, writer);
				writer.close();

				return thumb;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String computePhotoPathToGetThumbnailPath(
			String absolutePhotoPath) {
		String root = absolutePhotoPath.substring(0,
				absolutePhotoPath.lastIndexOf("/"));

		String photoName = absolutePhotoPath.substring(absolutePhotoPath
				.lastIndexOf("/") + 1);

		File thumbsDir = new File(root, "thumbs");
		if (!thumbsDir.exists()) {
			thumbsDir.mkdir();
		}

		return root + "/thumbs/" + photoName + THUMB_EXTENSION;

	}

	public static void setPicPhotos(Activity context, Battle battle,
			String originalFilename, LinearLayout into, String title) {
		if (into != null) {

			into.removeAllViews();

			String[] photos = fs.getAllFilenameLike(battle, originalFilename);
			File root = fs.getRootBattle(battle);

			into.setWeightSum(1);
                        			
                        if (photos != null){
                        for (String photo : photos) {

				File imageFile = new File(root.getAbsolutePath(), photo);

				ImageView imageView = new ImageView(
						context.getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

				LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
						1 / photos.length);
				imageView.setLayoutParams(new LayoutParams(param));

				ImageHelper.setPicAsync(context,
						ImageHelper.getThumbnailPath(battle, imageFile),
						imageView);

				imageView.setOnClickListener(new ZoomImageListener(context,
						imageFile, title, imageView));

				into.addView(imageView);
			}
                        }
		}
	}
}
