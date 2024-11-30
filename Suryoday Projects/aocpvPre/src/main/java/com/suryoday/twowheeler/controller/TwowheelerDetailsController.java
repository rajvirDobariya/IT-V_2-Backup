package com.suryoday.twowheeler.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.RemarkResponse;
import com.suryoday.twowheeler.pojo.AdditionalInfoResponse;
import com.suryoday.twowheeler.pojo.BsrDetailsResponse;
import com.suryoday.twowheeler.pojo.CatCompanies;
import com.suryoday.twowheeler.pojo.DealerMaster;
import com.suryoday.twowheeler.pojo.DisbustmentDetailsResponse;
import com.suryoday.twowheeler.pojo.DocCheckListResponse;
import com.suryoday.twowheeler.pojo.LeegalityInfo;
import com.suryoday.twowheeler.pojo.LoanDetailsResponse;
import com.suryoday.twowheeler.pojo.TWNominee;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMemberResponse;
import com.suryoday.twowheeler.pojo.TwowheelerAssetDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerPDResponse;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.pojo.UtilityBillResponse;
import com.suryoday.twowheeler.service.PreApprovalListService;
import com.suryoday.twowheeler.service.TwoWheelerMastersService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;
import com.suryoday.twowheeler.service.TwowheelerLeegalityService;
import com.suryoday.twowheeler.service.TwowheelerLoanCreationService;
import com.suryoday.twowheeler.service.TwowheelerUserLogService;

@RestController
@RequestMapping("/twowheelerDetails/customer")
public class TwowheelerDetailsController {

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@Autowired
	PreApprovalListService loanInputService;

	@Autowired
	TwowheelerImageService imageService;
	
	@Autowired
	TwoWheelerMastersService twowheelermastersservice;
	
	@Autowired 
	TwowheelerLoanCreationService loancreationservice;
	
	@Autowired 
	TwowheelerUserLogService twowheelerUserLogService;
	
	@Autowired 
	TwowheelerLeegalityService leegalityService;
	
	Logger logger = LoggerFactory.getLogger(TwowheelerDetailsController.class);

	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		String name = jsonObject.getJSONObject("Data").getString("name");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String appNoWithProductCode = jsonObject.getJSONObject("Data").getString("appNoWithProductCode");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String dateOfBirth = jsonObject.getJSONObject("Data").getString("dateOfBirth");
		String pancard = jsonObject.getJSONObject("Data").getString("pancard");
		String pancardVerify = jsonObject.getJSONObject("Data").getString("pancardVerify");
		String aadharNo = jsonObject.getJSONObject("Data").getString("aadharNo");
		String addressVerify = jsonObject.getJSONObject("Data").getString("addressVerify");
		String aadharNoVerify = jsonObject.getJSONObject("Data").getString("aadharNoVerify");
		String address = jsonObject.getJSONObject("Data").getJSONArray("address").toString();
		
		
		
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByAppNo(applicationNo);
		
		TwoWheelerFamilyMember familyMember=familyMemberService.fetchByApplicationNoAndMember(applicationNo,"APPLICANT");
		
		if(twowheelerDetails.getListType().equalsIgnoreCase("PRE")) {
			String preApprovalAmount = jsonObject.getJSONObject("Data").getString("preApprovalAmount");
			String accountBranchId = jsonObject.getJSONObject("Data").getString("accountBranchId");
			String gender = jsonObject.getJSONObject("Data").getString("gender");
			String married = jsonObject.getJSONObject("Data").getString("married");
			String occupation = jsonObject.getJSONObject("Data").getString("occupation");
			String education = jsonObject.getJSONObject("Data").getString("education");
			String educationCode = jsonObject.getJSONObject("Data").getString("educationCode");
			String marriedCode = jsonObject.getJSONObject("Data").getString("marriedCode");
			String genderCode = jsonObject.getJSONObject("Data").getString("genderCode");
			String occCode = jsonObject.getJSONObject("Data").getString("occCode");
			twowheelerDetails.setPreApprovalAmount(preApprovalAmount);
			twowheelerDetails.setAccountBranchId(accountBranchId);
			twowheelerDetails.setCustomerId(customerId);
			
		 	familyMember.setGender(gender);
		 	familyMember.setMarried(married);
		 	familyMember.setOccupation(occupation);
		 	familyMember.setEducation(education);
		    familyMember.setMarriedCode(marriedCode);
		 	familyMember.setEducationCode(educationCode);
		 	familyMember.setOccCode(occCode);
		 	familyMember.setGenderCode(genderCode);
		 	
			if(twowheelerDetails.getAccountNumber() == null) {
				twowheelerDetails=twowheelerDetailsService.getCustomerDetails(twowheelerDetails);	
			}	
		}
		
		
		if(jsonObject.getJSONObject("Data").has("form60")) {
			String form60 = jsonObject.getJSONObject("Data").getString("form60");
			twowheelerDetails.setForm60(form60);
			familyMember.setForm60(form60);
		}if(jsonObject.getJSONObject("Data").has("requiredAmount")) {
			String requiredAmount = jsonObject.getJSONObject("Data").getString("requiredAmount");
			twowheelerDetails.setRequiredAmount(requiredAmount);
		}if(jsonObject.getJSONObject("Data").has("selectedIdentityProof")) {
			String selectedIdentityProof = jsonObject.getJSONObject("Data").getString("selectedIdentityProof");
			twowheelerDetails.setSelectedIdentityProof(selectedIdentityProof);
			familyMember.setSelectedIdentityProof(selectedIdentityProof);
		}if(jsonObject.getJSONObject("Data").has("identityProof")) {
			String identityProof = jsonObject.getJSONObject("Data").getString("identityProof");
			twowheelerDetails.setIdentityProof(identityProof);
			familyMember.setIdentityProof(identityProof);
		}if(jsonObject.getJSONObject("Data").has("identityProofVerify")) {
			String identityProofVerify = jsonObject.getJSONObject("Data").getString("identityProofVerify");
			twowheelerDetails.setIdentityProof(identityProofVerify);
			familyMember.setIdentityProofVerify(identityProofVerify);
		}
			twowheelerDetails.setCustomerId(customerId);
			String[] split = name.split(" ");
	 	     int length = split.length;
	 	    if(jsonObject.getJSONObject("Data").has("voterId")) {
				String voterId = jsonObject.getJSONObject("Data").getString("voterId");
				twowheelerDetails.setVoterId(voterId);
				twowheelerDetails.setVoterId(voterId);
			}if(jsonObject.getJSONObject("Data").has("voterIdVerify")) {
				String voterIdVerify = jsonObject.getJSONObject("Data").getString("voterIdVerify");
				twowheelerDetails.setVoterIdVerify(voterIdVerify);
				twowheelerDetails.setVoterIdVerify(voterIdVerify);
			}
	 	   familyMember.setAppNoWithProductCode(appNoWithProductCode);
	 	   familyMember.setApplicationNo(applicationNo);
	 	   familyMember.setFirstName(split[0]);
	 	   familyMember.setLastName(split[length-1]);
	 	   familyMember.setDob(dateOfBirth);
	 	   familyMember.setMobile(mobileNo);
	 	   familyMember.setPanCard(pancard);
	 	   familyMember.setPancardNoVerify(pancardVerify);
	 	   familyMember.setAadharCard(aadharNo);
	 	   familyMember.setAadharNoVerify(aadharNoVerify);
	 	   familyMember.setCreatedTimestamp(LocalDateTime.now());
	 	   familyMember.setMember("APPLICANT");

	 	  
	 	   
