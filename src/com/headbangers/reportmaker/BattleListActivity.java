package com.headbangers.reportmaker;

import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdView;
import com.headbangers.reportmaker.adapter.BattleListAdapter;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.dao.impl.BattleDaoImpl;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.DroidTextPDFService;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.service.IPDFService;

/**
 * Affiche une liste contenant toutes les batailles ainsi qu'un bouton
 * permettant de lancer la configuration d'une nouvelle.
 * 
 * @author cbrouillard
 */
public class BattleListActivity extends RoboListActivity {

	@InjectView(android.R.id.list)
	private ListView battleList;

	private BattleDao battleDao = new BattleDaoImpl(this);

	private Battle selected = null;

	private FilesystemService fs = new FilesystemService();
	private IPDFService pdfService = new DroidTextPDFService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle_list);

		this.battleDao.open();
		this.pdfService.setDao(battleDao);

		// Remplissage de la liste.
		asyncLoadBattle();

		// Look up the AdView as a resource and load a request.
		AdsControl.buildAdIfEnable(this);

		registerForContextMenu(this.battleList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.battleDao.open();
		this.pdfService.setDao(battleDao);
		asyncLoadBattle();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.battleDao.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Un click sur un élément de la liste permet de lancer l'activité
		// d'édition
		Intent editBattle = new Intent(this, EditBattleActivity.class);
		editBattle.putExtra(EditBattleActivity.BATTLE_ID_ARG, id);
		startActivity(editBattle);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_battle_list_action, menu);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		selected = battleDao.findBattleById(info.id);
		if (selected != null) {
			menu.setHeaderTitle(selected.getName()); // Countries[info.position]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_battle_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_newBattle:

			// Lancement de l'activité de configuration d'une nouvelle partie
			Intent newBattle = new Intent(this,
					ConfigureNewBattleActivity.class);
			newBattle.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(newBattle);

			return true;
		case R.id.about:

			AboutDialog about = new AboutDialog(this);
			about.setTitle(getResources().getString(R.string.about));
			about.show();

			return true;

		}

		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_deleteBattle:
			// Effacement de la bataille après confirmation
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.delete_battle)
					.setMessage(R.string.really_delete)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// Effacement réel
									BattleListActivity.this
											.deleteSelectedBattle();
								}

							}).setNegativeButton(R.string.no, null).show();

			return true;
		case R.id.menu_exportBattle:
			// String pdfFilePath =
			// this.pdfService.exportBattle(selected.getId());
			//
			// if (pdfFilePath != null) {
			// Toast.makeText(this, R.string.pdf_hasbeen_generated,
			// Toast.LENGTH_LONG).show();
			//
			// // On fait quoi avec le PDF ? Partage ou Visualisation
			//
			// }

			pdfService.exportBattleAsync(selected.getId(), this);

			return true;
		}

		return false;
	}

	private void deleteSelectedBattle() {
		if (selected == null) {
			return;
		}

		this.battleDao.deleteBattle(selected.getId());
		this.fs.deleteBattleDirectory(selected.getId());

		Toast.makeText(this, R.string.battle_hasbeen_deleted, Toast.LENGTH_LONG)
				.show();

		asyncLoadBattle();
	}

	public void asyncLoadBattle() {

		setListAdapter(new BattleListAdapter(this,
				this.battleDao.getAllBattles()));
		//
		// new BattleListAsyncLoader(this, this.battleDao).setDialogText(
		// this.getResources().getString(R.string.loading)).execute();
	}

	public void refresh(Cursor result) {
		setListAdapter(new BattleListAdapter(this, result));
	}
}
