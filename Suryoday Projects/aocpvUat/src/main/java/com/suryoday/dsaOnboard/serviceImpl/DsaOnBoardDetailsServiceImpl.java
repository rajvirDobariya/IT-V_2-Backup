package com.suryoday.dsaOnboard.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.dsaOnboard.pojo.Address;
import com.suryoday.dsaOnboard.pojo.AmlReportResponse;
import com.suryoday.dsaOnboard.pojo.BankDetailsResponse;
import com.suryoday.dsaOnboard.pojo.BusinessReference;
import com.suryoday.dsaOnboard.pojo.CfrReportResponse;
import com.suryoday.dsaOnboard.pojo.CreditFeedbackResponse;
import com.suryoday.dsaOnboard.pojo.DsaImage;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.pojo.DsaOnboardPayoutDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;
import com.suryoday.dsaOnboard.pojo.DsaUserLog;
import com.suryoday.dsaOnboard.pojo.EmpanellementCriteriaResponse;
import com.suryoday.dsaOnboard.pojo.LoginResponse;
import com.suryoday.dsaOnboard.pojo.ReferenceCheckResponse;
import com.suryoday.dsaOnboard.pojo.RelationToSsfbStaffResponse;
import com.suryoday.dsaOnboard.repository.DsaOnBoardDetailsRepository;
import com.suryoday.dsaOnboard.repository.DsaSchemeMasterRepository;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.dsaOnboard.service.DsaPayoutDetailsService;
import com.suryoday.dsaOnboard.service.DsaUserLogService;

@Service
public class DsaOnBoardDetailsServiceImpl implements DsaOnBoardDetailsService {

	Logger logger = LoggerFactory.getLogger(DsaOnBoardDetailsServiceImpl.class);

	@Autowired
	DsaOnBoardDetailsRepository dsaOnBoardDetailsRepository;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	@Autowired
	DsaImageService imageService;

	@Autowired
	DsaUserLogService dsaUserLogService;

	@Autowired
	DsaPayoutDetailsService dsaPayoutDetailsService;

	@Autowired
	DsaSchemeMasterRepository masterRepository;

	@Override
	public String saveData(DsaOnboardDetails dsaOnboardDetails) {
		dsaOnBoardDetailsRepository.save(dsaOnboardDetails);
		return "update successfully";
	}

	@Override
	public DsaOnboardDetails getByApplicationNo(String applicationNo) {
		long applicationno = Long.parseLong(applicationNo);
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository.getByApplicationNo(applicationno);
		if (optional.isPresent()) {
			return optional.get();
		}
		return new DsaOnboardDetails();
	}

