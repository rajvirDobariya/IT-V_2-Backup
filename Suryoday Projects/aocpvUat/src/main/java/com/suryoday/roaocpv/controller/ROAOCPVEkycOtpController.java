package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.roaocpv.service.ROAOCPVEkycOtpService;
import com.suryoday.roaocpv.service.ROAOCPVValidateEkycOtpService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVEkycOtpController {

	@Autowired
	ROAOCPVEkycOtpService otpservice;

	@Autowired
	ROAOCPVValidateEkycOtpService validateOtpService;

	@RequestMapping(value = "/ekycOtp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> ekycOtp(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject jsonObject = new JSONObject(bm);
		String aadharNo = jsonObject.getJSONObject("Data").getString("AadharNo");
		int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		String stan = Integer.toString(random_int);

		String xmlRequest = otpservice.getXmlRequest(aadharNo, stan);

		String sendEkyc = otpservice.sendEkyc(xmlRequest);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		JSONObject jsonAPIResponse = new JSONObject(response);
		String responseCode = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData")
				.getJSONObject("OtpResponse").getJSONObject("OtpRes").getString("ret");
		org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
		if (responseCode.equalsIgnoreCase("y")) {
			pdresponse.put("message", "otp send successfully");
			pdresponse.put("UKC", stan);
			return new ResponseEntity<Object>(pdresponse, HttpStatus.OK);
		} else {
			pdresponse.put("message", "please enter valid aadharnumber");
			return new ResponseEntity<Object>(pdresponse, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/validateEkycOtp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOtp(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject jsonObject = new JSONObject(bm);
		String otp = jsonObject.getJSONObject("Data").getString("Otp");
		String aadharNo = jsonObject.getJSONObject("Data").getString("AadharNo");
		String UKC = jsonObject.getJSONObject("Data").getString("UKC");
		String xmlRequest = validateOtpService.getXmlRequest(otp, aadharNo, UKC);
		System.out.println(xmlRequest);
		String sendEkyc = validateOtpService.sendEkyc(xmlRequest);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		JSONObject jsonAPIResponse = new JSONObject(response);
		if (jsonAPIResponse.has("Response")) {
			String HsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
			if (!HsmData.equals("")) {
				String decryptString = validateOtpService.decryptString(HsmData);
				System.out.println(decryptString);
				org.jsoup.nodes.Document doc = Jsoup.parse(decryptString, "", Parser.xmlParser());

				Elements select = doc.select("KycRes");
				System.out.println(select.toString());
				String string = select.toString();
				org.json.JSONObject jsonConverted2 = XML.toJSONObject(string);
				String jsonStringResponse = jsonConverted2.toString();
				org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
				org.json.JSONObject mainResponse = new org.json.JSONObject();
				if (!jsonResponse.equals(null)) {
					HttpStatus h = HttpStatus.BAD_GATEWAY;
					// if (jsonResponse.has("KycRes")) {

					org.json.JSONObject responseObj = new org.json.JSONObject();
					org.json.JSONObject kycRes = new org.json.JSONObject();

					org.json.JSONObject uidData = jsonResponse.getJSONObject("kycres").getJSONObject("uiddata");
					// String txn = jsonResponse.getString("txn");
					// String ttl = jsonResponse.getString("ttl");
					// String ts = jsonResponse.getString("ts");
//				org.json.JSONObject responseData = jsonObject.getJSONObject("ResponseData");
//				String responseCode = jsonObject.getString("ResponseCode");
//				String responseMessage = jsonObject.getString("ResponseMessage");
//				String response2 = jsonObject.getString("Response2");
//				String response1 = jsonObject.getString("Response1");
					kycRes.put("UidData", uidData);
					// kycRes.put("txn", txn);
					// kycRes.put("ttl", ttl);
					// kycRes.put("ts", ts);
					responseObj.put("KycRes", kycRes);
//				responseObj.put("ResponseData", responseData);
//				responseObj.put("ResponseCode", responseCode);
//				responseObj.put("ResponseMessage", responseMessage);
//				responseObj.put("Response2", response2);
//				responseObj.put("Response1", response1);
					mainResponse.put("Response", responseObj);
					h = HttpStatus.OK;

					// }
					// else if (jsonResponse.has("Description")) {
					// h = HttpStatus.BAD_REQUEST;
					// }
					return new ResponseEntity<>(mainResponse.toString(), h);
				} else {

					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {

				return new ResponseEntity<Object>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
			}
		} else if (jsonAPIResponse.has("Description")) {

			return new ResponseEntity<>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
