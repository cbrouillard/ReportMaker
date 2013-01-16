package com.headbangers.reportmaker;

import android.os.Bundle;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;

public class DiceSimulationActivity extends SherlockActivity {

	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_simulation);

		this.gridView = (GridView) findViewById(R.id.diceGrid);
		
		// Affichage d'une boite de dialogue "Combien de dés et quel est le seuil ?"
	}
}
