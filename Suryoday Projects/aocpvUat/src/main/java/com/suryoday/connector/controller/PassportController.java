package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.roaocpv.service.ROAOCPVPassportService;

@Component
@RestController
@RequestMapping(value = "/connector/v1")
public class PassportController {

	@Autowired
	ROAOCPVPassportService passportservice;

	@Autowired
	AocpvIncomeService incomeDetailsService;

	private static Logger logger = LoggerFactory.getLogger(PassportController.class);

	@RequestMapping(value = "/verifyPassport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> verifyPassport(@RequestBody String bm,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Content-Type", Content_Type);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request" + bm);

		JSONObject jsonObject = new JSONObject(bm);
		String passportNo = jsonObject.getJSONObject("Data").getString("PassportNumber");
		String doi = jsonObject.getJSONObject("Data").getString("DateOfIssue");
		System.out.println(jsonObject.toString());
		JSONObject verifyPassport = passportservice.verifyPassport(passportNo, doi, Header, applicationNo);
		logger.debug("response from verifyPassport" + verifyPassport);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (verifyPassport != null) {
			String Data2 = verifyPassport.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);
			logger.debug("JSON Object " + Data2);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				incomeDetailsService.savePassport(Data1.toString(), applicationNo, member);
				// applicationDetailsService.saveData("passport", passportNo,
				// applicationNo,Data1.toString());
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			logger.debug("Main Response" + Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
