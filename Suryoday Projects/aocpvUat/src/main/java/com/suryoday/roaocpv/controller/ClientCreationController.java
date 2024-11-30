package com.suryoday.roaocpv.controller;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.pojo.BRNetMasters;
import com.suryoday.roaocpv.service.BRNetMastersService;
import com.suryoday.roaocpv.service.ClientCreationService;

@Component
@RestController
@RequestMapping(value = "roaocpv")
public class ClientCreationController {

	@Autowired
	ClientCreationService clientcreationservice;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	private static Logger logger = LoggerFactory.getLogger(ClientCreationController.class);

	@RequestMapping(value = "/clientCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> faceMatch(@RequestBody String bm,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		JSONObject clientCreation = clientcreationservice.clientCreation(applicationNo, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (clientCreation != null) {
			String Data2 = clientCreation.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONArray Resp = new JSONArray(Data2);
			JSONObject Data1 = Resp.getJSONObject(0);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			if (Data1.getBoolean("Response")) {
				String client = Data1.getString("ClientID");
				AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService
						.findByApplicationNo(Long.parseLong(applicationNo));
				aocpvLoanCreation.setClientId(client);
				aocpvLoanCreationService.saveData(aocpvLoanCreation);
				response.put("Data", Data1);
			} else {
				String errormsg = Data1.getString("ResponseMsg");
				response.put("ErrorMessage", errormsg);
				h = HttpStatus.BAD_REQUEST;
			}
			if (Data1.has("Data")) {
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(response.toString(), h.OK);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Autowired
	BRNetMastersService brmastersservice;

	@RequestMapping(value = "/fetchBrNetMasters", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchBrNetMasters(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		System.out.println("fetchCenterStatusMasters start");
		System.out.println("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			System.out.println("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String categotyType = jsonObject.getJSONObject("Data").getString("CategoryType");
		String param = jsonObject.getJSONObject("Data").getString("Parameter");
		System.out.println("db Call start");
		BRNetMasters brNetMasters = brmastersservice.fetchBrNetMasters(categotyType, param);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", brNetMasters);
		System.out.println("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
