package com.headbangers.reportmaker.dao.impl;

import com.headbangers.reportmaker.dao.DatabaseHelper;

import android.database.sqlite.SQLiteDatabase;

public class GenericDaoImpl implements GenericDao {

	protected SQLiteDatabase db;
	protected DatabaseHelper bdd;

	@Override
	public void close() {
		if (db != null) {
			db.close();
		}
	}

	@Override
	public void open() {
		if (db == null || !db.isOpen()) {
			db = bdd.getWritableDatabase();
		}
	}
}
