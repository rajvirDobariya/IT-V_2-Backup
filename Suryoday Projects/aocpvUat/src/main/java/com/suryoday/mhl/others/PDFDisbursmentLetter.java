package com.suryoday.mhl.others;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFDisbursmentLetter {

	private List<String> titles;

	public PDFDisbursmentLetter(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws Exception {

		Document document = new Document();
		FileOutputStream fos = new FileOutputStream("D:/PDF/DisbursmentRequestLetter.pdf");
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		document.open();

		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);

		byte1.addTemplate(tp1, 350, 777);
		document.add(new Paragraph("                                               "));
		document.add(Chunk.NEWLINE);
		Chunk textUnderline = new Chunk(titles.get(0));
		textUnderline.setUnderline(0.8f, -1f);
		Paragraph p = new Paragraph(textUnderline);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		Paragraph p2 = new Paragraph("(For Home Loan/LAP/SBLAP)");
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(
				"                                                                               Date: 2022-09-08 Place: CHAMARAJNAGAR"));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Customer Name:  ………………………………                   Loan A/c No:         "));
		document.add(new Paragraph(
				"Property address:  PROPERTY NO. UNIQUE NO.150800101800120203,IRASAVADI VILLAGE & GRAMAPANCHAYATH, CHMARAJANAGAR TALUK & DISTRICT. "));
		document.add(new Paragraph("City - CHAMARAJNAGAR  State: KARNATAKA  Pin code: 571441"));
		document.add(
				new Paragraph("Mobile Number:  7760912330         Registered Email ID:  ___________________________"));

		document.add(new Paragraph("Disbursement Details:"));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(2) + " 650000"));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"Date of registration (Agreement to sale/sale deed/Equitable mortgage deed/Registered Mortgage deed) - …………….\r\n"
						+ "       \r\n"
						+ "I have submitted original builder demand letter/requirement letter (if self-constructed) I/We hereby authorize bank to disburse an amount of Rs. 650000 as per below:  one day prior to the date of registration (wherever applicable) as mentioned above\r\n"
						+ ""));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(2)));
		document.add(new Paragraph("\n"));
		PdfPTable table = new PdfPTable(new float[] { 25, 50, 25 });
		table.setWidthPercentage(100f);
		table.addCell(new Phrase("Sr No."));
		table.addCell(new Phrase("DD Favoring"));
		table.addCell(new Phrase("Amount (in Rs.)  "));
		table.addCell(new Phrase("1."));
		table.addCell(new Phrase("                                 "));
		table.addCell(new Phrase("                                   "));
		table.addCell(new Phrase("2."));
		table.addCell(new Phrase("                                 "));
		table.addCell(new Phrase("                                   "));
		table.addCell(new Phrase("3."));
		table.addCell(new Phrase("                                 "));
		table.addCell(new Phrase("                                   "));
		table.addCell(new Phrase("4."));
		table.addCell(new Phrase("                                 "));
		table.addCell(new Phrase("                                   "));
		document.add(table);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph(titles.get(3)));
		document.add(new Paragraph("\n"));
		PdfPTable table2 = new PdfPTable(new float[] { 40, 30, 30 });
		table2.setWidthPercentage(100f);
		table2.addCell(new Phrase("Particulars  "));
		table2.addCell(new Phrase("Favoring Details 1  "));
		table2.addCell(new Phrase("Favoring Details 2  "));
		table2.addCell(new Phrase("RTGS/NEFT/Transfer  "));
		table2.addCell(new Phrase("   RTGS"));
		table2.addCell(new Phrase("                                   "));
		table2.addCell(new Phrase("Beneficiary Name  "));
		table2.addCell(new Phrase("                                 "));
		table2.addCell(new Phrase("                                   "));
		table2.addCell(new Phrase("Bank Name  "));
		table2.addCell(new Phrase("   STATE BANK OF INDIA"));
		table2.addCell(new Phrase("                                   "));
		table2.addCell(new Phrase("Bank A/C No.  "));
		table2.addCell(new Phrase("33666717017"));
		table2.addCell(new Phrase("                                   "));
		table2.addCell(new Phrase("IFSC CODE - In capital letters    "));
		table2.addCell(new Phrase("SBIN0004162"));
		table2.addCell(new Phrase("                                   "));
		table2.addCell(new Phrase("Disbursement Amount (in Rs.)  "));
		table2.addCell(new Phrase("                    "));
		table2.addCell(new Phrase("                                   "));
		document.add(table2);
		document.add(new Paragraph("*Information to Borrower:  "));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("#. " + titles.get(4)));
		document.add(new Paragraph("#. " + titles.get(5)));
		document.add(new Paragraph("#. " + titles.get(6)));
		document.add(new Paragraph("Yours Faithfully, "));
		document.add(Chunk.NEWLINE);

		document.add(
				new Paragraph("_______________________    _______________________     _________________________  "));
		document.add(
				new Paragraph(" Applicant/POA holder      Co-applicant 1/POA holder   Co-Applicant 2/POA Holder "));

		document.close();

	}

}