	 	  String saveData = familyMemberService.saveData(familyMember);
		twowheelerDetails.setApplicationNo(applicationNo);
		twowheelerDetails.setName(name);
		twowheelerDetails.setAppNoWithProductCode(appNoWithProductCode);
		twowheelerDetails.setDateOfBirth(dateOfBirth);
		twowheelerDetails.setMobileNo(mobileNo);
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setFlowStatus("TWPD");
		twowheelerDetails.setAadharNo(aadharNo);
		twowheelerDetails.setAadharNoVerify(aadharNoVerify);
		twowheelerDetails.setPancard(pancard);
		twowheelerDetails.setPancardVerify(pancardVerify);
		twowheelerDetails.setSalesBranchId(branchId);
		twowheelerDetails.setCreatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setAddress(address);
		twowheelerDetails.setPersonalDetailsVerify("YES");
		if(twowheelerDetails.getSalesCreatedBy() ==null) {
			twowheelerDetails.setSalesCreatedBy(X_User_ID);
		}
//		if(twowheelerDetails.getListType().equalsIgnoreCase("PRE")) {
//			LoanDetails loanDetails = loanInputService.getByReferenceNo(customerId);
//			loanDetails.setStatus("PROGRESS");
//			LoanDetails saveSingleData2 = loanInputService.saveSingleData(loanDetails);
//		}
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
								twowheelerUserLogService.saveUserLog(applicationNo,"SALE",X_User_ID);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByApplicationNo", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");

		TwowheelerDetailsResponse fetchByApplicationNo = twowheelerDetailsService.getByApplicationNo(applicationNo,"mob");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String startDate = jsonObject.getJSONObject("Data").getString("startDate");
		String endDate = jsonObject.getJSONObject("Data").getString("endDate");
		String status = jsonObject.getJSONObject("Data").getString("status");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String listType = jsonObject.getJSONObject("Data").getString("listType");
		
		if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "enter a proper Dates");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
//			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);

		if (status.equalsIgnoreCase("Initiated") || status.equalsIgnoreCase("Not Interested")) {
			LocalDate start = startdate.toLocalDate();
			LocalDate end = enddate.toLocalDate();

			Long branchid = Long.parseLong(branchId);
			List<LoanDetail> list3 = loanInputService.findByDate(start, end, branchid);

			List<LoanDetail> list1 = new ArrayList<>();
			for (LoanDetail aocpCustomerData : list3) {
				if (aocpCustomerData.getStatus().equalsIgnoreCase(status)
						&& aocpCustomerData.getBranchId() == branchid) {
					list1.add(aocpCustomerData);
				}
			}
			if (list1.isEmpty()) {
				throw new NoSuchElementException("List Is Empty");
			}
			return new ResponseEntity<Object>(list1, HttpStatus.OK);
		}
		List<TwowheelerResponse> fetchByApplicationNo = twowheelerDetailsService.fetchByDateAndbranch(startdate, enddate, status,
				branchId,listType,X_User_ID);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/savefamilyDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> savefamilyDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		TwoWheelerFamilyMember familyMember=familyMemberService.fetchByApplicationNoAndMember(applicationNo,member);
	
		String firstName = jsonObject.getJSONObject("Data").getString("firstName");
		String middleName = jsonObject.getJSONObject("Data").getString("middleName");
		String lastName = jsonObject.getJSONObject("Data").getString("lastName");
		String gender = jsonObject.getJSONObject("Data").getString("gender");
		String age = jsonObject.getJSONObject("Data").getString("age");
		String dob = jsonObject.getJSONObject("Data").getString("dob");
