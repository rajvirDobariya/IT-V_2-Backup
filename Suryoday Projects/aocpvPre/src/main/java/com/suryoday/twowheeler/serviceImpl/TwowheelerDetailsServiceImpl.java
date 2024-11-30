package com.suryoday.twowheeler.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.FinalSaction;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.PreApprovedListTwoWheeler;
import com.suryoday.aocpv.pojo.RemarkResponse;
import com.suryoday.aocpv.service.GetSavingDetailsService;
import com.suryoday.twowheeler.pojo.AdditionalInfoResponse;
import com.suryoday.twowheeler.pojo.Address;
import com.suryoday.twowheeler.pojo.BsrDetailsResponse;
import com.suryoday.twowheeler.pojo.CatCompanies;
import com.suryoday.twowheeler.pojo.DisbustmentDetailsResponse;
import com.suryoday.twowheeler.pojo.EkycPoaResponse;
import com.suryoday.twowheeler.pojo.GuardianDetails;
import com.suryoday.twowheeler.pojo.LoanCharges;
import com.suryoday.twowheeler.pojo.LoanDetailsResponse;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerAssetDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerImage;
import com.suryoday.twowheeler.pojo.TwowheelerPDResponse;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.pojo.UtilityBillResponse;
import com.suryoday.twowheeler.repository.CatCompaniesRepository;
import com.suryoday.twowheeler.repository.TwowheelerDetailsRepository;
import com.suryoday.twowheeler.service.PreApprovalListService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;

@Service
public class TwowheelerDetailsServiceImpl implements TwowheelerDetailsService {

	@Autowired
	TwowheelerDetailsRepository twowheelerDetailsRepository;

	@Autowired
	TwowheelerImageService imageService;

	@Autowired
	GetSavingDetailsService getSavingDetailsService;
	
	@Autowired
	TwowheelerFamilyMemberService familyMemberService;
	
	@Autowired
	CatCompaniesRepository catCompaniesRepository;
	
	@Autowired
	PreApprovalListService loanInputService;
	
	private static Logger logger = LoggerFactory.getLogger(TwowheelerDetailsServiceImpl.class);

	@Override
	public String saveData(TwowheelerDetailesTable twowheelerDetails) {
		twowheelerDetailsRepository.save(twowheelerDetails);
		return "data updated";
	}

	@Override
	public TwowheelerDetailsResponse getByApplicationNo(String applicationNo, String api) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			TwowheelerDetailsResponse twowheelerDetailsResponse = new TwowheelerDetailsResponse();
			
