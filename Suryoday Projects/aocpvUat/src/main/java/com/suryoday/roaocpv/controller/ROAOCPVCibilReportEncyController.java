package com.suryoday.roaocpv.controller;

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

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ROAOCPVCibilReportService;

@RestController
@RequestMapping("/roaocpv/uidauth")
public class ROAOCPVCibilReportEncyController {

	@Autowired
	ROAOCPVCibilReportService roaocpvCibilReportService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCibilReportEncyController.class);

	@RequestMapping(value = "/cibilReportency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authRequest(@RequestBody String parent,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request) {
		logger.debug("POST Request : " + parent);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(parent);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + parent.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject object = new JSONObject(decryptContainerString);
			String applicationNO = object.getJSONObject("Data").getString("applicationNo");

			int max = 950;
			int min = 350;
			int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
			String stan = Integer.toString(random_int);
			logger.debug("Stan : " + stan);
			org.json.JSONObject jsonObject = new org.json.JSONObject();
			jsonObject.put("Value", "CibilScore for the accountNo: " + applicationNO);
			jsonObject.put("Score: ", stan);
			org.json.JSONObject jsonObject1 = new org.json.JSONObject();
			jsonObject1.put("Data", jsonObject);
			data = jsonObject1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);

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
