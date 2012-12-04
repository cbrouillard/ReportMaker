package com.headbangers.reportmaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;

import com.headbangers.reportmaker.adapter.GalleryAdapter;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.fragment.TurnFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Informations;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.service.FilesystemService;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_battle_activity);

		battleDao.open();

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
		actionBar.addTab(actionBar.newTab().setText(R.string.t6)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.t7)
				.setTabListener(this));

		// Sauvegarde de l'id de la bataille
		this.battleId = this.getIntent().getExtras().getLong(BATTLE_ID_ARG);
		Log.d(this.getClass().getSimpleName(), "battleId = " + battleId);

		this.loadBattle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		battleDao.open();

		this.loadBattle();
	}

	@Override
	protected void onPause() {
		super.onPause();
		battleDao.close();
	}

	private void loadBattle() {
		this.battle = battleDao.findBattleById(battleId);

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

		this.gallery.setAdapter(new GalleryAdapter(this, battle));
		this.gallery
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {

						String extrasPath = (String) gallery.getAdapter()
								.getItem(position);

						File imageFile = new File(extrasPath);
						ImageHelper.showImageInDialog(imageFile,
								EditBattleActivity.this, "Zoom sur photos");
					}
				});
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
			fragment = this.turns[tab.getPosition() - 1];
			break;
		}

		if (fragment != null) {

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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

			String photoName = fs.determineNewExtraPhotoName(battle);

			// Prise d'une photo
			ImageHelper.takePhoto(this, fs, battle, photoName,
					TAKE_PHOTO_EXTRA_RESULT_CODE);

			return true;
		}

		return false;
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

}
