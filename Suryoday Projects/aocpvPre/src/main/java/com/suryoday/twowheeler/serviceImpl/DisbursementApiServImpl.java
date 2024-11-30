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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;
import com.suryoday.twowheeler.service.DisbursementApiService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerLoanCreationService;

@Component
public class DisbursementApiServImpl implements DisbursementApiService{
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
		JSONObject disbRequest=getDisbursementRequest(applicationNo);

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
			 obj = new URL(x.BASEURL+"asset/disbursement/v2?api_key="+x.api_key);
				logger.debug(x.BASEURL+"asset/disbursement/v2?api_key="+x.api_key);
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
			String processingFee="";
			String taxAmount1="";
			String docCharges="";
			String taxAmount2="";
			if(twowheelerDetailesTable.getLoanCharges() !=null) {
	            org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
	            for(int n=0;n<loanCarges.length();n++) {
	                JSONObject jsonObject = loanCarges.getJSONObject(n);
	                if(jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee"))
	                {
	                	processingFee=jsonObject.getString("chargeAmount");
	                	taxAmount1=jsonObject.getString("taxAmount");
	                }
	                else if(jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge"))
	                {
	                	docCharges=jsonObject.getString("chargeAmount");
	                	taxAmount2=jsonObject.getString("taxAmount");
	                }
	            }
	        }
			LocalDateTime now=LocalDateTime.now();
			LocalDateTime createdTimestamp = twowheelerDetailesTable.getCreatedTimestamp();
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String disbursementDate=now.format(formatter);
			String messageDate=createdTimestamp.format(formatter);
			
			JSONObject parent=new JSONObject();
			JSONObject data=new JSONObject();
			data.put("ApplicationID","PRE"+applicationNo);
			data.put("MessageDate",messageDate);
			data.put("MessageType","DISAMT");
			data.put("MessageData","");
			data.put("FinalDisb","Y");
			data.put("DisbursementDate",disbursementDate);
			data.put("LoanAccountNumber",twowheelerloancreation.getLoanAccoutNumber());
//			data.put("DisbTargetAccount",twowheelerDetailesTable.getBeneficiaryAccountNo());
			data.put("DisbAmount",twowheelerloancreation.getDisbursalAmount());
			data.put("SchmCode","TWPTL");
			data.put("TransactionComment","DISBURSEMENT PRE"+applicationNo);
			data.put("DisbursementMode","GL");
			if(!processingFee.equals("0.0") && !processingFee.equals("") && !processingFee.equals("0")) {
				data.put("ProcessingFees",processingFee+"#"+taxAmount1);		
			}
			
			data.put("LifeInsuraceFee",twowheelerDetailesTable.getInsuranceEmi());
//			data.put("GenInsuranceFee","");
//			data.put("StampDuty","");
			if(!docCharges.equals("0.0") && !docCharges.equals("") && !docCharges.equals("0")) {
				data.put("DocCharges",docCharges+"#"+taxAmount2);			
			}
//			data.put("OtherCharges1","");
//			data.put("OtherCharges2","");
//			data.put("OtherCharges3","");
			data.put("BeneficiaryName",twowheelerDetailesTable.getBeneficiaryName());
			data.put("TransferMode","NA");
			data.put("BeneficiaryAccountNumber",twowheelerDetailesTable.getBeneficiaryAccountNo());
			data.put("BeneficiaryIfscCode",twowheelerDetailesTable.getBeneficiaryIFSC());
			parent.put("Data",data);
			logger.debug("request"+parent.toString());
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
		JSONObject request=getCollateralRequest(applicationNo);
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
			 obj = new URL(x.BASEURL+"createOrUpdate/collateralDetails?api_key="+x.api_key);
			logger.debug(x.BASEURL+"createOrUpdate/collateralDetails?api_key="+x.api_key);

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
			JSONObject parent=new JSONObject();
			JSONObject data=new JSONObject();
			LocalDateTime now=LocalDateTime.now();
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String date=now.format(formatter);
			LocalDate loanCreationDate = twloanCreation.getLoanCreationDate();
			String loanCreationDateInString=loanCreationDate.format(formatter);
			data.put("AccountID",twloanCreation.getLoanAccoutNumber());
			data.put("CollateralID","");
			data.put("CollateralType","30");
			data.put("CollateralCode","10");
			data.put("RequestType","0");
			JSONObject input1=new JSONObject();
			input1.put("SecurityFlag","1");
			input1.put("TypeOfCharge","002");
			input1.put("Make",twowheelerDetailesTable.getManufacture());
			input1.put("Model",twowheelerDetailesTable.getVariantCode());
			input1.put("ChassisNumber",twowheelerDetailesTable.getChasisNumber());
			input1.put("EngineNumber",twowheelerDetailesTable.getEngineNumber());
			input1.put("HorsePower","");
			input1.put("DateOfHypothecation",date);
			input1.put("DateOfPurchase",loanCreationDateInString);
			input1.put("YearOfManufacture",twowheelerDetailesTable.getYearOfManufacturer());
			input1.put("BodyStyle","");
			input1.put("TitledOwner",twowheelerDetailesTable.getName());
//			input1.put("RegistryNumber","12345");
			input1.put("CurrencyCode","INR");
			input1.put("InsuranceCompany","HDFC Life");
			data.put("Input1",input1);
			JSONObject input2=new JSONObject();
			input2.put("DerivePledgeValue","I");
//			input2.put("MarketValue",null);
//			input2.put("AssessedValue",twowheelerDetailesTable.getTotalOnRoadPrice());
			input2.put("InvoiceValue",twowheelerDetailesTable.getTotalOnRoadPrice());
//			input2.put("WrittenDownValue","");
			input2.put("AppraisalDate",date);
			double onRoadPrice = Double.parseDouble(twowheelerDetailesTable.getTotalOnRoadPrice());
			double marginMoney = Double.parseDouble(twowheelerDetailesTable.getMarginMoney());
			double marginPercent=(marginMoney*100)/onRoadPrice;
			double roundOffMarginPercent = Math.round(marginPercent * 100.0) / 100.0;
			input2.put("Margin",roundOffMarginPercent);
			input2.put("ReviewFrequency","3YAE");
			input2.put("ReviewLastDate",date);
			data.put("Input2",input2);
			JSONObject input3=new JSONObject();
			double amount =Double.parseDouble(twowheelerDetailesTable.getAmount());
			double maximumPledgeAmount=(amount*80.00)/100.00;
			input3.put("MaximumPledgeAmount",Double.toString(maximumPledgeAmount));
			data.put("Input3",input3);
			parent.put("Data",data);
			return parent;
	}


	@Override
	public JSONObject loanCreation(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request=getLoanCreationRequest(applicationNo,header);
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
			 obj = new URL(x.BASEURL+"asset/creation/v2?api_key="+x.api_key);
				logger.debug(x.BASEURL+"asset/creation/v2?api_key="+x.api_key);
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "HOT");
	//		con.setRequestProperty("X-Correlation-ID", applicationNo);
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID","CB");
			con.setRequestProperty("X-To-ID","OTHERS");
			con.setRequestProperty("cache-control", "no-cache");
			con.setRequestProperty("Postman-Token", "88114120-e190-4682-9cce-665b614049cd");
			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}
		sendResponse.put("request", request);
		return sendResponse;
	}


	private JSONObject getLoanCreationRequest(String applicationNo,JSONObject header) {
		TwowheelerDetailesTable twowheelerdetails = twowheelerdetailsservice.getByApplication(applicationNo);
		TwoWheelerFamilyMember familyMember=familyMemberService.fetchByApplicationNoAndMember(applicationNo,"APPLICANT");
		String processingFee="";
		LocalDateTime createdTimestamp = twowheelerdetails.getCreatedTimestamp();
		String rateOfInterest = twowheelerdetails.getEffectiveIrr();
		if(twowheelerdetails.getLoanCharges() !=null) {
            org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerdetails.getLoanCharges());
            for(int n=0;n<loanCarges.length();n++) {
                JSONObject jsonObject = loanCarges.getJSONObject(n);
                if(jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee"))
                {
                	processingFee=jsonObject.getString("totalAmount");
                }
            }
        }
		LocalDate today = LocalDate.now();
		int dayOfMonth = today.getDayOfMonth();
		int monthValue = today.getMonthValue();
		if(dayOfMonth>=15)
		{
			monthValue=monthValue+2;
		}
		else {
			monthValue=monthValue+1;	
		}
		String emiStartDate="";
		if(monthValue>12) {
			monthValue=monthValue-12;
			int year = today.getYear();
			year++;
			 emiStartDate=year+"-0"+monthValue+"-05";
		}
		else if(monthValue==10 || monthValue==11 || monthValue==12) {
			emiStartDate=today.getYear()+"-"+monthValue+"-05";
		}
		else {
			 emiStartDate=today.getYear()+"-0"+monthValue+"-05";
		}
		
		LocalDate emiDate=LocalDate.parse(emiStartDate);
//		System.out.println(today.getYear()+"-"+monthValue+"-05");
		LocalDate sanctiondate = twowheelerdetails.getSanctiondate();
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate maturityDate = emiDate.plusMonths(Integer.parseInt(twowheelerdetails.getTenure()));
		JSONObject parent=new JSONObject();
		JSONObject data=new JSONObject();
		data.put("ApplicationId","PRE"+applicationNo);
		data.put("MessageDate",""+createdTimestamp.toLocalDate()+"");
		data.put("ProductGroup","LN");
		data.put("RMCode",twowheelerdetails.getSalesCreatedBy());
		data.put("InstallmentType","M");
		data.put("GovtSponsoredSchemes","0");
		data.put("Sanctiondate",""+sanctiondate+"");
		data.put("DPNDate","");
		data.put("OccupationCode",familyMember.getOccCode());
		data.put("MOBBranchCode",header.getString("X-Branch-ID"));
		data.put("CreditLimitMaturityDate",""+maturityDate+"");
		data.put("Subsectorcode","NAO");
		data.put("CurrencyCode","INR");
		data.put("SanctionDeptCode","13");
		data.put("SectorCode","O");
		data.put("CustomerNumber",twowheelerdetails.getCustomerId());
		data.put("InterestIndex","");
		data.put("MessageType","NEWLN");
		data.put("Dateofnote",""+LocalDate.now()+"");
		data.put("DistrictCode","");
		data.put("SchmCode","TWPTL");
		data.put("LimitId","");
		data.put("CredtBureauCode","51");
		data.put("Priority_NonPriorityFlag","N");
		data.put("IndustryCode","999");
		data.put("CustRelRelationshipCode","70");
		data.put("AccountRelationshipCode","A");
		data.put("AcctDrPrefRate","");
//		data.put("TypeOfAdvan","");
		data.put("Term",twowheelerdetails.getTenure()+"M");
		data.put("InstallmentFrequencyInterest","5M");
		data.put("BalloonPayment","");
		data.put("NatureofBorrower","99");
		data.put("OragnizationCode","82");
		data.put("RePaymntMthd","E");
		data.put("SegmentCode","6");
		data.put("Sanctionamount",twowheelerdetails.getAmount());
		data.put("InstallmentFrequencyPrincipal","5M");
		data.put("CheckRequired","N");
		data.put("ModofAdvan","OTH");
		data.put("NatOfAdvan","RET-RETAIL");
		data.put("TypeOfAdvan","MT");
		data.put("SpecialCategoryCode","6");
		data.put("BranchCode",twowheelerdetails.getAccountBranchId());
		data.put("InterestChangeFrequency","");
		data.put("NatOfBuss","12");
		data.put("SourceCode","");
		data.put("HoldInOperAcct","Y");//NTB
		data.put("MoratoriumPeriodPrincipal","");
		data.put("ChannelCode","TAB");
		data.put("RoleCode","M");
		data.put("PurposeCode","1");
		data.put("MoratoriumPeriod","");
		data.put("InstallStartDate",""+emiStartDate+"");
		data.put("AmountRequested",twowheelerdetails.getAmount());
		data.put("BenefAcctNo","");
		data.put("EquatedInst","Y");
		data.put("InterestRate",rateOfInterest.replace("%",""));
		data.put("InstStartDate",""+emiStartDate+"");
		data.put("BenfIFSC","");
		data.put("SBUCode","03");
		data.put("AutoPaymentAcct",twowheelerdetails.getAccountNumber());
		data.put("StatementFrequency","5M");
		data.put("LeadGenCode",header.getString("X-User-ID"));
		data.put("MessageData","");
		data.put("Amount",twowheelerdetails.getAmount());
		data.put("MoratoriumPeriodInterest","");
		data.put("StateCode","");
		data.put("BSRPopulationCode","5");
		data.put("PegReviewDate","");
		data.put("PaymentTerm",twowheelerdetails.getTenure()+"M");
		data.put("UpfrontProcessingFees",processingFee);
		data.put("BenAcctype","");
		data.put("RELCustomerNumber",twowheelerdetails.getCustomerId());
		data.put("UnsecuredFlag","Y");
		data.put("AccountTitle",twowheelerdetails.getAccountName());
		data.put("TypOfAcct","100");
		data.put("AccountOwnershipPercentage","");
		data.put("MaximumCreditLimit",twowheelerdetails.getAmount());
		data.put("OperateMode","1");
		data.put("NatOfAdvan","");
		parent.put("Data",data);

		System.out.println(parent);
		return parent;
	}


	
	@Override
	public JSONObject crmModification(String applicationNo, JSONObject header) {
		TwowheelerDetailesTable twowheelerdetails = twowheelerdetailsservice.getByApplication(applicationNo);
		String customerId = twowheelerdetails.getCustomerId();
		JSONObject req=getRequestCrm(twowheelerdetails);
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
			 obj = new URL(x.BASEURL+"customer/"+customerId+"/modify?api_key="+x.api_key);
				logger.debug(x.BASEURL+"customer/"+customerId+"/modify?api_key="+x.api_key);
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "CHQ");
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", "CB");
			con.setRequestProperty("X-To-ID", "CHQ");
			con.setRequestProperty("X-User-ID", "application/json");
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
		JSONObject Data=new JSONObject();
		Data.put("CustomerType","R");
		Data.put("CRMUserId","34244");
		Data.put("RegisteredMobile",twowheelerdetails.getMobileNo());
		Data.put("BranchCode",twowheelerdetails.getSalesBranchId());
		Data.put("Source","HOTFOOT");
		Data.put("UserId","34244");
		
		System.out.println(Data);
		return Data;
	}

}
