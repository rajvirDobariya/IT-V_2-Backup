package com.suryoday.roaocpv.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.ROAOCPVEkycService;
import com.suryoday.roaocpv.service.ROAOCPVErrorResponseService;

@RestController
@RequestMapping("/roaocpv/uidauth")
public class ROAOCPVEkycEncyController {

	@Autowired
	ROAOCPVEkycService roaocpvEkycService;

	@Autowired
	UserService userService;

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	@Autowired
	ROAOCPVErrorResponseService errorResponseService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVEkycEncyController.class);

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

			String latitude = request.getJSONObject("kycRequest").getString("latitude");
			String longitude = request.getJSONObject("kycRequest").getString("longitude");
			String address = request.getJSONObject("kycRequest").getString("address");
			String applicationNo = request.getJSONObject("kycRequest").getString("applicationNo");
			String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

			System.out.println(pidxml);
			String eKycRequest = roaocpvEkycService.getJsonRequest(pidxml, uid);

			logger.debug("Main Ekyc Request : " + eKycRequest);

			String sendEkyc = roaocpvEkycService.sendEkyc(eKycRequest);
			logger.debug("Response From the API : " + sendEkyc);

			org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
			String response = jsonConverted1.toString();
			logger.debug("XML Data Converted To JSON : " + jsonConverted1);
			logger.debug("JSONString : " + response);

			JSONObject jsonAPIResponse = new JSONObject(response);
			System.out.println(jsonAPIResponse);
			if (jsonAPIResponse.has("Response")) {
				org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
				String hsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
				logger.debug("hsmData : " + hsmData);
				if (!hsmData.equals("")) {
					String decryptString = roaocpvEkycService.decryptString(hsmData);
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

							kycRes.put("txn", txn);
							kycRes.put("ttl", ttl);
							kycRes.put("ts", ts);
							responseObj.put("KycRes", kycRes);
							responseObj.put("ResponseData", responseData);
							responseObj.put("ResponseCode", responseCode);
							responseObj.put("ResponseMessage", responseMessage);
							responseObj.put("Response2", response2);
							responseObj.put("Response1", response1);
							logger.debug("UID Data : " + uid);

							String ekycPhoto = uidData.getString("Pht");
							String ekycAadhar = uidData.getJSONObject("Prn").getString("content");
							Map<String, String> map = new HashMap();
							map.put("ekycPhoto", ekycPhoto);
							map.put("ekycAadhar", ekycAadhar);

							String[] saveData = { ekycPhoto, ekycAadhar };

							org.json.JSONObject ekyc_photo = new org.json.JSONObject();
							ekyc_photo.put("image", "photo.jpg");
							ekyc_photo.put("Lat", latitude);
							ekyc_photo.put("Long", longitude);
							ekyc_photo.put("Address", address);
							LocalDateTime localDateTime = LocalDateTime.now();
							ekyc_photo.put("timestamp", localDateTime);

							org.json.JSONObject ekyc_aadhar = new org.json.JSONObject();
							ekyc_aadhar.put("image", "photo.pdf");
							ekyc_aadhar.put("Lat", latitude);
							ekyc_aadhar.put("Long", longitude);
							ekyc_aadhar.put("Address", address);
							ekyc_aadhar.put("timestamp", localDateTime);

							org.json.JSONObject ekyc_photo1 = new org.json.JSONObject();
							ekyc_photo1.put("ekyc_photo", ekyc_photo);
							org.json.JSONObject ekyc_aadhar1 = new org.json.JSONObject();
							ekyc_aadhar1.put("ekyc_aadhar", ekyc_aadhar);

							org.json.JSONArray jsonArray = new org.json.JSONArray();
							jsonArray.put(ekyc_aadhar1);
							jsonArray.put(ekyc_photo1);

							Long applicationNo1 = Long.parseLong(applicationNo);
							aocpvImageService.saveAadharPhoto(map, applicationNo1, jsonArray, "PD");
							// uidData.remove("Pht");
							uidData.remove("Prn");
							kycRes.put("UidData", uidData);
							JSONObject poi = uidData.getJSONObject("Poi");
							mainResponse.put("Response", responseObj);
							applicationDetailsService.saveData("ekyc", uid, applicationNo, mainResponse.toString());

							h = HttpStatus.OK;
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

					String kycRes = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData")
							.getJSONObject("KycResponse").getJSONObject("Resp").getString("kycRes");
					String errorResponse = errorResponseService.getError(kycRes, Long.parseLong(applicationNo));
					data = errorResponse;
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
