package com.suryoday.roaocpv.controller;

import java.io.IOException;

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

import com.suryoday.roaocpv.service.ROAOCPVCreateWorkItemService;

@RestController
@RequestMapping(value = "/roaocpv")
public class ROAOCPVCreateWorkItemController {

	@Autowired
	ROAOCPVCreateWorkItemService roaocpvcreateworkitemservice;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCreateWorkItemController.class);

	@RequestMapping(value = "/workitem", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> workitem(@RequestBody String bm) throws IOException {
		JSONObject jsonObject = new JSONObject(bm);
		JSONObject sendAuthenticateResponse = new JSONObject();
		sendAuthenticateResponse = roaocpvcreateworkitemservice.workitem(jsonObject);
		logger.debug("Response in controller: " + sendAuthenticateResponse);
		return new ResponseEntity<>(sendAuthenticateResponse.toString(), HttpStatus.OK);
	}

}