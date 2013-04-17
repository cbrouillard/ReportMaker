package com.headbangers.reportmaker.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.util.Log;

public class Compress {
	private static final int BUFFER = 2048;

	private String[] _files;
	private String _zipFile;
	private File rootDir;

	public Compress(File directory, String[] files, String zipFile) {
		_files = files;
		_zipFile = zipFile;
		this.rootDir = directory;
	}

	public void zip() {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(_zipFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));

			byte data[] = new byte[BUFFER];

			for (int i = 0; i < _files.length; i++) {
				Log.v("Compress", "Adding: " + _files[i]);

				File toAdd = new File(rootDir, _files[i]);
				if (!toAdd.isDirectory()) {
					FileInputStream fi = new FileInputStream(toAdd);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(_files[i].substring(_files[i]
							.lastIndexOf("/") + 1));
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				} else {
					// un dossier ?
				}
			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public File getZipFile() {
		return new File(_zipFile);
	}

}
