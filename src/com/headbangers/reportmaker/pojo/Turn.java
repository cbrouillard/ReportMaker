package com.headbangers.reportmaker.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.headbangers.reportmaker.dao.DatabaseHelper;

public class Turn {

	private Integer num;

	private String commentsMove1;
	private String commentsMove2;

	private String commentsShoot1;
	private String commentsShoot2;

	private String commentsAssault1;
	private String commentsAssault2;

	private Boolean lastOne;
	private Boolean nightFight;

	public Turn(Cursor cursor) {
		this.commentsMove1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_MOVE1));
		this.commentsMove2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_MOVE2));

		this.commentsShoot1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_SHOOT1));
		this.commentsShoot2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_SHOOT2));

		this.commentsAssault1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_ASSAULT1));
		this.commentsAssault2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_ASSAULT2));

		this.lastOne = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_IS_LAST_ONE)) == 1 ? true
				: false;
		this.nightFight = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_IS_NIGHT_FIGHT)) == 1 ? true
				: false;

		this.num = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_NUM));
	}

	public Turn() {
	}

	public String getCommentsMove1() {
		return commentsMove1;
	}

	public void setCommentsMove1(String commentsMove1) {
		this.commentsMove1 = commentsMove1;
	}

	public String getCommentsMove2() {
		return commentsMove2;
	}

	public void setCommentsMove2(String commentsMove2) {
		this.commentsMove2 = commentsMove2;
	}

	public String getCommentsShoot1() {
		return commentsShoot1;
	}

	public void setCommentsShoot1(String commentsShoot1) {
		this.commentsShoot1 = commentsShoot1;
	}

	public String getCommentsShoot2() {
		return commentsShoot2;
	}

	public void setCommentsShoot2(String commentsShoot2) {
		this.commentsShoot2 = commentsShoot2;
	}

	public String getCommentsAssault1() {
		return commentsAssault1;
	}

	public void setCommentsAssault1(String commentsAssault1) {
		this.commentsAssault1 = commentsAssault1;
	}

	public String getCommentsAssault2() {
		return commentsAssault2;
	}

	public void setCommentsAssault2(String commentsAssault2) {
		this.commentsAssault2 = commentsAssault2;
	}

	public void setLastOne(Boolean isLastOne) {
		this.lastOne = isLastOne;
	}

	public void setNightFight(Boolean nightFight) {
		this.nightFight = nightFight;
	}

	public Boolean isNightFight() {
		return nightFight;
	}

	public Boolean isLastOne() {
		return lastOne;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public ContentValues asContentValues() {
		ContentValues values = new ContentValues();

		if (this.commentsMove1 != null) {
			values.put(DatabaseHelper.COL_COMMENT_MOVE1, this.commentsMove1);
		}
		if (this.commentsMove2 != null) {
			values.put(DatabaseHelper.COL_COMMENT_MOVE2, this.commentsMove2);
		}

		if (this.commentsShoot1 != null) {
			values.put(DatabaseHelper.COL_COMMENT_SHOOT1, this.commentsShoot1);
		}
		if (this.commentsShoot2 != null) {
			values.put(DatabaseHelper.COL_COMMENT_SHOOT2, this.commentsShoot2);
		}

		if (this.commentsAssault1 != null) {
			values.put(DatabaseHelper.COL_COMMENT_ASSAULT1,
					this.commentsAssault1);
		}
		if (this.commentsAssault2 != null) {
			values.put(DatabaseHelper.COL_COMMENT_ASSAULT2,
					this.commentsAssault2);
		}

		if (this.lastOne != null) {
			values.put(DatabaseHelper.COL_IS_LAST_ONE, this.lastOne);
		}

		if (this.nightFight != null) {
			values.put(DatabaseHelper.COL_IS_NIGHT_FIGHT, this.nightFight);
		}

		values.put(DatabaseHelper.COL_NUM, this.num);
		return values;

	}

}
