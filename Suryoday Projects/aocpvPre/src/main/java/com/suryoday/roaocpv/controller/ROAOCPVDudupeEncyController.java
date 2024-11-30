package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.CustomerService;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.ROAOCPVDedupeService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVDudupeEncyController {

	@Autowired
	ROAOCPVDedupeService roaocpvDedupeService;

	@Autowired
	UserService userService;

	@Autowired
	ApplicationDetailsService applicationDetailsService;
	
	@Autowired
	LoanInputService loanInputService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	@RequestMapping(value = "/dedupeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dedupeCall(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + bm.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject Header = new JSONObject();
			Header.put("X-Correlation-ID", X_CORRELATION_ID);
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-To-ID", X_To_ID);
			Header.put("X-Transaction-ID", X_Transaction_ID);
			Header.put("X-User-ID", X_User_ID);
			Header.put("X-Request-ID", X_Request_ID);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			System.out.println(jsonObject.toString());
			JSONObject dedupeCall = roaocpvDedupeService.dedupeCall(jsonObject, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (dedupeCall != null) {
				String Data2 = dedupeCall.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				JSONObject jsonObject2 = Data1.getJSONObject("Error");
				org.json.JSONObject Data4 = new org.json.JSONObject();
				JSONObject jsonObject3 = null;
				if (!jsonObject2.has("Description")) {
					jsonObject3 = Data1.getJSONObject("Data");
					Data4.put("Data", jsonObject3);
					Data4.put("Error", jsonObject2.toString());
				} else {
					Data4.put("Error", jsonObject2);
				}

				if (Data1.has("Data")) {
					String matchFlag = Data1.getJSONObject("Data").getString("MatchFlag");
					if(matchFlag.equalsIgnoreCase("100% Match")) {
						JSONObject jsonForCIF = new JSONObject();
						String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
						String cifNumber = Data1.getJSONObject("Data").getJSONObject("CustomerDetails").getString("CIFNumber");
						long customerNo = Long.parseLong(cifNumber);
						JSONObject externalDedupeCall =customerService.externaldedupe(cifNumber);
						 if(externalDedupeCall!=null) {
							 String data5 = externalDedupeCall.getString("data");
							 JSONObject response = new JSONObject(data5);
							 if(response.has("Data")) {
								 JSONObject Data = response.getJSONObject("Data");
								 if(Data.has("ProfileLoanDetails")) {
									 JSONArray loanDetails = Data.getJSONArray("ProfileLoanDetails");
									 Data4.put("ProfileLoanDetails", loanDetails);
								 }
							 }
						 }
						 AocpCustomer aocpCustomer = aocpCustomerDataService.getbycustomerId(customerNo);
							if(aocpCustomer != null) {
								String amount = aocpCustomer.getEligibleAmount();
								Data4.put("Eligible Amount", amount);	
								Data4.put("status", aocpCustomer.getStatus());
								Data4.put("updatedDate", aocpCustomer.getUpdatedate());
								Data4.put("name", aocpCustomer.getName());
								Data4.put("applicationNo", aocpCustomer.getAppNoWithProductCode());
							}
						
						jsonForCIF.put("customerId", cifNumber);
						applicationDetailsService.saveCIf(applicationNo, jsonForCIF);
					}
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}

				data = Data4.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
