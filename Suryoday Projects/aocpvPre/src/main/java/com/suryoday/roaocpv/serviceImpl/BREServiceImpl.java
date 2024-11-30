package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.BREService;

@Service
public class BREServiceImpl implements BREService{

	private static Logger logger = LoggerFactory.getLogger(BREServiceImpl.class);
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	
	
	
	
	@Override
	public JSONObject createLead(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		
		logger.debug("checkstatusEncy start");
		System.out.println("checkstatusEncy start request");
		JSONObject request=getRequest(applicationNo);
		logger.debug("checkstatusEncy start request"+request.toString());
		System.out.println("checkstatusEncy start request"+request.toString());
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
//			logger.debug(x.onefin + "api/v1/client/loan/");
//		obj = new URL(x.onefin + "api/v1/client/loan/");
		
			logger.debug(x.BASEURL + "lead/loan/creation?api_key="+x.api_key);
			obj = new URL(x.BASEURL + "lead/loan/creation?api_key="+x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			//con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
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
	
	private JSONObject getRequest(String applicationNo) {
		org.json.JSONObject request = new org.json.JSONObject();
		org.json.simple.JSONArray kyc = new org.json.simple.JSONArray();
		org.json.simple.JSONObject kyc1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject kyc2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject institute_detail = new org.json.simple.JSONObject();
		org.json.simple.JSONObject bank_account = new org.json.simple.JSONObject();
		org.json.simple.JSONArray disbursal_details = new org.json.simple.JSONArray();
		org.json.simple.JSONObject disbursal_detail = new org.json.simple.JSONObject();
		org.json.simple.JSONArray repayment_schedule = new org.json.simple.JSONArray();
		org.json.simple.JSONObject repayment_schedule1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject additional_variables = new org.json.simple.JSONObject();
		org.json.simple.JSONObject bank_account1 = new org.json.simple.JSONObject();
		org.json.simple.JSONArray co_applicant_details = new org.json.simple.JSONArray();
		org.json.simple.JSONObject additional_variable = new org.json.simple.JSONObject();
		
		logger.debug("checkstatusEncy start request1 ");
		System.out.println("checkstatusEncy request1 ");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		String dateofBirth="";
		String gender="";
		String FATHER_NAME="";
		String MOTHER_NAME="";
		String fullname="";
		String FIRST_NAME="";
		String address1="";
		long pincode=0;
		String city="";
		String state="";
		if(fetchByApplicationId.getEkycResponse() == null) {
			logger.debug("checkstatusEncy start request2 ");
			System.out.println("checkstatusEncy request2 ");
	 	   }
	 	   else {
	 		  logger.debug("checkstatusEncy start request3 ");
				System.out.println("checkstatusEncy request3 ");
		org.json.JSONObject ekyc=new org.json.JSONObject(fetchByApplicationId.getEkycResponse());
			JSONObject PoaResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poa");
			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
			if(PoaResponse.has("street")) {
				address1=PoaResponse.getString("street");
		 	}
			else if(PoaResponse.has("loc")) {
				address1=PoaResponse.getString("loc");
			}
			pincode=PoaResponse.getLong("pc");
		 	if(PoaResponse.has("loc")) {
		 		city= PoaResponse.getString("loc");
		 	}
		 	state=PoaResponse.getString("state");
		 	dateofBirth=PoiResponse.getString("dob");
		 	fullname= PoiResponse.getString("name");
		 	String[] split = fullname.split(" ");
		 	if(split.length>1) {
		 		FATHER_NAME=split[1];	
		 	}
		 	gender=PoiResponse.getString("gender"); 
	 	   }
		  logger.debug("checkstatusEncy start request4 ");
			System.out.println("checkstatusEncy request4 ");
		String address=new String(address1+","+city+","+state);
		if(fetchByApplicationId.getPanCard()!=null) {
			  logger.debug("checkstatusEncy start request5 ");
				System.out.println("checkstatusEncy request5 ");
			String panCardResponse = fetchByApplicationId.getPanCardResponse();
			JSONObject panResponse=new JSONObject(panCardResponse);
			
			//JSONArray panDetails = panResponse.getJSONObject("Data").getJSONArray("PANDetails");
			//JSONObject pandetail = panDetails.getJSONObject(0);
			kyc1.put("kyc_type", 2);
			kyc1.put("identifier", fetchByApplicationId.getPanCard());
			kyc1.put("verified_using", 1);
//			kyc1.put("verification_id", "12342Ak");
			if(panResponse.getString("NameOnCard").equals(""))
			{
				kyc1.put("kyc_name",fullname );
			}
			else
			{
				kyc1.put("kyc_name",panResponse.getString("NameOnCard") );
			}
		
			kyc1.put("is_verified", true);
			kyc1.put("address", address);
			kyc.add(kyc1);
		}
		else if(fetchByApplicationId.getVoterId()!=null)
		{
			 logger.debug("checkstatusEncy start request6 ");
				System.out.println("checkstatusEncy request6 ");
			String voterIdResponse = fetchByApplicationId.getVoterIdResponse();
			JSONObject voterResponse=new JSONObject(voterIdResponse);
			kyc2.put("kyc_type", 5);
			kyc2.put("identifier",voterResponse.getString("epic_no"));
			kyc2.put("verified_using", 1);
//			kyc2.put("verification_id", "12342Ak");
			kyc2.put("kyc_name", voterResponse.getString("name"));
			kyc2.put("is_verified", true);
			kyc2.put("address", address1);
			kyc.add(kyc2);
		}

		request.put("kyc", kyc);
		request.put("mobile_number", fetchByApplicationId.getMobileNo());
	//	request.put("partner_loan_id", fetchByApplicationId.getApplicationId());
		int max=1000000000;
		int min=100000000;
		double random = Math.random()*(max-min+1)+min; 
		request.put("partner_loan_id", fetchByApplicationId.getAppNoWithProductCode());
		request.put("email","");
		request.put("product","VKSLN");
		if(gender.equals("F"))
		{
		request.put("gender", 2);
		}
		else if(gender.equals("M"))
		{
			request.put("gender",1);
		}
		
		 logger.debug("checkstatusEncy start request7 ");
			System.out.println("checkstatusEncy request7 ");
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	      LocalDate dob1 = LocalDate.parse(dateofBirth, formatter);
				 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			   	String dob2= dob1.format(formatter1);
			   	logger.debug("checkstatusEncy start request8 ");
				System.out.println("checkstatusEncy request8 ");	   	
		request.put("date_of_birth", dob2);
//		request.put("fathers_name", FATHER_NAME);
//		request.put("mothers_name", MOTHER_NAME);
		request.put("address", address);
		request.put("pincode",pincode);
		request.put("channel", 3);
		request.put("employment_type", 2);
		request.put("company_name", "");
		//fullname=fetchByApplicationId.getName();
		request.put("full_name",fullname);
		LocalDateTime createts = fetchByApplicationId.getCreatets();
//		 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		  
		request.put("loan_application_date", createts.format(formatter1));
		request.put("loan_tenure","");
		request.put("loan_amount", Long.parseLong(fetchByApplicationId.getRequiredAmount()));
		request.put("loan_type", 5);
//		request.put("agreement_date", "20-Dec-2022");
//		request.put("agreement_signature_type", 2);
		
//		request.put("repayment_count","");
		request.put("interest_rate",28 );
		request.put("payment_frequency", 3);
	
//		co_applicant_detail.put("co_dob", "1983-03-31");
//		co_applicant_detail.put("co_email", "");
//		co_applicant_detail.put("co_phone", "");
//		co_applicant_detail.put("co_gender", 1);
//		co_applicant_detail.put("co_aadhaar", "");
//		co_applicant_detail.put("co_address", "#  bhavani nagar  #  Vite Sangli Maharashtra 415311");
//		co_applicant_detail.put("co_pancard", "BXAPB0524L");
//		co_applicant_detail.put("co_pincode", "415311");
//		co_applicant_detail.put("co_relation", 1);
//		co_applicant_detail.put("co_full_name", "");
//		co_applicant_detail.put("co_aadhaar_case_id", null);
//		co_applicant_detail.put("co_customer_id", "");
//
//		co_applicant_details.add(co_applicant_detail);
//		request.put("co_applicant_details", co_applicant_details);
		
//		institute_detail.put("brand_name", "Akash Brand");
//		institute_detail.put("course_name", "ABC");
//		bank_account.put("ac_name", "Test Application");
//		bank_account.put("ac_number", "11020001772");
//		bank_account.put("ifsc_code", "HDFC0000001");
//		institute_detail.put("bank_account", bank_account);
//		institute_detail.put("legal_entity_name", "RENAISSANCE UNIVERSITY");
//		bank_account1.put("ac_name", "Akash");
//		bank_account1.put("ac_number", "00011020001772");
//		bank_account1.put("ifsc_code", "HDFC0000001");
//		request.put("bank_account", bank_account1);
//		request.put("institute_detail", institute_detail);
		
//		additional_variable.put("channel_partner_name", "Sarathi");
//		additional_variable.put("aadhar_vault_number", fetchByApplicationId.getAadharNumber());
//		additional_variable.put("customer_id", "");
//		request.put("additional_variable", additional_variable);
		
//		disbursal_detail.put("date", "20-Dec-2022");
//		disbursal_detail.put("amount", 2000);
//		disbursal_detail.put("disbursal_type", 0);
//		disbursal_detail.put("processing_fee", 100);
//		disbursal_detail.put("pre_emi", 200);
//		disbursal_detail.put("gst", 20);
//		disbursal_detail.put("subvention", 350);
//		disbursal_detail.put("upfront_emi", 0);
//		disbursal_detail.put("downpayment", 100);
//		disbursal_detail.put("insurance", 10);
//		
//		disbursal_details.add(disbursal_detail);
//		
//		request.put("disbursal_detail", disbursal_details);
		
//		repayment_schedule1.put("due_date", "20-Dec-2022");
//		repayment_schedule1.put("due_amount", 100);
//		repayment_schedule1.put("principal_amount", 24000);
//		repayment_schedule1.put("interest_amount", 25);
//		
//		repayment_schedule.add(repayment_schedule1);
//		
//		request.put("repayment_schedule", repayment_schedule);
		
		//request.put("additional_variables", additional_variables);
	 	logger.debug("checkstatusEncy start request9 "+request.toString());
		System.out.println("checkstatusEncy request9 "+request.toString());	
		return request;
	}

	@Override
	public JSONObject fetchBre(String applicationNo, JSONObject header,String leadId) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = new JSONObject();
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
//			logger.debug(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
//			obj = new URL(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
			logger.debug(x.BASEURL + "client/loan/"+leadId+"/approve?api_key="+x.api_key);
			obj = new URL(x.BASEURL + "client/loan/"+leadId+"/approve?api_key="+x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			//con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	
	}

	@Override
	public JSONObject equifaxReport(String applicationNo, JSONObject header, String leadId) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = new JSONObject();
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
			
			logger.debug(x.BASEURL + "onefin/equifax/"+leadId+"?api_key="+x.api_key);
			obj = new URL(x.BASEURL + "onefin/equifax/"+leadId+"?api_key="+x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			//con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	@Override
	public JSONObject equifaxReport(String applicationNo) {
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		String personalDetailsReq = fetchByApplicationId.getPersonalDetailsReq();
		String FIRST_NAME="";
		String MIDDLE_NAME="";
		String LAST_NAME="";
		String MOBILE=fetchByApplicationId.getMobileNo();
		String DOB="";
		String AGE="";
		String GENDER="";
		if(fetchByApplicationId.getEkycResponse() != null) {
	 		  org.json.JSONObject ekyc=new org.json.JSONObject(fetchByApplicationId.getEkycResponse());
	 			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
	 		
			 	GENDER=	 PoiResponse.getString("gender");
			 	DOB= PoiResponse.getString("dob");
			 	 String[] fullname = PoiResponse.getString("name").split(" ");
			 	 if(fullname.length ==3) {
			 	FIRST_NAME = fullname[0];
			 	MIDDLE_NAME = fullname[1];
			 	LAST_NAME = fullname[2];
			 	 }else if(fullname.length ==2) {
			 		FIRST_NAME = fullname[0];
			 		LAST_NAME = fullname[1];
			 	 }else if(fullname.length > 3){
			 		FIRST_NAME = fullname[0];
				 	MIDDLE_NAME = fullname[1];
				 	String name="";
				 for(int n=2;n<fullname.length;n++) {
					 name=name+fullname[n];
				 } 	
				 LAST_NAME = name;
			 	 }
			 	 
		}
//		JSONObject fetchBre = bresService.equifaxReport(applicationNo, Header,fetchByApplicationId.getLeadId());
//		JSONObject fetchBre = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("status", "Approved");
	
//		fetchBre.put("data", data.toString());
		
		JSONObject fetchBre=new JSONObject();
		JSONObject data=new JSONObject();
		JSONObject rawEquifaxData=new JSONObject();
		JSONArray score=new JSONArray();
		JSONObject score1=new JSONObject();
		score1.put("Type","ERS");
		score1.put("Version","3.1");
		score.put(score1);
		rawEquifaxData.put("Score",score);
		JSONObject ccrResp=new JSONObject();
		ccrResp.put("Status","1");
		JSONArray cirRepDataLst=new JSONArray();
		JSONObject cirRepDataLst1=new JSONObject();
		JSONArray cirRepDataLstscore=new JSONArray();
		JSONObject cirRepDataLstscore1=new JSONObject();
		cirRepDataLstscore1.put("Type","ERS");
		cirRepDataLstscore1.put("Version","3.1");
		cirRepDataLstscore.put(cirRepDataLstscore1);
		cirRepDataLst1.put("Score",cirRepDataLstscore);
		JSONObject cirRepData=new JSONObject();
		JSONArray scoreDetails=new JSONArray();
		JSONObject scoreDetails1=new JSONObject();
		scoreDetails1.put("Type","ERS");
		scoreDetails1.put("Version","3.1");
		JSONArray scoringElements=new JSONArray();
		JSONObject scoringElements1=new JSONObject();
		scoringElements1.put("Description","Number of product trades");
		scoringElements1.put("type","RES");
		scoringElements1.put("seq","1");
		scoringElements.put(scoringElements1);
		scoreDetails1.put("ScoringElements",scoringElements);
		scoreDetails1.put("Value","737");
		scoreDetails1.put("Name","M001");
		scoreDetails.put(scoreDetails1);
		cirRepData.put("ScoreDetails",scoreDetails);
		JSONObject idConInfo=new JSONObject();
		JSONObject personalInfo=new JSONObject();
		JSONObject aliasName=new JSONObject();
		personalInfo.put("AliasName",aliasName);
		personalInfo.put("DateOfBirth",DOB);
		JSONObject placeOfBirthInfo=new JSONObject();
		personalInfo.put("PlaceOfBirthInfo",placeOfBirthInfo);
		personalInfo.put("Gender",GENDER);
		JSONObject age=new JSONObject();
		age.put("Age",AGE);
		personalInfo.put("Age",age);
		JSONObject name=new JSONObject();
		name.put("FirstName",FIRST_NAME);
		name.put("FullName",FIRST_NAME+" "+MIDDLE_NAME+" "+LAST_NAME);
		name.put("LastName",LAST_NAME);
		name.put("MiddleName",MIDDLE_NAME);
		personalInfo.put("Name",name);
		idConInfo.put("PersonalInfo",personalInfo);
		JSONObject identityInfo=new JSONObject();
		JSONArray PANId=new JSONArray();
		JSONObject PANId1=new JSONObject();
		JSONArray aadhar=new JSONArray();
		JSONObject aadhar1=new JSONObject();
		aadhar1.put("ReportedDate",LocalDate.now());
		aadhar1.put("IdNumber",fetchByApplicationId.getAadharNumber());
		aadhar1.put("seq","2");
		PANId1.put("ReportedDate",LocalDate.now());
		PANId1.put("IdNumber",fetchByApplicationId.getPanCard());
		PANId1.put("seq","1");
		PANId.put(PANId1);
		aadhar.put(aadhar1);
		identityInfo.put("PANId",PANId);
		identityInfo.put("AAdharCard",aadhar);
		idConInfo.put("IdentityInfo",identityInfo);
		JSONArray phoneInfo=new JSONArray();
		   JSONObject phoneInfo1=new JSONObject();
	        phoneInfo1.put("ReportedDate",LocalDate.now());
	        phoneInfo1.put("Number",fetchByApplicationId.getMobileNo());
	        phoneInfo1.put("seq","1");
	        phoneInfo1.put("typeCode","M");
	        phoneInfo.put(phoneInfo1);
		idConInfo.put("PhoneInfo",phoneInfo);
		JSONArray addInfo=new JSONArray();
		idConInfo.put("AddressInfo",addInfo);
		cirRepData.put("IDAndContactInfo",idConInfo);
		JSONArray enquiries=new JSONArray();
		JSONObject enquiries1=new JSONObject();
		enquiries1.put("Amount","10000");
		enquiries1.put("Time","14:23");
		enquiries1.put("Institution","Janlakshmi Financial Services Pvt Ltd");
		enquiries1.put("seq","0");
		enquiries1.put("Date","2022-07-25");
		enquiries1.put("RequestPurpose","51");
		enquiries.put(enquiries1);
		cirRepData.put("Enquiries",enquiries);
		JSONObject otherKeyEnd=new JSONObject();
		otherKeyEnd.put("AgeOfOldestTrade","112");
		otherKeyEnd.put("AllLinesEVERWrittenIn6Months","0");
		otherKeyEnd.put("NumberOfOpenTrades","0");
		otherKeyEnd.put("AllLinesEVERWritten","0.00");
		otherKeyEnd.put("AllLinesEVERWrittenIn9Months","0");
		cirRepData.put("OtherKeyInd",otherKeyEnd);
		JSONObject enquirySummary=new JSONObject();
		enquirySummary.put("Recent","2022-07-25");
		enquirySummary.put("Past30Days","0");
		enquirySummary.put("Total","1351");
		enquirySummary.put("Past12Months","17");
		enquirySummary.put("Past24Months","23");
		enquirySummary.put("Purpose","ALL");
		cirRepData.put("EnquirySummary",enquirySummary);
		JSONObject recentActivities=new JSONObject();
		recentActivities.put("TotalInquiries","5");
		recentActivities.put("AccountsOpened","0");
		recentActivities.put("AccountsDeliquent","0");
		recentActivities.put("AccountsUpdated","0");
		cirRepData.put("RecentActivities",recentActivities);
		JSONArray retailAccDetails=new JSONArray();
		JSONObject retailAccDetails1=new JSONObject();
		retailAccDetails1.put("OwnershipType","Individual");
		retailAccDetails1.put("DateOpened","2017-02-06");
		retailAccDetails1.put("DateReported","2018-01-31");
		retailAccDetails1.put("TermFrequency","Monthly");
		retailAccDetails1.put("RepaymentTenure","60");
		retailAccDetails1.put("InstallmentAmount","6742");
		retailAccDetails1.put("source","INDIVIDUAL");
		retailAccDetails1.put("Institution","State Bank Of India");
		retailAccDetails1.put("AccountType","Personal Loan");
		retailAccDetails1.put("Open","Yes");
		retailAccDetails1.put("AccountNumber","00000036518184846");
		retailAccDetails1.put("AccountStatus","Standard");
		retailAccDetails1.put("SanctionAmount","30000");
		retailAccDetails1.put("AssetClassification","Standard");
		retailAccDetails1.put("InterestRate","12.000");
		retailAccDetails1.put("Balance","256239");
		retailAccDetails1.put("seq","1");
		
		JSONObject retailAccDetails2=new JSONObject();
		retailAccDetails2.put("OwnershipType","Individual");
		retailAccDetails2.put("DateOpened","2013-05-07");
		retailAccDetails2.put("DateReported","2016-11-30");
		retailAccDetails2.put("TermFrequency","Monthly");
		retailAccDetails2.put("RepaymentTenure","60");
		retailAccDetails2.put("InstallmentAmount","6742");
		retailAccDetails2.put("source","INDIVIDUAL");
		retailAccDetails2.put("Institution","State Bank Of India");
		retailAccDetails2.put("AccountType","Personal Loan");
		retailAccDetails2.put("Open","NO");
		retailAccDetails2.put("AccountNumber","00000036518184846");
		retailAccDetails2.put("AccountStatus","Standard");
		retailAccDetails2.put("SanctionAmount","100000");
		retailAccDetails2.put("AssetClassification","Standard");
		retailAccDetails2.put("InterestRate","12.000");
		retailAccDetails2.put("Balance","0");
		retailAccDetails2.put("seq","2");
		retailAccDetails.put(retailAccDetails1);
		retailAccDetails.put(retailAccDetails2);
		cirRepData.put("RetailAccountDetails",retailAccDetails);
		JSONObject retailAccountSummary=new JSONObject();
		retailAccountSummary.put("TotalSanctionAmount","300000.00");
		retailAccountSummary.put("NoOfPastDueAccounts","0");
		retailAccountSummary.put("TotalCreditLimit","0.0");
		retailAccountSummary.put("RecentAccount","Personal Loan on 06-02-2017");
		retailAccountSummary.put("TotalHighCredit","0.00");
		retailAccountSummary.put("NoOfActiveAccounts","1");
		retailAccountSummary.put("OldestAccount","Personal Loan on 07-05-2013");
		retailAccountSummary.put("SingleHighestSanctionAmount","30000.00");
		retailAccountSummary.put("NoOfAccounts","2");
		retailAccountSummary.put("NoOfWriteOffs","0");
		retailAccountSummary.put("SingleHighestCredit","0.00");
		retailAccountSummary.put("AverageOpenBalance","256239.00");
		retailAccountSummary.put("TotalPastDue","0.00");
		retailAccountSummary.put("NoOfZeroBalanceAccounts","0");
		retailAccountSummary.put("TotalMonthlyPaymentAmount","6742.00");
		retailAccountSummary.put("TotalBalanceAmount","256239.00");
		retailAccountSummary.put("SingleHighestBalance","256239.00");
		cirRepData.put("RetailAccountsSummary",retailAccountSummary);
		cirRepDataLst1.put("CIRReportData",cirRepData);
		JSONObject inqRespHeader=new JSONObject();
		inqRespHeader.put("HitCode","10");
		inqRespHeader.put("ReportOrderNO","9031630");
		inqRespHeader.put("CustomerCode","IC01");
		inqRespHeader.put("SuccessCode","1");
		inqRespHeader.put("CustRefField","123456");
		inqRespHeader.put("Time","20:01:14");
		inqRespHeader.put("CustomerName","IC01");
		inqRespHeader.put("Date","2022-09-09");
		cirRepDataLst1.put("InquiryResponseHeader",inqRespHeader);
		JSONObject inqReqInfo=new JSONObject();
		inqReqInfo.put("InquiryPurpose","Other");
		JSONArray idDetails=new JSONArray();
		JSONObject idDetails1=new JSONObject();
		idDetails1.put("IDValue","ARSPB2789E");
		idDetails1.put("IDType","T");
		idDetails1.put("seq","1");
		idDetails1.put("Source","Inquiry");
		idDetails.put(idDetails1);
		inqReqInfo.put("IDDetails",idDetails);
		inqReqInfo.put("DOB","1960-05-30");
		inqReqInfo.put("FirstName","RAM SINGH BHANDARI");
		JSONObject mfiDetails=new JSONObject();
		JSONArray familyDetails=new JSONArray();
		JSONObject familyDetails1=new JSONObject();
		familyDetails1.put("AdditionalName","LAXMAN BHANDARI");
		familyDetails1.put("AdditionalNameType","K01");
		familyDetails1.put("seq","1");
		familyDetails.put(familyDetails1);
		JSONObject familyDetails2=new JSONObject();
		familyDetails2.put("AdditionalNameType","K01");
		familyDetails2.put("seq","2");
		familyDetails.put(familyDetails2);
		mfiDetails.put("FamilyDetails",familyDetails);
		inqReqInfo.put("MFIDetails",mfiDetails);
		JSONArray inqPhones=new JSONArray();
		JSONObject inqPhones1=new JSONObject();
		inqPhones.put(inqPhones1);
		inqPhones1.put("Number","8191817766");
		JSONArray phoneType=new JSONArray();
		phoneType.put("M");
		inqPhones1.put("PhoneType",phoneType);
		inqPhones1.put("seq","1");
		inqReqInfo.put("InquiryPhones",inqPhones);
		JSONArray inqAddresses=new JSONArray();
		JSONObject inqAddresses1=new JSONObject();
		inqAddresses1.put("AddressLine1","169 RAJEEV NAGAR  TARLI KANDOLI DEHRADUN UTTARAKHAND DEHRADUN");
		inqAddresses1.put("State","UK");
		JSONArray addressType=new JSONArray();
		addressType.put("H");
		inqAddresses1.put("AddressType",addressType);
		inqAddresses1.put("seq","1");
		inqAddresses1.put("Postal","249145");
		inqAddresses.put(inqAddresses1);
		inqReqInfo.put("InquiryAddresses",inqAddresses);
		cirRepDataLst1.put("InquiryRequestInfo",inqReqInfo);
		cirRepDataLst.put(cirRepDataLst1);
		ccrResp.put("CIRReportDataLst",cirRepDataLst);
		rawEquifaxData.put("CCRResponse",ccrResp);
		JSONObject inquiryRespHeader=new JSONObject();
		inquiryRespHeader.put("ReportOrderNO","9031630");
		JSONArray productCode=new JSONArray();
		productCode.put("CCR");
		inquiryRespHeader.put("ProductCode",productCode);
		inquiryRespHeader.put("ClientID","028FZ00016");
		inquiryRespHeader.put("SuccessCode","1");
		inquiryRespHeader.put("CustRefField","123456");
		inquiryRespHeader.put("Time","20:01:15");
		inquiryRespHeader.put("Date","2022-09-09");
		rawEquifaxData.put("InquiryResponseHeader",inquiryRespHeader);
		JSONObject inquiryReqInfo=new JSONObject();
		inqReqInfo.put("InquiryPurpose","Other");
		JSONArray IdDetails=new JSONArray();
		JSONObject IdDetails1=new JSONObject();
		IdDetails1.put("IDValue","ARSPB2789E");
		IdDetails1.put("IDType","T");
		IdDetails1.put("seq","1");
		idDetails1.put("Source","Inquiry");
		IdDetails.put(IdDetails1);
		inquiryReqInfo.put("IDDetails",idDetails);
		inquiryReqInfo.put("DOB","1960-05-30");
		inquiryReqInfo.put("FirstName","RAM SINGH BHANDARI");
		JSONObject MfiDetails=new JSONObject();
		JSONArray FamilyDetails=new JSONArray();
		JSONObject FamilyDetails1=new JSONObject();
		FamilyDetails1.put("AdditionalName","LAXMAN BHANDARI");
		FamilyDetails1.put("AdditionalNameType","K01");
		FamilyDetails1.put("seq","1");
		FamilyDetails.put(FamilyDetails1);
		JSONObject FamilyDetails2=new JSONObject();
		FamilyDetails2.put("AdditionalNameType","K01");
		FamilyDetails2.put("seq","2");
		FamilyDetails.put(FamilyDetails2);
		mfiDetails.put("FamilyDetails",FamilyDetails);
		inquiryReqInfo.put("MFIDetails",MfiDetails);
		JSONArray InqPhones=new JSONArray();
		JSONObject InqPhones1=new JSONObject();
		InqPhones1.put("Number","8191817766");
		InqPhones.put(InqPhones1);
		JSONArray PhoneType=new JSONArray();
		PhoneType.put("M");
		InqPhones1.put("PhoneType",phoneType);
		InqPhones1.put("seq","1");
		inquiryReqInfo.put("InquiryPhones",inqPhones);
		JSONArray InqAddresses=new JSONArray();
		JSONObject InqAddresses1=new JSONObject();
		InqAddresses1.put("AddressLine1","169 RAJEEV NAGAR  TARLI KANDOLI DEHRADUN UTTARAKHAND DEHRADUN");
		InqAddresses1.put("State","UK");
		JSONArray AddressType=new JSONArray();
		AddressType.put("H");
		InqAddresses1.put("AddressType",addressType);
		InqAddresses1.put("seq","1");
		InqAddresses1.put("Postal","249145");
		InqAddresses.put(inqAddresses1);
		inquiryReqInfo.put("InquiryAddresses",InqAddresses);
		rawEquifaxData.put("InquiryRequestInfo",inqReqInfo);
		data.put("raw_equifax_data",rawEquifaxData);
		data.put("is_success",true);
		ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		String filepath= x.FilePath;
		String equifax = equifaxData(filepath);
		 JSONObject euifaxJson=new JSONObject(equifax);
	        data.put("onfinereport", euifaxJson);
		fetchBre.put("data",data.toString());
		return fetchBre;
	}
	private static String equifaxData(String filePath)
    {
 

        StringBuilder builder = new StringBuilder();

        try (BufferedReader buffer = new BufferedReader(new java.io.FileReader(filePath))) {
 
            String str;
 
     
            while ((str = buffer.readLine()) != null) {
 
                builder.append(str).append("\n");
            }
        }
 
        catch (IOException e) {
            e.printStackTrace();
        }
 

        return builder.toString();
    }

	@Override
	public JSONObject brerules(String applicationNo) {
		
		ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
		String breResponse = applicationDetails.getBreResponse();
		
		JSONObject jsonObject=new JSONObject(breResponse);
	
		JSONObject Data=new JSONObject();
		JSONObject data=new JSONObject();
		JSONArray breData=new JSONArray();
		JSONObject breData1=new JSONObject();
		
		if(jsonObject.has("NO_NPA_ACCOUNTS_MFI")) {
			
			breData1.put("Parameter","NO_NPA_ACCOUNTS_MFI");
			breData1.put("outcome",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_MFI").getJSONObject("derived_values").get("COUNT_EQUIFAX_NPA_ACCOUNTS_MFI"));
			breData1.put("Deviation",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_MFI").get("deviation"));
			breData1.put("GoForNOGO",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_MFI").get("result"));
			
		}
		
		breData.put(breData1);
		JSONObject breData2=new JSONObject();
		if(jsonObject.has("CFR_MATCH_VIKAS_LOAN")) {
			breData2.put("Parameter","CFR_MATCH_VIKAS_LOAN");
			breData2.put("outcome",jsonObject.getJSONObject("CFR_MATCH_VIKAS_LOAN").getJSONObject("derived_values").get("CFR_VALUE"));
			breData2.put("Deviation",jsonObject.getJSONObject("CFR_MATCH_VIKAS_LOAN").get("deviation"));
			breData2.put("GoForNOGO",jsonObject.getJSONObject("CFR_MATCH_VIKAS_LOAN").get("result"));
			breData.put(breData2);	
		}
		if(jsonObject.has("EXISTING_MFI_CUSTOMER")) {
			JSONObject breData3=new JSONObject();
			breData3.put("Parameter","EXISTING_MFI_CUSTOMER");
			breData3.put("outcome",jsonObject.getJSONObject("EXISTING_MFI_CUSTOMER").getJSONObject("derived_values").get("NUM_OF_MFI_ACCOUNTS_VL"));
			breData3.put("Deviation",jsonObject.getJSONObject("EXISTING_MFI_CUSTOMER").get("deviation"));
			breData3.put("GoForNOGO",jsonObject.getJSONObject("EXISTING_MFI_CUSTOMER").get("result"));
			breData.put(breData3);
		}
		if(jsonObject.has("TOTAL_RETAIL_MFI_EMI")) {
			JSONObject breData4=new JSONObject();
			breData4.put("Parameter","TOTAL_RETAIL_MFI_EMI");
			breData4.put("outcome",jsonObject.getJSONObject("TOTAL_RETAIL_MFI_EMI").getJSONObject("derived_values").get("COUNT_RETAIL_MFI_EMI"));
			breData4.put("Deviation",jsonObject.getJSONObject("TOTAL_RETAIL_MFI_EMI").get("deviation"));
			breData4.put("GoForNOGO",jsonObject.getJSONObject("TOTAL_RETAIL_MFI_EMI").get("result"));
			breData.put(breData4);	
		}
		if(jsonObject.has("NO_WRITTEN_OFF_ACCOUNTS_RETAIL")) {
			JSONObject breData5=new JSONObject();
			breData5.put("Parameter","NO_WRITTEN_OFF_ACCOUNTS_RETAIL");
			breData5.put("outcome",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_RETAIL").getJSONObject("derived_values").get("COUNT_WRITTEN_OFF_ACCOUNTS_RETAIL"));
			breData5.put("Deviation",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_RETAIL").get("deviation"));
			breData5.put("GoForNOGO",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_RETAIL").get("result"));
			breData.put(breData5);
		}
		if(jsonObject.has("OLDEST_RETAIL_OR_MFI_ACCOUNT")) {
			JSONObject breData6=new JSONObject();
			breData6.put("Parameter","OLDEST_RETAIL_OR_MFI_ACCOUNT");
			breData6.put("outcome",jsonObject.getJSONObject("OLDEST_RETAIL_OR_MFI_ACCOUNT").getJSONObject("derived_values").get("FIRST_MFI_ACCOUNT_AGE_IN_MONTHS_VL"));
			breData6.put("Deviation",jsonObject.getJSONObject("OLDEST_RETAIL_OR_MFI_ACCOUNT").get("deviation"));
			breData6.put("GoForNOGO",jsonObject.getJSONObject("OLDEST_RETAIL_OR_MFI_ACCOUNT").get("result"));
			breData.put(breData6);	
		}
		if(jsonObject.has("NO_NPA_ACCOUNTS_RETAIL")) {
			JSONObject breData7=new JSONObject();
			breData7.put("Parameter","NO_NPA_ACCOUNTS_RETAIL");
			breData7.put("outcome",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_RETAIL").getJSONObject("derived_values").get("COUNT_EQUIFAX_NPA_ACCOUNTS_RETAIL"));
			breData7.put("Deviation",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_RETAIL").get("deviation"));
			breData7.put("GoForNOGO",jsonObject.getJSONObject("NO_NPA_ACCOUNTS_RETAIL").get("result"));
			breData.put(breData7);
		}
		
		if(jsonObject.has("NO_RESTRUCTURED_ACCOUNTS_RETAIL")) {
			JSONObject breData9=new JSONObject();
			breData9.put("Parameter","NO_RESTRUCTURED_ACCOUNTS_RETAIL");
			breData9.put("outcome",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_RETAIL").getJSONObject("derived_values"));
			breData9.put("Deviation",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_RETAIL").get("deviation"));
			breData9.put("GoForNOGO",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_RETAIL").get("result"));
			breData.put(breData9);	
		}
		if(jsonObject.has("NO_RESTRUCTURED_ACCOUNTS_MFI")) {
			JSONObject breData10=new JSONObject();
			breData10.put("Parameter","NO_RESTRUCTURED_ACCOUNTS_MFI");
			breData10.put("outcome",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_MFI").getJSONObject("derived_values"));
			breData10.put("Deviation",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_MFI").get("deviation"));
			breData10.put("GoForNOGO",jsonObject.getJSONObject("NO_RESTRUCTURED_ACCOUNTS_MFI").get("result"));
			breData.put(breData10);	
		}
		if(jsonObject.has("NO_WILFUL_DEFAULT_ACCOUNTS_MFI")) {
			JSONObject breData11=new JSONObject();
			breData11.put("Parameter","NO_WILFUL_DEFAULT_ACCOUNTS_MFI");
			breData11.put("outcome",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_MFI").getJSONObject("derived_values").get("COUNT_WILFUL_DEFAULT_ACCOUNTS_MFI"));
			breData11.put("Deviation",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_MFI").get("deviation"));
			breData11.put("GoForNOGO",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_MFI").get("result"));
			breData.put(breData11);
		}
		if(jsonObject.has("NO_WRITTEN_OFF_ACCOUNTS_MFI")) {
			JSONObject breData12=new JSONObject();
			breData12.put("Parameter","NO_WRITTEN_OFF_ACCOUNTS_MFI");
			breData12.put("outcome",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_MFI").getJSONObject("derived_values").get("COUNT_WRITTEN_OFF_ACCOUNTS_MFI"));
			breData12.put("Deviation",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_MFI").get("deviation"));
			breData12.put("GoForNOGO",jsonObject.getJSONObject("NO_WRITTEN_OFF_ACCOUNTS_MFI").get("result"));
			breData.put(breData12);	
		}
		if(jsonObject.has("NUM_MFI_ACCOUNTS_WITH_30_PLUS_DPD")) {
			JSONObject breData13=new JSONObject();
			breData13.put("Parameter","NUM_MFI_ACCOUNTS_WITH_30_PLUS_DPD");
			breData13.put("outcome",jsonObject.getJSONObject("NUM_MFI_ACCOUNTS_WITH_30_PLUS_DPD").getJSONObject("derived_values").get("COUNT_MFI_ACCOUNTS_WITH_30_PLUS_DPD"));
			breData13.put("Deviation",jsonObject.getJSONObject("NUM_MFI_ACCOUNTS_WITH_30_PLUS_DPD").get("deviation"));
			breData13.put("GoForNOGO",jsonObject.getJSONObject("NUM_MFI_ACCOUNTS_WITH_30_PLUS_DPD").get("result"));
			breData.put(breData13);	
		}
		if(jsonObject.has("LOAN_ELIGIBILITY_AMOUNT_DERIVED")) {
			JSONObject breData14=new JSONObject();
			breData14.put("Parameter","LOAN_ELIGIBILITY_AMOUNT_DERIVED");
			breData14.put("outcome",jsonObject.getJSONObject("LOAN_ELIGIBILITY_AMOUNT_DERIVED").getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT"));
			breData14.put("Deviation",jsonObject.getJSONObject("LOAN_ELIGIBILITY_AMOUNT_DERIVED").get("deviation"));
			breData14.put("GoForNOGO",jsonObject.getJSONObject("LOAN_ELIGIBILITY_AMOUNT_DERIVED").get("result"));
			breData.put(breData14);	
		}
		if(jsonObject.has("NUM_ACCOUNTS_30_PLUS_DPD_LAST_12_MONTHS_EXCEPT_GL_KCC_CC")) {
			JSONObject breData15=new JSONObject();
			breData15.put("Parameter","NUM_ACCOUNTS_30_PLUS_DPD_LAST_12_MONTHS_EXCEPT_GL_KCC_CC");
			breData15.put("outcome",jsonObject.getJSONObject("NUM_ACCOUNTS_30_PLUS_DPD_LAST_12_MONTHS_EXCEPT_GL_KCC_CC").getJSONObject("derived_values"));
			breData15.put("Deviation",jsonObject.getJSONObject("NUM_ACCOUNTS_30_PLUS_DPD_LAST_12_MONTHS_EXCEPT_GL_KCC_CC").get("deviation"));
			breData15.put("GoForNOGO",jsonObject.getJSONObject("NUM_ACCOUNTS_30_PLUS_DPD_LAST_12_MONTHS_EXCEPT_GL_KCC_CC").get("result"));
			breData.put(breData15);	
		}
		if(jsonObject.has("LOAN_CATEGORY")) {
			JSONObject breData16=new JSONObject();
			breData16.put("Parameter","LOAN_CATEGORY");
			breData16.put("outcome",jsonObject.getJSONObject("LOAN_CATEGORY").getJSONObject("derived_values").get("LOAN_CATEGORY_FROM_GRID"));
			breData16.put("Deviation",jsonObject.getJSONObject("LOAN_CATEGORY").get("deviation"));
			breData16.put("GoForNOGO",jsonObject.getJSONObject("LOAN_CATEGORY").get("result"));
			breData.put(breData16);	
		}
		if(jsonObject.has("NO_WILFUL_DEFAULT_ACCOUNTS_RETAIL")) {
			JSONObject breData17=new JSONObject();
			breData17.put("Parameter","NO_WILFUL_DEFAULT_ACCOUNTS_RETAIL");
			breData17.put("outcome",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_RETAIL").getJSONObject("derived_values").get("COUNT_WILFUL_DEFAULT_ACCOUNTS_RETAIL"));
			breData17.put("Deviation",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_RETAIL").get("deviation"));
			breData17.put("GoForNOGO",jsonObject.getJSONObject("NO_WILFUL_DEFAULT_ACCOUNTS_RETAIL").get("result"));
			breData.put(breData17);	
		}
		if(jsonObject.has("AML_SCORE_MATCH_VIKAS_LOAN")) {
			JSONObject breData18=new JSONObject();
			breData18.put("Parameter","AML_SCORE_MATCH_VIKAS_LOAN");
			breData18.put("outcome",jsonObject.getJSONObject("AML_SCORE_MATCH_VIKAS_LOAN").getJSONObject("derived_values").get("AML_SCORE_VALUE"));
			breData18.put("Deviation",jsonObject.getJSONObject("AML_SCORE_MATCH_VIKAS_LOAN").get("deviation"));
			breData18.put("GoForNOGO",jsonObject.getJSONObject("AML_SCORE_MATCH_VIKAS_LOAN").get("result"));
			breData.put(breData18);	
		}
		if(jsonObject.has("NUM_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD")) {
			JSONObject breData19=new JSONObject();
			breData19.put("Parameter","NUM_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD");
			breData19.put("outcome",jsonObject.getJSONObject("NUM_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD").getJSONObject("derived_values").get("COUNT_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD"));
			breData19.put("Deviation",jsonObject.getJSONObject("NUM_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD").get("deviation"));
			breData19.put("GoForNOGO",jsonObject.getJSONObject("NUM_RETAIL_ACCOUNTS_WITH_1_PLUS_DPD").get("result"));
			breData.put(breData19);	
		}
		if(jsonObject.has("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC")) {
			JSONObject breData20=new JSONObject();
			breData20.put("Parameter","NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC");
			breData20.put("outcome",jsonObject.getJSONObject("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC").getJSONObject("derived_values").get(""));
			breData20.put("Deviation",jsonObject.getJSONObject("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC").get("deviation"));
			breData20.put("GoForNOGO",jsonObject.getJSONObject("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC").get("result"));
			breData.put(breData20);	
		}
		if(jsonObject.has("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC")) {
			JSONObject breData20=new JSONObject();
			breData20.put("Parameter","NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC");
			breData20.put("outcome","0");
			breData20.put("Deviation",jsonObject.getJSONObject("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC").get("deviation"));
			breData20.put("GoForNOGO",jsonObject.getJSONObject("NUM_ACCOUNTS_ANY_DPD_LAST_3_MONTHS_EXCEPT_GL_KCC_CC").get("result"));
			breData.put(breData20);	
		}
		data.put("BREData",breData);
		Data.put("Data",data.toString());
		
		return Data;
	}
}
