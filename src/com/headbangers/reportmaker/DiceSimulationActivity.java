package com.headbangers.reportmaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.simonvt.widget.NumberPicker;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.actionbarsherlock.app.SherlockActivity;
import com.headbangers.reportmaker.tools.AdsControl;

public class DiceSimulationActivity extends SherlockActivity {

	private GridView gridView;

	private int nbDices = 25;
	private int sucessMinimum = 4;
	private SuccessType successType = SuccessType.MORE_OR_EQUAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_simulation);

		this.gridView = (GridView) findViewById(R.id.diceGrid);

		// Look up the AdView as a resource and load a request.
		AdsControl.buildAdIfEnable(this);

		// Affichage d'une boite de dialogue
		// "Combien de dés et quel est le seuil ?"
		DicesConfiguration config = new DicesConfiguration(this);
		config.setTitle(R.string.dice_simulation);
		config.show();
	}

	protected void randomRoll(int nbDices, int success, SuccessType type) {
		this.nbDices = nbDices;
		this.sucessMinimum = success;
		this.successType = type;

		// Tirage du tableau
		int[] values = new int[nbDices];
		Random rand = new Random();
		for (int d = 0; d < nbDices; d++) {
			values[d] = rand.nextInt(5) + 1;
		}

		// Rafraichissement de la gridView
		refreshGrid(values);
	}

	protected void refreshGrid(int[] dices) {
		this.gridView.setAdapter(new GridDicesAdapter(dices));
	}

	public int getSucessMinimum() {
		return sucessMinimum;
	}

	public SuccessType getSuccessType() {
		return successType;
	}

	class GridDicesAdapter extends BaseAdapter {

		private int[] dices;

		public GridDicesAdapter(int[] dices) {
			this.dices = dices;
		}

		@Override
		public int getCount() {
			return dices.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView diceImage = (ImageView) convertView;
			if (diceImage == null) {
				diceImage = new ImageView(DiceSimulationActivity.this);
			}

			int value = this.dices[position];
			Dices dice = Dices.byValue(value);

			SuccessType type = DiceSimulationActivity.this.getSuccessType();
			int success = DiceSimulationActivity.this.getSucessMinimum();

			int idDrawable = 0;
			switch (type) {
			case MORE_OR_EQUAL:
				idDrawable = (value >= success) ? dice.getDrawableOk() : dice
						.getDrawableKo();
				break;
			case EQUAL:
				idDrawable = (value == success) ? dice.getDrawableOk() : dice
						.getDrawableKo();
				break;
			case LESS_OR_EQUAL:
				idDrawable = (value <= success) ? dice.getDrawableOk() : dice
						.getDrawableKo();
				break;
			}

			diceImage.setImageResource(idDrawable);
			diceImage.setScaleType(ScaleType.FIT_CENTER);

			return diceImage;
		}
	}

	class DicesConfiguration extends Dialog {

		private NumberPicker howManyDices;
		private NumberPicker successPicker;
		private NumberPicker successTypePicker;
		private Button roll;

		public DicesConfiguration(Context context) {
			super(context, R.style.dialogBackground);
			this.setCanceledOnTouchOutside(false);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dices_configuration_dialog);

			howManyDices = (NumberPicker) findViewById(R.id.howManyPicker);
			successPicker = (NumberPicker) findViewById(R.id.successPicker);
			successTypePicker = (NumberPicker) findViewById(R.id.successTypePicker);
			roll = (Button) findViewById(R.id.rollDices);

			howManyDices.setMaxValue(100);
			howManyDices.setMinValue(1);
			howManyDices.setValue(DiceSimulationActivity.this.nbDices);

			successPicker.setMaxValue(6);
			successPicker.setMinValue(1);
			successPicker.setValue(DiceSimulationActivity.this.sucessMinimum);
			// successPicker.setFocusable(true);
			// successPicker.setFocusableInTouchMode(true);

			successTypePicker.setMaxValue(SuccessType.values().length - 1);
			successTypePicker.setMinValue(0);
			successTypePicker.setValue(SuccessType.MORE_OR_EQUAL.ordinal());
			successTypePicker.setDisplayedValues(SuccessType
					.displayedValues(DiceSimulationActivity.this));

			roll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Récupération des valeurs
					int nbDices = howManyDices.getValue();
					int success = successPicker.getValue();

					// Lancement du tirage aléatoire
					DiceSimulationActivity.this.randomRoll(nbDices, success,
							SuccessType.MORE_OR_EQUAL);

					// Fermeture de la boite de dialogue
					dismiss();
				}
			});
		}
	}

	enum SuccessType {
		MORE_OR_EQUAL(R.string.more_or_equal), EQUAL(R.string.equal), LESS_OR_EQUAL(
				R.string.less_or_equal);

		private SuccessType(int string) {
			this.string = string;
		}

		private int string;

		public static String[] displayedValues(DiceSimulationActivity context) {
			List<String> displayed = new ArrayList<String>();
			for (SuccessType type : SuccessType.values()) {
				displayed.add(context.getResources().getString(type.string));
			}
			return displayed.toArray(new String[0]);
		}
	}

	enum Dices {
		ONE(R.drawable.dice_1, R.drawable.dice_1_fade), TWO(R.drawable.dice_2,
				R.drawable.dice_2_fade), THREE(R.drawable.dice_3,
				R.drawable.dice_3_fade), FOUR(R.drawable.dice_4,
				R.drawable.dice_4_fade), FIVE(R.drawable.dice_5,
				R.drawable.dice_5_fade), SIX(R.drawable.dice_6,
				R.drawable.dice_6_fade);

		Dices(int drawableOk, int drawableKo) {
			this.drawableKo = drawableKo;
			this.drawableOk = drawableOk;
		}

		private int drawableOk;
		private int drawableKo;

		public int getDrawableKo() {
			return drawableKo;
		}

		public int getDrawableOk() {
			return drawableOk;
		}

		public static Dices byValue(int value) {
			return Dices.values()[value - 1];
		}
	}
}
