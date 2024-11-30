package com.suryoday.roaocpv.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ROAOCPVProductType;
import com.suryoday.roaocpv.service.ROAOCPVProductTypeService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVProductTypeEncyController {

	@Autowired
	ROAOCPVProductTypeService roaocpvProductTypeService;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(ROAOCPVProductTypeEncyController.class);

	@RequestMapping(value = "/fetchByCategoryTypeEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCategoryType(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("fetchByCategoryType start");
			logger.debug("request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String productType = jsonObject.getJSONObject("Data").getString("ProductType");
			logger.debug("db Call start");
			List<ROAOCPVProductType> roaocpvProductType = roaocpvProductTypeService.fetchByCategoryType(productType);
			logger.debug("db Call end" + roaocpvProductType);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(roaocpvProductType);
			response.put("Data", j);
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/fetchByAllEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByAll(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("fetchByAll start");
			logger.debug("request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			logger.debug("db Call start");
			List<ROAOCPVProductType> roaocpvProductType2 = roaocpvProductTypeService.fetchByAll();
			logger.debug("db Call end" + roaocpvProductType2);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(roaocpvProductType2);
			response.put("Data", j);
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
