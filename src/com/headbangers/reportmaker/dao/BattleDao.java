package com.headbangers.reportmaker.dao;

import android.database.Cursor;

import com.headbangers.reportmaker.dao.impl.GenericDao;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Player;

public interface BattleDao extends GenericDao {

	/**
	 * Retourne toutes les batailles enregistrées en base.
	 * 
	 * @return
	 */
	Cursor getAllBattles();

	
	/**
	 * Enregistre une nouvelle bataille dans la base de données.
	 * 
	 * @param battle les données de bataille
	 * @param one le joueur 1
	 * @param two le joueur 2
	 * @return un boolean indiquant si tout s'est bien passé
	 */
	Long createBattle (Battle battle, Player one, Player two);
}
