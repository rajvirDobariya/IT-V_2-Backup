package com.suryoday.aocpv.controller;

import java.util.List;

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

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.BillServiceProviders;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.UtilityBillService;

@Component
@RestController
@RequestMapping("/aocpv")
public class UtilityBillController {
	
	@Autowired
	UtilityBillService billservice;
	
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	Logger logger = LoggerFactory.getLogger(UtilityBillController.class);
	@RequestMapping(value = "/fetchServiceProviders", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchServiceProviders(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		

		List<BillServiceProviders> fetchServiceProviders = billservice.fetchServiceProviders();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", fetchServiceProviders);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/authenticateUtilityBill", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateUtilityBill(@RequestBody String bm,
			@RequestHeader(name = "X-Karza-Key", required = true) String X_Karza_Key,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("x-karza-key", X_Karza_Key);
		
		JSONObject jsonObject = new JSONObject(bm);
		String consumerId = jsonObject.getJSONObject("Data").getString("ConsumerId");
		String serviceProvider = jsonObject.getJSONObject("Data").getString("ServiceProvider");
		String serviceProviderCode = jsonObject.getJSONObject("Data").getString("serviceProviderCode");
		String utilityBill = jsonObject.getJSONObject("Data").getString("utilityBill");
		
		JSONObject request=new JSONObject();
		request.put("consent", "Y");
		request.put("consumer_id", consumerId);
		request.put("service_provider", serviceProviderCode);
		JSONObject clientData=new JSONObject();
		clientData.put("caseId", applicationNo);
		request.put("clientData", clientData);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		long applicationno = Long.parseLong(applicationNo);
		AocpCustomer byApplicationNo = aocpCustomerDataService.getByApplicationNo(applicationno);
		if(byApplicationNo.getUtilityBillIsVerify()!=null && byApplicationNo.getUtilityBillIsVerify().equalsIgnoreCase("YES")) {
			String utilityBillResponse = byApplicationNo.getUtilityBillResponse();
			org.json.JSONObject utilityResponse=new org.json.JSONObject(utilityBillResponse);
			return new ResponseEntity<Object>(utilityResponse.toString(), h.OK);
		}
		JSONObject response=billservice.authenticateUtilityBill(request,Header);
		
		
		if (response != null) {
//			String Data2 = disbursement.getString("data");
//			System.out.println("data2");
//			System.out.println(Data2);
//			String Data2="{\r\n"
//					+ "\"status-code\": \"101\",\r\n"
//					+ "\"request_id\": \"4112f27e-f41f-43c9-825a-02c226de50c5\",\r\n"
//					+ "\"result\": {\r\n"
//					+ "\"address\": \"SHOP IN-217-A G/F F/P SHAHPUR JAT NEW DELHI 110049\",\r\n"
//					+ "\"amount_payable\": \"2730.00\",\r\n"
//					+ "\"bill_amount\": \"\",\r\n"
//					+ "\"bill_date\": \"\",\r\n"
//					+ "\"bill_due_date\": \"20-05-2019\",\r\n"
//					+ "\"bill_issue_date\": \"\",\r\n"
//					+ "\"bill_no\": \"\",\r\n"
//					+ "\"consumer_name\": \"SHRI BHAGWAN\",\r\n"
//					+ "\"consumer_number\": \"102452589\",\r\n"
//					+ "\"email_address\": \"rahulpanwar04@gmail.com\",\r\n"
//					+ "\"mobile_number\": \"9268920233\",\r\n"
//					+ "\"total_amount\": \"\"\r\n"
//					+ "},\r\n"
//					+ "\"clientData\": {\r\n"
//					+ "\"caseId\": \"123456\"\r\n"
//					+ "}\r\n"
//					+ "}";
			String Data2 = response.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("result")) {
				h = HttpStatus.OK;
				if(!Data1.getString("status-code").equals("101")) {
					org.json.simple.JSONObject errorResponse = new org.json.simple.JSONObject();
					org.json.simple.JSONObject error = new org.json.simple.JSONObject();
					
					String errorcoede = Data1.getString("status-code");
					 
					if(errorcoede.equals("102")) {
						error.put("Description", "Invalid ID number or combination of inputs");
					}else if(errorcoede.equals("103")) {
						error.put("Description", "No records found for the given ID or combination of inputs");
					}else if(errorcoede.equals("104")) {
						error.put("Description", "Max retries exceeded");
					}else if(errorcoede.equals("105")) {
						error.put("Description", "Missing Consent");
					}else if(errorcoede.equals("106")) {
						error.put("Description", "Multiple Records Exist");
					}else if(errorcoede.equals("107")) {
						error.put("Description", "Not Supported");
					}else {
						error.put("Description", "");
					}

					error.put("Code", errorcoede);
					errorResponse.put("Error", error);
					return new ResponseEntity<Object>(errorResponse, h.INTERNAL_SERVER_ERROR);
				}
				
				byApplicationNo.setUtilityBillIsVerify("YES");
				byApplicationNo.setUtilityBillResponse(Data1.toString());
				byApplicationNo.setUtilityBillNo(consumerId);
				byApplicationNo.setServiceProvider(serviceProvider);
				byApplicationNo.setUtilityBill("ELECTRICITY BILL");
				byApplicationNo.setServiceProviderCode(serviceProviderCode);
				aocpCustomerDataService.saveSingleData(byApplicationNo);
				
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			
			return new ResponseEntity<Object>(Data1.toString(), h.OK);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
