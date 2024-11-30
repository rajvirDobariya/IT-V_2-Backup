package com.suryoday.aocpv.serviceImp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.IncomeDetailWeb;
import com.suryoday.aocpv.repository.AocpvIncomeDetailsRepository;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;

@Service
public class AocpvIncomeServiceImpl implements AocpvIncomeService{

	Logger logger = LoggerFactory.getLogger(AocpvIncomeServiceImpl.class);
	@Autowired
	AocpvIncomeDetailsRepository aocpvIncomeDetailsRepository;
	
	@Autowired
	AocpvImageService imageService;
	
	@Override
	public String saveAll(List<AocpvIncomeDetails> aocpvIncomeDetails) {
		int id=1;
			String count="one";
		for(AocpvIncomeDetails aocpvIncomeDetail :aocpvIncomeDetails) {
			
			Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
			if(optional1.isPresent()) {
				id=	optional1.get();
				id++;
			}
					long applicationNo = aocpvIncomeDetail.getApplicationNo();
					String member = aocpvIncomeDetail.getMember();
					aocpvIncomeDetail.setId(id);
//					if(member.equalsIgnoreCase("SELF")) {
//						long mobile = aocpvIncomeDetail.getMobile();
//						if(mobile == 0) {	
//						}
//						else {
//			List<AocpvIncomeDetails> list =aocpvIncomeDetailsRepository.getByMobile(mobile);
//						for(AocpvIncomeDetails details :list) {
//							String member2 = details.getMember();
//							if(member2.equalsIgnoreCase("SELF")) {
//								if(applicationNo == details.getApplicationNo()) {
//									
//								}
//								else {
//								throw new DuplicateEntryException("mobile no can not be duplicate");
//								}
//							}
//						}
//						}
//					}
	Optional<AocpvIncomeDetails> optional =aocpvIncomeDetailsRepository.find(applicationNo,member);
				int versionCode=1;
						if(optional.isPresent()) {
					AocpvIncomeDetails incomeDetails = optional.get();
					aocpvIncomeDetail.setId(incomeDetails.getId());	
					aocpvIncomeDetail.setPanCardResponse(incomeDetails.getPanCardResponse());
					aocpvIncomeDetail.setVoterIdResponse(incomeDetails.getVoterIdResponse());
					aocpvIncomeDetail.setPassportResponse(incomeDetails.getPassportResponse());
					if(incomeDetails.getAuthVerify() !=null) {
						aocpvIncomeDetail.setAuthVerify(incomeDetails.getAuthVerify());	
					}
					aocpvIncomeDetail.setDrivingLicenseResponse(incomeDetails.getDrivingLicenseResponse());
					versionCode	=incomeDetails.getVersionCode();
			//		versionCode++;
					if(member.equalsIgnoreCase("SELF")) {
						if(aocpvIncomeDetail.getFirstName()!=null && !incomeDetails.getFirstName().equals(aocpvIncomeDetail.getFirstName())) {
							throw new DuplicateEntryException("please select other memeber");
						}
						else if(aocpvIncomeDetail.getLastName()!=null && !incomeDetails.getLastName().equals(aocpvIncomeDetail.getLastName())) {
							throw new DuplicateEntryException("please select other memeber");
						}
					}
					if(versionCode == 0) {
						versionCode=1;
					}
						}
						aocpvIncomeDetail.setVersionCode(versionCode);
						if(aocpvIncomeDetail.getPrimarySourceOfIncome().equalsIgnoreCase("YES")) {
							List<AocpvIncomeDetails> list = aocpvIncomeDetailsRepository.getByApp(aocpvIncomeDetail.getApplicationNo());
							if(list.size()>1 || aocpvIncomeDetails.size()>1) {
								count="two";
							}
							if(list.size()!=0) {
								for(AocpvIncomeDetails a:list) {
									String member2 = a.getMember();
									String member3 = aocpvIncomeDetail.getMember();
									if(a.getPrimarySourceOfIncome()!=null && a.getPrimarySourceOfIncome().equalsIgnoreCase("YES")) {
										
										if(!member2.equalsIgnoreCase(member3)) {
											throw new DuplicateEntryException("primary source of income already present");
										}
										
									}
								}
							}
						}
						aocpvIncomeDetailsRepository.save(aocpvIncomeDetail);
		}
//		if(count == 0) {
//			
//			for(AocpvIncomeDetails aocpvIncomeDetail :aocpvIncomeDetails) {
//				
//				aocpvIncomeDetail.setId(id);
//			}
//			aocpvIncomeDetailsRepository.saveAll(aocpvIncomeDetails);
//		}
		return count;
	}

