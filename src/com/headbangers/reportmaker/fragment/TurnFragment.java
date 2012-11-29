package com.headbangers.reportmaker.fragment;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.headbangers.reportmaker.EditBattleActivity;
import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;

public class TurnFragment extends RoboFragment {

	private Battle battle = null;

	@InjectView(R.id.tabhost)
	private TabHost tabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.turn_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.tabHost.setup();

		TabSpec tab1 = tabHost.newTabSpec("1");
		tab1.setIndicator(this.battle.getOne().getName());
		tab1.setContent(R.id.playerOneTurn);
		this.tabHost.addTab(tab1);

		TabSpec tab2 = tabHost.newTabSpec("2");
		tab2.setIndicator(this.battle.getTwo().getName());
		tab2.setContent(R.id.playerTwoTurn);
		this.tabHost.addTab(tab2);

		tabHost.setCurrentTab(((EditBattleActivity) this.getActivity())
				.getFirstPlayer());
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}
}
