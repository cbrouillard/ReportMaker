package com.headbangers.reportmaker.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static int version = 1;

	public static final String TABLE_BATTLE = "battle";
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_DATE = "date_creation";
	public static final String COL_FORMAT = "format";
	public static final String COL_PLAYERONE = "player_one";
	public static final String COL_PLAYERTWO = "player_two";
	public static final String COL_RACEONE = "race_one";
	public static final String COL_RACETWO = "race_two";

	public static final String COL_INFO_COMMENTS = "info_comments";
	public static final String COL_INFO_DEPLOYMENT = "info_deployment";
	public static final String COL_INFO_SCENARIO = "info_scenario";
	public static final String COL_INFO_WHOSTART = "info_whostart";
	public static final String COL_INFO_LORD1_CAPACITY = "info_lordone";
	public static final String COL_INFO_LORD2_CAPACITY = "info_lordtwo";

	public static final String[] ALL_COLUMNS = new String[] { COL_ID, COL_NAME,
			COL_DATE, COL_FORMAT, COL_PLAYERONE, COL_PLAYERTWO, COL_RACEONE,
			COL_RACETWO, COL_INFO_COMMENTS, COL_INFO_DEPLOYMENT,
			COL_INFO_LORD1_CAPACITY, COL_INFO_LORD2_CAPACITY,
			COL_INFO_SCENARIO, COL_INFO_WHOSTART };

	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_BATTLE
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME
			+ " TEXT NOT NULL, " + COL_DATE + " DATE NOT NULL, " + COL_FORMAT
			+ " INTEGER NULL, " + COL_PLAYERONE + " TEXT NULL, "
			+ COL_PLAYERTWO + " TEXT NULL, " + COL_RACEONE + " TEXT NULL, "
			+ COL_RACETWO + " TEXT NULL, " + COL_INFO_COMMENTS + " TEXT NULL, "
			+ COL_INFO_DEPLOYMENT + " TEXT NULL, " + COL_INFO_LORD1_CAPACITY
			+ " TEXT NULL, " + COL_INFO_LORD2_CAPACITY + " TEXT NULL, "
			+ COL_INFO_SCENARIO + " TEXT NULL, " + COL_INFO_WHOSTART
			+ " INTEGER NULL);";

	public DatabaseHelper(Context context) {
		this(context, "battle.db", null, version);
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATTLE + ";");
		onCreate(db);
	}

}
