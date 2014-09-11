package com.headbangers.reportmaker.pojo;

public enum GameType {

	// never change this order.
	WARHAMMER_40K, WARHAMMER_BATTLE, XWING;

	// A value off "emptyNullSafe"
	public static GameType getFromString(String value) {
		if (value != null && !value.trim().equals("")) {
			return valueOf(value);
		} else {
			return WARHAMMER_40K;
		}
	}

	public static String[] names() {
		String[] names = new String[3];
		names[0] = "Warhammer 40k";
		names[1] = "Warhammer Battle";
		names[2] = "X-Wing";

		return names;
	}

	public static GameType getFromAppName(String appName) {
		if (appName.equals("Warhammer 40k")) {
			return WARHAMMER_40K;
		} else if (appName.equals("Warhammer Battle")) {
			return WARHAMMER_BATTLE;
		} else if (appName.equals("X-Wing")) {
			return XWING;
		}

		return WARHAMMER_40K;
	}

	public static GameType fromPosition(int position) {
		switch (position) {
		case 0:
			return WARHAMMER_40K;
		case 1:
			return WARHAMMER_BATTLE;
		case 2:
			return XWING;
		default:
			return WARHAMMER_40K;
		}
	}

}
