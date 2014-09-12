package com.headbangers.reportmaker.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.content.ContentValues;
import android.database.Cursor;

import com.headbangers.reportmaker.dao.DatabaseHelper;

@JsonIgnoreProperties({ "class" })
public class Turn {

	private Integer num;

	private String commentsMove1;
	private String commentsMove2;

	private String commentsShoot1;
	private String commentsShoot2;

	private String commentsAssault1;
	private String commentsAssault2;

	private String commentsCharge1;
	private String commentsCharge2;

	private String commentsPower1;
	private String commentsPower2;

	private String comments1;
	private String comments2;

	private Boolean lastOne;
	private Boolean nightFight;

	public Turn(Cursor cursor) {
		this.comments1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT1));
		this.comments2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT2));

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

		this.commentsCharge1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_CHARGE1));
		this.commentsCharge2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_CHARGE2));

		this.commentsPower1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_POWER1));
		this.commentsPower2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_COMMENT_POWER2));

		this.lastOne = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_IS_LAST_ONE)) == 1 ? true
				: false;
		this.nightFight = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_IS_NIGHT_FIGHT)) == 1 ? true
				: false;

		this.num = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_NUM));
	}

	public Turn() {
		nightFight = false;
		lastOne = false;
	}

	public String getCommentsCharge1() {
		return commentsCharge1;
	}

	public void setCommentsCharge1(String commentsCharge1) {
		this.commentsCharge1 = commentsCharge1;
	}

	public String getCommentsCharge2() {
		return commentsCharge2;
	}

	public void setCommentsCharge2(String commentsCharge2) {
		this.commentsCharge2 = commentsCharge2;
	}

	public String getCommentsPower1() {
		return commentsPower1;
	}

	public void setCommentsPower1(String commentsPower1) {
		this.commentsPower1 = commentsPower1;
	}

	public String getCommentsPower2() {
		return commentsPower2;
	}

	public void setCommentsPower2(String commentsPower2) {
		this.commentsPower2 = commentsPower2;
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

	public String getComments1() {
		return comments1;
	}

	public String getComments2() {
		return comments2;
	}

	public void setComments1(String comments1) {
		this.comments1 = comments1;
	}

	public void setComments2(String comments2) {
		this.comments2 = comments2;
	}

	public ContentValues asContentValues() {
		ContentValues values = new ContentValues();

		if (this.comments1 != null) {
			values.put(DatabaseHelper.COL_COMMENT1, this.comments1);
		}

		if (this.comments2 != null) {
			values.put(DatabaseHelper.COL_COMMENT2, this.comments2);
		}

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
		if (this.commentsCharge1 != null) {
			values.put(DatabaseHelper.COL_COMMENT_CHARGE1, this.commentsCharge1);
		}
		if (this.commentsCharge2 != null) {
			values.put(DatabaseHelper.COL_COMMENT_CHARGE2, this.commentsCharge2);
		}
		if (this.commentsPower1 != null) {
			values.put(DatabaseHelper.COL_COMMENT_POWER1, this.commentsPower1);
		}
		if (this.commentsPower2 != null) {
			values.put(DatabaseHelper.COL_COMMENT_POWER2, this.commentsPower2);
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

	public String getComments(int numPlayer) {
		return numPlayer == 1 ? getComments1() : getComments2();
	}

	public String getCommentsMove(int numPlayer) {
		return numPlayer == 1 ? getCommentsMove1() : getCommentsMove2();
	}

	public String getCommentsShoot(int numPlayer) {
		return numPlayer == 1 ? getCommentsShoot1() : getCommentsShoot2();
	}

	public String getCommentsAssault(int numPlayer) {
		return numPlayer == 1 ? getCommentsAssault1() : getCommentsAssault2();
	}

	public String getCommentsMove1() {
		return commentsMove1;
	}

	public String getCommentsMove2() {
		return commentsMove2;
	}

	public String getCommentsShoot1() {
		return commentsShoot1;
	}

	public String getCommentsShoot2() {
		return commentsShoot2;
	}

	public String getCommentsAssault1() {
		return commentsAssault1;
	}

	public String getCommentsAssault2() {
		return commentsAssault2;
	}

	public void setCommentsMove1(String commentsMove1) {
		this.commentsMove1 = commentsMove1;
	}

	public void setCommentsMove2(String commentsMove2) {
		this.commentsMove2 = commentsMove2;
	}

	public void setCommentsShoot1(String commentsShoot1) {
		this.commentsShoot1 = commentsShoot1;
	}

	public void setCommentsShoot2(String commentsShoot2) {
		this.commentsShoot2 = commentsShoot2;
	}

	public void setCommentsAssault1(String commentsAssault1) {
		this.commentsAssault1 = commentsAssault1;
	}

	public void setCommentsAssault2(String commentsAssault2) {
		this.commentsAssault2 = commentsAssault2;
	}

	public String getCommentsCharge(int numPlayer) {
		return numPlayer == 1 ? getCommentsCharge1() : getCommentsCharge2();
	}

	public String getCommentsPower(int numPlayer) {
		return numPlayer == 1 ? getCommentsPower1() : getCommentsPower2();
	}

}
