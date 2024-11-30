package com.suryoday.roaocpv.others;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

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

public class PDFCIF {

	private List<AocpCustomer> appln;
	private List<AocpvIncomeDetails> appln2;
	private byte[] imageCustomer;

	public PDFCIF(List<AocpCustomer> appln, List<AocpvIncomeDetails> appln2, byte[] imageCustomer) {
		this.appln = appln;
		this.appln2 = appln2;
		this.imageCustomer = imageCustomer;
	}

	public byte[] exportPDFCIF(HttpServletResponse res) throws Exception {

		byte[] arr;
		Document document = new Document();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();

		// Image image =
		// Image.getInstance("C:\\Users\\Neosoft\\Documents\\UAT\\aocpvUat\\src\\main\\resources\\static\\logo.jpg");
		// Image image=null;
		// image.setAbsolutePosition(0, 0);
		PdfContentByte byte1 = writer.getDirectContent();
		PdfTemplate tp1 = byte1.createTemplate(250, 50);
		// tp1.addImage(image);
		byte1.addTemplate(tp1, 350, 777);

		String encoded = Base64.getEncoder().encodeToString(imageCustomer);
		System.out.println(encoded);

		// Customer Image on PDF

//         document.add((Element) Image.getInstance(imageCustomer));

		document.add(new Paragraph("                                               "));
		document.add(Chunk.NEWLINE);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(15);
		font.setStyle("Calibri");
		font.setColor(Color.blue);
		Paragraph p = new Paragraph("SURYODAY SMALL FINANCE BANK LIMITED", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		document.add(Chunk.NEWLINE);
		// customer photo
		Image image = Image.getInstance(imageCustomer);
		image.scaleToFit(150, 220);
		image.setAbsolutePosition(410, 450);
//         instance.setAbsolutePosition(1011,258);
//         instance.setAlignment(instance.ALIGN_RIGHT);
//         instance.setAlignment(100);

		Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font2.setSize(10);
		font2.setStyle("Calibri");
		LocalDateTime now = LocalDateTime.now();
		DayOfWeek day = now.getDayOfWeek();

		List<String> list1 = new ArrayList<>();
		for (AocpCustomer application : appln) {

			long referenceNo = application.getApplicationNo();
			String applicationNo = Long.toString(referenceNo);
			long customerId = application.getCustomerId();
			String customer = Long.toString(customerId);
			String ifExisting = "New customer";
			String nameCust = application.getName();
			String shortName = "";
			String category = "";
			LocalDate dateOfBirth = application.getDateOfBirth();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dob = dateOfBirth.format(formatter1);
			int birthYear = dateOfBirth.getYear();

			String senior = "Minor";
			int presentYear = now.getYear();
			if (presentYear - birthYear > 60) {
				senior = "Senior Citizen";
			}
			long mobile = application.getMobileNo();
			String mobileNo = Long.toString(mobile);
			String officeNo = "";
			long residanceNo = application.getMobile2();
			String residance = Long.toString(residanceNo);
			String email = "";
			String ckycNumber = "";
			String addressType = " ";
			String residanceType = " ";
			String address = application.getAddress();
			String address1 = application.getAddress();
			String addressTypeP = "", addressTypeR = "", addressTypeO = "";
			String addressPLine1 = "", addressRLine1 = "", addressOLine1 = "";
			String addressPLine2 = "", addressRLine2 = "", addressOLine2 = "";
			String addressPLine3 = "", addressRLine3 = "", addressOLine3 = "";
			String cityP = "", cityR = "", cityO = "";
			String pincodeP = "", pincodeR = "", pincodeO = "";
			String districtP = "", districtR = "", districtO = "";
			String stateP = "", stateR = "", stateO = "";
			String countryP = "", countryR = "", countryO = "";
			String landmarkP = "", landmarkR = "", landmarkO = "";
			if (address1 == null || address1.isEmpty()) {

			} else {
				org.json.JSONArray addressInJson = new org.json.JSONArray(address1);
				for (int n = 0; n < addressInJson.length(); n++) {
					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
					String addressType1 = jsonObject2.getString("ADDRESSTYPE");

					if (addressType1.equalsIgnoreCase("PERMANENT ADDRESS")) {
						addressTypeP = jsonObject2.getString("ADDRESSTYPE");
						addressPLine1 = jsonObject2.getString("ADDRESS_LINE1");
						addressPLine2 = jsonObject2.getString("ADDRESS_LINE2");
						addressPLine3 = jsonObject2.getString("ADDRESS_LINE3");
						cityP = jsonObject2.getString("CITY");
						pincodeP = jsonObject2.getString("PINCODE");
						districtP = jsonObject2.getString("DISTRICT");
						stateP = jsonObject2.getString("STATE");
						countryP = jsonObject2.getString("COUNTRY");
						landmarkP = jsonObject2.getString("LANDMARK");
					} else if (addressType1.equalsIgnoreCase("CURRENT ADDRESS")) {
						addressTypeR = jsonObject2.getString("ADDRESSTYPE");
						addressRLine1 = jsonObject2.getString("ADDRESS_LINE1");
						addressRLine2 = jsonObject2.getString("ADDRESS_LINE2");
						addressRLine3 = jsonObject2.getString("ADDRESS_LINE3");
						cityR = jsonObject2.getString("CITY");
						pincodeR = jsonObject2.getString("PINCODE");
						districtR = jsonObject2.getString("DISTRICT");
						stateR = jsonObject2.getString("STATE");
						countryR = jsonObject2.getString("COUNTRY");
						landmarkR = jsonObject2.getString("LANDMARK");
					} else if (addressType1.equalsIgnoreCase("OFFICE ADDRESS")) {
						addressTypeO = jsonObject2.getString("ADDRESSTYPE");
						addressOLine1 = jsonObject2.getString("ADDRESS_LINE1");
						addressOLine2 = jsonObject2.getString("ADDRESS_LINE2");
						addressOLine3 = jsonObject2.getString("ADDRESS_LINE3");
						cityO = jsonObject2.getString("CITY");
						pincodeO = jsonObject2.getString("PINCODE");
						districtO = jsonObject2.getString("DISTRICT");
						stateO = jsonObject2.getString("STATE");
						countryO = jsonObject2.getString("COUNTRY");
						landmarkO = jsonObject2.getString("LANDMARK");
					}
				}
			}

			list1.add(applicationNo);
			list1.add(customer);
			list1.add(ifExisting);
			list1.add(nameCust);
			list1.add(shortName);
			list1.add(category);
			list1.add(dob);
			list1.add(senior);
			list1.add(mobileNo);
			list1.add(officeNo);
			list1.add(residance);
			list1.add(email);
			list1.add(ckycNumber);
			list1.add(addressType);
			list1.add(residanceType);
			list1.add(address);
			list1.add(addressTypeP);// 16
			list1.add(addressPLine1);
			list1.add(addressPLine2);
			list1.add(addressPLine3);
			list1.add(cityP);
			list1.add(pincodeP);
			list1.add(districtP);
			list1.add(stateP);
			list1.add(countryP);
			list1.add(landmarkP);
			list1.add(addressTypeR);
			list1.add(addressRLine1);
			list1.add(addressRLine2);
			list1.add(addressRLine3);
			list1.add(cityR);
			list1.add(pincodeR);
			list1.add(districtR);
			list1.add(stateR);
			list1.add(countryR);
			list1.add(landmarkR);
			list1.add(addressTypeO);
			list1.add(addressOLine1);
			list1.add(addressOLine2);
			list1.add(addressOLine3);
			list1.add(cityO);
			list1.add(pincodeO);
			list1.add(districtO);
			list1.add(stateO);
			list1.add(countryO);
			list1.add(landmarkO);
		}
		System.out.println(list1);

		List<String> list2 = new ArrayList<>();
		for (AocpvIncomeDetails application2 : appln2) {
			if (application2.getMember().equalsIgnoreCase("SELF")) {
				String gender = application2.getGender();
				String aadharNumber = application2.getAadharCard();
				String panNumber = application2.getPanCard();
				String form60 = "60";
				String voterId = application2.getVoterId();
				String education = "";
				String occupation = application2.getOccupation();
				String profession = "";
				String married = application2.getMarried();
				married = "Unmarried";
				if (married.equalsIgnoreCase("YES")) {
					married = "Married";
				}
				if (married.equalsIgnoreCase("NO")) {
					married = "Unmarried";
				}
				String bussinessType = "Abcd";
				String natureOfBussiness = "Services";
				String customerStatus = "";
				String nameOfCompany = "";
				String designation = "";
				String employeeID = "";
				String isSuryodayEmp = "";

				list2.add(gender);
				list2.add(aadharNumber);
				list2.add(panNumber);
				list2.add(form60);
				list2.add(voterId);
				list2.add(education);
				list2.add(occupation);
				list2.add(profession);
				list2.add(bussinessType);
				list2.add(natureOfBussiness);
				list2.add(customerStatus);
				list2.add(nameOfCompany);
				list2.add(designation);
				list2.add(employeeID);
				list2.add(isSuryodayEmp);
				list2.add(married);
				// System.out.println("List2"+list2);
			}
		}

		String title = " Application Reference Number            " + list1.get(0)
				+ "                             Customer ID        " + list1.get(1);
		String line1 = " Existing or New Customer                   " + list1.get(2);
		String line2 = " Name                                                       " + list1.get(3);
		String line3 = " Short Name                                            " + list1.get(4);
		String line4 = " Maiden Name                                         " + list1.get(4);
		String line5 = " Father’s/Spouse Name                          " + list1.get(4);
		String line6 = " Mother’s Name                                       " + list1.get(4);
		String line7 = " Gender                                                    " + list2.get(0);
		String line8 = " Category                                                " + list1.get(5);
		String line9 = " Date of Birth                                          " + list1.get(6);
		String line10 = " Marital Status                                        " + list2.get(15);
		String line11 = " Minor/Senior Citizen                             " + list1.get(7);

		String line12 = " Mobile Number                                     " + list1.get(8);
		String line13 = " Office Number                                      " + list1.get(9);
		String line14 = " Residence Number                               " + list1.get(10);
		String line15 = " Email Address                                      " + list1.get(11);

		String line16 = " CKYC Number                                      " + list1.get(12);
		String line17 = " Aadhaar Number                                  " + list2.get(1);
		String line18 = " PAN Number                                         " + list2.get(2);
		String line19 = " Form 60 / 61                                          " + list2.get(3);
		String line20 = " Voter Id No                                            " + list2.get(4);

		String line21 = " Preferred Address for Communication             " + list1.get(16);
		String line22 = " Residence Type                                                   Own";
		String line23 = " Permnanent Address                                          " + list1.get(17) + " , ";
		String line23p1 = "                                                                               "
				+ list1.get(18) + " , ";
		String line23p2 = "                                                                               "
				+ list1.get(19) + " , ";
		String line23p3 = "                                                                               "
				+ list1.get(20) + " , " + list1.get(21) + " , " + list1.get(22) + " , ";
		String line23p4 = "                                                                               "
				+ list1.get(23) + " , " + list1.get(24) + " , " + list1.get(25) + " ";
		String line24 = " Current Address                                                  " + list1.get(27) + " ";
		String line24p1 = "                                                                                "
				+ list1.get(28) + " ,";
		String line24p2 = "                                                                                "
				+ list1.get(29) + " ,";
		String line24p3 = "                                                                                "
				+ list1.get(30) + " , " + list1.get(31) + " , " + list1.get(32) + " ,";
		String line24p4 = "                                                                                "
				+ list1.get(33) + " , " + list1.get(34) + " , " + list1.get(35) + "";
		String line25 = " Office Address                                                     " + list1.get(37) + " , ";
		String line25p1 = "                                                                                "
				+ list1.get(38) + " , ";
		String line25p2 = "                                                                                "
				+ list1.get(39) + " , ";
		String line25p3 = "                                                                                "
				+ list1.get(40) + " , " + list1.get(41) + " , " + list1.get(42) + " , ";
		String line25p4 = "                                                                                "
				+ list1.get(43) + " , " + list1.get(44) + " , " + list1.get(45) + "";

		String line52 = " Education                                     " + list2.get(5);
		String line53 = " Occupation                                   " + list2.get(6);
		String line54 = " Profession                                    " + list2.get(7);
		String line55 = " Business Type                              " + list2.get(8);
		String line56 = " Nature of Business                       " + list2.get(9);
		String line57 = " Gross Annual Income                   " + list2.get(10);
		String line58 = " Customer Status                            " + list2.get(10);

		String line59 = " Name of Company                         " + list2.get(11);
		String line60 = " Designation                                    " + list2.get(12);
		String line61 = " Employee Id                                    " + list2.get(13);
		String line62 = " Is Suryoday Bank Staff?                 No  ";
		String line63 = " " + day + ", " + now
				+ "                                         CIF_Form                                                          2";

		document.add(new Paragraph(title, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line1, font2));

		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Personal Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(image);
		document.add(new Paragraph(line2, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line3, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line4, font2));
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

		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Contact Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(line12, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line13, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line14, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line15, font2));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" KYC Document Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line16, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line17, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line18, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line19, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line20, font2));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Address Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line21, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line22, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line23, font2));
		document.add(new Paragraph(line23p1, font2));
		document.add(new Paragraph(line23p2, font2));
		document.add(new Paragraph(line23p3, font2));
		document.add(new Paragraph(line23p4, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line24, font2));
		document.add(new Paragraph(line24p1, font2));
		document.add(new Paragraph(line24p2, font2));
		document.add(new Paragraph(line24p3, font2));
		document.add(new Paragraph(line24p4, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line25, font2));
		document.add(new Paragraph(line25p1, font2));
		document.add(new Paragraph(line25p2, font2));
		document.add(new Paragraph(line25p3, font2));
		document.add(new Paragraph(line25p4, font2));

		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Declaration – FATCA ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"I/We am/are not citizen / national / tax resident of any other country than India. ", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Additional Information ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line52, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line53, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line54, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line55, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line56, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line57, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line58, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(new Paragraph(" Employment Details ", font2));
		document.add(new Paragraph(
				"____________________________________________________________________________________________", font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line59, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line60, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line61, font2));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line62, font2));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(line63, font2));

		document.close();
		System.out.println("Hiiiiiiiiiiiiiiiiiiiiiii");
		arr = stream.toByteArray();
		return arr;
	}

}
