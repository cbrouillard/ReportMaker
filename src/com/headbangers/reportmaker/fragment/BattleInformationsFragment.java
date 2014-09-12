package com.headbangers.reportmaker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.GameType;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.tools.ImageHelper;

public class BattleInformationsFragment extends SherlockFragment {

	public static final int TAKE_PHOTO_TABLE_RESULT_CODE = 10001;
	public static final int TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE = 10002;
	public static final int TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE = 10003;
	public static final int TAKE_PHOTO_INFILTRATION1_RESULT_CODE = 10004;
	public static final int TAKE_PHOTO_INFILTRATION2_RESULT_CODE = 10005;
	public static final int TAKE_PHOTO_SCOOT1_RESULT_CODE = 10006;
	public static final int TAKE_PHOTO_SCOOT2_RESULT_CODE = 10007;
	public static final int LONG_TAKE_PHOTO_TABLE_RESULT_CODE = 10011;
	public static final int LONG_TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE = 10012;
	public static final int LONG_TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE = 10013;
	public static final int LONG_TAKE_PHOTO_INFILTRATION1_RESULT_CODE = 10014;
	public static final int LONG_TAKE_PHOTO_INFILTRATION2_RESULT_CODE = 10015;
	public static final int LONG_TAKE_PHOTO_SCOOT1_RESULT_CODE = 10016;
	public static final int LONG_TAKE_PHOTO_SCOOT2_RESULT_CODE = 10017;

	public static final String TABLE_PHOTO_NAME = "table.jpg";
	public static final String DEPLOYMENT1_PHOTO_NAME = "deploiement_j1.jpg";
	public static final String DEPLOYMENT2_PHOTO_NAME = "deploiement_j2.jpg";
	public static final String INFILTRATION1_PHOTO_NAME = "infiltration_j1.jpg";
	public static final String INFILTRATION2_PHOTO_NAME = "infiltration_j2.jpg";
	public static final String SCOOT1_PHOTO_NAME = "scoot_j1.jpg";
	public static final String SCOOT2_PHOTO_NAME = "scoot_j2.jpg";

	private Battle battle;

	private ImageButton takePhotoTable;
	private EditText deploymentType;
	private TextView deploymentTypeHeader;
	private EditText scenario;
	private Spinner whoStart;
	private TextView lordCapacityTextViewPlayer1;
	private TextView deploymentTextViewPlayer1;
	private TextView lordCapacityTextViewPlayer2;
	private TextView deploymentTextViewPlayer2;
	private EditText lordCapacity1;
	private ImageButton takePhotoDeployment1;
	private EditText lordCapacity2;
	private ImageButton takePhotoDeployment2;
	private EditText comments;

	private TextView powersPlayer1Header;
	private TextView powersPlayer1;

	private TextView powersPlayer2Header;
	private TextView powersPlayer2;

	private TextView infiltrationPlayer1;
	private ImageButton infiltrationTakePhotoPlayer1;
	private TextView infiltrationPlayer2;
	private ImageButton infiltrationTakePhotoPlayer2;
	private TextView scootPlayer1;
	private ImageButton scootTakePhotoPlayer1;
	private TextView scootPlayer2;
	private ImageButton scootTakePhotoPlayer2;

	private LinearLayout tablePhotosView;
	private LinearLayout deployment1PhotosView;
	private LinearLayout deployment2PhotosView;
	private LinearLayout infiltration1PhotosView;
	private LinearLayout infiltration2PhotosView;
	private LinearLayout scoot1PhotosView;
	private LinearLayout scoot2PhotosView;

