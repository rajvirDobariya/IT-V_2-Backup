package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.service.EmiCalculatorApiService;

@Component
@RestController
@RequestMapping(value = "/twowheeler")
public class EmiCalculatorApiControllerEncy {

	private static Logger logger = LoggerFactory.getLogger(EmiCalculatorApiControllerEncy.class);

	@Autowired
	EmiCalculatorApiService emicalculatorservice;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/emiCalculatorEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> emiCalculator(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject emiCalculator = emicalculatorservice.emiCalculator(jsonObject, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (emiCalculator != null) {
				String Data2 = emiCalculator.getString("data");
				System.out.println("data2");
				System.out.println(Data2);
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					h = HttpStatus.OK;

//				return new ResponseEntity<Object>(Data1.toString(), h);
				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				String data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
//			return new ResponseEntity<Object>(Data1.toString(), h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
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

	@RequestMapping(value = "/insuranceCalculatorEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> insuranceCalculator(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String amount = jsonObject.getJSONObject("Data").getString("Amount");
//		String product = jsonObject.getJSONObject("Data").getString("Product");
//		String bankName = jsonObject.getJSONObject("Data").getString("BankName");
			String product = "5019";
			String bankName = "HDFC Life";
			String tenure = jsonObject.getJSONObject("Data").getString("Tenure");
			double amountIndouble = Double.parseDouble(amount);
			String emi = emicalculatorservice.calculateInsurance(amountIndouble, product, bankName, tenure);
			org.json.simple.JSONObject json = new org.json.simple.JSONObject();
			json.put("Insurance_Amount", emi);
			json.put("TransactionCode", "00");
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", json);

			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
