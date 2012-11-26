package com.headbangers.reportmaker.fragment;

import java.io.File;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;

public class BattleInformationsFragment extends RoboFragment {

	private static final int TAKE_PHOTO_TABLE = 1;

	protected static final String TABLE_PHOTO_NAME = "table.jpg";

	private FilesystemService filesystemService = new FilesystemService();
	private Battle battle;

	@InjectView(R.id.takePhoto_table)
	private ImageButton takePhotoTable;

	@InjectView(R.id.takePhoto_table_photo)
	private ImageView tablePhotoView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.battle_infos_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.takePhotoTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				File imageFile = new File(filesystemService
						.getRootBattle(battle), TABLE_PHOTO_NAME);

				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(imageFile));

				startActivityForResult(takePictureIntent, TAKE_PHOTO_TABLE);

			}
		});

		fillPhotosIfNeeded();

	}

	private void fillPhotosIfNeeded() {

		File imageFile = new File(filesystemService.getRootBattle(battle),
				TABLE_PHOTO_NAME);

		if (imageFile.exists() && this.tablePhotoView != null) {
			setPic(imageFile.getAbsolutePath(), this.tablePhotoView);
		}

	}

	private void setPic(String photoPath, ImageView view) {
		// Get the dimensions of the View
		int targetW = 512; //view.getWidth();
		int targetH = 512; //view.getHeight();

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
		view.setImageBitmap(bitmap);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		fillPhotosIfNeeded();
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}

	@Override
	public void onResume() {
		super.onResume();

		fillPhotosIfNeeded();
	}
}
