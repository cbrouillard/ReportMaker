package com.headbangers.reportmaker.fragment;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.listener.ZoomImageListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.tools.ImageHelper;

public class BattleInformationsFragment extends SherlockFragment {

	public static final int TAKE_PHOTO_TABLE_RESULT_CODE = 10001;
	public static final int TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE = 10002;
	public static final int TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE = 10003;
	public static final int TAKE_PHOTO_INFILTRATION1_RESULT_CODE = 10004;
	public static final int TAKE_PHOTO_INFILTRATION2_RESULT_CODE = 10005;
	public static final int TAKE_PHOTO_SCOOT1_RESULT_CODE = 10006;
	public static final int TAKE_PHOTO_SCOOT2_RESULT_CODE = 10007;

	public static final String TABLE_PHOTO_NAME = "table.jpg";
	public static final String DEPLOYMENT1_PHOTO_NAME = "deploiement_j1.jpg";
	public static final String DEPLOYMENT2_PHOTO_NAME = "deploiement_j2.jpg";
	public static final String INFILTRATION1_PHOTO_NAME = "infiltration_j1.jpg";
	public static final String INFILTRATION2_PHOTO_NAME = "infiltration_j2.jpg";
	public static final String SCOOT1_PHOTO_NAME = "scoot_j1.jpg";
	public static final String SCOOT2_PHOTO_NAME = "scoot_j2.jpg";
	public static final String THUMB_EXTENSION = ".thumb";

	private FilesystemService fs = new FilesystemService();
	private Battle battle;

