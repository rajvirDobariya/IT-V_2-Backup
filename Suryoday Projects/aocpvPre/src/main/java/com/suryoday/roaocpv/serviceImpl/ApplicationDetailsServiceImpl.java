package com.suryoday.roaocpv.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.Charges;
import com.suryoday.aocpv.pojo.EmiResponse;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.ChargesService;
import com.suryoday.roaocpv.pojo.Address;
import com.suryoday.roaocpv.pojo.ApplicationDetailList;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponse;
import com.suryoday.roaocpv.pojo.PersonalDetailResponse;
import com.suryoday.roaocpv.repository.ApplicationDetailsRepository;
import com.suryoday.roaocpv.service.ApplicationDetailsService;

@Service
public class ApplicationDetailsServiceImpl implements ApplicationDetailsService {

	@Autowired 
	ApplicationDetailsRepository applicationDetailsRepository;
	
	 @Autowired
	 AocpvImageService aocpvImageService;
	 
		@Autowired
		ChargesService chargesservice;
		
	private static Logger logger = LoggerFactory.getLogger(ApplicationDetailsServiceImpl.class);

	@Override
	public List<ApplicationDetailList> fetchByStatusAndBranchId(String status, String branchId) {
		List<ApplicationDetailList> list=applicationDetailsRepository.fetchByStatusAndBranchId(status,branchId);
		if(list.size()==0) {
			throw new NoSuchElementException("No Record Found in ApplicationDetails");
		}
		return list;
	}

