package com.headbangers.reportmaker.async;

import android.database.Cursor;

import com.headbangers.reportmaker.BattleListActivity;
import com.headbangers.reportmaker.dao.BattleDao;

public class BattleListAsyncLoader extends GenericAsyncLoader<Void, Cursor> {

	private BattleDao dao;

	public BattleListAsyncLoader(BattleListActivity activity, BattleDao dao) {
		super(activity);
		this.dao = dao;
	}

	@Override
	protected Cursor doInBackground(Void... arg0) {
		this.dao.open();
		return this.dao.getAllBattles();
	}

	@Override
	protected void onPostExecute(Cursor result) {
		super.onPostExecute(result);

		BattleListActivity activity = (BattleListActivity) this.fromContext;
		activity.refresh(result);

	}

}
