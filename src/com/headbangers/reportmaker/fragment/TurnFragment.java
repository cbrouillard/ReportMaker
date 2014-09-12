package com.headbangers.reportmaker.fragment;

import org.jraf.android.backport.switchwidget.Switch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.EditBattleActivity;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.GameType;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.tools.ImageHelper;

public class TurnFragment extends SherlockFragment {

	private Battle battle = null;

	public static final String MOVE_PHOTO_NAME = "player{P}_turn{X}_move.jpg";
	public static final String SHOOT_PHOTO_NAME = "player{P}_turn{X}_shoot.jpg";
	public static final String ASSAULT_PHOTO_NAME = "player{P}_turn{X}_assault.jpg";
	public static final String CHARGE_PHOTO_NAME = "player{P}_turn{X}_charge.jpg";
	public static final String POWER_PHOTO_NAME = "player{P}_turn{X}_power.jpg";

	private static final int TAKE_PHOTO_MOVE_RESULT_CODE = 1;
	private static final int TAKE_PHOTO_SHOOT_RESULT_CODE = 2;
	private static final int TAKE_PHOTO_ASSAULT_RESULT_CODE = 3;
	private static final int TAKE_PHOTO_CHARGE_RESULT_CODE = 4;
	private static final int TAKE_PHOTO_POWER_RESULT_CODE = 5;

	private TabHost tabHost;
	private ScrollView playerOneTurn;
	private ScrollView playerTwoTurn;

	private TextView photosCharge1Header;
	private TextView photosCharge2Header;
	private TextView photosPower1Header;
	private TextView photosPower2Header;
	private TextView photosAssault1Header;
	private TextView photosAssault2Header;

	private ImageButton takePhotoCharge1;
	private ImageButton takePhotoMove1;
	private ImageButton takePhotoPower1;
	private ImageButton takePhotoShoot1;
	private ImageButton takePhotoAssault1;
	private ImageButton takePhotoCharge2;
	private ImageButton takePhotoMove2;
	private ImageButton takePhotoPower2;
	private ImageButton takePhotoShoot2;
	private ImageButton takePhotoAssault2;

	private LinearLayout photosMove1;
	private LinearLayout photosMove2;
	private LinearLayout photosPower1;
	private LinearLayout photosPower2;
	private LinearLayout photosCharge1;
	private LinearLayout photosCharge2;
	private LinearLayout photosAssault1;
	private LinearLayout photosAssault2;
	private LinearLayout photosShoot1;
	private LinearLayout photosShoot2;

	private LinearLayout extendedCommentsMove1;
	private LinearLayout extendedCommentsMove2;
	private LinearLayout extendedCommentsShoot1;
	private LinearLayout extendedCommentsShoot2;
	private LinearLayout extendedCommentsAssault1;
	private LinearLayout extendedCommentsAssault2;
	private LinearLayout extendedCommentsCharge1;
	private LinearLayout extendedCommentsCharge2;
	private LinearLayout extendedCommentsPower1;
	private LinearLayout extendedCommentsPower2;

	private LinearLayout containerCharge1;
	private LinearLayout containerCharge2;
	private LinearLayout containerPower1;
	private LinearLayout containerPower2;
	private LinearLayout containerAssault1;
	private LinearLayout containerAssault2;

	private EditText commentsMove1;
	private EditText commentsShoot1;
	private EditText commentsAssault1;
	private EditText commentsMove2;
	private EditText commentsShoot2;
	private EditText commentsAssault2;
	private EditText commentsCharge1;
	private EditText commentsCharge2;
	private EditText commentsPower1;
	private EditText commentsPower2;

	private EditText comments1;
	private EditText comments2;

	private Switch lastOne;
	private Switch nightFight;

