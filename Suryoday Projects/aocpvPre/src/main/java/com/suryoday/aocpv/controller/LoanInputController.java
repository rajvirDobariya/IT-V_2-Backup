package com.suryoday.aocpv.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.suryoday.twowheeler.service.PreApprovalListService;




@RestController
@CrossOrigin("*")
@RequestMapping("/aocpv/v1")
public class LoanInputController {
	Logger logger = LoggerFactory.getLogger(LoanInputController.class);
	
	@Autowired
	private LoanInputService loanInputService;
	
	@Autowired
	PreApprovalListService loanInputServiceTw;
	
	@PostMapping("/LoanInput/upload")
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
	
	@GetMapping("/getAll")
	public List<PreApprovedListVikasLoan> getAllDetails(){
		return loanInputService.getAllProduct();
	}
	
	@PostMapping("/getByIdAndStatus")
	public ResponseEntity<List<LoanDetail>> getByIdAndstatus(@RequestBody String s,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
								JSONObject jsonObject=new JSONObject(s);
								logger.debug("getByIdAndStatus start");	
								logger.debug("request Data "+s);	
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
		int size = list.size();
		logger.debug("final response size"+size);	
		return new  ResponseEntity<List<LoanDetail>>(list,HttpStatus.OK);
	}
	
	@PostMapping("/updateStatus")
	public ResponseEntity<PreApprovedListVikasLoan> updateStatus(@RequestBody String string,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("updateStatus start");	
		logger.debug("request Data "+string);
		JSONObject jsonObject=new JSONObject(string);
		
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
		return new ResponseEntity<PreApprovedListVikasLoan>(details,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/findByMobileAndBranchId", method = RequestMethod.POST)
	public ResponseEntity<Object> findByMobile(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("findByMobileAndBranchId start");	
		logger.debug("request Data "+jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
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
		 			
		 						logger.debug("findByMobileAndBranchId end");	
		 	 		return new ResponseEntity<Object>(loanDetail,HttpStatus.OK);
		
	}
	@RequestMapping(value="/fetchByCustomerId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCustomerId(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchByCustomerId start");	
		logger.debug("request Data "+jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String CustomerId = jsonObject.getJSONObject("Data").getString("CustomerId");
		 		long CustomerIdInLong = Long.parseLong(CustomerId); 		
		 		PreApprovedListVikasLoan loanDetail =loanInputService.fetchByCustomerId(CustomerIdInLong);	
		 						logger.debug("fetchByCustomerId end");	
		 	 		return new ResponseEntity<Object>(loanDetail,HttpStatus.OK);
		
	}
	
	

}
