package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.connector.service.AadharReferenceService;

@RestController
@RequestMapping("/connector/v1")
public class AadharRefernceController {

	@Autowired
	AadharReferenceService aadharReferenceService;

	@Autowired
	AocpvIncomeService incomeDetailsService;

	private static Logger logger = LoggerFactory.getLogger(AadharRefernceController.class);

	@RequestMapping(value = "/aadharReference", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> createAadharReference(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "tenant", required = true) String tenant,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("tenant", tenant);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request", bm);

		if (X_Request_ID.equals("NOVOPAY")) {
			JSONObject jsonObject = new JSONObject(bm);
			// String AadharNumber =
			// jsonObject.getJSONObject("Data").getString("AadharNumber");

			JSONObject createAadharReference = aadharReferenceService.createAadharReference(jsonObject, Header);
			logger.debug("response from CreateAadharReference", createAadharReference);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (createAadharReference != null) {
				String Data2 = createAadharReference.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Object ", Data2);

				if (Data1.has("Data")) {
					long adharReference = (long) Data1.getJSONArray("Data").getJSONObject(0).get("ReferenceNumber");
					String adharreference = Long.toString(adharReference);

					incomeDetailsService.fetchByAadharCard(adharreference);

					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				logger.debug("Main Response", Data1.toString());
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

	@RequestMapping(value = "/fetchAadharByReference/{referenceNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> validateOTP(@PathVariable("referenceNo") String referenceNo,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "tenant", required = true) String tenant,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("tenant", tenant);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("GET Request", referenceNo);

		if (X_Request_ID.equals("IEXCEED")) {

			JSONObject fetchAadharByReferenceNo = aadharReferenceService.fetchAadharByReferenceNo(referenceNo, Header);
			logger.debug("Response from API", fetchAadharByReferenceNo);
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (fetchAadharByReferenceNo != null) {
				String Data2 = fetchAadharByReferenceNo.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Data From API", Data2);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				logger.debug("Main Response From API", Data1.toString());
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
