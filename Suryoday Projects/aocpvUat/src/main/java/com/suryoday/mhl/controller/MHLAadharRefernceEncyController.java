package com.suryoday.mhl.controller;

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
import com.suryoday.mhl.service.MHLAadharReferenceService;

@RestController
@RequestMapping("mhl/v1")
public class MHLAadharRefernceEncyController {

	@Autowired
	MHLAadharReferenceService aadharReferenceService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(MHLAadharRefernceEncyController.class);

	@RequestMapping(value = "/aadharReferenceEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> createAadharReference(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "tenant", required = true) String tenant,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("tenant", tenant);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request : " + bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			logger.debug("start request" + bm.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			if (X_Request_ID.equals("NOVOPAY")) {
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				System.out.println(jsonObject.toString());
				JSONObject createAadharReference = aadharReferenceService.createAadharReference(jsonObject, Header);
				logger.debug("response from CreateAadharReference : " + createAadharReference);

				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (createAadharReference != null) {
					String Data2 = createAadharReference.getString("data");
					System.out.println("data2");
					JSONObject Data1 = new JSONObject(Data2);
					logger.debug("JSON Object : " + Data2);

					if (Data1.has("Data")) {
						h = HttpStatus.OK;

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					logger.debug("Main Response From : " + Data1.toString());
					data = Data1.toString();
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

	@RequestMapping(value = "/fetchAadharByReferenceEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOTP(
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "tenant", required = true) String tenant,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "referenceNo", required = true) String referenceNo,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("tenant", tenant);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request : " + referenceNo);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {

			String key = X_Session_ID;

			String data = "";
			if (X_Request_ID.equals("IEXCEED")) {

				JSONObject fetchAadharByReferenceNo = aadharReferenceService.fetchAadharByReferenceNo(referenceNo);
				logger.debug("Response from API : " + fetchAadharByReferenceNo);
				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (fetchAadharByReferenceNo != null) {
					String Data2 = fetchAadharByReferenceNo.getString("data");
					System.out.println("data2");
					JSONObject Data1 = new JSONObject(Data2);
					logger.debug("JSON Data From API : " + Data2);

					if (Data1.has("Data")) {
						h = HttpStatus.OK;

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					logger.debug("Main Response From API : " + Data1.toString());
					data = Data1.toString();
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
