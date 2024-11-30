package com.suryoday.aocpv.serviceImp;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.Address;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.pojo.CustomerWebResponse;
import com.suryoday.aocpv.pojo.EmiProductCodeResponse;
import com.suryoday.aocpv.pojo.EmiResponse;
import com.suryoday.aocpv.pojo.EmiResponseMaster;
import com.suryoday.aocpv.pojo.FetchAllWebResponse;
import com.suryoday.aocpv.pojo.FinalSaction;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.IncomeDetail;
import com.suryoday.aocpv.pojo.IncomeDetailWeb;
import com.suryoday.aocpv.pojo.NomineeDetails;
import com.suryoday.aocpv.pojo.OwnerAddress;
import com.suryoday.aocpv.pojo.PanCardResponse;
import com.suryoday.aocpv.pojo.PanCardResponseNew;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.pojo.RemarkResponse;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerData;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerDataWeb;
import com.suryoday.aocpv.pojo.UpaResponse;
import com.suryoday.aocpv.pojo.VoterIdResponse;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.EmiResponseMasterRepository;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.LoanInputService;
@Service
public class AocpCustomerDataServiceImpl implements AocpCustomerDataService{

	Logger logger = LoggerFactory.getLogger(AocpCustomerDataServiceImpl.class);
	@Autowired 
	AocpCustomerDataRepo aocpCustomerDataRepo;
	
	@Autowired
	AocpvImageService imageService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@Autowired
	LoanInputService loanInputService;
	
	@Autowired
	EmiResponseMasterRepository emiResponseMasterRepository;
	@Override
	public AocpCustomer getByApplicationNo(long applicationNoInLong) {
		
		Optional<AocpCustomer> optional=aocpCustomerDataRepo.getByApplicationNo(applicationNoInLong);
			if(optional.isPresent()) {
				return optional.get();
			}
			throw new NoSuchElementException("No Record Found");
	}

	@Override
	public String saveSingleData(AocpCustomer aocpCustomer) {
		if(aocpCustomer == null) {
			throw new NullPointerException("input is null");
		}
			aocpCustomerDataRepo.save(aocpCustomer)	;
		return "data updated";
	}

	@Override
	public List<ResponseAocpCustomerData> getAllData() {
		List<AocpCustomer> listOfCustomer = aocpCustomerDataRepo.findAll();
		if(listOfCustomer.isEmpty()) {
			throw new NoSuchElementException("customer not found");
		}
		List<ResponseAocpCustomerData> list1 =new ArrayList<>();
		for(AocpCustomer aocpCustomer: listOfCustomer) {
		ResponseAocpCustomerData responseAocpCustomerData = getResponse(aocpCustomer);
				list1.add(responseAocpCustomerData);
		}
		return list1;
	}

	@Override
	public ResponseAocpCustomerData getByCustomerId(long customerIdInLong) {
		Optional<AocpCustomer> optional=null;
		try {
		 optional=aocpCustomerDataRepo.getByCustomerId(customerIdInLong);
		 if(optional.isPresent()) {
				AocpCustomer aocpCustomer = optional.get();
					
			ResponseAocpCustomerData responseAocpCustomerData = getResponse(aocpCustomer);
				return responseAocpCustomerData;
			}
		 
		}
		catch(IncorrectResultSizeDataAccessException e) {
			List<AocpCustomer> list=aocpCustomerDataRepo.getByCustomerid(customerIdInLong);
			int n=-1;
			for(AocpCustomer details :list) {
				n++;
			}
			AocpCustomer aocpCustomer = list.get(n);
			ResponseAocpCustomerData responseAocpCustomerData = getResponse(aocpCustomer);
			return responseAocpCustomerData;
		}
		
		throw new NoSuchElementException("enter valid customer id");
	}

