package com.headbangers.reportmaker.dao.impl;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.DatabaseHelper;

public class BattleDaoImpl implements BattleDao {

	private DatabaseHelper bdd;

	public BattleDaoImpl(Activity context) {
		this.bdd = new DatabaseHelper(context);
	}

	@Override
	public Cursor getAllBattles() {

		SQLiteDatabase db = bdd.getWritableDatabase();

		return db.rawQuery("select id as _id,* from "
				+ DatabaseHelper.TABLE_BATTLE + ";", null);

	}
}
