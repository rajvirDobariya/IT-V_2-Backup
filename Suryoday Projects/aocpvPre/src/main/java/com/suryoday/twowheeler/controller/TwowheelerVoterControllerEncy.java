package com.suryoday.twowheeler.controller;

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
import com.suryoday.connector.service.VoterIDService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerVoterControllerEncy {
	@Autowired
	VoterIDService voterIDService;

	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(TwowheelerVoterControllerEncy.class);
	@RequestMapping(value = "/voterIdency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> voterId(@RequestBody String bm,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-karza-key", required = true) String X_KARZA_KEY,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			String data = "";
			JSONObject Header = new JSONObject();
			Header.put("X-karza-key", X_KARZA_KEY);
			logger.debug("POST Request : " + decryptContainerString);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			 String voterId = jsonObject.getJSONObject("Data").getString("epic_no");
	//			incomeDetailsService.fetchByVoterId(voterId);
			JSONObject voterID = voterIDService.voterID(jsonObject, Header);
			//logger.debug("response from voterId API : " + voterID);

//			HttpStatus h = HttpStatus.BAD_GATEWAY;
//			if (voterID != null) {
//				String Data2 = voterID.getString("data");
//				System.out.println("data2");
//				JSONObject Data1 = new JSONObject(Data2);
//				logger.debug("JSON Object : " + Data2);
//
//				if (Data1.has("status")) {
//					h = HttpStatus.OK;
//
//				} else if (Data1.has("Error")) {
//					h = HttpStatus.BAD_REQUEST;
//
//				}
//				logger.debug("Main Response : " + Data1.toString());
//				data = Data1.toString();
//				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//				org.json.JSONObject data2 = new org.json.JSONObject();
//				data2.put("value", encryptString2);
//				org.json.JSONObject data3 = new org.json.JSONObject();
//				data3.put("Data", data2);
//				logger.debug("response : " + data3.toString());
//				return new ResponseEntity<Object>(data3.toString(), h);
//			} else {
//				logger.debug("GATEWAY_TIMEOUT");
//				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
//			}

			JSONObject hardCodeValue = voterIDService.getHardCodeValue();
		//	logger.debug("HardCoded Response:" + hardCodeValue.toString());
			JSONObject respose = hardCodeValue.getJSONObject("result");
//			incomeDetailsService.savevoterIdResponse(respose.toString(),applicationNo,member);
			data = hardCodeValue.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
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
