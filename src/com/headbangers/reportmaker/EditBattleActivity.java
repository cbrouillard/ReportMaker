package com.headbangers.reportmaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.inject.Inject;
import com.headbangers.reportmaker.adapter.GalleryAdapter;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.fragment.TurnFragment;
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

public class EditBattleActivity extends RoboFragmentActivity implements
		ActionBar.TabListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "edit_battle_selected_navigation_item";
	public static final String BATTLE_ID_ARG = "battle_id";

	private static final int TAKE_PHOTO_EXTRA_RESULT_CODE = 1;

	private Long battleId = null;
	private Battle battle = null;
	private BattleDao battleDao = new BattleDaoImpl(this);
	private FilesystemService fs = new FilesystemService();

	private BattleInformationsFragment informations = new BattleInformationsFragment();
	private TurnFragment[] turns = { new TurnFragment(), new TurnFragment(),
			new TurnFragment(), new TurnFragment(), new TurnFragment(),
			new TurnFragment(), new TurnFragment() };

	@InjectView(R.id.extrasPhotosGallery)
	private Gallery gallery;

	private IPDFService pdfService = new DroidTextPDFService(this);

	private int currentTabSelected = 0;

	@Inject
	private SharedPreferences prefs;

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

		battleDao.open();
		pdfService.setDao(battleDao);

		// Sauvegarde de l'id de la bataille
		this.battleId = this.getIntent().getExtras().getLong(BATTLE_ID_ARG);
		Log.d(this.getClass().getSimpleName(), "battleId = " + battleId);

		this.loadBattle();

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText("•").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t3)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t4)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t5)
				.setTabListener(this));

		// L'affichage de ces tabs devrait être conditionné/ Galere.
		actionBar.addTab(actionBar.newTab().setText(R.string.t6)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t7)
				.setTabListener(this));

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
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Fragment fragment = null;

		this.currentTabSelected = tab.getPosition();

		switch (tab.getPosition()) {
		case 0:
			fragment = this.informations;
			break;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			fragment = this.turns[tab.getPosition() - 1];
			break;
		}

		if (fragment != null) {

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

		reloadGallery();

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		this.currentTabSelected = tab.getPosition();
		reloadGallery();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_battle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO_EXTRA_RESULT_CODE:
			this.gallery.setVisibility(View.VISIBLE);
			break;
		case PreferencesActivity.CODE_RESULT:
			Toast.makeText(this, R.string.preferences_ok, Toast.LENGTH_LONG)
					.show();
			// gérer le changement de paramètre sur l'écran
			ScreenHelper.applyAlwaysSwitched(this);

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
		title.append(" [").append(battle.getFormat()).append("]");
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
										.getString(R.string.zoom_extra));
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

}
