package com.suryoday.connector.serviceImpl;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.pojo.ConnectorImages;
import com.suryoday.connector.repository.ConnectorImageRepository;
import com.suryoday.connector.service.ConnectorImageService;

public class ConnectorImageServiceImpl implements ConnectorImageService {

	Logger logger = LoggerFactory.getLogger(ConnectorImageServiceImpl.class);

	@Autowired
	ConnectorImageRepository connectorImageRepository;

	@Override
	public String saveAllImages(MultipartFile[] files, Long appln, JSONArray document, String imageFlow) {

		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String utilityBillPhoto = null;
			String houseImageInside = null;
			String houseImageOutside = null;
			String fileNameInRequest = null;
			String customerPhoto = null;
			String Documenttype = null;
			String buisnessPhoto = null;
			String flowStatus = null;
			String panCardPhoto = null;
			String aadharFront = null;
			String aadharBack = null;
			JSONObject jsonObject2 = null;
			int id = 1;
			JSONObject jsonObject = null;
			if (document.isEmpty()) {

			} else {
				for (int n = 0; n < document.length(); n++) {
					ConnectorImages connectorImages = new ConnectorImages();
					JSONObject object = document.getJSONObject(n);
					if (object.has("utilityBillPhoto")) {
						jsonObject2 = object.getJSONObject("utilityBillPhoto");
						utilityBillPhoto = object.getJSONObject("utilityBillPhoto").toString();
						jsonObject = new JSONObject(utilityBillPhoto);
						connectorImages.setGeoLocation(utilityBillPhoto);
						flowStatus = "UDD";
						Documenttype = "utilityBillPhoto";
					} else if (object.has("houseImageInside")) {
						jsonObject2 = object.getJSONObject("houseImageInside");
						houseImageInside = object.getJSONObject("houseImageInside").toString();
						jsonObject = new JSONObject(houseImageInside);
						connectorImages.setGeoLocation(houseImageInside);
						flowStatus = "UDD";
						Documenttype = "houseImageInside";
					} else if (object.has("customerPhoto")) {
						jsonObject2 = object.getJSONObject("customerPhoto");
						customerPhoto = object.getJSONObject("customerPhoto").toString();
						jsonObject = new JSONObject(customerPhoto);
						connectorImages.setGeoLocation(customerPhoto);
						flowStatus = "UDD";
						Documenttype = "customerPhoto";
					} else if (object.has("panCardPhoto")) {
						jsonObject2 = object.getJSONObject("panCardPhoto");
						panCardPhoto = object.getJSONObject("panCardPhoto").toString();
						jsonObject = new JSONObject(panCardPhoto);
						connectorImages.setGeoLocation(panCardPhoto);
						flowStatus = "ID";
						Documenttype = "panCardPhoto";
					} else if (object.has("aadharFront")) {
						jsonObject2 = object.getJSONObject("aadharFront");
						aadharFront = object.getJSONObject("aadharFront").toString();
						jsonObject = new JSONObject(aadharFront);
						flowStatus = "UDD";
						connectorImages.setGeoLocation(aadharFront);
						Documenttype = "aadharFront";
					} else if (object.has("aadharBack")) {
						jsonObject2 = object.getJSONObject("aadharBack");
						aadharBack = object.getJSONObject("aadharBack").toString();
						jsonObject = new JSONObject(aadharBack);
						connectorImages.setGeoLocation(aadharBack);
						flowStatus = "UDD";
						Documenttype = "aadharBack";
					} else if (object.has("buisnessPhoto")) {
						jsonObject2 = object.getJSONObject("buisnessPhoto");
						buisnessPhoto = object.getJSONObject("buisnessPhoto").toString();
						jsonObject = new JSONObject(buisnessPhoto);
						connectorImages.setGeoLocation(buisnessPhoto);
						flowStatus = "UDD";
						Documenttype = "buisnessPhoto";
					}
					String lat = jsonObject2.getString("Lat");
					String plong = jsonObject2.getString("Long");
					if (lat.isEmpty() || plong.isEmpty() || lat.equals("0.0") || plong.equals("0.0")) {
						throw new NoSuchElementException("enter valid lat long in " + Documenttype);
					}
					fileNameInRequest = jsonObject.getString("image");

					if (fileName.equals(fileNameInRequest)) {
						try {
							connectorImages.setApplicationNo(appln);
							connectorImages.setFlowStatus(flowStatus);
							connectorImages.setImageName(fileName);
							connectorImages.setType(file.getContentType());
							connectorImages.setImages(file.getBytes());
							connectorImages.setDocumenttype(Documenttype);
							connectorImages.setSize(file.getSize());
							Optional<Integer> optional1 = connectorImageRepository.fetchLastId();
							if (optional1.isPresent()) {
								id = optional1.get();
								id++;
								connectorImages.setId(id);
							}
							int versioncode = 1;
							Optional<ConnectorImages> optional = connectorImageRepository
									.getByApplicationNoAndName(appln, Documenttype);
							if (optional.isPresent()) {
								ConnectorImages aocpvImage = optional.get();
								connectorImages.setId(aocpvImage.getId());
								versioncode = aocpvImage.getVersioncode();
								versioncode++;
							}
							connectorImages.setVersioncode(versioncode);
							connectorImageRepository.save(connectorImages);

						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}

		}

		return "Images Saved Sucessfully";
	}

}
