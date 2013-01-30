package com.headbangers.reportmaker.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.content.ContentValues;
import android.database.Cursor;

import com.headbangers.reportmaker.dao.DatabaseHelper;

@JsonIgnoreProperties({ "class" })
public class Battle {

	private Long id;

	private String name;
	private String format;
	private Date date;
	private String dateFormated;

	private Player one;
	private Player two;

	private Informations infos;
	private List<Turn> turns;

	public Battle(Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COL_ID));
		this.name = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_NAME));
		this.format = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_FORMAT));

		String dateInDB = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_DATE));

		try {
			this.date = DateFormat.getDateInstance().parse(dateInDB);
			this.dateFormated = DateFormat.getDateInstance().format(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.one = new Player();
		this.one.setName(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_PLAYERONE)));
		this.one.setRace(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_RACEONE)));
		this.one.setArmyComments(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_LISTONE)));

		this.two = new Player();
		this.two.setName(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_PLAYERTWO)));
		this.two.setRace(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_RACETWO)));
		this.two.setArmyComments(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_LISTTWO)));

		this.infos = new Informations(cursor);
	}

	public Battle() {
		this.one = new Player();
		this.two = new Player();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public Long getId() {
		return id;
	}

	public String getDateFormated() {
		return this.dateFormated;
	}

	public void setOne(Player one) {
		this.one = one;
	}

	public void setTwo(Player two) {
		this.two = two;
	}

	public Player getOne() {
		return one;
	}

	public Player getTwo() {
		return two;
	}

	public Informations getInfos() {
		return infos;
	}

	public void setInfos(Informations infos) {
		this.infos = infos;
	}

	public List<Turn> getTurns() {
		return turns;
	}

	public void setTurns(List<Turn> turns) {
		this.turns = turns;
	}

	public Turn getTurn(int numTurn) {

		for (Turn turn : turns) {
			if (turn.getNum().equals(numTurn)) {
				return turn;
			}
		}

		return new Turn();

	}

	public Player getPlayer(int firstPlayer) {

		if (firstPlayer == 0) {
			return one;
		}

		return two;
	}

	public Player getOtherPlayer(int firstPlayer) {
		if (firstPlayer == 0) {
			return two;
		}
		return one;
	}

	public ContentValues asContentValues() {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.COL_NAME, this.name);
		if (format != null) {
			values.put(DatabaseHelper.COL_FORMAT, this.format);
		}
		if (date != null) {
			values.put(DatabaseHelper.COL_DATE, DateFormat.getDateInstance()
					.format(this.date));
		}
		if (one != null) {
			values.put(DatabaseHelper.COL_PLAYERONE, one.getName());
			values.put(DatabaseHelper.COL_RACEONE, one.getRace());
			values.put(DatabaseHelper.COL_LISTONE, one.getArmyComments());
		}
		if (two != null) {
			values.put(DatabaseHelper.COL_PLAYERTWO, two.getName());
			values.put(DatabaseHelper.COL_RACETWO, two.getRace());
			values.put(DatabaseHelper.COL_LISTTWO, two.getArmyComments());
		}

		return values;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
