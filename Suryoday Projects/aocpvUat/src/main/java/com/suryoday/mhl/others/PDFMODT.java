package com.suryoday.mhl.others;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFMODT {

	private List<String> titles;

	public PDFMODT(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws Exception {

		Document document = new Document();
		FileOutputStream fos = new FileOutputStream("D:/PDF/MODT.pdf");
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		document.open();

		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);

		byte1.addTemplate(tp1, 350, 777);
		document.add(Chunk.NEWLINE);
		Chunk textUnderline = new Chunk(titles.get(0));
		textUnderline.setUnderline(0.8f, -1f);
		Paragraph p2 = new Paragraph(textUnderline);
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(8);
		font.setStyle("Palatino Linotype");
		document.add(Chunk.NEWLINE);
		Font font2 = FontFactory.getFont(FontFactory.HELVETICA);
		font2.setSize(10);
		font2.setStyle("Bookman Old Style");
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("On ……………..,MANIYAMMA        Residing at- PROPERTY NO. UNIQUE", font2));
		document.add(new Paragraph("NO.150800101800120203,IRASAVADI VILLAGE & GRAMAPANCHAYATH, CHMARAJANAGAR", font2));
		document.add(new Paragraph(
				"TALUK & DISTRICT. (hereinafter referred to as “Borrower” or “Depositor”, Which expression shall",
				font2));
		document.add(new Paragraph("include his heirs, executors and permitted assigns as the case may be).", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(1), font2));
		document.add(new Paragraph(titles.get(2), font2));
		document.add(new Paragraph(titles.get(3), font2));
		document.add(new Paragraph(titles.get(4), font2));
		document.add(new Paragraph(titles.get(5), font2));
		document.add(new Paragraph(titles.get(6), font2));
		document.add(new Paragraph(titles.get(7), font2));
		document.add(new Paragraph(titles.get(8), font2));
		document.add(new Paragraph(titles.get(9), font2));
		document.add(new Paragraph(titles.get(10), font2));
		document.add(new Paragraph(titles.get(11), font2));
		document.add(new Paragraph(titles.get(12), font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(13), font2));
		document.add(new Paragraph(titles.get(14), font2));

		document.add(new Paragraph(titles.get(15), font2));
		document.add(new Paragraph(titles.get(16), font2));
		document.add(new Paragraph(titles.get(17), font2));
		document.add(new Paragraph(titles.get(18), font2));
		document.add(new Paragraph(titles.get(19), font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(20), font2));

		document.add(new Paragraph(titles.get(21), font2));
		document.add(new Paragraph(titles.get(22), font2));
		document.add(new Paragraph(titles.get(23), font2));
		document.add(new Paragraph(titles.get(24), font2));
		document.add(new Paragraph(titles.get(25), font2));
		document.add(new Paragraph(titles.get(26), font2));
		document.add(new Paragraph(titles.get(27), font2));
		document.add(new Paragraph(titles.get(28), font2));
		document.add(new Paragraph(titles.get(29), font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(30), font2));

		document.add(new Paragraph(titles.get(31), font2));
		document.add(new Paragraph(titles.get(32), font2));
		document.add(new Paragraph(titles.get(33), font2));
		document.add(new Paragraph(titles.get(34), font2));
		document.add(new Paragraph(titles.get(35), font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(36), font2));

		document.add(new Paragraph(titles.get(37), font2));
		document.add(new Paragraph(titles.get(38), font2));
		document.add(new Paragraph(titles.get(39), font2));
		document.add(new Paragraph(titles.get(40), font2));

		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(titles.get(41), font2));
		document.add(new Paragraph(titles.get(42), font2));
		document.add(new Paragraph(titles.get(43), font2));
		document.add(new Paragraph(titles.get(44), font2));
		document.add(new Paragraph(titles.get(45), font2));
		document.add(new Paragraph(titles.get(46), font2));
		document.add(new Paragraph(titles.get(47), font2));
		document.add(new Paragraph(titles.get(48), font2));
		document.add(new Paragraph(titles.get(49), font2));

		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(titles.get(50), font2));
		document.add(new Paragraph(titles.get(51), font2));
		document.add(new Paragraph(titles.get(52), font2));
		document.add(new Paragraph(titles.get(53), font2));

		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(titles.get(54), font2));
		document.add(new Paragraph(titles.get(55), font2));
		document.add(new Paragraph(titles.get(56), font2));

		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(titles.get(57), font2));
		document.add(new Paragraph(titles.get(58), font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(60), font2));
		document.add(new Paragraph(titles.get(61), font2));
		document.add(new Paragraph(titles.get(62), font2));
		document.add(Chunk.NEWLINE);
		Font font3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font3.setSize(10);
		textUnderline = new Chunk(titles.get(59), font3);
		textUnderline.setUnderline(0.8f, -1f);
		document.add(textUnderline);
		document.add(new Paragraph("North- THE HOUSE OF NANJASHETTY", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("South- THE ROAD", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("East – THE ROAD.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("West – THE SITE OF PUTTAMMA.", font2));
		document.add(Chunk.NEWLINE);

		p2 = new Paragraph("SCHEDULE -II");
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);
		document.add(Chunk.NEWLINE);
		p2 = new Paragraph("(Description of Mortgage Properties as per Title Deed)");
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);
		document.add(Chunk.NEWLINE);
		PdfPTable table = new PdfPTable(new float[] { 10, 20, 50, 20 });
		table.setWidthPercentage(100f);
		table.addCell(new Phrase("   Sr No."));
		table.addCell(new Phrase("   Date"));
		table.addCell(new Phrase("       Document"));
		table.addCell(new Phrase("    Remark"));
		table.addCell(new Phrase("   1 "));
		table.addCell(new Phrase("   01-07-2006"));
		table.addCell(new Phrase(
				"   DEMAND REGISTER EXTRACT ISSUED BY THE PDO, ERASAVADI GRAMA PANCHAYATH STANDS IN THE NAME OF CO-APPLICANT SMT.MANIYAMMA W/O LATE.KAMASHETTY ."));
		table.addCell(new Phrase("   CERTIFIED COPY 2 PAGES     "));
		table.addCell(new Phrase("   2 "));
		table.addCell(new Phrase("   07-09-2022"));
		table.addCell(new Phrase(
				"   E-KHATHA EXTRACT FORM NO:- 9 & 11A VIDE BEARING E-KATHA NO -150800101800120203                 ISSUED BY PDO ERASAVADI  GRAMA PANCHAYATH IN FAVOUR OF THE CO-APPLICANT SMT.MANIYAMMA W/O KAMASHETTY."));
		table.addCell(new Phrase("   COMPUTERIZED  COPY 3 PAGES     "));
		table.addCell(new Phrase("   3 "));
		table.addCell(new Phrase("   30-08-2022"));
		table.addCell(new Phrase(
				"   TAX PAID RECEIPT ISSUED BY ERASAVADI GRAMA PANCHAYATH, WITH RESPECT TO THE PROPOSED  PROPERTY ."));
		table.addCell(new Phrase("   CARBON  COPY     "));
		table.addCell(new Phrase("   4 "));
		table.addCell(new Phrase("   13-09-2022"));
		table.addCell(new Phrase("   NOTARIZED FAMILY TREE EXECUTED IN FAVOUR OF  THE CO-APPLICANT SMT.MANIYAMMA ."));
		table.addCell(new Phrase("   ORIGINAL 2 PAGES     "));
		table.addCell(new Phrase("   5 "));
		table.addCell(new Phrase("   13-09-2022"));
		table.addCell(new Phrase(
				"   ENCUMBRANCE CERTIFICATE ISSUED BY THE SR OF KUDERU  FROM THE YEAR 01-04-2004  TO 12-09-2022."));
		table.addCell(new Phrase("   ORIGINAL 1 PAGE     "));
		table.addCell(new Phrase("   6 "));
		table.addCell(new Phrase("   30-08-2022"));
		table.addCell(new Phrase("   BUILDING PLAN ISSUED BY THE CONCERN ENGINEER ."));
		table.addCell(new Phrase("   CERTIFIED COPY 2 PAGES ORIGINAL 1 PAGE     "));
		table.addCell(new Phrase("   7 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   8 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   9 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   10 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   11 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   12 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   13 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   14 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   15 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("   16 "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		table.addCell(new Phrase("    "));
		document.add(table);

		document.add(new Paragraph("Mortgage: -", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"MANIYAMMA             PROPERTY NO. UNIQUE NO.150800101800120203,IRASAVADI VILLAGE &", font2));
		document.add(new Paragraph("GRAMAPANCHAYATH, CHMARAJANAGAR TALUK & DISTRICT.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Note: - This simple mortgage is without possession.  ", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"IN WITNESS WHERE of the Executant/s has/have signed this memorandum on this date - ………………", font2));
		document.add(new Paragraph("at CHAMRAJ NAGAR.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("(Signature(s) of Mortgagor(s))", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("........................................"));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"MANIYAMMA                           PHOTO                                THUMB IMPRESSION"));

		document.close();
	}

}
