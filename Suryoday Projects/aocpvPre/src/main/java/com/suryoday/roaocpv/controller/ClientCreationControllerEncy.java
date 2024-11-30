package com.suryoday.roaocpv.controller;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.BRNetMasters;
import com.suryoday.roaocpv.service.BRNetMastersService;
import com.suryoday.roaocpv.service.ClientCreationService;
@Component
@RestController
@RequestMapping(value="roaocpv")
public class ClientCreationControllerEncy {
	
	@Autowired
	ClientCreationService clientcreationservice;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BRNetMastersService brmastersservice;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	private static Logger logger = LoggerFactory.getLogger(ClientCreationControllerEncy.class);
	
	@RequestMapping(value = "/clientCreationEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> faceMatch(@RequestBody String bm,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		Header.put("Content-Type", ContentType);

		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		JSONObject clientCreation = clientcreationservice.clientCreation(applicationNo ,Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (clientCreation != null) {
			String Data2 = clientCreation.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONArray Resp = new JSONArray(Data2);
			JSONObject Data1 = Resp.getJSONObject(0);
			String client = Data1.getString("ClientID");
			AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(Long.parseLong(applicationNo));
			aocpvLoanCreation.setClientId(client);
			aocpvLoanCreationService.saveData(aocpvLoanCreation);
			org.json.simple.JSONObject response=new org.json.simple.JSONObject();
			response.put("Data",Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				String	data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			String	data = response.toString();
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
	
	@RequestMapping(value="/zipCodeMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> zipCodeMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchzipCodeMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="ZipCodeID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/cityMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> cityMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchcityMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="CityID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/stateMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> stateMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchstateMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="StateID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/districtMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> districtMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchdistrictMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="DistrictID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/subDistrictMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> subDistrictMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchsubDistrictMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="SubDistrictID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/occupationMasterEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> occupationMaster(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req) {

		System.out.println("fetchoccupationMaster start");
		System.out.println("request"+jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		System.out.println("db Call start");
		String id="OccupationID";
		List<BRNetMasters> list = brmastersservice.fetchMaritalStatus(id);
		System.out.println(list);
		JSONArray array=new JSONArray(list);
		System.out.println(array);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", array);
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/saveMasterDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveMasterData(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String ApplicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest req)	{
		System.out.println("saveMasterData start");
		System.out.println("request"+jsonRequest);
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		System.out.println("db Call start");
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String zipCode = jsonObject.getJSONObject("Data").getString("ZipCode");
		String city = jsonObject.getJSONObject("Data").getString("City");
		String state = jsonObject.getJSONObject("Data").getString("State");
		String district = jsonObject.getJSONObject("Data").getString("District");
		String subDistrict = jsonObject.getJSONObject("Data").getString("SubDistrict");
		String occupation = jsonObject.getJSONObject("Data").getString("Occupation");
		brmastersservice.saveData(jsonObject.getJSONObject("Data").toString(),ApplicationNo);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", "Save Data Successfully");
		System.out.println("final response"+response.toString());
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
