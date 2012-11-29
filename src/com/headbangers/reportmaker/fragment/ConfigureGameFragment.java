package com.headbangers.reportmaker.fragment;

import java.util.Date;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.BattleNameGenerator;
import com.headbangers.reportmaker.widget.CustomDatePicker;

public class ConfigureGameFragment extends RoboFragment {

	@InjectView(R.id.gameName)
	private EditText gameName;

	@InjectView(R.id.gameFormat)
	private EditText gameFormat;

	@InjectView(R.id.gameDate)
	private CustomDatePicker gameDate;

	@InjectView(R.id.generateName)
	private ImageButton generateName;

	private BattleNameGenerator battleNameGenerator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.configure_game_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.battleNameGenerator = new BattleNameGenerator(this.getActivity());
		this.generateName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gameName.setText(battleNameGenerator.generateCoolRandomName());

			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@SuppressWarnings("deprecation")
	public Battle buildGame() {
		Battle game = new Battle();

		String name = gameName.getText() != null ? gameName.getText()
				.toString() : null;

		String format = gameFormat != null ? gameFormat.getText().toString()
				: null;
		if (name == null || name.isEmpty()) {
			name = this.getActivity().getResources().getString(R.string.battle);
		}

		game.setName(name);
		game.setFormat(format != null && !format.isEmpty() ? Integer
				.valueOf(format) : null);
		game.setDate(new Date(gameDate.getYear() - 1900, gameDate.getMonth(),
				gameDate.getDayOfMonth()));

		return game;
	}

}
