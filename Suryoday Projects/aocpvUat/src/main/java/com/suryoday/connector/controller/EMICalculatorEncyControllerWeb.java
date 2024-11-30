package com.suryoday.connector.controller;

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

import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.EMICalculatorService;
import com.suryoday.connector.service.GetLoanDetailsService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping(value = "/connector/v1")
public class EMICalculatorEncyControllerWeb {

	@Autowired
	EMICalculatorService emiCalculatorService;

	@Autowired
	private LoanInputService loanInputService;

	@Autowired
	GetLoanDetailsService getLoanDetailsService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(CustomerDetailsController.class);

	@RequestMapping(value = "/EMICalculatorWebEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendEmail(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Customer-ID", required = true) String X_Customer_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-To-ID", X_To_ID);
		// logger.debug("POST Request : " + bm);

		JSONObject header = new JSONObject();
		header.put("X-Request-ID", "Channel_Name");
		header.put("X-Correlation-ID", "23456789");
		header.put("X-From-ID", "CB");
		header.put("X-To-ID", "Channel_Name");
		header.put("X-User-ID", "Channel_Name");

		String LoanOutstanding = "0";

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + bm.toString());

			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			// String decryptContainerString =
			// AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			if (X_Request_ID.equals("HOTFOOT")) {
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				// logger.debug("JSON Object Sent to Request : " + jsonObject.toString());
				// long customerId = Long.parseLong(X_Customer_ID);
				// LoanDetails loanDetails = loanInputService.fetchByCustomerId(customerId);
				// logger.debug("loanDetails : " + loanDetails.getAccountNo());
				// String accountNo = loanDetails.getAccountNo();
				// JSONObject getloanDetails = getLoanDetailsService.getLoanDetails(accountNo,
				// header);
				// logger.debug("response : " + getloanDetails.toString());
				// String Data3 = getloanDetails.getString("data");
				// JSONObject Data4 = new JSONObject(Data3);
				// if (Data4.has("Data")) {
				// LoanOutstanding = Data4.getJSONObject("Data").getJSONObject("AccountDetails")
				// .getJSONObject("LoanDtls").getString("LoanOutstanding");
				// Data4.getJSONObject("Data");
				// System.out.println(LoanOutstanding);
				// }

				JSONObject EMICalculator = emiCalculatorService.EMICalculator(jsonObject, Header);
				// logger.debug("Response from the API : " + EMICalculator);

				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (EMICalculator != null) {
					String Data2 = EMICalculator.getString("data");
					JSONObject Data1 = new JSONObject(Data2);
					// logger.debug("JSON Object from Response : " + Data2);

					if (Data1.has("Data")) {
						JSONObject jsonObject2 = Data1.getJSONObject("Data");
						jsonObject2.put("LoanOutstanding Amount", LoanOutstanding);
						h = HttpStatus.OK;

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					// logger.debug("Main Response from API : " + Data1.toString());
					data = Data1.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					// String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data,
					// key);
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
