package com.headbangers.reportmaker.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.ConfigureBattleActivity;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.listener.TakePhotoListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.tools.ImageHelper;

@SuppressLint("ValidFragment")
public class ConfigurePlayerFragment extends SherlockFragment {

	public static final String ARMY_PHOTO_NAME = "player{P}_army.jpg";

	private int num;

	private EditText playerName;
	private EditText playerRace;

	private LinearLayout takeArmyPhotos;
	private ImageButton camera;
	private EditText comments;

	private Player player = null;

	public ConfigurePlayerFragment() {
	}

	public ConfigurePlayerFragment(int num) {
		this.num = num;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.configure_one_player_fragment,
				container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		Battle battle = ((ConfigureBattleActivity) this.getActivity())
				.getBattle();
		
		ImageHelper.setPicPhotos(this.getActivity(), battle,
				ARMY_PHOTO_NAME.replace("{P}", "" + num), this.takeArmyPhotos,
				"Armée ");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Battle battle = ((ConfigureBattleActivity) this.getActivity())
				.getBattle();

		this.playerName = (EditText) view.findViewById(R.id.playerName);
		this.playerRace = (EditText) view.findViewById(R.id.playerRace);
		this.takeArmyPhotos = (LinearLayout) view
				.findViewById(R.id.takePhotos_army_photo);
		this.camera = (ImageButton) view.findViewById(R.id.takePhoto_army);
		this.comments = (EditText) view.findViewById(R.id.armyList);

		if (player != null) {
			this.playerName.setText(this.player.getName());
			if (this.player.getRace() != null
					&& !"".equals(this.player.getRace())) {
				this.playerRace.setText("" + this.player.getRace());
			}

			if (this.player.getArmyComments() != null
					&& !"".equals(this.player.getArmyComments())) {
				this.comments.setText(this.player.getArmyComments());
			}
		}

		ImageHelper.setPicPhotos(this.getActivity(), battle,
				ARMY_PHOTO_NAME.replace("{P}", "" + num), this.takeArmyPhotos,
				"Armée ");

		this.camera.setOnClickListener(new TakePhotoListener(this, battle,
				ARMY_PHOTO_NAME.replace("{P}", "" + num), 1234));
	}
	
	

	public Player getPlayer() {

		Player player = new Player();

		player.setName(playerName != null
				&& !"".equals(playerName.getText().toString()) ? playerName
				.getText().toString() : "Joueur " + num);
		player.setRace(playerRace != null
				&& !"".equals(playerRace.getText().toString()) ? playerRace
				.getText().toString() : null);
		player.setArmyComments(comments != null
				&& !"".equals(comments.getText().toString()) ? comments
				.getText().toString() : null);

		return player;
	}

	public Player getPlayerButNoDefaultValue() {

		Player player = new Player();

		player.setName(playerName != null
				&& !"".equals(playerName.getText().toString()) ? playerName
				.getText().toString() : null);
		player.setRace(playerRace != null
				&& !"".equals(playerRace.getText().toString()) ? playerRace
				.getText().toString() : null);

		player.setArmyComments(comments != null
				&& !"".equals(comments.getText().toString()) ? comments
				.getText().toString() : null);

		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
