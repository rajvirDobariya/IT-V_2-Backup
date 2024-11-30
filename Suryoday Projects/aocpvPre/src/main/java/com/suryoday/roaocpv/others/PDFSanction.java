package com.suryoday.roaocpv.others;

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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public class PDFSanction {
	
	private List<String> titles;
	AocpvLoanCreation loanCreation;
	
	public PDFSanction(List<String> titles,AocpvLoanCreation loanCreation ) {
		this.titles = titles;
		this.loanCreation = loanCreation;
		
	}
	
	public byte[] exportSanctionLetter(HttpServletResponse response) throws  Exception {

		byte[] arr;	 
		Document document = new Document();
		 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, stream);
		   LocalDate now = LocalDate.now();
         document.open();
         ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
         x.getappprop();
         Image image = Image.getInstance(x.LOGO+"logo.jpg");
         Image imageSign = Image.getInstance(x.LOGO+"Signature2.png");
         
         imageSign.scaleToFit(100,50);

        image.setAbsolutePosition(0, 0);
//        imageSign.setAbsolutePosition(410,450);
         PdfContentByte byte1 = writer.getDirectContent();
//         PdfContentByte byte2 = writer.getDirectContent();
         PdfTemplate tp1 = byte1.createTemplate(250,50);
//         PdfTemplate tp2 = byte2.createTemplate(50,350);
         tp1.addImage(image);
//         tp2.addImage(imageSign);
         
         byte1.addTemplate(tp1, 350, 777);  
//         byte1.addTemplate(tp2, 800, 200); 
         document.add(new Paragraph("                                               "));
         document.add( Chunk.NEWLINE );
         	
         Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
         font.setSize(10);
         font.setStyle("Calibri");
         Paragraph p = new Paragraph("SANCTION LETTER",font);
         p.setAlignment(Paragraph.ALIGN_CENTER);   
         document.add(p);
         document.add( Chunk.NEWLINE );
         
         Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	     font2.setSize(8);
	     font2.setStyle("Calibri");
	     Font font3 = FontFactory.getFont(FontFactory.HELVETICA);
	     font3.setSize(8);
	     font3.setStyle("Calibri");
	     
	     List<String> list1=new ArrayList<>();
	     
	    	 Long customerID = loanCreation.getCustomerId();
	    	 String customer = Long.toString(customerID);
	    	 String nameCust = loanCreation.getCustomerName();
	    	 Long mobile = loanCreation.getMobileNo();
	    	 String mobileNO = Long.toString(mobile);
	    	 String loanNo = loanCreation.getLoanAccoutNumber();
	    	 String loanAmount = loanCreation.getSanctionedLoanAmount();
	    	 LocalDate disbursldate = loanCreation.getDisbursalDate();
	    	 String disbursal ="NA";
	    	 if(disbursldate !=null) {
	    		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");	
	    		 disbursal = disbursldate.format(formatter1);
	    	 }
	    	 String tenure = loanCreation.getTenure();
	    	 String emiAmount = loanCreation.getSancationEMI();
	    	 String roi = loanCreation.getRateOfInterest();
	    	 String dusbursedamount=loanCreation.getDisbursementAmount();
	    	 
	    	 
	    	 list1.add(customer);
	    	 list1.add(nameCust);
	    	 list1.add(mobileNO);
	    	 list1.add(loanNo);
	    	 list1.add(loanAmount);
	    	 list1.add(disbursal);
	    	 list1.add(tenure);
	    	 list1.add(emiAmount);
	    	 list1.add(roi);
	    	 list1.add(dusbursedamount);
	    	   System.out.println(list1);
	  
	     
	     String line1="Date       :    "+now ;
	     String line2="Name     :    "+list1.get(1);
	     String line3="Mob.No :    "+list1.get(2);
	     String line4="Dear Customer,";
	     String line5="Sincerely,";
	     String line6="Star Loan";
	     String line7="3000";
	     String line8="Fixed";
	     String line9="Monthly";
	     String line10="SI";
	     String line11="201070080396";
	     String line12="3% Per Month";
	     
	     
	     document.add(new Paragraph(line1,font2));
	     document.add(new Paragraph(line2,font2));
	     document.add(new Paragraph(line3,font2));
	     document.add( Chunk.NEWLINE ); 
	     document.add(new Paragraph(line4,font2));
	     document.add( Chunk.NEWLINE );
	     document.add(new Paragraph(titles.get(0),font2));
	     
	     document.add( Chunk.NEWLINE ); 
	     PdfPTable table=new PdfPTable(2);
	     table.setWidthPercentage(70f);	
	     PdfPCell cell = new PdfPCell(new Phrase(titles.get(1),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(2),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(3),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(3),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(2),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(4),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase());
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(5),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(6),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(4),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(7),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line7,font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(8),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(9),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(9),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(8),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(10),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(11),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(6),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(12),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(13),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(5),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(14),font));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line8,font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(15),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line9,font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(16),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line10,font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(17),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line11,font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(18),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(7),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(19),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(20),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(21),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(list1.get(0),font3));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(titles.get(22),font2));
	     table.addCell(cell);
	     cell = new PdfPCell(new Phrase(line12,font3));
	     table.addCell(cell);
	     document.add(table); 
	     document.add( Chunk.NEWLINE );
	     document.add(new Paragraph(titles.get(23),font2));
		 document.add( Chunk.NEWLINE );
		 document.add(new Paragraph(line5,font2));
		 document.add(Chunk.NEWLINE);
		 document.add(imageSign);
		 document.add(new Paragraph(titles.get(24),font2));
		 document.add( Chunk.NEWLINE );
	      
		 document.close();
		 arr = stream.toByteArray();
		 return arr;	 
	    }

}
