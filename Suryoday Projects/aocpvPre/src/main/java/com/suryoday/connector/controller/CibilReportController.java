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

import com.suryoday.connector.service.CibilReportService;

@RestController
@RequestMapping("/uidauth")
public class CibilReportController {

	@Autowired
	CibilReportService cibilReportService;

	private static Logger logger = LoggerFactory.getLogger(CibilReportController.class);

	@RequestMapping(value = "/cibilReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authRequest(@RequestBody String parent){
			//@RequestHeader(name = "Cookie", required = true) String Cookie) {

		logger.debug("POST Request : " + parent);
//		JSONObject Header = new JSONObject();
//		Header.put("Cookie", Cookie);
//		
//		JSONObject request = new JSONObject(parent);
//		String jsontoStringRequest = request.toString();
//
//		String jsonConverted = XML.toString(jsontoStringRequest);
//		//String response1 = jsonConverted.toString();
//		logger.debug("XML Data Converted To JSON : " + jsonConverted);
//		//logger.debug("JSONString : " + response1);
		
		String mainRequest = cibilReportService.getJsonRequest(parent);

		logger.debug("MainRequest Sent To API" + mainRequest);
		
		String cibilReport = cibilReportService.getCibilReport(mainRequest);
		logger.debug("Response From the API : " + cibilReport);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(cibilReport);
		String response = jsonConverted1.toString();
		logger.debug("XML Data Converted To JSON : " + jsonConverted1);
		logger.debug("JSONString : " + response);

		JSONObject jsonResponse = new JSONObject(response);
		System.out.println(jsonResponse);
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("soapenv:Envelope")) {
				h = HttpStatus.OK;
			} else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResponse.toString());
			return new ResponseEntity<>(jsonResponse.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}

}
