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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFMemorandum {

	private List<String> titles;

	public PDFMemorandum(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws Exception {

		Document document = new Document();
		FileOutputStream fos = new FileOutputStream("D:/PDF/MemorandumOfEntry.pdf");
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		document.open();

		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);

		byte1.addTemplate(tp1, 350, 777);
		document.add(new Paragraph("                                               "));
		document.add(Chunk.NEWLINE);
		Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font2.setSize(22);
		font2.setStyle("Calibri");
		Paragraph p2 = new Paragraph(titles.get(0), font2);
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);
		document.add(Chunk.NEWLINE);
		Font font = new Font();
		font.setSize(10);
		document.add(new Paragraph(titles.get(1), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(2), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(3), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(4), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(5), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(6), font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(7), font));
		document.add(Chunk.NEWLINE);

		Paragraph p3 = new Paragraph(titles.get(8), font);
		p3.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p3);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Mortgage Date.", font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Place of Deposit- CHAMRAJ NAGAR", font));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Obligations: ", font));
		document.add(Chunk.NEWLINE);
		PdfPTable table = new PdfPTable(new float[] { 10, 25, 45, 20 });
		table.setWidthPercentage(100f);
		table.addCell(new Phrase("Sr No."));
		table.addCell(new Phrase("Nature of the Facility(ies)"));
		table.addCell(new Phrase("Details of the loan agreement"));
		table.addCell(new Phrase("Amount of the Facility(ies)"));
		table.addCell(new Phrase("   1. "));
		table.addCell(new Phrase("   REFINANCE"));
		table.addCell(new Phrase(
				"   PROPERTY NO. UNIQUE NO.150800101800120203,IRASAVADI VILLAGE & GRAMAPANCHAYATH, CHMARAJANAGAR TALUK & DISTRICT."));
		table.addCell(new Phrase("   650000      "));
		document.add(table);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Mortgagor: MANIYAMMA ", font));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("Borrower: BASAVARAJU  K", font));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("Mortgagor's Representative : MANIYAMMA      BASAVARAJU  K", font));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("Suryoday Representative: 30108 , PRASANNAKUMAR S", font));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("Witness -", font));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("Resolution Date: ", font));
		document.add(new Paragraph("\n"));
		document.add(Chunk.NEWLINE);
		Paragraph p4 = new Paragraph(titles.get(9), font);
		p4.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p4);
		document.add(new Paragraph("LIST OF DOCUMENTS OF TITLE, EVIDENCES DEEDS AND WRITINGS", font));
		document.add(Chunk.NEWLINE);

		PdfPTable table2 = new PdfPTable(new float[] { 10, 60, 30 });
		table2.setWidthPercentage(100f);
		table2.addCell(new Phrase("Sr No."));
		table2.addCell(new Phrase("    Description of Title Deed      "));
		table2.addCell(new Phrase("    Date of Title Deed     "));
		table2.addCell(new Phrase("   1 "));
		table2.addCell(new Phrase(
				"DEMAND REGISTER EXTRACT ISSUED BY THE PDO, ERASAVADI GRAMA PANCHAYATH STANDS IN THE NAME OF CO-APPLICANT SMT.MANIYAMMA W/O LATE.KAMASHETTY ."));
		table2.addCell(new Phrase("   01-07-2006"));
		table2.addCell(new Phrase("   2 "));
		table2.addCell(new Phrase(
				"E-KHATHA EXTRACT FORM NO:- 9 & 11A VIDE BEARING E-KATHA NO -150800101800120203                 ISSUED BY PDO ERASAVADI  GRAMA PANCHAYATH IN FAVOUR OF THE CO-APPLICANT SMT.MANIYAMMA W/O KAMASHETTY ."));
		table2.addCell(new Phrase("   07-09-2022"));
		table2.addCell(new Phrase("   3 "));
		table2.addCell(new Phrase(
				"TAX PAID RECEIPT ISSUED BY ERASAVADI GRAMA PANCHAYATH, WITH RESPECT TO THE PROPOSED  PROPERTY ."));
		table2.addCell(new Phrase("   30-08-2022"));
		table2.addCell(new Phrase("   4 "));
		table2.addCell(new Phrase("NOTARIZED FAMILY TREE EXECUTED IN FAVOUR OF  THE CO-APPLICANT SMT.MANIYAMMA ."));
		table2.addCell(new Phrase("   13-09-2022"));
		table2.addCell(new Phrase("   5 "));
		table2.addCell(new Phrase(
				"ENCUMBRANCE CERTIFICATE ISSUED BY THE SR OF KUDERU  FROM THE YEAR 01-04-2004  TO 12-09-2022 ."));
		table2.addCell(new Phrase("   13-09-2022"));
		table2.addCell(new Phrase("   1 "));
		table2.addCell(new Phrase(
				"DEMAND REGISTER EXTRACT ISSUED BY THE PDO, ERASAVADI GRAMA PANCHAYATH STANDS IN THE NAME OF CO-APPLICANT SMT.MANIYAMMA W/O LATE.KAMASHETTY ."));
		table2.addCell(new Phrase("   01-07-2006"));
		table2.addCell(new Phrase("   6 "));
		table2.addCell(new Phrase("BUILDING PLAN ISSUED BY THE CONCERN ENGINEER ."));
		table2.addCell(new Phrase("   30-08-2022"));
		table2.addCell(new Phrase("   7 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   8 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   9 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   10 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   11 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   12 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   13 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   14 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   15 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("   16 "));
		table2.addCell(new Phrase("                 "));
		table2.addCell(new Phrase("                 "));
		document.add(table2);
		document.add(new Paragraph("\n"));

		p4 = new Paragraph(titles.get(10), font);
		p4.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p4);
		p4 = new Paragraph("DESCRIPTION OF PROPERTY", font);
		p4.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p4);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph(
				"All those pieces of land comprised in the survey numbers specified below, within the limits of the villages, district and state specified below.",
				font));
		document.add(new Paragraph("\n"));

		PdfPTable table3 = new PdfPTable(new float[] { 10, 30, 20, 20, 20 });
		table3.setWidthPercentage(100f);
		table3.addCell(new Phrase("   Sr No. "));
		table3.addCell(new Phrase("  Village, District, State    "));
		table3.addCell(new Phrase("   Survey No."));
		PdfPCell c1 = new PdfPCell(new Phrase("Extent"));
		c1.setColspan(2);
		table3.addCell(c1);

		table3.addCell(new Phrase(" 1  "));
		table3.addCell(new Phrase(" CHAMARAJNAGAR , KARNATAKA                "));
		table3.addCell(new Phrase("          "));
		table3.addCell(new Phrase(" Acre      "));
		table3.addCell(new Phrase(" Cents     "));
		document.add(table3);
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("The above lands have boundaries as below:"));
		PdfPTable table4 = new PdfPTable(new float[] { 25, 75 });
		table4.setWidthPercentage(100f);
		table4.addCell(new Phrase("On or towards East"));
		table4.addCell(new Phrase("THE ROAD."));
		table4.addCell(new Phrase("On or towards West"));
		table4.addCell(new Phrase("THE SITE OF PUTTAMMA."));
		table4.addCell(new Phrase("On or towards North"));
		table4.addCell(new Phrase("THE HOUSE OF NANJASHETTY"));
		table4.addCell(new Phrase("On or towards South"));
		table4.addCell(new Phrase("THE ROAD"));
		document.add(table4);
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("                        FOR AND ON BEHALF of SURYODAY SMALL FINANCE BANK LIMITED"));
		document.add(Chunk.NEWLINE);
		table3.addCell(new Phrase("                         ___________________________________________________"));
		document.add(Chunk.NEWLINE);
		table3.addCell(new Phrase("                        ___________________________________________________"));

		document.close();
	}

}
