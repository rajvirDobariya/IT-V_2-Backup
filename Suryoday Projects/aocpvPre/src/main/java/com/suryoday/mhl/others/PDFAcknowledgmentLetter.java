package com.suryoday.mhl.others;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFAcknowledgmentLetter {
	
	private List<String> titles;

	public PDFAcknowledgmentLetter(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws Exception{
		
		Document document=new Document();
		 FileOutputStream fos=new FileOutputStream("D:/PDF/PDC.pdf");
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
	        Paragraph p2 = new Paragraph(titles.get(1));
	        p2.setAlignment(Paragraph.ALIGN_CENTER);   
	        document.add(p2);
	        document.add( Chunk.NEWLINE );
	        PdfPTable table = new PdfPTable(new float[] { 13, 10, 9, 9, 9, 13, 10, 9, 9, 9 });
	        table.setWidthPercentage(100f);            
	        PdfPCell c1 = new PdfPCell(new Phrase("          SECURITY CHEQUE DETAILS    "));
	        c1.setColspan(5);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("          INSURANCE CHEQUE DETAILS    "));
	        c1.setColspan(5);
	        table.addCell(c1);
	        
	        table.addCell(new Phrase(" Bank Name  "));  
	        c1 = new PdfPCell(new Phrase("  STATE BANK OF INDIA   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        table.addCell(new Phrase(" Bank Name  "));  
	        c1 = new PdfPCell(new Phrase("  STATE BANK OF INDIA   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        
	        table.addCell(new Phrase(" Branch  Name  "));  
	        c1 = new PdfPCell(new Phrase("  CHAMARAJANAGAR   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        table.addCell(new Phrase(" Branch  Name  "));  
	        c1 = new PdfPCell(new Phrase("  CHAMARAJANAGAR   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        
	        table.addCell(new Phrase(" Total Cheque  "));  
	        c1 = new PdfPCell(new Phrase("  6   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        table.addCell(new Phrase(" Total Cheque  "));  
	        c1 = new PdfPCell(new Phrase("  6   "));
	        c1.setColspan(4);
	        table.addCell(c1);
	        
	        table.addCell(new Phrase(" Cheque Number "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase(" Cheque Date "));
	        table.addCell(new Phrase("    "));
	        table.addCell(new Phrase(" Amount "));
	        table.addCell(new Phrase(" Cheque Number "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase(" Cheque Date "));
	        table.addCell(new Phrase("    "));
	        table.addCell(new Phrase(" Amount "));
	        
	        table.addCell(new Phrase(" From "));
	        table.addCell(new Phrase(" To "));
	        table.addCell(new Phrase(" From "));
	        table.addCell(new Phrase(" To "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase(" From "));
	        table.addCell(new Phrase(" To "));
	        table.addCell(new Phrase(" From "));
	        table.addCell(new Phrase(" To "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	        table.addCell(new Phrase("   "));
	       	        
	        document.add(table);
	        document.add( Chunk.NEWLINE );
	        
	        document.add(new Paragraph(titles.get(2)));
	        document.add(new Paragraph("\n"));
	        
	        PdfPTable table2 = new PdfPTable(new float[] { 15, 9, 9, 9, 9, 9, 10, 10, 10, 10 });
	        table2.setWidthPercentage(100f);   
	        table2.addCell(new Phrase(" Bank Name  "));
	        PdfPCell c2 = new PdfPCell(new Phrase(" STATE BANK OF INDIA"));
	        c2.setColspan(9);
	        table2.addCell(c2);
	        
	        table2.addCell(new Phrase(" Branch Name  "));
	        c2 = new PdfPCell(new Phrase(" CHAMARAJANAGAR"));
	        c2.setColspan(9);
	        table2.addCell(c2);
	        
	        table2.addCell(new Phrase(" Total Cheque  "));
	        c2 = new PdfPCell(new Phrase(" 6"));
	        c2.setColspan(9);
	        table2.addCell(c2);
	        
	        table2.addCell(new Phrase(" Sr No.  "));
	        c2 = new PdfPCell(new Phrase(" Cheque Number"));
	        c2.setColspan(2);
	        table2.addCell(c2);
	        table2.addCell(new Phrase("   "));
	        c2 = new PdfPCell(new Phrase(" Cheque Date"));
	        c2.setColspan(2);
	        table2.addCell(c2);
	        c2 = new PdfPCell(new Phrase(" Amount"));
	        c2.setColspan(2);
	        table2.addCell(c2);
	        c2 = new PdfPCell(new Phrase(" Count of cheque"));
	        c2.setColspan(2);
	        table2.addCell(c2);
	        
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase(" From "));
	        table2.addCell(new Phrase(" To  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase(" From  "));
	        table2.addCell(new Phrase(" To  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 1 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 2 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 3 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 4 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 5 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 6 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 7 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        table2.addCell(new Phrase(" 8 "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("  "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        table2.addCell(new Phrase("   "));
	        
	        document.add(table2);
	        document.add( Chunk.NEWLINE );
	        document.add(new Paragraph(titles.get(3)));
	        document.add(new Paragraph(titles.get(4))); 
	        document.add( Chunk.NEWLINE );
	        
	        document.add(new Paragraph("Borrower:- BASAVARAJU  K            Date:-                                  Place:- CHAMARAJNAGAR "));

	        document.close();
	}

}
