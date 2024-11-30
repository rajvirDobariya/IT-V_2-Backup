package com.suryoday.aocpv.controller;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.DisbursementService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@Component
@RestController
@RequestMapping(value = "aocpv")
public class DisbursementControllerEncy {
	private static Logger logger = LoggerFactory.getLogger(DisbursementControllerEncy.class);

	@Autowired
	DisbursementService disbursementservice;
	@Autowired
	UserService userService;

	@RequestMapping(value = "/fetchDisbursementStatusEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchDisbursementStatusEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchDisbursementStatusEncy start");
		System.out.println("request" + jsonRequest);

		if (jsonRequest.isEmpty()) {
			System.out.println("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			// System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			// logger.debug("PD request"+decryptContainerString);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			System.out.println("db Call start");

			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			System.out.println("db Call start");
			AocpvLoanCreation aocpvLoanCreation = disbursementservice
					.fetchDisbursementStatus(Long.parseLong(applicationNo));

			org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
			resp.put("CIFNo", aocpvLoanCreation.getCifNumber());
			resp.put("AccountNo", aocpvLoanCreation.getAccountNumber());
			resp.put("DMSUpload", "Y");
			resp.put("ClientCreation", aocpvLoanCreation.getClientId());
			resp.put("LoanCreation", aocpvLoanCreation.getLoanAccoutNumber());
			resp.put("Disbursemnt", aocpvLoanCreation.getDisbrustTranId());
			resp.put("Status", "Booking");

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", resp);
			System.out.println("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
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
