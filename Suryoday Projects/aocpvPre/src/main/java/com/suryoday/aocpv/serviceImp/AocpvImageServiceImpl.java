package com.suryoday.aocpv.serviceImp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.repository.AocpvImageRepository;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
@Service
public class AocpvImageServiceImpl implements AocpvImageService{
	
	Logger logger = LoggerFactory.getLogger(AocpvImageServiceImpl.class);
	@Autowired
	AocpvImageRepository imageRepository;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@Override
	public String saveSingleData(AocpvImages aocpvImages) {
		if(aocpvImages == null ) {
			throw new EmptyInputException("field is empty");
		}
			long applicationNo = aocpvImages.getApplicationNo();
			String flowStatus = aocpvImages.getFlowStatus();
			String imageName = aocpvImages.getImageName();
			int id=1;			
	Optional<AocpvImages> optional	=imageRepository.getByApplicationNoAndStatus(applicationNo,flowStatus);
			if(optional.isPresent()) {
				//imageRepository.updateImage(applicationNo,flowStatus,aocpvImages.getImages());
				System.out.println("update");
			}
			else {
				imageRepository.save(aocpvImages);
				System.out.println("save");
			}
		
		return "done";
	}
	
	

	@Override
	public void saveAll(List<AocpvImages> list) {
		int count=0;
		for(AocpvImages aocpvImages : list) {
			long applicationNo = aocpvImages.getApplicationNo();
			String imageName = aocpvImages.getImageName();
			
	Optional<AocpvImages> optional	=imageRepository.getByApplicationNoAndName(applicationNo,imageName);
			if(optional.isPresent()) {
				System.out.println(optional.get());
				//imageRepository.updateImageByName(applicationNo,imageName,aocpvImages.getImages());
				count++;
		}
			
		}
		if(count ==0) {
			imageRepository.saveAll(list);
		}
		
	}

	@Override
	public AocpvImages getByApplicationNo(long applicationNo) {
		//List<AocpvImages> list =imageRepository.findByApplicationNo(applicationNo);
		Optional<AocpvImages> optional = imageRepository.getByApplicationNoAndName(applicationNo,"customerPhoto");
		//if(list.isEmpty()) {
		//	throw new NoSuchElementException("list is empty");
		//}
		return optional.get();
	}
	@Override
	public List<AocpvImages> getByApplicationNoAndMember(long applicationNo,String member,String flowStatus) {
		//String flowStatus="UD";
		List<AocpvImages> list =imageRepository.getByApplicationNoAndMember(applicationNo,member,flowStatus);
//		if(list.size()==0) {
//			throw new NoSuchElementException("list is empty Image Table");	
//		}
		return list;
	}


