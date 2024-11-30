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
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.BankDetailsResponse;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;
import com.suryoday.twowheeler.service.DisbursementApiService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerLoanCreationService;

@Component
public class DisbursementApiServImpl implements DisbursementApiService {
	private static Logger logger = LoggerFactory.getLogger(DisbursementApiServImpl.class);

	@Autowired
	TwowheelerDetailsService twowheelerdetailsservice;

	@Autowired
	TwowheelerLoanCreationService twowheelerloancreationservice;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@Override
	public JSONObject disbursement(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject disbRequest = getDisbursementRequest(applicationNo);

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
			obj = new URL(x.BASEURL + "asset/disbursement/v3?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "asset/disbursement/v3?api_key=" + x.api_key);
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			sendResponse = getResponse(disbRequest, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}
		sendResponse.put("request", disbRequest);
		return sendResponse;
	}

	private JSONObject getDisbursementRequest(String applicationNo) {
		TwowheelerDetailesTable twowheelerDetailesTable = twowheelerdetailsservice.getByApplication(applicationNo);
		TwowheelerLoanCreation twowheelerloancreation = twowheelerloancreationservice.getbyApplicationNo(applicationNo);
		String processingFee = "";
		String taxAmount1 = "";
		String docCharges = "";
		String taxAmount2 = "";
		if (twowheelerDetailesTable.getLoanCharges() != null) {
			org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
			for (int n = 0; n < loanCarges.length(); n++) {
				JSONObject jsonObject = loanCarges.getJSONObject(n);
				if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
					processingFee = jsonObject.getString("chargeAmount");
					taxAmount1 = jsonObject.getString("taxAmount");
				} else if (jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge")) {
					docCharges = jsonObject.getString("chargeAmount");
					taxAmount2 = jsonObject.getString("taxAmount");
				}
			}
		}
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime createdTimestamp = twowheelerDetailesTable.getCreatedTimestamp();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String disbursementDate = now.format(formatter);
		String messageDate = createdTimestamp.format(formatter);

		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("ApplicationID", "PRE" + applicationNo);
		data.put("MessageDate", messageDate);
		data.put("MessageType", "DISAMT");
		data.put("MessageData", "");
		data.put("FinalDisb", "Y");
		data.put("DisbursementDate", disbursementDate);
		data.put("LoanAccountNumber", twowheelerloancreation.getLoanAccoutNumber());
//			data.put("DisbTargetAccount",twowheelerDetailesTable.getBeneficiaryAccountNo());
		data.put("DisbAmount", twowheelerloancreation.getDisbursalAmount());
		data.put("SchmCode", "5019");
		data.put("TransactionComment", "DISBURSEMENT PRE" + applicationNo);
		data.put("DisbursementMode", "GL");
		if (!processingFee.equals("0.0") && !processingFee.equals("") && !processingFee.equals("0")) {
			data.put("ProcessingFees", processingFee + "#" + taxAmount1);
		}

		data.put("LifeInsuraceFee", twowheelerDetailesTable.getInsuranceEmi());
//			data.put("GenInsuranceFee","");
//			data.put("StampDuty","");
		if (!docCharges.equals("0.0") && !docCharges.equals("") && !docCharges.equals("0")) {
			data.put("DocCharges", docCharges + "#" + taxAmount2);
		}
//			data.put("OtherCharges1","");
//			data.put("OtherCharges2","");
//			data.put("OtherCharges3","");
		data.put("BeneficiaryName", twowheelerDetailesTable.getBeneficiaryName());
		data.put("TransferMode", "NA");
		data.put("BeneficiaryAccountNumber", twowheelerDetailesTable.getBeneficiaryAccountNo());
		data.put("BeneficiaryIfscCode", twowheelerDetailesTable.getBeneficiaryIFSC());
		parent.put("Data", data);
		logger.debug("request" + parent.toString());
		System.out.println(parent);
		return parent;
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
	public JSONObject collateralDetails(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getCollateralRequest(applicationNo);
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
			obj = new URL(x.BASEURL + "createOrUpdate/collateralDetails/v2?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "createOrUpdate/collateralDetails/v2?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "OTHERS");
			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}
		sendResponse.put("request", request);
		return sendResponse;
	}

