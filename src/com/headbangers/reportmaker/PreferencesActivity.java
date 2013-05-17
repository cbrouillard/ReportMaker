package com.headbangers.reportmaker;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
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

		// Authentification
		Intent authIntent = new Intent(this, AuthActivity.class);
		authIntent.putExtra("forceMode", true);

		Preference authLink = (Preference) findPreference("gotoAuth");
		authLink.setIntent(authIntent);
	}

	enum ACTION_ON_QUIT {
		ALWAYS_ASK, ALWAYS_SAVE, NEVER_SAVE
	}

}
