package com.headbangers.reportmaker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.headbangers.reportmaker.tools.TimerHelper;

public class TimerManagementDialog extends Dialog {

	private EditBattleActivity mContext = null;

	private ImageButton settings;
	private ImageButton start;
	private ImageButton stop;
	private TextView status;

	public TimerManagementDialog(EditBattleActivity context) {
		super(context, R.style.dialogBackground);
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
		status = (TextView) findViewById(R.id.timer_status);

		// desactivation du start ou du stop
		boolean isRunning = TimerHelper.getInstance(this.mContext).isRunning();
		start.setEnabled(!isRunning);
		stop.setEnabled(isRunning);

		status.setText(isRunning ? R.string.timer_isOn : R.string.timer_isOff);
		status.setTextColor(isRunning ? Color.BLACK : Color.RED);

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
				Integer duration = TimerHelper.getInstance(
						TimerManagementDialog.this.mContext).startTimer();
				start.setEnabled(false);
				stop.setEnabled(true);

				status.setText(R.string.timer_isOn);
				status.setTextColor(Color.BLACK);

				Toast.makeText(
						TimerManagementDialog.this.mContext,
						TimerManagementDialog.this.mContext.getResources()
								.getString(R.string.timer_launched)
								.replace("[X]", duration + ""),
						Toast.LENGTH_LONG).show();
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimerHelper.getInstance(TimerManagementDialog.this.mContext)
						.stopTimer();
				start.setEnabled(true);
				stop.setEnabled(false);

				status.setText(R.string.timer_isOff);
				status.setTextColor(Color.RED);

				Toast.makeText(TimerManagementDialog.this.mContext,
						R.string.timer_stopped, Toast.LENGTH_LONG).show();
			}
		});

	}

}
