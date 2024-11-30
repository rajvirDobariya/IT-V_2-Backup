package com.suryoday.twowheeler.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerImage;
import com.suryoday.twowheeler.repository.TwowheelerDetailsRepository;
import com.suryoday.twowheeler.repository.TwowheelerImageRepository;
import com.suryoday.twowheeler.service.TWPDFService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerLeegalityService;

@Service
@SuppressWarnings({ "unchecked", "static-access" })
public class TWPDFServiceImpl implements TWPDFService {

	@Autowired
	TwowheelerImageRepository twowheelerimagerepository;

	@Autowired
	TwowheelerDetailsRepository twowheelerdetailsrepository;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;
	
	@Autowired
	TwowheelerLeegalityService leegalityservice;
	
	private static Logger logger = LoggerFactory.getLogger(TWPDFServiceImpl.class);

	@Override
	public String sanctionPdf(StringBuilder htmlString, String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerdetailsrepository.getByApplicationNo(applicationNo);
		if(optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			String address_Line1="";
			String address_Line2="";
			String address_Line3="";
			String city="";
			String pincode="";
			String country="";
			String district="";
			String state="";
			
//			Double payableamount=Double.parseDouble(twowheelerDetailesTable.getAmount())-Double.parseDouble(twowheelerDetailesTable.getTotaldeductionAmount());
//			String payableAmount = String.valueOf(payableamount);
			if (twowheelerDetailesTable.getAddress() != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(twowheelerDetailesTable.getAddress());
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					if(addressInJson.getString("addressType").equalsIgnoreCase("CURRENT ADDRESS")) {
						 address_Line1 = addressInJson.getString("address_Line1");
						 address_Line2 = addressInJson.getString("address_Line2");
						 address_Line3 = addressInJson.getString("address_Line3");
						 city = addressInJson.getString("city");
						 pincode = addressInJson.getString("pincode");
						 country = addressInJson.getString("country");
						 district = addressInJson.getString("district");
						 state = addressInJson.getString("state");
					}
				}
			}
			LocalDate now=LocalDate.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String format = now.format(dateTimeFormatter);
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			htmlString.append(new String("<!DOCTYPE html>\r\n" + "<html>\r\n" + "    <head>\r\n"  
					+ " <style>\r\n"
					+ "          table,\r\n"
					+ "    th,\r\n"
					+ "    td {\r\n"
					+ "      border: 1px solid black;\r\n"
					+ "      border-collapse: collapse;\r\n"
					
					+ "    }\r\n"
					+ "    body {\r\n"
					+ "        font-size: 12px;\r\n"
					+ "    }\r\n"
					+ "    p {\r\n"
					+ "        margin: 8px 0;  \r\n"
					+ "    }\r\n"
					+ "        </style>"
					+ "      </head>\r\n" + "    <body>\r\n"
					+ "        <div class=\"logo\" style=\"text-align: right;\">\r\n"
					+ "            <img style=\"width: 20%;\" src=" + x.LOGO + "logo.jpg" + ">\r\n"
					+ "          </div>\r\n" + "        <div class=\"container\">\r\n"
					+ "        <h3 style=\"text-align: center;\">SANCTION CUM DELIVERY LETTER</h3>\r\n"
					+ "            <p> NAME: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ twowheelerDetailesTable.getName()
					+ " <span class=\"tab\" style=\"padding-left: 250px;\">DATE:&nbsp;&nbsp;&nbsp;" + format
					+ "</span></p>\r\n" + "            <p> ADDRESS: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ address_Line1 + "," + address_Line2 + "</p>\r\n"
					+ "            <p style=\"padding-left: 105px;\"> " + city + ", " + district + "  ," + state + " "
					+ pincode + "\r\n" + "                </p>\r\n"
					+ "            <p> MOB NO: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ twowheelerDetailesTable.getMobileNo() + "</p>  \r\n"
					+ "            <p> With reference to your request, we are pleased to inform you that the following credit facilities have been approved subject to the\r\n"
					+ "                terms and conditions listed below:</p> \r\n"
					+ "            <table style=\"margin-left: auto; margin-right: auto;width: 60%;\">\r\n"
					+ "                <tr>\r\n" + "                    <td>Application ID</td>\r\n"
					+ "                    <td>"+twowheelerDetailesTable.getApplicationNo()+"</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>Credit Facility</td>\r\n"
					+ "                    <td>Two-Wheeler Loan</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>No.of Units </td>\r\n"
					+ "                    <td>1</td>\r\n" + "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>Applicant</td>\r\n" + "                    <td>"
					+ twowheelerDetailesTable.getName() + "</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>Co-applicant</td>\r\n"
					+ "                    <td>NA</td>\r\n" + "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>Vehicle Type/Mode</td>\r\n" + "                    <td>"
					+ twowheelerDetailesTable.getManufacture() + " " + twowheelerDetailesTable.getModel() + "</td>\r\n"
					+ "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>Total Funding Amount</td>\r\n" + "                    <td>"
					+ twowheelerDetailesTable.getAmount() + "</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>Term in Months</td>\r\n"
					+ "                    <td>" + twowheelerDetailesTable.getTenure() + "</td>\r\n"
					+ "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>ROI(Flat)</td>\r\n" + "                    <td>"
					+ twowheelerDetailesTable.getRateOfInterest() + " </td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>Effective IRR</td>\r\n"
					+ "                    <td>"+twowheelerDetailesTable.getEffectiveIrr()+"</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>EMI Amount </td>\r\n"
					+ "                    <td>Rs. " + twowheelerDetailesTable.getEmi() + "</td>\r\n"
					+ "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>Legal/Stamping/Other Charges</td>\r\n"
					+ "                    <td>As per Bank's Standard policy</td>\r\n" + "                </tr>\r\n"
					+ "                <tr>\r\n" + "                    <td>Payable amount to dealer </td>\r\n"
					+ "                    <td>Rs. " + twowheelerDetailesTable.getFinaldisbustmentAmount() + "</td>\r\n"
					+ "                </tr>\r\n" + "                <tr>\r\n"
					+ "                    <td>Dealer name </td>\r\n" + "                    <td>"
					+ twowheelerDetailesTable.getDealerName() + "</td>\r\n" + "                </tr>\r\n"
					+ "            </table>\r\n"
					+ "            <p> <b>The above approval is subject to signed receipt of the following pre and post disbursement documents.</b></p>\r\n"
					+ "            <p> &bull; &nbsp;&nbsp;&nbsp;Deed of Hypothecation</p>\r\n"
					+ "            <p> &bull; &nbsp;&nbsp;&nbsp;PDS/ECS/NACH mandate form for the entire tenure of the loan</p>\r\n"
					+ "            <p> &bull; &nbsp;&nbsp;&nbsp;Completion of all KYC norms as required by the Bank.</p>\r\n"
					+ "            <p> &bull; &nbsp;&nbsp;&nbsp;Loan Agreement to be franked to the extent of loan amount as per applicable Stamp Duty.</p>\r\n"
					+ "            <div style=\"font-size: 10px;\">"
					+ "            <p> <b>Other Terms and Conditions:</b></p>\r\n"
					+ "            <p> 1. Bank reserves the right to amend any of the terms and conditions of the sanction letter of cancel/recall the sanction letter at\r\n"
					+ "                any time at its sole discretion.</p>\r\n"
					+ "            <p> 2. The borrower shall execute the Bank's Loan Agreement and other documents; create security (including Hypothecation) and all\r\n"
					+ "                the documents as required by the bank. The borrower(s) shall also comply with any other requirement as specifed by the Bank</p>\r\n"
					+ "            <p> 3. The borrower(s) shall be deemed to have given their express consent to the bank to disclose the information and data\r\n"
					+ "                furnished by them to the bank/regulator and those regarding the credit facilities to TransUnion CIBIL Limited(CIBIL) and any\r\n"
					+ "                such credit information companies as defned by the RBI.</p> \r\n"
					+ "            <p>4. This sanction letter shall automatically cancelled and withdrawn at the end of 30 days from the issuance of this sanction letter,\r\n"
					+ "                unless the borrower complies with the conditions herein contained in the form and manner acceptable to Bank and avails\r\n"
					+ "                disbursement of the referred loan facility from the bank.</p>\r\n"
					+ "            <p> 5. This sanction letter is being sent in duplicate. Kindly return to the bank a duplicate here of duly signed by Borrower(s) as a\r\n"
					+ "                token of acceptance to the above and call the Bank for availing the loan facility in terms hereof.</p> \r\n"
					+ "            <p> 6. The borrower unconditionally agrees, undertakes and acknowledges that the Bank has an unconditional right to cancel the unutilized portion of the facility, whether in part or in full, at any time during the currency of the Facility without any prior\r\n"
					+ "                intimation for such cancellation to the Borrower.</p>\r\n"
					+ "             </div>"
					+ "            <br>\r\n" + "            <p> Thank you</p>   \r\n"
					+ "            <p> For Suryoday Small Finance Bank Ltd</p>\r\n"
					+ "            <div class=\"logo\" style=\"text-align: left;\">\r\n"
					+ "                <img style=\"width: 20%;\" src=" + x.LOGO + "Signature2.png" + ">\r\n"
					+ "              </div>\r\n" + "            <p> Authorized Signatory</p>\r\n"
					+ "            <br>\r\n"
					+ "            <p style=\"text-align: center; color: blue; margin-bottom: 0; font-size: 13px;\"> SURYODAY SMALL FINANCE BANK LIMITED</p>\r\n"
					+ "            <p style=\"color: blue; text-align: center; margin: 0; padding-top: 0;font-size: 12px;\"> Regd & Corp Offce : 1101, Sharda Terraces, Plot 65, Sector 11, CBD Belapur, Navi Mumbai-400614</p> \r\n"
					+ "            <p style=\"color: blue; text-align: center;margin: 0; padding-top: 0;font-size: 12px;\">Tel: 022-40435800 Email: info@suryodaybank.com | Web: www.suryodaybank.com | CIN: U65923MH2008PLC261472|GSTIN : 27AAMCS5499J1ZG</p>\r\n"
					+ "        </div>    \r\n" + "        </div>\r\n" + "    </body>\r\n" + "</html>      "));
		} else {
		}
		return htmlString.toString();
	
	}

	@Override
	public String loanApplicationFormPdf(StringBuilder htmlString, String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerdetailsrepository.getByApplicationNo(applicationNo);
		TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
		List<TwowheelerImage> list = twowheelerimagerepository.getByApplicationNoAndDocumentType(applicationNo, "customerPhoto");
		byte[] images = list.get(0).getImages();
		String encoded = Base64.getEncoder().encodeToString(images);
		System.out.println("Image"+encoded);
		TwoWheelerFamilyMember member = familyMemberService.getByApplicationNoAndMember(applicationNo, "APPLICANT");
		TwoWheelerFamilyMember nominee = familyMemberService.getByApplicationNoAndMember(applicationNo, "Nominee");
		String addressC="";
		String cityC="";
		String pincodeC="";
		String cityP="";
		String addressP="";
		String stateC="";
		String stateP="";
		String pincodeP="";
		String addressO="";
		String cityO="";
		String pincodeO="";
		String stateO="";
		if (twowheelerDetailesTable.getAddress() != null) {
			org.json.JSONArray addressArray = new org.json.JSONArray(twowheelerDetailesTable.getAddress());
			for (int n = 0; n < addressArray.length(); n++) {
				JSONObject addressInJson = addressArray.getJSONObject(n);
				if(addressInJson.getString("addressType").equalsIgnoreCase("CURRENT ADDRESS")) {
					 addressC = addressInJson.getString("address_Line1");
					 cityC = addressInJson.getString("city");
					 pincodeC = addressInJson.getString("pincode");
					 stateC = addressInJson.getString("state");
				}else if(addressInJson.getString("addressType").equalsIgnoreCase("Permanent ADDRESS")) {
					 addressP = addressInJson.getString("address_Line1");
					 cityP = addressInJson.getString("city");
					 pincodeP = addressInJson.getString("pincode");
					 stateP = addressInJson.getString("state");
				}else if(addressInJson.getString("addressType").equalsIgnoreCase("Office ADDRESS")) {
					 addressO = addressInJson.getString("address_Line1");
					 cityO = addressInJson.getString("city");
					 pincodeO = addressInJson.getString("pincode");
					 stateO = addressInJson.getString("state");
				}
			}
		}
		String referenceName="";
		String referenceName1="";
		String address="";
		String address1="";
		String pineCode="";
		String pineCode1="";
		String referenceType="";
		String referenceType1="";
		String mobileNumber="";
		String mobileNumber1="";
		
		if(twowheelerDetailesTable.getReferenceInfo() !=null) {
			org.json.JSONArray referenceArray = new org.json.JSONArray(twowheelerDetailesTable.getReferenceInfo());
				JSONObject addressInJson = referenceArray.getJSONObject(0);
				referenceName = addressInJson.getString("referenceName");
				address = addressInJson.getString("addressLine1");
				pineCode = addressInJson.getString("pineCode");
				referenceType = addressInJson.getString("referenceType");
				mobileNumber = addressInJson.getString("mobileNumber");
				if(referenceArray.length()==2) {
				JSONObject addressInJson1 = referenceArray.getJSONObject(1);
				referenceName1 = addressInJson1.getString("referenceName");
				address1 = addressInJson1.getString("addressLine1");
				pineCode1 = addressInJson1.getString("pineCode");
				referenceType1 = addressInJson1.getString("referenceType");
				mobileNumber1 = addressInJson1.getString("mobileNumber");	
				}
				
		}
		LocalDateTime createdTimestamp = twowheelerDetailesTable.getCreatedTimestamp();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String applicationDate = createdTimestamp.format(dateTimeFormatter);
		String dateOfBirth = twowheelerDetailesTable.getDateOfBirth();
//		LocalDate dob = LocalDate.parse(dateOfBirth);
//		String newDOb = dob.format(dateTimeFormatter);
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		htmlString.append(new String("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "    <head>\r\n"
				+ "        <style>\r\n"
				+ "          table,\r\n"
				+ "    th,\r\n"
				+ "    td {\r\n"
				+ "      border: 1px solid black;\r\n"
				+ "      border-collapse: collapse;\r\n"
				+ "    }\r\n"
				+ "        </style>\r\n"
				+ "      </head>\r\n"
				+ "    <body>\r\n"
				+ "        <div class=\"logo\" style=\"text-align: right;\">\r\n"
				+ "            <img style=\"width: 20%;\" src=" + x.LOGO + "logo.jpg" + ">\r\n"
				+ "          </div>\r\n"
				+ "        <div class=\"container\">\r\n"
				+ "        <h3 style=\"text-align: center; font-size: 20px;\">Two Wheeler Loan Application Form</h3>\r\n"
				+ "            <p> Application Date: &nbsp;&nbsp;"+applicationDate+"<span class=\"tab\" style=\"padding-left: 250px;\">Application Number:&nbsp;&nbsp;"+twowheelerDetailesTable.getApplicationNo()+"</span></p>\r\n"
				+ "            <p> RM Code: &nbsp;&nbsp;xxxxx</p> \r\n"
				+ "            <div class=\"img\" style=\"padding-left: 300px; display: flex;\"> \r\n"
				+ "            <div class=\"img2\" style=\"height: 150px ; width: 33%; text-align: center; border: 1px solid black;\">\r\n"
				+ "                <img src ='data:image/jpeg;base64,"+encoded+" 'style=\"height: 140px; width: 125px;  margin-top: 5px;\">\r\n"
				+ "                \r\n"
				+ "                </div> \r\n"
				+ "                <div class=\"img2\" style=\"height: 150px ; width: 33%; text-align: center; border: 1px solid black;\">\r\n"
				+ "                    <img src='data:image/jpeg;base64,' style=\"height: 140px; width: 125px;  margin-top: 5px;\">\r\n"
				+ "                \r\n"
				+ "                    </div> \r\n"
				+ "            </div>\r\n"
				+ "            <br>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Application Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Name of the applicant</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+twowheelerDetailesTable.getName()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Date of birth of the applicant</td>\r\n"
				+ "                    <td>"+dateOfBirth+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Customer ID</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getCustomerId()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>PAN Number</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getPancard()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Voter ID number</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getVoterId()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>DL Number</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Gender</td>\r\n"
				+ "                    <td>"+member.getGender()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Marital Status</td>\r\n"
				+ "                    <td>"+member.getMarried()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Education:</td>\r\n"
				+ "                    <td>"+member.getEducation()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Occupation details:</td>\r\n"
				+ "                    <td>"+member.getOccupation()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Mobile Number:</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getMobileNo()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Email ID:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Address Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Communication Address</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+addressC+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>City</td>\r\n"
				+ "                    <td>"+cityC+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>State</td>\r\n"
				+ "                    <td>"+stateC+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Pincode</td>\r\n"
				+ "                    <td>"+pincodeC+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Residence Ownership:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Years of current residence:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Permanent Address</td>\r\n"
				+ "                    <td>"+addressP+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>City</td>\r\n"
				+ "                    <td>"+cityP+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>State</td>\r\n"
				+ "                    <td>"+stateP+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Pincode</td>\r\n"
				+ "                    <td>"+pincodeP+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Residence Ownership:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Years of current residence:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Employer/Business Address:</td>\r\n"
				+ "                    <td>"+addressO+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td> Name of the employer/Business:</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>City</td>\r\n"
				+ "                    <td>"+cityO+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>State</td>\r\n"
				+ "                    <td>"+stateO+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Pincode</td>\r\n"
				+ "                    <td>"+pincodeO+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Employer/Business Mobile Number</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Years in current Job/Business</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>  \r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Bank Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Bank Name</td>\r\n"
				+ "                    <td style=\"width: 300px;\">SURYODAY SMALL FINANCE BANK</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Account Number</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getAccountNumber()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>IFSC Code</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getAccountIfsc()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Type of account</td>\r\n"
				+ "                    <td>SAVING</td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"3\" style=\"background-color: orangered; text-align: left;;\">Reference Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td style=\"text-align: center;\">Parameters</td>\r\n"
				+ "                    <td style=\"text-align: center;\">Reference 1</td>\r\n"
				+ "                    <td style=\"text-align: center;\">Reference 2</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Name</td>\r\n"
				+ "                    <td>"+referenceName+"</td>\r\n"
				+ "                    <td>"+referenceName1+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Address</td>\r\n"
				+ "                    <td>"+address+"</td>\r\n"
				+ "                    <td>"+address1+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Pincode</td>\r\n"
				+ "                    <td>"+pineCode+"</td>\r\n"
				+ "                    <td>"+pineCode1+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Relation</td>\r\n"
				+ "                    <td>"+referenceType+"</td>\r\n"
				+ "                    <td>"+referenceType1+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Email ID</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Mobile Number</td>\r\n"
				+ "                    <td>"+mobileNumber+"</td>\r\n"
				+ "                    <td>"+mobileNumber1+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Details of Co-applicant</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Name</td>\r\n"
				+ "                    <td style=\"width: 300px;\"></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>DOB</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Relationship</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Address</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>City</td>\r\n"
				+ "                    <td style=\"width: 300px;\"></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>State</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Pincode</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Occupation</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>PAN</td>\r\n"
				+ "                    <td style=\"width: 300px;\"></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Aadhar</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>VoterID</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Mobile</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Email ID</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Income Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Gross Salary/income per month</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+member.getAnnualIncome()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Details of vehicle to be purchased</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Cost of vehicle (Ex-showroom)</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+twowheelerDetailesTable.getExShowroomPrice()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Cost of registration</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Road Tax</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Insurance</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getInsuranceEmi()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Accessories</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+twowheelerDetailesTable.getAccessories()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Addon Charges </td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getAddonsCharges()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>On road price of vehicle</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getTotalOnRoadPrice()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Tenure</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getTenure()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Make/Model</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+twowheelerDetailesTable.getModel()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Name of dealer</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getDealerName()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Loan Request amount</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getAmount()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Margin amount</td>\r\n"
				+ "                    <td>"+twowheelerDetailesTable.getMarginMoney()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Loan Purpose</td>\r\n"
				+ "                    <td>TWO WHEELER</td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <br>\r\n"
				+ "            <br>\r\n"
				+ "            <table style=\"width: 98%;\">\r\n"
				+ "                <th colspan=\"2\" style=\"background-color: orangered; text-align: left;;\">Nominee Details</th>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Name</td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+nominee.getFirstName()+" "+nominee.getLastName()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>DOB</td>\r\n"
				+ "                    <td>"+nominee.getDob()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Age</td>\r\n"
				+ "                    <td>"+nominee.getAge()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Gender</td>\r\n"
				+ "                    <td>"+nominee.getGender()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Relationship with Applicant </td>\r\n"
				+ "                    <td style=\"width: 300px;\">"+nominee.getNomineeRelationship()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Address</td>\r\n"
				+ "                    <td>"+nominee.getAddress()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Mobile Number</td>\r\n"
				+ "                    <td>"+nominee.getMobile()+"</td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Percentage of Share</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Appointee Name </td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Appointee DOB</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td>Appointee Relationship</td>\r\n"
				+ "                    <td></td>\r\n"
				+ "                </tr>\r\n"
				+ "            </table>\r\n"
				+ "            <br>\r\n"
				+ "            <p>Declaration:</p>\r\n"
				+ "        </div>\r\n"
				+ "    </body>\r\n"
				+ "</html>      "));
		return htmlString.toString();
	}

	@Override
	public JSONObject sendMedia(JSONObject jsonObject, JSONObject Header) {
		JSONObject sendResponse = new JSONObject();
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		JSONObject request = getRequest(applicationNo, documentType);
		System.out.println(request.toString());
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			// GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			logger.debug(x.BASEURLV3 + "whatsapp/media/template/push?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV3 + "whatsapp/media/template/push?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", Header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", Header.getString("X-Request-ID"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(String applicationNo, String documentType) {
		String base64="";
		List<TwowheelerImage> list = twowheelerimagerepository.getByApplicationNoAndDocumentType(applicationNo, documentType);
		if(list.size() !=0) {
			TwowheelerImage twowheelerimage = list.get(0);
			byte[] images = twowheelerimage.getImages();
			 base64 = Base64.getEncoder().encodeToString(images);
		}
		
		Optional<TwowheelerDetailesTable> optional = twowheelerdetailsrepository.getByApplicationNo(applicationNo);
		org.json.JSONObject request = new org.json.JSONObject();
		if(optional.isPresent()) {
		TwowheelerDetailesTable twdetails = optional.get();
		org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
		org.json.simple.JSONArray Messages = new org.json.simple.JSONArray();
		org.json.simple.JSONArray Parameters = new org.json.simple.JSONArray();
		org.json.simple.JSONObject Parameter1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject Parameter2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject massage = new org.json.simple.JSONObject();
		massage.put("Sender", "919990082544");
		massage.put("To", "91" + twdetails.getMobileNo());
		massage.put("MessageId", "2993b6b54800sfsdffa51");
		massage.put("TransactionId", "2993b6b548000aasddfsd");
		massage.put("Channel", "wa");
		massage.put("Type", "mediaTemplate");
		Parameter1.put("Name", "1");
		Parameter1.put("Value", twdetails.getName());
		Parameter2.put("Name", "2");
		Parameter2.put("Value", "225020000070");
		Parameters.add(Parameter1);
		Parameters.add(Parameter2);
		org.json.simple.JSONObject MediaTemplate = new org.json.simple.JSONObject();
		MediaTemplate.put("ContentType", "application/pdf");
		MediaTemplate.put("Template", "praprloanscnltr");
		MediaTemplate.put("LangCode", "en");
		MediaTemplate.put("Filename", "SANCTION_LETTER");
		MediaTemplate.put("Content", base64);
		MediaTemplate.put("Parameters", Parameters);
		massage.put("MediaTemplate", MediaTemplate);
		Messages.add(massage);

		Data.put("Messages", Messages);
		Data.put("ResponseType", "json");
		request.put("Data", Data);
}
		return request;
	}

	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("POST request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}

		return sendAuthenticateResponse;

	}

	@Override
	public String loanAgreementPdf(StringBuilder htmlString, String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerdetailsrepository.getByApplicationNo(applicationNo);
//		List<LeegalityInfo> listDB = leegalityservice.getByApplicationNoAndDocument(applicationNo, "loanAgreement");
//		LeegalityInfo leegalityInfo = listDB.get(0);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			if(twowheelerDetailesTable.getEngineNumber() == null || twowheelerDetailesTable.getEngineNumber().equals("") ||
					  twowheelerDetailesTable.getChasisNumber() == null || twowheelerDetailesTable.getChasisNumber().equals("")	) {
						throw new EmptyInputException("Please insert the Engine number and Chasis number before proceeding with Loan Agreement process.");
					}
			String address_Line1="";
			String address_Line2="";
			String address_Line3="";
			String city="";
			String pincode="";
			String country="";
			String district="";
			String state="";
			if (twowheelerDetailesTable.getAddress() != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(twowheelerDetailesTable.getAddress());
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					if(addressInJson.getString("addressType").equalsIgnoreCase("CURRENT ADDRESS")) {
						 address_Line1 = addressInJson.getString("address_Line1");
						 address_Line2 = addressInJson.getString("address_Line2");
						 address_Line3 = addressInJson.getString("address_Line3");
						 city = addressInJson.getString("city");
						 pincode = addressInJson.getString("pincode");
						 country = addressInJson.getString("country");
						 district = addressInJson.getString("district");
						 state = addressInJson.getString("state");
					}
				}
			}
			String processingFee="";
			String docCharges="";
			if(twowheelerDetailesTable.getLoanCharges() !=null) {
	            org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
	            for(int n=0;n<loanCarges.length();n++) {
	                JSONObject jsonObject = loanCarges.getJSONObject(n);
	                if(jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee"))
	                {
	                	processingFee=jsonObject.getString("totalAmount");
	                }
	                else if(jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge"))
	                {
	                	docCharges=jsonObject.getString("totalAmount");
	                }
	            }
	        }
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String todayDate = LocalDate.now().format(dateTimeFormatter);
			ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
			htmlString.append(new String("<!doctype html>\r\n" + "<html class=\"h-full bg-white\">\r\n" + "	<head>\r\n"
					+ "		<meta charset=\"UTF-8\">\r\n"
					+ "		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "		<link rel=\"shortcut icon\" href=\"https://www.suryodaybank.com/favicon.ico\" type=\"image/x-icon\">\r\n"
					+ "		<title>SuryodayBank | Loan Agreement</title>\r\n" + "		<style>\r\n"
					+ "			html * {\r\n" + "				font-size: 12px;\r\n"
					+ "				font-family: Verdana, Arial, Helvetica, sans-serif;\r\n" + "			}\r\n"
					+ "			table, th, td {\r\n" + "				padding-top: 4px;\r\n"
					+ "				padding-left: 4px;\r\n" + "				border: 1.5px solid black;\r\n"
					+ "				border-collapse: collapse;\r\n" + "			}\r\n" + " .header1 {\r\n"
					+ "              display: flex;\r\n" + "			  justify-content: space-between;\r\n"
					+ "			  align-items: center;\r\n" + "			}" 
					+ "          .line-under-text{\r\n"
					+ "			   border-top: 1px solid black;\r\n"
					+ "			   margin-top:20px;\r\n"
					+ "			}"
					+         ".header2 {\r\n"
					+ "           display: flex;\r\n"
					+ "			  padding: 20px 10px;\r\n"
					+ "			  width: 100%;\r\n"
					+ "		}"
					+ "		</style>\r\n" + "	</head>\r\n"
					+ "	<body>\r\n" + " <div class=\"logo header1\">"
					+ "			<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "			<h3 style=\"margin: auto 120px\">Stamp Sr. No.: 17AC 886483</h3>\r\n"
					+ "            <img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "          </div>" + " <h3 style=\"text-align: center;\">TWO-WHEELER LOAN AGREEMENT</h3>\r\n"
					+ "		<h3 style=\"text-align: center;\">FACILITY CUM HYPOTHECATION AGREEMENT</h3>\r\n"
					+ "		<p>This facility cum hypothecation agreement(\"Agreement\")shall govern the Facility provided/to be provided to\r\n"
					+ "			the Borrower(s) by Suryoday Small Finance Bank Limited( \"Suryoday\" ) and shall form an integral part of the Facility\r\n"
					+ "			Documents</p>\r\n" + "		<h3>1. DEFINITIONS AND INTERPRETATION</h3>\r\n"
					+ "		<p style=\"font-weight: bold;\">1.1 Defnitions</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">In this Agreement the following words and expressions, unless repugnant to the meaning or context thereof shall\r\n"
					+ "			have the following meanings:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Application Form\"</b> means the initial form submitted by the Borrower(s) to Suryoday for applying/ availing the\r\n"
					+ "			Facility, all its annexures and all other documents, information, clarifcations and declarations, if any, furnished by\r\n"
					+ "			the Borrower(s);</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Applicable Law\"</b> means a statute, regulation, notifcation, circular, ordinance, requirement, direction, guideline,\r\n"
					+ "			announcement or other binding action or requirement of a government authority including court of law having\r\n"
					+ "			jurisdiction over this Agreement;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Borrower(s)\"</b> shall, mean such individual(s) as set out in the Schedule to whom Suryoday has agreed to grant/\r\n"
					+ "			granted the Facility, and the term shall where and when applicable, include their heirs, legal representatives,\r\n"
					+ "			successors, permitted assigns, executors, receivers, administrators as the case may be;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Due Date/s\"</b> means any date on which (a) an EMI is payable as per the Repayment Schedule, or (b) other fees,\r\n"
					+ "			costs, charges, expenses, stamp duty or any other monies whatsoever payable by the Borrower(s), fall due in terms\r\n"
					+ "			of the Facility Documents;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Dealer\"</b> means the seller or distributor of the Vehicle (and shall include the manufacturer of the Vehicle), as may be\r\n"
					+ "			applicable, who may be registered and enrolled with Suryoday;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\\Equated Monthly Instalment \\ or \\EMI\"</b> means the amount of monthly payment as set out in the Schedule\r\n"
					+ "			necessary to amortize the Facility with interest within such period as may be determined by Suryoday from time to\r\n"
					+ "			time;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Event of Default\"</b> shall mean the event(s) of default as provided under clause 10 of this Agreement;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Facility\"</b> means the amount of loan sanctioned to the Borrower(s) as specifed in the Schedule and which is\r\n"
					+ "			actually disbursed or to be disbursed by Suryoday to the Borrower(s) in terms of the Facility Documents;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Facility Documents\"</b> means collectively the Application Form, this Agreement, and all such other documents\r\n"
					+ "			incidental to the Facility including the deeds, power of attorney, security documents, undertakings, extensions,\r\n"
					+ "			addendums, novation agreements, etc., as may be required by Suryoday and notices/ letters issued by Suryoday:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Outstanding Amount\"</b> shall mean and include any one or more of the following:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(i) all/ any amounts payable by way of the repayment obligations in respect of principal, interest, additional interest\r\n"
					+ "			(due to application of Penal Interest), charges levied by Suryoday for prepayment, charges as set out in the\r\n"
					+ "			Schedule, and other costs, expenses, taxes in relation to the Facility payable by the Borrower(s); and</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(ii) costs, charges and expenses payable for possession/ re-possession of the Vehicle and any other costs incurred\r\n"
					+ "			<b>by the Suryoday in realizing monies and its appropriation towards the Facility.</b></p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Party\"</b> shall mean, individually, the Borrower(s) and Suryoday and Parties shall mean collectively both;</p>\r\n"
					+ "		<p style=\"padding-left: 15px; font-weight: bold;\">\"Penal Interest\" shall mean the interest charged by Suryoday on all amounts not paid when due for payment (or\r\n"
					+ "			reimbursement) by the Borrower(s) to Suryoday, and payable in accordance with clause 3.2 hereof;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Prepayment\"</b> where permitted, means premature repayment of the Facility in part or full, including principal\r\n"
					+ "			sum, interest thereon, and all Outstanding Amount which is not yet due for payment by the Borrower(s) under the\r\n"
					+ "			Facility Documents;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Purpose\"</b> means availing of the Facility by the Borrower(s) to purchase the Vehicle for personal use;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Rate of Interest\"</b> means the rate of interest applicable to the Facility as set out in the Schedule;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Repayment\"</b> means the repayment of the principal amount of the Facility, interest thereon, Penal Interest and all\r\n"
					+ "			other Outstanding Amount and all other charges and dues payable by the Borrower(s) under the Facility\r\n"
					+ "			Documents; and/ or installments for re- payment of the Facility as maybe agreed by the Borrower(s) in the Agreement;</p>\r\n"
					+ "     <div class=\"header2\">\r\n"
					+ "			<div>\r\n"
					+ "				<b>Signature of Borrower</b>\r\n"
					+ "				<hr class=\"line-under-text\" >\r\n"
					+ "			</div>\r\n"
					+ "			<div style=\"margin:0 100px\">\r\n"
					+ "				<b>Signature of Co-Applicant</b>\r\n"
					+ "				<hr class=\"line-under-text\">\r\n"
					+ "			</div>\r\n"
					+ "			<div>\r\n"
					+ "				<b>Signature of Authorized Signatory</b>\r\n"
					+ "				<hr class=\"line-under-text\">\r\n"
					+ "			</div>\r\n"
					+ "		</div>"
					+ "     <div class=\"logo header1\">\r\n"
					+ "			<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "         <h3 style=\"margin: 0 110px;\">Stamp Sr. No.: 17AC 886483</h3>"
					+ "            <img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "          </div>"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Repayment Schedule\"</b> shall mean the manner in which the repayment of Facility and interest thereon by way\r\n"
					+ "			installments shall be made by the Borrower(s);</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"RBI\"</b> means the Reserve Bank of India; and</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\"><b>\"Vehicle\"</b> means the two-wheeler to be purchased by the Borrower(s) for which the Facility is sanctioned bearing\r\n"
					+ "			such description, more particularly set out in Schedule.</p>"
					+ "     <p style=\"font-weight: bold;\">1.2 Interpretation</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(i) The headings and titles in the Facility Documents are inserted solely for convenience of reference and shall, in\r\n"
					+ "			no way defne, limit, construe or deem to affect the construction/ interpretation/ meaning/ scope/ extent of the\r\n"
					+ "			relative provisions.</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(ii) References to the masculine gender shall include references to the feminine gender or neuter gender as the\r\n"
					+ "			case may be and vice versa.</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(iii) References to the singular number shall include references to the plural number and vice versa in the context thereto.</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(iv) All references to the Facility Documents and/ or any of its schedules/annexures or any other agreements, documents,\r\n"
					+ "			instruments or schedules, exhibits, appendices, hall include (subject to all relevant approvals) a reference to the\r\n"
					+ "			Facility Documents and/or its schedules/ annexures and that other agreement, document, instruments, schedules,\r\n"
					+ "			exhibits or appendices, as modifed, supplemented, revised, substituted, novated or assigned from time to time.</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(v) The schedule/ annexures hereto and any amendments thereof shall be deemed to be part of the Facility Documents\r\n"
					+ "			as if the provisions thereof were set out herein in extension.</p>\r\n"
					+ "		<h3>2. FACILITY AND DISBURSEMENT</h3>\r\n"
					+ "		<p>2.1 &nbsp;&nbsp;The Borrower(s) has applied for Facility by way of an Application Form which was sanctioned by Suryoday</p>\r\n"
					+ "		<p>2.2 &nbsp;&nbsp;Parties agree that the disbursement of the Facility shall occur directly to the Dealer, upon the request of the Borrower(s).\r\n"
					+ "			Borrower(s) agrees and acknowledges that this disbursement to the Dealer shall be deemed to be a disbursement made\r\n"
					+ "			to the Borrower(s). Further, the Borrower(s) undertake(s) to acknowledge the receipt of the Facility or such part thereof\r\n"
					+ "			that has been disbursed, in the form required by Suryoday.</p>\r\n"
					+ "		<p>2.3 &nbsp;&nbsp;The Borrower(s) shall use the Facility only for the Purpose and not for any other purpose. The Borrower(s) shall provide\r\n"
					+ "			an �end use certifcate�, in a form and manner acceptable to Suryoday certifying that the end use of the Facility is\r\n"
					+ "			commensurate with the Purpose for which the Facility was sanctioned, if required by Suryoday.</p>\r\n"
					+ "		<p>2.4 &nbsp;&nbsp;The Facility amount disbursed to the Borrower(s) shall be exclusive of the administrative fees or any other fee charges\r\n"
					+ "			as stated in the Schedule.</p>\r\n"
					+ "		<p>2.5 &nbsp;&nbsp;Suryoday may at its sole discretion, suspend or cancel the Facility or any part thereof by issuing notice to the\r\n"
					+ "			Borrower(s). The Borrower(s) shall not cancel the Facility or any part thereof without the prior written approval of Suryoday.</p>\r\n"
					+ "		<p>2.6 &nbsp;&nbsp;This Agreement is a pure fnancing agreement and the Borrower(s) accepts the entire risk of non- performance, nondelivery, breach and/ or any other service defciency in relation to the Vehicle by the Dealer. The Borrower(s)\r\n"
					+ "			acknowledges that Suryoday shall not be liable to the Borrower(s) for any delay, non performance or any other claim,\r\n"
					+ "			loss, or expense of any kind caused directly or indirectly in relation to the Vehicle. The Borrower(s) shall continue to pay\r\n"
					+ "			the Outstanding Amount on the Due Dates and perform its other obligations under this Agreement regardless of any\r\n"
					+ "			issue of Borrower(s) with the Dealer in relation to the Vehicle.</p>\r\n"
					+ "		<h3>3. INTEREST AND PENAL INTEREST</h3>\r\n"
					+ "		<p>3.1 &nbsp;&nbsp;The Borrower(s) shall pay interest on the Facility at the Rate of Interest from the date of disbursal of the Facility. The\r\n"
					+ "			Borrower(s) acknowledges that the Rate of Interest is subject to change, at the discretion of Suryoday, and/ or in\r\n"
					+ "			accordance with Applicable Law. The interest shall be calculated at the end of each day and shall be computed based on\r\n"
					+ "			365 � day year.</p>\r\n"
					+ "		<p>3.2 &nbsp;&nbsp;The Borrower(s) covenants with Suryoday that, without prejudice to other rights and privileges available to Suryoday\r\n"
					+ "			under any Facility Documents and/ or under Applicable Law, upon any\r\n"
					+ "			(a) default in payment/ re-payment of dues under the Facility Documents, or (b) occurrence of any other Event of Default,\r\n"
					+ "			Suryoday shall be entitled to recover additional interest at the Penal Interest over and above the prevailing applicable\r\n"
					+ "			Rate of Interest by way of liquidated damages on the Outstanding Amount, payable from the date (I) when any monies/\r\n"
					+ "			dues payable by the Borrower(s) under this Agreement becomes due butis unpaid, (ii) when an Event of Default occurs,\r\n"
					+ "			from that date until the date when Borrower(s) cures such default and satisfes Suryoday in full in respect to the above</p>\r\n"
					+ "     <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "			<div class=\"logo header1\">\r\n"
					+ "				<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "         <h3 style=\"margin: 0 110px;\">Stamp Sr. No.: 17AC 886483</h3>"
					+ "				<img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "			  </div>"
					+ "		<h3>4. REPAYMENT</h3>\r\n"
					+ "		<p>4.1 &nbsp;&nbsp;The Borrower(s) shall pay the EMI as mentioned in the Schedule on a monthly basis more specifcally on the respective\r\n"
					+ "			Due Dates, which may be revised from time to time. For convenience, Parties agree that the EMIs should remain\r\n"
					+ "			constant, irrespective of change in Rate of Interest, due to which the number of EMIs shall vary. However, not\r\n"
					+ "			withstanding the above, Suryoday shall be entitled to increase the EMI and/ or the number of EMIs, at its discretion and\r\n"
					+ "			the Borrower(s) shall pay such revised EMI upon notifcation</p>\r\n"
					+ "		<p>4.2 &nbsp;&nbsp;The Borrower(s) gives/ agrees to give duly registered mandate for Electronic Clearing House (ECS) mandates/ National\r\n"
					+ "			Automated Clearing House (NACH) mandates/ cheques (�Instrument�) for the repayment of the Facility.</p>\r\n"
					+ "		<p>4.3 &nbsp;&nbsp;The Borrower(s) agrees that a dishonor/ revocation of the Instrument given by the Borrower(s), and a claim of\r\n"
					+ "			invalidity/ unauthorized issuance of the Instrument given by the Borrower(s) shall be deemed to be a dishonor\r\n"
					+ "			of Instrument under the Applicable Law. The Borrower(s) shall be liable to pay dishonor charges as mentioned in\r\n"
					+ "			the Schedule. Upon the Instrument being dishonored/ stopped, the Borrower(s) shall replace the Instrument with\r\n"
					+ "			such other cheque/ ECS/ ACH or any other instrument notifed by the government and/ or RBI. The Borrower(s)\r\n"
					+ "			shall also pay the Outstanding Amount, besides the dishonor charges and other charges. Suryoday reserves the\r\n"
					+ "			right to take recourse under Applicable Law for dishonor of Instruments.</p>\r\n"
					+ "		<h3>5. PREPAYMENT</h3>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The Borrower(s) may, with the prior approval of Suryoday be entitled to prepay the Facility, either partly or fully,\r\n"
					+ "			subject to such terms as may be stipulated by Suryoday (including any pre-payment charges) in Suryoday�s\r\n"
					+ "			sole discretion.</p>\r\n"
					+ "		<h3>6. SECURITY</h3>\r\n"
					+ "		<p>6.1 &nbsp;&nbsp;The Borrower(s) hereby hypothecates the Vehicle and as by way of frst and exclusive charge in favor of\r\n"
					+ "			Suryoday as security for the timely repayment of the Facility and the Outstanding Amount to Suryoday.</p>\r\n"
					+ "		<p>6.2 &nbsp;&nbsp;The Borrower(s) shall ensure that suitable endorsement is duly made in the registration certifcate book, in favor\r\n"
					+ "			of Suryoday as the frst and exclusive charge holder and a certifed copy of the registration certifcate\r\n"
					+ "			of the Vehicle must be submitted to Suryoday. The hypothecation comes into effect immediately on signing of\r\n"
					+ "			this Agreement or delivery of the Vehicle.</p>\r\n"
					+ "		<p>6.3 &nbsp;&nbsp;The security and charge created over the Vehicle shall be independent of any security interest or any other\r\n"
					+ "			security or right or remedy now or at anytime hereafter held by or available to Suryoday. The security over the\r\n"
					+ "			Vehicle and all other rights of Suryoday under this Agreement shall continue to be valid and enforceable as a\r\n"
					+ "			security for the due repayment of the Outstanding Amount and upon the entire Outstanding Amounts being\r\n"
					+ "			repaid to Suryoday, to the satisfaction of Suryoday, Suryoday may issue a certifcate releasing the security so\r\n"
					+ "			created.</p>\r\n"
					+ "		<h3>7. COVENANTS</h3>\r\n"
					+ "		<p>7.1 &nbsp;&nbsp;The Borrower(s) hereby expressly agree, confrm, assure, declare and covenants as follows:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">i. to obtain, produce and deposit with Suryoday a duly certifed true copy of all requisite permissions, clearances\r\n"
					+ "			and approvals as may be required obtained/ to be obtained under the Applicable Law with respect to (i) availing\r\n"
					+ "			the Facility; and (ii) any matter pertaining to the Facility;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">ii. to maintain the Vehicle in good order and condition and make all necessary repairs, additions and improvements\r\n"
					+ "			thereto during the tenure of the Facility;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">iii. to pay all duties, taxes and fees and other outgoings payable in respect of the Vehicle as and when the same\r\n"
					+ "			becomes due and to indemnify Suryoday against all such payments;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">iv. to permit Suryoday and its agents/ authorized representatives to enter into the premises at reasonable hours for\r\n"
					+ "			inspecting the Vehicle;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">v. to notify Suryoday of any change in the Borrower(s), residence or correspondence address within a period of 15\r\n"
					+ "			(ffteen) days from the change of address;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">vi. notwithstanding any delay in delivery/ receipt of the Vehicle though/ from the Dealer, any damage, total loss,\r\n"
					+ "			theft, of the Vehicle, the obligations of the Borrower(s) under this Agreement continue;</p>\r\n"
					+ "      <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "			<div class=\"logo header1\">\r\n"
					+ "				<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "         <h3 style=\"margin: 0 110px;\">Stamp Sr. No.: 17AC 886483</h3>"
					+ "				<img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "			  </div>"
					+ "		<p style=\"padding-left: 15px;\">vii. shall not sell, assign, sublet, hypothecate or otherwise encumber the Vehicle until all Outstanding Amounts have\r\n"
					+ "			been paid to Suryoday in full;</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">viii. in the event the Borrower(s) commit(s) a default in Repayment, Suryoday and/or the RBI will have an unqualifed\r\n"
					+ "			right to disclose or publish the details of the default and the name of the Borrower(s) as defaulters in such\r\n"
					+ "			manner and through such medium as Suryoday or RBI in their absolute discretion deem ft; and</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">ix. it shall bear all costs of stamp duty and applicable taxes as also of making good any defcit in stamp duty and\r\n"
					+ "			applicable taxes on any Facility Document.</p>\r\n"
					+ "		<h3>8. BORROWER(S)� REPRESENTATION AND WARRANTIES</h3>\r\n"
					+ "		<p>8.1 &nbsp;&nbsp;The Borrower(s) (a) is competent to and has authority to enter into the Facility Documents and has made all\r\n"
					+ "			material disclosures in relation to itself to Suryoday; (b) is in compliance in all respects with all Applicable Laws\r\n"
					+ "			affecting its business and operations; and (c) has not been designated as a willful defaulter. There are no\r\n"
					+ "			circumstances that may at any time prevent or interfere with such compliance; and (d) shall not use the Facility\r\n"
					+ "			(or any part thereof) for any purpose other than the Purpose specifed in this Agreement, or for any speculative,\r\n"
					+ "			improper or illegal or unlawful purposes/ activities.</p>\r\n"
					+ "		<p>8.2  &nbsp;&nbsp;The Borrower(s) acknowledges, agrees and confrms that Suryoday shall be entitled:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(a) to exercise general lien and or other right of set-off for any balance due to Suryoday on any account or in\r\n"
					+ "			respect of any security/ deposit/ monies held by Suryoday; and/ or</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(b) to extend rights obligations and securities of the Borrower(s) under any account to any other account of the\r\n"
					+ "			Borrower(s) with Suryoday not with standing that such account may not relate to any particular facility\r\n"
					+ "			granted/ to be granted to the Borrower(s).</p>\r\n"
					+ "		<h3>9. INSURANCE</h3>\r\n"
					+ "		<p>9.1 &nbsp;&nbsp;The Borrower(s) undertakes to keep the Vehicle insured at all times until due repayment of the Facility to the\r\n"
					+ "			satisfaction of Suryoday. The Borrower(s) shall ensure that the insurance policy so availed is endorsed in favor of\r\n"
					+ "			Suryoday as �frst loss payee� until repayment of the Facility. Where the Borrower(s) fail to insure the Vehicle,\r\n"
					+ "			Suryoday may insure the same at the cost of the Borrower(s).</p>\r\n"
					+ "		<h3>10. EVENT OF DEFAULT AND REMEDIES OF SURYODAY</h3>\r\n"
					+ "		<p>10.1 &nbsp;&nbsp;Occurrence of any of the following events shall constitute an �Event of Default�: (a) non-payment of any monies in\r\n"
					+ "			respect of the Facility in accordance with the terms of the Facility Documents; (b) default, breach has occurred in the\r\n"
					+ "			performance of any covenant, condition or agreement on the part of the Borrower( s ) under the Facility Documents;\r\n"
					+ "			(c ) the event of death, insolvency, failure in business, commission of an act of bankruptcy of the Borrower(s), or\r\n"
					+ "			change or termination of employment/ profession/ business for any reason whatsoever; (d) the Borrower(s) is unable\r\n"
					+ "			or has admitted in writing its inability to pay any of its debts; (e) occurrence of an event of default, howsoever\r\n"
					+ "			described occurs under any agreement or document relating to any indebtedness of the Borrower(s) or if any other\r\n"
					+ "			lenders of the Borrower(s) including fnancial institutions or banks with whom the Borrower(s) has entered into\r\n"
					+ "			agreements for fnancial assistance have refused to disburse, extend, or have cancelled or recalled its/ their\r\n"
					+ "			assistance or any part thereof; (f) occurrence of any circumstance or event which adversely affects Borrower�s\r\n"
					+ "			ability/ capacity to pay/ repay the Outstanding Amounts or any part therefore perform any of the obligations;(g) one or\r\n"
					+ "			more events, conditions or circumstances (including any change in law) has occurred or is likely to occur which has a\r\n"
					+ "			material adverse effect or prejudices the rights of Suryoday under this Agreement.</p>\r\n"
					+ "		<p>10.2 &nbsp;&nbsp;On the occurrence of any Event of Default, Suryoday shall serve notice on the Borrower(s), specifying the Event(s) of\r\n"
					+ "			Default and at the sole discretion of Suryoday, provide the Borrower(s) with reasonable time to rectify such Event(s) of\r\n"
					+ "			Default if the Event of Default is capable of being rectifed. The Borrower(s) hereby unconditionally agree that any\r\n"
					+ "			failure of the Borrower(s) to rectify such default would constitute an Event of Default as specifed in Clause 10.1 above\r\n"
					+ "			and the Borrower(s) shall, after notice or lapse of time period mentioned in the notice for the rectifcation of the\r\n"
					+ "			default, be liable to repay forth with the entire Outstanding Amount, and shall handover peaceful possession of the\r\n"
					+ "			Vehicle to Suryoday or Suryoday�s representative/ agent in this regard, in a good and proper working condition.\r\n"
					+ "			Suryoday shall be entitled to take all steps under Applicable Law to recover the Outstanding Amount including repossession and sale of the Vehicle as per Applicable Law.</p>\r\n"
					+ "      <br><br><br><br>"
					+ "      <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "			<div class=\"logo header1\">\r\n"
					+ "				<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "         <h3 style=\"margin: 0 110px;\">Stamp Sr. No.: 17AC 886483</h3>"
					+ "				<img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "			  </div>"
					+ "		<h3>11. WAIVER</h3>\r\n"
					+ "		<p>11.1 &nbsp;&nbsp;No delay in exercising or omission to exercise any right, power or remedy accruing in favour of Suryoday upon any\r\n"
					+ "			default under any of the Facility Document shall impair any such right, power or remedy nor shall such delay or\r\n"
					+ "			omission be construed to be a waiver thereof or any acquiescence in such default by Suryoday, nor shall the action or\r\n"
					+ "			inaction of Suryoday in respect of any default or any acquiescence by Suryoday of any default affect or impair any\r\n"
					+ "			right, power or remedy of Suryoday in respect of any other default.</p>\r\n"
					+ "		<h3>12. MISCELLANEOUS</h3>\r\n"
					+ "		<p>12.1 &nbsp;&nbsp;Obligations of Borrower(s) Joint and Several</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The rights and obligations of Borrower(s) and Co-Borrower(s) under the Facility Documents shall be joint and several.\r\n"
					+ "			Notwithstanding any instructions issued to the Borrower(s) for repayment of Outstanding Amounts under this\r\n"
					+ "			Agreement each of the Borrower(s) shall be separately liable to Suryoday for Repayment of the Outstanding Amounts.</p>\r\n"
					+ "		<p>12.2 &nbsp;&nbsp;Inspection</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The Borrower(s) shall permit inspection of all books of accounts and other records maintained by him/ her/ it/ them in\r\n"
					+ "			respect of the Facility, by persons (including credit bureaus) appointed authorized by Suryoday.</p>\r\n"
					+ "		<p>12.3 &nbsp;&nbsp;Disclosure</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">i. In the event of the Borrower(s) committing an Event of Default, Suryoday shall be entitled to disclose to the RBI or\r\n"
					+ "			any any statutory and regulatory authority or to any other third person, the name/ identity of the Borrower(s) and the\r\n"
					+ "			default committed. In case of default in payment of any of the Outstanding Amount, Suryoday or the RBI shall have\r\n"
					+ "			an unqualifed right to disclose or publish the name & photograph of the Borrower(s) as defaulters (including as\r\n"
					+ "			willful defaulters/ non-cooperative borrower(s)) in such manner and through such medium as Suryoday and/ or the\r\n"
					+ "			RBI in their absolute discretion may deem ft. The Borrower(s) further agrees that Suryoday may, as it deem\r\n"
					+ "			appropriate and necessary disclose and furnish to the Credit Information Bureau (India) Limited (�CIBIL�), and any\r\n"
					+ "			other agency authorized in this behalf by RBI, all or any of the following:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">a. information and data relating to the Borrower(s);</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">b. the information or data relating to the Facilities availed of/ to be availed, by the Borrower(s); and</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">c. the information and details of the default, if any, committed by the Borrower(s), in discharge of the Outstanding Amount.</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">ii. The Borrower(s) agrees and undertakes that:</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">a. CIBIL, and any other agency so authorized may use, process the said information and data disclosed by Suryoday\r\n"
					+ "			in the manner deemed ft by them; and</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">b. CIBIL, and any other agency may furnish for consideration, the processed information and data or products\r\n"
					+ "			thereof obtained by them, to banks/ fnancial institutions and other credit grantors or registered users, as may be\r\n"
					+ "			specifed by the RBI in this regard</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">iii. The Borrower(s) hereby agrees and confrms that Suryoday shall have the right to disclose and share any\r\n"
					+ "			information pertaining to the Borrower(s) including, but not limited to, credit facility/ies, constitution, net worth with\r\n"
					+ "			(a) any other bank(s)/ fnancial institution(s) in the form and manner prescribed by the RBI; (b) any agent, contractor,\r\n"
					+ "			third party service provider or professional advisor (wherever situate) of Suryoday; (c) any person to (or through)\r\n"
					+ "			whom Suryoday assigns or transfers or novates (or may potentially assign or transfer or novate) all or any of its\r\n"
					+ "			rights or obligations under the Facility Documents; and (d) any person to whom Suryoday are required to make\r\n"
					+ "			disclosure under the requirements of any law, regulation, guidelines (whether SEBI, RBI, etc.) or practice.</p>\r\n"
					+ "		<p>12.4  &nbsp;&nbsp;Appointment of Collection Agents</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The Borrower(s) expressly recognizes and accepts that Suryoday may, without prejudice to their right to perform\r\n"
					+ "			such activities themselves or through their offcers or personnel, be absolutely entitled and have full powers and\r\n"
					+ "			authority to appoint one or more party(ies) of Suryoday�s choice, and to transfer and delegate to such party(ies) the\r\n"
					+ "			right and authority to collect on behalf of Suryoday all unpaid dues and to perform and execute all act(s), deed(s),\r\n"
					+ "			matter(s) and thing(s) connected therewith or incidental thereto including sending notices of demand, receiving the\r\n"
					+ "			Outstanding Amount (in cash/ through draft/ cheque) from the Borrower(s), entering into a compromise with the\r\n"
					+ "			Borrower(s), giving a valid receipt and grant effectual discharge to the Borrower(s) and generally performing all lawful\r\n"
					+ "			acts as the third party(s) may consider appropriate for the purpose.</p>\r\n"
					+ "         <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "			<div class=\"logo header1\">\r\n"
					+ "				<h3 style=\"text-align: left;\">Document ID: EN001AB693</h3>\r\n"
					+ "         <h3 style=\"margin: 0 110px;\">Stamp Sr. No.: 17AC 886483</h3>"
					+ "				<img style=\"width: 20%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
					+ "			  </div>"
					+ "		<p>12.5  &nbsp;&nbsp;Assignment</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The Borrower(s) shall not assign nor transfer all or any of its rights, benefts or obligations under the Facility\r\n"
					+ "			Documents without the prior written approval of Suryoday. Suryoday may, at any time, assign, securitize, novate or\r\n"
					+ "			transfer all or any of its rights, benefts and obligations under the Facility Documents or share the credit risk of the\r\n"
					+ "			whole or a part of the Facility with any other person by way of participation. Notwithstanding any such assignment,\r\n"
					+ "			transfer or participation, the Borrower(s) shall, unless otherwise notifed by Suryoday, continue to make all payments\r\n"
					+ "			under the Facility Documents to Suryoday and all such payments when made to Suryoday shall constitute a full\r\n"
					+ "			discharge of the Borrower(s) from all of his liabilities inrespect of such payments.</p>\r\n"
					+ "		<p>12.6 &nbsp;&nbsp;Service of Notice</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(I) Any notice or request to be given or made by the Borrower(s) shall be in writing. Such notice or request shall\r\n"
					+ "			be made to the branch offce of Suryoday or such other address/ email address indicated on its website\r\n"
					+ "			www.suryodaybank.com</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">(II) Notice shall be deemed to have been received by the Party to whom it is given or where the notice is addressed to\r\n"
					+ "			Suryoday, when it shall have been actually received by Suryoday and if addressed to the Borrower(s):\r\n"
					+ "			(a) if given by post (ordinary or registered post with acknowledgment due) on the expiration of 3 (three) calendar\r\n"
					+ "			days after the same shall have been delivered to the post offce;\r\n"
					+ "			(b) if given by courier on the expiration of 2 (two) calendar days after the same shall have been handed over to the\r\n"
					+ "			courier agency;\r\n"
					+ "			(c) if delivered personally, when left at the address of the Borrower(s) as aforesaid, and a certifcate by an offcer/\r\n"
					+ "			agent of Suryoday who sent such notice or communication that the same was so given or made shall be fnal\r\n"
					+ "			and conclusive; and\r\n"
					+ "			(d) if given by e-mail, when sent by the sender and no �delivery failure notifcation� (or similar notifcation) is\r\n"
					+ "			received by the sender</p>\r\n"
					+ "		<p>12.7 &nbsp;&nbsp;Amendments to Facility Documents</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">Suryoday shall have the absolute discretion to amend or supplement the Facility Documents, including making\r\n"
					+ "			changes based on guidelines/ directives issued by the RBI, from time to time and shall endeavour to give prior notice\r\n"
					+ "			by email or put up the same on the website, as the case may be, wherever feasible, and such amended terms and\r\n"
					+ "			conditions shall there upon be binding on the Borrower(s).</p>\r\n"
					+ "		<p>12.8 &nbsp;&nbsp;Severance</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">Any provision of the Facility Documents which is prohibited or unenforceable in any jurisdiction shall, as to such\r\n"
					+ "			jurisdiction, be ineffective to the extent of prohibition or unenforceability but shall not invalidate the remaining\r\n"
					+ "			provisions of the Facility Documents or affect such provision in any other jurisdiction.</p>\r\n"
					+ "		<p>12.9 &nbsp;&nbsp;Right of Appropriation</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">The Borrower(s) agrees and confrms that Suryoday may at its absolute discretion, appropriate any payments made by\r\n"
					+ "			the Borrower(s) under the Facility Documents, towards the dues payable by the Borrower(s) to Suryoday under the\r\n"
					+ "			Facility Documents and/or other fnancing agreements entered into between the Borrower(s) and Suryoday, and such\r\n"
					+ "			appropriation by Suryoday shall be fnal and binding on the Borrower(s) in all respects. Any amount due and payable\r\n"
					+ "			under the Facility Documents and paid by the Borrower(s) shall be appropriated by Suryoday at its discretion. To\r\n"
					+ "			illustrate, Suryoday may appropriate amounts, either (i) frstly towards the costs, charges, expenses, incidental\r\n"
					+ "			charges, legal costs and other amount that may have been expended by Suryoday in connection with the Facility or\r\n"
					+ "			recovery of Outstanding Amount;(ii) hen towards interest, additional interest, charges and fees; and (iii) then towards\r\n"
					+ "			principal amount of the Facility, as the case may be; or (ii) as per the policy/ discretion of Suryoday from time to time.\r\n"
					+ "			Suryoday is entitled to settle any indebtedness whatsoever owed by the Borrower(s) to Suryoday by adjusting, settingoff any deposit(s) and/ or transferring monies lying to the balance of any account(s) held by the Borrower(s) with\r\n"
					+ "			Suryoday to combine or consolidate at any time all or any of the accounts and liabilities of the Borrower(s) including\r\n"
					+ "			accounts not related to the Facility, to sell any of the Borrower�s assets or properties held by the Suryoday and/or\r\n"
					+ "			group companies.</p>\r\n"
					+ "		<p>12.10 &nbsp;&nbsp;Indemnity</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">In consideration of the grant of Facility, the Borrower(s) unconditionally and irrevocably agrees to indemnify and\r\n"
					+ "			reimburse and hold Suryoday, its directors, employees, shareholders, agents, consultants, representatives free and\r\n"
					+ "			harmless against all liabilities, obligations, losses, damages, penalties, suits, costs, expenses, disbursements, claims,\r\n"
					+ "			actions, proceedings in the event of any act of omission and commission by the Borrower(s) in connection with the\r\n"
					+ "			Facility Documents.</p>\r\n"
					+ "		<p>12.11 &nbsp;&nbsp;Governing Law and Jurisdiction</p>\r\n"
					+ "		<p style=\"padding-left: 15px;\">This Agreement and the Facility Documents shall be governed by the laws of India. The Parties agrees that only the\r\n"
					+ "			courts and tribunals of competent jurisdiction at Mumbai shall have exclusive jurisdiction with respect to any suit,\r\n"
					+ "			action or any other proceedings arising out of or in relation to the Facility Documents. The Parties agrees that\r\n"
					+ "			any legal action or proceedings arising out of this Agreement, subject to the provisions of Clause 12.12 (Arbitration).</p>\r\n"
					+ "<div class=\"header2\">\r\n"
					+ "			<div>\r\n"
					+ "				<b>Signature of Borrower</b>\r\n"
					+ "				<hr class=\"line-under-text\" >\r\n"
					+ "			</div>\r\n"
					+ "<div style=\"margin:0 100px\">"
					+ "				<b>Signature of Co-Applicant</b>\r\n"
					+ "				<hr class=\"line-under-text\">\r\n"
					+ "			</div>\r\n"
					+ "			<div>\r\n"
					+ "				<b>Signature of Authorized Signatory</b>\r\n"
					+ "				<hr class=\"line-under-text\">\r\n"
					+ "			</div>\r\n"
					+ "		</div>"
					+ "		<table style=\" width:100%; \">\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"8\" style=\"text-align: center; height: 20px;\"><b>SCHEDULE</b></td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"4\">Loan Agreement No: &nbsp;</td>\r\n"
					+ "				<td colspan=\"4\">Place of Agreement:&nbsp; "+city+","+state+"</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"8\" style=\"text-align: center; background-color: darkgrey; height: 20px;\"><b>NAME AND ADDRESS</b></td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n" + "				<td colspan=\"2\">Borrower</td>\r\n"
					+ "				<td colspan=\"6\">" + twowheelerDetailesTable.getName() + "</td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Co-Borrower</td>\r\n" + "				<td colspan=\"6\">"
					+                "</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Branch Address of<br> Suryoday Bank</td>\r\n"
					+ "				<td colspan=\"6\">Shop Number1, Hermes Atrium,</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"8\" style=\"text-align: center; background-color: darkgrey; height: 20px;\"><b>FACILITY DETAILS</b></td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Dealer/ Seller Name</td>\r\n" + "				<td colspan=\"6\">"
					+ twowheelerDetailesTable.getDealerName() + "</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n" + "				<td colspan=\"2\">Vehicle Cost</td>\r\n"
					+ "				<td colspan=\"2\">" + twowheelerDetailesTable.getTotalOnRoadPrice() + "</td>\r\n"
					+ "				<td colspan=\"2\">Facility Amount</td>\r\n" + "				<td colspan=\"2\">"
					+ twowheelerDetailesTable.getAmount() + "</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Facility Tenure</td>\r\n" + "				<td colspan=\"2\">"
					+ twowheelerDetailesTable.getTenure() + "</td>\r\n"
					+ "				<td colspan=\"2\">EMI Scheme</td>\r\n" + "				<td colspan=\"2\">"
					+ twowheelerDetailesTable.getScheme() + "</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Advance EMI (Number)</td>\r\n"
					+ "				<td colspan=\"2\"></td>\r\n"
					+ "				<td colspan=\"2\">Advance EMI Amount</td>\r\n"
					+ "				<td colspan=\"2\"></td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">EMI Amount</td>\r\n" + "				<td colspan=\"2\">"
					+ twowheelerDetailesTable.getEmi() + "</td>\r\n"
					+ "				<td colspan=\"2\">Rate of Interest (%)</td>\r\n"
					+ "				<td colspan=\"2\">" + twowheelerDetailesTable.getRateOfInterest() + "</td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">EMI Start Date</td>\r\n"
					+ "				<td colspan=\"2\">DD/MM/YYYY</td>\r\n"
					+ "				<td colspan=\"2\">Effective IRR (%)</td>\r\n"
					+ "				<td colspan=\"2\"></td>\r\n" + "			</tr>\r\n"
					+ "          <tr>\r\n"
					+ "				<td colspan=\"2\">EMI End Date</td>\r\n"
					+ "				<td colspan=\"2\">DD/MM/YYYY</td>\r\n"
					+ "				<td colspan=\"2\">Interest Rate Type</td>\r\n"
					+ "				<td colspan=\"2\">Fixed</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"4\">Repayment amount (excluding any penalties and charges): Principal + Interest </td>\r\n"
					+ "				<td colspan=\"4\">Principal: Rs. ______________+ Interest: Rs.___________ = Rs. ___________________</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"4\">Loan Repayment frequency </td>\r\n"
					+ "				<td colspan=\"4\">Repayable in __________ Equated Monthly Instalments (EMI) on 5th / 10th / 15th of every month. EMI is inclusive of Principal and monthly interest due.</td>\r\n"
					+ "			</tr>"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"8\" style=\"text-align: center; background-color: darkgrey; height: 20px;\"><b>CHARGES</b></td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Processing Charges Amount</td>\r\n"
					+ "				<td colspan=\"2\">"+processingFee+"</td>\r\n"
					+ "				<td colspan=\"2\">Other Charges</td>\r\n"
					+ "				<td colspan=\"2\">"+docCharges+"</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Type of Insurance**</td>\r\n"
					+ "				<td colspan=\"6\"></td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Insurance Premium**</td>\r\n" + "				<td colspan=\"6\">"
					+ twowheelerDetailesTable.getInsuranceEmi() + "</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n" + "				<td colspan=\"2\">Name of Insurance Company **</td>\r\n"
					+ "				<td colspan=\"6\">HDFC Life Insurance</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td colspan=\"2\">Post Disbursal<br> Document Charges</td>\r\n"
					+ "				<td colspan=\"2\">NA</td>\r\n"
					+ "				<td colspan=\"2\">Cheque/ ECS/ NACH/ Direct<br> Debit Bounce Charges</td>\r\n"
					+ "				<td colspan=\"2\">500</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Late payment Charges</td>\r\n"
					+ "				<td colspan=\"2\">Rs. 500 per EMI</td>\r\n"
					+ "				<td colspan=\"2\">Loan Prepayment Charges</td>\r\n"
					+ "				<td colspan=\"2\">NA</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Loan Cancellation<br> & Rebooking Charges</td>\r\n"
					+ "				<td colspan=\"2\">1% of Principal Outstanding</td>\r\n" + "				<td colspan=\"2\">Pre EMI</td>\r\n"
					+ "				<td colspan=\"2\">NA</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Duplicate NOC<br> Issuance Charges</td>\r\n"
					+ "				<td colspan=\"2\">500</td>\r\n"
					+ "				<td colspan=\"2\">Stamp Duty Charges</td>\r\n"
					+ "				<td colspan=\"2\"></td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Legal/ Collections/ Repossession<br> & Incidental Charges</td>\r\n"
					+ "				<td colspan=\"2\"></td>\r\n" + "				<td colspan=\"2\">GST</td>\r\n"
					+ "				<td colspan=\"2\">18%</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"8\" style=\"text-align: center; background-color: darkgrey; height: 20px;\"><b>VEHICLE DETAILS</b></td>\r\n"
					+ "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Vehicle Make and Model <br> & Incidental Charges</td>\r\n"
					+ "				<td colspan=\"2\">" + twowheelerDetailesTable.getManufacture() + " "
					+ twowheelerDetailesTable.getModel() + "</td>\r\n"
					+ "				<td colspan=\"2\">Year of Manufacture</td>\r\n"
					+ "				<td colspan=\"2\">2023</td>\r\n" + "			</tr>\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Engine No</td>\r\n" + "				<td colspan=\"2\">"
					+ twowheelerDetailesTable.getEngineNumber() + "</td>\r\n"
					+ "				<td colspan=\"1\">Chassis No</td>\r\n" + "				<td colspan=\"1\">"
					+ twowheelerDetailesTable.getChasisNumber() + "</td>\r\n"
					+ "				<td colspan=\"1\">Vehicle Registration No.</td>\r\n"
					+ "				<td colspan=\"1\"></td>\r\n" + "			</tr>\r\n" + "		</table>\r\n"
					+ "		<p style=\"margin: 0; padding-top: 0;\">**As per the request expressed in the Application Form, for taking insurance from the insurance company as mentioned above,</p>\r\n"
					+ "		<p style=\"margin: 0; padding-top: 0;\">the Borrower(s) agrees that insurance is a third-party product and the Borrower(s) shall be subject to insurance terms of the</p>\r\n"
					+ "		<p style=\"margin: 0; padding-top: 0;\">insurance company. Insurance is optional and the insurance premium as agreed in the insurance enrollment form shall be paid by</p>\r\n"
					+ "		<p style=\"margin: 0; padding-top: 0;\">the Borrower(s).</p>\r\n"
					+ "     <br>\r\n"
					+ "     <br>\r\n"
					+ "     <br>\r\n"
					+ "     <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "     <br>\r\n"
					+ "     <br>\r\n"
					+ "		<table style=\" width:100%;\">\r\n"
					+ "			<th style=\"height: 25px;\">Example of SMA, NPA Classification and NPA Upgradation of Term Loan Cases</th>\r\n"
					+ "		</table>\r\n"
					+ "		<p>&bull;&nbsp;&nbsp;&nbsp;<b>A. IRAC Circular Refer Para No. 2.1.2 (i)</b> - interest and/ or instalment of principal remains overdue for a period of more than 90 days in respect of a term loan.</p>\r\n"
					+ "		<p style=\"margin: 0; padding-top: 0;\"><b>Example Description –</b> SMA and NPA Classification of Term Loan Cases based on overdue date. Example Detail: If due date of a loan account is March 31, 2021, and complete dues are not received before the lending institution runs the day-end process on this date, the date of overdue shall be March 31, 2021. If it continues to remain overdue, then this account shall get tagged as SMA-1 upon running day-end process on April 30, 2021 i.e. upon completion of 30 days of being continuously overdue. Accordingly, the date of SMA-1 classification for that account shall be April 30, 2021. Similarly, if the account continues to remain overdue, it shall get tagged as SMA-2 upon running day-end process on May 30, 2021 and if continues to remain overdue further, it shall get classified as NPA upon running day-end process on June 29, 2021. This is further elaborated in below mentioned table:</p>\r\n"
					+ "		<table style=\"width: 100%;\">\r\n"
					+ "			<tr>\r\n"
					+ "				<td style=\"width: 200px; text-align: center;height: 25px;\">Date</td>\r\n"
					+ "				<td style=\"width: 200px;text-align: center;\">DPD</td>\r\n"
					+ "				<td style=\"text-align: center;\">Classification</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td style=\"width: 200px; text-align: center;\">31-Mar-21 “(Due Date)\"</td>\r\n"
					+ "				<td style=\"width: 200px;text-align: center;\">1</td>\r\n"
					+ "				<td style=\"text-align: center;\">SMA-0</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td style=\"width: 200px; text-align: center;\">30-Apr-21</td>\r\n"
					+ "				<td style=\"width: 200px;text-align: center;\">31</td>\r\n"
					+ "				<td style=\"text-align: center;\">SMA-1</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td style=\"width: 200px; text-align: center;\">30-May-21</td>\r\n"
					+ "				<td style=\"width: 200px;text-align: center;\">61</td>\r\n"
					+ "				<td style=\"text-align: center;\">SMA-2</td>\r\n"
					+ "			</tr>\r\n"
					+ "			<tr>\r\n"
					+ "				<td style=\"width: 200px; text-align: center;\">29-Jun-21</td>\r\n"
					+ "				<td style=\"width: 200px;text-align: center;\">91</td>\r\n"
					+ "				<td style=\"text-align: center;\">NPA</td>\r\n"
					+ "			</tr>\r\n"
					+ "		</table>\r\n"
					+ "		<p><b>• B. Circular Refer Para No. 4.2.5 -</b> If arrears of interest and principal are paid by the borrower in the case of loan accounts classified as NPAs, the account should no longer be treated as non performing and may be classified as ‘standard’ accounts.\r\n"
					+ "			Example Description - Upgradation of NPA Account: loan accounts classified as NPAs may be upgraded as ‘standard’ asset only if entire arrears of interest and principal are paid by the borrower.</p>\r\n"
					+ "			<table style=\"width: 100%;\">\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 200px; text-align: center;height: 25px;\">Date</td>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;height: 25px;\">Due Amount</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">DPD</td>\r\n"
					+ "					<td style=\"text-align: center;\">Classification</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">31-Mar-21 “(Due Date)\"</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">10000</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">1</td>\r\n"
					+ "					<td style=\"text-align: center;\">SMA-0</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">30-Apr-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">10000</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">31</td>\r\n"
					+ "					<td style=\"text-align: center;\">SMA-1</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">30-May-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">61</td>\r\n"
					+ "					<td style=\"text-align: center;\">SMA-2</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">31-May-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">10000</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"text-align: center;\"></td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">91</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">30-Jun-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">10000</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"text-align: center;\"></td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">01-Jul-21</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\"></td>\r\n"
					+ "					<td style=\"text-align: center;\">Upgradation*</td>\r\n"
					+ "				</tr>\r\n"
					+ "			</table>\r\n"
					+ "			<p>*Upgradation of Account to standard category can be done after total pending due of Rs. 40000 is received from borrower by the Bank</p>\r\n"
					+ "			<p><b>2. A. IRAC Circular Reference Para No. 4.2.7 -</b> Asset Classification to be borrower-wise and not facility-wise.  Para No. 4.2.7.1 -It is difficult to envisage a situation when only one facility to a borrower/one investment in any of the securities issued by the borrower becomes a problem credit/investment and not others. Therefore, all the facilities granted by a bank to a borrower and investment in all the securities issued by the borrower will have to be treated as NPA/NPI and not the particular facility/investment or part thereof which has become irregular.</p>\r\n"
					+ "			<p><b>Example Description</b> – NPA Classification on based on borrower wise and not facility wise.</p>\r\n"
					+ "			<p><b>Example:</b> If any Facility of customer is classified as NPA upon running day-end process as on date, all the facility of the customer need to be classified NPA upon same day. It is further explained in below mentioned table:</p>\r\n"
					+ "			<p><b>Example:</b> If any Facility of customer is classified as NPA upon running day-end process as on date, all the facility of the customer need to be classified NPA upon same day. It is further explained in below mentioned table:</p>\r\n"
					+ "			<table style=\"width: 100%;\">\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;height: 25px;font-weight: bold;\">Customer ID</td>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;height: 25px;font-weight: bold;\">Facility Name</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;font-weight: bold;\">Date</td>\r\n"
					+ "					<td style=\"text-align: center;font-weight: bold;\">NPA Reason</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 1</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified as per above Example</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 2</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified Due to Customer A’s Term Loan 1 is classified NPA</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Cash Credit/Overdraft</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified Due to Customer A’s Term Loan 1 is classified NPA</td>\r\n"
					+ "				</tr>\r\n"
					+ "			</table>\r\n"
					+ "     <br>\r\n"
					+ "     <div class=\"header2\">\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Borrower</b>\r\n"
					+ "					<hr class=\"line-under-text\" >\r\n"
					+ "				</div>\r\n"
					+ "				<div style=\"margin:0 100px\">\r\n"
					+ "					<b>Signature of Co-Applicant</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "				<div>\r\n"
					+ "					<b>Signature of Authorized Signatory</b>\r\n"
					+ "					<hr class=\"line-under-text\">\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "		<p><b>• B. IRAC Circular Refer Para No. 4.2.5 -</b> If entire arrears of interest and principal are paid by the Borrower in the case of loan accounts classified as NPAs, the account should no longer be treated as non performing and may be classified as ‘standard’ accounts.\r\n"
					+ "			Example Description – Upgradation of NPA Account- loan accounts classified as NPAs may be upgraded if entire arrears of interest and principal are repaid in all the facilities of the Borrower.</p>\r\n"
					+ "			<table style=\"width: 100%;\">\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;height: 25px;font-weight: bold;\">Customer ID</td>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;height: 25px;font-weight: bold;\">Facility Name</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;font-weight: bold;\">Date</td>\r\n"
					+ "					<td style=\"text-align: center;font-weight: bold;\">NPA Reason</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 1</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified as per above Example</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 1</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified Due to Customer A’s Term Loan 1 is classified NPA</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Cash Credit/Overdraft</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">29-Jun-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">NPA Classified Due to Customer A’s Term Loan 1 is classified NPA</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 1</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">15-Jul-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">Upgrade*</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Term Loan 1</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">15-Jul-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">Upgrade*</td>\r\n"
					+ "				</tr>\r\n"
					+ "				<tr>\r\n"
					+ "					<td style=\"width: 100px; text-align: center;\">A</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">Cash Credit/Overdraft</td>\r\n"
					+ "					<td style=\"width: 100px;text-align: center;\">15-Jul-21</td>\r\n"
					+ "					<td style=\"text-align: center;\">Upgrade*</td>\r\n"
					+ "				</tr>\r\n"
					+ "			</table>\r\n"
					+ "		<p>*Upgradation of Borrower's accounts to standard can be done if arrears of interest and principal are repaid in all the facilities of the borrower</p>"
					+ "		<p>We Agree.</p>\r\n" + "		<table style=\"width: 100%;\">\r\n" + "			<tr>\r\n"
					+ "				<td colspan=\"2\">Borrower</td>\r\n"
					+ "				<td colspan=\"2\">Co-Applicant</td>\r\n" + "			</tr>\r\n"
					+ "			<tr>\r\n" + "				<td colspan=\"2\"></td>\r\n"
					+ "				<td colspan=\"2\" style=\"height: 20px;\"></td>\r\n" + "			</tr>\r\n"
					+ "			<th colspan=\"4\" style=\"background-color: darkgrey; height: 20px;\"><b>FOR OFFICIAL USE ONLY CENTRAL OPS</b></th>\r\n"
					+ "			<tr style=\"height: 50px;\">\r\n"
					+ "				<td colspan=\"4\">This Agreement will come into effect from -"+todayDate+" <br><br><br>Signature of Authorized Signatory</td>\r\n"
					+ "			</tr>			\r\n" + "		</table>\r\n"
					+ "		<p style=\"text-align: center; color: blue; margin-bottom: 0; font-size: 13px;\"> SURYODAY SMALL FINANCE BANK LIMITED</p>\r\n"
					+ "		<p style=\"color: blue; text-align: center; margin: 0; padding-top: 0;font-size: 12px;\"> Regd & Corp Offce : 1101, Sharda Terraces, Plot 65, Sector 11, CBD Belapur, Navi Mumbai-400614</p>\r\n"
					+ "		<p style=\"color: blue; text-align: center;margin: 0; padding-top: 0;font-size: 12px;\">Tel: 022-40435800 Email: info@suryodaybank.com | Web: www.suryodaybank.com | CIN: U65923MH2008PLC261472|<br>GSTIN : 27AAMCS5499J1ZG</p>\r\n"
					+ "	</body>\r\n" + "</html>"));
		} else {

		}
		return htmlString.toString();
	}

}
