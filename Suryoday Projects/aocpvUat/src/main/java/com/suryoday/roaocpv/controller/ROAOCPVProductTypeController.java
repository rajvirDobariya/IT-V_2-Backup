package com.suryoday.roaocpv.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.roaocpv.pojo.ROAOCPVProductType;
import com.suryoday.roaocpv.service.ROAOCPVProductTypeService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVProductTypeController {
	@Autowired
	ROAOCPVProductTypeService roaocpvProductTypeService;

	@RequestMapping(value = "/fetchByCategoryType", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCategoryType(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		System.out.println("fetchByAll start");
		System.out.println("request" + jsonRequest);

		if (jsonRequest.isEmpty()) {
			System.out.println("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String productType = jsonObject.getJSONObject("Data").getString("ProductType");
		System.out.println("db Call start");
		List<ROAOCPVProductType> roaocpvProductType = roaocpvProductTypeService.fetchByCategoryType(productType);
		System.out.println("db Call end" + roaocpvProductType);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", roaocpvProductType);
		System.out.println("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByAll", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByAll(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		System.out.println("fetchByAll start");
		System.out.println("request" + jsonRequest);

		if (jsonRequest.isEmpty()) {
			System.out.println("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		System.out.println("db Call start");
		List<ROAOCPVProductType> roaocpvProductType2 = roaocpvProductTypeService.fetchByAll();
		System.out.println("db Call end" + roaocpvProductType2);
		org.json.simple.JSONObject response2 = new org.json.simple.JSONObject();
		response2.put("Data", roaocpvProductType2);
		System.out.println("final response" + response2.toString());
		return new ResponseEntity<Object>(response2, HttpStatus.OK);

	}

}
