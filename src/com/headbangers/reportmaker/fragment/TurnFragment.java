package com.headbangers.reportmaker.fragment;

import java.io.File;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.headbangers.reportmaker.EditBattleActivity;
import com.headbangers.reportmaker.ImageHelper;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.listener.ZoomImageListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.service.FilesystemService;

public class TurnFragment extends RoboFragment {

	private Battle battle = null;

	private static final String MOVE_PHOTO_NAME = "player{P}_turn{X}_move.jpg";
	private static final String SHOOT_PHOTO_NAME = "player{P}_turn{X}_shoot.jpg";
	private static final String ASSAULT_PHOTO_NAME = "player{P}_turn{X}_assault.jpg";

	private static final int TAKE_PHOTO_MOVE_RESULT_CODE = 1;
	private static final int TAKE_PHOTO_SHOOT_RESULT_CODE = 2;
	private static final int TAKE_PHOTO_ASSAULT_RESULT_CODE = 3;

	@InjectView(R.id.tabhost)
	private TabHost tabHost;

	@InjectView(R.id.playerOneTurn)
	private ScrollView playerOneTurn;

	@InjectView(R.id.playerTwoTurn)
	private ScrollView playerTwoTurn;

	private ImageButton takePhotoMove1;
	private ImageButton takePhotoShoot1;
	private ImageButton takePhotoAssault1;
	private ImageButton takePhotoMove2;
	private ImageButton takePhotoShoot2;
	private ImageButton takePhotoAssault2;

	private ImageView photoMove1;
	private ImageView photoShoot1;
	private ImageView photoAssault1;
	private ImageView photoMove2;
	private ImageView photoShoot2;
	private ImageView photoAssault2;

	private EditText commentsMove1;
	private EditText commentsShoot1;
	private EditText commentsAssault1;
	private EditText commentsMove2;
	private EditText commentsShoot2;
	private EditText commentsAssault2;

	@InjectView(R.id.lastOne)
	private Switch lastOne;

	private int numTurn = 1;

	private FilesystemService fs = new FilesystemService();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.turn_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.tabHost.setup();

		TabSpec tab1 = tabHost.newTabSpec("1");
		tab1.setIndicator(this.battle.getOne().getName());
		tab1.setContent(R.id.playerOneTurn);
		this.tabHost.addTab(tab1);

		TabSpec tab2 = tabHost.newTabSpec("2");
		tab2.setIndicator(this.battle.getTwo().getName());
		tab2.setContent(R.id.playerTwoTurn);
		this.tabHost.addTab(tab2);

		tabHost.setCurrentTab(((EditBattleActivity) this.getActivity())
				.getFirstPlayer());

		construct();
		linkActions();