	@Override
public void saveCustomerPhoto(MultipartFile[] files, long applicationNo, JSONArray document, String flowStatus) {
		
		for(MultipartFile file : files ) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String customerPhoto=null;
		String fileNameInRequest=null;
		String ekyc_photo=null;
		String ekyc_aadhar=null;
		String Documenttype=null;
		JSONObject jsonObject2=null;
		int id=1;
		JSONObject jsonObject=null;
		if(document.isEmpty()) {
			
		}
		else {
			for(int n=0 ; n<document.length();n++) {
				AocpvImages aocpvImages=new AocpvImages();
				JSONObject object = document.getJSONObject(n);
				if(object.has("ekyc_photo")) {
					 jsonObject2 = object.getJSONObject("ekyc_photo");
					ekyc_photo=object.getJSONObject("ekyc_photo").toString();
					 jsonObject=new JSONObject(ekyc_photo);
					 aocpvImages.setGeoLocation(ekyc_photo);
					 Documenttype="ekyc_photo";
				}
				else if(object.has("ekyc_aadhar")) {
					jsonObject2 = object.getJSONObject("ekyc_aadhar");
					ekyc_aadhar=object.getJSONObject("ekyc_aadhar").toString();
					 jsonObject=new JSONObject(ekyc_aadhar);
					 aocpvImages.setGeoLocation(ekyc_aadhar);
					 Documenttype="ekyc_aadhar";
				}
				else if(object.has("customerPhoto")) {
					jsonObject2 = object.getJSONObject("customerPhoto");
					customerPhoto=object.getJSONObject("customerPhoto").toString();
					 jsonObject=new JSONObject(customerPhoto);
					 aocpvImages.setGeoLocation(customerPhoto);
					 Documenttype="customerPhoto";
				}
				
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
//					if(lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
//						 throw new NoSuchElementException("enter valid lat long in "+Documenttype);	
//				}
				
				 fileNameInRequest = jsonObject.getString("image");
					
		 			if(fileName.equals(fileNameInRequest)) {
				try {
				aocpvImages.setApplicationNo(applicationNo);
				aocpvImages.setFlowStatus(flowStatus);
				aocpvImages.setImageName(fileName);
				aocpvImages.setType(file.getContentType());	
				aocpvImages.setImages(file.getBytes());
				aocpvImages.setDocumenttype(Documenttype);
				aocpvImages.setSize(file.getSize());
				aocpvImages.setMember("SELF");
				Optional<Integer>optional1=imageRepository.fetchLastId();
				if(optional1.isPresent()) {
					 id = optional1.get();
					id++;
					aocpvImages.setId(id);
				}
				int versioncode=1;
		Optional<AocpvImages> optional=imageRepository.getByApplicationNoAndName(applicationNo, Documenttype);
				if(optional.isPresent()) {
					AocpvImages aocpvImage = optional.get();
					aocpvImages.setId(aocpvImage.getId());
					versioncode = aocpvImage.getVersioncode();
					
			//		versioncode++;	
				}
				aocpvImages.setVersioncode(versioncode);
				if(aocpvImages.getGeoLocation() != null) {
					imageRepository.save(aocpvImages);	
				}
	
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	 
		 
		 
}
}

}

}

	@Override
	public void savePhotos(MultipartFile[] files, long applicationNoInLong, JSONArray document, String flowStatus) {
		String Documenttype= null;
		for(MultipartFile file : files ) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			JSONObject jsonObject2=null;
			int id=1;
			JSONObject jsonObject=null;
			if(document.isEmpty()) {
				
			}
			else {
				for(int n=0 ; n<document.length();n++) {
					AocpvImages aocpvImages=new AocpvImages();
					JSONObject object = document.getJSONObject(n);
					if(object.has("utilityBillPhoto")) {
						 jsonObject2 = object.getJSONObject("utilityBillPhoto");
						String utilityBillPhoto=object.getJSONObject("utilityBillPhoto").toString();
						 jsonObject=new JSONObject(utilityBillPhoto);
						 aocpvImages.setGeoLocation(utilityBillPhoto);
						 Documenttype="utilityBillPhoto";
					}
					else if(object.has("houseImageInside")) {
						 jsonObject2 = object.getJSONObject("houseImageInside");
						String houseImageInside=object.getJSONObject("houseImageInside").toString();
						 jsonObject=new JSONObject(houseImageInside);
						 aocpvImages.setGeoLocation(houseImageInside);
						 Documenttype="houseImageInside";
					}
					else if(object.has("houseImageOutside")) {
						 jsonObject2 = object.getJSONObject("houseImageOutside");
						String houseImageOutside=object.getJSONObject("houseImageOutside").toString();
						 jsonObject=new JSONObject(houseImageOutside);
						 aocpvImages.setGeoLocation(houseImageOutside);
						 Documenttype="houseImageOutside";
					}
					else if(object.has("buisnessPhoto")) {
						 jsonObject2 = object.getJSONObject("buisnessPhoto");
						String buisnessPhoto=object.getJSONObject("buisnessPhoto").toString();
						 jsonObject=new JSONObject(buisnessPhoto);
						 aocpvImages.setGeoLocation(buisnessPhoto);
						 Documenttype="buisnessPhoto";
					}
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
//					if(lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
//						 throw new NoSuchElementException("enter valid lat long in "+Documenttype);		
//				}
						String fileNameInRequest = jsonObject.getString("image");
					
						 			if(fileName.equals(fileNameInRequest)) {
								try {
								aocpvImages.setApplicationNo(applicationNoInLong);
								aocpvImages.setFlowStatus(flowStatus);
								aocpvImages.setImageName(fileName);
								aocpvImages.setType(file.getContentType());	
								aocpvImages.setImages(file.getBytes());
								aocpvImages.setDocumenttype(Documenttype);
								aocpvImages.setSize(file.getSize());
								Optional<Integer>optional1=imageRepository.fetchLastId();
								if(optional1.isPresent()) {
									 id = optional1.get();
									id++;
									aocpvImages.setId(id);
								}
								int versioncode=1;
						Optional<AocpvImages> optional=imageRepository.getByApplicationNoAndName(applicationNoInLong, Documenttype);
								if(optional.isPresent()) {
									AocpvImages aocpvImage = optional.get();
									aocpvImages.setId(aocpvImage.getId());
									 versioncode = aocpvImage.getVersioncode();
								//	versioncode++;
								}
								aocpvImages.setVersioncode(versioncode);
								if(aocpvImages.getGeoLocation() != null) {
									imageRepository.save(aocpvImages);	
								}
									
								} catch (IOException e) {
									e.printStackTrace();
								}
						}	 
						 
						 
				}
			}
			
		}
		
	}



	@Override
	public void savePhotoIncome(MultipartFile[] files, long applicationNoInLong, JSONArray document, String flowStatus,	
			String member) {
		
		
		try
		{
			
			logger.debug("savePhotoIncome start");
		String Documenttype= null;
		for(MultipartFile file : files ) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			logger.debug("fileName start"+fileName);
			String panCardPhoto=null;
			String aadharFront=null;
			String aadharBack=null;
			int id;
			String fileNameInRequest=null;
			JSONObject jsonObject=null;
			JSONObject jsonObject2=null;
			
			if(document.isEmpty()) {
				
			}
			else {
				for(int n=0 ; n<document.length();n++) {
					AocpvImages aocpvImages=new AocpvImages();
					JSONObject object = document.getJSONObject(n);
					//logger.debug("object start"+object.toString());
					if(object.has("panCardPhoto")) {
						 jsonObject2 = object.getJSONObject("panCardPhoto");
						panCardPhoto=object.getJSONObject("panCardPhoto").toString();
						 jsonObject=new JSONObject(panCardPhoto);
						 aocpvImages.setGeoLocation(panCardPhoto);
						 Documenttype="panCardPhoto";
					}
					else if(object.has("aadharFront")) {
						 jsonObject2 = object.getJSONObject("aadharFront");
						aadharFront=object.getJSONObject("aadharFront").toString();
						 jsonObject=new JSONObject(aadharFront);
						 aocpvImages.setGeoLocation(aadharFront);
						 Documenttype="aadharFront";
					}
					else if(object.has("aadharBack")) {
						 jsonObject2 = object.getJSONObject("aadharBack");
						aadharBack=object.getJSONObject("aadharBack").toString();
						 jsonObject=new JSONObject(aadharBack);
						 aocpvImages.setGeoLocation(aadharBack);
						 Documenttype="aadharBack";
					}
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
//					if(lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
//						 throw new NoSuchElementException("enter valid lat long in "+Documenttype);		
//				}
						 fileNameInRequest = jsonObject.getString("image");
						 logger.debug("fileNameInRequest start"+fileNameInRequest);
						 			if(fileName.equals(fileNameInRequest)) {
								try {
									 logger.debug("fileNameInRequest start condition"+fileName.equals(fileNameInRequest));
								aocpvImages.setApplicationNo(applicationNoInLong);
								aocpvImages.setFlowStatus(flowStatus);
								aocpvImages.setImageName(fileName);
								aocpvImages.setType(file.getContentType());	
								aocpvImages.setImages(file.getBytes());
								aocpvImages.setDocumenttype(Documenttype);
								aocpvImages.setSize(file.getSize());
								aocpvImages.setMember(member);
								Optional<Integer>optional1=imageRepository.fetchLastId();
								if(optional1.isPresent()) {
									 id = optional1.get();
									id++;
									aocpvImages.setId(id);
								}
								int versioncode=1;
						Optional<AocpvImages> optional=imageRepository.getByApplicationNoMember(applicationNoInLong, Documenttype,member);
								if(optional.isPresent()) {
									AocpvImages aocpvImage = optional.get();
									aocpvImages.setId(aocpvImage.getId());
									 versioncode = aocpvImage.getVersioncode();
								//	versioncode++;
									
								}
								aocpvImages.setVersioncode(versioncode);
								 logger.debug("db call start");
								 if(aocpvImages.getGeoLocation() != null) {
										imageRepository.save(aocpvImages);	
									}
								
								logger.debug("Id photo save");
								} catch (IOException e) {
									e.printStackTrace();
									logger.debug("inside exception"+e.getMessage());
								}
						}	 
						 
						 
				}
			}
			
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.debug("Exception"+e.getMessage());
		}
		
		logger.debug("savePhotoIncome End");
	}

