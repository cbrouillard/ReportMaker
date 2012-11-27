package com.headbangers.reportmaker.service;

import java.util.Random;

import com.headbangers.reportmaker.R;

import android.app.Activity;

public class BattleNameGenerator {

	private String[] part1;
	private String[] planets;

	public BattleNameGenerator(Activity context) {
		this.part1 = context.getResources().getStringArray(
				R.array.battleName_random_part1);
		this.planets = context.getResources().getStringArray(R.array.planets);
	}

	public String generateCoolRandomName() {

		Random rand = new Random();

		int part1Num = rand.nextInt(part1.length);
		int planet = rand.nextInt(planets.length);

		return part1[part1Num] + planets[planet];
	}

}
