package com.suryoday.mhl.controller;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.mhl.pojo.Expenses;
import com.suryoday.mhl.service.ExpensesService;

@RestController
@RequestMapping("/mhl")
public class ExpensesControllerEncy {

	Logger logger = LoggerFactory.getLogger(ExpensesControllerEncy.class);

	@Autowired
	ExpensesService expensesService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/monthlyExpenseSaveEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveMonthlyExpenses(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		// System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(jsonRequest);
			logger.debug("save data start");
			// logger.debug("request"+jsonRequest);
			// System.out.println("JSON STRING "+jsonObject);

			if (jsonRequest.isEmpty()) {
				// logger.debug("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String status = jsonObject.getJSONObject("Data").getString("status");
			String foodAndUtility = jsonObject.getJSONObject("Data").getString("foodAndUtility");
			double foodAndUtilityD = Double.parseDouble(foodAndUtility);
			String rent = jsonObject.getJSONObject("Data").getString("rent");
			double rentD = Double.parseDouble(rent);
			String transportation = jsonObject.getJSONObject("Data").getString("transportation");
			double transportationD = Double.parseDouble(transportation);
			String medical = jsonObject.getJSONObject("Data").getString("medical");
			double medicalD = Double.parseDouble(medical);
			String education = jsonObject.getJSONObject("Data").getString("education");
			double educationD = Double.parseDouble(education);
			String other = jsonObject.getJSONObject("Data").getString("other");
			double otherD = Double.parseDouble(other);
			String total = jsonObject.getJSONObject("Data").getString("total");
			double totalD = Double.parseDouble(total);
			String monthlyBalance = jsonObject.getJSONObject("Data").getString("monthlyBalance");
			double monthlyBalanceD = Double.parseDouble(monthlyBalance);
			String insurance = jsonObject.getJSONObject("Data").getString("insurance");
			double insuranceD = Double.parseDouble(insurance);

			Expenses exp = new Expenses();
			exp.setApplicationNo(applicationNo);
			exp.setStatus(status);
			exp.setFoodAndUtility(foodAndUtilityD);
			exp.setEducation(educationD);
			exp.setInsurance(insuranceD);
			exp.setMedical(medicalD);
			exp.setMonthlyBalance(monthlyBalanceD);
			exp.setOther(otherD);
			exp.setRent(rentD);
			exp.setTransportation(transportationD);
			exp.setTotal(totalD);

			// logger.debug("db Call start"+exp);
			System.out.println("Monthly Expenses" + exp);
			expensesService.saveExpensesDetails(exp);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Data Save Successfully");
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

}
