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

public class BattleListAdapter extends SimpleCursorAdapter {

	private Activity context;

	public BattleListAdapter(Activity context, Cursor cursor) {
		super(context, R.layout.one_battle, cursor, DatabaseHelper.ALL_COLUMNS,
				null, 0);
		this.context = context;
	}

	@Override
	public void bindView(View row, Context ctx, Cursor cursor) {

		TextView label = (TextView) row.findViewById(R.id.battleName);
		TextView disclaimer = (TextView) row.findViewById(R.id.battleInfos);
		ImageView icon = (ImageView) row.findViewById(R.id.battleIcon);

		label.setText(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_NAME)));
		disclaimer.setText("informations date ou autre");

		icon.setImageResource(R.drawable.ic_launcher);
	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup group) {

		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.one_battle, null);

		bindView(row, ctx, cursor);

		return row;
	}
}
