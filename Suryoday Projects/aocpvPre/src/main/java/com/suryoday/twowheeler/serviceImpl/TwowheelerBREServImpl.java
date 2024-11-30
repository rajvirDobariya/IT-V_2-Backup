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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.repository.TwowheelerFamilyMemberRepository;
import com.suryoday.twowheeler.service.TwowheelerBREService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Component
public class TwowheelerBREServImpl implements TwowheelerBREService{
	
	private static Logger logger = LoggerFactory.getLogger(TwowheelerBREServImpl.class);
	
	@Autowired
	TwowheelerFamilyMemberRepository familymemberrepo;
	
	@Autowired
	TwowheelerFamilyMemberService familymemberservice;

	@Override
	public JSONObject fetchBre(JSONObject header, String leadId) {
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

			e.getMessage();
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
			System.out.println(sendAuthenticateResponse);
			
		} else {
			logger.debug("POST request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
			System.out.println(sendAuthenticateResponse);
		}

		return sendAuthenticateResponse;

	}

	@Override
	public JSONObject createLead(String applicationNo,String member, JSONObject header) {
//		JSONObject request=getRequest(applicationNo,member);
		JSONObject createlead = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject res = new JSONObject();

		data.put("is_success", true);
		data.put("data", res);
		res.put("loan_account_number", "D100026");
		res.put("lead_id", "b7ea25d0-f4e1-4ce6-b848-d68f626b9117");
		createlead.put("data", data.toString());
		return createlead;
	}

