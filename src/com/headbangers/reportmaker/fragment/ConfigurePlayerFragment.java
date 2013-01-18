package com.headbangers.reportmaker.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Player;

@SuppressLint("ValidFragment")
public class ConfigurePlayerFragment extends SherlockFragment {

	private int num;

	private EditText playerName;
	private EditText playerRace;

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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.playerName = (EditText) view.findViewById(R.id.playerName);
		this.playerRace = (EditText) view.findViewById(R.id.playerRace);

		if (player != null) {
			this.playerName.setText(this.player.getName());
			if (this.player.getRace() != null
					&& !"".equals(this.player.getRace())) {
				this.playerRace.setText("" + this.player.getRace());
			}
		}
	}

	public Player getPlayer() {

		Player player = new Player();

		player.setName(playerName != null
				&& !"".equals(playerName.getText().toString()) ? playerName
				.getText().toString() : "Joueur " + num);
		player.setRace(playerRace != null
				&& !"".equals(playerRace.getText().toString()) ? playerRace
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

		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
