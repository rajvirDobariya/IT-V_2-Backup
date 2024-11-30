package com.suryoday.connector.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

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
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.UserLogService;
import com.suryoday.connector.service.LoanCreationService;



@RestController
@RequestMapping(value="/connector")
public class LoanCreationController {
	
	@Autowired
	LoanCreationService loanCreationService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@Autowired
	UserLogService userLogService;
	
	@Autowired
    AocpCustomerDataService aocpCustomerDataService;
	
	private static Logger logger = LoggerFactory.getLogger(LoanCreationController.class);

	@RequestMapping(value="/loanCreation", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> loanCreation(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest req)  throws Exception{

		 JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	     long applicationNoInLong = Long.parseLong(applicationNo);
	     
	     
		JSONObject Header= new JSONObject();
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-Correlation-ID",X_Correlation_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		 logger.debug("POST Request",jsonRequest);
		 System.out.println(Header);
		 logger.debug("ApplicationNo Sent to Request", applicationNoInLong);
		 
		 AocpvLoanCreation aocpvLoanCreation =aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
		 LocalDate now = LocalDate.now();
		 if(aocpvLoanCreation.getLoanAccoutNumber() != null) {
			 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 	 		response.put("Message", "SuccessFully LoanAccountNumber Generated");
	 	 		response.put("LoanAccountNumber", aocpvLoanCreation.getLoanAccoutNumber());
	 	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
		 }
		 
		 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 String presentDate = now.format(formatter1);
		 LocalDate date = aocpvLoanCreation.getCreatedDate(); 
         String sanctionedDate= date.format(formatter1);
         String customerId = Long.toString(aocpvLoanCreation.getCustomerId());
         LocalDate emiDate1 = now.plusMonths(1);
         String emiDate = emiDate1.format(formatter1);
         
         if(applicationNo == null || applicationNo.isEmpty()) {
             throw new EmptyInputException("input field is empty");
         }
         AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
                 logger.debug("db response" +aocpCustomer );
         String amountRequested = Double.toString(aocpCustomer.getRequestLoan());
                 
			if(X_Request_ID.equals("HOT"))
			{
				JSONObject response = new JSONObject();
		        org.json.simple.JSONObject   response2= new org.json.simple.JSONObject();
		        response2.put("MessageDate",presentDate);//d
		        response2.put("MessageType","NEWLN");
		        response2.put("CustRelRelationshipCode","80");
		        response2.put("AccountTitle",aocpvLoanCreation.getCustomerName());
		        response2.put("Amount",aocpvLoanCreation.getSanctionedLoanAmount());//d
		        response2.put("Term",aocpvLoanCreation.getTenure());//d
////		        response2.put("BalloonPayment","0");
		        response2.put("CurrencyCode","INR");
		        response2.put("BranchCode",aocpvLoanCreation.getBranchId());//d
		        response2.put("InstallmentFrequencyPrincipal","5M");
		        response2.put("InstallmentFrequencyInterest","5M");
		        response2.put("InstallmentType","M");
		        response2.put("MoratoriumPeriodPrincipal","");
		        response2.put("MoratoriumPeriodInterest","");
		        response2.put("PurposeCode","206");
		        response2.put("EquatedInst","Y");
		        LocalDate localDate=null;
				int dayOfMonth = now.getDayOfMonth();
				int month = now.getMonthValue();
				int NextMonth=0;
				if(dayOfMonth>15) {
					 NextMonth =month+2;
				}else {
					 NextMonth =month+1;	
				}
				 localDate = LocalDate.of(now.getYear(), NextMonth, 5);	
				
		        response2.put("InstallStartDate",localDate);
		        response2.put("InstStartDate",presentDate);
		        response2.put("UpfrontProcessingFees","0");
		        response2.put("SchmCode","5019");
		        response2.put("AcctDrPrefRate",""); 
		        response2.put("PegReviewDate",""); 
		        response2.put("RePaymntMthd",""); 
		        response2.put("HoldInOperAcct",""); 
		        response2.put("TypOfAcct","100"); 
		        response2.put("IndustryCode",""); 
		        response2.put("SegmentCode","51"); 
		        response2.put("NatOfBuss","12"); 
		        response2.put("BenefAcctNo","9011902290"); 
		        response2.put("BenfIFSC","ICIC0000366"); 
		        response2.put("BenAcctype","10"); 
		        response2.put("NatOfAdvan","9011902290"); 
		        response2.put("TypeOfAdvan","ICIC0000366"); 
		        response2.put("ModofAdvan","ICIC0000366"); 
		        response2.put("InterestChangeFrequency","");
		        response2.put("Sanctionamount",aocpvLoanCreation.getSanctionedLoanAmount());//d
		        response2.put("Sanctiondate",sanctionedDate);//d
		        response2.put("Dateofnote",localDate);//d
		        response2.put("PaymentTerm",aocpvLoanCreation.getTenure());//d
		        response2.put("StatementFrequency","5M");
		        response2.put("MoratoriumPeriod","");
		        response2.put("SBUCode","03");
		        response2.put("DPNDate",localDate);//d
		        response2.put("SectorCode","O");
		        response2.put("Subsectorcode","NAO");
		        response2.put("Priority_NonPriorityFlag","N");
		        response2.put("UnsecuredFlag","Y");
		        response2.put("LimitId","");
		        String tenure = aocpvLoanCreation.getTenure();
		        long tenureInlong = Long.parseLong(tenure.substring(0,2));
		        LocalDate CreditLimitMaturityDate = localDate.plusMonths(tenureInlong);
		        response2.put("CreditLimitMaturityDate",CreditLimitMaturityDate);//d
		        response2.put("NatureofBorrower","99");
		        response2.put("SpecialCategoryCode","6");
		        response2.put("StateCode","MH");
		        response2.put("DistrictCode","TEST");
		        response2.put("OragnizationCode","82");
		        response2.put("OccupationCode","01101");
		        response2.put("BSRPopulationCode","1");
		        response2.put("GovtSponsoredSchemes","0");
		        response2.put("SanctionDeptCode","13");
		        response2.put("AmountRequested",amountRequested);//d
		        response2.put("MaximumCreditLimit",amountRequested);//d
		        response2.put("ProductGroup","LN");
		        response2.put("CustomerNumber",customerId);      //d
		        response2.put("RELCustomerNumber",customerId); //d
		        response2.put("RoleCode","M");
		        response2.put("AccountRelationshipCode","A");
		        response2.put("ApplicationId",aocpvLoanCreation.getApplicationNo());
		        response2.put("CheckRequired","N");
		        response2.put("OperateMode", "");
		        response2.put("InterestRate", "10");
		        response2.put("InterestIndex", "");
		        response2.put("RMCode", "");
		        response2.put("AutoPaymentAcct", "");
		        response2.put("LeadGenCode", "");
		        response2.put("MOBBranchCode", aocpvLoanCreation.getBranchId()); //d
		        response2.put("ChannelCode", "6");
		        response2.put("SourceCode", "PAYTM");
	        
		        logger.debug("Hard Coded Values in JSON Object",response2.toString());
		        
		        response.put("Data", response2);
		  System.out.println(response);
		        logger.debug("JSON Object Sent to Request", response);
		        JSONObject loanCreation = loanCreationService.loanCreation(response, Header);
		        logger.debug("Response from the API",loanCreation);
		        
				HttpStatus  h=HttpStatus.BAD_GATEWAY;
				 if(loanCreation!=null)
				 {
					 

					 JSONObject Data2= null;
					
						 
						  
						   String Data1 = loanCreation.getString("data");
						   Data2 = new JSONObject(Data1);
						   System.out.println(Data2);
						   
						   if(Data2.has("Data"))
							 {  
							   h= HttpStatus.OK;
						    JSONObject object = Data2.getJSONObject("Data");
						    String loanAccountNumber= object.getString("ResponseValue");
						
	                    //   String loanAccountNumber = Data2.getString("ResponseValue");
						aocpvLoanCreation.setStatus("LOAN CREATED");
						 
						aocpvLoanCreation.setLoanCreationDate(now);
						aocpvLoanCreation.setUpdetedDate(now);
						aocpvLoanCreation.setLoanAccoutNumber(loanAccountNumber);
						String massege=aocpvLoanCreationService.update(aocpvLoanCreation);
						String status ="LOAN CREATED";
						userLogService.save(X_User_ID,applicationNo,status,"AO",0);
						org.json.simple.JSONObject   finalResponse= new org.json.simple.JSONObject();
						finalResponse.put("Message", "SuccessFully LoanAccountNumber Generated");
						finalResponse.put("LoanAccountNumber", aocpvLoanCreation.getLoanAccoutNumber());
			 	 		return new ResponseEntity<Object>(finalResponse,HttpStatus.OK);	
					 }
					 else if(Data2.has("Error"))
					 {
						 h= HttpStatus.BAD_REQUEST;
						 
					 }
					 logger.debug("Main Response from API", Data2.toString());
					 return new ResponseEntity<Object>(Data2.toString(), h);
					 
				 }
				 else
				 {
					 logger.debug("GATEWAY_TIMEOUT");
					 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				 }	
			}
			else
			{ 
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
				
			}
	
	
	}

}
