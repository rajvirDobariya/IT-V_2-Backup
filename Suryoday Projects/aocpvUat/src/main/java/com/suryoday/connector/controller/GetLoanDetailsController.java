package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.service.GetLoanDetailsService;

@RestController
@RequestMapping(value = "/connector/v1")
public class GetLoanDetailsController {

	@Autowired
	GetLoanDetailsService getLoanDetailsService;

	private static Logger logger = LoggerFactory.getLogger(GetLoanDetailsController.class);

	@RequestMapping(value = "/getLoanDetails/{accountNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> validateOTP(@PathVariable("accountNo") String accountNo,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("GET Request", accountNo);

		if (X_Request_ID.equals("Channel_Name")) {

			JSONObject getloanDetails = getLoanDetailsService.getLoanDetails(accountNo, Header);
			logger.debug("Response from the API", getloanDetails);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (getloanDetails != null) {
				String Data2 = getloanDetails.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Object from Response", Data2);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				logger.debug("Main Response from API", Data1.toString());
				return new ResponseEntity<Object>(Data1.toString(), h);

			} else {
				logger.debug("GATEWAY_TIMEOUT");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			logger.debug("INVALID REQUEST");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}

}
