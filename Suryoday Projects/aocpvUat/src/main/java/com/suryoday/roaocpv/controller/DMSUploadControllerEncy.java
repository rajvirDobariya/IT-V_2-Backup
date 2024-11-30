package com.suryoday.roaocpv.controller;

import java.io.IOException;

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

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.DMSUploadService;

@RestController
@RequestMapping(value = "/roaocpv")
public class DMSUploadControllerEncy {

	@Autowired
	DMSUploadService dmsuplaodservice;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(DMSUploadControllerEncy.class);

	@RequestMapping(value = "/dmsuploadEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dmsuplaod(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws IOException {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject sendAuthenticateResponse = new JSONObject();
			// sendAuthenticateResponse=dmsuplaodservice.dmsupload(jsonObject);
			JSONArray jsonArray = sendAuthenticateResponse.getJSONArray("Response");
			HttpStatus h = HttpStatus.OK;
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Message", "DOCUMENT UPLOAD SUCCESSFULLY");

			for (int n = 0; n < jsonArray.length(); n++) {
				JSONObject jsonResponse = jsonArray.getJSONObject(n);
				if (jsonResponse.has("Errors")) {
					h = HttpStatus.BAD_REQUEST;
					response.put("Message", "FAILED !! PLEASE TRY AGAIN");
				}
			}
			logger.debug("Response in controller: " + response);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
			return new ResponseEntity<Object>(data3, h);
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