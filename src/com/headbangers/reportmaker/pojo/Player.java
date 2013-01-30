package com.headbangers.reportmaker.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "class" })
public class Player {

	private String name;
	private String race;
	private String armyComments;

	public String getName() {
		return name;
	}

	public String getRace() {
		return race;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getArmyComments() {
		return armyComments;
	}

	public void setArmyComments(String armyComments) {
		this.armyComments = armyComments;
	}

}
