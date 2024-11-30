package com.suryoday.dsaOnboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.suryoday.dsaOnboard.service.DsaOnBoardGstDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaOnBoardGstDetailsEncy {

	private static Logger logger = LoggerFactory.getLogger(DsaOnBoardGstDetailsEncy.class);

	@Autowired
	UserService userService;

	@Autowired
	DsaOnBoardGstDetailsService dsaOnBoardGstDetailsService;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	@RequestMapping(value = "/gstAuthenticationEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> createAadharReference(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject Header = new JSONObject();

			Header.put("x-karza-key", "En57jgfu0m1JFS40b645");
			logger.debug("POST Request" + bm);

			if (X_Request_ID.equals("AOCPV")) {
				JSONObject jsonObjectData = jsonObject.getJSONObject("Data");

				JSONObject gstAuthentication = dsaOnBoardGstDetailsService.gstAuthentication(jsonObjectData, Header);

				logger.debug("response from CreateAadharReference" + gstAuthentication);

				HttpStatus h = HttpStatus.OK;
				if (gstAuthentication != null) {
					String Data2 = gstAuthentication.getString("data");

					JSONObject Data1 = new JSONObject(Data2);
					int count = 0;
					if (member.equals("")) {
						member = RandomStringUtils.randomAlphanumeric(10);
						count++;
					}
					dsaOnboardMemberService.saveResponse("gst", jsonObjectData.getString("gstin"), applicationNo,
							Data1.toString(), member);
					if (count == 1) {
						Data1.put("member", member);
					}
					JSONObject Data4 = new JSONObject();
					Data4.put("Data", Data1);
					String data = Data4.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), h);
				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

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
