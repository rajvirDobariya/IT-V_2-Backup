package com.suryoday.collections.others;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.suryoday.collections.pojo.ReceiptPojo;

public class PDFReceipt {

	private List<String> titles;
	private List<ReceiptPojo> receiptPDF;

	public PDFReceipt(List<String> titles, List<ReceiptPojo> receiptPDF) {
		this.titles = titles;
		this.receiptPDF = receiptPDF;
	}

	public byte[] exportReceipt(HttpServletResponse response) throws Exception {

		byte[] arr;
		Document document = new Document();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();

		Image image = Image.getInstance("src\\main\\resources\\static\\assets\\logo.jpg");
		image.setAbsolutePosition(0, 0);
		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);
		tp1.addImage(image);
		byte1.addTemplate(tp1, 350, 777);
		document.add(new Paragraph("                                               "));
		document.add(Chunk.NEWLINE);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(15);
		font.setStyle("Calibri");
		Paragraph p = new Paragraph(titles.get(0), font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		document.add(Chunk.NEWLINE);

		Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font2.setSize(11);
		font2.setStyle("Calibri");

		List<String> list1 = new ArrayList<>();
		for (ReceiptPojo receiptlist : receiptPDF) {

			String receiptNo = receiptlist.getReceiptNo();
			LocalDate currentDate = receiptlist.getCurrentDate();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String receiptDate = currentDate.format(formatter1);
			String customerId = receiptlist.getCustomerID();
			String customerName = receiptlist.getCustName();
			String loanNo = receiptlist.getAccountNo();
			String mobile = receiptlist.getMobileNo();
			String ptp = receiptlist.getPtpAmount();
			String emi = receiptlist.getEmiAmount();
			String modeOfPayment = receiptlist.getModeOfPayment();
			String status = receiptlist.getPaymanetStatus();

			list1.add(receiptNo);
			list1.add(receiptDate);
			list1.add(customerId);
			list1.add(customerName);
			list1.add(loanNo);
			list1.add(mobile);
			list1.add(ptp);
			list1.add(emi);
			list1.add(modeOfPayment);
			list1.add(status);
		}
		System.out.println(list1);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.addCell(new Phrase(titles.get(1), font));
		table.addCell(new Phrase(list1.get(0), font));
		table.addCell(new Phrase(titles.get(2), font));
		table.addCell(new Phrase(list1.get(1), font));
		table.addCell(new Phrase(titles.get(3), font));
		table.addCell(new Phrase(list1.get(2), font));
		table.addCell(new Phrase(titles.get(4), font));
		table.addCell(new Phrase(list1.get(3), font));
		table.addCell(new Phrase(titles.get(5), font));
		table.addCell(new Phrase(list1.get(4), font));
		table.addCell(new Phrase(titles.get(6), font));
		table.addCell(new Phrase(list1.get(5), font));
		table.addCell(new Phrase(titles.get(7), font));
		table.addCell(new Phrase(list1.get(6), font));
		table.addCell(new Phrase(titles.get(8), font));
		table.addCell(new Phrase(list1.get(7), font));
		table.addCell(new Phrase(titles.get(9), font));
		table.addCell(new Phrase(list1.get(8), font));
		table.addCell(new Phrase(titles.get(10), font));
		table.addCell(new Phrase(list1.get(9), font));
		document.add(table);
		document.add(Chunk.NEWLINE);
		document.close();
		arr = stream.toByteArray();
		return arr;
	}
}
