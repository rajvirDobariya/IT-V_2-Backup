package com.suryoday.connector.service;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

public interface ConnectorImageService {

	String saveAllImages(MultipartFile[] files, Long appln, JSONArray document, String imageFlow);

}