	@Override
	public List<AocpvIncomeDetails> getByApplicationNo(long applicationNo) {
		List<AocpvIncomeDetails> list =aocpvIncomeDetailsRepository.findByApplicationNo(applicationNo);
		//if(list.isEmpty()) {
		//	throw new NoSuchElementException("list is empty");
		//}
		return list;
	}

	@Override
	public String getByApplicationNoAndMember(long applicationNo) {
		String member="SELF";
		
	Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNo,member);
	if(optional.isPresent()) {
		AocpvIncomeDetails incomeDetails = optional.get();
		String aadharCard = incomeDetails.getAadharCard();
		return aadharCard;
	}
	throw new NoSuchElementException("Adhar Card No Found");
	}

	@Override
	public String documentVerify(long applicationNoInLong, String document, String documentType , String documentVerify) {
		if(documentType.equalsIgnoreCase("aadharCard")) {
			aocpvIncomeDetailsRepository.aadharCardVerify(applicationNoInLong,document,documentVerify);
		}
		else if(documentType.equalsIgnoreCase("panCard")) {
			aocpvIncomeDetailsRepository.panCardVerify(applicationNoInLong,document,documentVerify);
		}
		else if(documentType.equalsIgnoreCase("voterId")) {
			aocpvIncomeDetailsRepository.voterIdVerify(applicationNoInLong,document,documentVerify);
		}
		else {
			throw new NoSuchElementException("documentType Not Found");
		}
		return "document verified";
	}

	@Override
	public List<IncomeDetailWeb> fetchByMember(long applicationNoInLong) {
		
		List<AocpvIncomeDetails> list = aocpvIncomeDetailsRepository.getByApp(applicationNoInLong);
		if(list.isEmpty()) {
			throw new NoSuchElementException("member Not Found");
		}
		 List<IncomeDetailWeb> listIncome =new ArrayList<>();
		 for(AocpvIncomeDetails incomeDetails:list) {
			 IncomeDetailWeb incomeDetail =new IncomeDetailWeb();
			 incomeDetail.setMember(incomeDetails.getMember());
			 incomeDetail.setTitle(incomeDetails.getTitle());
			 incomeDetail.setFirstName(incomeDetails.getFirstName());
			 incomeDetail.setLastName(incomeDetails.getLastName());
			 incomeDetail.setGender(incomeDetails.getGender());
			 incomeDetail.setAge(incomeDetails.getAge());
			 LocalDate date = incomeDetails.getDob();
		//	 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
			 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		   	String dob= date.format(formatter1);
			 incomeDetail.setDob(dob);
			 incomeDetail.setMobile(incomeDetails.getMobile());
			 incomeDetail.setMobileVerify(incomeDetails.getMobileVerify());
			 incomeDetail.setMarried(incomeDetails.getMarried());
			 incomeDetail.setAadharCard(incomeDetails.getAadharCard());
			 incomeDetail.setGaurantor(incomeDetails.getGaurantor());
			 incomeDetail.setAadharNoVerify(incomeDetails.getAadharNoVerify());
			 incomeDetail.setPanCard(incomeDetails.getPanCard());
			 incomeDetail.setPancardNoVerify(incomeDetails.getPancardNoVerify());
			 incomeDetail.setVoterId(incomeDetails.getVoterId());
			 incomeDetail.setVoterIdVerify(incomeDetails.getVoterIdVerify());
			 incomeDetail.setEarning(incomeDetails.getEarning());
			 incomeDetail.setOccupation(incomeDetails.getOccupation());
			 incomeDetail.setOccCode(incomeDetails.getOccCode());
			 incomeDetail.setPrimarySourceOfIncome(incomeDetails.getPrimarySourceOfIncome());
			 incomeDetail.setSecuredLoan(incomeDetails.getSecuredLoan());
			 incomeDetail.setUnsecuredLoan(incomeDetails.getUnsecuredLoan());
			 incomeDetail.setMonthlyIncome(incomeDetails.getMonthlyIncome());
			 incomeDetail.setForm60(incomeDetails.getForm60());
			 incomeDetail.setMonthlyLoanEmi(incomeDetails.getMonthlyLoanEmi()); 
			 incomeDetail.setAuthVerify(incomeDetails.getAuthVerify());
			 List<Image> listOfImages=new ArrayList<>();
			 List<AocpvImages> listAocpvImage = imageService.getByApplicationNoAndMember(applicationNoInLong,incomeDetails.getMember(),"ID");
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
		return listIncome;
	}

	@Override
	public String mobileVerify(long mobileNoInLong) {
		String member ="SELF";
		try {
			Optional<Long> optional=aocpvIncomeDetailsRepository.mobileVerify(mobileNoInLong,member);
			if(optional.isPresent()) {
			throw new DuplicateEntryException("mobile no is already exist");
			}
			return "no record found";
		}
		catch (IncorrectResultSizeDataAccessException e) {
			throw new DuplicateEntryException("mobile no is already exist");
		}
		
	}

	@Override
	public String saveIncome(AocpvIncomeDetails aocpvIncomeDetails) {
		int id=1;
		Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
		if(optional1.isPresent()) {
			id=	optional1.get();
			id++;
		}
		aocpvIncomeDetails.setId(id);
		Optional<AocpvIncomeDetails> optional =aocpvIncomeDetailsRepository.find(aocpvIncomeDetails.getApplicationNo(),aocpvIncomeDetails.getMember());
		if(optional.isPresent()) {
	AocpvIncomeDetails incomeDetails = optional.get();
	aocpvIncomeDetails.setId(incomeDetails.getId());
	aocpvIncomeDetails.setPanCardResponse(incomeDetails.getPanCardResponse());
	aocpvIncomeDetails.setVoterIdResponse(incomeDetails.getVoterIdResponse());
	aocpvIncomeDetails.setDrivingLicenseResponse(incomeDetails.getDrivingLicenseResponse());
	aocpvIncomeDetails.setPassportResponse(incomeDetails.getPassportResponse());
		}
//		if(aocpvIncomeDetails.getPrimarySourceOfIncome().equalsIgnoreCase("YES")) {
//			List<AocpvIncomeDetails> list = aocpvIncomeDetailsRepository.getByApp(aocpvIncomeDetails.getApplicationNo());
//			if(list.size()!=0) {
//				for(AocpvIncomeDetails a:list) {
//					if(a.getPrimarySourceOfIncome().equalsIgnoreCase("YES") && a.getMember() != aocpvIncomeDetails.getMember()) {
//						throw new DuplicateEntryException("primary source of income already present");
//					}
//				}
//			}
//		}
	
		aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		return "Data Save";
	}

	@Override
	public void fetchByPancardNo(String panCardNo) {
		try {
			Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.getByPanCard(panCardNo);
			if(optional.isPresent()) {
				throw new DuplicateEntryException("pancard no is already exist");
			}
		}catch (Exception e) {
			throw new DuplicateEntryException("pancard no is already exist");
		}
		
	}
	@Override
	public void fetchByVoterId(String voterId) {
		try {
			Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.getByVoterId(voterId);
			if(optional.isPresent()) {
				throw new DuplicateEntryException("voterId is already exist");
			}
		}catch (Exception e) {
			throw new DuplicateEntryException("voterId is already exist");
		}
		
	}
	@Override
	public void fetchByAadharCard(String aadhar) {
		try {
			Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.getByAadharCard(aadhar);
			if(optional.isPresent()) {
				throw new DuplicateEntryException("aadhar no is already exist");
			}
		}catch (Exception e) {
			throw new DuplicateEntryException("aadhar no is already exist");
		}
		
	}

	@Override
	public String deleteMember(long applicationNoInLong, String member) {
		if(member.equalsIgnoreCase("Self")) {
			throw new NoSuchElementException("self member can not delete");
		}
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNoInLong, member);
		if(optional.isPresent()) {
			aocpvIncomeDetailsRepository.deleteByApp(applicationNoInLong,member);
			return "Delete Successfully";
		}
		throw new NoSuchElementException("member Not Found");
	}

	@Override
	public int getByApplicationNoAndmember(long applicationNoInLong) {
		
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNoInLong,"Self");
		if(optional.isPresent()) {
			if(optional.get().getFirstName()==null) {
				return 2;
			}
			return 1;
		}
		
		return 2;
	}

	@Override
	public void updateversionCode(long applicationNoInLong, int versioncode) {
		
		List<AocpvIncomeDetails> list = aocpvIncomeDetailsRepository.getByApp(applicationNoInLong);
		if(list.size()==0) {
			
		}
		else {
			aocpvIncomeDetailsRepository.updateVersionCode(applicationNoInLong,versioncode);
		}
	}

	@Override
	public List<AocpvIncomeDetails> getByApplicationAndVersion(long applicationNo, int versioncode) {
		List<AocpvIncomeDetails> list=aocpvIncomeDetailsRepository.getByAppAndVersionCode(applicationNo,versioncode);
		if(list.size()==0) {
			throw new NoSuchElementException("member Not Found");
		}
		return list;
	}
	
	@Override
	public String updateMobileVerify(long applicationNoInLong, String member) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNoInLong, member);
		if(optional.isPresent()) {
			AocpvIncomeDetails aocpvIncomeDetails = optional.get();
			aocpvIncomeDetails.setMobileVerify("YES");
			aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		}
		else {
			throw new NoSuchElementException("member Not Found");
		}
		return "update sucessfully";
	}

	@Override
	public AocpvIncomeDetails getByApplicationNoAndmember(long applicationNoInLong, String string) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNoInLong, string);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("member Not Found");
	}

	@Override
	public void savePanResponse(String panResponse, String applicationNo, String member, String isVerify, JSONObject jsonRequest) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(Long.parseLong(applicationNo), member);
		if(optional.isPresent()) {
			AocpvIncomeDetails incomeDetails = optional.get();
			incomeDetails.setPanCardResponse(panResponse);
			incomeDetails.setPancardNoVerify(isVerify);
			incomeDetails.setPanCard(jsonRequest.getString("Pan"));
			incomeDetails.setNameOnnameOnPancard(jsonRequest.getString("Name"));
			incomeDetails.setDobONPancard(jsonRequest.getString("DateOfBirth"));
			aocpvIncomeDetailsRepository.save(incomeDetails);
		}
		else {
			int id=1;
			AocpvIncomeDetails aocpvIncomeDetails =new AocpvIncomeDetails();
			aocpvIncomeDetails.setPanCardResponse(panResponse);
			aocpvIncomeDetails.setApplicationNo(Long.parseLong(applicationNo));
			aocpvIncomeDetails.setMember(member);
			aocpvIncomeDetails.setPancardNoVerify(isVerify);
			aocpvIncomeDetails.setNameOnnameOnPancard(jsonRequest.getString("Name"));
			aocpvIncomeDetails.setDobONPancard(jsonRequest.getString("DateOfBirth"));
			aocpvIncomeDetails.setPanCard(jsonRequest.getString("Pan"));
			Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
			if(optional1.isPresent()) {
				id=	optional1.get();
				id++;
			}
			aocpvIncomeDetails.setId(id);
			aocpvIncomeDetails.setVersionCode(1);
			aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		}
	}

	@Override
	public void savevoterIdResponse(String voterIdResponse, String applicationNo, String member) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(Long.parseLong(applicationNo), member);
		if(optional.isPresent()) {
			AocpvIncomeDetails incomeDetails = optional.get();
			incomeDetails.setVoterIdResponse(voterIdResponse);
			aocpvIncomeDetailsRepository.save(incomeDetails);
		}
		else {
			int id=1;
			AocpvIncomeDetails aocpvIncomeDetails =new AocpvIncomeDetails();
			aocpvIncomeDetails.setVoterIdResponse(voterIdResponse);
			aocpvIncomeDetails.setApplicationNo(Long.parseLong(applicationNo));
			aocpvIncomeDetails.setMember(member);
			Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
			if(optional1.isPresent()) {
				id=	optional1.get();
				id++;
			}
			aocpvIncomeDetails.setId(id);
			aocpvIncomeDetails.setVersionCode(1);
			aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		}
	}

	@Override
	public void savePassport(String passportResponse, String applicationNo, String member) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(Long.parseLong(applicationNo), member);
		if(optional.isPresent()) {
			AocpvIncomeDetails incomeDetails = optional.get();
			incomeDetails.setPassportResponse(passportResponse);
			aocpvIncomeDetailsRepository.save(incomeDetails);
		}
		else {
			int id=1;
			AocpvIncomeDetails aocpvIncomeDetails =new AocpvIncomeDetails();
			aocpvIncomeDetails.setPassportResponse(passportResponse);
			aocpvIncomeDetails.setApplicationNo(Long.parseLong(applicationNo));
			aocpvIncomeDetails.setMember(member);
			Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
			if(optional1.isPresent()) {
				id=	optional1.get();
				id++;
			}
			aocpvIncomeDetails.setId(id);
			aocpvIncomeDetails.setVersionCode(1);
			aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		}
		
	}

	@Override
	public void saveDrivingLicense(String drivingLicenseReponse, String applicationNo, String member) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(Long.parseLong(applicationNo), member);
		if(optional.isPresent()) {
			AocpvIncomeDetails incomeDetails = optional.get();
			incomeDetails.setDrivingLicenseResponse(drivingLicenseReponse);
			aocpvIncomeDetailsRepository.save(incomeDetails);
		}
		else {
			int id=1;
			AocpvIncomeDetails aocpvIncomeDetails =new AocpvIncomeDetails();
			aocpvIncomeDetails.setDrivingLicenseResponse(drivingLicenseReponse);
			aocpvIncomeDetails.setApplicationNo(Long.parseLong(applicationNo));
			aocpvIncomeDetails.setMember(member);
			Optional<Integer>optional1=aocpvIncomeDetailsRepository.fetchLastId();
			if(optional1.isPresent()) {
				id=	optional1.get();
				id++;
			}
			aocpvIncomeDetails.setId(id);
			aocpvIncomeDetails.setVersionCode(1);
			aocpvIncomeDetailsRepository.save(aocpvIncomeDetails);
		}
		
	}

	@Override
	public void save(AocpvIncomeDetails incomeDetails) {
		aocpvIncomeDetailsRepository.save(incomeDetails);
		
	}

	@Override
	public AocpvIncomeDetails getByAppNoAndmember(long applicationNoInLong, String member) {
		Optional<AocpvIncomeDetails> optional = aocpvIncomeDetailsRepository.find(applicationNoInLong, member);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}}
