package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.XML;
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
import com.suryoday.connector.service.CibilReportService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/uidauth")
public class CibilReportEncyController {

	@Autowired
	CibilReportService cibilReportService;
	
	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(CibilReportEncyController.class);

	@RequestMapping(value = "/cibilReportency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authRequest(@RequestBody String parent,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 HttpServletRequest request){

	//	logger.debug("POST Request : " + parent);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(parent);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			//logger.debug("start request" + decryptContainerString.toString());
		
		String mainRequest = cibilReportService.getJsonRequest(decryptContainerString);

		//logger.debug("MainRequest Sent To API" + mainRequest);
		
		String cibilReport = cibilReportService.getCibilReport(mainRequest);
		//logger.debug("Response From the API : " + cibilReport);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(cibilReport);
		String response = jsonConverted1.toString();
		//logger.debug("XML Data Converted To JSON : " + jsonConverted1);
		//logger.debug("JSONString : " + response);

		JSONObject jsonResponse = new JSONObject(response);
		System.out.println(jsonResponse);
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("soapenv:Envelope")) {
				h = HttpStatus.OK;
			} else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
		//	logger.debug("Main Response : " + jsonResponse.toString());
			data = jsonResponse.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
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

}
