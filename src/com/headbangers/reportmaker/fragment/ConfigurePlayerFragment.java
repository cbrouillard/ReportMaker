package com.headbangers.reportmaker.fragment;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.headbangers.reportmaker.R;

public class ConfigurePlayerFragment extends RoboFragment {

	public static final String ARG_NUM = "num";

	@InjectView(R.id.action)
	private Button action;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// getArguments().getInt(ARG_NUM);
		return inflater.inflate(R.layout.configure_one_player_fragment,
				container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

}
