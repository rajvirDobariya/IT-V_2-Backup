package com.suryoday.aocpv.controller;

import java.util.List;

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
import com.suryoday.aocpv.pojo.Charges;
import com.suryoday.aocpv.service.ChargesService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.service.ApplicationDetailsService;


@Component
@RestController
@RequestMapping(value="aocpv")
public class ChargesController {
	
	private static Logger logger = LoggerFactory.getLogger(ChargesController.class);
	
	@Autowired
	ChargesService chargesservice;
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	@RequestMapping(value="/fetchByProductCode", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByProductCode(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		System.out.println("fetchByProductCode start");
		System.out.println("request"+jsonRequest);
		
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String loanAmount = jsonObject.getJSONObject("Data").getString("LoanAmount");
		String tenure = jsonObject.getJSONObject("Data").getString("Tenure");
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		System.out.println("db Call start");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		fetchByApplicationId.setFlowStatus(flowStatus);
		fetchByApplicationId.setTenure(tenure);
		fetchByApplicationId.setAmount(loanAmount);
		applicationDetailsService.save(fetchByApplicationId);
		List<Charges> list=chargesservice.fetchByProductCode(loanAmount,tenure);
		Charges charges = list.get(0); 
		
		System.out.println("db Call end"+list);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", charges);
		System.out.println("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/saveProductCode", method = RequestMethod.POST)
	public ResponseEntity<Object> saveProductCode(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		System.out.println("saveProductCode start");
		System.out.println("request"+jsonRequest);
		
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String productCode = jsonObject.getJSONObject("Data").getString("ProductCode");
		String netDisbustment = jsonObject.getJSONObject("Data").getString("netDisbustment");
		String fireInsurance = jsonObject.getJSONObject("Data").getString("fireInsurance");
		
		System.out.println("db Call start");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		fetchByApplicationId.setProductCode(productCode);
		fetchByApplicationId.setFlowStatus("CHARGES");
		fetchByApplicationId.setNetDisbustment(netDisbustment);
		fetchByApplicationId.setFireInsurance(fireInsurance);
		fetchByApplicationId.setTermInsurance("YES");
		applicationDetailsService.save(fetchByApplicationId);
		
		System.out.println("db Call end");
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", "save");
		System.out.println("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	
	}
}
