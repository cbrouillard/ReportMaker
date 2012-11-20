package com.headbangers.reportmaker.fragment;

import com.headbangers.reportmaker.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ConfigurePlayerFragment extends RoboFragment {

	@InjectView(android.R.id.list)
	private ListView test;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.battle_list, container, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		test.animate();
	}

}
