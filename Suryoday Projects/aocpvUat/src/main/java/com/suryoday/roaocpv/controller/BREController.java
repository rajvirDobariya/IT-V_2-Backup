package com.suryoday.roaocpv.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ErrorResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.BREService;

@Controller
@RequestMapping("/roaocpv")
public class BREController extends OncePerRequestFilter {

	@Autowired
	BREService bresService;

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	@Autowired
	UserService userService;

	private static final String HEADER_VALUE = "max-age=31536000";

	private static Logger logger = LoggerFactory.getLogger(BREController.class);

	@RequestMapping(value = "/createlead", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> createLead(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject createLead = new JSONObject();
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		if (fetchByApplicationId.getLeadId() != null) {

			JSONObject data = new JSONObject();
			JSONObject res = new JSONObject();
			data.put("is_success", true);
			data.put("data", res);
			res.put("loan_account_number", "D100026");
			res.put("lead_id", fetchByApplicationId.getLeadId());
			createLead.put("data", data.toString());
		} else {
			createLead = bresService.createLead(applicationNo, Header);
		}
//		JSONObject createLead = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("loan_account_number", "D100026");
//		res.put("lead_id", "b7ea25d0-f4e1-4ce6-b848-d68f626b9117");
//		createLead.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (createLead != null) {
			String Data2 = createLead.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;
				String leadID = Data1.getJSONObject("data").getString("lead_id");
				if (fetchByApplicationId.getLeadId() == null) {
					fetchByApplicationId.setLeadId(leadID);
					fetchByApplicationId.setIsLeadCreated("Yes");
					fetchByApplicationId.setIsBreRuring("No");
					fetchByApplicationId.setBreStatus("Initiated");
					applicationDetailsService.save(fetchByApplicationId);
				}

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/fetchBre", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBre(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		JSONObject fetchBre = bresService.fetchBre(applicationNo, Header, fetchByApplicationId.getLeadId());
//		JSONObject fetchBre = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("status", "Approved");

//		fetchBre.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/webhookcallback", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> webhookcallback(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("webhookcallback start");
		logger.debug("request" + bm);
		String method = request.getMethod();
		System.out.println("method" + method);
		if (request.getMethod().equals("OPTIONS")) {
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.FORBIDDEN);
		}
		if (X_From_ID.equals("CB") && X_Request_ID.equals("MB") && X_To_ID.equals("MB")
				&& X_Transaction_ID.equals("EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4")
				&& authorization.equals("Basic d2ViaG9va2NhbGxiYWNrOm9uZWZpbg=")) {

			User fetchById = userService.fetchById(X_User_ID);
			if (fetchById == null) {

				throw new NoSuchElementException("You are Not authorized");
			}

			JSONObject jsonObject = new JSONObject(bm);
			String Lead_ID = jsonObject.getString("lead_id");
			if (Lead_ID.equals("")) {
				throw new NoSuchElementException("Lead_Id cannot be Empty");
			}

			int respons = applicationDetailsService.fetchByLeadId(Lead_ID);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			if (respons == 1) {
				logger.debug("message : Success");
				response.put("message", "Success");
			} else {
				response.put("message", "failed");
				logger.debug("message : failed");
			}
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Strict-Transport-Security", "false");
			responseHeaders.set("X-Frame-Options", "");
			responseHeaders.set("X-Content-Type-Options", "nosniff");
			responseHeaders.set("Content-Security-Policy", "src");
			responseHeaders.set("X-XSS-Protection", "0");

			return ResponseEntity.ok().headers(responseHeaders).body(response);
			// return new ResponseEntity<Object>(response,HttpStatus.OK);
		} else {
			logger.debug("Invalid Request");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}

	@RequestMapping(value = "/equifaxReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxReport(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject1 = new JSONObject(bm);
		String applicationNo = jsonObject1.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		String ekycResponse = fetchByApplicationId.getEkycResponse();

		String FIRST_NAME = "";
		String MIDDLE_NAME = "";
		String LAST_NAME = "";
		String MOBILE = "";
		String DOB = "";
		String AGE = "";
		String GENDER = "";

//		JSONObject fetchBre = bresService.equifaxReport(applicationNo, Header,fetchByApplicationId.getLeadId());
//		JSONObject fetchBre = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("status", "Approved");

//		fetchBre.put("data", data.toString());
		JSONObject fetchBre = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject rawEquifaxData = new JSONObject();
		JSONArray score = new JSONArray();
		JSONObject score1 = new JSONObject();
		score1.put("Type", "ERS");
		score1.put("Version", "3.1");
		score.put(score1);
		rawEquifaxData.put("Score", score);
		JSONObject ccrResp = new JSONObject();
		ccrResp.put("Status", "1");
		JSONArray cirRepDataLst = new JSONArray();
		JSONObject cirRepDataLst1 = new JSONObject();
		JSONArray cirRepDataLstscore = new JSONArray();
		JSONObject cirRepDataLstscore1 = new JSONObject();
		cirRepDataLstscore1.put("Type", "ERS");
		cirRepDataLstscore1.put("Version", "3.1");
		cirRepDataLstscore.put(cirRepDataLstscore1);
		cirRepDataLst1.put("Score", cirRepDataLstscore);
		JSONObject cirRepData = new JSONObject();
		JSONArray scoreDetails = new JSONArray();
		JSONObject scoreDetails1 = new JSONObject();
		scoreDetails1.put("Type", "ERS");
		scoreDetails1.put("Version", "3.1");
		JSONArray scoringElements = new JSONArray();
		JSONObject scoringElements1 = new JSONObject();
		scoringElements1.put("Description", "Number of product trades");
		scoringElements1.put("type", "RES");
		scoringElements1.put("seq", "1");
		scoringElements.put(scoringElements1);
		scoreDetails1.put("ScoringElements", scoringElements);
		scoreDetails1.put("Value", "737");
		scoreDetails1.put("Name", "M001");
		scoreDetails.put(scoreDetails1);
		cirRepData.put("ScoreDetails", scoreDetails);
		JSONObject idConInfo = new JSONObject();
		JSONObject personalInfo = new JSONObject();
		JSONObject aliasName = new JSONObject();
		personalInfo.put("AliasName", aliasName);
		personalInfo.put("DateOfBirth", DOB);
		JSONObject placeOfBirthInfo = new JSONObject();
		personalInfo.put("PlaceOfBirthInfo", placeOfBirthInfo);
		personalInfo.put("Gender", GENDER);
		JSONObject age = new JSONObject();
		age.put("Age", AGE);
		personalInfo.put("Age", age);
		JSONObject name = new JSONObject();
		name.put("FirstName", FIRST_NAME);
		name.put("FullName", FIRST_NAME + " " + MIDDLE_NAME + " " + LAST_NAME);
		name.put("LastName", LAST_NAME);
		name.put("MiddleName", MIDDLE_NAME);
		personalInfo.put("Name", name);
		idConInfo.put("PersonalInfo", personalInfo);
		JSONObject identityInfo = new JSONObject();
		JSONArray PANId = new JSONArray();
		JSONObject PANId1 = new JSONObject();
		JSONArray aadhar = new JSONArray();
		JSONObject aadhar1 = new JSONObject();
		aadhar1.put("ReportedDate", "2015-03-31");
		aadhar1.put("IdNumber", fetchByApplicationId.getAadharNumber());
		aadhar1.put("seq", "2");
		PANId1.put("ReportedDate", "2015-03-31");
		PANId1.put("IdNumber", fetchByApplicationId.getPanCard());
		PANId1.put("seq", "1");
		PANId.put(PANId1);
		aadhar.put(aadhar1);
		identityInfo.put("PANId", PANId);
		identityInfo.put("AAdharCard", aadhar);
		idConInfo.put("IdentityInfo", identityInfo);
		JSONArray phoneInfo = new JSONArray();
		JSONObject phoneInfo1 = new JSONObject();
		phoneInfo1.put("ReportedDate", "2016-03-04");
		phoneInfo1.put("Number", fetchByApplicationId.getMobileNo());
		phoneInfo1.put("seq", "1");
		phoneInfo1.put("typeCode", "M");
		phoneInfo.put(phoneInfo1);
		idConInfo.put("PhoneInfo", phoneInfo);
		JSONArray addInfo = new JSONArray();
		idConInfo.put("AddressInfo", addInfo);
		cirRepData.put("IDAndContactInfo", idConInfo);
		JSONArray enquiries = new JSONArray();
		JSONObject enquiries1 = new JSONObject();
		enquiries1.put("Amount", "10000");
		enquiries1.put("Time", "14:23");
		enquiries1.put("Institution", "Janlakshmi Financial Services Pvt Ltd");
		enquiries1.put("seq", "0");
		enquiries1.put("Date", "2022-07-25");
		enquiries1.put("RequestPurpose", "51");
		enquiries.put(enquiries1);
		cirRepData.put("Enquiries", enquiries);
		JSONObject otherKeyEnd = new JSONObject();
		otherKeyEnd.put("AgeOfOldestTrade", "112");
		otherKeyEnd.put("AllLinesEVERWrittenIn6Months", "0");
		otherKeyEnd.put("NumberOfOpenTrades", "0");
		otherKeyEnd.put("AllLinesEVERWritten", "0.00");
		otherKeyEnd.put("AllLinesEVERWrittenIn9Months", "0");
		cirRepData.put("OtherKeyInd", otherKeyEnd);
		JSONObject enquirySummary = new JSONObject();
		enquirySummary.put("Recent", "2022-07-25");
		enquirySummary.put("Past30Days", "0");
		enquirySummary.put("Total", "1351");
		enquirySummary.put("Past12Months", "17");
		enquirySummary.put("Past24Months", "23");
		enquirySummary.put("Purpose", "ALL");
		cirRepData.put("EnquirySummary", enquirySummary);
		JSONObject recentActivities = new JSONObject();
		recentActivities.put("TotalInquiries", "5");
		recentActivities.put("AccountsOpened", "0");
		recentActivities.put("AccountsDeliquent", "0");
		recentActivities.put("AccountsUpdated", "0");
		cirRepData.put("RecentActivities", recentActivities);
		JSONArray retailAccDetails = new JSONArray();
		JSONObject retailAccDetails1 = new JSONObject();
		retailAccDetails1.put("OwnershipType", "Individual");
		retailAccDetails1.put("DateOpened", "2017-02-06");
		retailAccDetails1.put("DateReported", "2018-01-31");
		retailAccDetails1.put("TermFrequency", "Monthly");
		retailAccDetails1.put("RepaymentTenure", "60");
		retailAccDetails1.put("InstallmentAmount", "6742");
		retailAccDetails1.put("source", "INDIVIDUAL");
		retailAccDetails1.put("Institution", "State Bank Of India");
		retailAccDetails1.put("AccountType", "Personal Loan");
		retailAccDetails1.put("Open", "Yes");
		retailAccDetails1.put("AccountNumber", "00000036518184846");
		retailAccDetails1.put("AccountStatus", "Standard");
		retailAccDetails1.put("SanctionAmount", "30000");
		retailAccDetails1.put("AssetClassification", "Standard");
		retailAccDetails1.put("InterestRate", "12.000");
		retailAccDetails1.put("Balance", "256239");
		retailAccDetails1.put("seq", "1");
		retailAccDetails.put(retailAccDetails1);
		cirRepData.put("RetailAccountDetails", retailAccDetails);
		JSONObject retailAccountSummary = new JSONObject();
		retailAccountSummary.put("TotalSanctionAmount", "300000.00");
		retailAccountSummary.put("NoOfPastDueAccounts", "0");
		retailAccountSummary.put("TotalCreditLimit", "0.0");
		retailAccountSummary.put("RecentAccount", "Personal Loan on 06-02-2017");
		retailAccountSummary.put("TotalHighCredit", "0.00");
		retailAccountSummary.put("NoOfActiveAccounts", "1");
		retailAccountSummary.put("OldestAccount", "Personal Loan on 07-05-2013");
		retailAccountSummary.put("SingleHighestSanctionAmount", "30000.00");
		retailAccountSummary.put("NoOfAccounts", "2");
		retailAccountSummary.put("NoOfWriteOffs", "0");
		retailAccountSummary.put("SingleHighestCredit", "0.00");
		retailAccountSummary.put("AverageOpenBalance", "256239.00");
		retailAccountSummary.put("TotalPastDue", "0.00");
		retailAccountSummary.put("NoOfZeroBalanceAccounts", "0");
		retailAccountSummary.put("TotalMonthlyPaymentAmount", "6742.00");
		retailAccountSummary.put("TotalBalanceAmount", "256239.00");
		retailAccountSummary.put("SingleHighestBalance", "256239.00");
		cirRepData.put("RetailAccountsSummary", retailAccountSummary);
		cirRepDataLst1.put("CIRReportData", cirRepData);
		JSONObject inqRespHeader = new JSONObject();
		inqRespHeader.put("HitCode", "10");
		inqRespHeader.put("ReportOrderNO", "9031630");
		inqRespHeader.put("CustomerCode", "IC01");
		inqRespHeader.put("SuccessCode", "1");
		inqRespHeader.put("CustRefField", "123456");
		inqRespHeader.put("Time", "20:01:14");
		inqRespHeader.put("CustomerName", "IC01");
		inqRespHeader.put("Date", "2022-09-09");
		cirRepDataLst1.put("InquiryResponseHeader", inqRespHeader);
		JSONObject inqReqInfo = new JSONObject();
		inqReqInfo.put("InquiryPurpose", "Other");
		JSONArray idDetails = new JSONArray();
		JSONObject idDetails1 = new JSONObject();
		idDetails1.put("IDValue", "ARSPB2789E");
		idDetails1.put("IDType", "T");
		idDetails1.put("seq", "1");
		idDetails1.put("Source", "Inquiry");
		idDetails.put(idDetails1);
		inqReqInfo.put("IDDetails", idDetails);
		inqReqInfo.put("DOB", "1960-05-30");
		inqReqInfo.put("FirstName", "RAM SINGH BHANDARI");
		JSONObject mfiDetails = new JSONObject();
		JSONArray familyDetails = new JSONArray();
		JSONObject familyDetails1 = new JSONObject();
		familyDetails1.put("AdditionalName", "LAXMAN BHANDARI");
		familyDetails1.put("AdditionalNameType", "K01");
		familyDetails1.put("seq", "1");
		familyDetails.put(familyDetails1);
		JSONObject familyDetails2 = new JSONObject();
		familyDetails2.put("AdditionalNameType", "K01");
		familyDetails2.put("seq", "2");
		familyDetails.put(familyDetails2);
		mfiDetails.put("FamilyDetails", familyDetails);
		inqReqInfo.put("MFIDetails", mfiDetails);
		JSONArray inqPhones = new JSONArray();
		JSONObject inqPhones1 = new JSONObject();
		inqPhones.put(inqPhones1);
		inqPhones1.put("Number", "8191817766");
		JSONArray phoneType = new JSONArray();
		phoneType.put("M");
		inqPhones1.put("PhoneType", phoneType);
		inqPhones1.put("seq", "1");
		inqReqInfo.put("InquiryPhones", inqPhones);
		JSONArray inqAddresses = new JSONArray();
		JSONObject inqAddresses1 = new JSONObject();
		inqAddresses1.put("AddressLine1", "169 RAJEEV NAGAR  TARLI KANDOLI DEHRADUN UTTARAKHAND DEHRADUN");
		inqAddresses1.put("State", "UK");
		JSONArray addressType = new JSONArray();
		addressType.put("H");
		inqAddresses1.put("AddressType", addressType);
		inqAddresses1.put("seq", "1");
		inqAddresses1.put("Postal", "249145");
		inqAddresses.put(inqAddresses1);
		inqReqInfo.put("InquiryAddresses", inqAddresses);
		cirRepDataLst1.put("InquiryRequestInfo", inqReqInfo);
		cirRepDataLst.put(cirRepDataLst1);
		ccrResp.put("CIRReportDataLst", cirRepDataLst);
		rawEquifaxData.put("CCRResponse", ccrResp);
		JSONObject inquiryRespHeader = new JSONObject();
		inquiryRespHeader.put("ReportOrderNO", "9031630");
		JSONArray productCode = new JSONArray();
		productCode.put("CCR");
		inquiryRespHeader.put("ProductCode", productCode);
		inquiryRespHeader.put("ClientID", "028FZ00016");
		inquiryRespHeader.put("SuccessCode", "1");
		inquiryRespHeader.put("CustRefField", "123456");
		inquiryRespHeader.put("Time", "20:01:15");
		inquiryRespHeader.put("Date", "2022-09-09");
		rawEquifaxData.put("InquiryResponseHeader", inquiryRespHeader);
		JSONObject inquiryReqInfo = new JSONObject();
		inqReqInfo.put("InquiryPurpose", "Other");
		JSONArray IdDetails = new JSONArray();
		JSONObject IdDetails1 = new JSONObject();
		IdDetails1.put("IDValue", "ARSPB2789E");
		IdDetails1.put("IDType", "T");
		IdDetails1.put("seq", "1");
		idDetails1.put("Source", "Inquiry");
		IdDetails.put(IdDetails1);
		inquiryReqInfo.put("IDDetails", idDetails);
		inquiryReqInfo.put("DOB", "1960-05-30");
		inquiryReqInfo.put("FirstName", "RAM SINGH BHANDARI");
		JSONObject MfiDetails = new JSONObject();
		JSONArray FamilyDetails = new JSONArray();
		JSONObject FamilyDetails1 = new JSONObject();
		FamilyDetails1.put("AdditionalName", "LAXMAN BHANDARI");
		FamilyDetails1.put("AdditionalNameType", "K01");
		FamilyDetails1.put("seq", "1");
		FamilyDetails.put(FamilyDetails1);
		JSONObject FamilyDetails2 = new JSONObject();
		FamilyDetails2.put("AdditionalNameType", "K01");
		FamilyDetails2.put("seq", "2");
		FamilyDetails.put(FamilyDetails2);
		mfiDetails.put("FamilyDetails", FamilyDetails);
		inquiryReqInfo.put("MFIDetails", MfiDetails);
		JSONArray InqPhones = new JSONArray();
		JSONObject InqPhones1 = new JSONObject();
		InqPhones1.put("Number", "8191817766");
		InqPhones.put(InqPhones1);
		JSONArray PhoneType = new JSONArray();
		PhoneType.put("M");
		InqPhones1.put("PhoneType", phoneType);
		InqPhones1.put("seq", "1");
		inquiryReqInfo.put("InquiryPhones", inqPhones);
		JSONArray InqAddresses = new JSONArray();
		JSONObject InqAddresses1 = new JSONObject();
		InqAddresses1.put("AddressLine1", "169 RAJEEV NAGAR  TARLI KANDOLI DEHRADUN UTTARAKHAND DEHRADUN");
		InqAddresses1.put("State", "UK");
		JSONArray AddressType = new JSONArray();
		AddressType.put("H");
		InqAddresses1.put("AddressType", addressType);
		InqAddresses1.put("seq", "1");
		InqAddresses1.put("Postal", "249145");
		InqAddresses.put(inqAddresses1);
		inquiryReqInfo.put("InquiryAddresses", InqAddresses);
		rawEquifaxData.put("InquiryRequestInfo", inqReqInfo);
		data.put("raw_equifax_data", rawEquifaxData);
		data.put("is_success", true);
		fetchBre.put("data", data.toString());
		JSONObject fetchBreMB = new JSONObject();
		if (channel.equalsIgnoreCase("MB")) {
			fetchBreMB.put("data", idConInfo.toString());
			fetchBre = fetchBreMB;
		}
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/equifaxreport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxreport(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject1 = new JSONObject(bm);
		String applicationNo = jsonObject1.getJSONObject("Data").getString("ApplicationNo");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		JSONObject fetchBre = bresService.equifaxReport(applicationNo, Header, fetchByApplicationId.getLeadId());

		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			System.out.println(Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/brerules", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> brerules(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String breResp = "{\r\n" + "    \"is_success\": true,\r\n" + "    \"data\": {\r\n"
				+ "        \"status\": \"Approved\",\r\n" + "        \"bre_response\": {\r\n"
				+ "            \"OLDEST_MFI_ACCOUNT\": {\r\n"
				+ "                \"description\": \"First MFI Account should be atleast 36 months old\",\r\n"
				+ "                \"result\": false,\r\n"
				+ "                \"definition\": \"First MFI account  (If >=36 months Go, <36 No Go)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"FIRST_MFI_ACCOUNT_AGE_IN_MONTHS_VL\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"MAX_0_PLUS_DPD_LAST_3_MONTHS_RETAIL_MFI\": {\r\n"
				+ "                \"description\": \"Maximum 0+ DPD Accounts In Last 3 Months in Retail and MFI( Go- True , Hard Reject )\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"Maximum 0+ DPD Accounts (If 0 Go ; If >0 No Go )\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_0_PLUS_DPD_LAST_3_MONTHS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"EXISTING_MFI_CUSTOMER\": {\r\n"
				+ "                \"description\": \"Atleast 1 MFI Account should be there.\",\r\n"
				+ "                \"result\": false,\r\n"
				+ "                \"definition\": \"Number of MFI Accounts ( If >=1 Go, <1  No Go)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"NUM_OF_MFI_ACCOUNTS_VL\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"MAX_90_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI\": {\r\n"
				+ "                \"description\": \"Maximum 90+ DPD Accounts In Last 12 Months in Retail and MFI( Go- True , Hard Reject )\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"Maximum 90+ DPD Accounts (If 0 Go ; If >0 No Go )\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_90_PLUS_DPD_LAST_12_MONTHS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"NO_SUITFILED_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of Suit Filed accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of SUIT FILED ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_SUITFILED_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"OLDEST_RETAIL_ACCOUNT\": {\r\n"
				+ "                \"description\": \"First Retail Account should be atleast 36 months old\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"First Retail account  (If >=36 months Go, <36 No Go)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"FIRST_RETAIL_ACCOUNT_AGE_IN_MONTHS_VL\": 80.13333333333334\r\n"
				+ "                }\r\n" + "            },\r\n" + "            \"NO_NPA_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of substandard, doubtful and loss accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of SUBSTANDARD, DOUBTFUL, LOSS ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_EQUIFAX_NPA_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"NO_WILFUL_DEFAULT_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of Wilful Default accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of  WILFUL DEFAULT ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_WILFUL_DEFAULT_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"MAX_30_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI\": {\r\n"
				+ "                \"description\": \"Maximum 30+ DPD Accounts In Last 12 Months in Retail and MFI( Go- True , Hard Reject )\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"Maximum 30+ DPD Accounts (If 0 Go ; If >0 No Go )\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_30_PLUS_DPD_LAST_12_MONTHS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"TOTAL_RETAIL_MFI_EMI\": {\r\n"
				+ "                \"description\": null,\r\n" + "                \"result\": true,\r\n"
				+ "                \"definition\": null,\r\n" + "                \"deviation\": false,\r\n"
				+ "                \"derived_values\": {\r\n" + "                    \"TOTAL_MFI_EMI\": 0\r\n"
				+ "                }\r\n" + "            },\r\n" + "            \"LOAN_CATEGORY\": {\r\n"
				+ "                \"description\": \"Loan Category from grid\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"LOAN CATEGORY  CALCULATED FROM GRID\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"LOAN_CATEGORY_FROM_GRID\": \"5B\"\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"LOAN_ELIGIBILITY_AMOUNT\": {\r\n"
				+ "                \"description\": \"Loan eligibility amount from grid\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"LOAN ELIGIBILITY AMOUNT CALCULATED FROM GRID\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"LOAN_ELIGIBILITY_AMOUNT_FROM_GRID\": 5000\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"NO_SMA_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of Special mention accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of  SMA ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_SMA_ACCOUNTS\": 0\r\n" + "                }\r\n" + "            },\r\n"
				+ "            \"NO_WRITTEN_OFF_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of written off accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of WRITTEN OFF ACCOUNTS ( if =0 GO,  >=0 NoGO)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_WRITE_OFF_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"NO_SUBSTANDARD_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of Substandard accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of SUBSTANDARD ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_SUBSTANDARD_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"AML_SCORE_MATCH_VIKAS_LOAN\": {\r\n"
				+ "                \"description\": \"AML is should be less then 1\",\r\n"
				+ "                \"result\": true,\r\n" + "                \"definition\": null,\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"AML_SCORE_VALUE\": 0.5\r\n" + "                }\r\n" + "            },\r\n"
				+ "            \"NO_RESTRUCTURED_ACCOUNTS\": {\r\n"
				+ "                \"description\": \"Number of restructured accounts should be 0.\",\r\n"
				+ "                \"result\": true,\r\n"
				+ "                \"definition\": \"NUM of  Restructured ACCOUNTS ( if =0 GO,  >=0 NoGo)\",\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"COUNT_RESTRUCTURED_ACCOUNTS\": 0\r\n" + "                }\r\n"
				+ "            },\r\n" + "            \"CFR_MATCH_VIKAS_LOAN\": {\r\n"
				+ "                \"description\": \"CFR should be no match found\",\r\n"
				+ "                \"result\": true,\r\n" + "                \"definition\": null,\r\n"
				+ "                \"deviation\": false,\r\n" + "                \"derived_values\": {\r\n"
				+ "                    \"CFR_VALUE\": \"NO MATCH\"\r\n" + "                }\r\n" + "            }\r\n"
				+ "        }\r\n" + "    }\r\n" + "}";
		JSONObject brerule1 = new JSONObject(breResp);
		JSONArray breData = new JSONArray();
		JSONObject breData1 = new JSONObject();
		breData1.put("Parameter", "NO_SUITFIELD_ACCOUNTS");
		breData1.put("Variable", "0");
		breData1.put("Deviation", "False");
		breData1.put("GoForNOGO", "Go");
		breData.put(breData1);
		JSONObject breData2 = new JSONObject();
		breData2.put("Parameter", "OLDEST_MFI_ACCOUNT");
		breData2.put("Variable", "37");
		breData2.put("Deviation", "False");
		breData2.put("GoForNOGO", "Go");
		breData.put(breData2);
		JSONObject breData3 = new JSONObject();
		breData3.put("Parameter", "OLDEST_RETAIL_ACCOUNT");
		breData3.put("Variable", "37");
		breData3.put("Deviation", "False");
		breData3.put("GoForNOGO", "Go");
		breData.put(breData3);
		JSONObject breData4 = new JSONObject();
		breData4.put("Parameter", "MAX_90_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI");
		breData4.put("Variable", "0");
		breData4.put("Deviation", "False");
		breData4.put("GoForNOGO", "Go");
		breData.put(breData4);
		JSONObject breData5 = new JSONObject();
		breData5.put("Parameter", "EXISTING_MFI_CUSTOMER");
		breData5.put("Variable", "1");
		breData5.put("Deviation", "False");
		breData5.put("GoForNOGO", "NoGo");
		breData.put(breData5);
		JSONObject breData6 = new JSONObject();
		breData6.put("Parameter", "NO_LOSS_ACCOUNTS");
		breData6.put("Variable", "2");
		breData6.put("Deviation", "False");
		breData6.put("GoForNOGO", "Go");
		breData.put(breData6);
		JSONObject breData7 = new JSONObject();
		breData7.put("Parameter", "NO_SUBSTANDARD_ACCOUNTS");
		breData7.put("Variable", "0");
		breData7.put("Deviation", "False");
		breData7.put("GoForNOGO", "Go");
		breData.put(breData7);
		JSONObject breData8 = new JSONObject();
		breData8.put("Parameter", "NO_RESTRUCTURED_ACCOUNTS");
		breData8.put("Variable", "0");
		breData8.put("Deviation", "False");
		breData8.put("GoForNOGO", "Go");
		breData.put(breData8);
		JSONObject breData9 = new JSONObject();
		breData9.put("Parameter", "NO_SMA_ACCOUNTS");
		breData9.put("Variable", "0");
		breData9.put("Deviation", "False");
		breData9.put("GoForNOGO", "Go");
		breData.put(breData9);
		JSONObject breData10 = new JSONObject();
		breData10.put("Parameter", "NO_DOUBTFUL_ACCOUNTS");
		breData10.put("Variable", "0");
		breData10.put("Deviation", "False");
		breData10.put("GoForNOGO", "Go");
		breData.put(breData10);
		JSONObject breData11 = new JSONObject();
		breData11.put("Parameter", "NO_WILFUL_DEFAULT_ACCOUNTS");
		breData11.put("Variable", "0");
		breData11.put("Deviation", "False");
		breData11.put("GoForNOGO", "NoGo");
		breData.put(breData11);
		JSONObject breData12 = new JSONObject();
		breData12.put("Parameter", "NO_WRITTEN_OFF_ACCOUNTS");
		breData12.put("Variable", "0");
		breData12.put("Deviation", "False");
		breData12.put("GoForNOGO", "Go");
		breData.put(breData12);
		JSONObject breData13 = new JSONObject();
		breData13.put("Parameter", "MAX_30_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI");
		breData13.put("Variable", "0");
		breData13.put("Deviation", "False");
		breData13.put("GoForNOGO", "NoGo");
		breData.put(breData13);
		JSONObject breData14 = new JSONObject();
		breData14.put("Parameter", "MAX_0_PLUS_DPD_LAST_3_MONTHS_RETAIL_MFI");
		breData14.put("Variable", "0");
		breData14.put("Deviation", "False");
		breData14.put("GoForNOGO", "Go");
		breData.put(breData14);
		brerule1.put("BREData", breData);
		return new ResponseEntity<Object>(brerule1.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/checkstatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatus(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
		String breStatus = "Approved";// INITIATED , APPROVED, REJECT
		if (applicationDetails.getLeadId() == null) {
			JSONObject createLead = bresService.createLead(applicationNo, Header);
			HttpStatus h = HttpStatus.OK;
			if (createLead != null) {
				String Data2 = createLead.getString("data");
				System.out.println("data2" + Data2);
				JSONObject Data1 = new JSONObject(Data2);

				System.out.println(Data1);

				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String leadID = Data1.getJSONObject("data").getString("lead_id");
					if (applicationDetails.getLeadId() == null) {
						applicationDetails.setLeadId(leadID);
						applicationDetails.setIsLeadCreated("YES");
						applicationDetails.setIsBreRuring("NO");
						applicationDetails.setBreStatus("INITIATED");
						applicationDetailsService.save(applicationDetails);
					}
				} else if (Data1.has("error")) {
					String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
					applicationDetails.setLeadId(leadId);
					applicationDetails.setIsLeadCreated("YES");
					applicationDetails.setIsBreRuring("NO");
					applicationDetails.setBreStatus("INITIATED");
					applicationDetailsService.save(applicationDetails);

				}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}

		} else if (applicationDetails.getIsBreRuring().equalsIgnoreCase("YES")
				&& applicationDetails.getBreResponse() == null) {
			JSONObject fetchBre = bresService.fetchBre(applicationNo, Header, applicationDetails.getLeadId());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				String status = Data1.getJSONObject("data").getString("status");
				JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
				if (status.equalsIgnoreCase("Approved")) {
					applicationDetails.setBreResponse(breResp.toString());
					Object eligibilityAmount = breResp.getJSONObject("LOAN_ELIGIBILITY_AMOUNT")
							.getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT_FROM_GRID");
					applicationDetails.setMaxEmiEligibility(eligibilityAmount.toString());
					applicationDetails.setBreStatus(status);
					applicationDetails.setBreResponse(breResp.toString());
					applicationDetailsService.save(applicationDetails);
				} else if (status.equalsIgnoreCase("Rejected")) {
					applicationDetails.setBreStatus(status);
					applicationDetails.setBreResponse(breResp.toString());
					applicationDetailsService.save(applicationDetails);
				}
				System.out.println(Data1);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("LeadCreated", applicationDetails.getIsLeadCreated());
		response.put("isBreRuring", applicationDetails.getIsBreRuring());
		response.put("BreStatus", applicationDetails.getBreStatus());

		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equals("OPTIONS")) {
			// response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			// throw new NoSuchElementException("You are Not authorized");
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "unatharised Access");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(401);
			errorResponse.setMessage("Unauthorized Access");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getOutputStream().write(responseToSend);
			return;
		} else {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setHeader("Strict-Transport-Security", HEADER_VALUE);
			httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
			httpServletResponse.setHeader("X-Content-Type-Options", "no sniff");
			httpServletResponse.setHeader("Content-Security-Policy", "default-src");
			httpServletResponse.setHeader("X-XSS-Protection", "0");

			filterChain.doFilter(request, response);
		}
	}

	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}

}
