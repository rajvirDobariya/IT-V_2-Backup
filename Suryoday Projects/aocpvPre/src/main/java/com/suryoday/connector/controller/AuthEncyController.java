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

import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.AadharReferenceService;
import com.suryoday.connector.service.AuthService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/uidauth")
public class AuthEncyController {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;

	@Autowired
	AadharReferenceService aadharReferenceService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	private static Logger logger = LoggerFactory.getLogger(AuthEncyController.class);

	@RequestMapping(value = "/authEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authRequest(@RequestBody String parent,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest req) throws Exception {
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true ) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(parent);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			//logger.debug("start request" + parent.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
			String data = "";

			decryptContainerString = decryptContainerString.replaceAll("\t", "");

		//System.out.println(decryptContainerString);

		logger.debug("POST Request : " + decryptContainerString);

		JSONObject request = new JSONObject(decryptContainerString);

		String uid = request.getJSONObject("kycRequest").getString("uid");
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("tenant", "140");
		Header.put("X-Request-ID", X_Request_ID);
		long aadhar=0;
		JSONObject fetchAadharByReferenceNo = aadharReferenceService.fetchAadharByReferenceNo(uid,Header);
		if (fetchAadharByReferenceNo != null) {
			String Data2 = fetchAadharByReferenceNo.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			aadhar = Data1.getJSONObject("Data").getLong("AadharNumber");
			
		}

		String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

		//System.out.println(pidxml);
		String authRequest = authService.getJsonRequest(pidxml, Long.toString(aadhar));
		//System.out.println("Main Auth Request : " + authRequest);

		//logger.debug("Main Auth Request : " + authRequest);

		String sendAuth = authService.sendAuth(authRequest);
		//logger.debug("Response From the API : " + sendAuth);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendAuth);
		String response = jsonConverted1.toString();
		//logger.debug("XML Data Converted To JSON : " + jsonConverted1);
		//logger.debug("JSONString : " + response);

		JSONObject jsonResponse = new JSONObject(response);
		System.out.println(jsonResponse);
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("Response")) {
				h = HttpStatus.OK;
				String applicationNo = request.getJSONObject("kycRequest").getString("applicationNo");
				String member = request.getJSONObject("kycRequest").getString("member");
				
				AocpvIncomeDetails incomeDetails = aocpvIncomeService.getByAppNoAndmember(Long.parseLong(applicationNo), member);
				if(incomeDetails != null) {
					incomeDetails.setAuthVerify("YES");
					aocpvIncomeService.save(incomeDetails);	
				}
			} else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			//logger.debug("Main Response : " + jsonResponse.toString());
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
