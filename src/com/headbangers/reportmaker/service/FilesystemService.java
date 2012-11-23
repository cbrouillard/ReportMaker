package com.headbangers.reportmaker.service;

import java.io.File;

import android.os.Environment;

import com.headbangers.reportmaker.pojo.Battle;

public class FilesystemService {

	public File createRootBattle(Long idBattle) {

		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");
		File battleDirectory = new File(appRoot, "battle-" + idBattle);

		battleDirectory.mkdirs();

		return battleDirectory;

	}

	public File getRootBattle(Battle battle) {
		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");
		File battleDirectory = new File(appRoot, "battle-" + battle.getId());

		return battleDirectory;
	}

}
