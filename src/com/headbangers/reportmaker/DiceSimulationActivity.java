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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.headbangers.reportmaker.tools.AdsControl;

public class DiceSimulationActivity extends SherlockActivity {

	private GridView gridView;
	private TextView howManyDices;
	private TextView howManyDicesTheory;
	private TextView howManyDicesSuccess;
	private TextView howManyDicesFail;
	private ImageButton rerollSuccessDices;
	private LinearLayout stats;

	private int nbDices = 25;
	private int lastNbSuccess = 25;
	private int successMinimum = 4;
	private SuccessType successType = SuccessType.MORE_OR_EQUAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_simulation);

		this.gridView = (GridView) findViewById(R.id.diceGrid);
		this.howManyDices = (TextView) findViewById(R.id.dices_howMany);
		this.howManyDicesSuccess = (TextView) findViewById(R.id.dices_howMany_success);
		this.howManyDicesTheory = (TextView) findViewById(R.id.dices_howMany_theory);
		this.howManyDicesFail = (TextView) findViewById(R.id.dices_howMany_fail);
		this.rerollSuccessDices = (ImageButton) findViewById(R.id.action_rollSuccessDices);
		this.stats = (LinearLayout) findViewById(R.id.stats);

		this.rerollSuccessDices.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DiceSimulationActivity.this.nbDices = DiceSimulationActivity.this.lastNbSuccess;
				DiceSimulationActivity.this.configureNewRoll();
			}
		});

		// Look up the AdView as a resource and load a request.
		AdsControl.buildAdIfEnable(this);

		configureNewRoll();
	}

	public void configureNewRoll() {
		// Affichage d'une boite de dialogue
		// "Combien de dés et quel est le seuil ?"
		DicesConfiguration config = new DicesConfiguration(this);
		config.setTitle(R.string.dice_simulation);
		config.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_dices_simulation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_relaunchSame:
			randomRoll(this.nbDices, this.successMinimum, this.successType);
			return true;
		case R.id.menu_newRoll:
			configureNewRoll();
			return true;
		}

		return false;
	}

	protected void randomRoll(int nbDices, int success, SuccessType type) {
		this.nbDices = nbDices;
		this.successMinimum = success;
		this.successType = type;

		// Tirage du tableau
		int[] values = new int[nbDices];
		this.lastNbSuccess = 0;
		Random rand = new Random();
		for (int d = 0; d < nbDices; d++) {
			values[d] = rand.nextInt(6) + 1;
			if (isDiceSucceeded(values[d])) {
				this.lastNbSuccess++;
			}
		}

		// Rafraichissement de la gridView
		refreshGrid(values);

		// Rafraichissement des stats de résultat
		stats.setVisibility(View.VISIBLE);
		int successPercent = this.lastNbSuccess * 100 / this.nbDices;
		this.howManyDices.setText(this.nbDices + " "
				+ getString(R.string.dices_toObtain) + " "
				+ getString(this.successType.string) + this.successMinimum);
		this.howManyDicesSuccess
				.setText(getString(R.string.dices_howMany_success) + " "
						+ this.lastNbSuccess + " (" + successPercent + "%)");
		this.howManyDicesFail.setText(getString(R.string.dices_howMany_fail)
				+ " " + (this.nbDices - this.lastNbSuccess) + " ("
				+ (100 - successPercent) + "%)");

		float theorySuccessRatioByDice = successType.calculateSuccessTheory(successMinimum);
		float theorySuccess = this.nbDices * theorySuccessRatioByDice;
		
		this.howManyDicesTheory.setText("En théorie : " + theorySuccess
				+ " dés réussis (" + (theorySuccessRatioByDice * 100) + "%)");
	}

	protected void refreshGrid(int[] dices) {
		this.gridView.setAdapter(new GridDicesAdapter(dices));
	}

	public int getSucessMinimum() {
		return successMinimum;
	}

	public SuccessType getSuccessType() {
		return successType;
	}

	private boolean isDiceSucceeded(int diceValue) {
		switch (this.successType) {
		case MORE_OR_EQUAL:
			return diceValue >= successMinimum;
		case EQUAL:
			return diceValue == successMinimum;
		case LESS_OR_EQUAL:
			return diceValue <= successMinimum;
		}

		return false;
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

			int idDrawable = DiceSimulationActivity.this.isDiceSucceeded(value) ? dice.drawableOk
					: dice.drawableKo;

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
			successPicker.setValue(DiceSimulationActivity.this.successMinimum);

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
					int successType = successTypePicker.getValue();

					// Lancement du tirage aléatoire
					DiceSimulationActivity.this.randomRoll(nbDices, success,
							SuccessType.values()[successType]);

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

		public float calculateSuccessTheory(int successMini) {
			float value = 0;
			switch (SuccessType.this) {
			case EQUAL:
				value = (float) (1 / 6f);
				break;
			case LESS_OR_EQUAL:
				value = (float) (successMini / 6f);
				break;
			case MORE_OR_EQUAL:
				value = (float) ((6 - successMini + 1) / 6f);
				break;
			}
			return value;
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
