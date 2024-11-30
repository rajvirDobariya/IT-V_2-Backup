package com.suryoday.travelvisit.utils;

import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.suryoday.travelvisit.exception.TravelVisitException;

public class Image {
	
	public static String convertImageToBase64(MultipartFile image) {
		if (image.isEmpty()) {
			throw new TravelVisitException("Uploaded file is empty.");
		}
		byte[] imageBytes;
		try {
			imageBytes = image.getBytes();
		} catch (IOException e) {
			throw new TravelVisitException("Exception occur in get bytes of image :" + e.getMessage());
		}
		String base64 = Base64.getEncoder().encodeToString(imageBytes);
		return base64;
	}


}
