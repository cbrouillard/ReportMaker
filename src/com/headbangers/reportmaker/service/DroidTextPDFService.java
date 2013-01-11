package com.headbangers.reportmaker.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.async.GeneratePDFAsyncLoader;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.exception.TwrException;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.fragment.TurnFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.tools.ImageHelper;
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
	private Activity context;

	public void setDao(BattleDao dao) {
		this.dao = dao;
	}

	public DroidTextPDFService(Activity context) {
		this.context = context;
	}

	private String getString(int stringId) {
		return this.context.getResources().getString(stringId);
	}

	@Override
	public void exportBattleAsync(Long battleId, Activity fromContext) {

		GeneratePDFAsyncLoader asyncLoader = new GeneratePDFAsyncLoader(
				fromContext, this);
		asyncLoader.setDialogText(getString(R.string.pdf_generating));
		asyncLoader.execute(battleId);

	}

	@Override
	public String exportBattle(Long battleId) {
		Battle battle = dao.findBattleById(battleId);

		if (battle == null) {
			throw new TwrException(new IllegalArgumentException(
					"Battle est null."));
		}

		Document document = new Document();

		try {

			File documentFile = new File(fs.getRootBattle(battle), battle
					.getName().replace(" ", "_").replace("\n", "")
					+ ".pdf");
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
			scenario.add(new Chunk(getString(R.string.pdf_game_scenario)
					+ " : ", normalBold));
			scenario.add(new Chunk(battle.getInfos().getScenario() + "", normal));
			document.add(scenario);

			// Format en points
			Paragraph format = new Paragraph();
			format.add(new Chunk(getString(R.string.pdf_game_format) + " : ",
					normalBold));
			format.add(new Chunk(battle.getFormat() + "", normal));
			document.add(format);

			// Traits de seigneur de guerre
			Paragraph lord1 = new Paragraph();
			lord1.add(new Chunk(getString(R.string.pdf_warlord) + " "
					+ battle.getOne().getName() + " : ", normalBold));
			lord1.add(new Chunk(battle.getInfos().getLordCapacity1() + "",
					normal));
			document.add(lord1);

			Paragraph lord2 = new Paragraph();
			lord2.add(new Chunk(getString(R.string.pdf_warlord) + " "
					+ battle.getTwo().getName() + " : ", normalBold));
			lord2.add(new Chunk(battle.getInfos().getLordCapacity2() + "",
					normal));
			document.add(lord2);

			// Photo de la table
			addPhoto(document, battle,
					BattleInformationsFragment.TABLE_PHOTO_NAME,
					getString(R.string.pdf_table) + " : ", null);

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
			e.printStackTrace();
			throw new TwrException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new TwrException(e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new TwrException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TwrException(e);
		}
	}

	private void generateTurn(Document document, PdfContentByte cb,
			Battle battle, Turn turn) throws DocumentException {
		document.newPage();

		document.add(new Paragraph(getString(R.string.pdf_turn) + " "
				+ turn.getNum(), catFont));
		drawLine(cb, 765);
		addEmptyLine(document, 1);

		int firstPlayer = battle.getInfos().getFirstPlayer();
		Player one = battle.getPlayer(firstPlayer);
		Player two = battle.getOtherPlayer(firstPlayer);

		try {
			generatePlayerTurn(document, cb, battle, turn, one, firstPlayer + 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		document.newPage();

		try {
			generatePlayerTurn(document, cb, battle, turn, two,
					firstPlayer == 0 ? 2 : 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addEmptyLine(document, 1);

		List<String> allExtras = this.fs.findAllExtrasPhotosPath(battle,
				turn.getNum());
		if (allExtras.size() > 0) {
			document.add(new Paragraph(getString(R.string.pdf_more_photos)
					+ " :", subFont));

			for (String extra : allExtras) {
				try {
					addPhoto(document, extra);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void generatePlayerTurn(Document document, PdfContentByte cb,
			Battle battle, Turn turn, Player player, int numPlayer)
			throws DocumentException, MalformedURLException, IOException {

		document.add(new Paragraph(player.getName(), subFont));

		document.add(new Paragraph(getString(R.string.comments), normalBold));
		document.add(new Paragraph(turn.getComments(numPlayer), normal));

		// MOUVEMENT
		addPhoto(document, battle,
				TurnFragment.MOVE_PHOTO_NAME.replace("{P}", "" + numPlayer)
						.replace("{X}", "" + turn.getNum()),
				getString(R.string.pdf_move) + " " + player.getName() + " : ",
				getString(R.string.pdf_no_photo));

		// document.add(new Paragraph(turn.getCommentsMove(numPlayer), normal));

		// TIR
		addPhoto(document, battle,
				TurnFragment.SHOOT_PHOTO_NAME.replace("{P}", "" + numPlayer)
						.replace("{X}", "" + turn.getNum()),
				getString(R.string.pdf_shoot) + " " + player.getName() + " : ",
				getString(R.string.pdf_no_photo));

		// document.add(new Paragraph(turn.getCommentsShoot(numPlayer),
		// normal));

		// ASSAUT
		addPhoto(document, battle,
				TurnFragment.ASSAULT_PHOTO_NAME.replace("{P}", "" + numPlayer)
						.replace("{X}", "" + turn.getNum()),
				getString(R.string.pdf_assault) + " " + player.getName()
						+ " : ", getString(R.string.pdf_no_photo));

		// document.add(new Paragraph(turn.getCommentsAssault(numPlayer),
		// normal));

	}

	private void generateDeployment(Document document, PdfContentByte cb,
			Battle battle) throws DocumentException, MalformedURLException,
			IOException {
		document.newPage();

		document.add(new Paragraph(getString(R.string.pdf_deployment), catFont));
		drawLine(cb, 765);
		addEmptyLine(document, 1);

		int firstPlayer = battle.getInfos().getFirstPlayer();
		Player one = battle.getPlayer(firstPlayer);
		Player two = battle.getOtherPlayer(firstPlayer);

		document.add(new Paragraph(one.getName() + " "
				+ getString(R.string.pdf_begin), subFont));

		addPhoto(document, battle,
				"deploiement_j" + (firstPlayer + 1) + ".jpg",
				getString(R.string.pdf_deployment_of) + " " + one.getName()
						+ " :", null);

		addPhoto(document, battle, "deploiement_j" + (firstPlayer == 0 ? 2 : 1)
				+ ".jpg",
				getString(R.string.pdf_deployment_of) + " " + two.getName()
						+ " :", null);

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

	private void addPhoto(Document document, String photoPath)
			throws MalformedURLException, IOException, DocumentException {
		Bitmap photo = ImageHelper.photoAsPDFBitmap(photoPath);
		if (photo != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
					100 /* Ratio */, stream);
			Image jpg = Image.getInstance(stream.toByteArray());

			Paragraph element = new Paragraph();
			element.add(jpg);

			document.add(element);
		}
	}

	private void addPhoto(Document document, Battle battle, String photoName,
			String headerText, String textIfNoPhoto) throws DocumentException,
			MalformedURLException, IOException {
		Bitmap photo = ImageHelper.photoAsPDFBitmap(fs.getRootBattle(battle),
				photoName);
		if (photo != null) {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
					100 /* Ratio */, stream);
			Image jpg = Image.getInstance(stream.toByteArray());
			jpg.setAlignment(Image.LEFT | Image.TEXTWRAP);
			jpg.scaleToFit(20000, 170);

			Paragraph element = new Paragraph();

			if (headerText != null) {
				document.add(new Paragraph(headerText, normalBold));
			}
			element.add(new Paragraph(" "));
			element.add(new Chunk(jpg, 0, 0, true));
			document.add(element);
		} else {
			if (textIfNoPhoto != null) {
				document.add(new Paragraph(textIfNoPhoto, normal));
			}
		}
	}

}
