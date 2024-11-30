package com.suryoday.twowheeler.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.twowheeler.service.IfscService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Controller
@RestController
@RequestMapping("/twowheeler")
public class IfscController {
	private static Logger logger = LoggerFactory.getLogger(IfscController.class);
	
	@Autowired
	IfscService ifscservice;
	
	@Autowired
	TwowheelerFamilyMemberService familyMemberService;
	
	@RequestMapping(value = "/searchIfsc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> searchIfsc(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);
		String ifscCode = jsonObject.getJSONObject("Data").getString("IfscCode");
		JSONObject searchIfsc = ifscservice.searchIfsc(ifscCode ,Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (searchIfsc != null) {
			String Data2 = searchIfsc.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				JSONObject bankRespJson = Data1.getJSONArray("Data").getJSONObject(0).getJSONObject("Bank");
				System.out.println(bankRespJson);
				String bankName = bankRespJson.getString("Description");
				String branchName=bankRespJson.getString("BranchDesc");
				System.out.println("Bank Name :"+bankName+ "BranchName :"+branchName );
				
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	
	@RequestMapping(value = "/pennyDrop", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> pennyDrop(@RequestBody String bm,
	@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
	@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
	@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
	@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
	@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
	@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
	throws Exception{
		logger.debug("pennyDrop start");
		logger.debug("pennyDrop request");
		JSONObject Header= new JSONObject();
		Header.put("X-Correlation-ID",X_CORRELATION_ID );
		Header.put("X-From-ID",X_From_ID );
		Header.put("X-To-ID",X_To_ID );
		Header.put("X-Transaction-ID",X_Transaction_ID );
		Header.put("X-User-ID",X_User_ID );
		Header.put("X-Request-ID",X_Request_ID );
	 if(X_Request_ID.equals("AVAIL"))
		{	
		 	JSONObject jsonObject=new JSONObject(bm);
		 	String accountNo = jsonObject.getJSONObject("Data").getString("AccountNo");
		 	String ifsc = jsonObject.getJSONObject("Data").getString("IfscCode");
		 	String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		 	String member = jsonObject.getJSONObject("Data").getString("member");
		 	String bankName = jsonObject.getJSONObject("Data").getString("bankName");
		 	 HttpStatus  h=HttpStatus.BAD_GATEWAY;
//		 	 TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService.getByApplicationNoAndMember(applicationNo, member);
//		 	if(twoWheelerFamilyMember.getAccountNo() != null && twoWheelerFamilyMember.getAccountNo().equals(accountNo)){
//		 		String pennyDropResponse = twoWheelerFamilyMember.getPennyDropResponse();
//		 		org.json.JSONObject pennyDrop=new org.json.JSONObject(pennyDropResponse);
//				return new ResponseEntity<Object>(pennyDrop.toString(), h.OK);
//		 	}
	        JSONObject pennyDrop = ifscservice.pennyDrop(accountNo,ifsc, Header);
	        
			 if(pennyDrop!=null) {
				 String Data2= pennyDrop.getString("data");
				 logger.debug("data2");
				 JSONObject Data1= new JSONObject(Data2);
				
				 logger.debug(Data1.toString());
//				 JSONObject jsonObject = Data1.getJSONObject("Response").getJSONObject("Data").getJSONObject("Result");
//				 logger.debug("Resp"+jsonObject);
				 if(Data1.getJSONObject("Response").has("Data"))
				 {
					 JSONObject result = Data1.getJSONObject("Response").getJSONObject("Data").getJSONObject("Result");
//					 twoWheelerFamilyMember.setBankName(bankName);
//					 twoWheelerFamilyMember.setAccountNo(accountNo);
//					 twoWheelerFamilyMember.setIfscCode(result.getString("IFSC"));
//					 twoWheelerFamilyMember.setAccountholderName(result.getString("AccountName"));
//					 twoWheelerFamilyMember.setPennyDropResponse(Data1.toString());
//					 familyMemberService.save(twoWheelerFamilyMember);
					 
					   h= HttpStatus.OK;
					   
				 }
				 else if(Data1.getJSONObject("Response").has("Error"))
				 {
					 h= HttpStatus.BAD_REQUEST;
					 
				 }
				 logger.debug("response"+Data1);
				 return new ResponseEntity<Object>(Data1.toString(), h);
				 
			 }
			 else
			 {
				 logger.debug("timeout");
				 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			 }
		}
	 else
		{ 
		 logger.debug("Invalid Request");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
			
		} 
	}
}
