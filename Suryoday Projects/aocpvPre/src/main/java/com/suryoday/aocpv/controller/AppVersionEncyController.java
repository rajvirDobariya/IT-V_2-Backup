package com.suryoday.aocpv.controller;

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
import com.suryoday.connector.rd.util.AppzillonAESUtils;

@RestController
@RequestMapping("/aocpv/v1")
public class AppVersionEncyController {

	private static Logger logger = LoggerFactory.getLogger(AppVersionEncyController.class);

	@Autowired
	AocpService aocpvservice;
	
	@Autowired
	MdmBlockingService blockingService;

	@Autowired
	AppVersionService appVersionService;
	

	@RequestMapping(value = "/checkappversionency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> chekAppVersion(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String deviceId,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Package-ID", required = true) String X_Package_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

		logger.debug("start request" + bm.toString());

		String key = X_Transaction_ID;

		
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

		String acc_type = "";
		if (X_Request_ID.equals("AOCPV")) {
			String data = "";
			try {
			org.json.JSONObject sendAuthenticateResponse= new org.json.JSONObject();
	    	sendAuthenticateResponse=blockingService.samsungknox();
	    	String Bearer = sendAuthenticateResponse.getString("access_token");
	    	if(Bearer.equals("")) {
	    		throw new NoSuchElementException("SAMSUNGKNOW NOT WORKING PLEASE WAIT");
	    	}
	   	 org.json.JSONObject AuthenticateResponse= new org.json.JSONObject();
	   	String  deviceID=deviceId.toLowerCase();
			AuthenticateResponse = blockingService.tab(Bearer,deviceID);
			
			String result = AuthenticateResponse.getString("resultMessage");
			if(result.equalsIgnoreCase("Device - Not found.")) {
				throw new NoSuchElementException("YOUR DEVICE IS NOT IN MDM SERVICE KINDLY CONTACT DESKTOP SUPPORT TEAM.");
			}
			}catch (Exception e) {
				throw new NoSuchElementException("your device is not in mdm system please contact Support Team.");
			}
			org.json.JSONObject jk = new org.json.JSONObject(decryptContainerString);
			org.json.JSONObject jm = jk.getJSONObject("Data");
			JSONObject Data = new JSONObject();
			AppVersion appVersion = appVersionService.findVersion();
			if (appVersion.getAppId().equals(X_Package_ID)) {
				
				String versionResponse = appVersion.getAppVersion();
				String versionRequest = jm.getString("version");
				if(versionResponse.equals(versionRequest)) {
					 JSONObject data1= new JSONObject();
						data1.put("allow", true);
						data1.put("message", "updated");
						Data.put("Data", data1);
						data = Data.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.JSONObject data2 = new org.json.JSONObject();
						data2.put("value", encryptString2);
						org.json.JSONObject data3 = new org.json.JSONObject();
						data3.put("Data", data2);
						//logger.debug("response : " + data3.toString());
						return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
				 }
				 else {
//					 JSONObject data1= new JSONObject();
//					 	data1.put("allow", false);
//						data1.put("message", "Please Upgrade Application");
//						Data.put("Error", data1);
//						data = Data.toString();
//						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//						org.json.JSONObject data2 = new org.json.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.JSONObject data3 = new org.json.JSONObject();
//						data3.put("Data", data2);
//						logger.debug("response : " + data3.toString());
//						return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
					 throw new NoSuchElementException("Please Upgrade Application V"+appVersion.getAppVersion());
					 
				 }
//				if (versionResponse.charAt(0) == versionRequest.charAt(0)) {
//
//					if (versionResponse.charAt(2) == versionRequest.charAt(2)) {
//						JSONObject data1 = new JSONObject();
//						data1.put("allow", true);
//						data1.put("message", "updated");
//						Data.put("Data", data1);
//						data = Data.toString();
//						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//						org.json.JSONObject data2 = new org.json.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.JSONObject data3 = new org.json.JSONObject();
//						data3.put("Data", data2);
//						logger.debug("response : " + data3.toString());
//						return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
//					} else {
//						JSONObject data1 = new JSONObject();
//						data1.put("allow", false);
//						data1.put("message", "Please Upgrade Application");
//
//						Data.put("Error", data1);
//						data = Data.toString();
//						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//						org.json.JSONObject data2 = new org.json.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.JSONObject data3 = new org.json.JSONObject();
//						data3.put("Data", data2);
//						logger.debug("response : " + data3.toString());
//						return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
//					}
//				} else {
//					JSONObject data1 = new JSONObject();
//					data1.put("allow", false);
//					data1.put("message", "Please Upgrade Application");
//
//					Data.put("Error", data1);
//					data = Data.toString();
//					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//					org.json.JSONObject data2 = new org.json.JSONObject();
//					data2.put("value", encryptString2);
//					org.json.JSONObject data3 = new org.json.JSONObject();
//					data3.put("Data", data2);
//					logger.debug("response : " + data3.toString());
//					return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
//				}

			} else {
				JSONObject data1 = new JSONObject();
				data1.put("allow", false);
				data1.put("message", "Invalid appId");

				Data.put("Error", data1);
				//logger.debug("final response" + Data.toString());
				data = Data.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				logger.debug("response : " + data3.toString());
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
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

	@PostMapping("/preapprovallistency")
	public ResponseEntity<Object> getalldata(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)
			throws ResourceNotFoundException {

		
		//System.out.println("welcome");
		JSONObject Data = new JSONObject();
		List<Preapproval> a = aocpvservice.preapprovallist();
		if (a.size() == 0) {
			JSONObject data1 = new JSONObject();
			data1.put("code", HttpStatus.BAD_REQUEST);
			data1.put("message", "No Record Found");

			Data.put("Error", data1);
			return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
		} else {

			Data.put("Data", a);
			return new ResponseEntity<Object>(Data, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/checkdataency/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> chekAppVersion(@PathVariable("id") String id, HttpServletRequest request)
			throws Exception {

		// if atritbute name alredy preset update
		// else create new
		List<String> messages = (List<String>) request.getAttribute(id);

		if (messages == null) {
			messages = new ArrayList<>();
		}

		request.getSession().setAttribute(id, id);

		return new ResponseEntity<Object>("ok", HttpStatus.OK);

	}
	

}
