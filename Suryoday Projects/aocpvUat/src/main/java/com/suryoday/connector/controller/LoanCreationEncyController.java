package com.suryoday.connector.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.UserLogService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.LoanCreationService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping(value = "/connector")
public class LoanCreationEncyController {

	@Autowired
	LoanCreationService loanCreationService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(LoanCreationEncyController.class);

	@RequestMapping(value = "/loanCreationEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loanCreation(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			long applicationNoInLong = Long.parseLong(applicationNo);

			JSONObject Header = new JSONObject();
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-To-ID", X_To_ID);
			Header.put("X-Transaction-ID", X_Transaction_ID);
			Header.put("X-User-ID", X_User_ID);
			Header.put("X-Request-ID", X_Request_ID);
			// logger.debug("POST Request" + jsonRequest);
			System.out.println(Header);
			logger.debug("ApplicationNo Sent to Request : " + applicationNoInLong);

//		String sessionId = userService.validateSessionId( request);
//		if (sessionId != null) {

			AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
			if (aocpvLoanCreation.getLoanAccoutNumber() != null) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Message", "SuccessFully LoanAccountNumber Generated");
				response.put("LoanAccountNumber", aocpvLoanCreation.getLoanAccoutNumber());
				data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
			}

			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String presentDate = now.format(formatter1);
			LocalDate date = aocpvLoanCreation.getCreatedDate();
			String sanctionedDate = date.format(formatter1);
			String customerId = Long.toString(aocpvLoanCreation.getCustomerId());
			LocalDate emiDate1 = now.plusMonths(1);
			String emiDate = emiDate1.format(formatter1);

			if (applicationNo == null || applicationNo.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}
			AocpCustomer aocpCustomer = aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
			// logger.debug("db response : " + aocpCustomer);
			String amountRequested = Double.toString(aocpCustomer.getRequestLoan());

