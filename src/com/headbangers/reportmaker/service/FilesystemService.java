package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.headbangers.reportmaker.pojo.Battle;

public class FilesystemService {

	private static FilesystemService instance;

	public static FilesystemService getInstance() {
		if (instance == null) {
			instance = new FilesystemService();
		}
		return instance;
	}

	private FilesystemService() {

	}

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

	public String determineNewExtraPhotoName(Battle battle, final int turn) {

		File rootBattle = getRootBattle(battle);

		String[] extrasPhotos = rootBattle.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith("extra_t" + turn);
			}
		});

		return "extra_t" + turn + "_" + extrasPhotos.length + ".jpg";
	}

	public void deleteBattleDirectory(Long id) {

		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");
		File battleDirectory = new File(appRoot, "battle-" + id);

		if (battleDirectory.exists()) {
			battleDirectory.delete();
		}
	}

	public List<String> findAllExtrasPhotosPath(Battle battle, final int turn) {

		File rootBattle = getRootBattle(battle);

		String[] extrasPhotos = rootBattle.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith("extra_t" + turn);
			}
		});

		List<String> absolutePath = new ArrayList<String>();
		for (String extraPath : extrasPhotos) {
			absolutePath.add(rootBattle.getAbsolutePath() + "/" + extraPath);
		}

		return absolutePath;
	}

	public String determineNextPhotoName(Battle battle, String photoName) {
		File rootBattle = getRootBattle(battle);

		// photoName = table.jpg
		final String nameWithoutExt = photoName.substring(0,
				photoName.indexOf(".jpg"));
		// nameWithoutExt = table

		String[] photos = rootBattle.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith(nameWithoutExt)
						&& filename.endsWith(".jpg");
			}
		});

		if (photos.length > 0) {
			return nameWithoutExt + "_" + photos.length + ".jpg";
		}

		return nameWithoutExt + ".jpg";
	}
}
