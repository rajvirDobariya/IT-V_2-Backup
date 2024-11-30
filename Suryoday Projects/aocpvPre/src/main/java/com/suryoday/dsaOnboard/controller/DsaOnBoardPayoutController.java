package com.suryoday.dsaOnboard.controller;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.dsaOnboard.pojo.PayoutSchemeMaster;
import com.suryoday.dsaOnboard.service.DsaOnBoardPayoutService;


@Component
@RestController
@RequestMapping("/dsaOnBoard")
public class DsaOnBoardPayoutController {
	@Autowired
	DsaOnBoardPayoutService payoutservice;
	Logger logger = LoggerFactory.getLogger(DsaOnBoardPayoutController.class);
	@RequestMapping(value="/fetchByProductAndAgency", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchByProductAndAgency(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		String product = jsonObject.getJSONObject("Data").getString("Product");
		String agencyType = jsonObject.getJSONObject("Data").getString("AgencyType");
		logger.debug("fetchByProductAndAgency start");
		logger.debug("request"+jsonRequest);
		
		logger.debug("db Call start");
		PayoutSchemeMaster schemeMaster=payoutservice.fetchByProductAndAgency(product,agencyType);
		JSONObject json=new JSONObject(schemeMaster);
		logger.debug("db Call end"+json);
		org.json.simple.JSONObject response=new org.json.simple.JSONObject();
		response.put("Data", json);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response.toString(),HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/fetchBySchemeCode", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchBySchemeCode(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws IOException {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		String schemeCode = jsonObject.getJSONObject("Data").getString("SchemeCode");
		logger.debug("fetchBySchemeCode start");
		logger.debug("request"+jsonRequest);
		
		logger.debug("db Call start");
		String htmlString=payoutservice.fetchBySchemeCode(schemeCode);
		org.json.simple.JSONObject response=new org.json.simple.JSONObject();
		org.json.simple.JSONObject data=new org.json.simple.JSONObject();
		data.put("HtmlString",htmlString);
		response.put("Data", data);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response.toString(),HttpStatus.OK);
	
	}
}