	private JSONObject getCollateralRequest(String applicationNo) {
		TwowheelerDetailesTable twowheelerDetailesTable = twowheelerdetailsservice.getByApplication(applicationNo);
		TwowheelerLoanCreation twloanCreation = twowheelerloancreationservice.getbyApplicationNo(applicationNo);
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String date = now.format(formatter);
		LocalDate loanCreationDate = twloanCreation.getLoanCreationDate();
		String loanCreationDateInString = loanCreationDate.format(formatter);
		data.put("AccountID", twloanCreation.getLoanAccoutNumber());
		data.put("CollateralID", "");
		data.put("CollateralType", "30");
		data.put("CollateralCode", "10");
		data.put("RequestType", "0");
		JSONObject input1 = new JSONObject();
		input1.put("SecurityFlag", "1");
		input1.put("TypeOfCharge", "002");
		input1.put("Make", twowheelerDetailesTable.getManufacture());
		input1.put("Model", twowheelerDetailesTable.getModel());
		input1.put("ChassisNumber", twowheelerDetailesTable.getChasisNumber());
		input1.put("EngineNumber", twowheelerDetailesTable.getEngineNumber());
		input1.put("HorsePower", "");
		input1.put("DateOfHypothecation", date);
		input1.put("DateOfPurchase", loanCreationDateInString);
		input1.put("YearOfManufacture", twowheelerDetailesTable.getYearOfManufacturer());
		input1.put("BodyStyle", "");
		input1.put("TitledOwner", twowheelerDetailesTable.getName());
//			input1.put("RegistryNumber","12345");
		input1.put("CurrencyCode", "INR");
		input1.put("InsuranceCompany", "HDFC Life");
		data.put("Input1", input1);
		JSONObject input2 = new JSONObject();
		input2.put("DerivePledgeValue", "I");
//			input2.put("MarketValue",null);
//			input2.put("AssessedValue",twowheelerDetailesTable.getTotalOnRoadPrice());
		input2.put("InvoiceValue", twowheelerDetailesTable.getTotalOnRoadPrice());
//			input2.put("WrittenDownValue","");
		input2.put("AppraisalDate", date);
		double onRoadPrice = Double.parseDouble(twowheelerDetailesTable.getTotalOnRoadPrice());
		double marginMoney = Double.parseDouble(twowheelerDetailesTable.getMarginMoney());
		double marginPercent = (marginMoney * 100) / onRoadPrice;
		double roundOffMarginPercent = Math.round(marginPercent * 100.0) / 100.0;
		input2.put("Margin", roundOffMarginPercent);
		input2.put("ReviewFrequency", "3YAE");
		input2.put("ReviewLastDate", date);
		data.put("Input2", input2);
		JSONObject input3 = new JSONObject();
		double amount = Double.parseDouble(twowheelerDetailesTable.getAmount());
		double maximumPledgeAmount = (amount * 80.00) / 100.00;
		input3.put("MaximumPledgeAmount", Double.toString(maximumPledgeAmount));
		data.put("Input3", input3);
		parent.put("Data", data);
		return parent;
	}

