package com.suryoday.hastakshar.controller;

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

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.service.CustomerDetailsService;

@RestController
@RequestMapping("/hastakshar/web")
public class CustomerDetailControllerEncy {

	@Autowired
	private CustomerDetailsService custsomerdetailsservice;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(CustomerDetailControllerEncy.class);

	@RequestMapping(value = "/getCustomerDetailsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCustomerDetails(@RequestBody String bm,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = false) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest req)
			throws Exception {
		logger.debug("getCustomerDetails start");
		logger.debug("getCustomerDetails request" + bm);
		JSONObject Header = new JSONObject();
//		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		// Header.put("X-From-ID", X_From_ID);
		Header.put("X-User-ID", "30639");
		// Header.put("X-Request-ID", X_Request_ID);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);

		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject customerDetails = custsomerdetailsservice.getCustomerDetails(jsonObject, Header);
			String Data2 = customerDetails.getString("data");
			logger.debug("data2");
			JSONObject Data1 = new JSONObject(Data2);

			logger.debug("customerDetails" + customerDetails);
			data = Data1.toString();

			if (customerDetails != null) {

				if (customerDetails.has("data")) {

					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<>(data3.toString(), HttpStatus.OK);

				} else if (customerDetails.has("Error")) {

					logger.debug("response" + customerDetails);
					data = customerDetails.toString();
					logger.debug(" Controller_getCustomerDetailsEncy_responsedata :" + data);
					String encryptString2 = "";

					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
				}
			} else {
				logger.debug("timeout");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", "SessionId is expired or Invalid sessionId ");
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Error", data2);
		logger.debug("SessionId is expired or Invalid sessionId ");
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);

	}

}
