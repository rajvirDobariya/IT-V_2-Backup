package com.suryoday.hastakshar.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.service.CustomerDetailsService;

@RestController
@RequestMapping("/hastakshar")
public class CustomerDetailController {

	@Autowired
	private CustomerDetailsService custsomerdetailsservice;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(CustomerDetailController.class);

	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCustomerDetailsPlain(@RequestBody String bm, HttpServletRequest req)
			throws Exception {
		logger.debug("getCustomerDetails start");
		logger.debug("getCustomerDetails request" + bm);
		JSONObject Header = new JSONObject();
//		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		// Header.put("X-From-ID", X_From_ID);
		Header.put("X-User-ID", "30639");
		// Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);

		JSONObject customerDetails = custsomerdetailsservice.getCustomerDetails(jsonObject, Header);

		logger.debug("customerDetails" + customerDetails);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (customerDetails != null) {
			String Data2 = customerDetails.getString("data");
			logger.debug("data2");
			JSONObject Data1 = new JSONObject(Data2);
			System.out.println(Data1);
			logger.debug(Data1.toString());

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			logger.debug("response" + Data1);

			logger.debug(" Controller_getCustomerDetailsEncy_responsedata :" + Data1.toString());
			return new ResponseEntity<Object>(Data1, h);
		}

		else {
			logger.debug("timeout");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}

	@RequestMapping(value = "/get/accountIdsByCIFNo", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getAllAccountIdsByCIFNo(@RequestBody String bm, HttpServletRequest req)
			throws Exception {
		logger.debug("getAllAccountIdsByCIFNo request" + bm);
		JSONObject Header = new JSONObject();
//		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		// Header.put("X-From-ID", X_From_ID);
		Header.put("X-User-ID", "30639");
		// Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);

		JSONObject customerDetails = custsomerdetailsservice.getCustomerDetails(jsonObject, Header);

		logger.debug("customerDetails" + customerDetails);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (customerDetails != null) {
			String Data2 = customerDetails.getString("data");
			logger.debug("data2");
			JSONObject Data1 = new JSONObject(Data2);			

			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				//1
				JSONObject data3 = Data1.getJSONObject("Data");
				//2
				JSONArray accountDetails = data3.getJSONArray("AccountDetails");
				List<String> accountIds = new ArrayList<>();
				 for (int i = 0; i < accountDetails.length(); i++) {
			            JSONObject account = accountDetails.getJSONObject(i);
					System.out.println(account.toString());
					String accountId = account.getString("AccountId");
					System.out.println(accountId);
					accountIds.add(accountId);
				}
				
				return new ResponseEntity<Object>(accountIds, h);

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			logger.debug("response" + Data1);

			logger.debug(" Controller_getCustomerDetailsEncy_responsedata :" + Data1.toString());
			return new ResponseEntity<Object>(Data1, h);
		}

		else {
			logger.debug("timeout");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}

}
