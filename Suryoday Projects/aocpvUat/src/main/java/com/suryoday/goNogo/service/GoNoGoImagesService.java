package com.suryoday.goNogo.service;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

public interface GoNoGoImagesService {

	void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String string);

}