	@Override
	public String passorfail(String applicationId, String flowStatus, String status) {
		Optional<String> optional=applicationDetailsRepository.fetchByapplicationId(applicationId);
		if(optional.isPresent()) {
			applicationDetailsRepository.updateflowStatus(applicationId,flowStatus,status);
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}

	@Override
	public ApplicationDetails fetchByApplicationId(String applicationId) {
	Optional<ApplicationDetails> optional =applicationDetailsRepository.getByApplicationId(applicationId);
	if(optional.isPresent()) {
//		ApplicationDetails a = optional.get();
//		String personalDetailsReq = a.getPersonalDetailsReq();
//		 JSONObject jsonObject=new JSONObject(personalDetailsReq);
//		 String FIRST_NAME = jsonObject.getString("FIRST_NAME");
//		 String MIDDLE_NAME = jsonObject.getString("MIDDLE_NAME");
//		 String LAST_NAME = jsonObject.getString("LAST_NAME");
//		 String MOBILE = jsonObject.getString("MOBILE");
//		 String DOB = jsonObject.getString("DOB");
//		 String AGE = jsonObject.getString("AGE");
//		 String GENDER = jsonObject.getString("GENDER");
//		 String FATHER_NAME = jsonObject.getString("FATHER_NAME");
//		 String MOTHER_NAME = jsonObject.getString("MOTHER_NAME");
//		 String SPOUSE_NAME = jsonObject.getString("SPOUSE_NAME");
//		 String NO_OF_DEPENDENTS = jsonObject.getString("NO_OF_DEPENDENTS");
//		 String QUALIFICATION = jsonObject.getString("QUALIFICATION");
//		 String CASTE = jsonObject.getString("CASTE");
//		 String RELIGION = jsonObject.getString("RELIGION");
//	PersonalDetailResponse response=new PersonalDetailResponse(FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE,DOB,AGE,GENDER,FATHER_NAME,MOTHER_NAME,SPOUSE_NAME,NO_OF_DEPENDENTS,QUALIFICATION,CASTE,RELIGION);
//	ApplicationDetailsResponse response2=new ApplicationDetailsResponse();
//	response2.setPersonalDetailResponse(response);
//	response2.setApplicationId(a.getApplicationId());
//	response2.setAmount(a.getAmount());
//	response2.setBranchCode(a.getBranchCode());
//	response2.setName(a.getName());
	
		return optional.get();
	}
	throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}

	@Override
	public List<ApplicationDetails> fetchByDate(String status, String branchId, LocalDate startdate,
			LocalDate enddate) {
		List<ApplicationDetails> list=applicationDetailsRepository.fetchByDate(status,branchId,startdate,enddate);
		if(list.size()==0) {
			throw new NoSuchElementException("No Record Found in ApplicationDetails");
		}
		return list;
	}

	@Override
	public String addpersonalDetails(String applicationId, String personalDetails) {
		Optional<ApplicationDetails> optional =applicationDetailsRepository.getByApplicationId(applicationId);
		if(optional.isPresent()) {
			 ApplicationDetails applicationDetails = optional.get();
			 applicationDetails.setPersonalDetailsReq(personalDetails);
			 applicationDetails.setFlowStatus("ROPD");
			 applicationDetails.setUpdateDatets(LocalDateTime.now());
			 applicationDetailsRepository.save(applicationDetails);
			 return "data saved";
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
		}
	
	@Override
	public String createLead(String jsonRequest, String userId) {
//		String	applicationNo =(LocalDate.now().toString().replace("-", "")+"0001").substring(2, 12);
//
//		Optional<String> fetchLastApplicationNo = applicationDetailsRepository.fetchLastApplicationNo();
//
//		if (fetchLastApplicationNo.isPresent()) {
//			String application_No = fetchLastApplicationNo.get();
//			String dateInDB = application_No.substring(0,6);
//			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
//			if(currentDate.equals(dateInDB)) {
//				Long applicationno = Long.parseLong(application_No);
//				applicationno++;
//				applicationNo = applicationno.toString();
//			}
//					
////			Long application = Long.parseLong(application_No);
////			application++;
////			applicationNo = application.toString();
//		}
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String requiredAmount = jsonObject.getJSONObject("Data").getString("requiredAmount");
		String udamReg = jsonObject.getJSONObject("Data").getString("udamReg");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		String form60 = jsonObject.getJSONObject("Data").getString("form60");
		logger.debug("ApplicationNo" + applicationNo);

		Optional<ApplicationDetails> optional = applicationDetailsRepository.findByApplicationId(applicationNo);
		if(optional.isPresent()) {
			ApplicationDetails applicationDetails = optional.get();
			LocalDate date = LocalDate.now();
			applicationDetails.setUpdatedDate(date);
			applicationDetails.setUpdateDatets(LocalDateTime.now());
			applicationDetails.setCreatedBy(userId);
			applicationDetails.setListType("NEW");
			applicationDetails.setFlowStatus("FINAL");
			int requiredAmoutInInt = Integer.parseInt(requiredAmount);
			if(requiredAmoutInInt<60000) {
				requiredAmoutInInt=60000;
			}
			else if(requiredAmoutInInt>60000 && requiredAmoutInInt<75000) {
				requiredAmoutInInt=75000;
			}
			else if(requiredAmoutInInt>75000 && requiredAmoutInInt<85000) {
				requiredAmoutInInt=85000;
			}
			else if(requiredAmoutInInt>85000 && requiredAmoutInInt<100000) {
				requiredAmoutInInt=100000;
			}
			else if(requiredAmoutInInt>100000 && requiredAmoutInInt<125000) {
				requiredAmoutInInt=125000;
			}
			applicationDetails.setRequiredAmount(Integer.toString(requiredAmoutInInt));
			applicationDetails.setUdamReg(udamReg);
			applicationDetails.setEmail(emailId);
			applicationDetails.setForm60(form60);
			applicationDetailsRepository.save(applicationDetails);	
			return applicationDetails.getAppNoWithProductCode();
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
	
	}

	@Override
	public String saveCIf(String applicationId, JSONObject jsonObject2) {
			LocalDateTime now=LocalDateTime.now();
	Optional<ApplicationDetails> optional =applicationDetailsRepository.getByApplicationId(applicationId);
	if(optional.isPresent()) {
		ApplicationDetails applicationDetails = optional.get();
		if(jsonObject2.has("customerId")) {
			String customerId = jsonObject2.getString("customerId");
			applicationDetails.setCustomerId(customerId);
			applicationDetails.setCustomerCreationTs(now);
		}
		if(jsonObject2.has("accountNumber")) {
			String accountNumber = jsonObject2.getString("accountNumber");
			applicationDetails.setAccountNumber(accountNumber);
			applicationDetails.setAccountCreationTs(now);
		}
		if(jsonObject2.has("workItemId")) {
			String workItemId = jsonObject2.getString("workItemId");
			applicationDetails.setWorkItemId(workItemId);
			applicationDetails.setWorkItemTs(now);
		}
		if(jsonObject2.has("loanNumber")) {
			String loanNumber = jsonObject2.getString("loanNumber");
			applicationDetails.setLoanNumber(loanNumber);
			applicationDetails.setLoanNumberTs(now);
		}
		if(jsonObject2.has("disburstmenetNumber")) {
			String disburstmenetNumber = jsonObject2.getString("disburstmenetNumber");
			applicationDetails.setDisburstmenetNumber(disburstmenetNumber);
			applicationDetails.setDisburstmenetNumberTs(now);
		}
		applicationDetails.setUpdateDatets(now);
		applicationDetailsRepository.save(applicationDetails);
		return "update successfully";
	}
	throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}
	
	@Override
	public void blockValidation(String proof, String proofId, String applicationNo) {
		Optional<ApplicationDetails> findByApplicationId = applicationDetailsRepository
				.findByApplicationId(applicationNo);
		if (findByApplicationId.isPresent()) {
			ApplicationDetails applicationDetails = findByApplicationId.get();

			if (proof.equalsIgnoreCase("mobileNumber")) {
				applicationDetails.setMobileNo(proofId);
				applicationDetails.setIsMobileNoVerify("Yes");
				applicationDetails.setFlowStatus("Progress");

			}
			LocalDate date = LocalDate.now();
			applicationDetails.setUpdatedDate(date);
			applicationDetailsRepository.save(applicationDetails);
		} else {
			throw new NoSuchElementException("list is empty");
		}
	}

	@Override
	public void saveData(String proof, String proofId, String applicationNo, String response) {
		Optional<ApplicationDetails> findByApplicationId = applicationDetailsRepository
				.findByApplicationId(applicationNo);
		if (findByApplicationId.isPresent()) {
			ApplicationDetails applicationDetails = findByApplicationId.get();
			

			if (proof.equalsIgnoreCase("panCard")) {
				applicationDetails.setPanCard(proofId);
				applicationDetails.setPanCardResponse(response);

			} else if (proof.equalsIgnoreCase("voterId")) {
				applicationDetails.setVoterId(proofId);
				applicationDetails.setVoterIdResponse(response);
			} else if (proof.equalsIgnoreCase("drivingLicense")) {
				applicationDetails.setDrivingLicense(proofId);
				applicationDetails.setDrivingLicenseIsVerify("YES");
				applicationDetails.setDrivingLicenseResponse(response);
			} else if (proof.equalsIgnoreCase("Passport")) {
				applicationDetails.setPassport(proofId);
				applicationDetails.setPassportIsVerify("YES");
				applicationDetails.setPassportResponse(response);
			} else if (proof.equalsIgnoreCase("ekyc")) {
				applicationDetails.setEkycResponse(response);
				applicationDetails.setEkycVerify("YES");
				applicationDetails.setAadharNumber(proofId);
				 org.json.JSONObject ekyc=new org.json.JSONObject(response);
		 			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
		 			applicationDetails.setName(PoiResponse.getString("name"));
			} else if (proof.equalsIgnoreCase("aadharCard")) {
				JSONObject jsonObject = new JSONObject(response);
				Long referenceNo1 = jsonObject.getJSONArray("Data").getJSONObject(0).getLong("ReferenceNumber");
				String referenceNo = referenceNo1.toString();
				applicationDetails.setAadharReferenceNo(referenceNo);
				applicationDetails.setAadharNumber(proofId);
				applicationDetails.setIsAadharVerify("Yes");
			}
			LocalDate date = LocalDate.now();
			applicationDetails.setUpdatedDate(date);
			applicationDetailsRepository.save(applicationDetails);
		} else {
			throw new NoSuchElementException("ApplicationNo not present");
		}

	}

	@Override
	public PersonalDetailResponse getPersonalDetailsByAppln(String appln) {
		Optional<ApplicationDetails> optional=applicationDetailsRepository.getByApplicationId(appln);
		if(optional.isPresent()) {
			ApplicationDetails a = optional.get();
			String personalDetailsReq = a.getPersonalDetailsReq();
			if(personalDetailsReq != null) {
				 JSONObject jsonObject=new JSONObject(personalDetailsReq);
				 String title = jsonObject.getString("Title");
				 String FIRST_NAME = jsonObject.getString("FirstName");
				 String MIDDLE_NAME = jsonObject.getString("MiddleName");
				 String LAST_NAME = jsonObject.getString("LastName");
				 String MOBILE = jsonObject.getString("MobileNO");
				 String DOB = jsonObject.getString("DateOfBirth");
				 String marritialStatus = jsonObject.getString("marritialStatus");
				 String AGE = jsonObject.getString("Age");
				 String GENDER = jsonObject.getString("Gender");
				 String FATHER_NAME = jsonObject.getString("FatherName");
				 String MOTHER_NAME = jsonObject.getString("MotherName");
				 String SPOUSE_NAME = jsonObject.getString("SpouseName");
				 String NO_OF_DEPENDENTS = jsonObject.getString("DependantMember");
				 String QUALIFICATION = jsonObject.getString("EducationQualification");
				 String CASTE = jsonObject.getString("Caste");
				 String RELIGION = jsonObject.getString("Religion");
			PersonalDetailResponse response=new PersonalDetailResponse(title,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE,DOB,AGE,GENDER,FATHER_NAME,MOTHER_NAME,SPOUSE_NAME,NO_OF_DEPENDENTS,QUALIFICATION,CASTE,RELIGION,marritialStatus);
			return response;
			}
			 
		return new PersonalDetailResponse();
	}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
}

	@Override
	public String saveAddress(String appln, String address) {
		
		Optional<ApplicationDetails> optional =applicationDetailsRepository.getByApplicationId(appln);
		if(optional.isPresent()) {
			 ApplicationDetails applicationDetails = optional.get();
			 applicationDetails.setAddressDetailsReq(address);
			 applicationDetails.setFlowStatus("AD");
			 applicationDetails.setUpdateDatets(LocalDateTime.now());
			 applicationDetailsRepository.save(applicationDetails);
			 return "data saved";
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
		}

	@Override
	public List<Address> getAddress(String appln) {
		Optional<ApplicationDetails> optional=applicationDetailsRepository.getByApplicationId(appln);
		if(optional.isPresent()) {
			ApplicationDetails a = optional.get();
			List<Address> list=new ArrayList<>();
			
			if(a.getAddressDetailsReq() !=null) {
				org.json.JSONArray jsonArray =new org.json.JSONArray(a.getAddressDetailsReq());
			
				for(int n=0;n<jsonArray.length();n++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(n);
							String AddressType = jsonObject2.getString("ADDRESSTYPE");
							String address1 = jsonObject2.getString("ADDRESS_LINE1");
							String address2 = jsonObject2.getString("ADDRESS_LINE2");
							String address3 = jsonObject2.getString("ADDRESS_LINE3");
							String pincode = jsonObject2.getString("PINCODE");
							String city = jsonObject2.getString("CITY");
							String district = jsonObject2.getString("DISTRICT");
							String state = jsonObject2.getString("STATE");
							String landmark = jsonObject2.getString("LANDMARK");
						Address address=new Address(AddressType,address1,address2,pincode,city,district,state,landmark,address3);
						list.add(address);
				}
			}
			return list;
			
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}

	@Override
	public void validateMobile(String mobile) {
		try {
			Optional<ApplicationDetails> optional=applicationDetailsRepository.fetchByMobileNo(mobile);
			if(optional.isPresent()) {
				throw new DuplicateEntryException("mobile No already present");
			}
			else {
				
			}
		}catch (Exception e) {
			throw new DuplicateEntryException("mobile No already present");
		}
		
		
	}

	@Override
	public ApplicationDetailsResponse fetchAllByApplicationId(String applicationNo) {
		Optional<ApplicationDetails> optional =applicationDetailsRepository.getByApplicationId(applicationNo);
		if(optional.isPresent()) {
			ApplicationDetails applicationDetails = optional.get();
			
			
		
		     
			ApplicationDetailsResponse response=new ApplicationDetailsResponse();
			if(response.getProductCode() != null) {
				 List<Charges> list1=chargesservice.fetchByProductCode(response.getProductCode());
			     Charges charges = list1.get(0);
			     response.setCharges(charges);
			}
			
			List<Address> list=new ArrayList<>();
			if(applicationDetails.getAddressDetailsReq() !=null) {
				org.json.JSONArray jsonArray =new org.json.JSONArray(applicationDetails.getAddressDetailsReq());
			
				for(int n=0;n<jsonArray.length();n++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(n);
						String AddressType = jsonObject2.getString("ADDRESSTYPE");
						String address1 = jsonObject2.getString("ADDRESS_LINE1");
						String address2 = jsonObject2.getString("ADDRESS_LINE2");
						String address3 = jsonObject2.getString("ADDRESS_LINE3");
						String pincode = jsonObject2.getString("PINCODE");
						String city = jsonObject2.getString("CITY");
						String district = jsonObject2.getString("DISTRICT");
						String state = jsonObject2.getString("STATE");
						String landmark = jsonObject2.getString("LANDMARK");
						Address address=new Address(AddressType,address1,address2,pincode,city,district,state,landmark,address3);
						list.add(address);
				}
			}
			PersonalDetailResponse response1=null;
			String personalDetailsReq = applicationDetails.getPersonalDetailsReq();
			if(personalDetailsReq != null) {
				JSONObject jsonObject=new JSONObject(personalDetailsReq);
				 String title = jsonObject.getString("Title");
				 String FIRST_NAME = jsonObject.getString("FirstName");
				 
				 String MIDDLE_NAME = jsonObject.getString("MiddleName");
				 String LAST_NAME = jsonObject.getString("LastName");
				 String MOBILE = jsonObject.getString("MobileNO");
				 String DOB = jsonObject.getString("DateOfBirth");
				 String AGE = jsonObject.getString("Age");
				 
				 String marritialStatus = jsonObject.getString("marritialStatus");
				 String GENDER = jsonObject.getString("Gender");
				 
				 String FATHER_NAME = jsonObject.getString("FatherName");
				 String MOTHER_NAME = jsonObject.getString("MotherName");
				 String SPOUSE_NAME = jsonObject.getString("SpouseName");
				 String NO_OF_DEPENDENTS = jsonObject.getString("DependantMember");
				 String QUALIFICATION = jsonObject.getString("EducationQualification");
				 String CASTE = jsonObject.getString("Caste");
				 String RELIGION = jsonObject.getString("Religion");
			 response1=new PersonalDetailResponse(title,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE,DOB,AGE,GENDER,FATHER_NAME,MOTHER_NAME,SPOUSE_NAME,NO_OF_DEPENDENTS,QUALIFICATION,CASTE,RELIGION,marritialStatus);
			}
			response.setPersonalDetailResponse(response1);
			response.setAddressDetailsReq(list);
			response.setAppId(applicationDetails.getAppId());
			response.setApplicationId(applicationDetails.getApplicationId());
			response.setRequest(applicationDetails.getRequest());
			response.setName(applicationDetails.getName());
			response.setFirstName(applicationDetails.getFirstName());
			response.setLastName(applicationDetails.getLastName());
			response.setAmount(applicationDetails.getAmount());
			response.setMobileNo(applicationDetails.getMobileNo());
			response.setChannel(applicationDetails.getChannel());
			response.setStatus(applicationDetails.getStatus());
			response.setProductType(applicationDetails.getProductType());
			response.setAadharNumber(applicationDetails.getAadharNumber());
			response.setCreatets(applicationDetails.getCreatets());
			response.setUpdatedDate(applicationDetails.getUpdatedDate());
			response.setBranchName(applicationDetails.getBranchName());
			response.setBranchCode(applicationDetails.getBranchCode());
			response.setAgentId(applicationDetails.getAgentId());
			response.setFlowStatus(applicationDetails.getFlowStatus());
			response.setProductCode(applicationDetails.getProductCode());
			response.setPrimaryDetailsReq(applicationDetails.getPrimaryDetailsReq());
			response.setPrimaryDetailsTs(applicationDetails.getPrimaryDetailsTs());
			response.setPersonalDetailsTs(applicationDetails.getPersonalDetailsTs());
			response.setAddressDetailsTs(applicationDetails.getAddressDetailsTs());
			response.setAccountDetailsReq(applicationDetails.getAccountDetailsReq());
			response.setAccountDetailsTs(applicationDetails.getAccountDetailsTs());
			response.setProductDetailsReq(applicationDetails.getProductDetailsReq());
			response.setProductDetailsTs(applicationDetails.getProductDetailsTs());
			response.setDocumentDetailsReq(applicationDetails.getDocumentDetailsReq());
			response.setDocumentDetailsTs(applicationDetails.getDocumentDetailsTs());
			response.setBlockValidationReq(applicationDetails.getBlockValidationReq());
			response.setBlockValidationTs(applicationDetails.getBlockValidationTs());
			response.setDecelerationDetailsReq(applicationDetails.getDecelerationDetailsReq());
			response.setDecelerationDetailsTs(applicationDetails.getDecelerationDetailsTs());
			response.setCustomerId(applicationDetails.getCustomerId());
			response.setCustomerCreationTs(applicationDetails.getCustomerCreationTs());
			response.setInitialFundingTs(applicationDetails.getInitialFundingTs());
			response.setAccountNumber(applicationDetails.getAccountNumber());
			response.setAccountCreationTs(applicationDetails.getAccountCreationTs());
			response.setWorkItemId(applicationDetails.getWorkItemId());
			response.setWorkItemTs(applicationDetails.getWorkItemTs());
			response.setLoanNumber(applicationDetails.getLoanNumber());
			response.setLoanNumberTs(applicationDetails.getLoanNumberTs());
			response.setDisburstmenetNumber(applicationDetails.getDisburstmenetNumber());
			response.setDisburstmenetNumberTs(applicationDetails.getLoanNumberTs());
			response.setDocumentUploadTs(applicationDetails.getDocumentUploadTs());
			response.setProduct(applicationDetails.getProduct());
			response.setLoanPurchase(applicationDetails.getLoanPurchase());
			response.setRequiredAmount(applicationDetails.getRequiredAmount());
			response.setTenure(applicationDetails.getTenure());
			response.setSourceType(applicationDetails.getSourceType());
			response.setSourceName(applicationDetails.getSourceName());
			response.setIsMobileNoVerify(applicationDetails.getIsMobileNoVerify());
			response.setVoterId(applicationDetails.getVoterId());
			response.setVoterIdResponse(applicationDetails.getVoterIdResponse());
			response.setIsAadharVerify(applicationDetails.getIsAadharVerify());
			response.setPanCard(applicationDetails.getPanCard());
			response.setPanCardResponse(applicationDetails.getPanCardResponse());
			
			response.setAadharReferenceNo(applicationDetails.getAadharReferenceNo());
			response.setAppNoWithProductCode(applicationDetails.getAppNoWithProductCode());
			response.setEkycResponse(applicationDetails.getEkycResponse());
			response.setEkycVerify(applicationDetails.getEkycVerify());
			response.setForm60(applicationDetails.getForm60());
			response.setPassport(applicationDetails.getPassport());
			response.setPassportIsVerify(applicationDetails.getPassportIsVerify());
			response.setDrivingLicense(applicationDetails.getDrivingLicense());
			response.setDrivingLicenseIsVerify(applicationDetails.getDrivingLicenseIsVerify());
			response.setUpdateDatets(applicationDetails.getUpdateDatets());
			response.setFireInsurance(applicationDetails.getFireInsurance());
			response.setTermInsurance(applicationDetails.getTermInsurance());
			response.setNetDisbustment(applicationDetails.getNetDisbustment());
			response.setIsBreRuring(applicationDetails.getIsBreRuring());
			response.setListType(applicationDetails.getListType());
			response.setCreatedBy(applicationDetails.getCreatedBy());
			response.setMaxEmiEligibility(applicationDetails.getMaxEmiEligibility());
			if(applicationDetails.getMaxEmiEligibility() != null) {
				double emiAount = Double.parseDouble(applicationDetails.getMaxEmiEligibility());
						List<Charges> list1=chargesservice.fetchAll();
						List<Charges> responseList = list1.stream().filter(element -> Double.parseDouble(element.getRepaymentStrucyture1())<emiAount).collect(Collectors.toList());
						response.setBestOffers(responseList);
			}
			
			long applicationNoInLong = Long.parseLong(applicationNo);
			String responseEmi = applicationDetails.getResponseEmi();
			if(responseEmi!= null) {
				String OutstandingBalance=null;
				JSONObject jsonObject2=new JSONObject(responseEmi);
				String TransactionCode = jsonObject2.getString("TRANSACTIONCODE");
				String AmountRequested = jsonObject2.getString("AMOUNTREQUESTED");
				String AnnualPercentageRate = jsonObject2.getString("ANNUALPERCENTAGERATE");
				String BalanceAmount = jsonObject2.getString("BALANCEAMOUNT");
				String CommitmentFee = jsonObject2.getString("COMMITMENTFEE");
				String InterestAmortization = jsonObject2.getString("INTERESTAMORTIZATION");
				String InterestAmount = jsonObject2.getString("INTERESTAMOUNT");
				String InterestRate = jsonObject2.getString("INTERESTRATE");
				String MaturityBalance = jsonObject2.getString("MATURITYBALANCE");
				String MtgInsurance = jsonObject2.getString("MTGINSURANCE");
				String NoOfAddDays = jsonObject2.getString("NOOFADDDAYS");
				String OddDaysIntAmount = jsonObject2.getString("ODDDAYSINTAMOUNT");
				String OriginationFees = jsonObject2.getString("ORIGINATIONFEES");
				String PaymentAmount = jsonObject2.getString("PAYMENTAMOUNT");
				String Term = jsonObject2.getString("TERM");
				String TotalAmountFinanced = jsonObject2.getString("TOTALAMOUNTFINANCED");
				if(jsonObject2.has("OUTSTANDINGBALANCE")) {
				 OutstandingBalance = jsonObject2.getString("OUTSTANDINGBALANCE");
					
				}
	EmiResponse emiResponse =new EmiResponse(TransactionCode,AmountRequested,AnnualPercentageRate,BalanceAmount,CommitmentFee,InterestAmortization,InterestAmount,InterestRate,MaturityBalance,MtgInsurance,NoOfAddDays,OddDaysIntAmount,OriginationFees,PaymentAmount,Term,TotalAmountFinanced,OutstandingBalance);
				response.setEmiResponse(emiResponse);
			}
			
			List<AocpvImages> listAocpvImage =  aocpvImageService.getByApplicationNoAnddocument(applicationNoInLong,"customerPhoto");
			List<Image> listOfImages=new ArrayList<>();
			if(listAocpvImage.isEmpty()) {
			//	throw new EmptyInputException("Images Lists are Empty");
			}
			else {
				for(AocpvImages aocpvImages:listAocpvImage) {
						String geoLocation = aocpvImages.getGeoLocation();
						JSONObject jsonObjectImage=new JSONObject(geoLocation);
				 		String pimage = jsonObjectImage.getString("image");
				 		String pLat = jsonObjectImage.getString("Lat");
				 		String pLong = jsonObjectImage.getString("Long");
				 		String pAddress = jsonObjectImage.getString("Address");
				 		String ptimestamp = jsonObjectImage.getString("timestamp");
			
				 		GeoLcation geolocation =new GeoLcation(pimage,pLat,pLong,pAddress,ptimestamp);
 		
				 		String documenttype = aocpvImages.getDocumenttype();
				 		String imageName = aocpvImages.getImageName();
				 		String type = aocpvImages.getType();
				 		long size = aocpvImages.getSize();
				 		String member=aocpvImages.getMember();
				 		byte[] images2 = aocpvImages.getImages();
				 		String encoded = Base64.getEncoder().encodeToString(images2);
				 		 
				 		Image images = new Image(documenttype,imageName,type,size,encoded,member,geolocation);
				 			
					 		listOfImages.add(images);						
				}
		}
			response.setImages(listOfImages);
			return response;
		//throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
	
}

	@Override
	public void validateAadharCard(String referenceNo) {
		try {
		Optional<ApplicationDetails> optional =applicationDetailsRepository.getByAadharNumber(referenceNo);
		if(optional.isPresent()) {
			throw new DuplicateEntryException("aadhar no is already exist");
		}
	}catch (Exception e) {
		throw new DuplicateEntryException("aadhar no is already exist");
	}
	}

	@Override
	public void validatePancard(String panCardNo) {
		try {
			Optional<ApplicationDetails> optional =applicationDetailsRepository.getByPanCard(panCardNo);
			if(optional.isPresent()) {
				throw new DuplicateEntryException("panCardNo no is already exist");
			}
		}catch (Exception e) {
			throw new DuplicateEntryException("panCardNo no is already exist");
		}
		
	}

	@Override
	public String updateFlowStatus(String flowStatus,String applicationId,String status) {
		
		Optional<String> optional = applicationDetailsRepository.fetchByapplicationId(applicationId);
		if(optional.isPresent()) {
			applicationDetailsRepository.updateflowStatus(applicationId,flowStatus,status);
			return "done";
		}
		throw new NoSuchElementException("No Record Found in ApplicationDetails");
	}

	@Override
	public void save(ApplicationDetails fetchByApplicationId) {
		applicationDetailsRepository.save(fetchByApplicationId);
	}

	@Override
	public String createApplicationNo(String mobileNo, String product,String branchId) {
		String	applicationNo =(LocalDate.now().toString().replace("-", "")+"0001").substring(2, 12);

		Optional<String> fetchLastApplicationNo = applicationDetailsRepository.fetchLastApplicationNo();

		if (fetchLastApplicationNo.isPresent()) {
			String application_No = fetchLastApplicationNo.get();
			String dateInDB = application_No.substring(0,6);
			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
			if(currentDate.equals(dateInDB)) {
				Long applicationno = Long.parseLong(application_No);
				applicationno++;
				applicationNo = applicationno.toString();
			}
					
//			Long application = Long.parseLong(application_No);
//			application++;
//			applicationNo = application.toString();
		}	
		ApplicationDetails applicationDetails = new ApplicationDetails();
		applicationDetails.setApplicationId(applicationNo);
		LocalDateTime dateTime = LocalDateTime.now();
		applicationDetails.setCreatets(dateTime);
		LocalDate date = LocalDate.now();
		applicationDetails.setProduct(product);
		applicationDetails.setUpdatedDate(date);
		applicationDetails.setUpdateDatets(dateTime);
		applicationDetails.setMobileNo(mobileNo);
		applicationDetails.setAppNoWithProductCode(product+"-"+applicationNo);
		applicationDetails.setIsMobileNoVerify("YES");
		applicationDetails.setEkycVerify("NO");
		applicationDetails.setStatus("PROGRESS");
		applicationDetails.setFlowStatus("NEWLEAD");
		applicationDetails.setBranchCode(branchId);
		applicationDetailsRepository.save(applicationDetails);
		return applicationNo;
		}

	@Override
	public int fetchByLeadId(String lead_ID) {
		Optional<ApplicationDetails> optional=applicationDetailsRepository.getByLeadId( lead_ID);
		if(optional.isPresent()) {
			ApplicationDetails applicationDetails = optional.get();
			applicationDetails.setIsBreRuring("YES");
			applicationDetailsRepository.save(applicationDetails);
			return 1;
		}
		return 0;
	}

	@Override
	public List<ApplicationDetails> getByBreStatus(String status,LocalDate startdate, LocalDate enddate) {
		List<ApplicationDetails> list=applicationDetailsRepository.getByBreStatus(status,startdate,enddate);
		
		return list;
	}

	@Override
	public List<ApplicationDetails> getByBreStatus(String status, String branchId, LocalDate startdate,
			LocalDate enddate) {
List<ApplicationDetails> list=applicationDetailsRepository.getByBreStatus(status,branchId,startdate,enddate);
		
		return list;
	}
	}
