package com.suryoday.hastakshar.controller;

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

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.service.ReqStatusService;

@RestController
@RequestMapping("/hastakshar/web")
public class FetchAccountDetailsEncy {

	private static final Logger logger = LoggerFactory.getLogger(FetchAccountDetailsEncy.class);

	@Autowired
	private ReqStatusService reqstatusservice;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/fetchAccountDetailsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getAccountDetailsEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest request)
			throws Exception {

		JSONObject encryptJSONObject = new JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject Header = new JSONObject();
			Header.put("X-Request-ID", X_Request_ID);
			JSONObject SavedData = reqstatusservice.getAccountDetails(Header, jsonObject);

			if (SavedData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = SavedData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
