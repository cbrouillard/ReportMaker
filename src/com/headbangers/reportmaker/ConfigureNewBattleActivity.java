package com.headbangers.reportmaker;

import java.io.File;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.fragment.ConfigureGameFragment;
import com.headbangers.reportmaker.fragment.ConfigurePlayerFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.service.FilesystemService;

public class ConfigureNewBattleActivity extends RoboFragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "configure_new_battle_selected_navigation_item";

	private ConfigurePlayerFragment playerOneFragment = new ConfigurePlayerFragment();
	private ConfigurePlayerFragment playerTwoFragment = new ConfigurePlayerFragment();
	private ConfigureGameFragment gameFragment = new ConfigureGameFragment();

	private BattleDao battleDao = new BattleDaoImpl(this);

	private FilesystemService filesystemService = new FilesystemService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_new_battle);

		battleDao.open();

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.game)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.player_one)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.player_two)
				.setTabListener(this));
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		battleDao.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		battleDao.close();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_configure_new_battle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_done:

			createGame();

			return true;
		}

		return false;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {

		Fragment fragment = null;
		Bundle args = new Bundle();

		switch (tab.getPosition()) {
		case 0:
			// Configuration partie
			fragment = gameFragment;
			break;
		case 1:
			fragment = playerOneFragment;
			break;
		case 2:
			fragment = playerTwoFragment;
			break;
		}

		if (fragment != null) {

			args.putInt(ConfigurePlayerFragment.ARG_NUM, tab.getPosition());
			fragment.setArguments(args);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}

	private void createGame() {
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
			File battleDir = filesystemService.createRootBattle(idInserted);

			// Et redirection vers l'activité d'édition
			Intent editBattle = new Intent(this, EditBattleActivity.class);
			editBattle.putExtra(EditBattleActivity.BATTLE_ID_ARG, idInserted);
			startActivity(editBattle);
		}

	}

}
