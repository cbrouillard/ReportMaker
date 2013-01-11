package com.headbangers.reportmaker.tools;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

public class ScreenHelper {

	public static void applyAlwaysSwitched(Activity context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean screen = prefs.getBoolean("letScreenAlwaysOn", false);
		Log.d("EditBattleActivity.onCreate", "Settings screen is " + screen);
		if (screen) {
			context.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			context.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

}
