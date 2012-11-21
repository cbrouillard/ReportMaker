package com.headbangers.reportmaker.pojo;

public class Player {

	private String name;
	private String race;

	public String getName() {
		return name;
	}

	public String getRace() {
		return race;
	}

	public void setName(String name) {
		if (name.isEmpty()){
			// Génération d'un nom générique
			name = "Joueur";
		}
		this.name = name;
	}

	public void setRace(String race) {
		this.race = race;
	}

}
