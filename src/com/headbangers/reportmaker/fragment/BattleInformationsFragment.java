package com.headbangers.reportmaker.fragment;

import java.io.File;
import java.io.IOException;

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

	private static final String TABLE_PHOTO_NAME = "table.jpg";
	private static final String DEPLOYMENT1_PHOTO_NAME = "deploiement_j1.jpg";
	private static final String DEPLOYMENT2_PHOTO_NAME = "deploiement_j2.jpg";

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

	@InjectView(R.id.comments)
	private EditText comments;

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

		fillView();
	}

	private void fillPhotosIfNeeded() {

		File imageFile = new File(filesystemService.getRootBattle(battle),
				TABLE_PHOTO_NAME);
		File deployment1 = new File(filesystemService.getRootBattle(battle),
				DEPLOYMENT1_PHOTO_NAME);
		File deployment2 = new File(filesystemService.getRootBattle(battle),
				DEPLOYMENT2_PHOTO_NAME);

		if (imageFile.exists() && this.tablePhotoView != null) {
			this.tablePhotoView.setOnClickListener(new ZoomImageListener(this
					.getActivity(), imageFile, "Table"));
			try {
				ImageHelper.setPic(imageFile.getAbsolutePath(),
						this.tablePhotoView);
			} catch (IOException e) {
			}
		}

		if (deployment1.exists() && this.deployment1Photo != null) {
			this.deployment1Photo.setOnClickListener(new ZoomImageListener(this
					.getActivity(), deployment1, "Déploiement"));
			try {
				ImageHelper.setPic(deployment1.getAbsolutePath(),
						this.deployment1Photo);
			} catch (IOException e) {
			}
		}

		if (deployment2.exists() && this.deployment2Photo != null) {
			this.deployment2Photo.setOnClickListener(new ZoomImageListener(this
					.getActivity(), deployment2, "Déploiement"));
			try {
				ImageHelper.setPic(deployment2.getAbsolutePath(),
						this.deployment2Photo);
			} catch (IOException e) {
			}
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
}
