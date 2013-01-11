package com.headbangers.reportmaker.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.headbangers.reportmaker.EditBattleActivity;
import com.headbangers.reportmaker.R;

public class TimerHelper {

	private Handler handler;
	private EditBattleActivity context;

	private boolean nextMessageStop = false;
	private boolean isRunning = false;

	public TimerHelper() {
		this.handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				int minutes = msg.what;
				if (minutes == 0) {
					// Stop !
					TimerHelper.this.nextMessageStop = true;
					return true;
				} else if (!TimerHelper.this.nextMessageStop) {
					Log.d("Timer", "Tick !");

					// Paramètres.
					SharedPreferences preference = PreferenceManager
							.getDefaultSharedPreferences(TimerHelper.this.context);
					String strRingtonePreference = preference.getString(
							"ringtoneTimer", "DEFAULT_SOUND");
					boolean vibrate = preference.getBoolean("vibratorTimer",
							false);

					// On vibre si necessaire
					TimerHelper.this.vibrate(vibrate);

					// On notifie (pour permettre de stopper)
					TimerHelper.this.notify(strRingtonePreference);

					// Et on appelle le suivant !
					TimerHelper.this.configureTimer(minutes);
					return true;
				} else {
					// Stop !
					TimerHelper.this.nextMessageStop = false;
					return true;
				}
			}
		});
	}

	private void vibrate(boolean param) {
		if (param) {
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) this.context
					.getSystemService(Context.VIBRATOR_SERVICE);

			// This example will cause the phone to vibrate "SOS" in Morse Code
			// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
			// There are pauses to separate dots/dashes, letters, and words
			// The following numbers represent millisecond lengths
			int dot = 200; // Length of a Morse Code "dot" in milliseconds
			int dash = 500; // Length of a Morse Code "dash" in milliseconds
			int short_gap = 200; // Length of Gap Between dots/dashes
			int medium_gap = 500; // Length of Gap Between Letters
			int long_gap = 1000; // Length of Gap Between Words
			long[] pattern = { 0, // Start immediately
					dot, short_gap, dot, short_gap, dot, // s
					medium_gap, dash, short_gap, dash, short_gap, dash, // o
					medium_gap, dot, short_gap, dot, short_gap, dot, // s
					long_gap };

			// Only perform this pattern one time (-1 means "do not repeat")
			v.vibrate(pattern, -1);
		}

	}

	private void notify(String sound) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this.context)
				.setSmallIcon(R.drawable.icone)
				.setContentTitle(
						this.context.getResources().getString(
								R.string.timer_notification_title))
				.setContentText(
						this.context.getResources().getString(
								R.string.timer_notification_infos));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this.context, EditBattleActivity.class);
		resultIntent.putExtra(EditBattleActivity.STOP_TIMER, true);
		resultIntent.putExtra(EditBattleActivity.BATTLE_ID_ARG,
				this.context.getBattleId());

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(EditBattleActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setSound(Uri.parse(sound));
		mBuilder.setAutoCancel(true);

		NotificationManager mNotificationManager = (NotificationManager) this.context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}

	public void configureTimer(int minutes) {
		this.handler.sendEmptyMessageDelayed(minutes, (minutes * 60) * 1000);
		this.isRunning = true;
	}

	public void stopTimer() {
		this.handler.sendEmptyMessage(0);
		this.isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setContext(EditBattleActivity context) {
		this.context = context;
	}
}
