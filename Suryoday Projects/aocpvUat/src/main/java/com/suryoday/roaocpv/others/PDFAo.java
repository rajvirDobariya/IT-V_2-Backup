package com.suryoday.roaocpv.others;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public class PDFAo {
	Logger logger = LoggerFactory.getLogger(PDFAo.class);
	private List<AocpCustomer> appln;
	private List<AocpvIncomeDetails> appln2;
	// private byte[] imageCustomer;

	private AocpvLoanCreation appln3;

	public PDFAo(List<AocpCustomer> appln, List<AocpvIncomeDetails> appln2, AocpvLoanCreation appln3) {
		this.appln = appln;
		this.appln2 = appln2;
		this.appln3 = appln3;
	}

	public byte[] exportPDFAO(HttpServletResponse res) throws Exception {

		byte[] arr;
		Document document = new Document();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		Image image = Image.getInstance(x.LOGO + "logo.jpg");
		image.setAbsolutePosition(0, 0);
		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);
		tp1.addImage(image);
		byte1.addTemplate(tp1, 350, 777);

		// Customer Image on PDF

		// document.add((Element) com.itextpdf.text.Image.getInstance(imageCustomer));
		// document.add(Image.getInstance(imageCustomer));

//         document.add(new Paragraph("                                               "));
//         document.add( Chunk.NEWLINE );

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(12);
		font.setStyle("Calibri");
		font.setColor(Color.blue);
		Paragraph p = new Paragraph("SURYODAY SMALL FINANCE BANK LIMITED", font);
		p.setAlignment(Paragraph.ALIGN_RIGHT);
		document.add(p);
		document.add(Chunk.NEWLINE);

		Font font2 = FontFactory.getFont(FontFactory.HELVETICA);
		font2.setSize(10);
		font2.setStyle("Calibri");
		LocalDateTime now = LocalDateTime.now();
		DayOfWeek day = now.getDayOfWeek();

		List<String> list1 = new ArrayList<>();
		for (AocpCustomer application : appln) {
			String accountData = application.getAccountData();
//	    	 if(accountData == null || accountData.isEmpty()) {	
//				}
//				else {
//					org.json.JSONObject accountdetails=new org.json.JSONObject(accountData);
//					String DebitCard = accountdetails.getString("DebitCard");
//					String InternateBanking =accountdetails.getString("InternateBanking");
//					String MobileBanking =accountdetails.getString("MobileBanking");
//					String EStatement =accountdetails.getString("EStatement");
//					String SmsAlerts =accountdetails.getString("SmsAlerts");
//					String ChequeBook =accountdetails.getString("ChequeBook");
//					String SweepFacility =accountdetails.getString("SweepFacility");
			String DebitCard = "YES";
			String InternateBanking = "YES";
			String MobileBanking = "YES";
			String EStatement = "YES";
			String SmsAlerts = "YES";
			String ChequeBook = "YES";
			String SweepFacility = "YES";
			long referenceNo = application.getApplicationNo();
			String applicationNo = Long.toString(referenceNo);
			String custName = application.getName();
			long custId = application.getCustomerId();
			String custID = Long.toString(custId);
			String nomineeDetails = application.getNomineeDetails();
			JSONObject nominee = new JSONObject(nomineeDetails);
			String nomineeName = nominee.getString("name");
			String nomineeDob = nominee.getString("dob");
			String nomineeFrnd = nominee.getString("nomineeRelationship");

			list1.add(applicationNo);// 0
			list1.add(custName);// 1
			list1.add(custID);// 2
			list1.add(nomineeName);// 3
			list1.add(nomineeDob);// 4
			list1.add(nomineeFrnd);// 5
			list1.add(DebitCard);// 6
			list1.add(InternateBanking);// 7
			list1.add(MobileBanking);// 8
			list1.add(EStatement);// 9
			list1.add(SmsAlerts);// 10
			list1.add(ChequeBook);// 11
			list1.add(SweepFacility);// 12
		}
//	     }
		for (AocpvIncomeDetails application2 : appln2) {
			String member = application2.getMember();
			list1.add(member);// 13
		}
//	     for(AocpvLoanCreation application3:appln3)
//	     {
		String accountNo = appln3.getAccountNumber();
		list1.add(accountNo);// 14
		String branchId = appln3.getBranchId();
		list1.add(branchId);// 15
		System.out.println(list1);
		String title = " Application Reference Number                    " + list1.get(0)
				+ "                                        Branch ID           " + list1.get(8) + "";
		String line1 = " Account Number                                          " + list1.get(14);
		String line2 = " Customer ID                                         " + list1.get(2);
		String line3 = " Customer Name                                   " + list1.get(1);
		String line4 = " Mode Of Operation                               " + list1.get(13);
		String line5 = " Debit Card                                             " + list1.get(6);
		String line6 = " Name on Debit Card";
		String line7 = " Internet Banking                                   " + list1.get(7);
		String line8 = " Mobile Banking                                     " + list1.get(8);
		String line9 = " E-Statement                                          " + list1.get(9);
		String line10 = " SMS & E-mail alerts                              " + list1.get(10);
		String line11 = " Cheque Book                                         " + list1.get(11);
		String line12 = " Sweep Facility                                       " + list1.get(12);
		String line13 = " Mode of Payment";
		String line14 = " Amount";
		String line15 = " Nominee Name                  " + list1.get(3)
				+ "                                             Relationship with Depositor            " + list1.get(5)
				+ "";
		String line16 = " Nominee Date of Birth       " + list1.get(4);
		String line17 = " Nominee Address              SAME AS APPLICANTS PERMANENT ADDRESS";
		String line18 = " " + day + ", " + now
				+ "                                         AO_Form                                                          1";

		document.add(new Paragraph(title, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line1, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Declaration", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" I/We agree to open Savings Ujjwal (1003) account with the balance requirement of Rs.0.00.I/We fully",
				font2));
		document.add(new Paragraph(
				" understand the detailed charges (including the balance non-maintenance charges) \"applicable to this",
				font2));
		document.add(new Paragraph(
				" account as per, Schedule of Charges which is available on the Bank's Website / Branch notice board.",
				font2));
		document.add(new Paragraph(" This has also been explained to me/us by the Bank Official.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Applicant Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line2, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line3, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line4, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Facilities Opted for ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line5, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line6, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line7, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line8, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line9, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line10, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line11, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line12, font2));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Initial Funding Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line13, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line14, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Nomination Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" I/We NALINI NALINI, residing at W/O RAJIVGANTHI,465 MARIYAMAN KOVIL ST, TIRUVAMUR,", font2));
		document.add(new Paragraph(" PERIYAKALANI, PANRU, CUDDALORE, TAMIL NADU, INDIA - 607106 Nominate the following",
				font2));
		document.add(new Paragraph(
				" person to whom in the event of my/our/minor's death the amount of deposit, may be returned by",
				font2));
		document.add(new Paragraph(" Suryoday Small Finance Bank Limited, KUMARAPALAYAM (10128).", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line15, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line16, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line17, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Account Opening Rules ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" • All necessary documentation as mandated by the Regulatory/Bank authorities should be provided for",
				font2));
		document.add(new Paragraph("   opening the accounts.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" • All accounts should maintain the stipulated average balance based on the product/program and branch in",
				font2));
		document.add(new Paragraph("   which the account is opened.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" • In case of non-maintenance of the stipulated average balance, charges as outlined in the Schedule of",
				font2));
		document.add(new Paragraph("   Charges from time to time will be applicable.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(" • Savings Accounts can be opened for non-business purposes only.", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				" • In case of any query / suggestion / feedback / complaint relating to features of any of the products, you may",
				font2));
		document.add(new Paragraph("   contact the nearest Suryoday Small Finance Bank Branch.", font2));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line18, font2));
		document.close();
		System.out.println("Hiiiiiiiiiiiiiiiiiiiiiii");
		arr = stream.toByteArray();
		return arr;
	}

}
