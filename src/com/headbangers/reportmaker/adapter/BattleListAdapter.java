package com.headbangers.reportmaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.dao.DatabaseHelper;
import com.headbangers.reportmaker.pojo.Battle;

public class BattleListAdapter extends SimpleCursorAdapter {

	private Activity context;

	public BattleListAdapter(Activity context, Cursor cursor) {
		super(context, R.layout.one_battle, cursor,
				DatabaseHelper.ALL_BATTLE_COLUMNS, null, 0);
		this.context = context;
	}

	@Override
	public void bindView(View row, Context ctx, Cursor cursor) {

		TextView label = (TextView) row.findViewById(R.id.battleName);
		TextView disclaimer = (TextView) row.findViewById(R.id.battleInfos);
		TextView disclaimer2 = (TextView) row
				.findViewById(R.id.battleInfosPlus);
		ImageView icon = (ImageView) row.findViewById(R.id.battleIcon);

		Battle game = new Battle(cursor);

		label.setText(game.getName());
		disclaimer.setText(buildInformationsText(game));

		CharSequence infosPlus = buildInfosPlusText(game);
		disclaimer2.setText(infosPlus);
		icon.setImageResource(R.drawable.planet);

	}

	private CharSequence buildInformationsText(Battle game) {

		StringBuilder builder = new StringBuilder();

		if (game.getFormat() != null && !"".equals(game.getFormat())) {
			builder.append(game.getFormat());
			try {
				Integer.parseInt(game.getFormat());
				builder.append(" points ");
			} catch (NumberFormatException e) {
				// pas un chiffre, l'utilisateur a peut-être mis un texte qui
				// lui est propre.
			}
		}

		if (game.getDateFormated() != null) {
			builder.append("[").append(game.getDateFormated()).append("] ");
		}

		return builder.toString();
	}

	private CharSequence buildInfosPlusText(Battle game) {
		StringBuilder builder = new StringBuilder();
		builder.append(game.getOne().getName());
		builder.append(" vs ").append(game.getTwo().getName());
		return builder.toString();
	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup group) {

		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.one_battle, null);

		bindView(row, ctx, cursor);

		return row;
	}
}
