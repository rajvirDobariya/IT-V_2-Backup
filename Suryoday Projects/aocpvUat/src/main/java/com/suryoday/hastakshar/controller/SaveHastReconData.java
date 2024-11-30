package com.suryoday.hastakshar.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.hastakshar.service.SaveReconRequestService;

@RestController
@RequestMapping("/hastakshar")
public class SaveHastReconData {

	private static Logger logger = LoggerFactory.getLogger(SaveHastReconData.class);

	@Autowired
	private SaveReconRequestService saveRequest;

	@PostMapping(value = "/saveNewRequestRecon", produces = "application/json")
	public ResponseEntity<Object> saveNewRequestRecon(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = saveRequest.saveReconRequestData(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchReconList", produces = "application/json")
	public ResponseEntity<Object> fetchTopten(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = saveRequest.fetchReconList(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
