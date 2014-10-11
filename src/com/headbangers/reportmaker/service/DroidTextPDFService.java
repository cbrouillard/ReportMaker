package com.headbangers.reportmaker.service;

import harmony.java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.async.GeneratePDFAsyncLoader;
import com.headbangers.reportmaker.dao.BattleDao;
import com.headbangers.reportmaker.exception.TwrException;
import com.headbangers.reportmaker.fragment.BattleInformationsFragment;
import com.headbangers.reportmaker.fragment.ConfigurePlayerFragment;
import com.headbangers.reportmaker.fragment.TurnFragment;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.pojo.GameType;
import com.headbangers.reportmaker.pojo.Player;
import com.headbangers.reportmaker.pojo.Turn;
import com.headbangers.reportmaker.tools.ImageHelper;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

public class DroidTextPDFService implements IPDFService {

	private static Font catFont = new Font(Font.HELVETICA, 18, Font.BOLD);
	private static Font subFont = new Font(Font.HELVETICA, 14, Font.BOLD);
	private static Font normalBold = new Font(Font.HELVETICA, 12, Font.BOLD);
	private static Font normal = new Font(Font.HELVETICA, 12, Font.NORMAL);
	private static Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

	private BattleDao dao;

	private FilesystemService fs = FilesystemService.getInstance();
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
					.getName().replaceAll(" ", "_").replaceAll("\n", "")
					+ ".pdf");

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(documentFile));

			document.open();
			document.addAuthor("Tactical War Report");

			HeaderFooter footer = new HeaderFooter(new Phrase(battle.getName(),
					footerFont), false);
			footer.setAlignment(HeaderFooter.ALIGN_CENTER);
			footer.setBorderWidthBottom(0.0f);
			footer.setBorder(Rectangle.TOP);
			document.setFooter(footer);

			document.addTitle(battle.getName());

			PdfContentByte cb = writer.getDirectContent();

			document.add(new Paragraph(battle.getName(), catFont));
			document.add(new Paragraph(battle.getDateFormated(), normal));
			document.add(new Paragraph(battle.getOne().getName() + " vs "
					+ battle.getTwo().getName(), normal));
			if (battle.getOne().getRace() != null
					&& !"".equals(battle.getOne().getRace())
					&& battle.getTwo().getRace() != null
					&& !"".equals(battle.getTwo().getRace())) {

				document.add(new Paragraph(battle.getOne().getRace() + " vs "
						+ battle.getTwo().getRace(), normal));
			}

			addSeparator(document);
			addEmptyLine(document, 2);

			// Scénario
			if (battle.getInfos().getScenario() != null) {
				Paragraph scenario = new Paragraph();
				scenario.add(new Chunk(getString(R.string.pdf_game_scenario)
						+ " : ", normalBold));
				scenario.add(new Chunk(battle.getInfos().getScenario() + "",
						normal));
				document.add(scenario);
			}

			// Format en points
			if (battle.getFormat() != null) {
				Paragraph format = new Paragraph();
				format.add(new Chunk(getString(R.string.pdf_game_format)
						+ " : ", normalBold));
				format.add(new Chunk(battle.getFormat() + "", normal));
				document.add(format);
			}

			// Traits de seigneur de guerre
			if (battle.getGameType().equals(GameType.WARHAMMER_40K)) {
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
			}

			// Magie des sorciers
			if (battle.getGameType().equals(GameType.WARHAMMER_40K)
					|| battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {

				Paragraph sorcerers1 = new Paragraph();
				sorcerers1.add(new Chunk(getString(R.string.pdf_sorcerers)
						+ " " + battle.getOne().getName() + " : ", normalBold));
				sorcerers1.add(new Chunk(battle.getInfos().getPowers1() + "",
						normal));
				document.add(sorcerers1);

				Paragraph sorcerers2 = new Paragraph();
				sorcerers2.add(new Chunk(getString(R.string.pdf_sorcerers)
						+ " " + battle.getTwo().getName() + " : ", normalBold));
				sorcerers2.add(new Chunk(battle.getInfos().getPowers2() + "",
						normal));
				document.add(sorcerers2);

			}

			// Photo de la table
			addPhoto(320, document, battle,
					BattleInformationsFragment.TABLE_PHOTO_NAME,
					getString(R.string.pdf_table) + " : ", null, null);

			// Listes d'armées
			generateArmyList(document, cb, battle, battle.getOne(), 1);
			generateArmyList(document, cb, battle, battle.getTwo(), 2);

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
			Log.e("EXPORT", "Error :", e);
			throw new TwrException(e);
		} catch (DocumentException e) {
			Log.e("EXPORT", "Error :", e);
			throw new TwrException(e);
		} catch (MalformedURLException e) {
			Log.e("EXPORT", "Error :", e);
			throw new TwrException(e);
		} catch (IOException e) {
			Log.e("EXPORT", "Error :", e);
			throw new TwrException(e);
		}
	}

	private void generateArmyList(Document document, PdfContentByte cb,
			Battle battle, Player player, int num) throws DocumentException,
			MalformedURLException, IOException {

		String[] armyPhotos = fs.getAllFilenameLike(battle,
				ConfigurePlayerFragment.ARMY_PHOTO_NAME
						.replace("{P}", "" + num));

		Log.d("D", "ArmyPhotos = " + armyPhotos);

		if (armyPhotos.length > 0
				|| (player.getArmyComments() != null && !"".equals(player
						.getArmyComments()))) {
			// On génère la page pour le joueur
			document.newPage();
			document.add(new Paragraph(getString(R.string.army_list) + " "
					+ player.getName(), catFont));
			addSeparator(document);
			addEmptyLine(document, 1);

			if (armyPhotos.length > 0) {
				addPhotos(320, document, battle, armyPhotos, armyPhotos.length);
			}

			if ((player.getArmyComments() != null && !"".equals(player
					.getArmyComments()))) {
				document.add(new Paragraph(player.getArmyComments(), normal));
			}
		}

	}

	private void generateTurn(Document document, PdfContentByte cb,
			Battle battle, Turn turn) throws DocumentException {
		document.newPage();

		String turnHeader = getString(R.string.pdf_turn) + " " + turn.getNum();

		if (turn.isLastOne()) {
			turnHeader += " - " + getString(R.string.pdf_lastTurn);
		}

		if (turn.isNightFight()) {
			turnHeader += " - " + getString(R.string.pdf_nightFight);
		}

		document.add(new Paragraph(turnHeader, catFont));
		addSeparator(document);
		addEmptyLine(document, 1);

		try {
			if (battle.getGameType().equals(GameType.WARHAMMER_40K)
					|| battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {

				int firstPlayer = battle.getInfos().getFirstPlayer();
				Player one = battle.getPlayer(firstPlayer);
				Player two = battle.getOtherPlayer(firstPlayer);

				generatePlayerTurn(document, cb, battle, turn, one,
						firstPlayer + 1);

				generatePlayerTurn(document, cb, battle, turn, two,
						firstPlayer == 0 ? 2 : 1);

			} else if (battle.getGameType().equals(GameType.XWING)) {
				Player one = battle.getPlayer(0);
				this.generatePlayerTurn(document, cb, battle, turn, one, 1);
			}

		} catch (MalformedURLException e) {
			Log.e("TURN", "Error :", e);
		} catch (IOException e) {
			Log.e("TURN", "Error :", e);
		}

		addEmptyLine(document, 1);

		List<String> allExtras = this.fs.findAllExtrasPhotosPath(battle,
				turn.getNum());
		if (allExtras.size() > 0) {
			document.add(new Paragraph(getString(R.string.pdf_more_photos)
					+ " :", subFont));

			for (String extra : allExtras) {
				try {
					addPhoto(130, document, extra);
				} catch (MalformedURLException e) {
					Log.e("TURN_PHOTO_EXTRA", "Error :", e);
				} catch (IOException e) {
					Log.e("TURN_PHOTO_EXTRA", "Error :", e);
				}
			}
		}
	}

	private void generatePlayerTurn(Document document, PdfContentByte cb,
			Battle battle, Turn turn, Player player, int numPlayer)
			throws DocumentException, MalformedURLException, IOException {

		if (battle.getGameType().equals(GameType.WARHAMMER_40K)
				|| battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {
			document.add(new Paragraph(player.getName(), subFont));
		}

		if (turn.getComments(numPlayer) != null
				&& !"".equals(turn.getComments(numPlayer))) {
			addComments(document, turn.getComments(numPlayer));
		}

		// CHARGE
		if (battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {
			addPhoto(
					320,
					document,
					battle,
					TurnFragment.CHARGE_PHOTO_NAME.replace("{P}",
							"" + numPlayer).replace("{X}", "" + turn.getNum()),
					getString(R.string.pdf_charge) + " " + player.getName()
							+ " : ", " ", turn.getCommentsCharge(numPlayer));
		}

		// MOUVEMENT
		String moveTitle = (battle.getGameType().equals(GameType.XWING) ? getString(R.string.pdf_move_short)
				: getString(R.string.pdf_move) + " " + player.getName())
				+ " : ";
		addPhoto(320, document, battle,
				TurnFragment.MOVE_PHOTO_NAME.replace("{P}", "" + numPlayer)
						.replace("{X}", "" + turn.getNum()), moveTitle, " ",
				turn.getCommentsMove(numPlayer));

		// MAGIE
		if (battle.getGameType().equals(GameType.WARHAMMER_40K)
				|| battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {
			addPhoto(
					320,
					document,
					battle,
					TurnFragment.POWER_PHOTO_NAME
							.replace("{P}", "" + numPlayer).replace("{X}",
									"" + turn.getNum()),
					getString(R.string.pdf_power) + " " + player.getName()
							+ " : ", " ", turn.getCommentsPower(numPlayer));
		}

		// TIR
		String shootTitle = (battle.getGameType().equals(GameType.XWING) ? getString(R.string.pdf_shoot_short)
				: getString(R.string.pdf_shoot) + " " + player.getName())
				+ " : ";
		addPhoto(320, document, battle,
				TurnFragment.SHOOT_PHOTO_NAME.replace("{P}", "" + numPlayer)
						.replace("{X}", "" + turn.getNum()), shootTitle, " ",
				turn.getCommentsShoot(numPlayer));

		// ASSAUT
		if (battle.getGameType().equals(GameType.WARHAMMER_40K)
				|| battle.getGameType().equals(GameType.WARHAMMER_BATTLE)) {
			addPhoto(
					320,
					document,
					battle,
					TurnFragment.ASSAULT_PHOTO_NAME.replace("{P}",
							"" + numPlayer).replace("{X}", "" + turn.getNum()),
					getString(R.string.pdf_assault) + " " + player.getName()
							+ " : ", " ", turn.getCommentsAssault(numPlayer));
		}

	}

	private void generateDeployment(Document document, PdfContentByte cb,
			Battle battle) throws DocumentException, MalformedURLException,
			IOException {
		document.newPage();

		document.add(new Paragraph(getString(R.string.pdf_deployment), catFont));
		addSeparator(document);
		addEmptyLine(document, 1);

		int firstPlayer = battle.getInfos().getFirstPlayer();
		Player one = battle.getPlayer(firstPlayer);
		Player two = battle.getOtherPlayer(firstPlayer);

		document.add(new Paragraph(one.getName() + " "
				+ getString(R.string.pdf_begin), subFont));

		if (battle.getTurn(1).isNightFight()) {
			document.add(new Paragraph(getString(R.string.pdf_nightFightOn),
					subFont));
		}

		addPhoto(320, document, battle, "deploiement_j" + (firstPlayer + 1)
				+ ".jpg",
				getString(R.string.pdf_deployment_of) + " " + one.getName()
						+ " :", null, null);

		addPhoto(320, document, battle, "deploiement_j"
				+ (firstPlayer == 0 ? 2 : 1) + ".jpg",
				getString(R.string.pdf_deployment_of) + " " + two.getName()
						+ " :", null, null);

		if (battle.getInfos().getComments() != null
				&& !"".equals(battle.getInfos().getComments())) {
			addComments(document, battle.getInfos().getComments());
		}

	}

	// private void drawLine(PdfContentByte cb, int y) {
	// cb.setLineWidth(1f);
	// // cb.moveTo(20, y);
	// // cb.lineTo(575, y);
	// cb.newlineText();
	// cb.stroke();
	// }

	private void addSeparator(Document document) throws DocumentException {
		addEmptyLine(document, 1);
		document.add(new LineSeparator(1.0f, 100f, Color.BLACK,
				Element.ALIGN_CENTER, 1f));
	}

	private void addComments(Document document, String comments)
			throws DocumentException {
		// document.add(new Paragraph(getString(R.string.comments),
		// normalBold));
		Paragraph commentParagraph = new Paragraph(comments, normal);
		commentParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(commentParagraph);
	}

	// @SuppressWarnings("unused")
	// private void drawLittleLine(PdfContentByte cb, int y) {
	// cb.setLineWidth(0.5f);
	// cb.moveTo(50, y);
	// cb.lineTo(545, y);
	// cb.stroke();
	// }

	private void addEmptyLine(Document document, int number)
			throws DocumentException {
		for (int i = 0; i < number; i++) {
			document.add(new Paragraph(" "));
		}
	}

	private Image buildPhoto(String photoPath, int size)
			throws BadElementException, MalformedURLException, IOException {
		Bitmap photo = ImageHelper.photoAtAnySize(photoPath, size);
		if (photo != null) {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
					100 /* Ratio */, stream);
			Image jpg = Image.getInstance(stream.toByteArray());
			jpg.setAlignment(Image.LEFT | Image.TEXTWRAP);
			jpg.scaleToFit(size, 300);
			return jpg;
		}
		return null;
	}

	private Image buildPhoto(Battle battle, String photoName, int size)
			throws BadElementException, MalformedURLException, IOException {
		Bitmap photo = ImageHelper.photoAtAnySize(fs.getRootBattle(battle),
				photoName, size);
		if (photo != null) {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG /* FileType */,
					100 /* Ratio */, stream);
			Image jpg = Image.getInstance(stream.toByteArray());
			jpg.setAlignment(Image.LEFT | Image.TEXTWRAP);
			jpg.scaleToFit(size, 300);
			return jpg;
		}
		return null;
	}

	private void addPhotos(int size, Document document, Battle battle,
			String[] photosPath, int nbPhotos) throws MalformedURLException,
			IOException, DocumentException {
		Log.d("D", "Ajout de la table");
		PdfPTable table = new PdfPTable(nbPhotos);
		table.setSpacingBefore(10);
		table.setWidthPercentage(100);
		table.setKeepTogether(true);
		// table.setWidths(new float[] { 1f });
		table.setHorizontalAlignment(Element.ALIGN_LEFT);

		Log.d("D", "Battle = " + battle);
		for (String path : photosPath) {
			Log.d("D", "Ajout de " + path);

			Image jpg = buildPhoto(battle, path, size);

			PdfPCell imageCell = new PdfPCell(jpg);
			imageCell.setBorder(Rectangle.NO_BORDER);

			table.addCell(imageCell);
		}

		document.add(table);
	}

	private void addPhoto(int size, Document document, String photoPath)
			throws MalformedURLException, IOException, DocumentException {
		Image jpg = buildPhoto(photoPath, size);
		if (jpg != null) {
			Paragraph element = new Paragraph();
			element.add(jpg);

			document.add(element);
		}
	}

	private void addPhoto(int size, Document document, Battle battle,
			String photoName, String headerText, String textIfNoPhoto,
			String comments) throws DocumentException, MalformedURLException,
			IOException {
		Image jpg = buildPhoto(battle, photoName, size);
		if (jpg != null) {

			PdfPTable table = new PdfPTable(2);
			table.setSpacingBefore(10);
			table.setWidthPercentage(100);
			if (comments != null && !"".equals(comments)) {
				table.setWidths(new float[] { 5f, 3f });
			}
			table.setKeepTogether(true);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);

			if (headerText != null) {
				PdfPCell headerCell = new PdfPCell(new Paragraph(headerText,
						normalBold));
				headerCell.setColspan(2);
				headerCell.setBorder(Rectangle.NO_BORDER);
				table.addCell(headerCell);
			}

			PdfPCell imageCell = new PdfPCell(jpg);
			PdfPCell commentCell = new PdfPCell();

			commentCell.setBorder(Rectangle.NO_BORDER);
			imageCell.setBorder(Rectangle.NO_BORDER);

			table.addCell(imageCell);

			if (comments != null && !"".equals(comments)) {
				Paragraph commentParagraph = new Paragraph(comments, normal);
				commentParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
				commentCell.setPhrase(commentParagraph);
				commentCell.setBorder(Rectangle.BOX);
			} else {
				commentCell.setPhrase(new Paragraph(" "));
			}

			table.addCell(commentCell);
			document.add(table);
		} else {
			if (textIfNoPhoto != null) {
				document.add(new Paragraph(textIfNoPhoto, normal));
			}
		}
	}
}
