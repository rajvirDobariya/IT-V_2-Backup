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

public class PDFAffidivat {

	private List<String> titles;
	
	public PDFAffidivat(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws  Exception {

		Document document=new Document();
		 FileOutputStream fos=new FileOutputStream("D:/PDF/AffidivatDeclaration.pdf");
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
        font.setSize(11);
        font.setStyle("Calibri");
        
        document.add(new Paragraph(titles.get(1), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(2), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(3), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(4), font)); 
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(5), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(6), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(7), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(8), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(9), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(10), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(11), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(12), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(13), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(14), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(15), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(16), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(17), font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph(titles.get(18), font));
        document.add( Chunk.NEWLINE );
        
        Paragraph p1 = new Paragraph("SCHEDULE-I");
        p1.setAlignment(Paragraph.ALIGN_CENTER);   
        document.add(p1);
        document.add( Chunk.NEWLINE );      
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);	
        table.addCell(new Phrase(titles.get(19), font));
        table.addCell(new Phrase("        ", font));
        table.addCell(new Phrase(titles.get(20), font));
        table.addCell(new Phrase("CHAMARAJNAGAR", font));
        table.addCell(new Phrase(titles.get(21), font));
        table.addCell(new Phrase("PROPERTY PID NO-22-1-5-39, ASSESSMENT NO-1006/970, BR HILLS DOUBLE ROAD C-5, BRAMARAMBA BADAVANE NEAR BRANARAMBA THEATER, CHAMARAJANAGARA 571313", font));
        table.addCell(new Phrase(titles.get(22), font));
        table.addCell(new Phrase("BASAVARAJU  K  MANIYAMMA ERASAVADI IRASAVADI CHAMARAJANAGAR CHAMARAJANAGAR KARNATAKA 571441 ", font));
        table.addCell(new Phrase(titles.get(23), font));
        table.addCell(new Phrase("650000", font));
        table.addCell(new Phrase(titles.get(24), font));
        table.addCell(new Phrase("MANIYAMMA ERASAVADI IRASAVADI CHAMARAJANAGAR CHAMARAJANAGAR KARNATAKA 571441", font));
        table.addCell(new Phrase(titles.get(25), font));
        table.addCell(new Phrase("        ", font));
        table.addCell(new Phrase("        ", font));
        table.addCell(new Phrase("        ", font));
        document.add(table);
        document.add( Chunk.NEWLINE );
        
        Paragraph p2 = new Paragraph("SCHEDULE-II");
        p2.setAlignment(Paragraph.ALIGN_CENTER);   
        document.add(p2);
        document.add( Chunk.NEWLINE ); 
        
        Paragraph p3 = new Paragraph("Immovable Property details (Mortgaged Property)");
        p3.setAlignment(Paragraph.ALIGN_CENTER); 
        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100f);	
        table2.addCell(new Phrase(p3));  
        table2.addCell(new Phrase("Property Description as per legal report \n\n"
        		+ "ALL THAT PIECE AND PARCEL OF THE RCC HOUSE PROPERTY KHATHA ASSESMENT NO:-10,  E-KATHA NO-150800101800120203 SITUATED AT ERASAVADI VILLAGE, CHAMARAJANAGAR TQ & DISTRICT. WITHIN THE LIMITS OF  ERASAVADI GRAM PANCHAYATHI DURATION.\r\n"
        		+ "\n\n", font));
        table2.addCell(new Phrase("BOUNDARIES: - "
        		+"\n East by	               : 	THE ROAD."
        		+"\n West by	: 	THE SITE OF PUTTAMMA."
        		+"\n North by	: 	THE HOUSE OF NANJASHETTY."
        		+"\n South by	: 	THE ROAD", font));  

        table2.addCell(new Phrase("\n\n                                   ")); 
        document.add(table2);
        document.add( Chunk.NEWLINE ); 
        document.add( Chunk.NEWLINE ); 

        document.add(new Paragraph("Identified by me", font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph("Advocate                                                                                              X ________________", font));
        document.add(new Paragraph("                                                                                                           Deponent/Mortgagor", font));
        document.add( Chunk.NEWLINE );
        document.add(new Paragraph("Notary:", font));
        document.add( Chunk.NEWLINE );

        document.close();
	}

}
