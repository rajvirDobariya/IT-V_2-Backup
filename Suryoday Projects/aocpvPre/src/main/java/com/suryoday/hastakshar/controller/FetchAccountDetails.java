package com.suryoday.hastakshar.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.hastakshar.service.ReqStatusService;

@RestController
@RequestMapping("/hastakshar")
public class FetchAccountDetails {

	@Autowired
	private ReqStatusService reqstatusservice;

	@RequestMapping(value = "/fetchAccountDetails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getAccountDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req) {

		JSONObject requestBody = new JSONObject(jsonRequest);
		// String customerId =
		// requestBody.getJSONObject("Data").getString("accountNumber");
		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject accountDetails = reqstatusservice.getAccountDetails(Header, requestBody);

		return new ResponseEntity<Object>(accountDetails.toString(), HttpStatus.OK);

	}

}