	@Override
	public JSONObject loanCreation(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getLoanCreationRequest(applicationNo, header);
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
			obj = new URL(x.BASEURL + "asset/creation/v3?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "asset/creation/v3?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "HOT");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "OTHERS");
			con.setRequestProperty("cache-control", "no-cache");
			con.setRequestProperty("Postman-Token", "88114120-e190-4682-9cce-665b614049cd");
			sendResponse = getResponse(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.getMessage();
		}
		sendResponse.put("request", request);
		return sendResponse;
	}

	private JSONObject getLoanCreationRequest(String applicationNo, JSONObject header) {
		TwowheelerDetailesTable twowheelerdetails = twowheelerdetailsservice.getByApplication(applicationNo);
		TwoWheelerFamilyMember familyMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo,
				"APPLICANT");
		String processingFee = "";
		LocalDateTime createdTimestamp = twowheelerdetails.getCreatedTimestamp();
		String rateOfInterest = twowheelerdetails.getRateOfInterest();

		if (twowheelerdetails.getLoanCharges() != null) {
			org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerdetails.getLoanCharges());
			for (int n = 0; n < loanCarges.length(); n++) {
				JSONObject jsonObject = loanCarges.getJSONObject(n);
				if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
					processingFee = jsonObject.getString("totalAmount");
				}
			}
		}
		LocalDate today = LocalDate.now();
		int dayOfMonth = today.getDayOfMonth();
		LocalDate emiStartDate = null;
		if (dayOfMonth >= 15)
			if (dayOfMonth >= 15) {
				emiStartDate = today.plusMonths(2).withDayOfMonth(5);
			} else {
				emiStartDate = today.plusMonths(1).withDayOfMonth(5);
			}

		LocalDate sanctiondate = twowheelerdetails.getSanctiondate();
