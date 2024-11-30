package com.suryoday.dsaOnboard.controller;

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
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardImpsService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaOnboardIfscCodeEncy {

	@Autowired
	UserService userService;

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;

	@Autowired
	DsaOnboardImpsService dsaOnboardImpsService;

	private static Logger logger = LoggerFactory.getLogger(DsaOnboardIfscCodeEncy.class);

	@RequestMapping(value = "/searchIfscEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> searchIfsc(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Correlation-ID", X_Correlation_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);

		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			String data = "";
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String ifscCode = jsonObject.getJSONObject("Data").getString("IfscCode");
			JSONObject searchIfsc = dsaOnboardImpsService.searchIfsc(ifscCode, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (searchIfsc != null) {

				String Data2 = searchIfsc.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}

				data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				logger.debug("response : " + data3.toString());
				return new ResponseEntity<Object>(data3.toString(), h);

			} else {

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

	@RequestMapping(value = "/pennyDropEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> pennyDrop(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		logger.debug("pennyDrop start");
		logger.debug("pennyDrop request");
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		if (X_Request_ID.equals("AVAIL")) {
			boolean sessionId = userService.validateSessionId(X_Session_ID, req);
			if (sessionId == true) {
				userService.getSessionId(X_User_ID, req);
				JSONObject encryptJSONObject = new JSONObject(bm);
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
				String data = "";
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				String accountNo = jsonObject.getJSONObject("Data").getString("AccountNo");
				String ifsc = jsonObject.getJSONObject("Data").getString("IfscCode");
				String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
				String bankName = jsonObject.getJSONObject("Data").getString("bankName");
				String accountHolderName = jsonObject.getJSONObject("Data").getString("accountHolderName");
				HttpStatus h = HttpStatus.BAD_GATEWAY;

				JSONObject pennyDrop = dsaOnboardImpsService.pennyLess(accountNo, ifsc, Header, accountHolderName);

				if (pennyDrop != null) {

					String Data2 = pennyDrop.getString("data");
					JSONObject Data1 = new JSONObject(Data2);

					logger.debug(Data1.toString());
					String status = "NO";
					if (Data1.has("Data")) {
						h = HttpStatus.OK;
						if (Data1.getJSONObject("Data").getString("status").equals("SUCCESS")) {
							status = "YES";
						}
					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					DsaOnboardDetails byApplicationno = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
					byApplicationno.setBankDetailsResponse(Data1.toString());
					byApplicationno.setBankDetailsVerify(status);
					dsaOnBoardDetailsService.saveData(byApplicationno);
					data = Data1.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), h);

				} else {
					logger.debug("timeout");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("Invalid Request");
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
