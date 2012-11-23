package com.headbangers.reportmaker.fragment;

import java.io.File;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;

public class BattleInformationsFragment extends RoboFragment {

	private static final int TAKE_PHOTO_TABLE = 1;

	private FilesystemService filesystemService = new FilesystemService();
	private Battle battle;

	@InjectView(R.id.takePhoto_table)
	private Button takePhotoTable;

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
						.getRootBattle(battle), "table.jpg");

				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(imageFile));

				startActivityForResult(takePictureIntent, TAKE_PHOTO_TABLE);

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO_TABLE:
			// Affichage de l'image dans l'image view TABLE
			Log.d("BattleInformationsFragment",
					"Affichage de l'image dans l'imageView TABLE");
			Bundle extras = data.getExtras();
			this.tablePhotoView.setImageBitmap((Bitmap) extras.get("data"));
			break;
		}
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}
}
