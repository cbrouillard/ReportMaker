package com.headbangers.reportmaker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

public class AuthActivity extends RoboActivity {

	public static final String RELAY_ARG = "relay";
	public static final String FORCEMODE_ARG = "forceMode";

	@Inject
	private SharedPreferences prefs;

	@InjectView(R.id.login)
	private EditText login;

	@InjectView(R.id.password)
	private EditText password;

	@InjectView(R.id.connect)
	private Button connect;

	@InjectView(R.id.helperText)
	private TextView helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Récupération d'un éventuel forceMode
		boolean forceMode = getIntent().getBooleanExtra(FORCEMODE_ARG, false);

		// Si forceMode = true alors on affiche le layout.
		if (forceMode) {
			display();
		} else {
			// Sinon,
			// On regarde dans les préférences si user et pass sont définis.
			String user = prefs.getString("user", null);
			String pass = prefs.getString("pass", null);

			// Si oui et si le relay est défini, l'activity stoppe en renvoi les
			// données
			if (user != null && pass != null && !user.isEmpty()
					&& !pass.isEmpty()) {
				result(user, pass);
			} else {
				// Si non, on affiche le layout
				display();
			}
		}

	}

	protected void result(String user, String pass) {
		Intent data = new Intent();
		data.putExtra("user", user);
		data.putExtra("pass", pass);
		setResult(RESULT_OK, data);
		this.finish();
	}

	protected void save() {
		String username = login.getText().toString();
		String pass = encode(password.getText().toString());

		prefs.edit().putString("user", username).putString("pass", pass)
				.commit();

		result(username, pass);
	}

	private void display() {
		setContentView(R.layout.auth);

		helper.setLinkTextColor(Color.BLUE);
		Linkify.addLinks(helper, Linkify.ALL);

		this.connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Ecriture des données dans les préférences
				AuthActivity.this.save();
			}
		});
	}

	private String encode(String password) {
		byte[] uniqueKey = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("SHA-256").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			throw new Error("No SHA-256 support in this VM.");
		}

		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}
}
