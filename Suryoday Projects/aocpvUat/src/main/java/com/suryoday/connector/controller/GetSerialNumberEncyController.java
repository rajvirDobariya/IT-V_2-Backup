package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/uidauth")
public class GetSerialNumberEncyController {

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(GetSerialNumberEncyController.class);

	@RequestMapping(value = "/getSerialDataEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> serviceRequestDispatcher(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws ClassNotFoundException {

		System.out.println(jsonRequest);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject lResponse = new JSONObject(decryptContainerString);
			String piddata = lResponse.getJSONObject("Data").getString("PidData");
			// Json Request from FE/Postman
			System.out.println(piddata);
			// logger.debug("JSONREQUEST : " + piddata);

			String jsonString = null;
			try {
				// XML to JSON Conversion
				JSONObject jsonConverted = XML.toJSONObject(piddata);
				jsonString = jsonConverted.toString();
				System.out.println(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// Taking a value from JsonObject
			JSONObject obj = new JSONObject(jsonString);
			JSONArray jsonArray = obj.getJSONObject("PidData").getJSONObject("DeviceInfo")
					.getJSONObject("additional_info").getJSONArray("Param");
			System.out.println("Json Single Value" + jsonArray);
			// logger.debug("JSONARRAY : " + jsonArray);

			// Taking Value through FOR LOOP
			String stringValue = "";
			String stringName = "";
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				stringName = jsonObject.getString("name");
				if (stringName.equalsIgnoreCase("DeviceSerial")) {
					stringValue = jsonObject.getString("value");
				}
			}
			System.out.println(stringValue);

			JSONObject response2 = new JSONObject();
			response2.put("DeviceID", stringValue);
			JSONObject response = new JSONObject();
			response.put("Data", response2);
			// logger.debug("Main Response : " + response.toString());
			data = response.toString();
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
