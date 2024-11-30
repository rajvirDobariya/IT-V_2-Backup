package com.suryoday.aocpv.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.Address;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.pojo.CustomerResponse;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.IncomeDetailWeb;
import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.pojo.RemarkResponse;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerData;
import com.suryoday.aocpv.pojo.UserLog;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.ChargesService;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.aocpv.service.UserLogService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.BRNetMastersService;

@RestController

@RequestMapping("/aocp/customer")
public class AocpCustomerDataController {

	Logger logger = LoggerFactory.getLogger(AocpCustomerDataController.class);
	
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	@Autowired
	BRNetMastersService brmastersservice;
	
	@Autowired
	LoanInputService loanInputService;
	 
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@Autowired
	UserLogService userLogService;
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	

	@Autowired
	ChargesService chargesservice;

	@RequestMapping(value="/saveData", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestParam("file") MultipartFile[] files,@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Request-flag", required = true) String X_Request_flag
			 ) throws ParseException {

	
			logger.debug("PD start");
			logger.debug("PD request"+jsonRequest);
			if(X_From_ID.equalsIgnoreCase("APPROVED") || X_From_ID.equalsIgnoreCase("COMPLETED") || X_From_ID.equalsIgnoreCase("DISBURSED")) {
				org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     		pdresponse.put("status", HttpStatus.OK.toString());
	     		pdresponse.put("message", "view Mode");
	     		logger.debug(pdresponse.toString());
	     		logger.debug("UD end");
	     		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
			}
		 JSONObject jsonObject=new JSONObject(jsonRequest);
		 String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		 
		 if(flowStatus.equals("PD"))
		 {		 
			 String mobileNoVerify="NO";
			 String ekyc_verify ="NO";
			 long mobileNoInLong=0;
			 long customerIdInLong=0;
			
			 String nomineeDetails =null;
			 String eligible_amount=null;
			 String isNomineeDetails="";
			 String nomineeAddressSameAs  ="";
	     String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	     long applicationNoInLong = Long.parseLong(applicationNo);
	     String customerId = jsonObject.getJSONObject("Data").getString("customerId");
	     if(!customerId.isEmpty()) {
	    	   customerIdInLong = Long.parseLong(customerId);
	     }
	     String title = jsonObject.getJSONObject("Data").getString("title");
	     String name = jsonObject.getJSONObject("Data").getString("name");
	     if(name == null || name.isEmpty() || name.equals("")) {
				throw new EmptyInputException("name cannot be empty");
			}
	     String obligation = jsonObject.getJSONObject("Data").getString("obligation");
	    String listtype = jsonObject.getJSONObject("Data").getString("listtype");
	    String isLive = jsonObject.getJSONObject("Data").getString("isLive");
	    String pendingWith = jsonObject.getJSONObject("Data").getString("pendingWith");
	    String appNoWithProductCode = jsonObject.getJSONObject("Data").getString("appNoWithProductCode");
	     if(jsonObject.getJSONObject("Data").has("nomineeDetails")) {
	    	 nomineeDetails= jsonObject.getJSONObject("Data").getJSONObject("nomineeDetails").toString();
	     }
	     String aadhar_no = jsonObject.getJSONObject("Data").getString("aadhar_no");
	     String address_verify = jsonObject.getJSONObject("Data").getString("address_verify");
	     
	     
	     
	     if(jsonObject.getJSONObject("Data").has("isNomineeDetails"))
	     {
	    	   isNomineeDetails = jsonObject.getJSONObject("Data").getString("isNomineeDetails");
	     }
	     if(jsonObject.getJSONObject("Data").has("nomineeAddressSameAs"))
	     {
	    	  nomineeAddressSameAs = jsonObject.getJSONObject("Data").getString("nomineeAddressSameAs"); 
	     }
	     
	     if(jsonObject.getJSONObject("Data").has("eligible_amount")) {
	      eligible_amount = jsonObject.getJSONObject("Data").getString("eligible_amount");
	     }
	     
	     ekyc_verify = jsonObject.getJSONObject("Data").getString("ekyc_verify");
	     String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
	     					JSONObject object = jsonObject.getJSONObject("Data");
	     				
	      mobileNoVerify = jsonObject.getJSONObject("Data").getString("mobileNoVerify");
	     					
	     
	     					if(mobileNo.isEmpty()) {
								 
							}
							else {
								  mobileNoInLong = Long.parseLong(mobileNo);
							}
	    
	     String dateOfBirth = jsonObject.getJSONObject("Data").getString("dateOfBirth");
	     LocalDate dob=null;
	     String[] split2 = dateOfBirth.split("-",3);
	     String newDOB="";
	     for(String s:split2) {
	    	 if(s.length()==1) {
	    		 s="0"+s;
	    	 }
	    	 newDOB=newDOB+s+"-";
	     }
	     String substring = newDOB.substring(0,newDOB.length()-1);
	     
	     try {
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		       dob = LocalDate.parse(substring, formatter);	
		     }
		     catch(DateTimeParseException e) {
		    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			       dob = LocalDate.parse(substring, formatter);	
			       
		     }
		     
	      LocalDate now1=LocalDate.now();
	      Period period = Period.between(dob, now1);
	      int age = period.getYears();
	     String address = jsonObject.getJSONObject("Data").getJSONArray("address").toString();
	     String branchid=jsonObject.getJSONObject("Data").getString("branchid").toString();
	     
	     			if(X_Request_flag.equalsIgnoreCase("add")) {
	     			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		     		aocpvImageService.saveCustomerPhoto(files,applicationNoInLong,document,flowStatus);
	     			}
	     			else if(X_Request_flag.equalsIgnoreCase("edit")) {
	     				JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
	     				if(jsonObject2.has("document")) {
	     		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
	     			aocpvImageService.saveCustomerPhoto(files,applicationNoInLong,document,flowStatus);
	     				}
	     			}
	     int	no	=aocpvIncomeService.getByApplicationNoAndmember(applicationNoInLong);
	     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApp(applicationNoInLong);
	     if(jsonObject.getJSONObject("Data").has("upaResponse"))
	     {
	    	 if(jsonObject.getJSONObject("Data").getJSONObject("upaResponse").getString("clientReferenceId").equals("")) {
	    	 }else {
	    		 String upaResponse = jsonObject.getJSONObject("Data").getJSONObject("upaResponse").toString();
	    		 aocpCustomer.setUpaResponse(upaResponse);
	    	 }
	    
	     }if(jsonObject.getJSONObject("Data").has("upiVerify"))
	     {
	    	String upiVerify = jsonObject.getJSONObject("Data").getString("upiVerify");
	    	aocpCustomer.setUpiVerify(upiVerify);
	     }
	     if(no ==2) {
	    	  AocpvIncomeDetails aocpvIncomeDetails =new AocpvIncomeDetails();
	    	  if(listtype.equalsIgnoreCase("NEW")) {
	    		  ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
	    		  aocpvIncomeDetails.setPanCard(fetchByApplicationId.getPanCard());
	    		  aocpvIncomeDetails.setPancardNoVerify("YES");
	    		  aocpvIncomeDetails.setForm60(fetchByApplicationId.getForm60());
	    		  aocpvIncomeDetails.setDrivingLicense(fetchByApplicationId.getDrivingLicense());
	    		  aocpvIncomeDetails.setDrivingLicenseIsVerify(fetchByApplicationId.getDrivingLicenseIsVerify());
	    		  aocpvIncomeDetails.setPassport(fetchByApplicationId.getPassport());
	    		  aocpvIncomeDetails.setPassportIsVerify(fetchByApplicationId.getPassportIsVerify());
	    		  aocpvIncomeDetails.setPanCardResponse(fetchByApplicationId.getPanCardResponse());
	    		  aocpvIncomeDetails.setVoterIdResponse(fetchByApplicationId.getVoterIdResponse());
	    		  aocpvIncomeDetails.setIsActive("YES");
	    		  aocpvIncomeDetails.setPassportResponse(fetchByApplicationId.getPassportResponse());
	    		  aocpvIncomeDetails.setDrivingLicenseResponse(fetchByApplicationId.getDrivingLicenseResponse());
	    		  ekyc_verify=fetchByApplicationId.getEkycVerify();
	    		  aocpCustomer.setForm60(fetchByApplicationId.getForm60());
	    		  aocpCustomer.setBestOfferEmi(fetchByApplicationId.getEmi());
	    		  aocpCustomer.setBestOfferROI(fetchByApplicationId.getRateOfInterest());
	    		  aocpCustomer.setBestOfferTenuer(fetchByApplicationId.getTenure());
	    		  aocpCustomer.setBestOfferAmount(fetchByApplicationId.getRequiredAmount());
	    		  applicationDetailsService.updateFlowStatus("PD",applicationNo,"PROGRESS");
		    	 }
	 	     aocpvIncomeDetails.setApplicationNo(applicationNoInLong);
	 	     aocpvIncomeDetails.setMember("Self");
	 	     aocpvIncomeDetails.setTitle(title);
	 	     String[] split = name.split(" ");
	 	     String firstName = split[0];
	 	     int length = split.length;
	 	     String lastName = split[length-1];
	 	     if(firstName.equals("")) {
	 	    	firstName=lastName;
	 	    	lastName="";
	 	     }
	 	     aocpvIncomeDetails.setFirstName(firstName);
	 	     aocpvIncomeDetails.setEarning("NO");
	 	     aocpvIncomeDetails.setLastName(lastName);
	 	     aocpvIncomeDetails.setDob(dob);
	 	     aocpvIncomeDetails.setAge(age);
	 	     aocpvIncomeDetails.setPrimarySourceOfIncome("NO");
	 	     aocpvIncomeDetails.setIsActive("YES");
	 	     aocpvIncomeDetails.setMobile(mobileNoInLong);
	 	     aocpvIncomeDetails.setAadharCard(aadhar_no);
	 	     aocpvIncomeDetails.setAadharNoVerify(ekyc_verify);
	 	     aocpvIncomeDetails.setMobileVerify(mobileNoVerify);
	 	     String save=aocpvIncomeService.saveIncome(aocpvIncomeDetails); 
	     }
	   
	    
	    
	     	if(aocpCustomer.getListType() == null) {
	     		if(listtype.equalsIgnoreCase("PRE")) {
	     			PreApprovedListVikasLoan loanDetails=loanInputService.getByReferenceNo(customerId);
	 			    loanDetails.setStatus("PROGRESS");
	 			   PreApprovedListVikasLoan saveSingleData2 = loanInputService.saveSingleData(loanDetails);
	     		}
	     	}
	     	else if(aocpCustomer.getListType().equalsIgnoreCase("PRE")){
	     		PreApprovedListVikasLoan loanDetails=loanInputService.getByReferenceNo(customerId);
	 			    loanDetails.setStatus("PROGRESS");
	 			   PreApprovedListVikasLoan saveSingleData2 = loanInputService.saveSingleData(loanDetails);
	     	}
	     	
	     		aocpCustomer.setApplicationNo(applicationNoInLong);
	     	  
	     	  aocpCustomer.setAppNoWithProductCode(appNoWithProductCode);
	     		aocpCustomer.setCustomerId(customerIdInLong);
	     		aocpCustomer.setTitle(title);
	     		aocpCustomer.setName(name);
	     		aocpCustomer.setObligation(obligation);
	     		aocpCustomer.setMobileNo(mobileNoInLong);
	     		aocpCustomer.setDateOfBirth(dob);
	     		aocpCustomer.setNomineeDetails(nomineeDetails);
	     		aocpCustomer.setAddress(address);
	     		aocpCustomer.setIsActive("YES");
	     		aocpCustomer.setIsLive(isLive);
	     		if(aocpCustomer.getListType() == null) {
	     		aocpCustomer.setListType(listtype);
	     		}
	     		if(aocpCustomer.getPendingWith() == null) {
	     		aocpCustomer.setPendingWith(pendingWith);
	     		}
	     		aocpCustomer.setMobileNoVerify(mobileNoVerify);
	     		aocpCustomer.setFlowStatus(flowStatus);
	     		if(aocpCustomer.getVersioncode()==0) {
	     		aocpCustomer.setVersioncode(1);
	     		}
	     		aocpCustomer.setBranchid(branchid);
	     		LocalDate now = LocalDate.now();
	     		aocpCustomer.setCreationDate(now);
	     		aocpCustomer.setUpdatedate(now);
	     		aocpCustomer.setIsNomineeDetails(isNomineeDetails);
	     		aocpCustomer.setNomineeAddressSameAs(nomineeAddressSameAs);
	     		if(aocpCustomer.getCreatedby() == null) {
	     			aocpCustomer.setCreatedby(X_User_ID);
		     		}
	     		
	     		aocpCustomer.setAddressVerify(address_verify);
	     		aocpCustomer.setEligibleAmount(eligible_amount);
	     		aocpCustomer.setTimestamp(LocalDateTime.now());
	     		aocpCustomer.setEkycVerify(ekyc_verify);
	     		if(aocpCustomer.getStatus()==null) {
	     			aocpCustomer.setStatus("PROGRESS");	
		     		String status="PROGRESS";
		     		userLogService.save(X_User_ID,applicationNo,status,pendingWith,aocpCustomer.getVersioncode());
	     		}
	     		String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
	     		
	     		org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     		
	     		pdresponse.put("status", HttpStatus.OK.toString());
	     		pdresponse.put("message", saveSingleData);
	     		logger.debug(pdresponse.toString());
	     		logger.debug("PD end");
	     		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
	     		
		 }		
		 else if(flowStatus.equals("UD"))	{
			 logger.debug("UD start");
		     String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		     if(applicationNo == null || applicationNo.isEmpty()) {
		    	 throw new EmptyInputException("input field is empty");
		     }
		     long applicationNoInLong = Long.parseLong(applicationNo);
		     logger.debug("PD request"+jsonRequest);
		     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
		     String sameAs="NO";
		     String houseOwnership = jsonObject.getJSONObject("Data").getString("houseOwnership");
		     String roofType = jsonObject.getJSONObject("Data").getString("roofType");
		     String residenceStability = jsonObject.getJSONObject("Data").getString("residenceStability");
		     String utilityBill = jsonObject.getJSONObject("Data").getString("utilityBill");
		     String relationshipWithOwner = jsonObject.getJSONObject("Data").getString("relationshipWithOwner");
		     String address = jsonObject.getJSONObject("Data").getJSONArray("address").toString();
		     sameAs = jsonObject.getJSONObject("Data").getString("sameAs").toString();
		     String otherAssets = jsonObject.getJSONObject("Data").getJSONArray("otherAssets").toString();
		     String vintage = jsonObject.getJSONObject("Data").getString("vintage");
		     String buisness = jsonObject.getJSONObject("Data").getString("business");
		     String serviceProvider = jsonObject.getJSONObject("Data").getString("serviceProvider");
		     String utilityBillNo = jsonObject.getJSONObject("Data").getString("utilityBillNo");
		     String serviceProviderCode = jsonObject.getJSONObject("Data").getString("serviceProviderCode");
		     
		     if(X_Request_flag.equalsIgnoreCase("add")) {
		    	 org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			     aocpvImageService.savePhotos(files,applicationNoInLong,document,flowStatus);
	     			}
	     			else if(X_Request_flag.equalsIgnoreCase("edit")) {
	     				JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
	     			     if(jsonObject2.has("document")) {
	     			    	 org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
	     				     aocpvImageService.savePhotos(files,applicationNoInLong,document,flowStatus);
	     			     }
	     			}
		     
		     logger.debug("UD image save");
		     aocpCustomer.setHouseOwnership(houseOwnership);
		     aocpCustomer.setRoofType(roofType);
		     aocpCustomer.setResidenceStability(residenceStability);
		     aocpCustomer.setUtilityBill(utilityBill);
		     aocpCustomer.setRelationshipWithOwner(relationshipWithOwner);
		     aocpCustomer.setOwnerAddress(address);
		     aocpCustomer.setSameAs(sameAs);
		     aocpCustomer.setOtherAssets(otherAssets);
		     aocpCustomer.setVintage(vintage);
		     aocpCustomer.setServiceProvider(serviceProvider);
		     aocpCustomer.setUtilityBillNo(utilityBillNo);
		     aocpCustomer.setServiceProviderCode(serviceProviderCode);
		     if(X_To_ID.equalsIgnoreCase("UD1")) {
		    
		     }
		     else {
		    	 aocpCustomer.setFlowStatus(flowStatus);
		     	}
		     LocalDate now = LocalDate.now();
		     aocpCustomer.setUpdatedate(now);
		     aocpCustomer.setBuisness(buisness);
	    
		     String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
		     if(aocpCustomer.getListType().equalsIgnoreCase("NEW")) {
		    	 applicationDetailsService.updateFlowStatus("UD",applicationNo,"PROGRESS");
		     }
		     org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     		pdresponse.put("status", HttpStatus.OK.toString());
	     		pdresponse.put("message", saveSingleData);
	     		
	     		logger.debug(pdresponse.toString());
	     		logger.debug("UD end");
	     		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
				
		 }
		 
		 else if(flowStatus.equals("ID"))	{
			 logger.debug("ID Start");
			 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		     if(applicationNo == null || applicationNo.isEmpty()) {
		    	 throw new EmptyInputException("input field is empty");
		     }
		     long applicationNoInLong = Long.parseLong(applicationNo);
		     logger.debug("ID request"+jsonRequest);
		     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
		     
		     logger.debug("aocpCustomer"+aocpCustomer.toString());
		     String totalMonthlyIncome = jsonObject.getJSONObject("Data").getString("totalMonthlyIncome");
		     double totalMonthlyIncomeInDouble = Double.parseDouble(totalMonthlyIncome);
		     String totalMonthlyEmi = jsonObject.getJSONObject("Data").getString("totalMonthlyEmi");
		     
		     double totalMonthlyEmiInDouble = Double.parseDouble(totalMonthlyEmi);
		     
		     List<AocpvIncomeDetails> aocpvIncomeDetails = new ArrayList<>();
		   
		    				JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
		    				if(jsonObject2.has("incomeDetails")) {
	org.json.JSONArray jsonArrayIncomeDetails= jsonObject.getJSONObject("Data").getJSONArray("incomeDetails");
					for(int i = 0; i < jsonArrayIncomeDetails.length(); i++) {
						 
						    String aadharNoVerify="NO";
						    String pancardNoVerify="NO";
						    String voterIdVerify="NO";
						    long mobileInLong=0;
						    String gaurantor="";
						    String occupation=null;
						    String primarySourceOfIncome=null;
						    String occCode=null;
						    double securedLoanInDouble=0.0;
						    double unsecuredLoanInDouble=0.0;
						    double monthlyIncomeInDouble=0.0;
						    double monthlyLoanEmiInDouble=0.0;
					JSONObject json = jsonArrayIncomeDetails.getJSONObject(i);
					 AocpvIncomeDetails aocpvIncomeDetails2= new AocpvIncomeDetails();
								String member = json.getString("member");
								if(member == null || member.isEmpty() || member.equals("")) {
									throw new EmptyInputException("Please select member");
								}
								String title = json.getString("title");
								String firstName = json.getString("firstName");
								String lastName = json.getString("lastName");
								String gender = json.getString("gender");
								String form60 = "NO";
								String drivingLicense = "";
								String drivingLicenseIsVerify = "NO";
								String passport = "";
								String passportIsVerify ="NO";
								if(json.has("gaurantor")){
									gaurantor = json.getString("gaurantor");
									}
								if(json.has("form60")){
									form60 = json.getString("form60");
									}
								if(json.has("drivingLicense")){
									drivingLicense = json.getString("drivingLicense");
									}
								if(json.has("drivingLicenseIsVerify")){
									drivingLicenseIsVerify = json.getString("drivingLicenseIsVerify");
									}
								if(json.has("passport")){
									passport = json.getString("passport");
									}
								if(json.has("passportIsVerify")){
									passportIsVerify = json.getString("passportIsVerify");
									}
								String age = json.getString("age");
								int ageInInt = Integer.parseInt(age);
								String dob = json.getString("dob");
								// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
								 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								 LocalDate dob1 = LocalDate.parse(dob, formatter);
								String mobile = json.getString("mobile");
								
							String	 mobileVerify = json.getString("mobileVerify");
							
								if(mobile.isEmpty()) {
									 
								}
								else {
									mobileInLong = Long.parseLong(mobile);
								}
								String married = json.getString("married");
								String earning = json.getString("earning");
								if(json.has("occupation")){
								 occupation = json.getString("occupation");
								}
								if(json.has("occCode")){
									 occCode = json.getString("occCode");
									}
							
								
								if(json.has("primarySourceOfIncome")){
								 primarySourceOfIncome = json.getString("primarySourceOfIncome");
									}
								if(json.has("securedLoan")){
									String securedLoan = json.getString("securedLoan");
									 securedLoanInDouble = Double.parseDouble(securedLoan);
										}
								if(json.has("unsecuredLoan")){
									String unsecuredLoan = json.getString("unsecuredLoan");
									 unsecuredLoanInDouble = Double.parseDouble(unsecuredLoan);
										}
								if(json.has("monthlyIncome")){
									String monthlyIncome = json.getString("monthlyIncome");
									 monthlyIncomeInDouble = Double.parseDouble(monthlyIncome);
										}
								if(json.has("monthlyLoanEmi")){
									String monthlyLoanEmi = json.getString("monthlyLoanEmi");
									 monthlyLoanEmiInDouble = Double.parseDouble(monthlyLoanEmi);
										}
								
								String aadharCardNo = json.getString("aadharCard");
								if(json.has("aadharNoVerify")) {
									aadharNoVerify = json.getString("aadharNoVerify");
								} 
								String voterId = json.getString("voterId");
								if(json.has("voterIdVerify")) {
									voterIdVerify = json.getString("voterIdVerify");
								} 
								String pancardno = json.getString("panCard");
								if(json.has("pancardNoVerify")) {
									 pancardNoVerify = json.getString("pancardNoVerify");
								}
//								 if(X_Request_flag.equalsIgnoreCase("add")) {
//									 org.json.JSONArray document =json.getJSONArray("document"); 
//									 logger.debug("savePhotoIncome start "+member);		
//						aocpvImageService.savePhotoIncome(files,applicationNoInLong,document,flowStatus,member);
//						 logger.debug("Id image save end"+member);
//								 }
//								 else if(X_Request_flag.equalsIgnoreCase("edit")) {
									 if(json.has("document")) {
											org.json.JSONArray document =json.getJSONArray("document"); 
											 logger.debug("savePhotoIncome start "+member);		
								aocpvImageService.savePhotoIncome(files,applicationNoInLong,document,flowStatus,member);
								 logger.debug("Id image save end"+member);
										}
//								 }
									 logger.debug("Id details save start");
								aocpvIncomeDetails2.setMember(member);
								aocpvIncomeDetails2.setTitle(title);
								aocpvIncomeDetails2.setFirstName(firstName);
								aocpvIncomeDetails2.setLastName(lastName);
								aocpvIncomeDetails2.setGender(gender);
								aocpvIncomeDetails2.setAge(ageInInt);
								aocpvIncomeDetails2.setDob(dob1);
								aocpvIncomeDetails2.setMobile(mobileInLong);
								aocpvIncomeDetails2.setMobileVerify(mobileVerify);
								aocpvIncomeDetails2.setMarried(married);
								aocpvIncomeDetails2.setEarning(earning);
								aocpvIncomeDetails2.setOccupation(occupation);
								aocpvIncomeDetails2.setOccCode(occCode);
								aocpvIncomeDetails2.setPrimarySourceOfIncome(primarySourceOfIncome);
								aocpvIncomeDetails2.setSecuredLoan(securedLoanInDouble);
								aocpvIncomeDetails2.setUnsecuredLoan(unsecuredLoanInDouble);
								aocpvIncomeDetails2.setMonthlyIncome(monthlyIncomeInDouble);
								aocpvIncomeDetails2.setMonthlyLoanEmi(monthlyLoanEmiInDouble);
								aocpvIncomeDetails2.setGaurantor(gaurantor);
								aocpvIncomeDetails2.setAadharCard(aadharCardNo);
								aocpvIncomeDetails2.setAadharNoVerify(aadharNoVerify);
								aocpvIncomeDetails2.setPanCard(pancardno);
								aocpvIncomeDetails2.setPancardNoVerify(pancardNoVerify);
								aocpvIncomeDetails2.setVoterId(voterId);
								aocpvIncomeDetails2.setVoterIdVerify(voterIdVerify);
								aocpvIncomeDetails2.setForm60(form60);
								aocpvIncomeDetails2.setDrivingLicense(drivingLicense);
								aocpvIncomeDetails2.setDrivingLicenseIsVerify(drivingLicenseIsVerify);
								aocpvIncomeDetails2.setPassport(passport);
								aocpvIncomeDetails2.setIsActive("YES");
								aocpvIncomeDetails2.setPassportIsVerify(passportIsVerify);
								aocpvIncomeDetails2.setApplicationNo(applicationNoInLong);
								aocpvIncomeDetails.add(aocpvIncomeDetails2);
								 logger.debug("Id details save end");
					}
					 logger.debug("Id Data Save start");
					String msg =aocpvIncomeService.saveAll(aocpvIncomeDetails);
					 logger.debug("Id Data Save end");
					 
		    				}
		     aocpCustomer.setTotalMonthlyIncome(totalMonthlyIncomeInDouble);
		     aocpCustomer.setTotalMonthlyEmi(totalMonthlyEmiInDouble);
		     aocpCustomer.setFlowStatus(flowStatus);
		     if(jsonObject.getJSONObject("Data").has("guarantorRemarks")) {
			     String guarantorRemarks = jsonObject.getJSONObject("Data").getString("guarantorRemarks"); 
			     aocpCustomer.setGuarantorRemarks(guarantorRemarks);
			     }
		     if(aocpCustomer.getStatus()==null) {
		    	 aocpCustomer.setStatus("PROGRESS"); 
		     }
		     LocalDate now = LocalDate.now();
		     aocpCustomer.setUpdatedate(now);
		     String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
		     if(aocpCustomer.getListType().equalsIgnoreCase("NEW")) {
		    	 applicationDetailsService.updateFlowStatus("ID",applicationNo,"PROGRESS");
		     }
		     org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     		pdresponse.put("status", HttpStatus.OK.toString());
	     		pdresponse.put("message", saveSingleData);
	     		logger.debug(pdresponse.toString());
	     		logger.debug("Id end");
	     		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
		 }
		 
		 else if(flowStatus.equals("MED"))	{
			 logger.debug("MED Start");
			 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		     if(applicationNo == null || applicationNo.isEmpty()) {
		    	 throw new EmptyInputException("input field is empty");
		     }
		     long applicationNoInLong = Long.parseLong(applicationNo);
		     
		 	 AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
		 	 
		 	 String foodAndUtility = jsonObject.getJSONObject("Data").getString("foodAndUtility");
		 	 double foodAndUtilityInDouble = Double.parseDouble(foodAndUtility);
		 	 String rent = jsonObject.getJSONObject("Data").getString("rent");
		 	double rentInDouble = Double.parseDouble(rent);
		 	 String transportation = jsonObject.getJSONObject("Data").getString("transportation");
		 	double transportationInDouble = Double.parseDouble(transportation);
		 	 String medical = jsonObject.getJSONObject("Data").getString("medical");
		 	double medicalInDouble = Double.parseDouble(medical);
		 	 String education = jsonObject.getJSONObject("Data").getString("education");
		 	double educationInDouble = Double.parseDouble(education);
		 	 String other = jsonObject.getJSONObject("Data").getString("other");
		 	double otherInDouble = Double.parseDouble(other);
		 	 String total = jsonObject.getJSONObject("Data").getString("total");
		 	double totalInDouble = Double.parseDouble(total);
		 	 String monthlyBalance = jsonObject.getJSONObject("Data").getString("monthlyBalance");
		 	double monthlyBalanceInDouble = Double.parseDouble(monthlyBalance);
		 	 String customerClassification = jsonObject.getJSONObject("Data").getString("customerClassification");
		 	 				aocpCustomer.setFoodAndUtility(foodAndUtilityInDouble);
		 	 				aocpCustomer.setRent(rentInDouble);
		 	 				aocpCustomer.setTransportation(transportationInDouble);
		 	 				aocpCustomer.setMedical(medicalInDouble);
		 	 				aocpCustomer.setEducation(educationInDouble);
		 	 				aocpCustomer.setOther(otherInDouble);
		 	 				aocpCustomer.setTotal(totalInDouble);
		 	 				aocpCustomer.setMonthlyBalance(monthlyBalanceInDouble);
		 	 				aocpCustomer.setCustomerClassification(customerClassification);
		 	 				aocpCustomer.setFlowStatus(flowStatus);
		 	 				 LocalDate now = LocalDate.now();
		 	 			     aocpCustomer.setUpdatedate(now);
		 	 			   if(aocpCustomer.getStatus()==null) {
		 	 		    	 aocpCustomer.setStatus("PROGRESS"); 
		 	 		     }
		 	 				String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
		 	 				 if(aocpCustomer.getListType().equalsIgnoreCase("NEW")) {
		 	 			    	 applicationDetailsService.updateFlowStatus("MED",applicationNo,"PROGRESS");
		 	 			     }
		 	 				logger.debug("ID Data save");
		 	 				org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
		 		     		pdresponse.put("status", HttpStatus.OK);
		 		     		pdresponse.put("message", saveSingleData);
		 		     		logger.debug(pdresponse.toString());
		 		     		logger.debug("MED end");
		 		     		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
			 
		 }
		 
		 else if(flowStatus.equals("MFI"))	{
			 logger.debug("MfI start");
			 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		     if(applicationNo == null || applicationNo.isEmpty()) {
		    	 throw new EmptyInputException("input field is empty");
		     }
		     long applicationNoInLong = Long.parseLong(applicationNo);
		     
		 	 AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
		 	 
		 	 String purposeOfLoan = jsonObject.getJSONObject("Data").getString("purposeOfLoan");
		 	 String purposeId = jsonObject.getJSONObject("Data").getString("purposeId");
		 	 String subCategory = jsonObject.getJSONObject("Data").getString("subCategory");
		 	 String subCategoryPurposeId = jsonObject.getJSONObject("Data").getString("subCategoryPurposeId");
		 	 String loanCode = jsonObject.getJSONObject("Data").getString("loanCode");
		 	 String existingLoanPurpose = jsonObject.getJSONObject("Data").getString("existingLoanPurpose");
		 	 String mobileLinkToAadhar = jsonObject.getJSONObject("Data").getString("mobileLinkToAadhar");
		 	 String requestLoan = jsonObject.getJSONObject("Data").getString("requestLoan");
		 	 
		 	if(jsonObject.getJSONObject("Data").has("emiProductCode")) {
		 		String emiProductCode = jsonObject.getJSONObject("Data").getString("emiProductCode");
		 		aocpCustomer.setEmiProductCode(emiProductCode);
		 	}
		 	if(jsonObject.getJSONObject("Data").has("monthlyIncome")) {
		 		String monthlyIncome = jsonObject.getJSONObject("Data").getString("monthlyIncome");
		 		AocpvIncomeDetails incomeDetails = aocpvIncomeService.getByApplicationNoAndmember(applicationNoInLong, "SELF");
		 		incomeDetails.setMonthlyIncome(monthlyIncome!=""?Double.parseDouble(monthlyIncome):incomeDetails.getMonthlyIncome());
		 		aocpvIncomeService.save(incomeDetails);
		 	}
		 	if(jsonObject.getJSONObject("Data").has("totalMonthlyIncome")) {
		 		String totalMonthlyIncome = jsonObject.getJSONObject("Data").getString("totalMonthlyIncome");
		 		aocpCustomer.setTotalMonthlyIncome(totalMonthlyIncome!=""?Double.parseDouble(totalMonthlyIncome):aocpCustomer.getTotalMonthlyIncome());	
		 	}
		 	 double requestLoanInDouble = Double.parseDouble(requestLoan);
		 	 
		 	 if(jsonObject.getJSONObject("Data").has("requestedLoanResponse")) {
		 		String requestedLoanResponse = jsonObject.getJSONObject("Data").getJSONObject("requestedLoanResponse").toString();
		 		System.out.println(requestedLoanResponse);
		 			logger.debug("requestLoanresponse" +requestedLoanResponse);
		 			aocpCustomer.setResponseEmi(requestedLoanResponse);
		 	 }
         
 		 			
		 	 if(mobileLinkToAadhar.equalsIgnoreCase("no")) {
		 		 String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		 		 if(mobile.isEmpty() || mobile == null) {
		 			aocpCustomer.setMobile2(0);
		 		 }
		 		 else {
		 			long mobileInLong = Long.parseLong(mobile);
		 			aocpCustomer.setMobile2(mobileInLong);
		 		 }
		 	 }
		 	 else {
		 		 String mobile = jsonObject.getJSONObject("Data").getString("mobile"); 
		 		 long mobileInLong = Long.parseLong(mobile);
		 		aocpCustomer.setMobile2(mobileInLong);
		 	 }
		 	 
		 	
		 	 System.out.println(aocpCustomer.getMobile2());
		 	String maxEmiEligibility = jsonObject.getJSONObject("Data").getString("maxEmiEligibility");
		 	 long maxEmiEligibilityInLong = Long.parseLong(maxEmiEligibility);
		 	aocpCustomer.setPurposeOfLoan(purposeOfLoan);
		 	aocpCustomer.setExistingLoanPurpose(existingLoanPurpose);
		 	aocpCustomer.setMobileLinkToAadhar(mobileLinkToAadhar);
		 	aocpCustomer.setMaxEmieligibility(maxEmiEligibilityInLong);
		 	aocpCustomer.setLoanCode(loanCode);
		 	aocpCustomer.setSubCategory(subCategory);
		 	aocpCustomer.setPurposeId(purposeId);
		 	aocpCustomer.setSubCategoryPurposeId(subCategoryPurposeId);
		 
		 	aocpCustomer.setFlowStatus(flowStatus);
		 	aocpCustomer.setRequestLoan(requestLoanInDouble);
		 	
		 	
		 	
		 	System.out.println(aocpCustomer.toString());
		 	//aocpCustomer.setRequestedloanResponse(requestedLoanResponse);
		 	System.out.println(aocpCustomer.getResponseEmi());
		 	 LocalDate now = LocalDate.now();
		     aocpCustomer.setUpdatedate(now);
		     if(aocpCustomer.getStatus()==null) {
		    	 aocpCustomer.setStatus("PROGRESS"); 
		     }
		String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
		 if(aocpCustomer.getListType().equalsIgnoreCase("NEW")) {
	    	 applicationDetailsService.updateFlowStatus("MFI",applicationNo,"PROGRESS");
	     }
		logger.debug("MFI data save");
		org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
 		pdresponse.put("status", HttpStatus.OK.toString());
 		pdresponse.put("message", saveSingleData);
 		logger.debug(pdresponse.toString());
 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
		 }

		 org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	 		pdresponse.put("status", HttpStatus.OK);
	 		pdresponse.put("message", "Wrong Flowstatus");
	 		logger.debug(pdresponse.toString());
	 		return new ResponseEntity<Object>(pdresponse,HttpStatus.BAD_REQUEST);
		 
	}
	
