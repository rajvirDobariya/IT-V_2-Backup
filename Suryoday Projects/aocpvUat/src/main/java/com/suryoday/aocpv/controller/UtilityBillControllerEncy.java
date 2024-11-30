package com.suryoday.aocpv.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@Component
@RestController
@RequestMapping("/aocpv")
public class UtilityBillControllerEncy {

	@Autowired
	UtilityBillService billservice;

	@Autowired
	UserService userService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	Logger logger = LoggerFactory.getLogger(UtilityBillControllerEncy.class);

	@RequestMapping(value = "/fetchServiceProvidersEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchServiceProviders(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			// logger.debug(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);

			List<BillServiceProviders> fetchServiceProviders = billservice.fetchServiceProviders();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(fetchServiceProviders);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			logger.debug(data3.toString());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/authenticateUtilityBillEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateUtilityBill(@RequestBody String bm,
			@RequestHeader(name = "X-Karza-Key", required = true) String X_Karza_Key,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("x-karza-key", X_Karza_Key);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			// logger.debug(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String consumerId = jsonObject.getJSONObject("Data").getString("ConsumerId");
			String serviceProvider = jsonObject.getJSONObject("Data").getString("ServiceProvider");
			String serviceProviderCode = jsonObject.getJSONObject("Data").getString("serviceProviderCode");
			String utilityBill = jsonObject.getJSONObject("Data").getString("utilityBill");

			JSONObject request1 = new JSONObject();
			request1.put("consent", "Y");
			request1.put("consumer_id", consumerId);
			request1.put("service_provider", serviceProviderCode);
			JSONObject clientData = new JSONObject();
			clientData.put("caseId", applicationNo);
			request1.put("clientData", clientData);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			long applicationno = Long.parseLong(applicationNo);
			AocpCustomer byApplicationNo = aocpCustomerDataService.getByApplicationNo(applicationno);
			if (byApplicationNo.getUtilityBillIsVerify() != null
					&& byApplicationNo.getUtilityBillIsVerify().equalsIgnoreCase("YES")) {
				String utilityBillResponse = byApplicationNo.getUtilityBillResponse();
				org.json.JSONObject utilityResponse = new org.json.JSONObject(utilityBillResponse);
				String data = utilityResponse.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				logger.debug(data3.toString());
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}

			JSONObject response = billservice.authenticateUtilityBill(request1, Header);

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
					if (!Data1.getString("status-code").equals("101")) {
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

						byApplicationNo.setUtilityBillIsVerify("NO");
						aocpCustomerDataService.saveSingleData(byApplicationNo);
						String data = errorResponse.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						logger.debug(data3.toString());
						return new ResponseEntity<Object>(data3, HttpStatus.INTERNAL_SERVER_ERROR);
//					return new ResponseEntity<Object>(errorResponse, h.INTERNAL_SERVER_ERROR);
					}

					byApplicationNo.setUtilityBillIsVerify("YES");
					byApplicationNo.setUtilityBillNo(consumerId);
					byApplicationNo.setServiceProvider(serviceProvider);
					byApplicationNo.setUtilityBill(utilityBill);
					byApplicationNo.setServiceProviderCode(serviceProviderCode);
					byApplicationNo.setUtilityBillResponse(Data1.toString());

					aocpCustomerDataService.saveSingleData(byApplicationNo);

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				String data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				logger.debug(data3.toString());
				return new ResponseEntity<Object>(data3, HttpStatus.OK);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
