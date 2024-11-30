package com.suryoday.CustomerIntraction.Service.Impl;

import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Pojo.MeetingImage;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionImageRepository;
import com.suryoday.CustomerIntraction.Service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private CustomerIntractionImageRepository meetingImageRepo;
	

	@Autowired
	private DocumentFile documentFile;
	
	public void saveImageWeb(JSONArray imageArray, String meetingNumber, List<MeetingImage> image) {
		MeetingImage meetingImage = new MeetingImage();

		for (int i = 0; i < imageArray.length(); i++) {
			
			JSONObject imageObject = imageArray.getJSONObject(i);
			JSONObject documentJson = new JSONObject();
			String name = imageObject.optString("documentName");
			String type = imageObject.optString("documentType");
			String base64 = imageObject.optString("base64Image");
			
			documentJson.put("name", name);
			documentJson.put("type", type);
			documentJson.put("base64", base64);
			
			documentFile.validateDocument(documentJson);
			
			if (i < image.size()) {
				meetingImage = image.get(i);
		    } else {
		        // Handle the case where `momSummary` does not have enough elements
		    	meetingImage = new MeetingImage();
		    }

			String base64Image = imageObject.optString("base64Image");
			String documentType = imageObject.optString("documentType");

			// Decode base64Image and set to meetingImage
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);

			// save images in db

			meetingImage.setImage(imageBytes);
			meetingImage.setType(documentType);
			meetingImage.setMeetingNumber(meetingNumber);

			meetingImageRepo.save(meetingImage);
		}

	}

}
