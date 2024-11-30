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
import com.suryoday.connector.service.EkycService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/uidauth")
public class EkycEncyController {

	@Autowired
	EkycService ekycService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(EkycEncyController.class);

	@RequestMapping(value = "/ekycEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> ekycRequest(@RequestBody String parent,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest req)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(parent);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + parent.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			decryptContainerString = decryptContainerString.replaceAll("\t", "");

			System.out.println(decryptContainerString);

			logger.debug("POST Request : " + decryptContainerString);

			JSONObject request = new JSONObject(decryptContainerString);

			String uid = request.getJSONObject("kycRequest").getString("uid");
			System.out.println(uid);

			String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

			System.out.println(pidxml);
			String eKycRequest = ekycService.getJsonRequest(pidxml, uid);

			// logger.debug("Main Ekyc Request : " + eKycRequest);

			String sendEkyc = ekycService.sendEkyc(eKycRequest);
			// logger.debug("Response From the API : " + sendEkyc);

			org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
			String response = jsonConverted1.toString();
			// logger.debug("XML Data Converted To JSON : " + jsonConverted1);
			// logger.debug("JSONString : " + response);

			JSONObject jsonAPIResponse = new JSONObject(response);
			System.out.println(jsonAPIResponse);
			if (jsonAPIResponse.has("Response")) {
				org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
				String hsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
				logger.debug("hsmData : " + hsmData);
				if (!hsmData.equals("")) {
					String decryptString = ekycService.decryptString(hsmData);
					logger.debug("decrypted String : " + decryptString);
					org.json.JSONObject jsonConverted2 = XML.toJSONObject(decryptString);
					String jsonStringResponse = jsonConverted2.toString();
					// logger.debug("xmltoJsonConverted String : " + jsonStringResponse);
					org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
					// logger.debug("Main Json Response : " + jsonResponse);
					org.json.JSONObject mainResponse = new org.json.JSONObject();
					if (!jsonResponse.equals(null)) {
						HttpStatus h = HttpStatus.BAD_GATEWAY;
						if (jsonResponse.has("KycRes")) {
							h = HttpStatus.OK;
							jsonObject.put("HsmData", jsonResponse);

							org.json.JSONObject responseObj = new org.json.JSONObject();
							org.json.JSONObject kycRes = new org.json.JSONObject();
							org.json.JSONObject uidData = jsonResponse.getJSONObject("KycRes").getJSONObject("UidData");
							String txn = jsonResponse.getJSONObject("KycRes").getString("txn");
							String ttl = jsonResponse.getJSONObject("KycRes").getString("ttl");
							String ts = jsonResponse.getJSONObject("KycRes").getString("ts");
							org.json.JSONObject responseData = jsonObject.getJSONObject("ResponseData");
							String responseCode = jsonObject.getString("ResponseCode");
							String responseMessage = jsonObject.getString("ResponseMessage");
							String response2 = jsonObject.getString("Response2");
							String response1 = jsonObject.getString("Response1");
							kycRes.put("UidData", uidData);
							kycRes.put("txn", txn);
							kycRes.put("ttl", ttl);
							kycRes.put("ts", ts);
							responseObj.put("KycRes", kycRes);
							responseObj.put("ResponseData", responseData);
							responseObj.put("ResponseCode", responseCode);
							responseObj.put("ResponseMessage", responseMessage);
							responseObj.put("Response2", response2);
							responseObj.put("Response1", response1);
							mainResponse.put("Response", responseObj);
						} else if (jsonResponse.has("Description")) {
							h = HttpStatus.BAD_REQUEST;
						}
						// logger.debug("Main Response : " + mainResponse.toString());
						data = mainResponse.toString();
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
					logger.debug("If HmsData Empty Response : " + jsonAPIResponse.toString());
					data = jsonAPIResponse.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
				}
			} else if (jsonAPIResponse.has("Description")) {
				logger.debug("Main Response : " + jsonAPIResponse.toString());
				data = jsonAPIResponse.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				logger.debug("response : " + data3.toString());
				return new ResponseEntity<>(data3.toString(), HttpStatus.BAD_REQUEST);
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