		if (lastOne != null && numTurn < 5) {
			lastOne.setVisibility(View.GONE);
		}
	}

	private void construct() {
		this.takePhotoMove1 = (ImageButton) this.playerOneTurn
				.findViewById(R.id.takePhoto_move);
		this.takePhotoMove2 = (ImageButton) this.playerTwoTurn
				.findViewById(R.id.takePhoto_move);
		this.takePhotoShoot1 = (ImageButton) this.playerOneTurn
				.findViewById(R.id.takePhoto_shoot);
		this.takePhotoShoot2 = (ImageButton) this.playerTwoTurn
				.findViewById(R.id.takePhoto_shoot);
		this.takePhotoAssault1 = (ImageButton) this.playerOneTurn
				.findViewById(R.id.takePhoto_assault);
		this.takePhotoAssault2 = (ImageButton) this.playerTwoTurn
				.findViewById(R.id.takePhoto_assault);

		this.photoMove1 = (ImageView) this.playerOneTurn
				.findViewById(R.id.takePhoto_move_photo);
		this.photoMove2 = (ImageView) this.playerTwoTurn
				.findViewById(R.id.takePhoto_move_photo);

		this.photoShoot1 = (ImageView) this.playerOneTurn
				.findViewById(R.id.takePhoto_shoot_photo);
		this.photoShoot2 = (ImageView) this.playerTwoTurn
				.findViewById(R.id.takePhoto_shoot_photo);

		this.photoAssault1 = (ImageView) this.playerOneTurn
				.findViewById(R.id.takePhoto_assault_photo);
		this.photoAssault2 = (ImageView) this.playerTwoTurn
				.findViewById(R.id.takePhoto_assault_photo);

		this.commentsMove1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments_move);
		this.commentsMove2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments_move);
		this.commentsShoot1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments_shoot);
		this.commentsShoot2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments_shoot);
		this.commentsAssault1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments_assault);
		this.commentsAssault2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments_assault);
	}

	private void linkActions() {
		this.takePhotoMove1.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, MOVE_PHOTO_NAME.replace("{P}", "1")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_MOVE_RESULT_CODE));
		this.takePhotoMove2.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, MOVE_PHOTO_NAME.replace("{P}", "2")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_MOVE_RESULT_CODE));

		this.takePhotoShoot1.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, SHOOT_PHOTO_NAME.replace("{P}", "1")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_SHOOT_RESULT_CODE));
		this.takePhotoShoot2.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, SHOOT_PHOTO_NAME.replace("{P}", "2")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_SHOOT_RESULT_CODE));

		this.takePhotoAssault1.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, ASSAULT_PHOTO_NAME.replace("{P}", "1")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_ASSAULT_RESULT_CODE));
		this.takePhotoAssault2.setOnClickListener(new TakePhotoListener(this
				.getActivity(), battle, ASSAULT_PHOTO_NAME.replace("{P}", "2")
				.replace("{X}", "" + numTurn), TAKE_PHOTO_ASSAULT_RESULT_CODE));
	}

	public void setBattle(Battle battle, int numTurn) {
		this.battle = battle;
		this.numTurn = numTurn;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {

		case TAKE_PHOTO_MOVE_RESULT_CODE:
			fillMovePhotos();
			break;

		case TAKE_PHOTO_SHOOT_RESULT_CODE:
			fillShootPhotos();
			break;

		case TAKE_PHOTO_ASSAULT_RESULT_CODE:
			fillAssaultPhotos();
			break;

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		fillPhotosIfNeeded();
	}

	private void fillPhotosIfNeeded() {
		fillMovePhotos();
		fillShootPhotos();
		fillAssaultPhotos();
	}

	private void fillMovePhotos() {
		File image1 = new File(fs.getRootBattle(battle), MOVE_PHOTO_NAME
				.replace("{P}", "1").replace("{X}", "" + numTurn));
		File image2 = new File(fs.getRootBattle(battle), MOVE_PHOTO_NAME
				.replace("{P}", "2").replace("{X}", "" + numTurn));

		if (image1.exists() && this.photoMove1 != null) {
			this.photoMove1.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image1, "Mouvement - "
					+ battle.getOne().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image1.getAbsolutePath(), this.photoMove1);
		}
		if (image2.exists() && this.photoMove2 != null) {
			this.photoMove2.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image2, "Mouvement - "
					+ battle.getTwo().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image2.getAbsolutePath(), this.photoMove2);
		}
	}

	private void fillShootPhotos() {

		File image1 = new File(fs.getRootBattle(battle), SHOOT_PHOTO_NAME
				.replace("{P}", "1").replace("{X}", "" + numTurn));
		File image2 = new File(fs.getRootBattle(battle), SHOOT_PHOTO_NAME
				.replace("{P}", "2").replace("{X}", "" + numTurn));

		if (image1.exists() && this.photoShoot1 != null) {
			this.photoShoot1.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image1, "Tir - "
					+ battle.getOne().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image1.getAbsolutePath(), this.photoShoot1);
		}
		if (image2.exists() && this.photoShoot2 != null) {
			this.photoShoot2.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image2, "Tir - "
					+ battle.getTwo().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image2.getAbsolutePath(), this.photoShoot2);
		}
	}

	private void fillAssaultPhotos() {
		File image1 = new File(fs.getRootBattle(battle), ASSAULT_PHOTO_NAME
				.replace("{P}", "1").replace("{X}", "" + numTurn));
		File image2 = new File(fs.getRootBattle(battle), ASSAULT_PHOTO_NAME
				.replace("{P}", "2").replace("{X}", "" + numTurn));

		if (image1.exists() && this.photoAssault1 != null) {
			this.photoAssault1.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image1, "Assaut - "
					+ battle.getOne().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image1.getAbsolutePath(), this.photoAssault1);
		}

		if (image2.exists() && this.photoAssault2 != null) {
			this.photoAssault2.setOnClickListener(new ZoomImageListener(this
					.getActivity(), image2, "Assaut - "
					+ battle.getTwo().getName()));
			ImageHelper.setPicAsync(this.getActivity(),
					image2.getAbsolutePath(), this.photoAssault2);
		}
	}

	public Turn buildTurn() {
		Turn turn = new Turn();
		turn.setCommentsMove1(this.commentsMove1.getText().toString());
		return turn;
	}
}