	@Override
	public List<ResponseAocpCustomerData> getByStatus(String status) {
		
				Optional<List<AocpCustomer>> optional=aocpCustomerDataRepo.findByStatus(status);
				if(optional.isPresent()) {
					List<AocpCustomer> listOfCustomer = optional.get();
								
					List<ResponseAocpCustomerData> list1 =new ArrayList<>();
					for(AocpCustomer aocpCustomer: listOfCustomer) {
					ResponseAocpCustomerData responseAocpCustomerData = getResponse(aocpCustomer);
							list1.add(responseAocpCustomerData);
					}
					
						return list1;
				}
				
						throw new NoSuchElementException("customer not found");
			
	}
	@Override
	public ResponseAocpCustomerData getResponse(AocpCustomer aocpCustomer) {
		ResponseAocpCustomerData aocpCustomerData =new ResponseAocpCustomerData();
		String finalSanction = aocpCustomer.getFinalSanction();
		
		 //logger.debug("Step 3");
		String nominee=aocpCustomer.getNomineeDetails();
		if(nominee == null || nominee.isEmpty()) {	
		}
		else {
			// logger.debug("Step 4");
			org.json.JSONObject nomineeDetails=new org.json.JSONObject(nominee);
			NomineeDetails nomineedetails=new NomineeDetails();
			nomineedetails.setName(nomineeDetails.getString("name"));
			nomineedetails.setGuardianName(nomineeDetails.getString("guardianName"));
			nomineedetails.setNomineeAge(nomineeDetails.getString("nomineeAge"));
			nomineedetails.setGuardianRelationship(nomineeDetails.getString("guardianRelationship"));
			nomineedetails.setNomineeRelationship(nomineeDetails.getString("nomineeRelationship"));
			nomineedetails.setDob(nomineeDetails.getString("dob"));
			nomineedetails.setGuardianAge(nomineeDetails.getString("guardianAge"));
			nomineedetails.setAddress_Line1(nomineeDetails.getString("address_Line1"));
			nomineedetails.setAddress_Line2(nomineeDetails.getString("address_Line2"));
			nomineedetails.setAddress_Line3(nomineeDetails.getString("address_Line3"));
			nomineedetails.setPincode(nomineeDetails.getString("pincode"));
			nomineedetails.setCity(nomineeDetails.getString("city"));
			nomineedetails.setState(nomineeDetails.getString("state"));
			nomineedetails.setDistrict(nomineeDetails.getString("district"));
			nomineedetails.setCountry(nomineeDetails.getString("country"));
			nomineedetails.setLatitude(nomineeDetails.getString("latitude"));
			nomineedetails.setLongitude(nomineeDetails.getString("longitude"));
			aocpCustomerData.setNomineeDetails(nomineedetails);
		}
		if(finalSanction == null || finalSanction.isEmpty() ) {
			
		}
		else {
			// logger.debug("Step 5");
			org.json.JSONArray finalSanctionInJson =new org.json.JSONArray(finalSanction);
  			List<FinalSaction> list=new ArrayList<>();
  			for(int n=0;n<finalSanctionInJson.length();n++) {
  				JSONObject jsonObject2 = finalSanctionInJson.getJSONObject(n);
					String checklistname = jsonObject2.getString("checklistname");
					String result = jsonObject2.getString("result");
					String Remarks = jsonObject2.getString("Remarks");
					
					FinalSaction finalSaction=new FinalSaction(checklistname,result,Remarks);
					list.add(finalSaction);
  			}
  			aocpCustomerData.setFinalsanction(list);
  			// logger.debug("Step 6");
		}
		
		String address1 = aocpCustomer.getAddress();
			if(address1 == null || address1.isEmpty()) {
				
			}
			else {
				// logger.debug("Step 7");
				org.json.JSONArray addressInJson =new org.json.JSONArray(aocpCustomer.getAddress());
  			List<Address> list=new ArrayList<>();
  			String addressType="NA";
  			String landmark="NA";
  			String latitude="NA";
  			String longitude="NA";
  			for(int n=0;n<addressInJson.length();n++) {
  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
  					String address_Line1 = jsonObject2.getString("ADDRESS_LINE1");
  					String address_Line2 = jsonObject2.getString("ADDRESS_LINE2");
  					String address_Line3 = jsonObject2.getString("ADDRESS_LINE3");
  					String city = jsonObject2.getString("CITY");
  					String pincode = jsonObject2.getString("PINCODE");
  					String district = jsonObject2.getString("DISTRICT");
  					String state = jsonObject2.getString("STATE");
  					String country = jsonObject2.getString("COUNTRY");
  					if(jsonObject2.has("ADDRESSTYPE")) {
  						addressType=jsonObject2.getString("ADDRESSTYPE");
  					}
  					if(jsonObject2.has("LANDMARK")) {
  						landmark=jsonObject2.getString("LANDMARK");
  					}
  					if(jsonObject2.has("LATITUDE")) {
  						latitude=jsonObject2.getString("LATITUDE");
  					}
  					if(jsonObject2.has("LONGITUDE")) {
  						longitude=jsonObject2.getString("LONGITUDE");
  					}
  			Address a=new Address(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
  				list.add(a);
  				
  			}	
  			aocpCustomerData.setAddress(list);
  			// logger.debug("Step 8");
			}

String ownerAddress2 = aocpCustomer.getOwnerAddress();
if(ownerAddress2 == null || ownerAddress2.isEmpty()) {
	
}
else {
	// logger.debug("Step 9");
	org.json.JSONArray ownerAddressInJson =new org.json.JSONArray(aocpCustomer.getOwnerAddress());
	List<OwnerAddress> listOwnerAddress=new ArrayList<>();
	
	for(int n=0;n<ownerAddressInJson.length();n++) {
		String addressType="NA";
			String landmark="NA";
			String district="NA";
			String country="NA";
			String latitude="NA";
  			String longitude="NA";
			JSONObject ownerAddressInJson1 = ownerAddressInJson.getJSONObject(n);
			String address_Line1 = ownerAddressInJson1.getString("ADDRESS_LINE1");
				String address_Line2 = ownerAddressInJson1.getString("ADDRESS_LINE2");
				String address_Line3 = ownerAddressInJson1.getString("ADDRESS_LINE3");
				String city = ownerAddressInJson1.getString("CITY");
				String pincode = ownerAddressInJson1.getString("PINCODE");
				if(ownerAddressInJson1.has("DISTRICT")) {
					district=ownerAddressInJson1.getString("DISTRICT");
				}
				String state = ownerAddressInJson1.getString("STATE");
				if(ownerAddressInJson1.has("COUNTRY")) {
					country=ownerAddressInJson1.getString("COUNTRY");
				}
				if(ownerAddressInJson1.has("ADDRESSTYPE")) {
					addressType=ownerAddressInJson1.getString("ADDRESSTYPE");
				}
				if(ownerAddressInJson1.has("LANDMARK")) {
					landmark=ownerAddressInJson1.getString("LANDMARK");
				}
				if(ownerAddressInJson1.has("LATITUDE")) {
						latitude=ownerAddressInJson1.getString("LATITUDE");
					}
					if(ownerAddressInJson1.has("LONGITUDE")) {
						longitude=ownerAddressInJson1.getString("LONGITUDE");
					}
	OwnerAddress ownerAddress=new OwnerAddress(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
	listOwnerAddress.add(ownerAddress);
	
	}
	// logger.debug("Step 10");
	aocpCustomerData.setOwnerAddress(listOwnerAddress);
}
		String otherAssets1 = aocpCustomer.getOtherAssets();
		if(otherAssets1 == null || otherAssets1.isEmpty())	{
			
		}
		else {
			 //logger.debug("Step 11");
			org.json.JSONArray otherAssetsInJson =new org.json.JSONArray(aocpCustomer.getOtherAssets());
			List<String> listOtherAssets=new ArrayList<>();
			for(int n=0;n<otherAssetsInJson.length();n++) {
				String asset = otherAssetsInJson.getString(n);
				listOtherAssets.add(asset);
			}
			aocpCustomerData.setOtherAssets(listOtherAssets);
			// logger.debug("Step 12");
		}
		 aocpCustomerData.setFlowStatus(aocpCustomer.getFlowStatus());
		// logger.debug("Step 13");
	List<AocpvIncomeDetails>  incomeList=aocpvIncomeService.getByApplicationNo(aocpCustomer.getApplicationNo());
				// List<AocpvIncomeDetails> incomeList = aocpCustomer.getIncome();
	// logger.debug("Step 14");
				 List<IncomeDetail> listIncome =new ArrayList<>();
				 String authVerify="YES";
				 for(AocpvIncomeDetails incomeDetails:incomeList) {
					 IncomeDetail incomeDetail =new IncomeDetail();
					 incomeDetail.setMember(incomeDetails.getMember());
					 incomeDetail.setTitle(incomeDetails.getTitle());
					 incomeDetail.setFirstName(incomeDetails.getFirstName());
					 incomeDetail.setLastName(incomeDetails.getLastName());
					 incomeDetail.setGender(incomeDetails.getGender());
					// logger.debug("Step 15");
					 
					 incomeDetail.setAge(incomeDetails.getAge());
					 
					 if(incomeDetails.getDob() != null)
					 {
						 LocalDate date = incomeDetails.getDob();
						 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
					   	String dob= date.format(formatter1);
					    incomeDetail.setDob(dob);
					 }
					
					// logger.debug("Step 16");
					 incomeDetail.setMobile(incomeDetails.getMobile());
					 incomeDetail.setMobileVerify(incomeDetails.getMobileVerify());
					 incomeDetail.setMarried(incomeDetails.getMarried());
					 incomeDetail.setAadharCard(incomeDetails.getAadharCard());
					 incomeDetail.setAadharNoVerify(incomeDetails.getAadharNoVerify());
					 incomeDetail.setPanCard(incomeDetails.getPanCard());
					 incomeDetail.setGaurantor(incomeDetails.getGaurantor());
					 incomeDetail.setPancardNoVerify(incomeDetails.getPancardNoVerify());
					 incomeDetail.setNameOnnameOnPancard(incomeDetails.getNameOnnameOnPancard());
					 incomeDetail.setDobONPancard(incomeDetails.getDobONPancard());
					 incomeDetail.setVoterId(incomeDetails.getVoterId());
					 incomeDetail.setVoterIdVerify(incomeDetails.getVoterIdVerify());
					 incomeDetail.setEarning(incomeDetails.getEarning());
					 incomeDetail.setOccupation(incomeDetails.getOccupation());
					 incomeDetail.setOccCode(incomeDetails.getOccCode());
					 incomeDetail.setPrimarySourceOfIncome(incomeDetails.getPrimarySourceOfIncome());
					 incomeDetail.setSecuredLoan(incomeDetails.getSecuredLoan());
					 incomeDetail.setForm60(incomeDetails.getForm60());
					 incomeDetail.setDrivingLicense(incomeDetails.getDrivingLicense());
					 incomeDetail.setDrivingLicenseIsVerify(incomeDetails.getDrivingLicenseIsVerify());
					 incomeDetail.setPassport(incomeDetails.getPassport());
					 incomeDetail.setPassportIsVerify(incomeDetails.getPassportIsVerify());
					 incomeDetail.setUnsecuredLoan(incomeDetails.getUnsecuredLoan());
					 incomeDetail.setMonthlyIncome(incomeDetails.getMonthlyIncome());
					 incomeDetail.setMonthlyLoanEmi(incomeDetails.getMonthlyLoanEmi());
					 if(incomeDetails.getAuthVerify()==null || incomeDetails.getAuthVerify().equals("NO")) {
						 authVerify="NO";
					 }
					 incomeDetail.setAuthVerify(incomeDetails.getAuthVerify()!=null?incomeDetails.getAuthVerify():"NO");
					 		listIncome.add(incomeDetail);
				 }
				 if(aocpCustomer.getFlowStatus().equals("ID")) {
					 List<IncomeDetail> collect = listIncome.stream().filter(income->income.getMember().equalsIgnoreCase("Self")).collect(Collectors.toList());
					if(collect.get(0).getGaurantor() == null || collect.get(0).getGaurantor().equals("")) {
					}
					else {
						String gaurantor = collect.get(0).getGaurantor();
					List<IncomeDetail> collect1 = listIncome.stream().filter(income->income.getMember().equalsIgnoreCase(gaurantor)).collect(Collectors.toList());
						 if(collect1.size() == 0) {
							 aocpCustomerData.setFlowStatus("UD");	 
						 }
					}
					}
				
				
				 aocpCustomerData.setIncomeDetails(listIncome);
				 
aocpCustomerData.setApplicationNo(aocpCustomer.getApplicationNo());
aocpCustomerData.setAuthVerify(authVerify);
aocpCustomerData.setCustomerId(aocpCustomer.getCustomerId());
aocpCustomerData.setName(aocpCustomer.getName());
aocpCustomerData.setMobileNo(aocpCustomer.getMobileNo());
aocpCustomerData.setMobileNoVerify(aocpCustomer.getMobileNoVerify());
//aocpCustomerData.setDateOfBirth(aocpCustomer.getDateOfBirth());

 if(aocpCustomer.getDateOfBirth() !=null)
 {
	 LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String dob1= dateOfBirth.format(formatter1);
		aocpCustomerData.setDateOfBirth(dob1);
 }


 
 if(aocpCustomer.getUtilityBillIsVerify()!=null && aocpCustomer.getUtilityBillIsVerify().equalsIgnoreCase("YES")) {
		String utilityBillResponse1 = aocpCustomer.getUtilityBillResponse();
		org.json.JSONObject utilityResponse=new org.json.JSONObject(utilityBillResponse1);
		aocpCustomerData.setUtiltyAddresResponse(utilityResponse.getJSONObject("result").getString("address"));
		
	}
	
 
aocpCustomerData.setHouseOwnership(aocpCustomer.getHouseOwnership());
aocpCustomerData.setRoofType(aocpCustomer.getRoofType());
aocpCustomerData.setAoRemarks(aocpCustomer.getAoRemarks());
aocpCustomerData.setRoRemarks(aocpCustomer.getRoRemarks());
aocpCustomerData.setResidenceStability(aocpCustomer.getResidenceStability());
aocpCustomerData.setUtilityBill(aocpCustomer.getUtilityBill());
aocpCustomerData.setRelationshipWithOwner(aocpCustomer.getRelationshipWithOwner());
aocpCustomerData.setBestOfferEmi(aocpCustomer.getBestOfferEmi());
aocpCustomerData.setBestOfferAmount(aocpCustomer.getBestOfferAmount());
aocpCustomerData.setBestOfferROI(aocpCustomer.getBestOfferROI());
aocpCustomerData.setBestOfferTenuer(aocpCustomer.getBestOfferTenuer());
aocpCustomerData.setVintage(aocpCustomer.getVintage());
aocpCustomerData.setTotalMonthlyIncome(aocpCustomer.getTotalMonthlyIncome());
aocpCustomerData.setTotalMonthlyEmi(aocpCustomer.getTotalMonthlyEmi());
aocpCustomerData.setEligibleAmount(aocpCustomer.getEligibleAmount());
aocpCustomerData.setFoodAndUtility(aocpCustomer.getFoodAndUtility());
aocpCustomerData.setRent(aocpCustomer.getRent());
aocpCustomerData.setAddress_verify(aocpCustomer.getAddressVerify());
aocpCustomerData.setSameAs(aocpCustomer.getSameAs());
aocpCustomerData.setPurposeId(aocpCustomer.getPurposeId());
aocpCustomerData.setSubCategory(aocpCustomer.getSubCategory());
aocpCustomerData.setSubCategoryPurposeId(aocpCustomer.getSubCategoryPurposeId());
aocpCustomerData.setTransportation(aocpCustomer.getTransportation());
aocpCustomerData.setEkycVerify(aocpCustomer.getEkycVerify());
aocpCustomerData.setMedical(aocpCustomer.getMedical());
aocpCustomerData.setEducation(aocpCustomer.getEducation());
aocpCustomerData.setOther(aocpCustomer.getOther());
aocpCustomerData.setTotal(aocpCustomer.getTotal());
aocpCustomerData.setListType(aocpCustomer.getListType());
aocpCustomerData.setMonthlyBalance(aocpCustomer.getMonthlyBalance());
aocpCustomerData.setPurposeOfLoan(aocpCustomer.getPurposeOfLoan());
aocpCustomerData.setIsLive(aocpCustomer.getIsLive());
aocpCustomerData.setExistingLoanPurpose(aocpCustomer.getExistingLoanPurpose());
aocpCustomerData.setMobileLinkToAadhar(aocpCustomer.getMobileLinkToAadhar());
aocpCustomerData.setMobile2(aocpCustomer.getMobile2());
aocpCustomerData.setTimestamp(aocpCustomer.getTimestamp());
aocpCustomerData.setMaxEmieligibility(aocpCustomer.getMaxEmieligibility());
aocpCustomerData.setCustomerClassification(aocpCustomer.getCustomerClassification());
aocpCustomerData.setForm60(aocpCustomer.getForm60());
aocpCustomerData.setStatus(aocpCustomer.getStatus());
aocpCustomerData.setIsNomineeDetails(aocpCustomer.getIsNomineeDetails());
aocpCustomerData.setNomineeAddressSameAs(aocpCustomer.getNomineeAddressSameAs());
aocpCustomerData.setPendingWith(aocpCustomer.getPendingWith());
aocpCustomerData.setCreatedBy(aocpCustomer.getCreatedby());
aocpCustomerData.setUtilityBillIsVerify(aocpCustomer.getUtilityBillIsVerify());
aocpCustomerData.setGuarantorRemarks(aocpCustomer.getGuarantorRemarks());
aocpCustomerData.setServiceProvider(aocpCustomer.getServiceProvider());
aocpCustomerData.setUtilityBillNo(aocpCustomer.getUtilityBillNo());
aocpCustomerData.setServiceProviderCode(aocpCustomer.getServiceProviderCode());
aocpCustomerData.setEmiProductCode(aocpCustomer.getEmiProductCode()!=null?aocpCustomer.getEmiProductCode():"");
aocpCustomerData.setObligation(aocpCustomer.getObligation());
aocpCustomerData.setUpiVerify(aocpCustomer.getUpiVerify());
if(aocpCustomer.getUpaResponse() != null) {
	org.json.JSONObject upaResponse =new org.json.JSONObject(aocpCustomer.getUpaResponse());
	UpaResponse response=new UpaResponse();
	response.setClientReferenceId(upaResponse.getString("clientReferenceId"));
	response.setVerifiedName(upaResponse.getString("verifiedName"));
	response.setVirtualAddress(upaResponse.getString("virtualAddress"));
	aocpCustomerData.setUpaResponse(response);
}
if(aocpCustomer.getAppNoWithProductCode() == null) {
String appNoWithProductCode ="VL-"+Long.toString(aocpCustomer.getApplicationNo());
aocpCustomerData.setAppNoWithProductCode(appNoWithProductCode);
}
else {
	aocpCustomerData.setAppNoWithProductCode(aocpCustomer.getAppNoWithProductCode());
}
if(aocpCustomer.getStatus().equalsIgnoreCase("APPROVED")) {
	AocpvLoanCreation aocpvLoanCreation	=aocpvLoanCreationService.findByApplicationNo(aocpCustomer.getApplicationNo());
	aocpCustomerData.setAocpvLoanCreation(aocpvLoanCreation);
}

try {
	String remarks = aocpCustomer.getRemarks();
	if(remarks != null) {
	org.json.JSONArray remark =new org.json.JSONArray(remarks);
List<RemarkResponse> list=new ArrayList<>();
	
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
			list.add(remarkResponse);
	}
	aocpCustomerData.setRemark1(list);
	aocpCustomerData.setRemarks("NA");
	}	
}
catch (JSONException e) {
	aocpCustomerData.setRemarks(aocpCustomer.getRemarks());
}

aocpCustomerData.setRequestLoan(aocpCustomer.getRequestLoan());
			String requestedloanResponse = aocpCustomer.getResponseEmi();
			if(requestedloanResponse!= null) {
				String OutstandingBalance=null;
				JSONObject jsonObject2=new JSONObject(requestedloanResponse);
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
	    		if(jsonObject2.has("TOTALPAYABLEAMOUNT")) {
	    			emiResponse.setTotalPayableAmount(jsonObject2.getString("TOTALPAYABLEAMOUNT"));
	    		}else {
	    			emiResponse.setTotalPayableAmount("");
	    		}
			aocpCustomerData.setEmiResponse(emiResponse);
			}
			
	List<AocpvImages> listAocpvImage = imageService.getByApplicationNo1(aocpCustomer.getApplicationNo());
	List<Image> listOfImages=new ArrayList<>();
	for(AocpvImages aocpvImages:listAocpvImage) {
		
		String geoLocation = aocpvImages.getGeoLocation();
		
		JSONObject jsonObjectImage=new JSONObject(geoLocation);
		
 		String pimage = jsonObjectImage.getString("image");
 		String pLat = jsonObjectImage.getString("Lat");
 		String pLong = jsonObjectImage.getString("Long");
 		String pAddress = jsonObjectImage.getString("Address");
 		String ptimestamp = jsonObjectImage.getString("timestamp");
 		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy hh:mm:ss");
 		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
 		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
 		// LocalDate timeStamp = LocalDate.parse(ptimestamp, formatter);
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
	aocpCustomerData.setImages(listOfImages);
	aocpCustomerData.setBranchId(aocpCustomer.getBranchid());
	aocpCustomerData.setBuisness(aocpCustomer.getBuisness());

	LocalDate date1 = aocpCustomer.getCreationDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String creationDate1= date1.format(formatter);
		aocpCustomerData.setCreationDate(creationDate1);
		LocalDate updatedate = aocpCustomer.getUpdatedate();
		String updatedate1= updatedate.format(formatter);
		aocpCustomerData.setUpdateDate(updatedate1);
return aocpCustomerData;
		
	}
	
	
	@Override
	public List<FetchAllWebResponse> fetchAllByBranchIdAndStatus( String status) {
		Optional<List<AocpCustomer>> optional = aocpCustomerDataRepo.getTopTenByIdAndStatus(status);
		int size = optional.map(List::size).orElse(0);
		System.out.println(size);
		if(size==0)
		{
			throw new NoSuchElementException("NO Record found");
		}
		else
		{
			List<AocpCustomer> listOfCustomer = optional.get();	
			List<FetchAllWebResponse> list1 =new ArrayList<>();
			
			for(AocpCustomer aocpCustomer: listOfCustomer) {
				LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
				if(aocpCustomer.getAppNoWithProductCode() == null) {
					aocpCustomer.setAppNoWithProductCode("VL-"+aocpCustomer.getApplicationNo());
			   	}
				 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
				   	String dob= dateOfBirth.format(formatter1);
				   	LocalDate creationDate = aocpCustomer.getCreationDate();
				   	String creationdate=creationDate.format(formatter1);
				   	LocalDate updatedate = aocpCustomer.getUpdatedate();
				   	String updatedate1=updatedate.format(formatter1);		
FetchAllWebResponse fetchAllWebResponse = new FetchAllWebResponse(aocpCustomer.getApplicationNo(),aocpCustomer.getCustomerId(),aocpCustomer.getName(),aocpCustomer.getMobileNo(),dob,aocpCustomer.getCreatedby(),creationdate,updatedate1,aocpCustomer.getStatus(),aocpCustomer.getAppNoWithProductCode(),aocpCustomer.getBranchid());

					list1.add(fetchAllWebResponse);
			}
			
				return list1;	
		}
	 
		
				
	}

	@Override
	public List<AocpCustomer> findByDate(LocalDate startdate, LocalDate enddate) {
	try {
		logger.debug("find by date start"+startdate+"   "+enddate);
		
	Optional<List<AocpCustomer>> optional = aocpCustomerDataRepo.findByDate(startdate, enddate);
	//System.out.println(optional.get());
	//logger.debug(optional.get().toString());
	List<AocpCustomer> list = optional.get();
	logger.debug(""+list.size());
	if(list.isEmpty()) {
		logger.debug("0"+list.size());
		throw new NoSuchElementException("NO Record found");
	}
//	int size = optional.map(List::size).orElse(0);
//	System.out.println(size);
//	System.out.println(size);
//	logger.debug(""+size);
//	if(size==0)
//	{
//		throw new NoSuchElementException("NO Record found");
//	}
	else {
		List<AocpCustomer> listOfCustomer = optional.get();
		
		//List<ResponseAocpCustomerData> list1 =new ArrayList<>();
		//for(AocpCustomer aocpCustomer: listOfCustomer) {
		//ResponseAocpCustomerData responseAocpCustomerData = getResponse(aocpCustomer);
				//list1.add(responseAocpCustomerData);
		//}
		
			return listOfCustomer;	
	}
	
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}

	@Override
	public ResponseAocpCustomerData getResponseWithoutImage(AocpCustomer aocpCustomer) {
		ResponseAocpCustomerData aocpCustomerData =new ResponseAocpCustomerData();
		
		try {
		String finalSanction = aocpCustomer.getFinalSanction();
		if(finalSanction == null || finalSanction.isEmpty() ) {
			
		}
		else {
			org.json.JSONArray finalSanctionInJson =new org.json.JSONArray(finalSanction);
  			List<FinalSaction> list=new ArrayList<>();
  			for(int n=0;n<finalSanctionInJson.length();n++) {
  				JSONObject jsonObject2 = finalSanctionInJson.getJSONObject(n);
					String checklistname = jsonObject2.getString("checklistname");
					String result = jsonObject2.getString("result");
					String Remarks = jsonObject2.getString("Remarks");
					
					FinalSaction finalSaction=new FinalSaction(checklistname,result,Remarks);
					list.add(finalSaction);
  			}
  			aocpCustomerData.setFinalsanction(list);
		}
		logger.debug("finalSanction end");
		logger.debug("address1 start");
		String address1 = aocpCustomer.getAddress();
			if(address1 == null || address1.isEmpty()) {
				
			}
			else {
				org.json.JSONArray addressInJson =new org.json.JSONArray(aocpCustomer.getAddress());
  			List<Address> list=new ArrayList<>();
  			String addressType="NA";
  			String landmark="NA";
  			String latitude="NA";
  			String longitude="NA";
  			for(int n=0;n<addressInJson.length();n++) {
  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
  					String address_Line1 = jsonObject2.getString("ADDRESS_LINE1");
  					String address_Line2 = jsonObject2.getString("ADDRESS_LINE2");
  					String address_Line3 = jsonObject2.getString("ADDRESS_LINE3");
  					String city = jsonObject2.getString("CITY");
  					String pincode = jsonObject2.getString("PINCODE");
  					String district = jsonObject2.getString("DISTRICT");
  					String state = jsonObject2.getString("STATE");
  					String country = jsonObject2.getString("COUNTRY");
  					if(jsonObject2.has("ADDRESSTYPE")) {
  						addressType=jsonObject2.getString("ADDRESSTYPE");
  					}
  					if(jsonObject2.has("LANDMARK")) {
  						landmark=jsonObject2.getString("LANDMARK");
  					}
  					if(jsonObject2.has("LATITUDE")) {
  						latitude=jsonObject2.getString("LATITUDE");
  					}
  					if(jsonObject2.has("LONGITUDE")) {
  						longitude=jsonObject2.getString("LONGITUDE");
  					}
  			Address a=new Address(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
  				list.add(a);
  				
  			}	
  			aocpCustomerData.setAddress(list);
			}
			logger.debug("address1 start");
			logger.debug("ownerAddress2 start");
String ownerAddress2 = aocpCustomer.getOwnerAddress();
if(ownerAddress2 == null || ownerAddress2.isEmpty()) {
	
}
else {
	org.json.JSONArray ownerAddressInJson =new org.json.JSONArray(aocpCustomer.getOwnerAddress());
	List<OwnerAddress> listOwnerAddress=new ArrayList<>();
	String addressType="NA";
	String landmark="NA";
	String district="NA";
	String country="NA";
	String latitude="NA";
	String longitude="NA";
	for(int n=0;n<ownerAddressInJson.length();n++) {

			JSONObject ownerAddressInJson1 = ownerAddressInJson.getJSONObject(n);
			String address_Line1 = ownerAddressInJson1.getString("ADDRESS_LINE1");
			String address_Line2 = ownerAddressInJson1.getString("ADDRESS_LINE2");
			String address_Line3 = ownerAddressInJson1.getString("ADDRESS_LINE3");
			String city = ownerAddressInJson1.getString("CITY");
			String pincode = ownerAddressInJson1.getString("PINCODE");
			if(ownerAddressInJson1.has("DISTRICT")) {
				district=ownerAddressInJson1.getString("DISTRICT");
			}
			String state = ownerAddressInJson1.getString("STATE");
			if(ownerAddressInJson1.has("COUNTRY")) {
				country=ownerAddressInJson1.getString("COUNTRY");
			}
			if(ownerAddressInJson1.has("ADDRESSTYPE")) {
				addressType=ownerAddressInJson1.getString("ADDRESSTYPE");
			}
			if(ownerAddressInJson1.has("LANDMARK")) {
				landmark=ownerAddressInJson1.getString("LANDMARK");
			}
			if(ownerAddressInJson1.has("LATITUDE")) {
				latitude=ownerAddressInJson1.getString("LATITUDE");
			}
			if(ownerAddressInJson1.has("LONGITUDE")) {
				longitude=ownerAddressInJson1.getString("LONGITUDE");
			}
OwnerAddress ownerAddress=new OwnerAddress(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
listOwnerAddress.add(ownerAddress);
	
	}
	
	aocpCustomerData.setOwnerAddress(listOwnerAddress);
}
		String otherAssets1 = aocpCustomer.getOtherAssets();
		if(otherAssets1 == null || otherAssets1.isEmpty())	{
			
		}
		else {
			org.json.JSONArray otherAssetsInJson =new org.json.JSONArray(aocpCustomer.getOtherAssets());
			List<String> listOtherAssets=new ArrayList<>();
			for(int n=0;n<otherAssetsInJson.length();n++) {
				String asset = otherAssetsInJson.getString(n);
				listOtherAssets.add(asset);
			}
			aocpCustomerData.setOtherAssets(listOtherAssets);
		}
		logger.debug("incomeList start");
	List<AocpvIncomeDetails>  incomeList=aocpvIncomeService.getByApplicationNo(aocpCustomer.getApplicationNo());
				// List<AocpvIncomeDetails> incomeList = aocpCustomer.getIncome();
				 List<IncomeDetail> listIncome =new ArrayList<>();
				 for(AocpvIncomeDetails incomeDetails:incomeList) {
					 IncomeDetail incomeDetail =new IncomeDetail();
					 incomeDetail.setMember(incomeDetails.getMember());
					 incomeDetail.setTitle(incomeDetails.getTitle());
					 incomeDetail.setFirstName(incomeDetails.getFirstName());
					 incomeDetail.setLastName(incomeDetails.getLastName());
					 incomeDetail.setGender(incomeDetails.getGender());
					 incomeDetail.setAge(incomeDetails.getAge());
					
					 if(incomeDetails.getDob() != null)
					 {
						 LocalDate date = incomeDetails.getDob();
						 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
					   	String dob= date.format(formatter1);
					    incomeDetail.setDob(dob);
					 }
					 
					
					
					 incomeDetail.setMobile(incomeDetails.getMobile());
					 incomeDetail.setMobileVerify(incomeDetails.getMobileVerify());
					 incomeDetail.setMarried(incomeDetails.getMarried());
					 incomeDetail.setAadharCard(incomeDetails.getAadharCard());
					 incomeDetail.setPancardNoVerify(incomeDetails.getPancardNoVerify());
					 incomeDetail.setPanCard(incomeDetails.getPanCard());
					 incomeDetail.setAadharNoVerify(incomeDetails.getAadharNoVerify());
					 incomeDetail.setVoterId(incomeDetails.getVoterId());
					 incomeDetail.setGaurantor(incomeDetails.getGaurantor());
					 incomeDetail.setVoterIdVerify(incomeDetails.getVoterIdVerify());
					 incomeDetail.setEarning(incomeDetails.getEarning());
					 incomeDetail.setOccupation(incomeDetails.getOccupation());
					 incomeDetail.setOccCode(incomeDetails.getOccCode());
					 incomeDetail.setPrimarySourceOfIncome(incomeDetails.getPrimarySourceOfIncome());
					 incomeDetail.setSecuredLoan(incomeDetails.getSecuredLoan());
					 incomeDetail.setForm60(incomeDetails.getForm60());
					 incomeDetail.setDrivingLicense(incomeDetails.getDrivingLicense());
					 incomeDetail.setDrivingLicenseIsVerify(incomeDetails.getDrivingLicenseIsVerify());
					 incomeDetail.setPassport(incomeDetails.getPassport());
					 incomeDetail.setPassportIsVerify(incomeDetails.getPassportIsVerify());
					 incomeDetail.setUnsecuredLoan(incomeDetails.getUnsecuredLoan());
					 incomeDetail.setMonthlyIncome(incomeDetails.getMonthlyIncome());
					 incomeDetail.setMonthlyLoanEmi(incomeDetails.getMonthlyLoanEmi());
					 
					 		listIncome.add(incomeDetail);
				 }
				 aocpCustomerData.setIncomeDetails(listIncome);
					logger.debug("incomeList start");
aocpCustomerData.setApplicationNo(aocpCustomer.getApplicationNo());
aocpCustomerData.setCustomerId(aocpCustomer.getCustomerId());
aocpCustomerData.setName(aocpCustomer.getName());
aocpCustomerData.setMobileNo(aocpCustomer.getMobileNo());
aocpCustomerData.setMobileNoVerify(aocpCustomer.getMobileNoVerify());
//aocpCustomerData.setDateOfBirth(aocpCustomer.getDateOfBirth());

if(aocpCustomer.getDateOfBirth() !=null)
{
	LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
	String dob1= dateOfBirth.format(formatter1);
	aocpCustomerData.setDateOfBirth(dob1);
}

aocpCustomerData.setHouseOwnership(aocpCustomer.getHouseOwnership());
aocpCustomerData.setRoofType(aocpCustomer.getRoofType());
aocpCustomerData.setResidenceStability(aocpCustomer.getResidenceStability());
aocpCustomerData.setUtilityBill(aocpCustomer.getUtilityBill());
aocpCustomerData.setAoRemarks(aocpCustomer.getAoRemarks());
aocpCustomerData.setRoRemarks(aocpCustomer.getRoRemarks());
aocpCustomerData.setRelationshipWithOwner(aocpCustomer.getRelationshipWithOwner());
aocpCustomerData.setVintage(aocpCustomer.getVintage());
aocpCustomerData.setBestOfferEmi(aocpCustomer.getBestOfferEmi());
aocpCustomerData.setBestOfferAmount(aocpCustomer.getBestOfferAmount());
aocpCustomerData.setBestOfferROI(aocpCustomer.getBestOfferROI());
aocpCustomerData.setBestOfferTenuer(aocpCustomer.getBestOfferTenuer());
aocpCustomerData.setTotalMonthlyIncome(aocpCustomer.getTotalMonthlyIncome());
aocpCustomerData.setTotalMonthlyEmi(aocpCustomer.getTotalMonthlyEmi());
aocpCustomerData.setFoodAndUtility(aocpCustomer.getFoodAndUtility());
aocpCustomerData.setSameAs(aocpCustomer.getSameAs());
aocpCustomerData.setAddress_verify(aocpCustomer.getAddressVerify());
aocpCustomerData.setEligibleAmount(aocpCustomer.getEligibleAmount());
aocpCustomerData.setRent(aocpCustomer.getRent());
aocpCustomerData.setIsLive(aocpCustomer.getIsLive());
aocpCustomerData.setAoSelfieeIsLive(aocpCustomer.getAoSelfieeIsLive());
aocpCustomerData.setRoSelfieeIsLive(aocpCustomer.getRoSelfieeIsLive());
aocpCustomerData.setTransportation(aocpCustomer.getTransportation());
aocpCustomerData.setMedical(aocpCustomer.getMedical());
aocpCustomerData.setTimestamp(aocpCustomer.getTimestamp());
aocpCustomerData.setEkycVerify(aocpCustomer.getEkycVerify());
aocpCustomerData.setPurposeId(aocpCustomer.getPurposeId());
aocpCustomerData.setSubCategory(aocpCustomer.getSubCategory());
aocpCustomerData.setSubCategoryPurposeId(aocpCustomer.getSubCategoryPurposeId());
aocpCustomerData.setEducation(aocpCustomer.getEducation());
aocpCustomerData.setOther(aocpCustomer.getOther());
aocpCustomerData.setForm60(aocpCustomer.getForm60());
aocpCustomerData.setTotal(aocpCustomer.getTotal());
aocpCustomerData.setUtilityBillIsVerify(aocpCustomer.getUtilityBillIsVerify());
if(aocpCustomer.getStatus().equalsIgnoreCase("APPROVED")) {
	AocpvLoanCreation aocpvLoanCreation	=aocpvLoanCreationService.findByApplicationNo(aocpCustomer.getApplicationNo());
	aocpCustomerData.setAocpvLoanCreation(aocpvLoanCreation);
}
aocpCustomerData.setMonthlyBalance(aocpCustomer.getMonthlyBalance());
aocpCustomerData.setPurposeOfLoan(aocpCustomer.getPurposeOfLoan());
aocpCustomerData.setExistingLoanPurpose(aocpCustomer.getExistingLoanPurpose());
aocpCustomerData.setMobileLinkToAadhar(aocpCustomer.getMobileLinkToAadhar());
aocpCustomerData.setMobile2(aocpCustomer.getMobile2());
aocpCustomerData.setMaxEmieligibility(aocpCustomer.getMaxEmieligibility());
aocpCustomerData.setCustomerClassification(aocpCustomer.getCustomerClassification());
aocpCustomerData.setFlowStatus(aocpCustomer.getFlowStatus());
aocpCustomerData.setGuarantorRemarks(aocpCustomer.getGuarantorRemarks());
aocpCustomerData.setStatus(aocpCustomer.getStatus());
aocpCustomerData.setIsNomineeDetails(aocpCustomer.getIsNomineeDetails());
aocpCustomerData.setNomineeAddressSameAs(aocpCustomer.getNomineeAddressSameAs());
aocpCustomerData.setPendingWith(aocpCustomer.getPendingWith());
if(aocpCustomer.getAppNoWithProductCode() == null) {
String appNoWithProductCode ="VL-"+Long.toString(aocpCustomer.getApplicationNo());
aocpCustomerData.setAppNoWithProductCode(appNoWithProductCode);
}
else {
	aocpCustomerData.setAppNoWithProductCode(aocpCustomer.getAppNoWithProductCode());
}


try {
	String remarks = aocpCustomer.getRemarks();
	if(remarks != null) {
	org.json.JSONArray remark =new org.json.JSONArray(remarks);
List<RemarkResponse> list=new ArrayList<>();
	
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
			updatedBy=remark1.getString("UPDATEDBY")	;
		}
		RemarkResponse remarkResponse=new RemarkResponse(Decision,RejectReason,Remarks,date,updatedBy);
		list.add(remarkResponse);
}
	aocpCustomerData.setRemark1(list);
	aocpCustomerData.setRemarks("NA");
	}	
}
catch (JSONException e) {
	aocpCustomerData.setRemarks(aocpCustomer.getRemarks());
}


aocpCustomerData.setRequestLoan(aocpCustomer.getRequestLoan());
			String requestedloanResponse = aocpCustomer.getResponseEmi();
			if(requestedloanResponse!= null) {
				String OutstandingBalance=null;
				JSONObject jsonObject2=new JSONObject(requestedloanResponse);
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
				if(jsonObject2.has("OUTSTANDINGBALANCE")) {
					 OutstandingBalance = jsonObject2.getString("OUTSTANDINGBALANCE");
						
					}
				String TotalAmountFinanced = jsonObject2.getString("TOTALAMOUNTFINANCED");
	EmiResponse emiResponse =new EmiResponse(TransactionCode,AmountRequested,AnnualPercentageRate,BalanceAmount,CommitmentFee,InterestAmortization,InterestAmount,InterestRate,MaturityBalance,MtgInsurance,NoOfAddDays,OddDaysIntAmount,OriginationFees,PaymentAmount,Term,TotalAmountFinanced,OutstandingBalance);
					if(jsonObject2.has("TOTALPAYABLEAMOUNT")) {
						emiResponse.setTotalPayableAmount(jsonObject2.getString("TOTALPAYABLEAMOUNT"));
					}else {
						emiResponse.setTotalPayableAmount("");
					}
				aocpCustomerData.setEmiResponse(emiResponse);
			}
			
	List<AocpvImages> listAocpvImage = imageService.getByApplicationNo1(aocpCustomer.getApplicationNo());
	if(listAocpvImage.isEmpty()) {
		
	}
	else {
	List<Image> listOfImages=new ArrayList<>();
	for(AocpvImages aocpvImages:listAocpvImage) {
		
		if(aocpvImages.getDocumenttype().equals("customerPhoto") || aocpvImages.getDocumenttype().equals("AO_Selfie")) {
			String geoLocation = aocpvImages.getGeoLocation();
			JSONObject jsonObjectImage=new JSONObject(geoLocation);
	 		String pimage = jsonObjectImage.getString("image");
	 		String pLat = jsonObjectImage.getString("Lat");
	 		String pLong = jsonObjectImage.getString("Long");
	 		String pAddress = jsonObjectImage.getString("Address");
	 		String ptimestamp = jsonObjectImage.getString("timestamp");
	 		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy hh:mm:ss");
	 		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
	 		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
	 		
	 		// LocalDate timeStamp = LocalDate.parse(ptimestamp, formatter);
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
	aocpCustomerData.setImages(listOfImages);
	}
	aocpCustomerData.setBranchId(aocpCustomer.getBranchid());
	aocpCustomerData.setBuisness(aocpCustomer.getBuisness());

	LocalDate date1 = aocpCustomer.getCreationDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String creationDate1= date1.format(formatter);
		aocpCustomerData.setCreationDate(creationDate1);
		
		LocalDate updatedate = aocpCustomer.getUpdatedate();
		String updatedate1= updatedate.format(formatter);
		aocpCustomerData.setUpdateDate(updatedate1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
return aocpCustomerData;
		
	}

	
	@Override
	public List<AocpCustomer> getAllByBranchIdAndStatus(String branchId, String status){
		
Optional<List<AocpCustomer>> optional = aocpCustomerDataRepo.getByIdAndStatus(branchId, status);
		
		int size = optional.map(List::size).orElse(0);
		System.out.println(size);
		if(size==0)
		{
			throw new NoSuchElementException("NO Record found");
		}
		else
		{
				return optional.get();
		}
	}

	@Override
	public ResponseAocpCustomerDataWeb fetchByApplicationNo(long applicationNoInLong, int versioncode) {
		Optional<AocpCustomer> optional=null;
		if(versioncode==0) {
			 optional = aocpCustomerDataRepo.getByApplicationNo(applicationNoInLong);
		}
		else {
			optional	=aocpCustomerDataRepo.getByApplicationAndVersionCode(applicationNoInLong,versioncode);
		}
		if(optional.isPresent()) {
			AocpCustomer aocpCustomer = optional.get();
			ResponseAocpCustomerDataWeb responseAocpCustomerData = getResponseWeb(aocpCustomer,versioncode);
			return responseAocpCustomerData;
		}
		throw new NoSuchElementException("NO Record found In AocpCustomer Data");
	}

	@Override
	public AocpCustomer fetchByApp(long applicationNoInLong) {
		Optional<AocpCustomer> optional = aocpCustomerDataRepo.getByApplicationNo(applicationNoInLong);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("NO Record found");
	}

	@Override
	public AocpCustomer fetchByCustNo(long customerIdInLong) {
		Optional<AocpCustomer> optional = aocpCustomerDataRepo.getByCustomerId(customerIdInLong);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("NO Record found");
	}

	@Override
	public List<AocpCustomer> getByName(String cutomesearch, String branchId) {
	
		List<AocpCustomer> list =aocpCustomerDataRepo.getByName(cutomesearch,branchId);
		if(list.size()==0) {
			try {
			long mobile = Long.parseLong(cutomesearch);
			}catch (Exception e) {
				throw new NoSuchElementException("NO Record found");
			}
			List<AocpCustomer> list1 =aocpCustomerDataRepo.getByMobile(cutomesearch,branchId);
			if(list1.size()==0) {
				long customerId = Long.parseLong(cutomesearch);
				List<AocpCustomer> list3 =aocpCustomerDataRepo.getByAppID(cutomesearch,branchId);
				if(list3.size()==0) {
					List<AocpCustomer> list2 =aocpCustomerDataRepo.getByCustomerID(cutomesearch,branchId);
					if(list2.size()==0) {
						long branchIdInLong = Long.parseLong(branchId);
						PreApprovedListVikasLoan fetchByCustomerId = loanInputService.fetchByCustomerIdAndBranch(customerId,branchIdInLong);
						AocpCustomer aocpCustomer=new AocpCustomer();
						aocpCustomer.setMobileNo(fetchByCustomerId.getMobilePhone());
						aocpCustomer.setCustomerId(fetchByCustomerId.getCustomerID());
						aocpCustomer.setName(fetchByCustomerId.getMemberName());
						aocpCustomer.setBranchid(Long.toString(fetchByCustomerId.getBranchId()));
						aocpCustomer.setEligibleAmount(Double.toString(fetchByCustomerId.getAmount()));
						aocpCustomer.setCreationDate(fetchByCustomerId.getCreateDate());
						aocpCustomer.setUpdatedate(fetchByCustomerId.getUpdatedate());
						aocpCustomer.setListType("PRE");
						aocpCustomer.setStatus(fetchByCustomerId.getStatus());
						List<AocpCustomer> list6=new ArrayList<>();
						list6.add(aocpCustomer);
						if(list6.size()==0) {
							throw new NoSuchElementException("NO Record found");
						}
						else {
							return list6;
						}
					}
					else {
						return list2;
					}
				}
				else {
					return list3;
				}
		
			}
			else {
				
				return list1;
			}
		}
		else {
			return list;
		}
		
		
	}

	@Override
	public long statusChange(String applicationNo, String status) {
		long applicationNoInLong = Long.parseLong(applicationNo);
		Optional<AocpCustomer> optional = aocpCustomerDataRepo.getByApplicationNo(applicationNoInLong);
		long customerId = optional.get().getCustomerId();
		if(optional.isPresent()) {
				AocpCustomer aocpCustomer = optional.get();
				if(aocpCustomer.getStatus().equalsIgnoreCase("APPROVED")) {
					customerId=0;
				}else {
					aocpCustomerDataRepo.updateStatus(status,applicationNoInLong);
					
				}
				
				
				return customerId;
			}
			throw new NoSuchElementException("NO Record found");
	}

	@Override
	public long getByCustomerIDApplication(long customerNO) {
		try {
		 long optional = aocpCustomerDataRepo.getByCustomerIDAppln(customerNO);
		 return optional;
		}
		catch (Exception e) {
			throw new NoSuchElementException("NO Record found");
		}
	
	}

	@Override
	public List<AocpCustomer> findTopTenByDate(LocalDate startdate, LocalDate enddate) {
		List<AocpCustomer> list=aocpCustomerDataRepo.findTopTenByDate(startdate,enddate);
		return list;
	}

	@Override
	public AocpCustomer getByApp(long applicationNoInLong) {
		Optional<AocpCustomer> optional =aocpCustomerDataRepo.getByApp(applicationNoInLong);
		if(optional.isPresent()) {
			return optional.get();
		}
		AocpCustomer aocpCustomer =new AocpCustomer();
		return aocpCustomer;
	}

	@Override
	public void createHistory(String applicationNo) {
		
		aocpCustomerDataRepo.aocpvhistory(applicationNo);
	}
	
	@Override
	public ResponseAocpCustomerDataWeb getResponseWeb(AocpCustomer aocpCustomer, int versioncode) {
		ResponseAocpCustomerDataWeb aocpCustomerData =new ResponseAocpCustomerDataWeb();
		String finalSanction = aocpCustomer.getFinalSanction();
		if(finalSanction == null || finalSanction.isEmpty() ) {
			
		}
		else {
			org.json.JSONArray finalSanctionInJson =new org.json.JSONArray(finalSanction);
  			List<FinalSaction> list=new ArrayList<>();
  			for(int n=0;n<finalSanctionInJson.length();n++) {
  				JSONObject jsonObject2 = finalSanctionInJson.getJSONObject(n);
					String checklistname = jsonObject2.getString("checklistname");
					String result = jsonObject2.getString("result");
					String Remarks = jsonObject2.getString("Remarks");
					
					FinalSaction finalSaction=new FinalSaction(checklistname,result,Remarks);
					list.add(finalSaction);
  			}
  			aocpCustomerData.setFinalsanction(list);
		}
		
		String address1 = aocpCustomer.getAddress();
			if(address1 == null || address1.isEmpty()) {
				
			}
			else {
				org.json.JSONArray addressInJson =new org.json.JSONArray(aocpCustomer.getAddress());
  			List<Address> list=new ArrayList<>();
  			String addressType="NA";
  			String landmark="NA";
  			String latitude="NA";
  			String longitude="NA";
  			for(int n=0;n<addressInJson.length();n++) {
  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
  					String address_Line1 = jsonObject2.getString("ADDRESS_LINE1");
  					String address_Line2 = jsonObject2.getString("ADDRESS_LINE2");
  					String address_Line3 = jsonObject2.getString("ADDRESS_LINE3");
  					String city = jsonObject2.getString("CITY");
  					String pincode = jsonObject2.getString("PINCODE");
  					String district = jsonObject2.getString("DISTRICT");
  					String state = jsonObject2.getString("STATE");
  					String country = jsonObject2.getString("COUNTRY");
  					if(jsonObject2.has("ADDRESSTYPE")) {
  						addressType=jsonObject2.getString("ADDRESSTYPE");
  					}
  					if(jsonObject2.has("LANDMARK")) {
  						landmark=jsonObject2.getString("LANDMARK");
  					}
  					if(jsonObject2.has("LATITUDE")) {
  						latitude=jsonObject2.getString("LATITUDE");
  					}
  					if(jsonObject2.has("LONGITUDE")) {
  						longitude=jsonObject2.getString("LONGITUDE");
  					}
  			Address a=new Address(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
  			
  				list.add(a);
  				
  			}	
  			aocpCustomerData.setAddress(list);
			}

String ownerAddress2 = aocpCustomer.getOwnerAddress();
if(ownerAddress2 == null || ownerAddress2.isEmpty()) {
	
}
else {
	org.json.JSONArray ownerAddressInJson =new org.json.JSONArray(aocpCustomer.getOwnerAddress());
	List<OwnerAddress> listOwnerAddress=new ArrayList<>();
	String addressType="NA";
	String landmark="NA";
	String district="NA";
	String country="NA";
	String latitude="NA";
	String longitude="NA";
	for(int n=0;n<ownerAddressInJson.length();n++) {

			JSONObject ownerAddressInJson1 = ownerAddressInJson.getJSONObject(n);
			String address_Line1 = ownerAddressInJson1.getString("ADDRESS_LINE1");
			String address_Line2 = ownerAddressInJson1.getString("ADDRESS_LINE2");
			String address_Line3 = ownerAddressInJson1.getString("ADDRESS_LINE3");
			String city = ownerAddressInJson1.getString("CITY");
			String pincode = ownerAddressInJson1.getString("PINCODE");
			if(ownerAddressInJson1.has("DISTRICT")) {
				district=ownerAddressInJson1.getString("DISTRICT");
			}
			String state = ownerAddressInJson1.getString("STATE");
			if(ownerAddressInJson1.has("COUNTRY")) {
				country=ownerAddressInJson1.getString("COUNTRY");
			}
			if(ownerAddressInJson1.has("ADDRESSTYPE")) {
				addressType=ownerAddressInJson1.getString("ADDRESSTYPE");
			}
			if(ownerAddressInJson1.has("LANDMARK")) {
				landmark=ownerAddressInJson1.getString("LANDMARK");
			}
			if(ownerAddressInJson1.has("LATITUDE")) {
				latitude=ownerAddressInJson1.getString("LATITUDE");
			}
			if(ownerAddressInJson1.has("LONGITUDE")) {
				longitude=ownerAddressInJson1.getString("LONGITUDE");
			}
OwnerAddress ownerAddress=new OwnerAddress(address_Line1,address_Line2,address_Line3,city,pincode,district,state,country,addressType,landmark,latitude,longitude);
listOwnerAddress.add(ownerAddress);
	
	}
	
	aocpCustomerData.setOwnerAddress(listOwnerAddress);
}
		String otherAssets1 = aocpCustomer.getOtherAssets();
		if(otherAssets1 == null || otherAssets1.isEmpty())	{
			
		}
		else {
			org.json.JSONArray otherAssetsInJson =new org.json.JSONArray(aocpCustomer.getOtherAssets());
			List<String> listOtherAssets=new ArrayList<>();
			for(int n=0;n<otherAssetsInJson.length();n++) {
				String asset = otherAssetsInJson.getString(n);
				listOtherAssets.add(asset);
			}
			aocpCustomerData.setOtherAssets(listOtherAssets);
		}
		List<AocpvIncomeDetails>  incomeList=null;
		if(versioncode==0) {
		  incomeList=aocpvIncomeService.getByApplicationNo(aocpCustomer.getApplicationNo());	
		}
		else {
		incomeList=aocpvIncomeService.getByApplicationAndVersion(aocpCustomer.getApplicationNo(),versioncode);
		}
	
				// List<AocpvIncomeDetails> incomeList = aocpCustomer.getIncome();
				 List<IncomeDetailWeb> listIncome =new ArrayList<>();
				 for(AocpvIncomeDetails incomeDetails:incomeList) {
					 IncomeDetailWeb incomeDetail =new IncomeDetailWeb();
					 incomeDetail.setMember(incomeDetails.getMember());
					 incomeDetail.setTitle(incomeDetails.getTitle());
					 incomeDetail.setFirstName(incomeDetails.getFirstName());
					 incomeDetail.setLastName(incomeDetails.getLastName());
					 incomeDetail.setGender(incomeDetails.getGender());
					 incomeDetail.setAge(incomeDetails.getAge());
					 
					 if(incomeDetails.getDob() !=null)
					 {
						 LocalDate date = incomeDetails.getDob();
						 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
					   	String dob= date.format(formatter1);
						 incomeDetail.setDob(dob); 
					 }
					
					 incomeDetail.setMobile(incomeDetails.getMobile());
					 incomeDetail.setMobileVerify(incomeDetails.getMobileVerify());
					 incomeDetail.setMarried(incomeDetails.getMarried());
					 incomeDetail.setAadharCard(incomeDetails.getAadharCard());
					 incomeDetail.setAadharNoVerify(incomeDetails.getAadharNoVerify());
					 incomeDetail.setPanCard(incomeDetails.getPanCard());
					 incomeDetail.setPancardNoVerify(incomeDetails.getPancardNoVerify());
					 incomeDetail.setVoterId(incomeDetails.getVoterId());
					 incomeDetail.setGaurantor(incomeDetails.getGaurantor());
					 incomeDetail.setVoterIdVerify(incomeDetails.getVoterIdVerify());
					 incomeDetail.setEarning(incomeDetails.getEarning());
					 incomeDetail.setOccupation(incomeDetails.getOccupation());
					 incomeDetail.setOccCode(incomeDetails.getOccCode());
					 incomeDetail.setPrimarySourceOfIncome(incomeDetails.getPrimarySourceOfIncome());
					 incomeDetail.setSecuredLoan(incomeDetails.getSecuredLoan());
					 incomeDetail.setUnsecuredLoan(incomeDetails.getUnsecuredLoan());
					 incomeDetail.setMonthlyIncome(incomeDetails.getMonthlyIncome());
					 incomeDetail.setForm60(incomeDetails.getForm60());
					 incomeDetail.setDrivingLicense(incomeDetails.getDrivingLicense());
					 incomeDetail.setDrivingLicenseIsVerify(incomeDetails.getDrivingLicenseIsVerify());
					 incomeDetail.setPassport(incomeDetails.getPassport());
					 incomeDetail.setPassportIsVerify(incomeDetails.getPassportIsVerify());
					 incomeDetail.setMonthlyLoanEmi(incomeDetails.getMonthlyLoanEmi()); 
					 incomeDetail.setNameOnnameOnPancard(incomeDetails.getNameOnnameOnPancard());
					 incomeDetail.setDobONPancard(incomeDetails.getDobONPancard());
					 incomeDetail.setAuthVerify(incomeDetails.getAuthVerify()!=null?incomeDetails.getAuthVerify():"NO");
					 if(incomeDetails.getPanCardResponse() != null) {
						 org.json.JSONObject pancardObject =new org.json.JSONObject(incomeDetails.getPanCardResponse());
						try {
							String LastUpdatedDate = pancardObject.getString("LastUpdatedDate");
							 String FirstName = pancardObject.getString("FirstName");
							 String NameOnCard = pancardObject.getString("NameOnCard");
							 String Middlename = pancardObject.getString("Middlename");
							 String LastName = pancardObject.getString("LastName");
							 String PanTitle = pancardObject.getString("PanTitle");
							 String PanID = pancardObject.getString("PanID");
							 String Pan_Status = pancardObject.getString("Pan-Status");
							 PanCardResponse pan = new PanCardResponse(LastUpdatedDate,FirstName,NameOnCard,Middlename,LastName,PanTitle,PanID,Pan_Status);
							 incomeDetail.setPanCardResponse(pan);
						}
						 catch (Exception e) {
							 String DateOfBirth = pancardObject.getString("DateOfBirth");
							 String SeedingStatus = pancardObject.getString("SeedingStatus");
							 String PanStatus = pancardObject.getString("PanStatus");
							 String Pan = pancardObject.getString("Pan");
							 String Name = pancardObject.getString("Name");
							 String FatherName = pancardObject.getString("FatherName");
							 PanCardResponseNew pan=new PanCardResponseNew(DateOfBirth,SeedingStatus,PanStatus,Pan,Name,FatherName);
							 incomeDetail.setPanCardResponseNew(pan);
						}
						 
					 }
					 if(incomeDetails.getVoterIdResponse() != null) {
						 org.json.JSONObject voterIdObject =new org.json.JSONObject(incomeDetails.getVoterIdResponse());
						 String name = voterIdObject.getString("name");
						 String epic_no = voterIdObject.getString("epic_no");
						 String ac_name = voterIdObject.getString("ac_name");
						 String state = voterIdObject.getString("state");
						 String district = voterIdObject.getString("district");
						 String house_no = voterIdObject.getString("house_no");
						 String ps_name = voterIdObject.getString("ps_name");
						 String rln_type = voterIdObject.getString("rln_type");
						 String rln_name = voterIdObject.getString("rln_name");
						 String pc_name = voterIdObject.getString("pc_name");
						 VoterIdResponse voter = new VoterIdResponse(name,epic_no,ac_name,state,district,house_no,ps_name,rln_type,rln_name,pc_name);
						 incomeDetail.setVoterIdResponse(voter);
					 }
					 List<Image> listOfImages=new ArrayList<>();
					 List<AocpvImages> listAocpvImage=null;
					 if(versioncode==0) {
					 listAocpvImage = imageService.getByApplicationNoAndMember(aocpCustomer.getApplicationNo(),incomeDetails.getMember(),"ID");	 
					 }
					 else {
					 listAocpvImage	= imageService.getByappAndVersion(aocpCustomer.getApplicationNo(),incomeDetails.getMember(),versioncode);
					 }
					 if(listAocpvImage.size()!=0){
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
						
						incomeDetail.setImage(listOfImages);
					 		listIncome.add(incomeDetail);
					 		
				 }
				 aocpCustomerData.setIncomeDetails(listIncome);
				 
aocpCustomerData.setApplicationNo(aocpCustomer.getApplicationNo());
aocpCustomerData.setCustomerId(aocpCustomer.getCustomerId());
aocpCustomerData.setName(aocpCustomer.getName());
aocpCustomerData.setMobileNo(aocpCustomer.getMobileNo());
aocpCustomerData.setMobileNoVerify(aocpCustomer.getMobileNoVerify());
//aocpCustomerData.setDateOfBirth(aocpCustomer.getDateOfBirth());
LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
	String dob1= dateOfBirth.format(formatter1);
	aocpCustomerData.setDateOfBirth(dob1);
aocpCustomerData.setHouseOwnership(aocpCustomer.getHouseOwnership());
aocpCustomerData.setRoofType(aocpCustomer.getRoofType());
aocpCustomerData.setAoRemarks(aocpCustomer.getAoRemarks());
aocpCustomerData.setRoRemarks(aocpCustomer.getRoRemarks());
aocpCustomerData.setResidenceStability(aocpCustomer.getResidenceStability());
aocpCustomerData.setUtilityBill(aocpCustomer.getUtilityBill());
aocpCustomerData.setRelationshipWithOwner(aocpCustomer.getRelationshipWithOwner());
aocpCustomerData.setVintage(aocpCustomer.getVintage());
aocpCustomerData.setTotalMonthlyIncome(aocpCustomer.getTotalMonthlyIncome());
aocpCustomerData.setTotalMonthlyEmi(aocpCustomer.getTotalMonthlyEmi());
aocpCustomerData.setEligibleAmount(aocpCustomer.getEligibleAmount());
aocpCustomerData.setFoodAndUtility(aocpCustomer.getFoodAndUtility());
aocpCustomerData.setRent(aocpCustomer.getRent());
aocpCustomerData.setAddress_verify(aocpCustomer.getAddressVerify());
aocpCustomerData.setSameAs(aocpCustomer.getSameAs());
aocpCustomerData.setVersioncode(aocpCustomer.getVersioncode());
aocpCustomerData.setPurposeId(aocpCustomer.getPurposeId());
aocpCustomerData.setSubCategory(aocpCustomer.getSubCategory());
aocpCustomerData.setSubCategoryPurposeId(aocpCustomer.getSubCategoryPurposeId());
aocpCustomerData.setTransportation(aocpCustomer.getTransportation());
aocpCustomerData.setMedical(aocpCustomer.getMedical());
aocpCustomerData.setEducation(aocpCustomer.getEducation());
aocpCustomerData.setOther(aocpCustomer.getOther());
aocpCustomerData.setTotal(aocpCustomer.getTotal());
aocpCustomerData.setBestOfferEmi(aocpCustomer.getBestOfferEmi());
aocpCustomerData.setBestOfferAmount(aocpCustomer.getBestOfferAmount());
aocpCustomerData.setBestOfferROI(aocpCustomer.getBestOfferROI());
aocpCustomerData.setBestOfferTenuer(aocpCustomer.getBestOfferTenuer());
aocpCustomerData.setTimestamp(aocpCustomer.getTimestamp());
aocpCustomerData.setGuarantorRemarks(aocpCustomer.getGuarantorRemarks());
aocpCustomerData.setEkycVerify(aocpCustomer.getEkycVerify());
aocpCustomerData.setMonthlyBalance(aocpCustomer.getMonthlyBalance());
aocpCustomerData.setPurposeOfLoan(aocpCustomer.getPurposeOfLoan());
aocpCustomerData.setExistingLoanPurpose(aocpCustomer.getExistingLoanPurpose());
aocpCustomerData.setMobileLinkToAadhar(aocpCustomer.getMobileLinkToAadhar());
aocpCustomerData.setMobile2(aocpCustomer.getMobile2());
aocpCustomerData.setMaxEmieligibility(aocpCustomer.getMaxEmieligibility());
aocpCustomerData.setCustomerClassification(aocpCustomer.getCustomerClassification());
aocpCustomerData.setFlowStatus(aocpCustomer.getFlowStatus());
aocpCustomerData.setStatus(aocpCustomer.getStatus());
aocpCustomerData.setListType(aocpCustomer.getListType());
aocpCustomerData.setForm60(aocpCustomer.getForm60());
aocpCustomerData.setUtilityBillResponse(aocpCustomer.getUtilityBillResponse());
aocpCustomerData.setUtilityBillIsVerify(aocpCustomer.getUtilityBillIsVerify());
aocpCustomerData.setCreatedBy(aocpCustomer.getCreatedby());
aocpCustomerData.setUpaResponse(aocpCustomer.getUpaResponse());
aocpCustomerData.setUpiVerify(aocpCustomer.getUpiVerify());
aocpCustomerData.setServiceProvider(aocpCustomer.getServiceProvider());
if(aocpCustomer.getAppNoWithProductCode() == null) {
String appNoWithProductCode ="VL-"+Long.toString(aocpCustomer.getApplicationNo());
aocpCustomerData.setAppNoWithProductCode(appNoWithProductCode);
}
else {
	aocpCustomerData.setAppNoWithProductCode(aocpCustomer.getAppNoWithProductCode());
}

if(aocpCustomer.getStatus().equalsIgnoreCase("APPROVED")) {
	AocpvLoanCreation aocpvLoanCreation	=aocpvLoanCreationService.findByApplicationNo(aocpCustomer.getApplicationNo());
	aocpCustomerData.setAocpvLoanCreation(aocpvLoanCreation);
}

try {
	String remarks = aocpCustomer.getRemarks();
	if(remarks != null) {
	org.json.JSONArray remark =new org.json.JSONArray(remarks);
List<RemarkResponse> list=new ArrayList<>();
	
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
			list.add(remarkResponse);
	}
	aocpCustomerData.setRemark1(list);
	aocpCustomerData.setRemarks("NA");
	}	
}
catch (JSONException e) {
	aocpCustomerData.setRemarks(aocpCustomer.getRemarks());
}

aocpCustomerData.setRequestLoan(aocpCustomer.getRequestLoan());
			String requestedloanResponse = aocpCustomer.getResponseEmi();
			if(requestedloanResponse!= null) {
				String OutstandingBalance=null;
				JSONObject jsonObject2=new JSONObject(requestedloanResponse);
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
	    			aocpCustomerData.setEmiResponse(emiResponse);
			}else {
				EmiResponse emiResponse =new EmiResponse("","","","","","","","","","","","","","","","","");
    			aocpCustomerData.setEmiResponse(emiResponse);	
			}
		
			List<AocpvImages> listAocpvImage=null;
		if(versioncode==0) {
		listAocpvImage = imageService.getByApplicationNo1(aocpCustomer.getApplicationNo());
		}
		else {
			listAocpvImage=imageService.getByapplicationAndVersion(aocpCustomer.getApplicationNo(),versioncode);
		}
	List<Image> listOfImages=new ArrayList<>();
	for(AocpvImages aocpvImages:listAocpvImage) {
		
		String geoLocation = aocpvImages.getGeoLocation();
		logger.debug("aocpvImages"+geoLocation);
		JSONObject jsonObjectImage=new JSONObject(geoLocation);
 		String pimage = jsonObjectImage.getString("image");
 		String pLat = jsonObjectImage.getString("Lat");
 		String pLong = jsonObjectImage.getString("Long");
 		logger.debug("long :"+pLong);
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
	aocpCustomerData.setImages(listOfImages);
	aocpCustomerData.setBranchId(aocpCustomer.getBranchid());
	aocpCustomerData.setBuisness(aocpCustomer.getBuisness());

	LocalDate date1 = aocpCustomer.getCreationDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String creationDate1= date1.format(formatter);
		aocpCustomerData.setCreationDate(creationDate1);
		LocalDate updatedate = aocpCustomer.getUpdatedate();
		String updatedate1= updatedate.format(formatter);
		aocpCustomerData.setUpdateDate(updatedate1);
return aocpCustomerData;
		
	}

	@Override
	public List<AocpCustomer> fetchByApplicationNumberPDF(Long appln) {
	
		List<AocpCustomer> list = aocpCustomerDataRepo.findDetailsByApplnPDF(appln);
		return list;
	}

	@Override
	public void flowStatusChange(long applicationno) {
		
		Optional<AocpCustomer> optional = aocpCustomerDataRepo.getByApp(applicationno);
		if(optional.isPresent()) {
			AocpCustomer aocpCustomer = optional.get();
			aocpCustomer.setFlowStatus("DDD");
			aocpCustomerDataRepo.save(aocpCustomer);
		}
	}

	@Override
	public List<AocpCustomer> findByDateAndBranchID(LocalDate startdate, LocalDate enddate, String branchId) {
	List<AocpCustomer> list = aocpCustomerDataRepo.findByDateAndBranchID(startdate, enddate,branchId);
	if(list.size()==0) {
		throw new NoSuchElementException("List is empty");
	}
		return list;
	}

	@Override
	public AocpCustomer getbycustomerId(long customerNo) {
		List<AocpCustomer> list = aocpCustomerDataRepo.getByCustomerid(customerNo);
		if(list.size()==0) {
			return null;
		}else {
			int n=-1;
			for(AocpCustomer details :list) {
				n++;
			}
			return list.get(n);
		}
	}
	@Override
	public List<AocpCustomer> findByDateAndStatus(LocalDate startdate, LocalDate enddate, String status) {
		List<AocpCustomer> list = aocpCustomerDataRepo.findByDateAndStatus(startdate, enddate,status);
		if(list.size()==0) {
			//throw new NoSuchElementException("List is empty");
		}
			return list;
	}

	@Override
	public List<AocpCustomer> findByDateStatusAndBranchId(LocalDate startdate, LocalDate enddate, String status,
			String branchId) {
		List<AocpCustomer> list = aocpCustomerDataRepo.findByDateStatusAndBranchId(startdate, enddate,status,branchId);
		if(list.size()==0) {
			//throw new NoSuchElementException("List is empty");
		}
			return list;
	}

	@Override
	public List<AocpCustomer> findByDateWeb(LocalDate startdate, LocalDate enddate) {
		List<AocpCustomer> list = aocpCustomerDataRepo.findByDateWeb(startdate, enddate);
		if(list.size()==0) {
			//throw new NoSuchElementException("List is empty");
		}
			return list;
	}

	@Override
	public List<AocpCustomer> findByDateAndBranchIdWeb(LocalDate startdate, LocalDate enddate, String branchId) {
		List<AocpCustomer> list = aocpCustomerDataRepo.findByDateAndBranchIdWeb(startdate, enddate,branchId);
		if(list.size()==0) {
			throw new NoSuchElementException("List is empty");
		}
			return list;
	}

	@Override
	public Optional<AocpCustomer> getByApplicationno(long applicationNoInLong) {
		Optional<AocpCustomer> optional=aocpCustomerDataRepo.getByApplicationNo(applicationNoInLong);
		return optional;
	}

	@Override
	public JSONObject calculateEmi(String productCode) {
		Optional<EmiResponseMaster> optional=emiResponseMasterRepository.getbyAmountRequestedAndTerm(productCode);
		if (optional.isPresent()) {
			EmiResponseMaster emiResponseMaster = optional.get();
			JSONObject Data=new JSONObject();
			JSONObject data=new JSONObject();
			Data.put("InterestAmount", emiResponseMaster.getInterestAmount());
			Data.put("CommitmentFee", "0.00");
			Data.put("AmountRequested", emiResponseMaster.getAmountRequested());
			Data.put("OriginationFees", "0.00");
			Data.put("TransactionCode", "00");
			Data.put("TotalAmountFinanced", emiResponseMaster.getAmountRequested());
			Data.put("Term", emiResponseMaster.getTerm());
			Data.put("LoanOutstanding Amount", "0");
			Data.put("InterestRate", "28");
			Data.put("PaymentAmount", emiResponseMaster.getPaymentAmount());
			Data.put("totalPayableAmount", emiResponseMaster.getTotalPayableAmount());
			data.put("Data", Data);
			return data;
		}
		throw new NoSuchElementException("Please enter valid Amount");
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CustomerWebResponse> getRetriveReportData(String startDate, String endDate ,String status1, String state1, String city1, String area) {
		List<Object[]> results =aocpCustomerDataRepo.getRetriveReportData(startDate,endDate,status1,state1,city1,area);
		List<CustomerWebResponse> list = new ArrayList<>();

        for (Object[] result : results) {
        	CustomerWebResponse customerWebResponse = new CustomerWebResponse();
        	BigInteger mobilePhoneInBigInt = (BigInteger) result[0];
        	BigInteger mobilePhone=mobilePhoneInBigInt!=null?mobilePhoneInBigInt:BigInteger.ZERO;
        	BigInteger customerIDInBigInt = (BigInteger) result[1];
        	BigInteger customerID=customerIDInBigInt!=null?customerIDInBigInt:BigInteger.ZERO;
            String memberName = (String) result[2];
            Date dateOfBirth1 = (Date) result[3];
            String dateOfBirth=dateOfBirth1!=null?dateOfBirth1.toString():"NA";
            String addressJson = (String) result[4];
            String address="NA";
            String postal="NA";
            String state="NA";
            try {
			org.json.JSONArray addressInJson =new org.json.JSONArray(addressJson);
 
  			JSONObject jsonObject = addressInJson.getJSONObject(0);
  			 address = jsonObject.getString("ADDRESS_LINE1");	  					
  			 postal = jsonObject.getString("PINCODE");
  			 state = jsonObject.getString("STATE");
  				
				}
				catch (Exception e) {
					
				}
            
            String branchId = (String) result[5];
            String amount = (String) result[6];
            String status = (String) result[7];
            Date createDate1 = (Date) result[8];
            Date updateDate1 = (Date) result[9];
            String createDate=createDate1!=null?createDate1.toString():"NA";
            String updateDate=updateDate1!=null?updateDate1.toString():"NA";
            BigInteger applicationNoInBigInt = (BigInteger) result[10];
            BigInteger applicationNo=applicationNoInBigInt!=null?applicationNoInBigInt:BigInteger.ZERO;
            String buisness = (String) result[11];
            String createdBy = (String) result[12];
            String existingLoanPurpose = (String) result[13];
            String mobileNoVerify = (String) result[14];
            Double monthlyBalanceInDouble = (Double) result[15];
            Double monthlyBalance=monthlyBalanceInDouble!=null?monthlyBalanceInDouble:0;
            Double otherInDouble = (Double) result[16];
            Double other=otherInDouble!=null?otherInDouble:0;
        	String remarks = (String) result[17];
        	String updatedBy="NA";
        	if(remarks!=null) {
				try {
					org.json.JSONArray remark =new org.json.JSONArray(remarks);
				 JSONObject jsonObject = remark.getJSONObject(remark.length()-1);
				 if(jsonObject.has("UPDATEDBY")) {
						updatedBy=jsonObject.getString("UPDATEDBY");
					}
				
				}catch (JSONException e) {
				
				}
				
			}
        	Double rentInDouble = (Double) result[18];
        	Double rent=rentInDouble!=null?rentInDouble:0;
        	String residenceStability = (String) result[19];
        	String roofType = (String) result[20];
        	Double totalInDouble = (Double) result[21];
        	Double total=totalInDouble!=null?totalInDouble:0;
        	Double totalMonthlyEmiInDouble = (Double) result[22];
        	Double totalMonthlyEmi=totalMonthlyEmiInDouble!=null?totalMonthlyEmiInDouble:0;
        	Double totalMonthlyIncomeInDouble = (Double) result[23];
        	Double totalMonthlyIncome=totalMonthlyIncomeInDouble!=null?totalMonthlyIncomeInDouble:0;
        	Double transportationInDouble = (Double) result[24];
        	Double transportation=transportationInDouble!=null?transportationInDouble:0;
        	 String utilityBill = (String) result[25];
             String vintage = (String) result[26];
             String centerId ="NA";
             String schemeId ="NA";
             String purposeCategoryId = "NA";
             String loanPurposeId  = "NA";
             String nomineeName  = "NA";
             String savingAccount = "NA";
             String finalsanctionAmount = (String) result[27];
             String sanctionAmount="NA";
             String sanctionTENURE="NA";
             if(finalsanctionAmount!=null) {
 				org.json.JSONArray finalSanctionAmountlist =new org.json.JSONArray(finalsanctionAmount);
 				for(int n=0;n<finalSanctionAmountlist.length();n++) {
 					JSONObject jsonObject2 = finalSanctionAmountlist.getJSONObject(n);
 					if(jsonObject2.has("sanctionAmount")) {
 						 sanctionAmount = jsonObject2.getString("sanctionAmount");	
 					}
 					if(jsonObject2.has("sanctionTENURE")) {
 						 sanctionTENURE = jsonObject2.getString("sanctionTENURE");
 					}
 					
 				}
 			}
      
             String purposeid = (String) result[28];
             String subCategoryPurposeId = (String) result[29];
             String requestedloanResponse = (String) result[30];
             String title = (String) result[31];
             String firstName = (String) result[32];
             String lastName = (String) result[33];
             Date dobNominee = (Date) result[34];
             String dobNomineeInString=dobNominee!=null?dobNominee.toString():"NA";
             Integer age=(Integer)result[35];
             String member = (String) result[36];
             BigInteger mobileNoInBigInt = (BigInteger) result[37];
             BigInteger mobileNo=mobileNoInBigInt!=null?mobileNoInBigInt:BigInteger.ZERO;
             String mobileVerify = (String) result[38];
             String aadharCard = (String) result[39];
             String aadharNoVerify = (String) result[40];
             String voterId = (String) result[41];
             String voterIdVerify = (String) result[42];
             String panCard = (String) result[43];
             String pancardNoVerify = (String) result[44];
             String occupation = (String) result[45];
             String earning = (String) result[46];
             Double monthlyIncomeInDouble = (Double) result[47];
             Double monthlyIncome=monthlyIncomeInDouble!=null?monthlyIncomeInDouble:0;
             String primarySourceOfIncome = (String) result[48];
             
             String AmountRequested="NA";
 			 String interestRate="NA";
 			 String OriginationFees="NA";
 			 String PaymentAmount="NA";
 			 String Term="NA";
 			 String TotalAmountFinanced="NA";
 			 String InterestAmount="NA";
             if(requestedloanResponse!= null) {
 				String OutstandingBalance=null;
 				JSONObject jsonObject2=new JSONObject(requestedloanResponse);
 				if(jsonObject2.has("AMOUNTREQUESTED")) {
 				 AmountRequested = jsonObject2.getString("AMOUNTREQUESTED");	
 				}
 				if(jsonObject2.has("INTERESTRATE")) {
 					interestRate = jsonObject2.getString("INTERESTRATE");	
 					}
 				if(jsonObject2.has("ORIGINATIONFEES")) {
 					OriginationFees = jsonObject2.getString("ORIGINATIONFEES");	
 					}
 				if(jsonObject2.has("PAYMENTAMOUNT")) {
 					PaymentAmount = jsonObject2.getString("PAYMENTAMOUNT");	
 					}
 				if(jsonObject2.has("TERM")) {
 					 Term = jsonObject2.getString("TERM");	
 					}
 				if(jsonObject2.has("TOTALAMOUNTFINANCED")) {
 					TotalAmountFinanced = jsonObject2.getString("TOTALAMOUNTFINANCED");	
 					}
 				
 				if(jsonObject2.has("INTERESTAMOUNT")) {
 					InterestAmount = jsonObject2.getString("INTERESTAMOUNT");	
 					}
 				  			
 			}
         
            customerWebResponse.setMobilePhone(mobilePhone.longValue());
            customerWebResponse.setCustomerID(customerID.longValue());
            customerWebResponse.setMemberName(memberName!=null?memberName:"NA");
            customerWebResponse.setDateOfBirth(dateOfBirth!=null?dateOfBirth:"NA");
            customerWebResponse.setAddress(address!=null?address:"NA");
            customerWebResponse.setState(state!=null?state:"NA");
            customerWebResponse.setPostal(postal!=null?postal:"NA");
            customerWebResponse.setBranchId(branchId!=null?branchId:"NA");
            customerWebResponse.setAmount(amount!=null?amount:"NA");
            customerWebResponse.setStatus(status!=null?status:"NA");
            customerWebResponse.setCreateDate(createDate!=null?createDate:"NA");
            customerWebResponse.setUpdateDate(updateDate!=null?updateDate:"NA");
            customerWebResponse.setApplicationNo(applicationNo.longValue());
            customerWebResponse.setBuisness(buisness!=null?buisness:"NA");
            customerWebResponse.setCreatedBy(createdBy!=null?createdBy:"NA");
            customerWebResponse.setExistingLoanPurpose(existingLoanPurpose!=null?existingLoanPurpose:"");
            customerWebResponse.setMobileNoVerify(mobileNoVerify!=null?mobileNoVerify:"NA");
            customerWebResponse.setMonthlyBalance(monthlyBalance);
            customerWebResponse.setOther(other);
            customerWebResponse.setRemarks(remarks!=null?remarks:"NA");
            customerWebResponse.setRent(rent);
            customerWebResponse.setResidenceStability(residenceStability!=null?residenceStability:"NA");
            customerWebResponse.setRoofType(roofType!=null?roofType:"NA");
            customerWebResponse.setTotal(total);
            customerWebResponse.setTotalMonthlyEmi(totalMonthlyEmi);
            customerWebResponse.setTotalMonthlyIncome(totalMonthlyIncome);
            customerWebResponse.setTransportation(transportation);
            customerWebResponse.setUtilityBill(utilityBill!=null?utilityBill:"NA");
            customerWebResponse.setVintage(vintage!=null?vintage:"NA");
            customerWebResponse.setCenterId(centerId!=null?centerId:"NA");
            customerWebResponse.setSchemeId(schemeId!=null?schemeId:"NA");
            customerWebResponse.setPurposeCategoryId(purposeCategoryId!=null?purposeCategoryId:"NA");
            customerWebResponse.setLoanPurposeId(loanPurposeId!=null?loanPurposeId:"NA");
            customerWebResponse.setNomineeName(nomineeName!=null?nomineeName:"NA");
            customerWebResponse.setSavingAccount(savingAccount!=null?savingAccount:"NA");
            customerWebResponse.setSanctionAmount(sanctionAmount!=null?sanctionAmount:"NA");
            customerWebResponse.setSanctionTENURE(sanctionTENURE!=null?sanctionTENURE:"NA");
            customerWebResponse.setFinalSanctionAmount(finalsanctionAmount!=null?finalsanctionAmount:"NA");
            customerWebResponse.setUpdatedBy(updatedBy!=null?updatedBy:"NA");
            customerWebResponse.setPurposeid(purposeid!=null?purposeid:"NA");
            customerWebResponse.setSubCategoryPurposeId(subCategoryPurposeId!=null?subCategoryPurposeId:"NA");
            customerWebResponse.setAmountRequested(AmountRequested!=null?AmountRequested:"NA");
            customerWebResponse.setInterestRate(interestRate!=null?interestRate:"NA");
            customerWebResponse.setOriginationFees(OriginationFees!=null?OriginationFees:"NA");
            customerWebResponse.setPaymentAmount(PaymentAmount!=null?PaymentAmount:"NA");
            customerWebResponse.setTerm(Term!=null?Term:"NA");
            customerWebResponse.setTotalAmountFinanced(TotalAmountFinanced!=null?TotalAmountFinanced:"NA");
            customerWebResponse.setInterestAmount(InterestAmount!=null?InterestAmount:"NA");
            customerWebResponse.setTitle(title!=null?title:"NA");
            customerWebResponse.setFirstName(firstName!=null?firstName:"NA");
            customerWebResponse.setLastName(lastName!=null?lastName:"NA");
            customerWebResponse.setDob(dobNomineeInString!=null?dobNomineeInString:"NA");
            customerWebResponse.setAge(age!=null?age:0);
            customerWebResponse.setMember(member!=null?member:"NA");
            customerWebResponse.setMobile(mobileNo.longValue());
            customerWebResponse.setMobileVerify(mobileVerify!=null?mobileVerify:"NA");
            customerWebResponse.setAadharCard(aadharCard!=null?aadharCard:"NA");
            customerWebResponse.setAadharNoVerify(aadharNoVerify!=null?aadharNoVerify:"NA");
            customerWebResponse.setVoterId(voterId!=null?voterId:"NA");
            customerWebResponse.setVoterIdVerify(voterIdVerify!=null?voterIdVerify:"NA");
            customerWebResponse.setPanCard(panCard!=null?panCard:"NA");
            customerWebResponse.setPancardNoVerify(pancardNoVerify!=null?pancardNoVerify:"NA");
            customerWebResponse.setOccupation(occupation!=null?occupation:"NA");
            customerWebResponse.setEarning(earning!=null?earning:"NA");
            customerWebResponse.setMonthlyIncome(monthlyIncome);
            customerWebResponse.setPrimarySourceOfIncome(primarySourceOfIncome!=null?primarySourceOfIncome:"NA");
            
            list.add(customerWebResponse);
        }
        return list;
	}

	@Override
	public List<EmiProductCodeResponse> getEmiProductCodes() {
		List<EmiResponseMaster> list = emiResponseMasterRepository.findAll();
		List<EmiProductCodeResponse> responseList=new ArrayList<>();
		for(EmiResponseMaster emiResponseMaster:list) {
			EmiProductCodeResponse response=new EmiProductCodeResponse();
			response.setAmount(emiResponseMaster.getAmountRequested());
			response.setProductCode(emiResponseMaster.getProductCode());
			response.setTerm(emiResponseMaster.getTerm()+"M");
			responseList.add(response);
		}
		return responseList;
	}
	
}
