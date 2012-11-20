package com.headbangers.reportmaker.dao;

import android.database.Cursor;

public interface BattleDao {

	/**
	 * Retourne toutes les batailles enregistrées en base.
	 * 
	 * @return
	 */
	Cursor getAllBattles();

}
