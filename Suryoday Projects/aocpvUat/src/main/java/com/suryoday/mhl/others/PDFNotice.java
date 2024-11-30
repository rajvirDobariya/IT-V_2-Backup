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

public class PDFNotice {

	private List<String> titles;

	public PDFNotice(List<String> titles) {
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
		Paragraph p2 = new Paragraph(titles.get(0));
		p2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p2);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(1)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(2)));
		document.add(new Paragraph(titles.get(3) + "SURYODAY SMALL FINANCE BANK LTD."));
		document.add(new Paragraph("Address: SURYODAY SMALL "));
		document.add(new Paragraph("FINANCE BANK LTD.  "));
		document.add(new Paragraph("Pan /Tan: AAMCS5499J "));
		document.add(new Paragraph("Mobile No:  "));
		document.add(new Paragraph(" Email id:  "));
		document.add(new Paragraph(titles.get(4) + " MANIYAMMA "));
		document.add(new Paragraph("Address: ERASAVADI "));
		document.add(new Paragraph("IRASAVADI"));
		document.add(new Paragraph("CHAMARAJANAGAR "));
		document.add(new Paragraph("KARNATAKA 571441 "));
		document.add(new Paragraph("Pan /Tan: "));
		document.add(new Paragraph("Mobile No: 8459586696 "));
		document.add(new Paragraph("Email id:  "));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(5)));
		document.add(new Paragraph("District: CHAMARAJNAGAR"));
		document.add(new Paragraph("Taluka:  "));
		document.add(new Paragraph("Village: "));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(6)));
		document.add(new Paragraph("674.4 sq. ft."));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(7) + "  650000/- "));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(8) + "  18 %"));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(9)));
		document.add(Chunk.NEWLINE);

		PdfPTable table = new PdfPTable(new float[] { 25, 25, 25, 25 });
		table.setWidthPercentage(100f);
		table.addCell(new Phrase("Name of the property"));
		table.addCell(new Phrase("Party Photo (To be attended by mortgagee)"));
		table.addCell(new Phrase("Party thumb impression"));
		table.addCell(new Phrase("Signature(Incase of institution)"));
		table.addCell(new Phrase("Mortgagee"));
		table.addCell(new Phrase("           "));
		table.addCell(new Phrase("                "));
		table.addCell(new Phrase("        "));
		table.addCell(new Phrase("Mortgagor"));
		table.addCell(new Phrase("           "));
		table.addCell(new Phrase("                "));
		table.addCell(new Phrase("        "));
		document.add(table);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(titles.get(10)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(11)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(12)));

		document.close();

	}

}