	@Override
	public DsaOnboardDetails getByApplicationno(String applicationNo) {
		long applicationno = Long.parseLong(applicationNo);
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository.getByApplicationNo(applicationno);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public JSONObject getReviewData(String applicationNo, String request) {
		long applicationno = Long.parseLong(applicationNo);
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository.getByApplicationNo(applicationno);
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardDetails = optional.get();
			List<DsaOnboardMember> list = dsaOnboardMemberService.getByApplication(applicationno);
			org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
			org.json.simple.JSONArray BasicDetailsList = new org.json.simple.JSONArray();
			for (DsaOnboardMember dsaOnboardMember : list) {
				org.json.simple.JSONObject BasicDetails = new org.json.simple.JSONObject();
				BasicDetails.put("applicationNo", applicationNo);
				if (dsaOnboardMember.getPanCardNo() != null && request.equals("web")) {
					String substring = "XXXXXX" + dsaOnboardMember.getPanCardNo().substring(6, 10);
					BasicDetails.put("panCardNo", substring);
				} else {
					BasicDetails.put("panCardNo", dsaOnboardMember.getPanCardNo());
				}
				BasicDetails.put("panCardVerify", dsaOnboardMember.isPanCardVerify());
				BasicDetails.put("name", dsaOnboardMember.getName());
				BasicDetails.put("gender", dsaOnboardMember.getGender());
				BasicDetails.put("presentOccupation", dsaOnboardMember.getPresentOccupation());

				if (dsaOnboardMember.getMobile() != null && request.equals("web")) {
					String substring = "XXXXXX" + dsaOnboardMember.getMobile().substring(6, 10);
					BasicDetails.put("mobileNo", substring);
				} else {
					BasicDetails.put("mobileNo", dsaOnboardMember.getMobile());
				}
				BasicDetails.put("mobileNoVerify", dsaOnboardMember.isMobileNoVerify());

				if (dsaOnboardMember.getAlternateMobileNo() != null && request.equals("web")) {
					String substring = "XXXXXX" + dsaOnboardMember.getAlternateMobileNo().substring(6, 10);
					BasicDetails.put("alternateMobileNo", substring);
				} else {
					BasicDetails.put("alternateMobileNo", dsaOnboardMember.getAlternateMobileNo());
				}
				BasicDetails.put("alternateMobileNoVerify", dsaOnboardMember.isAlternateMobileNoVerify());
				BasicDetails.put("member", dsaOnboardMember.getMember());
				BasicDetails.put("emailId", dsaOnboardMember.getEmailId());
				BasicDetails.put("aadharNo", dsaOnboardMember.getAadharNo());
				BasicDetails.put("aadharNoVerify", dsaOnboardMember.isAadharNoVerify());
				BasicDetails.put("dateOfBirth", dsaOnboardMember.getDateOfBirth());
				BasicDetails.put("nameMatch", dsaOnboardMember.getNameMatch());
				BasicDetails.put("faceMatch", dsaOnboardMember.getFaceMatch());
				BasicDetails.put("nameMatchPercent", dsaOnboardMember.getNameMatchPercent());
				BasicDetails.put("faceMatchPercent", dsaOnboardMember.getFaceMatchPercent());
				BasicDetails.put("ekycVerify",
						dsaOnboardMember.getEkycVerify() != null ? dsaOnboardMember.getEkycVerify() : "NO");
				BasicDetails.put("ekycDoneby", dsaOnboardMember.getEkycDoneBy());
				BasicDetails.put("isMsmeRegister", dsaOnboardMember.getIsMsmeRegister());
				BasicDetails.put("gstNo", dsaOnboardMember.getGstNo());
				BasicDetails.put("gstVerify", dsaOnboardMember.getGstNoVerify());
				BasicDetails.put("cibilScore", dsaOnboardMember.getCibilScore());
				BasicDetails.put("nameOnPancard",
						dsaOnboardMember.getNameOnPanCard() != null ? dsaOnboardMember.getNameOnPanCard() : "");
				BasicDetails.put("dobONPancard",
						dsaOnboardMember.getDobOnPanCard() != null ? dsaOnboardMember.getDobOnPanCard() : "");
				dsaOnboardMember.setIsPrimaryMember(
						dsaOnboardMember.getIsPrimaryMember() != null ? dsaOnboardMember.getIsPrimaryMember() : "NO");
				BasicDetails.put("isPrimaryMember", dsaOnboardMember.getIsPrimaryMember());
				if (dsaOnboardMember.getAddress() != null) {
					String address = dsaOnboardMember.getAddress();
					org.json.JSONArray addressInJson = new org.json.JSONArray(address);
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						List<Address> personList = objectMapper.readValue(address, new TypeReference<List<Address>>() {
						});

//					 if(request.equals("mob")) {
						JSONArray p = new JSONArray(personList);
						BasicDetails.put("AddressDetails", p);
//					 }else {
//						 BasicDetails.put("AddressDetails", personList); 
//					 }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (request.equals("web")) {
					List<DsaImage> listOfImage = imageService.getByApplicationNoAndMember(applicationNo,
							dsaOnboardMember.getMember());
					org.json.simple.JSONArray images = new org.json.simple.JSONArray();
					if (listOfImage.size() != 0) {
						for (DsaImage aocpvImages : listOfImage) {
							String geoLocation = aocpvImages.getGeoLocation();
							org.json.JSONObject jsonObjectImage = new org.json.JSONObject(geoLocation);
							org.json.simple.JSONObject geolocation = new org.json.simple.JSONObject();
							org.json.simple.JSONObject image = new org.json.simple.JSONObject();
							geolocation.put("image", jsonObjectImage.getString("image"));
							geolocation.put("Lat", jsonObjectImage.getString("Lat"));
							geolocation.put("Long", jsonObjectImage.getString("Long"));
							geolocation.put("Address", jsonObjectImage.getString("Address"));
							geolocation.put("timestamp", jsonObjectImage.getString("timestamp"));
							image.put("geolocation", geolocation);
							image.put("documenttype", aocpvImages.getDocumenttype());
							image.put("imageName", aocpvImages.getImageName());
							image.put("type", aocpvImages.getType());
							image.put("size", aocpvImages.getSize());
							image.put("member", aocpvImages.getMember());
							byte[] images2 = aocpvImages.getImages();
							String encoded = Base64.getEncoder().encodeToString(images2);
							image.put("image", encoded);

							images.add(image);
						}
					}
					BasicDetails.put("Documents", images);
				}
				BasicDetailsList.add(BasicDetails);
			}
			org.json.simple.JSONObject RegistrationDetails = new org.json.simple.JSONObject();
			RegistrationDetails.put("applicationNo", applicationNo);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String format = dsaOnboardDetails.getCreatedDate().format(formatter);
			RegistrationDetails.put("createdTime", format);
			RegistrationDetails.put("companyName", dsaOnboardDetails.getCompanyName());
			RegistrationDetails.put("entity", dsaOnboardDetails.getEntity());
			RegistrationDetails.put("emailId", dsaOnboardDetails.getEmailId());
			RegistrationDetails.put("emailIdVerify", dsaOnboardDetails.isEmailIdVerify());
			RegistrationDetails.put("mobileNo", dsaOnboardDetails.getMobileNo());
			RegistrationDetails.put("mobileNoVerify", dsaOnboardDetails.isMobileNoVerify());
			RegistrationDetails.put("leadId", dsaOnboardDetails.getLeadId());
			RegistrationDetails.put("leadName", dsaOnboardDetails.getLeadName());
			RegistrationDetails.put("branchId", dsaOnboardDetails.getBranchId());
			RegistrationDetails.put("typeOfRelationship", dsaOnboardDetails.getTypeOfRelationship());
			if (dsaOnboardDetails.getProductType() != null) {
				JSONArray j = new org.json.JSONArray(dsaOnboardDetails.getProductType());
				RegistrationDetails.put("productType", j);
			}

			RegistrationDetails.put("noOfPartner", dsaOnboardDetails.getNoOfPartner());
			RegistrationDetails.put("constitutionType", dsaOnboardDetails.getConstitutionType());
			RegistrationDetails.put("status", dsaOnboardDetails.getStatus());
			Data.put("RegistrationDetails", RegistrationDetails);
			Data.put("BasicDetails", BasicDetailsList);
			if (dsaOnboardDetails.getNsmRemarkVerify() == null) {
				dsaOnboardDetails.setNsmRemarkVerify("NO");
			}
			if (dsaOnboardDetails.getPayoutDetailsVerify() == null) {
				dsaOnboardDetails.setPayoutDetailsVerify("NO");
			}
			Data.put("nsmRemarkVerify", dsaOnboardDetails.getNsmRemarkVerify());
			Data.put("payoutDetailsVerify", dsaOnboardDetails.getPayoutDetailsVerify());
			dsaOnboardDetails.setSendLeegalityVerify(
					dsaOnboardDetails.getSendLeegalityVerify() != null ? dsaOnboardDetails.getSendLeegalityVerify()
							: "NO");
			dsaOnboardDetails.setCheckLeegalityVerify(
					dsaOnboardDetails.getCheckLeegalityVerify() != null ? dsaOnboardDetails.getCheckLeegalityVerify()
							: "NO");
			Data.put("sendLeegalityVerify", dsaOnboardDetails.getSendLeegalityVerify());
			Data.put("checkLeegalityVerify", dsaOnboardDetails.getCheckLeegalityVerify());
			org.json.simple.JSONObject bankDetails = new org.json.simple.JSONObject();
			bankDetails.put("ifscCode", dsaOnboardDetails.getIfscCode());
			bankDetails.put("bankAccountNo", dsaOnboardDetails.getBankAccountNo());
			bankDetails.put("bankName", dsaOnboardDetails.getBankName());
			bankDetails.put("branchName", dsaOnboardDetails.getBranchName());
			bankDetails.put("accountholderName", dsaOnboardDetails.getAccountholderName());
			bankDetails.put("bankDetailsResponse", dsaOnboardDetails.getBankDetailsResponse());
			bankDetails.put("bankDetailsVerify",
					dsaOnboardDetails.getBankDetailsVerify() != null ? dsaOnboardDetails.getBankDetailsVerify() : "NO");
			Data.put("BankDetails", bankDetails);
			Data.put("dsaCode", dsaOnboardDetails.getDsaCode());
			org.json.simple.JSONObject addressMatch = new org.json.simple.JSONObject();
			addressMatch.put("officeAddressFromLatLong", "");
			addressMatch.put("addressMatch", "YES");
			Data.put("addressMatch", addressMatch);

			org.json.simple.JSONObject otherDetails = new org.json.simple.JSONObject();
			otherDetails.put("noOfYearsInBusiness", dsaOnboardDetails.getNoOfYearsInBusiness());
			otherDetails.put("noOfYearsInFinancialBusiness", dsaOnboardDetails.getNoOfYearsInFinancialBusiness());
			otherDetails.put("empanelledFiType", dsaOnboardDetails.getEmpanelledFiType());
			otherDetails.put("empanelledFiName", dsaOnboardDetails.getEmpanelledFiName());
			otherDetails.put("blacklisted", dsaOnboardDetails.getBlacklisted());
			otherDetails.put("ageOfProprietor", dsaOnboardDetails.getAgeOfProprietor());
//			if(dsaOnboardDetails.getBlacklisted().equalsIgnoreCase("YES")) {
//				String blacklistInfo = dsaOnboardDetails.getBlacklistInfo();
//				 ObjectMapper objectMapper = new ObjectMapper();
//				 try {
//				 List<BlackListInfo> reference = objectMapper.readValue(blacklistInfo, new TypeReference<List<BlackListInfo>>() {});
//				 otherDetails.put("BlacklistInfo", reference);
//				 } catch (Exception e) {
//			            e.printStackTrace();
//			        } 
//			}
			otherDetails.put("ageMoreThan70", dsaOnboardDetails.getAgeMoreThan70());
			Data.put("OtherDetails", otherDetails);
			if (dsaOnboardDetails.getBusinessReference() != null) {
				String businessReference = dsaOnboardDetails.getBusinessReference();
				org.json.JSONArray businessReferenceInArray = new org.json.JSONArray(businessReference);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<BusinessReference> reference = objectMapper.readValue(businessReference,
							new TypeReference<List<BusinessReference>>() {
							});
//				 if(request.equals("mob")) {
					JSONArray p = new JSONArray(reference);
					Data.put("BusinessReference", p);
//					 }else {
//						 Data.put("BusinessReference", reference);
//					 }

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dsaOnboardDetails.getRelationToSsfbStaff() != null) {
				String relationToSsfbStaff = dsaOnboardDetails.getRelationToSsfbStaff();
				org.json.JSONArray businessReferenceInArray = new org.json.JSONArray(relationToSsfbStaff);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<RelationToSsfbStaffResponse> reference = objectMapper.readValue(relationToSsfbStaff,
							new TypeReference<List<RelationToSsfbStaffResponse>>() {
							});
//				 if(request.equals("mob")) {
					JSONArray p = new JSONArray(reference);
					Data.put("RelationToSsfbStaff", p);
//					 }else {
//						 Data.put("RelationToSsfbStaff", reference);
//					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dsaOnboardDetails.getReferenceCheck() != null) {
				String referenceCheck = dsaOnboardDetails.getReferenceCheck();
				org.json.JSONArray businessReferenceInArray = new org.json.JSONArray(referenceCheck);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<ReferenceCheckResponse> reference = objectMapper.readValue(referenceCheck,
							new TypeReference<List<ReferenceCheckResponse>>() {
							});
//				 if(request.equals("mob")) {
					JSONArray p = new JSONArray(reference);
					Data.put("ReferenceCheck", p);
//					 }else {
//						 Data.put("ReferenceCheck", reference);
//					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dsaOnboardDetails.getSalesManagerFeedback() != null) {
				String salesManagerFeedback = dsaOnboardDetails.getSalesManagerFeedback();
				org.json.JSONObject salesManagerFeedbackObject = new org.json.JSONObject(salesManagerFeedback);
				org.json.simple.JSONObject salesManagerFeedbackResponse = new org.json.simple.JSONObject();
				salesManagerFeedbackResponse.put("placeOfVisit", salesManagerFeedbackObject.getString("placeOfVisit"));
				salesManagerFeedbackResponse.put("servicesOffered",
						salesManagerFeedbackObject.getString("servicesOffered"));
				salesManagerFeedbackResponse.put("recommend", salesManagerFeedbackObject.getString("recommend"));
				salesManagerFeedbackResponse.put("salesManagerRemarks",
						salesManagerFeedbackObject.getString("salesManagerRemarks"));
				Data.put("salesManagerFeedback", salesManagerFeedbackResponse);
			}
			if (dsaOnboardDetails.getCreditFeedback() != null) {
				String creditFeedback = dsaOnboardDetails.getCreditFeedback();
				org.json.JSONArray businessReferenceInArray = new org.json.JSONArray(creditFeedback);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<CreditFeedbackResponse> reference = objectMapper.readValue(creditFeedback,
							new TypeReference<List<CreditFeedbackResponse>>() {
							});
//				 if(request.equals("mob")) {
					JSONArray p = new JSONArray(reference);
					Data.put("creditFeedBack", p);
//					 }else {
//						 Data.put("creditFeedBack", reference);
//					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (request.equals("web")) {
				List<DsaImage> listOfImage = imageService.getByApplicationNo(applicationNo);
				org.json.simple.JSONArray images = new org.json.simple.JSONArray();
				if (listOfImage.size() != 0) {
					for (DsaImage aocpvImages : listOfImage) {
						String geoLocation = aocpvImages.getGeoLocation();
						org.json.JSONObject jsonObjectImage = new org.json.JSONObject(geoLocation);
						org.json.simple.JSONObject geolocation = new org.json.simple.JSONObject();
						org.json.simple.JSONObject image = new org.json.simple.JSONObject();
						geolocation.put("image", jsonObjectImage.getString("image"));
						geolocation.put("Lat", jsonObjectImage.getString("Lat"));
						geolocation.put("Long", jsonObjectImage.getString("Long"));
						geolocation.put("Address", jsonObjectImage.getString("Address"));
						geolocation.put("timestamp", jsonObjectImage.getString("timestamp"));
						image.put("geolocation", geolocation);
						image.put("documenttype", aocpvImages.getDocumenttype());
						image.put("imageName", aocpvImages.getImageName());
						image.put("type", aocpvImages.getType());
						image.put("size", aocpvImages.getSize());
						image.put("member", aocpvImages.getMember());
						byte[] images2 = aocpvImages.getImages();
						String encoded = Base64.getEncoder().encodeToString(images2);
						image.put("image", encoded);

						images.add(image);
					}
					Data.put("Documents", images);
				}
				List<DsaUserLog> listOfUserlog = dsaUserLogService.getByApplicationNo(applicationno);
				JSONArray j = new JSONArray(listOfUserlog);
				Data.put("UserLog", j);

				DsaOnboardPayoutDetails dsaOnboardPayoutDetails = dsaPayoutDetailsService
						.getByApplicationNo(applicationNo);
				if (dsaOnboardPayoutDetails.getAgencyType() != null) {
					org.json.JSONObject s = new org.json.JSONObject(dsaOnboardPayoutDetails);

					Data.put("payoutDetails", s);
				}

				if (dsaOnboardDetails.getCreditOpsFeedback() != null) {
					String creditOpsFeedback = dsaOnboardDetails.getCreditOpsFeedback();
					org.json.JSONArray businessReferenceInArray = new org.json.JSONArray(creditOpsFeedback);
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						List<CreditFeedbackResponse> reference = objectMapper.readValue(creditOpsFeedback,
								new TypeReference<List<CreditFeedbackResponse>>() {
								});
						JSONArray json = new JSONArray(reference);
						Data.put("creditOpsFeedback", j);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			return Data;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public void updateFlowStatus(long applicationNo, String flowStatus) {
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardDetails = optional.get();
			dsaOnboardDetails.setFlowStatus(flowStatus);
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			dsaOnBoardDetailsRepository.save(dsaOnboardDetails);
		}

	}

	@Override
	public String createLead(String mobileNo) {
//		String	applicationNo =(LocalDate.now().toString().replace("-", "")+"0001").substring(2, 12);
//		Optional<String> fetchLastApplicationNo = dsaOnBoardDetailsRepository.fetchLastApplicationNo();
//		if (fetchLastApplicationNo.isPresent()) {
//		
//			String application_No = fetchLastApplicationNo.get();
//			String dateInDB = application_No.substring(0,6);
//			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
//			if(currentDate.equals(dateInDB)) {
//				Long applicationno = Long.parseLong(application_No);
//				applicationno++;
//				applicationNo = applicationno.toString();
//				
//			}
//		}
		String applicationNo = LocalDateTime.now().toString().replace("-", "").replace("T", "").replace(":", "")
				.substring(2, 14);
		DsaOnboardDetails dsaOnboardDetails = new DsaOnboardDetails();
		dsaOnboardDetails.setApplicationNo(Long.parseLong(applicationNo));
		dsaOnboardDetails.setMobileNo(mobileNo);
		dsaOnboardDetails.setMobileNoVerify(true);
		dsaOnboardDetails.setCreatedDate(LocalDateTime.now());
		dsaOnBoardDetailsRepository.save(dsaOnboardDetails);

		return applicationNo;
	}

	@Override
	public LoginResponse getRegistrationDetails(String applicationNo) {
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository
				.getByApplicationNo(Long.parseLong(applicationNo));
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardDetails = optional.get();
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setApplicationNo(dsaOnboardDetails.getApplicationNo());
			loginResponse.setEntity(dsaOnboardDetails.getEntity());
			;
			loginResponse.setEmailIdVerify(dsaOnboardDetails.isEmailIdVerify());
			loginResponse.setMobileNo(dsaOnboardDetails.getMobileNo());
			loginResponse.setMobileNoVerify(dsaOnboardDetails.isMobileNoVerify());
			loginResponse.setCreatedDate(dsaOnboardDetails.getCreatedDate());
			loginResponse.setUpdatedDate(dsaOnboardDetails.getUpdatedDate());
			loginResponse.setStatus(dsaOnboardDetails.getStatus());
			loginResponse.setFlowStatus(dsaOnboardDetails.getFlowStatus());
			loginResponse.setBranchId(dsaOnboardDetails.getBranchId());
			loginResponse.setLeadId(dsaOnboardDetails.getLeadId());
			loginResponse.setLeadName(dsaOnboardDetails.getLeadName());
			loginResponse.setTypeOfRelationship(dsaOnboardDetails.getTypeOfRelationship());
			if (dsaOnboardDetails.getProductType() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<String> reference = objectMapper.readValue(dsaOnboardDetails.getProductType(),
							new TypeReference<List<String>>() {
							});
					loginResponse.setProductType(reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			loginResponse.setNoOfPartner(dsaOnboardDetails.getNoOfPartner());
			loginResponse.setConstitutionType(dsaOnboardDetails.getConstitutionType());
			loginResponse.setCompanyName(dsaOnboardDetails.getCompanyName());
			loginResponse.setEmailId(dsaOnboardDetails.getEmailId());
			return loginResponse;

		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public EmpanellementCriteriaResponse getEmpanellementCriteria(String applicationNo) {
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository
				.getByApplicationNo(Long.parseLong(applicationNo));
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardDetails = optional.get();
			EmpanellementCriteriaResponse criteriaResponse = new EmpanellementCriteriaResponse();
			criteriaResponse.setApplicationNo(applicationNo);
			criteriaResponse.setNoOfYearsInBusiness(dsaOnboardDetails.getNoOfYearsInBusiness());
			criteriaResponse.setNoOfYearsInFinancialBusiness(dsaOnboardDetails.getNoOfYearsInFinancialBusiness());
			criteriaResponse.setEmpanelledFiName(dsaOnboardDetails.getEmpanelledFiName());
			criteriaResponse.setEmpanelledFiType(dsaOnboardDetails.getEmpanelledFiType());
			criteriaResponse.setBlacklisted(dsaOnboardDetails.getBlacklisted());
			criteriaResponse.setAgeOfProprietor(dsaOnboardDetails.getAgeOfProprietor());
			if (dsaOnboardDetails.getCfrReport() != null) {
				String cfrReport = dsaOnboardDetails.getCfrReport();
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<CfrReportResponse> reference = objectMapper.readValue(cfrReport,
							new TypeReference<List<CfrReportResponse>>() {
							});
					criteriaResponse.setCfrReport(reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dsaOnboardDetails.getAmlReport() != null) {
				String amlReport = dsaOnboardDetails.getAmlReport();
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					AmlReportResponse reference = objectMapper.readValue(amlReport,
							new TypeReference<AmlReportResponse>() {
							});
					criteriaResponse.setAmlReport(reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			criteriaResponse.setStatus(dsaOnboardDetails.getStatus());
			criteriaResponse.setFlowStatus(dsaOnboardDetails.getFlowStatus());
			criteriaResponse.setOtherServices(dsaOnboardDetails.getOtherServices());
			return criteriaResponse;
		}

		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<DsaOnboardResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId) {
		List<DsaOnboardResponse> list = dsaOnBoardDetailsRepository.fetchByDate(startdate, enddate, status, branchId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DsaOnboardResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate) {
		List<DsaOnboardResponse> list = dsaOnBoardDetailsRepository.fetchByDate(startdate, enddate);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DsaOnboardResponse> fetchByDateAndStatus(LocalDateTime startdate, LocalDateTime enddate,
			String status) {
		List<DsaOnboardResponse> list = dsaOnBoardDetailsRepository.fetchByDateAndStatus(startdate, enddate, status);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DsaOnboardResponse> fetchByDateAndBranch(LocalDateTime startdate, LocalDateTime enddate,
			String branchId) {
		List<DsaOnboardResponse> list = dsaOnBoardDetailsRepository.fetchByDateAndBranch(startdate, enddate, branchId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DsaOnboardResponse> getByApplication(String applicationNo, String mobileNo, String emailId) {
		List<DsaOnboardResponse> list = null;
		if (applicationNo.equals("") && mobileNo.equals("")) {
			list = dsaOnBoardDetailsRepository.getByEmailId(emailId);
		} else if (applicationNo.equals("") && emailId.equals("")) {
			list = dsaOnBoardDetailsRepository.getMobileNo(mobileNo);
		} else if (emailId.equals("") && mobileNo.equals("")) {
			list = dsaOnBoardDetailsRepository.getByApplication(Long.parseLong(applicationNo));
		} else {
			throw new NoSuchElementException("No Record Found");
		}
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public BankDetailsResponse getBankDetails(String applicationNo) {
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository
				.getByApplicationNo(Long.parseLong(applicationNo));
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardResponse = optional.get();
			BankDetailsResponse bankDetailsResponse = new BankDetailsResponse();
			bankDetailsResponse.setApplicationNo(dsaOnboardResponse.getApplicationNo());
			bankDetailsResponse.setIfscCode(dsaOnboardResponse.getIfscCode());
			bankDetailsResponse.setBankAccountNo(dsaOnboardResponse.getBankAccountNo());
			bankDetailsResponse.setBankName(dsaOnboardResponse.getBankName());
			bankDetailsResponse.setBranchName(dsaOnboardResponse.getBranchName());
			bankDetailsResponse.setAccountholderName(dsaOnboardResponse.getAccountholderName());
			bankDetailsResponse.setUpdatedDate(dsaOnboardResponse.getUpdatedDate());
			bankDetailsResponse.setFlowStatus(dsaOnboardResponse.getFlowStatus());
			bankDetailsResponse.setStatus(dsaOnboardResponse.getStatus());
			bankDetailsResponse.setBankDetailsVerify(
					dsaOnboardResponse.getBankDetailsVerify() != null ? dsaOnboardResponse.getBankDetailsVerify()
							: "NO");
			return bankDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public void validateMobileNo(String mobile) {
		List<DsaOnboardDetails> list = dsaOnBoardDetailsRepository.validateMobileNo(mobile);
		if (list.size() != 0) {
			throw new DuplicateEntryException("MobileNo already present");
		}
	}

	@Override
	public String createlead(org.json.JSONObject jsonObject) {

		String emailId = jsonObject.getJSONObject("Data").getString("EmailID");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String leadId = jsonObject.getJSONObject("Data").getString("leadId");
		String leadName = jsonObject.getJSONObject("Data").getString("leadName");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String companyName = jsonObject.getJSONObject("Data").getString("CompanyName");

		String applicationNo = LocalDateTime.now().toString().replace("-", "").replace("T", "").replace(":", "")
				.substring(2, 14);
		List<DsaOnboardDetails> list = dsaOnBoardDetailsRepository.validateMobileNo(mobileNo);

		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String substring = uuid.substring(0, Math.min(5, uuid.length()));
		long currentTimeMillis = System.currentTimeMillis() % 1000;
		String regiCode = substring + currentTimeMillis;
		if (list.size() == 0) {
			DsaOnboardDetails dsaOnboardDetails = new DsaOnboardDetails();
			dsaOnboardDetails.setApplicationNo(Long.parseLong(applicationNo));
			dsaOnboardDetails.setMobileNo(mobileNo);
			dsaOnboardDetails.setLeadId(leadId);
			dsaOnboardDetails.setBranchId(branchId);
			dsaOnboardDetails.setLeadName(leadName);
			dsaOnboardDetails.setEmailId(emailId);
			dsaOnboardDetails.setCompanyName(companyName);
			dsaOnboardDetails.setCreatedDate(LocalDateTime.now());
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			dsaOnboardDetails.setRegiCode(regiCode);
			dsaOnboardDetails.setStatus("INITIATED");
			logger.debug("dsaOnboardDetails" + dsaOnboardDetails);
			dsaOnBoardDetailsRepository.save(dsaOnboardDetails);
			dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "INITIATED", leadId, "INITIATED");
		} else {
			DsaOnboardDetails dsaOnboardDetails = list.get(0);
			regiCode = dsaOnboardDetails.getRegiCode();
		}
		logger.debug("regiCode" + regiCode);
		return regiCode;
	}

	@Override
	public DsaOnboardDetails getByregiCode(String regicode) {
		Optional<DsaOnboardDetails> optional = dsaOnBoardDetailsRepository.getByRegiCode(regicode);
		if (optional.isPresent()) {
			DsaOnboardDetails dsaOnboardDetails = optional.get();
			if (dsaOnboardDetails.getEntity() == null) {
				return dsaOnboardDetails;
			}
			throw new NoSuchElementException("Application number - " + dsaOnboardDetails.getApplicationNo()
					+ " has been registered successfully !!");
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public String getlastDsaCode(String code) {
		String newCode = code.substring(0, 3);
		List<String> list = dsaOnBoardDetailsRepository.getlastDsaCode(newCode);
		String dsaCode = "";
		if (list.size() == 0) {
			dsaCode = code + "00001";
		} else {
			try {
				String codeString = list.get(0).substring(3);
				long newCodeLong = Long.parseLong(newCode);
				newCodeLong++;
				dsaCode = newCode + newCodeLong;
			} catch (Exception e) {
				dsaCode = code + "00001";
			}

		}
		return dsaCode;
	}

	@Override
	public List<DsaOnboardResponse> customSearch(String customSearch, String branchId) {
//		List<DsaOnboardResponse> list = dsaOnBoardDetailsRepository.getByName(customSearch);
//		if (list.size() == 0) {
		List<DsaOnboardResponse> list1 = dsaOnBoardDetailsRepository.getByAppID(customSearch, branchId);
		if (list1.size() == 0) {
			List<DsaOnboardResponse> list2 = dsaOnBoardDetailsRepository.getByMob(customSearch, branchId);
			if (list2.size() == 0) {
				throw new NoSuchElementException("No Record Found");
			} else {
				return list2;
			}
		} else {
			return list1;
		}
//		} else {
//			return list;
//		}
	}

	@Override
	public List<String> getSchemeCode(List<String> list) {
		List<String> schemeList = masterRepository.getSchemeCode(list);
		if (schemeList.size() == 0) {
			throw new NoSuchElementException("No Scheme Found");
		}
		return schemeList;
	}

}