			if (X_Request_ID.equals("COR")) {
				JSONObject response = new JSONObject();
				org.json.simple.JSONObject response2 = new org.json.simple.JSONObject();
				response2.put("MessageId", "TW212021010256");
				response2.put("MessageDate", presentDate);// d
				response2.put("MessageType", "NEWLN");
				response2.put("MessageData", "");
				response2.put("AccountTitle", "MRS.AARTI KUMAR PATEL");
				response2.put("Amount", aocpvLoanCreation.getSanctionedLoanAmount());// d
				response2.put("Term", aocpvLoanCreation.getTenure());// d
				response2.put("BalloonPayment", "0");
				response2.put("CurrencyCode", "INR");
				response2.put("BranchCode", aocpvLoanCreation.getBranchId());// d
				response2.put("FirstRepaymentDate", emiDate);// d
				response2.put("InstallmentFrequencyPrincipal", "1MA5");
				response2.put("InstallmentFrequencyInterest", "1MA5");
				response2.put("InstallmentType", "3");
				response2.put("MoratoriumPeriodPrincipal", "");
				response2.put("MoratoriumPeriodInterest", "");
				response2.put("CollateralNumber", "");
				response2.put("CollateralNumber2", "");
				response2.put("CollateralType", "");
				response2.put("CollateralCode", "");
				response2.put("CredtBureauCode", "51");
				response2.put("PurposeCode", "206");
				response2.put("InterestChangeFrequency", "");
				response2.put("PaymentChangefrequency", "");
				response2.put("Sanctionamount", aocpvLoanCreation.getSanctionedLoanAmount());// d
				response2.put("Sanctiondate", sanctionedDate);// d
				response2.put("Dateofnote", sanctionedDate);// d
				response2.put("PaymentTerm", aocpvLoanCreation.getTenure());// d
				response2.put("StatementFrequency", "1MA5");
				response2.put("MoratoriumPeriod", "");
				response2.put("SBUCode", "03");
				response2.put("DPNDate", sanctionedDate);// d
				response2.put("SectorCode", "O");
				response2.put("Subsectorcode", "NAO");
				response2.put("Priority_NonPriorityFlag", "N");
				response2.put("UnsecuredFlag", "Y");
				response2.put("LimitId", "");
				response2.put("CreditLimitMaturityDate", sanctionedDate);// d
				response2.put("NatureofBorrower", "99");
				response2.put("BorrowerWAHEAD", "100");
				response2.put("SpecialCategoryCode", "6");
				response2.put("StateCode", "MH");
				response2.put("DistrictCode", "TEST");
				response2.put("OragnizationCode", "82");
				response2.put("OccupationCode", "06001");
				response2.put("BSRPopulationCode", "1");
				response2.put("GovtSponsoredSchemes", "0");
				response2.put("SanctionDeptCode", "13");
				response2.put("AmountRequested", amountRequested);// d
				response2.put("RevolvingAccountFlag", "0");
				response2.put("ScoreRanging", "");
				response2.put("MaximumCreditLimit", amountRequested);// d
				response2.put("EMIAmount", "");
				response2.put("ProcessingFees", "100");
				response2.put("AccountName_productname", "");
				response2.put("AccountOriginationMethodCode", "");
				response2.put("CompanyCode", "SURY");
				response2.put("ProductType", "5019");
				response2.put("PackageName", "");
				response2.put("ARSEnrolledIndicator", "0");
				response2.put("ProductGroup", "LN");
				response2.put("CustomerNumber", customerId); // d
				response2.put("RELCustomerNumber", customerId); // d
				response2.put("RoleCode", "1");
				response2.put("AccountOwnershipPercentage", "100");
				response2.put("AccountRelationshipCode", "A");
				response2.put("APPID", "");
				response2.put("CheckRequired", "N");
				response2.put("FacilityID", "");
				response2.put("TransId", "12345");
				response2.put("OperateMode", "");
				response2.put("ClassificationCodesForLoan", "");
				response2.put("InterestRate", "10");
				response2.put("InterestIndex", "");
				response2.put("RMCode", "");
				response2.put("DSACode", "");
				response2.put("BDECode", "");
				response2.put("ReferralCode", "");
				response2.put("PMHousingFlag", "");
				response2.put("AutoPaymentAcct", "");
				response2.put("LeadGenCode", "");
				response2.put("MOBBranchCode", aocpvLoanCreation.getBranchId()); // d
				response2.put("ChannelCode", "6");
				response2.put("SourceCode", "PAYTM");
				org.json.simple.JSONArray dynamicField = new org.json.simple.JSONArray();

				org.json.simple.JSONObject key1 = new org.json.simple.JSONObject();
				key1.put("Key", "LN.ZSEGMNTCODE");
				key1.put("Value", "11");

				org.json.simple.JSONObject key2 = new org.json.simple.JSONObject();
				key2.put("Key", "LN.ZBUSSCD");
				key2.put("Value", "12");

				org.json.simple.JSONObject key3 = new org.json.simple.JSONObject();
				key3.put("Key", "LN.ZBCID");
				key3.put("Value", "9011902290");

				org.json.simple.JSONObject key4 = new org.json.simple.JSONObject();
				key4.put("Key", "LN.ZIFSC");
				key4.put("Value", "ICIC0000366");

				org.json.simple.JSONObject key5 = new org.json.simple.JSONObject();
				key5.put("Key", "LN.ZBTYPE");
				key5.put("Value", "10");

				dynamicField.add(key1);
				dynamicField.add(key2);
				dynamicField.add(key3);
				dynamicField.add(key4);
				dynamicField.add(key5);

				response2.put("DynamicField", dynamicField);

				logger.debug("Hard Coded Values in JSON Object : " + response2.toString());

				response.put("Data", response2);
				System.out.println(response);
				logger.debug("JSON Object Sent to Request : " + response);
				JSONObject loanCreation = loanCreationService.loanCreation(response, Header);
				logger.debug("Response from the API : " + loanCreation);

				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (loanCreation != null) {

					JSONObject Data2 = null;

					String Data1 = loanCreation.getString("data");
					Data2 = new JSONObject(Data1);
					System.out.println(Data2);

					if (Data2.has("Data")) {
						h = HttpStatus.OK;
						JSONObject object = Data2.getJSONObject("Data");
						String loanAccountNumber = object.getString("ResponseValue");

						// String loanAccountNumber = Data2.getString("ResponseValue");
						aocpvLoanCreation.setStatus("LOAN CREATED");

						aocpvLoanCreation.setLoanCreationDate(now);
						aocpvLoanCreation.setUpdetedDate(now);
						aocpvLoanCreation.setLoanAccoutNumber(loanAccountNumber);
						String massege = aocpvLoanCreationService.update(aocpvLoanCreation);
						String status = "LOAN CREATED";
						userLogService.save(X_User_ID, applicationNo, status, "AO", 0);
						org.json.simple.JSONObject finalResponse = new org.json.simple.JSONObject();
						finalResponse.put("Message", "SuccessFully LoanAccountNumber Generated");
						finalResponse.put("LoanAccountNumber", aocpvLoanCreation.getLoanAccoutNumber());
						data = finalResponse.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.JSONObject data2 = new org.json.JSONObject();
						data2.put("value", encryptString2);
						org.json.JSONObject data3 = new org.json.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
					} else if (loanCreation.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					// logger.debug("Main Response from API : " + Data2.toString());
					data = Data2.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), h);

				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

}
