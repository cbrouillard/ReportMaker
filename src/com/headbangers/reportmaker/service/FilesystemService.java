package com.headbangers.reportmaker.service;

import java.io.File;

import android.os.Environment;

public class FilesystemService {

	public File createRootBattle(Long idBattle) {

		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");
		File battleDirectory = new File(appRoot, "battle-" + idBattle);

		battleDirectory.mkdirs();

		return battleDirectory;

	}

}
