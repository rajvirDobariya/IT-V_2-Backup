package com.suryoday.twowheeler.controller;

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

import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerGasBillService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerGasBillController {

	@Autowired
	TwowheelerGasBillService twowheelerGasBillService;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerGasBillController.class);

	@RequestMapping(value = "/gasBillVerify", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> gasBillVerify(@RequestBody String request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject jsonObject = new JSONObject(request);
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);

		if (twowheelerDetails.getUtilityBillIsVerify() != null
				&& twowheelerDetails.getUtilityBillIsVerify().equalsIgnoreCase("YES")) {
			String utilityBillResponse = twowheelerDetails.getUtilityBillResponse();
			org.json.JSONObject utilityResponse = new org.json.JSONObject(utilityBillResponse);
			return new ResponseEntity<Object>(utilityResponse.toString(), HttpStatus.OK);
		}
		JSONObject gasBillVerify = twowheelerGasBillService.gasBillVerify(jsonObject);
		logger.debug("response from gasBillVerify" + gasBillVerify);

		HttpStatus h = HttpStatus.BAD_REQUEST;
		if (gasBillVerify != null) {
			String Data2 = gasBillVerify.getString("data");

			JSONObject Data1 = new JSONObject(Data2);
			logger.debug("JSON Object " + Data2);
			String statusCode = Data1.getString("status-code");
			if (statusCode.equals("101")) {
				h = HttpStatus.OK;
			} else {
				org.json.simple.JSONObject errorResponse = new org.json.simple.JSONObject();
				org.json.simple.JSONObject error = new org.json.simple.JSONObject();

				String errorcoede = Data1.getString("status-code");

				if (errorcoede.equals("102")) {
					error.put("Description", "Invalid ID number or combination of inputs");
				} else if (errorcoede.equals("103")) {
					error.put("Description", "No records found for the given ID or combination of inputs");
				} else if (errorcoede.equals("104")) {
					error.put("Description", "Max retries exceeded");
				} else if (errorcoede.equals("105")) {
					error.put("Description", "Missing Consent");
				} else if (errorcoede.equals("106")) {
					error.put("Description", "Multiple Records Exist");
				} else if (errorcoede.equals("107")) {
					error.put("Description", "Not Supported");
				} else {
					error.put("Description", "");
				}

				error.put("Code", errorcoede);
				errorResponse.put("Error", error);
				return new ResponseEntity<Object>(errorResponse, h.INTERNAL_SERVER_ERROR);
			}

			twowheelerDetails.setUtilityBillIsVerify("YES");
			twowheelerDetails.setUtilityBillResponse(Data1.toString());
			twowheelerDetails.setUtilityBillNo(jsonObject.getString("lpg_id"));
			twowheelerDetails.setServiceProvider("");
			twowheelerDetails.setUtilityBill("GAS BILL");
			twowheelerDetails.setServiceProviderCode("");
			twowheelerDetails.setFlowStatus("TWUBC");
			String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
			logger.debug("Main Response" + Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