	@RequestMapping(value="/fetchById", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchById Start");
		logger.debug("request Data" +jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	     if(applicationNo == null || applicationNo.isEmpty()) {
	    	 throw new EmptyInputException("input field is empty");
	     }
	     long applicationNoInLong = Long.parseLong(applicationNo);
	     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
	     		logger.debug("db response " );
	     ResponseAocpCustomerData aocpCustomerData = aocpCustomerDataService.getResponseWithoutImage(aocpCustomer);
	     			org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     			//org.json.JSONObject   pdresponse= new org.json.JSONObject();
	     	 		pdresponse.put("status", HttpStatus.OK.toString());
	     	 		pdresponse.put("Data", aocpCustomerData);
	     	 		logger.debug("final response");
	     	 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);	
	     			
	}
	
	@RequestMapping(value="/fetchAllData", method = RequestMethod.POST)
	public ResponseEntity<Object> getAllData(@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchAllData Start");
		
		org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
		
		
		List<ResponseAocpCustomerData> listOfCustomer =aocpCustomerDataService.getAllData();
	
 		pdresponse.put("status", HttpStatus.OK.toString());
 		pdresponse.put("Data", listOfCustomer);
		
 		logger.debug(pdresponse.toString());
	 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
	}

	
	@RequestMapping(value="/updateStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> updateStatus(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("updateStatus Start");
		logger.debug("request Data"+ jsonRequest);
		 JSONObject jsonObject=new JSONObject(jsonRequest);
		
		 

			 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			 String status = jsonObject.getJSONObject("Data").getString("decision");
			 String aoRemarks = jsonObject.getJSONObject("Data").getString("ao_remarks");
			 String roRemarks = jsonObject.getJSONObject("Data").getString("ro_remarks");
			 
		     if(applicationNo == null || applicationNo.isEmpty()) {
		    	 throw new EmptyInputException("input field is empty");
		     }
		     long applicationNoInLong = Long.parseLong(applicationNo);
		     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
		     	
		     if(aocpCustomer.getStatus().equalsIgnoreCase("APPROVED")) {
		    	 org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
			 		pdresponse.put("status", HttpStatus.OK.toString());
			 		pdresponse.put("message", "data updated");
			 		logger.debug(pdresponse.toString());
			 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
		     }
		     		if(aocpCustomer.getFlowStatus().equals("DDD") || aocpCustomer.getFlowStatus().equals("MFI"))
		       {
		     			if(aocpCustomer.getHouseOwnership() == null || aocpCustomer.getRelationshipWithOwner()  == null) {
		     				org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
				    		pdresponse.put("status", HttpStatus.OK.toString());
				    		pdresponse.put("message", "Please Add Utility Details");
				    		logger.debug(pdresponse.toString());
				    		return new ResponseEntity<Object>(pdresponse,HttpStatus.BAD_REQUEST);
		     			}
		    	   if(aocpCustomer.getPendingWith().equalsIgnoreCase("RO")) {
		    		   status="myTask";
		    		   userLogService.save(X_User_ID,applicationNo,status,aocpCustomer.getPendingWith(),aocpCustomer.getVersioncode());
		    		   aocpCustomer.setPendingWith("AO");
					 	aocpCustomerDataService.createHistory(applicationNo);
					     int versioncode = aocpCustomer.getVersioncode();
							versioncode++;
							aocpCustomer.setVersioncode(versioncode);
							aocpvIncomeService.updateversionCode(applicationNoInLong,versioncode);
							aocpvImageService.updateversionCode(applicationNoInLong,versioncode);
		    	   }
		    	   else {
		  //  		   status="COMPLETED"; 
		    		   userLogService.save(X_User_ID,applicationNo,status,aocpCustomer.getPendingWith(),aocpCustomer.getVersioncode());
		    	   }
		    	   aocpCustomer.setStatus(status);
		    	   
		    	   if(aocpCustomer.getListType().equalsIgnoreCase("PRE")) {
		    			String customerId = Long.toString(aocpCustomer.getCustomerId());
//		    			PreApprovedListVikasLoan loanDetails=loanInputService.getByReferenceNo(customerId);
//				    	   			loanDetails.setStatus(status); 			
//				    	   			PreApprovedListVikasLoan saveSingleData2 = loanInputService.saveSingleData(loanDetails);
				    	   logger.debug("status change to completed in loan table");  
		    	   } 
		    	   if(roRemarks.isEmpty()) {
		    		   aocpCustomer.setRoRemarks(roRemarks);
		    	   }
		    	   else if(aoRemarks.isEmpty()) {
		    		   aocpCustomer.setAoRemarks(aoRemarks);
		    	   }
		    	 
		    	   LocalDate now = LocalDate.now();
				     aocpCustomer.setUpdatedate(now);
				     aocpCustomer.setTimestamp(LocalDateTime.now());
				     String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
				     if(aocpCustomer.getListType().equalsIgnoreCase("NEW")) {
				    	 applicationDetailsService.updateFlowStatus("DDD",applicationNo,status);
				     }
				     logger.debug("status change to completed in Aocpv table");
			     		
				     org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
				 		pdresponse.put("status", HttpStatus.OK.toString());
				 		pdresponse.put("message", saveSingleData);
				 		logger.debug(pdresponse.toString());
				 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
				 
		       }
		       else
		       {
		    	   org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
		    		pdresponse.put("status", HttpStatus.OK.toString());
		    		pdresponse.put("message", "Wrong Flowstatus");
		    		logger.debug(pdresponse.toString());
		    		return new ResponseEntity<Object>(pdresponse,HttpStatus.BAD_REQUEST);
		    	   
		       }
		       
		   

	 
	
	}
	
