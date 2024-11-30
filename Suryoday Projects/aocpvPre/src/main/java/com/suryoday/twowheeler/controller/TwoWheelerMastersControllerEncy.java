package com.suryoday.twowheeler.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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
import com.suryoday.twowheeler.pojo.AssetMaster;
import com.suryoday.twowheeler.pojo.DealerByPincode;
import com.suryoday.twowheeler.pojo.DealerMaster;
import com.suryoday.twowheeler.pojo.SchemeMaster;
import com.suryoday.twowheeler.pojo.TWModelMaster;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwoWheelerMastersService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;

@RestController
@RequestMapping("/twowheeler")
public class TwoWheelerMastersControllerEncy {
	
	private static Logger logger = LoggerFactory.getLogger(TwoWheelerMastersControllerEncy.class);
	
	@Autowired
	TwoWheelerMastersService twowheelermastersservice;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;
	
	@RequestMapping(value = "/fetchModelMasterEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchVpaCodesAll(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {

	
		System.out.println("fetchModelMaster start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
		List<TWModelMaster> list=twowheelermastersservice.fetchModelMaster();
		JSONArray j= new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data",j); 
		
//		System.out.println("final response" + response.toString());
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
		}
	
	@RequestMapping(value="/fetchBySchemeNameEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByScheme(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String schemeName = jsonObject.getJSONObject("Data").getString("SchemeName");
		SchemeMaster master=twowheelermastersservice.fetchBySchemeCode(schemeName);
		System.out.println("db Call end" + master);
		JSONObject j= new JSONObject(master);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", j);
		logger.debug("final response"+response.size());
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
	}
	
	@RequestMapping(value = "/fetchManufactureEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchManufacture(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		List<String> list=twowheelermastersservice.fetchManufacture();
		JSONArray j= new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data",j); 
		
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
		}
	
	@RequestMapping(value = "/fetchSchemeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchSchemeDesc(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		List<String> list=twowheelermastersservice.fetchSchemeDesc();
		JSONArray j= new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data",j); 
		
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
		}
	
	@RequestMapping(value = "/fetchModelEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchModel(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
		String manufacturerName = jsonObject.getJSONObject("Data").getString("MaufactureName");
		List<String> list=twowheelermastersservice.fetchModel(manufacturerName);
		JSONArray j= new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data",j); 
		
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
		}
	
	@RequestMapping(value="/fetchByVariantEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchByVariant(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String VarinantName = jsonObject.getJSONObject("Data").getString("VarinantName");
		AssetMaster master=twowheelermastersservice.fetchByVariant(VarinantName);
		JSONObject j=new JSONObject(master);
	//	System.out.println("db Call end" + list);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);

		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
	}
	@RequestMapping(value = "/fetchDealerBypincodeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchDealerBypincode(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String manufacturer = jsonObject.getJSONObject("Data").getString("manufacturer");
//			String address=twowheelerDetailsService.fetchAddress(applicationNo);
//			String pincode="";
//			if(address != null) {
//				org.json.JSONArray addressArray =new org.json.JSONArray(address);
//				for(int n=0;n<addressArray.length();n++) {
//					JSONObject addressInJson = addressArray.getJSONObject(n);
//					if(addressInJson.getString("addressType").equalsIgnoreCase("CURRENT ADDRESS")) {
//						 pincode = addressInJson.getString("pincode");
//					}
//				}
//			}
//			List<DealerByPincode> list = twowheelermastersservice.fetchDealerBypincode(pincode);
			
			TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
			List<DealerByPincode> listResponse=new ArrayList<>();
			List<DealerByPincode> list = twowheelermastersservice.fetchAllDealer(twowheelerDetailesTable.getSalesBranchId(),manufacturer);
			for(DealerByPincode dealerByPincode:list) {
				String branchIds = dealerByPincode.getBranchIdArray();
				if(branchIds != null) {
					org.json.JSONArray branchIdArray =new org.json.JSONArray(branchIds);
					for(int n=0;n<branchIdArray.length();n++) {
						String branchId = branchIdArray.getString(n);
						if(branchId.equals(twowheelerDetailesTable.getSalesBranchId())) {
							listResponse.add(dealerByPincode);
						}
					}
				}
			}		
		JSONArray j=new JSONArray(listResponse);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
	}


	@RequestMapping(value = "/fetchByDealerCodeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByDealerCode(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
		String DealerCode = jsonObject.getJSONObject("Data").getString("DealerCode");
		List<DealerMaster> list = twowheelermastersservice.fetchByDealerCode(DealerCode);
		JSONArray j=new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
	}
	
	@RequestMapping(value = "/fetchVarientEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchVarient(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	 userService.getSessionId(X_User_ID, request); 
		String key = X_Session_ID;
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			String modelName = jsonObject.getJSONObject("Data").getString("modelName");
			List<String> list=twowheelermastersservice.fetchVarient(modelName);
		JSONArray j= new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data",j); 
		
		String	data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
	        else {
	            org.json.JSONObject data2 = new org.json.JSONObject();
	            data2.put("value", "SessionId is expired or Invalid sessionId");
	            org.json.JSONObject data3 = new org.json.JSONObject();
	            data3.put("Error", data2);
	            logger.debug("SessionId is expired or Invalid sessionId");
	            return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	        }
		}
}
