package com.headbangers.reportmaker;

import android.annotation.SuppressLint;
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

@SuppressLint("InlinedApi")
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

	private FilesystemService filesystemService = FilesystemService
			.getInstance();

	private Long battleId = null;
	private Battle battle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_new_battle);

		battleDao.open();

		this.battleId = this.getIntent().getExtras() != null ? this.getIntent()
				.getExtras().getLong(BATTLE_ID_ARG) : null;
		if (battleId != null) {
			// C'est une édition de la configuration d'une bataille existante
			// Remplissage des champs
			fillFields();
			this.setTitle(R.string.configure_battle);
		} else {
			this.battle = null;
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
		this.battle = this.battleDao.findBattleById(battleId);

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

		if (this.battleId != null) {
			Battle game = this.battle; // Dao.findBattleById(this.battleId);

			Player one = this.playerOneFragment.getPlayerButNoDefaultValue();
			Player two = this.playerTwoFragment.getPlayerButNoDefaultValue();
			Battle fromInterface = this.gameFragment.buildGame();

			// un peu pourri cette affaire (c'est pour éviter de perdre des
			// données a partir du moment ou le fragment n'a pas été affiché (et
			// initialisé)
			if (one.getName() != null) {
				game.getPlayer(0).setName(one.getName());
			}
			if (one.getRace() != null) {
				game.getPlayer(0).setRace(one.getRace());
			}
			if (one.getArmyComments() != null) {
				game.getPlayer(0).setArmyComments(one.getArmyComments());
			}
			if (two.getName() != null) {
				game.getPlayer(1).setName(two.getName());
			}
			if (two.getRace() != null) {
				game.getPlayer(1).setRace(two.getRace());
			}
			if (two.getArmyComments() != null) {
				game.getPlayer(1).setArmyComments(two.getArmyComments());
			}
			// FIN du hack pourri.

			game.setName(fromInterface.getName());
			game.setFormat(fromInterface.getFormat());
			game.setDate(fromInterface.getDate());
			game.setGameType(fromInterface.getGameType());

			battleDao.updateBattleConfiguration(game);

			// TODO proposer un toast avec des actions. EDITER LA BATAILLE |
			// LISTE DES BATAILLES
			Toast.makeText(
					this,
					getResources()
							.getString(R.string.update_configuration_done),
					Toast.LENGTH_LONG).show();

			this.finish();

		} else {
			Player one = this.playerOneFragment.getPlayer();
			Player two = this.playerTwoFragment.getPlayer();
			Battle game = this.gameFragment.buildGame();

			game.setOne(one);
			game.setTwo(two);

			Long idInserted = battleDao.createBattle(game);

			if (idInserted == -1) {
				Toast.makeText(this,
						getResources().getString(R.string.creation_error),
						Toast.LENGTH_LONG).show();
			} else {

				// Création du filesystem sur le téléphone.
				filesystemService.createRootBattle(idInserted);

				// Déplacement des photos temporaires dans le nouveau dossier
				filesystemService.moveTempPhotos(idInserted);

				// Et redirection vers l'activité d'édition
				Intent editBattle = new Intent(this, EditBattleActivity.class);
				// TODO Flag pour nettoyer l'historique en cours
				editBattle.putExtra(EditBattleActivity.BATTLE_ID_ARG,
						idInserted);
				startActivity(editBattle);

				this.finish();

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

	public Battle getBattle() {
		if (this.battle != null) {
			return this.battle;
		} else {
			return this.battleDao.findBattleById(battleId);
		}
	}

}