	private LinearLayout containerInfiltration1;
	private LinearLayout containerInfiltration2;
	private LinearLayout containerScoot1;
	private LinearLayout containerScoot2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.battle_infos_fragment, container,
				false);
	}

	private void findViews(View view) {
		this.takePhotoTable = (ImageButton) view
				.findViewById(R.id.takePhoto_table);

		this.deploymentType = (EditText) view.findViewById(R.id.deploymentType);
		this.deploymentTypeHeader = (TextView) view
				.findViewById(R.id.deploymentTypeHeader);
		this.scenario = (EditText) view.findViewById(R.id.scenario);
		this.whoStart = (Spinner) view.findViewById(R.id.whoStart);
		this.lordCapacityTextViewPlayer1 = (TextView) view
				.findViewById(R.id.lordCapacityPlayer1);
		this.deploymentTextViewPlayer1 = (TextView) view
				.findViewById(R.id.deploymentPlayer1);
		this.lordCapacityTextViewPlayer2 = (TextView) view
				.findViewById(R.id.lordCapacityPlayer2);
		this.deploymentTextViewPlayer2 = (TextView) view
				.findViewById(R.id.deploymentPlayer2);
		this.lordCapacity1 = (EditText) view.findViewById(R.id.lordCapacity1);
		this.takePhotoDeployment1 = (ImageButton) view
				.findViewById(R.id.takePhoto_deployment1);
		this.lordCapacity2 = (EditText) view.findViewById(R.id.lordCapacity2);
		this.takePhotoDeployment2 = (ImageButton) view
				.findViewById(R.id.takePhoto_deployment2);
		this.comments = (EditText) view.findViewById(R.id.comments);
		this.infiltrationPlayer1 = (TextView) view
				.findViewById(R.id.infiltrationPlayer1);
		this.infiltrationTakePhotoPlayer1 = (ImageButton) view
				.findViewById(R.id.takePhoto_infiltration1);
		this.infiltrationPlayer2 = (TextView) view
				.findViewById(R.id.infiltrationPlayer2);
		this.infiltrationTakePhotoPlayer2 = (ImageButton) view
				.findViewById(R.id.takePhoto_infiltration2);
		this.scootPlayer1 = (TextView) view.findViewById(R.id.scootPlayer1);
		this.scootTakePhotoPlayer1 = (ImageButton) view
				.findViewById(R.id.takePhoto_scoot1);
		this.scootPlayer2 = (TextView) view.findViewById(R.id.scootPlayer2);
		this.scootTakePhotoPlayer2 = (ImageButton) view
				.findViewById(R.id.takePhoto_scoot2);

		this.tablePhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_table_photos);
		this.deployment1PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_deployment1_photos);
		this.deployment2PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_deployment2_photos);
		this.infiltration1PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_infiltration1_photos);
		this.infiltration2PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_infiltration2_photos);
		this.scoot1PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_scoot1_photos);
		this.scoot2PhotosView = (LinearLayout) view
				.findViewById(R.id.takePhoto_scoot2_photos);

		this.powersPlayer1Header = (TextView) view
				.findViewById(R.id.powersPlayer1Header);
		this.powersPlayer2Header = (TextView) view
				.findViewById(R.id.powersPlayer2Header);
		this.powersPlayer1 = (EditText) view.findViewById(R.id.powersPlayer1);
		this.powersPlayer2 = (EditText) view.findViewById(R.id.powersPlayer2);

		this.containerInfiltration1 = (LinearLayout) view
				.findViewById(R.id.containerInfiltration1);
		this.containerInfiltration2 = (LinearLayout) view
				.findViewById(R.id.containerInfiltration2);
		this.containerScoot1 = (LinearLayout) view
				.findViewById(R.id.containerScoot1);
		this.containerScoot2 = (LinearLayout) view
				.findViewById(R.id.containerScoot2);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findViews(view);

		this.takePhotoTable.setOnClickListener(new TakePhotoListener(this,
				this.battle, TABLE_PHOTO_NAME, TAKE_PHOTO_TABLE_RESULT_CODE));

		this.takePhotoDeployment1.setOnClickListener(new TakePhotoListener(
				this, this.battle, DEPLOYMENT1_PHOTO_NAME,
				TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE));

		this.takePhotoDeployment2.setOnClickListener(new TakePhotoListener(
				this, this.battle, DEPLOYMENT2_PHOTO_NAME,
				TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE));

		this.infiltrationTakePhotoPlayer1
				.setOnClickListener(new TakePhotoListener(this, this.battle,
						INFILTRATION1_PHOTO_NAME,
						TAKE_PHOTO_INFILTRATION1_RESULT_CODE));
		this.infiltrationTakePhotoPlayer2
				.setOnClickListener(new TakePhotoListener(this, this.battle,
						INFILTRATION2_PHOTO_NAME,
						TAKE_PHOTO_INFILTRATION2_RESULT_CODE));

		this.scootTakePhotoPlayer1.setOnClickListener(new TakePhotoListener(
				this, this.battle, SCOOT1_PHOTO_NAME,
				TAKE_PHOTO_SCOOT1_RESULT_CODE));
		this.scootTakePhotoPlayer2.setOnClickListener(new TakePhotoListener(
				this, this.battle, SCOOT2_PHOTO_NAME,
				TAKE_PHOTO_SCOOT2_RESULT_CODE));

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

		this.infiltrationPlayer1.setText(this.getActivity().getResources()
				.getString(R.string.photo_infiltration)
				+ " " + this.battle.getOne().getName());

		this.infiltrationPlayer2.setText(this.getActivity().getResources()
				.getString(R.string.photo_infiltration)
				+ " " + this.battle.getTwo().getName());

		this.scootPlayer1.setText(this.getActivity().getResources()
				.getString(R.string.photo_scoot)
				+ " " + this.battle.getOne().getName());

		this.scootPlayer2.setText(this.getActivity().getResources()
				.getString(R.string.photo_scoot)
				+ " " + this.battle.getTwo().getName());

		fillView();
	}

	private void fillPhotosIfNeeded() {
		setPicPhotos(TABLE_PHOTO_NAME, this.tablePhotosView, "Table");
		setPicPhotos(DEPLOYMENT1_PHOTO_NAME, this.deployment1PhotosView,
				"Déploiement");
		setPicPhotos(DEPLOYMENT2_PHOTO_NAME, this.deployment2PhotosView,
				"Déploiement");
		setPicPhotos(INFILTRATION1_PHOTO_NAME, this.infiltration1PhotosView,
				"Infiltration");
		setPicPhotos(INFILTRATION2_PHOTO_NAME, this.infiltration2PhotosView,
				"Infiltration");
		setPicPhotos(SCOOT1_PHOTO_NAME, this.scoot1PhotosView,
				"Mouvement scoot");
		setPicPhotos(SCOOT2_PHOTO_NAME, this.scoot2PhotosView,
				"Mouvement scoot");

	}

	private void setPicPhotos(String originalFilename, LinearLayout into,
			String title) {

		ImageHelper.setPicPhotos(this.getActivity(), battle, originalFilename,
				into, title);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}

	@Override
	public void onResume() {
		super.onResume();
		fillPhotosIfNeeded();
	}

	public Informations buildInformations() {
		Informations infos = new Informations();

		infos.setDeploymentType(this.deploymentType.getText().toString());
		infos.setComments(this.comments.getText().toString());
		infos.setFirstPlayer(this.whoStart.getSelectedItemPosition());
		infos.setLordCapacity1(this.lordCapacity1.getText().toString());
		infos.setLordCapacity2(this.lordCapacity2.getText().toString());
		infos.setScenario(this.scenario.getText().toString());
		infos.setPowers1(this.powersPlayer1.getText().toString());
		infos.setPowers2(this.powersPlayer2.getText().toString());

		return infos;
	}

	public void fillView() {
		if (this.battle == null) {
			return;
		}

		this.comments.setText(this.battle.getInfos().getComments());
		this.deploymentType.setText(this.battle.getInfos().getDeploymentType());
		this.lordCapacity1.setText(this.battle.getInfos().getLordCapacity1());
		this.lordCapacity2.setText(this.battle.getInfos().getLordCapacity2());
		this.scenario.setText(this.battle.getInfos().getScenario());
		this.whoStart.setSelection(this.battle.getInfos().getFirstPlayer());
		this.powersPlayer1.setText(this.battle.getInfos().getPowers1());
		this.powersPlayer2.setText(this.battle.getInfos().getPowers2());

		this.hideOrShowAccordingGameType();
	}

	public void hideOrShowAccordingGameType() {
		if (this.battle == null) {
			return;
		}

		if (this.battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {
			this.lordCapacityTextViewPlayer1.setVisibility(View.GONE);
			this.lordCapacity1.setVisibility(View.GONE);
			this.lordCapacityTextViewPlayer2.setVisibility(View.GONE);
			this.lordCapacity2.setVisibility(View.GONE);
		}

		if (this.battle.getGameType().equals(GameType.XWING)) {

			// Type de déploiement
			this.deploymentType.setVisibility(View.GONE);
			this.deploymentTypeHeader.setVisibility(View.GONE);

			// Seigneurs de Guerre
			this.lordCapacityTextViewPlayer1.setVisibility(View.GONE);
			this.lordCapacity1.setVisibility(View.GONE);
			this.lordCapacityTextViewPlayer2.setVisibility(View.GONE);
			this.lordCapacity2.setVisibility(View.GONE);

			// Photos d'infiltrations et scoot.
			infiltrationPlayer1.setVisibility(View.GONE);
			infiltrationTakePhotoPlayer1.setVisibility(View.GONE);
			infiltrationPlayer2.setVisibility(View.GONE);
			infiltrationTakePhotoPlayer2.setVisibility(View.GONE);
			scootPlayer1.setVisibility(View.GONE);
			scootTakePhotoPlayer1.setVisibility(View.GONE);
			scootPlayer2.setVisibility(View.GONE);
			scootTakePhotoPlayer2.setVisibility(View.GONE);

			containerInfiltration1.setVisibility(View.GONE);
			containerInfiltration2.setVisibility(View.GONE);
			containerScoot1.setVisibility(View.GONE);
			containerScoot2.setVisibility(View.GONE);

			// Pouvoirs
			this.powersPlayer1.setVisibility(View.GONE);
			this.powersPlayer2.setVisibility(View.GONE);
			this.powersPlayer1Header.setVisibility(View.GONE);
			this.powersPlayer2Header.setVisibility(View.GONE);
		}
	}

	public int getFirstPlayer() {

		if (this.whoStart != null) {
			return this.whoStart.getSelectedItemPosition();
		}

		return 0;
	}
}
