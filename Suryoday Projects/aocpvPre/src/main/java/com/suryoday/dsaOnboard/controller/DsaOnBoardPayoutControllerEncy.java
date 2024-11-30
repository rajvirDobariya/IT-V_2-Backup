package com.suryoday.dsaOnboard.controller;

import java.io.IOException;

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
import com.suryoday.dsaOnboard.pojo.PayoutSchemeMaster;
import com.suryoday.dsaOnboard.service.DsaOnBoardPayoutService;

@Component
@RestController
@RequestMapping("/dsaOnBoard")
public class DsaOnBoardPayoutControllerEncy {
	@Autowired
	DsaOnBoardPayoutService payoutservice;
	@Autowired
	UserService userService;
	Logger logger = LoggerFactory.getLogger(DsaOnBoardPayoutControllerEncy.class);

	@RequestMapping(value = "/fetchByProductAndAgencyEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByProductAndAgency(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String product = jsonObject.getJSONObject("Data").getString("Product");
		String agencyType = jsonObject.getJSONObject("Data").getString("AgencyType");
		logger.debug("fetchByProductAndAgency start");
		logger.debug("request" + jsonRequest);

		logger.debug("db Call start");
		PayoutSchemeMaster schemeMaster = payoutservice.fetchByProductAndAgency(product, agencyType);
		JSONObject json = new JSONObject(schemeMaster);
		logger.debug("db Call end" + json);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", json);
		logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/fetchBySchemeCodeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBySchemeCode(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request) throws IOException {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String schemeCode = jsonObject.getJSONObject("Data").getString("SchemeCode");
		logger.debug("fetchBySchemeCode start");
		logger.debug("request" + jsonRequest);

		logger.debug("db Call start");
		String htmlString = payoutservice.fetchBySchemeCode(schemeCode);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
		resp.put("HtmlString", htmlString);
		response.put("Data", resp);
		logger.debug("final response" + response.toString());
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
