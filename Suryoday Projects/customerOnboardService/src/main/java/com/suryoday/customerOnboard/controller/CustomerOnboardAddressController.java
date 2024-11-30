package com.suryoday.customerOnboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
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

import com.suryoday.customerOnboard.service.CustomerOnboardAddressService;
import com.suryoday.customerOnboard.service.CustomerOnboardDetailsService;

@RestController
@RequestMapping
public class CustomerOnboardAddressController {

	@Autowired
	private CustomerOnboardAddressService addressService;

	@Autowired
	CustomerOnboardDetailsService onboardDetailsService;
	private static Logger logger = LoggerFactory.getLogger(CustomerOnboardPancardController.class);

	@RequestMapping(value = "/addAddress", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> panCardValidation(@RequestBody String jsonReq,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

				JSONObject jsonObject = new JSONObject(jsonReq);
				JSONObject data = jsonObject.getJSONArray("Data").getJSONObject(0);
				 addressService.addAddress(data,applicationNo, Header);			
		return new ResponseEntity<Object>("Invalid Request ", HttpStatus.OK);
	}
}
