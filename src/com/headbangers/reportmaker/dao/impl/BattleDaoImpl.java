package com.headbangers.reportmaker.dao.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.DatabaseHelper;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.pojo.Turn;

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
		values.put(DatabaseHelper.COL_LISTONE, one.getArmyComments());
		values.put(DatabaseHelper.COL_PLAYERTWO, two.getName());
		values.put(DatabaseHelper.COL_RACETWO, two.getRace());
		values.put(DatabaseHelper.COL_LISTTWO, two.getArmyComments());
		
		return db.insert(DatabaseHelper.TABLE_BATTLE,
				DatabaseHelper.COL_PLAYERONE, values);
	}

	@Override
	public Battle findBattleById(Long id) {
		if (id == null) {
			return null;
		}

		Cursor cursor = db.query(DatabaseHelper.TABLE_BATTLE,
				DatabaseHelper.ALL_BATTLE_COLUMNS, DatabaseHelper.COL_ID
						+ " = " + id, null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			Battle battle = new Battle(cursor);
			cursor.close();

			// Requête des tours
			Cursor turnsCursor = db.query(DatabaseHelper.TABLE_TURN,
					DatabaseHelper.ALL_TURN_COLUMNS,
					DatabaseHelper.COL_BATTLE_ID + " = " + id, null, null,
					null, DatabaseHelper.COL_NUM + " ASC");

			battle.setTurns(buildTurns(turnsCursor));
			turnsCursor.close();

			return battle;
		}

		return null;
	}

	private List<Turn> buildTurns(Cursor cursor) {
		List<Turn> turns = new ArrayList<Turn>();

		if (cursor != null) {

			while (cursor.moveToNext()) {
				turns.add(new Turn(cursor));
			}

		}

		return turns;

	}

	@Override
	public void updateBattleConfiguration(Battle battle) {
		Log.d("BattleDaoImpl", "Mise à jour des infos de la table BATTLE");
		db.update(DatabaseHelper.TABLE_BATTLE, battle.asContentValues(),
				DatabaseHelper.COL_ID + " = " + battle.getId(), null);
	}

	@Override
	public void updateBattle(Battle battle, Informations infos, List<Turn> turns) {

		Log.d("BattleDaoImpl", "Mise à jour des infos de la table BATTLE");
		db.update(DatabaseHelper.TABLE_BATTLE, infos.asContentValues(),
				DatabaseHelper.COL_ID + " = " + battle.getId(), null);

		Log.d("BattleDaoImpl", "Mise à jour des infos de la table TURN");
		for (Turn turn : turns) {

			// Existe t-il ?
			Cursor testCursor = db.query(
					DatabaseHelper.TABLE_TURN,
					new String[] { DatabaseHelper.COL_ID },
					DatabaseHelper.COL_BATTLE_ID + " = " + battle.getId()
							+ " AND " + DatabaseHelper.COL_NUM + " = "
							+ turn.getNum(), null, null, null, null);

			if (testCursor.getCount() > 0) {

				db.update(DatabaseHelper.TABLE_TURN, turn.asContentValues(),
						DatabaseHelper.COL_BATTLE_ID + " = " + battle.getId()
								+ " AND " + DatabaseHelper.COL_NUM + " = "
								+ turn.getNum(), null);
			} else {

				ContentValues values = turn.asContentValues();
				values.put(DatabaseHelper.COL_BATTLE_ID, battle.getId());

				db.insert(DatabaseHelper.TABLE_TURN, null, values);
			}

			testCursor.close();
		}

	}

	@Override
	public void deleteBattle(Long id) {

		db.delete(DatabaseHelper.TABLE_BATTLE, DatabaseHelper.COL_ID + " = "
				+ id, null);
	}
}
