package com.headbangers.reportmaker;

import android.os.Bundle;
import roboguice.activity.RoboPreferenceActivity;

public class PreferencesActivity extends RoboPreferenceActivity {

	public static final int CODE_RESULT = 9999;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	enum ACTION_ON_QUIT {
		ALWAYS_ASK, ALWAYS_SAVE, NEVER_SAVE
	}

}
