package com.suryoday.roaocpv.others;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public class PDFPassbook {

	private List<String> titles;
	AocpvLoanCreation loanCreation;

	public PDFPassbook(List<String> titles, AocpvLoanCreation loanCreation) {
		this.titles = titles;
		this.loanCreation = loanCreation;
	}

	public byte[] exportPassbook(HttpServletResponse response) throws Exception {

		byte[] arr;
		Document document = new Document();
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();

		Image image = Image.getInstance(x.LOGO + "logo.jpg");
		image.setAbsolutePosition(0, 0);
		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);
		tp1.addImage(image);
		byte1.addTemplate(tp1, 10, 777);
		document.add(new Paragraph("                                               "));
		document.add(Chunk.NEWLINE);
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(10);
		font.setStyle("Calibri");
		Paragraph p = new Paragraph("PASSBOOK LETTER", font);
		p.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(p);
		document.add(Chunk.NEWLINE);

		Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font2.setSize(8);
		font2.setStyle("Calibri");

		List<String> list1 = new ArrayList<>();

		String nameCust = loanCreation.getCustomerName();
		Long customerID = loanCreation.getCustomerId();
		String customer = Long.toString(customerID);
		Long mobile = loanCreation.getMobileNo();
		String mobileNO = Long.toString(mobile);
		String loanNo = loanCreation.getLoanAccoutNumber();
		String loanAmount = loanCreation.getSanctionedLoanAmount();
		LocalDate disbursldate = loanCreation.getDisbursalDate();
		String disbursal = "NA";
		if (disbursldate != null) {
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			disbursal = disbursldate.format(formatter1);
		}

		String tenure = loanCreation.getTenure();
		String emiAmount = loanCreation.getSancationEMI();
		String roi = loanCreation.getRateOfInterest();

		list1.add(nameCust);
		list1.add(customer);
		list1.add(mobileNO);
		list1.add(loanNo);
		list1.add(loanAmount);
		list1.add(disbursal);
		list1.add(tenure);
		list1.add(emiAmount);
		list1.add(roi);
		System.out.println(list1);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(70f);
		PdfPCell cell = new PdfPCell(new Phrase(titles.get(1), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(list1.get(0), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(2), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(list1.get(1), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(3), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(list1.get(2), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(4), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Not Applicable", font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(5), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Not Applicable", font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(6), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(list1.get(3), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(7), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Not Applicable", font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(8), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Not Applicable", font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(titles.get(9), font2));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("VIKAS LOAN", font2));
		table.addCell(cell);
		document.add(table);
		document.add(Chunk.NEWLINE);

		PdfPTable table2 = new PdfPTable(4);
		table2.setWidthPercentage(100f);
		table2.addCell(new Phrase(titles.get(10), font2));
		table2.addCell(new Phrase(list1.get(4), font2));
		table2.addCell(new Phrase(titles.get(15), font2));
		table2.addCell(new Phrase(list1.get(6), font2));
		table2.addCell(new Phrase(titles.get(11), font2));
		table2.addCell(new Phrase(list1.get(5), font2));
		table2.addCell(new Phrase(titles.get(16), font2));
		table2.addCell(new Phrase(list1.get(7), font2));
		table2.addCell(new Phrase(titles.get(12), font2));
		table2.addCell(new Phrase("FIRSTEMIDATE", font2));
		table2.addCell(new Phrase(titles.get(17), font2));
		table2.addCell(new Phrase(list1.get(8), font2));
		table2.addCell(new Phrase(titles.get(13), font2));
		table2.addCell(new Phrase("LASTEMIDATE", font2));
		table2.addCell(new Phrase(titles.get(18), font2));
		table2.addCell(new Phrase("Not Applicable", font2));
		table2.addCell(new Phrase(titles.get(14), font2));
		table2.addCell(new Phrase("5th Of Every Month", font2));
		table2.addCell(new Phrase(titles.get(19), font2));
		table2.addCell(new Phrase("Not Define", font2));
		document.add(table2);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(titles.get(20), font));
		document.add(Chunk.NEWLINE);
		String photo = titles.get(21);

		byte[] decodedBytes = Base64.getDecoder().decode(photo.getBytes());
		Image imageCustomer = Image.getInstance(decodedBytes);
		imageCustomer.scalePercent(50);
		document.add(imageCustomer);

//	     byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(photo);
//	     File of = new File("yourFile.png");
//	     FileOutputStream osf = new FileOutputStream(of);
//	     osf.write(btDataFile);
//	     osf.flush();

		document.add(new Paragraph(titles.get(0), font));
		document.add(new Paragraph(
				"Registered & Corporate Office: 1101, Sharda Terraces, Plot 65, Sector 11, CBD Belapur, Navi Mumbai - 400614.",
				font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("GSTIN: 27AAMCS5499J1ZG | CIN: U65923MH2008PLC261472", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Website: www.suryodaybank.com | Smile Centre: 18001020210", font2));
		document.close();

		arr = stream.toByteArray();
		return arr;
	}

}
