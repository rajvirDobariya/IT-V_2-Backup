package com.suryoday.roaocpv.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.roaocpv.service.ROAOCPVCibilReportService;

@RestController
@RequestMapping("/roaocpv/uidauth")
public class ROAOCPVCibilReportController {

	@Autowired
	ROAOCPVCibilReportService roaocpvCibilReportService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCibilReportController.class);

	@RequestMapping(value = "/cibilReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCibilReport(@RequestBody String parent) {

		JSONObject object = new JSONObject(parent);
		String applicationNO = object.getJSONObject("Data").getString("applicationNo");
		String mainRequest = roaocpvCibilReportService.getJsonRequest(parent);

		logger.debug("MainRequest Sent To API" + mainRequest);

		// String cibilReport = roaocpvCibilReportService.getCibilReport(mainRequest);
		// logger.debug("Response From the API : " + cibilReport);

//		org.json.JSONObject jsonConverted1 = XML.toJSONObject(cibilReport);
//		String response = jsonConverted1.toString();
//		logger.debug("XML Data Converted To JSON : " + jsonConverted1);
//		logger.debug("JSONString : " + response);
//
//		JSONObject jsonResponse = new JSONObject(response);
//		System.out.println(jsonResponse);
//		if (!jsonResponse.equals(null)) {
//			HttpStatus h = HttpStatus.BAD_GATEWAY;
//			if (jsonResponse.has("soapenv:Envelope")) {
//				h = HttpStatus.OK;
//			} else if (jsonResponse.has("Description")) {
//				h = HttpStatus.BAD_REQUEST;
//			}
//			logger.debug("Main Response : " + jsonResponse.toString());
//			return new ResponseEntity<>(jsonResponse.toString(), h);
		int max = 950;
		int min = 350;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		String stan = Integer.toString(random_int);
		logger.debug("Stan : " + stan);
		org.json.JSONObject jsonObject = new org.json.JSONObject();
		jsonObject.put("Value", "CibilScore for the accountNo: " + applicationNO);
		jsonObject.put("Score: ", stan);
		org.json.JSONObject jsonObject1 = new org.json.JSONObject();
		jsonObject1.put("Data", jsonObject);

		return new ResponseEntity<Object>(jsonObject1.toString(), HttpStatus.OK);
//		} else {
//			logger.debug("GATEWAY_TIMEOUT");
//			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
//		}
	}

}
