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
import com.suryoday.roaocpv.service.ROAOCPVDrivingLicenseService;

@Component
@RestController
@RequestMapping(value = "/connector/v1")
public class DrivingLicenseController {

	@Autowired
	ROAOCPVDrivingLicenseService drivinglicenseservice;

	@Autowired
	AocpvIncomeService incomeDetailsService;

	private static Logger logger = LoggerFactory.getLogger(DrivingLicenseController.class);

	@RequestMapping(value = "/authenticateDrivingLicense", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateDrivingLicense(@RequestBody String bm,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Content-Type", Content_Type);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request" + bm);

		if (X_Request_ID.equals("MHL")) {
			JSONObject jsonObject = new JSONObject(bm);
			String drivingLicenseNo = jsonObject.getJSONObject("Data").getString("DrivingLicenseNumber");
			String dob = jsonObject.getJSONObject("Data").getString("DOB");
			System.out.println(jsonObject.toString());
			JSONObject authenticateDrivingLicense = drivinglicenseservice.authenticateDrivingLicense(drivingLicenseNo,
					dob, Header);
			;
			logger.debug("response from verifyPassport" + authenticateDrivingLicense);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (authenticateDrivingLicense != null) {
				String Data2 = authenticateDrivingLicense.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Object " + Data2);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					incomeDetailsService.saveDrivingLicense(Data1.toString(), applicationNo, member);
//					applicationDetailsService.saveData("drivingLicense", drivingLicenseNo, applicationNo,Data1.toString());

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				logger.debug("Main Response" + Data1.toString());
				return new ResponseEntity<Object>(Data1.toString(), h.OK);
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
