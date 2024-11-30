package com.suryoday.twowheeler.serviceImpl;

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
import com.suryoday.aocpv.pojo.PreApprovedList;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.service.GetSavingDetailsService;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.twowheeler.pojo.AdditionalInfoResponse;
import com.suryoday.twowheeler.pojo.Address;
import com.suryoday.twowheeler.pojo.AddressDetails;
import com.suryoday.twowheeler.pojo.BankDetailsResponse;
import com.suryoday.twowheeler.pojo.BsrDetailsResponse;
import com.suryoday.twowheeler.pojo.CatCompanies;
import com.suryoday.twowheeler.pojo.DisbustmentDetailsResponse;
import com.suryoday.twowheeler.pojo.EkycPoaResponse;
import com.suryoday.twowheeler.pojo.GuardianDetails;
import com.suryoday.twowheeler.pojo.LoanCharges;
import com.suryoday.twowheeler.pojo.LoanDetailsResponse;
import com.suryoday.twowheeler.pojo.MandateDetails;
import com.suryoday.twowheeler.pojo.ManualDeviationResponse;
import com.suryoday.twowheeler.pojo.NachDetailsResponse;
import com.suryoday.twowheeler.pojo.NegativeLocalityMaster;
import com.suryoday.twowheeler.pojo.RemarkResponse;
import com.suryoday.twowheeler.pojo.SanctionConditionResponse;
import com.suryoday.twowheeler.pojo.SchemeMaster;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMemberResponse;
import com.suryoday.twowheeler.pojo.TwowheelerAssetDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerImage;
import com.suryoday.twowheeler.pojo.TwowheelerPDResponse;
import com.suryoday.twowheeler.pojo.TwowheelerReferenceDetail;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.pojo.TwowheelerUserLog;
import com.suryoday.twowheeler.pojo.UtilityBillResponse;
import com.suryoday.twowheeler.repository.CatCompaniesRepository;
import com.suryoday.twowheeler.repository.NegativeLocalityRepository;
import com.suryoday.twowheeler.repository.TwowheelerDetailsRepository;
import com.suryoday.twowheeler.service.NachMandateService;
import com.suryoday.twowheeler.service.TwoWheelerMastersService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;
import com.suryoday.twowheeler.service.TwowheelerUserLogService;

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
	LoanInputService loanInputService;

	@Autowired
	NegativeLocalityRepository negativeLocalityRepository;

	@Autowired
	TwowheelerUserLogService userLogService;

	@Autowired
	private NachMandateService nachMandateService;

	@Autowired
	TwoWheelerMastersService twowheelermastersservice;

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
			if (api.equals("web")) {
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
			String branchId, String assignTo) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDate(startdate, enddate, status, branchId,
				assignTo);
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
					PreApprovedList fetchByCustomerId = loanInputService.fetchByCustomerId(customSearch);
					TwowheelerResponse response = new TwowheelerResponse();
					response.setCustomerId(customSearch);
					response.setName(fetchByCustomerId.getMemberName());
					response.setStatus(fetchByCustomerId.getStatus());
					response.setSalesBranchId(Long.toString(fetchByCustomerId.getBranchId()));
					response.setPreApprovalAmount(Double.toString(fetchByCustomerId.getAmount()));
					response.setListType("PRE");
					response.setMobileNo(Long.toString(fetchByCustomerId.getMobilePhone()));
					List<TwowheelerResponse> list3 = new ArrayList<>();
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
	public String createApplicationNo(String mobileNo, String product, String branchId, String listType,
			String customerId, String x_User_ID) {

//		String applicationNo = (LocalDate.now().toString().replace("-", "") + "0001").substring(2, 12);
		String applicationNo = LocalDateTime.now().toString().replace("-", "").replace("T", "").replace(":", "")
				.substring(2, 14);
//		Optional<String> fetchLastApplicationNo = twowheelerDetailsRepository.fetchLastApplicationNo();
//
//		if (fetchLastApplicationNo.isPresent()) {
//			logger.debug("If ApplicationNo is Present");
//
//			String application_No = fetchLastApplicationNo.get();
//			logger.debug(application_No);
//			String dateInDB = application_No.substring(0, 6);
//			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
//			if (currentDate.equals(dateInDB)) {
//				logger.debug("If current Date  is equal to db date");
//				Long applicationno = Long.parseLong(application_No);
//				applicationno++;
//				applicationNo = applicationno.toString();
//				logger.debug(applicationNo + "after increment");
//			}
//		}
		TwowheelerDetailesTable twowheelerDetailesTable = new TwowheelerDetailesTable();
		twowheelerDetailesTable.setApplicationNo(applicationNo);
		LocalDateTime dateTime = LocalDateTime.now();
		twowheelerDetailesTable.setCreatedTimestamp(dateTime);
		twowheelerDetailesTable.setUpdatedTimestamp(dateTime);
		twowheelerDetailesTable.setMobileNo(mobileNo);
		twowheelerDetailesTable.setAppNoWithProductCode(product + applicationNo);
		twowheelerDetailesTable.setMobileNoVerify("YES");
		twowheelerDetailesTable.setEkycVerify("NO");
		twowheelerDetailesTable.setStatus("PROGRESS");
		twowheelerDetailesTable.setFlowStatus("NEWLEAD");
		twowheelerDetailesTable.setStage("SALES");
		twowheelerDetailesTable.setSalesBranchId(branchId);
//		twowheelerDetailesTable.setSalesCreatedBy(x_User_ID);
		twowheelerDetailesTable.setListType(listType);
		twowheelerDetailesTable.setEkycVerify("NO");
		if (listType.equalsIgnoreCase("PRE")) {
			twowheelerDetailesTable.setCustomerId(customerId);
			PreApprovedListVikasLoan loanDetails = loanInputService.getByReferenceNo(customerId);
			loanDetails.setStatus("PROGRESS");
			PreApprovedListVikasLoan saveSingleData2 = loanInputService.saveSingleData(loanDetails);

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
		userLogService.saveUserLog(applicationNo, "INITIATED", x_User_ID, "RO");
		twowheelerDetailsRepository.save(twowheelerDetailesTable);
		return applicationNo;
	}

	@Override
	public void saveResponse(String proof, String proofId, String applicationNo, String response) {

		Optional<TwowheelerDetailesTable> findByApplicationId = twowheelerDetailsRepository
				.getByApplicationNo(applicationNo);
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
				// org.json.JSONObject ekyc=new org.json.JSONObject(response);
				// JSONObject PoiResponse =
				// ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
				// applicationDetails.setName(PoiResponse.getString("name"));
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
			TwoWheelerFamilyMember member = null;
			if (twowheelerDetailesTable.getListType().equalsIgnoreCase("NEW")
					&& twowheelerDetailesTable.getPancard() != null) {
				member = familyMemberService.fetchByAppAndPan(applicationNo, twowheelerDetailesTable.getPancard());
			} else {
				member = familyMemberService.fetchByApplicationNoAndMember(applicationNo, "APPLICANT");

			}
			if (member.getEkycVerify() != null && member.getEkycVerify().equalsIgnoreCase("YES")) {
				JSONObject ekyc = new JSONObject(member.getEkycResponse());
				JSONObject PoaResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
						.getJSONObject("Poa");
				EkycPoaResponse ekycPoa = new EkycPoaResponse();
				if (PoaResponse.has("country")) {
					ekycPoa.setCountry(PoaResponse.getString("country"));
				}
				if (PoaResponse.has("lm")) {
					ekycPoa.setLm(PoaResponse.getString("lm"));
				}
				if (PoaResponse.has("loc")) {
					ekycPoa.setLoc(PoaResponse.getString("loc"));
				}
				if (PoaResponse.has("pc")) {
					ekycPoa.setPc(PoaResponse.getInt("pc"));
				}
				if (PoaResponse.has("vtc")) {
					ekycPoa.setVtc(PoaResponse.getString("vtc"));
				}
				if (PoaResponse.has("street")) {
					ekycPoa.setStreet(PoaResponse.getString("street"));
				}
				if (PoaResponse.has("dist")) {
					ekycPoa.setDist(PoaResponse.getString("dist"));
				}
				if (PoaResponse.has("state")) {
					ekycPoa.setState(PoaResponse.getString("state"));
				}
				if (PoaResponse.has("co")) {
					ekycPoa.setCo(PoaResponse.getString("co"));
				}
				if (PoaResponse.has("house")) {
					ekycPoa.setHouse(PoaResponse.getString("house"));
				}
				twowheelerDetailsResponse.setEkycResponse(ekycPoa);
				twowheelerDetailsResponse.setEkycVerify("YES");
			}
			twowheelerDetailsResponse.setEkycVerify(member.getEkycVerify());
			twowheelerDetailsResponse.setAadharNo(member.getAadharCard());
			twowheelerDetailsResponse.setAadharNoVerify(member.getAadharNoVerify());
			twowheelerDetailsResponse.setMarried(member.getMarried());
			twowheelerDetailsResponse.setGender(member.getGender());
			twowheelerDetailsResponse.setEducation(member.getEducation());
			twowheelerDetailsResponse.setOccupation(member.getOccupation());
			twowheelerDetailsResponse.setPancard(member.getPanCard());
			twowheelerDetailsResponse.setPancardVerify(member.getPancardNoVerify());
			twowheelerDetailsResponse.setVoterId(member.getVoterId());
			twowheelerDetailsResponse.setNameOnPan(member.getNameOnPan());
			twowheelerDetailsResponse.setDobOnPancard(member.getDobOnPancard());
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
			twowheelerDetailsResponse.setAddonsCharges(
					twowheelerDetailesTable.getAddonsCharges() != null ? twowheelerDetailesTable.getAddonsCharges()
							: "0.0");
			twowheelerDetailsResponse.setTotalOnRoadPriceUser(twowheelerDetailesTable.getTotalOnRoadPrice());
			twowheelerDetailsResponse
					.setTotalOnRoadPriceAllocated(twowheelerDetailesTable.getTotalOnRoadPriceAllocated());
			twowheelerDetailsResponse.setFlowStatus(twowheelerDetailesTable.getFlowStatus());
			twowheelerDetailsResponse.setStage(twowheelerDetailesTable.getStage());
			twowheelerDetailsResponse.setChasisNumber(twowheelerDetailesTable.getChasisNumber());
			twowheelerDetailsResponse.setEngineNumber(twowheelerDetailesTable.getEngineNumber());
			twowheelerDetailsResponse.setDealerCode(twowheelerDetailesTable.getDealerCode());
			twowheelerDetailsResponse.setInsurance(
					twowheelerDetailesTable.getInsurance() != null ? twowheelerDetailesTable.getInsurance() : "0.0");
			twowheelerDetailsResponse.setAssetType(twowheelerDetailesTable.getAssetType());
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
			twowheelerDetailsResponse.setEffectiveIrr(twowheelerDetailesTable.getEffectiveIrr());
			twowheelerDetailsResponse.setDocumentationCharge(twowheelerDetailesTable.getDocumentationCharge() != null
					? twowheelerDetailesTable.getDocumentationCharge()
					: "");
			twowheelerDetailsResponse.setProcessingFee(
					twowheelerDetailesTable.getProcessingFee() != null ? twowheelerDetailesTable.getProcessingFee()
							: "");
			if (twowheelerDetailesTable.getLoanCharges() != null) {
				List<LoanCharges> list = new ArrayList<>();
				org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
				for (int n = 0; n < loanCarges.length(); n++) {
					JSONObject jsonObject = loanCarges.getJSONObject(n);
					LoanCharges loanCharges = new LoanCharges(jsonObject.getString("chargeName"),
							jsonObject.getString("chargeAmount"), jsonObject.getString("taxAmount"),
							jsonObject.getString("totalAmount"), jsonObject.getString("type"));
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

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", "TAB");
		Header.put("X-To-ID", "Others");
		Header.put("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
		Header.put("X-User-ID", "");
		Header.put("X-Request-ID", "TAB");
		String AccountId = "";
		String AcctName = "";
		String customerId = twowheelerDetails.getCustomerId();
		if (!customerId.equals("0")) {
			JSONObject data = new JSONObject();
			data.put("MobileNo", "");
			data.put("AadhaarNo", "");
			data.put("AadhaarReferenceNo", "");
			data.put("PanNo", "");
			data.put("CustomerNo", customerId);
			data.put("BranchCode", "");
			data.put("ProductGroup", "CASA");

			JSONObject request = new JSONObject();
			request.put("Data", data);

			JSONObject getdetails = getSavingDetailsService.getDetails(request, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;

			if (getdetails != null) {
				String Data2 = getdetails.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.getJSONObject("Data").has("AccountDetails")) {
					JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("AccountDetails");
					for (int n = 0; n < jsonArray.length(); n++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(n);
						JSONObject jsonObject = jsonObject2.getJSONObject("Product");
						String group = jsonObject.getString("Group");
						if (group.equals("SAV")) {
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
			String processingFee = "";
			String documentationCharge = "";
			if (twowheelerDetailesTable.getLoanCharges() != null) {
				org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
				for (int n = 0; n < loanCarges.length(); n++) {
					JSONObject jsonObject = loanCarges.getJSONObject(n);
					if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
						processingFee = jsonObject.getString("totalAmount");
					}
					if (jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge")) {
						documentationCharge = jsonObject.getString("totalAmount");
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
	public List<TwowheelerResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String assignTo) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDate(startdate, enddate, assignTo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchByDateWithStatus(LocalDateTime startdate, LocalDateTime enddate, String status,
			String assignTo) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDateWithStatus(startdate, enddate, status,
				assignTo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchByDateWithBranchId(LocalDateTime startdate, LocalDateTime enddate,
			String branchId, String assignTo) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchByDateWithBranchId(startdate, enddate,
				branchId, assignTo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchbyApplicationNo(String applicationNo) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchbyApplicationNo(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public List<TwowheelerResponse> fetchbyCustomerId(String customerId) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchbyCustomerId(customerId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public org.json.simple.JSONObject getreviewData(String applicationNo, String api) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		List<TwoWheelerFamilyMember> listMember = familyMemberService.getByApplicationNo(applicationNo);

		TwoWheelerFamilyMember applicant = null;
		TwoWheelerFamilyMember nominee = null;

		for (TwoWheelerFamilyMember twoWheelerMember : listMember) {
			if (twoWheelerMember.getMember().equals("APPLICANT")) {
				applicant = twoWheelerMember;
			}
			if (twoWheelerMember.getMember().equals("Nominee")) {
				nominee = twoWheelerMember;
			}
		}
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();

			org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
			org.json.simple.JSONObject PersonalDetails = new org.json.simple.JSONObject();
			org.json.simple.JSONObject DealSummary = new org.json.simple.JSONObject();
			DealSummary.put("applicationNo", twowheelerDetailesTable.getApplicationNo());
			DealSummary.put("name", twowheelerDetailesTable.getName());
			DealSummary.put("loginDate", twowheelerDetailesTable.getCreatedTimestamp());
			org.json.simple.JSONObject sourcingDetails = new org.json.simple.JSONObject();
			sourcingDetails.put("rmBranchName", twowheelerDetailesTable.getRmBranchName());
			sourcingDetails.put("rmName", twowheelerDetailesTable.getRmName());
			sourcingDetails.put("rmMobileNo", twowheelerDetailesTable.getRmMobileNo());
			sourcingDetails.put("rmEmialId", twowheelerDetailesTable.getRmEmialId());
			sourcingDetails.put("rmStateName", twowheelerDetailesTable.getRmStateName());
			sourcingDetails.put("distFromBranch", applicant.getDistanceFromBranch());
			Data.put("DealSummary", DealSummary);
			Data.put("sourcingDetails", sourcingDetails);

			PersonalDetails.put("applicationNo", twowheelerDetailesTable.getApplicationNo());
			PersonalDetails.put("listType", twowheelerDetailesTable.getListType());
			PersonalDetails.put("customerId", twowheelerDetailesTable.getCustomerId());
			PersonalDetails.put("status", twowheelerDetailesTable.getStatus());
			PersonalDetails.put("name", twowheelerDetailesTable.getName());
			PersonalDetails.put("dateOfBirth", twowheelerDetailesTable.getDateOfBirth());
			PersonalDetails.put("mobileNo", twowheelerDetailesTable.getMobileNo());
			PersonalDetails.put("salesBranchId", twowheelerDetailesTable.getSalesBranchId());
			PersonalDetails.put("salesCreatedBy", twowheelerDetailesTable.getSalesCreatedBy());
			PersonalDetails.put("createdTimestamp", twowheelerDetailesTable.getCreatedTimestamp());
			PersonalDetails.put("occupation", applicant.getOccupation());
			PersonalDetails.put("married", applicant.getMarried());
			PersonalDetails.put("education", applicant.getEducation());
			PersonalDetails.put("gender", applicant.getGender());
			PersonalDetails.put("assignTo", twowheelerDetailesTable.getAssignTo());

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

			if (twowheelerDetailesTable.getListType().equalsIgnoreCase("NEW")) {

				List<BankDetailsResponse> bankDetailsResponses = new ArrayList<>();
				List<TwoWheelerFamilyMemberResponse> listOffamilyMember = new ArrayList<>();
				for (TwoWheelerFamilyMember familyMember : listMember) {
					TwoWheelerFamilyMemberResponse response = new TwoWheelerFamilyMemberResponse();
					response.setApplicationNo(familyMember.getApplicationNo());
					response.setFirstName(familyMember.getFirstName());
					response.setMiddleName(familyMember.getMiddleName());
					response.setLastName(familyMember.getLastName());
					response.setTitle(familyMember.getTitle());
					response.setOccupation(familyMember.getOccupation());
					response.setGender(familyMember.getGender());
					response.setAge(familyMember.getAge());
					response.setDob(familyMember.getDob());
					response.setAppNoWithProductCode(familyMember.getAppNoWithProductCode());
					response.setCreatedTimestamp(familyMember.getCreatedTimestamp());
					response.setMobile(familyMember.getMobile());
					response.setMobileVerify(familyMember.getMobileVerify());
					response.setMarried(familyMember.getMarried());
					response.setAadharCard(familyMember.getAadharCard());
					response.setAadharNoVerify(familyMember.getAadharNoVerify());
					response.setPanCard(familyMember.getPanCard());
					response.setPancardNoVerify(familyMember.getPancardNoVerify());
					response.setVoterId(familyMember.getVoterId());
					response.setVoterIdVerify(familyMember.getVoterId());
					response.setEarning(familyMember.getEarning());
					response.setMember(familyMember.getMember());
					response.setEkycVerify(familyMember.getEkycVerify());
					response.setEkycDoneBy(familyMember.getEkycDoneBy());
					response.setUpdatedTimestamp(familyMember.getUpdatedTimestamp());
					response.setCategory(familyMember.getCategory());
					response.setGuardian(familyMember.getGuardian());
					response.setNomineeRelationship(familyMember.getNomineeRelationship());
					response.setFatherName(familyMember.getFatherName());
					response.setMotherName(familyMember.getMotherName());
					response.setCaste(familyMember.getCaste());
					response.setReligion(familyMember.getReligion());
					response.setEducation(familyMember.getEducation());
					response.setNoOfDependent(familyMember.getNoOfDependent());
					response.setNoOfFamilyMember(familyMember.getNoOfFamilyMember());
					response.setAnnualIncome(familyMember.getAnnualIncome());
					response.setCompanyName(familyMember.getCompanyName());
					response.setForm60(familyMember.getForm60());
					response.setSelectedIdentityProof(familyMember.getSelectedIdentityProof());
					response.setIdentityProof(familyMember.getIdentityProof());
					response.setIdentityProofVerify(familyMember.getIdentityProofVerify());
					response.setOccCode(familyMember.getOccCode());
					response.setReligionCode(familyMember.getReligionCode());
					response.setCasteCode(familyMember.getCasteCode());
					response.setEducationCode(familyMember.getEducationCode());
					response.setMarriedCode(familyMember.getMarriedCode());
					response.setGenderCode(familyMember.getGenderCode());
					response.setTitleCode(familyMember.getTitle());
					response.setCategoryCode(familyMember.getCategoryCode());
					response.setEmailId(familyMember.getEmailId());
					response.setEmploymentType(familyMember.getEmploymentType());
					response.setWorkStability(familyMember.getWorkStability());
					response.setNegativeLocality(familyMember.getNegativeLocality());
					response.setLocalityName(familyMember.getLocalityName());
					response.setDistanceFromBranch(familyMember.getDistanceFromBranch());
					response.setNameOnAAdhar(familyMember.getNameOnAAdhar());
					response.setNameOnPan(familyMember.getNameOnPan());
					response.setDobOnPancard(familyMember.getDobOnPancard());
					response.setAmlScore(familyMember.getAmlScore());
					response.setPenny_drop_name_match_score(familyMember.getPenny_drop_name_match_score());
					response.setSpouseName(familyMember.getSpouseName());
					response.setRelationWithApplicant(familyMember.getRelationWithApplicant());
					response.setOtherCompanyName(familyMember.getOtherCompanyName());
					response.setCibilScore(familyMember.getCibilScore());
					response.setCustomerId(familyMember.getCustomerId());
					response.setDedupeResponse(familyMember.getDedupeResponse());
					if (familyMember.getAddressArray() != null) {
						ObjectMapper objectMapper = new ObjectMapper();
						try {
							List<AddressDetails> reference = objectMapper.readValue(familyMember.getAddressArray(),
									new TypeReference<List<AddressDetails>>() {
									});
							response.setAddressArray(reference);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					listOffamilyMember.add(response);

				}
				if (twowheelerDetailesTable.getBankDetails() != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						List<BankDetailsResponse> reference = objectMapper.readValue(
								twowheelerDetailesTable.getBankDetails(),
								new TypeReference<List<BankDetailsResponse>>() {
								});
						bankDetailsResponses.addAll(reference);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Data.put("memberManagement", listOffamilyMember);
				Data.put("bankDetails", bankDetailsResponses);
			}
			PersonalDetails.put("address", listAddress);
			Data.put("PersonalDetails", PersonalDetails);
			org.json.simple.JSONObject AssetDetails = new org.json.simple.JSONObject();
			AssetDetails.put("manufacture", twowheelerDetailesTable.getManufacture());
			AssetDetails.put("model", twowheelerDetailesTable.getModel());
			AssetDetails.put("engineNumber", twowheelerDetailesTable.getEngineNumber());
			AssetDetails.put("chasisNumber", twowheelerDetailesTable.getChasisNumber());
			AssetDetails.put("variant", twowheelerDetailesTable.getVariant());
			AssetDetails.put("dealerName", twowheelerDetailesTable.getDealerName());
			AssetDetails.put("dealerLocation", twowheelerDetailesTable.getDealerLocation());
			AssetDetails.put("exShowroomPrice", twowheelerDetailesTable.getExShowroomPrice());
			AssetDetails.put("roadTax", twowheelerDetailesTable.getRoadTax());
			AssetDetails.put("RegistrationCharges", twowheelerDetailesTable.getRegistrationCharges());
			AssetDetails.put("addonsCharges", twowheelerDetailesTable.getAddonsCharges());
			AssetDetails.put("accessories", twowheelerDetailesTable.getAccessories());
			AssetDetails.put("yearOfManufacturer", twowheelerDetailesTable.getYearOfManufacturer());
			AssetDetails.put("onRoadPrice", twowheelerDetailesTable.getTotalOnRoadPrice());
			Data.put("AssetDetails", AssetDetails);
			org.json.simple.JSONObject LoanChargesDetails = new org.json.simple.JSONObject();
			LoanChargesDetails.put("scheme", twowheelerDetailesTable.getScheme());
			LoanChargesDetails.put("marginMoney", twowheelerDetailesTable.getMarginMoney());
			LoanChargesDetails.put("requiredAmount", twowheelerDetailesTable.getRequiredAmount());
			LoanChargesDetails.put("preApprovalAmount", twowheelerDetailesTable.getPreApprovalAmount());
			LoanChargesDetails.put("loanAmount", twowheelerDetailesTable.getAmount());
			LoanChargesDetails.put("rateOfInterest", twowheelerDetailesTable.getRateOfInterest());
			LoanChargesDetails.put("tenure", twowheelerDetailesTable.getTenure());
			LoanChargesDetails.put("emi", twowheelerDetailesTable.getEmi());
			LoanChargesDetails.put("insuranceEmi", twowheelerDetailesTable.getInsuranceEmi());
			LoanChargesDetails.put("totalOnRoadPrice", twowheelerDetailesTable.getTotalOnRoadPrice());

			if (twowheelerDetailesTable.getReferenceInfo() != null) {
				String reference = twowheelerDetailesTable.getReferenceInfo();
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<TwowheelerReferenceDetail> list = objectMapper.readValue(reference,
							new TypeReference<List<TwowheelerReferenceDetail>>() {
							});
					Data.put("ReferenceDetail", list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (api.equals("mob")) {
				String processingFee = "";
				String documentationCharge = "";
				if (twowheelerDetailesTable.getLoanCharges() != null) {
					org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
					for (int n = 0; n < loanCarges.length(); n++) {
						JSONObject jsonObject = loanCarges.getJSONObject(n);
						if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
							processingFee = jsonObject.getString("totalAmount");
						}
						if (jsonObject.getString("chargeName").equalsIgnoreCase("Documentation Charge")) {
							documentationCharge = jsonObject.getString("totalAmount");
						}
					}
				}
				LoanChargesDetails.put("processingFee", processingFee);
				LoanChargesDetails.put("documentationCharge", documentationCharge);

				if (twowheelerDetailesTable.getCheckList() != null) {
					String checklist = twowheelerDetailesTable.getCheckList();
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						List<FinalSaction> checkList = objectMapper.readValue(checklist,
								new TypeReference<List<FinalSaction>>() {
								});
						List<FinalSaction> FilterCheckList = checkList.stream()
								.filter(element -> element.getResult().equalsIgnoreCase("IMAGE_DISTORTED"))
								.collect(Collectors.toList());
						Data.put("CheckList", FilterCheckList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			Data.put("LoanChargesDetails", LoanChargesDetails);
//			TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo,"Nominee");
			if (nominee != null) {
				org.json.simple.JSONObject NomineeDetails = new org.json.simple.JSONObject();
				NomineeDetails.put("firstName", nominee.getFirstName());
				NomineeDetails.put("middleName", nominee.getMiddleName());
				NomineeDetails.put("lastName", nominee.getLastName());
				NomineeDetails.put("dob", nominee.getDob());
				NomineeDetails.put("age", nominee.getAge());
				NomineeDetails.put("member", nominee.getNomineeRelationship());
				NomineeDetails.put("married", nominee.getMarried());
				NomineeDetails.put("mobile", nominee.getMobile());
				NomineeDetails.put("address", nominee.getAddress());
				String guardian = nominee.getGuardian();
				if (guardian != null) {
					org.json.JSONObject guardiandetails = new org.json.JSONObject(guardian);
					GuardianDetails guardianDetails = new GuardianDetails();
					guardianDetails.setGuardianName(guardiandetails.getString("guardianName"));
					guardianDetails.setGuardianRelationship(guardiandetails.getString("guardianRelationship"));
					guardianDetails.setGuardianAddress(guardiandetails.getString("guardianAddress"));
					guardianDetails.setGuardianDob(guardiandetails.getString("guardianDob"));
					guardianDetails.setGuardianAge(guardiandetails.getString("guardianAge"));
					NomineeDetails.put("guardian", guardianDetails);
				}
				Data.put("NomineeDetails", NomineeDetails);
			}
			org.json.simple.JSONArray images = new org.json.simple.JSONArray();
			if (api.equals("web")) {

				List<TwowheelerUserLog> userlog = userLogService.getByApplicationNo(applicationNo);
				Data.put("BeneficiaryDetails", userlog);
				List<TwowheelerImage> listImage = imageService.getByApplicationNoAndDocument(applicationNo,
						"customerPhoto");
				List<Image> listOfImages = new ArrayList<>();
				for (TwowheelerImage aocpvImages : listImage) {

					String geoLocation = aocpvImages.getGeoLocation();

					JSONObject jsonObjectImage = new JSONObject(geoLocation);
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
				Data.put("images", images);
				org.json.simple.JSONObject disbustmentDetails = new org.json.simple.JSONObject();
				disbustmentDetails.put("sanctionAmount", twowheelerDetailesTable.getAmount());
				disbustmentDetails.put("TotaldeductionAmount", twowheelerDetailesTable.getTotaldeductionAmount());
				disbustmentDetails.put("FinaldisbustmentAmount", twowheelerDetailesTable.getFinaldisbustmentAmount());
				disbustmentDetails.put("tvrForm", twowheelerDetailesTable.getTvrForm());
				Data.put("DisbustmentDetails", disbustmentDetails);
				if (twowheelerDetailesTable.getBreResponse() != null) {
					int count = 1;
					JSONObject breResponseJson = new JSONObject(twowheelerDetailesTable.getBreResponse());
					for (String keys : breResponseJson.keySet()) {
						if (!breResponseJson.getJSONObject(keys).get("deviation_group").equals(null)) {
							String p = breResponseJson.getJSONObject(keys).get("deviation_group").toString();
							int a = Character.getNumericValue(p.charAt(6));
							if (a > count) {
								count = a;
							}
						}
					}
					Data.put("approvedAuthority", "Level " + count);
				}
				org.json.simple.JSONObject beneficiaryDetails = new org.json.simple.JSONObject();
				beneficiaryDetails.put("BeneficiaryName", twowheelerDetailesTable.getBeneficiaryName());
				beneficiaryDetails.put("BeneficiaryAccountNo", twowheelerDetailesTable.getBeneficiaryAccountNo());
				beneficiaryDetails.put("beneficiaryBankName", twowheelerDetailesTable.getBeneficiaryBankName());
				beneficiaryDetails.put("beneficiaryBranchName", twowheelerDetailesTable.getBeneficiaryBranchName());
				beneficiaryDetails.put("beneficiaryIFSC", twowheelerDetailesTable.getBeneficiaryIFSC());
				beneficiaryDetails.put("disbursalAmount", twowheelerDetailesTable.getFinaldisbustmentAmount());
				Data.put("BeneficiaryDetails", beneficiaryDetails);

				org.json.simple.JSONObject repaymentAccDetails = new org.json.simple.JSONObject();
				repaymentAccDetails.put("MemberName", twowheelerDetailesTable.getName());
				repaymentAccDetails.put("AccountName", twowheelerDetailesTable.getAccountName());
				repaymentAccDetails.put("AccountNumber", twowheelerDetailesTable.getAccountNumber());
				repaymentAccDetails.put("AccountBranchId", twowheelerDetailesTable.getAccountBranchId());
				Data.put("RepaymentAccountDetails", repaymentAccDetails);

				String remark = twowheelerDetailesTable.getRemark();
				List<RemarkResponse> list = new ArrayList<>();
				if (remark != null) {
					org.json.JSONArray remarks = new org.json.JSONArray(remark);
					for (int n = 0; n < remarks.length(); n++) {
						JSONObject jsonObject2 = remarks.getJSONObject(n);
						String Decision = jsonObject2.getString("decision");
						String level = jsonObject2.getString("level");
						String remark1 = jsonObject2.getString("remarks");
						String date = jsonObject2.getString("date");
						String updatedBy = jsonObject2.getString("updatedBy");
						RemarkResponse remarkResponse = new RemarkResponse(Decision, level, date, updatedBy, remark1);
						list.add(remarkResponse);
					}
					// JSONArray j = new JSONArray(list);
				}
				Data.put("Remarks", list);
				String checkList = twowheelerDetailesTable.getCheckList();
				List<FinalSaction> checklist = new ArrayList<>();
				if (checkList != null) {
					org.json.JSONArray checkListWeb = new org.json.JSONArray(checkList);
					for (int n = 0; n < checkListWeb.length(); n++) {
						JSONObject jsonObject2 = checkListWeb.getJSONObject(n);
						String checklistname = jsonObject2.getString("checklistname");
						String result = jsonObject2.getString("result");
						String Remarks = jsonObject2.getString("remarks");
						FinalSaction finalSaction = new FinalSaction(checklistname, result, Remarks);
						checklist.add(finalSaction);
					}
					// JSONArray j = new JSONArray(list);
					Data.put("checkList", checklist);
				}
				String loanCharges = twowheelerDetailesTable.getLoanCharges();
				if (twowheelerDetailesTable.getLoanCharges() != null) {
					List<LoanCharges> listOfloanCharges = new ArrayList<>();
					org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetailesTable.getLoanCharges());
					for (int n = 0; n < loanCarges.length(); n++) {
						JSONObject jsonObject = loanCarges.getJSONObject(n);
						LoanCharges loanCharge = new LoanCharges(jsonObject.getString("chargeName"),
								jsonObject.getString("chargeAmount"), jsonObject.getString("taxAmount"),
								jsonObject.getString("totalAmount"), jsonObject.getString("type"));
						listOfloanCharges.add(loanCharge);
					}
					Data.put("loanCharges", listOfloanCharges);
				}
				List<MandateDetails> mandateDetails = nachMandateService.getMandateDetails(applicationNo);
				Data.put("nachMandateList", mandateDetails);

				if (twowheelerDetailesTable.getSecurityPdcDetails() != null) {
					Data.put("securityPdcDetails", twowheelerDetailesTable.getSecurityPdcDetails());
				}
			}
			return Data;
		}

		throw new NoSuchElementException("No Record Found ");
	}

	@Override
	public void updateCustomerId(String applicationNo, String cifNumber) {
		twowheelerDetailsRepository.updateCustomerId(applicationNo, cifNumber);

	}

	@Override
	public List<TwowheelerResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId, String listType, String userId) {
		List<TwowheelerResponse> list = null;
		if (status.equals("COMPLETED")) {

			list = twowheelerDetailsRepository.fetchCompleted(startdate, enddate, branchId, listType);
			list = list.stream().map(entity -> {
				entity.setStage("COMPLETED");
				return entity;
			}).collect(Collectors.toList());
		} else {
			list = twowheelerDetailsRepository.fetchByDateAndbranch(startdate, enddate, status, branchId, listType);
		}
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found ");
		}
		return list;
	}

	@Override
	public void updateFlowStatus(String applicationNo, String flowStatus) {
		twowheelerDetailsRepository.updateFlowStatus(applicationNo, flowStatus);

	}

	@Override
	public AdditionalInfoResponse fetchAdditionalInfo(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			AdditionalInfoResponse additionalInfoResponse = new AdditionalInfoResponse();
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
			BsrDetailsResponse bsrDetailsResponse = new BsrDetailsResponse();
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			String address = twowheelerDetailesTable.getAddress();
			if (address != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(address);
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					if (addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
						bsrDetailsResponse.setCity(addressInJson.getString("city"));
						bsrDetailsResponse.setDistrict(addressInJson.getString("district"));
						bsrDetailsResponse.setState(addressInJson.getString("state"));
					}
				}
			}
			TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService
					.fetchByApplicationNoAndMember(applicationNo, "APPLICANT");
			bsrDetailsResponse.setOccupation(twoWheelerFamilyMember.getOccupation());
			bsrDetailsResponse.setLoanPurpose(twowheelerDetailesTable.getLoanPurpose());
			bsrDetailsResponse.setBussinessSegment(twowheelerDetailesTable.getBussinessSegment());
			bsrDetailsResponse.setClassificationAdvance(twowheelerDetailesTable.getClassificationAdvance());
			bsrDetailsResponse.setGovernmentSponsoredScheme(twowheelerDetailesTable.getGovernmentSponsoredScheme());
			bsrDetailsResponse.setChannelCode(twowheelerDetailesTable.getChannelCode());
			bsrDetailsResponse.setPopulationCode(twowheelerDetailesTable.getPopulationCode());
			bsrDetailsResponse.setSanctionDepartment(twowheelerDetailesTable.getSanctionDepartment());

			return bsrDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<CatCompanies> getCatCompanyList() {
		List<CatCompanies> list = catCompaniesRepository.getAll();
		return list;
	}

	@Override
	public List<TwowheelerResponse> getbyCustomerId(String cifNumber) {
		List<TwowheelerResponse> list = twowheelerDetailsRepository.fetchbyCustomerId(cifNumber);
		if (list.size() == 0) {
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	public UtilityBillResponse fetchUtilityBillDetails(String applicationNo, String flowStatus) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			UtilityBillResponse utilityBillResponse = new UtilityBillResponse();
			utilityBillResponse.setUtilityBill(twowheelerDetailesTable.getUtilityBill());
			utilityBillResponse.setUtilityBillIsVerify(twowheelerDetailesTable.getUtilityBillIsVerify());
			utilityBillResponse.setUtilityBillNo(twowheelerDetailesTable.getUtilityBillNo());
			utilityBillResponse.setServiceProvider(twowheelerDetailesTable.getServiceProvider());
			utilityBillResponse.setApplicationNo(applicationNo);
			utilityBillResponse.setServiceProviderCode(twowheelerDetailesTable.getServiceProviderCode());
			return utilityBillResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<NegativeLocalityMaster> getNegativeLocalityName() {
		List<NegativeLocalityMaster> list = negativeLocalityRepository.getALL();
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public void updateAssignTo(List<String> list, String assignTo) {
		twowheelerDetailsRepository.updateAssignTo(list, assignTo, LocalDateTime.now());

	}

	@Override
	public org.json.simple.JSONObject getDealSummary(String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);

		List<TwoWheelerFamilyMember> listMember = familyMemberService.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
			Data.put("loginDate", twowheelerDetailesTable.getCreatedTimestamp());
			Data.put("applicationNo", "TW-" + twowheelerDetailesTable.getApplicationNo());
			Data.put("applicantName", twowheelerDetailesTable.getName());

			org.json.simple.JSONObject sourcingDetails = new org.json.simple.JSONObject();
			sourcingDetails.put("branchName", twowheelerDetailesTable.getRmBranchName());
			sourcingDetails.put("rmName", twowheelerDetailesTable.getRmName());
			sourcingDetails.put("rmMobileNo", twowheelerDetailesTable.getRmMobileNo());
			sourcingDetails.put("stateName", twowheelerDetailesTable.getRmStateName());
			sourcingDetails.put("distanceFromBranch", "");
			Data.put("sourcingDetails", sourcingDetails);
			org.json.simple.JSONArray memberDetails = new org.json.simple.JSONArray();
			for (TwoWheelerFamilyMember twoWheelerMember : listMember) {
				org.json.simple.JSONObject memberDetail = new org.json.simple.JSONObject();
				memberDetail.put("name", twoWheelerMember.getFirstName() + " " + twoWheelerMember.getLastName());
				memberDetail.put("memberRole", twoWheelerMember.getMember());
				memberDetail.put("memberRelation", twoWheelerMember.getRelationWithApplicant());
				memberDetail.put("ExistingCustomer", "NO");
				memberDetails.add(memberDetail);
			}
			Data.put("memberDetails", memberDetails);
			org.json.simple.JSONObject assetDetails = new org.json.simple.JSONObject();
			assetDetails.put("manufacture", twowheelerDetailesTable.getManufacture());
			assetDetails.put("model", twowheelerDetailesTable.getModel());
			assetDetails.put("dealerName", twowheelerDetailesTable.getDealerName());
			assetDetails.put("totalOnRoadPrice", twowheelerDetailesTable.getTotalOnRoadPrice());
			Data.put("assetDetails", assetDetails);
			org.json.simple.JSONObject loanDetails = new org.json.simple.JSONObject();
			loanDetails.put("scheme", twowheelerDetailesTable.getScheme());
			loanDetails.put("margineMoney", twowheelerDetailesTable.getMarginMoney());
			loanDetails.put("totalAmount", twowheelerDetailesTable.getAmount());
			loanDetails.put("roi", twowheelerDetailesTable.getRateOfInterest());
			loanDetails.put("tenure", twowheelerDetailesTable.getTenure());
			loanDetails.put("emi", twowheelerDetailesTable.getEmi());
			loanDetails.put("insuranceEmi", twowheelerDetailesTable.getInsuranceEmi());
			Data.put("loanDetails", loanDetails);
			String breResponse = twowheelerDetailesTable.getBreResponse();
			if (twowheelerDetailesTable.getManualDeviation() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<ManualDeviationResponse> reference = objectMapper.readValue(
							twowheelerDetailesTable.getManualDeviation(),
							new TypeReference<List<ManualDeviationResponse>>() {
							});
					Data.put("manualDeviation", reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (twowheelerDetailesTable.getSanctionCondition() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<SanctionConditionResponse> reference = objectMapper.readValue(
							twowheelerDetailesTable.getSanctionCondition(),
							new TypeReference<List<SanctionConditionResponse>>() {
							});
					Data.put("sanctionCondition", reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (twowheelerDetailesTable.getRemark() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<RemarkResponse> reference = objectMapper.readValue(twowheelerDetailesTable.getRemark(),
							new TypeReference<List<RemarkResponse>>() {
							});
					Data.put("decision", reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (breResponse != null) {
				int count = 1;
				JSONObject breResponseJson = new JSONObject(breResponse);
				for (String keys : breResponseJson.keySet()) {
					if (!breResponseJson.getJSONObject(keys).get("deviation_group").equals(null)) {
						String p = breResponseJson.getJSONObject(keys).get("deviation_group").toString();
						int a = Character.getNumericValue(p.charAt(6));
						if (a > count) {
							count = a;
						}
					}
				}
				Data.put("approvedAuthority", "Level " + count);
				Data.put("breResponse", breResponseJson.toString());
			}
			org.json.simple.JSONArray loanDetail = new org.json.simple.JSONArray();
			org.json.simple.JSONObject loanDetail1 = new org.json.simple.JSONObject();
			loanDetail1.put("field", "Amount");
			loanDetail1.put("applied", twowheelerDetailesTable.getRequiredAmount());
			loanDetail1.put("eligiable", "");
			loanDetail1.put("approved", twowheelerDetailesTable.getAmount());
			loanDetail.add(loanDetail1);
			org.json.simple.JSONObject loanDetail2 = new org.json.simple.JSONObject();
			loanDetail2.put("field", "ROI");
			loanDetail2.put("applied", twowheelerDetailesTable.getRateOfInterest());
			loanDetail2.put("eligiable", twowheelerDetailesTable.getRateOfInterest());
			loanDetail2.put("approved", twowheelerDetailesTable.getRateOfInterest());
			loanDetail.add(loanDetail2);
			org.json.simple.JSONObject loanDetail3 = new org.json.simple.JSONObject();
			loanDetail3.put("field", "Tenure");
			loanDetail3.put("applied", twowheelerDetailesTable.getTenure());
			loanDetail3.put("eligiable", twowheelerDetailesTable.getTenure());
			loanDetail3.put("approved", twowheelerDetailesTable.getTenure());
			loanDetail.add(loanDetail3);
			org.json.simple.JSONObject loanDetail4 = new org.json.simple.JSONObject();
			loanDetail4.put("field", "Emi");
			loanDetail4.put("applied", twowheelerDetailesTable.getEmi());
			loanDetail4.put("eligiable", twowheelerDetailesTable.getEmi());
			loanDetail4.put("approved", twowheelerDetailesTable.getEmi());
			loanDetail.add(loanDetail4);
			org.json.simple.JSONObject loanDetail5 = new org.json.simple.JSONObject();
			loanDetail5.put("field", "LTV");
			loanDetail5.put("applied",
					twowheelerDetailesTable.getAppliedLtv() != null ? twowheelerDetailesTable.getAppliedLtv() : "100");
			loanDetail5.put("eligiable",
					twowheelerDetailesTable.getEligibleLtv() != null ? twowheelerDetailesTable.getEligibleLtv() : "80");
			loanDetail5.put("approved", "80");
			loanDetail.add(loanDetail5);
			org.json.simple.JSONObject loanDetail6 = new org.json.simple.JSONObject();
			loanDetail6.put("field", "marginMoney");
			loanDetail6.put("applied", "0.0");
			loanDetail6.put("eligiable", "");
			loanDetail6.put("approved", twowheelerDetailesTable.getMarginMoney());
			loanDetail.add(loanDetail6);
			Data.put("loanDetail", loanDetail);
			Data.put("isBreRuning", twowheelerDetailesTable.getIsBreRuning());
			Data.put("breStatus", twowheelerDetailesTable.getBreStatus());

			return Data;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public int fetchByLeadId(String lead_ID) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByLeadId(lead_ID);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			twowheelerDetailesTable.setIsBreRuning("YES");
			twowheelerDetailsRepository.save(twowheelerDetailesTable);
			return 1;
		}
		return 0;
	}

	@Override
	public NachDetailsResponse getNachDetails(String applicationNo) {
		Optional<TwowheelerDetailesTable> optional = twowheelerDetailsRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			TwowheelerDetailesTable twowheelerDetailesTable = optional.get();
			NachDetailsResponse nachDetailsResponse = new NachDetailsResponse();
			nachDetailsResponse
					.setName(twowheelerDetailesTable.getName() != null ? twowheelerDetailesTable.getName() : "");
			nachDetailsResponse.setPancard(
					twowheelerDetailesTable.getPancard() != null ? twowheelerDetailesTable.getPancard() : "");
			nachDetailsResponse.setMobileNo(
					twowheelerDetailesTable.getMobileNo() != null ? twowheelerDetailesTable.getMobileNo() : "");
			nachDetailsResponse.setCollectionAmount(
					twowheelerDetailesTable.getEmi() != null ? twowheelerDetailesTable.getEmi() : "");
			nachDetailsResponse.setAmountType("MAXA");
			return nachDetailsResponse;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public boolean validateCalculation(String totalOnRoadPrice, String marginMoney, String scheme, String amount,
			String tenure, String loanCharges) {
		double totalOnRoadPriceDouble = Double.parseDouble(totalOnRoadPrice);
		double marginMoneyDouble = Double.parseDouble(marginMoney);
		double amountDouble = Double.parseDouble(amount);
		if (amountDouble == totalOnRoadPriceDouble - marginMoneyDouble) {
			return false;
		}
		SchemeMaster master = twowheelermastersservice.fetchBySchemeCode(scheme);
		master.getProcessingFee();

		JSONArray jsonArray = new JSONArray(loanCharges);
		for (int i = 0; i < jsonArray.length(); i++) {

		}
		return false;
	}
}
