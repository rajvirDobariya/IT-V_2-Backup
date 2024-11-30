package com.suryoday.aocpv.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.ResourceNotFoundException;
import com.suryoday.aocpv.pojo.Preapproval;
import com.suryoday.aocpv.service.AocpService;

@CrossOrigin(origins = "http://10.20.30.50:4200")
@RestController
@RequestMapping("/aocpv/web/v1")
public class AppVersionWebEncyController {

	
	@Autowired
	AocpService  aocpvservice;
	
	 @RequestMapping(value="/checkappversionwebency", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> chekAppVersionweb(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)  throws Exception{
	
		System.out.println("welcome");
	
		String acc_type="";
		if(X_Request_ID.equals("AOCPV"))
		{
			
			//call db
			//select quey if found 
			//if app version is match 
			//return allow
			//else please update apk
			System.out.println("hi ");
			
//			
//			
			org.json.JSONObject jk = new org.json.JSONObject(bm);
			
			 org.json.JSONObject jm = jk.getJSONObject("Data");
			 JSONObject Data= new JSONObject();
			if(jm.getString("version").equals("1.0.0"))
			{
				JSONObject data1= new JSONObject();
				
				
				data1.put("allow", true);
				data1.put("message", "updated");
				Data.put("Data", data1);
				return new ResponseEntity<Object>(Data, HttpStatus.OK);
			}
			else
			{
				JSONObject data1= new JSONObject();
				data1.put("allow", false);
				data1.put("message", "Please Upgrade Application V1.0.0");
				
				Data.put("Error", data1);
				return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);
			}
			
			
			
			
		}
		else
		{ 
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
			
		}
		
//		List<Benificier>  l = new ArrayList<Benificier>();
//		List<BenificiaryInfo>  BenificiaryInfo1 = new ArrayList<BenificiaryInfo>();
//		
//		
//		
//	//	l= benificierService.FindAllCustomer(customerid,accounttype);
//		l= benificierService.viewcustmerbytype(customerid,acc_type);
//		if(l.size() == 0)
//		{
//			JSONObject Response = new JSONObject();
//			JSONObject Error = new JSONObject();
//			Error.put("Code", "100");
//			Error.put("Description", "No Record Found");
//			Response.put("Error", Error);
//			return new ResponseEntity<Object>(Response, HttpStatus.OK);
//		}
//		else
//		{
//			
//			for(int i = 0; i<l.size(); i++){
//				Benificier b=l.get(i);
//				
//				BenificiaryInfo b1= new BenificiaryInfo();
//				
//				
////				if(b.getType().equals("2"))
////				{
////					
////				}
////				else if(b.getType().equals("3"))
////				{
////					
////				}
//					
//					b1.setBeneficiaryId(b.getAccount());
//					b1.setNickName(b.getNickName());
//					b1.setBeneficiaryName(b.getName());
//					b1.setSequence(Integer.parseInt(b.getSequence()));
//					b1.setBeneficiaryEmailId(b.getEmailId());
//					b1.setStatus(b.getStatus());
//					b1.setBeneficiaryMobileNumber(b.getMobileNumber());
//					b1.setBeneficiaryMaxLimit(b.getMaxLimit());
//					b1.setBeneficiaryBank(b.getBankName());
//					b1.setBeneficiaryBankCity(b.getBankCity());
//					b1.setBeneficiaryBranch(b.getBankBranch());
//					b1.setBeneficiaryBankIfsc(b.getBankIfsc());
//				
//				
//				
//				
//				BenificiaryInfo1.add(b1);
//			    
//			}
//			
//			
//			
//			
//		
//			
//			JSONObject BenificiaryInfo= new JSONObject();
//			BenificiaryInfo.put("BenificiaryInfo", BenificiaryInfo1);
//			BenificiaryInfo.put("TransactionCode", "00");
//			
			
			
		}
		
	 
		@PostMapping("/preapprovallistwebencry")
		public ResponseEntity<Object> getalldataweb(@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
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
		
	}
	
	
	
	 