			String address = twowheelerDetailesTable.getAddress();
			List<Address> listAddress = new ArrayList<>();
			if (address != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(address);
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					String address_Line1 = addressInJson.getString("address_Line1");
					String address_Line2 = addressInJson.getString("address_Line2");
					String address_Line3 = addressInJson.getString("address_Line3");
					String city = addressInJson.getString("city");
					String pincode = addressInJson.getString("pincode");
					String country = addressInJson.getString("country");
					String district = addressInJson.getString("district");
					String state = addressInJson.getString("state");
					String landmark = addressInJson.getString("landmark");
					Address addressConstructor = new Address(address_Line1, address_Line2, address_Line3, state, city,
							district, pincode, landmark, addressType);
					listAddress.add(addressConstructor);
				}
			}
			twowheelerDetailsResponse.setApplicationNo(twowheelerDetailesTable.getApplicationNo());
			twowheelerDetailsResponse.setAppNoWithProductCode(twowheelerDetailesTable.getAppNoWithProductCode());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
			twowheelerDetailsResponse.setCreatedTimestamp(dtf.format(twowheelerDetailesTable.getCreatedTimestamp()));
			twowheelerDetailsResponse.setUpdatedTimestamp(dtf.format(twowheelerDetailesTable.getUpdatedTimestamp()));
			twowheelerDetailsResponse.setSalesCreatedBy(twowheelerDetailesTable.getSalesCreatedBy());
			twowheelerDetailsResponse.setSalesBranchId(twowheelerDetailesTable.getSalesBranchId());
			twowheelerDetailsResponse.setStatus(twowheelerDetailesTable.getStatus());
			twowheelerDetailsResponse.setPsdBranchId(twowheelerDetailesTable.getPsdBranchId());
			twowheelerDetailsResponse.setPsdCreatedBy(twowheelerDetailesTable.getPsdCreatedBy());
			twowheelerDetailsResponse.setDealerName(twowheelerDetailesTable.getDealerName());
			twowheelerDetailsResponse.setCustomerId(twowheelerDetailesTable.getCustomerId());
			twowheelerDetailsResponse.setName(twowheelerDetailesTable.getName());
			twowheelerDetailsResponse.setMobileNo(twowheelerDetailesTable.getMobileNo());
			twowheelerDetailsResponse.setPreApprovalAmount(twowheelerDetailesTable.getPreApprovalAmount());
			twowheelerDetailsResponse.setScheme(twowheelerDetailesTable.getScheme());
			twowheelerDetailsResponse.setManufacture(twowheelerDetailesTable.getManufacture());
			twowheelerDetailsResponse.setModel(twowheelerDetailesTable.getModel());
			twowheelerDetailsResponse.setVariant(twowheelerDetailesTable.getVariant());
			twowheelerDetailsResponse.setDealerLocation(twowheelerDetailesTable.getDealerLocation());
			twowheelerDetailsResponse.setExShowroomPrice(twowheelerDetailesTable.getExShowroomPrice());
			twowheelerDetailsResponse.setRoadTax(twowheelerDetailesTable.getRoadTax());
			twowheelerDetailsResponse.setRegistrationCharges(twowheelerDetailesTable.getRegistrationCharges());
			twowheelerDetailsResponse.setAccessories(twowheelerDetailesTable.getAccessories());
			twowheelerDetailsResponse.setAddonsCharges(twowheelerDetailesTable.getAddonsCharges());
			twowheelerDetailsResponse.setTotalOnRoadPrice(twowheelerDetailesTable.getTotalOnRoadPrice());
			twowheelerDetailsResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			twowheelerDetailsResponse.setAadharNo(twowheelerDetailesTable.getAadharNo());
			twowheelerDetailsResponse.setAadharNoVerify(twowheelerDetailesTable.getAadharNoVerify());
			twowheelerDetailsResponse.setPancard(twowheelerDetailesTable.getPancard());
			twowheelerDetailsResponse.setPancardVerify(twowheelerDetailesTable.getPancardVerify());
			twowheelerDetailsResponse.setVoterId(twowheelerDetailesTable.getVoterId());
			twowheelerDetailsResponse.setVoterIdVerify(twowheelerDetailesTable.getVoterIdVerify());
			twowheelerDetailsResponse.setMobileNoVerify(twowheelerDetailesTable.getMobileNoVerify());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setDateOfBirth(twowheelerDetailesTable.getDateOfBirth());
			twowheelerDetailsResponse.setMarginMoney(twowheelerDetailesTable.getMarginMoney());
			twowheelerDetailsResponse.setAmount(twowheelerDetailesTable.getAmount());
			twowheelerDetailsResponse.setRateOfInterest(twowheelerDetailesTable.getRateOfInterest());
			twowheelerDetailsResponse.setTenure(twowheelerDetailesTable.getTenure());
			twowheelerDetailsResponse.setEmi(twowheelerDetailesTable.getEmi());
			twowheelerDetailsResponse.setInsuranceEmi(twowheelerDetailesTable.getInsuranceEmi());
			twowheelerDetailsResponse.setChasisNumber(twowheelerDetailesTable.getChasisNumber());
			twowheelerDetailsResponse.setEngineNumber(twowheelerDetailesTable.getEngineNumber());
			twowheelerDetailsResponse.setEkycVerify(twowheelerDetailesTable.getEkycVerify());
			twowheelerDetailsResponse.setEkycDoneBy(twowheelerDetailesTable.getEkycDoneBy());
			twowheelerDetailsResponse.setBeneficiaryName(twowheelerDetailesTable.getBeneficiaryName());
			twowheelerDetailsResponse.setBeneficiaryAccountNo(twowheelerDetailesTable.getBeneficiaryAccountNo());
			twowheelerDetailsResponse.setDealerCode(twowheelerDetailesTable.getDealerCode());
			twowheelerDetailsResponse.setAccountName(twowheelerDetailesTable.getAccountName());
			twowheelerDetailsResponse.setAccountNumber(twowheelerDetailesTable.getAccountNumber());
			twowheelerDetailsResponse.setAccountBranchId(twowheelerDetailesTable.getAccountBranchId());
			twowheelerDetailsResponse.setTotaldeductionAmount(twowheelerDetailesTable.getTotaldeductionAmount());
			twowheelerDetailsResponse.setFinaldisbustmentAmount(twowheelerDetailesTable.getFinaldisbustmentAmount());
			twowheelerDetailsResponse.setTenureMin(twowheelerDetailesTable.getTenureMin());
			twowheelerDetailsResponse.setTenureMax(twowheelerDetailesTable.getTenureMax());
			twowheelerDetailsResponse.setListType(twowheelerDetailesTable.getListType());
		
			twowheelerDetailsResponse.setAddress(listAddress);
			if(api.equals("web")) {
				List<TwowheelerImage> listImage = imageService.getByApplicationNoAndDocument(applicationNo,
						"customerPhoto");
				List<Image> listOfImages = new ArrayList<>();
				for (TwowheelerImage aocpvImages : listImage) {

					String geoLocation = aocpvImages.getGeoLocation();

					JSONObject jsonObjectImage = new JSONObject(geoLocation);

					String pimage = jsonObjectImage.getString("image");
					String pLat = jsonObjectImage.getString("Lat");
					String pLong = jsonObjectImage.getString("Long");
					String pAddress = jsonObjectImage.getString("Address");
					String ptimestamp = jsonObjectImage.getString("timestamp");
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy
					// hh:mm:ss");
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
					// LocalDate timeStamp = LocalDate.parse(ptimestamp, formatter);
					GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

					String documenttype = aocpvImages.getDocumenttype();
					String imageName = aocpvImages.getImageName();
					String type = aocpvImages.getType();
					long size = aocpvImages.getSize();
					String member = aocpvImages.getMember();
					byte[] images2 = aocpvImages.getImages();
					String encoded = Base64.getEncoder().encodeToString(images2);

					Image images = new Image(documenttype, imageName, type, size, encoded, member, geolocation);

					listOfImages.add(images);

				}
				twowheelerDetailsResponse.setImages(listOfImages);
			}

			return twowheelerDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<TwowheelerResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDate(startdate, enddate, status, branchId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public TwowheelerDetailesTable getByApplication(String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<TwowheelerResponse> customSearch(String customSearch) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.getByName(customSearch);
		if (list.size() == 0) {
			List<TwowheelerResponse> list1 = twowheelerDetailsRepository.getByAppID(customSearch);
			if (list1.size() == 0) {
				List<TwowheelerResponse> list2 = twowheelerDetailsRepository.getByCustomerId(customSearch);
				if (list2.size() == 0) {
					PreApprovedListTwoWheeler fetchByCustomerId = loanInputService.getByReferenceNo(customSearch);
					TwowheelerResponse response=new TwowheelerResponse();
					response.setCustomerId(customSearch);
					response.setName(fetchByCustomerId.getMemberName());
					response.setStatus(fetchByCustomerId.getStatus());
					response.setSalesBranchId(Long.toString(fetchByCustomerId.getBranchId()));
					response.setPreApprovalAmount(Double.toString(fetchByCustomerId.getAmount()));
					response.setListType("PRE");
					response.setMobileNo(Long.toString(fetchByCustomerId.getMobilePhone()));
					List<TwowheelerResponse> list3=new ArrayList<>();
					list3.add(response);
					return list3;
				} else {
					return list2;
				}
			} else {
				return list1;
			}
		} else {
			return list;
		}
	}

	@Override
	public String fetchAddress(String applicationNo) {
		Optional<String> optional = twowheelerDetailsRepository.fetchAddress(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("Please Add Address");
	}
	
	@Override
	public String createApplicationNo(String mobileNo, String product, String branchId, String listType, String customerId, String x_User_ID) {

		String applicationNo = (LocalDate.now().toString().replace("-", "") + "0001").substring(2, 12);

		Optional<String> fetchLastApplicationNo = twowheelerDetailsRepository.fetchLastApplicationNo();

		if (fetchLastApplicationNo.isPresent()) {
			logger.debug("If ApplicationNo is Present");

			String application_No = fetchLastApplicationNo.get();
			logger.debug(application_No);
			String dateInDB = application_No.substring(0, 6);
			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
			if (currentDate.equals(dateInDB)) {
				logger.debug("If current Date  is equal to db date");
				Long applicationno = Long.parseLong(application_No);
				applicationno++;
				applicationNo = applicationno.toString();
				logger.debug(applicationNo + "after increment");
			}

//			Long application = Long.parseLong(application_No);
//			application++;
//			applicationNo = application.toString();
		}
		TwowheelerDetailesTable twowheelerDetailesTable = new TwowheelerDetailesTable();
		twowheelerDetailesTable.setApplicationNo(applicationNo);
		LocalDateTime dateTime = LocalDateTime.now();
		twowheelerDetailesTable.setCreatedTimestamp(dateTime);
		twowheelerDetailesTable.setUpdatedTimestamp(dateTime);
		twowheelerDetailesTable.setMobileNo(mobileNo);
		twowheelerDetailesTable.setAppNoWithProductCode(product + "-" + applicationNo);
		twowheelerDetailesTable.setMobileNoVerify("YES");
		twowheelerDetailesTable.setEkycVerify("NO");
		twowheelerDetailesTable.setStatus("PROGRESS");
		twowheelerDetailesTable.setFlowStatus("NEWLEAD");
		twowheelerDetailesTable.setStage("SALE");
		twowheelerDetailesTable.setSalesBranchId(branchId);
		twowheelerDetailesTable.setSalesCreatedBy(x_User_ID);
		twowheelerDetailesTable.setListType(listType);
		twowheelerDetailesTable.setEkycVerify("NO");
		if(listType.equalsIgnoreCase("PRE")) {
			twowheelerDetailesTable.setCustomerId(customerId);
				PreApprovedListTwoWheeler loanDetails = loanInputService.getByReferenceNo(customerId);
				loanDetails.setStatus("PROGRESS");
				twowheelerDetailesTable.setPreApprovalAmount(Double.toString(loanDetails.getAmount()));
				loanInputService.saveSingleData(loanDetails);
			
		}
		
		Optional<String> optional = twowheelerDetailsRepository.fetchByapplicationId(applicationNo);
		if (optional.isPresent()) {
			logger.debug("If ApplicationNo is Present last stage");
			Long applicationno = Long.parseLong(applicationNo);
			applicationno++;
			applicationNo = applicationno.toString();
			logger.debug(applicationNo + "after increment last ");
			twowheelerDetailesTable.setApplicationNo(applicationNo);
		}
		twowheelerDetailsRepository.save(twowheelerDetailesTable);
		return applicationNo;
	}

	@Override
	public void saveResponse(String proof, String proofId, String applicationNo, String response) {
		
		Optional<TwowheelerDetailesTable> findByApplicationId = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (findByApplicationId.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = findByApplicationId.get();
			

			if (proof.equalsIgnoreCase("panCard")) {
				twowheelerDetailesTable.setPancard(proofId);
				twowheelerDetailesTable.setPancardVerify("YES");
				twowheelerDetailesTable.setPanResponse(response);

			} else if (proof.equalsIgnoreCase("voterId")) {
				twowheelerDetailesTable.setVoterId(proofId);
				twowheelerDetailesTable.setVoterIdVerify("YES");
				
//			} else if (proof.equalsIgnoreCase("drivingLicense")) {
//				twowheelerDetailesTable.setDrivingLicense(proofId);
//				twowheelerDetailesTable.setDrivingLicenseIsVerify("YES");
//				twowheelerDetailesTable.setDrivingLicenseResponse(response);
//			} else if (proof.equalsIgnoreCase("Passport")) {
//				twowheelerDetailesTable.setPassport(proofId);
//				twowheelerDetailesTable.setPassportIsVerify("YES");
//				twowheelerDetailesTable.setPassportResponse(response);
			} else if (proof.equalsIgnoreCase("ekyc")) {
				twowheelerDetailesTable.setEkycResponse(response);
				twowheelerDetailesTable.setEkycVerify("YES");
				twowheelerDetailesTable.setEkycDoneBy(proofId);
	//			 org.json.JSONObject ekyc=new org.json.JSONObject(response);
	//	 			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
	//	 			applicationDetails.setName(PoiResponse.getString("name"));
			} else if (proof.equalsIgnoreCase("aadharCard")) {
				JSONObject jsonObject = new JSONObject(response);
				Long referenceNo1 = jsonObject.getJSONArray("Data").getJSONObject(0).getLong("ReferenceNumber");
				String referenceNo = referenceNo1.toString();
				twowheelerDetailesTable.setAadharNo(proofId);
				twowheelerDetailesTable.setAadharNoVerify("YES");
			}
			LocalDateTime date = LocalDateTime.now();
			twowheelerDetailesTable.setUpdatedTimestamp(date);
			twowheelerDetailsRepository.save(twowheelerDetailesTable);
		} else {
			throw new NoSuchElementException("list is empty");
		}
		
	}

	@Override
	public TwowheelerPDResponse fetchPdData(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			TwowheelerPDResponse twowheelerDetailsResponse = new TwowheelerPDResponse();
			twowheelerDetailsResponse.setApplicationNo(twowheelerDetailesTable.getApplicationNo());
			twowheelerDetailsResponse.setAppNoWithProductCode(twowheelerDetailesTable.getAppNoWithProductCode());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
			twowheelerDetailsResponse.setCreatedTimestamp(dtf.format(twowheelerDetailesTable.getCreatedTimestamp()));
			twowheelerDetailsResponse.setUpdatedTimestamp(dtf.format(twowheelerDetailesTable.getUpdatedTimestamp()));
			twowheelerDetailsResponse.setSalesBranchId(twowheelerDetailesTable.getSalesBranchId());
			twowheelerDetailsResponse.setStatus(twowheelerDetailesTable.getStatus());
			twowheelerDetailsResponse.setCustomerId(twowheelerDetailesTable.getCustomerId());
			twowheelerDetailsResponse.setName(twowheelerDetailesTable.getName());
			twowheelerDetailsResponse.setMobileNo(twowheelerDetailesTable.getMobileNo());
			twowheelerDetailsResponse.setPreApprovalAmount(twowheelerDetailesTable.getPreApprovalAmount());
			twowheelerDetailsResponse.setAadharNo(twowheelerDetailesTable.getAadharNo());
			twowheelerDetailsResponse.setAadharNoVerify(twowheelerDetailesTable.getAadharNoVerify());
			twowheelerDetailsResponse.setPancard(twowheelerDetailesTable.getPancard());
			twowheelerDetailsResponse.setPancardVerify(twowheelerDetailesTable.getPancardVerify());
			twowheelerDetailsResponse.setVoterId(twowheelerDetailesTable.getVoterId());
			twowheelerDetailsResponse.setVoterIdVerify(twowheelerDetailesTable.getVoterIdVerify());
			twowheelerDetailsResponse.setMobileNoVerify(twowheelerDetailesTable.getMobileNoVerify());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setDateOfBirth(twowheelerDetailesTable.getDateOfBirth());
			twowheelerDetailsResponse.setForm60(twowheelerDetailesTable.getForm60());
			twowheelerDetailsResponse.setRequiredAmount(twowheelerDetailesTable.getRequiredAmount());
			twowheelerDetailsResponse.setSelectedIdentityProof(twowheelerDetailesTable.getSelectedIdentityProof());
			twowheelerDetailsResponse.setIdentityProof(twowheelerDetailesTable.getIdentityProof());
			twowheelerDetailsResponse.setIdentityProofVerify(twowheelerDetailesTable.getIdentityProofVerify());
			String address = twowheelerDetailesTable.getAddress();
			List<Address> listAddress = new ArrayList<>();
			if (address != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(address);
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					String address_Line1 = addressInJson.getString("address_Line1");
					String address_Line2 = addressInJson.getString("address_Line2");
					String address_Line3 = addressInJson.getString("address_Line3");
					String city = addressInJson.getString("city");
					String pincode = addressInJson.getString("pincode");
					String country = addressInJson.getString("country");
					String district = addressInJson.getString("district");
					String state = addressInJson.getString("state");
					String landmark = addressInJson.getString("landmark");
					Address addressConstructor = new Address(address_Line1, address_Line2, address_Line3, state, city,
							district, pincode, landmark, addressType);
					listAddress.add(addressConstructor);
				}
			}
			twowheelerDetailsResponse.setAddress(listAddress);
			List<TwowheelerImage> listImage = imageService.getByApplicationNoAndDocument(applicationNo,
					"customerPhoto");
			List<Image> listOfImages = new ArrayList<>();
			for (TwowheelerImage aocpvImages : listImage) {

				String geoLocation = aocpvImages.getGeoLocation();

				JSONObject jsonObjectImage = new JSONObject(geoLocation);

				String pimage = jsonObjectImage.getString("image");
				String pLat = jsonObjectImage.getString("Lat");
				String pLong = jsonObjectImage.getString("Long");
				String pAddress = jsonObjectImage.getString("Address");
				String ptimestamp = jsonObjectImage.getString("timestamp");
				// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy
				// hh:mm:ss");
				// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
				// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
				// LocalDate timeStamp = LocalDate.parse(ptimestamp, formatter);
				GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

				String documenttype = aocpvImages.getDocumenttype();
				String imageName = aocpvImages.getImageName();
				String type = aocpvImages.getType();
				long size = aocpvImages.getSize();
				String member = aocpvImages.getMember();
				byte[] images2 = aocpvImages.getImages();
				String encoded = Base64.getEncoder().encodeToString(images2);

				Image images = new Image(documenttype, imageName, type, size, encoded, member, geolocation);

				listOfImages.add(images);

			}
			twowheelerDetailsResponse.setImages(listOfImages);
			
			TwoWheelerFamilyMember member = familyMemberService.fetchByApplicationNoAndMember(applicationNo, "APPLICANT");
			if(member.getEkycVerify() != null && member.getEkycVerify().equalsIgnoreCase("YES")) {
				JSONObject ekyc=new JSONObject(member.getEkycResponse());
				JSONObject PoaResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poa");
				EkycPoaResponse ekycPoa=new EkycPoaResponse();
				if(PoaResponse.has("country")) {
					ekycPoa.setCountry(PoaResponse.getString("country"));
				}if(PoaResponse.has("lm")) {
					ekycPoa.setLm(PoaResponse.getString("lm"));
				}if(PoaResponse.has("loc")) {
					ekycPoa.setLoc(PoaResponse.getString("loc"));
				}if(PoaResponse.has("pc")) {
					ekycPoa.setPc(PoaResponse.getInt("pc"));
				}if(PoaResponse.has("vtc")) {
					ekycPoa.setVtc(PoaResponse.getString("vtc"));
				}if(PoaResponse.has("street")) {
					ekycPoa.setStreet(PoaResponse.getString("street"));
				}if(PoaResponse.has("dist")) {
					ekycPoa.setDist(PoaResponse.getString("dist"));
				}if(PoaResponse.has("state")) {
					ekycPoa.setState(PoaResponse.getString("state"));
				}if(PoaResponse.has("co")) {
					ekycPoa.setCo(PoaResponse.getString("co"));
				}if(PoaResponse.has("house")) {
					ekycPoa.setHouse(PoaResponse.getString("house"));
				}
				twowheelerDetailsResponse.setEkycResponse(ekycPoa);
				twowheelerDetailsResponse.setEkycVerify("YES");
			}
			twowheelerDetailsResponse.setEkycVerify(member.getEkycVerify());
			twowheelerDetailsResponse.setMarried(member.getMarried());
			twowheelerDetailsResponse.setGender(member.getGender());
			twowheelerDetailsResponse.setEducation(member.getEducation());
			twowheelerDetailsResponse.setOccupation(member.getOccupation());
			twowheelerDetailsResponse.setPancard(member.getPanCard());
			twowheelerDetailsResponse.setPancardVerify(member.getPancardNoVerify());
			twowheelerDetailsResponse.setNameOnPan(member.getNameOnPan());
			twowheelerDetailsResponse.setDobOnPancard(member.getDobOnPancard());
			twowheelerDetailsResponse.setVoterId(member.getVoterId());
			return twowheelerDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
		
	}

	@Override
	public TwowheelerAssetDetailsResponse fetchASSETData(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			TwowheelerAssetDetailsResponse twowheelerDetailsResponse = new TwowheelerAssetDetailsResponse();
			twowheelerDetailsResponse.setApplicationNo(twowheelerDetailesTable.getApplicationNo());
			twowheelerDetailsResponse.setAppNoWithProductCode(twowheelerDetailesTable.getAppNoWithProductCode());
			twowheelerDetailsResponse.setStatus(twowheelerDetailesTable.getStatus());
			twowheelerDetailsResponse.setDealerName(twowheelerDetailesTable.getDealerName());
			twowheelerDetailsResponse.setManufacture(twowheelerDetailesTable.getManufacture());
			twowheelerDetailsResponse.setModel(twowheelerDetailesTable.getModel());
			twowheelerDetailsResponse.setVariant(twowheelerDetailesTable.getVariant());
			twowheelerDetailsResponse.setDealerLocation(twowheelerDetailesTable.getDealerLocation());
			twowheelerDetailsResponse.setExShowroomPrice(twowheelerDetailesTable.getExShowroomPrice());
			twowheelerDetailsResponse.setRoadTax(twowheelerDetailesTable.getRoadTax());
			twowheelerDetailsResponse.setRegistrationCharges(twowheelerDetailesTable.getRegistrationCharges());
			twowheelerDetailsResponse.setAccessories(twowheelerDetailesTable.getAccessories());
			twowheelerDetailsResponse.setAddonsCharges(twowheelerDetailesTable.getAddonsCharges());
			twowheelerDetailsResponse.setTotalOnRoadPriceUser(twowheelerDetailesTable.getTotalOnRoadPrice());
			twowheelerDetailsResponse.setTotalOnRoadPriceAllocated(twowheelerDetailesTable.getTotalOnRoadPriceAllocated());
			twowheelerDetailsResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setChasisNumber(twowheelerDetailesTable.getChasisNumber());
			twowheelerDetailsResponse.setEngineNumber(twowheelerDetailesTable.getEngineNumber());
			twowheelerDetailsResponse.setDealerCode(twowheelerDetailesTable.getDealerCode());
			twowheelerDetailsResponse.setYearOfManufacturer(twowheelerDetailesTable.getYearOfManufacturer());
			return twowheelerDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public LoanDetailsResponse fetchLoanDetails(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			LoanDetailsResponse twowheelerDetailsResponse = new LoanDetailsResponse();
			twowheelerDetailsResponse.setApplicationNo(twowheelerDetailesTable.getApplicationNo());
			twowheelerDetailsResponse.setAppNoWithProductCode(twowheelerDetailesTable.getAppNoWithProductCode());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
			twowheelerDetailsResponse.setStatus(twowheelerDetailesTable.getStatus());
			twowheelerDetailsResponse.setScheme(twowheelerDetailesTable.getScheme());
			twowheelerDetailsResponse.setTotalOnRoadPrice(twowheelerDetailesTable.getTotalOnRoadPrice());
			twowheelerDetailsResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setMarginMoney(twowheelerDetailesTable.getMarginMoney());
			twowheelerDetailsResponse.setAmount(twowheelerDetailesTable.getAmount());
			twowheelerDetailsResponse.setRateOfInterest(twowheelerDetailesTable.getRateOfInterest());
			twowheelerDetailsResponse.setTenure(twowheelerDetailesTable.getTenure());
			twowheelerDetailsResponse.setEmi(twowheelerDetailesTable.getEmi());
			twowheelerDetailsResponse.setInsuranceEmi(twowheelerDetailesTable.getInsuranceEmi());
			twowheelerDetailsResponse.setPreApprovalAmount(twowheelerDetailesTable.getPreApprovalAmount());
			twowheelerDetailsResponse.setRequiredAmount(twowheelerDetailesTable.getRequiredAmount());
			twowheelerDetailsResponse.setTenureMin(twowheelerDetailesTable.getTenureMin());
			twowheelerDetailsResponse.setTenureMax(twowheelerDetailesTable.getTenureMax());
			if(twowheelerDetailesTable.getLoanCharges() !=null) {
				List<LoanCharges> list=new ArrayList<>();
				org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
				for(int n=0;n<loanCarges.length();n++) {
					JSONObject jsonObject = loanCarges.getJSONObject(n);
					LoanCharges loanCharges=new LoanCharges(jsonObject.getString("chargeName"), jsonObject.getString("chargeAmount"), jsonObject.getString("taxAmount"), jsonObject.getString("totalAmount"), jsonObject.getString("type"));
					list.add(loanCharges);
				}
				twowheelerDetailsResponse.setLoanCharges(list);
			}
			return twowheelerDetailsResponse;	
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public TwowheelerDetailesTable getCustomerDetails(TwowheelerDetailesTable twowheelerDetails) {
		
		JSONObject Header= new JSONObject();
		 Header.put("X-From-ID","TAB" );
		 Header.put("X-To-ID","Others" );
		 Header.put("X-Transaction-ID","EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4" );
		 Header.put("X-User-ID","" );
		 Header.put("X-Request-ID","TAB" );
		 String AccountId="";
		 String AcctName="";
		 String customerId = twowheelerDetails.getCustomerId();
		 if(!customerId.equals("0")) {
			 JSONObject   data= new JSONObject();
			 data.put("MobileNo", "");
			 data.put("AadhaarNo", "");
			 data.put("AadhaarReferenceNo", "");
			 data.put("PanNo", "");
			 data.put("CustomerNo",customerId);
			 data.put("BranchCode", "");
			 data.put("ProductGroup", "CASA");
			 
			 JSONObject  request= new JSONObject();
			 request.put("Data", data);
			 
			 JSONObject getdetails= getSavingDetailsService.getDetails(request, Header);
			 System.out.println(request.toString());
			 HttpStatus  h=HttpStatus.BAD_GATEWAY;
			System.out.println("finacalResponse"+getdetails.toString());
			 if(getdetails!=null) {
				 String Data2= getdetails.getString("data");
				 JSONObject Data1= new JSONObject(Data2);
				 if(Data1.getJSONObject("Data").has("AccountDetails")) {
					 JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("AccountDetails");
					 for(int n=0;n<jsonArray.length();n++) {
					 JSONObject jsonObject2 = jsonArray.getJSONObject(n);
					 JSONObject jsonObject = jsonObject2.getJSONObject("Product");
					 	String group = jsonObject.getString("Group");
					 	if(group.equals("SAV")) {
					 		AccountId = jsonObject2.getString("AccountId");
					 		AcctName = jsonObject.getString("AcctName");	
					 	}
					 } 
				 }	 	  
			 }
		 }
		 twowheelerDetails.setAccountName(AcctName);
		 twowheelerDetails.setAccountNumber(AccountId);
		return twowheelerDetails;
	}

	@Override
	public TwowheelerDetailesTable getByAppNo(String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return new TwowheelerDetailesTable();
	}

	@Override
	public DisbustmentDetailsResponse fetchDisbustmentDetails(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			DisbustmentDetailsResponse twowheelerDetailsResponse = new DisbustmentDetailsResponse();
			twowheelerDetailsResponse.setApplicationNo(twowheelerDetailesTable.getApplicationNo());
			twowheelerDetailsResponse.setAppNoWithProductCode(twowheelerDetailesTable.getAppNoWithProductCode());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
			twowheelerDetailsResponse.setUpdatedTimestamp(dtf.format(twowheelerDetailesTable.getUpdatedTimestamp()));
			twowheelerDetailsResponse.setStatus(twowheelerDetailesTable.getStatus());
			twowheelerDetailsResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setAmount(twowheelerDetailesTable.getAmount());
			twowheelerDetailsResponse.setBeneficiaryName(twowheelerDetailesTable.getBeneficiaryName());
			twowheelerDetailsResponse.setBeneficiaryAccountNo(twowheelerDetailesTable.getBeneficiaryAccountNo());
			twowheelerDetailsResponse.setBeneficiaryIFSC(twowheelerDetailesTable.getBeneficiaryIFSC());
			twowheelerDetailsResponse.setAccountName(twowheelerDetailesTable.getAccountName());
			twowheelerDetailsResponse.setAccountNumber(twowheelerDetailesTable.getAccountNumber());
			twowheelerDetailsResponse.setAccountBranchId(twowheelerDetailesTable.getAccountBranchId());
			twowheelerDetailsResponse.setTotaldeductionAmount(twowheelerDetailesTable.getTotaldeductionAmount());
			twowheelerDetailsResponse.setFinaldisbustmentAmount(twowheelerDetailesTable.getFinaldisbustmentAmount());
			String processingFee="";
			String documentationCharge="";
			if(twowheelerDetailesTable.getLoanCharges() !=null) {
	            org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
	            for(int n=0;n<loanCarges.length();n++) {
	                JSONObject jsonObject = loanCarges.getJSONObject(n);
	                if(jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee"))
	                {
	                	processingFee=jsonObject.getString("totalAmount");
	                }
	                if(jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge"))
	                {
	                	documentationCharge=jsonObject.getString("totalAmount");
	                }
	            }
	        }
			twowheelerDetailsResponse.setProcessingFee(processingFee);
			twowheelerDetailsResponse.setDocumentationCharge(documentationCharge);
			twowheelerDetailsResponse.setInsuranceEmi(twowheelerDetailesTable.getInsuranceEmi());
			
			return twowheelerDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
		
	}

	@Override
	public List<TwowheelerResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDate(startdate, enddate);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchByDateWithStatus(LocalDateTime startdate, LocalDateTime enddate,
			String status) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDateWithStatus(startdate, enddate, status);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchByDateWithBranchId(LocalDateTime startdate, LocalDateTime enddate,
			String branchId) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDateWithBranchId(startdate, enddate,branchId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchbyApplicationNo(String applicationNo) {
		List<TwowheelerResponse> list=twowheelerDetailsRepository.fetchbyApplicationNo(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchbyCustomerId(String customerId) {
		List<TwowheelerResponse> list=twowheelerDetailsRepository.fetchbyCustomerId(customerId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public org.json.simple.JSONObject getreviewData(String applicationNo, String api) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		TwoWheelerFamilyMember twoWheelerMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo,"APPLICANT");
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			
			org.json.simple.JSONObject   Data= new org.json.simple.JSONObject();
			org.json.simple.JSONObject   PersonalDetails= new org.json.simple.JSONObject();
			PersonalDetails.put("applicationNo",twowheelerDetailesTable.getApplicationNo());
			PersonalDetails.put("listType",twowheelerDetailesTable.getListType());
			PersonalDetails.put("customerId",twowheelerDetailesTable.getCustomerId());
			PersonalDetails.put("status",twowheelerDetailesTable.getStatus());
			PersonalDetails.put("name",twowheelerDetailesTable.getName());
			PersonalDetails.put("dateOfBirth",twowheelerDetailesTable.getDateOfBirth());
			PersonalDetails.put("mobileNo",twowheelerDetailesTable.getMobileNo());
			PersonalDetails.put("salesBranchId",twowheelerDetailesTable.getSalesBranchId());
			PersonalDetails.put("salesCreatedBy",twowheelerDetailesTable.getSalesCreatedBy());
			PersonalDetails.put("createdTimestamp",twowheelerDetailesTable.getCreatedTimestamp());
			PersonalDetails.put("occupation",twoWheelerMember.getOccupation());
			PersonalDetails.put("married",twoWheelerMember.getMarried());
			PersonalDetails.put("education",twoWheelerMember.getEducation());
			PersonalDetails.put("gender",twoWheelerMember.getGender());
			
			String address = twowheelerDetailesTable.getAddress();
			List<Address> listAddress = new ArrayList<>();
			if (address != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(address);
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					String address_Line1 = addressInJson.getString("address_Line1");
					String address_Line2 = addressInJson.getString("address_Line2");
					String address_Line3 = addressInJson.getString("address_Line3");
					String city = addressInJson.getString("city");
					String pincode = addressInJson.getString("pincode");
					String country = addressInJson.getString("country");
					String district = addressInJson.getString("district");
					String state = addressInJson.getString("state");
					String landmark = addressInJson.getString("landmark");
					Address addressConstructor = new Address(address_Line1, address_Line2, address_Line3, state, city,
							district, pincode, landmark, addressType);
					listAddress.add(addressConstructor);
				}
			}
			PersonalDetails.put("address",listAddress);
			Data.put("PersonalDetails", PersonalDetails);
			org.json.simple.JSONObject   AssetDetails= new org.json.simple.JSONObject();
			AssetDetails.put("manufacture",twowheelerDetailesTable.getManufacture());
			AssetDetails.put("model",twowheelerDetailesTable.getModel());
			AssetDetails.put("engineNumber",twowheelerDetailesTable.getEngineNumber());
			AssetDetails.put("chasisNumber",twowheelerDetailesTable.getChasisNumber());
			AssetDetails.put("variant",twowheelerDetailesTable.getVariant());
			AssetDetails.put("dealerName",twowheelerDetailesTable.getDealerName());
			AssetDetails.put("dealerLocation",twowheelerDetailesTable.getDealerLocation());
			AssetDetails.put("exShowroomPrice",twowheelerDetailesTable.getExShowroomPrice());
			AssetDetails.put("roadTax",twowheelerDetailesTable.getRoadTax());
			AssetDetails.put("RegistrationCharges",twowheelerDetailesTable.getRegistrationCharges());
			AssetDetails.put("addonsCharges",twowheelerDetailesTable.getAddonsCharges());
			AssetDetails.put("accessories",twowheelerDetailesTable.getAccessories());
			AssetDetails.put("yearOfManufacturer",twowheelerDetailesTable.getYearOfManufacturer());
			Data.put("AssetDetails", AssetDetails);
			org.json.simple.JSONObject   LoanChargesDetails= new org.json.simple.JSONObject();
			LoanChargesDetails.put("scheme",twowheelerDetailesTable.getScheme());
			LoanChargesDetails.put("marginMoney",twowheelerDetailesTable.getMarginMoney());
			LoanChargesDetails.put("requiredAmount",twowheelerDetailesTable.getRequiredAmount());
			LoanChargesDetails.put("preApprovalAmount",twowheelerDetailesTable.getPreApprovalAmount());
			LoanChargesDetails.put("loanAmount",twowheelerDetailesTable.getAmount());
			LoanChargesDetails.put("rateOfInterest",twowheelerDetailesTable.getRateOfInterest());
			LoanChargesDetails.put("tenure",twowheelerDetailesTable.getTenure());
			LoanChargesDetails.put("emi",twowheelerDetailesTable.getEmi());
			LoanChargesDetails.put("insuranceEmi",twowheelerDetailesTable.getInsuranceEmi());
			LoanChargesDetails.put("totalOnRoadPrice",twowheelerDetailesTable.getTotalOnRoadPrice());
			if(api.equals("mob")) {
				String processingFee="";
				String documentationCharge="";
				if(twowheelerDetailesTable.getLoanCharges() !=null) {
		            org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
		            for(int n=0;n<loanCarges.length();n++) {
		                JSONObject jsonObject = loanCarges.getJSONObject(n);
		                if(jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee"))
		                {
		                	processingFee=jsonObject.getString("totalAmount");
		                }
		                if(jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge"))
		                {
		                	documentationCharge=jsonObject.getString("totalAmount");
		                }
		            }
		        }
				LoanChargesDetails.put("processingFee",processingFee);
				LoanChargesDetails.put("documentationCharge",documentationCharge);	
				
				if(twowheelerDetailesTable.getCheckList() != null) {
					String checklist = twowheelerDetailesTable.getCheckList();
					 ObjectMapper objectMapper = new ObjectMapper();
					 try {
					 List<FinalSaction> checkList = objectMapper.readValue(checklist, new TypeReference<List<FinalSaction>>() {});
					 List<FinalSaction> FilterCheckList = checkList.stream().filter(element->element.getResult().equalsIgnoreCase("IMAGE_DISTORTED")).collect(Collectors.toList());
					 Data.put("CheckList",FilterCheckList);
					 } catch (Exception e) {
				            e.printStackTrace();
				        } 
				}
			}
			Data.put("LoanChargesDetails",LoanChargesDetails);
			TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo,"Nominee");
			if(twoWheelerFamilyMember != null)
			{
				org.json.simple.JSONObject   NomineeDetails= new org.json.simple.JSONObject();
				NomineeDetails.put("firstName", twoWheelerFamilyMember.getFirstName());
				NomineeDetails.put("middleName", twoWheelerFamilyMember.getMiddleName());
				NomineeDetails.put("lastName", twoWheelerFamilyMember.getLastName());
				NomineeDetails.put("dob", twoWheelerFamilyMember.getDob());
				NomineeDetails.put("age", twoWheelerFamilyMember.getAge());
				NomineeDetails.put("member", twoWheelerFamilyMember.getNomineeRelationship());
				NomineeDetails.put("married", twoWheelerFamilyMember.getMarried());
				NomineeDetails.put("mobile", twoWheelerFamilyMember.getMobile());
				NomineeDetails.put("address", twoWheelerFamilyMember.getAddress());
				String guardian = twoWheelerFamilyMember.getGuardian();
				if(guardian != null) {
					org.json.JSONObject guardiandetails =new org.json.JSONObject(guardian);
					GuardianDetails guardianDetails =new GuardianDetails();
					guardianDetails.setGuardianName(guardiandetails.getString("guardianName"));
					guardianDetails.setGuardianRelationship(guardiandetails.getString("guardianRelationship"));
					guardianDetails.setGuardianAddress(guardiandetails.getString("guardianAddress"));
					guardianDetails.setGuardianDob(guardiandetails.getString("guardianDob"));
					guardianDetails.setGuardianAge(guardiandetails.getString("guardianAge"));	
					NomineeDetails.put("guardian", guardianDetails);
				}
				Data.put("NomineeDetails", NomineeDetails);
			}
			org.json.simple.JSONArray   images= new org.json.simple.JSONArray();
			if(api.equals("web")) {
				List<TwowheelerImage> listImage = imageService.getByApplicationNoAndDocument(applicationNo,
						"customerPhoto");
				List<Image> listOfImages = new ArrayList<>();
				for (TwowheelerImage aocpvImages : listImage) {

					String geoLocation = aocpvImages.getGeoLocation();

					JSONObject jsonObjectImage = new JSONObject(geoLocation);
					org.json.simple.JSONObject   geolocation= new org.json.simple.JSONObject();
					org.json.simple.JSONObject   image= new org.json.simple.JSONObject();
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
				Data.put("images", images);
				org.json.simple.JSONObject   disbustmentDetails= new org.json.simple.JSONObject();
				disbustmentDetails.put("sanctionAmount", twowheelerDetailesTable.getAmount());
				disbustmentDetails.put("TotaldeductionAmount", twowheelerDetailesTable.getTotaldeductionAmount());
				disbustmentDetails.put("FinaldisbustmentAmount", twowheelerDetailesTable.getFinaldisbustmentAmount());
				disbustmentDetails.put("tvrForm", twowheelerDetailesTable.getTvrForm());
				Data.put("DisbustmentDetails", disbustmentDetails);
				
				org.json.simple.JSONObject beneficiaryDetails= new org.json.simple.JSONObject();
				beneficiaryDetails.put("BeneficiaryName",twowheelerDetailesTable.getBeneficiaryName());
				beneficiaryDetails.put("BeneficiaryAccountNo", twowheelerDetailesTable.getBeneficiaryAccountNo());
				beneficiaryDetails.put("beneficiaryBankName", twowheelerDetailesTable.getBeneficiaryBankName());
				beneficiaryDetails.put("beneficiaryBranchName", twowheelerDetailesTable.getBeneficiaryBranchName());
				beneficiaryDetails.put("beneficiaryIFSC", twowheelerDetailesTable.getBeneficiaryIFSC());
				beneficiaryDetails.put("disbursalAmount", twowheelerDetailesTable.getFinaldisbustmentAmount());
				Data.put("BeneficiaryDetails", beneficiaryDetails);
				
				
				
				org.json.simple.JSONObject repaymentAccDetails= new org.json.simple.JSONObject();
				repaymentAccDetails.put("MemberName",twowheelerDetailesTable.getName());
				repaymentAccDetails.put("AccountName", twowheelerDetailesTable.getAccountName());
				repaymentAccDetails.put("AccountNumber", twowheelerDetailesTable.getAccountNumber());
				repaymentAccDetails.put("AccountBranchId", twowheelerDetailesTable.getAccountBranchId());
				Data.put("RepaymentAccountDetails", repaymentAccDetails);
				
				String remark = twowheelerDetailesTable.getRemark();
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
		  		//	JSONArray j = new JSONArray(list);
		  			Data.put("Remarks", list);
				}
				String checkList = twowheelerDetailesTable.getCheckList();
				List<FinalSaction> checklist=new ArrayList<>();
				if(checkList != null) {
					org.json.JSONArray checkListWeb =new org.json.JSONArray(checkList);
		  			for(int n=0;n<checkListWeb.length();n++) {
		  				JSONObject jsonObject2 = checkListWeb.getJSONObject(n);
		  				String checklistname = jsonObject2.getString("checklistname");
		  				String result = jsonObject2.getString("result");
		  				String Remarks = jsonObject2.getString("remarks");
		  				FinalSaction finalSaction=new FinalSaction(checklistname,result,Remarks);
		  				checklist.add(finalSaction);
		  			}
		  		//	JSONArray j = new JSONArray(list);
		  			Data.put("checkList", checklist);
				}
				String loanCharges = twowheelerDetailesTable.getLoanCharges();
				if(twowheelerDetailesTable.getLoanCharges() !=null) {
					List<LoanCharges> listOfloanCharges=new ArrayList<>();
					org.json.JSONArray loanCarges =new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
					for(int n=0;n<loanCarges.length();n++) {
						JSONObject jsonObject = loanCarges.getJSONObject(n);
						LoanCharges loanCharge=new LoanCharges(jsonObject.getString("chargeName"), jsonObject.getString("chargeAmount"), jsonObject.getString("taxAmount"), jsonObject.getString("totalAmount"), jsonObject.getString("type"));
						listOfloanCharges.add(loanCharge);
					}
					Data.put("loanCharges", listOfloanCharges);
				}
			}
			return Data;
		}
			
		throw new NoSuchElementException("No Record Found ");
	}

	@Override
	public void updateCustomerId(String applicationNo, String cifNumber) {
		twowheelerDetailsRepository.updateCustomerId(applicationNo,cifNumber);
		
	}

	@Override
	public List<TwowheelerResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId, String listType,String userId) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDateAndbranch(startdate, enddate, status, branchId,listType);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public void updateFlowStatus(String applicationNo, String flowStatus) {
		twowheelerDetailsRepository.updateFlowStatus(applicationNo,flowStatus);
		
	}

	@Override
	public AdditionalInfoResponse fetchAdditionalInfo(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			AdditionalInfoResponse additionalInfoResponse=new AdditionalInfoResponse();
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			additionalInfoResponse.setAccountBankName(twowheelerDetailesTable.getAccountBankName());
			additionalInfoResponse.setAccountName(twowheelerDetailesTable.getAccountName());
			additionalInfoResponse.setAccountBranchId(twowheelerDetailesTable.getAccountBranchId());
			additionalInfoResponse.setAccountIfsc(twowheelerDetailesTable.getAccountIfsc());
			additionalInfoResponse.setAccountNumber(twowheelerDetailesTable.getAccountNumber());
			additionalInfoResponse.setApplicationNo(applicationNo);
			additionalInfoResponse.setStatus(twowheelerDetailesTable.getStatus());
			additionalInfoResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			additionalInfoResponse.setRepaymentType(twowheelerDetailesTable.getRepaymentType());
			additionalInfoResponse.setAccountType(twowheelerDetailesTable.getAccountType());
			additionalInfoResponse.setBeneficiaryAccountType(twowheelerDetailesTable.getBeneficiaryAccountType());
			return additionalInfoResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public BsrDetailsResponse fetchBsrDetails(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			BsrDetailsResponse bsrDetailsResponse=new BsrDetailsResponse();
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			String address = twowheelerDetailesTable.getAddress();
			if (address != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(address);
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					if(addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
						bsrDetailsResponse.setCity(addressInJson.getString("city"));
						bsrDetailsResponse.setDistrict(addressInJson.getString("district"));
						bsrDetailsResponse.setState(addressInJson.getString("state"));
					}
				}
			}
			TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo,"APPLICANT");
			bsrDetailsResponse.setOccupation(twoWheelerFamilyMember.getOccupation());
			return bsrDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<CatCompanies> getCatCompanyList() {
		List<CatCompanies> list=catCompaniesRepository.getAll();
		return list;
	}

	@Override
	public UtilityBillResponse fetchUtilityBillDetails(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			UtilityBillResponse utilityBillResponse=new UtilityBillResponse();
			utilityBillResponse.setUtilityBill(twowheelerDetailesTable.getUtilityBill());
			utilityBillResponse.setUtilityBillIsVerify(twowheelerDetailesTable.getUtilityBillIsVerify());
			utilityBillResponse.setUtilityBillNo(twowheelerDetailesTable.getUtilityBillNo());
			utilityBillResponse.setServiceProvider(twowheelerDetailesTable.getServiceProvider());
			utilityBillResponse.setServiceProviderCode(twowheelerDetailesTable.getServiceProviderCode());
			return utilityBillResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}
}