	private ImageButton takePhotoTable;
	private ImageView tablePhotoView;
	private EditText deploymentType;
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
	private ImageView deployment1Photo;
	private ImageView deployment2Photo;
	private EditText comments;
	private TextView infiltrationPlayer1;
	private ImageButton infiltrationTakePhotoPlayer1;
	private ImageView infiltrationPhotoPlayer1;
	private TextView infiltrationPlayer2;
	private ImageButton infiltrationTakePhotoPlayer2;
	private ImageView infiltrationPhotoPlayer2;
	private TextView scootPlayer1;
	private ImageButton scootTakePhotoPlayer1;
	private ImageView scootPhotoPlayer1;
	private TextView scootPlayer2;
	private ImageButton scootTakePhotoPlayer2;
	private ImageView scootPhotoPlayer2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.battle_infos_fragment, container,
				false);
	}

	private void findViews(View view) {
		this.takePhotoTable = (ImageButton) view
				.findViewById(R.id.takePhoto_table);
		this.tablePhotoView = (ImageView) view
				.findViewById(R.id.takePhoto_table_photo);
		this.deploymentType = (EditText) view.findViewById(R.id.deploymentType);
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
		this.deployment1Photo = (ImageView) view
				.findViewById(R.id.takePhoto_deployment1_photo);
		this.deployment2Photo = (ImageView) view
				.findViewById(R.id.takePhoto_deployment2_photo);
		this.comments = (EditText) view.findViewById(R.id.comments);
		this.infiltrationPlayer1 = (TextView) view
				.findViewById(R.id.infiltrationPlayer1);
		this.infiltrationTakePhotoPlayer1 = (ImageButton) view
				.findViewById(R.id.takePhoto_infiltration1);
		this.infiltrationPhotoPlayer1 = (ImageView) view
				.findViewById(R.id.takePhoto_infiltration1_photo);
		this.infiltrationPlayer2 = (TextView) view
				.findViewById(R.id.infiltrationPlayer2);
		this.infiltrationTakePhotoPlayer2 = (ImageButton) view
				.findViewById(R.id.takePhoto_infiltration2);
		this.infiltrationPhotoPlayer2 = (ImageView) view
				.findViewById(R.id.takePhoto_infiltration2_photo);
		this.scootPlayer1 = (TextView) view.findViewById(R.id.scootPlayer1);
		this.scootTakePhotoPlayer1 = (ImageButton) view
				.findViewById(R.id.takePhoto_scoot1);
		this.scootPhotoPlayer1 = (ImageView) view
				.findViewById(R.id.takePhoto_scoot1_photo);
		this.scootPlayer2 = (TextView) view.findViewById(R.id.scootPlayer2);
		this.scootTakePhotoPlayer2 = (ImageButton) view
				.findViewById(R.id.takePhoto_scoot2);
		this.scootPhotoPlayer2 = (ImageView) view
				.findViewById(R.id.takePhoto_scoot2_photo);
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

		File table = new File(fs.getRootBattle(battle), TABLE_PHOTO_NAME);
		File deployment1 = new File(fs.getRootBattle(battle),
				DEPLOYMENT1_PHOTO_NAME);
		File deployment2 = new File(fs.getRootBattle(battle),
				DEPLOYMENT2_PHOTO_NAME);
		File infiltration1 = new File(fs.getRootBattle(battle),
				INFILTRATION1_PHOTO_NAME);
		File infiltration2 = new File(fs.getRootBattle(battle),
				INFILTRATION2_PHOTO_NAME);
		File scoot1 = new File(fs.getRootBattle(battle), SCOOT1_PHOTO_NAME);
		File scoot2 = new File(fs.getRootBattle(battle), SCOOT2_PHOTO_NAME);

		setPicPhoto(table, this.tablePhotoView, "Table");
		setPicPhoto(deployment1, this.deployment1Photo, "Déploiement");
		setPicPhoto(deployment2, this.deployment2Photo, "Déploiement");
		setPicPhoto(infiltration1, this.infiltrationPhotoPlayer1,
				"Infiltration");
		setPicPhoto(infiltration2, this.infiltrationPhotoPlayer2,
				"Infiltration");
		setPicPhoto(scoot1, this.scootPhotoPlayer1, "Mouvement scoot");
		setPicPhoto(scoot2, this.scootPhotoPlayer2, "Mouvement scoot");

	}

	private void setPicPhoto(File photo, ImageView into, String title) {
		if (photo.exists() && into != null) {
			into.setOnClickListener(new ZoomImageListener(this.getActivity(),
					photo, title));
			ImageHelper.setPicAsync(this.getActivity(), photo.getAbsolutePath()
					+ THUMB_EXTENSION, into);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode >= TAKE_PHOTO_TABLE_RESULT_CODE
				&& requestCode <= TAKE_PHOTO_SCOOT2_RESULT_CODE) {
			this.createThumbnails(requestCode);
		}

	}

	public void createThumbnails(int photo) {
		switch (photo) {
		case TAKE_PHOTO_TABLE_RESULT_CODE:
			File table = new File(fs.getRootBattle(battle), TABLE_PHOTO_NAME);
			ImageHelper.createThumbnail(table.getAbsolutePath());
			break;

		case TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE:
			File deployment1 = new File(fs.getRootBattle(battle),
					DEPLOYMENT1_PHOTO_NAME);
			ImageHelper.createThumbnail(deployment1.getAbsolutePath());
			break;

		case TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE:
			File deployment2 = new File(fs.getRootBattle(battle),
					DEPLOYMENT2_PHOTO_NAME);
			ImageHelper.createThumbnail(deployment2.getAbsolutePath());
			break;

		case TAKE_PHOTO_INFILTRATION1_RESULT_CODE:
			File infiltration1 = new File(fs.getRootBattle(battle),
					INFILTRATION1_PHOTO_NAME);
			ImageHelper.createThumbnail(infiltration1.getAbsolutePath());
			break;

		case TAKE_PHOTO_INFILTRATION2_RESULT_CODE:
			File infiltration2 = new File(fs.getRootBattle(battle),
					INFILTRATION2_PHOTO_NAME);
			ImageHelper.createThumbnail(infiltration2.getAbsolutePath());
			break;

		case TAKE_PHOTO_SCOOT1_RESULT_CODE:
			File scoot1 = new File(fs.getRootBattle(battle), SCOOT1_PHOTO_NAME);
			ImageHelper.createThumbnail(scoot1.getAbsolutePath());
			break;

		case TAKE_PHOTO_SCOOT2_RESULT_CODE:
			File scoot2 = new File(fs.getRootBattle(battle), SCOOT2_PHOTO_NAME);
			ImageHelper.createThumbnail(scoot2.getAbsolutePath());
			break;
		}
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
	}

	public int getFirstPlayer() {

		if (this.whoStart != null) {
			return this.whoStart.getSelectedItemPosition();
		}

		return 0;
	}
}
