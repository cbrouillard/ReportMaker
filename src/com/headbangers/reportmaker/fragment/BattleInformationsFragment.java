package com.headbangers.reportmaker.fragment;

import java.io.File;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
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
			this.tablePhotoView.setImageURI(Uri.fromFile(imageFile));
		}

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
