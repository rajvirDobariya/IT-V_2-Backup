package com.suryoday.familyMember.serviceImpl;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.familyMember.pojo.FamilyMemberImages;
import com.suryoday.familyMember.repository.MemberImageRepository;
import com.suryoday.familyMember.service.MemberImageService;

@Service
public class MemberImageServiceImpl implements MemberImageService {
	Logger logger = LoggerFactory.getLogger(MemberImageServiceImpl.class);

	@Autowired
	MemberImageRepository imageRepository;

	@Override
	public void saveImage(MultipartFile[] files, String customerId, JSONArray document, String member) {
		String Documenttype = null;
		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			String panCardPhoto = null;
			String aadharFront = null;
			String aadharBack = null;
			String customerPhoto = null;
			int id = 1;
			String fileNameInRequest = null;
			JSONObject jsonObject = null;
			JSONObject jsonObject2 = null;

			if (document.isEmpty()) {

			} else {
				for (int n = 0; n < document.length(); n++) {
					FamilyMemberImages familyMemberImages = new FamilyMemberImages();
					JSONObject object = document.getJSONObject(n);

					if (object.has("panCardPhoto")) {
						jsonObject2 = object.getJSONObject("panCardPhoto");
						panCardPhoto = object.getJSONObject("panCardPhoto").toString();
						jsonObject = new JSONObject(panCardPhoto);
						familyMemberImages.setGeoLocation(panCardPhoto);
						Documenttype = "panCardPhoto";
					} else if (object.has("aadharFront")) {
						jsonObject2 = object.getJSONObject("aadharFront");
						aadharFront = object.getJSONObject("aadharFront").toString();
						jsonObject = new JSONObject(aadharFront);
						familyMemberImages.setGeoLocation(aadharFront);
						Documenttype = "aadharFront";
					} else if (object.has("aadharBack")) {
						jsonObject2 = object.getJSONObject("aadharBack");
						aadharBack = object.getJSONObject("aadharBack").toString();
						jsonObject = new JSONObject(aadharBack);
						familyMemberImages.setGeoLocation(aadharBack);
						Documenttype = "aadharBack";
					} else if (object.has("customerPhoto")) {
						jsonObject2 = object.getJSONObject("customerPhoto");
						customerPhoto = object.getJSONObject("customerPhoto").toString();
						jsonObject = new JSONObject(customerPhoto);
						familyMemberImages.setGeoLocation(customerPhoto);
						Documenttype = "customerPhoto";
					}
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
					if (lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
						throw new NoSuchElementException("enter valid lat long in " + Documenttype);
					}
					try {
						fileNameInRequest = jsonObject.getString("image");
						logger.debug("fileNameInRequest start" + fileNameInRequest);
						if (fileName.equals(fileNameInRequest)) {
							familyMemberImages.setCustomerId(customerId);
							familyMemberImages.setImageName(fileName);
							familyMemberImages.setType(file.getContentType());
							familyMemberImages.setImages(file.getBytes());
							familyMemberImages.setDocumenttype(Documenttype);
							familyMemberImages.setSize(file.getSize());
							familyMemberImages.setMember(member);

							Optional<Integer> optional1 = imageRepository.fetchLastId();
							if (optional1.isPresent()) {
								id = optional1.get();
								id++;
							}
							familyMemberImages.setId(id);

							Optional<FamilyMemberImages> optional = imageRepository.getByApplicationNoMember(customerId,
									Documenttype, member);
							if (optional.isPresent()) {
								FamilyMemberImages image = optional.get();
								familyMemberImages.setId(image.getId());
							}

							if (familyMemberImages.getGeoLocation() != null) {
								imageRepository.save(familyMemberImages);
							}

							logger.debug("Id photo save");

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

}