	@Override
    public String getGeoLoactionByApplication(long applicationNo) {
		String documenttype="houseImageInside";
       Optional<String> optional = imageRepository.getGeoLoactionByAppln(applicationNo,documenttype);
       if(optional.isPresent()) {
    	   return optional.get();
       }
       throw new NoSuchElementException("NO Record found");
    }



	@Override
	public AocpvImages getImageByApplication(Long application) {

		  String documenttype = "customerPhoto";
	        String member = "self";
	        
	        Optional<AocpvImages> optional = imageRepository.getByApplicationNoMember(application,documenttype,member);
	           if(optional.isPresent()) {
	               return optional.get();
	           }
	           throw new NoSuchElementException("NO Record found in image table");
	}



	@Override
	public String saveMultipleImages(MultipartFile[] files, JSONArray document, long applicationNo,String member) {
		for(MultipartFile file : files ) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String Documenttype=null;
			String flowStatus=null;
			
			JSONObject jsonObject2=null;
			int id=1;
			JSONObject jsonObject=null;
			if(member.equals("")) {
				member="SELF";
			}
			if(document.isEmpty()) {
				
			}
			else {
				for(int n=0 ; n<document.length();n++) {
					AocpvImages aocpvImages=new AocpvImages();
					JSONObject object = document.getJSONObject(n);
					if(object.has("utilityBillPhoto")) {
						 jsonObject2 = object.getJSONObject("utilityBillPhoto");
					String	utilityBillPhoto=object.getJSONObject("utilityBillPhoto").toString();
						 jsonObject=new JSONObject(utilityBillPhoto);
						 aocpvImages.setGeoLocation(utilityBillPhoto);
						 flowStatus="UD";
						
						 Documenttype="utilityBillPhoto";
					}
					else if(object.has("houseImageInside")) {
						 jsonObject2 = object.getJSONObject("houseImageInside");
						 String	houseImageInside=object.getJSONObject("houseImageInside").toString();
						 jsonObject=new JSONObject(houseImageInside);
						 aocpvImages.setGeoLocation(houseImageInside);
						 flowStatus="UD";
					
						 Documenttype="houseImageInside";
					}
					else if(object.has("houseImageOutside")) {
						 jsonObject2 = object.getJSONObject("houseImageOutside");
						String houseImageOutside=object.getJSONObject("houseImageOutside").toString();
						 jsonObject=new JSONObject(houseImageOutside);
						 aocpvImages.setGeoLocation(houseImageOutside);
						 flowStatus="UD";
						 Documenttype="houseImageOutside";
					}
					else if(object.has("roadsideImage")) {
						 jsonObject2 = object.getJSONObject("roadsideImage");
						String houseImageOutside=object.getJSONObject("roadsideImage").toString();
						 jsonObject=new JSONObject(houseImageOutside);
						 aocpvImages.setGeoLocation(houseImageOutside);
						 flowStatus="UD";
						 Documenttype="roadsideImage";
					}
					else if(object.has("kycPhoto")) {
						 jsonObject2 = object.getJSONObject("kycPhoto");
						 String	kycPhoto=object.getJSONObject("kycPhoto").toString();
						 jsonObject=new JSONObject(kycPhoto);
						 aocpvImages.setGeoLocation(kycPhoto);
						 flowStatus="UD";
					
						 Documenttype="kycPhoto";
					}
					else if(object.has("customerPhoto")) {
						 jsonObject2 = object.getJSONObject("customerPhoto");
						 String customerPhoto=object.getJSONObject("customerPhoto").toString();
						 jsonObject=new JSONObject(customerPhoto);
						 aocpvImages.setGeoLocation(customerPhoto);
						 flowStatus="PD";
						
						 Documenttype="customerPhoto";
					}
					else if(object.has("AO_Selfie")) {
						 jsonObject2 = object.getJSONObject("AO_Selfie");
						 String AO_Selfie=object.getJSONObject("AO_Selfie").toString();
						 jsonObject=new JSONObject(AO_Selfie);
						 aocpvImages.setGeoLocation(AO_Selfie);
						 flowStatus="UD";
						 Documenttype="AO_Selfie";
					}
					else if(object.has("RO_Selfie")) {
						 jsonObject2 = object.getJSONObject("RO_Selfie");
						 String RO_Selfie=object.getJSONObject("RO_Selfie").toString();
						 jsonObject=new JSONObject(RO_Selfie);
						 aocpvImages.setGeoLocation(RO_Selfie);
						 flowStatus="UD";
						 Documenttype="RO_Selfie";
					}
					else if(object.has("Loan_Agreement_Verify")) {
						 jsonObject2 = object.getJSONObject("Loan_Agreement_Verify");
						 String Loan_Agreement_Verify=object.getJSONObject("Loan_Agreement_Verify").toString();
						 jsonObject=new JSONObject(Loan_Agreement_Verify);
						 aocpvImages.setGeoLocation(Loan_Agreement_Verify);
						 flowStatus="UD";
						 Documenttype="Loan_Agreement_Verify";
						 AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNo);
						// aocpvLoanCreation.setIsVerify("YES");
						 aocpvLoanCreation.setDocumentVerify("physically");
						 aocpvLoanCreation.setUpload_aggreement_letter("Yes");
						 aocpvLoanCreationService.saveData(aocpvLoanCreation);
					}
					else if(object.has("Sanction_letter_verify")) {
						 jsonObject2 = object.getJSONObject("Sanction_letter_verify");
						 String Sanction_letter_verify=object.getJSONObject("Sanction_letter_verify").toString();
						 jsonObject=new JSONObject(Sanction_letter_verify);
						 aocpvImages.setGeoLocation(Sanction_letter_verify);
						 flowStatus="UD";
						 Documenttype="Sanction_letter_verify";
						 AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNo);
						// aocpvLoanCreation.setIsVerify("YES");

						 aocpvLoanCreation.setUpload_sancation_letter("YES");
						 aocpvLoanCreationService.saveData(aocpvLoanCreation);
					}
					else if(object.has("panCardPhoto")) {
						 jsonObject2 = object.getJSONObject("panCardPhoto");
						 String panCardPhoto=object.getJSONObject("panCardPhoto").toString();
						 jsonObject=new JSONObject(panCardPhoto);
						 aocpvImages.setGeoLocation(panCardPhoto);
						 flowStatus="ID";
						
						 Documenttype="panCardPhoto";
					}
					else if(object.has("aadharFront")) {
						 jsonObject2 = object.getJSONObject("aadharFront");
						 String aadharFront=object.getJSONObject("aadharFront").toString();
						 jsonObject=new JSONObject(aadharFront);
						 flowStatus="ID";
						
						 aocpvImages.setGeoLocation(aadharFront);
						 Documenttype="aadharFront";
					}
					else if(object.has("aadharBack")) {
						 jsonObject2 = object.getJSONObject("aadharBack");
						 String aadharBack=object.getJSONObject("aadharBack").toString();
						 jsonObject=new JSONObject(aadharBack);
						 aocpvImages.setGeoLocation(aadharBack);
						
						 flowStatus="ID";
						 Documenttype="aadharBack";
					}
					else if(object.has("buisnessPhoto")) {
						 jsonObject2 = object.getJSONObject("buisnessPhoto");
						 String	buisnessPhoto=object.getJSONObject("buisnessPhoto").toString();
						 jsonObject=new JSONObject(buisnessPhoto);
						 aocpvImages.setGeoLocation(buisnessPhoto);
						 flowStatus="UD";
						
						 Documenttype="buisnessPhoto";
					}
					else if(object.has("form60Photo")) {
						 jsonObject2 = object.getJSONObject("form60Photo");
						 String	buisnessPhoto=object.getJSONObject("form60Photo").toString();
						 jsonObject=new JSONObject(buisnessPhoto);
						 aocpvImages.setGeoLocation(buisnessPhoto);
						 flowStatus="ID";
						 Documenttype="form60Photo";
					}
					else if(object.has("voterIdCardPhoto")) {
						 jsonObject2 = object.getJSONObject("voterIdCardPhoto");
						 String	buisnessPhoto=object.getJSONObject("voterIdCardPhoto").toString();
						 jsonObject=new JSONObject(buisnessPhoto);
						 aocpvImages.setGeoLocation(buisnessPhoto);
						 flowStatus="ID";
						 Documenttype="voterIdCardPhoto";
					}
					else if(object.has("signaturePhoto")) {
						 jsonObject2 = object.getJSONObject("signaturePhoto");
						 String	buisnessPhoto=object.getJSONObject("signaturePhoto").toString();
						 jsonObject=new JSONObject(buisnessPhoto);
						 aocpvImages.setGeoLocation(buisnessPhoto);
						 flowStatus="ID";
						 Documenttype="signaturePhoto";
					}
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
//					if(lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
//						 throw new NoSuchElementException("enter valid lat long in "+Documenttype);		
//				}
					String	 fileNameInRequest = jsonObject.getString("image");
					
						 			if(fileName.equals(fileNameInRequest)) {
								try {
								aocpvImages.setApplicationNo(applicationNo);
								aocpvImages.setFlowStatus(flowStatus);
								aocpvImages.setImageName(fileName);
								aocpvImages.setMember(member);
								aocpvImages.setType(file.getContentType());	
								aocpvImages.setImages(file.getBytes());
								aocpvImages.setDocumenttype(Documenttype);
								aocpvImages.setSize(file.getSize());
								Optional<Integer>optional1=imageRepository.fetchLastId();
								if(optional1.isPresent()) {
									 id = optional1.get();
									id++;
									aocpvImages.setId(id);
								}
								int versioncode=1;
								Optional<AocpvImages> optional=null;
								if(flowStatus.equals("UD")){
									 optional=imageRepository.getByApplicationNoAndName(applicationNo, Documenttype);
								}
								else {
									 optional=imageRepository.getByApplicationNoMember(applicationNo, Documenttype,member);
								}
								if(optional.isPresent()) {
									AocpvImages aocpvImage = optional.get();
									if(Documenttype.equals("buisnessPhoto")) {
										Documenttype="buisnessPhoto_1";
									}
									else {
										aocpvImages.setId(aocpvImage.getId());	
									}
//									else if(Documenttype.equals("utilityBillPhoto")) {
//										Documenttype="utilityBillPhoto_1";
//									}
									aocpvImages.setDocumenttype(Documenttype);
								 versioncode = aocpvImage.getVersioncode();
							//		versioncode++;
								}
								aocpvImages.setVersioncode(versioncode);
								if(aocpvImages.getGeoLocation() != null) {
									imageRepository.save(aocpvImages);	
								}
									
								} catch (IOException e) {
									e.printStackTrace();
								}
						}	 
						 
						 
				}
			}
			
		}
		return "Image saved";
	}

	@Override
	public void saveAadharPhoto(Map<String, String> files, long applicationNoInLong, JSONArray document, String flowStatus) {
		
			String fileName = "";
			String fileNameInRequest = null;
			String ekyc_photo = null;
			String ekyc_aadhar = null;
			String Documenttype = null;
			String content = "";
			String file="";
			JSONObject jsonObject2 = null;
			int id = 1;
			JSONObject jsonObject = null;
			if (document.isEmpty()) {

			} else {
				for (int n = 0; n < document.length(); n++) {
					AocpvImages aocpvImages = new AocpvImages();
					JSONObject object = document.getJSONObject(n);
					if (object.has("ekyc_photo")) {
						jsonObject2 = object.getJSONObject("ekyc_photo");
						ekyc_photo = object.getJSONObject("ekyc_photo").toString();
						jsonObject = new JSONObject(ekyc_photo);
						fileName = "photo.jpg";
						content = "jpg/*";
						file=files.get("ekycPhoto");
						aocpvImages.setGeoLocation(ekyc_photo);
						Documenttype = "ekyc_photo";
					} else if (object.has("ekyc_aadhar")) {
						jsonObject2 = object.getJSONObject("ekyc_aadhar");
						ekyc_aadhar = object.getJSONObject("ekyc_aadhar").toString();
						jsonObject = new JSONObject(ekyc_aadhar);
						fileName = "photo.pdf";
						file=files.get("ekycAadhar");
						content = "pdf/*";
						aocpvImages.setGeoLocation(ekyc_aadhar);
						Documenttype = "ekyc_aadhar";
					}

//					String lat = jsonObject2.getString("Lat");
//					String plong = jsonObject2.getString("Long");
//					if (lat.isEmpty() || plong.isEmpty()) {
//						throw new NoSuchElementException("enter valid lat long in " + Documenttype);
//					}

					fileNameInRequest = jsonObject.getString("image");
					  byte[] bytes = Base64.getDecoder().decode(file);
					//byte[] bytes = file.getBytes();
					int length = bytes.length;
					if (fileName.equals(fileNameInRequest)) {
						try {
							aocpvImages.setApplicationNo(applicationNoInLong);
							aocpvImages.setFlowStatus(flowStatus);
							aocpvImages.setImageName(fileName);
							aocpvImages.setType(content);
							aocpvImages.setImages(bytes);
							aocpvImages.setDocumenttype(Documenttype);
							aocpvImages.setSize(length);
							aocpvImages.setMember("SELF");
							Optional<Integer> optional1 = imageRepository.fetchLastId();
							if (optional1.isPresent()) {
								id = optional1.get();
								id++;
								aocpvImages.setId(id);
							}
							int versioncode = 1;
							Optional<AocpvImages> optional = imageRepository
									.getByApplicationNoAndName(applicationNoInLong, Documenttype);
							if (optional.isPresent()) {
								AocpvImages aocpvImage = optional.get();
								aocpvImages.setId(aocpvImage.getId());
								versioncode = aocpvImage.getVersioncode();
							//	versioncode++;
							}
							aocpvImages.setVersioncode(versioncode);
							if(aocpvImages.getGeoLocation() != null) {
								imageRepository.save(aocpvImages);	
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}

		

	}



	@Override
	public List<Image> fetchByApplicationNoAndFlow(long applicationNoInLong, String flowStatus) {
		List<AocpvImages> list = imageRepository.getByApplicationNoAndflowStatus(applicationNoInLong, flowStatus);
		List<Image> list1=new ArrayList<>();
		if(list.size() == 0) {
			return list1;
		}
		for(AocpvImages aocpvImages:list) {
			
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
	 			
	 		list1.add(images);						
	}
		return list1;
	}



	@Override
	public void updateversionCode(long applicationNoInLong, int versioncode) {
	
		List<AocpvImages> list = imageRepository.findByApplicationNo(applicationNoInLong);
		if(list.size()==0) {
			
		}
		else {
			imageRepository.updateVersionCode(applicationNoInLong,versioncode);
		}
	}




	@Override
	public List<AocpvImages> getByappAndVersion(long applicationNo, String member, int versioncode) {
		List<AocpvImages> list=imageRepository.getByappAndVersion(applicationNo,member,versioncode);
		if(list.size()==0) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}



	@Override
	public List<AocpvImages> getByapplicationAndVersion(long applicationNo, int versioncode) {
		List<AocpvImages> list=imageRepository.getByapplicationAndVersion(applicationNo,versioncode);
		if(list.size()==0) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}



	@Override
	public String savePdf(byte[] image, JSONObject object, long applicationNoInLong) {
		//String fileName = "";
		String fileNameInRequest = null;
		String ekyc_photo = null;
		String ekyc_aadhar = null;
		String Documenttype = null;
		String flowStatus="PDF";
		String content = "";
		JSONObject jsonObject2 = null;
		int id = 1;
		JSONObject jsonObject = null;
		
				AocpvImages aocpvImages = new AocpvImages();
				if (object.has("cifPdf")) {
					jsonObject2 = object.getJSONObject("cifPdf");
					ekyc_aadhar = object.getJSONObject("cifPdf").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
					//fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "cifPdf";
				}
				else if(object.has("accountOpenpdf")) {
					jsonObject2 = object.getJSONObject("accountOpenpdf");
					ekyc_aadhar = object.getJSONObject("accountOpenpdf").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
				//	fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "accountOpenpdf";
				}
				else if(object.has("agreementpdf")) {
					jsonObject2 = object.getJSONObject("agreementpdf");
					ekyc_aadhar = object.getJSONObject("agreementpdf").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
				//	fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "agreementpdf";
				}
				else if(object.has("sanctionLetter")) {
					jsonObject2 = object.getJSONObject("sanctionLetter");
					ekyc_aadhar = object.getJSONObject("sanctionLetter").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
				//	fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "sanctionLetter";
				}
				else if(object.has("onePager")) {
					jsonObject2 = object.getJSONObject("onePager");
					ekyc_aadhar = object.getJSONObject("onePager").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
				//	fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "onePager";
				}
				else if(object.has("pasbookPdf")) {
					jsonObject2 = object.getJSONObject("pasbookPdf");
					ekyc_aadhar = object.getJSONObject("pasbookPdf").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
				//	fileName = "photo.pdf";
					content = "pdf/*";
					aocpvImages.setGeoLocation(ekyc_aadhar);
					Documenttype = "pasbookPdf";
				}
				String lat = jsonObject2.getString("Lat");
				String plong = jsonObject2.getString("Long");
				if (lat.isEmpty() || plong.isEmpty()) {
					throw new NoSuchElementException("enter valid lat long in " + Documenttype);
				}

				fileNameInRequest = jsonObject.getString("image");
				int length = image.length;
			//	if (fileName.equals(fileNameInRequest)) {
					try {
						aocpvImages.setApplicationNo(applicationNoInLong);
						aocpvImages.setFlowStatus(flowStatus);
						aocpvImages.setImageName(fileNameInRequest);
						aocpvImages.setType(content);
						aocpvImages.setImages(image);
						aocpvImages.setDocumenttype(Documenttype);
						aocpvImages.setSize(length);
						aocpvImages.setMember("SELF");
						Optional<Integer> optional1 = imageRepository.fetchLastId();
						if (optional1.isPresent()) {
							id = optional1.get();
							id++;
							aocpvImages.setId(id);
						}
						int versioncode = 1;
						Optional<AocpvImages> optional = imageRepository
								.getByApplicationNoAndName(applicationNoInLong, Documenttype);
						if (optional.isPresent()) {
							AocpvImages aocpvImage = optional.get();
							aocpvImages.setId(aocpvImage.getId());
							versioncode = aocpvImage.getVersioncode();
						//	versioncode++;
						}
						aocpvImages.setVersioncode(versioncode);
						if(aocpvImages.getGeoLocation() != null) {
							imageRepository.save(aocpvImages);	
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
	//	}
		
	
	
		return "pdf save to db";
		}



	@Override
	public List<AocpvImages> getByApplicationNoAll(long applicationNoInLong) {
   List<AocpvImages> list =imageRepository.findByApplicationNo(applicationNoInLong);
//	if(list.size()==0) {
// 			throw new NoSuchElementException("list is empty");
// 		}
 		return list;
	}



	@Override
	public List<AocpvImages> getByApplicationNo1(long applicationNo) {
				List<AocpvImages> list =imageRepository.fetchByAppl(applicationNo);
				//if(list.isEmpty()) {
				//	throw new NoSuchElementException("list is empty");
				//}
				return list;
	}



	@Override
	public List<AocpvImages> getImageBydocType(long applicationNO) {
		List<AocpvImages> list = imageRepository.getImageBydocType(applicationNO);
		if(list.size()==0) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}



	@Override
	public List<AocpvImages> getImageForComparison(long applicationNoInLong) {
		List<AocpvImages> list =imageRepository.getImageforComparison(applicationNoInLong);
		return list;
	}

@Override
	public List<Image> fetchMemberImage(long applicationNo, String member, String document) {
		List<AocpvImages> list=null;
		if(member.equalsIgnoreCase("Self")) {
			 list =imageRepository.fetchSelfImage(applicationNo);
		}
		else if(member.equalsIgnoreCase("others")) {
			if(document.equalsIgnoreCase("utilityBillPhoto")) {
				list =imageRepository.fetchUtilityBillPhoto(applicationNo);
			}
			else if(document.equalsIgnoreCase("houseImageInside")) {
				list = imageRepository.getByApplicationNoAndDocument(applicationNo,"houseImageInside");
			}
			else if(document.equalsIgnoreCase("houseImageOutside")) {
				list = imageRepository.getByApplicationNoAndDocument(applicationNo,"houseImageOutside");
			}
			else if(document.equalsIgnoreCase("roadsideImage")) {
				list = imageRepository.getByApplicationNoAndDocument(applicationNo,"roadsideImage");
			}
			else if(document.equalsIgnoreCase("buisnessPhoto")) {
				list =imageRepository.fetchBuisnessPhoto(applicationNo);
			}	 
		}
		else {
			list =imageRepository.fetchfatherImage(applicationNo, member);
			
		}
		if(list.size()==0) {
			throw new NoSuchElementException("list is empty");
		}
		List<Image> listOfImages=new ArrayList<>();
		for(AocpvImages aocpvImages:list) {	
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
	 		String member1=aocpvImages.getMember();
	 		byte[] images2 = aocpvImages.getImages();
	 		String encoded = Base64.getEncoder().encodeToString(images2); 
	 		Image images = new Image(documenttype,imageName,type,size,encoded,member1,geolocation);
		 		listOfImages.add(images);	
		}
		return listOfImages;
	}



	@Override
	public List<String> fetchMemberName(long applicationNoInLong) {
		List<String> list=imageRepository.fetchMemberName(applicationNoInLong);
		return list;
	}



	@Override
	public AocpvImages getByApplicationNoAndName(long applicationNoInLong, String string) {
		Optional<AocpvImages> optional = imageRepository.getByApplicationNoAndName(applicationNoInLong, string);
		if(optional.isPresent()){
			return optional.get();
		}
		throw new NoSuchElementException(string+" Not Present");
	}



	@Override
	public List<AocpvImages> fetchImageforOnePager(long applicationNoInLong) {
		List<AocpvImages> list =imageRepository.fetchImageforOnePager(applicationNoInLong);
		return list;
	}



	@Override
	public String fetchBydocumenttype(long applicationNoInLong, String documentType) {
		Optional<AocpvImages> optional=imageRepository.getByApplicationNoAndName(applicationNoInLong, documentType);
		if(optional.isPresent()) {
			return Base64.getEncoder().encodeToString(optional.get().getImages());
		}
		throw new NoSuchElementException(documentType+" Not Present");
	}

	
@Override
	public List<AocpvImages> getByApplicationNoAnddocument(long applicationNoInLong, String string) {
		List<AocpvImages> list = imageRepository.getByApplicationNoAndDocument(applicationNoInLong, string);
		return list;
	}



	@Override
	public List<Image> getsanctionLetterAndagreement(String applicationNo) {
		List<AocpvImages> list=imageRepository.getsanctionLetterAndagreement(applicationNo);
		if(list.size()==0) {
			throw new NoSuchElementException("list is empty no documents found");
		}
		List<Image> listOfImages=new ArrayList<>();
		for(AocpvImages aocpvImages:list) {
			
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
		return listOfImages;
	}

		@Override
	public String fetchMemberImageByApplicationNo(long applicationNoInLong, String member, String documentType) {
		Optional<AocpvImages>optional= imageRepository.getByApplicationNoMember(applicationNoInLong, documentType, member);

		if(optional.isPresent())
		{
			AocpvImages aocpvImages = optional.get();
			if(aocpvImages.getImages()==null)
			{
				return "Please Capture Form60";
			}
			return "Success";
			
		}
		return "Please Select Form60";
				}
		




	@Override
	public String validateUDImage(long applicationNoInLong) {
		List<AocpvImages> list = imageRepository.fetchUDImage(applicationNoInLong);
		if(list.size() == 3) {
			return "Success";
		}
		else {
			Boolean utilityBillPhoto= true;
			Boolean houseImageInside= true;
			Boolean houseImageOutside= true;
			
			for(AocpvImages aocpvImages :list) {
//				if(aocpvImages.getDocumenttype().equals("utilityBillPhoto")) {
//					utilityBillPhoto=false;
//				}else 
				if(aocpvImages.getDocumenttype().equals("houseImageInside")) {
					houseImageInside=false;
				}else if(aocpvImages.getDocumenttype().equals("houseImageOutside")) {
					houseImageOutside=false;
				}
			}
//			if(utilityBillPhoto) {
//				return "Please Capture Utility Bill Photo";
//			}else
				
			
			if(houseImageInside) {
				return "Please Capture House Image Inside";
			}else if (houseImageOutside) {
				return "Please Capture House Image Outside";
			}
			
		}
		return "Success";
	}

     @Override
	public void saveImageWeb(byte[] image, JSONArray document, long applicationNoInLong,String member) {

		String fileNameInRequest = null;
		String Documenttype = null;
		String flowStatus="PDF";
		String content = "";
		JSONObject jsonObject2 = null;
		int id = 1;
		JSONObject jsonObject = null;
		
				
				for(int n=0 ; n<document.length();n++) {
					AocpvImages aocpvImages=new AocpvImages();
					JSONObject object = document.getJSONObject(n);
				if(object.has("utilityBillPhoto")) {
					 jsonObject2 = object.getJSONObject("utilityBillPhoto");
				String	utilityBillPhoto=object.getJSONObject("utilityBillPhoto").toString();
					 jsonObject=new JSONObject(utilityBillPhoto);
					 aocpvImages.setGeoLocation(utilityBillPhoto);
					 flowStatus="UD";
					 Documenttype="utilityBillPhoto";
				}
				else if(object.has("houseImageInside")) {
					 jsonObject2 = object.getJSONObject("houseImageInside");
					 String	houseImageInside=object.getJSONObject("houseImageInside").toString();
					 jsonObject=new JSONObject(houseImageInside);
					 aocpvImages.setGeoLocation(houseImageInside);
					 flowStatus="UD";
					 Documenttype="houseImageInside";
				}
				else if(object.has("houseImageOutside")) {
					 jsonObject2 = object.getJSONObject("houseImageOutside");
					String houseImageOutside=object.getJSONObject("houseImageOutside").toString();
					 jsonObject=new JSONObject(houseImageOutside);
					 aocpvImages.setGeoLocation(houseImageOutside);
					 flowStatus="UD";
					 Documenttype="houseImageOutside";
				}
				else if(object.has("roadsideImage")) {
					 jsonObject2 = object.getJSONObject("roadsideImage");
					String houseImageOutside=object.getJSONObject("roadsideImage").toString();
					 jsonObject=new JSONObject(houseImageOutside);
					 aocpvImages.setGeoLocation(houseImageOutside);
					 flowStatus="UD";
					 Documenttype="roadsideImage";
				}
				else if(object.has("kycPhoto")) {
					 jsonObject2 = object.getJSONObject("kycPhoto");
					 String	kycPhoto=object.getJSONObject("kycPhoto").toString();
					 jsonObject=new JSONObject(kycPhoto);
					 aocpvImages.setGeoLocation(kycPhoto);
					 flowStatus="UD";
					 Documenttype="kycPhoto";
				}
				else if(object.has("customerPhoto")) {
					 jsonObject2 = object.getJSONObject("customerPhoto");
					 String customerPhoto=object.getJSONObject("customerPhoto").toString();
					 jsonObject=new JSONObject(customerPhoto);
					 aocpvImages.setGeoLocation(customerPhoto);
					 flowStatus="PD";
					 Documenttype="customerPhoto";
				}
				else if(object.has("AO_Selfie")) {
					 jsonObject2 = object.getJSONObject("AO_Selfie");
					 String AO_Selfie=object.getJSONObject("AO_Selfie").toString();
					 jsonObject=new JSONObject(AO_Selfie);
					 aocpvImages.setGeoLocation(AO_Selfie);
					 flowStatus="UD";
					 Documenttype="AO_Selfie";
				}
				else if(object.has("RO_Selfie")) {
					 jsonObject2 = object.getJSONObject("RO_Selfie");
					 String RO_Selfie=object.getJSONObject("RO_Selfie").toString();
					 jsonObject=new JSONObject(RO_Selfie);
					 aocpvImages.setGeoLocation(RO_Selfie);
					 flowStatus="UD";
					 Documenttype="RO_Selfie";
				}
				else if(object.has("Loan_Agreement_Verify")) {
					 jsonObject2 = object.getJSONObject("Loan_Agreement_Verify");
					 String Loan_Agreement_Verify=object.getJSONObject("Loan_Agreement_Verify").toString();
					 jsonObject=new JSONObject(Loan_Agreement_Verify);
					 aocpvImages.setGeoLocation(Loan_Agreement_Verify);
					 flowStatus="UD";
					 Documenttype="Loan_Agreement_Verify";
				}
				else if(object.has("Sanction_letter_verify")) {
					 jsonObject2 = object.getJSONObject("Sanction_letter_verify");
					 String Sanction_letter_verify=object.getJSONObject("Sanction_letter_verify").toString();
					 jsonObject=new JSONObject(Sanction_letter_verify);
					 aocpvImages.setGeoLocation(Sanction_letter_verify);
					 flowStatus="UD";
					 Documenttype="Sanction_letter_verify";
				}
				else if(object.has("panCardPhoto")) {
					 jsonObject2 = object.getJSONObject("panCardPhoto");
					 String panCardPhoto=object.getJSONObject("panCardPhoto").toString();
					 jsonObject=new JSONObject(panCardPhoto);
					 aocpvImages.setGeoLocation(panCardPhoto);
					 flowStatus="ID";
					 Documenttype="panCardPhoto";
				}
				else if(object.has("aadharFront")) {
					 jsonObject2 = object.getJSONObject("aadharFront");
					 String aadharFront=object.getJSONObject("aadharFront").toString();
					 jsonObject=new JSONObject(aadharFront);
					 flowStatus="ID";
					 aocpvImages.setGeoLocation(aadharFront);
					 Documenttype="aadharFront";
				}
				else if(object.has("aadharBack")) {
					 jsonObject2 = object.getJSONObject("aadharBack");
					 String aadharBack=object.getJSONObject("aadharBack").toString();
					 jsonObject=new JSONObject(aadharBack);
					 aocpvImages.setGeoLocation(aadharBack);
					 flowStatus="ID";
					 Documenttype="aadharBack";
				}
				else if(object.has("buisnessPhoto")) {
					 jsonObject2 = object.getJSONObject("buisnessPhoto");
					 String	buisnessPhoto=object.getJSONObject("buisnessPhoto").toString();
					 jsonObject=new JSONObject(buisnessPhoto);
					 aocpvImages.setGeoLocation(buisnessPhoto);
					 flowStatus="UD";
					 Documenttype="buisnessPhoto";
				}
				else if(object.has("form60Photo")) {
					 jsonObject2 = object.getJSONObject("form60Photo");
					 String	buisnessPhoto=object.getJSONObject("form60Photo").toString();
					 jsonObject=new JSONObject(buisnessPhoto);
					 aocpvImages.setGeoLocation(buisnessPhoto);
					 flowStatus="ID";
					 Documenttype="form60Photo";
				}
				else if(object.has("voterIdCardPhoto")) {
					 jsonObject2 = object.getJSONObject("voterIdCardPhoto");
					 String	buisnessPhoto=object.getJSONObject("voterIdCardPhoto").toString();
					 jsonObject=new JSONObject(buisnessPhoto);
					 aocpvImages.setGeoLocation(buisnessPhoto);
					 flowStatus="ID";
					 Documenttype="voterIdCardPhoto";
				}
				else if(object.has("signaturePhoto")) {
					 jsonObject2 = object.getJSONObject("signaturePhoto");
					 String	buisnessPhoto=object.getJSONObject("signaturePhoto").toString();
					 jsonObject=new JSONObject(buisnessPhoto);
					 aocpvImages.setGeoLocation(buisnessPhoto);
					 flowStatus="ID";
					 Documenttype="signaturePhoto";
				}
				else if(object.has("voterIdPhoto")) {
					 jsonObject2 = object.getJSONObject("voterIdPhoto");
					 String	buisnessPhoto=object.getJSONObject("voterIdPhoto").toString();
					 jsonObject=new JSONObject(buisnessPhoto);
					 aocpvImages.setGeoLocation(buisnessPhoto);
					 flowStatus="ID";
					 Documenttype="voterIdPhoto";
				}
				String lat = jsonObject2.getString("Lat");
				String plong = jsonObject2.getString("Long");
				if (lat.isEmpty() || plong.isEmpty()) {
					throw new NoSuchElementException("enter valid lat long in " + Documenttype);
				}

				fileNameInRequest = jsonObject.getString("image");
				int length = image.length;
	
					try {
						aocpvImages.setApplicationNo(applicationNoInLong);
						aocpvImages.setFlowStatus(flowStatus);
						aocpvImages.setImageName(fileNameInRequest);
						aocpvImages.setType(content);
						aocpvImages.setImages(image);
						aocpvImages.setDocumenttype(Documenttype);
						aocpvImages.setSize(length);
						aocpvImages.setMember(member);
						Optional<Integer> optional1 = imageRepository.fetchLastId();
						if (optional1.isPresent()) {
							id = optional1.get();
							id++;
							aocpvImages.setId(id);
						}
						int versioncode = 1;
						Optional<AocpvImages> optional=null;
						if(flowStatus.equalsIgnoreCase("ID")) {
							optional= imageRepository.getByApplicationNoMember(applicationNoInLong, Documenttype, member);
						}
						else {
							 optional = imageRepository.getByApplicationNoAndName(applicationNoInLong, Documenttype);	
						}
						
						if (optional.isPresent()) {
							AocpvImages aocpvImage = optional.get();
							aocpvImages.setId(aocpvImage.getId());
							versioncode = aocpvImage.getVersioncode();
						//	versioncode++;
						}
						aocpvImages.setVersioncode(versioncode);
						if(aocpvImages.getGeoLocation() != null) {
							imageRepository.save(aocpvImages);	
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

		
	
				}
		
		}



	@Override
	public List<AocpvImages> getByApplicationId(long applicationNO) {
		List<AocpvImages> list=imageRepository.getByApplicationId(applicationNO);
		return list;
	}




}
