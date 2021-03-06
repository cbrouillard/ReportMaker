package com.headbangers.reportmaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.headbangers.reportmaker.adapter.GalleryAdapter;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.fragment.TurnFragment;
import com.headbangers.reportmaker.listener.AddTabListener;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.service.DroidTextPDFService;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.service.IPDFService;
import com.headbangers.reportmaker.tools.AdsControl;
import com.headbangers.reportmaker.tools.ImageHelper;
import com.headbangers.reportmaker.tools.ScreenHelper;
import com.headbangers.reportmaker.tools.TimerHelper;

@SuppressWarnings("deprecation")
public class EditBattleActivity extends SherlockFragmentActivity implements
		TabListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "edit_battle_selected_navigation_item";
	public static final String BATTLE_ID_ARG = "battle_id";

	private static final int TAKE_PHOTO_EXTRA_RESULT_CODE = 100;

	private Long battleId = null;
	private Battle battle = null;
	private BattleDao battleDao = new BattleDaoImpl(this);
	private FilesystemService fs = FilesystemService.getInstance();

	private BattleInformationsFragment informations = new BattleInformationsFragment();

	private List<TurnFragment> turns = new ArrayList<TurnFragment>();

	private Gallery gallery;
	private SharedPreferences prefs;

	private IPDFService pdfService = new DroidTextPDFService(this);

	private int currentTabSelected = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// Activity was brought to front and not created,
			// Thus finishing this will get us to the last viewed activity
			finish();
			return;
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_battle_activity);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.gallery = (Gallery) findViewById(R.id.extrasPhotosGallery);

		battleDao.open();
		pdfService.setDao(battleDao);

		// Sauvegarde de l'id de la bataille
		this.battleId = this.getIntent().getExtras().getLong(BATTLE_ID_ARG);
		Log.d(this.getClass().getSimpleName(), "battleId = " + battleId);

		this.loadBattle();

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Création des turnFragment
		int nbTurn = battle.getTurns().size() > 0 ? battle.getTurns().size()
				: this.battle.getGameType().getNbDefaultTurn();
		actionBar.addTab(actionBar.newTab().setText("•").setTabListener(this));
		for (int f = 0; f < nbTurn; f++) {

			TurnFragment turn = new TurnFragment();
			turn.setBattle(battle, f + 1);
			this.turns.add(turn);

			actionBar.addTab(actionBar.newTab().setText("T" + (f + 1))
					.setTabListener(this));
		}

		actionBar.addTab(actionBar.newTab().setText("+")
				.setTabListener(new AddTabListener(this, actionBar)));

		AdsControl.buildAdIfEnable(this);

		// Doit-on laisser l'écran allumé en permanence ?
		ScreenHelper.applyAlwaysSwitched(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		battleDao.open();

		this.loadBattle();

		AdsControl.buildAdIfEnable(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		battleDao.close();
	}

	private void loadBattle() {
		Battle battle = battleDao.findBattleById(battleId);
		refresh(battle);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_edit_battle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_done:

			// Enregistrement
			save();

			return true;
		case R.id.menu_photo:

			String photoName = fs.determineNewExtraPhotoName(battle,
					this.currentTabSelected);

			// Prise d'une photo
			ImageHelper.takePhoto(this, fs, battle, photoName,
					TAKE_PHOTO_EXTRA_RESULT_CODE);

			return true;
		case R.id.menu_exportBattle:
			pdfService.exportBattleAsync(battle.getId(), this);
			return true;
		case R.id.preferences:
			Intent preferences = new Intent(this, PreferencesActivity.class);
			startActivityForResult(preferences, PreferencesActivity.CODE_RESULT);

			return true;
		case R.id.menu_timer:
			TimerHelper.getInstance(this).manageDialog();
			return true;
		case R.id.diceSimulator:
			Intent simulator = new Intent(this, DiceSimulationActivity.class);
			startActivity(simulator);
			return true;
		case R.id.menu_configureBattle:
			// Lancement de l'activité de configuration d'une nouvelle partie
			// AVEC un ID
			Intent configureBattle = new Intent(this,
					ConfigureBattleActivity.class);
			// configureBattle.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			configureBattle.putExtra(EditBattleActivity.BATTLE_ID_ARG,
					this.battle.getId());
			startActivity(configureBattle);
			break;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case TAKE_PHOTO_EXTRA_RESULT_CODE:
			this.gallery.setVisibility(View.VISIBLE);
			break;
		case PreferencesActivity.CODE_RESULT:
			Toast.makeText(this, R.string.preferences_ok, Toast.LENGTH_LONG)
					.show();
			// gérer le changement de paramètre sur l'écran
			ScreenHelper.applyAlwaysSwitched(this);

			// est ce que les commentaires étendus apparaissent ou disparaissent
			// ?
			for (TurnFragment turn : turns) {
				turn.applyExtendedCommentsSettings();
			}

			break;
		}
	}

	@Override
	public void onBackPressed() {

		String actionStr = this.prefs.getString("actionWhenQuit",
				PreferencesActivity.ACTION_ON_QUIT.ALWAYS_ASK.toString());

		PreferencesActivity.ACTION_ON_QUIT action = PreferencesActivity.ACTION_ON_QUIT
				.valueOf(actionStr);

		Log.d("EditBattleActivity.onBackPressed", "Settings action is "
				+ action);

		switch (action) {
		case ALWAYS_ASK:
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.edition)
					.setMessage(R.string.really_quit)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Sauvegarde
									EditBattleActivity.this.save();
									dialog.dismiss();
									EditBattleActivity.this.finish();
								}

							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									EditBattleActivity.this.finish();
								}

							})
					.setNeutralButton(R.string.dont_want_to_quit, null)

					.show();
			break;

		case ALWAYS_SAVE:
			this.save();
		case NEVER_SAVE:
			this.finish();
			break;
		}

		// Extinction des timers
		if (TimerHelper.getInstance(this).isRunning()) {
			TimerHelper.getInstance(this).stopTimer();
		}

	}

	private void save() {

		// Récupération des données dispatchées dans les fragments et sauvegarde
		// en base
		Informations infos = this.informations.buildInformations();

		List<Turn> allTurnsInfos = new ArrayList<Turn>();
		for (TurnFragment oneTurn : turns) {
			allTurnsInfos.add(oneTurn.buildTurn());
		}

		battleDao.updateBattle(battle, infos, allTurnsInfos);
		loadBattle();

		Toast.makeText(this, R.string.battle_saved, Toast.LENGTH_LONG).show();

	}

	public int getFirstPlayer() {
		if (this.informations == null) {
			return 0;
		}
		return this.informations.getFirstPlayer();
	}

	public void refresh(Battle result) {
		this.battle = result;

		if (battle == null) {
			this.finish();
		}

		StringBuilder title = new StringBuilder(battle.getName());
		if (battle.getFormat() != null) {
			title.append(" [").append(battle.getFormat()).append("]");
		}
		this.setTitle(title.toString());

		// Création des fragments
		this.informations.setBattle(battle);
		int cpt = 1;
		for (TurnFragment turn : turns) {
			turn.setBattle(battle, cpt);
			cpt++;
		}

		reloadGallery();
	}

	public void reloadGallery() {
		this.gallery.setAdapter(new GalleryAdapter(this, battle,
				this.currentTabSelected));
		this.gallery
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {

						String extrasPath = (String) gallery.getAdapter()
								.getItem(position);

						File imageFile = new File(extrasPath);
						ImageHelper.showImageInDialog(imageFile,
								EditBattleActivity.this,
								EditBattleActivity.this.getResources()
										.getString(R.string.zoom_extra),
								(ImageView) view);
					}
				});
		if (this.gallery.getAdapter().getCount() == 0) {
			this.gallery.setVisibility(View.GONE);
		} else {
			this.gallery.setVisibility(View.VISIBLE);
		}
	}

	public Long getBattleId() {
		return battleId;
	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		Fragment fragment = null;

		this.currentTabSelected = tab.getPosition();

		if (tab.getPosition() == 0) {
			fragment = this.informations;
		} else {
			fragment = this.turns.get(tab.getPosition() - 1);
		}

		if (fragment != null) {

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

		reloadGallery();

	}

	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		this.currentTabSelected = tab.getPosition();
		reloadGallery();

	}

	public void addTurnFragment(int num) {
		TurnFragment fragment = new TurnFragment();
		fragment.setBattle(this.battle, num);
		this.turns.add(fragment);
	}

}
