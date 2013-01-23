package com.headbangers.reportmaker.service;

import java.io.File;

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
		// try {
		// Bitmap bitmap = ImageHelper.rotateAndResize(path, 512);
		Bitmap bitmap = null;
		File imageFile = new File(path);

		if (!imageFile.exists() && imageFile.getPath().endsWith(".thumb")) {
			// Création du thumb à la volée
			String unsized = path.substring(0, path.indexOf(".thumb"));
			bitmap = ImageHelper.createThumbnail(unsized);
		} else {

			bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

		}

		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

		return drawable;

		// } catch (IOException e) {
		// }
		//
		// return null;

		// try {
		//
		// InputStream is = null;
		// if (cacheFileName != null) {
		//
		// File root = Environment.getExternalStorageDirectory();
		// root.mkdir();
		// File storage = new File(root, CACHE_DIRECTORY);
		// storage.mkdirs();
		// File file = new File(storage, cacheFileName);
		//
		// if (file.exists()) {
		// Drawable drawable = Drawable.createFromStream(
		// new FileInputStream(file), "src");
		// drawableMap.put(urlString, drawable);
		// return drawable;
		// }
		//
		// is = fetch(urlString);
		//
		// FileOutputStream fos = new FileOutputStream(file);
		// BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);
		// int current;
		//
		// while ((current = is.read()) != -1) {
		// bos.write(current);
		// }
		// bos.flush();
		// bos.close();
		//
		// is.close();
		// fos.close();
		//
		// is = new FileInputStream(new File(storage, cacheFileName));
		//
		// } else {
		// is = fetch(urlString);
		// }
		//
		// Drawable drawable = Drawable.createFromStream(is, "src");
		// drawableMap.put(urlString, drawable);
		//
		// return drawable;
		//
		// } catch (MalformedURLException e) {
		// Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		// return null;
		// } catch (IOException e) {
		// Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		// return null;
		// }
	}

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
