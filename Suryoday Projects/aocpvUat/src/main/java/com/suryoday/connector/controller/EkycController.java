package com.suryoday.connector.controller;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.service.EkycService;

@RestController
@RequestMapping("/uidauth")
public class EkycController {

	@Autowired
	EkycService ekycService;

	private static Logger logger = LoggerFactory.getLogger(EkycController.class);

	@RequestMapping(value = "/ekyc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> ekycRequest(@RequestBody String parent) throws Exception {

		parent = parent.replaceAll("\t", "");

		System.out.println(parent);

		logger.debug("POST Request : " + parent);

		JSONObject request = new JSONObject(parent);

		String uid = request.getJSONObject("kycRequest").getString("uid");
		System.out.println(uid);

		String udc = request.getJSONObject("kycRequest").getString("udc");

		String otp = request.getJSONObject("kycRequest").getString("otp");

		String serviceType = request.getJSONObject("kycRequest").getString("serviceType");

		String isKyc = request.getJSONObject("kycRequest").getString("isKyc");

		String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

		System.out.println(pidxml);
		String eKycRequest = ekycService.getJsonRequest(pidxml, uid);

		logger.debug("Main Ekyc Request : " + eKycRequest);

		String sendEkyc = ekycService.sendEkyc(eKycRequest);
		logger.debug("Response From the API :  " + sendEkyc);

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
				String decryptString = ekycService.decryptString(hsmData);
				logger.debug("decrypted String : " + decryptString);
				org.json.JSONObject jsonConverted2 = XML.toJSONObject(decryptString);
				String jsonStringResponse = jsonConverted2.toString();
				logger.debug("xmltoJsonConverted String : " + jsonStringResponse);
				org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
				logger.debug("Main Json Response : " + jsonResponse);
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
						h = HttpStatus.OK;

					} else if (jsonResponse.has("Description")) {
						h = HttpStatus.BAD_REQUEST;
					}
					logger.debug("Main Response : " + mainResponse.toString());
					return new ResponseEntity<>(mainResponse.toString(), h);
				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("If HmsData Empty Response : " + jsonAPIResponse.toString());
				return new ResponseEntity<Object>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
			}
		} else if (jsonAPIResponse.has("Description")) {
			logger.debug("Main Response : " + jsonAPIResponse.toString());
			return new ResponseEntity<>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}

}