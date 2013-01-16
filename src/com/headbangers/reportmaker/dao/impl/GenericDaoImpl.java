package com.headbangers.reportmaker.dao.impl;

import android.database.sqlite.SQLiteDatabase;

import com.headbangers.reportmaker.dao.DatabaseHelper;

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
