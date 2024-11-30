package com.suryoday.collections.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/collections")
public class CollectionDetailsController {

	Logger logger = LoggerFactory.getLogger(CollectionDetailsController.class);

	@RequestMapping(value = "/addCenter", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCIFData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE) throws Exception {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("addCenter start");

		String centerName = jsonObject.getJSONObject("Data").getString("centerName");
		String address = jsonObject.getJSONObject("Data").getString("address");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String isActive = jsonObject.getJSONObject("Data").getString("isActive");

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("Data", null);
		logger.debug("Final Response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
