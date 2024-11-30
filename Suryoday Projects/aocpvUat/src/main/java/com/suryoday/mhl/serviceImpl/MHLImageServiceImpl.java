package com.suryoday.mhl.serviceImpl;

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

import com.suryoday.mhl.pojo.MHLImages;
import com.suryoday.mhl.repository.MHLImageRepository;
import com.suryoday.mhl.service.MHLImageService;

@Service
public class MHLImageServiceImpl implements MHLImageService {

	@Autowired
	MHLImageRepository imageRepository;
	Logger logger = LoggerFactory.getLogger(MHLImageServiceImpl.class);

	@Override
	public String saveImage(MultipartFile file, JSONObject jsonObject) {

		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String status = jsonObject.getJSONObject("Data").getString("status");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		String member = jsonObject.getJSONObject("Data").getString("member");
		String geoLocation = jsonObject.getJSONObject("Data").getJSONObject("Photo").toString();

		JSONObject jsonObject1 = new JSONObject(geoLocation);
		String fileNameInRequest = jsonObject1.getString("image");
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		if (fileName.equals(fileNameInRequest)) {
			try {
				MHLImages image = new MHLImages();
				image.setApplicationNo(applicationNo);
				image.setStatus(status);
				image.setGeoLocation(geoLocation);
				image.setImageName(fileName);
				image.setType(file.getContentType());
				image.setSize(file.getSize());
				image.setDocumenttype(documentType);
				image.setImages(file.getBytes());
				image.setMember(member);

				Optional<MHLImages> optional = imageRepository.getByApplicationNo(applicationNo, documentType, member);
				if (optional.isPresent()) {
					MHLImages MHLImage = optional.get();
					image.setId(MHLImage.getId());
				}
				imageRepository.save(image);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "image saved";
	}

	@Override
	public String saveMultipleImages(MultipartFile[] files, JSONObject jsonObject) {
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String status = jsonObject.getJSONObject("Data").getString("status");
		String member = jsonObject.getJSONObject("Data").getString("member");
		JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest = null;
			String Documenttype = null;
			JSONObject jsonObject1 = null;
			String panCardPhoto = null;
			String aadharFront = null;
			String aadharBack = null;
			if (document.isEmpty()) {

			} else {
				for (int n = 0; n < document.length(); n++) {
					MHLImages images = new MHLImages();
					JSONObject object = document.getJSONObject(n);
					logger.debug("object start" + object.toString());
					if (object.has("panCardPhoto")) {
						panCardPhoto = object.getJSONObject("panCardPhoto").toString();
						jsonObject = new JSONObject(panCardPhoto);
						images.setGeoLocation(panCardPhoto);
						Documenttype = "panCardPhoto";
					} else if (object.has("aadharFront")) {
						aadharFront = object.getJSONObject("aadharFront").toString();
						jsonObject = new JSONObject(aadharFront);
						images.setGeoLocation(aadharFront);
						Documenttype = "aadharFront";
					} else if (object.has("aadharBack")) {
						aadharBack = object.getJSONObject("aadharBack").toString();
						jsonObject = new JSONObject(aadharBack);
						images.setGeoLocation(aadharBack);
						Documenttype = "aadharBack";
					}
					fileNameInRequest = jsonObject.getString("image");
					logger.debug("fileNameInRequest start" + fileNameInRequest);
					if (fileName.equals(fileNameInRequest)) {
						try {
							logger.debug("fileNameInRequest start condition" + fileName.equals(fileNameInRequest));
							images.setApplicationNo(applicationNo);
							images.setStatus(status);
							images.setImageName(fileName);
							images.setType(file.getContentType());
							images.setImages(file.getBytes());
							images.setDocumenttype(Documenttype);
							images.setSize(file.getSize());
							images.setMember(member);
							Optional<MHLImages> optional = imageRepository.getByApplicationNo(applicationNo,
									Documenttype, member);
							if (optional.isPresent()) {
								MHLImages MHLImage = optional.get();
								images.setId(MHLImage.getId());
							}
							imageRepository.save(images);
						} catch (IOException e) {
							e.printStackTrace();
							logger.debug("inside exception" + e.getMessage());
						}
					}
				}
			}
		}
		return "image saved";
	}

}
