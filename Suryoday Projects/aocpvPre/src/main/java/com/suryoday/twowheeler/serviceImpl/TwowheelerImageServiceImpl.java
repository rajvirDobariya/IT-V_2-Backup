package com.suryoday.twowheeler.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.twowheeler.pojo.TwowheelerImage;
import com.suryoday.twowheeler.repository.TwowheelerImageRepository;
import com.suryoday.twowheeler.service.TwowheelerImageService;

@Service
public class TwowheelerImageServiceImpl implements TwowheelerImageService{

	@Autowired
	TwowheelerImageRepository imageRepository;
	
	@Override
	public void savePhoto(MultipartFile[] files, String applicationNo, JSONArray document, String flowStatus, String member) {
		
		for(MultipartFile file : files ) {
			
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest=null;
			String Documenttype=null;
			int id=1;
			JSONObject jsonObject=null;
			if(!document.isEmpty()) {
				
				for(int n=0 ; n<document.length();n++) {
					TwowheelerImage twowheelerImage=new TwowheelerImage();
					JSONObject object = document.getJSONObject(n);
//					if(object.has("ekyc_photo")) {
//						 jsonObject = object.getJSONObject("ekyc_photo");
//					String ekyc_photo=object.getJSONObject("ekyc_photo").toString();
//						 jsonObject=new JSONObject(ekyc_photo);
//					twowheelerImage.setGeoLocation(ekyc_photo);
//						 Documenttype="ekyc_photo";
//					}
//					else if(object.has("ekyc_aadhar")) {
//						jsonObject = object.getJSONObject("ekyc_aadhar");
//						String ekyc_aadhar=object.getJSONObject("ekyc_aadhar").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
//						twowheelerImage.setGeoLocation(ekyc_aadhar);
//						 Documenttype="ekyc_aadhar";
//					}
					 if(object.has("Aadhar")) {
						jsonObject = object.getJSONObject("Aadhar");
						String Aadhar=object.getJSONObject("Aadhar").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(Aadhar);
						 Documenttype="Aadhar";
					}
					else if(object.has("Pan")) {
						jsonObject = object.getJSONObject("Pan");
						String Pan=object.getJSONObject("Pan").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(Pan);
						 Documenttype="Pan";
					}
					else if(object.has("Voter_ID")) {
						jsonObject = object.getJSONObject("Voter_ID");
						String Voter_ID=object.getJSONObject("Voter_ID").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(Voter_ID);
						 Documenttype="Voter_ID";
					}
					else if(object.has("DeliveryOrder")) {
						jsonObject = object.getJSONObject("DeliveryOrder");
						String DeliveryOrder=object.getJSONObject("DeliveryOrder").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(DeliveryOrder);
						 Documenttype="DeliveryOrder";
					}
					else if(object.has("AssetInsurance")) {
						jsonObject = object.getJSONObject("AssetInsurance");
						String AssetInsurance=object.getJSONObject("AssetInsurance").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(AssetInsurance);
						 Documenttype="AssetInsurance";
					}
					else if(object.has("CreditLifeInsurance")) {
						jsonObject = object.getJSONObject("CreditLifeInsurance");
						String CreditLifeInsurance=object.getJSONObject("CreditLifeInsurance").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(CreditLifeInsurance);
						 Documenttype="CreditLifeInsurance";
					}
					else if(object.has("Invoice")) {
						jsonObject = object.getJSONObject("Invoice");
						String Invoice=object.getJSONObject("Invoice").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(Invoice);
						 Documenttype="Invoice";
					}
					else if(object.has("RtoKit")) {
						jsonObject = object.getJSONObject("RtoKit");
						String RtoKit=object.getJSONObject("RtoKit").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(RtoKit);
						 Documenttype="RtoKit";
					}
					else if(object.has("MarginMoneyPaySlip")) {
						jsonObject = object.getJSONObject("MarginMoneyPaySlip");
						String MarginMoneyPaySlip=object.getJSONObject("MarginMoneyPaySlip").toString();
//						 jsonObject=new JSONObject(ekyc_aadhar);
						twowheelerImage.setGeoLocation(MarginMoneyPaySlip);
						 Documenttype="MarginMoneyPaySlip";
					}
					else if(object.has("customerPhoto")) {
						jsonObject = object.getJSONObject("customerPhoto");
						String customerPhoto=object.getJSONObject("customerPhoto").toString();
//						 jsonObject=new JSONObject(customerPhoto);
						 twowheelerImage.setGeoLocation(customerPhoto);
						 Documenttype="customerPhoto";
					}else {
						 throw new NoSuchElementException("enter Proper Documenttype");	
					}
					
//						String lat = jsonObject.getString("Lat");
//						String plong = jsonObject.getString("Long");
//						if(lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
//							 throw new NoSuchElementException("enter valid lat long in "+Documenttype);	
//					}
					
					 fileNameInRequest = jsonObject.getString("image");
						
			 			if(fileName.equals(fileNameInRequest)) {
					try {
						twowheelerImage.setApplicationNo(applicationNo);
						twowheelerImage.setFlowStatus(flowStatus);
						twowheelerImage.setImageName(fileName);
						twowheelerImage.setType(file.getContentType());	
						twowheelerImage.setImages(file.getBytes());
						twowheelerImage.setDocumenttype(Documenttype);
						twowheelerImage.setSize(file.getSize());
						twowheelerImage.setMember(member);
					Optional<Integer>optional1=imageRepository.fetchLastId();
					if(optional1.isPresent()) {
						 id = optional1.get();
						id++;
						twowheelerImage.setId(id);
					}
					int versioncode=1;
//			Optional<TwowheelerImage> optional=imageRepository.getByApplicationNoAndDocumentType(applicationNo, Documenttype);
//					if(optional.isPresent()) {
//						TwowheelerImage aocpvImage = optional.get();
//						twowheelerImage.setId(aocpvImage.getId());
	//					versioncode = aocpvImage.getVersioncode();
						
				//		versioncode++;	
//					}
//					twowheelerImage.setVersioncode(versioncode);
					if(twowheelerImage.getGeoLocation() != null) {
						imageRepository.save(twowheelerImage);	
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
	public List<TwowheelerImage> getByApplicationNoAndDocument(String applicationNo, String documentType) {
		List<TwowheelerImage> list =imageRepository.getByApplicationNoAndDocumentType(applicationNo,documentType);
		return list;
	}

	@Override
	public List<Image> getByAppNoAndMember(String applicationNo, String member, String documentType) {
		List<TwowheelerImage> list=null;
		if(member.equals("LOAN_DOCUMENTS")) {
			 list=imageRepository.getByAppNoAndMemberWeb(applicationNo);
		 }
		else if(member.equals("SELF_DOCUMENTS")){
			list=imageRepository.getSelfdocumentWeb(applicationNo);
		}
		else if(documentType.equals("")) {
			 list=imageRepository.getByAppNoAndMember(applicationNo,member);
		}
		else {
			list=imageRepository.getByAppNoAndMemberAndDocument(applicationNo,documentType);
		}
		if (list.size() == 0) {
			throw new NoSuchElementException("No Image Found");
		}
		List<Image> listOfImages = new ArrayList<>();
		for (TwowheelerImage aocpvImages : list) {

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
			String member1 = aocpvImages.getMember();
			byte[] images2 = aocpvImages.getImages();
			String encoded = Base64.getEncoder().encodeToString(images2);

			Image images = new Image(documenttype, imageName, type, size, encoded, member1, geolocation);

			listOfImages.add(images);

		}
		return listOfImages;
	}

	@Override
	public void saveAadharPhoto(Map<String, String> files, String applicationNo, JSONArray document, String member) {
		
			String fileName = "";
			String fileNameInRequest = null;
			String Documenttype = null;
			String content = "";
			String file="";
			JSONObject jsonObject2 = null;
			int id = 1;
			JSONObject jsonObject = null;
			if (document.isEmpty()) {

			} else {
				for (int n = 0; n < document.length(); n++) {
					TwowheelerImage TWImages = new TwowheelerImage();
					JSONObject object = document.getJSONObject(n);
					if (object.has("ekyc_photo")) {
						jsonObject2 = object.getJSONObject("ekyc_photo");
						String ekyc_photo = object.getJSONObject("ekyc_photo").toString();
						jsonObject = new JSONObject(ekyc_photo);
						fileName = "photo.jpg";
						content = "jpg/*";
						file=files.get("ekycPhoto");
						TWImages.setGeoLocation(ekyc_photo);
						TWImages.setMember(member);
						Documenttype = "ekyc_photo";
					} else if (object.has("ekyc_aadhar")) {
						jsonObject2 = object.getJSONObject("ekyc_aadhar");
					String ekyc_aadhar = object.getJSONObject("ekyc_aadhar").toString();
						jsonObject = new JSONObject(ekyc_aadhar);
						fileName = "photo.pdf";
						file=files.get("ekycAadhar");
						content = "pdf/*";
						TWImages.setGeoLocation(ekyc_aadhar);
						TWImages.setMember("SELF_COMPULSORY");
						Documenttype = "ekyc_aadhar";
					}

					fileNameInRequest = jsonObject.getString("image");
					  byte[] bytes = Base64.getDecoder().decode(file);
					int length = bytes.length;
					if (fileName.equals(fileNameInRequest)) {
						try {
							TWImages.setApplicationNo(applicationNo);
							TWImages.setFlowStatus("DU");
							TWImages.setImageName(fileName);
							TWImages.setType(content);
							TWImages.setImages(bytes);
							TWImages.setDocumenttype(Documenttype);
							TWImages.setSize(length);
						
							Optional<Integer> optional1 = imageRepository.fetchLastId();
							if (optional1.isPresent()) {
								id = optional1.get();
								id++;
								TWImages.setId(id);
							}
							Optional<TwowheelerImage> optional = imageRepository.getByApplicationNoAnddocumentType(applicationNo, Documenttype);
							if (optional.isPresent()) {
								TwowheelerImage aocpvImage = optional.get();
								TWImages.setId(aocpvImage.getId());
							
							}
						
							if(TWImages.getGeoLocation() != null) {
								imageRepository.save(TWImages);	
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}

		

	}

	@Override
	public void savepdf(byte[] image, JSONObject object, String applicationNo) {
		JSONObject jsonObject = null;
		String flowStatus="PDF";
		String content = "";
		String Documenttype = null;
		int id = 1;
		
		TwowheelerImage TWImages = new TwowheelerImage();
		if (object.has("sanctionLetter")) {
			jsonObject = object.getJSONObject("sanctionLetter");
		String 	sanctionLetter = object.getJSONObject("sanctionLetter").toString();
			content = "pdf/*";
			TWImages.setGeoLocation(sanctionLetter);
			Documenttype = "sanctionLetterPdf";
		}
		else if(object.has("loanApplicationForm")) {
			jsonObject = object.getJSONObject("loanApplicationForm");
			String loanApplicationForm = object.getJSONObject("loanApplicationForm").toString();
			content = "pdf/*";
			TWImages.setGeoLocation(loanApplicationForm);
			Documenttype = "loanApplicationForm";
		}
		else if(object.has("loanAgreementPdf")) {
			jsonObject = object.getJSONObject("loanAgreementPdf");
			String loanApplicationForm = object.getJSONObject("loanAgreementPdf").toString();
			content = "pdf/*";
			TWImages.setGeoLocation(loanApplicationForm);
			Documenttype = "loanAgreementPdf";
		}
		String fileNameInRequest = jsonObject.getString("image");
		int length = image.length;
		
		try {
			TWImages.setApplicationNo(applicationNo);
			TWImages.setFlowStatus(flowStatus);
			TWImages.setImageName(fileNameInRequest);
			TWImages.setType(content);
			TWImages.setImages(image);
			TWImages.setDocumenttype(Documenttype);
			TWImages.setSize(length);
			TWImages.setMember("SELF");
			Optional<Integer> optional1 = imageRepository.fetchLastId();
			if (optional1.isPresent()) {
				id = optional1.get();
				id++;
				TWImages.setId(id);
			}
			int versioncode = 1;
			Optional<TwowheelerImage> optional = imageRepository.getByApplicationNoAnddocumentType(applicationNo, Documenttype);
			if (optional.isPresent()) {
				TwowheelerImage aocpvImage = optional.get();
				TWImages.setId(aocpvImage.getId());
				
			}
		
			if(TWImages.getGeoLocation() != null) {
				imageRepository.save(TWImages);	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String flowStatus,
			String member) {
		for(MultipartFile file : files ) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest=null;
			String Documenttype=null;
			int id=1;
			TwowheelerImage twowheelerImage=new TwowheelerImage();
			if(!document.isEmpty()) {
				for(int n=0 ; n<document.length();n++) {
					JSONObject object = document.getJSONObject(n);
					 fileNameInRequest = object.getString("image");
					 if(fileName.equals(fileNameInRequest)) {
						 twowheelerImage.setGeoLocation(object.toString());
						 Documenttype=object.getString("documentName");
						 break;
					 }
				}
				if(fileName.equals(fileNameInRequest)) {
				try {
					twowheelerImage.setApplicationNo(applicationNo);
					twowheelerImage.setFlowStatus(flowStatus);
					twowheelerImage.setImageName(fileName);
					twowheelerImage.setType(file.getContentType());	
					twowheelerImage.setImages(file.getBytes());
					twowheelerImage.setDocumenttype(Documenttype);
					twowheelerImage.setSize(file.getSize());
					twowheelerImage.setMember(member);
					Optional<Integer>optional1=imageRepository.fetchLastId();
					
					if(optional1.isPresent()) {
						 id = optional1.get();
						id++;
						twowheelerImage.setId(id);
					}
//			Optional<TwowheelerImage> optional=imageRepository.getByApplicationNoAndDocumentType(applicationNo, Documenttype);
//					if(optional.isPresent()) {
//						TwowheelerImage aocpvImage = optional.get();
//						twowheelerImage.setId(aocpvImage.getId());
//	
//					}
					if(twowheelerImage.getGeoLocation() != null) {
						imageRepository.save(twowheelerImage);	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
				else {
					throw new NoSuchElementException("No Image Found");
				}
				
			}
		}
		
	}

	@Override
	public void saveImageWeb(byte[] image, JSONArray document, String applicationNo, String member) {
		
		String Documenttype = null;
		String flowStatus="DocumentUpload";
		String content = "";
		String fileNameInRequest = null;
		int id = 1;
		TwowheelerImage twowheelerImage=new TwowheelerImage();
		for(int n=0 ; n<document.length();n++) {
			JSONObject object = document.getJSONObject(n);
			
				 twowheelerImage.setGeoLocation(object.toString());
				 Documenttype=object.getString("documentName");
				 fileNameInRequest = object.getString("image");
				 break;
		}
		
		int length = image.length;

			try {
				twowheelerImage.setApplicationNo(applicationNo);
				twowheelerImage.setFlowStatus(flowStatus);
				twowheelerImage.setImageName(fileNameInRequest);
				twowheelerImage.setType(content);
				twowheelerImage.setImages(image);
				twowheelerImage.setDocumenttype(Documenttype);
				twowheelerImage.setSize(length);
				twowheelerImage.setMember(member);
				Optional<Integer> optional1 = imageRepository.fetchLastId();
				if (optional1.isPresent()) {
					id = optional1.get();
					id++;
					twowheelerImage.setId(id);
				}
			
//					Optional<TwowheelerImage> optional=imageRepository.getByApplicationNoAndDocumentType(applicationNo, Documenttype);
				
//				if (optional.isPresent()) {
//					TwowheelerImage aocpvImage = optional.get();
//					twowheelerImage.setId(aocpvImage.getId());
//
//				}
			
				if(twowheelerImage.getGeoLocation() != null) {
					imageRepository.save(twowheelerImage);	
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public List<String> getDocumentTypes(String applicationNo) {
		List<String> list=imageRepository.getDocumentTypes(applicationNo);
//		if(list.size()==0) {
//			return new ArrayList();
//		}else {
//			return list;
		return list;
//		}
	}

	@Override
	public Set<String> getDocumentTypesWeb(String applicationNo) {
		Set<String> list=imageRepository.getDocumentTypesWeb(applicationNo);
		if(list.size()==0) {
			throw new NoSuchElementException("No Image Found");
		}else {
			return list;
		}
	}
}
