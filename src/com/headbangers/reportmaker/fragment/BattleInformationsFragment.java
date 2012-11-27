package com.headbangers.reportmaker.fragment;

import java.io.File;
import java.io.IOException;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;

public class BattleInformationsFragment extends RoboFragment {

	private static final int TAKE_PHOTO_TABLE_RESULT_CODE = 1;
	private static final int TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE = 2;
	private static final int TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE = 3;

	private static final String TABLE_PHOTO_NAME = "table.jpg";
	private static final String DEPLOYMENT_PHOTO_NAME = "deployment_{p}.jpg";

	private FilesystemService filesystemService = new FilesystemService();
	private Battle battle;

	@InjectView(R.id.takePhoto_table)
	private ImageButton takePhotoTable;

	@InjectView(R.id.takePhoto_table_photo)
	private ImageView tablePhotoView;

	@InjectView(R.id.deploymentType)
	private EditText deploymentType;

	@InjectView(R.id.scenario)
	private EditText scenario;

	@InjectView(R.id.whoStart)
	private Spinner whoStart;

	@InjectView(R.id.lordCapacityPlayer1)
	private TextView lordCapacityTextViewPlayer1;

	@InjectView(R.id.deploymentPlayer1)
	private TextView deploymentTextViewPlayer1;

	@InjectView(R.id.lordCapacityPlayer2)
	private TextView lordCapacityTextViewPlayer2;

	@InjectView(R.id.deploymentPlayer2)
	private TextView deploymentTextViewPlayer2;

	@InjectView(R.id.lordCapacity1)
	private EditText lordCapacity1;

	@InjectView(R.id.takePhoto_deployment1)
	private ImageButton takePhotoDeployment1;

	@InjectView(R.id.lordCapacity2)
	private EditText lordCapacity2;

	@InjectView(R.id.takePhoto_deployment2)
	private ImageButton takePhotoDeployment2;

	@InjectView(R.id.takePhoto_deployment1_photo)
	private ImageView deployment1Photo;

	@InjectView(R.id.takePhoto_deployment2_photo)
	private ImageView deployment2Photo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.battle_infos_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.takePhotoTable.setOnClickListener(new TakePhotoListener(this
				.getActivity(), this.battle, TABLE_PHOTO_NAME,
				TAKE_PHOTO_TABLE_RESULT_CODE));

		this.takePhotoDeployment1.setOnClickListener(new TakePhotoListener(this
				.getActivity(), this.battle, DEPLOYMENT_PHOTO_NAME.replace(
				"{p}", this.battle.getOne().getName()),
				TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE));
		
		this.takePhotoDeployment1.setOnClickListener(new TakePhotoListener(this
				.getActivity(), this.battle, DEPLOYMENT_PHOTO_NAME.replace(
				"{p}", this.battle.getTwo().getName()),
				TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE));

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this.getActivity(), android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(this.battle.getOne().getName());
		adapter.add(this.battle.getTwo().getName());
		this.whoStart.setAdapter(adapter);

		this.lordCapacityTextViewPlayer1.setText(this.getActivity()
				.getResources().getString(R.string.lord_capacity)
				+ " " + this.battle.getOne().getName());

		this.lordCapacityTextViewPlayer2.setText(this.getActivity()
				.getResources().getString(R.string.lord_capacity)
				+ " " + this.battle.getTwo().getName());

		this.deploymentTextViewPlayer1.setText(this.getActivity()
				.getResources().getString(R.string.photo_deployment_player)
				+ " " + this.battle.getOne().getName());

		this.deploymentTextViewPlayer2.setText(this.getActivity()
				.getResources().getString(R.string.photo_deployment_player)
				+ " " + this.battle.getTwo().getName());
	}

	private void fillPhotosIfNeeded() {

		File imageFile = new File(filesystemService.getRootBattle(battle),
				TABLE_PHOTO_NAME);

		if (imageFile.exists() && this.tablePhotoView != null) {
			try {
				setPic(imageFile.getAbsolutePath(), this.tablePhotoView);
			} catch (IOException e) {
			}
		}

	}

	private void setPic(String photoPath, ImageView view) throws IOException {

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
