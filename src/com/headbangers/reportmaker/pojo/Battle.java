package com.headbangers.reportmaker.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import android.database.Cursor;

import com.headbangers.reportmaker.dao.DatabaseHelper;

public class Battle {

	private Long id;
	private String name;
	private Integer format;
	private Date date;
	private String dateFormated;

	public Battle(Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COL_ID));
		this.name = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_NAME));
		this.format = cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.COL_FORMAT));

		String dateInDB = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_DATE));

		try {
			this.date = DateFormat.getDateInstance().parse(dateInDB);
			this.dateFormated = DateFormat.getDateInstance().format(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public Battle() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFormat() {
		return format;
	}

	public void setFormat(Integer format) {
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

}
