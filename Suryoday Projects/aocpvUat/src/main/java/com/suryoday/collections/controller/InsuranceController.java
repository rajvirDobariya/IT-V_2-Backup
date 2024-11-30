package com.suryoday.collections.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.collections.service.InsuranceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/Insurance/")
public class InsuranceController {

	@Autowired
	InsuranceService insuranceService;

	@RequestMapping(value = "/calculate/Emi", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loginLdap(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String amount = jsonObject.getJSONObject("Data").getString("Amount");
		String product = jsonObject.getJSONObject("Data").getString("Product");
		String bankName = jsonObject.getJSONObject("Data").getString("BankName");
		String tenure = jsonObject.getJSONObject("Data").getString("Tenure");
		double amountIndouble = Double.parseDouble(amount);
		String emi = insuranceService.calculateEmi(amountIndouble, product, bankName, tenure);
		org.json.simple.JSONObject data = new org.json.simple.JSONObject();
		data.put("Insurance_Amount", emi);
		data.put("TransactionCode", "00");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", data);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
