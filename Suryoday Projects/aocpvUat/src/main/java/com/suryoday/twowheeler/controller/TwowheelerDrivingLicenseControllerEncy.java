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
import com.suryoday.roaocpv.service.ROAOCPVDrivingLicenseService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerDrivingLicenseControllerEncy {

	@Autowired
	ROAOCPVDrivingLicenseService drivinglicenseservice;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerDrivingLicenseControllerEncy.class);

	@RequestMapping(value = "/authenticateDrivingLicenseEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateDrivingLicense(@RequestBody String bm,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = true;
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject Header = new JSONObject();
			Header.put("Content-Type", Content_Type);
			Header.put("X-Request-ID", X_Request_ID);
			logger.debug("POST Request" + bm);

			if (X_Request_ID.equals("TW")) {
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				String drivingLicenseNo = jsonObject.getJSONObject("Data").getString("DrivingLicenseNumber");
				String dob = jsonObject.getJSONObject("Data").getString("DOB");
				System.out.println(jsonObject.toString());
				JSONObject authenticateDrivingLicense = drivinglicenseservice
						.authenticateDrivingLicense(drivingLicenseNo, dob, Header);
				;
				logger.debug("response from verifyPassport" + authenticateDrivingLicense);

				HttpStatus h = HttpStatus.OK;
				if (authenticateDrivingLicense != null) {
					String Data2 = authenticateDrivingLicense.getString("data");
					System.out.println("data2");
					JSONObject Data1 = new JSONObject(Data2);
					logger.debug("JSON Object " + Data2);

					if (Data1.has("Data")) {
						h = HttpStatus.OK;

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					logger.debug("Main Response" + Data1.toString());
					String data = Data1.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
