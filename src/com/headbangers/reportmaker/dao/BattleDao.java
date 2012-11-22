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
	 * @param battle
	 *            les données de bataille
	 * @return l'id de l'objet inséré. -1 si erreur
	 */
	Long createBattle(Battle battle);

	/**
	 * Récupère une partie avec son id base.
	 * 
	 * @param id
	 *            l'id de la bataille.
	 * @return une bataille ou null si non existant.
	 */
	Battle findBattleById(Long id);
}
