package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.BREService;

@Controller
@RequestMapping("/roaocpv")
public class BREControllerEncy {
	
	@Autowired
	BREService bresService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	private static Logger logger = LoggerFactory.getLogger(BREControllerEncy.class);
	@RequestMapping(value = "/createleadEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> createLead(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
    	JSONObject jsonObject=new JSONObject(decryptContainerString);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject createLead = new JSONObject();
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		if(fetchByApplicationId.getLeadId() != null) {
			
			JSONObject data = new JSONObject();
			JSONObject res = new JSONObject();
			data.put("is_success", true);
			data.put("data", res);
			res.put("loan_account_number", "D100026");
			res.put("lead_id",fetchByApplicationId.getLeadId());
			createLead.put("data", data.toString());
		}
		else {
			 createLead = bresService.createLead(applicationNo, Header);	
		}
//		JSONObject createLead = bresService.createLead(applicationNo, Header);
//		JSONObject createLead = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();
//
//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("loan_account_number", "D100026");
//		res.put("lead_id", "b7ea25d0-f4e1-4ce6-b848-d68f626b9117");
//		createLead.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (createLead != null) {
			String Data2 = createLead.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			String data1 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
			return new ResponseEntity<Object>(data3, h);

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
	
	@RequestMapping(value = "/fetchBreEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBre(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
    	JSONObject jsonObject=new JSONObject(decryptContainerString);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		
//		JSONObject fetchBre = bresService.fetchBre(applicationNo, Header);
		JSONObject fetchBre = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject res = new JSONObject();

		data.put("is_success", true);
		data.put("data", res);
		res.put("status", "Approved");
	
		fetchBre.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			String data1 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
			return new ResponseEntity<Object>(data3, h);

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
	
	@RequestMapping(value = "/equifaxReportEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxReport(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		//logger.debug("equifaxReportEncy start "+jsonRequest);
		//System.out.println("equifaxReportEncy start "+jsonRequest);
		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
    	JSONObject jsonObject1=new JSONObject(decryptContainerString);
		String applicationNo=jsonObject1.getJSONObject("Data").getString("ApplicationNo");	
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		
		logger.debug("equifaxReportEncy start fetchByApplicationId"+fetchByApplicationId.getLeadId());
		System.out.println("equifaxReportEncy start  fetchByApplicationId"+fetchByApplicationId.getLeadId());
		
		JSONObject	fetchBre=bresService.equifaxReport(applicationNo, Header, fetchByApplicationId.getLeadId());
		
		logger.debug("equifaxReportEncy end fetchByApplicationId"+fetchBre.toString());
		System.out.println("equifaxReportEncy end  fetchByApplicationId"+fetchBre.toString());
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			String data1 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
			return new ResponseEntity<Object>(data3, h);
		}
		else {

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
	
	
	
	@RequestMapping(value = "/brerulesEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> brerules(
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String applicationNo,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 	
			String key = X_Session_ID;
		JSONObject brerules = bresService.brerules(applicationNo);
		String Data = brerules.getString("Data");
		JSONObject Data1 = new JSONObject(Data);
		String data1 = Data1.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
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
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
			}
	}
	       
	@RequestMapping(value = "/checkstatusEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatusEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		
		logger.debug("checkstatusEncy strart"+jsonRequest);
		System.out.println("checkstatusEncy strart"+jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
    	JSONObject jsonObject=new JSONObject(decryptContainerString);
    	JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
    	String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
    	System.out.println(applicationNo);
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
//		System.out.println(applicationDetails);
		if(applicationDetails.getCustomerId() != null){
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			response.put("Message", "YOU ARE EXITING CUSTOMER");
			logger.debug("final response"+response.size());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
		if(applicationDetails.getLeadId() == null) {
			
			 JSONObject createLead = bresService.createLead(applicationNo, Header);
			 
			 HttpStatus h = HttpStatus.OK;
			 if (createLead != null) {
					String Data2 = createLead.getString("data");
					System.out.println("data2"+Data2);
					JSONObject Data1 = new JSONObject(Data2);

					System.out.println(Data1);

					if (Data1.has("data")) {
						h = HttpStatus.OK;
						String leadID = Data1.getJSONObject("data").getString("lead_id");
						logger.debug("checkstatusEncy start lead id"+leadID);
						System.out.println("checkstatusEncy start lead id"+leadID);
						if(applicationDetails.getLeadId() == null) {
							applicationDetails.setLeadId(leadID);
							applicationDetails.setIsLeadCreated("YES");
							applicationDetails.setIsBreRuring("NO");
							applicationDetails.setBreStatus("INITIATED");
							applicationDetailsService.save(applicationDetails);
						}
					} 
					else if (Data1.has("error")) {
						String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
						applicationDetails.setLeadId(leadId);
						applicationDetails.setIsLeadCreated("YES");
						applicationDetails.setIsBreRuring("NO");
						applicationDetails.setBreStatus("INITIATED");
						applicationDetailsService.save(applicationDetails);
						

					}
				}
				else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
		}
		else if(applicationDetails.getIsBreRuring().equalsIgnoreCase("YES") && applicationDetails.getBreResponse()==null)
		{
			logger.debug("checkstatusEncy start fetchBre");
			System.out.println("checkstatusEncy start fetchBre");
			JSONObject fetchBre = bresService.fetchBre(applicationNo, Header,applicationDetails.getLeadId());
			logger.debug("checkstatusEncy end fetchBre"+fetchBre.toString());
			System.out.println("checkstatusEncy end fetchBre"+fetchBre.toString());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				
				System.out.println(Data1);

				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String status = Data1.getJSONObject("data").getString("status");
					JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
					if(status.equalsIgnoreCase("Approved"))
					{
						applicationDetails.setBreResponse(breResp.toString());
						Object eligibilityAmount = breResp.getJSONObject("LOAN_ELIGIBILITY_AMOUNT_DERIVED").getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT");
						applicationDetails.setMaxEmiEligibility(eligibilityAmount.toString());
						applicationDetails.setBreStatus(status);
						applicationDetails.setFlowStatus("DD");
						applicationDetails.setBreResponse(breResp.toString());
						applicationDetailsService.save(applicationDetails);
					}
					else if(status.equalsIgnoreCase("Rejected"))
					{
						applicationDetails.setBreStatus(status);
						applicationDetails.setBreResponse(breResp.toString());
						applicationDetails.setStatus(status.toUpperCase());
						applicationDetailsService.save(applicationDetails);
					}

				} else if (Data1.has("error")) {
					h = HttpStatus.BAD_REQUEST;
					String erromsg=Data1.getJSONObject("error").getString("message").toString();
					
					
					JSONObject errr = new JSONObject();
					errr.put("Description",  erromsg);
					errr.put("Code",  h);
					JSONObject j = new JSONObject();
					j.put("Error", errr);
					

					String data1 = j.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					System.out.println(data3);
					
					
					
					return new ResponseEntity<Object>(data3, h);
				}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("LeadCreated", applicationDetails.getIsLeadCreated());
		response.put("isBreRuring", applicationDetails.getIsBreRuring());
		response.put("BreStatus", applicationDetails.getBreStatus());
		
		
		logger.debug("final response"+response.size());
		logger.debug("checkstatusEncy start response"+response.toString());
		System.out.println("checkstatusEncy start response"+response.toString());
		
		
		String data1 = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
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
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
			}
	}
}
