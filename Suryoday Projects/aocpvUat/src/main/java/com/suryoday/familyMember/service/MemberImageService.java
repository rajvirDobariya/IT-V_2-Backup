package com.suryoday.familyMember.service;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

public interface MemberImageService {

	void saveImage(MultipartFile[] files, String customerId, JSONArray document, String member);

}
