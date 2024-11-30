package com.suryoday.aocpv.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.excelToDatabase.ExcelToJava;
import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.service.PreApprovalListService;




@RestController
@CrossOrigin("*")
@RequestMapping("/aocpv/v1")
public class LoanInputControllerEncy {
	Logger logger = LoggerFactory.getLogger(LoanInputControllerEncy.class);
	@Autowired
	private LoanInputService loanInputService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	PreApprovalListService loanInputServiceTw;
	
	@PostMapping("/LoanInput/uploadency")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		if(ExcelToJava.checkExcelformat(file)) {
			logger.debug("upload excel Data api start");
			this.loanInputService.save(file);
			
			
			logger.debug("file is upload");
		
			return ResponseEntity.status(HttpStatus.OK).body("file is uploaded and data is save to db");
			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please upload excel data");
	}
	
	@GetMapping("/getAllency")
	public List<PreApprovedListVikasLoan> getAllDetails(){
		return loanInputService.getAllProduct();
	}
	
	@PostMapping("/getByIdAndStatusency")
	public ResponseEntity<Object> getByIdAndstatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
				//logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
						
					JSONObject jsonObject=new JSONObject(decryptContainerString);
					logger.debug("getByIdAndStatus start");	
					logger.debug("request Data "+decryptContainerString);	
					 String branchId = jsonObject.getJSONObject("Data").getString("BranchID");
					 String status = jsonObject.getJSONObject("Data").getString("Status");
					 String productCode = jsonObject.getJSONObject("Data").getString("productCode");				
					long branchId1 = Long.parseLong(branchId);
		
								List<LoanDetail> list=null;
								if(productCode.equalsIgnoreCase("VL")) {
									 list=loanInputService.getByIdAndstatus(branchId1,status,productCode);
								}else {
									 list=loanInputServiceTw.getByIdAndstatus(branchId1,status,productCode);
								}
								int count=list.size();
								logger.debug("final response "+count);	
		
								
								 JSONArray j= new JSONArray(list);
	                                org.json.simple.JSONObject response = new org.json.simple.JSONObject();
	                              //  response.put("status", HttpStatus.OK);
	                                response.put("Data", j);
	                              //  logger.debug(response.toString());	
							//	org.json.simple.JSONObject response = new org.json.simple.JSONObject();
							//	response.put("status", HttpStatus.OK);
							//	response.put("Data", list);
							//	logger.debug(response.toString());
								
								String data = response.toString();
								String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
								org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
								data2.put("value", encryptString2);
								org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
								data3.put("Data", data2);
								//System.out.println(data3);
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
	
	@PostMapping("/updateStatusency")
	public ResponseEntity<Object> updateStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		//System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());
		String key = X_Session_ID;
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
				
		logger.debug("updateStatus start");	
		logger.debug("request Data "+decryptContainerString);
		JSONObject jsonObject=new JSONObject(decryptContainerString);
		
		 String branchId = jsonObject.getJSONObject("Data").getString("BranchID");
		 String status = jsonObject.getJSONObject("Data").getString("Status");
		 String customerID = jsonObject.getJSONObject("Data").getString("customerID");

		 PreApprovedListVikasLoan loanDetails=loanInputService.getByReferenceNo(customerID);
		loanDetails.setStatus(status);
		
		LocalDate date=LocalDate.now();
		loanDetails.setUpdatedate(date);
		PreApprovedListVikasLoan details=loanInputService.saveSingleData(loanDetails);
		logger.debug("status change"+status);
		logger.debug("updateStatus end");
		JSONObject j=new JSONObject(details);
		 //JSONArray j= new JSONArray(details);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		//response.put("status", HttpStatus.OK);
		response.put("Data", j);
		//logger.debug(response.toString());
		
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		//System.out.println(data3);
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
	
	
	@RequestMapping(value="/findByMobileAndBranchIdency", method = RequestMethod.POST)
	public ResponseEntity<Object> findByMobile(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println("hiiiii"+encryptJSONObject);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		//System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());
		String key = X_Session_ID;
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		logger.debug("findByMobileAndBranchId start");	
		logger.debug("request Data "+decryptContainerString);
		JSONObject jsonObject=new JSONObject(decryptContainerString);
		 String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		 String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		 if(mobile.isEmpty() || branchId.isEmpty()) {
			 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				//response.put("status", HttpStatus.OK);
				response.put("message", "mobile no could not be empty");
				logger.debug(response.toString());	
				return new ResponseEntity<Object>(response,HttpStatus.OK);
		 }
		 
		 		long mobilePhone = Long.parseLong(mobile);
		 		long branchIdInLong = Long.parseLong(branchId);
		 List<LoanDetail> loanDetail =loanInputService.findByMobilePhone(mobilePhone,branchIdInLong);
		 			
//		 			 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
//		 	 		response.put("Data", loanDetail);
		 						//logger.debug(loanDetail.toString());	
		 						logger.debug("findByMobileAndBranchId end");
		 						JSONObject j=new JSONObject(loanDetail);
		 						//JSONArray j= new JSONArray(loanDetail);
		 						org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		 						//response.put("status", HttpStatus.OK);
		 						response.put("Data", j);
		 						//logger.debug(response.toString());
		 						
		 						String data = response.toString();
		 						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		 						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		 						data2.put("value", encryptString2);
		 						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		 						data3.put("Data", data2);
		 						//System.out.println(data3);
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
