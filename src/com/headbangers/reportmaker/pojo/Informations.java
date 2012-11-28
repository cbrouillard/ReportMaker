package com.headbangers.reportmaker.pojo;

import com.headbangers.reportmaker.dao.DatabaseHelper;

import android.content.ContentValues;
import android.database.Cursor;

public class Informations {

	private String deploymentType;
	private String scenario;
	private Integer firstPlayer;
	private String lordCapacity1;
	private String lordCapacity2;
	private String comments;

	public Informations() {
	}

	public Informations(Cursor cursor) {
		this.comments = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_COMMENTS));
		this.deploymentType = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_DEPLOYMENT));
		this.firstPlayer = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_WHOSTART));
		this.lordCapacity1 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_LORD1_CAPACITY));
		this.lordCapacity2 = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_LORD2_CAPACITY));
		this.scenario = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_INFO_SCENARIO));
	}

	public String getDeploymentType() {
		return deploymentType;
	}

	public void setDeploymentType(String deploymentType) {
		this.deploymentType = deploymentType;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public int getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(Integer firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public String getLordCapacity1() {
		return lordCapacity1;
	}

	public void setLordCapacity1(String lordCapacity1) {
		this.lordCapacity1 = lordCapacity1;
	}

	public String getLordCapacity2() {
		return lordCapacity2;
	}

	public void setLordCapacity2(String lordCapacity2) {
		this.lordCapacity2 = lordCapacity2;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ContentValues asContentValues() {
		ContentValues values = new ContentValues();

		if (this.comments != null) {
			values.put(DatabaseHelper.COL_INFO_COMMENTS, this.comments);
		}

		if (this.deploymentType != null) {
			values.put(DatabaseHelper.COL_INFO_DEPLOYMENT, this.deploymentType);
		}

		if (this.firstPlayer != null) {
			values.put(DatabaseHelper.COL_INFO_WHOSTART, this.firstPlayer);
		}

		if (this.lordCapacity1 != null) {
			values.put(DatabaseHelper.COL_INFO_LORD1_CAPACITY,
					this.lordCapacity1);
		}

		if (this.lordCapacity2 != null) {
			values.put(DatabaseHelper.COL_INFO_LORD2_CAPACITY,
					this.lordCapacity2);
		}

		if (this.scenario != null) {
			values.put(DatabaseHelper.COL_INFO_SCENARIO, this.scenario);
		}

		return values;
	}

}
