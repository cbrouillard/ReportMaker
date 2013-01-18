package com.headbangers.reportmaker.fragment;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.BattleNameGenerator;
import com.headbangers.reportmaker.widget.CustomDatePicker;

public class ConfigureGameFragment extends SherlockFragment {

	private EditText gameName;
	private EditText gameFormat;
	private CustomDatePicker gameDate;
	private ImageButton generateName;

	private BattleNameGenerator battleNameGenerator;

	private Battle battle = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.configure_game_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.gameName = (EditText) view.findViewById(R.id.gameName);
		this.gameFormat = (EditText) view.findViewById(R.id.gameFormat);
		this.gameDate = (CustomDatePicker) view.findViewById(R.id.gameDate);
		this.generateName = (ImageButton) view.findViewById(R.id.generateName);

		this.battleNameGenerator = new BattleNameGenerator(this.getActivity());
		this.generateName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gameName.setText(battleNameGenerator.generateCoolRandomName());

			}
		});

		if (battle != null) {
			this.gameName.setText("" + battle.getName());
			this.gameFormat.setText("" + battle.getFormat());

			final Calendar c = Calendar.getInstance();
			c.setTime(battle.getDate());
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			this.gameDate.init(year, month, day, null);

		}

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
		if (name == null || "".equals(name)) {
			name = this.getActivity().getResources().getString(R.string.battle);
		}

		game.setName(name);
		game.setFormat(format != null && !"".equals(format) ? Integer
				.valueOf(format) : null);
		game.setDate(new Date(gameDate.getYear() - 1900, gameDate.getMonth(),
				gameDate.getDayOfMonth()));

		return game;
	}

	public void setBattle(Battle battle) {
		this.battle = battle;

	}

}