	private int numTurn = 1;
	private Turn cache = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.turn_fragment, container, false);
	}

	private void findViews(View view) {
		this.tabHost = (TabHost) view.findViewById(R.id.tabhost);
		this.playerOneTurn = (ScrollView) view.findViewById(R.id.playerOneTurn);
		this.playerTwoTurn = (ScrollView) view.findViewById(R.id.playerTwoTurn);
		this.lastOne = (Switch) view.findViewById(R.id.lastOne);
		this.nightFight = (Switch) view.findViewById(R.id.nightFight);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findViews(view);

		this.tabHost.setup();

		if (!this.battle.getGameType().equals(GameType.XWING)) {
			TabSpec tab1 = tabHost.newTabSpec("1");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tab1.setIndicator(this.battle.getOne().getName());
			} else {
				tab1.setIndicator(makeTabIndicator(this.battle.getOne()
						.getName()));
			}
			tab1.setContent(R.id.playerOneTurn);
			this.tabHost.addTab(tab1);

			TabSpec tab2 = tabHost.newTabSpec("2");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tab2.setIndicator(this.battle.getTwo().getName());
			} else {
				tab2.setIndicator(makeTabIndicator(this.battle.getTwo()
						.getName()));
			}
			tab2.setContent(R.id.playerTwoTurn);

			this.tabHost.addTab(tab2);
			tabHost.setCurrentTab(((EditBattleActivity) this.getActivity())
					.getFirstPlayer());
		} else {
			this.playerTwoTurn.setVisibility(View.GONE);
		}

		construct();
		linkActions();

		if (nightFight != null && (numTurn > 1 && numTurn < 5)) {
			nightFight.setVisibility(View.GONE);
		}

		fillView();
	}

	@SuppressWarnings("deprecation")
	private TextView makeTabIndicator(String text) {
		TextView tabView = new TextView(this.getActivity());
		LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT, 40, 1);
		lp3.setMargins(1, 0, 1, 0);
		tabView.setLayoutParams(lp3);
		tabView.setText(text);
		tabView.setTextColor(Color.rgb(64, 64, 64));
		tabView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		tabView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tab_indicator));
		tabView.setPadding(10, 0, 10, 0);
		return tabView;
	}

	private void construct() {
		this.takePhotoPower1 = (ImageButton) this.playerOneTurn
				.findViewById(R.id.takePhoto_power);
		this.takePhotoPower2 = (ImageButton) this.playerTwoTurn
				.findViewById(R.id.takePhoto_power);
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
		this.takePhotoCharge1 = (ImageButton) this.playerOneTurn
				.findViewById(R.id.takePhoto_charge);
		this.takePhotoCharge2 = (ImageButton) this.playerTwoTurn
				.findViewById(R.id.takePhoto_charge);

		this.photosMove1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.takePhotos_move_photo);
		this.photosMove2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.takePhotos_move_photo);

		this.photosCharge1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.takePhotos_charge_photo);
		this.photosCharge2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.takePhotos_charge_photo);

		this.photosCharge1Header = (TextView) this.playerOneTurn
				.findViewById(R.id.chargePhotoHeader);
		this.photosCharge2Header = (TextView) this.playerTwoTurn
				.findViewById(R.id.chargePhotoHeader);
		this.photosPower1Header = (TextView) this.playerOneTurn
				.findViewById(R.id.powerPhotoHeader);
		this.photosPower2Header = (TextView) this.playerTwoTurn
				.findViewById(R.id.powerPhotoHeader);
		this.photosAssault1Header = (TextView) this.playerOneTurn
				.findViewById(R.id.assaultPhotosHeader);
		this.photosAssault2Header = (TextView) this.playerTwoTurn
				.findViewById(R.id.assaultPhotosHeader);
		
		this.photosShoot1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.takePhotos_shoot_photo);
		this.photosShoot2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.takePhotos_shoot_photo);

		this.photosAssault1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.takePhotos_assault_photo);
		this.photosAssault2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.takePhotos_assault_photo);

		this.photosPower1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.takePhotos_power_photo);
		this.photosPower2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.takePhotos_power_photo);

		this.comments1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments);
		this.comments2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments);

		this.extendedCommentsMove1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.extendedComment_move);
		this.extendedCommentsMove2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.extendedComment_move);
		this.extendedCommentsShoot1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.extendedComment_shoot);
		this.extendedCommentsShoot2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.extendedComment_shoot);
		this.extendedCommentsAssault1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.extendedComment_assault);
		this.extendedCommentsAssault2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.extendedComment_assault);
		this.extendedCommentsCharge1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.extendedComment_charge);
		this.extendedCommentsCharge2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.extendedComment_charge);
		this.extendedCommentsPower1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.extendedComment_power);
		this.extendedCommentsPower2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.extendedComment_power);

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
		this.commentsCharge1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments_charge);
		this.commentsCharge2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments_charge);
		this.commentsPower1 = (EditText) this.playerOneTurn
				.findViewById(R.id.comments_power);
		this.commentsPower2 = (EditText) this.playerTwoTurn
				.findViewById(R.id.comments_power);

		this.containerCharge1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.containerCharge);
		this.containerCharge2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.containerCharge);
		this.containerPower1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.containerPower);
		this.containerPower2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.containerPower);
		this.containerAssault1 = (LinearLayout) this.playerOneTurn
				.findViewById(R.id.containerAssault);
		this.containerAssault2 = (LinearLayout) this.playerTwoTurn
				.findViewById(R.id.containerAssault);

		// Doit-on faire apparaitre les commentaires étendus ?
		applyExtendedCommentsSettings();

		// Modifie l'interface en fonction du jeu
		this.hideOrShowAccordingGameType();
	}

	public void applyExtendedCommentsSettings() {
		if (getActivity() != null) { // teste si le fragment a déjà été
										// initialisé
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity());

			int visibility = preferences.getBoolean("extendedComments", false) ? View.VISIBLE
					: View.GONE;

			this.extendedCommentsMove1.setVisibility(visibility);
			this.extendedCommentsMove2.setVisibility(visibility);
			this.extendedCommentsShoot1.setVisibility(visibility);
			this.extendedCommentsShoot2.setVisibility(visibility);
			this.extendedCommentsAssault1.setVisibility(visibility);
			this.extendedCommentsAssault2.setVisibility(visibility);
			this.extendedCommentsCharge1.setVisibility(visibility);
			this.extendedCommentsCharge2.setVisibility(visibility);
			this.extendedCommentsPower1.setVisibility(visibility);
			this.extendedCommentsPower2.setVisibility(visibility);
		}
	}

	private void linkActions() {
		this.takePhotoMove1.setOnClickListener(new TakePhotoListener(this,
				battle, MOVE_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), TAKE_PHOTO_MOVE_RESULT_CODE));
		this.takePhotoMove2.setOnClickListener(new TakePhotoListener(this,
				battle, MOVE_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), TAKE_PHOTO_MOVE_RESULT_CODE));

		this.takePhotoShoot1.setOnClickListener(new TakePhotoListener(this,
				battle, SHOOT_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), TAKE_PHOTO_SHOOT_RESULT_CODE));
		this.takePhotoShoot2.setOnClickListener(new TakePhotoListener(this,
				battle, SHOOT_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), TAKE_PHOTO_SHOOT_RESULT_CODE));

		this.takePhotoAssault1.setOnClickListener(new TakePhotoListener(this,
				battle, ASSAULT_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), TAKE_PHOTO_ASSAULT_RESULT_CODE));
		this.takePhotoAssault2.setOnClickListener(new TakePhotoListener(this,
				battle, ASSAULT_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), TAKE_PHOTO_ASSAULT_RESULT_CODE));

		this.takePhotoCharge1.setOnClickListener(new TakePhotoListener(this,
				battle, CHARGE_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), TAKE_PHOTO_CHARGE_RESULT_CODE));
		this.takePhotoCharge2.setOnClickListener(new TakePhotoListener(this,
				battle, CHARGE_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), TAKE_PHOTO_CHARGE_RESULT_CODE));

		this.takePhotoPower1.setOnClickListener(new TakePhotoListener(this,
				battle, POWER_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), TAKE_PHOTO_POWER_RESULT_CODE));
		this.takePhotoPower2.setOnClickListener(new TakePhotoListener(this,
				battle, POWER_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), TAKE_PHOTO_POWER_RESULT_CODE));
	}

	public void setBattle(Battle battle, int numTurn) {
		this.battle = battle;
		this.numTurn = numTurn;

		fillView();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case TAKE_PHOTO_MOVE_RESULT_CODE:
			// createThumbnails(MOVE_PHOTO_NAME);
			fillMovePhotos();
			break;

		case TAKE_PHOTO_SHOOT_RESULT_CODE:
			// createThumbnails(SHOOT_PHOTO_NAME);
			fillShootPhotos();
			break;

		case TAKE_PHOTO_ASSAULT_RESULT_CODE:
			// createThumbnails(ASSAULT_PHOTO_NAME);
			fillAssaultPhotos();
			break;

		case TAKE_PHOTO_CHARGE_RESULT_CODE:
			// createThumbnails(ASSAULT_PHOTO_NAME);
			fillChargePhotos();
			break;

		case TAKE_PHOTO_POWER_RESULT_CODE:
			// createThumbnails(ASSAULT_PHOTO_NAME);
			fillPowerPhotos();
			break;

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		fillPhotosIfNeeded();
		restoreCache();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		createCache();
	}

	private void fillPhotosIfNeeded() {
		fillMovePhotos();
		fillShootPhotos();
		fillAssaultPhotos();
		fillChargePhotos();
		fillPowerPhotos();
	}

	private void fillMovePhotos() {
		setPicPhotos(
				MOVE_PHOTO_NAME.replace("{P}", "1")
						.replace("{X}", "" + numTurn), this.photosMove1,
				"Mouvement - " + battle.getOne().getName());

		setPicPhotos(
				MOVE_PHOTO_NAME.replace("{P}", "2")
						.replace("{X}", "" + numTurn), this.photosMove2,
				"Mouvement - " + battle.getTwo().getName());

	}

	private void fillShootPhotos() {

		setPicPhotos(
				SHOOT_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), this.photosShoot1, "Tir - "
						+ battle.getOne().getName());

		setPicPhotos(
				SHOOT_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), this.photosShoot2, "Tir - "
						+ battle.getTwo().getName());
	}

	private void fillAssaultPhotos() {
		setPicPhotos(
				ASSAULT_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), this.photosAssault1, "Assaut - "
						+ battle.getOne().getName());

		setPicPhotos(
				ASSAULT_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), this.photosAssault2, "Assaut - "
						+ battle.getTwo().getName());
	}

	private void fillChargePhotos() {
		setPicPhotos(
				CHARGE_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), this.photosCharge1, "Charge - "
						+ battle.getOne().getName());

		setPicPhotos(
				CHARGE_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), this.photosCharge2, "Charge - "
						+ battle.getTwo().getName());
	}

	private void fillPowerPhotos() {
		setPicPhotos(
				POWER_PHOTO_NAME.replace("{P}", "1").replace("{X}",
						"" + numTurn), this.photosPower1, "Power - "
						+ battle.getOne().getName());

		setPicPhotos(
				POWER_PHOTO_NAME.replace("{P}", "2").replace("{X}",
						"" + numTurn), this.photosPower2, "Power - "
						+ battle.getTwo().getName());
	}

	private void setPicPhotos(String originalFilename, LinearLayout into,
			String title) {

		ImageHelper.setPicPhotos(this.getActivity(), battle, originalFilename,
				into, title);
	}

	public Turn buildTurn() {
		Turn turn = new Turn();

		turn.setComments1(this.comments1 != null ? this.comments1.getText()
				.toString() : null);
		turn.setComments2(this.comments2 != null ? this.comments2.getText()
				.toString() : null);

		turn.setCommentsMove1(this.commentsMove1 != null ? this.commentsMove1
				.getText().toString() : null);
		turn.setCommentsMove2(this.commentsMove2 != null ? this.commentsMove2
				.getText().toString() : null);

		turn.setCommentsShoot1(this.commentsShoot1 != null ? this.commentsShoot1
				.getText().toString() : null);
		turn.setCommentsShoot2(this.commentsShoot2 != null ? this.commentsShoot2
				.getText().toString() : null);

		turn.setCommentsAssault1(this.commentsAssault1 != null ? this.commentsAssault1
				.getText().toString() : null);
		turn.setCommentsAssault2(this.commentsAssault2 != null ? this.commentsAssault2
				.getText().toString() : null);

		turn.setCommentsCharge1(this.commentsCharge1 != null ? this.commentsCharge1
				.getText().toString() : null);
		turn.setCommentsCharge2(this.commentsCharge2 != null ? this.commentsCharge2
				.getText().toString() : null);

		turn.setCommentsPower1(this.commentsPower1 != null ? this.commentsPower1
				.getText().toString() : null);
		turn.setCommentsPower2(this.commentsPower2 != null ? this.commentsPower2
				.getText().toString() : null);

		turn.setLastOne(this.lastOne != null ? this.lastOne.isChecked() : null);

		turn.setNightFight(this.nightFight != null ? this.nightFight
				.isChecked() : null);

		turn.setNum(numTurn);

		return turn;
	}

	public void fillView() {
		if (this.battle == null) {
			return;
		}

		Turn turn = this.battle.getTurn(this.numTurn);
		fillView(turn);
	}

	private void fillView(Turn turn) {
		if (this.comments1 != null) {
			this.comments1.setText(turn.getComments1());
		}
		if (this.comments2 != null) {
			this.comments2.setText(turn.getComments2());
		}

		if (this.commentsMove1 != null) {
			this.commentsMove1.setText(turn.getCommentsMove1());
		}
		if (this.commentsMove2 != null) {
			this.commentsMove2.setText(turn.getCommentsMove2());
		}
		if (this.commentsShoot1 != null) {
			this.commentsShoot1.setText(turn.getCommentsShoot1());
		}
		if (this.commentsShoot2 != null) {
			this.commentsShoot2.setText(turn.getCommentsShoot2());
		}
		if (this.commentsAssault1 != null) {
			this.commentsAssault1.setText(turn.getCommentsAssault1());
		}
		if (this.commentsAssault2 != null) {
			this.commentsAssault2.setText(turn.getCommentsAssault2());
		}
		if (this.commentsCharge1 != null) {
			this.commentsCharge1.setText(turn.getCommentsCharge1());
		}
		if (this.commentsCharge2 != null) {
			this.commentsCharge2.setText(turn.getCommentsCharge2());
		}
		if (this.commentsPower1 != null) {
			this.commentsPower1.setText(turn.getCommentsPower1());
		}
		if (this.commentsPower2 != null) {
			this.commentsPower2.setText(turn.getCommentsPower2());
		}

		if (this.lastOne != null) {
			this.lastOne.setChecked(turn.isLastOne() != null ? turn.isLastOne()
					: false);
		}
		if (this.nightFight != null) {
			this.nightFight.setChecked(turn.isNightFight() != null ? turn
					.isNightFight() : false);
		}
	}

	private void hideOrShowAccordingGameType() {
		switch (this.battle.getGameType()) {
		case WARHAMMER_40K:
			this.photosCharge1Header.setVisibility(View.GONE);
			this.photosCharge2Header.setVisibility(View.GONE);

			this.containerCharge1.setVisibility(View.GONE);
			this.containerCharge2.setVisibility(View.GONE);
			
			this.extendedCommentsCharge1.setVisibility(View.GONE);
			this.extendedCommentsCharge2.setVisibility(View.GONE);

			break;
		case WARHAMMER_BATTLE:
			if (this.nightFight != null) {
				this.nightFight.setVisibility(View.GONE);
			}
			break;
		case XWING:
			this.photosCharge1Header.setVisibility(View.GONE);
			this.photosCharge2Header.setVisibility(View.GONE);
			this.photosPower1Header.setVisibility(View.GONE);
			this.photosPower2Header.setVisibility(View.GONE);
			this.photosAssault1Header.setVisibility(View.GONE);
			this.photosAssault2Header.setVisibility(View.GONE);
			
			this.containerCharge1.setVisibility(View.GONE);
			this.containerCharge2.setVisibility(View.GONE);
			this.containerAssault1.setVisibility(View.GONE);
			this.containerAssault2.setVisibility(View.GONE);
			this.containerPower1.setVisibility(View.GONE);
			this.containerPower2.setVisibility(View.GONE);
			
			if (this.nightFight != null) {
				this.nightFight.setVisibility(View.GONE);
			}
			
			this.extendedCommentsAssault1.setVisibility(View.GONE);
			this.extendedCommentsAssault2.setVisibility(View.GONE);
			this.extendedCommentsCharge1.setVisibility(View.GONE);
			this.extendedCommentsCharge2.setVisibility(View.GONE);
			this.extendedCommentsPower1.setVisibility(View.GONE);
			this.extendedCommentsPower2.setVisibility(View.GONE);
			
			break;
		}
	}

	public void createCache() {
		this.cache = buildTurn();
	}

	public void restoreCache() {
		if (this.cache != null) {
			fillView(this.cache);
		}
	}
}
