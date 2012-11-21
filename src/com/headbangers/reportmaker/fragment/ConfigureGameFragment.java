package com.headbangers.reportmaker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.headbangers.reportmaker.R;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ConfigureGameFragment extends RoboFragment {

	@InjectView(R.id.action)
	private Button action;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.configure_game_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

}
