package com.headbangers.reportmaker.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.graphics.Bitmap;

import com.headbangers.reportmaker.ImageHelper;
import com.headbangers.reportmaker.async.GeneratePDFAsyncLoader;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Turn;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class DroidTextPDFService implements IPDFService {

	private static Font catFont = new Font(Font.HELVETICA, 18, Font.BOLD);
	private static Font subFont = new Font(Font.HELVETICA, 14, Font.BOLD);
	private static Font normalBold = new Font(Font.HELVETICA, 12, Font.BOLD);
	private static Font normal = new Font(Font.HELVETICA, 12, Font.NORMAL);

	private BattleDao dao;

	private FilesystemService fs = new FilesystemService();

	public void setDao(BattleDao dao) {
		this.dao = dao;
	}

	@Override
	public void exportBattleAsync(Long battleId, Activity fromContext) {

		GeneratePDFAsyncLoader asyncLoader = new GeneratePDFAsyncLoader(
				fromContext, this);
		asyncLoader.setDialogText("PDF en cours de génération...");
		asyncLoader.execute(battleId);

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
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(documentFile));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			document.add(new Paragraph(battle.getName(), catFont));
			document.add(new Paragraph(battle.getDateFormated(), normal));
			document.add(new Paragraph(battle.getOne().getName() + " vs "
					+ battle.getTwo().getName(), normal));
			if (battle.getOne().getRace() != null
					&& !battle.getOne().getRace().isEmpty()
					&& battle.getTwo().getRace() != null
					&& !battle.getTwo().getRace().isEmpty()) {

				document.add(new Paragraph(battle.getOne().getRace() + " vs "
						+ battle.getTwo().getRace(), normal));
			}

			drawLine(cb, 715);
			addEmptyLine(document, 2);

			// Scénario
			Paragraph scenario = new Paragraph();
			scenario.add(new Chunk("Scénario de la partie : ", normalBold));
			scenario.add(new Chunk(battle.getInfos().getScenario(), normal));
			document.add(scenario);

			// Format en points
			Paragraph format = new Paragraph();
			format.add(new Chunk("Format de la partie : ", normalBold));
			format.add(new Chunk(battle.getFormat() + "", normal));
			document.add(format);

			// Traits de seigneur de guerre
			Paragraph lord1 = new Paragraph();
			lord1.add(new Chunk("Seigneur de guerre de "
					+ battle.getOne().getName() + " : ", normalBold));
			lord1.add(new Chunk(battle.getInfos().getLordCapacity1(), normal));
			document.add(lord1);

			Paragraph lord2 = new Paragraph();
			lord2.add(new Chunk("Seigneur de guerre de "
					+ battle.getTwo().getName() + " : ", normalBold));
			lord2.add(new Chunk(battle.getInfos().getLordCapacity2(), normal));
			document.add(lord2);

			// Photo de la table
			Bitmap photo = ImageHelper.photoAsPDFBitmap(
					fs.getRootBattle(battle),
					BattleInformationsFragment.TABLE_PHOTO_NAME);
			if (photo != null) {
				document.add(new Paragraph("Table de jeu : ", normalBold));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
						100 /* Ratio */, stream);
				Image jpg = Image.getInstance(stream.toByteArray());
				document.add(jpg);
			}

			// Deploiement
			generateDeployment(document, cb, battle);

			// Tours
			for (Turn turn : battle.getTurns()) {
				generateTurn(document, cb, battle, turn);

				if (turn.isLastOne()) {
					break;
				}
			}

			document.close();

			return documentFile.getAbsolutePath();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void generateTurn(Document document, PdfContentByte cb,
			Battle battle, Turn turn) throws DocumentException {
		Bitmap photo = null;
		document.newPage();

		document.add(new Paragraph("Tour " + turn.getNum(), catFont));
		drawLine(cb, 765);
		addEmptyLine(document, 1);
	}

	private void generateDeployment(Document document, PdfContentByte cb,
			Battle battle) throws DocumentException, MalformedURLException,
			IOException {
		Bitmap photo = null;

		document.newPage();

		document.add(new Paragraph("Déploiement", catFont));
		drawLine(cb, 765);
		addEmptyLine(document, 1);

		int firstPlayer = battle.getInfos().getFirstPlayer();

		if (firstPlayer == 0) {
			document.add(new Paragraph(battle.getOne().getName()
					+ " commence !", subFont));
			photo = ImageHelper.photoAsPDFBitmap(fs.getRootBattle(battle),
					BattleInformationsFragment.DEPLOYMENT1_PHOTO_NAME);
			if (photo != null) {
				document.add(new Paragraph("Déploiement de "
						+ battle.getOne().getName() + " :", normalBold));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
						100 /* Ratio */, stream);
				Image jpg = Image.getInstance(stream.toByteArray());
				document.add(jpg);
			}

			photo = ImageHelper.photoAsPDFBitmap(fs.getRootBattle(battle),
					BattleInformationsFragment.DEPLOYMENT2_PHOTO_NAME);
			if (photo != null) {
				document.add(new Paragraph("Déploiement de "
						+ battle.getTwo().getName() + " :", normalBold));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
						100 /* Ratio */, stream);
				Image jpg = Image.getInstance(stream.toByteArray());
				document.add(jpg);
			}
		} else {
			document.add(new Paragraph(battle.getTwo().getName()
					+ " commence !", subFont));
			photo = ImageHelper.photoAsPDFBitmap(fs.getRootBattle(battle),
					BattleInformationsFragment.DEPLOYMENT2_PHOTO_NAME);
			if (photo != null) {
				document.add(new Paragraph("Déploiement de "
						+ battle.getTwo().getName() + " :", normalBold));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
						100 /* Ratio */, stream);
				Image jpg = Image.getInstance(stream.toByteArray());
				document.add(jpg);
			}

			photo = ImageHelper.photoAsPDFBitmap(fs.getRootBattle(battle),
					BattleInformationsFragment.DEPLOYMENT1_PHOTO_NAME);
			if (photo != null) {
				document.add(new Paragraph("Déploiement de "
						+ battle.getOne().getName() + " :", normalBold));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
						100 /* Ratio */, stream);
				Image jpg = Image.getInstance(stream.toByteArray());
				document.add(jpg);
			}
		}

		// TODO combat nocturne ?

		// TODO commentaires

	}

	private void drawLine(PdfContentByte cb, int y) {
		cb.setLineWidth(1f);
		cb.moveTo(20, y);
		cb.lineTo(575, y);
		cb.stroke();
	}

	private void addEmptyLine(Document document, int number)
			throws DocumentException {
		for (int i = 0; i < number; i++) {
			document.add(new Paragraph(" "));
		}
	}
}