//		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		String dateSanction=sanctiondate.format(formatter);
//		String emiStartDate=today.getYear()+"-0"+monthValue+"-05";
//		LocalDate emiDate=LocalDate.parse(emiStartDate);
		LocalDate maturityDate = emiStartDate.plusMonths(Integer.parseInt(twowheelerdetails.getTenure()));
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("ApplicationId", "PRE" + applicationNo);
		data.put("MessageDate", "" + createdTimestamp.toLocalDate() + "");
		data.put("ProductGroup", "LN");
		data.put("RMCode", twowheelerdetails.getSalesCreatedBy());
		data.put("InstallmentType", "M");
		data.put("GovtSponsoredSchemes", "0");
		data.put("Sanctiondate", "" + sanctiondate + "");
		data.put("DPNDate", "");
		data.put("OccupationCode", familyMember.getOccCode());
		data.put("MOBBranchCode", header.getString("X-Branch-ID"));
		data.put("CreditLimitMaturityDate", "" + maturityDate + "");
		data.put("Subsectorcode", "NAO");
		data.put("CurrencyCode", "INR");
		data.put("SanctionDeptCode", "13");
		data.put("SectorCode", "O");
		data.put("CustomerNumber", twowheelerdetails.getCustomerId());
		data.put("InterestIndex", "");
		data.put("MessageType", "NEWLN");
		data.put("Dateofnote", "" + LocalDate.now() + "");
		data.put("DistrictCode", "");
		data.put("SchmCode", "TWPTL");
		data.put("LimitId", "");
		data.put("CredtBureauCode", "51");
		data.put("Priority_NonPriorityFlag", "N");
		data.put("IndustryCode", "999");
		data.put("CustRelRelationshipCode", "70");
		data.put("AccountRelationshipCode", "A");
		data.put("AcctDrPrefRate", "");
		data.put("TypeOfAdvan", "");
		data.put("Term", twowheelerdetails.getTenure() + "M");
		data.put("InstallmentFrequencyInterest", "5M");
		data.put("BalloonPayment", "");
		data.put("NatureofBorrower", "99");
		data.put("OragnizationCode", "82");
		data.put("RePaymntMthd", "E");
		data.put("SegmentCode", "6");
		data.put("Sanctionamount", twowheelerdetails.getAmount());
		data.put("InstallmentFrequencyPrincipal", "5M");
		data.put("CheckRequired", "N");
		data.put("ModofAdvan", "OTH-Other");
		data.put("SpecialCategoryCode", "6");
		if (twowheelerdetails.getAccountBranchId() == null) {
			if (twowheelerdetails.getBankDetails() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<BankDetailsResponse> reference = objectMapper.readValue(twowheelerdetails.getBankDetails(),
							new TypeReference<List<BankDetailsResponse>>() {
							});
					BankDetailsResponse bankDetailsResponse = reference.stream()
							.filter(bankResp -> bankResp.getRepaymentType().equals("YES")).findAny().get();

					data.put("BranchCode", bankDetailsResponse.getAccountBranchId());
					data.put("AutoPaymentAcct", bankDetailsResponse.getAccountNumber());
					data.put("AccountTitle", bankDetailsResponse.getAccountName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {

			if (twowheelerdetails.getListType().equalsIgnoreCase("PRE")) {
				data.put("BranchCode", twowheelerdetails.getAccountBranchId());
				data.put("AutoPaymentAcct", twowheelerdetails.getAccountNumber());
				data.put("HoldInOperAcct", "Y");
			} else {
				data.put("BranchCode", twowheelerdetails.getSalesBranchId());
				data.put("AutoPaymentAcct", "");
				data.put("HoldInOperAcct", "N");
			}

			data.put("AccountTitle", twowheelerdetails.getAccountName());
		}
		data.put("InterestChangeFrequency", "");
		data.put("NatOfBuss", "12");
		data.put("SourceCode", "");
		data.put("MoratoriumPeriodPrincipal", "");
		data.put("ChannelCode", "TAB");
		data.put("RoleCode", "M");
		data.put("PurposeCode", "1");
		data.put("MoratoriumPeriod", "");
		data.put("InstallStartDate", "" + emiStartDate + "");
		data.put("AmountRequested", twowheelerdetails.getAmount());
		data.put("BenefAcctNo", "");
		data.put("EquatedInst", "Y");
		data.put("InterestRate", rateOfInterest.replace("%", ""));
		data.put("InstStartDate", "" + emiStartDate + "");
		data.put("BenfIFSC", "");
		data.put("SBUCode", "03");
		data.put("StatementFrequency", "5M");
		data.put("LeadGenCode", header.getString("X-User-ID"));
		data.put("MessageData", "");
		data.put("Amount", twowheelerdetails.getAmount());
		data.put("MoratoriumPeriodInterest", "");
		data.put("StateCode", "");
		data.put("BSRPopulationCode", "5");
		data.put("PegReviewDate", "");
		data.put("PaymentTerm", twowheelerdetails.getTenure() + "M");
		data.put("UpfrontProcessingFees", processingFee);
		data.put("BenAcctype", "");
		data.put("RELCustomerNumber", twowheelerdetails.getCustomerId());
		data.put("UnsecuredFlag", "Y");
		data.put("TypOfAcct", "100");
		data.put("AccountOwnershipPercentage", "");
		data.put("MaximumCreditLimit", twowheelerdetails.getAmount());
		data.put("OperateMode", "1");
		data.put("NatOfAdvan", "RET-RETAIL");
		parent.put("Data", data);

		System.out.println(parent);
		return parent;
	}

	@Override
	public JSONObject crmModification(String applicationNo, JSONObject header) {
		TwowheelerDetailesTable twowheelerdetails = twowheelerdetailsservice.getByApplication(applicationNo);
		String customerId = twowheelerdetails.getCustomerId();
		JSONObject req = getRequestCrm(twowheelerdetails);
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
			obj = new URL(x.BASEURL + "customer/" + customerId + "/modify/v2?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "customer/" + customerId + "/modify/v2?api_key=" + x.api_key);
			;
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "CHQ");
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "CHQ");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("Postman-Token", "e5aba7b6-c666-4555-8cc3-cb1e2513e929");
			con.setRequestProperty("cache-control", "no-cache");
			sendResponse = getResponse(req, sendResponse, con, "PUT");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}
		sendResponse.put("request", req);
		return sendResponse;
	}

	private JSONObject getRequestCrm(TwowheelerDetailesTable twowheelerdetails) {
		JSONObject Data = new JSONObject();
		Data.put("CustomerType", "R");
		Data.put("CRMUserId", "34244");
		Data.put("RegisteredMobile", twowheelerdetails.getMobileNo());
		Data.put("BranchCode", twowheelerdetails.getSalesBranchId());
		Data.put("Source", "HOTFOOT");
		Data.put("UserId", "34244");

		System.out.println(Data);
		return Data;
	}

	@Override
	public JSONObject cifCreation(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();

		JSONObject request = getCifRequest(applicationNo);
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
			logger.debug(x.BASEURL + "customer/v2?api_key=" + x.api_key);

			obj = new URL(x.BASEURL + "customer/v2?api_key=" + x.api_key);

			// obj= new
			// URL("https://intramashery.suryodaybank.co.in/ssfbuat/customer/v2?api_key=kyqak5muymxcrjhc5q57vz9v");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "HOTFOOT");
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));

			sendResponse = getResponse(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}
		sendResponse.put("request", request);
		return sendResponse;
	}

	private JSONObject getCifRequest(String applicationNo) {

		TwowheelerDetailesTable twowheelerDetailesTable = twowheelerdetailsservice.getByApplication(applicationNo);
		TwoWheelerFamilyMember familyMember = familyMemberService.getByApplicationNoAndMember(applicationNo,
				"APPLICANT");
		JSONObject Data = new JSONObject();

		JSONObject data = new JSONObject();
		org.json.simple.JSONObject individual = new org.json.simple.JSONObject();

		individual.put("Type", "Retail Business");
		individual.put("BranchId", twowheelerDetailesTable.getSalesBranchId());
		individual.put("NamePrefix", "1");
		individual.put("FirstName", familyMember.getFirstName());
		individual.put("MiddleName", familyMember.getMiddleName());
		individual.put("LastName", familyMember.getLastName());
		individual.put("MotherNamePrefix", "Mrs.");
		individual.put("MotherMaidenName", " ");
		individual.put("MotherMiddleName", "");
		individual.put("MotherLastName", "");
		individual.put("FatherNamePrefix", "");
		individual.put("FatherName", "");
		individual.put("FatherMiddleName", "");
		individual.put("FatherLastName", "");
		individual.put("Caste", familyMember.getCasteCode());
		individual.put("Religion", familyMember.getReligionCode());
		individual.put("MaritalStatus", familyMember.getMarriedCode());
		individual.put("SpouseNamePrefix", "");
		individual.put("SpouseName", "");
		individual.put("SpouseMiddleName", "");
		individual.put("SpouseLastName", "");
		// char charAt = aocpvIncomeDetails.getGender().charAt(0);
		individual.put("Gender", familyMember.getGenderCode());
		org.json.simple.JSONObject dateandplaceofbirth = new org.json.simple.JSONObject();
		String dob = familyMember.getDob();// 03-07-1997
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate birthDt = LocalDate.parse(dob, formatter);
		dateandplaceofbirth.put("BirthDt", birthDt);
		dateandplaceofbirth.put("CityOfBirth", "");
		dateandplaceofbirth.put("CountryOfBirth", "IN");
		individual.put("DateAndPlaceOfBirth", dateandplaceofbirth);
		individual.put("ResidentialStatus", "1");
		org.json.simple.JSONObject ccm = new org.json.simple.JSONObject();
		ccm.put("MinorityCode", "11");
		individual.put("CCM", ccm);
		org.json.simple.JSONObject verifier = new org.json.simple.JSONObject();
		verifier.put("Name", twowheelerDetailesTable.getRmName());
		verifier.put("Id", twowheelerDetailesTable.getSalesCreatedBy());
		verifier.put("OrganizationName", "Suryoday Small Finance Bank");
		verifier.put("Designation", "RM");
		verifier.put("VerificationDate", LocalDate.now());
		verifier.put("CKYCApplicationType", "New");
		verifier.put("OrganizationCode", "IN1906");
		verifier.put("CKYCAccountType", "1");
		verifier.put("VerificationDoctype", "");
		individual.put("Verifier", verifier);
		org.json.simple.JSONArray document = new org.json.simple.JSONArray();
		org.json.simple.JSONObject document1 = new org.json.simple.JSONObject();
		document1.put("Type", "PAN");
		document1.put("IdentityNumber", familyMember.getPanCard());
		document.add(document1);
		org.json.simple.JSONObject document2 = new org.json.simple.JSONObject();
		document2.put("Type", "AADHAR");
		document2.put("IdentityNumber", familyMember.getAadharCard());
		document.add(document2);
		individual.put("Document", document);
		org.json.simple.JSONObject contactDetails = new org.json.simple.JSONObject();
		org.json.simple.JSONArray contactdetails = new org.json.simple.JSONArray();
		org.json.simple.JSONObject contactDetails1 = new org.json.simple.JSONObject();

		org.json.simple.JSONObject postalAddress = new org.json.simple.JSONObject();
		org.json.simple.JSONObject contactDetails2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject postalAddress1 = new org.json.simple.JSONObject();
		String address1 = twowheelerDetailesTable.getAddress();
		if (address1 != null) {
			org.json.JSONArray addressInJson = new org.json.JSONArray(address1);
			for (int n = 0; n < addressInJson.length(); n++) {
				JSONObject jsonObject2 = addressInJson.getJSONObject(n);
				String addressType = jsonObject2.getString("addressType");
				if (addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
					addressType = "R";
					postalAddress.put("Type", addressType);
					postalAddress.put("Address1", jsonObject2.getString("address_Line1"));
					postalAddress.put("Address2", jsonObject2.getString("address_Line2"));
					postalAddress.put("Address3", jsonObject2.getString("address_Line3"));
					postalAddress.put("Address4", "");
					postalAddress.put("Taluka", "");
					postalAddress.put("Landmark", jsonObject2.getString("landmark"));
					postalAddress.put("District", jsonObject2.getString("district"));
					postalAddress.put("PostCode", jsonObject2.getString("pincode"));
					postalAddress.put("City", jsonObject2.getString("city"));
					postalAddress.put("State", jsonObject2.getString("state"));
					postalAddress.put("Country", "IN");
					org.json.simple.JSONObject geoLocation = new org.json.simple.JSONObject();
					geoLocation.put("Latitude", "");
					geoLocation.put("Longitude", "");
					postalAddress.put("GeoLocation", geoLocation);
				} else if (addressType.equalsIgnoreCase("Permanent ADDRESS")) {
					addressType = "P";
					postalAddress1.put("Type", addressType);
					postalAddress1.put("Address1", jsonObject2.getString("address_Line1"));
					postalAddress1.put("Address2", jsonObject2.getString("address_Line2"));
					postalAddress1.put("Address3", jsonObject2.getString("address_Line3"));
					postalAddress1.put("Address4", "");
					postalAddress1.put("Taluka", "");
					postalAddress1.put("Landmark", jsonObject2.getString("landmark"));
					postalAddress1.put("District", jsonObject2.getString("district"));
					postalAddress1.put("PostCode", jsonObject2.getString("pincode"));
					postalAddress1.put("City", jsonObject2.getString("city"));
					postalAddress1.put("State", jsonObject2.getString("state"));
					postalAddress1.put("Country", "IN");
					org.json.simple.JSONObject geoLocation1 = new org.json.simple.JSONObject();
					geoLocation1.put("Latitude", "");
					geoLocation1.put("Longitude", "");
					postalAddress1.put("GeoLocation", geoLocation1);

				}

			}
		}
		contactDetails1.put("PostalAddress", postalAddress);
		contactDetails2.put("PostalAddress", postalAddress1);
		contactdetails.add(contactDetails1);

		contactdetails.add(contactDetails2);

		contactDetails.put("ContactDetails", contactdetails);
		individual.put("ContactDetails", contactDetails);
		individual.put("CKYCDateOfDeclaration", "");
		individual.put("SMSRegistration", "");
		individual.put("SMSCustomer", "1");
		individual.put("EducationQualification", familyMember.getEducationCode());
		individual.put("Form60", "");
		individual.put("KYCType", "");
		org.json.simple.JSONObject party = new org.json.simple.JSONObject();
		party.put("EmailAddress", "");
		party.put("Occupation", familyMember.getOccCode());
		party.put("AnnualNetIncome", "");
		party.put("MobileNumber", familyMember.getMobile());
		party.put("Occupation", "");
		party.put("CommunicationAddressType", "P");
		party.put("EmployeeId", "");
		individual.put("Party", party);
		org.json.simple.JSONObject introducer = new org.json.simple.JSONObject();
		org.json.simple.JSONArray postaladdress = new org.json.simple.JSONArray();
		introducer.put("PostalAddress", postaladdress);
		introducer.put("CountryOfResidence", "");
		introducer.put("IdentificaitonType", "");
		individual.put("Introducer", introducer);
		individual.put("CustomerCode", "1");
		individual.put("InternetBanking", "");
		individual.put("MobileBankingFlag", "");
		individual.put("EmailStatement", "");
		individual.put("AddressProof", "");
		individual.put("KYCLastModifiedDate", "");
		individual.put("PermanentAddressType", "");
		individual.put("PermanentAddressProofType", "");
		individual.put("PermanentAddressProofNumber", "");
		individual.put("MailingAddressType", "");
		individual.put("MailingPermanentAddressFlag", "");
		individual.put("MailingAddressProofType", "");
		individual.put("MailingAddressProofNumber", "");
		individual.put("FATCACompliance", "0");
		individual.put("Source", "IEXCEED");
		individual.put("PoliticallyExposedPerson", "");
		individual.put("CountryOfResidence", "IN");
		individual.put("SBUCode", "03");
		individual.put("EmailStatementFrequency", "");
		org.json.simple.JSONArray identificationType = new org.json.simple.JSONArray();
		org.json.simple.JSONObject identificationType1 = new org.json.simple.JSONObject();
		identificationType1.put("IdentityType", "PAN");
		identificationType1.put("IdentityNumber", familyMember.getPanCard());
		identificationType.add(identificationType1);
		org.json.simple.JSONObject identificationType2 = new org.json.simple.JSONObject();
		identificationType2.put("IdentityType", "AADHAR");
		identificationType2.put("IdentityNumber", familyMember.getAadharCard());
		identificationType.add(identificationType2);
		org.json.simple.JSONObject identificationType3 = new org.json.simple.JSONObject();
		identificationType3.put("Category", "ADDRESSPROOF");
		identificationType3.put("IdentityType", "AADRCD");
		identificationType.add(identificationType3);
		individual.put("IdentificationType", identificationType);
		individual.put("UserId", twowheelerDetailesTable.getSalesCreatedBy());
		individual.put("DND", "");
		data.put("Individual", individual);
		Data.put("Data", data);
		System.out.println(Data);
		logger.debug("cif request" + Data.toString());

		return Data;
	}

	@Override
	public JSONObject spdc(TwowheelerLoanCreation twloanCreation, JSONObject header) {
		JSONObject request = getSpdcRequest(twloanCreation);
		twloanCreation.setSpdcRequest(request.toString());
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
			obj = new URL(x.BASEURL + "Asset/SPDCManagement/" + twloanCreation.getLoanAccoutNumber()
					+ "/v2?Operation=Add&api_key=" + x.api_key + "&=null");
			logger.debug(x.BASEURL + "Asset/SPDCManagement/" + twloanCreation.getLoanAccoutNumber()
					+ "/v2?Operation=Add&api_key=" + x.api_key + "&=null");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "HOTFOOT");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));

			sendResponse = getResponse(request, sendResponse, con, "PUT");

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	private JSONObject getSpdcRequest(TwowheelerLoanCreation twloanCreation) {
		TwowheelerDetailesTable twowheelerDetailesTable = twowheelerdetailsservice
				.getByApplication(twloanCreation.getApplicationNo());
		JSONObject jsonObject = new JSONObject(twowheelerDetailesTable.getSecurityPdcDetails());
		jsonObject.put("CustomerID", twloanCreation.getCustomerId());
		JSONObject Data = new JSONObject();
		Data.put("Data", jsonObject);
		return Data;
	}
}
