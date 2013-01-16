package com.headbangers.reportmaker;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputType;

public class PreferencesActivity extends PreferenceActivity {

	public static final int CODE_RESULT = 9999;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		EditTextPreference durationTimer = (EditTextPreference) findPreference("durationTimer");
		durationTimer.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	enum ACTION_ON_QUIT {
		ALWAYS_ASK, ALWAYS_SAVE, NEVER_SAVE
	}

}
