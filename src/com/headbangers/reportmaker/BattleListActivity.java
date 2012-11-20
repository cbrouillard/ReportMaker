package com.headbangers.reportmaker;

import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.headbangers.reportmaker.adapter.BattleListAdapter;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;

/**
 * Affiche une liste contenant toutes les batailles ainsi qu'un bouton
 * permettant de lancer la configuration d'une nouvelle.
 * 
 * @author cbrouillard
 */
public class BattleListActivity extends RoboListActivity {

	@InjectView(android.R.id.list)
	private ListView battleList;

	// TODO inject
	private BattleDao battleDao = new BattleDaoImpl(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle_list);

		// Remplissage de la liste.
		fillList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillList();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Un click sur un élément de la liste permet de lancer l'activité
		// d'édition
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_battle_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_newBattle:

			// Lancement de l'activité de configuration d'une nouvelle partie
			startActivity(new Intent(this, ConfigureNewBattleActivity.class));

			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private void fillList() {
		Cursor cursor = battleDao.getAllBattles();
		setListAdapter(new BattleListAdapter(this, cursor));
	}

}
