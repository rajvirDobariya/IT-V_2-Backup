package com.suryoday.customerOnboard.serviceImpl;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.customerOnboard.entity.CustomerOnboardDetails;
import com.suryoday.customerOnboard.entity.CustomerOnboardImage;
import com.suryoday.customerOnboard.exceptionHandler.ResourceNotFoundException;
import com.suryoday.customerOnboard.repository.CustomerOnboardImageRepository;
import com.suryoday.customerOnboard.service.CustomerOnboardDetailsService;
import com.suryoday.customerOnboard.service.CustomerOnboardImageService;

@Service
public class CustomerOnboardImageServiceImpl implements CustomerOnboardImageService {

	@Autowired
	CustomerOnboardImageRepository imageRepository;

	@Autowired
	CustomerOnboardDetailsService onboardDetailsService;

	@Override
	public void saveAadharPhoto(Map<String, String> files, String applicationNo, JSONArray document) {

		CustomerOnboardDetails customeDetails = onboardDetailsService.getByAapplicationNo(applicationNo);

		List<CustomerOnboardImage> images = customeDetails.getImages();

		String fileName = "";
		String fileNameInRequest = null;
		String Documenttype = null;
		String content = "";
		String file = "";
		JSONObject jsonObject2 = null;
		int id = 1;
		JSONObject jsonObject = null;
		if (document.isEmpty()) {

		} else {
			for (int n = 0; n < document.length(); n++) {
				CustomerOnboardImage customerOnboardImage = new CustomerOnboardImage();
				JSONObject object = document.getJSONObject(n);
				if (object.has("ekyc_photo")) {
					jsonObject2 = object.getJSONObject("ekyc_photo");
					String ekyc_photo = object.getJSONObject("ekyc_photo").toString();
					jsonObject = new JSONObject(ekyc_photo);
					fileName = "photo.jpg";
					content = "jpg/*";
					file = files.get("ekycPhoto");
					customerOnboardImage.setGeoLocation(ekyc_photo);
//					customerOnboardImage.setMember(member);
					Documenttype = "ekyc_photo";
				} else if (object.has("ekyc_aadhar")) {
					jsonObject2 = object.getJSONObject("ekyc_aadhar");
					String ekyc_aadhar = object.getJSONObject("ekyc_aadhar").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
					fileName = "photo.pdf";
					file = files.get("ekycAadhar");
					content = "pdf/*";
					customerOnboardImage.setGeoLocation(ekyc_aadhar);
//					customerOnboardImage.setMember("SELF_COMPULSORY");
					Documenttype = "ekyc_aadhar";
				}

				fileNameInRequest = jsonObject.getString("image");
				byte[] bytes = Base64.getDecoder().decode(file);
				int length = bytes.length;
				if (fileName.equals(fileNameInRequest)) {
					try {
//						customerOnboardImage.setApplicationNo(applicationNo);
						customerOnboardImage.setFlowStatus("DU");
						customerOnboardImage.setImageName(fileName);
						customerOnboardImage.setType(content);
						customerOnboardImage.setImages(bytes);
						customerOnboardImage.setDocumenttype(Documenttype);
						customerOnboardImage.setSize(length);
						images.add(customerOnboardImage);
//						Optional<Integer> optional1 = imageRepository.fetchLastId();
//						if (optional1.isPresent()) {
//							id = optional1.get();
//							id++;
//							TWImages.setId(id);
//						}
//						Optional<TwowheelerImage> optional = imageRepository.getByApplicationNoAnddocumentType(applicationNo, Documenttype);
//						if (optional.isPresent()) {
//							TwowheelerImage aocpvImage = optional.get();
//							TWImages.setId(aocpvImage.getId());
//						
//						}

//						if(TWImages.getGeoLocation() != null) {
//							imageRepository.save(TWImages);	
//						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
		customeDetails.setImages(images);
		onboardDetailsService.save(customeDetails);
	}

	public void saveImage(MultipartFile[] files, String applicationNo, JSONArray document) {

		CustomerOnboardDetails customeDetails = onboardDetailsService.getByAapplicationNo(applicationNo);

		List<CustomerOnboardImage> images = customeDetails.getImages();

		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest = null;
			String Documenttype = null;
			CustomerOnboardImage customerOnboardImage = new CustomerOnboardImage();

			if (!document.isEmpty()) {
				for (int n = 0; n < document.length(); n++) {
					JSONObject object = document.getJSONObject(n);
					fileNameInRequest = object.getString("image");
					if (fileName.equals(fileNameInRequest)) {
						customerOnboardImage.setGeoLocation(object.toString());
						Documenttype = object.getString("documentName");
						break;
					}
				}
				if (fileName.equals(fileNameInRequest)) {
					try {

						CustomerOnboardImage goNoGoImages = images.stream()
								.filter(e -> e.getDocumenttype().equals(customerOnboardImage.getDocumenttype()))
								.findAny().orElseGet(() -> new CustomerOnboardImage());
						goNoGoImages.setImageName(fileName);
						goNoGoImages.setType(file.getContentType());
						goNoGoImages.setImages(file.getBytes());
						goNoGoImages.setDocumenttype(Documenttype);
						goNoGoImages.setSize(file.getSize());
						goNoGoImages.setDocumenttype(Documenttype);

						images.add(goNoGoImages);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					throw new ResourceNotFoundException("No Image Found");
				}

			}
		}

		customeDetails.setImages(images);
		onboardDetailsService.save(customeDetails);
	}
}
