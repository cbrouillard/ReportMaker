package com.headbangers.reportmaker.pojo;

public class Turn {

	private String commentsMove1;
	private String commentsMove2;

	private String commentsShoot1;
	private String commentsShoot2;

	private String commentsAssault1;
	private String commentsAssault2;

	private boolean lastOne;

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

	public void setLastOne(boolean isLastOne) {
		this.lastOne = isLastOne;
	}

	public boolean isLastOne() {
		return lastOne;
	}

}
