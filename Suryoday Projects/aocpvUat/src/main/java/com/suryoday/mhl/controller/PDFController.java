package com.suryoday.mhl.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.mhl.others.PDFAcknowledgmentLetter;
import com.suryoday.mhl.others.PDFAffidivat;
import com.suryoday.mhl.others.PDFDisbursmentLetter;
import com.suryoday.mhl.others.PDFExporter;
import com.suryoday.mhl.others.PDFMODT;
import com.suryoday.mhl.others.PDFMemorandum;
import com.suryoday.mhl.others.PDFNotice;
import com.suryoday.mhl.others.PDFSIForm;

@RestController
@RequestMapping("/mhl")
public class PDFController {

	Logger logger = LoggerFactory.getLogger(PDFController.class);

	@RequestMapping(value = "/exportPDF/sanctionLetter", method = RequestMethod.POST)
	public String sancationLetter(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/sanction_letter.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String date = prop.getProperty("date");
		String employee_id = prop.getProperty("empolyee.id");
		String name_sourcing = prop.getProperty("name.sourcing");
		String applnNo = prop.getProperty("application.no");

		String borrowrType = prop.getProperty("table.head1");
		String name = prop.getProperty("table.head2");
		String address = prop.getProperty("table.head3");
		String applicant = prop.getProperty("row.applicantName");
		String coapplicant1 = prop.getProperty("row.coapplicant1");
		String coapplicant2 = prop.getProperty("row.coapplicant2");
		String coapplicant3 = prop.getProperty("right.coapplicant3");
		String signature = prop.getProperty("signature");
		String sub = prop.getProperty("sub");
		String subContent = prop.getProperty("sub.content");

		String tableleft1 = prop.getProperty("table.left1");
		String tableleft2 = prop.getProperty("table.left2");
		String tableleft3 = prop.getProperty("table.left3");
		String tableleft4 = prop.getProperty("table.left4");
		String tableleft5 = prop.getProperty("table.left5");
		String tableleft6 = prop.getProperty("table.left6");
		String tableleft7 = prop.getProperty("table.left7");
		String tableleft8 = prop.getProperty("table.left8");
		String tableleft9 = prop.getProperty("table.left9");
		String tableleft10 = prop.getProperty("table.left10");
		String tableleft11 = prop.getProperty("table.left11");
		String tableleft12 = prop.getProperty("table.left12");
		String tableleft13 = prop.getProperty("table.left13");
		String tableleft14 = prop.getProperty("table.left14");
		String tableleft15 = prop.getProperty("table.left15");
		String tableleft16 = prop.getProperty("table.left16");

		String terms = prop.getProperty("terms");
		String terms1 = prop.getProperty("terms.1");
		String terms2 = prop.getProperty("terms.2");
		String terms3 = prop.getProperty("terms.3");
		String terms4 = prop.getProperty("terms.4");
		String terms5 = prop.getProperty("terms.5");
		String terms6 = prop.getProperty("terms.6");
		String terms7 = prop.getProperty("terms.7");
		String terms8 = prop.getProperty("terms.8");
		String terms9 = prop.getProperty("terms.9");
		String terms10 = prop.getProperty("terms.10");
		String terms11 = prop.getProperty("terms.11");
		String terms12 = prop.getProperty("terms.12");
		String terms13 = prop.getProperty("terms.13");
		String terms14 = prop.getProperty("terms.14");
		String terms15 = prop.getProperty("terms.15");
		String terms16 = prop.getProperty("terms.16");
		String terms17 = prop.getProperty("terms.17");
		String terms18 = prop.getProperty("terms.18");
		String terms19 = prop.getProperty("terms.19");
		String terms20 = prop.getProperty("terms.20");
		String terms21 = prop.getProperty("terms.21");
		String terms22 = prop.getProperty("terms.22");

		String thankyou = prop.getProperty("thankyou");
		String yours = prop.getProperty("yours");
		String bank = prop.getProperty("bank");
		String sign = prop.getProperty("sign");
		String acknow = prop.getProperty("acknow");
		String conditions = prop.getProperty("conditions");
		String copy = prop.getProperty("copy");

		List<String> list = new ArrayList<String>();
		list.add(title);
		list.add(date);
		list.add(employee_id);
		list.add(name_sourcing);
		list.add(applnNo);

		list.add(borrowrType);
		list.add(name);
		list.add(address);
		list.add(applicant);
		list.add(coapplicant1);
		list.add(coapplicant2);
		list.add(coapplicant3);

		list.add(tableleft1);
		list.add(tableleft2);
		list.add(tableleft3);
		list.add(tableleft4);
		list.add(tableleft5);
		list.add(tableleft6);
		list.add(tableleft7);
		list.add(tableleft8);
		list.add(tableleft9);
		list.add(tableleft10);
		list.add(tableleft11);
		list.add(tableleft12);
		list.add(tableleft13);
		list.add(tableleft14);
		list.add(tableleft15);
		list.add(tableleft16);

		list.add(terms);
		list.add(terms1);
		list.add(terms2);
		list.add(terms3);
		list.add(terms4);
		list.add(terms5);
		list.add(terms6);
		list.add(terms7);
		list.add(terms8);
		list.add(terms9);
		list.add(terms10);
		list.add(terms11);
		list.add(terms12);
		list.add(terms13);
		list.add(terms14);
		list.add(terms15);
		list.add(terms16);
		list.add(terms17);
		list.add(terms18);
		list.add(terms19);
		list.add(terms20);
		list.add(terms21);
		list.add(terms22);

		list.add(thankyou);
		list.add(yours);
		list.add(bank);
		list.add(sign);
		list.add(acknow);
		list.add(conditions);
		list.add(copy);
		list.add(signature);
		list.add(sub);
		list.add(subContent);

		PDFExporter exporter10 = new PDFExporter(list);
		exporter10.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/affidivatCumDeclaration", method = RequestMethod.POST)
	public String affidivatCumDeclartion(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/affidivat_Declaration.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String points = prop.getProperty("points");
		String point1 = prop.getProperty("point1");
		String point2 = prop.getProperty("point2");
		String point3 = prop.getProperty("point3");
		String point4 = prop.getProperty("point4");
		String point5 = prop.getProperty("point5");
		String point6 = prop.getProperty("point6");
		String point7 = prop.getProperty("point7");
		String point8 = prop.getProperty("point8");
		String point9 = prop.getProperty("point9");
		String point10 = prop.getProperty("point10");
		String point11 = prop.getProperty("point11");
		String point12 = prop.getProperty("point12");
		String point13 = prop.getProperty("point13");
		String point14 = prop.getProperty("point14");
		String point15 = prop.getProperty("point15");
		String point16 = prop.getProperty("point16");
		String point17 = prop.getProperty("point17");

		String leftTable1 = prop.getProperty("left.table1");
		String leftTable2 = prop.getProperty("left.table2");
		String leftTable3 = prop.getProperty("left.table3");
		String leftTable4 = prop.getProperty("left.table4");
		String leftTable5 = prop.getProperty("left.table5");
		String leftTable6 = prop.getProperty("left.table6");
		String leftTable7 = prop.getProperty("left.table7");

		String tableHead = prop.getProperty("table.head");
		String tableRow1 = prop.getProperty("table.row1");
		String tableRow2 = prop.getProperty("table.row2");
		String tableRow3 = prop.getProperty("table.row3");

		List<String> list = new ArrayList<String>();
		list.add(title);

		list.add(points);
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		list.add(point6);
		list.add(point7);
		list.add(point8);
		list.add(point9);
		list.add(point10);
		list.add(point11);
		list.add(point12);
		list.add(point13);
		list.add(point14);
		list.add(point15);
		list.add(point16);
		list.add(point17);

		list.add(leftTable1);
		list.add(leftTable2);
		list.add(leftTable3);
		list.add(leftTable4);
		list.add(leftTable5);
		list.add(leftTable6);
		list.add(leftTable7);

		list.add(tableHead);
		list.add(tableRow1);
		list.add(tableRow2);
		list.add(tableRow3);

		PDFAffidivat exporter4 = new PDFAffidivat(list);
		exporter4.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/disbusrmentRequestLetter", method = RequestMethod.POST)
	public String disbusrmentRequestLetter(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/disbursment_Letter.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String row1 = prop.getProperty("row1");
		String row2 = prop.getProperty("row2");
		String row3 = prop.getProperty("row3");
		String row4 = prop.getProperty("row4");
		String row5 = prop.getProperty("row5");
		String row6 = prop.getProperty("row6");

		List<String> list = new ArrayList<String>();
		list.add(title);

		list.add(row1);
		list.add(row2);
		list.add(row3);
		list.add(row4);
		list.add(row5);
		list.add(row6);
		list.add(title);

		PDFDisbursmentLetter exporter5 = new PDFDisbursmentLetter(list);
		exporter5.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/memorandum", method = RequestMethod.POST)
	public String memorandum(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/Memorandum.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String point1 = prop.getProperty("points1");
		String point2 = prop.getProperty("points2");
		String point3 = prop.getProperty("points3");
		String point4 = prop.getProperty("points4");
		String point5 = prop.getProperty("points5");
		String point6 = prop.getProperty("points6");
		String point7 = prop.getProperty("points7");

		String schedule1 = prop.getProperty("schedule1");
		String schedule2 = prop.getProperty("schedule2");
		String schedule3 = prop.getProperty("schedule3");

		List<String> list = new ArrayList<String>();
		list.add(title);

		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		list.add(point6);
		list.add(point7);

		list.add(schedule1);
		list.add(schedule2);
		list.add(schedule3);

		PDFMemorandum exporter6 = new PDFMemorandum(list);
		exporter6.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/notice", method = RequestMethod.POST)
	public String notice(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/Notice.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String points = prop.getProperty("points");
		String point1 = prop.getProperty("point1");
		String point2 = prop.getProperty("point2");
		String point3 = prop.getProperty("point3");
		String point4 = prop.getProperty("point4");
		String point5 = prop.getProperty("point5");
		String point6 = prop.getProperty("point6");
		String point7 = prop.getProperty("point7");
		String point8 = prop.getProperty("point8");
		String point9 = prop.getProperty("point9");
		String point1a = prop.getProperty("point1a");
		String point1b = prop.getProperty("point1b");

		List<String> list = new ArrayList<String>();
		list.add(title);

		list.add(points);
		list.add(point1);
		list.add(point1a);
		list.add(point1b);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		list.add(point6);
		list.add(point7);
		list.add(point8);
		list.add(point9);

		PDFNotice exporter8 = new PDFNotice(list);
		exporter8.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/pdcAcknowledgementLetter", method = RequestMethod.POST)
	public String pdcAcknowledgementLetter(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/pdc.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String points = prop.getProperty("points");
		String point1 = prop.getProperty("point1");
		String point2 = prop.getProperty("point2");
		String point3 = prop.getProperty("point3");

		List<String> list = new ArrayList<String>();
		list.add(title);
		list.add(points);
		list.add(point1);
		list.add(point2);
		list.add(point3);

		PDFAcknowledgmentLetter exporter9 = new PDFAcknowledgmentLetter(list);
		exporter9.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/SIForm", method = RequestMethod.POST)
	public String siForm(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/SIForm.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
		String points = prop.getProperty("points");
		String point1 = prop.getProperty("point1");
		String point2 = prop.getProperty("point2");
		String point3 = prop.getProperty("point3");
		String point4 = prop.getProperty("point4");
		String point5 = prop.getProperty("point5");
		String point6 = prop.getProperty("point6");
		String point7 = prop.getProperty("point7");
		String point8 = prop.getProperty("point8");
		String point9 = prop.getProperty("point9");
		String point10 = prop.getProperty("point10");
		String point11 = prop.getProperty("point11");
		String point12 = prop.getProperty("point12");
		String point13 = prop.getProperty("point13");

		List<String> list = new ArrayList<String>();
		list.add(title);
		list.add(points);
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		list.add(point6);
		list.add(point7);
		list.add(point8);
		list.add(point9);
		list.add(point10);
		list.add(point11);
		list.add(point12);
		list.add(point13);

		PDFSIForm exporter11 = new PDFSIForm(list);
		exporter11.export(response);

		return "PDF Downloaded Successfully";
	}

	@RequestMapping(value = "/exportPDF/MODT", method = RequestMethod.POST)
	public String modtAutomationDeclartion(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String sConfigFile = "props/Letters/Modt.properties";
		InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);

		String title = prop.getProperty("header");
//		Paragraph
		String pline1 = prop.getProperty("pl1");
		String pline2 = prop.getProperty("pl2");
		String pline3 = prop.getProperty("pl3");
		String pline4 = prop.getProperty("pl4");
		String pline5 = prop.getProperty("pl5");
		String pline6 = prop.getProperty("pl6");
		String pline7 = prop.getProperty("pl7");
		String pline8 = prop.getProperty("pl8");
		String pline9 = prop.getProperty("pl9");
		String pline10 = prop.getProperty("pl10");
		String pline11 = prop.getProperty("pl11");
		String pline12 = prop.getProperty("pl12");

//		Paragraph1
		String p1line1 = prop.getProperty("p1l1");
		String p1line2 = prop.getProperty("p1l2");
		String p1line3 = prop.getProperty("p1l3");
		String p1line4 = prop.getProperty("p1l4");
		String p1line5 = prop.getProperty("p1l5");
		String p1line6 = prop.getProperty("p1l6");
		String p1line7 = prop.getProperty("p1l7");

//		Paragraph2
		String p2line1 = prop.getProperty("p2l1");
		String p2line2 = prop.getProperty("p2l2");
		String p2line3 = prop.getProperty("p2l3");
		String p2line4 = prop.getProperty("p2l4");
		String p2line5 = prop.getProperty("p2l5");
		String p2line6 = prop.getProperty("p2l6");
		String p2line7 = prop.getProperty("p2l7");
		String p2line8 = prop.getProperty("p2l8");
		String p2line9 = prop.getProperty("p2l9");

//		Paragraph3
		String p3line1 = prop.getProperty("p3l1");
		String p3line2 = prop.getProperty("p3l2");
		String p3line3 = prop.getProperty("p3l3");
		String p3line4 = prop.getProperty("p3l4");
		String p3line5 = prop.getProperty("p3l5");
		String p3line6 = prop.getProperty("p3l6");

//		Paragraph4
		String p4line1 = prop.getProperty("p4l1");
		String p4line2 = prop.getProperty("p4l2");
		String p4line3 = prop.getProperty("p4l3");
		String p4line4 = prop.getProperty("p4l4");
		String p4line5 = prop.getProperty("p4l5");

//		Paragraph5
		String p5line1 = prop.getProperty("p5l1");
		String p5line2 = prop.getProperty("p5l2");
		String p5line3 = prop.getProperty("p5l3");
		String p5line4 = prop.getProperty("p5l4");
		String p5line5 = prop.getProperty("p5l5");
		String p5line6 = prop.getProperty("p5l6");
		String p5line7 = prop.getProperty("p5l7");
		String p5line8 = prop.getProperty("p5l8");

//		Paragraph6
		String p6line1 = prop.getProperty("p6l1");
		String p6line2 = prop.getProperty("p6l2");
		String p6line3 = prop.getProperty("p6l3");
		String p6line4 = prop.getProperty("p6l4");

//		Paragraph6
		String p7line1 = prop.getProperty("p7l1");
		String p7line2 = prop.getProperty("p7l2");
		String p7line3 = prop.getProperty("p7l3");

//		Paragraph8
		String p8line1 = prop.getProperty("p8l1");
		String p8line2 = prop.getProperty("p8l2");

		String title2 = prop.getProperty("title2");

//		  SCHEDULE1
		String s1line1 = prop.getProperty("s1l1");
		String s1line2 = prop.getProperty("s1l2");
		String s1line3 = prop.getProperty("s1l3");
		String s1line4 = prop.getProperty("s1l4");

		List<String> list = new ArrayList<String>();
		list.add(title);
//		  Paragraph
		list.add(pline1);
		list.add(pline2);
		list.add(pline3);
		list.add(pline4);
		list.add(pline5);
		list.add(pline6);
		list.add(pline7);
		list.add(pline8);
		list.add(pline9);
		list.add(pline10);
		list.add(pline11);
		list.add(pline12);
//		  Paragraph1
		list.add(p1line1);
		list.add(p1line2);
		list.add(p1line3);
		list.add(p1line4);
		list.add(p1line5);
		list.add(p1line6);
		list.add(p1line7);
//		  Paragraph2
		list.add(p2line1);
		list.add(p2line2);
		list.add(p2line3);
		list.add(p2line4);
		list.add(p2line4);
		list.add(p2line5);
		list.add(p2line6);
		list.add(p2line7);
		list.add(p2line8);
		list.add(p2line9);
//		  Paragraph3
		list.add(p3line1);
		list.add(p3line2);
		list.add(p3line3);
		list.add(p3line4);
		list.add(p3line5);
		list.add(p3line6);
//		  Paragraph4
		list.add(p4line1);
		list.add(p4line2);
		list.add(p4line3);
		list.add(p4line4);
		list.add(p4line5);
//		  Paragraph5
		list.add(p5line1);
		list.add(p5line2);
		list.add(p5line3);
		list.add(p5line4);
		list.add(p5line4);
		list.add(p5line5);
		list.add(p5line6);
		list.add(p5line7);
		list.add(p5line8);
//		  Paragraph6
		list.add(p6line1);
		list.add(p6line2);
		list.add(p6line3);
		list.add(p6line4);

//		  Paragraph7
		list.add(p7line1);
		list.add(p7line2);
		list.add(p7line3);

//		  Paragraph8
		list.add(p8line1);
		list.add(p8line2);

//		  Title2
		list.add(title2);

//		  SCHEDULE1
		list.add(s1line1);
		list.add(s1line2);
		list.add(s1line3);
		list.add(s1line4);

		PDFMODT exporter7 = new PDFMODT(list);
		exporter7.export(response);

		return "PDF Downloaded Successfully";
	}

	/*
	 * @RequestMapping(value="/exportPDF/AGREEMENT", method = RequestMethod.POST)
	 * public String agreement(HttpServletResponse response) throws Exception {
	 * response.setContentType("application/pdf");
	 * 
	 * String sConfigFile = "props/Letters/Agreement.properties"; InputStream in =
	 * ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile); if
	 * (in == null) { System.out.println("Blank File"); } Properties prop = new
	 * Properties(); prop.load(in);
	 * 
	 * String title=prop.getProperty("header");
	 * 
	 * List<String> list=new ArrayList<String>(); list.add(title);
	 * 
	 * 
	 * PDFMODT exporter7 = new PDFMODT(list); exporter7.export(response);
	 * 
	 * return "PDF Downloaded Successfully"; }
	 */
}