	@RequestMapping(value="/fetchByCustomerId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCustomerId(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("fetchByCustomerId start");
		logger.debug("request Data"+jsonRequest);
		 String customerId = jsonObject.getJSONObject("Data").getString("customerId");
	     if(customerId == null || customerId.isEmpty()) {
	    	 throw new EmptyInputException("input field is empty");
	     }
	     long customerIdInLong = Long.parseLong(customerId);
	     ResponseAocpCustomerData aocpCustomerData=aocpCustomerDataService.getByCustomerId(customerIdInLong);
	     logger.debug("db call aocpv table"+aocpCustomerData);
	     PreApprovedListVikasLoan loanDetails=loanInputService.getByReferenceNo(customerId);
	     	logger.debug("db call loan table"+loanDetails);
	     
	     
	     
	     org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
			//org.json.JSONObject   pdresponse= new org.json.JSONObject();
	 		pdresponse.put("status", HttpStatus.OK.toString());
	 		pdresponse.put("LoanDetails", loanDetails);
	 		pdresponse.put("CustomerDetails", aocpCustomerData);
	 		logger.debug(pdresponse.toString());
	 		
	 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);	
		

		
	}
	
	@RequestMapping(value="/fetchAllByStatusAndBranchId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllByStatusAndBranchId(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchAllByStatusAndBranchId start");
		logger.debug("request Data"+jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String status = jsonObject.getJSONObject("Data").getString("status");
		 String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			logger.debug("db call aocpv table start");
	List<AocpCustomer> fetchAllByBranchIdAndStatus = aocpCustomerDataService.getAllByBranchIdAndStatus(branchId, status);
						logger.debug("db call aocpv table end");
				List<LoanDetail> list=new ArrayList<>();
				
	for(AocpCustomer fetchByBranchIdAndStatus : fetchAllByBranchIdAndStatus) {
		
		long applicationNo = fetchByBranchIdAndStatus.getApplicationNo();
				long customerId = fetchByBranchIdAndStatus.getCustomerId();
				String status2 = fetchByBranchIdAndStatus.getStatus();
				
				LoanDetail byCustomerId = loanInputService.getByCustomerId(customerId,status2);
							byCustomerId.setApplicationNo(applicationNo);
							
				list.add(byCustomerId);
				logger.debug("applicationNo"+applicationNo);
	}
	
//	 	org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
//		pdresponse.put("status", HttpStatus.OK);
//		pdresponse.put("Data", list);
		
		return new ResponseEntity<Object>(list,HttpStatus.OK);	
		 }
		
	
	
	@RequestMapping(value="/finalUpdate", method = RequestMethod.POST)
	public ResponseEntity<Object> finalUpdate(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
				JSONObject jsonObject=new JSONObject(jsonRequest);
				logger.debug("final updata start");
				logger.debug("request Data"+jsonRequest);
				 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
				 String status = jsonObject.getJSONObject("Data").getString("status");
				 JSONArray remark = jsonObject.getJSONObject("Data").getJSONArray("remark");
				  long applicationNoInLong = Long.parseLong(applicationNo);
				  AocpCustomer aocpCustomer=aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
				 if(status.equalsIgnoreCase("Remarks")) {
					 aocpCustomer.setRemarks(remark.toString());
					 String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
					 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				 		response.put("status", HttpStatus.OK.toString());
				 		response.put("message", saveSingleData);
				 		logger.debug(" final response "+response.toString());
				 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
				 }
				 
				 String rejectreason = jsonObject.getJSONObject("Data").getString("rejectreason");
				 String finalsanction = jsonObject.getJSONObject("Data").getJSONArray("finalsanction").toString();
				 String finalsanctionAmount = jsonObject.getJSONObject("Data").getJSONArray("finalsanctionAmount").toString();
				 
				 String houseOwnership = jsonObject.getJSONObject("Data").getString("houseOwnership");
					String relationshipWithOwner = jsonObject.getJSONObject("Data").getString("relationshipWithOwner");
					String purposeOfLoan = jsonObject.getJSONObject("Data").getString("purposeOfLoan");
					String subCategory = jsonObject.getJSONObject("Data").getString("subCategory");
					String purposeId = jsonObject.getJSONObject("Data").getString("purposeId");
					String subCategoryPurposeId = jsonObject.getJSONObject("Data").getString("subCategoryPurposeId");
					
					if (applicationNo.isEmpty() || houseOwnership.isEmpty() || relationshipWithOwner.isEmpty() || purposeOfLoan.isEmpty()
							|| subCategory.isEmpty() ||  purposeId.isEmpty() || subCategoryPurposeId.isEmpty() ||  subCategoryPurposeId.equalsIgnoreCase("SELECT OPTION")) {
						throw new EmptyInputException("input field is empty");
					}
					
			   		     
			     if(aocpCustomer.getHouseOwnership()==null || !aocpCustomer.getHouseOwnership().equalsIgnoreCase(houseOwnership)) {
						aocpCustomer.setHouseOwnership(houseOwnership);
					}
					if(aocpCustomer.getRelationshipWithOwner()==null || !aocpCustomer.getRelationshipWithOwner().equalsIgnoreCase(relationshipWithOwner)){
						aocpCustomer.setRelationshipWithOwner(relationshipWithOwner);
					}
					if(aocpCustomer.getPurposeOfLoan()==null || !aocpCustomer.getPurposeOfLoan().equalsIgnoreCase(purposeOfLoan)){
						aocpCustomer.setPurposeOfLoan(purposeOfLoan);
					}
					if(aocpCustomer.getSubCategory()==null || !aocpCustomer.getSubCategory().equalsIgnoreCase(subCategory)){
						aocpCustomer.setSubCategory(subCategory);
					}
					if(aocpCustomer.getPurposeId()==null || !aocpCustomer.getPurposeId().equalsIgnoreCase(purposeId)){
						aocpCustomer.setPurposeId(purposeId);
					}
					if(aocpCustomer.getSubCategoryPurposeId()==null || !aocpCustomer.getSubCategoryPurposeId().equalsIgnoreCase(subCategoryPurposeId)){
						aocpCustomer.setSubCategoryPurposeId(subCategoryPurposeId);
					}
					
			     if(aocpCustomer.getStatus().equalsIgnoreCase("COMPLETED")) {
//			    		aocpCustomerDataService.createHistory(applicationNo);
//			    		int versioncode = aocpCustomer.getVersioncode();
//						versioncode++;
//						aocpCustomer.setVersioncode(versioncode);
//						aocpvIncomeService.updateversionCode(applicationNoInLong,versioncode);
//						aocpvImageService.updateversionCode(applicationNoInLong,versioncode);
			    	 if(status.equals("APPROVED")) {
			    	  aocpvLoanCreationService.save(aocpCustomer,X_User_ID,finalsanctionAmount);
					 	//logger.debug("db call response"+aocpCustomer);
		    	 }
			    	 LocalDate now = LocalDate.now();
			    	 
			    	 if(aocpCustomer.getListType().equalsIgnoreCase("PRE")) {
			    		 PreApprovedListVikasLoan loanDetails=loanInputService.fetchByCustomerID(aocpCustomer.getCustomerId());
			    		 if(loanDetails!=null) {
								loanDetails.setStatus(status);	
								loanDetails.setUpdatedate(now);
								PreApprovedListVikasLoan loanDetails2 = loanInputService.saveSingleData(loanDetails);
							}
			    	 }
			    	
						aocpCustomer.setStatus(status);
						if(finalsanction == null || finalsanction.isEmpty()) {
							aocpCustomer.setFinalSanction(null);
						}
						else {
							aocpCustomer.setFinalSanction(finalsanction);
							logger.debug("set final sanction"+finalsanction);
						}
						
						if(finalsanctionAmount == null || finalsanctionAmount.isEmpty()) {
							aocpCustomer.setFinalsanctionAmount(null);
						}
						else {
							aocpCustomer.setFinalsanctionAmount(finalsanctionAmount);
							logger.debug("set final sanction"+finalsanctionAmount);
						}
						
						
						if(remark== null || remark.isEmpty()) {
							aocpCustomer.setRemarks(null);
						}
						else {
							aocpCustomer.setRemarks(remark.toString());
		
						}
						
						if(status.equals("REJECTED"))
						{
							aocpCustomer.setRejectreason(rejectreason);
							aocpCustomer.setRejectedBy(X_User_ID);
							logger.debug("set reject reason"+rejectreason);
						}
						
						aocpCustomer.setUpdatedate(now);
						aocpCustomer.setTimestamp(LocalDateTime.now());
						
						String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
						
						userLogService.save(X_User_ID,applicationNo,status,"CREDIT",aocpCustomer.getVersioncode());
						 org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
					 		pdresponse.put("status", HttpStatus.OK.toString());
					 		pdresponse.put("message", saveSingleData);
					 		logger.debug(" final response "+pdresponse.toString());
					 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);	
			     }
			     else if(aocpCustomer.getStatus().equalsIgnoreCase("ReVerify")) {
			    	 aocpCustomer.setStatus(status);
			    	   aocpCustomer.setRemarks(remark.toString());
			    	   LocalDate now = LocalDate.now();
			    	   aocpCustomer.setUpdatedate(now);
			    		aocpCustomer.setTimestamp(LocalDateTime.now());
			    	   String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
			    	   org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				 		response.put("status", HttpStatus.OK.toString());
				 		response.put("message", saveSingleData);
				 		logger.debug(" final response "+response.toString());
				 		return new ResponseEntity<Object>(response,HttpStatus.OK);
			     }
			     org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
			 		pdresponse.put("status", HttpStatus.BAD_REQUEST.toString());
			 		pdresponse.put("message","status is not completed");
			 		logger.debug(pdresponse.toString());
			 	
			 		return new ResponseEntity<Object>(pdresponse,HttpStatus.BAD_REQUEST);	
		
	}
	
	@RequestMapping(value="/findByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> findByDate(@RequestBody String jsonRequest,@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "pendingWith", required = true) String pendingWith,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) 
	{
		logger.debug("findByDate start");
		logger.debug("request Data"+jsonRequest);
				JSONObject jsonObject=new JSONObject(jsonRequest);
				 String startDate = jsonObject.getJSONObject("Data").getString("startDate");
				 String endDate = jsonObject.getJSONObject("Data").getString("endDate");
				 String status = jsonObject.getJSONObject("Data").getString("status");
				 String branchId = jsonObject.getJSONObject("Data").getString("branchId");
				 
				 if(startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
					 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
						response.put("status", HttpStatus.BAD_REQUEST.toString());
						response.put("message", "enter a proper Dates");
						logger.debug("final response"+response.toString());
						return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);	
				 } 
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
				 LocalDate startdate = LocalDate.parse(startDate, formatter);
				 LocalDate enddate = LocalDate.parse(endDate, formatter);
				 
				 if(status.equalsIgnoreCase("Initiated") || status.equalsIgnoreCase("Not Interested")) {
						Long branchid=Long.parseLong(branchId);
						List<LoanDetail> list3=loanInputService.findByDate(startdate,enddate,branchid);
						
						List<LoanDetail> list1 = new ArrayList<>();
						for(LoanDetail aocpCustomerData :list3) {

							if(aocpCustomerData.getStatus().equalsIgnoreCase(status) && aocpCustomerData.getBranchId() == branchid){
								list1.add(aocpCustomerData);
							}
						}
						if(list1.isEmpty()) {
							throw new NoSuchElementException("List Is Empty");	
						}
						return new ResponseEntity<Object>(list1,HttpStatus.OK);
					}
				 
				 List<AocpCustomer> list=aocpCustomerDataService.findByDateAndBranchID(startdate,enddate,branchId);
					
					List<CustomerResponse> listOfCustomer = new ArrayList<>();
					String address_Line1="";
							String state="";
							String postal="";
							
							
						for(AocpCustomer data :list) {	
							if(data.getPendingWith().equalsIgnoreCase(pendingWith)) {
								
							
							String address1 = data.getAddress();
							if(address1 == null || address1.isEmpty()) {
								
							}
							else {
								
								try {
								org.json.JSONArray addressInJson =new org.json.JSONArray(data.getAddress());
							
				  			List<Address> listAddress=new ArrayList<>();
				  			
				  			for(int n=0;n<addressInJson.length();n++) {
				  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
				  				 address_Line1 = jsonObject2.getString("ADDRESS_LINE1");	  					
				  					postal = jsonObject2.getString("PINCODE");
				  					state = jsonObject2.getString("STATE");
				  			}
								}catch (Exception e) {
									// TODO: handle exception
								}
							}
							LocalDate dateOfBirth = data.getDateOfBirth();
							DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
						   	String dob1= dateOfBirth.format(formatter1);
						   	LocalDate creationDate = data.getCreationDate();
						   	String updatedateString=null;
						   	if(data.getTimestamp() != null) {
						   		LocalDateTime timestamp = data.getTimestamp();
						   		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm:ss");
						   	  updatedateString = sdf.format(timestamp);
						   	}
						   	else {
						   		LocalDate	 updatedate = data.getUpdatedate();
						   		 updatedateString= updatedate.format(formatter1);
						   	}
						   	String creationDateString= creationDate.format(formatter1);
						   	if(data.getAppNoWithProductCode() == null) {
						   		data.setAppNoWithProductCode("VL-"+data.getApplicationNo());
						   	}
						   	String remarks = data.getRemarks();
						   	List<RemarkResponse> listOfremarks=new ArrayList<>();
						   	if(remarks != null) {
						   		try {
						   		org.json.JSONArray remark =new org.json.JSONArray(remarks);
						   		for(int n=0;n<remark.length();n++) {
						   			String date="NA";
						   			String updatedBy ="NA";
						   				JSONObject remark1 = remark.getJSONObject(n);
						   				String Decision = remark1.getString("DECISION");
						   				String RejectReason = remark1.getString("REJECTREASON");
						   				String Remarks = remark1.getString("REMARKS");
						   				if(remark1.has("DATE")) {
						   				date=remark1.getString("DATE");
						   				}
						   				if(remark1.has("UPDATEDBY")) {
						   					updatedBy=remark1.getString("UPDATEDBY");
						   				}
						   				RemarkResponse remarkResponse=new RemarkResponse(Decision,RejectReason,Remarks,date,updatedBy);
						   				listOfremarks.add(remarkResponse);
						   		}
						   		}catch (Exception e) {
									// TODO: handle exception
								}
						   	}
	CustomerResponse customerResponse=new CustomerResponse(data.getMobileNo(),data.getCustomerId(),data.getName(),dob1,address_Line1,state,postal,data.getBranchid(),data.getEligibleAmount(),data.getStatus(),creationDateString,data.getApplicationNo(),updatedateString,data.getListType(),listOfremarks,data.getAppNoWithProductCode(),data.getCreatedby());
					listOfCustomer.add(customerResponse);
						}
						}
					
					if(branchId == null || branchId.isEmpty()) {
						
						throw new EmptyInputException("branchId connot be empty");
					}
					if(status == null || status.isEmpty()) {
					
						
						if(listOfCustomer.isEmpty()) {
//							org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
//							response.put("status", HttpStatus.BAD_REQUEST.toString());
//							response.put("message", "list is empty");
//							logger.debug("final response"+response.toString());
//							return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
							
							throw new NoSuchElementException("List Is Empty");
						}
						
						org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
						//response.put("status", HttpStatus.OK);
						response.put("Data", listOfCustomer);
						logger.debug("final response"+response.toString());
						return new ResponseEntity<Object>(listOfCustomer,HttpStatus.OK);
						
					}
					
//					if(status.equalsIgnoreCase("COMPLETED") || status.equalsIgnoreCase("PROGRESS") || status.equalsIgnoreCase("approved") ||  status.equalsIgnoreCase("REJECTED") ||  status.equalsIgnoreCase("DISBURSED") || status.equalsIgnoreCase("REFERBACK") || status.equalsIgnoreCase("myTask")) {
						List<CustomerResponse> list1 = new ArrayList<>();
						for(CustomerResponse aocpCustomerData :listOfCustomer) {
							if(aocpCustomerData.getStatus().equalsIgnoreCase(status) && aocpCustomerData.getBranchId().equalsIgnoreCase(branchId)) {
								list1.add(aocpCustomerData);
							}
							
						}
						
						if(list1.isEmpty()) {
//							org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
//							response.put("status", HttpStatus.BAD_REQUEST.toString());
//							response.put("message", "list is empty");
//							logger.debug("final response"+response.toString());
//							return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
							
							throw new NoSuchElementException("List Is Empty");
						}
						
						org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
						response.put("Data", list1);
						logger.debug("final response"+response.toString());
						return new ResponseEntity<Object>(list1,HttpStatus.OK);
//					}
//					org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
//					response.put("status", HttpStatus.BAD_REQUEST.toString());
//					response.put("message", "enter proper status");
//					logger.debug("final response"+response.toString());
//					return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
						
		
				 	 
	}
	
	
	@RequestMapping(value="/fetchByIdWithImage", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNoWithImage(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchByIdWithImage Start");
		logger.debug("request Data" +jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String listType = jsonObject.getJSONObject("Data").getString("listType");
		 String type = jsonObject.getJSONObject("Data").getString("type");
		 String status = jsonObject.getJSONObject("Data").getString("status");
		 
	     if(applicationNo == null || applicationNo.isEmpty()) {
	    	 throw new EmptyInputException("input field is empty");
	     }
	     long applicationNoInLong = Long.parseLong(applicationNo);
	     
	     AocpCustomer aocpCustomer=aocpCustomerDataService.getByApp(applicationNoInLong);
	     
	     if(listType.equalsIgnoreCase("NEW") && type.equalsIgnoreCase("RO") || aocpCustomer.getApplicationNo()==0) {
	 	    ApplicationDetailsResponse response=applicationDetailsService.fetchAllByApplicationId(applicationNo);
	 	   org.json.simple.JSONObject   response1= new org.json.simple.JSONObject();
	 	   
	 	   if(response.getEkycResponse() == null) {
	 	   }
	 	   else {
	 		  org.json.JSONObject ekyc=new org.json.JSONObject(response.getEkycResponse());
	 			JSONObject PoaResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poa");
	 			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
	 			org.json.simple.JSONObject   ekycResponse= new org.json.simple.JSONObject();
			 	 org.json.simple.JSONObject   KycResponse= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   Resp= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   TransactionInfo= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   ResponseData= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   KycRes= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   UidData= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   Poa= new org.json.simple.JSONObject();
				 org.json.simple.JSONObject   Poi= new org.json.simple.JSONObject();
				 
			 	 ekycResponse.put("ResponseCode", "00");
			 	 ekycResponse.put("ResponseMessage", "Approved");
			 	 ekycResponse.put("Response2", "00");
			 	 ekycResponse.put("Response1", "00");
			 	 
			 	Resp.put("ret", "Y");
			 	Resp.put("code", "e62336010d534ea89b085156587188cb");
			 	Resp.put("ko", "KSA");
			 	Resp.put("txn", "UKC:87027720230421");
			 	Resp.put("status", 0);
			 	Resp.put("ts", "21-04-2023 14:46:53");
			 
			 	TransactionInfo.put("CA_TA", "Koramangala 2nd Block BANGALORE KAIN");
			 	TransactionInfo.put("ResponseCode", "00");
			 	TransactionInfo.put("Local_Trans_Time", 144642);
			 	TransactionInfo.put("AcqId", 200071);
			 	TransactionInfo.put("Stan", 870277);
			 	TransactionInfo.put("RRN", 144642870);
			 	TransactionInfo.put("Proc_Code", 130000);
			 	TransactionInfo.put("CA_ID", "SRY000000008000");
			 	TransactionInfo.put("Transm_Date_time", 144642870);
			 	TransactionInfo.put("CA_Tid", "register");
				TransactionInfo.put("Pan", "6080220 897783434820");
			 	TransactionInfo.put("Local_date", "0421");
			 	
			 	if(PoaResponse.has("street")) {
			 	 	Poa.put("street", PoaResponse.getString("street"));
			 	}else {
			 		Poa.put("street", "NA");	
			 	}
			 	if(PoaResponse.has("lm")) {
			 	 	Poa.put("lm", PoaResponse.getString("lm"));
			 	}else {
			 		Poa.put("lm", "NA");	
			 	}
			 	if(PoaResponse.has("loc")) {
			 	 	Poa.put("loc", PoaResponse.getString("loc"));
			 	}else {
			 		Poa.put("loc", "NA");	
			 	}
			 	if(PoaResponse.has("vtc")) {
			 	 	Poa.put("vtc", PoaResponse.getString("vtc"));
			 	}else {
			 		Poa.put("vtc", "NA");	
			 	}
			 	if(PoaResponse.has("house")) {
			 	 	Poa.put("house", PoaResponse.getString("house"));
			 	}else {
			 		Poa.put("house", "NA");	
			 	}
		
			 	if(PoaResponse.has("co")) {
			 	 	Poa.put("co", PoaResponse.getString("co"));
			 	}else {
			 		Poa.put("co", "NA");	
			 	}
			 	Poa.put("country", PoaResponse.getString("country"));

			 	Poa.put("pc", PoaResponse.getLong("pc"));

			 	Poa.put("dist", PoaResponse.getString("dist"));
			 	Poa.put("state", PoaResponse.getString("state"));
			 	//Poa.put("co", PoaResponse.getString("co"));

			 	Poi.put("gender", PoiResponse.getString("gender"));
			 	Poi.put("dob", PoiResponse.getString("dob"));
			 	Poi.put("name", PoiResponse.getString("name"));
			 	
			 	UidData.put("uid", 89778343);
			 	UidData.put("Poa", Poa);
			 	UidData.put("tkn", "01002010dn7LmHQm4C1qShIWQ9SjN4B+vEqKORs3I9lO4lWD6eSEFr2E1DXvr5eN8wcm/WrF");
				UidData.put("LData", "");
				UidData.put("Poi", Poi);
				
				String fetchBydocumenttype = aocpvImageService.fetchBydocumenttype(applicationNoInLong, "ekyc_photo");
				UidData.put("Pht", fetchBydocumenttype);
			 	KycRes.put("UidData", UidData);
			 	KycRes.put("txn", "UKC:87027720230421");
			 	KycRes.put("ttl", "2024-04-20T14:46:53");
			 	KycRes.put("ts", "2023-04-21T14:46:53.193+05:30");
			 	
			 	KycResponse.put("Resp", Resp);
			 	KycResponse.put("TransactionInfo", TransactionInfo);
			 
			 	ResponseData.put("KycResponse", KycResponse);
				ekycResponse.put("ResponseData", ResponseData);
				ekycResponse.put("KycRes", KycRes);
				ekycResponse.put("ResponseMessage", "Approved");
				ekycResponse.put("Response2", "");
				ekycResponse.put("Response1", "");  
				
				response1.put("ekycResponse", ekycResponse);
	 	   }
		 	
			
	 		response1.put("status", HttpStatus.OK.toString());
	 		response.setEkycResponse(null);
	 		response1.put("Data", response);
	 		logger.debug("final response"+response1.toString());
	 		return new ResponseEntity<Object>(response1,HttpStatus.OK);
	 	     }
	 //    logger.debug("Step 1"+applicationNoInLong);
	     
	    
	     				List<Image> list1=new ArrayList<>();
	     				// logger.debug("Step 2"+applicationNoInLong);
	     ResponseAocpCustomerData aocpCustomerData = aocpCustomerDataService.getResponse(aocpCustomer);
	    // logger.debug("Step End"+aocpCustomerData.toString());
	     				if(aocpCustomerData.getListType().equalsIgnoreCase("NEW") && status.equalsIgnoreCase("myTask")) {
	     					aocpCustomerData.setImages(list1);
	     				}
	     				else if(status.equalsIgnoreCase("DISBURSED")) {
	     					aocpCustomerData.setImages(list1);
	     				}
	     			org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
	     	 		pdresponse.put("status", HttpStatus.OK.toString());
	     	 		pdresponse.put("Data", aocpCustomerData);

	     	 		logger.debug("final response"+pdresponse.toString());
	     	 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);	
	     			
	}
	
	
	@RequestMapping(value="/fetchByBranchId", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchByBranchId(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchByBranchId Start");
		logger.debug("request Data" +jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		 String status = jsonObject.getJSONObject("Data").getString("status");
	     if(branchId == null || branchId.isEmpty()) {
	    	 throw new EmptyInputException("input field is empty");
	     }
				List<AocpvLoanCreation> list=aocpvLoanCreationService.fetchByBranchId(branchId,status);
				JSONArray array=new JSONArray(list);
				String brnetadd="";
				for (int i=0;i<list.size();i++)
				{
					
					AocpvLoanCreation aocpvLoanCreation = list.get(i);
					AocpCustomer aocpCustomer = aocpCustomerDataService.getByApp(aocpvLoanCreation.getApplicationNo());
					
					brnetadd=aocpCustomer.getBrNetAddress();
					
					System.out.println(brnetadd);
					JSONObject accDetailsJson = array.getJSONObject(i);
					JSONObject blankjson=new JSONObject();
					
					if(brnetadd==null)
					{
						accDetailsJson.put("brNetAddressDetails",blankjson);
					}
					else {
					
					if(brnetadd.isEmpty())
					{
						accDetailsJson.put("brNetAddressDetails",blankjson);
					}
					else if(brnetadd!=null && !brnetadd.isEmpty())
					{
					JSONObject brnetjson=new JSONObject(brnetadd);
					accDetailsJson.put("brNetAddressDetails",brnetjson);
					}
				
				
					}
				}
				
				JSONObject   response= new JSONObject();
     	 		//response.put("status", HttpStatus.OK);
     	 		response.put("Data", array);
     	 		logger.debug("final response"+response.toString());
     	 		return new ResponseEntity<Object>(response.toString(),HttpStatus.OK);	
		
	}
	

@RequestMapping(value="/userlogdetails", method = RequestMethod.POST)
	public ResponseEntity<Object> userlogdetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
		logger.debug("userlogdetails Start");
		logger.debug("request Data" +jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	     if(applicationNo == null || applicationNo.isEmpty()) {
	    	 throw new EmptyInputException("input field is empty");
	     }
	     long applicationNoInLong = Long.parseLong(applicationNo);
		
	     	List<UserLog>   list=userLogService.getByApplicationNo(applicationNoInLong);
	     
	     	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
 	 		//response.put("status", HttpStatus.OK);
 	 		response.put("Data", list);
 	 		logger.debug("final response"+response.toString());
 	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
	}
	
@RequestMapping(value="/verifyDocument", method = RequestMethod.POST)
public ResponseEntity<Object> verifyDocument(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	
	logger.debug("verifyDocument Start");
	logger.debug("request Data" +jsonRequest);
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 String documentType = jsonObject.getJSONObject("Data").getString("documentType");
	 String document = jsonObject.getJSONObject("Data").getString("documentNo");
	 String documentVerify = jsonObject.getJSONObject("Data").getString("documentVerify");
	 
		
     if(applicationNo == null || applicationNo.isEmpty()) {
    	 throw new EmptyInputException("input field is empty");
     }
     long applicationNoInLong = Long.parseLong(applicationNo);
     		String save=aocpvIncomeService.documentVerify(applicationNoInLong,document,documentType,documentVerify);
     	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 		//response.put("status", HttpStatus.OK);
	 		response.put("message", save);
	 		logger.debug("final response"+response.toString());
	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
}

@RequestMapping(value="/fetchByMember", method = RequestMethod.POST)
public ResponseEntity<Object> fetchByMember(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	
	logger.debug("fetchByMember Start");
	logger.debug("request Data" +jsonRequest);
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
     if(applicationNo == null || applicationNo.isEmpty()) {
    	 throw new EmptyInputException("input field is empty");
     }
     long applicationNoInLong = Long.parseLong(applicationNo);
     
     List<IncomeDetailWeb> list=aocpvIncomeService.fetchByMember(applicationNoInLong);
     //String flowStatus="ID";
   //  	List<Image> list1	=aocpvImageService.fetchByApplicationNoAndFlow(applicationNoInLong,flowStatus);
     	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 		//response.put("status", HttpStatus.OK);
    	//org.json.simple.JSONObject   response1= new org.json.simple.JSONObject();
	 		response.put("Data", list);
	 		//response.put("images", list1);
	 	//	response1.put("Data", response);
	 		//logger.debug("final response"+response.toString());
	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
}
@RequestMapping(value="/mobileVerify", method = RequestMethod.POST)
public ResponseEntity<Object> mobileVerify(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	
	logger.debug("mobileVerify Start");
	logger.debug("request Data" +jsonRequest);
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		
     if(mobile == null || mobile.isEmpty()) {
    	 throw new EmptyInputException("input field is empty");
     }
     long mobileNoInLong = Long.parseLong(mobile);
     	String message=aocpvIncomeService.mobileVerify(mobileNoInLong);
     	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 		//response.put("status", HttpStatus.OK);
	 		response.put("message", message);
	 		logger.debug("final response"+response.toString());
	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
}

@RequestMapping(value="/customerFilter", method = RequestMethod.POST)
public ResponseEntity<Object> searchMore(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	
	logger.debug("customerFilter Start");
	logger.debug("request Data" +jsonRequest);
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String cutomesearch = jsonObject.getJSONObject("Data").getString("cutomesearch");
	 String branchId = jsonObject.getJSONObject("Data").getString("branchId");
	 
	 List<AocpCustomer> list=aocpCustomerDataService.getByName(cutomesearch,branchId);
	 List<CustomerResponse> listOfCustomer = new ArrayList<>();
		String address_Line1="";
				String state="";
				String postal="";
				
				
			for(AocpCustomer data :list) {	
				String address1 = data.getAddress();
				if(address1 == null || address1.isEmpty()) {
					
				}
				else {
					org.json.JSONArray addressInJson =new org.json.JSONArray(data.getAddress());
	  			List<Address> listAddress=new ArrayList<>();
	  			
	  			for(int n=0;n<addressInJson.length();n++) {
	  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
	  				 address_Line1 = jsonObject2.getString("ADDRESS_LINE1");	  					
	  					postal = jsonObject2.getString("PINCODE");
	  					state = jsonObject2.getString("STATE");
	  			}	
				}
			//	LocalDate dateOfBirth = data.getDateOfBirth();
				DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
				String dob1="";
				if(data.getDateOfBirth()!=null) {
					LocalDate dateOfBirth = data.getDateOfBirth();	
					 dob1= dateOfBirth.format(formatter1);
				}
				 	
			   	LocalDate creationDate = data.getCreationDate();
			   	LocalDate updatedate = data.getUpdatedate();
			   	String creationDateString= creationDate.format(formatter1);
			   	String updatedateString= updatedate.format(formatter1);
			   	if(data.getAppNoWithProductCode() == null) {
			   		data.setAppNoWithProductCode("VL-"+data.getApplicationNo());
			   	}
				String remarks = data.getRemarks();
			   	List<RemarkResponse> listOfremarks=new ArrayList<>();
			   	if(remarks != null) {
			   		org.json.JSONArray remark =new org.json.JSONArray(remarks);
			   		for(int n=0;n<remark.length();n++) {
			   			String date="NA";
			   			String updatedBy ="NA";
			   				JSONObject remark1 = remark.getJSONObject(n);
			   				String Decision = remark1.getString("DECISION");
			   				String RejectReason = remark1.getString("REJECTREASON");
			   				String Remarks = remark1.getString("REMARKS");
			   				if(remark1.has("DATE")) {
			   				date=remark1.getString("DATE");
			   				}
			   				if(remark1.has("UPDATEDBY")) {
			   					updatedBy=remark1.getString("UPDATEDBY");
			   				}
			   				RemarkResponse remarkResponse=new RemarkResponse(Decision,RejectReason,Remarks,date,updatedBy);
			   				listOfremarks.add(remarkResponse);
			   		}
			   	}
CustomerResponse customerResponse=new CustomerResponse(data.getMobileNo(),data.getCustomerId(),data.getName(),dob1,address_Line1,state,postal,data.getBranchid(),data.getEligibleAmount(),data.getStatus(),creationDateString,data.getApplicationNo(),updatedateString,data.getListType(),listOfremarks,data.getAppNoWithProductCode(),data.getCreatedby());
		listOfCustomer.add(customerResponse);
			}
	 
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			//response.put("status", HttpStatus.OK);
			response.put("Data", listOfCustomer);
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}
@RequestMapping(value="/statusChange", method = RequestMethod.POST)
public ResponseEntity<Object> statusChange(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("statusChange Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 String status = jsonObject.getJSONObject("Data").getString("status");
	 				long customerId = aocpCustomerDataService.statusChange(applicationNo,status);
	 				if(customerId != 0) {
	 					loanInputService.statusChange(customerId,status);
	 				}
	 				org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 				//response.put("status", HttpStatus.OK);
	 				response.put("message", "status updated");
	 				logger.debug("final response"+response.toString());
	 				return new ResponseEntity<Object>(response,HttpStatus.OK);

}
@RequestMapping(value="/fetchLoandetails", method = RequestMethod.POST)
public ResponseEntity<Object> fetchLoandetails(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("statusChange Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 String status = jsonObject.getJSONObject("Data").getString("status");
	 	AocpvLoanCreation aocpvLoanCreation=aocpvLoanCreationService.getByApplicationNo(applicationNoInLong,status);
	 	
	 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			//response.put("status", HttpStatus.OK);
			response.put("Data", aocpvLoanCreation);
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}
@RequestMapping(value="/deleteMember", method = RequestMethod.POST)
public ResponseEntity<Object> deleteMember(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("deleteMember Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 String member = jsonObject.getJSONObject("Data").getString("member");
	
	String message= aocpvIncomeService.deleteMember(applicationNoInLong,member);
	 	
	 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", message);
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}
@RequestMapping(value="/accountDetails", method = RequestMethod.POST)
public ResponseEntity<Object> accountDetails(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("accountDetails Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 String accountDetails = jsonObject.getJSONObject("Data").getJSONObject("accountDetails").toString();
	 String brNetAddressDetails = jsonObject.getJSONObject("Data").getJSONObject("brNetAddressDetails").toString();
	 brmastersservice.saveData(brNetAddressDetails, applicationNo);
	 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	AocpvLoanCreation a = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
	a.setAccountData(accountDetails);
	if(a.getUpload_sancation_letter().equalsIgnoreCase("NO") || a.getUpload_aggreement_letter().equalsIgnoreCase("NO")){
		response.put("upload_sancation_letter", a.getUpload_sancation_letter());
		response.put("upload_aggreement_letter", a.getUpload_aggreement_letter());
	}else {
		aocpvLoanCreationService.saveData(a);
		response.put("upload_sancation_letter", a.getUpload_sancation_letter());
		response.put("upload_aggreement_letter", a.getUpload_aggreement_letter());
	}
			
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}
@RequestMapping(value="/fetchMemberName", method = RequestMethod.POST)
public ResponseEntity<Object> fetchMemberName(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("fetchMemberName Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 List<String> list=aocpvImageService.fetchMemberName(applicationNoInLong);
	 org.json.simple.JSONArray   array= new org.json.simple.JSONArray();
	for(String other:list) {
		array.add(other);
	}
	org.json.simple.JSONArray   others= new org.json.simple.JSONArray();
	 	others.add("utilityBillPhoto");
	 	others.add("houseImageInside");
	 	others.add("houseImageOutside");	






	 	others.add("roadsideImage");	
AocpCustomer fetchByApp = aocpCustomerDataService.fetchByApp(applicationNoInLong);
	 	if(fetchByApp.getBuisness().equalsIgnoreCase("YES")) {
	 		others.add("buisnessPhoto");	
	 	}	
	 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("List", array);
			response.put("OTHERS", others);
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}
@RequestMapping(value="/fetchMemberImage", method = RequestMethod.POST)
public ResponseEntity<Object> fetchMemberImage(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("fetchMemberImage Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 String member = jsonObject.getJSONObject("Data").getString("member");
	 String document = jsonObject.getJSONObject("Data").getString("document");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 List<Image> list=aocpvImageService.fetchMemberImage(applicationNoInLong,member,document);
	 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 	if(member.equalsIgnoreCase("self")) {
	 		AocpvIncomeDetails incomeDetails = aocpvIncomeService.getByApplicationNoAndmember(applicationNoInLong, member);
		 	if(incomeDetails.getForm60() != null) {
		 		if(incomeDetails.getForm60().equalsIgnoreCase("YES")) {
		 			response.put("form60", "YES");
		 		}else {
		 			response.put("form60", "NO");
		 		}
		 	}	
	 	}
			response.put("List", list);
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}

@RequestMapping(value="/fetchBydocumenttype", method = RequestMethod.POST)
public ResponseEntity<Object> fetchBydocumenttype(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("fetchBydocumenttype Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	 String documentType = jsonObject.getJSONObject("Data").getString("documentType");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 String image=aocpvImageService.fetchBydocumenttype(applicationNoInLong,documentType);
	 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("pdf", image);
			return new ResponseEntity<Object>(response,HttpStatus.OK);
}

@RequestMapping(value="/uploadValidation", method = RequestMethod.POST)
public ResponseEntity<Object> imageValidation(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("fetchBydocumenttype Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
	 long applicationNoInLong = Long.parseLong(applicationNo);
	 String response="";
	 List<AocpvIncomeDetails> list = aocpvIncomeService.getByApplicationNo(applicationNoInLong);
	 for(AocpvIncomeDetails aocpvIncomeDetails :list)
	 {
	
		 String form60 = aocpvIncomeDetails.getForm60();
		 System.out.println(form60);
		 String member = aocpvIncomeDetails.getMember();
		
		 if(form60==null)
		 {
			 response="Success";
		 }
		 else if(form60.equalsIgnoreCase("YES"))
		 {
		 String fetchMemberImageByApplicationNo = aocpvImageService.fetchMemberImageByApplicationNo(applicationNoInLong,member,"form60Photo");
		 System.out.println(fetchMemberImageByApplicationNo);
		 response=fetchMemberImageByApplicationNo;
		 }
		 
	 }
	 response =aocpvImageService.validateUDImage(applicationNoInLong);
	 if(response.equalsIgnoreCase("Success"))
	 {
		 org.json.simple.JSONObject   resp= new org.json.simple.JSONObject();
		 	org.json.simple.JSONObject  success=new org.json.simple.JSONObject();
		 	success.put("Description", "Success");
		 	resp.put("Success",success);
				return new ResponseEntity<Object>(resp,HttpStatus.OK);
	 }
	 	org.json.simple.JSONObject   resp= new org.json.simple.JSONObject();
	 	org.json.simple.JSONObject  error=new org.json.simple.JSONObject();
	 	error.put("Code","400");
	 	error.put("Description", response);
	 	resp.put("Error",error);
			return new ResponseEntity<Object>(resp,HttpStatus.BAD_REQUEST);
}
	@RequestMapping(value="/rejectData", method = RequestMethod.POST)
	public ResponseEntity<Object> rejectData(@RequestBody String jsonRequest,
		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
		 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
		 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
		 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
		 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
	logger.debug("rejectData Start");
	JSONObject jsonObject=new JSONObject(jsonRequest);
	 
	 String startDate = jsonObject.getJSONObject("Data").getString("startDate");
	 String endDate = jsonObject.getJSONObject("Data").getString("endDate");
	 String status = jsonObject.getJSONObject("Data").getString("status");
	
	 if(startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "enter a proper Dates");
			logger.debug("final response"+response.toString());
			return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);	
	 } 
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
	 LocalDate startdate = LocalDate.parse(startDate, formatter);
	 LocalDate enddate = LocalDate.parse(endDate, formatter);
	 List<Long> listOfApplicationNo=new ArrayList<>();
	 List<AocpCustomer> list =aocpCustomerDataService.findByDateAndStatus(startdate,enddate,status);
	 for(AocpCustomer aocpCustomer:list) {
		 aocpCustomer.setStatus("REJECTED");
		 aocpCustomer.setRejectedBy("BACKEND");
		 aocpCustomer.setUpdatedate(LocalDate.now());
		 userLogService.save(X_User_ID,Long.toString(aocpCustomer.getApplicationNo()),"REJECTED","BACKEND",aocpCustomer.getVersioncode());
		PreApprovedListVikasLoan byReferenceNo = loanInputService.getByReferenceNo(Long.toString(aocpCustomer.getCustomerId()));
			byReferenceNo.setStatus("REJECTED");	
			byReferenceNo.setUpdatedate(LocalDate.now());
			 loanInputService.saveSingleData(byReferenceNo);
			String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
			listOfApplicationNo.add(aocpCustomer.getApplicationNo());
	 }
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 	response.put("message","Data updated");
		 	response.put("Data",listOfApplicationNo);
				return new ResponseEntity<Object>(response,HttpStatus.OK);

	}
	
	@RequestMapping(value="/calculateEmi", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateEmi(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("calculateEmi Start");
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String productCode = jsonObject.getJSONObject("Data").getString("productCode");
		 
		 JSONObject EMICalculator=aocpCustomerDataService.calculateEmi(productCode);
		 	
				return new ResponseEntity<Object>(EMICalculator.toString(),HttpStatus.OK);
	}
}
