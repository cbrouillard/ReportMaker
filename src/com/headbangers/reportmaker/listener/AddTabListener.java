package com.headbangers.reportmaker.listener;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.headbangers.reportmaker.EditBattleActivity;

public class AddTabListener implements TabListener {

	private EditBattleActivity activity;
	private ActionBar actionBar;

	public AddTabListener(EditBattleActivity editBattleActivity,
			ActionBar actionBar) {

		this.actionBar = actionBar;
		this.activity = editBattleActivity;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		int nbTab = actionBar.getTabCount() - 1; // -1 "tab +"
		activity.addTurnFragment(nbTab);
		actionBar.addTab(actionBar.newTab().setText("T" + (nbTab))
				.setTabListener(this.activity), nbTab, true);
		actionBar.setSelectedNavigationItem(nbTab);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