//		LocalDate date = LocalDate.parse(dobInput);
//		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		String dob = date.format(dateTimeFormatter);
		String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		String address = jsonObject.getJSONObject("Data").getString("address");
		String mobileVerify = jsonObject.getJSONObject("Data").getString("mobileVerify");
		String married = jsonObject.getJSONObject("Data").getString("married");
		
		if (jsonObject.getJSONObject("Data").has("aadharCard")) {
			String aadharCard = jsonObject.getJSONObject("Data").getString("aadharCard");
			familyMember.setAadharCard(aadharCard);
		}
		if (jsonObject.getJSONObject("Data").has("aadharNoVerify")) {
			String aadharNoVerify = jsonObject.getJSONObject("Data").getString("aadharNoVerify");
			familyMember.setAadharNoVerify(aadharNoVerify);
		}
		if (jsonObject.getJSONObject("Data").has("panCard")) {
			String panCard = jsonObject.getJSONObject("Data").getString("panCard");
			familyMember.setPanCard(panCard);
		}
		if (jsonObject.getJSONObject("Data").has("pancardNoVerify")) {
			String pancardNoVerify = jsonObject.getJSONObject("Data").getString("pancardNoVerify");
			familyMember.setPancardNoVerify(pancardNoVerify);
		}
		if (jsonObject.getJSONObject("Data").has("voterId")) {
			String voterId = jsonObject.getJSONObject("Data").getString("voterId");
			familyMember.setVoterId(voterId);
		}
		if (jsonObject.getJSONObject("Data").has("voterIdVerify")) {
			String voterIdVerify = jsonObject.getJSONObject("Data").getString("voterIdVerify");
			familyMember.setVoterIdVerify(voterIdVerify);
		}
		if (jsonObject.getJSONObject("Data").has("earning")) {
			String earning = jsonObject.getJSONObject("Data").getString("earning");
			familyMember.setEarning(earning);
		}
		if (jsonObject.getJSONObject("Data").has("GuardianDetails")) {
			String guardian = jsonObject.getJSONObject("Data").getJSONObject("GuardianDetails").toString();
			familyMember.setGuardian(guardian);
		}
		if (jsonObject.getJSONObject("Data").has("nomineeRelationship")) {
			String nomineeRelationship = jsonObject.getJSONObject("Data").getString("nomineeRelationship");
			familyMember.setNomineeRelationship(nomineeRelationship);
		}
		if(member.equalsIgnoreCase("Nominee") || member.equalsIgnoreCase("APPLICANT") || member.equalsIgnoreCase("CO_APPLICANT")) {	
		}else {
			throw new NoSuchElementException("Please Add Proper Member"+member+" not allowed");
		}
		if(!member.equalsIgnoreCase("Nominee")) {
			String occupation = jsonObject.getJSONObject("Data").getString("occupation");
			String title = jsonObject.getJSONObject("Data").getString("title");
			String category = jsonObject.getJSONObject("Data").getString("category");
			String fatherName = jsonObject.getJSONObject("Data").getString("fatherName");
			String motherName = jsonObject.getJSONObject("Data").getString("motherName");
			String caste = jsonObject.getJSONObject("Data").getString("caste");
			String education = jsonObject.getJSONObject("Data").getString("education");
			String noOfDependent = jsonObject.getJSONObject("Data").getString("noOfDependent");
			String noOfFamilyMember = jsonObject.getJSONObject("Data").getString("noOfFamilyMember");
			String annualIncome = jsonObject.getJSONObject("Data").getString("annualIncome");
			String companyName = jsonObject.getJSONObject("Data").getString("companyName");
			String form60 = jsonObject.getJSONObject("Data").getString("form60");
			String selectedIdentityProof = jsonObject.getJSONObject("Data").getString("selectedIdentityProof");
			String identityProof = jsonObject.getJSONObject("Data").getString("identityProof");
			String identityProofVerify = jsonObject.getJSONObject("Data").getString("identityProofVerify");
			String occCode = jsonObject.getJSONObject("Data").getString("occCode");
			String religionCode = jsonObject.getJSONObject("Data").getString("religionCode");
			String casteCode = jsonObject.getJSONObject("Data").getString("casteCode");
			String educationCode = jsonObject.getJSONObject("Data").getString("educationCode");
			String marriedCode = jsonObject.getJSONObject("Data").getString("marriedCode");
			String genderCode = jsonObject.getJSONObject("Data").getString("genderCode");
			String titleCode = jsonObject.getJSONObject("Data").getString("titleCode");
			String categoryCode = jsonObject.getJSONObject("Data").getString("categoryCode");
			
			familyMember.setOccupation(occupation);
			familyMember.setTitle(title);
			familyMember.setFatherName(fatherName);
			familyMember.setMotherName(motherName);
			familyMember.setCaste(caste);
			familyMember.setEducation(education);
			familyMember.setNoOfDependent(noOfDependent);
			familyMember.setAnnualIncome(annualIncome);
			familyMember.setCompanyName(companyName);
			familyMember.setForm60(form60);
			familyMember.setNoOfFamilyMember(noOfFamilyMember);
			familyMember.setSelectedIdentityProof(selectedIdentityProof);
			familyMember.setIdentityProof(identityProof);
			familyMember.setIdentityProofVerify(identityProofVerify);
			familyMember.setOccCode(occCode);
			familyMember.setReligionCode(religionCode);
			familyMember.setCasteCode(casteCode);
			familyMember.setEducationCode(educationCode);
			familyMember.setMarriedCode(marriedCode);
			familyMember.setGenderCode(genderCode);
			familyMember.setTitleCode(titleCode);
			familyMember.setCategoryCode(categoryCode);
		}
		
		familyMember.setApplicationNo(applicationNo);
		familyMember.setFirstName(firstName);
		familyMember.setMiddleName(middleName);
		familyMember.setLastName(lastName);
		familyMember.setGender(gender);
		familyMember.setAge(age);
		familyMember.setDob(dob);
		familyMember.setMobile(mobile);
		familyMember.setMobileVerify(mobileVerify);
		familyMember.setMarried(married);
		familyMember.setMember(member);
		familyMember.setAddress(address);
		familyMember.setCreatedTimestamp(LocalDateTime.now());
		String saveSingleData = familyMemberService.saveData(familyMember);
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		if(member.equalsIgnoreCase("Nominee")) {
			 twowheelerDetails.setFlowStatus("TWNOM");
			 twowheelerDetails.setAddNomineeVerify("YES");
		}
		else {
			twowheelerDetails.setFlowStatus("TWMM");
		}
		twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestParam("file") MultipartFile[] files,
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		
		if (!document.isEmpty()) {
			imageService.saveImage(files, applicationNo, document, "TWPD",member);
		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "Image Saved");
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveAssetDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAssetDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String manufacture = jsonObject.getJSONObject("Data").getString("manufacture");
		String model = jsonObject.getJSONObject("Data").getString("model");
		String variant = jsonObject.getJSONObject("Data").getString("variant");
		String dealerLocation = jsonObject.getJSONObject("Data").getString("dealerLocation");
		String dealerName = jsonObject.getJSONObject("Data").getString("dealerName");
		String dealerCode = jsonObject.getJSONObject("Data").getString("DealerCode");
		String variantCode = jsonObject.getJSONObject("Data").getString("variantCode");
		
		List<DealerMaster> list = twowheelermastersservice.fetchByDealerCode(dealerCode);
		DealerMaster dealerMaster = list.get(0);
		String exShowroomPrice = jsonObject.getJSONObject("Data").getString("exShowroomPrice");
		String roadTax = jsonObject.getJSONObject("Data").getString("roadTax");
		String RegistrationCharges = jsonObject.getJSONObject("Data").getString("RegistrationCharges");
		String accessories = jsonObject.getJSONObject("Data").getString("accessories");
		String addonsCharges = jsonObject.getJSONObject("Data").getString("addonsCharges");
		String totalOnRoadPriceUser = jsonObject.getJSONObject("Data").getString("totalOnRoadPriceUser");
		String totalOnRoadPriceAllocated = jsonObject.getJSONObject("Data").getString("totalOnRoadPriceAllocated");
		String engineNumber = jsonObject.getJSONObject("Data").getString("engineNumber");
		String chasisNumber = jsonObject.getJSONObject("Data").getString("chasisNumber");
		String yearOfManufacturer = jsonObject.getJSONObject("Data").getString("yearOfManufacturer");
		
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);

		twowheelerDetails.setFlowStatus("TWASSET");
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setManufacture(manufacture);
		twowheelerDetails.setModel(model);
		twowheelerDetails.setVariant(variant);
		twowheelerDetails.setVariantCode(variantCode);
		twowheelerDetails.setDealerLocation(dealerLocation);
		twowheelerDetails.setDealerName(dealerName);
		twowheelerDetails.setExShowroomPrice(exShowroomPrice);
		twowheelerDetails.setRoadTax(roadTax);
		twowheelerDetails.setRegistrationCharges(RegistrationCharges);
		twowheelerDetails.setAccessories(accessories);
		twowheelerDetails.setAddonsCharges(addonsCharges);
		twowheelerDetails.setTotalOnRoadPrice(totalOnRoadPriceUser);
		twowheelerDetails.setTotalOnRoadPriceAllocated(totalOnRoadPriceAllocated);
		twowheelerDetails.setChasisNumber(chasisNumber);
		twowheelerDetails.setBeneficiaryName(dealerMaster.getDealerName());
		twowheelerDetails.setBeneficiaryAccountNo(dealerMaster.getAccountNumber());
		twowheelerDetails.setBeneficiaryBankName(dealerMaster.getBankName());
		twowheelerDetails.setBeneficiaryIFSC(dealerMaster.getIfscCode());
		twowheelerDetails.setBeneficiaryBranchName(dealerMaster.getBranchDescription());
		twowheelerDetails.setEngineNumber(engineNumber);
		twowheelerDetails.setDealerCode(dealerCode);
		twowheelerDetails.setYearOfManufacturer(yearOfManufacturer);
		twowheelerDetails.setAssetDetailsVerify("YES");
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/customSearch", method = RequestMethod.POST)
	public ResponseEntity<Object> customSearch(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);

		String customSearch = jsonObject.getJSONObject("Data").getString("customSearch");
