package com.headbangers.reportmaker;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.fragment.ConfigureGameFragment;
import com.headbangers.reportmaker.fragment.ConfigurePlayerFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.tools.AdsControl;

public class ConfigureBattleActivity extends SherlockFragmentActivity implements
		TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "configure_new_battle_selected_navigation_item";
	public static final String BATTLE_ID_ARG = "battle_id";

	private ConfigurePlayerFragment playerOneFragment = new ConfigurePlayerFragment(
			1);
	private ConfigurePlayerFragment playerTwoFragment = new ConfigurePlayerFragment(
			2);
	private ConfigureGameFragment gameFragment = new ConfigureGameFragment();

	private BattleDao battleDao = new BattleDaoImpl(this);

	private FilesystemService filesystemService = new FilesystemService();

	private Long battleId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_new_battle);

		battleDao.open();

		this.battleId = this.getIntent().getExtras().getLong(BATTLE_ID_ARG);
		if (battleId != null) {
			// C'est une édition de la configuration d'une bataille existante
			// Remplissage des champs
			fillFields();
			this.setTitle(R.string.configure_battle);
		}

		final com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.game)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.player_one)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.player_two)
				.setTabListener(this));

		// Look up the AdView as a resource and load a request.
		AdsControl.buildAdIfEnable(this);
	}

	private void fillFields() {
		Battle battle = this.battleDao.findBattleById(battleId);

		playerOneFragment.setPlayer(battle.getOne());
		playerTwoFragment.setPlayer(battle.getTwo());
		gameFragment.setBattle(battle);

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		battleDao.open();

		AdsControl.buildAdIfEnable(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		battleDao.close();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater()
				.inflate(R.menu.menu_configure_new_battle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_done:

			saveOrUpdateGame();

			return true;
		}

		return false;
	}

	private void saveOrUpdateGame() {
		Player one = this.playerOneFragment.getPlayer();
		Player two = this.playerTwoFragment.getPlayer();
		Battle game = this.gameFragment.buildGame();

		game.setOne(one);
		game.setTwo(two);

		if (this.battleId != null) {
			game.setId(this.battleId);
			battleDao.updateBattleConfiguration(game);

			// TODO proposer un toast avec des actions. EDITER LA BATAILLE |
			// LISTE DES BATAILLES
			Toast.makeText(
					this,
					getResources()
							.getString(R.string.update_configuration_done),
					Toast.LENGTH_LONG).show();

		} else {
			Long idInserted = battleDao.createBattle(game);

			if (idInserted == -1) {
				Toast.makeText(this,
						getResources().getString(R.string.creation_error),
						Toast.LENGTH_LONG).show();
			} else {

				// Création du filesystem sur le téléphone.
				filesystemService.createRootBattle(idInserted);

				// Et redirection vers l'activité d'édition
				Intent editBattle = new Intent(this, EditBattleActivity.class);
				editBattle.putExtra(EditBattleActivity.BATTLE_ID_ARG,
						idInserted);
				startActivity(editBattle);

			}
		}

	}

	@Override
	public void onTabSelected(com.actionbarsherlock.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		Fragment fragment = null;
		String tag = null;

		switch (tab.getPosition()) {
		case 0:
			// Configuration partie
			fragment = gameFragment;
			tag = "gameFragment";
			break;
		case 1:
			fragment = playerOneFragment;
			tag = "playerOneFragment";
			break;
		case 2:
			fragment = playerTwoFragment;
			tag = "playerTwoFragment";
			break;
		}

		if (fragment != null) {
			Fragment inCacheFragment = getSupportFragmentManager()
					.findFragmentByTag(tag);

			if (inCacheFragment != null) {
				fragment = inCacheFragment;
			}

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

	}

	@Override
	public void onTabUnselected(com.actionbarsherlock.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(com.actionbarsherlock.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

	}

}
