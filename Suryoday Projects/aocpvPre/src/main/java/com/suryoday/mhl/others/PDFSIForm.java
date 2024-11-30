package com.suryoday.mhl.others;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFSIForm {

	private List<String> titles;
	
	public PDFSIForm(List<String> titles) {
		this.titles = titles;
	}

	public void export(HttpServletResponse response) throws Exception {
		
		Document document=new Document();
		FileOutputStream fos=new FileOutputStream("D:/PDF/SIForm.pdf");
		PdfWriter writer = PdfWriter.getInstance(document, fos);
        document.open();
             
       PdfContentByte byte1 = writer.getDirectContent();
       PdfTemplate tp1 = byte1.createTemplate(250,50);
       
       byte1.addTemplate(tp1, 350, 777); 
       document.add(new Paragraph("                                               "));
       document.add( Chunk.NEWLINE );
       
       Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
       font.setSize(10);
       font.setStyle("Calibri ");
       
       Paragraph p = new Paragraph(titles.get(0),font);
       p.setAlignment(Paragraph.ALIGN_CENTER);   
       document.add(p);
       font = new Font();
       font.setSize(9);
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(" Date: ",font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(" Form, ",font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(" To, ",font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(" The Branch Manager. ",font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph("Suryoday Small Finance Bank Ltd. ",font));
       document.add( Chunk.NEWLINE );
       
       document.add(new Paragraph(titles.get(1),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(2),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(3),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(4),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(5),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(6),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(7),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(8),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(9),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(10),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(11),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(12),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(13),font));
       document.add( Chunk.NEWLINE );
       document.add(new Paragraph(titles.get(14),font));
       document.add( Chunk.NEWLINE );
       
       document.close();
	}

}
