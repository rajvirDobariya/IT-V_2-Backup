package com.suryoday.mhl.others;

import java.io.FileOutputStream;
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



public class PDFExporter{
	
	private List<String> titles;
	
	public PDFExporter(List<String> titles) {
		this.titles = titles;
	}
	
	 public void export(HttpServletResponse response) throws  Exception {
	      
		 Document document=new Document();
		 FileOutputStream fos=new FileOutputStream("D:/PDF/SanctionLetter.pdf");
		 PdfWriter writer = PdfWriter.getInstance(document, fos);
         document.open();
         
         Image image = Image.getInstance("D:\\FinalUAT\\src\\main\\resources\\static\\logo.jpg");
         image.setAbsolutePosition(0, 0);
         PdfContentByte byte1 = writer.getDirectContent();
         PdfTemplate tp1 = byte1.createTemplate(250,50);
         tp1.addImage(image);
         byte1.addTemplate(tp1, 350, 777);  
         document.add(new Paragraph("                                               "));
         document.add( Chunk.NEWLINE );
         	Chunk textUnderline = new Chunk(titles.get(0));
	        textUnderline.setUnderline(0.8f, -1f);
         	Paragraph p = new Paragraph(textUnderline);
	        p.setAlignment(Paragraph.ALIGN_CENTER);   
	        document.add(p);
	        document.add( Chunk.NEWLINE );
         	Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        font.setSize(8);
	        font.setStyle("Palatino Linotype");
		 
	        PdfPTable table = new PdfPTable(4);
	        table.setWidthPercentage(100f);	       
	      table.addCell(new Phrase(titles.get(1), font));       
	      table.addCell(new Phrase("10-09-2022", font));       
	      table.addCell(new Phrase(titles.get(2), font));      
	      table.addCell(new Phrase(" 30108 - PRASANNAKUMAR S", font));       
	      table.addCell(new Phrase(titles.get(3), font));        
	      table.addCell(new Phrase("Inclusive Finance Branch", font));    
	      table.addCell(new Phrase(titles.get(4), font));  
	      table.addCell(new Phrase("MHLN-00000001742-BOS", font)); 
	      document.add(table); 
	      document.add(new Paragraph("To,", font));
	      document.add(new Paragraph("\n"));
	      	PdfPTable table2 = new PdfPTable(3);
	        table2.setWidthPercentage(100f);	    
		     table2.addCell(new Phrase(titles.get(5), font));
		     table2.addCell(new Phrase(titles.get(6), font));
		     table2.addCell(new Phrase(titles.get(7), font));
		     table2.addCell(new Phrase(titles.get(8), font));  
		     table2.addCell(new Phrase("BASAVARAJU  K", font)); 
		     table2.addCell(new Phrase("ERASAVADI IRASAVADI CHAMARAJANAGAR CHAMARAJANAGAR KARNATAKA 571441", font)); 
		     table2.addCell(new Phrase(titles.get(9), font)); 
		     table2.addCell(new Phrase("MANIYAMMA", font)); 
		     table2.addCell(new Phrase("           ", font));      
		     document.add(table2); 
		     document.add( Chunk.NEWLINE );
		     document.add(new Paragraph("Dear Sir/ Madam,", font)); 
		     document.add(new Paragraph(titles.get(59), font));
		     document.add(new Paragraph(titles.get(60), font));
		     document.add( Chunk.NEWLINE );
		     
		     PdfPTable table3 = new PdfPTable(2);
		        table3.setWidthPercentage(100f);	
		     table3.addCell(new Phrase(titles.get(12), font));
		     table3.addCell(new Phrase("MICRO_HOUSING_LOAN", font));
		     table3.addCell(new Phrase(titles.get(13), font));
		     table3.addCell(new Phrase("REFINANCE", font));
		     table3.addCell(new Phrase(titles.get(14), font));
		     table3.addCell(new Phrase("Fixed interest rate of  18 % p.a., payable at monthly rests.", font));
		     table3.addCell(new Phrase(titles.get(15), font));
		     table3.addCell(new Phrase("Rs. 650000 /- ( SIX LAKH FIFTY  THOUSAND RUPEES Only)", font));
		     table3.addCell(new Phrase(titles.get(16), font));
		     table3.addCell(new Phrase("Rs. 13662/- ( THIRTEEN THOUSAND SIX HUNDRED AND SIXTY TWO RUPEES Only)", font));
		     table3.addCell(new Phrase(titles.get(17), font));
		     table3.addCell(new Phrase("84", font));
		     table3.addCell(new Phrase(titles.get(18), font));
		     table3.addCell(new Phrase("Life Insurance Rs. 11428.3 for a tenure of 84 Months and Property Insurance Rs. 3140  for a tenure of 84 Months.", font));
		     table3.addCell(new Phrase(titles.get(19), font));
		     table3.addCell(new Phrase("PROPERTY NO. UNIQUE NO.150800101800120203,IRASAVADI VILLAGE & GRAMAPANCHAYATH, CHMARAJANAGAR TALUK & DISTRICT - 571117.", font));
		     table3.addCell(new Phrase(titles.get(20), font));
		     table3.addCell(new Phrase("From the Date of Disbursement, you will be required to pay PEMI interest on 10th of the succeeding month through Cheque, &  84 Equated monthly installments thereafter as mentioned above through NACH. ", font));
		     table3.addCell(new Phrase(titles.get(21), font));
		     table3.addCell(new Phrase("SI Mode (SI mandate)/NACH Mode and 6 cheques (undated) are to be given", font));
		     table3.addCell(new Phrase(titles.get(22), font));
		     table3.addCell(new Phrase("36.00% per annum on the overdue amount", font));
		     table3.addCell(new Phrase(titles.get(23), font));
		     table3.addCell(new Phrase("Minimum Amount: Rs. 25000/- ", font));
		     table3.addCell(new Phrase(titles.get(24), font));
		     table3.addCell(new Phrase("NIL", font));
		     table3.addCell(new Phrase(titles.get(25), font));
		     table3.addCell(new Phrase("Administrative charges of Rs.5,000 including GST needs to be paid post sanction and Processing Fee of Rs. 10000  including GST , which is non-refundable will be deducted from the loan amount", font));
		     table3.addCell(new Phrase(titles.get(26), font));
		     table3.addCell(new Phrase("As required by the Bank ", font));
		     table3.addCell(new Phrase(titles.get(27), font));
		     table3.addCell(new Phrase("1) POSITIVE RCU REPORT\r\n"
		     		+ "2) POSITIVE LEGAL  AND TECHNICAL REPORT\r\n"
		     		+ "3) HOUSE CONSTRUCTION BELOW 6 MONTHS LETTER REQUIRED FROM PANCHAYTH SIDE BEFORE DISBURSEMENT", font));
		     document.add(table3); 
		     
		     document.add( Chunk.NEWLINE );
		     Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		        font2.setSize(7);
		        font2.setStyle("Palatino Linotype");
		     document.add(new Paragraph(titles.get(28), font2));
		     document.add(new Paragraph(titles.get(29), font2));
		     document.add(new Paragraph(titles.get(30), font2));
		     document.add(new Paragraph(titles.get(31), font2));
		     document.add(new Paragraph(titles.get(32), font2));
		     document.add(new Paragraph(titles.get(33), font2));
		     document.add(new Paragraph(titles.get(34), font2));
		     document.add(new Paragraph(titles.get(35), font2));
		     document.add(new Paragraph(titles.get(36), font2));
		     document.add(new Paragraph(titles.get(37), font2));
		     document.add(new Paragraph(titles.get(38), font2));
		     document.add(new Paragraph(titles.get(39), font2));
		     document.add(new Paragraph(titles.get(40), font2));
		     document.add(new Paragraph(titles.get(41), font2));
		     document.add(new Paragraph(titles.get(42), font2));
		     document.add(new Paragraph(titles.get(43), font2));
		     document.add(new Paragraph(titles.get(44), font2));
		     document.add(new Paragraph(titles.get(45), font2));
		     document.add(new Paragraph(titles.get(46), font2));
		     document.add(new Paragraph(titles.get(47), font2));
		     document.add(new Paragraph(titles.get(48), font2));
		     document.add(new Paragraph(titles.get(49), font2));
		     document.add(new Paragraph(titles.get(50), font2));
		     document.add( Chunk.NEWLINE );
		     document.add(new Paragraph(titles.get(51), font2));
		     document.add(new Paragraph(titles.get(52), font2));
		     document.add(new Paragraph(titles.get(53), font2));
		     document.add(new Paragraph("\n"));
		     document.add(new Paragraph(titles.get(54), font2));
		     document.add( Chunk.NEWLINE );
		     document.add(new Paragraph(titles.get(55), font2));
		     document.add(new Paragraph(titles.get(56), font2));
		     document.add( Chunk.NEWLINE );
		     document.add(table2); 
		     document.add( Chunk.NEWLINE );
		     	
		     document.add(new Paragraph("Date: 10-09-2022", font));
		     document.add(new Paragraph(titles.get(57), font));
		 document.close(); 	 
	    }
}
