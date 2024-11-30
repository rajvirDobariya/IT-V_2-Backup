package com.suryoday.aocpv.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.exceptionhandling.ResourceNotFoundException;
import com.suryoday.aocpv.pojo.AppVersion;
import com.suryoday.aocpv.pojo.Preapproval;
import com.suryoday.aocpv.service.AocpService;
import com.suryoday.aocpv.service.AppVersionService;
import com.suryoday.aocpv.service.MdmBlockingService;


@RestController
@RequestMapping("/aocpv/v1")
public class AppVersionController {

	
	Logger logger = LoggerFactory.getLogger(AppVersionController.class);
	
	@Autowired
	AocpService  aocpvservice;
	
	@Autowired
	AppVersionService appVersionService;
	
	@Autowired
	MdmBlockingService blockingService;
	
	

	
	 @RequestMapping(value="/checkappversion", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> chekAppVersion(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String deviceId,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Package-ID", required = true) String X_Package_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest request)  throws Exception{
	
		 
		//if atritbute name alredy preset update
		 //else create new 
//		 List<String> messages = (List<String>) request.getAttribute(X_User_ID);
//
//		 
//			if (messages == null) {
//				messages = new ArrayList<>();
//			}
//			
//          
//		 request.getSession().setAttribute(X_User_ID, X_User_ID);
		 
		 logger.debug("start request"+bm.toString());
			
			String acc_type="";
			if(X_Request_ID.equals("AOCPV"))
			{
			 	org.json.JSONObject sendAuthenticateResponse= new org.json.JSONObject();
		    sendAuthenticateResponse=blockingService.samsungknox();
		    	String Bearer = sendAuthenticateResponse.getString("access_token");
		   	 org.json.JSONObject AuthenticateResponse= new org.json.JSONObject();
		   	String  deviceID=deviceId.toLowerCase();
				AuthenticateResponse = blockingService.tab(Bearer,deviceID);
				
				String result = AuthenticateResponse.getString("resultMessage");
				if(result.equalsIgnoreCase("Device - Not found.")) {
					throw new NoSuchElementException("your device is not in mdm system please contact Support Team.");
				}
				org.json.JSONObject jk = new org.json.JSONObject(bm);
				 org.json.JSONObject jm = jk.getJSONObject("Data");
				 JSONObject Data= new JSONObject();
				 AppVersion appVersion	=appVersionService.findVersion();
				 if(appVersion.getAppId().equals(X_Package_ID)) {
						
					 String versionResponse = appVersion.getAppVersion();
					 String versionRequest = jm.getString("version"); 
					 if(versionResponse.equals(versionRequest)) {
						 JSONObject data1= new JSONObject();
							data1.put("allow", true);
							data1.put("message", "updated");
							Data.put("Data", data1);
							return new ResponseEntity<Object>(Data, HttpStatus.OK);
					 }
					 else {
//						 JSONObject data1= new JSONObject();
//						 	data1.put("allow", false);
//							data1.put("message", "Please Upgrade Application");
//							Data.put("Error", data1);
//							return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
						 throw new NoSuchElementException("Please Upgrade Application V"+appVersion.getAppVersion());
					 }
//					 if(versionResponse.charAt(0)==versionRequest.charAt(0)){
//						 
//						 if(versionResponse.charAt(2)==versionRequest.charAt(2)) {
//							 JSONObject data1= new JSONObject();
//								data1.put("allow", true);
//								data1.put("message", "updated");
//								Data.put("Data", data1);
//								return new ResponseEntity<Object>(Data, HttpStatus.OK);
//						 }
//						 else {
//							 JSONObject data1= new JSONObject();
//							 	data1.put("allow", false);
//								data1.put("message", "Please Upgrade Application");
//								
//								Data.put("Error", data1);
//								return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
//						 }
//					 }
//					 else {
//						 	JSONObject data1= new JSONObject();
//						 	data1.put("allow", false);
//							data1.put("message", "Please Upgrade Application");
//							
//							Data.put("Error", data1);
//							return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
//					 }

					}
					else {
						JSONObject data1= new JSONObject();
						data1.put("allow", false);
						data1.put("message", "Invalid appId");
						
						Data.put("Error", data1);
						logger.debug("final response"+Data.toString());
						return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
					}
				
			}
			else
			{ 
				logger.debug("Invalid Request"+ HttpStatus.BAD_REQUEST);
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
				
			}

			
//			List<Benificier>  l = new ArrayList<Benificier>();
//			List<BenificiaryInfo>  BenificiaryInfo1 = new ArrayList<BenificiaryInfo>();
//			
//			
//			
//		//	l= benificierService.FindAllCustomer(customerid,accounttype);
//			l= benificierService.viewcustmerbytype(customerid,acc_type);
//			if(l.size() == 0)
//			{
//				JSONObject Response = new JSONObject();
//				JSONObject Error = new JSONObject();
//				Error.put("Code", "100");
//				Error.put("Description", "No Record Found");
//				Response.put("Error", Error);
//				return new ResponseEntity<Object>(Response, HttpStatus.OK);
//			}
//			else
//			{
//				
//				for(int i = 0; i<l.size(); i++){
//					Benificier b=l.get(i);
//					
//					BenificiaryInfo b1= new BenificiaryInfo();
//					
//					
////					if(b.getType().equals("2"))
////					{
////						
////					}
////					else if(b.getType().equals("3"))
////					{
////						
////					}
//						
//						b1.setBeneficiaryId(b.getAccount());
//						b1.setNickName(b.getNickName());
//						b1.setBeneficiaryName(b.getName());
//						b1.setSequence(Integer.parseInt(b.getSequence()));
//						b1.setBeneficiaryEmailId(b.getEmailId());
//						b1.setStatus(b.getStatus());
//						b1.setBeneficiaryMobileNumber(b.getMobileNumber());
//						b1.setBeneficiaryMaxLimit(b.getMaxLimit());
//						b1.setBeneficiaryBank(b.getBankName());
//						b1.setBeneficiaryBankCity(b.getBankCity());
//						b1.setBeneficiaryBranch(b.getBankBranch());
//						b1.setBeneficiaryBankIfsc(b.getBankIfsc());
//					
//					
//					
//					
//					BenificiaryInfo1.add(b1);
//				    
//				}
//				
//				
//				
//				
//			
//				
//				JSONObject BenificiaryInfo= new JSONObject();
//				BenificiaryInfo.put("BenificiaryInfo", BenificiaryInfo1);
//				BenificiaryInfo.put("TransactionCode", "00");
//				
				
				
			}
			
		 
			@PostMapping("/preapprovallist")
			public ResponseEntity<Object> getalldata(@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
					 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
					 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
					 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
					 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
					 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)  throws  ResourceNotFoundException{
			
				System.out.println("welcome");
				 JSONObject Data= new JSONObject();
				 List<Preapproval> a=	 aocpvservice.preapprovallist();
				 if(a.size()==0)
				 {
					    JSONObject data1= new JSONObject();
						data1.put("code",  HttpStatus.BAD_REQUEST);
						data1.put("message", "No Record Found");
						
						Data.put("Error", data1);
					 return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
				 }
				 else
				 {
				
						Data.put("Data", a);
					 return new ResponseEntity<Object>(Data, HttpStatus.OK);
				 }
					 
					
			}
			
			
			
			
			
			 @RequestMapping(value="/checkdata/{id}", method = RequestMethod.GET,produces = "application/json")
				public ResponseEntity<Object> chekAppVersion(@PathVariable ("id") String id,HttpServletRequest request)  throws Exception{
				
				 
					 
					//if atritbute name alredy preset update
					 //else create new 
				 //db call check id present 0fbf490f-72f4-4bfd-b33d-d4f68d2f9fc3
				 //check id present in db or not
				 //if not then create session
				 //if present check session valid or not 
				 //if valid proceed
				 //if not invalid session
				 
					 List<String> messages = (List<String>) request.getAttribute(id);

					 
						if (messages == null) {
							messages = new ArrayList<>();
						}
						
			          
					 request.getSession().setAttribute(id, id);
					 
					 return  new ResponseEntity<Object>("ok", HttpStatus.OK);
					 

			 }
			 @RequestMapping(value = "/deviceinfo", method = RequestMethod.POST , produces = "application/json")
			 
			    public ResponseEntity<Object> tabres(
			    		@RequestHeader(name = "X-Bearer-ID", required = true) String Bearer,
						@RequestHeader(name = "X-deviceId-ID", required = true) String deviceId) throws IOException {
			    			
				 org.json.JSONObject sendAuthenticateResponse= new org.json.JSONObject();
			    					sendAuthenticateResponse = blockingService.tab(Bearer,deviceId);
			    	logger.debug("Response in controller: "+sendAuthenticateResponse);
			    	
					return new ResponseEntity<>(sendAuthenticateResponse.toString(), HttpStatus.OK);
			    }
			    
			    @RequestMapping(value = "/samsungknox", method = RequestMethod.POST , produces = "application/json")
			    public ResponseEntity<Object> samsungknox() throws IOException {
			    			
			    	org.json.JSONObject sendAuthenticateResponse= new org.json.JSONObject();
			    	sendAuthenticateResponse=blockingService.samsungknox();
			    	logger.debug("Response in controller: "+sendAuthenticateResponse);
			    	
					return new ResponseEntity<>(sendAuthenticateResponse.toString(), HttpStatus.OK);
			    }
		}
		


		
		
		 

