package com.headbangers.reportmaker;

import roboguice.activity.RoboFragmentActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.headbangers.reportmaker.fragment.ConfigurePlayerFragment;

public class ConfigureNewBattleActivity extends RoboFragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "configure_new_battle_selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_new_battle);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
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
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {

		Fragment fragment = new ConfigurePlayerFragment();

		Bundle args = new Bundle();
		args.putInt(ConfigurePlayerFragment.ARG_NUM, tab.getPosition() + 1);
		fragment.setArguments(args);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}

}