	private JSONObject getRequest(String applicationNo,String member) {
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
		
		TwoWheelerFamilyMember fetchByAppNo = familymemberservice.getByApplicationNoAndMember(applicationNo,member);
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
		if(fetchByAppNo.getEkycResponse() == null) {
	 	   }
	 	   else {
		org.json.JSONObject ekyc=new org.json.JSONObject(fetchByAppNo.getEkycResponse());
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
		 	FATHER_NAME=split[1];
		 	gender=PoiResponse.getString("gender"); 
	 	   }
		String address=new String(address1+","+city+","+state);
		if(fetchByAppNo.getPanCard()!=null) {
			String panCardResponse = fetchByAppNo.getPanCardResponse();
			JSONObject panResponse=new JSONObject(panCardResponse);
			JSONArray panDetails = panResponse.getJSONObject("Data").getJSONArray("PANDetails");
			JSONObject pandetail = panDetails.getJSONObject(0);
			kyc1.put("kyc_type", 2);
			kyc1.put("identifier", "BUXPS2681D");
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
		else if(fetchByAppNo.getVoterId()!=null)
		{
			String voterIdResponse = fetchByAppNo.getVoterIdResponse();
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
		request.put("mobile_number", fetchByAppNo.getMobile());
	//	request.put("partner_loan_id", fetchByApplicationId.getApplicationId());
		int max=1000000000;
		int min=100000000;
		double random = Math.random()*(max-min+1)+min; 
		request.put("partner_loan_id", fetchByAppNo.getAppNoWithProductCode());
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
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	      LocalDate dob1 = LocalDate.parse(dateofBirth, formatter);
				 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			   	String dob2= dob1.format(formatter1);
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
		LocalDateTime createts = fetchByAppNo.getCreatedTimestamp();
//		 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		  
		request.put("loan_application_date", createts.format(formatter1));
		request.put("loan_tenure","");
		request.put("loan_amount", Long.parseLong(fetchByAppNo.getRequiredAmount()));
		request.put("loan_type", 5);

		request.put("interest_rate",28 );
		request.put("payment_frequency", 3);
	

		
		//request.put("additional_variables", additional_variables);
		logger.debug(request.toString());
		return request;

	}

	@Override
	public JSONObject breRules(String applicationNo) {
		JSONObject Data=new JSONObject();
		JSONObject data=new JSONObject();
		JSONArray breData=new JSONArray();
		JSONObject breData1=new JSONObject();
		breData1.put("Parameter","NO_SUITFIELD_ACCOUNTS");
		breData1.put("Variable","0");
		breData1.put("Deviation","False");
		breData1.put("GoForNOGO","Go");
		breData.put(breData1);
		JSONObject breData2=new JSONObject();
		breData2.put("Parameter","OLDEST_MFI_ACCOUNT");
		breData2.put("Variable","37");
		breData2.put("Deviation","False");
		breData2.put("GoForNOGO","Go");
		breData.put(breData2);
		JSONObject breData3=new JSONObject();
		breData3.put("Parameter","OLDEST_RETAIL_ACCOUNT");
		breData3.put("Variable","37");
		breData3.put("Deviation","False");
		breData3.put("GoForNOGO","Go");
		breData.put(breData3);
		JSONObject breData4=new JSONObject();
		breData4.put("Parameter","MAX_90_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI");
		breData4.put("Variable","0");
		breData4.put("Deviation","False");
		breData4.put("GoForNOGO","Go");
		breData.put(breData4);
		JSONObject breData5=new JSONObject();
		breData5.put("Parameter","EXISTING_MFI_CUSTOMER");
		breData5.put("Variable","1");
		breData5.put("Deviation","False");
		breData5.put("GoForNOGO","NoGo");
		breData.put(breData5);
		JSONObject breData6=new JSONObject();
		breData6.put("Parameter","NO_LOSS_ACCOUNTS");
		breData6.put("Variable","2");
		breData6.put("Deviation","False");
		breData6.put("GoForNOGO","Go");
		breData.put(breData6);
		JSONObject breData7=new JSONObject();
		breData7.put("Parameter","NO_SUBSTANDARD_ACCOUNTS");
		breData7.put("Variable","0");
		breData7.put("Deviation","False");
		breData7.put("GoForNOGO","Go");
		breData.put(breData7);
		JSONObject breData8=new JSONObject();
		breData8.put("Parameter","NO_RESTRUCTURED_ACCOUNTS");
		breData8.put("Variable","0");
		breData8.put("Deviation","False");
		breData8.put("GoForNOGO","Go");
		breData.put(breData8);
		JSONObject breData9=new JSONObject();
		breData9.put("Parameter","NO_SMA_ACCOUNTS");
		breData9.put("Variable","0");
		breData9.put("Deviation","False");
		breData9.put("GoForNOGO","Go");
		breData.put(breData9);
		JSONObject breData10=new JSONObject();
		breData10.put("Parameter","NO_DOUBTFUL_ACCOUNTS");
		breData10.put("Variable","0");
		breData10.put("Deviation","False");
		breData10.put("GoForNOGO","Go");
		breData.put(breData10);
		JSONObject breData11=new JSONObject();
		breData11.put("Parameter","NO_WILFUL_DEFAULT_ACCOUNTS");
		breData11.put("Variable","0");
		breData11.put("Deviation","False");
		breData11.put("GoForNOGO","NoGo");
		breData.put(breData11);
		JSONObject breData12=new JSONObject();
		breData12.put("Parameter","NO_WRITTEN_OFF_ACCOUNTS");
		breData12.put("Variable","0");
		breData12.put("Deviation","False");
		breData12.put("GoForNOGO","Go");
		breData.put(breData12);
		JSONObject breData13=new JSONObject();
		breData13.put("Parameter","MAX_30_PLUS_DPD_LAST_12_MONTHS_RETAIL_MFI");
		breData13.put("Variable","0");
		breData13.put("Deviation","False");
		breData13.put("GoForNOGO","NoGo");
		breData.put(breData13);
		JSONObject breData14=new JSONObject();
		breData14.put("Parameter","MAX_0_PLUS_DPD_LAST_3_MONTHS_RETAIL_MFI");
		breData14.put("Variable","0");
		breData14.put("Deviation","False");
		breData14.put("GoForNOGO","Go");
		breData.put(breData14);
		data.put("BREData",breData);
		Data.put("Data",data);
		return Data;
	}

	@Override
	public JSONObject equifaxReport(String applicationNo) {
		TwoWheelerFamilyMember twfamilymember = familymemberservice.getByApplicationNoAndMember(applicationNo,"APPLICANT");
		String FIRST_NAME="";
		String MIDDLE_NAME="";
		String LAST_NAME="";
		String MOBILE=twfamilymember.getMobile();
		String DOB="";
		String AGE="";
		String GENDER="";
		if(twfamilymember.getEkycResponse() != null) {
	 		  org.json.JSONObject ekyc=new org.json.JSONObject(twfamilymember.getEkycResponse());
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
		aadhar1.put("IdNumber",twfamilymember.getAadharCard());
		aadhar1.put("seq","2");
		PANId1.put("ReportedDate",LocalDate.now());
		PANId1.put("IdNumber",twfamilymember.getPanCard());
		PANId1.put("seq","1");
		PANId.put(PANId1);
		aadhar.put(aadhar1);
		identityInfo.put("PANId",PANId);
		identityInfo.put("AAdharCard",aadhar);
		idConInfo.put("IdentityInfo",identityInfo);
		JSONArray phoneInfo=new JSONArray();
		   JSONObject phoneInfo1=new JSONObject();
	        phoneInfo1.put("ReportedDate",LocalDate.now());
	        phoneInfo1.put("Number",twfamilymember.getMobile());
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


}