//			 String branchId = jsonObject.getJSONObject("Data").getString("branchId");

		if (customSearch == null || customSearch.isEmpty()) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "enter a proper Dates");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		List<TwowheelerResponse> fetchByApplicationNo = twowheelerDetailsService.customSearch(customSearch);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveLoanDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> saveLoanDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String scheme = jsonObject.getJSONObject("Data").getString("scheme");
		String marginMoney = jsonObject.getJSONObject("Data").getString("marginMoney");
		String amount = jsonObject.getJSONObject("Data").getString("amount");
		String rateOfInterest = jsonObject.getJSONObject("Data").getString("rateOfInterest");
		String effectiveIrr = jsonObject.getJSONObject("Data").getString("effectiveIrr");
		String tenure = jsonObject.getJSONObject("Data").getString("tenure");
		String tenureMin = jsonObject.getJSONObject("Data").getString("tenureMin");
		String tenureMax = jsonObject.getJSONObject("Data").getString("tenureMax");
		String emi = jsonObject.getJSONObject("Data").getString("emi");
		String insuranceEmi = jsonObject.getJSONObject("Data").getString("insuranceEmi");
		String totalOnRoadPrice = jsonObject.getJSONObject("Data").getString("totalOnRoadPrice");
		String finaldisbustmentAmount = jsonObject.getJSONObject("Data").getString("finaldisbustmentAmount");
		String totaldeductionAmount = jsonObject.getJSONObject("Data").getString("totaldeductionAmount");
		String loanCharges = jsonObject.getJSONObject("Data").getJSONArray("loanCharges").toString();
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
//		if(twowheelerDetails.getListType().equalsIgnoreCase("PRE") ){
//			LoanDetails fetchByCustomerId = loanInputService.fetchByCustomerId(Long.parseLong(twowheelerDetails.getCustomerId()));
//			twowheelerDetails.setSanctiondate(fetchByCustomerId.getCreateDate());	
//		}
		twowheelerDetails.setFlowStatus("TWLNCHR");
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setScheme(scheme);
		twowheelerDetails.setMarginMoney(marginMoney);
		twowheelerDetails.setAmount(amount);
		twowheelerDetails.setRateOfInterest(rateOfInterest);
		twowheelerDetails.setTenure(tenure);
		twowheelerDetails.setTenureMin(tenureMin);
		twowheelerDetails.setTenureMax(tenureMax);
		twowheelerDetails.setEmi(emi);
		twowheelerDetails.setEffectiveIrr(effectiveIrr);
		twowheelerDetails.setInsuranceEmi(insuranceEmi);
		twowheelerDetails.setTotalOnRoadPrice(totalOnRoadPrice);
		twowheelerDetails.setLoanCharges(loanCharges);
		twowheelerDetails.setTotaldeductionAmount(totaldeductionAmount);
		twowheelerDetails.setFinaldisbustmentAmount(finaldisbustmentAmount);
		twowheelerDetails.setLoanChargeVerify("YES");
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchFamilyDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchFamilyDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");

		List<TWNominee> fetchByApplicationNo = familyMemberService.getNomineeDetails(applicationNo);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
	
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

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
		 TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		 org.json.simple.JSONArray   selfCompulsory= new org.json.simple.JSONArray();
		 selfCompulsory.add("aadharFront");
		 selfCompulsory.add("aadharBack");	
		 selfCompulsory.add("Pan");
		if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
			selfCompulsory.add("customerPhoto");
		}
		 org.json.simple.JSONArray   selfOptional= new org.json.simple.JSONArray();
		 selfOptional.add("VoterIdFront");
		 selfOptional.add("VoterIdBack");
		 selfOptional.add("DrivingLicense");
		 selfOptional.add("Passport");
		 selfOptional.add("RationCard");
	 
		 org.json.simple.JSONArray   otherCompulsory= new org.json.simple.JSONArray();
		 otherCompulsory.add("EbBill");

		 org.json.simple.JSONArray   otherOptional= new org.json.simple.JSONArray();
		 otherOptional.add("ITRCopy");
		 otherOptional.add("Form16");
		 otherOptional.add("RentedAgreement");	
		 otherOptional.add("roadsideImage");
		 otherOptional.add("EducationProof");	
		 otherOptional.add("EmployementProof");
		 otherOptional.add("RC");
		 otherOptional.add("PNG");	
		 otherOptional.add("LPG");
		 otherOptional.add("NotarizedAgreement");
		 otherOptional.add("ManualFI");	
		 otherOptional.add("BloodRelationProof");
		 otherOptional.add("ManualFI");
		 otherOptional.add("DisbursementMemo");
		 otherOptional.add("RepaymentPDC1");
		 otherOptional.add("RepaymentPDC2");
		 otherOptional.add("SecurityPDC1");
		 otherOptional.add("SecurityPDC2");
		
		 if(twowheelerDetails.getStage().equalsIgnoreCase("PSD")) {
			 otherCompulsory.add("Invoice");
			 otherCompulsory.add("AssetInsurance");
			 otherCompulsory.add("RTO");
			 otherCompulsory.add("DeliveryOrder");
			 otherCompulsory.add("MarginMoney");
			 otherCompulsory.add("CreditLifeInsurance");
			
		 }else {
			 otherOptional.add("Invoice");
			 otherOptional.add("AssetInsurance");
			 otherOptional.add("RTO");
			 otherOptional.add("DeliveryOrder");
			 otherOptional.add("MarginMoney");
			 otherOptional.add("CreditLifeInsurance");
			
		 }
		 
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 response.put("SELF_COMPULSORY", selfCompulsory);
		 response.put("SELF_OPTIONAL", selfOptional);
		 response.put("LOAN_DOCUMENTS_COMPULSORY", otherCompulsory);
		 response.put("LOAN_DOCUMENTS_OPTIONAL", otherOptional);
		 
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
		 String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		 
		 List<Image> list=imageService.getByAppNoAndMember(applicationNo,member,documentType);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("List", list);
				logger.debug("final response"+list.size());
				return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/fetchByAppNoAndFlowStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByAppNoAndFlowStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		logger.debug("fetchByAppNoAndFlowStatus Start");
		JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 if(flowStatus.equals("TWPD")) {
			 TwowheelerPDResponse PDresponse =twowheelerDetailsService.fetchPdData(applicationNo,flowStatus);
				response.put("Data", PDresponse);
		 }
		 else if(flowStatus.equals("TWASSET")) {
			 TwowheelerAssetDetailsResponse ASSETresponse =twowheelerDetailsService.fetchASSETData(applicationNo,flowStatus);
				response.put("Data", ASSETresponse);
		 }else if(flowStatus.equals("TWUBC")) {
			 UtilityBillResponse utilityBillResponse =twowheelerDetailsService.fetchUtilityBillDetails(applicationNo,flowStatus);
				response.put("Data", utilityBillResponse);
		 }else if(flowStatus.equals("TWLNCHR")) {
			 LoanDetailsResponse loanDetails= twowheelerDetailsService.fetchLoanDetails(applicationNo,flowStatus);
			 response.put("Data", loanDetails);
		 }
		 else if(flowStatus.equals("TWDLR")) {
			 DisbustmentDetailsResponse disbustmentDetails= twowheelerDetailsService.fetchDisbustmentDetails(applicationNo,flowStatus);
			 response.put("Data", disbustmentDetails);
		 }else if(flowStatus.equals("TWDDC")) {
			 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
			 if(twowheelerDetailesTable.getDudupeResponse() !=null) {
				 JSONObject dudupeResponse=new JSONObject(twowheelerDetailesTable.getDudupeResponse());
				 return new ResponseEntity<Object>(dudupeResponse.toString(),HttpStatus.OK);
			 }else {
				 throw new NoSuchElementException("No Record Found");
			 }
		 }else if(flowStatus.equals("TWADD")) {
			 AdditionalInfoResponse additionalInfoResponse= twowheelerDetailsService.fetchAdditionalInfo(applicationNo,flowStatus);
			 response.put("Data", additionalInfoResponse);
		 }else if(flowStatus.equals("TWBSR")) {
			 BsrDetailsResponse bsrDetailsResponse= twowheelerDetailsService.fetchBsrDetails(applicationNo,flowStatus);
			 response.put("Data", bsrDetailsResponse);
		 }else if(flowStatus.equals("TWREF")) {
			 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
			 if(twowheelerDetailesTable.getReferenceInfo() !=null) {
				 org.json.JSONArray referenceInfo=new org.json.JSONArray(twowheelerDetailesTable.getReferenceInfo());
				 return new ResponseEntity<Object>(referenceInfo.toString(),HttpStatus.OK); 
			 }else {
				 throw new NoSuchElementException("No Record Found");
			 }
		 }else if(flowStatus.equals("TWSAN")) {
			 List<LeegalityInfo> list= leegalityService.getByApplicationNoAndDocument(applicationNo,"sanctionLetter");
			 response.put("Data", list);
		 }else if(flowStatus.equals("TWLNMT")) {
			 List<LeegalityInfo> list= leegalityService.getByApplicationNoAndDocument(applicationNo,"loanAgreement");
			 response.put("Data", list);
		 }
				return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/saveDisbustmentDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> saveDisbustmentDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String finaldisbustmentAmount = jsonObject.getJSONObject("Data").getString("finaldisbustmentAmount");
		String totaldeductionAmount = jsonObject.getJSONObject("Data").getString("totaldeductionAmount");

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
			twowheelerDetails.setFlowStatus("TWDLR");
