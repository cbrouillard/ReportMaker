package com.headbangers.reportmaker.fragment;

import java.util.Date;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;

public class ConfigureGameFragment extends RoboFragment {

	// @InjectView(R.id.action)
	// private Button action;

	@InjectView(R.id.gameName)
	private EditText gameName;

	@InjectView(R.id.gameFormat)
	private EditText gameFormat;

	@InjectView(R.id.gameDate)
	private DatePicker gameDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.configure_game_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@SuppressWarnings("deprecation")
	public Battle buildGame() {
		Battle game = new Battle();

		String name = gameName.getText() != null ? gameName.getText()
				.toString() : null;

		String format = gameFormat != null ? gameFormat.getText().toString()
				: null;
		if (name == null || name.isEmpty()) {
			name = "Battle";
		}

		game.setName(name);
		game.setFormat(format != null && !format.isEmpty() ? Integer
				.valueOf(format) : null);
		game.setDate(new Date(gameDate.getYear() - 1900, gameDate.getMonth(),
				gameDate.getDayOfMonth()));

		return game;
	}

}
