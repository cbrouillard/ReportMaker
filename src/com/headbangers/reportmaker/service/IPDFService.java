package com.headbangers.reportmaker.service;

import android.app.Activity;

import com.headbangers.reportmaker.dao.BattleDao;

public interface IPDFService {

	public void setDao(BattleDao dao);

	/**
	 * Exporte la bataille au format PDF.
	 * 
	 * @param battleId
	 *            id de la bataille séléctionnée. Elle sera rechargée depuis la
	 *            base afin d'être sur de la fraicheur des données.
	 * @return le nom du fichier pdf généré.
	 */
	public String exportBattle(Long battleId);

	public void exportBattleAsync(Long battleId, Activity fromContext);

}
