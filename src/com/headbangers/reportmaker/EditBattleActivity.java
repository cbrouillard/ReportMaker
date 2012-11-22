package com.headbangers.reportmaker;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class EditBattleActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "edit_battle_selected_navigation_item";
	public static final String BATTLE_ID_ARG = "battle_id";

	private Long battleId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_battle_activity);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText("•").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t3)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t4)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t5)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t6)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t7)
				.setTabListener(this));

		// Sauvegarde de l'id de la bataille
		this.battleId = this.getIntent().getExtras().getLong(BATTLE_ID_ARG);
		Log.d(this.getClass().getSimpleName(), "battleId = " + battleId);
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
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
