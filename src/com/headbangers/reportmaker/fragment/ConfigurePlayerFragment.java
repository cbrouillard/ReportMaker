package com.headbangers.reportmaker.fragment;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Player;

public class ConfigurePlayerFragment extends RoboFragment {

	public static final String ARG_NUM = "num";

	@InjectView(R.id.action)
	private Button action;

	@InjectView(R.id.playerName)
	private EditText playerName;

	@InjectView(R.id.playerRace)
	private EditText playerRace;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getArguments().getInt(ARG_NUM);
		return inflater.inflate(R.layout.configure_one_player_fragment,
				container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	public Player getPlayer() {

		Player player = new Player();

		player.setName(playerName.getText().toString());
		player.setRace(playerRace.getText().toString());

		return player;
	}
}
