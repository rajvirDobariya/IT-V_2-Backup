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

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.UserLogService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.LoanDisbursementService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping(value = "/connector")
public class LoanDisbursementEncyController {

	@Autowired
	LoanDisbursementService loanDisbursementService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	UserService userService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	private static Logger logger = LoggerFactory.getLogger(LoanDisbursementEncyController.class);

	@RequestMapping(value = "/loanDisbursementEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loanDisbursement(@RequestBody String sm,
			@RequestHeader(name = "X-IpAddress-ID", required = true) String X_IpAddress_ID,
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
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(sm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			//logger.debug("start request" + sm.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject jsonObject1 = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject1.getJSONObject("Data").getString("applicationNo");
			long applicationNoInLong = Long.parseLong(applicationNo);

			JSONObject Header = new JSONObject();
			Header.put("X-IpAddress-ID", X_IpAddress_ID);
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-To-ID", X_To_ID);
			Header.put("X-Transaction-ID", X_Transaction_ID);
			Header.put("X-User-ID", X_User_ID);
			Header.put("X-Request-ID", X_Request_ID);
			//logger.debug("POST Request : " + sm);
			//logger.debug("ApplicationNo Sent to Request : " + applicationNoInLong);
			System.out.println(Header);

			AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String presentDate = now.format(formatter1);
			if (X_Request_ID.equals("COR")) {
				JSONObject jsonObject = new JSONObject();
				;

				org.json.simple.JSONObject response2 = new org.json.simple.JSONObject();
				response2.put("MessageId", "TW10801082");
				response2.put("MessageDate", presentDate); // d
				response2.put("MessageType", "DISAMT");
				response2.put("MessageData", "");
				response2.put("ProcessingFees", "100.00#500.00#300.00");
				response2.put("DisbursementDate", presentDate); // d
				response2.put("LoanAccountNumber", aocpvLoanCreation.getLoanAccoutNumber()); // d
				response2.put("DisbTargetAccount", "201030386891");
				response2.put("DisbAmount", aocpvLoanCreation.getSanctionedLoanAmount()); // d
				response2.put("TransactionComment", "LOAN DISBURSEMENT ~HOTFOOT");
				response2.put("DisbursementMode", "Account Credit");
				response2.put("BeneficiaryName", "MR.RAJESH");
				response2.put("InsuranceFee", "0");

				//logger.debug("Hard Coded Values in JSON Object : " + response2.toString());

				jsonObject.put("Data", response2);
				System.out.println(jsonObject);
				//logger.debug("JSON Object Sent to Request : " + jsonObject.toString());

				JSONObject loanDisbursement = loanDisbursementService.loanDisbursement(jsonObject, Header);
				System.out.println(loanDisbursement);
				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (loanDisbursement != null) {

					JSONObject Data2 = null;

					String Data1 = loanDisbursement.getString("data");
					Data2 = new JSONObject(Data1);
					System.out.println(Data2);

					if (Data2.has("Data")) {
						h = HttpStatus.OK;
						JSONObject object = Data2.getJSONObject("Data");

						String disbrustTranId = object.getString("ResponseValue");
						aocpvLoanCreation.setStatus("DISBURSED");
//							 aocpvLoanCreation.setDisbursementAmount("50000.00"); //d
//							 aocpvLoanCreation.setLoanAccoutNumber("208000000291"); //d
//							 aocpvLoanCreation.setTargetAccount("201020001667"); //d
						aocpvLoanCreation.setDisbrustTranId(disbrustTranId);
						aocpvLoanCreation.setDisbursalDate(now);
						aocpvLoanCreation.setUpdetedDate(now);
						aocpvLoanCreation.setStatus("DISBURSED");
						String massege = aocpvLoanCreationService.update(aocpvLoanCreation);
						String status = "DISBURSED";
						userLogService.save(X_User_ID, applicationNo, status,"AO",0);
						long customerId = aocpCustomerDataService.statusChange(applicationNo,status);
					} else if (loanDisbursement.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					//logger.debug("Main Response from API : " + Data2.toString());
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
