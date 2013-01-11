package com.headbangers.reportmaker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.headbangers.reportmaker.tools.TimerHelper;

public class TimerManagementDialog extends Dialog {

	private EditBattleActivity mContext = null;

	private ImageButton settings;
	private ImageButton start;
	private ImageButton stop;

	public TimerManagementDialog(EditBattleActivity context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * Standard Android on create method that gets called when the activity
	 * initialized.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.timer_management_dialog);
		settings = (ImageButton) findViewById(R.id.action_timer_settings);
		start = (ImageButton) findViewById(R.id.action_timer_launch);
		stop = (ImageButton) findViewById(R.id.action_timer_stop);

		// desactivation du start ou du stop
		boolean isRunning = TimerHelper.getInstance(this.mContext).isRunning();
		start.setEnabled(!isRunning);
		stop.setEnabled(isRunning);

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent preferences = new Intent(
						TimerManagementDialog.this.mContext,
						PreferencesActivity.class);
				TimerManagementDialog.this.mContext.startActivityForResult(
						preferences, PreferencesActivity.CODE_RESULT);
			}
		});

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimerHelper.getInstance(TimerManagementDialog.this.mContext)
						.startTimer();
				start.setEnabled(false);
				stop.setEnabled(true);
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimerHelper.getInstance(TimerManagementDialog.this.mContext)
						.stopTimer();
				start.setEnabled(true);
				stop.setEnabled(false);
			}
		});

	}

}
