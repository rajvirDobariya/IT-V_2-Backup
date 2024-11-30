package com.suryoday.mhl.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface MHLImageService {

	String saveImage(MultipartFile files, JSONObject jsonObject);

	String saveMultipleImages(MultipartFile[] files, JSONObject jsonObject);

}
