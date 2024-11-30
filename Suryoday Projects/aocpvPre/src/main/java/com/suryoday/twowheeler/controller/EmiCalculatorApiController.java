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

import com.suryoday.connector.service.EMICalculatorService;
import com.suryoday.twowheeler.service.EmiCalculatorApiService;

@RestController
@RequestMapping(value="/twowheeler")
public class EmiCalculatorApiController {
	
private static Logger logger = LoggerFactory.getLogger(EmiCalculatorApiController.class);

	@Autowired
	EMICalculatorService emicalculatorservice1;
	
	@Autowired
	EmiCalculatorApiService emicalculatorservice;
	
	@RequestMapping(value = "/emiCalculator", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> emiCalculator(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);
	 
		JSONObject jsonObject = new JSONObject(bm);
//		JSONObject emiCalculator = emicalculatorservice.emiCalculator(jsonObject ,Header);
		JSONObject emiCalculator = emicalculatorservice1.EMICalculator(jsonObject ,Header);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (emiCalculator != null) {
			String Data2 = emiCalculator.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				
				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@RequestMapping(value = "/insuranceCalculator", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> insuranceCalculator(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String amount = jsonObject.getJSONObject("Data").getString("Amount");
//		String product = jsonObject.getJSONObject("Data").getString("Product");
//		String bankName = jsonObject.getJSONObject("Data").getString("BankName");
		String product = "TWPTL";
		String bankName = "HDFC Life";
		String tenure = jsonObject.getJSONObject("Data").getString("Tenure");
		double amountIndouble = Double.parseDouble(amount);
		String emi = emicalculatorservice.calculateInsurance(amountIndouble, product, bankName, tenure);
		org.json.simple.JSONObject data = new org.json.simple.JSONObject();
		data.put("Insurance_Amount", emi);
		data.put("TransactionCode", "00");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", data);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
