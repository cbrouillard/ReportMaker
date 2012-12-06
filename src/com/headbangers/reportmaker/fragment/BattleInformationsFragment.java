package com.headbangers.reportmaker.fragment;

import java.io.File;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
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

import com.headbangers.reportmaker.ImageHelper;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.listener.ZoomImageListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.service.FilesystemService;

public class BattleInformationsFragment extends RoboFragment {

	private static final int TAKE_PHOTO_TABLE_RESULT_CODE = 1;
	private static final int TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE = 2;
	private static final int TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE = 3;
	private static final int TAKE_PHOTO_INFILTRATION1_RESULT_CODE = 4;
	private static final int TAKE_PHOTO_INFILTRATION2_RESULT_CODE = 5;
	private static final int TAKE_PHOTO_SCOOT1_RESULT_CODE = 6;
	private static final int TAKE_PHOTO_SCOOT2_RESULT_CODE = 7;

	public static final String TABLE_PHOTO_NAME = "table.jpg";
	public static final String DEPLOYMENT1_PHOTO_NAME = "deploiement_j1.jpg";
	public static final String DEPLOYMENT2_PHOTO_NAME = "deploiement_j2.jpg";
	public static final String INFILTRATION1_PHOTO_NAME = "infiltration_j1.jpg";
	public static final String INFILTRATION2_PHOTO_NAME = "infiltration_j2.jpg";
	public static final String SCOOT1_PHOTO_NAME = "scoot_j1.jpg";
	public static final String SCOOT2_PHOTO_NAME = "scoot_j2.jpg";

	private FilesystemService fs = new FilesystemService();
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

	@InjectView(R.id.comments)
	private EditText comments;

	@InjectView(R.id.infiltrationPlayer1)
	private TextView infiltrationPlayer1;
	@InjectView(R.id.takePhoto_infiltration1)
	private ImageButton infiltrationTakePhotoPlayer1;
	@InjectView(R.id.takePhoto_infiltration1_photo)
	private ImageView infiltrationPhotoPlayer1;

	@InjectView(R.id.infiltrationPlayer2)
	private TextView infiltrationPlayer2;
	@InjectView(R.id.takePhoto_infiltration2)
	private ImageButton infiltrationTakePhotoPlayer2;
	@InjectView(R.id.takePhoto_infiltration2_photo)
	private ImageView infiltrationPhotoPlayer2;

	@InjectView(R.id.scootPlayer1)
	private TextView scootPlayer1;
	@InjectView(R.id.takePhoto_scoot1)
	private ImageButton scootTakePhotoPlayer1;
	@InjectView(R.id.takePhoto_scoot1_photo)
	private ImageView scootPhotoPlayer1;

	@InjectView(R.id.scootPlayer2)
	private TextView scootPlayer2;
	@InjectView(R.id.takePhoto_scoot2)
	private ImageButton scootTakePhotoPlayer2;
	@InjectView(R.id.takePhoto_scoot2_photo)
	private ImageView scootPhotoPlayer2;

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
				.getActivity(), this.battle, DEPLOYMENT1_PHOTO_NAME,
				TAKE_PHOTO_DEPLOYMENT1_RESULT_CODE));

		this.takePhotoDeployment2.setOnClickListener(new TakePhotoListener(this
				.getActivity(), this.battle, DEPLOYMENT2_PHOTO_NAME,
				TAKE_PHOTO_DEPLOYMENT2_RESULT_CODE));

		this.infiltrationTakePhotoPlayer1
				.setOnClickListener(new TakePhotoListener(this.getActivity(),
						this.battle, INFILTRATION1_PHOTO_NAME,
						TAKE_PHOTO_INFILTRATION1_RESULT_CODE));
		this.infiltrationTakePhotoPlayer2
				.setOnClickListener(new TakePhotoListener(this.getActivity(),
						this.battle, INFILTRATION2_PHOTO_NAME,
						TAKE_PHOTO_INFILTRATION2_RESULT_CODE));

		this.scootTakePhotoPlayer1.setOnClickListener(new TakePhotoListener(
				this.getActivity(), this.battle, SCOOT1_PHOTO_NAME,
				TAKE_PHOTO_SCOOT1_RESULT_CODE));
		this.scootTakePhotoPlayer2.setOnClickListener(new TakePhotoListener(
				this.getActivity(), this.battle, SCOOT2_PHOTO_NAME,
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

		File imageFile = new File(fs.getRootBattle(battle), TABLE_PHOTO_NAME);
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

		setPicPhoto(imageFile, this.tablePhotoView, "Table");
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
			ImageHelper.setPicAsync(this.getActivity(),
					photo.getAbsolutePath(), into);
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
