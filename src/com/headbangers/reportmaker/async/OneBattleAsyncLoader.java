package com.headbangers.reportmaker.async;

import com.headbangers.reportmaker.EditBattleActivity;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.pojo.Battle;

public class OneBattleAsyncLoader extends GenericAsyncLoader<Long, Battle> {

	private BattleDao dao;

	public OneBattleAsyncLoader(EditBattleActivity activity, BattleDao dao) {
		super(activity);
		this.dao = dao;
	}

	@Override
	protected Battle doInBackground(Long... params) {
		return this.dao.findBattleById(params[0]);
	}

	@Override
	protected void onPostExecute(Battle result) {
		super.onPostExecute(result);

		EditBattleActivity activity = (EditBattleActivity) fromContext;
		activity.refresh(result);
	}

}
