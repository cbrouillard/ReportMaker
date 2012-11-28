package com.headbangers.reportmaker.dao.impl;

import java.text.DateFormat;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.DatabaseHelper;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.pojo.Player;

public class BattleDaoImpl extends GenericDaoImpl implements BattleDao {

	public BattleDaoImpl(Activity context) {
		this.bdd = new DatabaseHelper(context);
	}

	@Override
	public Cursor getAllBattles() {

		return db.rawQuery("select id as _id,* from "
				+ DatabaseHelper.TABLE_BATTLE + ";", null);

	}

	@Override
	public Long createBattle(Battle battle) {

		if (battle == null) {
			return -1L;
		}

		Player one = battle.getOne();
		Player two = battle.getTwo();

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_NAME, battle.getName());
		values.put(DatabaseHelper.COL_DATE, DateFormat.getDateInstance()
				.format(battle.getDate()));
		values.put(DatabaseHelper.COL_FORMAT, battle.getFormat());
		values.put(DatabaseHelper.COL_PLAYERONE, one.getName());
		values.put(DatabaseHelper.COL_RACEONE, one.getRace());
		values.put(DatabaseHelper.COL_PLAYERTWO, two.getName());
		values.put(DatabaseHelper.COL_RACETWO, two.getRace());

		return db.insert(DatabaseHelper.TABLE_BATTLE,
				DatabaseHelper.COL_PLAYERONE, values);
	}

	@Override
	public Battle findBattleById(Long id) {
		if (id == null) {
			return null;
		}

		Cursor cursor = db.query(DatabaseHelper.TABLE_BATTLE,
				DatabaseHelper.ALL_COLUMNS, DatabaseHelper.COL_ID + " = " + id,
				null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			Battle battle = new Battle(cursor);
			cursor.close();
			return battle;
		}

		return null;
	}

	@Override
	public void updateBattle(Battle battle, Informations infos) {

		db.update(DatabaseHelper.TABLE_BATTLE, infos.asContentValues(),
				DatabaseHelper.COL_ID + " = " + battle.getId(), null);

	}
}
