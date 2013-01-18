package com.headbangers.reportmaker.dao;

import java.util.List;

import android.database.Cursor;

import com.headbangers.reportmaker.dao.impl.GenericDao;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.pojo.Turn;

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

	/**
	 * Enregistre les données d'une bataille.
	 * 
	 * @param battle
	 *            la bataille
	 * @param infos
	 *            les informations générales
	 * @param allTurnsInfos
	 *            la liste des infos sur tous les tours.
	 */
	void updateBattle(Battle battle, Informations infos,
			List<Turn> allTurnsInfos);

	/**
	 * Met à jour les données de config de la bataille.
	 * 
	 * @param battle
	 *            la bataille à modifier
	 */
	void updateBattleConfiguration(Battle battle);

	/**
	 * Efface une bataille en base.
	 * 
	 * @param id
	 *            id de la bataille à supprimer.
	 */
	void deleteBattle(Long id);
}
