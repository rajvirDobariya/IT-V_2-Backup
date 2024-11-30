package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.AocpvImageRepository;
import com.suryoday.aocpv.repository.AocpvLoanCreationRepo;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.ROAOCPVLoanCreationService;

@Component
public class ROAOCPVLoanCreationServImpl implements ROAOCPVLoanCreationService {
	private static Logger logger = LoggerFactory.getLogger(ROAOCPVLoanCreationServImpl.class);

	@Autowired
	AocpvLoanCreationRepo aocpvloancreationrepo;
	@Autowired
	AocpvImageRepository aocpvimagerepo;

	@Autowired
	AocpCustomerDataRepo aocpcustomerdatarepo;

	@Override
	public JSONObject loanCreation(String applicationNo, JSONObject header) {
		JSONObject request = getRequest(applicationNo);
		JSONObject sendResponse = new JSONObject();

		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			obj = new URL(x.BrNetconnect + "BRConnectClientNew/v1/BrNetconnect");
			logger.debug(x.BrNetconnect + "BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", header.getString("Authorization"));

			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(String applicationNo) {
//		JSONObject Data=new JSONObject();
		long appNo = Long.parseLong(applicationNo);
		Optional<AocpvLoanCreation> getByApplicationNo = aocpvloancreationrepo.getByApplicationNo(appNo);
		Optional<AocpCustomer> getByApp = aocpcustomerdatarepo.getByApp(appNo);
		AocpCustomer aocpCustomer = getByApp.get();
		AocpvLoanCreation aocpvLoanCreation = getByApplicationNo.get();
		String accountData = aocpvLoanCreation.getAccountData();
		String productType = "";
		String productCategory = "";
		String repaymentType = "";
		String disbursementType = "";
		if (accountData == null || accountData.isEmpty()) {
		} else {
			org.json.JSONObject accountdetails = new org.json.JSONObject(accountData);
			productType = accountdetails.getString("productType");
			productCategory = accountdetails.getString("productCategory");
			repaymentType = accountdetails.getString("repaymentType");
			disbursementType = accountdetails.getString("disbursementType");
		}
		String branchid = aocpCustomer.getBranchid().substring(2);
		LocalDate createddate = aocpCustomer.getCreationDate();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String creationdate = createddate.format(formatter1);

		JSONObject data = new JSONObject();
		data.put("Method", "POSTINDVLOANAPPLICATIONCREATION");
//		data.put("BranchId",branchid);
		data.put("BranchId", "000");
		data.put("ApplicationDate", creationdate);
		data.put("WFAdvTypeID", "ILNR");
		data.put("IndividualLoanDetails", "<IndividualLoanDetails><ClientBranchID>" + branchid
				+ "</ClientBranchID><ClientID>" + aocpvLoanCreation.getClientId()
				+ "</ClientID><ProductID>IL14</ProductID><PurposeCategoryID>01</PurposeCategoryID><PurposeCodeID>02</PurposeCodeID><CreditOfficerID>11009</CreditOfficerID><LoanAmount>"
				+ aocpvLoanCreation.getSanctionedLoanAmount() + "</LoanAmount><CurrencyID></CurrencyID><LoanTerm>"
				+ aocpvLoanCreation.getTenure().substring(0, 2)
				+ "</LoanTerm><NoOfDisbursements>3</NoOfDisbursements><DisbursementDate>04/29/2021</DisbursementDate><DisbursementModeID>CH</DisbursementModeID><RepaymentModeID>NACH</RepaymentModeID><FileNumber></FileNumber><ExternalID>Test</ExternalID></IndividualLoanDetails>");
		data.put("InsuranceDetails",
				"<InsuranceDetails><InsuranceDetail><ChargeID>ILS</ChargeID><InsuredForID>R</InsuredForID><RelationID>S</RelationID><RelationName>Test</RelationName><RelationRefNo>1</RelationRefNo><NomineeName></NomineeName><NomineeAge></NomineeAge><NomineeAgeAsOn></NomineeAgeAsOn><NomineeRelationID></NomineeRelationID><MemberIdentificationNo>45</MemberIdentificationNo></InsuranceDetail><InsuranceDetail><ChargeID>ILS</ChargeID><InsuredForID>C</InsuredForID><RelationID></RelationID><RelationName></RelationName><RelationRefNo></RelationRefNo><NomineeName>Trusha</NomineeName><NomineeAge>25</NomineeAge><NomineeAgeAsOn>03/09/2021</NomineeAgeAsOn><NomineeRelationID>M</NomineeRelationID><MemberIdentificationNo>12</MemberIdentificationNo></InsuranceDetail></InsuranceDetails>");
		data.put("CollateralDetail", "");
		data.put("CollateralMoreDetail", "");
		data.put("CollateralDocumentDetails", "");
		data.put("LoanDisbScheduleDet", "");
		data.put("PSLDetails", "");
		data.put("NetOffAccountDetails", "");
		data.put("DocumentDetails", "");
		data.put("CoApplicantDetails", "");
		data.put("UserDefinedField", "");

//		Data.put("Data",data);
		System.out.println(data);
		return data;
	}

	private static JSONObject getResponse(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

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
			logger.debug("GET request not worked");

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
	public JSONObject statuscheck(String branchId, String applicationId, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(branchId, applicationId);
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			obj = new URL(x.BrNetconnect + "BRConnectClientNew/v1/BrNetconnect");
			logger.debug(x.BrNetconnect + "BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", header.getString("Authorization"));

			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(String branchId, String applicationId) {
		JSONObject parent = new JSONObject();
		parent.put("Method", "GetLoanApplicationStatus");
		parent.put("OurBranchID", branchId);
		parent.put("ApplicationID", applicationId);
		return parent;
	}

}
