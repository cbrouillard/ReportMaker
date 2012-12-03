package com.headbangers.reportmaker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.pojo.Battle;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class DroidTextPDFService implements IPDFService {

	private static Font catFont = new Font(Font.HELVETICA, 18, Font.BOLD);
	private static Font subFont = new Font(Font.HELVETICA, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.HELVETICA, 12, Font.BOLD);

	private BattleDao dao;

	private FilesystemService fs = new FilesystemService();

	public void setDao(BattleDao dao) {
		this.dao = dao;
	}

	@Override
	public String exportBattle(Long battleId) {
		Battle battle = dao.findBattleById(battleId);

		if (battle == null) {
			return null;
		}

		Document document = new Document();

		try {

			File documentFile = new File(fs.getRootBattle(battle), battle
					.getName().replace(" ", "_") + ".pdf");
			PdfWriter.getInstance(document, new FileOutputStream(documentFile));

			document.open();

			document.add(new Paragraph(battle.getName(), catFont));

			document.close();

			return documentFile.getAbsolutePath();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
