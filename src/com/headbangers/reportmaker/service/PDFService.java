package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Turn;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;

public class PDFService {

	private BattleDao dao;

	private FilesystemService fs = new FilesystemService();

	public void setDao(BattleDao dao) {
		this.dao = dao;
	}

	private static int DEFAULT_LEFT_MARGE = 20;

	/**
	 * Exporte la bataille au format PDF.
	 * 
	 * @param battleId
	 *            id de la bataille séléctionnée. Elle sera rechargée depuis la
	 *            base afin d'être sur de la fraicheur des données.
	 * @return le nom du fichier pdf généré.
	 */
	public String exportBattle(Long battleId) {
		Battle battle = dao.findBattleById(battleId);

		if (battle == null) {
			return null;
		}

		// Export PDF !
		PDFWriter mPDFWriter = new PDFWriter(PaperSize.FOLIO_WIDTH,
				PaperSize.FOLIO_HEIGHT);

		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA_BOLD);
		mPDFWriter.addText(DEFAULT_LEFT_MARGE, 800, 26, battle.getName());
		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA);
		mPDFWriter.addText(DEFAULT_LEFT_MARGE, 770, 16, "Rapport de bataille");
		mPDFWriter.addText(DEFAULT_LEFT_MARGE, 740, 16, battle.getOne()
				.getName() + " vs " + battle.getTwo().getName());

		mPDFWriter.addLine(DEFAULT_LEFT_MARGE, 720, PaperSize.FOLIO_WIDTH
				- DEFAULT_LEFT_MARGE, 720);

		for (Turn turn : battle.getTurns()) {

			mPDFWriter.newPage();
			mPDFWriter.setFont(StandardFonts.SUBTYPE,
					StandardFonts.HELVETICA_BOLD);
			mPDFWriter.addText(DEFAULT_LEFT_MARGE, 850, 26,
					"Tour " + turn.getNum());

			if (turn.isLastOne()) {
				break;
			}
		}

		File rootBattle = fs.getRootBattle(battle);
		File pdfFile = new File(rootBattle, battle.getName().replace(" ", "_")
				+ ".pdf");

		FileOutputStream pdfOs;
		try {

			pdfOs = new FileOutputStream(pdfFile);
			pdfOs.write(mPDFWriter.asString().getBytes());
			pdfOs.close();

			return pdfFile.getAbsolutePath();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("PdfService", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("PdfService", e.getMessage());
		}

		return null;
	}
}
