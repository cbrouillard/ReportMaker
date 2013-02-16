package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.tools.FileTool;

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
		File thumbs = new File(battleDirectory, ".thumbs");
		thumbs.mkdirs();

		return battleDirectory;

	}

	public File getRootBattle(Battle battle) {
		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");

		if (battle != null) {
			File dir = new File(appRoot, "battle-" + battle.getId());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			File temp = new File(appRoot, "temp");
			if (!temp.exists()) {
				temp.mkdir();
			}
			return temp;
		}
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
		// photoName = table.jpg
		final String nameWithoutExt = photoName.substring(0,
				photoName.indexOf(".jpg"));
		// nameWithoutExt = table

		String[] photos = getAllFilenameLike(battle, photoName);

		if (photos != null && photos.length > 0) {
			return nameWithoutExt + "_" + photos.length + ".jpg";
		}

		return nameWithoutExt + ".jpg";
	}

	public String[] getAllFilenameLike(Battle battle, String originalFilename) {
		File rootBattle = getRootBattle(battle);

		final String nameWithoutExt = originalFilename.substring(0,
				originalFilename.indexOf(".jpg"));
		// nameWithoutExt = table

		String[] photos = rootBattle.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith(nameWithoutExt)
						&& filename.endsWith(".jpg");
			}
		});

		return photos;
	}

	public void moveTempPhotos(Long idInserted) {
		File androidRoot = Environment.getExternalStorageDirectory();
		File appRoot = new File(androidRoot, "reportmaker");

		File battleDir = new File(appRoot, "battle-" + idInserted);
		File tempDir = new File(appRoot, "temp");

		if (!tempDir.exists()) {
			tempDir.mkdir();
		}

		try {
			FileTool.delete(new File(tempDir, "thumbs"));

			for (File ph : tempDir.listFiles()) {
				FileTool.copyFile(ph, new File(battleDir, ph.getName()));
				FileTool.delete(ph);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