//			twowheelerDetails.setStatus("COMPLETED");
//			twowheelerDetails.setStage("PSD");
		}else {
			twowheelerDetails.setFlowStatus("TWDLR");
//			twowheelerDetails.setStatus("PSD");
//			twowheelerDetails.setStage("PSD");	
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setFinaldisbustmentAmount(finaldisbustmentAmount);
		twowheelerDetails.setTotaldeductionAmount(totaldeductionAmount);
		twowheelerDetails.setDisbursementDetailsVerify("YES");
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/statusChange", method = RequestMethod.POST)
	public ResponseEntity<Object> statusChange(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String status = jsonObject.getJSONObject("Data").getString("status");
		String flowstatus = jsonObject.getJSONObject("Data").getString("flowstatus");
		String stage = jsonObject.getJSONObject("Data").getString("stage");

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		if(flowstatus.equalsIgnoreCase("TWSAN") && status.equalsIgnoreCase("PROGRESS")) {
			twowheelerDetails.setSanctionLetterVerify("YES");
		}else if(flowstatus.equalsIgnoreCase("TWLNMT") && status.equalsIgnoreCase("PSD")) {
			twowheelerDetails.setLoanAgreementVerify("YES");
			twowheelerUserLogService.saveUserLog(applicationNo,"CREDIT",X_User_ID);
		}else if(flowstatus.equalsIgnoreCase("TWVERIFY") && status.equalsIgnoreCase("PSD")) {
			twowheelerUserLogService.saveUserLog(applicationNo,"PSD",X_User_ID);
		}
		twowheelerDetails.setFlowStatus(flowstatus);
		twowheelerDetails.setStatus(status);
		twowheelerDetails.setStage(stage);
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getreviewData", method = RequestMethod.POST)
	public ResponseEntity<Object> getreviewData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		org.json.simple.JSONObject fetchByApplicationNo = twowheelerDetailsService.getreviewData(applicationNo,"mob");
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/checkMobileNo", method = RequestMethod.POST)
	public ResponseEntity<Object> checkMobileNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		if(twowheelerDetails.getMobileNo().equals(mobileNo)) {
		
			response.put("message", "Please change mobileNo");	
		}else {
			response.put("message", "done");
		}
		response.put("status", HttpStatus.OK.toString());
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/fetchRemarks", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchRemarks(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		String remark = twowheelerDetails.getRemark();
		List<RemarkResponse> list=new ArrayList<>();
		if(remark != null) {
			org.json.JSONArray remarks =new org.json.JSONArray(remark);
  			for(int n=0;n<remarks.length();n++) {
  				JSONObject jsonObject2 = remarks.getJSONObject(n);
  				String Decision = jsonObject2.getString("Decision");
  				String RejectReason = jsonObject2.getString("RejectReason");
  				String Remarks = jsonObject2.getString("Remarks");
  				String date = jsonObject2.getString("date");
  				String updatedBy = jsonObject2.getString("updatedBy");
  				RemarkResponse remarkResponse=new RemarkResponse(Decision,RejectReason,Remarks,date,updatedBy);
					list.add(remarkResponse);
  			}
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getFamilyDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getFamilyDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");

		List<TwoWheelerFamilyMemberResponse> fetchByApplicationNo = familyMemberService.getFamilyDetails(applicationNo);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
	
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	@RequestMapping(value = "/documentVerify", method = RequestMethod.POST)
	public ResponseEntity<Object> documentVerify(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
	 
		List<String> documentNotPresent=new ArrayList<>();
		List<String> list =imageService.getDocumentTypes(applicationNo);
		 TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		 List<String> documentType =null;
		 if(twowheelerDetails.getStage().equalsIgnoreCase("SALE")) {
			 documentType = new ArrayList<>(Arrays.asList("EbBill","Pan"));
			 if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
				 documentType.add("customerPhoto");
				}
		 }
		 else {
			 documentType = new ArrayList<>(Arrays.asList("Invoice","AssetInsurance","RTO","DeliveryOrder","MarginMoney","CreditLifeInsurance","EbBill","Pan"));
			 if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
				 documentType.add("customerPhoto");
				}
		 }
		
		 for(String documentInList:documentType) {
			 int count=1;
			 	for(String documentInDb:list) {
			 		if(documentInList.equalsIgnoreCase(documentInDb)) {
			 			count++;
			 			break;
			 		}
			 	}
			 	if(count==1) {
			 		documentNotPresent.add(documentInList);
			 	}
		 }
		 org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		 if(documentNotPresent.size()==0) {
			 twowheelerDetails.setDocumentUploadVerify("YES");
			 twowheelerDetailsService.saveData(twowheelerDetails);
		 }
//		 else {
				response.put("List", documentNotPresent);
//		 }
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/documentCheckList", method = RequestMethod.POST)
	public ResponseEntity<Object> documentCheckList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
	 
		Set<DocCheckListResponse> documentResponse=new HashSet<>();
		 List<String> list =imageService.getDocumentTypes(applicationNo);
		 TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		 List<String> documentType =null;
		 if(twowheelerDetails.getStage().equalsIgnoreCase("SALE")) {
			 documentType = new ArrayList<>(Arrays.asList("EbBill","Pan"));
			 if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
				 documentType.add("customerPhoto");
				}
		 }
		 else {
			 documentType = new ArrayList<>(Arrays.asList("Invoice","AssetInsurance","RTO","DeliveryOrder","MarginMoney","CreditLifeInsurance","EbBill","Pan")); 
			 if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
				 documentType.add("customerPhoto");
				}
		 }
		
		 for(String documentInList:documentType) {
			 int count=0;
			 	for(String documentInDb:list) {
			 		if(documentInList.equalsIgnoreCase(documentInDb)) {
			 			count++;
			 		}
			 	}
			 	if(count==0) {
			 		DocCheckListResponse checkListResponse=new DocCheckListResponse(documentInList,Integer.toString(count), "NO");
			 		documentResponse.add(checkListResponse);
			 	}else {
			 		DocCheckListResponse checkListResponse=new DocCheckListResponse(documentInList,Integer.toString(count), "YES");
			 		documentResponse.add(checkListResponse);
			 	}
			 	
		 }
		 List<String> filteredList=null;
		 if(twowheelerDetails.getStage().equalsIgnoreCase("SALE")) {
			  filteredList = list.stream()
		                .filter(element -> !new ArrayList<>(Arrays.asList("EbBill","Pan")).contains(element))
		                .collect(Collectors.toList()); 
		 }else {
			 filteredList = list.stream()
		                .filter(element -> !new ArrayList<>(Arrays.asList("Invoice","AssetInsurance","RTO","DeliveryOrder","MarginMoney","CreditLifeInsurance","EbBill","Pan")).contains(element))
		                .collect(Collectors.toList()); 
		 }
		 if(twowheelerDetails.getListType().equalsIgnoreCase("NEW")) {
			 if(filteredList.contains("customerPhoto")) {
				 filteredList.remove("customerPhoto");
			 } 
			}
	    filteredList.stream()
	                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
	                .forEach((element, count) ->documentResponse.add(new DocCheckListResponse(element,Long.toString(count), "YES")));
		 
		 org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("List", documentResponse);
		logger.debug(response.toString());
		logger.debug("PD end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	@RequestMapping(value = "/saveAdditionalInfo", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAdditionalInfo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String accountIfsc = jsonObject.getJSONObject("Data").getString("accountIfsc");
		String accountBankName = jsonObject.getJSONObject("Data").getString("accountBankName");
		String accountBranchId = jsonObject.getJSONObject("Data").getString("accountBranchId");
		String accountNumber = jsonObject.getJSONObject("Data").getString("accountNumber");
		String accountName = jsonObject.getJSONObject("Data").getString("accountName");
		String repaymentType = jsonObject.getJSONObject("Data").getString("repaymentType");
		String accountType = jsonObject.getJSONObject("Data").getString("accountType");
		String beneficiaryAccountType = jsonObject.getJSONObject("Data").getString("beneficiaryAccountType");

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);

		twowheelerDetails.setFlowStatus("TWADD");
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setAccountIfsc(accountIfsc);
		twowheelerDetails.setAccountBankName(accountBankName);
		twowheelerDetails.setAccountBranchId(accountBranchId);
		twowheelerDetails.setAccountNumber(accountNumber);
		twowheelerDetails.setAccountName(accountName);
		twowheelerDetails.setRepaymentType(repaymentType);
		twowheelerDetails.setAccountType(accountType);
		twowheelerDetails.setBeneficiaryAccountType(beneficiaryAccountType);
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/saveReferenceInfo", method = RequestMethod.POST)
	public ResponseEntity<Object> saveReferenceInfo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String referenceInfo = jsonObject.getJSONObject("Data").getJSONArray("ReferenceInfo").toString();
	
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);

		twowheelerDetails.setFlowStatus("TWREF");
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setReferenceInfo(referenceInfo);
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getCatCompanyList", method = RequestMethod.POST)
	public ResponseEntity<Object> getCatCompanyList(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		List<CatCompanies> list = twowheelerDetailsService.getCatCompanyList();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", list);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/saveBSRDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBSRDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);

		String loanPurpose = jsonObject.getJSONObject("Data").getString("loanPurpose");
		String bussinessSegment = jsonObject.getJSONObject("Data").getString("bussinessSegment");
		String classificationAdvance = jsonObject.getJSONObject("Data").getString("classificationAdvance");
		String governmentSponsoredScheme = jsonObject.getJSONObject("Data").getString("governmentSponsoredScheme");
		String channelCode = jsonObject.getJSONObject("Data").getString("channelCode");
		String populationCode = jsonObject.getJSONObject("Data").getString("populationCode");
		String sanctionDepartment = jsonObject.getJSONObject("Data").getString("sanctionDepartment");
		
		twowheelerDetails.setFlowStatus("TWBSR");
		if(twowheelerDetails.getStatus() == null || twowheelerDetails.getStage() == null) {
			twowheelerDetails.setStatus("PROGRESS");
			twowheelerDetails.setStage("SALE");
		}
		twowheelerDetails.setUpdatedTimestamp(LocalDateTime.now());
		twowheelerDetails.setLoanPurpose(loanPurpose);
		twowheelerDetails.setBussinessSegment(bussinessSegment);
		twowheelerDetails.setClassificationAdvance(classificationAdvance);
		twowheelerDetails.setGovernmentSponsoredScheme(governmentSponsoredScheme);
		twowheelerDetails.setChannelCode(channelCode);
		twowheelerDetails.setPopulationCode(populationCode);
		twowheelerDetails.setSanctionDepartment(sanctionDepartment);
		
		String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveSingleData);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getCheckList", method = RequestMethod.POST)
	public ResponseEntity<Object> getCheckList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws ParseException {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		Map<String, String> checkList=new HashMap<>();
		if(twowheelerDetails.getStage().equalsIgnoreCase("PSD")) {
			checkList.put("loanAgreementVerify", twowheelerDetails.getLoanAgreementVerify());
			checkList.put("disbursementDetailsVerify", twowheelerDetails.getDisbursementDetailsVerify());
		}
			checkList.put("personalDetailsVerify", twowheelerDetails.getPersonalDetailsVerify());
			checkList.put("assetDetailsVerify", twowheelerDetails.getAssetDetailsVerify());
			checkList.put("loanChargeVerify", twowheelerDetails.getLoanChargeVerify());
			checkList.put("addNomineeVerify", twowheelerDetails.getAddNomineeVerify());
			checkList.put("documentUploadVerify", twowheelerDetails.getDocumentUploadVerify());
			checkList.put("sanctionLetterVerify", twowheelerDetails.getSanctionLetterVerify());
			
			checkList.replaceAll((keys, value) -> value == null ? "NO" : value);
			
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("Data", checkList);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
