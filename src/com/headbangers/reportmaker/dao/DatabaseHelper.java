package com.headbangers.reportmaker.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static int version = 2;

	public static final String TABLE_BATTLE = "battle";
	public static final String TABLE_TURN = "turn";

	public static final String COL_BATTLE_ID = "battle_id";
	public static final String COL_COMMENT_MOVE1 = "comment_move1";
	public static final String COL_COMMENT_MOVE2 = "comment_move2";
	public static final String COL_COMMENT_SHOOT1 = "comment_shoot1";
	public static final String COL_COMMENT_SHOOT2 = "comment_shoot2";
	public static final String COL_COMMENT_ASSAULT1 = "comment_assault1";
	public static final String COL_COMMENT_ASSAULT2 = "comment_assault2";
	public static final String COL_COMMENT1 = "comment_1";
	public static final String COL_COMMENT2 = "comment_2";
	public static final String COL_IS_LAST_ONE = "last_turn";
	public static final String COL_IS_NIGHT_FIGHT = "night_fight";
	public static final String COL_NUM = "num_turn";

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

	public static final String[] ALL_BATTLE_COLUMNS = new String[] { COL_ID,
			COL_NAME, COL_DATE, COL_FORMAT, COL_PLAYERONE, COL_PLAYERTWO,
			COL_RACEONE, COL_RACETWO, COL_INFO_COMMENTS, COL_INFO_DEPLOYMENT,
			COL_INFO_LORD1_CAPACITY, COL_INFO_LORD2_CAPACITY,
			COL_INFO_SCENARIO, COL_INFO_WHOSTART };

	public static final String[] ALL_TURN_COLUMNS = new String[] {
			COL_COMMENT_MOVE1, COL_COMMENT_MOVE2, COL_COMMENT_SHOOT1,
			COL_COMMENT_SHOOT2, COL_COMMENT_ASSAULT1, COL_COMMENT_ASSAULT2,
			COL_IS_LAST_ONE, COL_NUM, COL_IS_NIGHT_FIGHT, COL_COMMENT1,
			COL_COMMENT2 };

	private static final String CREATE_BDD_TABLE_BATTLE = "CREATE TABLE "
			+ TABLE_BATTLE + " (" + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME
			+ " TEXT NOT NULL, " + COL_DATE + " DATE NOT NULL, " + COL_FORMAT
			+ " TEXT NULL, " + COL_PLAYERONE + " TEXT NULL, " + COL_PLAYERTWO
			+ " TEXT NULL, " + COL_RACEONE + " TEXT NULL, " + COL_RACETWO
			+ " TEXT NULL, " + COL_INFO_COMMENTS + " TEXT NULL, "
			+ COL_INFO_DEPLOYMENT + " TEXT NULL, " + COL_INFO_LORD1_CAPACITY
			+ " TEXT NULL, " + COL_INFO_LORD2_CAPACITY + " TEXT NULL, "
			+ COL_INFO_SCENARIO + " TEXT NULL, " + COL_INFO_WHOSTART
			+ " INTEGER NULL);";

	private static final String CREATE_BDD_TABLE_TURN = "CREATE TABLE "
			+ TABLE_TURN + " (" + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BATTLE_ID
			+ " INTEGER NOT NULL, " + COL_COMMENT_MOVE1 + " TEXT NULL, "
			+ COL_COMMENT_MOVE2 + " TEXT NULL, " + COL_COMMENT_SHOOT1
			+ " TEXT NULL, " + COL_COMMENT_SHOOT2 + " TEXT NULL, "
			+ COL_COMMENT_ASSAULT1 + " TEXT NULL, " + COL_COMMENT1
			+ " TEXT NULL," + COL_COMMENT2 + " TEXT NULL,"
			+ COL_COMMENT_ASSAULT2 + " TEXT NULL, " + COL_IS_LAST_ONE
			+ " BOOLEAN NULL, " + COL_NUM + " INTEGER NOT NULL, "
			+ COL_IS_NIGHT_FIGHT + " BOOLEAN NULL, " + "FOREIGN KEY("
			+ COL_BATTLE_ID + ") REFERENCES " + TABLE_BATTLE + "(" + COL_ID
			+ ")" + ");";

	public DatabaseHelper(Context context) {
		this(context, "battle.db", null, version);
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD_TABLE_BATTLE);
		db.execSQL(CREATE_BDD_TABLE_TURN);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		try {
			db.execSQL(CREATE_BDD_TABLE_BATTLE.replace("battle",
					"TABLE_BATTLE_TEMP"));
			db.execSQL("INSERT INTO TABLE_BATTLE_TEMP SELECT * FROM battle");
			db.execSQL("DROP TABLE battle");
			db.execSQL("ALTER TABLE TABLE_BATTLE_TEMP RENAME TO battle");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

}
